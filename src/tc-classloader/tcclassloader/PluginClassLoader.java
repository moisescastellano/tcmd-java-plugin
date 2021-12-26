package tcclassloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * Works around deficencies in Sun's URLClassLoader implementation.
 * Unfortunately, the URLClassLoader doesn't like it when the original JAR file
 * changes, and reportedly on Windows it keeps the JAR file locked too. As well,
 * it seems that you can't make a URLClassLoader using URLs from Resources in a
 * previous URLClassLoader. So this ClassLoader loads the contents of the JAR(s)
 * into memory immediately and then releases the files. The classes are flushed
 * as they are used, but other files stay in memory permanently. Note that you
 * cannot acquire a class file as a resource (URL or stream).
 */

public class PluginClassLoader extends ClassLoader {

	/**
	 * The suffix length of each class name.
	 */
	private static final int CLASSNAME_SUFFIX = 6;

	/**
	 * This is the I/O buffer size.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * A hash map with all classes of the jar files.
	 */
	private HashMap classes = new HashMap();

	/**
	 * A hash map with all other resources of the jar files.
	 */
	private HashMap others = new HashMap();

	/**
	 * The plugin directory.
	 */
	private String pluginDirectory;

	/**
	 * The plugin library path name.
	 */
	private String fLibName;

	/**
	 * For native access: the current classloader instance (key=the plugin
	 * library).
	 */
	private static Hashtable classLoaders = new Hashtable();

	/**
	 * The Java plugin loaded by this plugin classloader.
	 */
	private Object pluginObject;

	/**
	 * The global JARs directory.
	 */
	private static final String JAVALIB = System.getProperty("tc.java.lib");

	/**
	 * Root ClassLoader with global JARs for all plugins.
	 */
	private static final PluginClassLoader ROOT_CL = new PluginClassLoader();

	/**
	 * Called by native code: Get the Java plugin instance of the Total
	 * Commander plugin library.
	 * 
	 * @param libName
	 *            he plugin library name
	 * @param className
	 *            the plugin class name
	 * @return the plugin instance
	 * @throws ClassNotFoundException
	 *             class could not be found
	 * @throws IOException
	 *             the plugin library could not be found
	 * @throws InstantiationException
	 *             the Java plugin could not be instanciated
	 * @throws IllegalAccessException
	 *             the Java plugin constructor is not accessible
	 */
	public static Object getPlugin(final String libName, final String className)
			throws ClassNotFoundException, IOException, InstantiationException,
			IllegalAccessException {
		PluginClassLoader cl = getPluginClassLoader(libName);
		if (cl.pluginObject == null) {
			// the plugin classloader finds the plugin class
			Class pluginClass = cl.findClass(className);
			// create a new instance of the plugin and store it in the plugin
			// classloader instance
			cl.pluginObject = pluginClass.newInstance();
		}
		// return the Java plugin instance of the plugin classloader
		return cl.pluginObject;
	}

	/**
	 * Called by native code: Get the Java plugin classloader of the Total
	 * Commander plugin library.
	 * 
	 * @param libName
	 *            he plugin library name
	 * @return the plugin classloader instance
	 * @throws IOException
	 *             the plugin library could not be found
	 */
	public static PluginClassLoader getPluginClassLoader(final String libName)
			throws IOException {
		PluginClassLoader cl;
		// get the plugin classloader associated to the plugin library
		if (classLoaders.get(libName) == null) {
			// create a new plugin classloader
			cl = new PluginClassLoader(libName);
			// create an association to the plugin library
			classLoaders.put(libName, cl);
		} else {
			// get the already created plugin classloader associated to the
			// plugin library
			cl = (PluginClassLoader) classLoaders.get(libName);
		}
		// return the Java plugin classloader
		return cl;
	}

	/**
	 * Create a ROOT ClassLoader for all Plugins in TC javalib.
	 * 
	 */
	private PluginClassLoader() {
		super(PluginClassLoader.class.getClassLoader());
		try {
			this.pluginDirectory = JAVALIB;
			configureFromDirectory(JAVALIB);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a new ClassLoader.
	 * 
	 * @param cwd
	 *            the current working directory with all JAR files.
	 * @param libName
	 *            the name of the TC library
	 * @throws IOException
	 *             I/O exception
	 */
	public PluginClassLoader(final String libName) throws IOException {
		super(ROOT_CL);
		File tcLib = new File(libName);
		String cwd = tcLib.getParentFile().getAbsolutePath();
		this.pluginDirectory = cwd;
		this.fLibName = tcLib.getName();
		configureFromDirectory(cwd);
	}

	/**
	 * Configure this ClassLoader with JARs from the current working directory.
	 * 
	 * @param cwd
	 *            the current working directory with all jar files and libraries
	 * @throws IOException
	 *             I/O exception
	 */
	private void configureFromDirectory(final String cwd) throws IOException {
		File dir = new File(cwd);
		String[] files = dir.list(new FilenameFilter() {
			/**
			 * {@inheritDoc}
			 */
			public boolean accept(final File dir, final String name) {
				return name.endsWith(".jar");
			}

		});
		for (int i = 0; i < files.length; i++) {
			JarFile file = new JarFile(new File(cwd, files[i]));
			addJar(file);
		}
	}

	/**
	 * Get a resource in the jar files as an input stream.
	 * 
	 * @param name
	 *            the name of the resource
	 * @return the input stream of the resource or null
	 * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
	 */
	public final InputStream getResourceAsStream(final String name) {
		InputStream stream = getParent().getResourceAsStream(name);
		if (stream == null) {
			byte[] buf = (byte[]) others.get(name);
			if (buf != null) {
				stream = new ByteArrayInputStream(buf);
			}
		}
		return stream;
	}

	/**
	 * Get resource as url. Not yet implemented.
	 * 
	 * @param name
	 *            the resource name
	 * @return the url
	 * @see java.lang.ClassLoader#getResource(java.lang.String)
	 */
	public final URL getResource(final String name) {
		try {
			return new File(this.pluginDirectory
					+ System.getProperty("file.separator") + name).toURL();
		} catch (MalformedURLException e) {
			throw new Error("Illegal URL for file=" + this.pluginDirectory
					+ System.getProperty("file.separator") + name);
		}
	}

	/**
	 * Get resources as enumeration. Not yet implemented.
	 * 
	 * @param name
	 *            the resources to find
	 * @return the enumeration of resources
	 * @throws IOException
	 *             resource is not found
	 */
	protected final Enumeration findResources(final String name)
			throws IOException {
		throw new Error("Not Yet Implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean equals(final Object o) {
		if (o instanceof PluginClassLoader) {
			return ((PluginClassLoader) o).getParent() == getParent();
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int hashCode() {
		return getParent().hashCode();
	}

	/**
	 * Find class in jar files.
	 * 
	 * @param name
	 *            the class name to find
	 * @return the found class
	 * @exception ClassNotFoundException
	 *                the class could not be found
	 */
	public final Class findClass(final String name)
			throws ClassNotFoundException {
		byte[] data = findClassData(name);
		if (data != null) {
			return defineClass(name, data, 0, data.length);
		} else {
			throw new ClassNotFoundException(name);
		}
	}

	/**
	 * Adds a new JAR to this ClassLoader. This may be called at any time.
	 * 
	 * @param jar
	 *            the jar file to add
	 */
	public final void addJar(final JarFile jar) {
		Enumeration entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			if (entry.getName().endsWith(".class")) {
				try {
					addClassFile(jar, entry);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					addOtherFile(jar, entry);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Adds a new JAR to this ClassLoader. This may be called at any time.
	 * 
	 * @param stream
	 *            the jar input stream to add
	 */
	public final void addJar(final JarInputStream stream) {
		byte[] buf = new byte[BUFFER_SIZE];
		int count;
		try {
			while (true) {
				JarEntry entry = stream.getNextJarEntry();
				if (entry == null) {
					break;
				}
				String name = entry.getName();
				int size = (int) entry.getSize();
				ByteArrayOutputStream out;
				if (size >= 0) {
					out = new ByteArrayOutputStream(size);
				} else {
					out = new ByteArrayOutputStream(BUFFER_SIZE);
				}
				while ((count = stream.read(buf)) > -1) {
					out.write(buf, 0, count);
				}
				out.close();
				if (name.endsWith(".class")) {
					classes.put(getClassName(name), out.toByteArray());
				} else {
					others.put(name, out.toByteArray());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * find class data for class name.
	 * 
	 * @param name
	 *            the class name
	 * @return the byte array with class data
	 */
	private byte[] findClassData(final String name) {

		return (byte[]) classes.remove(name);

	}

	/**
	 * Add class file to classes.
	 * 
	 * @param jar
	 *            the jar file
	 * @param entry
	 *            the jar file entry
	 * @throws IOException
	 *             I/O error
	 */
	private void addClassFile(final JarFile jar, final JarEntry entry)
			throws IOException {
		classes.put(getClassName(entry.getName()), getFileBytes(jar, entry));
	}

	/**
	 * Add other resource file to classes.
	 * 
	 * @param jar
	 *            the jar file
	 * @param entry
	 *            the jar file entry
	 * @throws IOException
	 *             I/O error
	 */
	private void addOtherFile(final JarFile jar, final JarEntry entry)
			throws IOException {
		others.put(entry.getName(), getFileBytes(jar, entry));
	}

	/**
	 * get class name from file name.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the class name
	 */
	private static String getClassName(final String fileName) {
		return fileName.substring(0, fileName.length() - CLASSNAME_SUFFIX)
				.replace('/', '.');
	}

	/**
	 * Get file bytes for class.
	 * 
	 * @param jar
	 *            the jar file
	 * @param entry
	 *            the jar file entry
	 * @return the byte array with class
	 * @throws IOException
	 *             I/O error
	 */
	private static byte[] getFileBytes(final JarFile jar, final JarEntry entry)
			throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream((int) entry
				.getSize());
		byte[] buf = new byte[BUFFER_SIZE];
		BufferedInputStream in = new BufferedInputStream(jar
				.getInputStream(entry));
		int count;
		while ((count = in.read(buf)) > -1) {
			stream.write(buf, 0, count);
		}
		in.close();
		stream.close();
		return stream.toByteArray();
	}

	/**
	 * {@inheritDoc}
	 */
	protected final String findLibrary(final String libname) {
		if (libname.startsWith("tc.library.name")) {
			String absolutePathName = this.pluginDirectory
					+ System.getProperty("file.separator") + fLibName;
			return absolutePathName;
		} else {
			String absolutePathName = this.pluginDirectory
					+ System.getProperty("file.separator")
					+ System.mapLibraryName(libname);
			return absolutePathName;
		}
	}

}
