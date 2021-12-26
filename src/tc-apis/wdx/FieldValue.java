/**
 * 
 */
package plugins.wdx;

import plugins.FileTime;

/**
 * FieldValue is used by contentGetValue. Here the plugin needs to return the
 * requested data. The data format depends on the field type.
 * 
 * @author Ken
 */
public class FieldValue {

	/**
	 * contentGetSupportedField: The FieldIndex is beyond the last available
	 * field.
	 */
	public static final int FT_NOMOREFIELDS = 0;

	/**
	 * contentGetSupportedField: A 32-bit signed number.
	 */
	public static final int FT_NUMERIC_32 = 1;

	/**
	 * contentGetSupportedField: A 64-bit signed number, e.g. for file sizes.
	 */
	public static final int FT_NUMERIC_64 = 2;

	/**
	 * contentGetSupportedField: A double precision floating point number.
	 */
	public static final int FT_NUMERIC_FLOATING = 3;

	/**
	 * contentGetSupportedField: A date value (year, month, day). Date and time
	 * are in local time.
	 */
	public static final int FT_DATE = 4;

	/**
	 * contentGetSupportedField: A time value (hour, minute, second). Date and
	 * time are in local time.
	 */
	public static final int FT_TIME = 5;

	/**
	 * contentGetSupportedField: A true/false value.
	 */
	public static final int FT_BOOLEAN = 6;

	/**
	 * contentGetSupportedField: A value allowing a limited number of choices.
	 * Use the Units field to return all possible values.
	 */
	public static final int FT_MULTIPLECHOICE = 7;

	/**
	 * contentGetSupportedField: A text string.
	 */
	public static final int FT_STRING = 8;

	/**
	 * contentGetSupportedField: A full text (multiple text strings), only used
	 * for searching. Can be used e.g. for searching in the text portion of
	 * binary files, where the plugin makes the necessary translations. All
	 * fields of this type MUST be placed at the END of the field list,
	 * otherwise you will get errors in Total Commander!
	 */
	public static final int FT_FULLTEXT = 9;

	/**
	 * contentGetSupportedField: A timestamp of type FILETIME, as returned e.g.
	 * by FindFirstFile(). It is a 64-bit value representing the number of
	 * 100-nanosecond intervals since January 1, 1601. The time MUST be relative
	 * to universal time (Greenwich mean time) as returned by the file system,
	 * not local time!
	 */
	public static final int FT_DATETIME = 10;

	/**
	 * contentGetValue: error, invalid field number given.
	 */
	public static final int FT_NOSUCHFIELD = -1;

	/**
	 * contentGetValue: file i/o error.
	 */
	public static final int FT_FILEERROR = -2;

	/**
	 * contentGetValue: field valid, but empty.
	 */
	public static final int FT_FIELDEMPTY = -3;

	/**
	 * contentGetValue: field will be retrieved only when user presses
	 * &lt;SPACEBAR&gt;.
	 */
	public static final int FT_ONDEMAND = -4;

	/**
	 * Function not supported.
	 */
	public static final int FT_NOTSUPPORTED = -5;

	/**
	 * contentEditValue(): The user clicked on cancel.
	 */
	public static final int FT_SETCANCEL = -6;

	/**
	 * contentGetValue: field takes a long time to extract -> try again in
	 * background.
	 */
	public static final int FT_DELAYED = 0;

	/**
	 * The field type of this FieldValue.
	 */
	private int fieldType;

	/**
	 * Value, if fieldType == FT_NUMERIC_32.
	 */
	private int intValue;

	/**
	 * Value, if fieldType == FT_NUMERIC_32.
	 * 
	 * @return Returns the intValue.
	 */
	public final int getIntValue() {
		return intValue;
	}

	/**
	 * Value, if fieldType == FT_NUMERIC_64.
	 */
	private long longValue;

	/**
	 * Value, if fieldType == FT_NUMERIC_64.
	 * 
	 * @return Returns the longValue.
	 */
	public final long getLongValue() {
		return longValue;
	}

	/**
	 * Value, if fieldType == FT_NUMERIC_FLOATING.
	 */
	private double doubleValue;

	/**
	 * Value, if fieldType == FT_NUMERIC_FLOATING.
	 * 
	 * @return Returns the doubleValue.
	 */
	public final double getDoubleValue() {
		return doubleValue;
	}

	/**
	 * Value, if fieldType == FT_BOOLEAN.
	 */
	private boolean boolValue;

	/**
	 * Value, if fieldType == FT_BOOLEAN.
	 * 
	 * @return Returns the boolValue.
	 */
	public final boolean isBoolValue() {
		return boolValue;
	}

	/**
	 * Value, if fieldType == FT_MULTIPLECHOICE || fieldType == FT_STRING ||
	 * fieldType == FT_FULLTEXT.
	 */
	private String str;

	/**
	 * Value, if fieldType == FT_MULTIPLECHOICE || fieldType == FT_STRING ||
	 * fieldType == FT_FULLTEXT.
	 * 
	 * @return Returns the str.
	 */
	public final String getStr() {
		return str;
	}

	/**
	 * Value, if fieldType == FT_DATETIME.
	 */
	private FileTime fileTime;

	/**
	 * Value, if fieldType == FT_DATETIME.
	 * 
	 * @return Returns the fileTime.
	 */
	public final FileTime getFileTime() {
		return fileTime;
	}

	/**
	 * Value, if fieldType == FT_DATE.
	 */
	private LocalDate date;

	/**
	 * Value, if fieldType == FT_DATE.
	 * 
	 * @return Returns the date.
	 */
	public final LocalDate getDate() {
		return date;
	}

	/**
	 * Value, if fieldType == FT_TIME.
	 */
	private LocalTime time;

	/**
	 * Value, if fieldType == FT_TIME.
	 * 
	 * @return Returns the time.
	 */
	public final LocalTime getTime() {
		return time;
	}

	/**
	 * @return Returns the fieldType.
	 *         <UL>
	 *         <LI>FT_NUMERIC_32: FieldValue points to a 32-bit signed integer
	 *         variable.
	 *         <LI>FT_NUMERIC_64: FieldValue points to a 64-bit signed integer
	 *         variable.
	 *         <LI>FT_NUMERIC_FLOATING: FieldValue points to a 64-bit floating
	 *         point variable (ISO standard double precision) <BR>
	 *         See remark below about additional string field!
	 *         <LI>FT_DATE: FieldValue points to a structure containing
	 *         year,month,day as 2 byte values.
	 *         <LI>FT_TIME: FieldValue points to a structure containing
	 *         hour,minute,second as 2 byte values.
	 *         <LI>FT_BOOLEAN: FieldValue points to a 32-bit number. 0 neans
	 *         false, anything else means true.
	 *         <LI>FT_STRING or ft_multiplechoice: FieldValue is a pointer to a
	 *         0-terminated string.
	 *         <LI>FT_FULLTEXT: Read maxlen bytes of interpreted data starting
	 *         at offset UnitIndex. The data must be a 0 terminated string.
	 *         <LI>FT_DATETIME: A timestamp of type FILETIME, as returned e.g.
	 *         by FindFirstFile(). It is a 64-bit value representing the number
	 *         of 100-nanosecond intervals since January 1, 1601. The time MUST
	 *         be relative to universal time (Greenwich mean time) as returned
	 *         by the file system, not local time!
	 *         <LI>FT_DELAYED, FT_ONDEMAND: You may return a zero-terminated
	 *         string as in FT_STRING, which will be shown until the actual
	 *         value has been extracted. Requires plugin version>=1.4.
	 *         </UL>
	 */
	public final int getFieldType() {
		return fieldType;
	}

	/**
	 * Create a new field value for the data format of the field type.
	 * 
	 * @param type
	 *            <UL>
	 *            <LI>FT_NUMERIC_32: FieldValue points to a 32-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_64: FieldValue points to a 64-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_FLOATING: FieldValue points to a 64-bit
	 *            floating point variable (ISO standard double precision) <BR>
	 *            See remark below about additional string field!
	 *            <LI>FT_DATE: FieldValue points to a structure containing
	 *            year,month,day as 2 byte values.
	 *            <LI>FT_TIME: FieldValue points to a structure containing
	 *            hour,minute,second as 2 byte values.
	 *            <LI>FT_BOOLEAN: FieldValue points to a 32-bit number. 0 neans
	 *            false, anything else means true.
	 *            <LI>FT_STRING or ft_multiplechoice: FieldValue is a pointer
	 *            to a 0-terminated string.
	 *            <LI>FT_FULLTEXT: Read maxlen bytes of interpreted data
	 *            starting at offset UnitIndex. The data must be a 0 terminated
	 *            string.
	 *            <LI>FT_DATETIME: A timestamp of type FILETIME, as returned
	 *            e.g. by FindFirstFile(). It is a 64-bit value representing the
	 *            number of 100-nanosecond intervals since January 1, 1601. The
	 *            time MUST be relative to universal time (Greenwich mean time)
	 *            as returned by the file system, not local time!
	 *            <LI>FT_DELAYED, FT_ONDEMAND: You may return a zero-terminated
	 *            string as in FT_STRING, which will be shown until the actual
	 *            value has been extracted. Requires plugin version>=1.4.
	 *            </UL>
	 * @param value
	 *            The value of the field type.
	 */
	public final void setValue(final int type, final Object value) {
		this.fieldType = type;
		if (fieldType == FT_NUMERIC_32) {
			this.intValue = ((Integer) value).intValue();
		} else if (fieldType == FT_NUMERIC_64) {
			this.longValue = ((Long) value).longValue();
		} else if (fieldType == FT_NUMERIC_FLOATING) {
			this.doubleValue = ((Double) value).doubleValue();
		} else if (fieldType == FT_DATE) {
			this.date = ((LocalDate) value);
		} else if (fieldType == FT_TIME) {
			this.time = ((LocalTime) value);
		} else if (fieldType == FT_BOOLEAN) {
			this.boolValue = ((Boolean) value).booleanValue();
		} else if (fieldType == FT_MULTIPLECHOICE) {
			this.str = ((String) value);
		} else if (fieldType == FT_STRING) {
			this.str = ((String) value);
		} else if (fieldType == FT_FULLTEXT) {
			this.str = ((String) value);
		} else if (fieldType == FT_DATETIME) {
			this.fileTime = ((FileTime) value);
		}
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		if (fieldType == FT_NUMERIC_32) {
			ret.append(this.intValue);
		} else if (fieldType == FT_NUMERIC_64) {
			ret.append(this.longValue);
		} else if (fieldType == FT_NUMERIC_FLOATING) {
			ret.append(this.doubleValue);
		} else if (fieldType == FT_DATE) {
			ret.append(this.date);
		} else if (fieldType == FT_TIME) {
			ret.append(this.time);
		} else if (fieldType == FT_BOOLEAN) {
			ret.append(this.boolValue);
		} else if (fieldType == FT_MULTIPLECHOICE) {
			ret.append(this.str);
		} else if (fieldType == FT_STRING) {
			ret.append(this.str);
		} else if (fieldType == FT_FULLTEXT) {
			ret.append(this.str);
		} else if (fieldType == FT_DATETIME) {
			ret.append(this.fileTime);
		}
		return ret.toString();
	}

	/**
	 * Parse text as expected type fieldType.
	 * 
	 * @param text
	 *            the string presentation of the value
	 */
	public final void parseValue(final String text) {
		if (fieldType == FieldValue.FT_NUMERIC_32) {
			setValue(FieldValue.FT_NUMERIC_32, new Integer(Integer
					.parseInt(text)));
		} else if (fieldType == FieldValue.FT_NUMERIC_64) {
			setValue(FieldValue.FT_NUMERIC_64, new Long(Long.parseLong(text)));
		} else if (fieldType == FieldValue.FT_NUMERIC_FLOATING) {
			setValue(FieldValue.FT_NUMERIC_FLOATING, new Double(Double
					.parseDouble(text)));
		} else if (fieldType == FieldValue.FT_STRING) {
			setValue(FieldValue.FT_STRING, text);
		}
	}
}
