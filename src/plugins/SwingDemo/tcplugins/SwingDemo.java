package tcplugins;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wlx.SwingWLXPlugin;


/**
 * @author Ken Swing Demo
 */
public class SwingDemo extends SwingWLXPlugin {
	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(SwingDemo.class);

	/**
	 * {@inheritDoc}
	 */
	public final String listGetDetectString(final int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("SwingDemo.listGetDetectString()");
		}
		return "EXT=\"SWING\"";
	}

	/**
	 * {@inheritDoc}
	 */
	public final JFrame createJFrame() {
		if (log.isDebugEnabled()) {
			log.debug("SwingDemo.createJFrame()");
		}
		return new JFrame("Swing Demo");
	}

	@Override
	public void listCloseWindow(JFrame frame) {
		if (log.isDebugEnabled()) {
			log.debug("SwingDemo.listCloseWindow()");
		}
	}

	@Override
	public void listLoad(JFrame frame, String input, int showFlags) {
		if (log.isDebugEnabled()) {
			log.debug("SwingDemo.listLoad()");
		}
		new SwingSet2(frame, null, null);
	}
}
