// contents of fsplugin.h  version 1.3 (10.Dec.2002)

// 
#define ft_nomorefields 0
#define ft_numeric_32 1
#define ft_numeric_64 2
#define ft_numeric_floating 3
#define ft_date 4

#define ft_time 5
#define ft_boolean 6
#define ft_multiplechoice 7
#define ft_string 8
#define ft_fulltext 9
#define ft_datetime 10

// for FsContentGetValue
#define ft_nosuchfield -1   // error, invalid field number given
#define ft_fileerror -2     // file i/o error
#define ft_fieldempty -3    // field valid, but empty
#define ft_ondemand -4      // field will be retrieved only when user presses <SPACEBAR>
#define ft_delayed 0        // field takes a long time to extract -> try again in background

// for FsContentSetValue
#define ft_setsuccess 0     // setting of the attribute succeeded

// for FsContentGetSupportedFieldFlags
#define contflags_edit 1
#define contflags_substsize 2
#define contflags_substdatetime 4
#define contflags_substdate 6
#define contflags_substtime 8
#define contflags_substattributes 10
#define contflags_substattributestr 12
#define contflags_substmask 14

// for FsContentSetValue
#define setflags_first_attribute 1     // First attribute of this file

#define setflags_last_attribute  2     // Last attribute of this file
#define setflags_only_date       4     // Only set the date of the datetime value!


#define CONTENT_DELAYIFSLOW 1  // ContentGetValue called in foreground

typedef struct {

WORD wYear;
	WORD wMonth;
	WORD wDay;
} tdateformat,*pdateformat;

typedef struct {
	WORD wHour;
	WORD wMinute;
	WORD wSecond;
} ttimeformat,*ptimeformat;

typedef struct {
    DWORD SizeLow,SizeHigh;
    FILETIME LastWriteTime;
    int Attr;
} RemoteInfoStruct;


typedef struct {
	int size;
	DWORD PluginInterfaceVersionLow;
	DWORD PluginInterfaceVersionHi;
	char DefaultIniName[MAX_PATH];
} FsDefaultParamStruct;

// callback functions
typedef int (__stdcall *tProgressProc)(int PluginNr,char* SourceName,
             char* TargetName,int PercentDone);
typedef void (__stdcall *tLogProc)(int PluginNr,int MsgType,char* LogString);
typedef BOOL (__stdcall *tRequestProc)(int PluginNr,int RequestType,char* CustomTitle,
              char* CustomText,char* ReturnedText,int maxlen);
