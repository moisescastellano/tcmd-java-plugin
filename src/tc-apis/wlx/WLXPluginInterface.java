package plugins.wlx;

import java.awt.Rectangle;

import plugins.DefaultParam;

/**
 * This is the interface for Total Commander WLX plugins.
 * 
 * @author Ken Händel
 */
public interface WLXPluginInterface {
	/**
	 * everything is ok.
	 */
	int LISTPLUGIN_OK = 0;

	/**
	 * something is wrong.
	 */
	int LISTPLUGIN_ERROR = 1;

	/**
	 * listSearchText: String to be searched.
	 */
	int LCS_FINDFIRST = 1;

	/**
	 * listSearchText: Search from the beginning of the first displayed line
	 * (not set: find next).
	 */
	int LCS_MATCHCASE = 2;

	/**
	 * listSearchText: The search string is to be treated case-sensitively.
	 */
	int LCS_WHOLEWORDS = 4;

	/**
	 * listSearchText: Search backwards towards the beginning of the file.
	 */
	int LCS_BACKWARDS = 8;

	/**
	 * listSendCommand: Copy current selection to the clipboard.
	 */
	int LC_COPY = 1;

	/**
	 * listSendCommand: New parameters passed to plugin, see LCP_* constants.
	 */
	int LC_NEWPARAMS = 2;

	/**
	 * listSendCommand: Select the whole contents.
	 */

	int LC_SELECT_ALL = 3;

	/**
	 * listSendCommand: Go to new position in document (in percent).
	 */
	int LC_SETPERCENT = 4;

	/**
	 * listSendCommand: Text: Word wrap mode is checked.
	 */
	int LCP_WRAPTEXT = 1;

	/**
	 * listSendCommand: Images: Fit image to window is checked.
	 */
	int LCP_FITTOWINDOW = 2;

	/**
	 * listSendCommand: Ansi charset is checked.
	 */
	int LCP_ANSI = 4;

	/**
	 * listSendCommand: Ascii(DOS) charset is checked.
	 */
	int LCP_ASCII = 8;

	/**
	 * listSendCommand: Variable width charset is checked.
	 */
	int LCP_VARIABLE = 12;

	/**
	 * listSendCommand: force show lister window.
	 */
	int LCP_FORCESHOW = 16;

	/**
	 * listSendCommand: in addition to LCP_FITTOWINDOW.
	 */
	int LCP_FITLARGERONLY = 32;

	/**
	 * listSendCommand: the image needs to be centered.
	 */
	int LCP_CENTER = 64;

	/**
	 * ListLoad is called when a user opens lister with F3 or the Quick View
	 * Panel with Ctrl+Q, and when the definition string either doesn't exist,
	 * or its evaluation returns true.<BR>
	 * Please note that multiple Lister windows can be open at the same time!
	 * Therefore you cannot save settings in global variables. You can call
	 * RegisterClass with the parameter cbWndExtra to reserve extra space for
	 * your data, which you can then access via GetWindowLong(). Or use an
	 * internal list, and store the list parameter via
	 * SetWindowLong(hwnd,GWL_ID,...). Lister will subclass your window to catch
	 * some hotkeys like 'n' or 'p'.<BR>
	 * When lister is activated, it will set the focus to your window. If your
	 * window contains child windows, then make sure that you set the focus to
	 * the correct child when your main window receives the focus!<BR>
	 * 
	 * If lcp_forceshow is defined, you may try to load the file even if the
	 * plugin wasn't made for it. Example: A plugin with line numbers may only
	 * show the file as such when the user explicitly chooses 'Image/Multimedia'
	 * from the menu.
	 * 
	 * Lister plugins which only create thumbnail images do not need to
	 * implement this function.
	 * 
	 * @param parentWin
	 *            This is lister's window. Create your plugin window as a child
	 *            of this window.
	 * @param input
	 *            The name of the file which has to be loaded.
	 * @param showFlags
	 *            A combination of the following flags:
	 *            <UL>
	 *            <LI>lcp_wraptext Text: Word wrap mode is checked
	 *            <LI>lcp_fittowindow Images: Fit image to window is checked
	 *            <LI>lcp_ansi Ansi charset is checked
	 *            <LI>lcp_ascii Ascii(DOS) charset is checked
	 *            <LI>lcp_variable Variable width charset is checked
	 *            <LI>lcp_forceshow User chose 'Image/Multimedia' from the
	 *            menu. See remarks.
	 *            </UL>
	 *            You may ignore these parameters if they don't apply to your
	 *            document type.
	 * @return Return a handle to your window if load succeeds, NULL otherwise.
	 *         If NULL is returned, Lister will try the next plugin.
	 */
	int listLoad(final int parentWin, final String input, final int showFlags);

	/**
	 * ListCloseWindow is called when a user closes lister, or loads a different
	 * file. If ListCloseWindow isn't present, DestroyWindow() is called.<BR>
	 * You can use this function to close open files, free buffers etc.
	 * 
	 * @param listWin
	 *            This is the window handle which needs to be destroyed.
	 */
	void listCloseWindow(final int listWin);

	/**
	 * ListGetDetectString is called when the plugin is loaded for the first
	 * time. It should return a parse function which allows Lister to find out
	 * whether your plugin can probably handle the file or not. You can use this
	 * as a first test - more thorough tests may be performed in ListLoad().
	 * It's very important to define a good test string, especially when there
	 * are dozens of plugins loaded! The test string allows lister to load only
	 * those plugins relevant for that specific file type.<BR>
	 * 
	 * The syntax of the detection string is as follows. There are operands,
	 * operators and functions. Operands:
	 * <UL>
	 * <LI>
	 * 
	 * EXT The extension of the file to be loaded (always uppercase).
	 * <LI>SIZE The size of the file to be loaded.
	 * <LI>FORCE 1 if the user chose 'Image/Multimedia' from the menu, 0
	 * otherwise.
	 * 
	 * <LI>MULTIMEDIA This detect string is special: It is always TRUE (also in
	 * older TC versions). If it is present in the string, this plugin overrides
	 * internal multimedia viewers in TC. If not, the internal viewers are used.
	 * Check the example below!
	 * 
	 * <LI>[5] The fifth byte in the file to be loaded. The first 8192 bytes
	 * can be checked for a match.
	 * <LI>12345 The number 12345
	 * <LI>"TEST" The string "TEST"
	 * </UL>
	 * Operators
	 * <UL>
	 * <LI>& AND. The left AND the right expression must be true (!=0).
	 * <LI>| OR: Either the left OR the right expression needs to be true
	 * (!=0).
	 * <LI>= EQUAL: The left and right expression need to be equal.
	 * <LI>!= UNEQUAL: The left and right expression must not be equal.
	 * 
	 * <LI>< SMALLER: The left expression is smaller than the right expression.
	 * Comparing a number and a string returns false (0). Booleans are stored as
	 * 0 (false) and 1 (true).
	 * <LI>> LARGER: The left expression is larger than the right expression.
	 * </UL>
	 * Functions
	 * <UL>
	 * <LI> () Braces: The expression inside the braces is evaluated as a whole.
	 * <LI>!() NOT: The expression inside the braces will be inverted. Note
	 * that the braces are necessary!
	 * <LI>FIND() The text inside the braces is searched in the first 8192
	 * bytes of the file. Returns 1 for success and 0 for failure.
	 * <LI>FINDI() The text inside the braces is searched in the first 8192
	 * bytes of the file. Upper/lowercase is ignored.
	 * </UL>
	 * Internal handling of variables<BR>
	 * 
	 * Varialbes can store numbers and strings. Operators can compare numbers
	 * with numbers and strings with strings, but not numbers with strings.
	 * Exception: A single char can also be compared with a number. Its value is
	 * its ANSI character code (e.g. "A"=65). Boolean values of comparisons are
	 * stored as 1 (true) and 0 (false). <BR>
	 * Examples:
	 * <UL>
	 * <LI>EXT="WAV" | EXT="AVI" The file may be a Wave or AVI file.
	 * 
	 * <LI>EXT="WAV" & [0]="R" & [1]="I" & [2]="F" & [3]="F" & FIND("WAVEfmt")
	 * <LI>Also checks for Wave header "RIFF" and string "WAVEfmt"
	 * 
	 * <LI>EXT="WAV" & (SIZE<1000000 | FORCE) Load wave files smaller than
	 * 1000000 bytes at startup/file change, and all wave files if the user
	 * explictly chooses 'Image/Multimedia' from the menu.
	 * 
	 * <LI>([0]="P" & [1]="K" & [2]=3 & [3]=4) | ([0]="P" & [1]="K" & [2]=7 &
	 * [3]=8) Checks for the ZIP header PK#3#4 or PK#7#8 (the latter is used for
	 * multi-volume zip files).
	 * 
	 * <LI>EXT="TXT" & !(FINDI("&lt;HEAD&gt;") | FINDI("&lt;BODY&gt;")) This
	 * plugin handles text files which aren't HTML files. A first detection is
	 * done with the &lt;HEAD&gt; and &lt;BODY&gt; tags. If these are not found,
	 * a more thorough check may be done in the plugin itself.
	 * 
	 * <LI>MULTIMEDIA & (EXT="WAV" | EXT="MP3") Replace the internal player for
	 * WAV and MP3 files (which normally uses Windows Media Player as a plugin).
	 * Requires TC 6.0 or later!
	 * </UL>
	 * Operator precedence:
	 * <UL>
	 * <LI>The strongest operators are =, != &lt; and &gt;, then comes &, and
	 * finally |. What does this mean? Example:
	 * 
	 * <LI>expr1="a" & expr2 | expr3<5 & expr4!=b will be evaluated as
	 * ((expr1="a") & expr2) | ((expr3<5) & (expr4!="b")) If in doubt, simply
	 * use braces to make the evaluation order clear.
	 * </UL>
	 * 
	 * @param maxLen
	 *            Maximum length, in bytes, of the detection string (currently
	 *            2k).
	 * 
	 * @return Return the detection string here. See remarks for the syntax.
	 */
	String listGetDetectString(int maxLen);

	/**
	 * ListListSearchText is called when the user tries to find text in the
	 * plugin. Don't implement this function if your plugin doesn't contain any
	 * text, or doesn't support text searches!<BR>
	 * The plugin needs to highlight/select the found text by itself.
	 * 
	 * @param handle
	 *            Handle to your list window created with ListLoad
	 * @param searchString
	 *            String to be searched.
	 * @param searchParameter
	 *            A combination of the following search flags: LCS_FINDFIRST,
	 *            LCS_MATCHCASE, LCS_WHOLEWORDS, LCS_BACKWARDS
	 * @return LISTPLUGIN_OK - found, LISTPLUGIN_ERROR - not found
	 */
	int listSearchText(final int handle, final String searchString,
			final int searchParameter);

	/**
	 * ListSendCommand is called when the user changes some options in Lister's
	 * menu.
	 * 
	 * @param listWin
	 *            hande to your list window created with ListLoad
	 * @param command
	 *            One of the following commands: LC_COPY, LC_NEWPARAMS,
	 *            LC_SELECT_ALL, LC_SETPERCENT
	 * @param parameter
	 *            Used for lc_newparams. May be a combination of:
	 *            LCP_FITTOWINDOW, LCP_ANSI, LCP_ASCII, LCP_VARIABLE,
	 *            LCP_WRAPTEXT, LCP_FORCESHOW
	 * @return Return either LISTPLUGIN_OK or LISTPLUGIN_ERROR.
	 */
	int listSendCommand(final int listWin, final int command,
			final int parameter);

	/**
	 * ListPrint is called when the user chooses the print function.<BR>
	 * You need to show a print dialog, in which the user can choose what to
	 * print, and select a different printer. See the sample plugin on how to do
	 * this!
	 * 
	 * @param listWin
	 *            Hande to your list window created with ListLoad
	 * @param fileToPrint
	 *            The full name of the file which needs to be printed. This is
	 *            the same file as loaded with ListLoad.
	 * @param defPrinter
	 *            Name of the printer currently chosen in Total Commander. May
	 *            be NULL (use default printer).
	 * @param printFlags
	 *            Currently not used (set to 0). May be used in a later version.
	 * @param margins
	 *            The left, top, right and bottom margins of the print area, in
	 *            MM_LOMETRIC measurement units (1/10 mm). May be ignored.
	 * @return Return either LISTPLUGIN_OK or LISTPLUGIN_ERROR.
	 */
	int listPrint(final int listWin, final String fileToPrint,
			final String defPrinter, final int printFlags,
			final Rectangle margins);

	/**
	 * ListNotificationReceived is called when the parent window receives a
	 * notification message from the child window: WM_COMMAND, WM_NOTIFY,
	 * WM_MEASUREITEM or WM_DRAWITEM.<BR>
	 * Do not implement this function if you don't use any owner-drawn controls
	 * and don't require any notification messages! Possible applications:
	 * Owner-drawn Listview control, reacting to scroll messages, etc.
	 * 
	 * @param listWin
	 *            Hande to your list window created with ListLoad
	 * @param message
	 *            The received message, one of the following: WM_COMMAND,
	 *            WM_NOTIFY, WM_MEASUREITEM or WM_DRAWITEM. (from winuser.h)
	 * @param wParam
	 *            The WPARAM parameter of the message.
	 * @param lParam
	 *            The LPARAM parameter of the message.
	 * @return Return the value described for that message in the Windows API
	 *         help.
	 */
	int listNotificationReceived(final int listWin, final int message,
			final int wParam, final int lParam);

	/**
	 * ListGetPreviewBitmap is called to retrieve a bitmap for the thumbnails
	 * view. Please only implement and export this function if it makes sense to
	 * show preview pictures for the supported file types! This function is new
	 * in version 1.4. It requires Total Commander >=6.5, but is ignored by
	 * older versions.
	 * <OL>
	 * <LI> This function is only called in Total Commander 6.5 and later. The
	 * plugin version will be >= 1.4.
	 * <LI> The bitmap handle goes into possession of Total Commander, which
	 * will delete it after using it. The plugin must not delete the bitmap
	 * handle!
	 * <LI> Make sure you scale your image correctly to the desired maximum
	 * width+height! Do not fill the rest of the bitmap - instead, create a
	 * bitmap which is SMALLER than requested! This way, Total Commander can
	 * center your image and fill the rest with the default background color.
	 * </OL>
	 * 
	 * @param fileToLoad
	 *            The name of the file for which to load the preview bitmap.
	 * @param width
	 *            Requested maximum width of the bitmap.
	 * @param height
	 *            Requested maximum height of the bitmap
	 * @param contentBuf
	 *            The first 8 kBytes (8k) of the file. Often this is enough data
	 *            to show a reasonable preview, e.g. the first few lines of a
	 *            text file.
	 * @param contentBufLen
	 *            The length of the data passed in contentbuf. Please note that
	 *            contentbuf is not a 0 terminated string, it may contains 0
	 *            bytes in the middle! It's just the 1:1 contents of the first
	 *            8k of the file.
	 * @param filename
	 *            Here you need to return the bitmap handle. Three forms are
	 *            supported:
	 *            <ol>
	 *            <li>filename.append ("253|shell32.dll"); // load bitmap from a
	 *            resource (EXE/DLL), referenced by a resource id
	 *            <li>filename.append
	 *            ("G:\\Totalcmd\\plugins\\java\\Drives\\test.bmp"); // load
	 *            bitmap from bmp file (absolute path name)
	 *            <li>filename.append ("%CWD%\\test.bmp"); // load bitmap from
	 *            bmp file in the plugin directory
	 *            </ol>
	 * @return Return a device-dependent bitmap created with e.g.
	 *         CreateCompatibleBitmap.
	 */
	Object listGetPreviewBitmap(final String fileToLoad, final int width,
			final int height, final String contentBuf, final int contentBufLen,
			final StringBuffer filename);

	/**
	 * ListSetDefaultParams is called immediately after loading the DLL, before
	 * ListLoad. This function is new in version 1.2. It requires Total
	 * Commander >=5.51, but is ignored by older versions.<BR>
	 * This function is only called in Total Commander 5.51 and later. The
	 * plugin version will be >= 1.2.
	 * 
	 * @param dps
	 *            This structure of type ListDefaultParamStruct currently
	 *            contains the version number of the plugin interface, and the
	 *            suggested location for the settings file (ini file). It is
	 *            recommended to store any plugin-specific information either
	 *            directly in that file, or in that directory under a different
	 *            name. Make sure to use a unique header when storing data in
	 *            this file, because it is shared by other file system plugins!
	 *            If your plugin needs more than 1kbyte of data, you should use
	 *            your own ini file because ini files are limited to 64k.
	 */
	void listDefaultGetParams(DefaultParam dps);

	/**
	 * New in Total Commander 7: ListLoadNext is called when a user switches to
	 * the next or previous file in lister with 'n' or 'p' keys, or goes to the
	 * next/previous file in the Quick View Panel, and when the definition
	 * string either doesn't exist, or its evaluation returns true.
	 * 
	 * @param parentWin
	 *            This is lister's window. Your plugin window needs to be a
	 *            child of this window
	 * 
	 * @param listWin
	 *            The plugin window returned by ListLoad
	 * 
	 * @param fileToLoad
	 *            The name of the file which has to be loaded.
	 * 
	 * 
	 * @param showFlags
	 *            A combination of the following flags:
	 *            <UL>
	 *            <LI>lcp_wraptext Text: Word wrap mode is checked
	 *            <LI>lcp_fittowindow Images: Fit image to window is checked
	 *            <LI>lcp_fitlargeronly Fit image to window only if larger than
	 *            the window. Always set together with lcp_fittowindow.
	 *            <LI>lcp_center Center image in viewer window
	 *            <LI>lcp_ansi Ansi charset is checked
	 *            <LI>lcp_ascii Ascii(DOS) charset is checked
	 *            <LI>lcp_variable Variable width charset is checked
	 *            <LI>lcp_forceshow User chose 'Image/Multimedia' from the
	 *            menu. See remarks.
	 *            </UL>
	 *            You may ignore these parameters if they don't apply to your
	 *            document type.
	 * 
	 * @return LISTPLUGIN_OK if load succeeds, LISTPLUGIN_ERROR otherwise. If
	 *         LISTPLUGIN_ERROR is returned, Lister will try to load the file
	 *         with the normal ListLoad function (also with other plugins).
	 * 
	 * <BR>
	 * <B>Remarks:</B><BR>
	 * 
	 * Please note that multiple Lister windows can be open at the same time!
	 * Therefore you cannot save settings in global variables. You can call
	 * RegisterClass with the parameter CBWNDEXTRA to reserve extra space for
	 * your data, which you can then access via GetWindowLong(). Or use an
	 * internal list, and store the list parameter via
	 * SetWindowLong(hwnd,GWL_ID,...). Lister will subclass your window to catch
	 * some hotkeys like 'n' or 'p'. When lister is activated, it will set the
	 * focus to your window. If your window contains child windows, then make
	 * sure that you set the focus to the correct child when your main window
	 * receives the focus! <BR>
	 * If LCP_FORCESHOW is defined, you may try to load the file even if the
	 * plugin wasn't made for it. <BR>
	 * Example: A plugin with line numbers may only show the file as such when
	 * the user explicitly chooses 'Image/Multimedia' from the menu. <BR>
	 * Lister plugins which only create thumbnail images do not need to
	 * implement this function. If you do not implement ListLoadNext but only
	 * ListLoad, then the plugin will be unloaded and loaded again when
	 * switching through files, which results in flickering.
	 */
	int listLoadNext(int parentWin, int listWin, String fileToLoad,
			int showFlags);

	/**
	 * ListSearchDialog is called when the user tries to find text in the
	 * plugin. Only implement this function if your plugin requires a
	 * plugin-specific search dialog! For searching text, please implement
	 * ListSearchText instead!
	 * 
	 * @param listWin
	 *            Hande to your list window created with ListLoad
	 * 
	 * 
	 * @param findNext
	 *            0: FindFirst was chosen by the user<BR>
	 *            1: FindNext was chosen from the menu
	 * 
	 * @return LISTPLUGIN_OK if you implement this function, or LISTPLUGIN_ERROR
	 *         if Total Commander should show its own text search dialog and
	 *         call ListSearchText later. This allows a plugin to support both
	 *         its own search method via ListSearchDialog, and the standard
	 *         search method via ListSearchText! Do NOT return LISTPLUGIN_ERROR
	 *         if the search fails!
	 * 
	 * <BR>
	 * <B>Remarks:</B><BR>
	 * 
	 * The plugin needs to show the search dialog and highlight/select the found
	 * text by itself. Requires Total Commander 7 or later.
	 */
	int listSearchDialog(int listWin, int findNext);

}
