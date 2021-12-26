package tcplugins;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wlx.SwingWLXPlugin;

public class ImageViewer extends SwingWLXPlugin {
	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(ImageViewer.class);

	@Override
	public final String listGetDetectString(final int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("ImageViewer.listGetDetectString()");
		}
		String[] suffixes = ImageIO.getReaderFileSuffixes();
		StringBuffer extensions = new StringBuffer("MULTIMEDIA&");
		for (int i = 0; i < suffixes.length; i++) {
			extensions.append("EXT=\"" + suffixes[i].toUpperCase() + "\"");
			if (i != suffixes.length - 1) {
				extensions.append("|");
			}
		}
		return extensions.toString();
	}

	@Override
	public JFrame createJFrame() {
		if (log.isDebugEnabled()) {
			log.debug("ImageViewer.createJFrame()");
		}
		return new JFrame("Load Image Sample");
	}

	@Override
	public void listLoad(JFrame frame, String input, int showFlags) {
		if (log.isDebugEnabled()) {
			log.debug("ImageViewer.listLoad()");
		}
		System.gc();
		LoadImageApp app = new LoadImageApp(input);
		frame.add(new JScrollPane(app));
	}

	@Override
	public void listCloseWindow(JFrame frame) {
		if (log.isDebugEnabled()) {
			log.debug("ImageViewer.listCloseWindow()");
		}
	}

}
