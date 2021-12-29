Java Plugin Interface - history of changes
=====================================

2022-jan-xx v2.3
----------------
- PluginClassLoader changes:
  - define package for classes so that getpackage does not fail
  - help and properties files in plugin directory are available as resourceStream

2021-dec-xx v2.2
----------------

- Java plugin interface is now hosted at [Github pages](https://moisescastellano.github.io/tcmd-java-plugin/)
- Source code is now available at [Github](https://github.com/moisescastellano/tcmd-java-plugin)
  - src\vc-project: Visual C++ source for creating the dll (the plugin core is this dll renamed to wlx, wfx, wdx or wcx)
  - src\tc-apis: The Java plugin API source code
  - src\plugins: Java source code for each example plugin, previously sparsed in multiple .jar files
  - Frozen release-1.7 branch is intended to keep **original code by Ken Handel**, java plugin interface version 1.7.
  - More info regarding this source code / git branches / folder structure information at [src readme file](src/README.md)
- Further testing done on the [example plugins](https://moisescastellano.github.io/tcmd-java-plugin/examples_64bit)
  - ImageContent plugin was missing swt library
- Logging is updated to SLF4J for each plugin. Previously was based on deprecated Apache commons-logging implementation.
  - Logging now works for Log4j via SLF4J.
  - Documentation about [logging configuration](https://github.com/moisescastellano/tcmd-java-plugin/logging.md)
- PluginClassLoader was incomplete (missing e.g. findResources implementation). It has now been completed

2021-dec-18 v2.1
----------------

Java plugin examples by Handel are now updated and available at [Github](https://moisescastellano.github.io/tcmd-java-plugin/examples_64bit)
- cleaning from extra libraries not specific for each plugin, most of them are now under 1 Mb
- further testing have been done, look at the 64-bit update comment in the [table](https://moisescastellano.github.io/tcmd-java-plugin/examples_64bit)
- some of them need more rework, check the [to do](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/to-do.md)

2021-dec-14 v2.0
----------------

Now, every java plugin:
- Works as an independent 64-bit plugin in TC64. Still works in TC32.
- May be installed as usual, just entering the zip, without further ado.

### Changes for 64-bit version plugins (from 32-bit original java plugin by Ken Handel):
- included the new (wcx64 / wfx64 / wlx64 / wdx64) dll, recompiled by Ghisler (author of Total Commander), from Handel sources
- included the javalib dirs: Now every Java plugin installs just entering the zip, without further ado
- modified tc_javaplugin.ini accordingly (to refer the new location of javalib)
- included text file with some extra info for the 64-version
- repackaged as .zip (.tgz needed first to be un-tgz-ed to tar, also not so standard)




