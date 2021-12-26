/**
 * 
 */
package plugins.wdx;

/**
 * LocalTime can be used for setting the time value in ContentGetValue.
 * 
 * @author Ken
 */
public class LocalTime {

	/**
	 * 
	 */
	private int fHour;

	/**
	 * @return Returns the fHour.
	 */
	public final int getHour() {
		return fHour;
	}

	/**
	 * 
	 */
	private int fMinute;

	/**
	 * @return Returns the fMinute.
	 */
	public final int getMinute() {
		return fMinute;
	}

	/**
	 * 
	 */
	private int fSecond;

	/**
	 * @return Returns the fSecond.
	 */
	public final int getSecond() {
		return fSecond;
	}

	private LocalTime() {
	}
	
	/**
	 * Create a new local time.
	 * 
	 * @param hour
	 *            the hour
	 * @param minute
	 *            the minute
	 * @param second
	 *            the second
	 */
	public LocalTime(final int hour, final int minute, final int second) {
		fHour = hour;
		fMinute = minute;
		fSecond = second;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return fHour + ":" + fMinute + "." + fSecond;
	}

}
