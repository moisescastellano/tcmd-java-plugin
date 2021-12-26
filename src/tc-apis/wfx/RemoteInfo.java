/**
 * 
 */
package plugins.wfx;

import plugins.FileTime;

/**
 * RemoteInfoStruct is passed to FsGetFile and FsRenMovFile. It contains details
 * about the remote file being copied.
 * 
 * @author Ken
 */
public class RemoteInfo {

	/**
	 * Low DWORD (32 bit each) of the remote file size. Useful for a progress
	 * indicator.
	 */
	private long fSizeLow;

	/**
	 * @return Returns the the Low DWORD (32 bit each) of the remote file size.
	 *         Useful for a progress indicator.
	 */
	public final long getSizeLow() {
		return fSizeLow;
	}

	/**
	 * High DWORD (32 bit each) of the remote file size. Useful for a progress
	 * indicator.
	 */
	private long fSizeHigh;

	/**
	 * @return Returns the High DWORD (32 bit each) of the remote file size.
	 *         Useful for a progress indicator.
	 */
	public final long getSizeHigh() {
		return fSizeHigh;
	}

	/**
	 * Time stamp of the remote file - should be copied with the file.
	 */
	private FileTime fLastWriteTime;

	/**
	 * @return Returns the Time stamp of the remote file - should be copied with
	 *         the file.
	 */
	public final FileTime getLastWriteTime() {
		return fLastWriteTime;
	}

	/**
	 * Attributes of the remote file - should be copied with the file.
	 */
	private int fAttr;

	/**
	 * @return Returns the Attributes of the remote file - should be copied with
	 *         the file.
	 */
	public final int getAttr() {
		return fAttr;
	}

	/**
	 * RemoteInfoStruct is passed to FsGetFile and FsRenMovFile. It contains
	 * details about the remote file being copied.<BR>
	 * Important note:<BR>
	 * 
	 * This struct is passed to FsGetFile and FsRenMovFile to make it easier for
	 * the plugin to copy the file. You can of course also ignore this
	 * parameter.
	 * 
	 * @param sizeLow
	 *            Low DWORD (32 bit each) of the remote file size. Useful for a
	 *            progress indicator.
	 * @param sizeHigh
	 *            High DWORD (32 bit each) of the remote file size. Useful for a
	 *            progress indicator.
	 * @param lastWriteTime
	 *            Time stamp of the remote file - should be copied with the
	 *            file.
	 * @param attr
	 *            Attributes of the remote file - should be copied with the
	 *            file.
	 */
	public RemoteInfo(final long sizeLow, final long sizeHigh,
			final FileTime lastWriteTime, final int attr) {
		this.fSizeLow = sizeLow;
		this.fSizeHigh = sizeHigh;
		this.fLastWriteTime = lastWriteTime;
		this.fAttr = attr;
	}

}
