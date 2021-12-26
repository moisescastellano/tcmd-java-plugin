package tcplugins;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wcx.HeaderData;
import plugins.wcx.OpenArchiveData;
import plugins.wcx.WCXPluginAdapter;

/**
 * @author Ken CBM emulator file image types support.
 * 
 */
public class DirCBM extends WCXPluginAdapter {

	/** Object for trace-/debug messages. */
	private static Log log = LogFactory.getLog(DirCBM.class);

	/**
	 * @author Ken A generic file image support.
	 * 
	 */
	private class ImageInfo {
		/**
		 * The file image name.
		 */
		private String arcName;

		/**
		 * The type of the file image (IMAGE_TYPE_... constants).
		 */
		private int diskType;

		/**
		 * The file image object specific to the diskType field.
		 */
		private Object fileImage;
	};

	/**
	 * File extension T64.
	 */
	private static final int FILE_EXT_T64 = 64;

	/**
	 * File extension D82.
	 */
	private static final int FILE_EXT_D82 = 82;

	/**
	 * File extension 80.
	 */
	private static final int FILE_EXT_D80 = 80;

	/**
	 * File extension D64.
	 */
	private static final int FILE_EXT_D64 = 64;

	/**
	 * Unknown file image.
	 */
	public static final int IMAGE_TYPE_NO_TYPE = 0;

	/**
	 * D64 file image.
	 */
	public static final int IMAGE_TYPE_1541_D64 = 1;

	/**
	 * D80 file image.
	 */
	public static final int IMAGE_TYPE_8050_D80 = 2;

	/**
	 * D82 file image.
	 */
	public static final int IMAGE_TYPE_8250_D82 = 3;

	/**
	 * T64 file image.
	 */
	public static final int IMAGE_TYPE_T64 = 4;

	/**
	 * The T64 file image implementation.
	 */
	private IFileImage fImage;

	//
	// public methods
	//

	/**
	 * {@inheritDoc}
	 */
	public final Object openArchive(final OpenArchiveData archiveData) {
		try {
			int diskType = IMAGE_TYPE_NO_TYPE;
			String arcName = archiveData.getArcName();
			// check image extension
			if (arcName.indexOf('.') != -1) {
				diskType = getTypeFromExtension(arcName.substring(arcName
						.indexOf('.') + 1));
			}

			// do we have a good type ?
			if (diskType == IMAGE_TYPE_NO_TYPE) {

				if (log.isDebugEnabled()) {
					log.debug("DirCBM.openArchive(ArchiveData)"
							+ "unknown disk type\n");
				}
				return null;
			}

			if (log.isDebugEnabled()) {
				log
						.debug("DirCBM.openArchive(ArchiveData)" + "Open "
								+ arcName);
			}

			File file = new File(arcName);
			if (file == null) {
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.openArchive(ArchiveData)"
							+ "error CreateFile " + arcName + "\n");
				}
				return null;
			}

			// allocate disk memory
			ImageInfo pInfo = new ImageInfo();

			// global struct
			pInfo.diskType = diskType;
			pInfo.arcName = archiveData.getArcName();

			// check image extension
			switch (diskType) {
			case IMAGE_TYPE_T64:
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.openArchive(ArchiveData)"
							+ "tape type = T64\n");
				}
				try {
					// set tape header pointer
					fImage = new T64();
					pInfo.fileImage = fImage.readImage(file);
				} catch (FileNotFoundException e) {
					log.error(e.getMessage(), e);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}

				break;

			case IMAGE_TYPE_1541_D64:
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.openArchive(ArchiveData)"
							+ "disk type = D64\n");
				}
				try {
					// set tape header pointer
					fImage = new D64();
					pInfo.fileImage = fImage.readImage(file);
				} catch (FileNotFoundException e) {
					log.error(e.getMessage(), e);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
				break;

			case IMAGE_TYPE_8050_D80:
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.openArchive(ArchiveData)"
							+ "disk type = D80\n");
				}
				break;
			case IMAGE_TYPE_8250_D82:
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.openArchive(ArchiveData)"
							+ "disk type = D82\n");
				}
				break;

			case IMAGE_TYPE_NO_TYPE:
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.openArchive(ArchiveData)"
							+ "unknown disk type\n");
				}
				return null;

			default:
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.openArchive(ArchiveData)"
							+ "unknown disk type\n");
				}
				return null;
			}

			return (pInfo);
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final int closeArchive(final Object info) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int processFile(final Object archiveData, final int operation,
			final String destPath, final String destName) {
		if (operation == PK_SKIP || operation == PK_TEST) {
			return SUCCESS;
		}
		ImageInfo pInfo = (ImageInfo) archiveData;

		if (pInfo.diskType == IMAGE_TYPE_T64
				|| pInfo.diskType == IMAGE_TYPE_1541_D64) {
			try {
				boolean ok = fImage.getFileContents(pInfo.fileImage, destPath,
						destName);
				if (ok) {
					return SUCCESS;
				}
			} catch (RuntimeException e) {
				log.error(e.getMessage(), e);
			}
		}
		return E_NO_FILES;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int readHeader(final Object archiveData,
			final HeaderData headerData) {
		ImageInfo pInfo = (ImageInfo) archiveData;

		try {
			if (pInfo.diskType == IMAGE_TYPE_T64
					|| pInfo.diskType == IMAGE_TYPE_1541_D64) {
				headerData.setArcName(pInfo.arcName);
				if (fImage.getFile(pInfo.fileImage, headerData)) {
					return SUCCESS;
				}
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
		}
		return E_END_ARCHIVE;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int getPackerCaps() {
		return PK_CAPS_NEW | PK_CAPS_MODIFY | PK_CAPS_MULTIPLE | PK_CAPS_DELETE;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int packFiles(final String packedFile, final String subPath,
			final String srcPath, final String addList, final int flags) {

		if (log.isDebugEnabled()) {
			log.debug("DirCBM.packFiles()" + "PackFiles: PackedFile="
					+ packedFile + ", SubPath=" + subPath + ", SrcPath="
					+ srcPath);
		}
		int diskType = IMAGE_TYPE_NO_TYPE;

		// get disk type from name
		if (packedFile.indexOf('.') != -1) {
			diskType = getTypeFromExtension(packedFile.substring(packedFile
					.indexOf('.') + 1));
		}
		// only disk images are supported now
		if (diskType == IMAGE_TYPE_T64 || diskType == IMAGE_TYPE_NO_TYPE) {
			return E_NOT_SUPPORTED;
		}

		// allocate disk memory
		ImageInfo pInfo = new ImageInfo();

		// global struct
		pInfo.diskType = diskType;
		pInfo.arcName = packedFile;

		File file = new File(packedFile);
		if (file.exists()) {
			if (log.isDebugEnabled()) {
				log.debug("DirCBM.packFiles() "
						+ "opening existing pack file <" + packedFile + ">");
				log.debug("file.length=" + file.length());
			}
			try {
				pInfo.fileImage = fImage.readImage(file);
			} catch (FileNotFoundException e) {
				log.error(e.getMessage(), e);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		} else {
			// archive not available --> create new archive (only for dxx
			// formats)
			if (log.isDebugEnabled()) {
				log.debug("DirCBM.packFiles() " + "create new pack file <"
						+ packedFile + ">");
			}

			// create empty disk images
			switch (diskType) {
			case IMAGE_TYPE_1541_D64:
				pInfo.fileImage = fImage.createImage(file);
				break;

			case IMAGE_TYPE_8050_D80:
				return E_NOT_SUPPORTED;

			case IMAGE_TYPE_8250_D82:
				return E_NOT_SUPPORTED;

			default:
				return E_NOT_SUPPORTED;
			}
		}

		if (!fImage.updateImage(pInfo.fileImage, packedFile, subPath, srcPath,
				addList, flags)) {
			return E_NOT_SUPPORTED;
		}
		return SUCCESS;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int deleteFiles(final String packedFile,
			final String deleteList) {
		if (log.isDebugEnabled()) {
			log.debug("DirCBM.fprintf()" + "DeleteFiles: PackedFile="
					+ packedFile);
		}

		int diskType = IMAGE_TYPE_NO_TYPE;

		// get disk type from name
		if (packedFile.indexOf('.') != -1) {
			diskType = getTypeFromExtension(packedFile.substring(packedFile
					.indexOf('.') + 1));
		}
		// only disk images are supported now
		if (diskType == IMAGE_TYPE_T64 || diskType == IMAGE_TYPE_NO_TYPE) {
			return E_NOT_SUPPORTED;
		}
		return SUCCESS;
	}

	/**
	 * Get the file image type of the file extension.
	 * 
	 * @param extension
	 *            the file extension
	 * @return the file image type
	 */
	public final int getTypeFromExtension(final String extension) {
		int rc = IMAGE_TYPE_NO_TYPE;
		int parseInt = Integer.parseInt(extension.substring(1));

		if (extension.toLowerCase().charAt(0) == 'd') {
			switch (parseInt) {
			case FILE_EXT_D64:
				rc = IMAGE_TYPE_1541_D64;
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.getTypeFromExtension(extension)"
							+ "disk type = D64\n");
				}
				break;

			case FILE_EXT_D80:
				rc = IMAGE_TYPE_8050_D80;
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.getTypeFromExtension(extension)"
							+ "disk type = D80\n");
				}
				break;
			case FILE_EXT_D82:
				rc = IMAGE_TYPE_8250_D82;
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.getTypeFromExtension(extension)"
							+ "disk type = D82\n");
				}
				break;

			default:
				if (log.isDebugEnabled()) {
					log.debug("DirCBM.getTypeFromExtension(extension)"
							+ "unknown disk type\n");
				}
				break;
			}
		} else {
			if (extension.toLowerCase().charAt(0) == 't') {
				switch (parseInt) {
				case FILE_EXT_T64:
					rc = IMAGE_TYPE_T64;
					if (log.isDebugEnabled()) {
						log.debug("DirCBM.getTypeFromExtension(extension)"
								+ "image type = T64\n");
					}
					break;

				default:
					if (log.isDebugEnabled()) {
						log.debug("DirCBM.getTypeFromExtension(extension)"
								+ "unknown disk type\n");
					}
					break;
				}
			} else if (log.isDebugEnabled()) {
				log.debug("DirCBM.getTypeFromExtension(extension)"
						+ "unknown disk type\n");
			}
		}
		return rc;
	}

}
