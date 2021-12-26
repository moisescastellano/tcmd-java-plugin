package tcplugins;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Ken Resourece bundle for language dependant message texts
 */
public final class TextBundle {
	/**
	 * The message texts resource bundle name.
	 */
	private static final String BUNDLE_NAME = "tcplugins.languagetexts";

	/**
	 * The resource bundle.
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/**
	 * hidden constructor of utility class.
	 */
	private TextBundle() {
	}

	/**
	 * Get a message text of the resource bundle.
	 * 
	 * @param key
	 *            the key
	 * @return the message text
	 */
	public static String getString(final String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
