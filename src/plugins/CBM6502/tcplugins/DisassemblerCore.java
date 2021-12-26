package tcplugins;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author Ken CPU disassembler for 6502
 */
public class DisassemblerCore {

	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(DisassemblerCore.class);

	/**
	 * bundle name with CPU commands.
	 */
	private static final String CPU_CMDS_BUNDLE = "tcplugins/6502"; //$NON-NLS-1$

	/**
	 * base of hex numbers.
	 */
	private static final int BASE_HEX = 16;

	/**
	 * the empty string.
	 */
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * delimiter in the bundle of CPU commands.
	 */
	private static final String COLON = ":"; //$NON-NLS-1$

	/**
	 * 2 byte command.
	 */
	private static final int CMD_2_BYTES = 2;

	/**
	 * 3 byte command.
	 */
	private static final int CMD_3_BYTES = 3;

	/**
	 * carriage return character.
	 */
	private static final int CR = 0x0d;

	/**
	 * newline character.
	 */
	private static final int NEWLINE = 0x0a;

	/**
	 * tab width settings.
	 */
	private static final int TAB_WIDTH = 4;

	/**
	 * HashMap of All CPU commands.
	 */
	private HashMap cpuCommands = new HashMap();

	/**
	 * printing: font type.
	 */
	private Font font;

	/**
	 * printing: print drawing area.
	 */
	private GC gc;

	/**
	 * printing: printer font.
	 */
	private Font printerFont;

	/**
	 * printing: printer for-/background colours.
	 */
	private Color printerForegroundColor, printerBackgroundColor;

	/**
	 * printing: line height.
	 */
	private int lineHeight = 0;

	/**
	 * printing: tab width settings.
	 */
	private int tabWidth = 0;

	/**
	 * printing: margins.
	 */
	private int leftMargin, rightMargin, topMargin, bottomMargin;

	/**
	 * printing: current position.
	 */
	private int x, y;

	/**
	 * printing: index and end offset.
	 */
	private int index, end;

	/**
	 * printing: text to print.
	 */
	private String textToPrint;

	/**
	 * printing: line buffer.
	 */
	private StringBuffer wordBuffer;

	/**
	 * Read the PRG file and disassemble.
	 * 
	 * @param input
	 *            the input filename
	 * @param table
	 *            the table
	 */
	public final void readFile(final String input, final Table table) {

		FileInputStream fin = null;
		try {
			fin = new FileInputStream(input);
			boolean initial = true;
			int off = 0;
			while (fin.available() > 0) {
				ArrayList args = new ArrayList();
				// Startadresse lesen
				if (initial) {
					initial = false;
					getHighLowHex(fin.read(), fin.read(), args);
					off = Integer.parseInt((String) args.get(1)
							+ (String) args.get(0), 16);
					args = new ArrayList();
				}

				int ch = fin.read();
				Command cmd = (Command) cpuCommands.get(new Integer(ch));
				if (cmd != null) {
					int opCode = cmd.getOpCode();
					int numBytes = cmd.getNumBytes();
					getHighLowHex(
							(numBytes == CMD_2_BYTES || numBytes == CMD_3_BYTES) ? fin
									.read()
									: -1, (numBytes == CMD_3_BYTES) ? fin
									.read() : -1, args);
					String opStr = toHex(opCode);
					args.add(opStr);
					args.add(String.valueOf(cmd.getCycles()));
					if (numBytes == CMD_3_BYTES) {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(0, String.valueOf(off));
						item.setText(1, (String) args.get(2));
						item.setText(2, (String) args.get(0));
						item.setText(3, (String) args.get(1));
						item.setText(4, cmd.getFormatString());
						item.setText(5, MessageFormat.format(
								cmd.getArguments(), args.toArray()));
						item.setText(6, String.valueOf(numBytes));
						item.setText(7, (String) args.get(3));
					} else if (numBytes == CMD_2_BYTES) {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(0, String.valueOf(off));
						item.setText(1, (String) args.get(1));
						item.setText(2, (String) args.get(0));
						item.setText(4, cmd.getFormatString());
						item.setText(5, MessageFormat.format(
								cmd.getArguments(), args.toArray()));
						item.setText(6, String.valueOf(numBytes));
						item.setText(7, (String) args.get(2));
					} else {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(0, String.valueOf(off));
						item.setText(1, (String) args.get(0));
						item.setText(4, cmd.getFormatString());
						item.setText(6, String.valueOf(numBytes));
						item.setText(7, (String) args.get(1));
					}
					off += numBytes;
				} else {
					String opStr = toHex(ch);
					args.add(opStr);
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, String.valueOf(off));
					item.setText(1, (String) args.get(0));
					item.setText(4, TextBundle.getString("CMD_ILL_TEXT"));
					item.setText(6, String.valueOf(1));
					off++;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				fin.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Read the 6502 configuration file and generate the command table.
	 */
	public final void readConfiguration() {
		ResourceBundle commands = null;
		try {
			commands = ResourceBundle.getBundle(CPU_CMDS_BUNDLE);
		} catch (MissingResourceException e) {
			log.error("Disassembler.Disassembler()", e); //$NON-NLS-1$
			return;
		}
		Enumeration cmds = commands.getKeys();
		while (cmds.hasMoreElements()) {
			String opCode = (String) cmds.nextElement();
			String formatString = EMPTY_STRING;
			String arguments = EMPTY_STRING;
			int numBytes = 1;
			int cycles = 0;
			StringTokenizer tok = new StringTokenizer(commands
					.getString(opCode), COLON);
			int i = 0;
			if (tok.countTokens() == 3) {
				while (tok.hasMoreTokens()) {
					switch (i) {
					case 0:
						formatString = (String) tok.nextToken();
						break;

					case 1:
						numBytes = Integer.parseInt((String) tok.nextToken());
						break;

					case 2:
						cycles = Integer.parseInt((String) tok.nextToken());
						break;

					default:
						break;
					}
					i++;
				}
			} else {
				while (tok.hasMoreTokens()) {
					switch (i) {
					case 0:
						formatString = (String) tok.nextToken();
						break;

					case 1:
						arguments = (String) tok.nextToken();
						break;

					case 2:
						numBytes = Integer.parseInt((String) tok.nextToken());
						break;

					case 3:
						cycles = Integer.parseInt((String) tok.nextToken());
						break;

					default:
						break;
					}
					i++;
				}

			}
			int parseInt = Integer.parseInt(opCode, BASE_HEX);
			cpuCommands.put(new Integer(parseInt), new Command(parseInt,
					formatString, arguments, numBytes, cycles));
		}

	}

	/**
	 * Get hex value from integer (exactly two digits).
	 * 
	 * @param ch
	 *            the integer value to convert
	 * @return the two digit hex value of the integer
	 */
	private String toHex(final int ch) {
		String opStr = Integer.toHexString(ch).toUpperCase();
		if (opStr.length() < 2) {
			opStr = "0" + opStr; //$NON-NLS-1$
		}
		return opStr;
	}

	/**
	 * get low and high byte (as integers) and convert them to hex. Put these
	 * hex digits into the arraylist. Ignore the low or high byte, if it is -1.
	 * 
	 * @param low
	 *            the low byte
	 * @param high
	 *            the high byte
	 * @param args
	 *            the result list
	 * @throws IOException
	 */
	private void getHighLowHex(final int low, final int high,
			final ArrayList args) {
		if (low != -1) {
			String lowStr = toHex(low);
			args.add(lowStr);
		}
		if (high != -1) {
			String highStr = toHex(high);
			args.add(highStr);
		}
	}

	/**
	 * Print to the printer.
	 * 
	 * @param display
	 *            the display
	 * @param printer
	 *            the printer
	 * @param string
	 *            the contents to rpint
	 * @param foregroundColor
	 *            TODO
	 * @param backgroundColor
	 *            TODO
	 */
	public final void print(final Display display, final Printer printer,
			final String string, final Color foregroundColor,
			final Color backgroundColor) {
		if (log.isDebugEnabled()) {
			log.debug("DisassemblerCore.print() string=" + string);
		}
		font = new Font(display, "Courier", 10, SWT.NORMAL);

		textToPrint = string;
		if (printer.startJob("Text")) { // the string is the job name - shows up
			// in the printer's job list
			org.eclipse.swt.graphics.Rectangle clientArea = printer
					.getClientArea();
			org.eclipse.swt.graphics.Rectangle trim = printer.computeTrim(0, 0,
					0, 0);
			Point dpi = printer.getDPI();
			leftMargin = dpi.x + trim.x; // one inch from left side of paper
			rightMargin = clientArea.width - dpi.x + trim.x + trim.width; // one
			// inch
			// from
			// right
			// side
			// of
			// paper
			topMargin = dpi.y + trim.y; // one inch from top edge of paper
			bottomMargin = clientArea.height - dpi.y + trim.y + trim.height;
			// one
			// inch
			// from
			// bottom
			// edge
			// of
			// paper

			/* Create a buffer for computing tab width. */
			int tabSize = TAB_WIDTH;
			// is tab width a user setting in your UI?
			StringBuffer tabBuffer = new StringBuffer(tabSize);
			for (int i = 0; i < tabSize; i++) {
				tabBuffer.append(' ');
			}
			String tabs = tabBuffer.toString();

			/*
			 * Create printer GC, and create and set the printer font &
			 * foreground color.
			 */
			gc = new GC(printer);

			FontData fontData = font.getFontData()[0];
			printerFont = new Font(printer, fontData.getName(), fontData
					.getHeight(), fontData.getStyle());
			gc.setFont(printerFont);
			tabWidth = gc.stringExtent(tabs).x;
			lineHeight = gc.getFontMetrics().getHeight();

			RGB rgb = foregroundColor.getRGB();
			printerForegroundColor = new Color(printer, rgb);
			gc.setForeground(printerForegroundColor);

			rgb = backgroundColor.getRGB();
			printerBackgroundColor = new Color(printer, rgb);
			gc.setBackground(printerBackgroundColor);

			/* Print text to current gc using word wrap */
			printText(printer);
			printer.endJob();

			/* Cleanup graphics resources used in printing */
			printerFont.dispose();
			printerForegroundColor.dispose();
			printerBackgroundColor.dispose();
			gc.dispose();
		}
	}

	/**
	 * Print the text field.
	 * 
	 * @param printer
	 *            the printer
	 */
	private void printText(final Printer printer) {
		if (log.isDebugEnabled()) {
			log.debug("DisassemblerCore.printText()");
		}
		printer.startPage();
		wordBuffer = new StringBuffer();
		x = leftMargin;
		y = topMargin;
		index = 0;
		end = textToPrint.length();
		while (index < end) {
			char c = textToPrint.charAt(index);
			index++;
			if (c != 0) {
				if (c == NEWLINE || c == CR) {
					if (c == CR && index < end
							&& textToPrint.charAt(index) == NEWLINE) {
						index++; // if this is cr-lf, skip the lf
					}
					printWordBuffer(printer);
					newline(printer);
				} else {
					if (c != '\t') {
						wordBuffer.append(c);
					}
					if (Character.isWhitespace(c)) {
						printWordBuffer(printer);
						if (c == '\t') {
							x += tabWidth;
						}
					}
				}
			}
		}
		if (y + lineHeight <= bottomMargin) {
			printer.endPage();
		}
	}

	/**
	 * Print a string.
	 * 
	 * @param printer
	 *            the printer
	 */
	private void printWordBuffer(final Printer printer) {
		if (log.isDebugEnabled()) {
			log.debug("DisassemblerCore.printWordBuffer()");
		}
		if (wordBuffer.length() > 0) {
			String word = wordBuffer.toString();
			int wordWidth = gc.stringExtent(word).x;
			if (x + wordWidth > rightMargin) {
				/* word doesn't fit on current line, so wrap */
				newline(printer);
			}
			gc.drawString(word, x, y, false);
			x += wordWidth;
			wordBuffer = new StringBuffer();
		}
	}

	/**
	 * Print newline.
	 * 
	 * @param printer
	 *            the printer
	 */
	private void newline(final Printer printer) {
		x = leftMargin;
		y += lineHeight;
		if (y + lineHeight > bottomMargin) {
			printer.endPage();
			if (index + 1 < end) {
				y = topMargin;
				printer.startPage();
			}
		}
	}
}
