package plugins.wfx;

import java.io.File;

import plugins.FileTime;

/**
 * This class contains all file informations about one file system entry.
 * 
 * @author Ken
 */
public class Win32FindData {

	/**
	 * error code for setLastError(): no error detected.
	 */
	public static final long ERROR_SUCCESS = 0;

	/**
	 * error code for setLastError(): empty directory detected.
	 */
	public static final long ERROR_NO_MORE_FILES = 18;

	/**
	 * error code: Currently supported: ERROR_NO_MORE_FILES, ERROR_SUCCESS
	 * (default).
	 */
	private long lastErrorMessage = ERROR_SUCCESS;

	/**
	 * Get error code: Currently supported: ERROR_NO_MORE_FILES, ERROR_SUCCESS
	 * (default).
	 * 
	 * @return Returns the lastErrorMessage.
	 */
	public final long getLastErrorMessage() {
		return lastErrorMessage;
	}

	/**
	 * Set error code: Currently supported: ERROR_NO_MORE_FILES, ERROR_SUCCESS
	 * (default).
	 * 
	 * @param errorCode
	 *            the error code
	 */
	public final void setLastErrorMessage(final long errorCode) {
		this.lastErrorMessage = errorCode;
	}

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_READONLY = 0x00000001;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_HIDDEN = 0x00000002;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_SYSTEM = 0x00000004;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_DIRECTORY = 0x00000010;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_ARCHIVE = 0x00000020;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_DEVICE = 0x00000040;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_NORMAL = 0x00000080;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_TEMPORARY = 0x00000100;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_SPARSE_FILE = 0x00000200;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_REPARSE_POINT = 0x00000400;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_COMPRESSED = 0x00000800;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_OFFLINE = 0x00001000;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 0x00002000;

	/**
	 * file attribute of dwFileAttributes.
	 */
	public static final long FILE_ATTRIBUTE_ENCRYPTED = 0x00004000;

	/**
	 * File attributes. Use at least the FILE_ATTRIBUTE_DIRECTORY flag (default)
	 * to distinguish between files and directories. Links should be returned as
	 * files.<BR>
	 * Any combination of FILE_ATTRIBUTE_... constants
	 */
	private long dwFileAttributes = FILE_ATTRIBUTE_DIRECTORY;

	/**
	 * Get File attributes. Use at least the FILE_ATTRIBUTE_DIRECTORY flag
	 * (default) to distinguish between files and directories. Links should be
	 * returned as files.<BR>
	 * Any combination of FILE_ATTRIBUTE_... constants
	 * 
	 * @return Returns the dwFileAttributes.
	 */
	public final long getFileAttributes() {
		return dwFileAttributes;
	}

	/**
	 * Set File attributes. Use at least the FILE_ATTRIBUTE_DIRECTORY flag
	 * (default) to distinguish between files and directories. Links should be
	 * returned as files.<BR>
	 * Any combination of FILE_ATTRIBUTE_... constants
	 * 
	 * @param fileAttributes
	 *            The dwFileAttributes to set.
	 */
	public final void setFileAttributes(final long fileAttributes) {
		this.dwFileAttributes = fileAttributes;
	}

	/**
	 * Currently unused. If available, set to the time when the file was
	 * created.
	 */
	private FileTime ftCreationTime = new FileTime();

	/**
	 * Get Set Currently unused. If available, set to the time when the file was
	 * created.
	 * 
	 * @return Returns the ftCreationTime.
	 */
	public final FileTime getCreationTime() {
		return ftCreationTime;
	}

	/**
	 * Set Currently unused. If available, set to the time when the file was
	 * created.
	 * 
	 * @param creationTime
	 *            The ftCreationTime to set.
	 */
	public final void setCreationTime(final FileTime creationTime) {
		this.ftCreationTime = creationTime;
	}

	/**
	 * Currently unused. If available, set to the time when the file was last
	 * accessed.
	 */
	private FileTime ftLastAccessTime = new FileTime();

	/**
	 * Get Currently unused. If available, set to the time when the file was
	 * last accessed.
	 * 
	 * @return Returns the ftLastAccessTime.
	 */
	public final FileTime getLastAccessTime() {
		return ftLastAccessTime;
	}

	/**
	 * Set Currently unused. If available, set to the time when the file was
	 * last accessed.
	 * 
	 * @param lastAccessTime
	 *            The ftLastAccessTime to set.
	 */
	public final void setLastAccessTime(final FileTime lastAccessTime) {
		this.ftLastAccessTime = lastAccessTime;
	}

	/**
	 * Time stamp shown in the Total Commander file list, and copied with files.
	 * Use the following settings for files which don't have a time:<BR>
	 * ftLastWriteTime.dwHighDateTime=0<BR>
	 * ftLastWriteTime.dwLowDateTime=0;
	 */
	private FileTime ftLastWriteTime = new FileTime();

	/**
	 * Get Time stamp shown in the Total Commander file list, and copied with
	 * files. Use the following settings for files which don't have a time:<BR>
	 * ftLastWriteTime.dwHighDateTime=0<BR>
	 * ftLastWriteTime.dwLowDateTime=0;
	 * 
	 * @return Returns the ftLastWriteTime.
	 */
	public final FileTime getLastWriteTime() {
		return ftLastWriteTime;
	}

	/**
	 * Set Time stamp shown in the Total Commander file list, and copied with
	 * files. Use the following settings for files which don't have a time:<BR>
	 * ftLastWriteTime.dwHighDateTime=0<BR>
	 * ftLastWriteTime.dwLowDateTime=0;
	 * 
	 * @param lastWriteTime
	 *            The ftLastWriteTime to set.
	 */
	public final void setLastWriteTime(final FileTime lastWriteTime) {
		this.ftLastWriteTime = lastWriteTime;
	}

	/**
	 * High word of file size.
	 */
	private long nFileSizeHigh;

	/**
	 * Get High word of file size.
	 * 
	 * @return Returns the nFileSizeHigh.
	 */
	public final long getFileSizeHigh() {
		return nFileSizeHigh;
	}

	/**
	 * Set High word of file size.
	 * 
	 * @param fileSizeHigh
	 *            The nFileSizeHigh to set.
	 */
	public final void setFileSizeHigh(final long fileSizeHigh) {
		nFileSizeHigh = fileSizeHigh;
	}

	/**
	 * Low word of file size.
	 */
	private long nFileSizeLow;

	/**
	 * Get Low word of file size.
	 * 
	 * @return Returns the nFileSizeLow.
	 */
	public final long getFileSizeLow() {
		return nFileSizeLow;
	}

	/**
	 * Set Low word of file size.
	 * 
	 * @param fileSizeLow
	 *            The nFileSizeLow to set.
	 */
	public final void setFileSizeLow(final long fileSizeLow) {
		nFileSizeLow = fileSizeLow;
	}

	/**
	 * On Unix systems, you can | (or) the dwFileAttributes field with
	 * 0x80000000 and set the dwReserved0 parameter to the Unix file mode
	 * (permissions). These will then be shown in Wincmd and can be changed
	 * through Files - Change attributes.
	 */
	private long dwReserved0;

	/**
	 * Get On Unix systems, you can | (or) the dwFileAttributes field with
	 * 0x80000000 and set the dwReserved0 parameter to the Unix file mode
	 * (permissions). These will then be shown in Wincmd and can be changed
	 * through Files - Change attributes.
	 * 
	 * @return Returns the dwReserved0.
	 */
	public final long getReserved0() {
		return dwReserved0;
	}

	/**
	 * Set On Unix systems, you can | (or) the dwFileAttributes field with
	 * 0x80000000 and set the dwReserved0 parameter to the Unix file mode
	 * (permissions). These will then be shown in Wincmd and can be changed
	 * through Files - Change attributes.
	 * 
	 * @param reserved0
	 *            The dwReserved0 to set.
	 */
	public final void setReserved0(final long reserved0) {
		this.dwReserved0 = reserved0;
	}

	/**
	 * Unused, must be set to 0. Reserved for future plugin enhancements.
	 */
	private long dwReserved1;

	/**
	 * Get Unused, must be set to 0. Reserved for future plugin enhancements.
	 * 
	 * @return Returns the dwReserved1.
	 */
	public final long getReserved1() {
		return dwReserved1;
	}

	/**
	 * Set Unused, must be set to 0. Reserved for future plugin enhancements.
	 * 
	 * @param reserved1
	 *            The dwReserved1 to set.
	 */
	public final void setReserved1(final long reserved1) {
		this.dwReserved1 = reserved1;
	}

	/**
	 * Local file name relative to the directory (without the path).
	 */
	private String cFileName;

	/**
	 * Get Local file name relative to the directory (without the path).
	 * 
	 * @return Returns the cFileName.
	 */
	public final String getFileName() {
		return cFileName;
	}

	/**
	 * Set Local file name relative to the directory (without the path).
	 * 
	 * @param fileName
	 *            The cFileName to set.
	 */
	public final void setFileName(final String fileName) {
		cFileName = fileName;
	}

	/**
	 * DOS-style file name (optional), set empty if unused.
	 */
	private String cAlternateFileName = "";

	/**
	 * Get DOS-style file name (optional), set empty if unused.
	 * 
	 * @return Returns the cAlternateFileName.
	 */
	public final String getAlternateFileName() {
		return cAlternateFileName;
	}

	/**
	 * Set DOS-style file name (optional), set empty if unused.
	 * 
	 * @param alternateFileName
	 *            The cAlternateFileName to set.
	 */
	public final void setAlternateFileName(final String alternateFileName) {
		cAlternateFileName = alternateFileName;
	}

	/**
	 * Set Win32 find data.
	 * 
	 * @param file
	 *            the file with the information
	 */
	public final void setFromFile(final File file) {
		cFileName = file.getName();
		if (file.isDirectory()) {
			dwFileAttributes = Win32FindData.FILE_ATTRIBUTE_DIRECTORY;
		} else {
			dwFileAttributes = Win32FindData.FILE_ATTRIBUTE_NORMAL;
		}
		if (file.isHidden()) {
			dwFileAttributes |= Win32FindData.FILE_ATTRIBUTE_HIDDEN;
		}
		if (!file.canRead()) {
			dwFileAttributes |= Win32FindData.FILE_ATTRIBUTE_READONLY;
		}
		if (file.canRead()) {
			ftLastWriteTime.setDate(file.lastModified());
		}
		nFileSizeLow = file.length();
		nFileSizeHigh = 0;
	}

}
