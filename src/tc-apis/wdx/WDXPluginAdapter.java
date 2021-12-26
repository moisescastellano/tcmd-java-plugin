/**
 * 
 */
package plugins.wdx;

import plugins.DefaultParam;

/**
 * @author Ken
 * 
 */
public abstract class WDXPluginAdapter implements WDXPluginInterface {

	/**
	 * 
	 */
	public WDXPluginAdapter() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract int contentGetSupportedField(final int fieldIndex,
			final StringBuffer fieldName, final StringBuffer units,
			final int maxlen);

	/**
	 * {@inheritDoc}
	 */
	public abstract int contentGetValue(final String fileName,
			final int fieldIndex, final int unitIndex,
			final FieldValue fieldValue, final int maxlen, final int flags);

	/**
	 * {@inheritDoc}
	 */
	public String contentGetDetectString(final int maxLen) {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	public void contentSetDefaultParams(final DefaultParam dps) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void contentStopGetValue(final String fileName) {
	}

	/**
	 * {@inheritDoc}
	 */
	public int contentGetDefaultSortOrder(final int fieldIndex) {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	public void contentPluginUnloading() {
	}

	/**
	 * {@inheritDoc}
	 */
	public int contentGetSupportedFieldFlags(final int fieldIndex) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int contentEditValue(final int parentWin,
			final int fieldIndex, final int unitIndex, final int fieldType,
			final FieldValue fieldValue, final int maxlen, final int flags,
			final String langidentifier) {
		return FieldValue.FT_NOSUCHFIELD;
	}

	/**
	 * {@inheritDoc}
	 */
	public void contentSendStateInformation(final int state,
			final String path) {
	}

	/**
	 * {@inheritDoc}
	 */
	public int contentSetValue(final String fileName,
			final int fieldIndex, final int unitIndex, final int fieldType,
			final FieldValue fieldValue, final int flags) {
		return FieldValue.FT_NOSUCHFIELD;
	}
}
