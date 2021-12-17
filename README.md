# Java Plugin interface 2.0 for Total Commander

This interface makes it possible to write Total Commander plugins (WLX, WFX, WDX and WCX) in Java.

The original Java Plugin is Copyright (C) 2006-2007 Ken Handel: 
he also provided a lot of [java plugin examples](http://java.totalcmd.net/V1.7/examples.html) for any kind: lister, packer, file system and content.

[JCatalogue](http://wincmd.ru/files/9924387/JCatalogue.zip) is one of those examples updated to 64-bit.

However the developer abandoned the project in 2007 (now is 2021), and he has been unreachable since then (some people tried to reach him long ago). 64-bit versions of this plugin were no available, because as said the project was abandoned in 2007 and 64-bit tcmd came around 2011. That makes it unusable for most people, nowadays using 64-bit TC.

64-bit version
==============

As the license allows so, we have undertaken the project;
special thanks to Ghisler (author of TC) for recompiling the dll.
you can see the thread discussing it in [this thread of TC forum](https://www.ghisler.ch/board/viewtopic.php?t=75726)

Java plugin examples
====================
Check the [list of java plugin examples](examples_64bit.hmlt)

Now, every java plugin:
  1. Works as an independent 64-bit plugin in TC64. Still works in TC32.
  2. May be installed as usual, just entering the zip, without further ado.
  
Changes for 64-bit version (from 32-bit original java plugin by Ken Handel):
============================================================================
- included the new (wcx64 / wfx64 / wlx64 / wdx64) dll, recompiled by Ghisler (author of Total Commander), from Handel sources
- included the javalib dirs: Now every Java plugin installs just entering the zip, without further ado
- modified tc_javaplugin.ini accordingly (to refer the new location of javalib)
- included this text file with some extra info for the 64-version
- repackaged as .zip (.tgz needs first to be untgzed to tar, also not so standard)

DiskDirCrc Total Commander plugin
=================================
[DiskDirCrc](https://github.com/moisescastellano/diskdircrc-tcplugin) is the first java plugin of my own.

As the original DiskDir plugin, DiskDirCrc creates a list file with all selected files and directories, including subdirs. You can then "navigate" this list with Total Commander as if it was an archive or directory containing the files.

DiskDirCrc also calculates the CRC of the files and writes them into the index file. CRC is an error-detecting code commonly used in digital storage devices to detect accidental changes to data. DiskDirCrc can then check (Alt+Shift+F9) the integrity of files comparing the CRC in the list.

Download and resources
======================
I recommend checking first [DiskDirCrc](https://github.com/moisescastellano/diskdircrc-tcplugin), my own TC java plugin.

You can download the java plugin interface from [totalcmd.net](http://totalcmd.net/plugring/tc_java_64bits.html)

Also check the original [Java Plugin interface from Ken Handel](http://totalcmd.net/plugring/tc_java.html)

Here is the [JCatalogue example](http://wincmd.ru/files/9924387/JCatalogue.zip)

Java Plugin interface at [totalcmd.net](http://totalcmd.net/plugring/tc_java_64bits.html)

Contact
=======
Let me know if you have any comment, suggestion or problem regarding this java plugin, 
choose the most appropiate way to contact me:
 - [this thread in the Total Commander forum](https://www.ghisler.ch/board/viewtopic.php?t=75726)
 - email: moises.castellano (at) gmail.com
 - [Github Java plugin interface project](https://github.com/moisescastellano/tcmd-java-plugin/issues)

Please detail the specific java plugin and JRE version you are using.

Disclaimer
==========
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS “AS IS” AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

License
=======
Licensed under under the GNU General Public License v3.0, a strong copyleft license:
https://github.com/moisescastellano/tcmd-java-plugin/blob/main/LICENSE

