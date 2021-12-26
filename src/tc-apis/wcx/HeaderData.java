package plugins.wcx;

import java.util.Calendar;

/**
 * HeaderData is a structure used in readerHeader.
 * 
 * @author Ken
 * 
 */
public class HeaderData {

	/**
	 * setFileTime(): One hour in milliseconds.
	 */
	private static final int MS_ONE_HOUR = 3600000;

	/**
	 * setFileTime(): Base date 1980.
	 */
	private static final int YEAR_1980 = 1980;

	/**
	 * setFileTime(): Bit shift for the year.
	 */
	private static final int SHIFT_YEAR = 25;

	/**
	 * setFileTime(): Bit shift for the month.
	 */
	private static final int SHIFT_MONTH = 21;

	/**
	 * setFileTime(): Bit shift for the day.
	 */
	private static final int SHIFT_DAY = 16;

	/**
	 * setFileTime(): Bit shift for the hour.
	 */
	private static final int SHIFT_HOUR = 11;

	/**
	 * setFileTime(): Bit shift for the minute.
	 */
	private static final int SHIFT_MINUTE = 5;

	/**
	 * setFileTime(): Bit shift for the second.
	 */
	private static final int SHIFT_SECOND = 2;

	/**
	 * file attribute of fileAttr.
	 */
	public static final int FILE_ATTRIBUTE_READONLY = 0x00000001;

	/**
	 * file attribute of fileAttr.
	 */
	public static final int FILE_ATTRIBUTE_HIDDEN = 0x00000002;

	/**
	 * file attribute of fileAttr.
	 */
	public static final int FILE_ATTRIBUTE_SYSTEM = 0x00000004;

	/**
	 * file attribute of fileAttr.
	 */
	public static final int FILE_ATTRIBUTE_VOLUME_ID_FILE = 0x00000008;

	/**
	 * file attribute of fileAttr.
	 */
	public static final int FILE_ATTRIBUTE_DIRECTORY = 0x00000010;

	/**
	 * file attribute of fileAttr.
	 */
	public static final int FILE_ATTRIBUTE_ARCHIVE = 0x00000020;

	/**
	 * file attribute of fileAttr.
	 */
	public static final int FILE_ATTRIBUTE_ANY_FILE = 0x0000003F;

	/**
	 * ArcName contain the name of the archive.
	 */
	private String fArcName;

	/**
	 * ArcName contain the name of the archive.
	 * 
	 * @return Returns the arcName.
	 */
	public final String getArcName() {
		return fArcName;
	}

	/**
	 * ArcName contain the name of the archive.
	 * 
	 * @param arcName
	 *            The arcName to set.
	 */
	public final void setArcName(final String arcName) {
		fArcName = arcName;
	}

	/**
	 * ArcName contain the name of the file within the archive.
	 */
	private String fFileName;

	/**
	 * ArcName contain the name of the file within the archive.
	 * 
	 * @return Returns the fileName.
	 */
	public final String getFileName() {
		return fFileName;
	}

	/**
	 * ArcName contain the name of the file within the archive.
	 * 
	 * @param fileName
	 *            The fileName to set.
	 */
	public final void setFileName(final String fileName) {
		this.fFileName = fileName;
	}

	/**
	 * ???
	 */
	private int fFlags;

	/**
	 * @return Returns the flags.
	 */
	public final int getFlags() {
		return fFlags;
	}

	/**
	 * @param flags
	 *            The flags to set.
	 */
	public final void setFlags(final int flags) {
		this.fFlags = flags;
	}

	/**
	 * PackSize contain size of the file when packed.
	 */
	private long fPackSize;

	/**
	 * PackSize contain size of the file when packed.
	 * 
	 * @return Returns the packSize.
	 */
	public final long getPackSize() {
		return fPackSize;
	}

	/**
	 * PackSize contain size of the file when packed.
	 * 
	 * @param packSize
	 *            The packSize to set.
	 */
	public final void setPackSize(final long packSize) {
		this.fPackSize = packSize;
	}

	/**
	 * UnpSize contain the size of the file when extracted.
	 */
	private long fUnpSize;

	/**
	 * UnpSize contain the size of the file when extracted.
	 * 
	 * @return Returns the unpSize.
	 */
	public final long getUnpSize() {
		return fUnpSize;
	}

	/**
	 * UnpSize contain the size of the file when extracted.
	 * 
	 * @param unpSize
	 *            The unpSize to set.
	 */
	public final void setUnpSize(final long unpSize) {
		this.fUnpSize = unpSize;
	}

	/**
	 * HostOS is there for compatibility with unrar.dll only, and should be set
	 * to zero.
	 */
	private int fHostOs;

	/**
	 * HostOS is there for compatibility with unrar.dll only, and should be set
	 * to zero.
	 * 
	 * @return Returns the hostOs.
	 */
	public final int getHostOs() {
		return fHostOs;
	}

	/**
	 * HostOS is there for compatibility with unrar.dll only, and should be set
	 * to zero.
	 * 
	 * @param hostOs
	 *            The hostOs to set.
	 */
	public final void setHostOs(final int hostOs) {
		this.fHostOs = hostOs;
	}

	/**
	 * FileCRC is the 32-bit CRC (cyclic redundancy check) checksum of the file.
	 * If not available, set to zero.
	 */
	private long fFileCRC;

	/**
	 * FileCRC is the 32-bit CRC (cyclic redundancy check) checksum of the file.
	 * If not available, set to zero.
	 * 
	 * @return Returns the fileCRC.
	 */
	public final long getFileCRC() {
		return fFileCRC;
	}

	/**
	 * FileCRC is the 32-bit CRC (cyclic redundancy check) checksum of the file.
	 * If not available, set to zero.
	 * 
	 * @param fileCRC
	 *            The fileCRC to set.
	 */
	public final void setFileCRC(final long fileCRC) {
		this.fFileCRC = fileCRC;
	}

	/**
	 * FileTime contains the date and the time of the file’s last update. Use
	 * the following algorithm to set the value: <TABLE>
	 * <TR>
	 * <TD>FileTime = (year - 1980) &lt;&lt; 25</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>month &lt;&lt; 21</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>day &lt;&lt; 16</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>hour &lt; 11</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>minute &lt;&lt; 5</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>second/2</TD>
	 * <TD> &nbsp; </TD>
	 * </TR>
	 * </TABLE>
	 * 
	 * <B>Make sure that:</B>
	 * <UL>
	 * <LI>year is in the four digit format between 1980 and 2100
	 * <LI>month is a number between 1 and 12
	 * <LI>hour is in the 24 hour format
	 * </UL>
	 */
	private long fFileTime;

	/**
	 * FileTime contains the date and the time of the file’s last update. Use
	 * the following algorithm to set the value: <TABLE>
	 * <TR>
	 * <TD>FileTime = (year - 1980) &lt;&lt; 25</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>month &lt;&lt; 21</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>day &lt;&lt; 16</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>hour &lt;&lt; 11</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>minute &lt;&lt; 5</TD>
	 * <TD> | </TD>
	 * </TR>
	 * <TR>
	 * <TD>second/2</TD>
	 * <TD> &nbsp; </TD>
	 * </TR>
	 * </TABLE>
	 * 
	 * <B>Make sure that:</B>
	 * <UL>
	 * <LI>year is in the four digit format between 1980 and 2100
	 * <LI>month is a number between 1 and 12
	 * <LI>hour is in the 24 hour format
	 * </UL>
	 * 
	 * @return Returns the fileTime.
	 */
	public final long getFileTime() {
		return fFileTime;
	}

	/**
	 * FileTime contains the date and the time of the file’s last update.<BR>
	 * milliseconds since the epoch (00:00:00 GMT, January 1, 1970<BR>
	 * 
	 * @param cal
	 *            The fileTime to set.
	 */
	public final void setFileTime(final Calendar cal) {
		int timezoneOffset = cal.get(Calendar.ZONE_OFFSET) / MS_ONE_HOUR;
		cal.add(Calendar.HOUR_OF_DAY, timezoneOffset);
		long value = (cal.get(Calendar.YEAR) - YEAR_1980) << SHIFT_YEAR
				| (cal.get(Calendar.MONTH) + 1) << SHIFT_MONTH
				| (cal.get(Calendar.DATE)) << SHIFT_DAY
				| (cal.get(Calendar.HOUR_OF_DAY)) << SHIFT_HOUR
				| (cal.get(Calendar.MINUTE)) << SHIFT_MINUTE
				| (cal.get(Calendar.SECOND) / 2) << SHIFT_SECOND;
		this.fFileTime = value;
	}

	/**
	 * ???
	 */
	private int fUnpVer;

	/**
	 * @return Returns the unpVer.
	 */
	public final int getUnpVer() {
		return fUnpVer;
	}

	/**
	 * @param unpVer
	 *            The unpVer to set.
	 */
	public final void setUnpVer(final int unpVer) {
		this.fUnpVer = unpVer;
	}

	/**
	 * ???
	 */
	private int fMethod;

	/**
	 * @return Returns the method.
	 */
	public final int getMethod() {
		return fMethod;
	}

	/**
	 * @param method
	 *            The method to set.
	 */
	public final void setMethod(final int method) {
		this.fMethod = method;
	}

	/**
	 * This is the file attribute. Can be set to any combination of the
	 * following values:
	 * <UL>
	 * <LI>FILE_ATTRIBUTE_READONLY - Read-only file
	 * <LI>FILE_ATTRIBUTE_HIDDEN - Hidden file
	 * <LI>FILE_ATTRIBUTE_SYSTEM - System file
	 * <LI>FILE_ATTRIBUTE_VOLUME_ID_FILE - Volume ID file
	 * <LI>FILE_ATTRIBUTE_DIRECTORY - Directory
	 * <LI>FILE_ATTRIBUTE_ARCHIVE - Archive file
	 * <LI>FILE_ATTRIBUTE_ANY_FILE - Any file
	 * </UL>
	 */
	private int fFileAttr;

	/**
	 * This is the file attribute. Can be set to any combination of the
	 * following values:
	 * <UL>
	 * <LI>FILE_ATTRIBUTE_READONLY - Read-only file
	 * <LI>FILE_ATTRIBUTE_HIDDEN - Hidden file
	 * <LI>FILE_ATTRIBUTE_SYSTEM - System file
	 * <LI>FILE_ATTRIBUTE_VOLUME_ID_FILE - Volume ID file
	 * <LI>FILE_ATTRIBUTE_DIRECTORY - Directory
	 * <LI>FILE_ATTRIBUTE_ARCHIVE - Archive file
	 * <LI>FILE_ATTRIBUTE_ANY_FILE - Any file
	 * </UL>
	 * 
	 * @return Returns the fileAttr.
	 */
	public final int getFileAttr() {
		return fFileAttr;
	}

	/**
	 * This is the file attribute. Can be set to any combination of the
	 * following values:
	 * <UL>
	 * <LI>FILE_ATTRIBUTE_READONLY - Read-only file
	 * <LI>FILE_ATTRIBUTE_HIDDEN - Hidden file
	 * <LI>FILE_ATTRIBUTE_SYSTEM - System file
	 * <LI>FILE_ATTRIBUTE_VOLUME_ID_FILE - Volume ID file
	 * <LI>FILE_ATTRIBUTE_DIRECTORY - Directory
	 * <LI>FILE_ATTRIBUTE_ARCHIVE - Archive file
	 * <LI>FILE_ATTRIBUTE_ANY_FILE - Any file
	 * </UL>
	 * 
	 * @param fileAttr
	 *            The fileAttr to set.
	 */
	public final void setFileAttr(final int fileAttr) {
		this.fFileAttr = fileAttr;
	}

	/**
	 * The cmtBuf variable is for the file comment. It is currently not used by
	 * Total Commander, so may be set to NULL.
	 */
	private String fCmtBuf;

	/**
	 * The cmtBuf variable is for the file comment. It is currently not used by
	 * Total Commander, so may be set to NULL.
	 * 
	 * @return Returns the cmtBuf.
	 */
	public final String getCmtBuf() {
		return fCmtBuf;
	}

	/**
	 * The cmtBuf variable is for the file comment. It is currently not used by
	 * Total Commander, so may be set to NULL.
	 * 
	 * @param cmtBuf
	 *            the fCmtBuf to set
	 */
	public final void setCmtBuf(final String cmtBuf) {
		fCmtBuf = cmtBuf;
	}

	/**
	 * The cmtBufSize variable is for the file comment. It is currently not used
	 * by Total Commander, so may be set to NULL.
	 */
	private int fCmtBufSize;

	/**
	 * The cmtBufSize variable is for the file comment. It is currently not used
	 * by Total Commander, so may be set to NULL.
	 * 
	 * @return Returns the cmtBufSize.
	 */
	public final int getCmtBufSize() {
		return fCmtBufSize;
	}

	/**
	 * The cmtBufSize variable is for the file comment. It is currently not used
	 * by Total Commander, so may be set to NULL.
	 * 
	 * @param cmtBufSize
	 *            the fCmtBufSize to set
	 */
	public final void setCmtBufSize(final int cmtBufSize) {
		fCmtBufSize = cmtBufSize;
	}

	/**
	 * The cmtSize variable is for the file comment. It is currently not used by
	 * Total Commander, so may be set to NULL.
	 */
	private int fCmtSize;

	/**
	 * The cmtSize variable is for the file comment. It is currently not used by
	 * Total Commander, so may be set to NULL.
	 * 
	 * @return Returns the cmtSize.
	 */
	public final int getCmtSize() {
		return fCmtSize;
	}

	/**
	 * The cmtSize variable is for the file comment. It is currently not used by
	 * Total Commander, so may be set to NULL.
	 * 
	 * @param cmtSize
	 *            the fCmtSize to set
	 */
	public final void setCmtSize(final int cmtSize) {
		fCmtSize = cmtSize;
	}

	/**
	 * The cmtState variable is for the file comment. It is currently not used
	 * by Total Commander, so may be set to NULL.
	 */
	private int fCmtState;

	/**
	 * The cmtState variable is for the file comment. It is currently not used
	 * by Total Commander, so may be set to NULL.
	 * 
	 * @return Returns the cmtState.
	 */
	public final int getCmtState() {
		return fCmtState;
	}

	/**
	 * The cmtState variable is for the file comment. It is currently not used
	 * by Total Commander, so may be set to NULL.
	 * 
	 * @param cmtState
	 *            the fCmtState to set
	 */
	public final void setCmtState(final int cmtState) {
		fCmtState = cmtState;
	}

	/**
	 * HeaderData is a structure used in ReaderHeader.
	 */
	public HeaderData() {
	}

}
