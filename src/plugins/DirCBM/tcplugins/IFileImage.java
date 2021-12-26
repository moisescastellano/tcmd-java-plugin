package tcplugins;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wcx.HeaderData;

/**
 * @author Ken
 * 
 */
public abstract class IFileImage {

	/**
	 * The bits per byte.
	 */
	public static final int BITS_PER_BYTE = 8;

	public static final int IMAGE_TYPE_NO_TYPE = 0;

	public static final int IMAGE_TYPE_1541_D64 = 1;

	public static final int IMAGE_TYPE_8050_D80 = 2;

	public static final int IMAGE_TYPE_8250_D82 = 3;

	public static final int IMAGE_TYPE_T64 = 4;

	/**
	 * BITMASK_FILETYPE.
	 */
	public static final byte BITMASK_FILETYPE = (byte) 0x7;

	/**
	 * FILETYPE_DEL.
	 */
	public static final byte FILETYPE_DEL = (byte) 0x00;

	/**
	 * FILETYPE_SEQ.
	 */
	public static final byte FILETYPE_SEQ = (byte) 0x01;

	/**
	 * FILETYPE_PRG.
	 */
	public static final byte FILETYPE_PRG = (byte) 0x02;

	/**
	 * FILETYPE_USR.
	 */
	public static final byte FILETYPE_USR = (byte) 0x03;

	/**
	 * FILETYPE_REL.
	 */
	public static final byte FILETYPE_REL = (byte) 0x04;

	/**
	 * All file extensions.
	 */
	static final String[] FILETYPES = new String[] { ".del", ".seq", ".prg",
			".usr", ".rel" };

	/**
	 * Start of inverted characters.
	 */
	private static final int CBM_CHAR_INVERS = 128;

	/**
	 * The Z character.
	 */
	private static final int CBM_CHAR_Z = 26;

	/**
	 * The [ character.
	 */
	private static final int CBM_CHAR_ECKAUF = 27;

	/**
	 * The ] character.
	 */
	private static final int CBM_CHAR_ECKZU = 29;

	/**
	 * Yet another CBM special character.
	 */
	private static final int CBM_CHAR_SOND1 = 28;

	/**
	 * Yet another CBM special character.
	 */
	private static final int CBM_CHAR_SOND2 = 30;

	/**
	 * Yet another CBM special character.
	 */
	private static final int CBM_CHAR_SOND3 = 31;

	/**
	 * non-printable CBM characters.
	 */
	public static final byte[] BADCHARS = new byte[] { (byte) '\\', (byte) '/',
			(byte) ':', (byte) '*', (byte) '?', (byte) '"', (byte) '<',
			(byte) '>', (byte) '|' };

	/**
	 * Read the file and get a state object.
	 * 
	 * @param file
	 *            the file
	 * @return A new state object
	 * @throws IOException
	 *             file not found/not readable
	 */
	public abstract Object readImage(final File file) throws IOException;

	/**
	 * Store the file contents in a file (destPath/destName).
	 * 
	 * @param info
	 *            the state object returned by readFile() method call.
	 * @param destPath
	 *            the path to the file to create.
	 * @param destName
	 *            the name of the file to create.
	 * @return The file created with no errors
	 */
	public abstract boolean getFileContents(final Object info,
			final String destPath, final String destName);

	/**
	 * Get the next file entry of the image.
	 * 
	 * @param info
	 *            the state returned by readFile() method call
	 * @param headDt
	 *            the HeaderData object to fill
	 * @return an entry was found?
	 */
	public abstract boolean getFile(final Object info, final HeaderData headDt);

	/**
	 * Convert a CBM string to ASCII.
	 * 
	 * @param str
	 *            the string to convert
	 */
	public final void convertCBMString(final byte[] str) {
		int x;
		byte c;

		for (x = 0; x < str.length; x++) {
			c = str[x];
			if (c < 0) { // 128-255 -> 0-127 (invers ignorieren)
				c += CBM_CHAR_INVERS;
			}
			if (c == '\0') {
				break;
			}
			if (c >= 'A' && c <= 'Z') { // 65-90 -> 97-122 (A -> a)
				c = (byte) (c - (byte) 'A' + (byte) 'a');
			} else if (c >= 1 && c <= CBM_CHAR_Z) { // 1-26 -> 97-122 (^A -> a)
				c = (byte) (c - 1 + (byte) 'a');
			}

			if (c == CBM_CHAR_ECKAUF) { // 27 -> 90 ([ -> [)
				c = '[';
			}
			if (c == CBM_CHAR_ECKZU) { // 29 -> 93 (] -> ])
				c = ']';
			}

			if (c == CBM_CHAR_SOND1 || c == CBM_CHAR_SOND2
					|| c == CBM_CHAR_SOND3) { // 28, 30, 31 -> _
				c = (byte)'_';
			}

			// bad char ?
			for (int i = 0; i < BADCHARS.length; i++) {
				 // "\\/:*?\"<>|"
				if (BADCHARS[i]==c) {
					str[x] = (byte)'_'; // yes, replace
				} else {
					str[x] = c; // no, take it
				}
			}
		}
	}

	/**
	 * Append the file extension according to this file type.
	 * 
	 * @param fileName
	 *            the filename to append
	 * @param fileType
	 *            the file type of this tape image file
	 * @return the appended filename
	 */
	public final String convertFilename(final byte[] fileName,
			final int fileType) {
		String fn = new String(fileName);
		int ft = fileType & BITMASK_FILETYPE;
		// append extension if applicable
		if (ft >= FILETYPE_DEL && ft <= FILETYPE_REL) {
			fn += FILETYPES[ft - FILETYPE_DEL];
		}
		return fn;
	}

	/**
	 * Get the filename without path from Total commander path/name.
	 * 
	 * @param destPath
	 *            destination path or null, if the path is part of destName
	 * @param destName
	 *            destination name or absolute path name, if destPath is null
	 * @return the filename without the path
	 */
	public final String getName(final String destPath, final String destName) {
		String myFile;
		if (destPath == null) {
			int i = destName.lastIndexOf('\\');
			if (i != -1) {
				myFile = destName.substring(i + 1);
			} else {
				myFile = destName;
			}
		} else {
			myFile = destName;
		}
		return myFile;
	}

	/**
	 * @param file
	 * @return
	 */
	public abstract Object createImage(File file);

	/**
	 * @param fileImage
	 * @param packedFile
	 * @param subPath
	 * @param srcPath
	 * @param addList
	 * @param flags
	 * @return
	 */
	public abstract boolean updateImage(Object fileImage, String packedFile,
			String subPath, String srcPath, String addList, int flags);

}
