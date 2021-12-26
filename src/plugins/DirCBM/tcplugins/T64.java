/**
 * 
 */
package tcplugins;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wcx.HeaderData;

/**
 * @author Ken
 * 
 */
public class T64 extends IFileImage {

	/** Object for trace-/debug messages. */
	private static Log log = LogFactory.getLog(T64.class);

	/**
	 * The bit mask to get the high byte of a WORD.
	 */
	private static final int BITMASK_HI_BYTE = 0xff00;

	/**
	 * The bit mask to get the low byte of a WORD.
	 */
	private static final int BITMASK_LOW_BYTE = 0xff;

	/**
	 * @author Ken Header of T64 images.
	 */
	private class TapeHeader {
		/**
		 * the size of the tape header: description.
		 */
		public static final int TAPE_DESC_SIZE = 32;

		/**
		 * the size of the tape header: user description.
		 */
		public static final int USER_DESC_SIZE = 24;

		/**
		 * The first 32 bytes ($000000-00001F) represent the signature of the
		 * file, telling us it is a tape image file for C64S. Note that it is
		 * padded with $00 to make the signature 32 bytes long.
		 */
		private byte[] tapeDescr = new byte[TAPE_DESC_SIZE];

		/**
		 * $20-21: Tape version number of either $0100 or $0101. I am not sure
		 * what differences exist between versions.
		 */
		private byte[] version = new byte[2];

		/**
		 * 22-23: Maximum number of entries in the directory, stored in low/high
		 * byte order (in this case $0190 = 400 total).
		 */
		private char maxFiles;

		/**
		 * 24-25: Total number of used entries, once again in low/high byte.
		 * Used = $0005 = 5 entries.
		 */
		private char currFiles;

		/**
		 * 26-27: Not used.
		 */
		private char reserved;

		/**
		 * 28-3F: Tape image name, 24 characters, padded with $20 (space).
		 */
		private byte[] userDescr = new byte[USER_DESC_SIZE];
	};

	/**
	 * @author Ken Entry of T64 images.
	 */
	private class TapeEntry {
		/**
		 * The maximum filename size of a tape image file.
		 */
		public static final int FILENAME_SIZE = 16;

		/**
		 * $40: C64s filetype 0 = free (usually) 1 = Normal tape file 3 = Memory
		 * Snapshot, v .9, uncompressed 2-255 = Reserved (for memory snapshots).
		 */
		private byte entryUsed;

		/**
		 * 41: 1541 file type (0x82 for PRG, 0x81 for SEQ, etc). You will find
		 * it can vary between 0x01, 0x44, and the normal D64 values. In reality
		 * any value that is not a $00 is seen as a PRG file. When this value is
		 * a $00 (and the previous byte at $40 is >1), then the file is a
		 * special T64 "FRZ" (frozen) C64s session snapshot.
		 */
		private byte fileType;

		/**
		 * 42-43: Start address (or Load address). This is the first two bytes
		 * of the C64 file which is usually the load address (typically $01
		 * $08). If the file is a snapshot, the address will be 0.
		 */
		private char startAddr;

		/**
		 * 44-45: End address (actual end address in memory, if the file was
		 * loaded into a C64). If the file is a snapshot, then the address will
		 * be a 0.
		 */
		private char endAddr;

		/**
		 * 46-47: Not used.
		 */
		private byte[] reservedA = new byte[2];

		/**
		 * 48-4B: Offset into the image file (from the beginning) of where the
		 * C64 file starts (stored as low/high byte).
		 */
		private int tapePos;

		/**
		 * 4C-4F: Not used.
		 */
		private byte[] reservedB = new byte[2 + 2];

		/**
		 * 50-5F: C64 filename (in PETASCII, padded with $20, not $A0).
		 */
		private byte[] cbmFileName = new byte[FILENAME_SIZE];

		/**
		 * The actual file data.
		 */
		private byte[] data;
	};

	/**
	 * @author Ken The tape file entries.
	 * 
	 */
	private class TapeState {
		/**
		 * The tape header.
		 */
		private TapeHeader header;

		/**
		 * The tape file entries.
		 */
		private TapeEntry[] entry;

		/**
		 * the current file slot.
		 */
		private int currFileSlot;

		/**
		 * The tape size.
		 */
		private long tapeSize;
	};

	/**
	 * Read the file and get a TapeState object.
	 * 
	 * @param file
	 *            the T64 file
	 * @return A new TapeState object
	 * @throws IOException
	 *             file not found/not readable
	 */
	public final Object readImage(final File file) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("DirCBM.openArchive(ArchiveData)" + "image type = T64\n");
		}
		TapeState tapeInfo = new TapeState();
		tapeInfo.currFileSlot = 0;
		tapeInfo.tapeSize = file.length();
		if (log.isDebugEnabled()) {
			log.debug("DirCBM.openArchive(ArchiveData)"
					+ "tapeInfo.tapeSize = " + tapeInfo.tapeSize);
		}

		DataInputStream di = new DataInputStream(new FileInputStream(file));

		tapeInfo.header = new TapeHeader();
		di.read(tapeInfo.header.tapeDescr, 0, TapeHeader.TAPE_DESC_SIZE);
		di.read(tapeInfo.header.version, 0, 2);
		tapeInfo.header.maxFiles = (char) readChar(di);
		tapeInfo.header.currFiles = (char) readChar(di);
		tapeInfo.header.reserved = (char) readChar(di);
		di.read(tapeInfo.header.userDescr, 0, TapeHeader.USER_DESC_SIZE);
		if (log.isDebugEnabled()) {
			log.debug("DirCBM.openArchive() Header.TapeDescr="
					+ printBytes(tapeInfo.header.tapeDescr));
			log.debug("DirCBM.openArchive() Header.Version="
					+ printBytes(tapeInfo.header.version));
			log.debug("DirCBM.openArchive() Header.MaxFiles="
					+ printShort((short) tapeInfo.header.maxFiles));
			log.debug("DirCBM.openArchive() Header.CurrFiles="
					+ printShort((short) tapeInfo.header.currFiles));
			log.debug("DirCBM.openArchive() Header.Reserved="
					+ printShort((short) tapeInfo.header.reserved));
			log.debug("DirCBM.openArchive() Header.UserDescr="
					+ printBytes(tapeInfo.header.userDescr));
		}

		tapeInfo.entry = new TapeEntry[tapeInfo.header.maxFiles];
		for (int i = 0; i < tapeInfo.header.maxFiles; i++) {
			tapeInfo.entry[i] = new TapeEntry();
			tapeInfo.entry[i].entryUsed = di.readByte();
			tapeInfo.entry[i].fileType = di.readByte();
			tapeInfo.entry[i].startAddr = readChar(di);
			tapeInfo.entry[i].endAddr = readChar(di);
			di.read(tapeInfo.entry[i].reservedA, 0, 2);
			tapeInfo.entry[i].tapePos = di.readInt();
			di.read(tapeInfo.entry[i].reservedB, 0, 2 + 2);
			di.read(tapeInfo.entry[i].cbmFileName, 0, TapeEntry.FILENAME_SIZE);
			convertCBMString(tapeInfo.entry[i].cbmFileName);
			if (log.isDebugEnabled()) {
				log.debug("DirCBM.openArchive() Entry[" + i + "].EntryUsed="
						+ tapeInfo.entry[i].entryUsed);
				log.debug("DirCBM.openArchive() Entry[" + i + "].FileType="
						+ tapeInfo.entry[i].fileType);
				log.debug("DirCBM.openArchive() Entry[" + i + "].StartAddr="
						+ printChar(tapeInfo.entry[i].startAddr));
				log.debug("DirCBM.openArchive() Entry[" + i + "].EndAddr="
						+ printChar(tapeInfo.entry[i].endAddr));
				log.debug("DirCBM.openArchive() Entry[" + i + "].ReservedA="
						+ printBytes(tapeInfo.entry[i].reservedA));
				log.debug("DirCBM.openArchive() Entry[" + i + "].TapePos="
						+ printInt(tapeInfo.entry[i].tapePos));
				log.debug("DirCBM.openArchive() Entry[" + i + "].ReservedB="
						+ printBytes(tapeInfo.entry[i].reservedB));
				log.debug("DirCBM.openArchive() Entry[" + i + "].FileName="
						+ printBytes(tapeInfo.entry[i].cbmFileName));
			}
		}
		for (int i = 0; i < tapeInfo.header.maxFiles; i++) {

			int size = (int) tapeInfo.entry[i].endAddr
					- (int) tapeInfo.entry[i].startAddr;
			if (log.isDebugEnabled()) {
				log.debug("DirCBM.openArchive() size=" + size);
			}
			tapeInfo.entry[i].data = new byte[size];
			di.read(tapeInfo.entry[i].data, 0, size);
			if (log.isDebugEnabled()) {
				log.debug("DirCBM.openArchive() data="
						+ printBytes(tapeInfo.entry[i].data));
			}
		}

		// close file
		di.close();
		return tapeInfo;
	}

	/**
	 * Get printable version of the start address.
	 * 
	 * @param startAddr
	 *            the start address.
	 * @return the printable version
	 */
	private String printChar(final char startAddr) {
		return String.valueOf((int) startAddr);
	}

	/**
	 * Read lo/high byte of the stream.
	 * 
	 * @param di
	 *            the stream
	 * @return the WORD (lo/high byte)
	 * @throws IOException
	 *             read error
	 */
	private char readChar(final DataInputStream di) throws IOException {
		byte lo = di.readByte();
		byte hi = di.readByte();
		return new Character((char) (lo + (hi << BITS_PER_BYTE))).charValue();
	}

	/**
	 * Get printable version of the tape position.
	 * 
	 * @param tapePos
	 *            the tape position.
	 * @return the printable version
	 */
	private String printInt(final int tapePos) {
		return String.valueOf(tapePos);
	}

	/**
	 * Get printable version of the maximum count of files.
	 * 
	 * @param maxFiles
	 *            the maximum count of files.
	 * @return the printable version
	 */
	private String printShort(final short maxFiles) {
		return String.valueOf(maxFiles);
	}

	/**
	 * Get printable version of the tape description.
	 * 
	 * @param tapeDescr
	 *            the tape description.
	 * @return the printable version
	 */
	private String printBytes(final byte[] tapeDescr) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < tapeDescr.length; i++) {
			buf.append((char) tapeDescr[i]);
		}
		return buf.toString();
	}

	/**
	 * Store the file contents in a file (destPath/destName).
	 * 
	 * @param info
	 *            the tape state object returned by readFile() method call.
	 * @param destPath
	 *            the path to the file to create.
	 * @param destName
	 *            the name of the file to create.
	 * @return The file created with no errors
	 */
	public final boolean getFileContents(final Object info,
			final String destPath, final String destName) {
		TapeState tapeInfo = (TapeState) info;
		
		// format file name
		String myFile = getName(destPath, destName);
		
		// tape images
		// later with hashing
		TapeEntry pTapeFile = null;
		for (int i = 0; i < tapeInfo.header.maxFiles; i++) {
			String currFile = convertFilename(tapeInfo.entry[i].cbmFileName,
					FILETYPE_PRG);
			if (log.isDebugEnabled()) {
				log.debug("DirCBM.searchTapeEntry()" + "currFile=" + currFile);
			}
			if (myFile.equals(currFile)) {
				pTapeFile = tapeInfo.entry[i]; // found
			}
		}

		if (pTapeFile != null) {
			DataOutputStream dout;
			try {
				dout = new DataOutputStream(new FileOutputStream(new File(
						destName)));
				dout.writeByte(pTapeFile.startAddr & BITMASK_LOW_BYTE);
				int hiBytePart = pTapeFile.startAddr & BITMASK_HI_BYTE;
				dout.writeByte((hiBytePart) >> BITS_PER_BYTE);
				dout.write(pTapeFile.data);
				dout.close();
			} catch (FileNotFoundException e) {
				log.error(e.getMessage(), e);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}

			return true; // success
		}
		return false;
	}

	/**
	 * Get the next file entry of the tape image.
	 * 
	 * @param info
	 *            the tape state returned by readFile() method call
	 * @param headDt
	 *            the HeaderData object to fill
	 * @return an entry was found?
	 */
	public final boolean getFile(final Object info, final HeaderData headDt) {
		TapeState tapeInfo = (TapeState) info;

		while (tapeInfo.currFileSlot < tapeInfo.header.maxFiles) {
			TapeEntry pCurrTapeEntry = tapeInfo.entry[tapeInfo.currFileSlot];
			if (pCurrTapeEntry.entryUsed == 1) {
				// normal file, no snapshot
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.readHeader()"
							+ "file found in tape slot "
							+ tapeInfo.currFileSlot);
				}

				if (log.isDebugEnabled()) {
					log.debug("DirCBM.readHeader()" + "pInfo.ArcName="
							+ headDt.getArcName());
				}
				String filenameExt = convertFilename(
						pCurrTapeEntry.cbmFileName, FILETYPE_PRG);
				headDt.setFileName(filenameExt);
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.readHeader()" + "filenameExt="
							+ headDt.getFileName());
				}
				headDt.setPackSize(0);
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.readHeader()" + "packSize="
							+ headDt.getPackSize());
				}
				headDt.setUnpSize((int) pCurrTapeEntry.endAddr
						- (int) pCurrTapeEntry.startAddr);
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.readHeader()" + "unpSize="
							+ headDt.getUnpSize());
				}
				headDt.setFileAttr(0);
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.readHeader()" + "fileAttr="
							+ headDt.getFileAttr());
				}
				headDt.setFileTime(Calendar.getInstance());
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.readHeader()" + "fileTime="
							+ headDt.getFileTime());
				}
				// set next slot to search next time
				tapeInfo.currFileSlot++;

				// file found
				return true;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.readHeader()" + "empty tape slot "
							+ tapeInfo.currFileSlot);
				}
			}
			// advance search to next slot
			tapeInfo.currFileSlot++;
		}

		// no more files on tape
		if (log.isDebugEnabled()) {
			log.debug("DirCBM.readHeader()+"
					+ "ReadHeader exit E_END_ARCHIVE\n");
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object createImage(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean updateImage(Object fileImage, String packedFile, String subPath, String srcPath, String addList, int flags) {
		// TODO Auto-generated method stub
		return false;
	}

}
