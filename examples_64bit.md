Java Plugin examples
======================

The plugin archives are installed automatically, when the .zip is entered or double clicked in Total Commander.

## [DiskDirCrc](https://moisescastellano.github.io/diskdircrc-tcplugin/)

As the original DiskDir plugin, DiskDirCrc creates a list file with all selected files and directories, including subdirs. You can then "navigate" this list with Total Commander as if it was an archive or directory containing the files.

DiskDirCrc also calculates the CRC of the files and writes them into the index file. CRC is an error-detecting code commonly used in digital storage devices to detect accidental changes to data. DiskDirCrc can then **check (Alt+Shift+F9) the integrity of files comparing the CRC in the list**.

 ![JavaDecompiler screenshot](https://github.com/moisescastellano/diskdircrc-tcplugin/raw/main/screenshots/DiskDirCrc.png)


## [JavaDecompiler](https://moisescastellano.github.io/javadecompiler-tcplugin/)

This plugin allows Total Commander to both **decompile** and **navigate** java *.class* files. It is a packer plugin, meaning you can "enter" these files as archives. 

**But... a .class file is not an archive, is it?**

It is not, .class files do NOT contain other files. However this plugin "hacks" the TC packer interface so that class files appear to be archives containing all of:
 - a file "classname.java" which you can view (F3) or copy (F5). This java is the decompiled class file 
 - a list of directories representing all the methods, fields, constructors, member classes and interfaces of the class. This way you can have a very quick view of the class structure
 - a couple more directories show the system properties and environment variables (this info is not directly related to the class, but it could be useful)
 
 In the screenshot, on the left panel we have entered to a .class file. The sub-directories contain members of the class. Decompiled class is viewed on the right panel.
 
 ![JavaDecompiler screenshot](https://github.com/moisescastellano/javadecompiler-tcplugin/raw/main/screenshots/JavaDecompiler.png)
 
The plugin uses CFR 0.152 decompiler as a library, so it does not need any extra executables or processes.

# Original examples by Ken Handel

Now, every java plugin work as an independent (no need to install javalib.tgz) 64-bit plugin in TC64\. They still work in TC32. 

See the 64-bit update comment columns. You can also check the [list of changes](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/changes.md).

All samples by Handel have been cleaned from extra libraries, repackaged and are available at he links below.

Each plugin has a README.TXT with additional notes and included source code in each *.jar file.

## Demo Plugins

| Plugin Type | Plugin Name | Version / Download | File Extensions | Description | 64-bit update comment |
| --- | --- | --- | --- | --- | --- |
| Lister plugin | Swing Demo | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/SwingDemo.zip) | *.swing | The popular JFC Applet [SwingSet2](http://java.sun.com/products/plugin/1.5.0/demos/plugin/applets.html) | 64-bit version works fine |
| Lister plugin | SWT Demo | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/SWTDemo.tgz) | *.swt | The Eclipse Demo Control Example | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress... |

## Multimedia Plugins

| Plugin Type | Plugin Name | Version / Download | File Extensions | Description | 64-bit update comment |
| --- | --- | --- | --- | --- | --- |
| Lister plugin | Audio Player | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JLGui.zip) | *.mp3, *.mpeg, *.flac, *.ape,  <br>*.mac, *.ogg, *.spx | Winamp Clone in Java [JLGui](http://sourceforge.net/projects/jlgui/) |     |
| Lister plugin | Video Player | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/VideoPlayer.zip) | *.mpg, *.mpeg, *.mov, *.avi | Java Video Player. (Deprecated: For this plugin you have to install first [Java Media Framework](http://java.sun.com/products/java-media/jmf/downloads/index.html)) | 64-bit version works fine. I don't even needed to install JMF. It shows exception when starting, but then videos are shown correctly |

## Java Development

| Plugin Type | Plugin Name | Version / Download | File Extensions | Description | 64-bit update comment |
| --- | --- | --- | --- | --- | --- |
| Lister plugin | Java Decompiler | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JADDemo.zip) | *.class, *.java | Uses [JAD](http://www.kpdus.com/jad.html) to decompile class files (supports syntax highlighting and context menu) | 64-bit version works fine. Decompiler (jad.exe) is included so you do not need to install it |

## Fans of good old C-64

| Plugin Type | Plugin Name | Version | File Extensions | Description | 64-bit update comment |
| --- | --- | --- | --- | --- | --- |
| Packer plugin | D64 disk image viewer | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/DirCBM.zip) | *.d64 | Java version of [DIRCBM](http://www.totalcmd.net/plugring/DIRCBM.html) | 64-bit works fine for unpackaging. For packaging, it throws an exception, BUT this also happens in the 32-bit version: DirCBM was ever only able to \*show\* the contents of C64 disk images |
| Lister plugin | CBM 6510 Disassembler | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/CBM6502.TGZ) | *.prg | Disassemble CBM 6510 machine code instructions | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress... |
| Lister plugin | C64 SID player | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JSid.zip) | *.sid, *.psid | Uses [JSIDPlay](http://www.jac64.com/) of the Java emulator JAC64 to play C64 sound files |     |

## Graphics

| Plugin Type | Plugin Name | Version / Download | File Extensions | Description | 64-bit update comment |
| --- | --- | --- | --- | --- | --- |
| Lister plugin | 3D graphics model viewer | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/3DDemo.zip) | *.3ds | Uses [Starfire Research](http://www.starfireresearch.com/services/java3d/inspector3ds.html)s Java 3D loader for the 3DS file format. For this plugin you have to install Java 3D | 64-bit version not tested. Java 3D is deprecated technology. |
| Lister plugin | Image Viewer | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ImageViewer.zip) | *.bmp, *.ico, *.jp(e)g, *.gif, *.png | Image viewer using Java 2D | 64-bit version works fine |

## Windows Application Integration (OLE based)

| Plugin Type | Plugin Name | Version / Download | File Extensions | Description | 64-bit update comment |
| --- | --- | --- | --- | --- | --- |
| Lister plugin | OLE Viewer | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/OLEDemo.tgz) | *.doc, *.rtf, *.xls, *.wmv, *.mpa, *.mp(e)g, *.avi, *.asf, *.wav, *.pdf | Uses Microsofts OLE interface | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress... |

## Internet Access

| Plugin Type | Plugin Name | Version / Download | File Extensions | Description | 64-bit update comment |
| --- | --- | --- | --- | --- | --- |
| Lister plugin | HTML Browser | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/BrowserDemo.tgz) | *.html | Embeds Microsoft Internet Explorer | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. However, who cares about IE? |
| File system plugin | Email reader | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/EMail.zip) |     | Check your email account, protocols pop3 and imap are supported |     |

## Networking

| Plugin Type | Plugin Name | Version | File Extensions | Description | 64-bit update comment |
| --- | --- | --- | --- | --- | --- |
| File system plugin | SNMP plugin | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/SNMPplugin.zip) |     | Analyse network using SNMP (Simple Network Monitor Protocol), plugin author: Ján Gregor |     |

## Plugin Development Samples to Learn

| Plugin Type | Plugin Name | Version / Download | File Extensions | Description | 64-bit update comment |
| --- | --- | --- | --- | --- | --- |
| Packer plugin | File lister | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JCatalogue.zip) | *.jlst | Creates a file list and browse its contents | 64-bit version works fine |
| Content plugin | Image Content | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ImageContent.zip) | *.bmp, *.ico, *.jp(e)g, *.gif, *.png | Shows image properties (width, height, bit-depth) - poor performance :-( | 64-bit version works fine. |
| Lister plugin | Hello World | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/HelloWorld.tgz) | *.tst | Hello World: show java properties in a window | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress... |
| File system plugin | Local Drives | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/Drives.zip) |     | Browse your local file systems |     |
| Content plugin | Content Demo | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ContentDemo.zip) | *.prg | Shows several test columns |  64-bit version works fine.   |
