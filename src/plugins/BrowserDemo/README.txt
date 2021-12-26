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
This plugin shows the browser "internet explorer" in the lister window for HTML files (*.html).
You can navigate with the tool-bar buttons and surf the internet with other locations typed in in the address line.

How to use this plugin:
=======================
Simply open the file "demo.html" in the lister window (F3) or in quick view (Ctrl+Q) to start the browser.
You will have a toolbar to navigate your already visited pages (back/forward) you can stop the currently loading page
or reload it. The status line shows additional infos while loading a page.
To quit the demo simply press Escape and close the window (Ctrl+Q again in quick view or closing the external window)

Plugin Directory Contents:
==========================
demo.html - example HTML file for the browser
BrowserDemo.wlx - The plugin file you installed
errormessages.ini - multilingual error message texts of the Java Plugin Interface
license.txt - the license of the Java Plugin Interface
pluginst.inf - Total Commander plugin auto-install configuration file (needed for installation only)
Readme.txt - This file you are reading now
tc_javaplugin.ini - The configuration file of this Java plugin
tc-apis-*.jar - Java API of the Java Plugin Interface
other resources and
*.jar - The Java implementation of this plugin (containing source code, classes and additional resources)
