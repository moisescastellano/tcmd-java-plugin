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
This plugin shows example values as additional columns in the Total Commander window.
Supported file extension is PRG (this is only a demonstration)
The additional columns shows all supported column types (int32, int64, float, boolean, date, time, datetime,
string, multiple choice and fulltext)

How to use this plugin:
=======================
Right click on the column header of the Total Commander main window and choose "configure custom columns".
Click Button New, type in a new name "test" and choose "add columns".
Press "+"-sign and choose "contentdemo" then choose the information you want to see.
Press button OK twice to return to the Total Commander main window.

Plugin Directory Contents:
==========================
demo.prg - example file
ContentDemo.wdx - The plugin file you installed
errormessages.ini - multilingual error message texts of the Java Plugin Interface
license.txt - the license of the Java Plugin Interface
pluginst.inf - Total Commander plugin auto-install configuration file (needed for installation only)
Readme.txt - This file you are reading now
tc_javaplugin.ini - The configuration file of this Java plugin
tc-apis-*.jar - Java API of the Java Plugin Interface
other resources and
*.jar - The Java implementation of this plugin (containing source code, classes and additional resources)
