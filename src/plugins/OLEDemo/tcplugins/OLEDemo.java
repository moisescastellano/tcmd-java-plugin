package tcplugins;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.PrintWriter;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import plugins.wlx.SWTWLXPlugin;

public class OLEDemo extends SWTWLXPlugin {
	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(OLEDemo.class);

	@Override
	public final String listGetDetectString(final int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("OLEDemo.listGetDetectString()");
		}
		return "EXT=\"DOC\"|EXT=\"RTF\"|"//+"EXT=\"TXT\"|"
				+ // Word
				"EXT=\"XLS\"|"
				+ // Excel
				"EXT=\"WMV\"|EXT=\"MPA\"|EXT=\"MPG\"|EXT=\"MPEG\"|EXT=\"AVI\"|EXT=\"ASF\"|EXT=\"WAV\"|"
				+ // Media Player
				"EXT=\"PDF\""; // Acrobat Reader
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
			log.debug("OLEDemo.listLoad(shell, input, showFlags)");
		}
		open(shell, input);
	}

	public static void main(String[] args) {
		Shell shell = new Shell(new Display(), SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		new OLEDemo().listLoad(shell,
				args[0], 0);
		shell.open();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}

	OleClientSite clientSite;

	OleFrame oleFrame;

	Button closeButton;

	/**
	 * Create a file Exit menu item
	 */
	void addFileMenu(OleFrame frame) {
		final Shell shell = frame.getShell();
		Menu menuBar = shell.getMenuBar();
		if (menuBar == null) {
			menuBar = new Menu(shell, SWT.BAR);
			shell.setMenuBar(menuBar);
		}
		MenuItem fileMenu = new MenuItem(menuBar, SWT.CASCADE);
		fileMenu.setText("&File");
		Menu menuFile = new Menu(fileMenu);
		fileMenu.setMenu(menuFile);
		frame.setFileMenus(new MenuItem[] { fileMenu });

		MenuItem menuFileExit = new MenuItem(menuFile, SWT.CASCADE);
		menuFileExit.setText("Exit");
		menuFileExit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
	}

	void disposeClient() {
		if (clientSite != null)
			clientSite.dispose();
		clientSite = null;
	}

	/**
	 * Prompt the user for a file and try to open it with some known ActiveX
	 * controls.
	 */
	void fileOpen(String fileName) {
		if (fileName == null) {
			Shell shell = oleFrame.getShell();
			FileDialog dialog = new FileDialog(shell, SWT.OPEN);
			fileName = dialog.open();
			if (fileName == null)
				return;
		}
		disposeClient();

		// try opening a .doc file using Word
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("doc")
						|| fileExtension.equalsIgnoreCase("rtf")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE,
								"Word.Document", new File(fileName));
					} catch (SWTException e) {
						disposeClient();
						Shell shell = new Shell(SWT.SHELL_TRIM);
						shell.setLayout(new FillLayout());
						shell.setText(e.getMessage());
						Label lbl = new Label(shell, SWT.NONE);
						CharArrayWriter wr = new CharArrayWriter();
						e.printStackTrace(new PrintWriter(wr));
						lbl.setText(wr.toString());
					}
				}
			}
		}

		// try opening a xls file with Excel
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("xls")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE,
								"Excel.Sheet", new File(fileName));
					} catch (SWTException e) {
						disposeClient();
						Shell shell = new Shell(SWT.SHELL_TRIM);
						shell.setLayout(new FillLayout());
						shell.setText(e.getMessage());
						Label lbl = new Label(shell, SWT.NONE);
						CharArrayWriter wr = new CharArrayWriter();
						e.printStackTrace(new PrintWriter(wr));
						lbl.setText(wr.toString());
					}
				}
			}
		}

		// try opening a media file with MPlayer
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("mpa")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE,
								"MPlayer", new File(fileName));
					} catch (SWTException e) {
						disposeClient();
						Shell shell = new Shell(SWT.SHELL_TRIM);
						shell.setLayout(new FillLayout());
						shell.setText(e.getMessage());
						Label lbl = new Label(shell, SWT.NONE);
						CharArrayWriter wr = new CharArrayWriter();
						e.printStackTrace(new PrintWriter(wr));
						lbl.setText(wr.toString());
					}
				}
			}
		}

		// try opening with wmv, mpg, mpeg, avi, asf, wav with WMPlayer
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("wmv")
						|| fileExtension.equalsIgnoreCase("mpg")
						|| fileExtension.equalsIgnoreCase("mpeg")
						|| fileExtension.equalsIgnoreCase("avi")
						|| fileExtension.equalsIgnoreCase("asf")
						|| fileExtension.equalsIgnoreCase("wav")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE,
								"WMPlayer.OCX");
						OleAutomation player = new OleAutomation(clientSite);
						int playURL[] = player
								.getIDsOfNames(new String[] { "URL" });
						if (playURL != null) {
							boolean suceeded = player.setProperty(playURL[0],
									new Variant(fileName));
							if (!suceeded)
								disposeClient();
						} else {
							disposeClient();
						}
						player.dispose();
					} catch (SWTException e) {
						disposeClient();
						Shell shell = new Shell(SWT.SHELL_TRIM);
						shell.setLayout(new FillLayout());
						shell.setText(e.getMessage());
						Label lbl = new Label(shell, SWT.NONE);
						CharArrayWriter wr = new CharArrayWriter();
						e.printStackTrace(new PrintWriter(wr));
						lbl.setText(wr.toString());
						shell.open();
					}
				}
			}
		}

		// try opening a PDF file with Acrobat reader
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("pdf")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE,
								"PDF.PdfCtrl.5");
						clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
						OleAutomation pdf = new OleAutomation(clientSite);
						int loadFile[] = pdf
								.getIDsOfNames(new String[] { "LoadFile" });
						if (loadFile != null) {
							Variant result = pdf.invoke(loadFile[0],
									new Variant[] { new Variant(fileName) });
							if (result == null)
								disposeClient();
							else
								result.dispose();
						} else {
							disposeClient();
						}
						pdf.dispose();
					} catch (SWTException e) {
						disposeClient();
						Shell shell = new Shell(SWT.SHELL_TRIM);
						shell.setLayout(new FillLayout());
						shell.setText(e.getMessage());
						Label lbl = new Label(shell, SWT.NONE);
						CharArrayWriter wr = new CharArrayWriter();
						e.printStackTrace(new PrintWriter(wr));
						lbl.setText(wr.toString());
					}
				}
			}
		}

		// try opening with Explorer
		if (clientSite == null) {
			try {
				clientSite = new OleClientSite(oleFrame, SWT.NONE,
						"Shell.Explorer");
				OleAutomation explorer = new OleAutomation(clientSite);
				int[] navigate = explorer
						.getIDsOfNames(new String[] { "Navigate" });

				if (navigate != null) {
					Variant result = explorer.invoke(navigate[0],
							new Variant[] { new Variant(fileName) });
					if (result == null)
						disposeClient();
					else
						result.dispose();
				} else {
					disposeClient();
				}
				explorer.dispose();
			} catch (SWTException e) {
				disposeClient();
				Shell shell = new Shell(SWT.SHELL_TRIM);
				shell.setLayout(new FillLayout());
				shell.setText(e.getMessage());
				Label lbl = new Label(shell, SWT.NONE);
				CharArrayWriter wr = new CharArrayWriter();
				e.printStackTrace(new PrintWriter(wr));
				lbl.setText(wr.toString());
			}
		}

		if (clientSite != null) {
			clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
		}
	}

	void newClientSite(String progID) {
		disposeClient();
		try {
			clientSite = new OleClientSite(oleFrame, SWT.NONE, progID);
		} catch (SWTException error) {

		}
		if (clientSite != null)
			clientSite.doVerb(OLE.OLEIVERB_SHOW);
	}

	public void open(Shell shell, final String input) {
		shell.setText("OLE Example");

		Composite parent = new Composite(shell, SWT.NONE);
		parent.setLayout(new GridLayout(4, true));

		Composite buttons = new Composite(parent, SWT.NONE);
		buttons.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.BEGINNING, SWT.FILL, false, false);
		buttons.setLayoutData(gridData);

		Composite displayArea = new Composite(parent, SWT.BORDER);
		displayArea.setLayout(new FillLayout());
		displayArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				3, 1));

		Button excelButton = new Button(buttons, SWT.RADIO);
		excelButton.setText("New Excel Sheet");
		excelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					newClientSite("Excel.Sheet");
			}
		});
		Button mediaPlayerButton = new Button(buttons, SWT.RADIO);
		mediaPlayerButton.setText("New MPlayer");
		mediaPlayerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					newClientSite("MPlayer");
			}
		});
		Button powerPointButton = new Button(buttons, SWT.RADIO);
		powerPointButton.setText("New PowerPoint Slide");
		powerPointButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					newClientSite("PowerPoint.Slide");
			}
		});
		Button wordButton = new Button(buttons, SWT.RADIO);
		wordButton.setText("New Word Document");
		wordButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					newClientSite("Word.Document");
			}
		});
		new Label(buttons, SWT.NONE);
		Button openButton = new Button(buttons, SWT.RADIO);
		openButton.setText("Open file...");
		openButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					fileOpen(null);
			}
		});
		new Label(buttons, SWT.NONE);
		closeButton = new Button(buttons, SWT.RADIO);
		closeButton.setText("Close file");
		closeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					disposeClient();
			}
		});
		closeButton.setSelection(true);

		oleFrame = new OleFrame(displayArea, SWT.NONE);
		addFileMenu(oleFrame);

		shell.setSize(800, 600);
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				fileOpen(input);
			}
		});
	}

	@Override
	public void listCloseWindow() {
		if (log.isDebugEnabled()) {
			log.debug("OLEDemo.listCloseWindow()");
		}
	}

}
