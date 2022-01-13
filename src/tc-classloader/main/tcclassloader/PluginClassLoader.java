package tcclassloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import moi.tcplugins.PluginLogger;

/**
 * Works around deficiencies in Sun's URLClassLoader implementation.
 * Unfortunately, the URLClassLoader doesn't like it when the original JAR file
 * changes, and reportedly on Windows it keeps the JAR file locked too. As well,
 * it seems that you can't make a URLClassLoader using URLs from Resources in a
 * previous URLClassLoader. So this ClassLoader loads the contents of the JAR(s)
 * into memory immediately and then releases the files. The classes are flushed
 * as they are used, but other files stay in memory permanently. Note that you
 * cannot acquire a class file as a resource (URL or stream).
 */

public class PluginClassLoader extends ClassLoader {

	// public static final Logger log = LoggerFactory.getLogger(PluginClassLoader.class);
	private static PluginLogger log = new PluginLogger();
	
	/**
	 * The suffix length of each class name.
	 */
	private static final int CLASSNAME_SUFFIX = ".class".length();

	/**
	 * This is the I/O buffer size.
	 */
	private static final int BUFFER_SIZE = 8*1024;

	/**
	 * A hash map with all classes of the jar files.
	 */
	private HashMap<String, byte[]> classes = new HashMap<>();

	/**
	 * A hash map with all other resources of the jar files.
	 */
	private HashMap<String, byte[]> others = new HashMap<>();
	
	private HashMap<String, URL> urls = new HashMap<>();

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
	private static Hashtable<String, PluginClassLoader> classLoaders = new Hashtable<>();

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

	private static final int[] versionNumber = {2,3,0}; // 2.3.0
	private static final String version = versionNumber[0] + "." + versionNumber[1] + "." + versionNumber[2]; 
	
	public static int[] getVersionNumber() {
		return versionNumber;
	}
	
	public static String getVersion() {
		return version;
	}
	
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
		
		if (log.isDebugEnabled()) {
			log.debug("getPlugin: libName=[" + libName + "]; className =[" + className + "]");
		}
		PluginClassLoader cl = getPluginClassLoader(libName);
		if (cl.pluginObject == null) {
			// the plugin classloader finds the plugin class
			Class<?> pluginClass = cl.findClass(className);
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

		if (log.isDebugEnabled()) {
			log.debug("getPluginClassLoader: libName=[" + libName + "]");
		}

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
			cl = classLoaders.get(libName);
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
		if (log.isDebugEnabled()) {
			log.debug("PluginClassLoader: JAVALIB="+JAVALIB);
		}
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
		if (log.isDebugEnabled()) {
			log.debug("PluginClassLoader: libName="+libName);
		}
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
		log.debug("configureFromDirectory: cwd="+cwd);
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
		
		File[] otherFiles = dir.listFiles(new FilenameFilter() {
			private String[] nonResources = new String[] {".jar",".zip",".ini",".wcx",".wcx64",".wfx",".wfx64",".wdx",".wdx64",".wlx",".wlx64"};  
			public boolean accept(final File dir, final String name) {
				for (String ext: nonResources) {
					if (name.endsWith(ext)) {
						return false;
					}
				}
				return true;
			}
		});
		for (File otherFile: otherFiles) {
			if (!otherFile.isDirectory()) {
				addOtherFile(otherFile);
			}
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
		if (log.isDebugEnabled()) {
			log.debug("getResourceAsStream: name="+name);
		}
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
	 * Get resource as url.
	 * 
	 * @param name
	 *            the resource name
	 * @return the url
	 * @see java.lang.ClassLoader#getResource(java.lang.String)
	 */
	public final URL getResource(final String name) {
		URL url = super.getResource(name);
		if (url == null) {
			url = urls.get(name);
		}
		if (log.isDebugEnabled()) {
			log.debug("getResource: name=[" + name + "];URL=[" + urls.get(name) +"]");
		}
		return url;
	}

	/**
	 * Get resources as enumeration.
	 * 
	 * @param name
	 *            the resources to find
	 * @return the enumeration of resources
	 * @throws IOException
	 *             resource is not found
	 */
	protected final Enumeration<URL> findResources(final String name) throws IOException {
		List<URL> result = new ArrayList<URL>();
		URL url = urls.get(name);
		if (url != null) {
			result.add(url);
		}
		if (log.isDebugEnabled()) {
			log.debug("findResources: name="+ name + "];URL=[" + url +"]");
		}
		return Collections.enumeration(result);
	}

	protected final URL findResource(final String name) {
		URL url = urls.get(name);
		if (log.isDebugEnabled()) {
			log.debug("findResource: name="+ name + "];URL=[" + url +"]");
		}
		return url;
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
	
    protected final Class<?> defineClass(String name, byte[] b, int len) throws ClassFormatError {        
    	int i = name.lastIndexOf('.');
        if (i != -1) {
            String pkgname = name.substring(0, i);
            if (getPackage(pkgname) == null) {
            	definePackage(pkgname, null, null, null, null, null, null, null);
            }
        }
        return defineClass(name, b, 0, len, null);
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
	public final Class<?> findClass(final String name)
			throws ClassNotFoundException {
		if (log.isDebugEnabled()) {
			log.debug("findClass: name="+name);
		}
		byte[] data = findClassData(name);
		if (data != null) {
			return defineClass(name, data, data.length);
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
		log.debug("addJar: jar="+jar.getName());
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			if (log.isDebugEnabled()) {
				log.debug("addJar: JarInputStream:" + entryName);
			}
			if (entryName.endsWith("/")) {
				// do nothing
			} else if (entry.getName().endsWith(".class")) {
				try {
					addClassFile(jar, entry);
				} catch (IOException e) {
					log.error("addJar: on addClassFile", e);
					e.printStackTrace();
				}
			} else {
				try {
					addOtherFile(jar, entry);
				} catch (IOException e) {
					log.error("addJar: on addOtherFile", e);
					e.printStackTrace();
				}
			}
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
		if (log.isDebugEnabled()) {
			byte[] cd = classes.get(name);
			if (cd == null) {
				log.debug("findClassData: name=[" + name + "] is null");
			} else {
				log.debug("findClassData: name=[" + name + "];length=" + cd.length);
			}
		}
		return classes.remove(name);

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
	private void addClassFile(final JarFile jar, final JarEntry entry) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("addClassFile: jar=["+jar.getName()+"];entry["+entry+"]");
		}
		classes.put(getClassName(entry.getName()), getFileBytes(jar, entry));
		addUrl(jar, entry);
	}
	
	private void addUrl(final JarFile jar, final JarEntry entry) throws MalformedURLException {
		String url = "jar:file:/" + jar.getName() + "!/" + entry.getName(); 
		if (log.isDebugEnabled()) {
			log.debug("addUrl: jar=["+jar.getName()+"];entry["+entry+"];url=["+url+"]");
		}
		urls.put(entry.getName(), new URL(url));
	}
	
	private void addUrl(final File file) throws MalformedURLException {
		String url = "file:/" + file.getName(); 
		if (log.isDebugEnabled()) {
			log.debug("addUrl: file=["+file+"]");
		}
		urls.put(file.getName(), new URL(url));
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
		if (log.isDebugEnabled()) {
			log.debug("addOtherFile: jar=["+jar.getName()+"];entry["+entry+"]");
		}
		others.put(entry.getName(), getFileBytes(jar, entry));
		addUrl(jar, entry);
	}

	private void addOtherFile(File file)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("addOtherFile: file=["+file+"]");
		}
		others.put(file.getName(), getFileBytes(file));
		addUrl(file);
	}

	/**
	 * get class name from file name.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the class name
	 */
	private static String getClassName(final String fileName) {
		if (log.isDebugEnabled()) {
			log.trace("getClassName: fileName=["+fileName+"]");
		}
		return fileName.substring(0, fileName.length() - CLASSNAME_SUFFIX).replace('/', '.');
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
	private static byte[] getFileBytes(final JarFile jar, final JarEntry entry) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("getFileBytes: jar=[" + jar.getName() + "];entry[" + entry + "]");
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream((int) entry.getSize());
		byte[] buf = new byte[BUFFER_SIZE];
		BufferedInputStream in = new BufferedInputStream(jar.getInputStream(entry));
		int count;
		while ((count = in.read(buf)) > -1) {
			stream.write(buf, 0, count);
		}
		in.close();
		stream.close();
		return stream.toByteArray();
	}

	private static byte[] getFileBytes(File file) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("getFileBytes: file=[" + file + "]");
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream((int)file.length());
		byte[] buf = new byte[BUFFER_SIZE];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
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
		if (log.isDebugEnabled()) {
			log.debug("findLibrary: libname=["+libname+"]");
		}
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
