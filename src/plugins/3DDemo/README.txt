Additional Installation Notes:
==============================
A Java Runtime Environment is required. If you are using Total Commander x64, you need to use a 64-bit JRE.
Installing additional libraries as stated in original README.TXT file is NO LONGER REQUIRED.

Also Java 3D must be installed to this Java Runtime Environment

What this plugin does:
======================
This plugin shows 3D models created by "Autodesk 3ds Max" (http://www.autodesk.de/3dsmax) (*.3ds).
In the quick view, the slider is badly invisible, so open with F3.

How to use this plugin:
=======================
Simply open the files "demoX.3ds" in the lister window (F3) to show the 3D model.
You can rotate the model by holding the left mouse button pushed and dragging over the picture.
You can move the model by holding the right mouse button pushed and dragging over the picture.
You can zoom the picture in and out, by pulling the slider at the right hand of the picture up or down.
To quit the demo simply press Escape and close the window (Ctrl+Q again in quick view or closing the external window)

64-bit version:
=================
The original Java Plugin is Copyright (C) 2006-2007 Ken Handel. 
He also provided a lot of java plugin examples for any kind: lister, packer, file system and content: http://java.totalcmd.net/V1.7/examples.html
This is one of those examples.

However the developer abandoned the project in 2007 (now is 2021), and he has been unreachable since then (some people tried to reach him long ago).
64-bit versions of this plugin were no available, because as said the project was abandoned in 2007 and 64-bit tcmd came around 2011.
That makes it unusable for most people, nowadays using 64-bit TC.

As the license allows so, we have undertaken the project;
special thanks to Ghisler (author of TC) for recompiling the dll.
you can see the thread discussing it in the TC forum at: https://www.ghisler.ch/board/viewtopic.php?t=75726

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

Contact
=======
Let me know if you have any comment, suggestion or problem regarding this java plugin, 
choose the most appropiate way to contact me:
 - the forum thread above: https://www.ghisler.ch/board/viewtopic.php?t=75726
 - email: moises.castellano (at) gmail.com
 - https://github.com/moisescastellano/tcmd-java-plugin/issues  (github for future java plugins, in progress)
Please detail the specific java plugin and JRE version you are using.

 
Enjoy!
