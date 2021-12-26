package tcplugins;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.examples.controlexample.ControlExample;
import org.eclipse.swt.widgets.Shell;

import plugins.wlx.SWTWLXPlugin;

public class SWTDemo extends SWTWLXPlugin {
	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(SWTDemo.class);

	@Override
	public final String listGetDetectString(final int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("SWTDemo.listGetDetectString()");
		}
		return "EXT=\"SWT\"";
	}

	@Override
	public Shell createShell() {
		if (log.isDebugEnabled()) {
			log.debug("BrowserExample.createShell()");
		}
		return new Shell(display);
	}

	@Override
	public void listLoad(Shell shell, String input, int showFlags) {
		if (log.isDebugEnabled()) {
			log.debug("SWTDemo.listLoad(shell, input, showFlags)");
		}
		new ControlExample(shell);
		shell.setText(ControlExample.getResourceString("window.title"));
	}

	@Override
	public void listCloseWindow() {
		if (log.isDebugEnabled()) {
			log.debug("SWTDemo.listCloseWindow()");
		}
	}
}
