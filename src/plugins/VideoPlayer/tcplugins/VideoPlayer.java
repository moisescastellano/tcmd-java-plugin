package tcplugins;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wlx.SwingWLXPlugin;

public class VideoPlayer extends SwingWLXPlugin {

	Log log = LogFactory.getLog(VideoPlayer.class);
	
	@Override
	public JFrame createJFrame() {
		if (log.isDebugEnabled()) {
			log.debug("VideoPlayer.createJFrame()");
		}
		return new PlayerFrame();
	}

	@Override
	public void listLoad(JFrame frame, String input, int showFlags) {
		if (log.isDebugEnabled()) {
			log.debug("VideoPlayer.listLoad()");
		}
		PlayerFrame player = (PlayerFrame)frame;
		player.init(input);
		player.start();
	}

	@Override
	public void listCloseWindow(JFrame frame) {
		if (log.isDebugEnabled()) {
			log.debug("VideoPlayer.listCloseWindow()");
		}
		PlayerFrame player = (PlayerFrame)frame;
		player.stop();
		player.destroy();
	}

	@Override
	public String listGetDetectString(int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("VideoPlayer.listGetDetectString()");
		}
		return "MULTIMEDIA&EXT=\"MPG\"|EXT=\"MPEG\"|EXT=\"MOV\"|EXT=\"AVI\"";
	}

}
