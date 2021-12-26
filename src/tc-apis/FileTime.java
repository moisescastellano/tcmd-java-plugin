package plugins;

import java.util.Date;

/**
 * File System time stamp. (milliseconds since the epoch 00:00:00 GMT,<BR>
 * January 1, 1970)
 * 
 * @author Ken
 */
public class FileTime {

	/**
	 * The time conversion word size (32-bit).
	 */
	private static final int WORD_SIZE = 32;

	/**
	 * The time base.
	 */
	private static final long TIME_BASE = 116444736000000000L;

	/**
	 * The time conversion factor.
	 */
	private static final int FACTOR = 10000;

	/**
	 * File System time stamp.<BR>
	 * milliseconds since the epoch (00:00:00 GMT, January 1, 1970<BR>
	 * low DWORD
	 */
	private long dwLowDateTime = 0;

	/**
	 * @return Returns the File System time stamp.<BR>
	 *         milliseconds since the epoch (00:00:00 GMT, January 1, 1970<BR>
	 *         low DWORD
	 */
	public final Date getDate() {
		return new Date(dwLowDateTime);
	}

	/**
	 * Set the File System time stamp.<BR>
	 * milliseconds since the epoch (00:00:00 GMT, January 1, 1970<BR>
	 * low DWORD
	 * 
	 * @param lowDateTime
	 *            The dwLowDateTime to set.
	 */
	public final void setDate(final long lowDateTime) {
		this.dwLowDateTime = lowDateTime;
	}

	/**
	 * Create a new time stamp (64-bit value representing the number
	 * of100-nanosecond intervals since January 1, 1601 (UTC).
	 * 
	 * @param low
	 *            Low word of a 64-bit value representing the number of
	 *            100-nanosecond intervals since January 1, 1601 (UTC).
	 * @param high
	 *            High word of a 64-bit value representing the number of
	 *            100-nanosecond intervals since January 1, 1601 (UTC).
	 */
	private FileTime(final long low, final long high) {
		dwLowDateTime = (low + (high << WORD_SIZE) - TIME_BASE) / FACTOR;
	}

	/**
	 * Create a new time stamp: 00:00:00 GMT, January 1, 1970.
	 */
	public FileTime() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return getDate().toString();
	}
}
