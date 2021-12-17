# Java Plugins

The plugin archives are installed automatically, when the .zip is entered or double clicked in Total Commander. Each plugin has a README.TXT with additional notes and included source code in each *.jar file.

# DiskDirCrc

[DiskDirCrc](https://github.com/moisescastellano/diskdircrc-tcplugin) is the first java plugin of my own.

As the original DiskDir plugin, DiskDirCrc creates a list file with all selected files and directories, including subdirs. You can then "navigate" this list with Total Commander as if it was an archive or directory containing the files.

DiskDirCrc also calculates the CRC of the files and writes them into the index file. CRC is an error-detecting code commonly used in digital storage devices to detect accidental changes to data. DiskDirCrc can then check (Alt+Shift+F9) the integrity of files comparing the CRC in the list.

# Original examples by Ken Handel

Now, every java plugin work as an independent (no need to install javalib.tgz) 64-bit plugin in TC64\. They still work in TC32. See the 64-bit update comment (2021) All samples by Handel have been cleaned from extra libraries, repackaged and are available at he links below.

| Plugin Type | Plugin Download | Version | File Extensions | Description | 64-bit update comment (2021) |
| --- | --- | --- | --- | --- | --- |
| **Demo Plugins**
| Lister plugin | Swing Demo | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/SwingDemo.zip.zip) | *.swing | The popular JFC Applet [SwingSet2](http://java.sun.com/products/plugin/1.5.0/demos/plugin/applets.html) | 64-bit version works fine |
| Lister plugin | SWT Demo | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/SWTDemo.tgz) | *.swt | The Eclipse Demo Control Example | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress... |
| **Multimedia Plugins**
| Lister plugin | Audio Player | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JLGui.zip) | *.mp3, *.mpeg, *.flac, *.ape,  <br>*.mac, *.ogg, *.spx | Winamp Clone in Java [JLGui](http://sourceforge.net/projects/jlgui/) |     |
| Lister plugin | Video Player | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/%20VideoPlayer.zip) | *.mpg, *.mpeg, *.mov, *.avi | Java Video Player. (Deprecated: For this plugin you have to install first [Java Media Framework](http://java.sun.com/products/java-media/jmf/downloads/index.html)) | 64-bit version works fine. I don't even needed to install JMF. It shows exception when starting, but then videos are shown correctly |
| ** Java Developement**
| Lister plugin | Java Decompiler | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JADDemo.zip) | *.class, *.java | Uses [JAD](http://www.kpdus.com/jad.html) to decompile class files (supports syntax highlighting and context menu) | 64-bit version works fine. Decompiler (jad.exe) is included so you do not need to install it |
| **Fans of good old C-64
| Packer plugin | D64 disk image viewer | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/DirCBM.zip) | *.d64 | Java version of [DIRCBM](http://www.totalcmd.net/plugring/DIRCBM.html) | 64-bit works fine for unpackaging. For packaging, it throws an exception, BUT this also happens in the 32-bit version: DirCBM was ever only able to \*show\* the contents of C64 disk images |
| Lister plugin | CBM 6510 Disassembler | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/CBM6502.TGZ) | *.prg | Disassemble CBM 6510 machine code instructions | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress... |
| Lister plugin | C64 SID player | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JSid.zip) | *.sid, *.psid | Uses [JSIDPlay](http://www.jac64.com/) of the Java emulator JAC64 to play C64 sound files |     |
| **Graphics**
| Lister plugin | 3D graphics model viewer | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/3DDemo.zip) | *.3ds | Uses [Starfire Research](http://www.starfireresearch.com/services/java3d/inspector3ds.html)s Java 3D loader for the 3DS file format. For this plugin you have to install Java 3D | 64-bit version not tested. Java 3D is deprecated technology. |
| Lister plugin | Image Viewer | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ImageViewer.zip) | *.bmp, *.ico, *.jp(e)g, *.gif, *.png | Image viewer using Java 2D | 64-bit version works fine |
| **Windows Application Integration (OLE based)**
| Lister plugin | OLE Viewer | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/OLEDemo.tgz) | *.doc, *.rtf, *.xls, *.wmv, *.mpa, *.mp(e)g, *.avi, *.asf, *.wav, *.pdf | Uses Microsofts OLE interface | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress... |
| **Internet Access**
| Lister plugin | HTML Browser | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/BrowserDemo.tgz) | *.html | Embeds Microsoft Internet Explorer | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. However, who cares about IE? |
| File system plugin | Email reader | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/EMail.zip) |     | Check your email account, protocols pop3 and imap are supported |     |
| **Networking**
| File system plugin | SNMP plugin | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/SNMPplugin.zip) |     | Analyse network using SNMP (Simple Network Monitor Protocol), plugin author: Ján Gregor |     |
| **Plugin Developement Samples to Learn
| Packer plugin | File lister | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JCatalogue.zip) | *.jlst | Creates a file list and browse its contents | 64-bit version works fine |
| Content plugin | Image Content | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ImageContent.zip) | *.bmp, *.ico, *.jp(e)g, *.gif, *.png | Shows image properties (width, height, bit-depth) - poor performance :-( |     |
| Lister plugin | Hello World | [1.7 (32 bits)](http://java.totalcmd.net/V1.7/HelloWorld.tgz) | *.tst | Hello World: show java properties in a window | SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress... |
| File system plugin | Local Drives | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/Drives.zip) |     | Browse your local file systems |     |
| Content plugin | Content Demo | [2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ContentDemo.zip) | *.prg | Shows several test columns |     |