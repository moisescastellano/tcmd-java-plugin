package tcplugins;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import plugins.wlx.SWTWLXPlugin;

public class CBM6502 extends SWTWLXPlugin {
	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(CBM6502.class);

	/**
	 * All Disassembler specific logic.
	 */
	private DisassemblerCore disassCore = new DisassemblerCore();

	@Override
	public final String listGetDetectString(final int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("CBM6502.listGetDetectString()");
		}
		return "EXT=\"PRG\"";
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
			log.debug("CBM6502.listLoad(shell, input, showFlags)");
		}
		Table fTable = new Table(shell, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		fTable.setLinesVisible(true);
		fTable.setHeaderVisible(true);
		String[] titles = { "Address", "+0", "+1", "+2", "Command",
				"Arguments", "Bytes", "Cycles" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(fTable, SWT.NONE);
			column.setText(titles[i]);
		}
		for (int i = 0; i < titles.length; i++) {
			fTable.getColumn(i).pack();
		}
		fTable.setSize(fTable.computeSize(SWT.DEFAULT, 200));
		disassCore.readConfiguration();
		disassCore.readFile(input, fTable);
	}

	@Override
	public void listCloseWindow() {
		if (log.isDebugEnabled()) {
			log.debug("CBM6502.listCloseWindow()");
		}
	}
}
