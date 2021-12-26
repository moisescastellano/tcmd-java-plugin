package tcplugins;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import javazoom.jlgui.player.amp.PlayerActionEvent;
import javazoom.jlgui.player.amp.StandalonePlayer;
import javazoom.jlgui.player.amp.playlist.PlaylistItem;
import javazoom.jlgui.player.amp.skin.Skin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wlx.SwingWLXPlugin;

public class JLGui extends SwingWLXPlugin {

	/**
	 * logging support
	 */
	Log log = LogFactory.getLog(JLGui.class);

	public JLGui() {
		super(true);
	}
	
	public String listGetDetectString(int maxLen) {
		return "MULTIMEDIA&EXT=\"MP3\"|EXT=\"MPEG\"|EXT=\"FLAC\"|EXT=\"APE\"|EXT=\"MAC\"|EXT=\"OGG\"|EXT=\"SPX\"";
	}

	/**
	 * {@inheritDoc}
	 */
	public JFrame createJFrame() {

		javazoom.jlgui.player.amp.StandalonePlayer standalonePlayer = new javazoom.jlgui.player.amp.StandalonePlayer();
		standalonePlayer.setTitle(Skin.TITLETEXT);
		standalonePlayer.setUndecorated(true);
		return standalonePlayer;
	}

	/**
	 * {@inheritDoc}
	 */
	public void listLoad(final JFrame frame, final String input, int showFlags) {
		if (log.isDebugEnabled()) {
			log.debug("JLGui.listLoad(frame, input, showFlags)");
		}
		StandalonePlayer player = (StandalonePlayer) frame;
		player.parseParameters(new String[] { "-start" });
		player.loadUI();
		player.loadJS();
		player.loadPlaylist();
		player.getMp().getPlaylist().addItemAt(
				new PlaylistItem(input, input, -1, true), 0);
		player.getMp().getPlaylist().setCursor(0);
		player.boot();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void listCloseWindow(final JFrame frame) {
		if (log.isDebugEnabled()) {
			log.debug("JLGui.listCloseWindow(frame)");
		}
		StandalonePlayer player = (StandalonePlayer) frame;
		player.getMp().actionPerformed(new PlayerActionEvent(this, 0, PlayerActionEvent.ACEXIT));
		player.close();
	}

	@Override
	protected int getHeight(JFrame frame) {
		StandalonePlayer player = (StandalonePlayer) frame;
		return player.getMp().getSkin().getMainHeight();
	}

	@Override
	protected int getWidth(JFrame frame) {
		StandalonePlayer player = (StandalonePlayer) frame;
		return player.getMp().getSkin().getMainWidth();
	}

}
