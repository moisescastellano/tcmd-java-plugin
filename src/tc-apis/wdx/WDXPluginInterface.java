package plugins.wdx;

import plugins.DefaultParam;

/**
 * This is the interface for Total Commander WDX plugins.
 * 
 * @author Ken
 */
public interface WDXPluginInterface {

	/**
	 * contentGetValue(): called in foreground.
	 */
	int CONTENT_DELAYIFSLOW = 1;

	/**
	 * contentGetSupportedFieldFlags(): The plugin allows to edit (modify) this
	 * field.
	 */
	int CONTFLAGS_EDIT = 1;

	/**
	 * contentGetSupportedFieldFlags(): Use the file size.
	 */
	int CONTFLAGS_SUBSTSIZE = 2;

	/**
	 * contentGetSupportedFieldFlags(): Use the file date+time
	 * (FieldValue.FT_DATETIME).
	 */
	int CONTFLAGS_SUBSTDATETIME = 4;

	/**
	 * contentGetSupportedFieldFlags(): Use the file date (FieldValue.FD_DATE).
	 */
	int CONTFLAGS_SUBSTDATE = 6;

	/**
	 * contentGetSupportedFieldFlags(): Use the file time (FieldValue.FD_TIME).
	 */
	int CONTFLAGS_SUBSTTIME = 8;

	/**
	 * contentGetSupportedFieldFlags(): Use the file attributes
	 * (FieldValue.NUMERIC_32 or FieldValue.NUMERIC_64).
	 */
	int CONTFLAGS_SUBSTATTRIBUTES = 10;

	/**
	 * contentGetSupportedFieldFlags(): Use the file attribute string in form:
	 * -a-- .
	 */
	int CONTFLAGS_SUBSTATTRIBUTESTR = 12;

	/**
	 * contentGetSupportedFieldFlags(): Pass the size as
	 * FieldValue.FT_NUMERIC_FLOATING to contentGetValue.
	 */
	int CONTFLAGS_PASSTHROUGH_SIZE_FLOAT = 14;

	/**
	 * contentGetSupportedFieldFlags(): TC will show a button in "change
	 * attributes". This allows plugins to have their own field editors.
	 */
	int CONTFLAGS_FIELDEDIT = 16;

	/**
	 * contentGetSupportedFieldFlags(): A combination of all above substitution
	 * flags.
	 */
	int CONTFLAGS_SUBSTMASK = CONTFLAGS_SUBSTSIZE | CONTFLAGS_SUBSTDATETIME
			| CONTFLAGS_SUBSTDATE | CONTFLAGS_SUBSTTIME
			| CONTFLAGS_SUBSTATTRIBUTES;

	/**
	 * contentSetValue(): First attribute of this file.
	 */
	int SETFLAGS_FIRST_ATTRIBUTE = 1;

	/**
	 * contentSetValue(): Last attribute of this file.
	 */
	int SETFLAGS_LAST_ATTRIBUTE = 2;

	/**
	 * contentSetValue(): Only set the date of the datetime value.
	 */
	int SETFLAGS_ONLY_DATE = 4;

	/**
	 * contentSendStateInformation(): TC reads one of the file lists.
	 */
	int CONTST_READNEWDIR = 1;

	/**
	 * contentSendStateInformation(): The user has pressed F2 or Ctrl+R to force
	 * a reload.
	 */
	int CONTST_REFRESHPRESSED = 2;

	/**
	 * contentSendStateInformation(): A tooltip/hint window is shown for the
	 * current file.
	 */
	int CONTST_SHOWHINT = 4;

	/**
	 * ContentGetSupportedField is called to enumerate all supported fields.
	 * FieldIndex is increased by 1 starting from 0 until the plugin returns
	 * FT_NOMOREFIELDS.
	 * 
	 * <BR>
	 * <B>Remarks:</B><BR>
	 * 
	 * Please note that fields of type FT_FULLTEXT only show up in the search
	 * function, not in the multi-rename tool or the file lists. All fields of
	 * this type MUST be placed at the END of the field list, otherwise you will
	 * get errors in Total Commander! This is necessary because these fields
	 * will be removed from field lists e.g. in the "configure custom column
	 * view" dialog. You should use the FT_STRING type for shorter one line
	 * texts suitable for displaying in file lists and for renaming.
	 * 
	 * @param fieldIndex
	 *            The index of the field for which TC requests information.
	 *            Starting with 0, the FieldIndex is increased until the plugin
	 *            returns an error.
	 * 
	 * @param fieldName
	 *            Here the plugin has to return the name of the field with index
	 *            FieldIndex. The field may not contain the following chars: .
	 *            (dot) | (vertical line) : (colon). You may return a maximum of
	 *            maxlen characters, including the trailing 0.
	 * 
	 * @param units
	 *            When a field supports several units like bytes, kbytes, Mbytes
	 *            etc, they need to be specified here in the following form:
	 *            bytes|kbytes|Mbytes . The separator is the vertical dash
	 *            (Alt+0124). As field names, unit names may not contain a
	 *            vertical dash, a dot, or a colon. You may return a maximum of
	 *            maxlen characters, including the trailing 0. If the field type
	 *            is FT_MULTIPLECHOICE, the plugin needs to return all possible
	 *            values here. Example: The field "File Type" of the built-in
	 *            content plugin can have the values "File", "Folder" and
	 *            "Reparse point". The available choices need to be returned in
	 *            the following form: File|Folder|Reparse point . The same
	 *            separator is used as for Units. You may return a maximum of
	 *            maxlen characters, including the trailing 0. The field type
	 *            FT_MULTIPLECHOICE does NOT support any units.
	 * @param maxlen
	 *            The maximum number of characters, including the trailing 0,
	 *            which may be returned in each of the fields.
	 * 
	 * @return The function needs to return one of the following values:
	 *         <UL>
	 *         <LI>FT_NOMOREFIELDS The FieldIndex is beyond the last available
	 *         field.
	 *         <LI>FT_NUMERIC_32 A 32-bit signed number
	 *         <LI>FT_NUMERIC_64 A 64-bit signed number, e.g. for file sizes
	 *         <LI>FT_NUMERIC_FLOATING A double precision floating point number
	 *         <LI>FT_DATE A date value (year, month, day)
	 *         <LI>FT_TIME A time value (hour, minute, second). Date and time
	 *         are in local time.
	 *         <LI>FT_BOOLEAN A true/false value
	 *         <LI>FT_MULTIPLECHOICE A value allowing a limited number of
	 *         choices. Use the Units field to return all possible values.
	 * 
	 * <LI>FT_STRING A text string
	 * <LI>FT_FULLTEXT A full text (multiple text strings), only used for
	 * searching. Can be used e.g. for searching in the text portion of binary
	 * files, where the plugin makes the necessary translations. All fields of
	 * this type MUST be placed at the END of the field list, otherwise you will
	 * get errors in Total Commander!
	 * <LI>FT_DATETIME A timestamp of type FILETIME, as returned e.g. by
	 * FindFirstFile(). It is a 64-bit value representing the number of
	 * 100-nanosecond intervals since January 1, 1601. The time MUST be relative
	 * to universal time (Greenwich mean time) as returned by the file system,
	 * not local time!
	 * </UL>
	 */
	int contentGetSupportedField(final int fieldIndex,
			final StringBuffer fieldName, final StringBuffer units,
			final int maxlen);

	/**
	 * ContentGetValue is called to retrieve the value of a specific field for a
	 * given file, e.g. the date field of a file. <BR>
	 * <B>Remarks:</B></BR>
	 * 
	 * FT_FULLTEXT handling is a bit special. It is only used for searching in
	 * interpreted file contents, e.g. for finding text in binary files. For
	 * example, the ID3 plugin uses FT_FULLTEXT to allow the user to search for
	 * a string in ALL header fields. Calls work like this: First,
	 * ContentGetValue is called with UnitIndex set to 0. The plugin then parses
	 * the file data, and (if necessary) keeps it in a cache. It writes the
	 * first block of maxlen-1 bytes to FieldValue and returns FT_FULLTEXT. The
	 * data written must be a 0-terminated string! Total Commander then searches
	 * in the block, and requests the next block with offset maxlen-1, etc. Once
	 * there is no more data, the plugin needs to retrun FT_FIELDEMPTY. If there
	 * is a match, TC signals the plugin that it can delete the cached data by
	 * calling ContentGetValue with UnitIndex set to -1! The return value should
	 * be FT_FIELDEMPTY in this case. This call with UnitIndex=-1 does not
	 * happen when the plugin terminates the search with FT_FIELDEMPTY because
	 * it reached the end of the file. <BR>
	 * Total Commander now accepts that ContentGetValue returns a different data
	 * type than ContentGetSupportedField for the same field, e.g. a string "no
	 * value" instead of a numeric field. Note that older versions of Total
	 * Commander crashed in the search function in this case, so if you want to
	 * do this, you MUST check that the plugin version is reported as >=1.3
	 * (hi=1, low>=3 or hi>=2). <BR>
	 * FT_NUMERIC_FLOATING (New with TC 6.52, plugin interface version >=1.4):
	 * You can now put a 0-terminated string immediately behind the 64bit
	 * floating point variable, which will then be shown instead in file lists.
	 * This is useful if the conversion precision used by TC isn't appropriate
	 * for your variables. The numeric variable will still be used for sorting
	 * and searching. If the string is empty, TC will ignore it (it is set to 0
	 * before calling this function, so the function will remain
	 * backwards-compatible). Example: The numeric value is 0.000002. You can
	 * return this value as a 64-bit variable, and the string you find most
	 * appropriate, e.g. "2*10^-6" or "0.000002". <BR>
	 * About caching the data: Total Commander will not call a mix
	 * ContentGetValue for different files, it will only call it for the next
	 * file when the previous file can be closed. Therefore a single cache per
	 * running Total Commander would be sufficient. However, there may be other
	 * calls to ContentGetValue with requests to other fields in the background,
	 * e.g. for displaying result lists. There may also be multiple instances of
	 * Total Commander at the same time, so if you use a TEMP file for storing
	 * the cached data, make sure to give it a unique name (e.g. via
	 * GetTempFileName).
	 * 
	 * @param fileName
	 *            The name of the file for which the plugin needs to return the
	 *            field data.
	 * 
	 * @param fieldIndex
	 *            The index of the field for which the content has to be
	 *            returned. This is the same index as the FieldIndex value in
	 *            ContentGetSupportedField.
	 * 
	 * @param unitIndex
	 *            The index of the unit used. <BR>
	 *            <B>Example:</B><BR>
	 *            If the plugin returned the following unit string in
	 *            ContentGetSupportedField: bytes|kbytes|Mbytes<BR>
	 *            Then a UnitIndex of 0 would mean bytes, 1 means kbytes and 2
	 *            means MBytes If no unit string was returned, UnitIndex is 0.
	 *            <BR>
	 *            For FT_FULLTEXT, UnitIndex contains the offset of the data to
	 *            be read.
	 * 
	 * @param fieldValue
	 *            Here the plugin needs to return the requested data. The data
	 *            format depends on the field type:
	 *            <UL>
	 *            <LI>FT_NUMERIC_32: FieldValue points to a 32-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_64: FieldValue points to a 64-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_FLOATING: FieldValue points to a 64-bit
	 *            floating point variable (ISO standard double precision) <BR>
	 *            See remark below about additional string field!
	 *            <LI>FT_DATE: FieldValue points to a structure containing
	 *            year,month,day as 2 byte values.
	 *            <LI>FT_TIME: FieldValue points to a structure containing
	 *            hour,minute,second as 2 byte values.
	 *            <LI>FT_BOOLEAN: FieldValue points to a 32-bit number. 0 neans
	 *            false, anything else means true.
	 *            <LI>FT_STRING or ft_multiplechoice: FieldValue is a pointer
	 *            to a 0-terminated string.
	 *            <LI>FT_FULLTEXT: Read maxlen bytes of interpreted data
	 *            starting at offset UnitIndex. The data must be a 0 terminated
	 *            string.
	 *            <LI>FT_DATETIME: A timestamp of type FILETIME, as returned
	 *            e.g. by FindFirstFile(). It is a 64-bit value representing the
	 *            number of 100-nanosecond intervals since January 1, 1601. The
	 *            time MUST be relative to universal time (Greenwich mean time)
	 *            as returned by the file system, not local time!
	 *            <LI>FT_DELAYED, FT_ONDEMAND: You may return a zero-terminated
	 *            string as in FT_STRING, which will be shown until the actual
	 *            value has been extracted. Requires plugin version>=1.4.
	 *            </UL>
	 * 
	 * @param maxlen
	 *            The maximum number of bytes fitting into the FieldValue
	 *            variable.
	 * 
	 * @param flags
	 *            Currently only one flag is defined:
	 *            <UL>
	 *            <LI>CONTENT_DELAYIFSLOW: If this flag is set, the plugin
	 *            should return FT_DELAYED for fields which take a long time to
	 *            extract, like file version information. Total Commander will
	 *            then call the function again in a background thread without
	 *            the CONTENT_DELAYIFSLOW flag. This means that your plugin must
	 *            be implemented thread-safe if you plan to return FT_DELAYED.
	 *            The plugin may also reutrn FT_ONDEMAND if CONTENT_DELAYIFSLOW
	 *            is set. In this case, the field will only be retrieved when
	 *            the user presses &lt;SPACEBAR&gt;. This is only recommended
	 *            for fields which take a VERY long time, e.g. directory content
	 *            size. You should offer the same field twice in this case, once
	 *            as delayed, and once as on demand. The field will be retrieved
	 *            in the background thread also in this case.
	 *            </UL>
	 * @return Return the field type in case of success, or one of the following
	 *         error values otherwise:
	 *         <UL>
	 *         <LI>FT_NOSUCHFIELD - The given FieldIndex is invalid
	 *         <LI>FT_FILEERROR - Error accessing the specified file FileName
	 *         <LI>FT_FIELDEMPTY - The file does not contain the specified
	 *         field
	 *         <LI>FT_DELAYED - The extraction of the field would take a long
	 *         time, so Total Commander should request it again in a background
	 *         thread. This error may only be returned if the flag
	 *         CONTENT_DELAYIFSLOW was set, and if the plugin is thread-safe.
	 * 
	 * <LI>FT_ONDEMAND - The extraction of the field would take a very long
	 * time, so it should only be retrieved when the user presses the space bar.
	 * This error may only be returned if the flag CONTENT_DELAYIFSLOW was set,
	 * and if the plugin is thread-safe.
	 * </UL>
	 */
	int contentGetValue(final String fileName, final int fieldIndex,
			final int unitIndex, final FieldValue fieldValue, final int maxlen,
			final int flags);

	/**
	 * ContentGetDetectString is called when the plugin is loaded for the first
	 * time. It should return a parse function which allows Total Commander to
	 * find out whether your plugin can probably handle the file or not. You can
	 * use this as a first test - more thorough tests may be performed in
	 * ContentGetValue(). It's very important to define a good test string,
	 * especially when there are dozens of plugins loaded! The test string
	 * allows Total Commander to call only those plugins relevant for that
	 * specific file type.<BR>
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
	String contentGetDetectString(int maxLen);

	/**
	 * ContentsSetDefaultParams is called immediately after loading the DLL.
	 * <BR>
	 * <B>Note:</B><BR>
	 * Since this is a new plugin interface, this function will be called in all
	 * versions of Total Commander supporting content plugins.
	 * 
	 * @param dps
	 *            This structure of type ContentDefaultParamStruct currently
	 *            contains the version number of the plugin interface, and the
	 *            suggested location for the settings file (ini file). It is
	 *            recommended to store any plugin-specific information either
	 *            directly in that file, or in that directory under a different
	 *            name. Make sure to use a unique header when storing data in
	 *            this file, because it is shared by other file system plugins!
	 *            If your plugin needs more than 1kbyte of data, you should use
	 *            your own ini file because ini files are limited to 64k.
	 */
	void contentSetDefaultParams(DefaultParam dps);

	/**
	 * ContentStopGetValue is called to tell a plugin that a directory change
	 * has occurred, and the plugin should stop loading a value. <BR>
	 * <B>Note:</B><BR>
	 * This function only needs to be implemented when handling very slow
	 * fields, e.g. the calculation of the total size of all files in a
	 * directory. It will be called only while a call to ContentGetValue is
	 * active in a background thread. <BR>
	 * A plugin could handle this mechanism like this:
	 * <OL>
	 * <LI>When ContentGetValue is called, set a variable GetAborted to false.
	 * <LI>When ContentStopGetValue is called, set GetAborted to true.
	 * <LI>Check GetAborted during the lengthy operation, and if it becomes
	 * true, return FT_FIELDEMPTY.
	 * </OL>
	 * 
	 * @param fileName
	 *            The name of the file for which ContentGetValue is currently
	 *            being called.
	 * 
	 */
	void contentStopGetValue(final String fileName);

	/**
	 * ContentGetDefaultSortOrder is called when the user clicks on the sorting
	 * header above the columns. <BR>
	 * <B>Note:</B><BR>
	 * You may implement this function if there are fields which are usually
	 * sorted in descending order, like the size field (largest file first) or
	 * the date/time fields (newest first). If the function isn't implemented,
	 * ascending will be the default.}
	 * 
	 * @param fieldIndex
	 *            The index of the field for which the sort order should be
	 *            returned.
	 * @return Return 1 for ascending (a..z, 1..9), or -1 for descending (z..a,
	 *         9..0).
	 */
	int contentGetDefaultSortOrder(int fieldIndex);

	/**
	 * ContentPluginUnloading is called just before the plugin is unloaded, e.g.
	 * to close buffers, abort operations etc. <BR>
	 * <B>Note:</B><BR>
	 * This function was added by request from a user who needs to unload GDI+.
	 * It seems that GDI+ has a bug which makes it crash when unloading it in
	 * the DLL unload function, therefore a separate unload function is needed.
	 */
	void contentPluginUnloading();

	/**
	 * ContentGetSupportedFieldFlags is called to get various information about
	 * a plugin variable. It's first called with fieldIndex=-1 to find out
	 * whether the plugin supports any special flags at all, and then for each
	 * field separately.
	 * 
	 * <BR>
	 * <B>Note:</B><BR>
	 * 
	 * Returning one of the CONTFLAGS_SUBST* flags instructs Total Commander to
	 * replace (substitute) the returned variable by the indicated default
	 * internal value if no plugin variable can be retrieved. Example: Content
	 * plugins do not work on FTP servers. A field which shows the size of files
	 * and directories should be replaced by the size of the FTP files in this
	 * case, so return CONTFLAGS_SUBSTSIZE. Alternatvely, you can also return
	 * CONTFLAGS_PASSTHROUGH_SIZE_FLOAT - then Total Commander will call
	 * contentGetValue and pass the size as FT_NUMERIC_FLOATING to your plugin,
	 * so you can format the display string yourself, and apply custom units.
	 * 
	 * Description of parameters:
	 * 
	 * @param fieldIndex
	 *            The index of the field for which the sort order should be
	 *            returned.
	 *            <UL>
	 *            <LI>-1: Return a combination (or) of all supported flags,
	 *            e.g. contflags_edit | contflags_substmask
	 *            <LI>>=0: Return the field-specific flags
	 *            </UL>
	 * @return The function needs to return a combination of the following
	 *         flags:
	 *         <UL>
	 * 
	 * <LI>CONTFLAGS_EDIT The plugin allows to edit (modify) this field via
	 * Files - Change attributes. This should only be returned for fields where
	 * it makes sense, e.g. a file date. <BR>
	 * Only ONE of the following flags: (See description and example under
	 * "Note").
	 * 
	 * <LI>CONTFLAGS_SUBSTSIZE use the file size
	 * <LI>CONTFLAGS_SUBSTDATETIME use the file date+time (ft_datetime)
	 * <LI>CONTFLAGS_SUBSTDATE use the file date (fd_date)
	 * <LI>CONTFLAGS_SUBSTTIME use the file time (fd_time)
	 * <LI>CONTFLAGS_SUBSTATTRIBUTES use the file attributes (numeric)
	 * <LI>CONTFLAGS_SUBSTATTRIBUTESTR use the file attribute string in form
	 * -a--
	 * 
	 * <LI>CONTFLAGS_PASSTHROUGH_SIZE_FLOAT pass the size as
	 * FT_NUMERIC_FLOATING to contentGetValue. The plugin will then apply the
	 * correct units, and return the formatted display string in the additional
	 * string field.
	 * 
	 * <LI>CONTFLAGS_SUBSTMASK A combination of all above substituion flags.
	 * Should be returned for index -1 if the content plugin contains ANY of the
	 * substituted fields.
	 * <LI>CONTFLAGS_FIELDEDIT If set, TC will show a button >> in change
	 * attributes which lets the user call the function ContentEditValue. This
	 * allows plugins to have their own field editors, like the custom editor
	 * for tc.comments or tc.*date/time fields.
	 * </UL>
	 */
	int contentGetSupportedFieldFlags(int fieldIndex);

	/**
	 * ContentSetValue is called to set the value of a specific field for a
	 * given file, e.g. to change the date field of a file.
	 * 
	 * @param fileName
	 *            The name of the file for which the plugin needs to change the
	 *            field data. This is set to NULL to indicate the end of change
	 *            attributes (see remarks below).
	 * 
	 * @param fieldIndex
	 *            The index of the field for which the content has to be
	 *            returned. This is the same index as the FieldIndex value in
	 *            ContentGetSupportedField. This is set to -1 to signal the end
	 *            of change attributes (see remarks below).
	 * 
	 * @param unitIndex
	 *            The index of the unit used. <BR>
	 *            Example: If the plugin returned the following unit string in
	 *            contentGetSupportedField: bytes|kbytes|Mbytes Then a unitIndex
	 *            of 0 would mean bytes, 1 means kbytes and 2 means MBytes If no
	 *            unit string was returned, UnitIndex is 0. FT_FULLTEXT is
	 *            currently unsupported.
	 * 
	 * @param fieldType
	 *            The type of data passed to the plugin in FieldValue. This is
	 *            the same type as returned by the plugin via
	 *            ContentGetSupportedField. If the plugin returned a different
	 *            type via ContentGetValue, the the FieldType _may_ be of that
	 *            type too.
	 * 
	 * @param fieldValue
	 *            Here the plugin receives the data to be changed. The data
	 *            format depends on the field type:
	 *            <UL>
	 *            <LI>FT_NUMERIC_32: FieldValue points to a 32-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_64: FieldValue points to a 64-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_FLOATING: FieldValue points to a 64-bit
	 *            floating point variable (ISO standard double precision)
	 *            <LI>FT_DATE: FieldValue points to a structure containing
	 *            year,month,day as 2 byte values.
	 *            <LI>FT_TIME: FieldValue points to a structure containing
	 *            hour,minute,second as 2 byte values.
	 *            <LI>FT_BOOLEAN: FieldValue points to a 32-bit number. 0 neans
	 *            false, anything else means true.
	 *            <LI>FT_STRING or ft_multiplechoice: FieldValue is a pointer
	 *            to a 0-terminated string.
	 *            <LI>FT_FULLTEXT: Currently unsupported.
	 *            <LI>FT_DATETIME: A timestamp of type FILETIME, as returned
	 *            e.g. by FindFirstFile(). It is a 64-bit value representing the
	 *            number of 100-nanosecond intervals since January 1, 1601. The
	 *            time MUST be relative to universal time (Greenwich mean time)
	 *            as returned by the file system, not local time!
	 *            <LI>FT_DELAYED, FT_ONDEMAND: You may return a zero-terminated
	 *            string as in ft_string, which will be shown until the actual
	 *            value has been extracted. Requires plugin version>=1.4.
	 *            </UL>
	 * @param flags
	 *            Currently the following flags are defined:
	 *            <UL>
	 *            <LI>SETFLAGS_FIRST_ATTRIBUTE: This is the first attribute to
	 *            be set for this file via this plugin. May be used for
	 *            optimization.
	 *            <LI>SETFLAGS_LAST_ATTRIBUTE: This is the last attribute to be
	 *            set for this file via this plugin.
	 *            <LI>SETFLAGS_ONLY_DATE: For field type FT_DATETIME only: User
	 *            has only entered a date, don't change the time
	 *            </UL>
	 * @return
	 *            <UL>
	 * 
	 * <LI>FT_SETSUCCESS Change was successful
	 * <LI>FT_FILEERROR Error accessing the specified file FileName, or cannot
	 * set the given value
	 * 
	 * <LI>FT_NOSUCHFIELD The given field index was invalid
	 * </UL>
	 * 
	 * <BR>
	 * <B>Note:</B><BR>
	 * 
	 * <B>About caching the data:</B> Total Commander will not call a mix
	 * contentSetValue for different files, it will only call it for the next
	 * file when the previous file can be closed. Therefore a single cache per
	 * running Total Commander should be sufficient. <BR>
	 * <B>About the flags:</B> If the flags SETFLAGS_FIRST_ATTRIBUTE and
	 * SETFLAGS_LAST_ATTRIBUTE are both set, then this is the only attribute of
	 * this plugin which is changed for this file. <BR>
	 * FileName is set to NULL and FieldIndex to -1 to signal to the plugin that
	 * the change attributes operation has ended. This can be used to flush
	 * unsaved data to disk, e.g. when setting comments for multiple files.
	 */
	int contentSetValue(String fileName, int fieldIndex, int unitIndex,
			int fieldType, FieldValue fieldValue, int flags);

	/**
	 * ContentSendStateInformation is called to inform the plugin about a state
	 * change.
	 * 
	 * @param state
	 *            The state which has changed. The following states are defined:
	 *            <UL>
	 *            <LI>CONTST_READNEWDIR: It is called when TC reads one of the
	 *            file lists.
	 *            <LI>CONTST_REFRESHPRESSED: The user has pressed F2 or Ctrl+R
	 *            to force a reload.
	 *            <LI>CONTSTR_SHOWHINT: A tooltip/hint window is shown for the
	 *            current file.
	 *            </UL>
	 * @param path
	 *            Current path. In case of CONTSTR_SHOWHINT, this is the path to
	 *            the file, otherwise to the current directory.
	 * 
	 * <BR>
	 * <B>Note:</B><BR>
	 * <UL>
	 * <LI>This function may be used to clear a directory cache when called
	 * with parameter CONTST_REFRESHPRESSED or with CONTST_READNEWDIR, depending
	 * on the needs of the plugin.
	 * <LI> When the user presses F2 or Ctrl+R in custom columns view or
	 * thumbnails view, contentSendStateInformation is called first with
	 * parameter CONTST_REFRESHPRESSED, then with CONTST_READNEWDIR.
	 * <LI> When the user changes to a different directory in custom columns
	 * view or thumbnails view, contentSendStateInformation is called only with
	 * parameter CONTST_READNEWDIR.
	 * 
	 * <LI> When the user switches from full view to custom columns view or
	 * thumbnails view, contentSendStateInformation is not called at all!
	 * <LI> Do not ignore the state parameter, there may be more parameters
	 * added in future versions!
	 * </UL>
	 */
	void contentSendStateInformation(int state, String path);

	/***************************************************************************
	 * ContentEditValue allows a plugin to implement a custom input dialog to
	 * enter special values like date and time. This function is called in
	 * change attributes if you returned the flag CONTFLAGS_FIELDEDIT in
	 * contentGetSupportedFieldFlags for a field.
	 * 
	 * @param parentWin
	 *            The parent window handle for the dialog.
	 * 
	 * @param fieldIndex
	 *            The index of the field for which the content editor is called.
	 *            This is the same index as the fieldIndex value in
	 *            contentGetSupportedField.
	 * 
	 * @param unitIndex
	 *            The index of the unit used. <BR>
	 *            Example: If the plugin returned the following unit string in
	 *            contentGetSupportedField: bytes|kbytes|Mbytes Then a UnitIndex
	 *            of 0 would mean bytes, 1 means kbytes and 2 means MBytes If no
	 *            unit string was returned, UnitIndex is 0.
	 * 
	 * @param fieldType
	 *            The type of data passed to the plugin in fieldValue. This is
	 *            the same type as returned by the plugin via
	 *            contentGetSupportedField.
	 * 
	 * @param fieldValue
	 *            Here the plugin receives the data to be edited, and returns
	 *            the result back to the caller. The data format depends on the
	 *            field type:
	 *            <UL>
	 *            <LI>FT_NUMERIC_32: FieldValue points to a 32-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_64: FieldValue points to a 64-bit signed
	 *            integer variable.
	 *            <LI>FT_NUMERIC_FLOATING: FieldValue points to a 64-bit
	 *            floating point variable (ISO standard double precision)
	 *            <LI>FT_DATE: FieldValue points to a structure containing
	 *            year,month,day as 2 byte values.
	 *            <LI>FT_TIME: FieldValue points to a structure containing
	 *            hour,minute,second as 2 byte values.
	 *            <LI>FT_BOOLEAN: Currently unsupported.
	 *            <LI>FT_STRING: FieldValue is a pointer to a 0-terminated
	 *            string.
	 *            <LI>FT_MULTIPLECHOICE: Currently unsupported.
	 *            <LI>FT_FULLTEXT: Currently unsupported.
	 *            <LI>FT_DATETIME: A timestamp of type FILETIME, as returned
	 *            e.g. by FindFirstFile(). It is a 64-bit value representing the
	 *            number of 100-nanosecond intervals since January 1, 1601. The
	 *            time MUST be relative to universal time (Greenwich mean time)
	 *            as returned by the file system, not local time!
	 *            </UL>
	 * @param maxlen
	 *            The maximum number of bytes fitting into the fieldValue
	 *            variable.
	 * 
	 * @param flags
	 *            Currently the following flags are defined:
	 *            <UL>
	 *            <LI>EDITFLAGS_INITIALIZE: The data passed in via fieldValue
	 *            should be used to initialize the dialog.
	 *            </UL>
	 * @param langidentifier
	 *            A 1-3 character language identifier, the same as the last part
	 *            of the language file name used. <BR>
	 *            Example: The German language file is called wcmd_deu.lng, so
	 *            the langidentifier is "deu". May be used to translate the
	 *            dialog.
	 * 
	 * @return
	 * <UL>
	 * <LI>FT_SETSUCCESS User confirmed dialog with OK, and the data is valid
	 * <LI>FT_NOSUCHFIELD The given field index was invalid
	 * <LI>FT_SETCANCEL The user clicked on cancel
	 * </UL>
	 * <BR>
	 * <B>Note:</B><BR>
	 * Total Commander already implements an internal editor for the fields
	 * FT_DATE, FT_TIME, AND FT_DATETIME. You can override it with your own by
	 * specifying the contflags_fieldedit flag for your date/time fields. Fields
	 * of type boolean and multiple choice do not currently support a field
	 * editor.
	 **************************************************************************/
	int contentEditValue(int parentWin, int fieldIndex, int unitIndex,
			int fieldType, FieldValue fieldValue, int maxlen, int flags,
			String langidentifier);

}
