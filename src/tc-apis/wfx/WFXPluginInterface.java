package plugins.wfx;

import java.io.File;

import plugins.DefaultParam;
import plugins.FileTime;
import plugins.wdx.FieldValue;

/**
 * This is the interface for Total Commander WFX plugins.
 * 
 * @author Ken Händel
 */
public interface WFXPluginInterface {

	/**
	 * error code for fsFindFirst(): An invalid file handle indicates an error.
	 */
	Object INVALID_HANDLE_VALUE = null;

	/**
	 * fsExecuteFile: program executed successfully.
	 */
	int FS_EXEC_OK = 0;

	/**
	 * fsExecuteFile: program executed with errors.
	 */
	int FS_EXEC_ERROR = 1;

	/**
	 * fsExecuteFile: Let Total Commander execute the program himself.
	 */
	int FS_EXEC_YOURSELF = -1;

	/**
	 * fsExecuteFile: Let Total Commander switch to that directory.
	 */
	int FS_EXEC_SYMLINK = -2;

	/**
	 * The file was copied/moved OK.
	 */
	int FS_FILE_OK = 0;

	/**
	 * fsRenMovFile: The target file already exists.
	 */
	int FS_FILE_EXISTS = 1;

	/**
	 * fsRenMovFile: The source file couldn't be found or opened.
	 */
	int FS_FILE_NOTFOUND = 2;

	/**
	 * fsRenMovFile: There was an error reading from the source file.
	 */
	int FS_FILE_READERROR = 3;

	/**
	 * fsRenMovFile: There was an error writing to the target file, e.g. disk
	 * full
	 */
	int FS_FILE_WRITEERROR = 4;

	/**
	 * fsRenMovFile: Copying was aborted by the user (through ProgressProc).
	 */
	int FS_FILE_USERABORT = 5;

	/**
	 * fsRenMovFile: The operation is not supported (e.g. resume)
	 */
	int FS_FILE_NOTSUPPORTED = 6;

	/**
	 * fsRenMovFile: not used here.
	 */
	int FS_FILE_EXISTSRESUMEALLOWED = 7;

	/**
	 * fsGetFile, fsPutFile: overwrite without warning.
	 */
	int FS_COPYFLAGS_OVERWRITE = 1;

	/**
	 * fsGetFile, fsPutFile: Resume an aborted or failed transfer.
	 */
	int FS_COPYFLAGS_RESUME = 2;

	/**
	 * fsGetFile, fsPutFile: The plugin needs to delete the remote file after
	 * uploading.
	 */
	int FS_COPYFLAGS_MOVE = 4;

	/**
	 * fsPutFile: The remote file exists and needs to be overwritten.This is a
	 * hint to the plugin to allow optimizations:<BR>
	 * Depending on the plugin type, it may be very slow to check the server for
	 * every single file when uploading.
	 */
	int FS_COPYFLAGS_EXISTS_SAMECASE = 8;

	/**
	 * fsPutFile: The remote file exists and needs to be overwritten.This is a
	 * hint to the plugin to allow optimizations:<BR>
	 * Depending on the plugin type, it may be very slow to check the server for
	 * every single file when uploading.
	 */
	int FS_COPYFLAGS_EXISTS_DIFFERENTCASE = 16;

	/**
	 * fsStatusInfo: Operation starts (allocate buffers if needed).
	 */
	int FS_STATUS_START = 0;

	/**
	 * fsStatusInfo: Operation has ended (free buffers, flush cache etc).
	 */
	int FS_STATUS_END = 1;

	/**
	 * fsStatusInfo: Retrieve a directory listing.
	 */
	int FS_STATUS_OP_LIST = 1;

	/**
	 * fsStatusInfo: Get a single file from the plugin file system.
	 */
	int FS_STATUS_OP_GET_SINGLE = 2;

	/**
	 * fsStatusInfo: Get multiple files, may include subdirs.
	 */
	int FS_STATUS_OP_GET_MULTI = 3;

	/**
	 * fsStatusInfo: Put a single file to the plugin file system.
	 */
	int FS_STATUS_OP_PUT_SINGLE = 4;

	/**
	 * fsStatusInfo: Put multiple files, may include subdirs.
	 */
	int FS_STATUS_OP_PUT_MULTI = 5;

	/**
	 * fsStatusInfo: Rename/Move/Remote copy a single file.
	 */
	int FS_STATUS_OP_RENMOV_SINGLE = 6;

	/**
	 * fsStatusInfo: RenMov multiple files, may include subdirs.
	 */
	int FS_STATUS_OP_RENMOV_MULTI = 7;

	/**
	 * fsStatusInfo: Delete multiple files, may include subdirs.
	 */
	int FS_STATUS_OP_DELETE = 8;

	/**
	 * fsStatusInfo: Change attributes/times, may include subdirs.
	 */
	int FS_STATUS_OP_ATTRIB = 9;

	/**
	 * fsStatusInfo: Create a single directory.
	 */
	int FS_STATUS_OP_MKDIR = 10;

	/**
	 * fsStatusInfo: Start a single remote item, or a command line.
	 */
	int FS_STATUS_OP_EXEC = 11;

	/**
	 * fsStatusInfo: Calculating size of subdir (user pressed SPACE).
	 */
	int FS_STATUS_OP_CALCSIZE = 12;

	/**
	 * fsStatusInfo: Searching for file names only (using
	 * FsFindFirst/Next/Close).
	 */
	int FS_STATUS_OP_SEARCH = 13;

	/**
	 * fsStatusInfo: Searching for file contents (using also FsGetFile() calls).
	 */
	int FS_STATUS_OP_SEARCH_TEXT = 14;

	/**
	 * fsStatusInfo: Synchronize dirs searches subdirs for info.
	 */
	int FS_STATUS_OP_SYNC_SEARCH = 15;

	/**
	 * fsStatusInfo: Synchronize: Downloading files from plugin.
	 */
	int FS_STATUS_OP_SYNC_GET = 16;

	/**
	 * fsStatusInfo: Synchronize: Uploading files to plugin.
	 */
	int FS_STATUS_OP_SYNC_PUT = 17;

	/**
	 * fsStatusInfo: Synchronize: Deleting files from plugin.
	 */
	int FS_STATUS_OP_SYNC_DELETE = 18;

	/**
	 * fsExtractCustomIcon: Requests the small 16x16 icon.
	 */
	int FS_ICONFLAG_SMALL = 1;

	/**
	 * fsExtractCustomIcon: The function is called from the background thread
	 * (see note below).
	 */
	int FS_ICONFLAG_BACKGROUND = 2;

	/**
	 * fsExtractCustomIcon: No icon is returned. The calling app should show the
	 * default icon for this file type.
	 */
	int FS_ICON_USEDEFAULT = 0;

	/**
	 * fsExtractCustomIcon: An icon was returned in TheIcon. The icon must NOT
	 * be freed by the calling app.
	 */
	int FS_ICON_EXTRACTED = 1;

	/**
	 * fsExtractCustomIcon: An icon was returned in TheIcon. The icon MUST be
	 * destroyed by the calling app, e.g. because it was created with
	 * CreateIcon(), or extracted with ExtractIconEx().
	 */
	int FS_ICON_EXTRACTED_DESTROY = 2;

	/**
	 * fsExtractCustomIcon: This return value is only valid if
	 * FS_ICONFLAG_BACKGROUND was NOT set. It tells the calling app to show a
	 * default icon, and request the true icon in a background thread.
	 */
	int FS_ICON_DELAYED = 3;

	/**
	 * fsSetProgress: one hundred percent progress done.
	 */
	int HUNDRED_PERCENT = 100;

	/**
	 * fsLog: Connect to a file system requiring disconnect.
	 */
	int MSGTYPE_CONNECT = 1;

	/**
	 * fsLog: Disconnected successfully.
	 */
	int MSGTYPE_DISCONNECT = 2;

	/**
	 * fsLog: Not so important messages like directory changing.
	 */
	int MSGTYPE_DETAILS = 3;

	/**
	 * fsLog: A file transfer was completed successfully.
	 */
	int MSGTYPE_TRANSFERCOMPLETE = 4;

	/**
	 * fsLog: unused.
	 */
	int MSGTYPE_CONNECTCOMPLETE = 5;

	/**
	 * fsLog: An important error has occured.
	 */
	int MSGTYPE_IMPORTANTERROR = 6;

	/**
	 * fsLog: An operation other than a file transfer has completed.
	 */
	int MSGTYPE_OPERATIONCOMPLETE = 7;

	/**
	 * fsRequest: The requested string is none of the default types.
	 */
	int RT_OTHER = 0;

	/**
	 * fsRequest: Ask for the user name, e.g. for a connection.
	 */
	int RT_USERNAME = 1;

	/**
	 * fsRequest: Ask for a password, e.g. for a connection (shows ***).
	 */
	int RT_PASSWORD = 2;

	/**
	 * fsRequest: Ask for an account (needed for some FTP servers).
	 */
	int RT_ACCOUNT = 3;

	/**
	 * fsRequest: User name for a firewall.
	 */
	int RT_USERNAMEFIREWALL = 4;

	/**
	 * fsRequest: Password for a firewall.
	 */
	int RT_PASSWORDFIREWALL = 5;

	/**
	 * fsRequest: Asks for a local directory (with browse button).
	 */
	int RT_TARGETDIR = 6;

	/**
	 * fsRequest: Asks for an URL.
	 */
	int RT_URL = 7;

	/**
	 * fsRequest: Shows MessageBox with OK button.
	 */
	int RT_MSGOK = 8;

	/**
	 * fsRequest: Shows MessageBox with Yes/No buttons.
	 */
	int RT_MSGYESNO = 9;

	/**
	 * fsRequest: Shows MessageBox with OK/Cancel buttons.
	 */
	int RT_MSGOKCANCEL = 10;

	/**
	 * fsContentGetValue(): called in foreground.
	 */
	int CONTENT_DELAYIFSLOW = 1;

	/**
	 * fsContentGetSupportedFieldFlags(): The plugin allows to edit (modify)
	 * this field.
	 */
	int CONTFLAGS_EDIT = 1;

	/**
	 * fsContentGetSupportedFieldFlags(): Use the file size.
	 */
	int CONTFLAGS_SUBSTSIZE = 2;

	/**
	 * fsContentGetSupportedFieldFlags(): Use the file date+time
	 * (FieldValue.FT_DATETIME).
	 */
	int CONTFLAGS_SUBSTDATETIME = 4;

	/**
	 * fsContentGetSupportedFieldFlags(): Use the file date
	 * (FieldValue.FD_DATE).
	 */
	int CONTFLAGS_SUBSTDATE = 6;

	/**
	 * fsContentGetSupportedFieldFlags(): Use the file time
	 * (FieldValue.FD_TIME).
	 */
	int CONTFLAGS_SUBSTTIME = 8;

	/**
	 * fsContentGetSupportedFieldFlags(): Use the file attributes
	 * (FieldValue.NUMERIC_32 or FieldValue.NUMERIC_64).
	 */
	int CONTFLAGS_SUBSTATTRIBUTES = 10;

	/**
	 * fsContentGetSupportedFieldFlags(): Use the file attribute string in form:
	 * -a-- .
	 */
	int CONTFLAGS_SUBSTATTRIBUTESTR = 12;

	/**
	 * fsContentGetSupportedFieldFlags(): Pass the size as
	 * FieldValue.FT_NUMERIC_FLOATING to contentGetValue.
	 */
	int CONTFLAGS_PASSTHROUGH_SIZE_FLOAT = 14;

	/**
	 * fsContentGetSupportedFieldFlags(): TC will show a button in "change
	 * attributes". This allows plugins to have their own field editors.
	 */
	int CONTFLAGS_FIELDEDIT = 16;

	/**
	 * fsContentGetSupportedFieldFlags(): A combination of all above
	 * substitution flags.
	 */
	int CONTFLAGS_SUBSTMASK = CONTFLAGS_SUBSTSIZE | CONTFLAGS_SUBSTDATETIME
			| CONTFLAGS_SUBSTDATE | CONTFLAGS_SUBSTTIME
			| CONTFLAGS_SUBSTATTRIBUTES;

	/**
	 * fsContentSetValue(): First attribute of this file.
	 */
	int SETFLAGS_FIRST_ATTRIBUTE = 1;

	/**
	 * fsContentSetValue(): Last attribute of this file.
	 */
	int SETFLAGS_LAST_ATTRIBUTE = 2;

	/**
	 * fsContentSetValue(): Only set the date of the datetime value.
	 */
	int SETFLAGS_ONLY_DATE = 4;

	/**
	 * FsInit is called when loading the plugin. The passed values should be
	 * stored in the plugin for later use.
	 * 
	 * @param pluginNr
	 *            Internal number this plugin was given in Total Commander. Has
	 *            to be passed as the first parameter in all callback functions
	 *            so Wincmd knows which plugin has sent the request.
	 * @return The return value is currently unused. You should return 0 when
	 *         successful.
	 */
	int fsInit(final int pluginNr);

	/**
	 * FsFindFirst is called to retrieve the first file in a directory of the
	 * plugin's file system. <BR>
	 * 
	 * <B>Important note:</B><BR>
	 * FsFindFirst may be called directly with a subdirectory of the plugin! You
	 * cannot rely on it being called with the root \ after it is loaded.
	 * Reason: Users may have saved a subdirectory to the plugin in the Ctrl+D
	 * directory hotlist in a previous session with the plugin.
	 * 
	 * @param path
	 *            Full path to the directory for which the directory listing has
	 *            to be retrieved. Important: no wildcards are passed to the
	 *            plugin! All separators will be backslashes, so you will need
	 *            to convert them to forward slashes if your file system uses
	 *            them! As root, a single backslash is passed to the plugin. The
	 *            root items appear in the plugin base directory retrieved by
	 *            FsGetDefRootName at installation time. This default root name
	 *            is NOT part of the path passed to the plugin! All subdirs are
	 *            built from the directory names the plugin returns through
	 *            FsFindFirst and FsFindNext, separated by single backslashes,
	 *            e.g. \Some server\c:\subdir
	 * @param lastFindData
	 *            Contains the file or directory details. Use the
	 *            dwFileAttributes field set to FILE_ATTRIBUTE_DIRECTORY to
	 *            distinguish files from directories. On Unix systems, you can |
	 *            (or) the dwFileAttributes field with 0x80000000 and set the
	 *            dwReserved0 parameter to the Unix file mode (permissions).
	 * 
	 * @return Return INVALID_HANDLE_VALUE if an error occurs, or an object if
	 *         not. It is recommended to pass an instance to an internal class,
	 *         which stores the current state of the search. This will allow
	 *         recursive directory searches needed for copying whole trees. This
	 *         instance will be passed as parameter 'handle' to FsFindNext() by
	 *         the calling program.
	 */
	Object fsFindFirst(final String path, final Win32FindData lastFindData);

	/**
	 * FsFindNext is called to retrieve the next file in a directory of the
	 * plugin's file system.
	 * 
	 * @param handle
	 *            The object returned by FsFindFirst.
	 * @param findData
	 *            Contains the file or directory details. Use the
	 *            dwFileAttributes field set to FILE_ATTRIBUTE_DIRECTORY to
	 *            distinguish files from directories. On Unix systems, you can |
	 *            (or) the dwFileAttributes field with 0x80000000 and set the
	 *            dwReserved0 parameter to the Unix file mode (permissions).
	 * @return Return false if an error occurs or if there are no more files,
	 *         and true otherwise.
	 */
	boolean fsFindNext(final Object handle, final Win32FindData findData);

	/**
	 * FsFindClose is called to end a FsFindFirst/FsFindNext loop, either after
	 * retrieving all files, or when the user aborts it.
	 * 
	 * @param handle
	 *            The find handle returned by FsFindFirst.
	 * @return Currently unused, should return 0.
	 * 
	 * 
	 */
	int fsFindClose(final Object handle);

	/**
	 * FsGetDefRootName is called only when the plugin is installed. It asks the
	 * plugin for the default root name which should appear in the Network
	 * Neighborhood. This root name is NOT part of the path passed to the plugin
	 * when Wincmd accesses the plugin file system! The root will always be "\",
	 * and all subpaths will be built from the directory names returned by the
	 * plugin.<BR>
	 * 
	 * Example: The root name may be "Linux file system" for a plugin which
	 * accesses Linux drives. If this function isn't implemented, Wincmd will
	 * suggest the name of the DLL (without extension .DLL) as the plugin root.
	 * This function is called directly after loading the plugin (when the user
	 * installs it), FsInit() is NOT called when installing the plugin.
	 * 
	 * @param maxlen
	 *            Maximum number of characters (including the final 0) which fit
	 *            in the buffer.
	 * @return root name.
	 */
	String fsGetDefRootName(final int maxlen);

	/**
	 * FsGetFile is called to transfer a file from the plugin's file system to
	 * the normal file system (drive letters or UNC).<BR>
	 * 
	 * <B>Important notes:</B><BR>
	 * Total Commander usually calls this function twice:
	 * <UL>
	 * <LI>once with CopyFlags==0 or CopyFlags==FS_COPYFLAGS_MOVE. If the local
	 * file exists and resume is supported, return FS_FILE_EXISTSRESUMEALLOWED.
	 * If resume isn't allowed, return FS_FILE_EXISTS
	 * <LI> a second time with FS_COPYFLAGS_RESUME or FS_COPYFLAGS_OVERWRITE,
	 * depending on the user's choice.
	 * <LI> The resume option is only offered to the user if
	 * FS_FILE_EXISTSRESUMEALLOWED was returned by the first call.
	 * <LI> FS_COPYFLAGS_EXISTS_SAMECASE and FS_COPYFLAGS_EXISTS_DIFFERENTCASE
	 * are NEVER passed to this function, because the plugin can easily
	 * determine whether a local file exists or not.
	 * <LI> FS_COPYFLAGS_MOVE is set, the plugin needs to delete the remote file
	 * after a successful download.
	 * </UL>
	 * While copying the file, but at least at the beginning and the end, call
	 * ProgressProc to show the copy progress and allow the user to abort the
	 * operation.
	 * 
	 * @param remoteName
	 *            Name of the file to be retrieved, with full path. The name
	 *            always starts with a backslash, then the names returned by
	 *            FsFindFirst/FsFindNext separated by backslashes.
	 * @param localName
	 *            Local file name with full path, either with a drive letter or
	 *            UNC path (\\Server\Share\filename). The plugin may change the
	 *            NAME/EXTENSION of the file (e.g. when file conversion is
	 *            done), but not the path!
	 * @param copyFlags
	 *            Can be a combination of the following three flags:<BR>
	 *            FS_COPYFLAGS_OVERWRITE: If set, overwrite any existing file
	 *            without asking. If not set, simply fail copying.<BR>
	 *            FS_COPYFLAGS_RESUME: Resume an aborted or failed transfer.<BR>
	 *            FS_COPYFLAGS_MOVE: The plugin needs to delete the remote file
	 *            after uploading.<BR>
	 *            See above for important notes!
	 * @param remoteInfo
	 *            This parameter contains information about the remote file
	 *            which was previously retrieved via FsFindFirst/FsFindNext: The
	 *            size, date/time, and attributes of the remote file. May be
	 *            useful to copy the attributes with the file, and for
	 *            displaying a progress dialog.
	 * @return Return one of the following values:<BR>
	 * 
	 * FS_FILE_OK The file was copied OK<BR>
	 * 
	 * FS_FILE_EXISTS The local file already exists, and resume is not
	 * supported.<BR>
	 * FS_FILE_NOTFOUND The remote file couldn't be found or opened.<BR>
	 * FS_FILE_READERROR There was an error reading from the remote file.<BR>
	 * FS_FILE_WRITEERROR There was an error writing to the local file, e.g.
	 * disk full.<BR>
	 * FS_FILE_USERABORT Copying was aborted by the user (through ProgressProc).
	 * FS_FILE_NOTSUPPORTED The operation is not supported (e.g. resume).<BR>
	 * FS_FILE_EXISTSRESUMEALLOWED The local file already exists, and resume is
	 * supported.
	 */
	int fsGetFile(final String remoteName, final String localName,
			final int copyFlags, final RemoteInfo remoteInfo);

	/**
	 * FsPutFile is called to transfer a file from the normal file system (drive
	 * letters or UNC) to the plugin's file system.<BR>
	 * 
	 * <B>Important notes:</B><BR>
	 * Total Commander usually calls this function twice, with the following
	 * parameters in CopyFlags:
	 * <UL>
	 * <LI> once with neither FS_COPYFLAGS_RESUME nor FS_COPYFLAGS_OVERWRITE
	 * set. If the remote file exists and resume is supported, return
	 * FS_FILE_EXISTSRESUMEALLOWED. If resume isn't allowed, return
	 * FS_FILE_EXISTS
	 * <LI> a second time with FS_COPYFLAGS_RESUME or FS_COPYFLAGS_OVERWRITE,
	 * depending on the user's choice. The resume option is only offered to the
	 * user if FS_FILE_EXISTSRESUMEALLOWED was returned by the first call.
	 * <LI> The flags FS_COPYFLAGS_EXISTS_SAMECASE or
	 * FS_COPYFLAGS_EXISTS_DIFFERENTCASE are added to CopyFlags when the remote
	 * file exists and needs to be overwritten. This is a hint to the plugin to
	 * allow optimizations: Depending on the plugin type, it may be very slow to
	 * check the server for every single file when uploading.
	 * 
	 * <LI> If the flag FS_COPYFLAGS_MOVE is set, the plugin needs to delete the
	 * local file after a successful upload.
	 * </UL>
	 * While copying the file, but at least at the beginning and the end, call
	 * ProgressProc to show the copy progress and allow the user to abort the
	 * operation.
	 * 
	 * @param localName
	 *            Local file name with full path, either with a drive letter or
	 *            UNC path (\\Server\Share\filename). This file needs to be
	 *            uploaded to the plugin's file system.
	 * @param remoteName
	 *            Name of the remote file, with full path. The name always
	 *            starts with a backslash, then the names returned by
	 *            FsFindFirst/FsFindNext separated by backslashes. The plugin
	 *            may change the NAME/EXTENSION of the file (e.g. when file
	 *            conversion is done), but not the path!
	 * @param copyFlags
	 *            Can be a combination of the FS_COPYFLAGS_xxx flags<BR>
	 *            FS_COPYFLAGS_OVERWRITE: If set, overwrite any existing file
	 *            without asking. If not set, simply fail copying.<BR>
	 *            FS_COPYFLAGS_RESUME: Resume an aborted or failed transfer.<BR>
	 *            FS_COPYFLAGS_MOVE: The plugin needs to delete the local file
	 *            after uploading.<BR>
	 *            FS_COPYFLAGS_EXISTS_SAMECASE: A hint from the calling program:
	 *            The remote file exists and has the same case (upper/lowercase)
	 *            as the local file.<BR>
	 *            FS_COPYFLAGS_EXISTS_DIFFERENTCASE: A hint from the calling
	 *            program: The remote file exists and has different case
	 *            (upper/lowercase) than the local file.<BR>
	 *            See above for important notes!
	 * @return Return one of the following values:<BR>
	 *         FS_FILE_OK The file was copied OK.<BR>
	 *         FS_FILE_EXISTS The remote file already exists, and resume is not
	 *         supported.<BR>
	 *         FS_FILE_NOTFOUND The local file couldn't be found or opened.<BR>
	 *         FS_FILE_READERROR There was an error reading from the local file.
	 *         FS_FILE_WRITEERROR There was an error writing to the remote file,
	 *         e.g. disk full.<BR>
	 *         FS_FILE_USERABORT Copying was aborted by the user (through
	 *         ProgressProc.<BR>
	 *         FS_FILE_NOTSUPPORTED The operation is not supported (e.g. resume)
	 *         FS_FILE_EXISTSRESUMEALLOWED The remote file already exists, and
	 *         resume is supported
	 */
	int fsPutFile(final String localName, final String remoteName,
			final int copyFlags);

	/**
	 * FsRenMovFile is called to transfer (copy or move) a file within the
	 * plugin's file system. <BR>
	 * 
	 * <B>Important notes:</B><BR>
	 * Total Commander usually calls this function twice: - once with
	 * OverWrite==false. If the remote file exists, return FS_FILE_EXISTS. If it
	 * doesn't exist, try to copy the file, and return an appropriate error
	 * code. - a second time with OverWrite==true, if the user chose to
	 * overwrite the file.
	 * 
	 * While copying the file, but at least at the beginning and the end, call
	 * ProgressProc to show the copy progress and allow the user to abort the
	 * operation.
	 * 
	 * @param oldName
	 *            Name of the remote source file, with full path. The name
	 *            always starts with a backslash, then the names returned by
	 *            FsFindFirst/FsFindNext separated by backslashes.
	 * @param newName
	 *            Name of the remote destination file, with full path. The name
	 *            always starts with a backslash, then the names returned by
	 *            FsFindFirst/FsFindNext separated by backslashes.
	 * @param move
	 *            If true, the file needs to be moved to the new location and
	 *            name. Many file systems allow to rename/move a file without
	 *            actually moving any of its data, only the pointer to it.
	 * @param overwrite
	 *            Tells the function whether it should overwrite the target file
	 *            or not. See notes below on how this parameter is used.
	 * @param remoteInfo
	 *            An instance of class RemoteInfo which contains the parameters
	 *            of the file being renamed/moved (not of the target file!). In
	 *            TC 5.51, the fields are set as follows for directories:
	 *            SizeLow=0, SizeHigh=0xFFFFFFFF.
	 * @return Return one of the following values:
	 *         <UL>
	 *         <LI> FS_FILE_OK The file was copied/moved OK
	 *         <LI>FS_FILE_EXISTS The target file already exists
	 *         <LI>FS_FILE_NOTFOUND The source file couldn't be found or
	 *         opened.
	 *         <LI>FS_FILE_READERROR There was an error reading from the source
	 *         file
	 *         <LI>FS_FILE_WRITEERROR There was an error writing to the target
	 *         file, e.g. disk full
	 *         <LI>FS_FILE_USERABORT Copying was aborted by the user (through
	 *         ProgressProc)
	 *         <LI>FS_FILE_NOTSUPPORTED The operation is not supported (e.g.
	 *         resume)
	 *         <LI>FS_FILE_EXISTSRESUMEALLOWED not used here
	 *         </UL>
	 */
	int fsRenMovFile(final String oldName, final String newName,
			final boolean move, final boolean overwrite, RemoteInfo remoteInfo);

	/**
	 * FsDeleteFile is called to delete a file from the plugin's file system.
	 * 
	 * @param remoteName
	 *            RemoteName Name of the file to be deleted, with full path. The
	 *            name always starts with a backslash, then the names returned
	 *            by FsFindFirst/FsFindNext separated by backslashes.
	 * @return Return TRUE if the file could be deleted, FALSE if not.
	 */
	boolean fsDeleteFile(final String remoteName);

	/**
	 * FsRemoveDir is called to remove a directory from the plugin's file
	 * system.
	 * 
	 * @param remoteName
	 *            Name of the directory to be removed, with full path. The name
	 *            always starts with a backslash, then the names returned by
	 *            FsFindFirst/FsFindNext separated by backslashes.
	 * @return Return TRUE if the directory could be removed, FALSE if not.
	 */
	boolean fsRemoveDir(final String remoteName);

	/**
	 * FsMkDir is called to create a directory on the plugin's file system.
	 * 
	 * @param path
	 *            Name of the directory to be created, with full path. The name
	 *            always starts with a backslash, then the names returned by
	 *            FsFindFirst/FsFindNext separated by backslashes.
	 * @return Return TRUE if the directory could be created, FALSE if not.
	 */
	boolean fsMkDir(final String path);

	/**
	 * FsExecuteFile is called to execute a file on the plugin's file system, or
	 * show its property sheet. It is also called to show a plugin configuration
	 * dialog when the user right clicks on the plugin root and chooses
	 * 'properties'. The plugin is then called with RemoteName="\" and
	 * Verb="properties" (requires TC>=5.51).
	 * 
	 * @param mainWin
	 *            Parent window which can be used for showing a property sheet.
	 * @param remoteName
	 *            Name of the file to be executed, with full path.
	 * @param verb
	 *            This can be either "open", "properties", "chmod" or "quote"
	 *            (case-insensitive). open: This is called when the user presses
	 *            ENTER on a file. There are three ways to handle it: a) For
	 *            internal commands like "Add new connection", execute it in the
	 *            plugin and return FS_EXEC_OK or FS_EXEC_ERROR b) Let Total
	 *            Commander download the file and execute it locally: return
	 *            FS_EXEC_YOURSELF c) If the file is a (symbolic) link, set
	 *            RemoteName to the location to which the link points (including
	 *            the full plugin path), and return FS_EXEC_SYMLINK. Total
	 *            Commander will then switch to that directory. You can also
	 *            switch to a directory on the local harddisk! To do this,
	 *            return a path starting either with a drive letter, or an UNC
	 *            location (\\server\share). The maximum allowed length of such
	 *            a path is MAX_PATH-1 = 259 characters! properties: Show a
	 *            property sheet for the file (optional). Currently not handled
	 *            by internal Wincmd functions if FS_EXEC_YOURSELF is returned,
	 *            so the plugin needs to do it internally. chmod xxx: The xxx
	 *            stands for the new Unix mode (attributes) to be applied to the
	 *            file RemoteName. This verb is only used when returning Unix
	 *            attributes through FsFindFirst/FsFindNext quote commandline:
	 *            Execute the command line entered by the user in the directory
	 *            RemoteName . This is called when the user enters a command in
	 *            Wincmd's command line, and presses ENTER. This is optional,
	 *            and allows to send plugin-specific commands. It's up to the
	 *            plugin writer what to support here. If the user entered e.g. a
	 *            cd directory command, you can return the new path in
	 *            RemoteName (max 259 characters), and give FS_EXEC_SYMLINK as
	 *            return value. Return FS_EXEC_OK to cause a refresh (re-read)
	 *            of the active panel.
	 * 
	 * @return Return FS_EXEC_YOURSELF if Total Commander should download the
	 *         file and execute it locally, FS_EXEC_OK if the command was
	 *         executed successfully in the plugin (or if the command isn't
	 *         applicable and no further action is needed), FS_EXEC_ERROR if
	 *         execution failed, or FS_EXEC_SYMLINK if this was a (symbolic)
	 *         link or .lnk file pointing to a different directory.
	 */
	int fsExecuteFile(final int mainWin, final String remoteName,
			final String verb);

	/**
	 * FsSetAttr is called to set the (Windows-Style) file attributes of a
	 * file/dir. FsExecuteFile is called for Unix-style attributes.
	 * 
	 * @param remoteName
	 *            Name of the file/directory whose attributes have to be set
	 * @param newAttr
	 *            New file attributes. These are a commbination of the following
	 *            standard file attributes:
	 *            <UL>
	 *            <LI>Win32FindData.FILE_ATTRIBUTE_READONLY
	 *            <LI>Win32FindData.FILE_ATTRIBUTE_HIDDEN
	 *            <LI>Win32FindData.FILE_ATTRIBUTE_SYSTEM
	 *            <LI>Win32FindData.FILE_ATTRIBUTE_ARCHIVE
	 *            </UL>
	 * @return Return TRUE if successful, FALSE if the function failed. Do NOT
	 *         export this function if it isn't supported by your plugin! See
	 *         also: fsExecuteFile();
	 */
	boolean fsSetAttr(final String remoteName, final int newAttr);

	/**
	 * FsSetTime is called to set the (Windows-Style) file times of a file/dir.
	 * 
	 * @param remoteName
	 *            Name of the file/directory whose attributes have to be set
	 * @param creationTime
	 *            Creation time of the file. May be NULL to leave it unchanged.
	 * @param lastAccessTime
	 *            Last access time of the file. May be NULL to leave it
	 *            unchanged.
	 * @param lastWriteTime
	 *            Last write time of the file. May be NULL to leave it
	 *            unchanged. If your file system only supports one time, use
	 *            this parameter!
	 * @return Return TRUE if successful, FALSE if the function failed. Do NOT
	 *         export this function if it isn't supported by your plugin!
	 */
	boolean fsSetTime(final String remoteName, final FileTime creationTime,
			final FileTime lastAccessTime, final FileTime lastWriteTime);

	/**
	 * FsDisconnect is called when the user presses the Disconnect button in the
	 * FTP connections toolbar. This toolbar is only shown if MSGTYPE_CONNECT is
	 * passed to LogProc().<BR>
	 * 
	 * <B>Important note:</B><BR>
	 * To get calls to this function, the plugin MUST call LogProc with the
	 * parameter MSGTYPE_CONNECT. The parameter LogString MUST start with
	 * "CONNECT", followed by one whitespace and the root of the file system
	 * which has been connected. This file system root will be passed to
	 * FsDisconnect when the user presses the Disconnect button, so the plugin
	 * knows which connection to close.<BR>
	 * Do NOT call LogProc with MSGTYPE_CONNECT if your plugin does not require
	 * connect/disconnect!<BR>
	 * Examples:
	 * <UL>
	 * <LI> FTP requires connect/disconnect. Connect can be done automatically
	 * when the user enters a subdir, disconnect when the user clicks the
	 * Disconnect button.
	 * <LI> Access to local file systems (e.g. Linux EXT2) does not require
	 * connect/disconnect, so don't call LogProc with the parameter
	 * MSGTYPE_CONNECT.
	 * </UL>
	 * 
	 * @param disconnectRoot
	 *            This is the root dir which was passed to LogProc when
	 *            connecting. It allows the plugin to have serveral open
	 *            connections to different file systems (e.g. ftp servers).
	 *            Should be either \ (for a single possible connection) or
	 *            \Servername (e.g. when having multiple open connections).
	 * @return Return TRUE if the connection was closed (or never open), FALSE
	 *         if it couldn't be closed.
	 */
	boolean fsDisconnect(final String disconnectRoot);

	/**
	 * FsStatusInfo is called just as an information to the plugin that a
	 * certain operation starts or ends. It can be used to allocate/free
	 * buffers, and/or to flush data from a cache. There is no need to implement
	 * this function if the plugin doesn't require it.<BR>
	 * Please note that future versions of the framework may send additional
	 * values!<BR>
	 * 
	 * <B>Important note:</B><BR>
	 * This function has been added for the convenience of plugin writers. All
	 * calls to plugin functions will be enclosed in a pair of FsStatusInfo()
	 * calls: At the start, FsStatusInfo(...,FS_STATUS_START,...) and when the
	 * operation is done FsStatusInfo(...,FS_STATUS_END,...). Multiple plugin
	 * calls can be between these two calls. For example, a download may contain
	 * multiple calls to FsGetFile(), and FsFindFirst(), FsFindNext(),
	 * FsFindClose() (for copying subdirs).
	 * 
	 * @param remoteDir
	 *            RemoteDir This is the current source directory when the
	 *            operation starts. May be used to find out which part of the
	 *            file system is affected.
	 * @param infoStartEnd
	 *            Information whether the operation starts or ends. Possible
	 *            values:
	 *            <UL>
	 *            <LI>FS_STATUS_START Operation starts (allocate buffers if
	 *            needed)
	 *            <LI>FS_STATUS_END Operation has ended (free buffers, flush
	 *            cache etc)
	 *            </UL>
	 * 
	 * @param infoOperation
	 *            Information of which operaration starts/ends. Possible values:
	 *            <UL>
	 *            <LI>FS_STATUS_OP_LIST Retrieve a directory listing
	 *            <LI>FS_STATUS_OP_GET_SINGLE Get a single file from the plugin
	 *            file system
	 *            <LI>FS_STATUS_OP_GET_MULTI Get multiple files, may include
	 *            subdirs
	 *            <LI>FS_STATUS_OP_PUT_SINGLE Put a single file to the plugin
	 *            file system
	 *            <LI>FS_STATUS_OP_PUT_MULTI Put multiple files, may include
	 *            subdirs
	 *            <LI>FS_STATUS_OP_RENMOV_SINGLE Rename/Move/Remote copy a
	 *            single file
	 *            <LI>FS_STATUS_OP_RENMOV_MULTI RenMov multiple files, may
	 *            include subdirs
	 *            <LI>FS_STATUS_OP_DELETE Delete multiple files, may include
	 *            subdirs
	 *            <LI>FS_STATUS_OP_ATTRIB Change attributes/times, may include
	 *            subdirs
	 *            <LI>FS_STATUS_OP_MKDIR Create a single directory
	 *            <LI>FS_STATUS_OP_EXEC Start a single remote item, or a
	 *            command line
	 *            <LI>FS_STATUS_OP_CALCSIZE Calculating size of subdir (user
	 *            pressed SPACE)
	 *            <LI>FS_STATUS_OP_SEARCH Searching for file names only (using
	 *            FsFindFirst/Next/Close)
	 *            <LI>FS_STATUS_OP_SEARCH_TEXT Searching for file contents
	 *            (using also FsGetFile() calls)
	 *            <LI>FS_STATUS_OP_SYNC_SEARCH Synchronize dirs searches
	 *            subdirs for info
	 *            <LI>FS_STATUS_OP_SYNC_GET Synchronize: Downloading files from
	 *            plugin
	 *            <LI>FS_STATUS_OP_SYNC_PUT Synchronize: Uploading files to
	 *            plugin
	 *            <LI>FS_STATUS_OP_SYNC_DELETE Synchronize: Deleting files from
	 *            plugin
	 *            </UL>
	 */
	void fsStatusInfo(final String remoteDir, final int infoStartEnd,
			final int infoOperation);

	/**
	 * FsExtractCustomIcon is called when a file/directory is displayed in the
	 * file list. It can be used to specify a custom icon for that
	 * file/directory. This function is new in version 1.1. It requires Total
	 * Commander >=5.51, but is ignored by older versions.<BR>
	 * 
	 * <B>Important note:</B><BR>
	 * If you return FS_ICON_DELAYED, FsExtractCustomIcon() will be called again
	 * from a background thread at a later time. A critical section is used by
	 * the calling app to ensure that FsExtractCustomIcon() is never entered
	 * twice at the same time. This return value should be used for icons which
	 * take a while to extract, e.g. EXE icons. In the fsplugin sample plugin,
	 * the drive icons are returned immediately (because they are stored in the
	 * plugin itself), but the EXE icons are loaded with a delay. If the user
	 * turns off background loading of icons, the function will be called in the
	 * foreground with the FS_ICONFLAG_BACKGROUND flag.<BR>
	 * 
	 * <B>How to define an icon</B><BR>
	 * Each plugin can have an icon in Network Neighborhood to the left of its
	 * name. Wincmd will load the FIRST icon it can find (by index) in the
	 * plugin DLL, so just include a resource file with exactly one icon in it.
	 * The icon should contain at least one image with 16x16 pixels, although
	 * larger images will be scaled down for displaying in Wincmd. If no icon is
	 * contained within the plugin DLL, Wincmd will show the default folder
	 * icon. The icons in subfolders will be determined with the normal file
	 * association process in Windows.
	 * 
	 * @param remoteName
	 *            This is the full path to the file or directory whose icon is
	 *            to be retrieved. When extracting an icon, you can return an
	 *            icon name here - this ensures that the icon is only cached
	 *            once in the calling program. The returned icon name must not
	 *            be longer than MAX_PATH characters (including terminating 0!).
	 *            The icon handle must still be returned in TheIcon!
	 * @param extractFlags
	 *            Flags for the extract operation. A combination of the
	 *            following:
	 *            <UL>
	 *            <LI>FS_ICONFLAG_SMALL Requests the small 16x16 icon
	 *            <LI>FS_ICONFLAG_BACKGROUND The function is called from the
	 *            background thread (see note below)
	 *            </UL>
	 * 
	 * @param theIcon
	 *            Here you need to return the icon handle. Three forms are
	 *            supported:
	 *            <ol>
	 *            <li>theIcon.append ("253|shell32.dll"); // load icon from a
	 *            resource (EXE/DLL), referenced by a resource id
	 *            <li>theIcon.append
	 *            ("G:\\Totalcmd\\plugins\\java\\Drives\\test.ico"); // load
	 *            icon from ico file (absolute path name)
	 *            <li>theIcon.append ("%CWD%\\test.ico"); // load icon from ico
	 *            file in the plugin directory
	 *            </ol>
	 * @return The function has to return one of the following values:
	 *         <UL>
	 *         <LI>FS_ICON_USEDEFAULT No icon is returned. The calling app
	 *         should show the default icon for this file type.
	 *         <LI>FS_ICON_EXTRACTED An icon was returned in TheIcon. The icon
	 *         must NOT be freed by the calling app, e.g. because it was loaded
	 *         with LoadIcon, or the DLL handles destruction of the icon.
	 *         <LI>FS_ICON_EXTRACTED_DESTROY An icon was returned in TheIcon.
	 *         The icon MUST be destroyed by the calling app, e.g. because it
	 *         was created with CreateIcon(), or extracted with ExtractIconEx().
	 *         <LI>FS_ICON_DELAYED This return value is only valid if
	 *         FS_ICONFLAG_BACKGROUND was NOT set. It tells the calling app to
	 *         show a default icon, and request the true icon in a background
	 *         thread. See note below.
	 *         </UL>
	 */
	int fsExtractCustomIcon(final String remoteName, final int extractFlags,
			final StringBuffer theIcon);

	/**
	 * FsSetDefaultParams is called immediately after FsInit(). This function is
	 * new in version 1.3. It requires Total Commander >=5.51, but is ignored by
	 * older versions.<BR>
	 * 
	 * <B>Important note:</B><BR>
	 * This function is only called in Total Commander 5.51 and later. The
	 * plugin version will be >= 1.3.
	 * 
	 * @param dps
	 *            This structure of type FsDefaultParamStruct currently contains
	 *            the version number of the plugin interface, and the suggested
	 *            location for the settings file (ini file). It is recommended
	 *            to store any plugin-specific information either directly in
	 *            that file, or in that directory under a different name. Make
	 *            sure to use a unique header when storing data in this file,
	 *            because it is shared by other file system plugins! If your
	 *            plugin needs more than 1kbyte of data, you should use your own
	 *            ini file because ini files are limited to 64k.
	 */
	void fsSetDefaultParams(final DefaultParam dps);

	/***************************************************************************
	 * FsGetPreviewBitmap is called when a file/directory is displayed in
	 * thumbnail view. It can be used to return a custom bitmap for that
	 * file/directory. This function is new in version 1.4. It requires Total
	 * Commander >=7.0, but is ignored by older versions.
	 * 
	 * @param remoteName
	 *            This is the full path to the file or directory whose bitmap is
	 *            to be retrieved. When extracting a bitmap, you can return a
	 *            bitmap name here - this ensures that the icon is only cached
	 *            once in the calling program. The returned bitmap name must not
	 *            be longer than MAX_PATH characters (including terminating 0!).
	 *            The bitmap handle must still be returned in ReturnedBitmap!
	 * 
	 * 
	 * @param width
	 *            The maximum width of the preview bitmap. If your image is
	 *            smaller, or has a different side ratio, then you need to
	 *            return an image which is smaller than these dimensions! See
	 *            notes below!
	 * 
	 * @param height
	 *            The maximum height of the preview bitmap. If your image is
	 *            smaller, or has a different side ratio, then you need to
	 *            return an image which is smaller than these dimensions! See
	 *            notes below!
	 * 
	 * @param filename
	 *            Here you need to return the bitmap handle. Three forms are
	 *            supported:
	 *            <ol>
	 *            <li>filename.append ("253|shell32.dll"); // load bitmap from
	 *            a resource (EXE/DLL), referenced by a resource id
	 *            <li>filename.append
	 *            ("G:\\Totalcmd\\plugins\\java\\Drives\\test.bmp"); // load
	 *            bitmap from bmp file (absolute path name)
	 *            <li>filename.append ("%CWD%\\test.bmp"); // load bitmap from
	 *            bmp file in the plugin directory
	 *            </ol>
	 * @return The function has to return one of the following values:
	 *         <UL>
	 *         <LI>FS_BITMAP_NONE There is no preview bitmap.
	 * 
	 * <LI>FS_BITMAP_EXTRACTED The image was extracted and is returned in
	 * ReturnedBitmap
	 * <LI>FS_BITMAP_EXTRACT_YOURSELF Tells the caller to extract the image by
	 * itself. The full local path to the file needs to be returned in
	 * RemoteName. The returned bitmap name must not be longer than MAX_PATH.
	 * <LI>FS_BITMAP_EXTRACT_YOURSELF_ANDDELETE Tells the caller to extract the
	 * image by itself, and then delete the temporary image file. The full local
	 * path to the temporary image file needs to be returned in RemoteName. The
	 * returned bitmap name must not be longer than MAX_PATH. In this case, the
	 * plugin downloads the file to TEMP and then asks TC to extract the image.
	 * 
	 * <LI>FS_BITMAP_CACHE This value must be ADDED to one of the above values
	 * if the caller should cache the image. Do NOT add this image if you will
	 * cache the image yourself!
	 * </UL>
	 * 
	 * <B>Important notes:</B><BR>
	 * <OL>
	 * <LI>This function is only called in Total Commander 7.0 and later. The
	 * reported plugin version will be >= 1.4.
	 * 
	 * <LI>The bitmap handle goes into possession of Total Commander, which
	 * will delete it after using it. The plugin must not delete the bitmap
	 * handle!
	 * 
	 * <LI>
	 * 
	 * <LI>Make sure you scale your image correctly to the desired maximum
	 * width+height! Do not fill the rest of the bitmap - instead, create a
	 * bitmap which is SMALLER than requested! This way, Total Commander can
	 * center your image and fill the rest with the default background color.
	 * </OL>
	 */
	int fsGetPreviewBitmap(String remoteName, int width, int height,
			final StringBuffer filename);

	/**
	 * FsLinksToLocalFiles must not be implemented unless your plugin is a
	 * temporary file panel plugin! Temporary file panels just hold links to
	 * files on the local file system.
	 * 
	 * @return The function has to return one of the following values:
	 *         <UL>
	 *         <LI>true The plugin is a temporary panel-style plugin
	 * 
	 * <LI>false The plugin is a normal file system plugin
	 * </UL>
	 * 
	 * <B>Important note:</B><BR>
	 * If your plugin is a temporary panel plugin, the following functions MUST
	 * be thread-safe (can be called from background transfer manager):
	 * <UL>
	 * <LI>FsLinksToLocalFiles
	 * <LI>FsFindFirst
	 * <LI>FsFindNext
	 * <LI>FsFindClose
	 * <LI>FsGetLocalName
	 * </UL>
	 * This means that when uploading subdirectories from your plugin to FTP in
	 * the background, Total Commander will call these functions in a background
	 * thread. If the user continues to work in the foreground, calls to
	 * FsFindFirst and FsFindNext may be occuring at the same time! Therefore
	 * it's very important to use the search handle to keep temporary
	 * information about the search. <BR>
	 * FsStatusInfo will NOT be called from the background thread!
	 * 
	 */
	boolean fsLinksToLocalFiles();

	/**
	 * 
	 * FsGetLocalName must not be implemented unless your plugin is a temporary
	 * file panel plugin! Temporary file panels just hold links to files on the
	 * local file system.
	 * 
	 * @param remoteName
	 *            In: Full path to the file name in the plugin namespace, e.g.
	 *            \somedir\file.ext <BR>
	 *            Out: Return the path of the file on the local file system,
	 *            e.g. c:\windows\file.ext
	 * 
	 * @param maxlen
	 *            Maximum number of characters you can return in RemoteName,
	 *            including the final 0.
	 * 
	 * @return The function has to return one of the following values:
	 *         <UL>
	 *         <LI>true The name points to a local file, which is returned in
	 *         RemoteName.
	 * 
	 * <LI>false The name does not point to a local file, RemoteName is left
	 * unchanged.
	 * </UL>
	 * 
	 * <B>Important note:</B><BR>
	 * If your plugin is a temporary panel plugin, the following functions MUST
	 * be thread-safe (can be called from background transfer manager):
	 * <UL>
	 * <LI>FsGetLocalName
	 * <LI>FsLinksToLocalFiles
	 * <LI>FsFindFirst
	 * <LI>FsFindNext
	 * <LI>FsFindClose
	 * </UL>
	 * This means that when uploading subdirectories from your plugin to FTP in
	 * the background, Total Commander will call these functions in a background
	 * thread. If the user continues to work in the foreground, calls to
	 * FsFindFirst and FsFindNext may be occuring at the same time! Therefore
	 * it's very important to use the search handle to keep temporary
	 * information about the search.
	 * 
	 * FsStatusInfo will NOT be called from the background thread!
	 * 
	 */
	boolean fsGetLocalName(String remoteName, int maxlen);

	/**
	 * FsContentGetSupportedField is called to enumerate all supported fields.
	 * FieldIndex is increased by 1 starting from 0 until the plugin returns
	 * FT_NOMOREFIELDS.
	 * 
	 * This function is identical to the function ContentGetSupportedField in
	 * Content plugins, except that FT_FULLTEXT isn't currently supported.
	 * 
	 * @param fieldIndex
	 *            The index of the field for which TC requests information.
	 *            Starting with 0, the FieldIndex is increased until the plugin
	 *            returns an error.
	 * 
	 * @param fieldName
	 *            Here the plugin has to return the name of the field with index
	 *            FieldIndex. The field may not contain the following chars: .
	 *            (dot) | (vertical line) : (colon). You may return a maximum of
	 *            maxlen characters, including the trailing 0.
	 * 
	 * @param units
	 *            When a field supports several units like bytes, kbytes, Mbytes
	 *            etc, they need to be specified here in the following form:
	 *            bytes|kbytes|Mbytes . The separator is the vertical dash
	 *            (Alt+0124). As field names, unit names may not contain a
	 *            vertical dash, a dot, or a colon. You may return a maximum of
	 *            maxlen characters, including the trailing 0. If the field type
	 *            is FT_MULTIPLECHOICE, the plugin needs to return all possible
	 *            values here. <BR>
	 *            Example: The field "File Type" of the built-in content plugin
	 *            can have the values "File", "Folder" and "Reparse point". The
	 *            available choices need to be returned in the following form:
	 *            File|Folder|Reparse point . The same separator is used as for
	 *            Units. You may return a maximum of maxlen characters,
	 *            including the trailing 0. The field type FT_MULTIPLECHOICE
	 *            does NOT support any units.
	 * @param maxlen
	 *            The maximum number of characters, including the trailing 0,
	 *            which may be returned in each of the fields.
	 * 
	 * @return The function needs to return one of the following values:
	 *         <UL>
	 *         <LI>FT_NOMOREFIELDS The FieldIndex is beyond the last available
	 *         field.
	 *         <LI>FT_NUMERIC_32 A 32-bit signed number
	 *         <LI>FT_NUMERIC_64 A 64-bit signed number, e.g. for file sizes
	 *         <LI>FT_NUMERIC_FLOATING A double precision floating point number
	 *         <LI>FT_DATE A date value (year, month, day)
	 *         <LI>FT_TIME A time value (hour, minute, second). Date and time
	 *         are in local time.
	 *         <LI>FT_BOOLEAN A true/false value
	 *         <LI>FT_MULTIPLECHOICE A value allowing a limited number of
	 *         choices. Use the Units field to return all possible values.
	 * 
	 * <LI>FT_STRING A text string
	 * <LI>FT_DATETIME A timestamp of type FILETIME, as returned e.g. by
	 * FindFirstFile(). It is a 64-bit value representing the number of
	 * 100-nanosecond intervals since January 1, 1601. The time MUST be relative
	 * to universal time (Greenwich mean time) as returned by the file system,
	 * not local time!
	 * </UL>
	 */
	int fsContentGetSupportedField(final int fieldIndex,
			final StringBuffer fieldName, final StringBuffer units,
			final int maxlen);

	/**
	 * fsContentGetValue is called to retrieve the value of a specific field for
	 * a given file, e.g. the date field of a file. <BR>
	 * <B>Remarks:</B></BR> Total Commander now accepts that fsContentGetValue
	 * returns a different data type than fsContentGetSupportedField for the
	 * same field, e.g. a string "no value" instead of a numeric field. Note
	 * that older versions of Total Commander crashed in the search function in
	 * this case, so if you want to do this, you MUST check that the plugin
	 * version is reported as >=1.3 (hi=1, low>=3 or hi>=2). <BR>
	 * <BR>
	 * FT_NUMERIC_FLOATING (New with TC 6.52, plugin interface version >=1.4):
	 * You can now put a 0-terminated string immediately behind the 64bit
	 * floating point variable, which will then be shown instead in file lists.
	 * This is useful if the conversion precision used by TC isn't appropriate
	 * for your variables. The numeric variable will still be used for sorting
	 * and searching. If the string is empty, TC will ignore it (it is set to 0
	 * before calling this function, so the function will remain
	 * backwards-compatible). Example: The numeric value is 0.000002. You can
	 * return this value as a 64-bit variable, and the string you find most
	 * appropriate, e.g. "2*10^-6" or "0.000002". <BR>
	 * <BR>
	 * <B>About caching the data:</B> Total Commander will not call a mix
	 * fsContentGetValue for different files, it will only call it for the next
	 * file when the previous file can be closed. Therefore a single cache per
	 * running Total Commander would be sufficient. However, there may be other
	 * calls to fsContentGetValue with requests to other fields in the
	 * background, e.g. for displaying result lists. There may also be multiple
	 * instances of Total Commander at the same time, so if you use a TEMP file
	 * for storing the cached data, make sure to give it a unique name (e.g. via
	 * GetTempFileName).
	 * 
	 * @param fileName
	 *            The name of the file for which the plugin needs to return the
	 *            field data.
	 * 
	 * @param fieldIndex
	 *            The index of the field for which the content has to be
	 *            returned. This is the same index as the FieldIndex value in
	 *            fsContentGetSupportedField.
	 * 
	 * @param unitIndex
	 *            The index of the unit used. <BR>
	 *            <B>Example:</B><BR>
	 *            If the plugin returned the following unit string in
	 *            fsContentGetSupportedField: bytes|kbytes|Mbytes<BR>
	 *            Then a UnitIndex of 0 would mean bytes, 1 means kbytes and 2
	 *            means MBytes If no unit string was returned, UnitIndex is 0.
	 *            <BR>
	 *            For FT_FULLTEXT, UnitIndex contains the offset of the data to
	 *            be read.
	 * 
	 * @param fieldValue
	 *            Here the plugin needs to return the requested data. The data
	 *            format depends on the field type:
	 *            <UL>
	 *            <LI>FT_NUMERIC_32: FieldValue points to a 32-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_64: FieldValue points to a 64-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_FLOATING: FieldValue points to a 64-bit
	 *            floating point variable (ISO standard double precision) <BR>
	 *            See remark below about additional string field!
	 *            <LI>FT_DATE: FieldValue points to a structure containing
	 *            year,month,day as 2 byte values.
	 *            <LI>FT_TIME: FieldValue points to a structure containing
	 *            hour,minute,second as 2 byte values.
	 *            <LI>FT_BOOLEAN: FieldValue points to a 32-bit number. 0 neans
	 *            false, anything else means true.
	 *            <LI>FT_STRING or ft_multiplechoice: FieldValue is a pointer
	 *            to a 0-terminated string.
	 *            <LI>FT_FULLTEXT: Read maxlen bytes of interpreted data
	 *            starting at offset UnitIndex. The data must be a 0 terminated
	 *            string.
	 *            <LI>FT_DATETIME: A timestamp of type FILETIME, as returned
	 *            e.g. by FindFirstFile(). It is a 64-bit value representing the
	 *            number of 100-nanosecond intervals since January 1, 1601. The
	 *            time MUST be relative to universal time (Greenwich mean time)
	 *            as returned by the file system, not local time!
	 *            <LI>FT_DELAYED, FT_ONDEMAND: You may return a zero-terminated
	 *            string as in FT_STRING, which will be shown until the actual
	 *            value has been extracted. Requires plugin version>=1.4.
	 *            </UL>
	 * 
	 * @param maxlen
	 *            The maximum number of bytes fitting into the FieldValue
	 *            variable.
	 * 
	 * @param flags
	 *            Currently only one flag is defined:
	 *            <UL>
	 *            <LI>CONTENT_DELAYIFSLOW: If this flag is set, the plugin
	 *            should return FT_DELAYED for fields which take a long time to
	 *            extract, like file version information. Total Commander will
	 *            then call the function again in a background thread without
	 *            the CONTENT_DELAYIFSLOW flag. This means that your plugin must
	 *            be implemented thread-safe if you plan to return FT_DELAYED.
	 *            The plugin may also reutrn FT_ONDEMAND if CONTENT_DELAYIFSLOW
	 *            is set. In this case, the field will only be retrieved when
	 *            the user presses &lt;SPACEBAR&gt;. This is only recommended
	 *            for fields which take a VERY long time, e.g. directory content
	 *            size. You should offer the same field twice in this case, once
	 *            as delayed, and once as on demand. The field will be retrieved
	 *            in the background thread also in this case.
	 *            </UL>
	 * @return Return the field type in case of success, or one of the following
	 *         error values otherwise:
	 *         <UL>
	 *         <LI>FT_NOSUCHFIELD - The given FieldIndex is invalid
	 *         <LI>FT_FILEERROR - Error accessing the specified file FileName
	 *         <LI>FT_FIELDEMPTY - The file does not contain the specified
	 *         field
	 *         <LI>FT_DELAYED - The extraction of the field would take a long
	 *         time, so Total Commander should request it again in a background
	 *         thread. This error may only be returned if the flag
	 *         CONTENT_DELAYIFSLOW was set, and if the plugin is thread-safe.
	 * 
	 * <LI>FT_ONDEMAND - The extraction of the field would take a very long
	 * time, so it should only be retrieved when the user presses the space bar.
	 * This error may only be returned if the flag CONTENT_DELAYIFSLOW was set,
	 * and if the plugin is thread-safe.
	 * </UL>
	 */
	int fsContentGetValue(final String fileName, final int fieldIndex,
			final int unitIndex, final FieldValue fieldValue, final int maxlen,
			final int flags);

	/**
	 * fsContentStopGetValue is called to tell a plugin that a directory change
	 * has occurred, and the plugin should stop loading a value. <BR>
	 * <B>Note:</B><BR>
	 * This function only needs to be implemented when handling very slow
	 * fields, e.g. the calculation of the total size of all files in a
	 * directory. It will be called only while a call to fsContentGetValue is
	 * active in a background thread. <BR>
	 * A plugin could handle this mechanism like this:
	 * <OL>
	 * <LI>When fsContentGetValue is called, set a variable GetAborted to
	 * false.
	 * <LI>When fsContentStopGetValue is called, set GetAborted to true.
	 * <LI>Check GetAborted during the lengthy operation, and if it becomes
	 * true, return FT_FIELDEMPTY.
	 * </OL>
	 * 
	 * @param fileName
	 *            The name of the file for which fsContentGetValue is currently
	 *            being called.
	 * 
	 */
	void fsContentStopGetValue(final String fileName);

	/**
	 * fsContentGetDefaultSortOrder is called when the user clicks on the
	 * sorting header above the columns. <BR>
	 * <B>Note:</B><BR>
	 * You may implement this function if there are fields which are usually
	 * sorted in descending order, like the size field (largest file first) or
	 * the date/time fields (newest first). If the function isn't implemented,
	 * ascending will be the default.}
	 * 
	 * @param fieldIndex
	 *            The index of the field for which the sort order should be
	 *            returned.
	 * @return Return 1 for ascending (a..z, 1..9), or -1 for descending (z..a,
	 *         9..0).
	 */
	int fsContentGetDefaultSortOrder(int fieldIndex);

	/**
	 * fsContentPluginUnloading is called just before the plugin is unloaded,
	 * e.g. to close buffers, abort operations etc. <BR>
	 * <B>Note:</B><BR>
	 * This function was added by request from a user who needs to unload GDI+.
	 * It seems that GDI+ has a bug which makes it crash when unloading it in
	 * the DLL unload function, therefore a separate unload function is needed.
	 */
	void fsContentPluginUnloading();

	/**
	 * fsContentGetSupportedFieldFlags is called to get various information
	 * about a plugin variable. It's first called with fieldIndex=-1 to find out
	 * whether the plugin supports any special flags at all, and then for each
	 * field separately.
	 * 
	 * <BR>
	 * <B>Note:</B><BR>
	 * 
	 * Returning one of the CONTFLAGS_SUBST* flags instructs Total Commander to
	 * replace (substitute) the returned variable by the indicated default
	 * internal value if this field is displayed outside of the context of the
	 * plugin. It may also be used to determine default sort orders.
	 * 
	 * @param fieldIndex
	 *            The index of the field for which the sort order should be
	 *            returned.
	 *            <UL>
	 *            <LI>-1: Return a combination (or) of all supported flags,
	 *            e.g. contflags_edit | contflags_substmask
	 *            <LI>>=0: Return the field-specific flags
	 *            </UL>
	 * @return The function needs to return a combination of the following
	 *         flags:
	 *         <UL>
	 * 
	 * <LI>CONTFLAGS_EDIT The plugin allows to edit (modify) this field via
	 * Files - Change attributes. This should only be returned for fields where
	 * it makes sense, e.g. a file date. <BR>
	 * Only ONE of the following flags: (See description and example under
	 * "Note").
	 * 
	 * <LI>CONTFLAGS_SUBSTSIZE use the file size
	 * <LI>CONTFLAGS_SUBSTDATETIME use the file date+time (ft_datetime)
	 * <LI>CONTFLAGS_SUBSTDATE use the file date (fd_date)
	 * <LI>CONTFLAGS_SUBSTTIME use the file time (fd_time)
	 * <LI>CONTFLAGS_SUBSTATTRIBUTES use the file attributes (numeric)
	 * <LI>CONTFLAGS_SUBSTATTRIBUTESTR use the file attribute string in form
	 * -a--
	 * 
	 * <LI>CONTFLAGS_SUBSTMASK A combination of all above substituion flags.
	 * Should be returned for index -1 if the content plugin contains ANY of the
	 * substituted fields.
	 * </UL>
	 */
	int fsContentGetSupportedFieldFlags(int fieldIndex);

	/**
	 * fsContentSetValue is called to set the value of a specific field for a
	 * given file, e.g. to change the date field of a file.
	 * 
	 * @param fileName
	 *            The name of the file for which the plugin needs to change the
	 *            field data. This is set to NULL to indicate the end of change
	 *            attributes (see remarks below).
	 * 
	 * @param fieldIndex
	 *            The index of the field for which the content has to be
	 *            returned. This is the same index as the FieldIndex value in
	 *            fsContentGetSupportedField. This is set to -1 to signal the
	 *            end of change attributes (see remarks below).
	 * 
	 * @param unitIndex
	 *            The index of the unit used. <BR>
	 *            Example: If the plugin returned the following unit string in
	 *            fsContentGetSupportedField: bytes|kbytes|Mbytes Then a
	 *            unitIndex of 0 would mean bytes, 1 means kbytes and 2 means
	 *            MBytes If no unit string was returned, UnitIndex is 0.
	 *            FT_FULLTEXT is currently unsupported.
	 * 
	 * @param fieldType
	 *            The type of data passed to the plugin in FieldValue. This is
	 *            the same type as returned by the plugin via
	 *            fsContentGetSupportedField. If the plugin returned a different
	 *            type via fsContentGetValue, the the FieldType _may_ be of that
	 *            type too.
	 * 
	 * @param fieldValue
	 *            Here the plugin receives the data to be changed. The data
	 *            format depends on the field type:
	 *            <UL>
	 *            <LI>FT_NUMERIC_32: FieldValue points to a 32-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_64: FieldValue points to a 64-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_FLOATING: FieldValue points to a 64-bit
	 *            floating point variable (ISO standard double precision)
	 *            <LI>FT_DATE: FieldValue points to a structure containing
	 *            year,month,day as 2 byte values.
	 *            <LI>FT_TIME: FieldValue points to a structure containing
	 *            hour,minute,second as 2 byte values.
	 *            <LI>FT_BOOLEAN: FieldValue points to a 32-bit number. 0 neans
	 *            false, anything else means true.
	 *            <LI>FT_STRING or ft_multiplechoice: FieldValue is a pointer
	 *            to a 0-terminated string.
	 *            <LI>FT_FULLTEXT: Currently unsupported.
	 *            <LI>FT_DATETIME: A timestamp of type FILETIME, as returned
	 *            e.g. by FindFirstFile(). It is a 64-bit value representing the
	 *            number of 100-nanosecond intervals since January 1, 1601. The
	 *            time MUST be relative to universal time (Greenwich mean time)
	 *            as returned by the file system, not local time!
	 *            <LI>FT_DELAYED, FT_ONDEMAND: You may return a zero-terminated
	 *            string as in ft_string, which will be shown until the actual
	 *            value has been extracted. Requires plugin version>=1.4.
	 *            </UL>
	 * @param flags
	 *            Currently the following flags are defined:
	 *            <UL>
	 *            <LI>SETFLAGS_FIRST_ATTRIBUTE: This is the first attribute to
	 *            be set for this file via this plugin. May be used for
	 *            optimization.
	 *            <LI>SETFLAGS_LAST_ATTRIBUTE: This is the last attribute to be
	 *            set for this file via this plugin.
	 *            <LI>SETFLAGS_ONLY_DATE: For field type FT_DATETIME only: User
	 *            has only entered a date, don't change the time
	 *            </UL>
	 * @return
	 *            <UL>
	 * 
	 * <LI>FT_SETSUCCESS Change was successful
	 * <LI>FT_FILEERROR Error accessing the specified file FileName, or cannot
	 * set the given value
	 * 
	 * <LI>FT_NOSUCHFIELD The given field index was invalid
	 * </UL>
	 * 
	 * <BR>
	 * <B>Note:</B><BR>
	 * 
	 * <B>About caching the data:</B> Total Commander will not call a mix of
	 * FsContentSetValue for different files, it will only call it for the next
	 * file when the previous file can be closed. Therefore a single cache per
	 * running Total Commander should be sufficient. <BR>
	 * <B>About the flags:</B> If the flags setflags_first_attribute and
	 * setflags_last_attribute are both set, then this is the only attribute of
	 * this plugin which is changed for this file. <BR>
	 * <B>FsSetAttr</B> needs to be implemented too, otherwise the change
	 * attributes dialog (which is also used to change custom plugin attributes)
	 * cannot be used for this function. <BR>
	 * <B>FileName</B> is set to NULL and FieldIndex to -1 to signal to the
	 * plugin that the change attributes operation has ended. This can be used
	 * to flush unsaved data to disk, e.g. when setting comments for multiple
	 * files.
	 */
	int fsContentSetValue(String fileName, int fieldIndex, int unitIndex,
			int fieldType, FieldValue fieldValue, int flags);

	/***************************************************************************
	 * FsContentGetDefaultView is called to get the default view to which Total
	 * Commander should switch when this file system plugin is entered.
	 * 
	 * @param viewContents
	 *            Return the default fields for this plugin here, e.g. [=<fs>.size.bkM2]\n[=fs.writetime]
	 *            <BR>
	 *            Note that in C, you need to write \\n to return a backslash
	 *            and 'n' instead of a newline character!
	 * 
	 * @param viewHeaders
	 *            Return the default headers shown in the sorting header bar,
	 *            e.g. "Size\nDate/Time"
	 * 
	 * @param viewWidths
	 *            Return the default column widths shown in the sorting header
	 *            bar, e.g. "148,23,-35,-35" <BR>
	 *            Negative values mean that the field is right-aligned. The
	 *            first two widths are for name and extension
	 * 
	 * @param viewOptions
	 *            The two values, separated by a vertical line, mean: -
	 *            auto-adjust-width, or -1 for no adjust - horizontal scrollbar
	 *            flag
	 * 
	 * @return Return true if you returned a default view, false if no default
	 *         view should be shown.
	 * 
	 * <BR>
	 * <B>Note:</B><BR>
	 * 
	 * It's best to create a custom columns view in Total Commander, save it,
	 * and then copy the definitions from the Wincmd.ini to your plugin. The
	 * values in ViewContents and ViewHeaders are separated by a backslash and
	 * lowercase 'n' character. Note that in C, you need to write \\n to return
	 * a backslash and 'n' instead of a newline character!
	 **************************************************************************/
	boolean fsContentGetDefaultView(String viewContents, String viewHeaders,
			String viewWidths, String viewOptions, int maxlen);

}
