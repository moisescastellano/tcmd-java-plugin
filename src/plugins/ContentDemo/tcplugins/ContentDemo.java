package tcplugins;

import java.io.File;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.DefaultParam;
import plugins.FileTime;
import plugins.wdx.FieldValue;
import plugins.wdx.LocalDate;
import plugins.wdx.LocalTime;
import plugins.wdx.WDXPluginAdapter;

public class ContentDemo extends WDXPluginAdapter {
	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(ContentDemo.class);

	/**
	 * the logging support.
	 */
	private Log log2 = LogFactory.getLog("test");

	public ContentDemo() {
		if (log.isDebugEnabled()) {
			log.debug("ContentDemo.ContentDemo()");
		}
	}

	@Override
	public int contentGetSupportedField(int fieldIndex, StringBuffer fieldName,
			StringBuffer units, int maxlen) {
		if (log.isDebugEnabled()) {
			log
					.debug("ContentDemo.contentGetSupportedField(fieldIndex, fieldName, units, maxlen)");
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

	@Override
	public int contentGetValue(String fileName, int fieldIndex, int unitIndex,
			FieldValue fieldValue, int maxlen, int flags) {
		if (log.isDebugEnabled()) {
			log
					.debug("ContentDemo.contentGetValue(fileName, fieldIndex, unitIndex, fieldValue, maxlen, flags)");
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
					log.debug("ContentDemo.contentGetValue() date="
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
					log.debug("ContentDemo.contentGetValue() time="
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
					log.debug("ContentDemo.contentGetValue() datetime="
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
				// if ((flags & CONTENT_DELAYIFSLOW) != 0) {
				// if (log.isDebugEnabled()) {
				// log
				// .debug("ContentDemo.contentGetValue() CONTENT_DELAYIFSLOW");
				// }
				// fieldValue.setValue(fieldType, new String("delayed"));
				// return FieldValue.FT_DELAYED;
				// }
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

	@Override
	public String contentGetDetectString(int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("ContentDemo.contentGetDetectString(maxLen)");
		}
		return "EXT=\"PRG\"";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int contentGetSupportedFieldFlags(int fieldIndex) {
		if (log2.isDebugEnabled()) {
			log2.debug("ContentDemo.contentGetSupportedFieldFlags(fieldIndex="
					+ fieldIndex + ")");
		}
		if (fieldIndex == -1) {
			// return if field editing is supported
			if (log2.isDebugEnabled()) {
				log2
						.debug("CONTFLAGS_SUBSTMASK | CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT");
			}
			// CONTFLAGS_SUBSTMASK: all flags are uses by the plugin fields
			// CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT: custom field editor support
			return CONTFLAGS_SUBSTMASK | CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT;
		}
		if (fieldIndex == 0 | fieldIndex == 1) {
			// FT_NUMERIC_32/64 supports field editing
			if (log2.isDebugEnabled()) {
				log2.debug("CONTFLAGS_EDIT | CONTFLAGS_SUBSTATTRIBUTES");
			}
			// use file numeric editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_SUBSTATTRIBUTES;
		}
		if (fieldIndex == 2) {
			// FT_NUMERIC_FLOATING supports field editing
			if (log2.isDebugEnabled()) {
				log2.debug("CONTFLAGS_EDIT | CONTFLAGS_PASSTHROUGH_SIZE_FLOAT");
			}
			// use file float editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_PASSTHROUGH_SIZE_FLOAT;
		}
		if (fieldIndex == 3) {
			// FT_BOOLEAN supports field editing
			if (log2.isDebugEnabled()) {
				log2.error("FT_BOOLEAN currently unsupported!");
			}
			// use custom editor for that field
			return CONTFLAGS_EDIT;
		}
		if (fieldIndex == 4) {
			// FT_DATE supports field editing
			if (log2.isDebugEnabled()) {
				log2.debug("CONTFLAGS_EDIT | CONTFLAGS_SUBSTDATE");
			}
			// use file date editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_SUBSTDATE;
		}
		if (fieldIndex == 5) {
			// FT_TIME supports field editing
			if (log2.isDebugEnabled()) {
				log2.debug("CONTFLAGS_EDIT | CONTFLAGS_SUBSTTIME");
			}
			// use file time editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_SUBSTTIME;
		}
		if (fieldIndex == 6) {
			// FT_DATETIME supports field editing
			if (log2.isDebugEnabled()) {
				log2.debug("CONTFLAGS_EDIT | CONTFLAGS_SUBSTDATETIME");
			}
			// use file date/time editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_SUBSTDATETIME;
		}
		if (fieldIndex == 7) {
			// FT_STRING supports custom field editing
			if (log2.isDebugEnabled()) {
				log2.debug("CONTFLAGS_EDIT");
			}
			// use text editor for that field
			return CONTFLAGS_EDIT;
		}
		if (fieldIndex == 8) {
			// FT_MULTIPLECHOICE supports custom field editing
			if (log2.isDebugEnabled()) {
				log2.debug("CONTFLAGS_EDIT");
			}
			// use combo box for that field
			return CONTFLAGS_EDIT;
		}
		if (fieldIndex == 9) {
			// FT_STRING supports custom field editing
			if (log2.isDebugEnabled()) {
				log2.debug("CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT");
			}
			// use custom editor for that field
			return CONTFLAGS_EDIT | CONTFLAGS_FIELDEDIT;
		}
		return super.contentGetSupportedFieldFlags(fieldIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int contentSetValue(String fileName, int fieldIndex, int unitIndex,
			int fieldType, FieldValue fieldValue, int flags) {
		if (log2.isDebugEnabled()) {
			log2.debug("ContentDemo.contentSetValue(fileName=" + fileName
					+ ", fieldIndex=" + fieldIndex + ", unitIndex=" + unitIndex
					+ ", fieldType=" + fieldType + ", fieldValue=" + fieldValue
					+ ", flags=" + flags + ")");
		}
		if (fieldIndex == 0 || fieldIndex == 1) {
			if (log2.isDebugEnabled()) {
				log2.debug("Set Numeric 32/64");
			}
			return 0;
		}
		if (fieldIndex == 2) {
			if (log2.isDebugEnabled()) {
				log2.debug("Set Float");
			}
			return 0;
		}
		if (fieldIndex == 3) {
			if (log2.isDebugEnabled()) {
				log2.debug("Set Boolean");
			}
			return 0;
		}
		if (fieldIndex == 4) {
			if (log2.isDebugEnabled()) {
				log2.debug("Set Date");
			}
			return 0;
		}
		if (fieldIndex == 5) {
			if (log2.isDebugEnabled()) {
				log2.debug("Set Time");
			}
			return 0;
		}
		if (fieldIndex == 6) {
			if (log2.isDebugEnabled()) {
				log2.debug("Set Date/Time");
			}
			return 0;
		}
		if (fieldIndex == 7 || fieldIndex == 8 || fieldIndex == 9) {
			if (log2.isDebugEnabled()) {
				log2.debug("Set Custom String");
			}
			return 0;
		}
		if (fieldIndex == -1) {
			if (log2.isDebugEnabled()) {
				log2.debug("End Edit");
			}
			return 0;
		}
		return super.contentSetValue(fileName, fieldIndex, unitIndex,
				fieldType, fieldValue, flags);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int contentEditValue(final int parentWin, int fieldIndex,
			int unitIndex, final int fieldType, final FieldValue fieldValue,
			int maxlen, int flags, String langidentifier) {
		if (log2.isDebugEnabled()) {
			log2.debug("ContentDemo.contentEditValue(parentWin=" + parentWin
					+ ", fieldIndex=" + fieldIndex + ", unitIndex=" + unitIndex
					+ ", fieldType=" + fieldType + ", fieldValue=" + fieldValue
					+ ", flags=" + flags + ")");
		}
		String text = JOptionPane.showInputDialog(new JFrame(), "Enter text:");
		try {
			fieldValue.parseValue(text);
		} catch (NumberFormatException e) {
			log.error(".keyReleased(arg0)", e);
		}
		if (log2.isDebugEnabled()) {
			log2.debug(".keyReleased(arg0) new value=" + text);
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int contentGetDefaultSortOrder(int fieldIndex) {
		if (log2.isDebugEnabled()) {
			log2.debug("ContentDemo.contentGetDefaultSortOrder(fieldIndex="
					+ fieldIndex + ")");
		}
		return super.contentGetDefaultSortOrder(fieldIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contentPluginUnloading() {
		if (log2.isDebugEnabled()) {
			log2.debug("ContentDemo.contentPluginUnloading()");
		}
		super.contentPluginUnloading();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contentSendStateInformation(int state, String path) {
		if (log2.isDebugEnabled()) {
			log2.debug("ContentDemo.contentSendStateInformation(state=" + state
					+ ", path=" + path + ")");
		}
		super.contentSendStateInformation(state, path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contentSetDefaultParams(DefaultParam dps) {
		if (log2.isDebugEnabled()) {
			log2.debug("ContentDemo.contentSetDefaultParams(dps)");
		}
		super.contentSetDefaultParams(dps);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contentStopGetValue(String fileName) {
		if (log2.isDebugEnabled()) {
			log2.debug("ContentDemo.contentStopGetValue(fileName=" + fileName
					+ ")");
		}
		super.contentStopGetValue(fileName);
	}

}
