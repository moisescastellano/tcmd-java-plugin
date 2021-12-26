Additional Installation Notes:
==============================
At least Java Runtime Environment (JRE) 5.0 is required (I do not test with earlier versions)
Download it from here: http://java.sun.com/javase/downloads/index_jdk5.jsp

You must have installed additional libraries first BEFORE installing any Java plugin, if not, do it now and re-install.
These libraries are shared between all Java plugins.
To check this be sure that the sub-directory javalib in the Total Commander installation directory exist and is not empty.
This step is done only once, before the first java plugin can be installed.
If javalib is missing install it from http://www.totalcmd.net/plugring/tc_java.html

What this plugin does:
======================
This plugin is a player for sound files of the good old C64 (SID), based on JaC64 by Dreamfabrics (http://sourceforge.net/projects/jac64) .
With this plugin you can play the contents of any (not all works fine) SID/PSID sound file.

How to use this plugin:
=======================
Simply open the SID or PSID file "Turrican.sid" or "Last_Ninja_2.sid" in the lister window (F3) or in quick view (Ctrl+Q)
to show up an equalizer window and play the sound file.
To quit the demo simply press Escape and close the window (Ctrl+Q again in quick view or closing the external window)

Plugin Directory Contents:
==========================
Turrican.sid or Last_Ninja_2.sid  - example sound files to play
JSid.wlx - The plugin file you installed
errormessages.ini - multilingual error message texts of the Java Plugin Interface
license.txt - the license of the Java Plugin Interface
pluginst.inf - Total Commander plugin auto-install configuration file (needed for installation only)
Readme.txt - This file you are reading now
tc_javaplugin.ini - The configuration file of this Java plugin
tc-apis-*.jar - Java API of the Java Plugin Interface
other resources and
*.jar - The Java implementation of this plugin (containing source code, classes and additional resources)
