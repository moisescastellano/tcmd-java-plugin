/**
 * 
 */
package tcplugins;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author Ken
 * 
 */
public class SendMail {

	/**
	 * The weight of the attachments.
	 */
	private static final int ATTACHMENT_WEIGHT = 35;

	/**
	 * The weight of the mail address.
	 */
	private static final int ADDRESS_WEIGHT = 65;

	/**
	 * The width of the table column size.
	 */
	private static final int SIZE_WIDTH = 60;

	/**
	 * The width of the table column attachment.
	 */
	private static final int ATTACHMENT_WIDTH = 200;

	/**
	 * The weight of the mail contents.
	 */
	private static final int CONTENTS_WEIGHT = 80;

	/**
	 * The weight of the mail address.
	 */
	private static final int HEADER_WEIGHT = 20;

	/**
	 * The height of the shell.
	 */
	private static final int SHELL_HEIGHT = 600;

	/**
	 * The width of the shell.
	 */
	private static final int SHELL_WIDTH = 800;

	/**
	 * The root shell.
	 */
	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="13,-69"

	/**
	 * The root composite.
	 */
	private Composite compRoot = null;

	/**
	 * The sash to seperate address from mail contents.
	 */
	private SashForm sashHeader = null;

	/**
	 * The sash to seperate address from attachments.
	 */
	private SashForm sashAddress = null;

	/**
	 * The address container.
	 */
	private Composite compAddress = null;

	/**
	 * The table with the mail attachments.
	 */
	private Table tblAttachments = null;

	/**
	 * The combo box with from-address.
	 */
	private CCombo cbxFrom = null;

	/**
	 * The combo box with to-address.
	 */
	private CCombo cbxTo = null;

	/**
	 * The combo box with CC-address.
	 */
	private CCombo cbxCc = null;

	/**
	 * The combo box with from-address.
	 */
	private Text txtFrom = null;

	/**
	 * The combo box with to-address.
	 */
	private Text txtTo = null;

	/**
	 * The combo box with CC-address.
	 */
	private Text txtCc = null;

	/**
	 * The container for address and contents.
	 */
	private Composite compBelowButtons = null;

	/**
	 * The toolBar.
	 */
	private ToolBar toolBar = null;

	/**
	 * The container for the mail contents.
	 */
	private Composite compDetails = null;

	/**
	 * The text area for the mail contents.
	 */
	private Text textAreaDetails = null;

	/**
	 * This method initializes sShell.
	 */
	private void createSShell() {

		sShell = new Shell();
		sShell.setText("Write Message");
		createToolBar();
		sShell.setSize(new Point(SHELL_WIDTH, SHELL_HEIGHT));
		createCompRoot();
		sShell.setLayout(new GridLayout());
	}

	/**
	 * This method initializes compRoot.
	 * 
	 */
	private void createCompRoot() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = GridData.FILL;
		FillLayout fillLayout1 = new FillLayout();
		fillLayout1.type = org.eclipse.swt.SWT.VERTICAL;
		compRoot = new Composite(sShell, SWT.NONE);
		createCompBelowButtons();
		compRoot.setLayoutData(gridData4);
		compRoot.setLayout(fillLayout1);
	}

	/**
	 * This method initializes sashHeader.
	 * 
	 */
	private void createSashHeader() {
		sashHeader = new SashForm(compBelowButtons, SWT.BORDER);
		sashHeader.setOrientation(SWT.VERTICAL);
		sashHeader.setLayout(new FillLayout());
		createSashForm2();
		createCompDetails();
		sashHeader.setWeights(new int[] { HEADER_WEIGHT, CONTENTS_WEIGHT });
	}

	/**
	 * This method initializes sashForm2.
	 * 
	 */
	private void createSashForm2() {
		sashAddress = new SashForm(sashHeader, SWT.BORDER);
		createCompAddress();
		tblAttachments = new Table(sashAddress, SWT.NONE);
		tblAttachments.setHeaderVisible(true);
		tblAttachments.setLinesVisible(true);
		tblAttachments
				.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					public void mouseDoubleClick(
							final org.eclipse.swt.events.MouseEvent e) {
						System.out.println("mouseDoubleClick()"); // TODO
						// Auto-generated
						// Event
						// stub
						// mouseDoubleClick()
					}
				});
		TableColumn tableColumn = new TableColumn(tblAttachments, SWT.NONE);
		tableColumn.setWidth(ATTACHMENT_WIDTH);
		tableColumn.setText("Attachment");
		TableColumn tableColumn1 = new TableColumn(tblAttachments, SWT.NONE);
		tableColumn1.setWidth(SIZE_WIDTH);
		tableColumn1.setText("Size");
		sashAddress.setWeights(new int[] { ADDRESS_WEIGHT, ATTACHMENT_WEIGHT });
	}

	/**
	 * This method initializes compAddress.
	 * 
	 */
	private void createCompAddress() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.BEGINNING;
		gridData3.verticalAlignment = GridData.CENTER;
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = GridData.CENTER;
		gridData2.horizontalAlignment = GridData.FILL;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.horizontalAlignment = GridData.FILL;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.makeColumnsEqualWidth = false;
		compAddress = new Composite(sashAddress, SWT.NONE);
		compAddress.setLayout(gridLayout);
		cbxFrom = new CCombo(compAddress, SWT.NONE);
		cbxFrom.setText("From");
		cbxFrom.setLayoutData(gridData3);
		txtFrom = new Text(compAddress, SWT.BORDER);
		txtFrom.setText("kschwiersch@yahoo.de");
		txtFrom.setLayoutData(gridData);
		cbxTo = new CCombo(compAddress, SWT.NONE);
		cbxTo.setText("To");
		txtTo = new Text(compAddress, SWT.BORDER);
		txtTo.setText("Katrin.Haendel@web.de");
		txtTo.setLayoutData(gridData1);
		cbxCc = new CCombo(compAddress, SWT.NONE);
		cbxCc.setText("Cc");
		txtCc = new Text(compAddress, SWT.BORDER);
		txtCc.setText("shycpc@aol.com");
		txtCc.setLayoutData(gridData2);
	}

	/**
	 * This method initializes compBelowButtons.
	 * 
	 */
	private void createCompBelowButtons() {
		compBelowButtons = new Composite(compRoot, SWT.NONE);
		compBelowButtons.setLayout(new FillLayout());
		createSashHeader();
	}

	/**
	 * This method initializes toolBar.
	 * 
	 */
	private void createToolBar() {
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.BEGINNING;
		gridData5.verticalAlignment = GridData.CENTER;
		toolBar = new ToolBar(sShell, SWT.NONE);
		toolBar.setLayoutData(gridData5);
		ToolItem sendItem = new ToolItem(toolBar, SWT.PUSH);
		sendItem.setText("Send");
		sendItem.setSelection(false);
		sendItem
				.setImage(new Image(
						Display.getCurrent(),
						"C:/Dokumente und Einstellungen/Ken/Eigene Dateien/Open Office/Cliparts/computer/icons/lemon-theme/actions/redo.png"));
		ToolItem attachItem = new ToolItem(toolBar, SWT.PUSH);
		attachItem.setText("Attach");
		attachItem
				.setImage(new Image(
						Display.getCurrent(),
						"C:/Dokumente und Einstellungen/Ken/Eigene Dateien/Open Office/Cliparts/computer/icons/lemon-theme/actions/attach.png"));
		ToolItem saveItem = new ToolItem(toolBar, SWT.PUSH);
		saveItem.setText("Save");
		saveItem
				.setDisabledImage(new Image(
						Display.getCurrent(),
						"C:/Dokumente und Einstellungen/Ken/Eigene Dateien/Open Office/Cliparts/computer/floppy_disk_architetto_f_01.png"));
		saveItem.setEnabled(true);
		saveItem
				.setImage(new Image(
						Display.getCurrent(),
						"C:/Dokumente und Einstellungen/Ken/Eigene Dateien/Open Office/Cliparts/computer/floppy.png"));
		ToolItem closeItem = new ToolItem(toolBar, SWT.PUSH);
		closeItem
				.setImage(new Image(
						Display.getCurrent(),
						"C:/Dokumente und Einstellungen/Ken/Eigene Dateien/Open Office/Cliparts/computer/icons/lemon-theme/actions/stop.png"));
		closeItem.setText("Close");
	}

	/**
	 * This method initializes compDetails.
	 * 
	 */
	private void createCompDetails() {
		FillLayout fillLayout2 = new FillLayout();
		fillLayout2.type = org.eclipse.swt.SWT.HORIZONTAL;
		fillLayout2.spacing = 0;
		compDetails = new Composite(sashHeader, SWT.NONE);
		compDetails.setLayout(fillLayout2);
		textAreaDetails = new Text(compDetails, SWT.MULTI | SWT.WRAP
				| SWT.V_SCROLL | SWT.BORDER);
		textAreaDetails
				.setText("Dear Mr. Nobody, I want invite you to my party."
						+ " I hope you can come. Sincerely yours, Ken Händel");
	}
}
