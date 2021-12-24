package tcplugins;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Vector3f;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wlx.SwingWLXPlugin;
import sun.org.mozilla.javascript.internal.debug.DebugFrame;

import com.mnstarfire.utilities.Debug;

public class J3DDemo extends SwingWLXPlugin {

	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(J3DDemo.class);

	@Override
	public final String listGetDetectString(final int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("J3DDemo.listGetDetectString()");
		}
		return "EXT=\"3DS\"";
	}

	@Override
	public JFrame createJFrame() {
		if (log.isDebugEnabled()) {
			log.debug("J3DDemo.createJFrame()");
		}
		// TODO Auto-generated method stub
		return new JFrame("3D Demo");
	}

	@Override
	public void listCloseWindow(JFrame frame) {
		if (log.isDebugEnabled()) {
			log.debug("J3DDemo.listCloseWindow(frame)");
		}
	}

	@Override
	public void listLoad(JFrame frame, String input, int showFlags) {
		// global area
		JPanel tp = new JPanel();
		if (log.isDebugEnabled()) {
			log.debug("J3DDemo.listLoad(input, showFlags)");
		}
		if (log.isDebugEnabled()) {
			log.debug(".listLoad(input, showFlags) java.home="
					+ System.getProperty("java.home"));
			log
					.debug("J3DDemo.listLoad(input, showFlags) java.runtime.version="
							+ System.getProperty("java.runtime.version"));
		}
		try {
			final ObjLoad objLoad = new ObjLoad(
					new String[] { "file:/" + input });
			
			GridLayout g = new GridLayout(1, 2);
			g.setHgap(5);
			g.setVgap(5);
			tp.setLayout(g);

			JPanel vp = new JPanel();
			vp.setLayout(new BoxLayout(vp, BoxLayout.Y_AXIS));
			tp.add(vp);

			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			p.setBorder(new TitledBorder("Zoom"));
			JSlider s = new JSlider(JSlider.HORIZONTAL, 0, 10000, 1);
			p.add(Box.createRigidArea(new Dimension(1,5)));
			p.add(s);
			p.add(Box.createRigidArea(new Dimension(1,5)));
			
			vp.add(p);
			vp.add(new JScrollPane(objLoad));
			s.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					JSlider s1 = (JSlider) e.getSource();

					TransformGroup vpTrans = objLoad.getObjTrans();
					Vector3f translate = new Vector3f();
					// translate.set(100,100,100); // move the view to
					// wherever you want it.
					Transform3D T3D = new Transform3D();
					T3D.setTranslation(translate);
					T3D.setScale((double) s1.getValue());
					vpTrans.setTransform(T3D);
				}

			});
			s.setValue(4);
			Debug.closeLogFile();
		} catch (final Throwable e) {
			if (log.isDebugEnabled()) {
				log.debug(".run()", e);
			}
			CharArrayWriter wr = new CharArrayWriter();
			e.printStackTrace(new PrintWriter(wr));
			Container cp = frame.getContentPane();
			cp.add(new JTextArea(wr.toString()));
		}
		if (log.isDebugEnabled()) {
			log.debug("J3DDemo.listLoad(input, showFlags) READY");
		}
		Container cp = frame.getContentPane();
		cp.add(tp);
	}

}
