package plugins.wlx;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * @author Santhosh Kumar T - santhosh@in.fiorano.com
 * 
 * Event queue to check if a context menu is to be opened.
 */
public class PopupMenuEventQueue extends EventQueue {
	/**
	 * {@inheritDoc}
	 */
	protected final void dispatchEvent(final AWTEvent event) {
		super.dispatchEvent(event);

		// interested only in mouseevents
		if (!(event instanceof MouseEvent)) {
			return;
		}
		MouseEvent me = (MouseEvent) event;

		// interested only in popuptriggers
		if (!me.isPopupTrigger()) {
			return;
		}
		// me.getComponent(...) retunrs the heavy weight component on which
		// event occured
		Component comp = SwingUtilities.getDeepestComponentAt(
				me.getComponent(), me.getX(), me.getY());

		// interested only in textcomponents
		if (!(comp instanceof JTextComponent)) {
			return;
		}
		// no popup shown by user code
		if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0) {
			return;
		}

		// create popup menu and show
		JTextComponent tc = (JTextComponent) comp;
		JPopupMenu menu = new JPopupMenu();
		menu.add(new CutAction(tc));
		menu.add(new CopyAction(tc));
		menu.add(new PasteAction(tc));
		menu.add(new DeleteAction(tc));
		menu.addSeparator();
		menu.add(new SelectAllAction(tc));

		Point pt = SwingUtilities.convertPoint(me.getComponent(),
				me.getPoint(), tc);
		menu.show(tc, pt.x, pt.y);
	}

	/**
	 * @author Santhosh Kumar T - santhosh@in.fiorano.com
	 * 
	 */
	class CutAction extends AbstractAction {
		/**
		 * default serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * text component of the text action.
		 */
		private JTextComponent fComp;

		/**
		 * create a new text action.
		 * 
		 * @param comp
		 *            text component of the text action.
		 */
		public CutAction(final JTextComponent comp) {
			super("Cut");
			this.fComp = comp;
		}

		/**
		 * {@inheritDoc}
		 */
		public void actionPerformed(final ActionEvent e) {
			fComp.cut();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isEnabled() {
			return fComp.isEditable() && fComp.isEnabled()
					&& fComp.getSelectedText() != null;
		}
	}

	/**
	 * @author Santhosh Kumar T - santhosh@in.fiorano.com
	 * 
	 */
	class PasteAction extends AbstractAction {
		/**
		 * default serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * text component of the text action.
		 */
		private JTextComponent fComp;

		/**
		 * create a new text action.
		 * 
		 * @param comp
		 *            text component of the text action.
		 */
		public PasteAction(final JTextComponent comp) {
			super("Paste");
			this.fComp = comp;
		}

		/**
		 * {@inheritDoc}
		 */
		public void actionPerformed(final ActionEvent e) {
			fComp.paste();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isEnabled() {
			if (fComp.isEditable() && fComp.isEnabled()) {
				Transferable contents = Toolkit.getDefaultToolkit()
						.getSystemClipboard().getContents(this);
				return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
			} else {
				return false;
			}
		}
	}

	/**
	 * @author Santhosh Kumar T - santhosh@in.fiorano.com
	 * 
	 */
	class DeleteAction extends AbstractAction {
		/**
		 * default serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * text component of the text action.
		 */
		private JTextComponent fComp;

		/**
		 * create a new text action.
		 * 
		 * @param comp
		 *            text component of the text action.
		 */
		public DeleteAction(final JTextComponent comp) {
			super("Delete");
			this.fComp = comp;
		}

		/**
		 * {@inheritDoc}
		 */
		public void actionPerformed(final ActionEvent e) {
			fComp.replaceSelection(null);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isEnabled() {
			return fComp.isEditable() && fComp.isEnabled()
					&& fComp.getSelectedText() != null;
		}
	}

	/**
	 * @author Santhosh Kumar T - santhosh@in.fiorano.com
	 * 
	 */
	class CopyAction extends AbstractAction {
		/**
		 * default serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * text component of the text action.
		 */
		private JTextComponent fComp;

		/**
		 * create a new text action.
		 * 
		 * @param comp
		 *            text component of the text action.
		 */
		public CopyAction(final JTextComponent comp) {
			super("Copy");
			this.fComp = comp;
		}

		/**
		 * {@inheritDoc}
		 */
		public void actionPerformed(final ActionEvent e) {
			fComp.copy();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isEnabled() {
			return fComp.isEnabled() && fComp.getSelectedText() != null;
		}
	}

	/**
	 * @author Santhosh Kumar T - santhosh@in.fiorano.com
	 * 
	 */
	class SelectAllAction extends AbstractAction {
		/**
		 * default serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * text component of the text action.
		 */
		private JTextComponent fComp;

		/**
		 * create a new text action.
		 * @param comp  text component of the text action.
		 */
		public SelectAllAction(final JTextComponent comp) {
			super("Select All");
			this.fComp = comp;
		}

		/**
		 * {@inheritDoc}
		 */
		public void actionPerformed(final ActionEvent e) {
			fComp.selectAll();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isEnabled() {
			return fComp.isEnabled() && fComp.getText().length() > 0;
		}
	}

}
