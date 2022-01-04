
Things To do
============
This is a work in progress. **Help wanted!** - in particular with Visual C++ issues. See contact below.

I took in charge updating the Java Plugin by Ken Handel to 64-bit version in order to develop muy own [DiskDirCrc](https://github.com/moisescastellano/diskdircrc-tcplugin) java plugin.

Check also the [issues page](https://github.com/moisescastellano/tcmd-java-plugin/issues).

[Plugins based on this interface](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/examples_64bit.md) have their own to-do page.

Priority issues
---------
There are now a couple (closely related) issues reported regarding this Java plugin interface, which are difficult to solve for myself as they are Visual C++ related, which I am not familiar with.

For some JDK/JRE versions the plugin has a hard time to find or load the Java Virtual Machine.

[The first one](https://github.com/moisescastellano/javadecompiler-tcplugin/issues/1) is the error "**JRE is not installed**" when finding the library, that the plugin searchs in two ways described in the issue.

[The second one](https://github.com/moisescastellano/tcmd-java-plugin/issues/2) is plugin complains "**LoadLibrary Failed / Starting Java Virtual Machine failed**". Specific JREs failing are reported in the issue.

If you are giving a try to the Java plugin interface or any of its plugins, **some working (Oracle JREs) versions** are: jre1.8.0_211, jre-8u311-windows-x64

If you are a developer and can help, the [JVM search code is performed here](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/src/vc-project/Total%20Commander%20Java%20Plugin/java.cpp)

Miscellaneous issues
---------
- If you have both TCx64 and TCx32 installed, plugins are installed under TCx64, but the javalib is searched under %COMMANDER% variable, wich TCx32 stablish to its own dir. Not a big problem, as I guess most people have just a TC, can be solved copying plugin dir including javalib.
- In the lister plugin the frame is not inside the lister/quickview window: Frame is there but not in the correct placement.
- The JadDemo plugin is much slower in TCx64, even when the JRE is the same. The only reason I can think of, is that in order to recompile the dll64, Ghisler "had to disable optimizations, it crashes with optimizations on"

Update / Reviewing libraries not needed
------------------------------------
As javalib is now copied in every plugin zip, it should be reviewed which of the libraries are needed for which plugins, to remove the others.


WLX - SWT based plugins
---------
SWT based plugins (SWTDemo, OLEDemo, HelloWorld, CBM6502, BrowserExample) need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress...


Testing
---------
I have no tested every plugin in 64-bit Total Commander. 
Please feel free to do it and let me know.
If you are interested in a particular plugin and you have problems running it, also let me know.


Contact
---------
If you want to help with the things above, or you have any comment, suggestion or problem regarding this java plugin,
you contact me at:
 - email: moises.castellano (at) gmail.com
 - https://github.com/moisescastellano/tcmd-java-plugin/issues  (github for future java plugins, in progress)
Please specify the java plugin and the JRE version you are using.

