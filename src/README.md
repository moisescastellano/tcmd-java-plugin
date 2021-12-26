
# Source code for the Total Commander Java Plugin

## Branches

### main branch

Last released code

### release-1.7 branch: 

Intended to keep **original code by Ken Handel**, java plugin interface version 1.7. New changes to code will NOT be merged into this branch.

### release-2.x branches:

Releases with new code: refer to [changes](../changes.md)

### develop:

Features are merged into this branch. Releases branchs are created off of develop. Check [GitFlow](https://datasift.github.io/gitflow/IntroducingGitFlow.html)

## Folders structure

### src\vc-project

Visual C++ source for creating the dll (the plugin core is this dll renamed to wlx, wfx, wdx or wcx)

For further details refer to [vc-project/README file](vc-project/README.md)

### src\tc-apis

The Java plugin API source code

### src\tc-classloader

The plugins class loader

### src\plugins

Java source code for each example plugin

#### src\plugins\SNMPplugin 

(at release-1.7 branch): code was not included in the jar by Handel. SNMP library code is mixed together with the plugin class, which should be "snmpplugin.PluginEngine" - to be decompiled

#### src\plugins\SwingDemo 

(at release-1.7 branch): in the original jar by Handel, Sun Microsystems code is mixed together with the SwingDemo plugin - to be cleaned

