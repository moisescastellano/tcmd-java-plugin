package plugins.wcx;

import java.util.Calendar;

import plugins.DefaultParam;

/**
 * @author Ken
 * 
 */
public abstract class WCXPluginAdapter implements WCXPluginInterface {

	/**
	 * 
	 */
	public WCXPluginAdapter() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract Object openArchive(final OpenArchiveData archiveData);

	/**
	 * {@inheritDoc}
	 */
	public abstract int readHeader(final Object archiveData,
			final HeaderData headerData);

	/**
	 * {@inheritDoc}
	 */
	public abstract int processFile(final Object archiveData,
			final int operation, final String destPath, final String destName);

	/**
	 * {@inheritDoc}
	 */
	public abstract int closeArchive(final Object archiveData);

	/**
	 * {@inheritDoc}
	 */
	public int packFiles(final String packedFile, final String subPath,
			final String srcPath, final String addList, final int flags) {
		return SUCCESS;
	}

	/**
	 * {@inheritDoc}
	 */
	public int deleteFiles(final String packedFile, final String deleteList) {
		return SUCCESS;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPackerCaps() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int configurePacker(final int parentWin) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object startMemPack(final int options, final String fileName) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int packToMem(final Object hMemPack, final String bufIn,
			final int inLen, final String bufOut, final int outLen,
			final int seekBy, final PackToMem packToMem) {
		return MEMPACK_OK;
	}

	/**
	 * {@inheritDoc}
	 */
	public int doneMemPack(final Object hMemPack) {
		return SUCCESS;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canYouHandleThisFile(final String fileName) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void packSetDefaultParams(final DefaultParam dps) {
	}

	/**
	 * {@inheritDoc}
	 */
	public int readHeaderEx(final Object archiveData,
			final HeaderDataEx headerDataEx) {
		HeaderData headerData = new HeaderData();
		int ret = readHeader(archiveData, headerData);
		headerDataEx.setArcName(headerData.getArcName());
		headerDataEx.setFileName(headerData.getFileName());
		headerDataEx.setFlags(headerData.getFlags());
		headerDataEx.setPackSize(headerData.getPackSize());
		headerDataEx.setUnpSize(headerData.getUnpSize());
		headerDataEx.setHostOs(headerData.getHostOs());
		headerDataEx.setFileCRC(headerData.getFileCRC());
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(headerData.getFileTime());
		headerDataEx.setFileTime(cal);
		headerDataEx.setUnpVer(headerData.getUnpVer());
		headerDataEx.setMethod(headerData.getMethod());
		headerDataEx.setFileAttr(headerData.getFileAttr());
		headerDataEx.setCmtBuf(headerData.getCmtBuf());
		headerDataEx.setCmtBufSize(headerData.getCmtBufSize());
		headerDataEx.setCmtSize(headerData.getCmtSize());
		headerDataEx.setCmtState(headerData.getCmtState());
		return ret;
	}

	{
		// load library for native functions
		System.loadLibrary("tc.library.name");
	}

	/**
	 * When you want the user to be asked about changing volume, call this
	 * function with appropriate parameters. The function itself is part of
	 * Totalcmd - you only specify the question. Totalcmd then asks the user,
	 * and you get the answer as the result of the call to this function.
	 * 
	 * @param arcName
	 *            specifies the filename of the archive that you are processing,
	 *            and will receive the name of the next volume.
	 * @param mode
	 *            Set Mode to one of the following values, according to what you
	 *            want Totalcmd to ask the user:
	 *            <UL>
	 *            <LI> PK_VOL_ASK
	 *            <LI> PK_VOL_NOTIFY
	 *            </UL>
	 * @return If the user has aborted the operation, the function returns zero.
	 */
	public final native int packerSetChangeVol(final String arcName,
			final int mode);

	/**
	 * The function that notifies the user about the progress when un/packing
	 * files. When you want to notify the user about the progress when
	 * un/packing files, call this function with appropriate parameters. The
	 * function itself is part of Totalcmd - you only specify what Totalcmd
	 * should display. In addition, Totalcmd displays the Cancel button that
	 * allows the user to abort the un/packing process.
	 * 
	 * @param fileName
	 *            can be used to pass a pointer to the currently processed
	 *            filename, or NULL if it is not available.
	 * @param size
	 *            Size to the number of bytes processed since the previous call
	 *            to the function.
	 * @return If the user has clicked on Cancel, the function returns zero.
	 */
	public final native int packerSetProcessData(final String fileName,
			final int size);
}
