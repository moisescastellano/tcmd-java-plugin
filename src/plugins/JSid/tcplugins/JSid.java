/**
 * 
 */
package tcplugins;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wlx.SwingWLXPlugin;


import com.dreamfabric.jsidplay.JSIDPlay;

/**
 * @author de15267
 * 
 */
public class JSid extends SwingWLXPlugin {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String listGetDetectString(int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("JSid.listGetDetectString()");
		}
		return "EXT=\"SID\"|EXT=\"PSID\"";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void listCloseWindow(final JFrame frame) {
		if (log.isDebugEnabled()) {
			log.debug("JSid.listCloseWindow()");
		}
		final JSIDPlay sp = (JSIDPlay) frame;

		sp.stop();
	}

	private Log log = LogFactory.getLog(JSid.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void listLoad(final JFrame frame, final String input, int showFlags) {
		final JSIDPlay sp = (JSIDPlay) frame;
		if (log.isDebugEnabled()) {
			log.debug("JSid.listLoad(frame, input, showFlags)");
		}
		sp.init();
		sp.start();
		sp.readSIDFromFile(input);
		sp.playSID();
	}

	@Override
	public JFrame createJFrame() {
		if (log.isDebugEnabled()) {
			log.debug("JSid.createJFrame()");
		}
		return new JSIDPlay();
	}

}
