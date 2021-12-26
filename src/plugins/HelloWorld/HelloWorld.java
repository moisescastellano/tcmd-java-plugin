import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import plugins.wlx.SWTWLXPlugin;

public class HelloWorld extends SWTWLXPlugin {

	Log log = LogFactory.getLog(HelloWorld.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String listGetDetectString(int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("HelloWorld.listGetDetectString()");
		}
		return "EXT=\"tst\"";
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
			log.debug("HelloWorld.listLoad()");
		}
		StringBuffer buf = new StringBuffer();
		Properties p = System.getProperties();
		Enumeration < Object > elements = p.keys();
		while (elements.hasMoreElements()) {
			String element = (String) elements.nextElement();
			buf.append("" + element + " = " + System.getProperty(element)
					+ "\n");
		}
		Text lbl = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		lbl.setText(buf.toString());
	}

	@Override
	public void listCloseWindow() {
		if (log.isDebugEnabled()) {
			log.debug("HelloWorld.listCloseWindow()");
		}
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		Shell shell = new Shell(new Display(), SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		new HelloWorld().listLoad(shell,
				"g:/Totalcmd/plugins/java/HelloWorld/demo.tst", 0);
		shell.open();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}
}
