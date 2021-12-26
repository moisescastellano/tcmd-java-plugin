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
SNMP is network protocol for reaching informations about protocols, bandwith and other informations from network devices.
This plugin can read those informations, show them in tree structure, show statistics, device interfaces load,
also user can ping and tracert any device on network.

How to use this plugin:
=======================
Choose Network as the drive letter to be shown in Total Commander and choose "snmpplugin".
First you have to select device, then it will be availible in dir managed devices/ip addres/ then you can enter manage device file,
which will open new window with specific tabs. or you can run mib-browser, which is quick utility,
which main purpose is to get all informations quick and brief. mib browser is controlled from command-line of TC
as all other functions of my plugin.
All commands will be shown on my page, but now i will write some most usefull :
 
Mib-browser
 
mb sh - show mib browser window
mb hi - hide ...
mb cl - clear mb table
mb get x.x.x.x.x - add value correspondig to ID x.x.x.x. to mib browser table
 
other commands
 
sh device uptime
sh device load
sh device statistics
ping - ping selected device if any exists
ping x.x.x.x - ping device with IP x.x.x.x
tracert
tracert x.x.x.x - same syntax as ping
 
set timeout x - set SNMP timeout for communication in ms
set refresh-interval x - set refresh interval for showing values in ms

Plugin Directory Contents:
==========================
SNMPplugin.wfx - The plugin file you installed
errormessages.ini - multilingual error message texts of the Java Plugin Interface
license.txt - the license of the Java Plugin Interface
pluginst.inf - Total Commander plugin auto-install configuration file (needed for installation only)
Readme.txt - This file you are reading now
tc_javaplugin.ini - The configuration file of this Java plugin
tc-apis-*.jar - Java API of the Java Plugin Interface
other resources and
*.jar - The Java implementation of this plugin (containing source code, classes and additional resources)
