Java Plugin Interface - history of changes
=====================================

2021-dec-18
-----------

Java plugin examples by Handel are now available at [Github](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/examples_64bit.md)
- cleaning from extra libraries not specific for each plugin, most of them are now under 1 Mb
- further testing have been done, look at the 64-bit update comment in the [table](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/examples_64bit.md)
- some of them need more rework, check the [to do](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/to-do.md)


Changes for 64-bit version (from 32-bit original java plugin by Ken Handel):
----------------------------------------------------------------------------
- included the new (wcx64 / wfx64 / wlx64 / wdx64) dll, recompiled by Ghisler (author of Total Commander), from Handel sources
- included the javalib dirs: Now every Java plugin installs just entering the zip, without further ado
- modified tc_javaplugin.ini accordingly (to refer the new location of javalib)
- included text file with some extra info for the 64-version
- repackaged as .zip (.tgz needed first to be un-tgz-ed to tar, also not so standard)




