# Java Plugin Interface 2.3 for [Total Commander](https://www.ghisler.com)

This interface makes it possible to write Total Commander plugins (WLX, WFX, WDX and WCX) in Java.

The original Java Plugin is Copyright (C) 2006-2007 Ken Handel: 
he also provided a lot of [java plugin examples](http://java.totalcmd.net/V1.7/examples.html) for any kind: lister, packer, file system and content, now [updated to 64-bit](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/examples_64bit.md).

However the developer abandoned the project in 2007 and he has been unreachable since then (some people tried to reach him long ago). 64-bit versions of this plugin were no available, because as said the project was abandoned in 2007 and 64-bit tcmd came around 2011. That makes it unusable for most people, nowadays using 64-bit TC.

As the license allows so, we have undertaken the project;
special thanks to Ghisler (author of TC) for recompiling the dll:
you can see the thread discussing it in [this thread of TC forum](https://www.ghisler.ch/board/viewtopic.php?t=75726).

Java plugin examples
--------------------
Check the [list of java plugin examples](examples_64bit.md). **There are over 20 of them now!**

Now, every java plugin:
 - Works as an independent 64-bit plugin in TC64. Still works in TC32.
 - May be installed as usual, just entering the zip, without further ado.
 
### [DiskDirCrc](https://github.com/moisescastellano/diskdircrc-tcplugin)

As the original DiskDir plugin, DiskDirCrc creates a list file with all selected files and directories, including subdirs. You can then "navigate" this list with Total Commander as if it was an archive or directory containing the files.

DiskDirCrc also calculates the CRC of the files and writes them into the index file. CRC is an error-detecting code commonly used in digital storage devices to detect accidental changes to data. **DiskDirCrc can then check (Alt+Shift+F9) the integrity of files comparing the CRC in the list**.

### [JavaDecompiler](https://moisescastellano.github.io/javadecompiler-tcplugin/)

This plugin allows Total Commander to both **decompile** and **navigate** java *.class* files. It is a packer plugin, meaning you can "enter" these files as archives. 

### [ThousandTypes](https://github.com/moisescastellano/thousandTypes-tcplugin)

Have you ever being checking what's inside of a lot of documents such as PDFs or .doc files, 
spending a lot of time waiting for a new Acrobat Reader, MS-Word or Whatever-program to open, just to close it and continuing the process?

Have you ever wondered what a particular file contained and wanted to take a look at its contents, not having the associated application to open it?

This plugin allows TC to show a very quick text preview of almost **every file format**.

Download and resources
--------------------
- [Java plugin examples](https://moisescastellano.github.io/tcmd-java-plugin/examples_64bit)
- Page of java plugin interface at [totalcmd.net](http://totalcmd.net/plugring/tc_java_64bits.html)
- Also check the original [Java Plugin interface by Ken Handel](http://totalcmd.net/plugring/tc_java.html)
- This is a work in progress, you can help with [things to do](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/to-do.md)
- History of [changes](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/changes.md)
- Thread for [converting java based plugins to 64-bit](https://www.ghisler.ch/board/viewtopic.php?t=75726) at the TC forum


Troubleshooting guide
-----------------------------------

This interface and all derived plugins are written in Java, so you need to have installed a [Java Runtime Environment (JRE)](https://www.java.com/en/download/manual.jsp). The Java plugin interface and derived plugins were tested on **Oracle (Sun) JRE 1.8**  (jre-8u311-windows-x64.exe).

In case you have any of the following issues, refer to the [Troubleshooting guide](https://moisescastellano.github.io/tcmd-java-plugin/troubleshooting)
- [In case you have more than one Java plugin installed](troubleshooting.md#In-case-you-have-more-than-one-Java-plugin-installed)
- [Be sure you use the same (32/64) platform for JVM and TC](troubleshooting.md#Be-sure-you-use-the-same-(32/64)-platform-for-JVM-and-TC)
- [In case you have both TCx64 and TCx32 installed](troubleshooting.md#In-case-you-have-both-TCx64-and-TCx32-installed)
- [Error *Java Runtime Environment is not installed on this Computer*](troubleshooting.md#Error-Java-Runtime-Environment-is-not-installed-on-this-Computer)
- [Error *LoadLibrary Failed*](troubleshooting.md#Error-LoadLibrary-Failed)
- [Error *Starting Java Virtual Machine failed*](troubleshooting.md#Error-Starting-Java-Virtual-Machine-failed)
- [Error *Class not found class='tcclassloader/PluginClassLoader'*](troubleshooting.md#Error-Class-not-found-class-tcclassloader/PluginClassLoader)
- [Error *Initialization failed in class...*](troubleshooting.md#Error-Initialization-failed-in-class)
- [Error *Exception in class 'tcclassloader/PluginClassLoader'*](troubleshooting.md#Error-Exception-in-class-tcclassloader/PluginClassLoader)
- [Error *Access violation at address...*](troubleshooting.md#Error-Access-violation-at-address)
- [Error *Crash in plugin ... Access violation at address...*](troubleshooting.md#Error-Crash-in-plugin-Access-violation-at-address)

Issues and things to-do
----------------------
This is a work in progress. **Help wanted!** - in particular with Visual C++ issues.
 - Refer to [things to do](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/to-do.md) for work in progress.
 - Check also the [issues page](https://github.com/moisescastellano/tcmd-java-plugin/issues).
 - [Plugins based on this interface](https://github.com/moisescastellano/tcmd-java-plugin/blob/main/examples_64bit.md) have their own to-do page.

Contact
--------------------
Let me know if you have any comment, suggestion or problem regarding this java plugin, 
choose the most appropiate way to contact me:
 - [this thread in the Total Commander forum](https://www.ghisler.ch/board/viewtopic.php?t=75726)
 - email: moises.castellano (at) gmail.com
 - [Github Java plugin interface project's issues page](https://github.com/moisescastellano/tcmd-java-plugin/issues)

Please detail the specific version of: Java plugin interface, Total Commander and JRE that you are using.

Disclaimer
--------------------
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ???AS IS??? AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

License
--------------------
Licensed under the GNU General Public License v3.0, a strong copyleft license:
https://github.com/moisescastellano/tcmd-java-plugin/blob/main/LICENSE

