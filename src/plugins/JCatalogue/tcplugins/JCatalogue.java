package tcplugins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.wcx.HeaderData;
import plugins.wcx.OpenArchiveData;
import plugins.wcx.WCXPluginAdapter;

public class JCatalogue extends WCXPluginAdapter {

	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(JCatalogue.class);

	/**
	 * catalog file: Column with fully qualified name.
	 */
	public static final int COL_FULLNAME = 0;

	/**
	 * catalog file: Column with file size.
	 */
	public static final int COL_SIZE = 1;

	/**
	 * catalog file: Column with file attributes.
	 */
	public static final int COL_ATTR = 2;

	/**
	 * catalog file: Column with file time.
	 */
	public static final int COL_DATE = 3;

	/**
	 * The catalog date picture.
	 */
	private static final String DATE_PATTERN = "dd.MM.yyyy G 'at' HH:mm:ss Z";

	/**
	 * The default locale.
	 */
	private Locale locale = Locale.getDefault();

	/**
	 * Information of a JLST file.
	 * 
	 * @author DE15267
	 * 
	 */
	private class CatalogInfo {
		/**
		 * The name of the archive.
		 */
		private String arcName;

		/**
		 * The JLST reader itself.
		 */
		private RandomAccessFile file;

		/**
		 * The current file pointer.
		 */
		private long fp;

		/**
		 * The current header data of the JLST file.
		 */
		private HeaderData headerData;
	}

	@Override
	public Object openArchive(OpenArchiveData archiveData) {
		if (log.isDebugEnabled()) {
			log
					.debug("JCatalogue.openArchive(archiveData)");
		}
		try {
			RandomAccessFile pf = new RandomAccessFile(
					archiveData.getArcName(), "rw");
			CatalogInfo catalogInfo = new CatalogInfo();
			catalogInfo.file = pf;
			catalogInfo.arcName = archiveData.getArcName();
			return catalogInfo;
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public int closeArchive(Object archiveData) {
		if (log.isDebugEnabled()) {
			log.debug("JCatalogue.closeArchive(archiveData)");
		}
		RandomAccessFile pf = ((CatalogInfo) archiveData).file;
		try {
			pf.close();
			return SUCCESS;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return E_ECLOSE;
	}

	@Override
	public int processFile(Object archiveData, int operation, String destPath,
			String destName) {
		if (log.isDebugEnabled()) {
			log
					.debug("JCatalogue.processFile(archiveData, operation, destPath, destName)");
		}
		CatalogInfo catalogInfo = (CatalogInfo) archiveData;
		HeaderData headerData = catalogInfo.headerData;
		if (log.isDebugEnabled()) {
			log.debug("JCatalogue.processFile() headerData = " + headerData.getFileName() + ", destPath = " + destPath
					+ ",  destName = " + destName + ", operation = " + operation);
		}
		try {
			if (operation == PK_EXTRACT) {
				String fullName;
				if (destPath == null) {
					fullName = destName;
				} else {
					fullName = destPath + destName;
				}
				if (log.isDebugEnabled()) {
					log.debug("JCatalogue.processFile() EXTRACT " + fullName);
				}
				copyFile(new File(headerData.getFileName().replace('_', ':')),
						new File(fullName), false);
			} else if (operation == PK_TEST) {
				if (log.isDebugEnabled()) {
					log.debug("JCatalogue.processFile() TEST "
							+ headerData.getFileName());
				}
			} else if (operation == PK_SKIP) {
				if (log.isDebugEnabled()) {
					log.debug("JCatalogue.processFile() SKIP "
							+ headerData.getFileName());
				}
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}

	@Override
	public int readHeader(Object archiveData, HeaderData headerData) {
		if (log.isDebugEnabled()) {
			log.debug("JCatalogue.readHeader(archiveData, headerData)");
		}
		try {
			CatalogInfo catalogInfo = (CatalogInfo) archiveData;
			catalogInfo.headerData = headerData;
			RandomAccessFile file = catalogInfo.file;
			headerData.setArcName(catalogInfo.arcName);
			if (catalogInfo.fp != -1) {
				file.seek(file.getFilePointer());
				log.debug("seek to " + file.getFilePointer());
			} else {
				catalogInfo.fp = file.getFilePointer();
				log.debug("fp=" + catalogInfo.fp);
			}
			String line = file.readLine();
			log.debug("read line=" + line);
			if (line != null) {
				StringTokenizer tok = new StringTokenizer(line, "\t", false);
				int col = 0;
				while (tok.hasMoreTokens()) {
					switch (col) {
					case COL_FULLNAME:
						// filename
						String fileName = tok.nextToken();
						headerData.setFileName(fileName.replace(':', '_'));
						if (log.isDebugEnabled()) {
							log.debug("JCatalogue.readHeader() fileName="
									+ headerData.getFileName());
						}
						break;

					case COL_SIZE:
						// size
						long size = Long.parseLong(tok.nextToken());
						headerData.setUnpSize(size);
						headerData.setPackSize(0);
						if (log.isDebugEnabled()) {
							log.debug("JCatalogue.readHeader() size="
									+ headerData.getPackSize());
						}
						break;

					case COL_ATTR:
						// attributes
						boolean canWrite = Boolean
								.parseBoolean(tok.nextToken());
						if (!canWrite) {
							int fileAttr = HeaderData.FILE_ATTRIBUTE_READONLY;
							headerData.setFileAttr(fileAttr);
						}
						if (log.isDebugEnabled()) {
							log.debug("JCatalogue.readHeader() fileAttr="
									+ headerData.getFileAttr());
						}
						break;

					case COL_DATE:
						// date
						SimpleDateFormat df = new SimpleDateFormat();
						df.applyPattern(DATE_PATTERN);
						try {
							Calendar cal = Calendar.getInstance(locale);
							cal.setTime(df.parse(tok.nextToken()));
							headerData.setFileTime(cal);
						} catch (ParseException e) {
							log.error(e.getMessage(), e);
						}
						if (log.isDebugEnabled()) {
							log.debug("JCatalogue.readHeader() time="
									+ headerData.getFileTime());
						}
						break;

					default:
						break;
					}
					col++;
				}
				log.debug("SUCCESS");
				return SUCCESS;
			}
			log.debug("END_OF_ARCHIVE");
			return E_END_ARCHIVE;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		if (log.isErrorEnabled()) {
			log.error("JCatalogue.readHeader() E_BAD_DATA");
		}
		return E_BAD_DATA;
	}

	@Override
	public int getPackerCaps() {
		return PK_CAPS_HIDE | PK_CAPS_NEW | PK_CAPS_MULTIPLE | PK_CAPS_MEMPACK;
	}

	@Override
	public int packFiles(String packedFile, String subPath, String srcPath,
			String addList, int flags) {
		try {
			File pf = new File(packedFile);
			PrintWriter writer = new PrintWriter(new BufferedWriter(
					new FileWriter(pf)));
			StringTokenizer tok = new StringTokenizer(addList, ":");
			while (tok.hasMoreTokens()) {
				String addFilename = tok.nextToken();
				if (!addFilename.equals("")) {
					String fullName = srcPath + addFilename;
					File file = new File(fullName);
					Calendar cal = Calendar.getInstance(locale);
					cal.setTimeInMillis(file.lastModified());
					SimpleDateFormat df = new SimpleDateFormat();
					df.applyPattern(DATE_PATTERN);
					writer.print(fullName);
					writer.print("\t" + file.length());
					writer.print("\t" + file.canWrite());
					writer.print("\t" + df.format(cal.getTime()));
					writer.println();
					if (log.isDebugEnabled()) {
						log.debug("JCatalogue.packFiles() filename="
								+ fullName);
					}
				}
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
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
			return E_ECREATE;
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
			return E_EOPEN;
		} catch (IOException ioe) {
			return E_EWRITE;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (bw != null) {
					bw.close();
				}
			} catch (IOException ioe) {
				return E_EWRITE;
			}
		}
		return SUCCESS;
	}

}
