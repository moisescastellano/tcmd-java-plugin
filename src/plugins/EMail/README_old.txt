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
This plugin shows your email messages of your email account (pop3/imap) as
a file system. Each file represents an email message.

How to use this plugin:
=======================
Choose Network as the drive letter to be shown in Total Commander and choose "tcplugins.EMail".
A dialoge appears to enter the connection string for your email account, for example:
pop3://<user>:<password>@pop.mail.yahoo.de
If all entered correctly, the connection will be established and the emails are downloaded. You can cancel the operation
and view the messages get so far (newest message is loaded first).

Plugin Directory Contents:
==========================
EMail.wfx - The plugin file you installed
errormessages.ini - multilingual error message texts of the Java Plugin Interface
license.txt - the license of the Java Plugin Interface
pluginst.inf - Total Commander plugin auto-install configuration file (needed for installation only)
Readme.txt - This file you are reading now
tc_javaplugin.ini - The configuration file of this Java plugin
tc-apis-*.jar - Java API of the Java Plugin Interface
other resources and
*.jar - The Java implementation of this plugin (containing source code, classes and additional resources)
