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
This plugin is a player for sound files (MP3, MPEG, FLAC, APE, MAC, OGG, SPX), based on JLGui by JavaZoom (http://www.javazoom.net/jlgui/jlgui.html) .
With this plugin you can play the contents of sound files.

How to use this plugin:
=======================
Simply open the audio file (e.g. "demo.*") in the lister window (F3) or in quick view (Ctrl+Q)
to play the sound file.
To quit the demo simply press Escape and close the window (Ctrl+Q again in quick view or closing the external window)

Plugin Directory Contents:
==========================
demo.* - example sound files to play
JLGui.wlx - The plugin file you installed
errormessages.ini - multilingual error message texts of the Java Plugin Interface
license.txt - the license of the Java Plugin Interface
pluginst.inf - Total Commander plugin auto-install configuration file (needed for installation only)
Readme.txt - This file you are reading now
tc_javaplugin.ini - The configuration file of this Java plugin
tc-apis-*.jar - Java API of the Java Plugin Interface
other resources and
*.jar - The Java implementation of this plugin (containing source code, classes and additional resources)
