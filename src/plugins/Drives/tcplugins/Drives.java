package tcplugins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.FileTime;
import plugins.wdx.FieldValue;
import plugins.wdx.LocalDate;
import plugins.wdx.LocalTime;
import plugins.wfx.RemoteInfo;
import plugins.wfx.WFXPluginAdapter;
import plugins.wfx.Win32FindData;

/**
 * @author Ken An example filesystem plugin.
 */
public class Drives extends WFXPluginAdapter {
	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(Drives.class);

	/**
	 * 
	 */
	private JFrame frame;

	// /**
	// *
	// */
	// private JDialog dialog;
	//
	// /**
	// *
	// */
	// private JButton yesButton;
	//
	// /**
	// *
	// */
	// private JButton noButton;

	/**
	 * This is the memorable file information.
	 * 
	 * @author Ken
	 */
	private class FileInfo {
		/**
		 * the current file handle.
		 */
		private File fileHandle;

		/**
		 * the child files of file system roots.
		 */
		private File[] roots;

		/**
		 * The current index of file system roots.
		 */
		private int rootNum;

		/**
		 * the child files of this FileInfo.
		 */
		private File[] childFiles;

		/**
		 * The current index for the child files.
		 */
		private int childNum;

	}

	/**
	 * Create a new FileSystem plugin.
	 */
	public Drives() {
		if (log.isDebugEnabled()) {
			log.debug("Drives.Drives()");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object fsFindFirst(final String pathPar,
			final Win32FindData findData) {
		if (log.isDebugEnabled()) {
			log.debug("Drive.fsFindFirst() path=" + pathPar);
		}
		String path = pathPar;
		if (!path.endsWith("\\")) {
			path += "\\";
		}
		try {
			FileInfo lf = new FileInfo();
			if ("\\".equals(path)) {
				// ROOT of file system
				File[] roots = File.listRoots();
				if (roots == null) {
					// roots could not be determined
					long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
					findData.setLastErrorMessage(lastErrorMessage);
					return INVALID_HANDLE_VALUE;
				}
				if (roots.length == 0) {
					// empty directory
					long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
					findData.setLastErrorMessage(lastErrorMessage);
					return INVALID_HANDLE_VALUE;
				}
				lf.roots = roots;
				lf.rootNum = 0;
				findData.setFromFile(roots[lf.rootNum]);
				findData.setFileName(roots[lf.rootNum].getAbsolutePath());
				long fileAtts = Win32FindData.FILE_ATTRIBUTE_DIRECTORY;
				findData.setFileAttributes(fileAtts);
				return lf;
			} else {
				// get first child file from path
				File file = new File(path);
				if (log.isDebugEnabled()) {
					log.debug("Drives.fsFindFirst() check drive:" + file);
				}
				lf.childFiles = file.listFiles();
				if (lf.childFiles == null) {
					// IO-error
					long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
					findData.setLastErrorMessage(lastErrorMessage);
					return INVALID_HANDLE_VALUE;
				}
				if (lf.childFiles.length == 0) {
					// empty directory
					long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
					findData.setLastErrorMessage(lastErrorMessage);
					return INVALID_HANDLE_VALUE;
				}
				// First child file of the next level
				lf.childNum = 0;
				lf.fileHandle = lf.childFiles[lf.childNum];
				findData.setFromFile(lf.fileHandle);
				return lf;
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean fsFindNext(final Object handle,
			final Win32FindData findData) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsFindNext()");
			}
			FileInfo lf = (FileInfo) handle;
			if (lf.roots != null) {
				// find next drive sibling of current roots
				if (lf.rootNum + 1 < lf.roots.length) {
					// search next drive letter (next ROOT of file system)
					File driveRootFile = lf.roots[++lf.rootNum];
					if (log.isDebugEnabled()) {
						log.debug("Drives.fsFindNext() driveRootFile="
								+ driveRootFile.getAbsolutePath());
					}
					findData.setFromFile(driveRootFile);
					findData.setFileName(driveRootFile.getAbsolutePath());
					long fileAtts = Win32FindData.FILE_ATTRIBUTE_DIRECTORY;
					findData.setFileAttributes(fileAtts);
					return true;
				} else {
					// end of all drive letters reached
					return false;
				}
			} else {
				// search next file sibling of current fileHandle
				// next sibling exists?
				if ((lf.childNum + 1) < lf.childFiles.length) {
					lf.fileHandle = lf.childFiles[++lf.childNum];
					findData.setFromFile(lf.fileHandle);
					return true;
				} else {
					return false;
				}
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final int fsFindClose(final Object handle) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsFindClose()");
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int fsGetFile(final String remoteName, final String localName,
			final int copyFlags, final RemoteInfo remoteInfo) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsGetFile() remoteName=" + remoteName
					+ ", localName=" + localName + ", copyFlags=" + copyFlags);
			log.debug("Drives.fsGetFile() remoteInfo.getSizeLow()="
					+ remoteInfo.getSizeLow() + ", remoteInfo.getSizeHigh()="
					+ remoteInfo.getSizeHigh()
					+ ", remoteInfo.getLastWriteTime()="
					+ remoteInfo.getLastWriteTime().getDate()
					+ ", remoteInfo.getAttr()=" + remoteInfo.getAttr());
		}
		try {
			boolean overwrite = (copyFlags & FS_COPYFLAGS_OVERWRITE) != 0;
			boolean resume = (copyFlags & FS_COPYFLAGS_RESUME) != 0;
			boolean move = (copyFlags & FS_COPYFLAGS_MOVE) != 0;
			if (resume) {
				return FS_FILE_NOTSUPPORTED;
			}
			File oldFile = new File(remoteName.substring(1));
			File newFile = new File(localName);
			if (move) {
				return moveFile(oldFile, newFile, overwrite);
			} else {
				return copyFile(oldFile, newFile, overwrite);
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			return FS_FILE_NOTSUPPORTED;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final int fsPutFile(final String localName, final String remoteName,
			final int copyFlags) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsPutFile() remoteName=" + remoteName
					+ ", localName=" + localName + ", copyFlags=" + copyFlags);
		}
		try {
			boolean overwrite = (copyFlags & FS_COPYFLAGS_OVERWRITE) != 0;
			boolean resume = (copyFlags & FS_COPYFLAGS_RESUME) != 0;
			boolean move = (copyFlags & FS_COPYFLAGS_MOVE) != 0;
			if (resume) {
				return FS_FILE_NOTSUPPORTED;
			}
			File oldFile = new File(localName);
			File newFile = new File(remoteName.substring(1));
			if (move) {
				return moveFile(oldFile, newFile, overwrite);
			} else {
				return copyFile(oldFile, newFile, overwrite);
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			return FS_FILE_NOTSUPPORTED;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final int fsRenMovFile(final String oldName, final String newName,
			final boolean move, final boolean overwrite,
			final RemoteInfo remoteInfo) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsRenMovFile() oldName=" + oldName + ", newName="
					+ newName + ", move=" + move + ", overwrite=" + overwrite);
			log.debug("Drives.fsRenMovFile() remoteInfo.getSizeLow()="
					+ remoteInfo.getSizeLow() + ", remoteInfo.getSizeHigh()="
					+ remoteInfo.getSizeHigh()
					+ ", remoteInfo.getLastWriteTime()="
					+ remoteInfo.getLastWriteTime().getDate()
					+ ", remoteInfo.getAttr()=" + remoteInfo.getAttr());
		}
		try {
			File oldFile = new File(oldName.substring(1));
			File newFile = new File(newName.substring(1));
			if (move) {
				return moveFile(oldFile, newFile, overwrite);
			} else {
				return copyFile(oldFile, newFile, overwrite);
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			return FS_FILE_EXISTS;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean fsDeleteFile(final String remoteName) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsDeleteFile() remoteName=" + remoteName);
		}
		return new File(remoteName.substring(1)).delete();
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean fsRemoveDir(final String remoteName) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsRemoveDir() remoteName=" + remoteName);
		}
		return new File(remoteName.substring(1)).delete();
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean fsMkDir(final String path) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsMkDir() path=" + path);
		}
		return new File(path.substring(1)).mkdir();
	}

	/**
	 * {@inheritDoc}
	 */
	public final int fsExecuteFile(final int mainWin, final String remoteName,
			final String verb) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsExecuteFile() remoteName=" + remoteName
					+ ", verb=" + verb);
		}
		if ("open".equals(verb)) {
			String pathName = remoteName.substring(1);
			try {
				File userCwd = new File(new File(pathName).getParent());
				Runtime.getRuntime().exec(pathName, null, userCwd);
				return FS_EXEC_OK;
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		} else if ("properties".equals(verb)) {
			try {
				frame = new JFrame();
				// frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				// String myMessage = "This is the properties dialog.";
				// dialog = new JDialog(frame);
				// dialog.setTitle("Plugin Properties");
				// JPanel myPanel = new JPanel();
				// dialog.getContentPane().add(myPanel);
				// myPanel.add(new JLabel(myMessage));
				// yesButton = new JButton("Yes");
				// yesButton.addActionListener(this);
				// myPanel.add(yesButton);
				// noButton = new JButton("No");
				// noButton.addActionListener(this);
				// myPanel.add(noButton);
				// dialog.pack();
				// dialog.setLocationRelativeTo(frame);
				// dialog.setVisible(true);
				int rc = JOptionPane.showOptionDialog(frame,
						"This is the properties dialog.", "Plugin Properties",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, null, null);
				log.debug("rc = " + rc);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return FS_EXEC_OK;
		}
		return FS_EXEC_ERROR;
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean fsSetAttr(final String remoteName, final int newAttr) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsSetAttr() remoteName=" + remoteName
					+ ", newAttr=" + newAttr);
		}
		boolean archive = (newAttr & Win32FindData.FILE_ATTRIBUTE_ARCHIVE) != 0;
		boolean hidden = (newAttr & Win32FindData.FILE_ATTRIBUTE_HIDDEN) != 0;
		boolean readOn = (newAttr & Win32FindData.FILE_ATTRIBUTE_READONLY) != 0;
		boolean system = (newAttr & Win32FindData.FILE_ATTRIBUTE_SYSTEM) != 0;
		File file = new File(remoteName.substring(1));
		if (readOn) {
			return file.setReadOnly();
		}
		if (archive || hidden || system) {
			// unsupported feature
			return false;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean fsSetTime(final String remoteName,
			final FileTime creationTime, final FileTime lastAccessTime,
			final FileTime lastWriteTime) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsSetTime() remoteName=" + remoteName);
			if (creationTime != null) {
				log.debug(", creationTime=" + creationTime.getDate());
			}
			if (lastAccessTime != null) {
				log.debug(", lastAccessTime=" + lastAccessTime.getDate());
			}
			if (lastWriteTime != null) {
				log.debug(", lastWriteTime=" + lastWriteTime.getDate());
			}
		}
		// TODO creationTime unsupported
		// TODO lastAccessTime unsupported
		File file = new File(remoteName.substring(1));
		if (lastWriteTime != null) {
			return file.setLastModified(lastWriteTime.getDate().getTime());
		} else {
			return true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final void fsStatusInfo(final String remoteDir,
			final int infoStartEnd, final int infoOperation) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsStatusInfo() remoteDir=" + remoteDir
					+ ", infoStartEnd=" + infoStartEnd + ", infoOperation="
					+ infoOperation);
		}
		switch (infoStartEnd) {
		case FS_STATUS_START:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_START");
			}
			break;

		case FS_STATUS_END:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_END");
			}
			break;

		default:
			break;
		}
		switch (infoOperation) {
		case FS_STATUS_OP_LIST:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_LIST");
			}
			break;

		case FS_STATUS_OP_GET_SINGLE:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_GET_SINGLE");
			}
			break;

		case FS_STATUS_OP_GET_MULTI:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_GET_MULTI");
			}
			break;

		case FS_STATUS_OP_PUT_SINGLE:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_PUT_SINGLE");
			}
			break;

		case FS_STATUS_OP_PUT_MULTI:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_PUT_MULTI");
			}
			break;

		case FS_STATUS_OP_RENMOV_SINGLE:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_RENMOV_SINGLE");
			}
			break;

		case FS_STATUS_OP_RENMOV_MULTI:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_RENMOV_MULTI");
			}
			break;

		case FS_STATUS_OP_DELETE:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_DELETE");
			}
			break;

		case FS_STATUS_OP_ATTRIB:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_ATTRIB");
			}
			break;

		case FS_STATUS_OP_MKDIR:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_MKDIR");
			}
			break;

		case FS_STATUS_OP_EXEC:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_EXEC");
			}
			break;

		case FS_STATUS_OP_CALCSIZE:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_CALCSIZE");
			}
			break;

		case FS_STATUS_OP_SEARCH:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_SEARCH");
			}
			break;

		case FS_STATUS_OP_SEARCH_TEXT:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_SEARCH_TEXT");
			}
			break;

		case FS_STATUS_OP_SYNC_SEARCH:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_SYNC_SEARCH");
			}
			break;

		case FS_STATUS_OP_SYNC_GET:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_SYNC_GET");
			}
			break;

		case FS_STATUS_OP_SYNC_PUT:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_SYNC_PUT");
			}
			break;

		case FS_STATUS_OP_SYNC_DELETE:
			if (log.isDebugEnabled()) {
				log.debug("Drives.fsStatusInfo() FS_STATUS_OP_SYNC_DELETE");
			}
			break;

		default:
			break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final int fsExtractCustomIcon(final String remoteName,
			final int extractFlags, final StringBuffer theIcon) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsExtractCustomIcon() remoteName=" + remoteName
					+ ", extractFlags=" + extractFlags);
		}

		// theIcon.append("253|shell32.dll");

		// theIcon.append("G:\\Totalcmd\\plugins\\java\\Drives\\test.ico");

		if (new File(remoteName.substring(1)).isDirectory()) {
			theIcon.append("%CWD%\\dir.ico");
		} else {
			theIcon.append("%CWD%\\file.ico");
		}
		return FS_ICON_EXTRACTED;
	}

	/**
	 * Copy source file to target file.
	 * 
	 * @param source
	 *            the source file
	 * @param dest
	 *            the target file
	 * @param overwrite
	 *            overwrite destination
	 * @return FS_FILE_... constants
	 */
	private int copyFile(final File source, final File dest,
			final boolean overwrite) {
		if (overwrite) {
			dest.delete();
		}
		if (dest.exists()) {
			return FS_FILE_EXISTS;
		}
		FileWriter fw = null;
		FileReader fr = null;
		BufferedReader br = null;
		BufferedWriter bw = null;

		try {
			fr = new FileReader(source);
			fw = new FileWriter(dest);
			br = new BufferedReader(fr);
			bw = new BufferedWriter(fw);

			int fileLength = (int) source.length();

			char[] charBuff = new char[fileLength];

			while (br.read(charBuff, 0, fileLength) != -1) {
				bw.write(charBuff, 0, fileLength);
			}
		} catch (FileNotFoundException fnfe) {
			return FS_FILE_NOTFOUND;
		} catch (IOException ioe) {
			return FS_FILE_WRITEERROR;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (bw != null) {
					bw.close();
				}
			} catch (IOException ioe) {
				return FS_FILE_WRITEERROR;
			}
		}
		return FS_FILE_OK;
	}

	/**
	 * Move source file to dest file.
	 * 
	 * @param source
	 *            the source file
	 * @param dest
	 *            the dest file
	 * @param overwrite
	 *            overwrite destination?
	 * @return FS_FILE_... constants
	 */
	private int moveFile(final File source, final File dest,
			final boolean overwrite) {
		if (overwrite) {
			dest.delete();
		}

		if (dest.exists()) {
			return FS_FILE_EXISTS;
		}
		if (!source.exists() || !source.canRead()) {
			return FS_FILE_NOTFOUND;
		}
		boolean ok = source.renameTo(dest);

		if (ok) {
			return FS_FILE_OK;
		} else {
			return FS_FILE_WRITEERROR;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int fsContentGetSupportedField(int fieldIndex,
			StringBuffer fieldName, StringBuffer units, int maxlen) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsContentGetSupportedField()");
		}
		try {
			if (fieldIndex == 0) {
				fieldName.append("FT_NUMERIC_32");
				return FieldValue.FT_NUMERIC_32;
			} else if (fieldIndex == 1) {
				fieldName.append("FT_NUMERIC_64");
				return FieldValue.FT_NUMERIC_64;
			} else if (fieldIndex == 2) {
				fieldName.append("FT_NUMERIC_FLOATING");
				return FieldValue.FT_NUMERIC_FLOATING;
			} else if (fieldIndex == 3) {
				fieldName.append("FT_BOOLEAN");
				return FieldValue.FT_BOOLEAN;
			} else if (fieldIndex == 4) {
				fieldName.append("FT_DATE");
				return FieldValue.FT_DATE;
			} else if (fieldIndex == 5) {
				fieldName.append("FT_TIME");
				return FieldValue.FT_TIME;
			} else if (fieldIndex == 6) {
				fieldName.append("FT_DATETIME");
				return FieldValue.FT_DATETIME;
			} else if (fieldIndex == 7) {
				fieldName.append("FT_STRING");
				return FieldValue.FT_STRING;
			} else if (fieldIndex == 8) {
				fieldName.append("FT_MULTIPLECHOICE");
				units.append("c1|c2|c3");
				return FieldValue.FT_MULTIPLECHOICE;
			} else if (fieldIndex == 9) {
				fieldName.append("FT_STRING2");
				return FieldValue.FT_STRING;
			} else if (fieldIndex == 10) {
				fieldName.append("FT_FULLTEXT");
				return FieldValue.FT_FULLTEXT;
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
		}
		return FieldValue.FT_NOMOREFIELDS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int fsContentGetValue(String fileName, int fieldIndex,
			int unitIndex, FieldValue fieldValue, int maxlen, int flags) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsContentGetValue()");
		}
		try {
			if (fieldIndex == 0) {
				int fieldType = FieldValue.FT_NUMERIC_32;
				fieldValue.setValue(fieldType, new Integer(1));
				return fieldType;
			} else if (fieldIndex == 1) {
				int fieldType = FieldValue.FT_NUMERIC_64;
				fieldValue.setValue(fieldType, new Long(2));
				return fieldType;
			} else if (fieldIndex == 2) {
				int fieldType = FieldValue.FT_NUMERIC_FLOATING;
				fieldValue.setValue(fieldType, new Double(3.0));
				return fieldType;
			} else if (fieldIndex == 3) {
				int fieldType = FieldValue.FT_BOOLEAN;
				fieldValue.setValue(fieldType, new Boolean(true));
				return fieldType;
			} else if (fieldIndex == 4) {
				int fieldType = FieldValue.FT_DATE;
				Calendar cal = Calendar.getInstance();
				LocalDate date = new LocalDate(cal.get(Calendar.YEAR), cal
						.get(Calendar.MONTH) + 1, cal
						.get(Calendar.DAY_OF_MONTH));
				fieldValue.setValue(fieldType, date);
				if (log.isDebugEnabled()) {
					log.debug("Drives.contentGetValue() date="
							+ date.getYear() + " " + date.getMonth() + " "
							+ date.getDay());
				}
				return fieldType;
			} else if (fieldIndex == 5) {
				int fieldType = FieldValue.FT_TIME;
				Calendar cal = Calendar.getInstance();
				LocalTime time = new LocalTime(cal.get(Calendar.HOUR_OF_DAY),
						cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
				fieldValue.setValue(fieldType, time);
				if (log.isDebugEnabled()) {
					log.debug("Drives.contentGetValue() time="
							+ time.getHour() + " " + time.getMinute() + " "
							+ time.getSecond());
				}
				return fieldType;
			} else if (fieldIndex == 6) {
				int fieldType = FieldValue.FT_DATETIME;
				FileTime ftLastWriteTime = new FileTime();
				ftLastWriteTime.setDate(new File(fileName).lastModified());
				fieldValue.setValue(fieldType, ftLastWriteTime);
				if (log.isDebugEnabled()) {
					log.debug("Drives.contentGetValue() datetime="
							+ ftLastWriteTime);
				}
				return fieldType;
			} else if (fieldIndex == 7) {
				int fieldType = FieldValue.FT_STRING;
				fieldValue.setValue(fieldType, new String("huhu!"));
				return fieldType;
			} else if (fieldIndex == 8) {
				int fieldType = FieldValue.FT_MULTIPLECHOICE;
				fieldValue.setValue(fieldType, "c2");
				return fieldType;
			} else if (fieldIndex == 9) {
				int fieldType = FieldValue.FT_STRING;
//				if ((flags & CONTENT_DELAYIFSLOW) != 0) {
//					if (log.isDebugEnabled()) {
//						log
//								.debug("Drives.contentGetValue() CONTENT_DELAYIFSLOW");
//					}
//					fieldValue.setValue(fieldType, new String("delayed"));
//					return FieldValue.FT_DELAYED;
//				}
				fieldValue.setValue(fieldType, new String("huhu!"));
				return fieldType;
			} else if (fieldIndex == 10) {
				int fieldType = FieldValue.FT_FULLTEXT;
				fieldValue.setValue(fieldType, new String("long!"));
				return fieldType;
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			return FieldValue.FT_FIELDEMPTY;
		}
		return FieldValue.FT_NOMOREFIELDS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int fsContentGetSupportedFieldFlags(int fieldIndex) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsContentGetSupportedFieldFlags()");
		}
		if (fieldIndex == -1) {
			// return if field editing is supported
			if (log.isDebugEnabled()) {
				log
						.debug("CONTFLAGS_SUBSTMASK | CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT");
			}
			// CONTFLAGS_SUBSTMASK: all flags are uses by the plugin fields
			// CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT: custom field editor support
			return CONTFLAGS_SUBSTMASK | CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT;
		}
		if (fieldIndex == 0 | fieldIndex == 1) {
			// FT_NUMERIC_32/64 supports field editing
			if (log.isDebugEnabled()) {
				log.debug("CONTFLAGS_EDIT | CONTFLAGS_SUBSTATTRIBUTES");
			}
			// use file numeric editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_SUBSTATTRIBUTES;
		}
		if (fieldIndex == 2) {
			// FT_NUMERIC_FLOATING supports field editing
			if (log.isDebugEnabled()) {
				log.debug("CONTFLAGS_EDIT | CONTFLAGS_PASSTHROUGH_SIZE_FLOAT");
			}
			// use file float editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_PASSTHROUGH_SIZE_FLOAT;
		}
		if (fieldIndex == 3) {
			// FT_BOOLEAN supports field editing
			if (log.isDebugEnabled()) {
				log.error("FT_BOOLEAN currently unsupported!");
			}
			// use custom editor for that field
			return CONTFLAGS_EDIT;
		}
		if (fieldIndex == 4) {
			// FT_DATE supports field editing
			if (log.isDebugEnabled()) {
				log.debug("CONTFLAGS_EDIT | CONTFLAGS_SUBSTDATE");
			}
			// use file date editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_SUBSTDATE;
		}
		if (fieldIndex == 5) {
			// FT_TIME supports field editing
			if (log.isDebugEnabled()) {
				log.debug("CONTFLAGS_EDIT | CONTFLAGS_SUBSTTIME");
			}
			// use file time editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_SUBSTTIME;
		}
		if (fieldIndex == 6) {
			// FT_DATETIME supports field editing
			if (log.isDebugEnabled()) {
				log.debug("CONTFLAGS_EDIT | CONTFLAGS_SUBSTDATETIME");
			}
			// use file date/time editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_SUBSTDATETIME;
		}
		if (fieldIndex == 7) {
			// FT_STRING supports custom field editing
			if (log.isDebugEnabled()) {
				log.debug("CONTFLAGS_EDIT");
			}
			// use text editor for that field
			return CONTFLAGS_EDIT;
		}
		if (fieldIndex == 8) {
			// FT_MULTIPLECHOICE supports custom field editing
			if (log.isDebugEnabled()) {
				log.debug("CONTFLAGS_EDIT");
			}
			// use combo box for that field
			return CONTFLAGS_EDIT;
		}
		if (fieldIndex == 9) {
			// FT_STRING supports custom field editing
			if (log.isDebugEnabled()) {
				log.debug("CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT");
			}
			// use custom editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT;
		}
		return super.fsContentGetSupportedFieldFlags(fieldIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int fsContentSetValue(String fileName, int fieldIndex, int unitIndex, int fieldType, FieldValue fieldValue, int flags) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsContentSetValue()");
		}
		// TODO Auto-generated method stub
		return super.fsContentSetValue(fileName, fieldIndex, unitIndex, fieldType,
				fieldValue, flags);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int fsGetPreviewBitmap(String remoteName, int width, int height, StringBuffer filename) {
		if (log.isDebugEnabled()) {
			log.debug("Drives.fsGetPreviewBitmap()");
		}
		filename.append("%CWD%\\Angler.bmp");
		return FS_ICON_EXTRACTED;
	}
}
