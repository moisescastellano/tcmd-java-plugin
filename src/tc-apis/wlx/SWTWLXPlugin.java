/**
 * Adapter class for SWT version of WLXPlugin interface.
 */
package plugins.wlx;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.swing.JFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Ken
 * 
 */
public abstract class SWTWLXPlugin extends WLXPluginAdapter {

	/**
	 * Store shell of the parent window handle.
	 */
	private static Hashtable shells = new Hashtable();

	/**
	 * Parameter count of procedure windowProc.
	 */
	protected static final int PROC_PARAM_CNT = 4;

	/**
	 * SWT display.
	 */
	protected Display display;

	/**
	 * Timer value, after the event dispatcher starts.
	 */
	private static final int EVT_TIMER_CONSTANT = 1000;

	/**
	 * {@inheritDoc}
	 */
	public final int listLoad(final int parentWin, final String input,
			final int showFlags) {
		if (display == null) {
			display = Display.getCurrent();
		}
		if (display == null) {
			display = Display.getDefault();
		}
		final Shell shell = createShell();
		display.asyncExec(new Runnable() {

			public void run() {
				final int oldProc = OS.GetWindowLong(parentWin, OS.GWL_WNDPROC);
				Object windowProc = new Object() {

					/**
					 * To prevent an endles event loop.
					 */
					public int windowProc(final int hwnd, final int msg,
							final int wParam, final int lParam) {
						if (msg == OS.WM_CLOSE) {
							OS
									.SetWindowLong(parentWin, OS.GWL_WNDPROC,
											oldProc);
							if (!shell.isDisposed()) {
								shell.dispose();
							}
						}
						return OS.CallWindowProc(oldProc, hwnd, msg, wParam,
								lParam);

					}
				};
				Callback newProc = new Callback(windowProc, "windowProc",
						PROC_PARAM_CNT);
				OS.SetWindowLong(parentWin, OS.GWL_WNDPROC, newProc
						.getAddress());
				shell.setLayout(new FillLayout());
				shell.addListener(SWT.Traverse, new Listener() {
					/**
					 * {@inheritDoc} ESC-key can close the shell
					 */
					public void handleEvent(final Event event) {
						switch (event.detail) {
						case SWT.TRAVERSE_ESCAPE:
							shell.dispose();
							event.detail = SWT.TRAVERSE_NONE;
							event.doit = false;
							break;
						default:
							break;
						}
					}
				});
				synchronized (shells) {
					shells.put(new Integer(shell.handle), shell);
				}
				try {
					listLoad(shell, input, showFlags);
				} catch (final Throwable e) {
					shell.setText(e.getMessage() != null ? e.getMessage()
							: "Throwable");
					Label lbl = new Label(shell, SWT.NONE);
					CharArrayWriter wr = new CharArrayWriter();
					e.printStackTrace(new PrintWriter(wr));
					lbl.setText(wr.toString());
				}
				shell.setLocation(0, 0);
				shell.setVisible(true);
			}
		});
		display.timerExec(EVT_TIMER_CONSTANT, new Runnable() {

			public void run() {
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
				OS.SetFocus(parentWin);
			}

		});
		return shell.handle;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void listCloseWindow(final int listWin) {
		listCloseWindow();
		synchronized (shells) {
			Shell shell = (Shell) shells.get(listWin);
			shells.remove(shell);
			shell.dispose();
		}
	}

	/**
	 * Create a new Shell. This method is called first. Do not create
	 * contents here, listLoad is the right place to create contents.<BR>
	 * <BR>
	 * <CODE>e.g. return new Shell(display, SWT.SHELL_TRIM | SWT.RESIZE);</CODE>
	 * 
	 * @return the Shell instance of the lister window contents.
	 */
	public abstract Shell createShell();

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
	 * @param shell
	 *            SWT shell
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
	 * @see WLXPluginInterface#listLoad(int, String, int)
	 */
	public abstract void listLoad(final Shell shell, final String input,
			final int showFlags);

	/**
	 * ListCloseWindow is called when a user closes lister, or loads a different
	 * file. If ListCloseWindow isn't present, DestroyWindow() is called.<BR>
	 * You can use this function to close open files, free buffers etc.
	 * 
	 * @see WLXPluginInterface#listCloseWindow(int)
	 */
	public abstract void listCloseWindow();
}
