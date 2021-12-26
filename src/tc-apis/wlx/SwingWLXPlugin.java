package plugins.wlx;

import java.awt.Toolkit;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * @author Ken
 * 
 * This class makes it possible to embed Swing javax.swing.JFrame as lister
 * plugin contents.
 */
public abstract class SwingWLXPlugin extends WLXPluginAdapter {

	/**
	 * Default width/height of fixed sized lister windows.
	 */
	private static final int DEFAULT_WIDTH_HEIGHT = 100;

	{
		// initialize support for popup menu (copy/paste)
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(
				new PopupMenuEventQueue());
	}

	/**
	 * Frames of the parent window handle. (key= window handle HWND).
	 */
	private static Hashtable < Integer, JFrame > frames = new Hashtable < Integer, JFrame >();

	/**
	 * Flag to set the lister window size according to getWidth() and
	 * getHeight().
	 */
	private boolean fFixedSize;

	/**
	 * Create a plugin with variable lister window size.
	 */
	public SwingWLXPlugin() {
		this(false);
	}

	/**
	 * Create a plugin with fixed/variable lister window size.
	 * 
	 * @param fixedSize
	 *            true - use a fixed sized lister window according to getWidth()
	 *            and getHeight(), otherwise variable size.
	 */
	public SwingWLXPlugin(final boolean fixedSize) {
		fFixedSize = fixedSize;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int listLoad(final int parentWin, final String input,
			final int showFlags) {
		// create JFrame to embed in the lister window
		final JFrame jframe = createJFrame();
		// show frame, to determine HWND window handle
		jframe.setVisible(true);
		// determine HWND window handle
		int listWin = getHWND2(jframe);
		synchronized (frames) {
			// store JFrame identified by HWND
			frames.put(new Integer(listWin), jframe);
			// use seperate thread to call listLoad (it could last a while)
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					try {
						// create JFrame contents
						listLoad(jframe, input, showFlags);
						// if it is a fixed size lister windows:
						if (fFixedSize) {
							// set parent window size according to getWidth()
							// and getHeight()
							setSize(parentWin, getWidth(jframe),
									getHeight(jframe));
						}
					} catch (Throwable e) {
						// catch exceptions and display it in the JFrame
						CharArrayWriter wr = new CharArrayWriter();
						e.printStackTrace(new PrintWriter(wr));
						jframe.add(new JTextArea(wr.toString()));
					}
				}

			});
		}
		// return with the HWND of the JFrame instance created by createJFrame()
		return listWin;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void listCloseWindow(final int listWin) {
		synchronized (frames) {
			// get JFrame identified by HWND
			final JFrame jframe = (JFrame) frames.get(new Integer(listWin));
			// remove stored JFrame instance, it is no longer used
			frames.remove(jframe);
			// use seperate thread to call listClose (it could last a while)
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					// uninitialize the JFrame lister window contents
					listCloseWindow(jframe);
					// dispose the JFrame (it is no longer used)
					jframe.dispose();
				}

			});
		}
	}

	/**
	 * Create a new JFrame window. This method is called first. Do not create
	 * contents here, listLoad is the right place to create contents.<BR>
	 * <B>Important Note:</B> <BR>
	 * Set a title string for the JFrame, otherwise the frame cannot be embedded
	 * into the lister window. <BR>
	 * <CODE>e.g. return new JFrame("My Demo");</CODE>
	 * 
	 * @return the JFrame instance of the lister window contents.
	 */
	public abstract JFrame createJFrame();

	/**
	 * ListLoad is called when a user opens lister with F3 or the Quick View
	 * Panel with Ctrl+Q, and when the definition string either doesn't exist,
	 * or its evaluation returns true.<BR>
	 * Please note that multiple Lister windows can be open at the same time!
	 * Therefore you cannot save settings in global variables. You can call
	 * RegisterClass with the parameter cbWndExtra to reserve extra space for
	 * your data, which you can then access via GetWindowLong(). Or use an
	 * internal list, and store the list parameter via
	 * SetWindowLong(hwnd,GWL_ID,...). Lister will subclass your window to catch
	 * some hotkeys like 'n' or 'p'.<BR>
	 * When lister is activated, it will set the focus to your window. If your
	 * window contains child windows, then make sure that you set the focus to
	 * the correct child when your main window receives the focus!<BR>
	 * 
	 * If lcp_forceshow is defined, you may try to load the file even if the
	 * plugin wasn't made for it. Example: A plugin with line numbers may only
	 * show the file as such when the user explicitly chooses 'Image/Multimedia'
	 * from the menu.
	 * 
	 * Lister plugins which only create thumbnail images do not need to
	 * implement this function.
	 * 
	 * @param frame
	 *            This is the JFrame instance created by createFrame().
	 * @param input
	 *            The name of the file which has to be loaded.
	 * @param showFlags
	 *            A combination of the following flags:
	 *            <UL>
	 *            <LI>lcp_wraptext Text: Word wrap mode is checked
	 *            <LI>lcp_fittowindow Images: Fit image to window is checked
	 *            <LI>lcp_ansi Ansi charset is checked
	 *            <LI>lcp_ascii Ascii(DOS) charset is checked
	 *            <LI>lcp_variable Variable width charset is checked
	 *            <LI>lcp_forceshow User chose 'Image/Multimedia' from the
	 *            menu. See remarks.
	 *            </UL>
	 *            You may ignore these parameters if they don't apply to your
	 *            document type.
	 */
	public abstract void listLoad(final JFrame frame, final String input,
			final int showFlags);

	/**
	 * ListCloseWindow is called when a user closes lister, or loads a different
	 * file. If ListCloseWindow isn't present, DestroyWindow() is called.<BR>
	 * You can use this function to close open files, free buffers etc.<BR>
	 * <B>Important Note:</B> <BR>
	 * Do not dispose the JFrame instance, it will be disposed elsewhere.
	 * 
	 * @param frame
	 *            This is the JFrame instance created by createFrame().
	 */
	public abstract void listCloseWindow(final JFrame frame);

	/**
	 * If the plugin was created with fixed lister window size. Return the
	 * client area height here.
	 * 
	 * @param frame
	 *            This is the JFrame instance created by createFrame().
	 * @return the client area height
	 */
	protected int getHeight(final JFrame frame) {
		return DEFAULT_WIDTH_HEIGHT;
	}

	/**
	 * If the plugin was created with fixed lister window size. Return the
	 * client area width here.
	 * 
	 * @param frame
	 *            This is the JFrame instance created by createFrame().
	 * @return the client area width
	 */
	protected int getWidth(final JFrame frame) {
		return DEFAULT_WIDTH_HEIGHT;
	}
}
