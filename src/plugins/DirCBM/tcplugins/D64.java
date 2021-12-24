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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wcx.HeaderData;

/**
 * @author Ken
 * 
 */
public class D64 extends IFileImage {

	/**
	 * IMAGE_SIZE.
	 */
	private static final int IMAGE_SIZE = 174848;

	/** Object for trace-/debug messages. */
	private static Log log = LogFactory.getLog(D64.class);

	/**
	 * Absolute max nr of sectors per track for supported disk formats.
	 */
	public static final int MAX_SECTORS_PER_TRACK = 30;

	/**
	 * Absolute max nr of tracks of supported disk formats.
	 */
	public static final int MAX_TRACKS = 160;

	/**
	 * Nr of tracks * max sectors per track.
	 */
	public static final int MAX_OVERALL_SECTORS = MAX_SECTORS_PER_TRACK
			* MAX_TRACKS;

	/**
	 * A sectors size in bytes.
	 */
	public static final int SECTOR_SIZE = 256;

	/**
	 * Track count for 35 track disks.
	 */
	public static final int IMAGE_35_TRACKS = 35;

	/**
	 * Track count for 40 track disks.
	 */
	public static final int IMAGE_40_TRACKS = 40;

	/**
	 * Track of the first directory entry.
	 */
	public static final int FIRST_DIR_TRACK = 18;

	/**
	 * Sector of the first directory entry.
	 */
	public static final int FIRST_DIR_SECTOR = 1;

	/**
	 * Track of the BAM.
	 */
	public static final int BAM_TRACK = 18;

	/**
	 * Sector of the BAM.
	 */
	public static final int BAM_SECTOR = 0;

	/**
	 * Number of directory entries per block.
	 */
	private static final int NR_ENTRIES_PER_BLOCK = 7;

	/**
	 * Size of the error infos of a 35 tracks disk.
	 */
	private static final int TRACKS_35_ERROR_BYTES_SIZE = 683;

	/**
	 * Size of the Disk: 35 tracks WITHOUT error bytes.
	 */
	private static final int TRACKS_35_NO_ERRORS = 683 * SECTOR_SIZE;

	/**
	 * Size of the Disk: 35 tracks WITH error bytes.
	 */
	private static final int TRACKS_35_WITH_ERRORS = TRACKS_35_NO_ERRORS
			+ TRACKS_35_ERROR_BYTES_SIZE;

	/**
	 * Size of the error infos of a 40 tracks disk.
	 */
	private static final int TRACKS_40_ERROR_BYTES_SIZE = 768;

	/**
	 * Size of the Disk: 40 tracks WITHOUT error bytes.
	 */
	private static final int TRACKS_40_NO_ERRORS = 768 * SECTOR_SIZE;

	/**
	 * Size of the Disk: 40 tracks WITH error bytes.
	 */
	private static final int TRACKS_40_WITH_ERRORS = TRACKS_40_NO_ERRORS
			+ TRACKS_40_ERROR_BYTES_SIZE;

	/**
	 * Index of disk blocks for array sectorAddresses.
	 */
	public static final int BLOCK_INFO = 0;

	/**
	 * Index of addresses of disk blocks for array sectorAddresses.
	 */
	public static final int ADDR_INFO = 1;

	/**
	 * All sector adresses of D64 disks (track, disk image offset).
	 */
	protected static final int[][] SECTORADRESSES_D64 = new int[][] {
			{ 21, 0x00000}, { 21, 0x01500}, { 21, 0x02A00}, { 21, 0x03F00},
			{ 21, 0x05400}, { 21, 0x06900}, { 21, 0x07E00}, { 21, 0x09300},
			{ 21, 0x0A800}, { 21, 0x0BD00}, { 21, 0x0D200}, { 21, 0x0E700},
			{ 21, 0x0FC00}, { 21, 0x11100}, { 21, 0x12600}, { 21, 0x13B00},
			{ 21, 0x15000}, { 19, 0x16500}, { 19, 0x17800}, { 19, 0x18B00},
			{ 19, 0x19E00}, { 19, 0x1B100}, { 19, 0x1C400}, { 19, 0x1D700},
			{ 18, 0x1EA00}, { 18, 0x1FC00}, { 18, 0x20E00}, { 18, 0x22000},
			{ 18, 0x23200}, { 18, 0x24400}, { 17, 0x25600}, { 17, 0x26700},
			{ 17, 0x27800}, { 17, 0x28900}, { 17, 0x29A00}, { 17, 0x2AB00},
			{ 17, 0x2BC00}, { 17, 0x2CD00}, { 17, 0x2DE00}, { 17, 0x2EF00}};

	/**
	 * Disk error code OK. Self explanatory. No errors were detected in the
	 * reading and decoding of the sector.
	 */
	public static final int OK = 0x01;

	/**
	 * Disk error code 20. Header descriptor byte not found ($08). Each sector
	 * is preceeded by an 8-byte header block, which starts with the value $08.
	 * If this value is not $08, this error is generated.
	 */
	public static final int ERROR_20 = 0x02;

	/**
	 * Disk error code 21. No sync sequence found. Each sector data block and
	 * header block are preceeded by SYNC marks. If NO sync sequence is found,
	 * then the whole track is unreadable, and likely unformatted.
	 */
	public static final int ERROR_21 = 0x03;

	/**
	 * Disk error code 22. Data descriptor byte not found ($07). Each sector
	 * data block is preceeded by the value $07, the "data block" descriptor. If
	 * this value is not there, this error is generated. Each encoded sector has
	 * actually 260 bytes. First is the descriptor byte, then follows the 256
	 * bytes of data, a checksum, and two "off" bytes.
	 */
	public static final int ERROR_22 = 0x04;

	/**
	 * Disk error code 23. Checksum error in data block. The checksum of the
	 * data read of the disk is calculated, and compared against the one stored
	 * at the end of the sector. If there's a discrepancy, this error is
	 * generated.
	 */
	public static final int ERROR_23 = 0x05;

	/**
	 * Disk error code 24. Write verify (on format).
	 */
	public static final int ERROR_24 = 0x06;

	/**
	 * Disk error code 25. Write verify error. Once the GCR-encoded sector is
	 * written out, the drive waits for the sector to come around again and
	 * verifies the whole 325-byte GCR block. Any errors encountered will
	 * generate this error.
	 */
	public static final int ERROR_25 = 0x07;

	/**
	 * Disk error code 26. Write protect on. Self explanatory. Remove the
	 * write-protect tab, and try again.
	 */
	public static final int ERROR_26 = 0x08;

	/**
	 * Disk error code 27. Checksum error in header block. The 8-byte header
	 * block contains a checksum value, calculated by XOR'ing the TRACK, SECTOR,
	 * ID1 and ID2 values. If this checksum is wrong, this error is generated.
	 * 
	 */
	public static final int ERROR_27 = 0x09;

	/**
	 * Disk error code 28. Write error. In actual fact, this error never occurs,
	 * but it is included for completeness.
	 * 
	 */
	public static final int ERROR_28 = 0x0A;

	/**
	 * Disk error code 29. Disk sector ID mismatch. The ID's from the header
	 * block of the currently read sector are compared against the ones from the
	 * header of 18/0. If there is a mismatch, this error is generated.
	 * 
	 */
	public static final int ERROR_29 = 0x0B;

	/**
	 * Disk error code 74. Drive Not Ready (no disk in drive or no device 1).
	 */
	public static final int ERROR_74 = 0x0F;

	/**
	 * Error messages for disk errors.
	 */
	private static final String[] ERRORTEXT = { "", // not used
			"00, No error, Sektor ok.", "20, Header block not found", // Read
			"21, No sync character", // Seek
			"22, Data block not present", // Read
			"23, Checksum error in data block", // Read
			"24, Write verify (on format)", // Write
			"25, Write verify error", // Write
			"26, Write protect on", // Write
			"27, Checksum error in header block", // Seek
			"28, Write error", // Write
			"29, Disk ID mismatch", // Seek
			"74, Disk Not Ready (no device 1)" // Read
	};

	/**
	 * @author Ken Stores all infos of a disk.
	 * 
	 */
	private class DiskState {

		/**
		 * MAX_SECTORS_IN_DIR_TRACK.
		 */
		public static final int MAX_SECTORS_IN_DIR_TRACK = 100;

		/**
		 * Disk infos: track/sector/entry.
		 */
		private byte nextTrack, nextSector, nextEntry;

		/**
		 * Disk infos: actual values of track/sector.
		 */
		private byte currNextTrack, currNextSector;

		/**
		 * All files of the directory.
		 */
		private ArrayList pFiles = new ArrayList();

		/**
		 * Number of files of the directory.
		 */
		private int fileCounter;

		/**
		 * Number of tracks of this disk.
		 */
		private byte nrOfTracks;

		/**
		 * First run of this disk.
		 */
		private boolean firstRun;

		/**
		 * Lookup table for cycles in the sector chaining.
		 */
		private int[] arrCyclicAccessInfo = new int[MAX_SECTORS_IN_DIR_TRACK];

		/**
		 * First track/sector of the directory.
		 */
		private byte firstDirTrack, firstDirSector;

		/**
		 * First track/sector of the BAM (block availability map).
		 */
		private byte firstBAMTrack, firstBAMSector;

		/**
		 * The sector adresses of all disks (track, disk image offset).
		 */
		private int[][] pSectorAdresses;

		/**
		 * Disk image contains error information.
		 */
		private boolean errorInformation;

		/**
		 * The error infomration of the disk image (768 or 683 bytes).
		 */
		private byte[] errorData;

		/**
		 * The size of the disk image.
		 */
		private long diskSize;

		/**
		 * All bytes of the disk image file.
		 */
		private byte[] diskData;
	};

	/**
	 * @author Ken Stores all infos of a directory entry.
	 * 
	 */
	private class DirEntry {
		/**
		 * Byte array index for directory track.
		 */
		private static final int IDX_DIR_TRACK = 0;

		/**
		 * Byte array index for directory sector.
		 */
		private static final int IDX_DIR_SECTOR = 1;

		/**
		 * Byte array index for file type.
		 */
		private static final int IDX_FILETYPE = 2;

		/**
		 * Byte array index for file track.
		 */
		private static final int IDX_FILE_TRACK = 3;

		/**
		 * Byte array index for file sector.
		 */
		private static final int IDX_FILE_SEKTOR = 4;

		/**
		 * Byte array index for file name.
		 */
		private static final int IDX_FILE_NAME = 5;

		/**
		 * Byte array index for unused fields.
		 */
		private static final int IDX_UNUSED = 21;

		/**
		 * Byte array index for the total number of sectors (low byte).
		 */
		private static final int IDX_NR_SECTORS_L = 30;

		/**
		 * Byte array index for the total number of sectors (high byte).
		 */
		private static final int IDX_NR_SECTORS_H = 31;

		/**
		 * Size in bytes of a directory entry.
		 */
		public static final int DIR_ENTRY_SIZE = 32;

		/**
		 * Size of a filename entry.
		 */
		private static final int FILE_NAME_SIZE = 16;

		/**
		 * Size of unused fields.
		 */
		private static final int UNUSED_SIZE = 9;

		/**
		 * Create a new directory entry of DIR_ENTRY_SIZE bytes.
		 * 
		 * @param entryBytes
		 *            the byte contents of a directory entry
		 */
		public DirEntry(final byte[] entryBytes) {
			nextDirTrack = entryBytes[IDX_DIR_TRACK];
			nextDirSector = entryBytes[IDX_DIR_SECTOR];
			fileType = entryBytes[IDX_FILETYPE];
			firstFileTrack = entryBytes[IDX_FILE_TRACK];
			firstFileSector = entryBytes[IDX_FILE_SEKTOR];
			System.arraycopy(entryBytes, IDX_FILE_NAME, filename, 0,
					FILE_NAME_SIZE);
			System.arraycopy(entryBytes, IDX_UNUSED, unused, 0, UNUSED_SIZE);
			byte lowByte = entryBytes[IDX_NR_SECTORS_L];
			byte highByte = entryBytes[IDX_NR_SECTORS_H];
			nrSectors = (char) (lowByte + (highByte << BITS_PER_BYTE));
		}

		/**
		 * Next directory track/sector.
		 */
		private byte nextDirTrack, nextDirSector;

		/**
		 * The file type.
		 */
		private byte fileType;

		/**
		 * The first track/sector of the file.
		 */
		private byte firstFileTrack, firstFileSector;

		/**
		 * The filename of the file.
		 */
		private byte[] filename = new byte[FILE_NAME_SIZE];

		/**
		 * Unused bytes.
		 */
		private byte[] unused = new byte[UNUSED_SIZE];

		/**
		 * The total number of sektors.
		 */
		private char nrSectors;
	};

	private class BamInfoTrack {
		/**
		 * @param diskData
		 * @param addr
		 */
		public BamInfoTrack(byte[] diskData, int addr) {
			sectorsLeft = diskData[addr];
			System.arraycopy(diskData, addr + 1, BAMOfTrack, 0, 3);
			if (log.isDebugEnabled()) {
				log.debug("BamInfoTrack.BamInfoTrack() " + "sectorsLeft="
						+ sectorsLeft + ", BAMOfTrack="
						+ new String(BAMOfTrack));
			}
		}

		byte sectorsLeft;

		byte[] BAMOfTrack = new byte[3];
	};

	private class Bam1541 {
		/**
		 * @param addr
		 */
		public Bam1541(byte[] diskData, int addr) {
			nextDirTrack = diskData[addr];
			nextDirSector = diskData[addr + 1];
			formatID = diskData[addr + 2];
			reserved1 = diskData[addr + 3];
			if (log.isDebugEnabled()) {
				log.debug("Bam1541.Bam1541() " + "nextDirTrack=" + nextDirTrack
						+ ", nextDirSector=" + nextDirSector);
				log.debug("Bam1541.Bam1541() " + "formatID=" + formatID
						+ ", reserved1=" + reserved1);
			}

			for (int i = 0; i < IMAGE_35_TRACKS; i++) {
				BAM_Data[i] = new BamInfoTrack(diskData, addr + 4 + (i * 4));
			}
			int afterBamInfos = (4 + IMAGE_35_TRACKS * 4);
			System.arraycopy(diskData, afterBamInfos, diskName, 0, 18);
			System.arraycopy(diskData, afterBamInfos + 18, diskID, 0, 2);
			fillByte1 = diskData[afterBamInfos + 18 + 2];
			System.arraycopy(diskData, afterBamInfos + 18 + 2 + 1,
					formatIDName, 0, 2);
			System.arraycopy(diskData, afterBamInfos + 18 + 2 + 1 + 2,
					fillBytes, 0, 4);
			System.arraycopy(diskData, afterBamInfos + 18 + 2 + 1 + 2 + 4,
					reserved2, 0, 85);
			if (log.isDebugEnabled()) {
				log.debug("Bam1541.Bam1541() " + "diskName="
						+ new String(diskName) + ", diskID="
						+ new String(diskID));
				log.debug("Bam1541.Bam1541() " + "formatIDName="
						+ new String(formatIDName));
			}
		}

		byte nextDirTrack, nextDirSector;

		byte formatID;

		byte reserved1;

		BamInfoTrack[] BAM_Data = new BamInfoTrack[35];

		byte[] diskName = new byte[18];

		byte[] diskID = new byte[2];

		byte fillByte1;

		byte[] formatIDName = new byte[2];

		byte[] fillBytes = new byte[4];

		byte[] reserved2 = new byte[85];
	};

	/**
	 * {@inheritDoc}
	 */
	public final boolean getFile(final Object info, final HeaderData headDt) {
		DiskState diskInfo = (DiskState) info;

		// disk images
		byte[] currSector = new byte[SECTOR_SIZE];
		while (true) {
			// determine next block
			if (diskInfo.firstRun) {
				// read first BAM block
				diskInfo.nextTrack = diskInfo.firstDirTrack;
				diskInfo.nextSector = diskInfo.firstDirSector;
				diskInfo.nextEntry = 0;

				if (log.isDebugEnabled()) {
					log.debug("D64.getFile()" + "firstSector "
							+ diskInfo.nextTrack + ":" + diskInfo.nextSector
							+ "-" + diskInfo.nextEntry);
				}

				diskInfo.firstRun = false;

				// setup cyclic sector access check memory
				for (int i = 0; i < diskInfo.arrCyclicAccessInfo.length; i++) {
					diskInfo.arrCyclicAccessInfo[i] = 0;
				}
			} else {
				// not first block
				if (diskInfo.nextEntry < NR_ENTRIES_PER_BLOCK) {
					diskInfo.nextEntry++; // same block, next entry
				} else {
					// switch block or finish
					if (diskInfo.currNextTrack != 0) {
						diskInfo.nextTrack = diskInfo.currNextTrack;
						diskInfo.nextSector = diskInfo.currNextSector;
						diskInfo.nextEntry = 0;
					} else { // end of BAM chain
						if (log.isDebugEnabled()) {
							log.debug("D64.getFile()"
									+ "ReadHeader exit E_END_ARCHIVE\n");
						}
						return false;
					}
				}
			}

			// read selected sector
			if (log.isDebugEnabled()) {
				log.debug("D64.getFile()" + "currSector " + diskInfo.nextTrack
						+ ":" + diskInfo.nextSector + "-" + diskInfo.nextEntry);
			}
			if (!getDiskSector(diskInfo, diskInfo.nextTrack,
					diskInfo.nextSector, currSector)) {

				if (log.isErrorEnabled()) {
					log.error("D64.getFile()"
							+ "error ReadHeader GetDiskSector "
							+ diskInfo.nextTrack + ":" + diskInfo.nextSector);
				}
				return false;
			}

			// cyclic check and enter sector index info (but only if first dir
			// entry)
			if (diskInfo.nextEntry == 0 && diskInfo.currNextTrack != 0
					&& diskInfo.arrCyclicAccessInfo[diskInfo.nextSector] != 0) {
				if (log.isErrorEnabled()) {
					log.error("D64.getFile()" + "error cyclic access for "
							+ diskInfo.nextTrack + ":" + diskInfo.nextSector);
				}

				return false;
			} else {
				diskInfo.arrCyclicAccessInfo[diskInfo.nextSector]++;
				// mark sector as read
			}

			// store current next tracks/sector entry
			diskInfo.currNextTrack = currSector[0];
			diskInfo.currNextSector = currSector[1];

			byte[] entryBytes = new byte[DirEntry.DIR_ENTRY_SIZE];
			// read next file entry
			System.arraycopy(currSector, diskInfo.nextEntry
					* DirEntry.DIR_ENTRY_SIZE, entryBytes, 0,
					DirEntry.DIR_ENTRY_SIZE);
			log.debug("D64.getFile() pCurrDirEntry.fileType = "
					+ currSector[diskInfo.nextEntry * DirEntry.DIR_ENTRY_SIZE
							+ 2]);
			DirEntry pCurrDirEntry = new DirEntry(entryBytes);
			if (pCurrDirEntry.fileType != FILETYPE_DEL) {
				convertCBMString(pCurrDirEntry.filename);

				// store dir entry in master array
				++diskInfo.fileCounter;
				diskInfo.pFiles.add(pCurrDirEntry);

				headDt.setFileName(new String(convertFilename(
						pCurrDirEntry.filename, pCurrDirEntry.fileType)));
				headDt.setPackSize(0);
				headDt.setUnpSize(pCurrDirEntry.nrSectors);
				headDt.setFileAttr(0);
				headDt.setFileTime(Calendar.getInstance());

				if (log.isDebugEnabled()) {
					log.debug("D64.getFile() filename="
							+ new String(convertFilename(
									pCurrDirEntry.filename, pCurrDirEntry.fileType)));
				}

				break; // leave loop
			} else {
				if (log.isDebugEnabled()) {
					log.debug("D64.getFile() scratched file at "
							+ diskInfo.nextTrack + ":" + diskInfo.nextSector
							+ "-" + diskInfo.nextEntry);
				}
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean getFileContents(final Object info,
			final String destPath, final String destName) {
		DiskState diskInfo = (DiskState) info;
		// search disk

		DirEntry pFile = searchDirEntry(diskInfo, destPath, destName);
		if (pFile != null) {
			if (log.isDebugEnabled()) {
				log.debug("D64.getFileContents() EXTRACT found <"
						+ new String(pFile.filename) + "> " + pFile.nrSectors);
			}
			DataOutputStream dout;
			try {
				dout = new DataOutputStream(new FileOutputStream(new File(
						destName)));
				if (log.isDebugEnabled()) {
					log.debug("D64.getFileContents(info, destPath, destName) destName="+destName);
				}
				byte[] currSector = new byte[SECTOR_SIZE];
				byte nextTrack, nextSector;
				int offset;

				int[] arrCyclicAccessInfo = new int[MAX_OVERALL_SECTORS];
				for (int i = 0; i < arrCyclicAccessInfo.length; i++) {
					arrCyclicAccessInfo[i] = 0;
				}
				nextTrack = pFile.firstFileTrack;
				nextSector = pFile.firstFileSector;
				if (log.isDebugEnabled()) {
					log.debug("D64.getFileContents() pFile.firstFileTrack="
							+ pFile.firstFileTrack + ", pFile.firstFileSector"
							+ pFile.firstFileSector);

				}
				while (true) {
					if (!getDiskSector(diskInfo, nextTrack, nextSector,
							currSector)) {
						if (log.isErrorEnabled()) {
							log.error("D64.getFileContents()"
									+ "error Extract GetDiskSector "
									+ nextTrack + ":" + nextSector);
						}
						dout.close();
						return false;
					}

					// check if cyclic sector chain
					offset = nextTrack * MAX_SECTORS_PER_TRACK + nextSector;
					if (arrCyclicAccessInfo[offset] != 0) {
						if (log.isErrorEnabled()) {
							log.error("D64.getFileContents()"
									+ "error cyclic access for " + nextTrack
									+ ":" + nextSector);
							log.error("D64.getFileContents() "
									+ "Cyclic block chain in file "
									+ pFile.filename);
						}
						dout.close();
						return false;
					} else {
						arrCyclicAccessInfo[offset]++; // mark sector as read
					}
					// last sector ??
					if (currSector[0] == 0) {
						if (log.isDebugEnabled()) {
							log.debug("D64.getFileContents() currSector.length"
									+ currSector.length);
							log.debug("D64.getFileContents() 1="
									+ currSector[1]);
						}
						dout.write(currSector, 2, currSector.length - 2);
						dout.close();
						return true;
					}

					dout.write(currSector, 2, SECTOR_SIZE - 2);
					nextTrack = currSector[0];
					nextSector = currSector[1];
					if (log.isDebugEnabled()) {
						log.debug("D64.getFileContents() nextTrack="
								+ nextTrack);
						log.debug("D64.getFileContents() nextSector="
								+ nextSector);
					}
				}
			} catch (FileNotFoundException e) {
				log.error(e.getMessage(), e);
				return false;
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object readImage(final File file) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("D64.readImage() " + "image type = D64\n");
		}
		DiskState diskInfo = new DiskState();

		diskInfo.firstRun = true;
		setNrOfTracks(file, diskInfo);
		diskInfo.firstDirTrack = FIRST_DIR_TRACK;
		diskInfo.firstDirSector = FIRST_DIR_SECTOR;
		diskInfo.firstBAMTrack = BAM_TRACK;
		diskInfo.firstBAMSector = BAM_SECTOR;
		diskInfo.pSectorAdresses = SECTORADRESSES_D64;
		diskInfo.diskSize = file.length();

		DataInputStream di = new DataInputStream(new FileInputStream(file));
		diskInfo.diskData = new byte[(int) diskInfo.diskSize];
		di.read(diskInfo.diskData, 0, (int) diskInfo.diskSize);
		if (log.isDebugEnabled()) {
			log.debug("D64.readImage() " + "diskInfo.diskData.length="
					+ diskInfo.diskData.length);
		}
		di.close();
		return diskInfo;
	}

	/**
	 * @param file
	 * @return
	 */
	public Object createImage(File file) {
		if (log.isDebugEnabled()) {
			log.debug("D64.createImage() " + "image type = D64");
		}
		DiskState diskInfo = new DiskState();

		// create new image with DIRCBM title like loo.d64 (empty)
		// image is 0-filled, block 18/00 with info, block 18/01 has only 00 FF
		diskInfo.nrOfTracks = IMAGE_35_TRACKS;
		diskInfo.firstDirTrack = FIRST_DIR_TRACK;
		diskInfo.firstDirSector = FIRST_DIR_SECTOR;
		diskInfo.firstBAMTrack = BAM_TRACK;
		diskInfo.firstBAMSector = BAM_SECTOR;
		diskInfo.pSectorAdresses = SECTORADRESSES_D64;
		diskInfo.diskSize = file.length();

		diskInfo.diskData = new byte[IMAGE_SIZE];

		// fill first BAM block
		fillFirstD64BAMBlock(diskInfo);

		return diskInfo;
	}

	boolean fillFirstD64BAMBlock(Object info) {
		DiskState diskInfo = (DiskState) info;
		// int x, rc;

		int pAddr = GetDiskSectorAddr(diskInfo, diskInfo.firstBAMTrack,
				diskInfo.firstBAMSector);
		if (pAddr == -1) {
			if (log.isDebugEnabled()) {
				log.debug("D64.fillFirstD64BAMBlock() "
						+ "fillFirstD64BAMBlock: bad GetDiskSectorAddr call ("
						+ diskInfo.firstBAMTrack + ":"
						+ diskInfo.firstBAMSector + ")");
			}
			return false;
		}
		Bam1541 pBlock = new Bam1541(diskInfo.diskData, pAddr);

		// memset(pAddr, 0, 256+2);

		pBlock.nextDirTrack = diskInfo.firstBAMTrack;
		pBlock.nextDirSector = 1;
		pBlock.formatID = 'A';

		int value;
		for (int x = 1; x <= diskInfo.nrOfTracks; x++) {
			if (x != diskInfo.firstBAMTrack) {
				// setup data track info
				pBlock.BAM_Data[x - 1].sectorsLeft = (byte) diskInfo.pSectorAdresses[x - 1][BLOCK_INFO];
				value = (1 << pBlock.BAM_Data[x - 1].sectorsLeft) - 1;
			} else {
				// setup BAM track info
				// zwei Blöcke bereits verbraucht
				pBlock.BAM_Data[x - 1].sectorsLeft = (byte) diskInfo.pSectorAdresses[x - 1][BLOCK_INFO];
				value = ((1 << pBlock.BAM_Data[x - 1].sectorsLeft) - 1) & 0x00fffffc;
				pBlock.BAM_Data[x - 1].sectorsLeft -= 2;
			}
			pBlock.BAM_Data[x - 1].BAMOfTrack[0] = (byte) (value & 0x000000ff);
			pBlock.BAM_Data[x - 1].BAMOfTrack[1] = (byte) ((value & 0x0000ff00) >> BITS_PER_BYTE);
			pBlock.BAM_Data[x - 1].BAMOfTrack[2] = (byte) ((value & 0x00ff0000) >> (2 * BITS_PER_BYTE));
		}
		String diskName = "DIRCBM" + 0xA0 + "DISK" + 0xA0 + 0xA0 + 0xA0 + 0xA0
				+ 0xA0 + 0xA0 + 0xA0;
		for (int i = 0; i < pBlock.diskName.length; i++) {
			if (i < diskName.length()) {
				pBlock.diskName[i] = (byte) diskName.charAt(i);
			} else {
				pBlock.diskName[i] = (byte) 0xA0;
			}
		}
		pBlock.diskID[0] = '0';
		pBlock.diskID[1] = '1';
		pBlock.fillByte1 = (byte) 0xA0;
		pBlock.formatIDName[0] = '2';
		pBlock.formatIDName[1] = 'A';
		pBlock.fillBytes = new byte[] { (byte) 0xA0, (byte) 0xA0, (byte) 0xA0,
				(byte) 0xA0};

		diskInfo.diskData[pAddr + 257] = (byte) 0xff;
		return true;
	}

	// ////////////////////////////////////////////////////////////////////////////
	private int GetDiskSectorAddr(DiskState diskInfo, byte track, byte sector) {
		int index, errorInfo;

		if (track < 1 || track > diskInfo.nrOfTracks)
			return -1;

		if (sector > diskInfo.pSectorAdresses[track - 1][BLOCK_INFO])
			return -1;

		// check error info
		if (diskInfo.errorInformation) {
			// get absolute index of sector
			index = getAbsSectorIndex(diskInfo, track, sector);
			errorInfo = diskInfo.errorData[index];
			if (errorInfo != OK) {
				if (log.isErrorEnabled()) {
					log.error("D64.GetDiskSectorAddr() "
							+ "disk image error occured at " + track + ":"
							+ sector + "\n[" + ERRORTEXT[errorInfo] + "]");
				}
				return -1;
			}
		}
		return diskInfo.pSectorAdresses[track - 1][ADDR_INFO] + sector * 256;
	}

	/**
	 * Search in diskInfo.pFiles for a specific directory entry. The names of
	 * the entries are compared with the specified filename.
	 * 
	 * @param diskInfo
	 *            the diskInfo of the disk image
	 * @param destPath
	 *            the destination path of the file
	 * @param destName
	 *            the filename of the file to search for
	 * @return the directory entry found or null (not found)
	 */
	private DirEntry searchDirEntry(final DiskState diskInfo,
			final String destPath, final String destName) {
		if (log.isDebugEnabled()) {
			log.debug("D64.searchDirEntry() destPath=" + destPath
					+ ", destName=" + destName);
		}

		// format file name
		String myFile = getName(destPath, destName);
		if (log.isDebugEnabled()) {
			log.debug("D64.searchDirEntry()myfile=" + myFile);
		}

		// later with hashing
		if (log.isDebugEnabled()) {
			log.debug("D64.searchDirEntry() COMPARE fileCounter"
					+ diskInfo.fileCounter);
		}
		for (int i = 0; i < diskInfo.fileCounter; i++) {

			DirEntry entry = (DirEntry) diskInfo.pFiles.get(i);

			convertCBMString(entry.filename);
			String currFile = new String(convertFilename(entry.filename,
					entry.fileType));
			if (log.isDebugEnabled()) {
				log.debug("D64.searchDirEntry() COMPARE<" + myFile + "> <"
						+ currFile + ">");
			}
			if (currFile.equals(myFile)) {
				return entry;
			}
		}
		return null;
	}

	/**
	 * Determine the number of tracks of the disk image file length.
	 * 
	 * @param file
	 *            the disk image file
	 * @param diskInfo
	 *            the disk info to store the number of tracks
	 */
	private void setNrOfTracks(final File file, final DiskState diskInfo) {
		if (file.length() >= TRACKS_40_NO_ERRORS) {
			// 40 track image
			diskInfo.nrOfTracks = IMAGE_40_TRACKS;
			if (file.length() == TRACKS_40_WITH_ERRORS) {
				diskInfo.errorInformation = true;
				diskInfo.errorData = new byte[TRACKS_40_ERROR_BYTES_SIZE];
				System.arraycopy(diskInfo.diskData, (int) file.length()
						- TRACKS_40_ERROR_BYTES_SIZE, diskInfo.errorData, 0,
						TRACKS_40_ERROR_BYTES_SIZE);
				if (log.isDebugEnabled()) {
					log.debug("D64.setNrOfTracks() "
							+ "40 tracks disk WITH error bytes");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("D64.setNrOfTracks() "
							+ "40 tracks disk WITHOUT error bytes");
				}
				diskInfo.errorInformation = false;
			}
		} else {
			// 35 track image
			diskInfo.nrOfTracks = IMAGE_35_TRACKS;
			if (file.length() == TRACKS_35_WITH_ERRORS) {
				diskInfo.errorInformation = true;
				diskInfo.errorData = new byte[TRACKS_35_ERROR_BYTES_SIZE];
				System.arraycopy(diskInfo.diskData, (int) file.length()
						- TRACKS_35_ERROR_BYTES_SIZE, diskInfo.errorData, 0,
						TRACKS_35_ERROR_BYTES_SIZE);
				if (log.isDebugEnabled()) {
					log.debug("D64.setNrOfTracks() "
							+ "35 tracks disk WITH error bytes");
				}
			} else {
				diskInfo.errorInformation = false;
				if (log.isDebugEnabled()) {
					log.debug("D64.setNrOfTracks() "
							+ "35 tracks disk WITHOUT error bytes");
				}
			}
		}
	}

	/**
	 * Get the disk sector bytes for a specific track/sector.
	 * 
	 * @param diskInfo
	 *            the disk info of the disk image
	 * @param nextTrack
	 *            the track of the desired sector bytes
	 * @param nextSector
	 *            the sector of the desired sector bytes
	 * @param sectorBuffer
	 *            the buffer to fill with the result
	 * @return true - OK, false if an error occured
	 */
	private boolean getDiskSector(final DiskState diskInfo,
			final byte nextTrack, final byte nextSector,
			final byte[] sectorBuffer) {
		// check if the track is within range
		if (nextTrack < 1 || nextTrack > diskInfo.nrOfTracks) {
			return false;
		}
		// check if the sector is within range
		if (nextSector > diskInfo.pSectorAdresses[nextTrack - 1][BLOCK_INFO]) {
			return false;
		}
		// check error info bytes
		if (diskInfo.errorInformation) {
			// get absolute index of sector
			int index = getAbsSectorIndex(diskInfo, nextTrack, nextSector);
			int errorInfo = diskInfo.errorData[index];
			if (errorInfo != OK) {
				if (log.isErrorEnabled()) {
					log.error("D64.getDiskSector() "
							+ "disk image error occured at " + nextTrack + ":"
							+ nextSector + "[" + ERRORTEXT[errorInfo] + "]");
				}
				return false;
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("D64.GetDiskSector() "
					+ "diskInfo.pSectorAdresses[diskInfo.nextTrack - 1]"
					+ "[ADDR_INFO]="
					+ diskInfo.pSectorAdresses[nextTrack - 1][ADDR_INFO]);
		}
		System.arraycopy(diskInfo.diskData,
				diskInfo.pSectorAdresses[nextTrack - 1][ADDR_INFO] + nextSector
						* SECTOR_SIZE, sectorBuffer, 0, SECTOR_SIZE);

		return true;
	}

	/**
	 * Get absolute sector index of the disk image.
	 * 
	 * @param diskInfo
	 *            the disk info of the disk image
	 * @param nextTrack
	 *            the track to get the sector index for
	 * @param nextSector
	 *            the sector of nextTrack to get the sector index for
	 * @return the absolute sector index
	 */
	private int getAbsSectorIndex(final DiskState diskInfo,
			final byte nextTrack, final byte nextSector) {
		int index = 0;

		// check if the track is within range
		if (nextTrack < 1 || nextTrack > diskInfo.nrOfTracks) {
			return -1;
		}
		// check if the sector is within range
		if (nextSector > diskInfo.pSectorAdresses[nextTrack - 1][BLOCK_INFO]) {
			return -1;
		}
		// accumulate sector offsets for all preceding tracks
		for (int i = 0; i < nextTrack - 1; i++) {
			index += diskInfo.pSectorAdresses[i][BLOCK_INFO];
		}
		// add sector
		index += nextSector;

		return index;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean updateImage(Object info, String packedFile, String subPath, String srcPath, String addList, int flags) {
// FILE *fdArchive;
// BOOL archiveWasExisting;
// BYTE *pImage;
// int diskType=IMAGE_TYPE_NO_TYPE, rc, bytesRead;
// WIN32_FIND_DATA fileInfo;
// HANDLE hFile;
// T_MAIN_STATE *pInfo;
// BYTE *pBAMSector, *pPos;
// FILE *fdLog=NULL;
// char logFile [MAX_PATH];

		DiskState diskInfo = (DiskState) info;
		  // archive available --> add files to archive
		  StringTokenizer tok = new StringTokenizer(addList, ":");
			while (tok.hasMoreTokens()) {
				String pCurrFile = tok.nextToken();
				String nameBuffer = srcPath+pCurrFile;
				if (log.isDebugEnabled()) {
					log.debug("D64.updateImage() " +
							"adding file <"+nameBuffer+">\n");
				}
// char nameBuffer [_MAX_PATH];
// int blocksToWrite, blocksFree, x;
// FILE *fd;
// BYTE *pPrevData=NULL, *pData, currTrack, currSector, *pExt;
// T_DIR_ENTRY *pDirEntry=NULL;
// char *newFilename;

		    // get info if file is existing and size if available
		    File fileInfo = new File(nameBuffer);

		    // check disk image space
		    int blocksToWrite = (int) (fileInfo.length() / 254 + (((fileInfo.length() % 254)>0) ? 1 : 0 ));
		    int blocksFree = getFreeDataBlocksOnDisk(diskInfo);
		    if (blocksToWrite > blocksFree)
		    {
		    	if (log.isDebugEnabled()) {
					log.debug("D64.updateImage() " +
							"error not enough space <"+blocksToWrite+">");
				}
		      return false;
		    }
		    if (log.isDebugEnabled()) {
				log.debug("D64.updateImage() " +
						"blocks to write <"+blocksToWrite+">, blocks free <"+blocksFree+">");
			}
			}
			return true;
/*
		    // get dir entry
		    pDirEntry = getFreeDirEntry(pInfo, fdLog);
		    if (pDirEntry == NULL)
		    {
		      IFDEBUG fprintf(fdLog, "error: no free dir entry\n");
		      free(pInfo->diskData);
		      free(pInfo);
		      IFDEBUG fclose(fdLog);
		      return E_TOO_MANY_FILES;
		    }

		    // init dir info
		    memset(((BYTE *)pDirEntry)+2, 0, sizeof(T_DIR_ENTRY)-2);
		    pDirEntry->fileType = FILETYPE_PRG;

		    // setup file name buffer and init
		    newFilename = strdup(pCurrFile);
		    convertPCString(newFilename, strlen(newFilename));
		    memset(pDirEntry->filename, 0xA0, sizeof(pDirEntry->filename));

		    // do we have an known extension ?
		    pExt = strrchr(newFilename, '.');
		    if (pExt)
		    {
		      if (stricmp(pExt, ".PRG") == 0)
		      {
		        *pExt = '\0';
		        pDirEntry->fileType = FILETYPE_PRG;
		      }
		      else
		      {
		        if (stricmp(pExt, ".SEQ") == 0)
		        {
		          *pExt = '\0';
		          pDirEntry->fileType = FILETYPE_SEQ;
		        }
		        else
		          pDirEntry->fileType = FILETYPE_SEQ;
		      }
		    }
		    else
		      pDirEntry->fileType = FILETYPE_SEQ;  // set SEQ file type for all
													// files other than .PRG

		    // copy filename
		    strncpy(pDirEntry->filename, newFilename, min(strlen(newFilename), sizeof(pDirEntry->filename)));
		    free(newFilename);

		    pDirEntry->nrSectors = blocksToWrite;

		    // write data to image
		    fd = fopen(nameBuffer, "rb");
		    if (fd == NULL)
		    {
		      IFDEBUG fprintf(fdLog, "error reading file <%s>\n", nameBuffer);
		      free(pInfo->diskData);
		      free(pInfo);
		      IFDEBUG fclose(fdLog);
		      return E_EREAD;
		    }

		    // write block loop
		    for (x=0; x<blocksToWrite; x++)
		    {
		      pData = getFreeDataBlock(pInfo, &currTrack, &currSector, fdLog);  // get
																				// free
																				// block
																				// and
																				// mark
																				// as
																				// not
																				// free
		      if (pData == NULL)
		      {
		        IFDEBUG fprintf(fdLog, "error: no free blocks\n");
		        free(pInfo->diskData);
		        free(pInfo);
		        IFDEBUG fclose(fdLog);
		        return E_NO_MEMORY;
		      }

		      IFDEBUG fprintf(fdLog, "writing sector %d:%d\n", currTrack, currSector);

		      if (x == 0)
		      {
		        // store first track and sector of file chain
		        pDirEntry->firstFileTrack = currTrack;
		        pDirEntry->firstFileSector = currSector;
		      }

		      // update last block link info
		      if (pPrevData)
		      {
		        *pPrevData = currTrack;
		        *(pPrevData+1) = currSector;
		      }

		      memset(pData, 0, 256);
		      rc = fread(pData+2, 1, 254, fd);

		      IFDEBUG fprintf(fdLog, "marking sector %d:%d\n", currTrack, currSector);
		      markSectorAsUsed(pInfo, currTrack, currSector, fdLog);

		      pPrevData = pData;
		    }

		    // write last block header info
		    *pPrevData = 0;
		    *(pPrevData+1) = rc+2-1;

		    fclose(fd);

		    // next file
		    pCurrFile += strlen(pCurrFile) + 1;
		  }

		  IFDEBUG fprintf(fdLog, "packing done\n");

		  // write image in memory to disk
		  fdArchive = fopen(PackedFile, "wb");
		  if (fdArchive == NULL)
		  {
		    IFDEBUG fprintf(fdLog, "error: fopen (write image) failed\n");
		    free(pInfo->diskData);
		    free(pInfo);
		    IFDEBUG fclose(fdLog);
		    return E_EWRITE;
		  }
		  rc = fwrite(pInfo->diskData, 256, 683, fdArchive);  // TODO: !!!
																// 1541 specific
		  if (rc != 683)                        // TODO: !!! 1541 specific
		  {
		    IFDEBUG fprintf(fdLog, "error: fwrite (write image) wrote less (%d != 683)\n", rc);
		    free(pInfo->diskData);
		    free(pInfo);
		    IFDEBUG fclose(fdLog);
		    return E_EWRITE;
		  }
		  fclose(fdArchive);

		  free(pInfo->diskData);
		  free(pInfo);

		  IFDEBUG fclose(fdLog);

		  return 0;
		return false;
*/
	}
	// /////////////////////////////////////////////////////////////////////////////////////////////////////
	private int getFreeDataBlocksOnDisk(DiskState diskInfo) {

		int pAddr = GetDiskSectorAddr(diskInfo, diskInfo.firstBAMTrack,
				diskInfo.firstBAMSector);
		if (pAddr == -1) {
			if (log.isDebugEnabled()) {
				log.debug("D64.getFreeDataBlocksOnDisk() "
						+ "fillFirstD64BAMBlock: bad GetDiskSectorAddr call ("
						+ diskInfo.firstBAMTrack + ":"
						+ diskInfo.firstBAMSector + ")");
			}
			return 0;
		}
		Bam1541 pBlock = new Bam1541(diskInfo.diskData, pAddr);

		int blocksFree = 0;
		for (int x = 1; x <= diskInfo.nrOfTracks; x++) {
			if (x != diskInfo.firstBAMTrack) {
				blocksFree += pBlock.BAM_Data[x - 1].sectorsLeft;
			}
		}
		return blocksFree;
	}

}
