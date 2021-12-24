/**
 * 
 */
package plugins.wlx;

import java.awt.Rectangle;

import javax.swing.JFrame;

import plugins.DefaultParam;

/**
 * @author Ken
 * 
 */
public abstract class WLXPluginAdapter implements WLXPluginInterface {

	{
		// load library for native functions
		System.loadLibrary("tc.library.name");
	}

	/**
	 * getHWMD is a callback function, to get the window handle of an AWT
	 * component.
	 * 
	 * @param component
	 *            the component to get the HWND for
	 * @return hwnd the HWND window handle of the diplayed component
	 */
	public final int getHWND(final Object component) {
		String jrePath = System.getProperty("sun.boot.library.path");
		String sep = System.getProperty("file.separator");
		return getHWND(component, jrePath + sep + "jawt.dll", jrePath + sep
				+ "awt.dll");
	}

	/**
	 * getHWMD is a callback function, to get the window handle of an AWT
	 * component.
	 * 
	 * @param component
	 *            the component to get the HWND for
	 * @param jawtPath
	 *            the path to AWT.DLL
	 * @param awtPath
	 *            the path to JAWT.DLL
	 * @return hwnd the HWND window handle of the diplayed component
	 */
	private native int getHWND(final Object component, final String jawtPath,
			final String awtPath);

	/**
	 * getHWMD2 is a callback function, to get the window handle of a JFrame.
	 * 
	 * @param frame
	 *            the JFrame to get the HWND window handle for.
	 * @return hwnd the HWND window handle of the JFrame
	 */
	public final native int getHWND2(final JFrame frame);

	/**
	 * Set the size of the lister window client area.
	 * 
	 * @param hwnd
	 *            the window handle of the lister window
	 * @param width
	 *            the width of the client area
	 * @param height
	 *            the height of the client area
	 */
	protected final native void setSize(final int hwnd, final int width,
			final int height);

	/**
	 * {@inheritDoc}
	 */
	public abstract int listLoad(final int parentWin, final String input,
			final int showFlags);

	/**
	 * {@inheritDoc}
	 */
	public void listCloseWindow(final int listWin) {
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract String listGetDetectString(final int maxLen);

	/**
	 * {@inheritDoc}
	 */
	public int listSearchText(final int handle, final String searchString,
			final int searchParameter) {
		return LISTPLUGIN_OK;
	}

	/**
	 * {@inheritDoc}
	 */
	public int listSendCommand(final int listWin, final int command,
			final int parameter) {
		return LISTPLUGIN_OK;
	}

	/**
	 * {@inheritDoc}
	 */
	public int listPrint(final int listWin, final String fileToPrint,
			final String defPrinter, final int printFlags,
			final Rectangle margins) {
		return LISTPLUGIN_OK;
	}

	/**
	 * {@inheritDoc}
	 */
	public int listNotificationReceived(final int listWin, final int message,
			final int wParam, final int lParam) {
		return LISTPLUGIN_OK;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object listGetPreviewBitmap(final String fileToLoad,
			final int width, final int height, final String contentBuf,
			final int contentBufLen, final StringBuffer filename) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void listDefaultGetParams(final DefaultParam dps) {
	}

	/**
	 * {@inheritDoc}
	 */
	public int listLoadNext(final int parentWin, final int listWin,
			final String fileToLoad, final int showFlags) {
		return LISTPLUGIN_ERROR;
	}

	/**
	 * {@inheritDoc}
	 */
	public int listSearchDialog(final int listWin, final int findNext) {
		return LISTPLUGIN_ERROR;
	}

}
