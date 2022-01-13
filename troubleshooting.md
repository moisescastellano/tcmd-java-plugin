# Troubleshooting Java Plugins guide

This interface and all derived plugins are written in Java, so you need to have installed a [Java Runtime Environment (JRE)](https://www.java.com/en/download/manual.jsp). The Java plugin interface and derived plugins were tested on **Oracle (Sun) JRE 1.8**  (jre-8u311-windows-x64.exe).

Let me know whether this guide solves your problem or not - contact details below

Table of Contents
-----------------

- [In case you have more than one Java plugin installed](#In-case-you-have-more-than-one-Java-plugin-installed)
- [Error *Java Runtime Environment is not installed on this Computer*](#Error-Java-Runtime-Environment-is-not-installed-on-this-Computer)
- [Error *LoadLibrary Failed*](#Error-LoadLibrary-Failed)
- [Error *Starting Java Virtual Machine failed*](#Error-Starting-Java-Virtual-Machine-failed)
- [Error *Class not found class='tcclassloader/PluginClassLoader'*](#Error-Class-not-found-class-tcclassloader/PluginClassLoader)
- [Error *Initialization failed in class...*](#Error-Initialization-failed-in-class)
- [Error *Exception in class 'tcclassloader/PluginClassLoader'*](#Error-Exception-in-class-tcclassloader/PluginClassLoader)
- [Issues and things to-do](#Issues-and-things-to-do)
- [Contact](#Contact)

In case you have more than one Java plugin installed
------------------------------------------------------

In order to save CPU and memory resources, **every Java plugin is executed in the same JVM**. Besides resources being saved, plugin execution is much faster. 

The drawback of this approach is that **configuration, such as libraries and properties, is shared between all the Java plugins**. The configuration of the first loaded plugin, specified in *tc_javaplugin.ini*, is used.

In particular, be sure that the JAVA.CLASS.PATH variable in the JVM section of tc_javaplugin.ini points to a javalib directory with the last version of tc-classsloader-x.y.z.jar


Error *Java Runtime Environment is not installed on this Computer*
----------------------------------------------------

[Issue for this error has been closed](https://github.com/moisescastellano/javadecompiler-tcplugin/issues/1)

### Short version of solution:

Add these 2 properties to the [JVM] section in the _**[TOTALCMD-INSTALL-DIR]/wcx/JavaDecompiler/tc_javaplugin.ini**_  file, changing the paths to your JRE install dir:
```
JVM_DLL=c:\Program Files\Java\jre1.8.0_311\bin\server\jvm.dll
JVM_HOME=c:\Program Files\Java\jre1.8.0_311
```

### Long version of solution:

The Java plugin interface searchs for the JRE in two ways:

1. In the Windows Registry, it searchs for the key **HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Runtime Environment**

	there it queries for "_JavaHome_" and "_RuntimeLib_", e.g.	my computer registry values:

		JavaHome		C:\Program Files\Java\jre1.8.0_311
		RuntimeLib		C:\Program Files\Java\jre1.8.0_311\bin\server\jvm.dll
2. If those values do not exist, it searches for the config file:	**_[PLUGIN_DIR]/tc_javaplugin.ini_**

	in that file, in the [JVM] section, it searches for two properties:

		JVM_DLL=c:\Program Files\Java\jre1.8.0_311\bin\server\jvm.dll
		JVM_HOME=c:\Program Files\Java\jre1.8.0_311

Oracle's (Sun) JDK and JRE installlables are executables that set those registry values. If your JRE installable was just a zip file and it did not set those registry values, this is an example of how _**tc_javaplugin.ini**_ can be configured:

```
[GENERAL]
LANGUAGE=EN

[JVM]
JAVA.OPTIONS=
JAVA.TC.JAVALIB=-Dtc.java.lib=%COMMANDER_PATH%/plugins/wcx/JavaDecompiler/javalib
JAVA.CLASS.PATH=-Djava.class.path=%COMMANDER_PATH%/plugins/wcx/JavaDecompiler/javalib/tc-classloader-2.2.jar
JVM_DLL=c:\Program Files\Java\jre1.8.0_311\bin\server\jvm.dll
JVM_HOME=c:\Program Files\Java\jre1.8.0_311

[WCX]
CLASS=moi.tcplugins.decompiler.Decompiler

```

If you get a _**Java Runtime Environment is not installed on this Computer**_ error, and you have it installed, check the [response to this issue](https://github.com/moisescastellano/javadecompiler-tcplugin/issues/1). In short:
add these 2 properties to the [JVM] section in the _tc_javaplugin.ini_  file, changing the paths to your JRE install dir:
```
JVM_DLL=c:\Program Files\Java\jre1.8.0_311\bin\server\jvm.dll
JVM_HOME=c:\Program Files\Java\jre1.8.0_311
```

Error *LoadLibrary Failed*
--------------------------------

There is an [open issue for this error](https://github.com/moisescastellano/tcmd-java-plugin/issues/2)

**First be sure you use the same (32/64) platform for JVM and TC**. This is, if you still use TC32 bits, the JVM should be a 32 bits version, and if you use TCx64, JVM should be a x64 bits version.
For checking which JVM/JRE is the Java plugin using, refer to the [JRE section](#Java-Runtime-Environment-is-not-installed-on-this-Computer)

In case that fails, continue reading.

Java plugin interface and derived plugins like [JavaDecompiler](https://moisescastellano.github.io/javadecompiler-tcplugin/) and [DiskDirCrc](https://moisescastellano.github.io/diskdircrc-tcplugin/) have been tested on **Oracle (Sun) JRE 1.8**  (jre-8u311-windows-x64.exe). Other Oracle versions should also work fine.

However **OpenJDK versions have been reported to fail** with 2 types of error when the plugin tries to start the JVM:

 - _Starting the Java Virtual Machine failed. status =-6_
 - _LoadLibrary Failed path='...' Please correct INI file and '...' Restart Total Commander._

Some development has to be done to get OpenJDKs to work (help wanted!).

**By now, recommendation is to install an Oracle (Sun) JDK/JRE version.** 

Let me know if you have this issue, and what are your JDK/JRE and TCMD versions

### JREs reported to work fine with the Java plugin interface:
 - jre1.8.0_211
 - jre-8u311-windows-x64.exe 
 
### JREs reported to give _LoadLibrary Failed_ error:
  - openjdk-8u41-b04-windows-i586-14_jan_2020.zip   (32 bit)  
  - jre-8u112-windows-x64.exe (in spite being a JRE Oracle version)

Error *Starting Java Virtual Machine failed*
--------------------------------

There is an [open issue for this error](https://github.com/moisescastellano/tcmd-java-plugin/issues/2)

Java plugin interface and derived plugins like [JavaDecompiler](https://moisescastellano.github.io/javadecompiler-tcplugin/) and [DiskDirCrc](https://moisescastellano.github.io/diskdircrc-tcplugin/) have been tested on **Oracle (Sun) JRE 1.8**  (jre-8u311-windows-x64.exe). Other Oracle versions should also work fine.

### JREs reported to give _Starting the Java Virtual Machine failed. status =-6_ error:
  - (OpenJDK) java-se-9-ri (64-bit)
  - openjdk-17+35_windows-x64_bin.zip  (64-bit)

**OpenJDK versions have been reported to fail**,
**By now, recommendation is to install an Oracle (Sun) JDK/JRE version.** 

Error *Class not found class='tcclassloader/PluginClassLoader'*
-------------------------------------

Be sure that the JAVA.CLASS.PATH variable in the JVM section of tc_javaplugin.ini points to a javalib directory with the last version of tc-classsloader-x.y.z.jar

If you have more java plugins installed, refer to [that section](#In-case-you-have-more-than-one-Java-plugin-installed).

Error *Initialization failed in class...'*
-------------------------------------

Be sure that the JAVA.CLASS.PATH variable in the JVM section of tc_javaplugin.ini points to a javalib directory with the last version of tc-classsloader-x.y.z.jar

If you have more java plugins installed, refer to [that section](#In-case-you-have-more-than-one-Java-plugin-installed).

Error *Exception in class 'tcclassloader/PluginClassLoader'*
------------------------------------------------------
Most probably another java plugin with and older version of PluginClassLoader has been loaded first.

Refer to [In-case-you-have-more-than-one-Java-plugin-installed](#In-case-you-have-more-than-one-Java-plugin-installed) section.


Issues and things to-do
----------------------
This is a work in progress. **Help wanted!** - in particular with Visual C++ issues.
 - Refer to [things to do](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/to-do.md) for work in progress.
 - Check also the [issues page](https://github.com/moisescastellano/tcmd-java-plugin/issues).
 - [Plugins based on this interface](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/examples_64bit.md) have their own to-do page:
   - [JavaDecompiler issues page] https://github.com/moisescastellano/javadecompiler-tcplugin/issues
   - [DiskDirCrc issues page] https://github.com/moisescastellano/diskdircrc-tcplugin

Contact
--------------------
Let me know if you have any comment, suggestion or problem regarding this java plugin, 
choose the most appropiate way to contact me:
 - [this thread in the Total Commander forum](https://www.ghisler.ch/board/viewtopic.php?t=75726)
 - email: moises.castellano (at) gmail.com
 - [Github Java plugin interface project's issues page](https://github.com/moisescastellano/tcmd-java-plugin/issues)

Please detail the specific version of: Java plugin interface, Total Commander and JRE that you are using.

