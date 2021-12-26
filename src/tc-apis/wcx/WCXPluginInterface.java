package plugins.wcx;

import plugins.DefaultParam;

/**
 * This is the interface for Total Commander WCX plugins.
 * 
 * @author Ken
 * 
 */
public interface WCXPluginInterface {

	/**
	 * Success.
	 */
	int SUCCESS = 0;

	/**
	 * No more files in archive.
	 */
	int E_END_ARCHIVE = 10;

	/**
	 * Not enough memory.
	 */
	int E_NO_MEMORY = 11;

	/**
	 * Data is bad.
	 */
	int E_BAD_DATA = 12;

	/**
	 * CRC error in archive data.
	 */
	int E_BAD_ARCHIVE = 13;

	/**
	 * Archive format unknown.
	 */
	int E_UNKNOWN_FORMAT = 14;

	/**
	 * Cannot open existing file.
	 */
	int E_EOPEN = 15;

	/**
	 * Cannot create file.
	 */
	int E_ECREATE = 16;

	/**
	 * Error closing file.
	 */
	int E_ECLOSE = 17;

	/**
	 * Error reading from file.
	 */
	int E_EREAD = 18;

	/**
	 * Error writing to file.
	 */
	int E_EWRITE = 19;

	/**
	 * Buffer too small.
	 */
	int E_SMALL_BUF = 20;

	/**
	 * Function aborted by user.
	 */
	int E_EABORTED = 21;

	/**
	 * No files found.
	 */
	int E_NO_FILES = 22;

	/**
	 * Too many files to pack.
	 */
	int E_TOO_MANY_FILES = 23;

	/**
	 * Function not supported.
	 */
	int E_NOT_SUPPORTED = 24;

	/**
	 * processFile(): Skip this file.
	 */
	int PK_SKIP = 0;

	/**
	 * processFile(): Test file integrity.
	 */
	int PK_TEST = 1;

	/**
	 * processFile(): Extract to disk.
	 */
	int PK_EXTRACT = 2;

	/**
	 * packFiles(): Delete original after packing.
	 */
	int PK_PACK_MOVE_FILES = 1;

	/**
	 * packFiles(): Save path names of files.
	 */
	int PK_PACK_SAVE_PATHS = 2;

	/**
	 * getPackerCaps(): Can create new archives.
	 */
	int PK_CAPS_NEW = 1;

	/**
	 * getPackerCaps(): Can modify existing archives.
	 */
	int PK_CAPS_MODIFY = 2;

	/**
	 * getPackerCaps(): Archive can contain multiple files.
	 */
	int PK_CAPS_MULTIPLE = 4;

	/**
	 * getPackerCaps(): Can delete files.
	 */
	int PK_CAPS_DELETE = 8;

	/**
	 * getPackerCaps(): Has options dialog.
	 */
	int PK_CAPS_OPTIONS = 16;

	/**
	 * getPackerCaps(): Supports packing in memory.
	 */
	int PK_CAPS_MEMPACK = 32;

	/**
	 * getPackerCaps(): Detect archive type by content.
	 */
	int PK_CAPS_BY_CONTENT = 64;

	/**
	 * getPackerCaps(): Allow searching for text in archives created with this
	 * plugin.
	 */
	int PK_CAPS_SEARCHTEXT = 128;

	/**
	 * getPackerCaps(): Don't show packer icon, don't open with Enter but with
	 * Ctrl+PgDn.
	 */
	int PK_CAPS_HIDE = 256;

	/**
	 * getPackerCaps(): Plugin supports PK_PACK_ENCRYPT option.
	 */
	int PK_CAPS_ENCRYPT = 512;

	/**
	 * startMemPack(): The output stream should include the complete headers
	 * (beginning+end).
	 */
	int MEM_OPTIONS_WANTHEADERS = 1;

	/**
	 * packToMem(): SUCCESS.
	 */
	int MEMPACK_OK = 0;

	/**
	 * packToMem(): DONE.
	 */
	int MEMPACK_DONE = 1;

	/**
	 * setChangeVol(): Ask user for location of next volume.
	 */
	int PK_VOL_ASK = 0;

	/**
	 * setChangeVol(): Notify app that next volume will be unpacked.
	 */
	int PK_VOL_NOTIFY = 1;

	/**
	 * OpenArchive should return a unique handle representing the archive. The
	 * handle should remain valid until CloseArchive is called. If an error
	 * occurs, you should return zero, and specify the error by setting
	 * OpenResult member of ArchiveData. <BR>
	 * OpenArchive should perform all necessary operations when an archive is to
	 * be opened. <BR>
	 * You can use the ArchiveData to query information about the archive being
	 * open, and store the information in ArchiveData to some location that can
	 * be accessed via the handle.
	 * 
	 * @param archiveData
	 *            The archive data being open
	 * @return A unique handle representing the archive
	 */
	Object openArchive(final OpenArchiveData archiveData);

	/**
	 * Totalcmd calls ReadHeader to find out what files are in the archive.<BR>
	 * ReadHeader is called as long as it returns zero (as long as the previous
	 * call to this function returned zero). Each time it is called, HeaderData
	 * is supposed to provide Totalcmd with information about the next file
	 * contained in the archive. When all files in the archive have been
	 * returned, ReadHeader should return E_END_ARCHIVE which will prevent
	 * ReaderHeader from being called again. If an error occurs, ReadHeader
	 * should return one of the error values or 0 for no error.
	 * 
	 * In short, you are supposed to set at least PackSize, UnpSize, FileTime,
	 * and FileName members of tHeaderData. Totalcmd will use this information
	 * to display content of the archive when the archive is viewed as a
	 * directory.
	 * 
	 * @param archiveData
	 *            Contains the handle returned by OpenArchive. The programmer is
	 *            encouraged to store other information in the location that can
	 *            be accessed via this handle. For example, you may want to
	 *            store the position in the archive when returning files
	 *            information in ReadHeader.
	 * @param headerData
	 *            HeaderData is supposed to provide Totalcmd with information
	 *            about the next file contained in the archive.
	 * @return SUCCESS or E_END_ARCHIVE or error codes E_...
	 */
	int readHeader(final Object archiveData, final HeaderData headerData);

	/**
	 * ProcessFile should unpack the specified file or test the integrity of the
	 * archive. Unlike PackFiles, ProcessFile is passed only one filename. When
	 * Total Commander first opens an archive, it scans all file names with
	 * OpenMode==PK_OM_LIST, so ReadHeader() is called in a loop with calling
	 * ProcessFile(...,PK_SKIP,...). When the user has selected some files and
	 * started to decompress them, Total Commander again calls ReadHeader() in a
	 * loop. For each file which is to be extracted, Total Commander calls
	 * ProcessFile() with Operation==PK_EXTRACT immediately after the
	 * ReadHeader() call for this file. If the file needs to be skipped, it
	 * calls it with Operation==PK_SKIP. <BR>
	 * Each time DestName is set to contain the filename to be extracted,
	 * tested, or skipped.
	 * 
	 * @param archiveData
	 *            contains the handle previously returned by you in OpenArchive.
	 *            Using this, you should be able to find out information (such
	 *            as the archive filename) that you need for extracting files
	 *            from the archive.
	 * @param operation
	 *            Operation is set to one of the following:
	 *            <UL>
	 *            <LI>PK_SKIP - Skip this file
	 *            <LI>PK_TEST - Test file integrity
	 *            <LI>PK_EXTRACT - Extract to disk
	 *            </UL>
	 * @param destPath
	 *            Either DestName contains the full path and file name and
	 *            DestPath is NULL, or DestName contains only the file name and
	 *            DestPath the file path. This is done for compatibility with
	 *            unrar.dll.
	 * @param destName
	 *            Either DestName contains the full path and file name and
	 *            DestPath is NULL, or DestName contains only the file name and
	 *            DestPath the file path. This is done for compatibility with
	 *            unrar.dll.
	 * @return ProcessFile should return SUCCESS or one of the error values
	 *         otherwise.
	 */
	int processFile(final Object archiveData, final int operation,
			final String destPath, final String destName);

	/**
	 * CloseArchive should perform all necessary operations when an archive is
	 * about to be closed.
	 * 
	 * @param archiveData
	 *            refers to the value returned by a programmer within a previous
	 *            call to OpenArchive.
	 * @return CloseArchive should return SUCCESS or one of the error values
	 *         otherwise. It should free all the resources associated with the
	 *         open archive.
	 */
	int closeArchive(Object archiveData);

	/**
	 * PackFiles specifies what should happen when a user creates, or adds files
	 * to the archive.
	 * 
	 * @param packedFile
	 *            refers to the archive that is to be created or modified. The
	 *            string contains the full path.
	 * @param subPath
	 *            is either NULL, when the files should be packed with the paths
	 *            given with the file names, or not NULL when they should be
	 *            placed below the given subdirectory within the archive.
	 *            Example:
	 *            <UL>
	 *            <LI>SubPath="subdirectory"
	 *            <LI>Name in AddList="subdir2\filename.ext"
	 *            </UL>-> File should be packed as
	 *            "subdirectory\subdir2\filename.ext"
	 * @param srcPath
	 *            contains path to the files in AddList. SrcPath and AddList
	 *            together specify files that are to be packed into PackedFile.
	 * @param addList
	 *            Each string in AddList is zero-delimited (ends in zero), and
	 *            the AddList string ends with an extra zero byte, i.e. there
	 *            are two zero bytes at the end of AddList.
	 * @param flags
	 *            can contain a combination of the following values reflecting
	 *            the user choice from within Totalcmd:
	 *            <UL>
	 *            <LI>PK_PACK_MOVE_FILES
	 *            <LI>PK_PACK_SAVE_PATHS
	 *            </UL>
	 * @return PackFiles should return SUCCESS or one of the error values
	 *         otherwise.
	 */
	int packFiles(final String packedFile, final String subPath,
			final String srcPath, final String addList, final int flags);

	/**
	 * DeleteFiles should delete the specified files from the archive.
	 * 
	 * @param packedFile
	 *            contains full path and name of the the archive.
	 * @param deleteList
	 *            contains the list of files that should be deleted from the
	 *            archive. The format of this string is the same as AddList
	 *            within PackFiles.
	 * @return DeleteFiles should return SUCCESS or one of the error values
	 *         otherwise.
	 */
	int deleteFiles(final String packedFile, final String deleteList);

	/**
	 * GetPackerCaps tells Totalcmd what features your packer plugin supports.
	 * <BR>
	 * <B>Important note:</B><BR>
	 * If you change the return values of this function, e.g. add packing
	 * support, you need to reinstall the packer plugin in Total Commander,
	 * otherwise it will not detect the new capabilities.
	 * 
	 * @return Implement GetPackerCaps to return a combination of the following
	 *         values:
	 *         <UL>
	 *         <li> PK_CAPS_NEW
	 *         <li> PK_CAPS_MODIFY <BR>
	 *         <BR>
	 *         Omitting PK_CAPS_NEW and PK_CAPS_MODIFY means PackFiles will
	 *         never be called and so you don’t have to implement PackFiles.
	 *         <BR>
	 *         <BR>
	 *         <li> PK_CAPS_MULTIPLE <BR>
	 *         <BR>
	 *         Omitting PK_CAPS_MULTIPLE means PackFiles will be supplied with
	 *         just one file. <BR>
	 *         <BR>
	 *         <li> PK_CAPS_DELETE<BR>
	 *         <BR>
	 *         Leaving out PK_CAPS_DELETE means DeleteFiles will never be
	 *         called. <BR>
	 *         <BR>
	 *         <li> PK_CAPS_OPTIONS <BR>
	 *         <BR>
	 *         leaving out PK_CAPS_OPTIONS means ConfigurePacker will not be
	 *         called. <BR>
	 *         <BR>
	 *         <li> PK_CAPS_MEMPACK <BR>
	 *         <BR>
	 *         PK_CAPS_MEMPACK enables the functions StartMemPack, PackToMem and
	 *         DoneMemPack. <BR>
	 *         <BR>
	 *         <li> PK_CAPS_BY_CONTENT <BR>
	 *         <BR>
	 *         If PK_CAPS_BY_CONTENT is returned, Totalcmd calls the function
	 *         CanYouHandleThisFile when the user presses Ctrl+PageDown on an
	 *         unknown archive type. <BR>
	 *         <BR>
	 *         <li> PK_CAPS_SEARCHTEXT <BR>
	 *         <BR>
	 *         If PK_CAPS_SEARCHTEXT is returned, Total Commander will search
	 *         for text inside files packed with this plugin. This may not be a
	 *         good idea for certain plugins like the diskdir plugin, where file
	 *         contents may not be available. <BR>
	 *         <BR>
	 *         <li> PK_CAPS_HIDE <BR>
	 *         <BR>
	 *         Finally, if PK_CAPS_HIDE is set, the plugin will not show the
	 *         file type as a packer. This is useful for plugins which are
	 *         mainly used for creating files, e.g. to create batch files, avi
	 *         files etc. The file needs to be opened with Ctrl+PgDn in this
	 *         case, because Enter will launch the associated application. <BR>
	 *         <BR>
	 *         </UL>
	 */
	int getPackerCaps();

	/**
	 * ConfigurePacker gets called when the user clicks the Configure button
	 * from within "Pack files..." dialog box in Totalcmd. Usually, you provide
	 * a user with a dialog box specifying a method and/or its parameters that
	 * should be applied in the packing process. Or, you just want to display a
	 * message box about what your plugin is, just like Christian Ghisler’s
	 * DiskDir does. <BR>
	 * <BR>
	 * You may decide not to implement this function. Then, make sure you omit
	 * PK_CAPS_OPTIONS from return values of GetPackerCaps.
	 * 
	 * @param parentWin
	 *            That is, you make your dialog box a child of Parent.
	 * @return Return a handle to your configuration window.
	 */
	int configurePacker(final int parentWin);

	/**
	 * StartMemPack starts packing into memory. This function is only needed if
	 * you want to create archives in combination with TAR, e.g. TAR.BZ2. It
	 * allows Totalcmd to create a TAR.Plugin file in a single step.
	 * 
	 * @param options
	 *            Options can contain a combination of the following values:
	 *            <UL>
	 *            <LI>MEM_OPTIONS_WANTHEADERS
	 *            </UL>
	 * @param fileName
	 *            refers to the name of the file being packed - some packers
	 *            store the name in the local header.
	 * @return StartMemPack should return a user-defined handle (e.g. pointer to
	 *         a structure) on success, zero otherwise.
	 */
	Object startMemPack(final int options, final String fileName);

	/**
	 * PackToMem packs the next chunk of data passed to it and/or returns the
	 * compressed data to the calling program. It is implemented together with
	 * StartMemPack and DoneMemPack.
	 * 
	 * PackToMem is the most complex function of the packer plugin. It is called
	 * by Total Commander in a loop as long as there is data to be packed, and
	 * as there is data to retrieve. The plugin should do the following:
	 * <OL>
	 * <LI> As long as there is data sent through BufIn, take it and add it to
	 * your internal buffers (if there is enough space).
	 * <LI> As soon as there is enough data in the internal input buffers, start
	 * packing to the output buffers.
	 * <LI> As soon as there is enough data in the internal output buffers,
	 * start sending data to BufOut.
	 * <LI> When InLen is 0, there is no more data to be compressed, so finish
	 * sending data to BufOut until no more data is in the output buffer.
	 * <LI> When there is no more data available, return 1.
	 * 
	 * <LI> There is no obligation to take any data through BufIn or send any
	 * through BufOut. Total Commander will call this function until it either
	 * returns 1, or an error.
	 * </OL>
	 * 
	 * @param hMemPack
	 *            is the handle returned by StartMemPack()
	 * @param bufIn
	 *            is a pointer to the data which needs to be packed
	 * @param inLen
	 *            contains the number of bytes pointed to by BufIn
	 * @param bufOut
	 *            is a pointer to a buffer which can receive packed data
	 * @param outLen
	 *            contains the size of the buffer pointed to by BufOut
	 * @param seekBy
	 *            May by set to the offset from the current output posisition by
	 *            which the file pointer has to be moved BEFORE accepting the
	 *            data in BufOut. This allows the plugin to modify a file header
	 *            also AFTER packing, e.g. to write a CRC to the header.
	 * @param packToMem
	 *            has to receive infos about the operation of this method.
	 * @return PackToMem should return MEMPACK_OK on success, MEMPACK_DONE when
	 *         done, or one of the error values otherwise.
	 */
	int packToMem(final Object hMemPack, final String bufIn, final int inLen,
			final String bufOut, final int outLen, final int seekBy,
			final PackToMem packToMem);

	/**
	 * DoneMemPack ends packing into memory. This function is used together with
	 * StartMemPack and PackToMem. <BR>
	 * It may be called in two different cases:
	 * <OL>
	 * <LI> The packing functions have completed successfully, or
	 * <LI> The user has aborted the packing operation.
	 * </OL>
	 * The plugin should free all data allocated when packing.
	 * 
	 * @param hMemPack
	 *            is the handle returned by StartMemPack.
	 * @return oneMemPack should return SUCCESS, or one of the error codes
	 *         otherwise.
	 */
	int doneMemPack(final Object hMemPack);

	/**
	 * CanYouHandleThisFile allows the plugin to handle files with different
	 * extensions than the one defined in Total Commander. It is called when the
	 * plugin defines PK_CAPS_BY_CONTENT, and the user tries to open an archive
	 * with Ctrl+PageDown.
	 * 
	 * @param fileName
	 *            contains the fully qualified name (path+name) of the file to
	 *            be checked.
	 * @return CanYouHandleThisFile should return true (nonzero) if the plugin
	 *         recognizes the file as an archive which it can handle. The
	 *         detection must be by contents, NOT by extension. If this function
	 *         is not implemented, Totalcmd assumes that only files with a given
	 *         extension can be handled by the plugin.
	 */
	boolean canYouHandleThisFile(final String fileName);

	/**
	 * PackSetDefaultParams is called immediately after loading the DLL, before
	 * any other function. This function is new in version 2.1. It requires
	 * Total Commander >=5.51, but is ignored by older versions. <BR>
	 * <B>Important note:</B></BR>
	 * 
	 * This function is only called in Total Commander 5.51 and later. The
	 * plugin version will be >= 2.1.
	 * 
	 * @param dps
	 *            This object currently contains the version number of the
	 *            plugin interface, and the suggested location for the settings
	 *            file (ini file). It is recommended to store any
	 *            plugin-specific information either directly in that file, or
	 *            in that directory under a different name. Make sure to use a
	 *            unique header when storing data in this file, because it is
	 *            shared by other file system plugins! If your plugin needs more
	 *            than 1kbyte of data, you should use your own ini file because
	 *            ini files are limited to 64k.
	 */
	void packSetDefaultParams(final DefaultParam dps);

	/**
	 * Totalcmd calls ReadHeaderEx to find out what files are in the archive.
	 * This function is always called instead of ReadHeader if it is present. It
	 * only needs to be implemented if the supported archive type may contain
	 * files >2 GB. You should implement both ReadHeader and ReadHeaderEx in
	 * this case, for compatibility with older versions of Total Commander.
	 * <P>
	 * ReadHeaderEx is called as long as it returns zero (as long as the
	 * previous call to this function returned zero). Each time it is called,
	 * HeaderDataEx is supposed to provide Totalcmd with information about the
	 * next file contained in the archive. When all files in the archive have
	 * been returned, ReadHeaderEx should return E_END_ARCHIVE which will
	 * prevent ReaderHeaderEx from being called again. If an error occurs,
	 * ReadHeaderEx should return one of the error values or 0 for no error.
	 * <P>
	 * In short, you are supposed to set at least PackSize, PackSizeHigh,
	 * UnpSize, UnpSizeHigh, FileTime, and FileName members of tHeaderDataEx.
	 * Totalcmd will use this information to display content of the archive when
	 * the archive is viewed as a directory.
	 * 
	 * @param archiveData
	 *            contains the handle returned by OpenArchive. The programmer is
	 *            encouraged to store other information in the location that can
	 *            be accessed via this handle. For example, you may want to
	 *            store the position in the archive when returning files
	 *            information in ReadHeaderEx.
	 * @param headerDataEx
	 *            HeaderDataEx is supposed to provide Totalcmd with information
	 *            about the next file contained in the archive.
	 * @return SUCCESS or E_END_ARCHIVE or error codes E_...
	 */
	int readHeaderEx(final Object archiveData, final HeaderDataEx headerDataEx);

}
