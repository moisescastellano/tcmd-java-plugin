package plugins.wcx;

/**
 * PackToMem is used in packToMem.
 * 
 * @author Ken
 */
public class PackToMem {

	/**
	 * Has to receive the number of bytes taken from the buffer. If not the
	 * whole buffer is taken, the calling program will pass the remaining bytes
	 * to the plugin in a later call.
	 */
	private int fTaken;

	/**
	 * @return Returns the taken.
	 */
	public final int getTaken() {
		return fTaken;
	}

	/**
	 * @param taken
	 *            The taken to set.
	 */
	public final void setTaken(final int taken) {
		this.fTaken = taken;
	}

	/**
	 * Has to receive the number of bytes placed in the buffer pointed to by
	 * BufOut.
	 */
	private int fWritten;

	/**
	 * @return Returns the written.
	 */
	public final int getWritten() {
		return fWritten;
	}

	/**
	 * @param written
	 *            The written to set.
	 */
	public final void setWritten(final int written) {
		this.fWritten = written;
	}

	/**
	 * packToMem(): Has to receive infos about the operation of this method.
	 */
	public PackToMem() {
	}
}
