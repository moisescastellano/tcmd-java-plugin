package tcplugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wlx.SwingWLXPlugin;

import com.Ostermiller.Syntax.HighlightedDocument;

public class JADDemo extends SwingWLXPlugin {
	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(JADDemo.class);

	@Override
	public final String listGetDetectString(final int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("JADDemo.listGetDetectString()");
		}
		return "EXT=\"CLASS\"|EXT=\"JAVA\"";
	}

	@Override
	public JFrame createJFrame() {
		if (log.isDebugEnabled()) {
			log.debug("JADDemo.createJFrame()");
		}
		return new JFrame("JAD Demo");
	}

	@Override
	public void listLoad(final JFrame frame, String input, int showFlags) {
		if (log.isDebugEnabled()) {
			log.debug("JADDemo.listLoad(input, showFlags)");
		}
		StringBuffer ret = new StringBuffer();
		File inputFile = new File(input);
		try {
			if (input.endsWith(".class")) {
				String nameExt = inputFile.getName();
				String basename = nameExt.substring(0, nameExt.indexOf('.'));
				File dir = inputFile.getParentFile();

				URL url = this.getClass().getClassLoader().getResource(
						"jad.exe");
				String cmd = url.getFile() + " -o -s java -d \""
						+ dir.getPath().replace('\\', '/') + "\" \"" + input
						+ "\"";
				if (log.isDebugEnabled()) {
					log.debug("JADDemo.listLoad(input, showFlags) cmd=" + cmd);
				}
				Process p = Runtime.getRuntime().exec(cmd);
				if (log.isDebugEnabled()) {
					log.debug("JADDemo.listLoad(input, showFlags) OK: cmd="
							+ cmd);
				}
				InputStream stderr = p.getErrorStream();
				if (stderr != null) {
					InputStreamReader isr = new InputStreamReader(stderr);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					ret.append("/*\n");
					while ((line = br.readLine()) != null) {
						ret.append(line + "\n");
						if (log.isDebugEnabled()) {
							log.debug(".run() line=" + line);
						}
					}
					ret.append("\n*/\n");
					br.close();
				}
				if (log.isDebugEnabled()) {
					log
							.debug("JADDemo.listLoad(input, showFlags) WAIT FOR EXEC END");
				}
				try {
					p.waitFor();
				} catch (InterruptedException e) {
					log.debug(e.getMessage(), e);
				}
				int rc = p.exitValue();
				if (log.isDebugEnabled()) {
					log
							.debug("JADDemo.listLoad(input, showFlags) Terminated: RC="
									+ rc);
				}
				inputFile = new File(dir, basename + ".java");
			} else {
				inputFile = new File(input);
			}
			if (log.isDebugEnabled()) {
				log.debug("JADDemo.listLoad(input, showFlags) file="
						+ inputFile.getAbsolutePath());
			}
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				ret.append(line).append('\n');
			}
			br.close();

			HighlightedDocument document = new HighlightedDocument();
			document.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
			JTextPane textPane = new JTextPane(document);
			textPane.setText(ret.toString());

			frame.add(new JScrollPane(textPane));

		} catch (FileNotFoundException e) {
			if (log.isErrorEnabled()) {
				log.error("JADDemo.listLoad(input, showFlags)", e);
			}
			throw new RuntimeException(e);
		} catch (IOException e) {
			if (log.isErrorEnabled()) {
				log.error("JADDemo.listLoad(input, showFlags)", e);
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public void listCloseWindow(final JFrame frame) {
		if (log.isDebugEnabled()) {
			log.debug("JADDemo.listCloseWindow()");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object listGetPreviewBitmap(String fileToLoad, int width,
			int height, String contentBuf, int contentBufLen,
			StringBuffer filename) {
		if (log.isDebugEnabled()) {
			log.debug("JADDemo.listGetPreviewBitmap()");
		}
		// filename.append("252|shell32.dll");
		filename.append("%CWD%\\Angler.bmp");
		return super.listGetPreviewBitmap(fileToLoad, width, height,
				contentBuf, contentBufLen, filename);
	}
}
