Java Plugin Interface - Logging
==============================

Original plugin 1.7 included *commons-logging* and *log4j* libraries.
*commons-logging* lib itself has been deprecated, not updated since a decade. 

Since version 2.2, Apache *commons-logging* has been replaced with **SLF4J**.
From [SLF4J page](https://www.slf4j.org/):
*SLF4J serves as a simple facade or abstraction for various logging frameworks (e.g. java.util.logging, logback, log4j) allowing the end user to plug in the desired logging framework at deployment time*.

**Logging is disabled by default**, any logging implementation has been removed from the plugin itself.
For a typical user there is no difference removing Log4J (except now the .zip is some kBs smaller), as he will not be using logs.
A developer can still use Log4J or any logging library, just by including the .jar he prefers in javalib, no recompilation needed.


See below how to configure SLF4J with Log4j2

PluginClassLoader
-------------------------
The way the *PluginClassLoader* loads the jars from javalib is incompatible with commons-logging and SLF4J loggers instantiation.

Because of that, a very simple *PluginLogger* has been implemented to log the PluginClassLoader methods.
To enable logging, a dir "c:/logs/[level]" should be created, where [level] is the lowest to be logged, e.g. "c:/logs/debug"

PluginClassLoader itself was also incomplete (missing e.g.  findResources implementation).
It has been now completed for version 2.2


Configuring SLF4J with Log4j2
-------------------------
In order to use SLF4J with Log4j2:
- place the last versions of these libraries on a dir, e.g. c:/logs
  - log4j-api-2.17.0.jar 
  - log4j-core-2.17.0.jar
  - log4j-slf4j18-impl-2.17.0.jar
  - slf4j-api-1.8.0-beta4.jar

- make a **log4j2.xml** config file:

```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
  <Appenders>
    <File name="file" fileName="c:/logs/debug/javaplugin.log">
      <PatternLayout>
        <Pattern>%d %p %c{1.} [%t] %m %ex%n</Pattern>
      </PatternLayout>
    </File>
    <Console name="STDOUT" target="SYSTEM_OUT">
      <PatternLayout pattern="%m%n"/>
    </Console>
  </Appenders>
  <Loggers>
    <Root level="trace">
      <AppenderRef ref="file" level="DEBUG"/>
      <AppenderRef ref="STDOUT" level="INFO"/>
    </Root>
  </Loggers>
</Configuration>
```

- modify **tc_javaplugin.ini** *JAVA.OPTIONS* and *JAVA.CLASS.PATH*:

This is the default *tc_javaplugin.ini* for the JavaDecompiler plugin:

```
[GENERAL]
LANGUAGE=EN

[JVM]
JAVA.OPTIONS=
JAVA.TC.JAVALIB=-Dtc.java.lib=%COMMANDER_PATH%/plugins/wcx/JavaDecompiler/javalib
JAVA.CLASS.PATH=-Djava.class.path=%COMMANDER_PATH%/plugins/wcx/JavaDecompiler/javalib/tc-classloader-2.2.jar

[WCX]
CLASS=moi.tcplugins.decompiler.Decompiler
```

This is the logging capable	*tc_javaplugin.ini* config file, for the JavaDecompiler plugin:

```
[GENERAL]
LANGUAGE=EN

[JVM]
JAVA.OPTIONS=-Dlog4j.configurationFile=file:/c:/logs/log4j2.xml
JAVA.TC.JAVALIB=-Dtc.java.lib=%COMMANDER_PATH%/plugins/wcx/JavaDecompiler/javalib
JAVA.CLASS.PATH=-Djava.class.path=%COMMANDER_PATH%/plugins/wcx/JavaDecompiler/javalib/tc-classloader-2.2.jar;c:/logs/log4j-api-2.17.0.jar;c:/logs/log4j-core-2.17.0.jar;c:/logs/log4j-slf4j18-impl-2.17.0.jar;c:/logs/slf4j-api-1.8.0-beta4.jar

[WCX]
CLASS=moi.tcplugins.decompiler.Decompiler
```

