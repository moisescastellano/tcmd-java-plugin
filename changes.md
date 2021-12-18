Java Plugin Interface - history of changes
=====================================

2021-dec-18 v2.1
----------------

Java plugin examples by Handel are now updated and available at [Github](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/examples_64bit.md)
- cleaning from extra libraries not specific for each plugin, most of them are now under 1 Mb
- further testing have been done, look at the 64-bit update comment in the [table](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/examples_64bit.md)
- some of them need more rework, check the [to do](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/to-do.md)

2021-dec-14 v2.0
----------------

Now, every java plugin:
  1. Works as an independent 64-bit plugin in TC64. Still works in TC32.
  2. May be installed as usual, just entering the zip, without further ado.

Changes for 64-bit version (from 32-bit original java plugin by Ken Handel):
----------------------------------------------------------------------------
- included the new (wcx64 / wfx64 / wlx64 / wdx64) dll, recompiled by Ghisler (author of Total Commander), from Handel sources
- included the javalib dirs: Now every Java plugin installs just entering the zip, without further ado
- modified tc_javaplugin.ini accordingly (to refer the new location of javalib)
- included text file with some extra info for the 64-version
- repackaged as .zip (.tgz needed first to be un-tgz-ed to tar, also not so standard)




