// Contents of file contplug.h

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

// for ContentGetValue
#define ft_nosuchfield -1   // error, invalid field number given
#define ft_fileerror -2     // file i/o error
#define ft_fieldempty -3    // field valid, but empty

#define ft_ondemand -4      // field will be retrieved only when user presses <SPACEBAR>
#define ft_delayed 0        // field takes a long time to extract -> try again in background

#define CONTENT_DELAYIFSLOW 1  // ContentGetValue called in foreground

typedef struct {
    int size;
    DWORD PluginInterfaceVersionLow;
    DWORD PluginInterfaceVersionHi;
    char DefaultIniName[MAX_PATH];
} ContentDefaultParamStruct;

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

void __stdcall ContentGetDetectString(char* DetectString,int maxlen);
int __stdcall ContentGetSupportedField(int FieldIndex,char* FieldName,char* Units,int maxlen);
int __stdcall ContentGetValue(char* FileName,int FieldIndex,int UnitIndex,void* FieldValue,int maxlen,int flags);
void __stdcall ContentSetDefaultParams(ContentDefaultParamStruct* dps);

void __stdcall ContentStopGetValue(char* FileName);
int __stdcall ContentGetDefaultSortOrder(int FieldIndex);

void __stdcall ContentPluginUnloading(void);
