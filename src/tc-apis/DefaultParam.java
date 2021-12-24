package plugins;

/**
 * DefaultParamStruct is passed to inform the plugin about the current plugin
 * interface version and ini file location.
 * 
 * @author Ken
 */
public class DefaultParam {

	/**
	 * The size of the structure, in bytes. Later revisions of the plugin
	 * interface may add more structure members, and will adjust this size field
	 * accordingly.
	 */
	private int fSize;

	/**
	 * Low value of plugin interface version. This is the value after the comma,
	 * multiplied by 100! Example. For plugin interface version 1.3, the low
	 * DWORD is 30 and the high DWORD is 1.
	 */
	private long fPluginInterfaceVersionLow;

	/**
	 * High value of plugin interface version. This is the value after the
	 * comma, multiplied by 100! Example. For plugin interface version 1.3, the
	 * low DWORD is 30 and the high DWORD is 1.
	 */
	private long fPluginInterfaceVersionHi;

	/**
	 * Suggested location+name of the ini file where the plugin could store its
	 * data. This is a fully qualified path+file name, and will be in the same
	 * directory as the wincmd.ini. It's recommended to store the plugin data in
	 * this file or at least in this directory, because the plugin directory or
	 * the Windows directory may not be writable!
	 */
	private String fDefaultIniName;

	/**
	 * DefaultParam is passed to inform the plugin about the current plugin
	 * interface version and ini file location.
	 * 
	 * @param size
	 *            The size of the structure, in bytes. Later revisions of the
	 *            plugin interface may add more structure members, and will
	 *            adjust this size field accordingly.
	 * @param pluginInterfaceVersionLow
	 *            Low value of plugin interface version. This is the value after
	 *            the comma, multiplied by 100! Example. For plugin interface
	 *            version 1.3, the low DWORD is 30 and the high DWORD is 1.
	 * @param pluginInterfaceVersionHi
	 *            High value of plugin interface version.
	 * @param defaultIniName
	 *            Suggested location+name of the ini file where the plugin could
	 *            store its data. This is a fully qualified path+file name, and
	 *            will be in the same directory as the wincmd.ini. It's
	 *            recommended to store the plugin data in this file or at least
	 *            in this directory, because the plugin directory or the Windows
	 *            directory may not be writable!
	 */
	public DefaultParam(final int size, final long pluginInterfaceVersionLow,
			final long pluginInterfaceVersionHi, final String defaultIniName) {
		fSize = size;
		fPluginInterfaceVersionLow = pluginInterfaceVersionLow;
		fPluginInterfaceVersionHi = pluginInterfaceVersionHi;
		fDefaultIniName = defaultIniName;
	}

	/**
	 * This is a fully qualified path+file name, and will be in the same
	 * directory as the wincmd.ini. It's recommended to store the plugin data in
	 * this file or at least in this directory, because the plugin directory or
	 * the Windows directory may not be writable!
	 * 
	 * @return Suggested location+name of the ini file where the plugin could
	 *         store its data
	 */
	public final String getDefaultIniName() {
		return fDefaultIniName;
	}

	/**
	 * High value of plugin interface version. This is the value after the
	 * comma, multiplied by 100! Example. For plugin interface version 1.3, the
	 * low DWORD is 30 and the high DWORD is 1.
	 * 
	 * @return High value of plugin interface version
	 */
	public final long getPluginInterfaceVersionHi() {
		return fPluginInterfaceVersionHi;
	}

	/**
	 * Low value of plugin interface version. This is the value after the comma,
	 * multiplied by 100! Example. For plugin interface version 1.3, the low
	 * DWORD is 30 and the high DWORD is 1.
	 * 
	 * @return Low value of plugin interface version
	 */
	public final long getPluginInterfaceVersionLow() {
		return fPluginInterfaceVersionLow;
	}

	/**
	 * The size of the structure, in bytes. Later revisions of the plugin
	 * interface may add more structure members, and will adjust this size field
	 * accordingly.
	 * 
	 * @return The size of the structure
	 */
	public final int getSize() {
		return fSize;
	}

}
