# Java Plugins

The plugin archives are installed automatically, when the .zip is entered or double clicked in Total Commander. Each plugin has a README.TXT with additional notes and included source code in each *.jar file.

# DiskDirCrc

[DiskDirCrc](https://github.com/moisescastellano/diskdircrc-tcplugin) is the first java plugin of my own.

As the original DiskDir plugin, DiskDirCrc creates a list file with all selected files and directories, including subdirs. You can then "navigate" this list with Total Commander as if it was an archive or directory containing the files.

DiskDirCrc also calculates the CRC of the files and writes them into the index file. CRC is an error-detecting code commonly used in digital storage devices to detect accidental changes to data. DiskDirCrc can then check (Alt+Shift+F9) the integrity of files comparing the CRC in the list.

# Original examples by Ken Handel

Now, every java plugin work as an independent (no need to install javalib.tgz) 64-bit plugin in TC64\. They still work in TC32. See the 64-bit update comment (2021) All samples by Handel have been cleaned from extra libraries, repackaged and are available at he links below.

<table border="1">

<tbody>

<tr>

<th>Plugin Type</th>

<th>Plugin Download</th>

<th>Version</th>

<th>File Extensions</th>

<th>Description</th>

<th>64-bit update comment (2021)</th>

</tr>

<tr>

<th colspan="6">

## Demo Plugins

</th>

</tr>

<tr>

<td>Lister plugin</td>

<td>Swing Demo</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/SwingDemo.zip.zip)</td>

<td>*.swing</td>

<td>The popular JFC Applet [SwingSet2](http://java.sun.com/products/plugin/1.5.0/demos/plugin/applets.html)</td>

<td>64-bit version works fine</td>

</tr>

<tr>

<td>Lister plugin</td>

<td>SWT Demo</td>

<td>[1.7 (32 bits)](http://java.totalcmd.net/V1.7/SWTDemo.tgz)</td>

<td>*.swt</td>

<td>The Eclipse Demo Control Example</td>

<td>SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress...</td>

</tr>

<tr>

<th colspan="6">

## Multimedia Plugins

</th>

</tr>

<tr>

<td>Lister plugin</td>

<td>Audio Player</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JLGui.zip)</td>

<td>*.mp3, *.mpeg, *.flac, *.ape,  
*.mac, *.ogg, *.spx</td>

<td>Winamp Clone in Java [JLGui](http://sourceforge.net/projects/jlgui/)</td>

<td> </td>

</tr>

<tr>

<td>Lister plugin</td>

<td>Video Player</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ VideoPlayer.zip)</td>

<td>*.mpg, *.mpeg, *.mov, *.avi</td>

<td>Java Video Player. (Deprecated: For this plugin you have to install first [Java Media Framework](http://java.sun.com/products/java-media/jmf/downloads/index.html))</td>

<td>64-bit version works fine. I don't even needed to install JMF. It shows exception when starting, but then videos are shown correctly</td>

</tr>

<tr>

<th colspan="6">

## Java Developement

</th>

</tr>

<tr>

<td>Lister plugin</td>

<td>Java Decompiler</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JADDemo.zip)</td>

<td>*.class, *.java</td>

<td>Uses [JAD](http://www.kpdus.com/jad.html) to decompile class files (supports syntax highlighting and context menu)</td>

<td>64-bit version works fine. Decompiler (jad.exe) is included so you do not need to install it</td>

</tr>

<tr>

<th colspan="6">

## Fans of good old C-64

</th>

</tr>

<tr>

<td>Packer plugin</td>

<td>D64 disk image viewer</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/DirCBM.zip)</td>

<td>*.d64</td>

<td>Java version of [DIRCBM](http://www.totalcmd.net/plugring/DIRCBM.html)</td>

<td>64-bit works fine for unpackaging. For packaging, it throws an exception, BUT this also happens in the 32-bit version: DirCBM was ever only able to *show* the contents of C64 disk images</td>

</tr>

<tr>

<td>Lister plugin</td>

<td>CBM 6510 Disassembler</td>

<td>[1.7 (32 bits)](http://java.totalcmd.net/V1.7/CBM6502.TGZ)</td>

<td>*.prg</td>

<td>Disassemble CBM 6510 machine code instructions</td>

<td>SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress...</td>

</tr>

<tr>

<td>Lister plugin</td>

<td>C64 SID player</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JSid.zip)</td>

<td>*.sid, *.psid</td>

<td>Uses [JSIDPlay](http://www.jac64.com/) of the Java emulator JAC64 to play C64 sound files</td>

<td> </td>

</tr>

<tr>

<th colspan="6">

## Graphics

</th>

</tr>

<tr>

<td>Lister plugin</td>

<td>3D graphics model viewer</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/3DDemo.zip)</td>

<td>*.3ds</td>

<td>Uses [Starfire Research](http://www.starfireresearch.com/services/java3d/inspector3ds.html)s Java 3D loader for the 3DS file format. For this plugin you have to install Java 3D</td>

<td>64-bit version not tested. Java 3D is deprecated technology.</td>

</tr>

<tr>

<td>Lister plugin</td>

<td>Image Viewer</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ImageViewer.zip)</td>

<td>*.bmp, *.ico, *.jp(e)g, *.gif, *.png</td>

<td>Image viewer using Java 2D</td>

<td>64-bit version works fine</td>

</tr>

<tr>

<th colspan="6">

## Windows Application Integration (OLE based)

</th>

</tr>

<tr>

<td>Lister plugin</td>

<td>OLE Viewer</td>

<td>[1.7 (32 bits)](http://java.totalcmd.net/V1.7/OLEDemo.tgz)</td>

<td>*.doc, *.rtf, *.xls, *.wmv, *.mpa, *.mp(e)g, *.avi, *.asf, *.wav, *.pdf</td>

<td>Uses Microsofts OLE interface</td>

<td>SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress...</td>

</tr>

<tr>

<th colspan="6">

## Internet Access

</th>

</tr>

<tr>

<td>Lister plugin</td>

<td>HTML Browser</td>

<td>[1.7 (32 bits)](http://java.totalcmd.net/V1.7/BrowserDemo.tgz)</td>

<td>*.html</td>

<td>Embeds Microsoft Internet Explorer</td>

<td>SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. However, who cares about IE?</td>

</tr>

<tr>

<td>File system plugin</td>

<td>Email reader</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/EMail.zip)</td>

<td> </td>

<td>Check your email account, protocols pop3 and imap are supported</td>

<td> </td>

</tr>

<tr>

<th colspan="6">

## Networking

</th>

</tr>

<tr>

<td>File system plugin</td>

<td>SNMP plugin</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/SNMPplugin.zip)</td>

<td> </td>

<td>Analyse network using SNMP (Simple Network Monitor Protocol), plugin author: Ján Gregor</td>

<td> </td>

</tr>

<tr>

<th colspan="6">

## Plugin Developement Samples to Learn

</th>

</tr>

<tr>

<td>Packer plugin</td>

<td>File lister</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/JCatalogue.zip)</td>

<td>*.jlst</td>

<td>Creates a file list and browse its contents</td>

<td>64-bit version works fine</td>

</tr>

<tr>

<td>Content plugin</td>

<td>Image Content</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ImageContent.zip)</td>

<td>*.bmp, *.ico, *.jp(e)g, *.gif, *.png</td>

<td>Shows image properties (width, height, bit-depth) - poor performance :-(</td>

<td> </td>

</tr>

<tr>

<td>Lister plugin</td>

<td>Hello World</td>

<td>[1.7 (32 bits)](http://java.totalcmd.net/V1.7/HelloWorld.tgz)</td>

<td>*.tst</td>

<td>Hello World: show java properties in a window</td>

<td>SWT based plugins need corresponding 64-bit libraries. I have installed them, yet these plugins dont work. In progress...</td>

</tr>

<tr>

<td>File system plugin</td>

<td>Local Drives</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/Drives.zip)</td>

<td> </td>

<td>Browse your local file systems</td>

<td> </td>

</tr>

<tr>

<td>Content plugin</td>

<td>Content Demo</td>

<td>[2.1 (32/64 bits)](https://github.com/moisescastellano/tcmd-java-plugin/raw/main/examples/v2.1/ContentDemo.zip)</td>

<td>*.prg</td>

<td>Shows several test columns</td>

<td> </td>

</tr>

</tbody>

</table>