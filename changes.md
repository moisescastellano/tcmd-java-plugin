Java Plugin Interface - history of changes
==========================================

v2.3 - 2022-jan-14
------------------

- New [Troubleshooting guide for Java plugins](https://moisescastellano.github.io/tcmd-java-plugin/troubleshooting) added
- [Javalib folder](https://github.com/moisescastellano/tcmd-java-plugin/tree/main/javalib) now hosts last version of tc-classloader-x.x.x.jar 
  - Refer to Troubleshooting guide: "In case you have more than one Java plugin installed" section
- [PluginClassLoader](https://github.com/moisescastellano/tcmd-java-plugin/tree/main/src/tc-classloader/main/tcclassloader) changes:
  - new method getVersionNumber to avoid problems when multiple java plugins are installed with different javalib versions
  - define package for classes so that getpackage call (e.g. from Tika libraries) does not fail
  - resource files (such as configuration yamls) in plugin directory are now available as resourceStream (getResourceAsStream)
- There are now 21 plugins based on the Java interface:
	- [DiskDirCrc](https://github.com/moisescastellano/diskdircrc-tcplugin)
	- [JavaDecompiler](https://github.com/moisescastellano/javadecompiler-tcplugin)
	- The [19 original examples by Ken Handel](https://moisescastellano.github.io/tcmd-java-plugin/examples_64bit)
- Issue "JRE not found" was solved and documented in main README.md
  - for more info refer to JavaDecompiler [issues page](https://github.com/moisescastellano/javadecompiler-tcplugin/issues/1)
- errormessages.ini added to (new) [resources folder](https://github.com/moisescastellano/tcmd-java-plugin/tree/main/resources)
  - corrected typos on english and german messages

v2.2 - 2021-dec-30
------------------

- Java plugin interface is now hosted at [Github pages](https://moisescastellano.github.io/tcmd-java-plugin/)
- Source code is now available at [Github](https://github.com/moisescastellano/tcmd-java-plugin)
  - src\vc-project: Visual C++ source for creating the dll (the plugin core is this dll renamed to wlx, wfx, wdx or wcx)
  - src\tc-apis: The Java plugin API source code
  - src\plugins: Java source code for each example plugin, previously sparsed in multiple .jar files
  - Frozen release-1.7 branch is intended to keep **original code by Ken Handel**, java plugin interface version 1.7.
  - More info regarding this source code / git branches / folder structure at [src/README.md file](src/README.md)
- Further testing done on the [example plugins](https://moisescastellano.github.io/tcmd-java-plugin/examples_64bit)
  - ImageContent plugin was missing swt library
- Logging is updated to SLF4J for each plugin (previously was based on deprecated Apache commons-logging implementation).
  - Logging now works for Log4j2 via SLF4J.
  - Logging is disabled by default, any logging implementation has been removed from the plugin itself.
  - Documentation about [how to configure logging for plugins](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/logging.md).
- PluginClassLoader was incomplete (missing e.g. findResources implementation). It has now been completed

v2.1 - 2021-dec-18
------------------

Java plugin examples by Handel are now updated and available at [Github](https://moisescastellano.github.io/tcmd-java-plugin/examples_64bit)
- cleaning from extra libraries not specific for each plugin, most of them are now under 1 Mb
- further testing have been done, look at the 64-bit update comment in the [table](https://moisescastellano.github.io/tcmd-java-plugin/examples_64bit)
- some of them need more rework, check the [to do](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/to-do.md)

v2.0 - 2021-dec-14
------------------

Now, every java plugin:
- Works as an independent 64-bit plugin in TC64. Still works in TC32.
- May be installed as usual, just entering the zip, without further ado.

### Changes for 64-bit version plugins (from 32-bit original java plugin by Ken Handel):
- included the new (wcx64 / wfx64 / wlx64 / wdx64) dll, recompiled by Ghisler (author of Total Commander), from Handel sources
- included the javalib dirs: Now every Java plugin installs just entering the zip, without further ado
- modified tc_javaplugin.ini accordingly (to refer the new location of javalib)
- included text file with some extra info for the 64-version
- repackaged as .zip (.tgz needed first to be un-tgz-ed to tar, also not so standard)

Why do version numbers begin at v2.0?
--------------------------------

The [Java Plugin Interface 1.x](http://totalcmd.net/plugring/tc_java.html) is (C) 2006-2007 Ken Handel. However the developer abandoned the project in 2007, and he has been unreachable since then (some people tried to reach him long ago). 64-bit versions of this plugin were no available, because as said the project was abandoned in 2007 and 64-bit tcmd came around 2011. That makes it unusable for most people, nowadays using 64-bit TC.

As the license allows so, we have undertaken the project.

I numbered first 64-bit version as 2.0 as it would be confusing to start with 1.0, since Ken Handel's latest version was 1.7
