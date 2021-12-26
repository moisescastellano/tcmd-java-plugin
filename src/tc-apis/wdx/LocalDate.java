/**
 * 
 */
package plugins.wdx;

/**
 * LocalDate can be used for setting the date value in ContentGetValue.
 * 
 * @author Ken
 */
public class LocalDate {

	/**
	 * 
	 */
	private int fYear;

	/**
	 * @return Returns the fYear.
	 */
	public final int getYear() {
		return fYear;
	}

	/**
	 * 
	 */
	private int fMonth;

	/**
	 * @return Returns the fMonth.
	 */
	public final int getMonth() {
		return fMonth;
	}

	/**
	 * 
	 */
	private int fDay;

	/**
	 * @return Returns the fDay.
	 */
	public final int getDay() {
		return fDay;
	}

	private LocalDate() {
	}
	
	/**
	 * Create a new local date.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param day
	 *            the day
	 */
	public LocalDate(final int year, final int month, final int day) {
		this.fYear = year;
		this.fMonth = month;
		this.fDay = day;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return fDay + "." + fMonth + "." + fYear;
	}
}
