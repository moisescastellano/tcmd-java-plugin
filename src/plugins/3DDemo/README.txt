Additional Installation Notes:
==============================
At least Java Runtime Environment (JRE) 5.0 is required (I do not test with earlier versions)
Download it from here: http://java.sun.com/javase/downloads/index_jdk5.jsp

Also Java 3D must be installed to this Java Runtime Environment
To keep things simple the installer is available in the plugin directory.
Please install java3d-1_5_0-windows-i586.exe

You must have installed additional libraries first BEFORE installing any Java plugin, if not, do it now and re-install.
These libraries are shared between all Java plugins.
To check this be sure that the sub-directory javalib in the Total Commander installation directory exist and is not empty.
This step is done only once, before the first java plugin can be installed.
If javalib is missing install it from http://www.totalcmd.net/plugring/tc_java.html

What this plugin does:
======================
This plugin shows 3D models created by "Autodesk 3ds Max" (http://www.autodesk.de/3dsmax) (*.3ds).
In the quick view, the slider is badly invisible, so open with F3.

How to use this plugin:
=======================
Simply open the files "demoX.3ds" in the lister window (F3) to show the 3D model.
You can rotate the model by holding the left mouse button pushed and dragging over the picture.
You can move the model by holding the right mouse button pushed and dragging over the picture.
You can zoom the picture in and out, by pulling the slider at the right hand of the picture up or down.
To quit the demo simply press Escape and close the window (Ctrl+Q again in quick view or closing the external window)

Plugin Directory Contents:
==========================
demoX.3ds - example HTML file for the browser
3DDemo.wlx - The plugin file you installed
errormessages.ini - multilingual error message texts of the Java Plugin Interface
license.txt - the license of the Java Plugin Interface
pluginst.inf - Total Commander plugin auto-install configuration file (needed for installation only)
Readme.txt - This file you are reading now
tc_javaplugin.ini - The configuration file of this Java plugin
tc-apis-*.jar - Java API of the Java Plugin Interface
other resources and
StarfireExt.jar - downloaded from http://www.starfireresearch.com/services/java3d/inspector3ds.html
                  (if you find like it, please support them)
*.jar - The Java implementation of this plugin (containing source code, classes and additional resources)
