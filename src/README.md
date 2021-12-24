
#Source code for the Total Commander Java Plugin

## Branches

### release-1.7 branch: 

Intended to keep original code by Ken Handel, java plugin interface version 1.7. New changes to code will NOT be merged into this branch.

### release-2.x branches:

Releases with new code: refer to [changes](../changes.md)

### develop:

Features are merged into this branch. Releases branchs are created off of develop. Check [GitFlow](https://datasift.github.io/gitflow/IntroducingGitFlow.html)

## Folders structure

### src\vc-project

Visual C++ source for creating the dll (the plugin core is this dll renamed to wlx, wfx, wdx or wcx)

	https://stackoverflow.com/questions/72298/should-i-add-the-visual-studio-suo-and-user-files-to-source-control

	Should I add the Visual Studio .suo and .user files to source control?

	These files contain user preference configurations that are in general specific to your machine, so it's better not to put it in SCM. Also, VS will change it almost every time you execute it, so it will always be marked by the SCM as 'changed'. I don't include either, I'm in a project using VS for 2 years and had no problems doing that. The only minor annoyance is that the debug parameters (execution path, deployment target, etc.) are stored in one of those files (don't know which), so if you have a standard for them you won't be able to 'publish' it via SCM for other developers to have the entire development environment 'ready to use'.

### src\tc-apis

The Java plugin API source code

### src\plugins

Java source code for each example plugin

*Notes*:
src\plugins\SNMPplugin 
	release-1.7 branch: code was not included in the jar by Handel. SNMP library code is mixed together with the plugin class, which should be "snmpplugin.PluginEngine" - to be decompiled
src\plugins\SwingDemo 
	release-1.7 branch: in the original jar by Handel, Sun Microsystems code is mixed together with the SwingDemo plugin - to be cleaned

