# Visual C++ source

Visual C++ source for creating the dll (the plugin core is just this dll renamed to wlx, wfx, wdx or wcx)

## Removed: .suo and .user files

[Should I add the Visual Studio .suo and .user files to source control?](https://stackoverflow.com/questions/72298/should-i-add-the-visual-studio-suo-and-user-files-to-source-control)


> These files contain user preference configurations that are in general specific to your machine, so it's better not to put it in SCM. Also, VS will change it almost every time you execute it, so it will always be marked by the SCM as 'changed'. I don't include either, I'm in a project using VS for 2 years and had no problems doing that. The only minor annoyance is that the debug parameters (execution path, deployment target, etc.) are stored in one of those files (don't know which), so if you have a standard for them you won't be able to 'publish' it via SCM for other developers to have the entire development environment 'ready to use'.

## Tips to compile dll library

To compile, change:
- General -->	Outputdirectory
- Linker  --> Output File
- Debug --> Release
- Platform --> x64

## Original code by Ken Handel (32-bit version)

Original code by Ken Handel (32-bit version) can be found in [release-1.7 branch](https://github.com/moisescastellano/tcmd-java-plugin/tree/release-1.7)

## Project changes by Ghisler (64-bit version)

[Re: Converting java based plugins to 64-bit - Post by *ghisler(Author) » 2021-12-08, 09:57 UTC](https://www.ghisler.ch/board/viewtopic.php?p=408040#p408040)

>	I managed to compile it for 64-bit now, with only minor changes:
>	https://www.totalcommander.ch/win/packer/JCatalogue64.zip
>
>	1. I had to disable optimizations, it crashes with optimizations on
>	2. I changed the runtime library to Multi-threaded /MT instead of Multi-threaded-DLL. The latter does not mean that it's a library to create a dll. It means that it uses the Microsoft VCC runtime libraries.
>	3. I get warnings that HWND (8 bytes) is converted to a smaller variable jint (4 bytes). But to my knowledge, Windows uses only the lower 32-bit of a Windows handle to have compatible handles between 32-bit and 64-bit programs. Therefore it should work fine.
>

Other code changes by Ghisler:
- changed some "int" to "HANDLE" in wcxhead.h and wcxplug.cpp for *Mem* functions
