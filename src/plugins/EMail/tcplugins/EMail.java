package tcplugins;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.FileTime;
import plugins.wfx.RemoteInfo;
import plugins.wfx.WFXPluginAdapter;
import plugins.wfx.WFXPluginInterface;
import plugins.wfx.Win32FindData;

/**
 * This is an eMail Plugin class.
 * 
 * @author Ken
 */
public class EMail extends WFXPluginAdapter {

	/**
	 * default eMail URL.
	 */
	private static final String DEFAULT_URL = "pop3://<user>:<password>"
			+ "@pop.mail.yahoo.de";

	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(EMail.class);

	/**
	 * eMail URL.
	 */
	private String fUrl = DEFAULT_URL;

	/**
	 * The eMail store.
	 */
	private Store store;

	/**
	 * The eMail folder.
	 */
	private Folder folder;

	/**
	 * All messages of the mail folder.
	 */
	private Message[] msgs;

	/**
	 * Number of total messages of the folder.
	 */
	private int totalMessages;

	/**
	 * This is the memorable message information.
	 * 
	 * @author Ken
	 */
	private class MessageInfo {

		/**
		 * The last procesed read message.
		 */
		private int lastMessage;

		/**
		 * The last processed message.
		 */
		private Message message;

	}

	/**
	 * Create an eMail plugin instance.
	 */
	public EMail() {
		super();
		if (log.isDebugEnabled()) {
			log.debug("Email.Email()");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object fsFindFirst(final String path,
			final Win32FindData findData) {
		if (log.isDebugEnabled()) {
			log.debug("Email.fsFindFirst()");
		}
		MessageInfo messageInfo = new MessageInfo();
		// plugin ROOT
		if ("\\".equals(path)) {
			// Attributes & Flags for all messages ..
			try {
				if (DEFAULT_URL.equals(fUrl)) {
					configureEmail();
				}
				long lastErrorMessage = connect();
				if (Win32FindData.ERROR_SUCCESS != lastErrorMessage) {
					findData.setLastErrorMessage(lastErrorMessage);
					return INVALID_HANDLE_VALUE;
				}
				messageInfo.lastMessage = totalMessages - 1;
				messageInfo.message = msgs[messageInfo.lastMessage];

				String messageName = getMessageName(messageInfo.message);
				fsSetProgress(getPluginNr(), fUrl + "("
						+ (messageInfo.lastMessage + 1) + ")", "\\"
						+ messageName,
						(totalMessages - messageInfo.lastMessage)
								* WFXPluginInterface.HUNDRED_PERCENT
								/ totalMessages);
				setFromMessage(findData, messageInfo.message);
				log.debug(messageInfo.message.toString());
				return messageInfo;
			} catch (MessagingException e) {
				fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, e.getMessage());
				log.error(e.getMessage(), e);
				long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
				findData.setLastErrorMessage(lastErrorMessage);
				return INVALID_HANDLE_VALUE;
			}

		} else {
			// only ROOT is supported
			long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
			findData.setLastErrorMessage(lastErrorMessage);
			return INVALID_HANDLE_VALUE;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean fsFindNext(final Object handle,
			final Win32FindData findData) {
		if (log.isDebugEnabled()) {
			log.debug("Email.fsFindNext()");
		}
		MessageInfo messageInfo = (MessageInfo) handle;
		if (msgs != null) {
			if (messageInfo.lastMessage - 1 >= 0) {
				int nextMessage = --messageInfo.lastMessage;
				String messageName;
				try {
					messageName = getMessageName(messageInfo.message);
					int status = fsSetProgress(getPluginNr(), fUrl + "("
							+ (messageInfo.lastMessage + 1) + ")", "\\"
							+ messageName,
							(totalMessages - messageInfo.lastMessage)
									* WFXPluginInterface.HUNDRED_PERCENT
									/ totalMessages);
					if (status == 1) {
						// operation canceled by the user?
						fsLog(getPluginNr(), MSGTYPE_DETAILS,
								"operation canceled by the user");
						return false;
					}
				} catch (MessagingException e) {
					log.error(e.getMessage(), e);
				}
				messageInfo.lastMessage = nextMessage;
				messageInfo.message = msgs[nextMessage];
				setFromMessage(findData, messageInfo.message);
				log.debug(messageInfo.message.toString());
				return true;
			} else {
				// no more messages
				return false;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int fsFindClose(final Object handle) {
		disconnect();
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int fsExecuteFile(final int mainWin, final String remoteName,
			final String verb) {
		if ("open".equals(verb)) {
			if (log.isDebugEnabled()) {
				log.debug("Email.fsExecuteFile() open");
			}
			return FS_EXEC_ERROR;
		} else if ("properties".equals(verb)) {
			if (log.isDebugEnabled()) {
				log.debug("Email.fsExecuteFile() properties");
			}
			configureEmail();
		}
		return FS_EXEC_OK;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int fsGetFile(final String remoteName, final String localName,
			final int copyFlags, final RemoteInfo remoteInfo) {
		if (log.isDebugEnabled()) {
			log.debug("Email.fsGetFile()");
		}
		try {
			long lastErrorMessage = connect();
			if (Win32FindData.ERROR_SUCCESS != lastErrorMessage) {
				return FS_FILE_NOTFOUND;
			}
			if (log.isDebugEnabled()) {
				log.debug("Email.fsGetFile() CONNECTED");
			}
			int index = Integer.valueOf(
					remoteName.substring(1, remoteName.indexOf('_')))
					.intValue();
			if (log.isDebugEnabled()) {
				log.debug("Email.fsGetFile() SEARCH COMPLETE");
			}
			Message msg = msgs[index];
			boolean overwrite = (copyFlags & FS_COPYFLAGS_OVERWRITE) != 0;
			boolean resume = (copyFlags & FS_COPYFLAGS_RESUME) != 0;
			boolean move = (copyFlags & FS_COPYFLAGS_MOVE) != 0;
			if (resume) {
				return FS_FILE_NOTSUPPORTED;
			}
			File localFile = new File(localName);
			if (move) {
				return FS_FILE_NOTSUPPORTED;
			} else {
				if (overwrite) {
					localFile.delete();
				}

				if (localFile.exists()) {
					return FS_FILE_EXISTS;
				}
				if (log.isDebugEnabled()) {
					log.debug("Email.fsGetFile() do copy");
				}
				PrintStream out = new PrintStream(new BufferedOutputStream(
						new FileOutputStream(localFile)));
				int ok = copyFile(msg, out, overwrite);
				out.flush();
				out.close();
				if (log.isDebugEnabled()) {
					log.debug("Email.copyFile() length=" + localFile.length());
				}
				return ok;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return FS_FILE_NOTSUPPORTED;
		} finally {
			disconnect();
		}
	}

	/**
	 * Connect to the message store.
	 * 
	 * @return Win32FindData.ERROR_NO_MORE_FILES | Win32FindData.ERROR_SUCCESS
	 * @throws MessagingException
	 *             error communcating with the message store
	 */
	private long connect() throws MessagingException {
		if (log.isDebugEnabled()) {
			log.debug("Email.connect()");
		}
		// parameters
		boolean debug = true;
		String mbox = "INBOX";

		// Get a Properties object
		Properties props = System.getProperties();

		// Get a Session object
		Session session = Session.getInstance(props, null);
		session.setDebug(debug);

		// Get a Store object
		store = null;
		URLName urln = new URLName(fUrl);
		try {
			store = session.getStore(urln);
		} catch (NoSuchProviderException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, e.getMessage());
			log.error(e.getMessage(), e);
			long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
			return lastErrorMessage;
		}
		store.addStoreListener(new StoreListener() {
			public void notification(final StoreEvent e) {
				String s;
				if (e.getMessageType() == StoreEvent.ALERT) {
					s = "ALERT: ";
				} else {
					s = "NOTICE: ";
				}
				log.debug(s + e.getMessage());
			}
		});
		try {
			store.connect();
		} catch (MessagingException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, e.getMessage());
			log.error(e.getMessage(), e);
			long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
			return lastErrorMessage;
		}

		// Open the Folder
		try {
			folder = store.getDefaultFolder();
		} catch (MessagingException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, e.getMessage());
			log.error(e.getMessage(), e);
			long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
			return lastErrorMessage;
		}
		if (folder == null) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, "No default folder");
			log.error("No default folder");
			long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
			return lastErrorMessage;
		}

		if (mbox == null) {
			mbox = "INBOX";
		}
		try {
			folder = folder.getFolder(mbox);
		} catch (MessagingException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, "No default folder");
			log.error(e.getMessage(), e);
		}
		if (folder == null) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, "Invalid folder");
			log.error("Invalid folder");
			long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
			return lastErrorMessage;
		}
		// try to open read/write and if that fails try read-only
		try {
			folder.open(Folder.READ_WRITE);
		} catch (MessagingException ex) {
			try {
				folder.open(Folder.READ_ONLY);
			} catch (MessagingException e) {
				fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR,
						"No default folder");
				log.error("Folder not readable");
				long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
				return lastErrorMessage;
			}
		}
		// read message count
		try {
			totalMessages = folder.getMessageCount();
		} catch (MessagingException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR,
					"Message count undetermined");
			log.error("Message count undetermined");
			long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
			return lastErrorMessage;
		}
		if (totalMessages == 0) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, "Empty folder");
			log.error("Empty folder");
			long lastErrorMessage = Win32FindData.ERROR_NO_MORE_FILES;
			return lastErrorMessage;
		}
		if (log.isDebugEnabled()) {
			log.debug("Total messages = " + totalMessages);
		}
		msgs = folder.getMessages();
		if (log.isDebugEnabled()) {
			log.debug("Email.connect() folder.getMessages() COMPLETE");
		}
		return Win32FindData.ERROR_SUCCESS;
	}

	/**
	 * Disconnect from the message store.
	 */
	private void disconnect() {
		if (log.isDebugEnabled()) {
			log.debug("Email.disconnect()");
		}
		try {
			if (folder != null) {
				folder.close(false);
			}
			if (store != null) {
				store.close();
			}
		} catch (MessagingException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, e.getMessage());
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Set Win32FindData information from eMail message.
	 * 
	 * @param findData
	 *            the Win32FindData to fill
	 * @param message
	 *            the eMail message
	 */
	private void setFromMessage(final Win32FindData findData,
			final Message message) {
		findData.setFileAttributes(Win32FindData.FILE_ATTRIBUTE_NORMAL);
		try {
			findData.setFileName(getMessageName(message));
		} catch (MessagingException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, e.getMessage());
			findData.setFileName(e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, e.getMessage());
			findData.setFileName(e.getMessage());
		}
		try {
			findData.setFileSizeLow(message.getSize());
		} catch (MessagingException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, e.getMessage());
			findData.setFileSizeLow(0);
		}
		try {
			if (message.getSentDate() != null) {
				FileTime fileTime = new FileTime();
				fileTime.setDate(message.getSentDate().getTime());
				findData.setLastWriteTime(fileTime);
			}
		} catch (MessagingException e) {
			fsLog(getPluginNr(), MSGTYPE_IMPORTANTERROR, e.getMessage());
			FileTime fileTime = new FileTime();
			fileTime.setDate(Calendar.getInstance().getTime().getTime());
			findData.setLastWriteTime(fileTime);
		}
	}

	/**
	 * Get the file name of the message.
	 * 
	 * @param message
	 *            the message
	 * @return the filename
	 * @throws MessagingException
	 *             message details could not be determined
	 */
	private String getMessageName(final Message message)
			throws MessagingException {
		return (message.getMessageNumber() - 1)
				+ "_"
				+ (message.getSubject() != null ? message.getSubject().replace(
						'\"', '\'') : "") + " ("
				+ message.getFrom()[0].toString().replace('\"', '\'') + ").eml";
	}

	/**
	 * @param messageStream
	 * @param dest
	 * @param overwrite
	 * @return
	 */
	private int copyFile(Part p, PrintStream out, final boolean overwrite) {
		try {
			if (log.isDebugEnabled()) {
				String ct = p.getContentType();
				try {
					if (log.isDebugEnabled()) {
						log.debug("Email.copyFile() CONTENT-TYPE: "
								+ (new ContentType(ct)).toString());
					}
				} catch (ParseException pex) {
					log.error("BAD CONTENT-TYPE: " + ct, pex);
				}
				String filename = p.getFileName();
				if (filename != null)
					log.debug("Email.copyFile() FILENAME: " + filename);
			}

			/*
			 * Using isMimeType to determine the content type avoids fetching
			 * the actual content data until we need it.
			 */
			if (p.isMimeType("text/plain")) {
				if (log.isDebugEnabled()) {
					log.debug("This is plain text");
					log.debug("---------------------------");
				}
				if (log.isDebugEnabled()) {
					log.debug("Email.copyFile() GET CONTENT");
				}
				if (p.getContent() instanceof InputStream) {
					InputStream is = (InputStream) p.getContent();
					int c;
					int i = 0;
					while ((c = is.read()) != -1) {
						++i;
						fsSetProgress(getPluginNr(), "server", "client", (i
								* WFXPluginInterface.HUNDRED_PERCENT / (is
								.available() + 1)));
						out.print((char) c);
					}
					is.close();
				} else {
					out.println((String) p.getContent());
				}
				if (log.isDebugEnabled()) {
					log.debug("Email.copyFile() END GET CONTENT");
				}
			} else if (p.isMimeType("multipart/*")) {
				if (log.isDebugEnabled()) {
					log.debug("This is a Multipart");
					log.debug("---------------------------");
				}
				if (log.isDebugEnabled()) {
					log.debug("Email.copyFile() GET CONTENT");
				}
				if (p.getContent() instanceof String) {
					if (log.isDebugEnabled()) {
						log.debug("This is a string");
						log.debug("---------------------------");
					}
					out.println((String) p.getContent());
				} else if (p.getContent() instanceof InputStream) {
					InputStream is = (InputStream) p.getContent();
					int c;
					int i = 0;
					while ((c = is.read()) != -1) {
						++i;
						fsSetProgress(getPluginNr(), "server", "client", (i
								* WFXPluginInterface.HUNDRED_PERCENT / (is
								.available() + 1)));
						out.print((char) c);
					}
					is.close();
				} else if (p.getContent() instanceof Multipart) {
					Multipart mp = (Multipart) p.getContent();
					if (log.isDebugEnabled()) {
						log.debug("Email.copyFile() END GET CONTENT");
					}
					int count = mp.getCount();
					for (int i = 0; i < count; i++) {
						fsSetProgress(getPluginNr(), "server", "client", (i
								* WFXPluginInterface.HUNDRED_PERCENT / count));
						copyFile(mp.getBodyPart(i), out, overwrite);
					}
				}
			} else if (p.isMimeType("message/rfc822")) {
				if (log.isDebugEnabled()) {
					log.debug("This is a Nested Message");
					log.debug("---------------------------");
				}
				if (log.isDebugEnabled()) {
					log.debug("Email.copyFile() GET CONTENT");
				}
				copyFile((Part) p.getContent(), out, overwrite);
				if (log.isDebugEnabled()) {
					log.debug("Email.copyFile() END GET CONTENT");
				}
			} else {
				/*
				 * If we actually want to see the data, and it's not a MIME type
				 * we know, fetch it and check its Java type.
				 */
				if (log.isDebugEnabled()) {
					log.debug("Email.copyFile() GET CONTENT");
				}
				Object o = p.getContent();
				if (log.isDebugEnabled()) {
					log.debug("Email.copyFile() END GET CONTENT");
				}
				if (o instanceof String) {
					if (log.isDebugEnabled()) {
						log.debug("This is a string");
						log.debug("---------------------------");
					}
					out.println((String) o);
				} else if (o instanceof InputStream) {
					if (log.isDebugEnabled()) {
						log.debug("This is just an input stream");
						log.debug("---------------------------");
					}
					InputStream is = (InputStream) o;
					int c;
					int i = 0;
					while ((c = is.read()) != -1) {
						++i;
						fsSetProgress(getPluginNr(), "server", "client", (i
								* WFXPluginInterface.HUNDRED_PERCENT / (is
								.available() + 1)));
						out.print((char) c);
					}
					is.close();
				} else {
					if (log.isDebugEnabled()) {
						log.debug("This is an unknown type");
						log.debug("---------------------------");
					}
					out.println(o.toString());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return FS_FILE_OK;
	}

	/**
	 * configure eMail client
	 */
	private void configureEmail() {
		StringBuffer urlBuffer = new StringBuffer(fUrl);
		fsRequest(getPluginNr(), RT_URL,
				"Please specify your eMail Account URL", null, urlBuffer);
		String newUrl = urlBuffer.substring(fUrl.length());
		if (log.isDebugEnabled()) {
			log.debug("Email.fsExecuteFile() newUrl=" + newUrl);
		}

		fUrl = newUrl;
	}

}
