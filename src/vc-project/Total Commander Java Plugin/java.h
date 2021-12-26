#ifdef JAVA
#  define EXTERN
#else
#  define EXTERN extern
#endif

// Plugin Configuration Path (global path and plugin path)
#define GLB_PLUGIN_INI "%COMMANDER_PATH%/javalib/tc_javaplugin.ini"
#define PLUGIN_INI "./tc_javaplugin.ini"
// Plugin error messages Path
#define ERROR_INI "./errormessages.ini"
// maximum stacktrace length of error message box
#define MAX_STACKTRACE 2048

// Registry infos about installed Java Runtime Environment
#define REG_PATH_JRE "SOFTWARE\\JavaSoft\\Java Runtime Environment"
#define REG_KEY_JRE_VERSION "CurrentVersion"
#define REG_KEY_JRE_RTLIB "RuntimeLib"
#define REG_KEY_JRE_HOME "JavaHome"

// maximum error message length
#define MAX_MESSAGE 512
// language independant error message keys
#define TLE_ERROR "TLE_ERROR"
#define ERR_PLUGIN_NOT_FOUND "ERR_PLUGIN_NOT_FOUND"
#define ERR_JRE_NOT_FOUND "ERR_JRE_NOT_FOUND"
#define ERR_RUNTIME_NOT_FOUND "ERR_RUNTIME_NOT_FOUND"
#define ERR_PROC_UNDEFINED "ERR_PROC_UNDEFINED"
#define ERR_JVM_START_FAILED "ERR_JVM_START_FAILED"
#define ERR_CLASS_NOT_FOUND "ERR_CLASS_NOT_FOUND"
#define ERR_METHOD_NOT_FOUND "ERR_METHOD_NOT_FOUND"
#define ERR_FIELD_NOT_FOUND "ERR_FIELD_NOT_FOUND"
#define ERR_CLASS_INIT_FAILED "ERR_CLASS_INIT_FAILED"
#define ERR_EXCEPTION "ERR_EXCEPTION"

// JVM library function to create Java Virtual Machine
#define PROC_CREATE_JVM "JNI_CreateJavaVM"

// JVM library function to create Java Virtual Machine
#define PROC_GETCREATED_JVM "JNI_GetCreatedJavaVMs"

// Throwable methods
#define CLASS_THROWABLE "java/lang/Throwable"
#define METHOD_THROWABLE_PRINTSTACKTRACE "printStackTrace"
#define METHOD_THROWABLE_PRINTSTACKTRACE_SIGNATURE "(Ljava/io/PrintWriter;)V"

// common constructor signature
#define CLASS_INIT "<init>"
#define CLASS_INIT_SIGNATURE "()V"

// class RemoteInfo
#define CLASS_REMOTEINFO "plugins.wfx.RemoteInfo"
#define CLASS_REMOTEINFO_INIT_SIGNATURE "(JJLplugins/FileTime;I)V"

// class Rectangle
#define CLASS_RECTANGLE "java/awt/Rectangle"
#define CLASS_RECTANGLE_INIT_SIGNATURE "(IIII)V"

// class String
#define CLASS_STRING "java/lang/String"
#define METHOD_STRING_GETBYTES "getBytes"
#define METHOD_STRING_GETBYTES_SIGNATURE "()[B"

// class CharArrayWriter
#define CLASS_CHARARRAYWRITER "java/io/CharArrayWriter"
#define METHOD_CHARARRAYWRITER_TOSTRING "toString"
#define METHOD_CHARARRAYWRITER_TOSTRING_SIGNATURE "()Ljava/lang/String;"

// class PrintWriter
#define CLASS_PRINTWRITER "java/io/PrintWriter"
#define CLASS_PRINTWRITER_INIT_SIGNATURE "(Ljava/io/Writer;)V"

// class StringBuffer
#define CLASS_STRINGBUFFER "java/lang/StringBuffer"
#define METHOD_STRINGBUFFER_TOSTRING "toString"
#define METHOD_STRINGBUFFER_TOSTRING_SIGNATURE "()Ljava/lang/String;"
#define METHOD_STRINGBUFFER_APPEND "append"
#define METHOD_STRINGBUFFER_APPEND_SIGNATURE "(Ljava/lang/String;)Ljava/lang/StringBuffer;"

// class MemoryClassLoader
#define CLASS_PLUGINCLASSLOADER "tcclassloader/PluginClassLoader"
#define METHOD_PLUGINCLASSLOADER_GETPLUGIN "getPlugin"
#define METHOD_PLUGINCLASSLOADER_GETPLUGIN_SIGNATURE "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;"
#define METHOD_PLUGINCLASSLOADER_GETPLUGINCLASSLOADER "getPluginClassLoader"
#define METHOD_PLUGINCLASSLOADER_GETPLUGINCLASSLOADER_SIGNATURE "(Ljava/lang/String;)Ltcclassloader/PluginClassLoader;"
#define METHOD_PLUGINCLASSLOADER_LOADCLASS "loadClass"
#define METHOD_PLUGINCLASSLOADER_LOADCLASS_SIGNATURE "(Ljava/lang/String;)Ljava/lang/Class;"

// class DefaultParam
#define CLASS_DEFAULTPARAM "plugins.DefaultParam"
#define CLASS_DEFAULTPARAM_INIT_SIGNATURE "(IJJLjava/lang/String;)V"

// class FileTime
#define CLASS_FILETIME "plugins.FileTime"
#define CLASS_FILETIME_INIT_SIGNATURE "(JJ)V"

// class Win32FindData
#define CLASS_WIN32FINDDATA "plugins.wfx.Win32FindData"
#define FIELD_DWFILEATTRIBUTES "dwFileAttributes"
#define FIELD_DWFILEATTRIBUTES_SIGNATURE "J"
#define FIELD_FILETIME_SIGNATURE "Lplugins/FileTime;"
#define FIELD_FTLASTWRITETIME "ftLastWriteTime"
#define FIELD_FTCREATIONTIME "ftCreationTime"
#define FIELD_FTLASTACCESSTIME "ftLastAccessTime"
#define FIELD_DWLOWDATETIME "dwLowDateTime"
#define FIELD_DWLOWDATETIME_SIGNATURE "J"
#define FIELD_NFILESIZELOW "nFileSizeLow"
#define FIELD_NFILESIZELOW_SIGNATURE "J"
#define FIELD_NFILESIZEHIGH "nFileSizeHigh"
#define FIELD_NFILESIZEHIGH_SIGNATURE "J"
#define FIELD_DWRESERVED0 "dwReserved0"
#define FIELD_DWRESERVED0_SIGNATURE "J"
#define FIELD_DWRESERVED1 "dwReserved1"
#define FIELD_DWRESERVED1_SIGNATURE "J"
#define FIELD_CFILENAME "cFileName"
#define FIELD_CFILENAME_SIGNATURE "Ljava/lang/String;"
#define FIELD_CALTERNATEFILENAME "cAlternateFileName"
#define FIELD_CALTERNATEFILENAME_SIGNATURE "Ljava/lang/String;"
#define FIELD_LASTERRORMESSAGE "lastErrorMessage"
#define FIELD_LASTERRORMESSAGE_SIGNATURE "J"

// class FieldValue
#define CLASS_FIELDVALUE "plugins.wdx.FieldValue"
#define FIELD_FIELDTYPE "fieldType"
#define FIELD_FIELDTYPE_SIGNATURE "I"
#define FIELD_INTVALUE "intValue"
#define FIELD_INTVALUE_SIGNATURE "I"
#define FIELD_LONGVALUE "longValue"
#define FIELD_LONGVALUE_SIGNATURE "J"
#define FIELD_DOUBLEVALUE "doubleValue"
#define FIELD_DOUBLEVALUE_SIGNATURE "D"
#define FIELD_BOOLEANVALUE "boolValue"
#define FIELD_BOOLEANVALUE_SIGNATURE "Z"
#define FIELD_STR "str"
#define FIELD_STR_SIGNATURE "Ljava/lang/String;"
#define FIELD_DATEVALUE "date"
#define FIELD_DATEVALUE_SIGNATURE "Lplugins/wdx/LocalDate;"
#define FIELD_TIMEVALUE "time"
#define FIELD_TIMEVALUE_SIGNATURE "Lplugins/wdx/LocalTime;"
#define FIELD_STR "str"
#define FIELD_STR_SIGNATURE "Ljava/lang/String;"
#define FIELD_FILETIME "fileTime"
#define FIELD_FILETIME_SIGNATURE "Lplugins/FileTime;"

// class LocalDate
#define CLASS_LOCALDATE "plugins.wdx.LocalDate"
#define FIELD_YEAR "fYear"
#define FIELD_YEAR_SIGNATURE "I"
#define FIELD_MONTH "fMonth"
#define FIELD_MONTH_SIGNATURE "I"
#define FIELD_DAY "fDay"
#define FIELD_DAY_SIGNATURE "I"

// class LocalTime
#define CLASS_LOCALTIME "plugins.wdx.LocalTime"
#define FIELD_HOUR "fHour"
#define FIELD_HOUR_SIGNATURE "I"
#define FIELD_MINUTE "fMinute"
#define FIELD_MINUTE_SIGNATURE "I"
#define FIELD_SECOND "fSecond"
#define FIELD_SECOND_SIGNATURE "I"

// class OpenArchiveData
#define CLASS_OPENARCHIVEDATA "plugins.wcx.OpenArchiveData"
#define CLASS_OPENARCHIVEDATA_INIT_SIGNATURE "(Ljava/lang/String;II)V"
#define FIELD_OPENRESULT "fOpenResult"
#define FIELD_OPENRESULT_SIGNATURE "I"

// class HeaderData
#define CLASS_HEADERDATA "plugins.wcx.HeaderData"
#define CLASS_HEADERDATA_INIT_SIGNATURE "()V"
#define CLASS_HEADERDATAEX "plugins.wcx.HeaderDataEx"
#define CLASS_HEADERDATAEX_INIT_SIGNATURE "()V"
#define FIELD_FARCNAME "fArcName"
#define FIELD_FARCNAME_SIGNATURE "Ljava/lang/String;"
#define FIELD_FFILENAME "fFileName"
#define FIELD_FFILENAME_SIGNATURE "Ljava/lang/String;"
#define FIELD_FFLAGS "fFlags"
#define FIELD_FFLAGS_SIGNATURE "I"
#define FIELD_FPACKSIZE "fPackSize"
#define FIELD_FPACKSIZE_SIGNATURE "J"
#define FIELD_FUNPSIZE "fUnpSize"
#define FIELD_FUNPSIZE_SIGNATURE "J"
#define FIELD_FHOSTOS "fHostOs"
#define FIELD_FHOSTOS_SIGNATURE "I"
#define FIELD_FFILECRC "fFileCRC"
#define FIELD_FFILECRC_SIGNATURE "J"
#define FIELD_FFILETIME "fFileTime"
#define FIELD_FFILETIME_SIGNATURE "J"
#define FIELD_FUNPVER "fUnpVer"
#define FIELD_FUNPVER_SIGNATURE "I"
#define FIELD_FMETHOD "fMethod"
#define FIELD_FMETHOD_SIGNATURE "I"
#define FIELD_FFILEATTR "fFileAttr"
#define FIELD_FFILEATTR_SIGNATURE "I"

// class PackToMem
#define CLASS_PACKTOMEM "plugins.wcx.PackToMem"
#define CLASS_PACKTOMEM_INIT_SIGNATURE "()V"
#define FIELD_FTAKEN "fTaken"
#define FIELD_FTAKEN_SIGNATURE "I"
#define FIELD_FWRITTEN "fWritten"
#define FIELD_FWRITTEN_SIGNATURE "I"

#define METHOD_WLXPLUGIN_LISTLOAD "listLoad"
#define METHOD_WLXPLUGIN_LISTLOAD_SIGNATURE "(ILjava/lang/String;I)I"
#define METHOD_WLXPLUGIN_LISTLOADNEXT "listLoadNext"
#define METHOD_WLXPLUGIN_LISTLOADNEXT_SIGNATURE "(IILjava/lang/String;I)I"
#define METHOD_WLXPLUGIN_LISTCLOSEWINDOW "listCloseWindow"
#define METHOD_WLXPLUGIN_LISTCLOSEWINDOW_SIGNATURE "(I)V"
#define METHOD_WLXPLUGIN_LISTGETDETECTSTRING "listGetDetectString"
#define METHOD_WLXPLUGIN_LISTGETDETECTSTRING_SIGNATURE "(I)Ljava/lang/String;"
#define METHOD_WLXPLUGIN_LISTSEARCHDIALOG "listSearchDialog"
#define METHOD_WLXPLUGIN_LISTSEARCHDIALOG_SIGNATURE "(II)I"
#define METHOD_WLXPLUGIN_LISTSEARCHTEXT "listSearchText"
#define METHOD_WLXPLUGIN_LISTSEARCHTEXT_SIGNATURE "(ILjava/lang/String;I)I"
#define METHOD_WLXPLUGIN_LISTSENDCOMMAND "listSendCommand"
#define METHOD_WLXPLUGIN_LISTSENDCOMMAND_SIGNATURE "(III)I"
#define METHOD_WLXPLUGIN_LISTPRINT "listPrint"
#define METHOD_WLXPLUGIN_LISTPRINT_SIGNATURE "(ILjava/lang/String;Ljava/lang/String;ILjava/awt/Rectangle;)I"
#define METHOD_WLXPLUGIN_LISTNOTIFICATIONRECEIVED "listNotificationReceived"
#define METHOD_WLXPLUGIN_LISTNOTIFICATIONRECEIVED_SIGNATURE "(IIII)I"
#define METHOD_WLXPLUGIN_LISTGETPREVIEWBITMAP "listGetPreviewBitmap"
#define METHOD_WLXPLUGIN_LISTGETPREVIEWBITMAP_SIGNATURE "(Ljava/lang/String;IILjava/lang/String;ILjava/lang/StringBuffer;)Ljava/lang/Object;"
#define METHOD_WLXPLUGIN_LISTDEFAULTGETPARAMS "listDefaultGetParams"
#define METHOD_WLXPLUGIN_LISTDEFAULTGETPARAMS_SIGNATURE "(Lplugins/DefaultParam;)V"

#define METHOD_WFXPLUGIN_FSINIT "fsInit"
#define METHOD_WFXPLUGIN_FSINIT_SIGNATURE "(I)I"
#define METHOD_WFXPLUGIN_FSFINDFIRST "fsFindFirst"
#define METHOD_WFXPLUGIN_FSFINDFIRST_SIGNATURE "(Ljava/lang/String;Lplugins/wfx/Win32FindData;)Ljava/lang/Object;"
#define METHOD_WFXPLUGIN_FSFINDNEXT "fsFindNext"
#define METHOD_WFXPLUGIN_FSFINDNEXT_SIGNATURE "(Ljava/lang/Object;Lplugins/wfx/Win32FindData;)Z"
#define METHOD_WFXPLUGIN_FSFINDCLOSE "fsFindClose"
#define METHOD_WFXPLUGIN_FSFINDCLOSE_SIGNATURE "(Ljava/lang/Object;)I"
#define METHOD_WFXPLUGIN_FSGETDEFROOTNAME "fsGetDefRootName"
#define METHOD_WFXPLUGIN_FSGETDEFROOTNAME_SIGNATURE "(I)Ljava/lang/String;"
#define METHOD_WFXPLUGIN_FSGETFILE "fsGetFile"
#define METHOD_WFXPLUGIN_FSGETFILE_SIGNATURE "(Ljava/lang/String;Ljava/lang/String;ILplugins/wfx/RemoteInfo;)I"
#define METHOD_WFXPLUGIN_FSPUTFILE "fsPutFile"
#define METHOD_WFXPLUGIN_FSPUTFILE_SIGNATURE "(Ljava/lang/String;Ljava/lang/String;I)I"
#define METHOD_WFXPLUGIN_FSRENMOVFILE "fsRenMovFile"
#define METHOD_WFXPLUGIN_FSRENMOVFILE_SIGNATURE "(Ljava/lang/String;Ljava/lang/String;ZZLplugins/wfx/RemoteInfo;)I"
#define METHOD_WFXPLUGIN_FSDELETEFILE "fsDeleteFile"
#define METHOD_WFXPLUGIN_FSDELETEFILE_SIGNATURE "(Ljava/lang/String;)Z"
#define METHOD_WFXPLUGIN_FSREMOVEDIR "fsRemoveDir"
#define METHOD_WFXPLUGIN_FSREMOVEDIR_SIGNATURE "(Ljava/lang/String;)Z"
#define METHOD_WFXPLUGIN_FSMKDIR "fsMkDir"
#define METHOD_WFXPLUGIN_FSMKDIR_SIGNATURE "(Ljava/lang/String;)Z"
#define METHOD_WFXPLUGIN_FSEXECUTEFILE "fsExecuteFile"
#define METHOD_WFXPLUGIN_FSEXECUTEFILE_SIGNATURE "(ILjava/lang/String;Ljava/lang/String;)I"
#define METHOD_WFXPLUGIN_FSSETATTR "fsSetAttr"
#define METHOD_WFXPLUGIN_FSSETATTR_SIGNATURE "(Ljava/lang/String;I)Z"
#define METHOD_WFXPLUGIN_FSSETTIME "fsSetTime"
#define METHOD_WFXPLUGIN_FSSETTIME_SIGNATURE "(Ljava/lang/String;Lplugins/FileTime;Lplugins/FileTime;Lplugins/FileTime;)Z"
#define METHOD_WFXPLUGIN_FSDISCONNECT "fsDisconnect"
#define METHOD_WFXPLUGIN_FSDISCONNECT_SIGNATURE "(Ljava/lang/String;)Z"
#define METHOD_WFXPLUGIN_FSSTATUSINFO "fsStatusInfo"
#define METHOD_WFXPLUGIN_FSSTATUSINFO_SIGNATURE "(Ljava/lang/String;II)V"
#define METHOD_WFXPLUGIN_FSEXTRACTCUSTOMICON "fsExtractCustomIcon"
#define METHOD_WFXPLUGIN_FSEXTRACTCUSTOMICON_SIGNATURE "(Ljava/lang/String;ILjava/lang/StringBuffer;)I"
#define METHOD_WFXPLUGIN_FSSETDEFAULTPARAMS "fsSetDefaultParams"
#define METHOD_WFXPLUGIN_FSSETDEFAULTPARAMS_SIGNATURE "(Lplugins/DefaultParam;)V"
#define METHOD_WFXPLUGIN_FSGETPREVIEWBITMAP "fsGetPreviewBitmap"
#define METHOD_WFXPLUGIN_FSGETPREVIEWBITMAP_SIGNATURE "(Ljava/lang/String;IILjava/lang/StringBuffer;)I"
#define METHOD_WFXPLUGIN_FSLINKSTOLOCALFILES "fsLinksToLocalFiles"
#define METHOD_WFXPLUGIN_FSLINKSTOLOCALFILES_SIGNATURE "()Z"
#define METHOD_WFXPLUGIN_FSGETLOCALNAME "fsGetLocalName"
#define METHOD_WFXPLUGIN_FSGETLOCALNAME_SIGNATURE "(Ljava/lang/String;I)Z"
#define METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTFIELD "fsContentGetSupportedField"
#define METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTFIELD_SIGNATURE "(ILjava/lang/StringBuffer;Ljava/lang/StringBuffer;I)I"
#define METHOD_WFXPLUGIN_FSCONTENTGETVALUE "fsContentGetValue"
#define METHOD_WFXPLUGIN_FSCONTENTGETVALUE_SIGNATURE "(Ljava/lang/String;IILplugins/wdx/FieldValue;II)I"
#define METHOD_WFXPLUGIN_FSCONTENTSTOPGETVALUE "fsContentStopGetValue"
#define METHOD_WFXPLUGIN_FSCONTENTSTOPGETVALUE_SIGNATURE "(Ljava/lang/String;)V"
#define METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTSORTORDER "fsContentGetDefaultSortOrder"
#define METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTSORTORDER_SIGNATURE "(I)I"
#define METHOD_WFXPLUGIN_FSCONTENTPLUGINUNLOADING "fsContentPluginUnloading"
#define METHOD_WFXPLUGIN_FSCONTENTPLUGINUNLOADING_SIGNATURE "()V"
#define METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTEDFIELDFLAGS "fsContentGetSupportedFieldFlags"
#define METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTEDFIELDFLAGS_SIGNATURE "(I)I"
#define METHOD_WFXPLUGIN_FSCONTENTSETVALUE "fsContentSetValue"
#define METHOD_WFXPLUGIN_FSCONTENTSETVALUE_SIGNATURE "(Ljava/lang/String;IIILplugins/wdx/FieldValue;I)I"
#define METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTVIEW "fsContentGetDefaultView"
#define METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTVIEW_SIGNATURE "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)Z"

#define METHOD_WDXPLUGIN_CONTENTGETSUPPORTFIELD "contentGetSupportedField"
#define METHOD_WDXPLUGIN_CONTENTGETSUPPORTFIELD_SIGNATURE "(ILjava/lang/StringBuffer;Ljava/lang/StringBuffer;I)I"
#define METHOD_WDXPLUGIN_CONTENTGETVALUE "contentGetValue"
#define METHOD_WDXPLUGIN_CONTENTGETVALUE_SIGNATURE "(Ljava/lang/String;IILplugins/wdx/FieldValue;II)I"
#define METHOD_WDXPLUGIN_CONTENTGETDETECTSTRING "contentGetDetectString"
#define METHOD_WDXPLUGIN_CONTENTGETDETECTSTRING_SIGNATURE "(I)Ljava/lang/String;"
#define METHOD_WDXPLUGIN_CONTENTDEFAULTGETPARAMS "contentSetDefaultParams"
#define METHOD_WDXPLUGIN_CONTENTDEFAULTGETPARAMS_SIGNATURE "(Lplugins/DefaultParam;)V"
#define METHOD_WDXPLUGIN_CONTENTSTOPGETVALUE "contentStopGetValue"
#define METHOD_WDXPLUGIN_CONTENTSTOPGETVALUE_SIGNATURE "(Ljava/lang/String;)V"
#define METHOD_WDXPLUGIN_CONTENTGETDEFAULTSORTORDER "contentGetDefaultSortOrder"
#define METHOD_WDXPLUGIN_CONTENTGETDEFAULTSORTORDER_SIGNATURE "(I)I"
#define METHOD_WDXPLUGIN_CONTENTPLUGINUNLOADING "contentPluginUnloading"
#define METHOD_WDXPLUGIN_CONTENTPLUGINUNLOADING_SIGNATURE "()V"
#define METHOD_WDXPLUGIN_CONTENTGETSUPPORTEDFIELDFLAGS "contentGetSupportedFieldFlags"
#define METHOD_WDXPLUGIN_CONTENTGETSUPPORTEDFIELDFLAGS_SIGNATURE "(I)I"
#define METHOD_WDXPLUGIN_CONTENTSETVALUE "contentSetValue"
#define METHOD_WDXPLUGIN_CONTENTSETVALUE_SIGNATURE "(Ljava/lang/String;IIILplugins/wdx/FieldValue;I)I"
#define METHOD_WDXPLUGIN_CONTENTEDITVALUE "contentEditValue"
#define METHOD_WDXPLUGIN_CONTENTEDITVALUE_SIGNATURE "(IIIILplugins/wdx/FieldValue;IILjava/lang/String;)I"
#define METHOD_WDXPLUGIN_CONTENTSENDSTATEINFORMATION "contentSendStateInformation"
#define METHOD_WDXPLUGIN_CONTENTSENDSTATEINFORMATION_SIGNATURE "(ILjava/lang/String;)V"

#define METHOD_WCXPLUGIN_PACKSETDEFAULTPARAMS "packSetDefaultParams"
#define METHOD_WCXPLUGIN_PACKSETDEFAULTPARAMS_SIGNATURE "(Lplugins/DefaultParam;)V"
#define METHOD_WCXPLUGIN_OPENARCHIVE "openArchive"
#define METHOD_WCXPLUGIN_OPENARCHIVE_SIGNATURE "(Lplugins/wcx/OpenArchiveData;)Ljava/lang/Object;"
#define METHOD_WCXPLUGIN_READHEADER "readHeader"
#define METHOD_WCXPLUGIN_READHEADER_SIGNATURE "(Ljava/lang/Object;Lplugins/wcx/HeaderData;)I"
#define METHOD_WCXPLUGIN_READHEADEREX "readHeaderEx"
#define METHOD_WCXPLUGIN_READHEADEREX_SIGNATURE "(Ljava/lang/Object;Lplugins/wcx/HeaderDataEx;)I"
#define METHOD_WCXPLUGIN_PROCESSFILE "processFile"
#define METHOD_WCXPLUGIN_PROCESSFILE_SIGNATURE "(Ljava/lang/Object;ILjava/lang/String;Ljava/lang/String;)I"
#define METHOD_WCXPLUGIN_CLOSEARCHIVE "closeArchive"
#define METHOD_WCXPLUGIN_CLOSEARCHIVE_SIGNATURE "(Ljava/lang/Object;)I"
#define METHOD_WCXPLUGIN_PACKFILES "packFiles"
#define METHOD_WCXPLUGIN_PACKFILES_SIGNATURE "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)I"
#define METHOD_WCXPLUGIN_DELETEFILES "deleteFiles"
#define METHOD_WCXPLUGIN_DELETEFILES_SIGNATURE "(Ljava/lang/String;Ljava/lang/String;)I"
#define METHOD_WCXPLUGIN_GETPACKERCAPS "getPackerCaps"
#define METHOD_WCXPLUGIN_GETPACKERCAPS_SIGNATURE "()I"
#define METHOD_WCXPLUGIN_CONFIGUREPACKER "configurePacker"
#define METHOD_WCXPLUGIN_CONFIGUREPACKER_SIGNATURE "(I)I"
#define METHOD_WCXPLUGIN_STARTMEMPACK "startMemPack"
#define METHOD_WCXPLUGIN_STARTMEMPACK_SIGNATURE "(ILjava/lang/String;)Ljava/lang/Object;"
#define METHOD_WCXPLUGIN_PACKTOMEM "packToMem"
#define METHOD_WCXPLUGIN_PACKTOMEM_SIGNATURE "(Ljava/lang/Object;Ljava/lang/String;ILjava/lang/String;IILplugins/wcx/PackToMem;)I"
#define METHOD_WCXPLUGIN_DONEMEMPACK "doneMemPack"
#define METHOD_WCXPLUGIN_DONEMEMPACK_SIGNATURE "(Ljava/lang/Object;)I"
#define METHOD_WCXPLUGIN_CANYOUHANDLETHISFILE "canYouHandleThisFile"
#define METHOD_WCXPLUGIN_CANYOUHANDLETHISFILE_SIGNATURE "(Ljava/lang/String;)Z"

// Function Pointer to start JVM
typedef jint (CALLBACK* LPFNDLLCREATEJVM)(JavaVM**,void**,JavaVMInitArgs*);

// Function Pointer to start JVM
typedef jint (CALLBACK* LPFNDLLGETCREATEDJVM)(JavaVM**,jint,jint*);

typedef jboolean (JNICALL *PJAWT_GETAWT)(JNIEnv*, JAWT*);

// JVM DLL module handle
EXTERN HMODULE JVMHandle;
EXTERN HMODULE _hAWT;
// The Java Virtual Machine (JVM)
EXTERN JavaVM *_jvm;

// The WLX Plugin Class instance and plugin methods
EXTERN char pluginClass[MAX_PATH];

// The WFX Plugin Class instance and plugin methods
EXTERN char wfxPluginClass[MAX_PATH];

// The WDX Plugin Class instance and plugin methods
EXTERN char wdxPluginClass[MAX_PATH];

// The WCX Plugin Class instance and plugin methods
EXTERN char wcxPluginClass[MAX_PATH];

// absolute path name of this library
EXTERN char moduleFilename[MAX_PATH];
// the error message language (DE/EN/...)
EXTERN char language[3];

// exported function
EXTERN char *replace_str(char *str, char *orig, char *rep);
EXTERN void errorDialog(char *errorKey, ...);
EXTERN bool exceptionHandling(JNIEnv *, char *pluginClass, char *method, char *methodSignature);
EXTERN JNIEnv *startJVM();
EXTERN void copyJstringBytes(JNIEnv *, char *dest, jstring source, int max);

#ifdef JAVA
	HINSTANCE theDll = NULL;
	bool init = true;
#else
	extern HINSTANCE theDll;
	EXTERN bool init;
#endif

#undef JAVA
