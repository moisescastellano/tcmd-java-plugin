/**
 * 
 */
package plugins.wfx;

import plugins.DefaultParam;
import plugins.FileTime;
import plugins.wdx.FieldValue;

/**
 * @author Ken
 * 
 */
public abstract class WFXPluginAdapter implements WFXPluginInterface {

	/**
	 * The plugin number.
	 */
	private int fPluginNr;

	/**
	 * @return Returns the fPluginNr.
	 */
	public final int getPluginNr() {
		return fPluginNr;
	}

	/**
	 * 
	 */
	public WFXPluginAdapter() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsInit(final int pluginNr) {
		fPluginNr = pluginNr;
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract Object fsFindFirst(String path, Win32FindData lastFindData);

	/**
	 * {@inheritDoc}
	 */
	public abstract boolean fsFindNext(Object handle, Win32FindData findData);

	/**
	 * {@inheritDoc}
	 */
	public abstract int fsFindClose(Object handle);

	/**
	 * {@inheritDoc}
	 */
	public String fsGetDefRootName(final int maxlen) {
		return getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsGetFile(final String remoteName, final String localName,
			final int copyFlags, final RemoteInfo remoteInfo) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsPutFile(final String localName, final String remoteName,
			final int copyFlags) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsRenMovFile(final String oldName, final String newName,
			final boolean move, final boolean overwrite,
			final RemoteInfo remoteInfo) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean fsDeleteFile(final String remoteName) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean fsRemoveDir(final String remoteName) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean fsMkDir(final String path) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsExecuteFile(final int mainWin, final String remoteName,
			final String verb) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean fsSetAttr(final String remoteName, final int newAttr) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean fsSetTime(final String remoteName,
			final FileTime creationTime, final FileTime lastAccessTime,
			final FileTime lastWriteTime) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean fsDisconnect(final String disconnectRoot) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void fsStatusInfo(final String remoteDir, final int infoStartEnd,
			final int infoOperation) {
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsExtractCustomIcon(final String remoteName,
			final int extractFlags, final StringBuffer theIcon) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void fsSetDefaultParams(final DefaultParam dps) {
	}

	{
		// load library for native functions
		System.loadLibrary("tc.library.name");
	}

	/**
	 * ProgressProc is a callback function, which the plugin can call to show
	 * copy progress.<BR>
	 * 
	 * <B>Important note:</B><BR>
	 * You should call this function at least twice in the copy functions
	 * FsGetFile(), FsPutFile() and FsRenMovFile(), at the beginning and at the
	 * end. If you can't determine the progress, call it with 0% at the
	 * beginning and 100% at the end.<BR>
	 * New in 1.3: During the FsFindFirst/FsFindNext/FsFindClose loop, the
	 * plugin may now call the ProgressProc to make a progess dialog appear.
	 * This is useful for very slow connections. Don't call ProgressProc for
	 * fast connections! The progress dialog will only be shown for normal dir
	 * changes, not for compound operations like get/put. The calls to
	 * ProgressProc will also be ignored during the first 5 seconds, so the user
	 * isn't bothered with a progress dialog on every dir change.
	 * 
	 * @param pluginNr
	 *            Here the plugin needs to pass the plugin number received
	 *            through the FsInit() function.
	 * @param sourceName
	 *            Name of the source file being copied. Depending on the
	 *            direction of the operation (Get, Put), this may be a local
	 *            file name of a name in the plugin file system.
	 * @param targetName
	 *            Name to which the file is copied.
	 * @param percentDone
	 *            Percentage of THIS file being copied. Total Commander
	 *            automatically shows a second percent bar if possible when
	 *            multiple files are copied.
	 * @return Total Commander returns 1 if the user wants to abort copying, and
	 *         0 if the operation can continue.
	 */
	public final native int fsSetProgress(final int pluginNr,
			final String sourceName, final String targetName,
			final int percentDone);

	/**
	 * LogProc is a callback function, which the plugin can call to show the FTP
	 * connections toolbar, and to pass log messages to it. Wincmd can show
	 * these messages in the log window (ftp toolbar) and write them to a log
	 * file. The address of this callback function is received through the
	 * FsInit() function when the plugin is loaded.<BR>
	 * 
	 * <B>Important note:</B><BR>
	 * 
	 * Do NOT call LogProc with MSGTYPE_CONNECT if your plugin does not require
	 * connect/disconnect! If you call it with MsgType==MSGTYPE_CONNECT, the
	 * function FsDisconnect will be called (if defined) when the user presses
	 * the Disconnect button.<BR>
	 * 
	 * Examples:
	 * <UL>
	 * <LI> FTP requires connect/disconnect, so call LogProc with
	 * MSGTYPE_CONNECT when a connection is established.
	 * <LI> Access to local file systems (e.g. Linux EXT2) does not require
	 * connect/disconnect
	 * </UL>
	 * 
	 * @param pluginNr
	 *            Here the plugin needs to pass the plugin number received
	 *            through the FsInit() function.
	 * @param msgType
	 *            Can be one of the MSGTYPE_XXX flags:
	 *            <UL>
	 *            <LI>MSGTYPE_CONNECT Connect to a file system requiring
	 *            disconnect
	 *            <LI>MSGTYPE_DISCONNECT Disconnected successfully
	 *            <LI>MSGTYPE_DETAILS Not so important messages like directory
	 *            changing
	 *            <LI>MSGTYPE_TRANSFERCOMPLETE A file transfer was completed
	 *            successfully
	 *            <LI>MSGTYPE_CONNECTCOMPLETE unused
	 *            <LI>MSGTYPE_IMPORTANTERROR An important error has occured
	 *            <LI>MSGTYPE_OPERATIONCOMPLETE An operation other than a file
	 *            transfer has completed
	 *            </UL>
	 *            Total Commander supports logging to files. While one log file
	 *            will store all messages, the other will only store important
	 *            errors, connects, disconnects and complete
	 *            operations/transfers, but not messages of type
	 *            MSGTYPE_DETAILS.
	 * @param logString
	 *            String which should be logged. When MsgType==MSGTYPE_CONNECT,
	 *            the string MUST have a specific format: "CONNECT" followed by
	 *            a single whitespace, then the root of the file system which
	 *            was connected, without trailing backslash. Example: CONNECT
	 *            \Filesystem<BR>
	 *            When MsgType==MSGTYPE_TRANSFERCOMPLETE, this parameter should
	 *            contain both the source and target names, separated by an
	 *            arrow " -> ", e.g. Download complete:
	 *            \Filesystem\dir1\file1.txt -> c:\localdir\file1.txt<BR>
	 */
	public final native void fsLog(final int pluginNr, final int msgType,
			final String logString);

	/**
	 * RequestProc is a callback function, which the plugin can call to request
	 * input from the user. When using one of the standard parameters, the
	 * request will be in the selected language. The address of this callback
	 * function is received through the FsInit() function when the plugin is
	 * loaded.<BR>
	 * 
	 * <B>Important note:</B><BR>
	 * 
	 * Leave CustomText empty if you want to use the (translated) default
	 * strings!
	 * 
	 * @param pluginNr
	 *            Here the plugin needs to pass the plugin number received
	 *            through the FsInit() function.
	 * @param requestType
	 *            Can be one of the RT_XXX flags:
	 *            <UL>
	 *            <LI>RT_OTHER The requested string is none of the default
	 *            types
	 *            <LI>RT_USERNAME Ask for the user name, e.g. for a connection
	 *            <LI>RT_PASSWORD Ask for a password, e.g. for a connection
	 *            (shows ***)
	 *            <LI>RT_ACCOUNT Ask for an account (needed for some FTP
	 *            servers)
	 *            <LI>RT_USERNAMEFIREWALL User name for a firewall
	 *            <LI>RT_PASSWORDFIREWALL Password for a firewall
	 *            <LI>RT_TARGETDIR Asks for a local directory (with browse
	 *            button)
	 *            <LI>RT_URL Asks for an URL
	 *            <LI>RT_MSGOK Shows MessageBox with OK button
	 *            <LI>RT_MSGYESNO Shows MessageBox with Yes/No buttons
	 *            <LI>RT_MSGOKCANCEL Shows MessageBox with OK/Cancel buttons
	 *            </UL>
	 * 
	 * @param customTitle
	 *            Custom title for the dialog box. If NULL or empty, it will be
	 *            "Total Commander"
	 * @param customText
	 *            Override the text defined with RequestType. Set this to NULL
	 *            or an empty string to use the default text. The default text
	 *            will be translated to the language set in the calling program.
	 * @param returnedText
	 *            This string contains the default text presented to the user,
	 *            and will receive the (modified) text which the user enters.
	 *            set ReturnedText="" to have no default text.
	 * @return Returns TRUE if the user clicked OK or Yes, FALSE otherwise.
	 */
	public final native boolean fsRequest(final int pluginNr,
			final int requestType, final String customTitle,
			final String customText, final StringBuffer returnedText);

	/**
	 * {@inheritDoc}
	 */
	public int fsContentGetDefaultSortOrder(final int fieldIndex) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean fsContentGetDefaultView(final String viewContents,
			final String viewHeaders, final String viewWidths,
			final String viewOptions, final int maxlen) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsContentGetSupportedField(final int fieldIndex,
			final StringBuffer fieldName, final StringBuffer units,
			final int maxlen) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsContentGetSupportedFieldFlags(final int fieldIndex) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsContentGetValue(final String fileName, final int fieldIndex,
			final int unitIndex, final FieldValue fieldValue, final int maxlen,
			final int flags) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void fsContentPluginUnloading() {
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsContentSetValue(final String fileName, final int fieldIndex,
			final int unitIndex, final int fieldType,
			final FieldValue fieldValue, final int flags) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void fsContentStopGetValue(final String fileName) {
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean fsGetLocalName(final String remoteName, final int maxlen) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public int fsGetPreviewBitmap(final String remoteName, final int width,
			final int height, final StringBuffer filename) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean fsLinksToLocalFiles() {
		return false;
	}
}
