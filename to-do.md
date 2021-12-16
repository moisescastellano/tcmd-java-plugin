
Things To do
============
This is a work in progress. 
I took in charge updating the Java Plugin by Ken Handel to 64-bit version in order to develop muy own [DiskDirCrc] java plugin.

Logging
=======
I have not been able to get the logging for plugins to work. Handel configured the ZeroConfSocketHubAppender by default, that works along with Apache Chainsaw, sending logs visa sockets and showing in a GUI. I installed Chainsaw, but just the initial connections is logged. Also Chainsaw is quite deprecated, having a main page with lots of broken links, and dependencies are hard to find.

So I tried to update the commons-logging and log4j libraries and config them to log in file, but also that did not work. 

Then I updated to log4j2, it didn't work either.

I have come to the conclusion that the way the PluginClassLoader loads the jars from javalib is incompatible with Log4j's Loggers instantiation.

If you wanna give it a try, let me know if you can make it work.


Update / Reviewing libraries not needed
=======================================
As javalib is now copied in every plugin zip, it should be reviewed which of the libraries are needed for which plugins, to remove the others.


WLX - SWT based plugins
=======================
SWT based plugins (SWTDemo, OLEDemo, HelloWorld, CBM6502, BrowserExample) need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress...


Testing
=======
I have no tested every plugin in 64-bit Total Commander. 
Please feel free to do it and let me know.
If you are interested in a particular plugin and you have problems running it, also let me know.


Contact
=======
If you want to help with the things above, or you have any comment, suggestion or problem regarding this java plugin,
you contact me at:
 - email: moises.castellano (at) gmail.com
 - https://github.com/moisescastellano/tcmd-java-plugin/issues  (github for future java plugins, in progress)
Please specify the java plugin and the JRE version you are using.

