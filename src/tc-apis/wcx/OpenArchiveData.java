package plugins.wcx;

/**
 * OpenArchiveData is used in openArchive.
 * 
 * @author Ken
 * 
 */
public class OpenArchiveData {

	/**
	 * Open file for reading of file names only.
	 */
	public static final int PK_OM_LIST = 0;

	/**
	 * Open file for processing (extract or test).
	 */
	public static final int PK_OM_EXTRACT = 1;

	/**
	 * Contains the name of the archive to open.
	 */
	private String fArcName;

	/**
	 * Contains the name of the archive to open.
	 * 
	 * @return Returns the arcName.
	 */
	public final String getArcName() {
		return fArcName;
	}

	/**
	 * OpenMode is set to one of the following values:
	 * <UL>
	 * <LI>PK_OM_LIST - Open file for reading of file names only.
	 * <LI>PK_OM_EXTRACT - Open file for processing (extract or test).
	 * </UL>
	 * <B>Note:</B><BR>
	 * <BR>
	 * 
	 * If the file is opened with OpenMode==PK_OM_LIST, ProcessFile will never
	 * be called by Total Commander.
	 */
	private int fOpenMode;

	/**
	 * OpenMode is set to one of the following values:
	 * <UL>
	 * <LI>PK_OM_LIST - Open file for reading of file names only.
	 * <LI>PK_OM_EXTRACT - Open file for processing (extract or test).
	 * </UL>
	 * <B>Note:</B><BR>
	 * <BR>
	 * 
	 * If the file is opened with OpenMode==PK_OM_LIST, ProcessFile will never
	 * be called by Total Commander.
	 * 
	 * @return Returns the openMode.
	 */
	public final int getOpenMode() {
		return fOpenMode;
	}

	/**
	 * Used to return one of the error values if an error occurs.
	 */
	private int fOpenResult;

	/**
	 * Used to return one of the error values if an error occurs.
	 * 
	 * @return Returns the openResult.
	 */
	public final int getOpenResult() {
		return fOpenResult;
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
	 * OpenArchiveData is used in OpenArchive.
	 * 
	 * @param arcName
	 *            Contains the name of the archive to open.
	 * @param openmode
	 *            Open mode
	 * @param openresult
	 *            Used to return one of the error values if an error occurs.
	 */
	public OpenArchiveData(final String arcName, final int openmode,
			final int openresult) {
		this.fArcName = arcName;
		this.fOpenMode = openmode;
		this.fOpenResult = openresult;
	}

}
