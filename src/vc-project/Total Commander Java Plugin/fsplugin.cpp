#include "stdafx.h"
#include "fsplugin.h"
#include "java.h"
#include "plugins_wfx_WFXPluginAdapter.h"

// Total Commander procedures
// progress dialog
tProgressProc ProgressProc;
// log messages
tLogProc LogProc;
// request dialogue
tRequestProc RequestProc;

EXTERN jobject __stdcall GetPlugin(JNIEnv *, char *);

EXTERN jobject __stdcall GetPluginClassLoader(JNIEnv *);

EXTERN jclass __stdcall GetClass(JNIEnv *, char *);

const __int64 SECS_TO_100NS = 10000000;
const __int64 SECS_BETWEEN_EPOCHS = 11644473600;

/* Convert windows time (FILETIME) time to Java time (java.lang.Date) */
time_t FileTimeToUnixTime( FILETIME FileTime, long *nsec )
 {
     __int64 UnixTime;

     /* get the full win32 value, in 100ns */
     UnixTime = ((__int64)FileTime.dwHighDateTime << 32) + 
         FileTime.dwLowDateTime;
 
     /* convert to the Unix epoch */
     UnixTime -= (SECS_BETWEEN_EPOCHS * SECS_TO_100NS);
 
     if ( nsec )
     {
         /* get the number of 100ns, convert to ns */
         *nsec = (UnixTime % SECS_TO_100NS) * 100;
     }
 
     UnixTime /= SECS_TO_100NS; /* now convert to seconds */
 
	 return (time_t)UnixTime;
}

/* Convert Java time (java.lang.Date) time to windows (FILETIME) */
void UnixTimeToFileTime(time_t t, LPFILETIME pft)
   {
     // Note that LONGLONG is a 64-bit value
     LONGLONG ll;

	 if (t==0) 
	 {
		// unused date time?
		pft->dwLowDateTime = 0xFFFFFFFE;
		pft->dwHighDateTime = 0xFFFFFFFF;
		return;
	 }
     ll = Int32x32To64(t, 10000000) + 116444736000000000;
     pft->dwLowDateTime = (DWORD)ll;
     pft->dwHighDateTime = ll >> 32;
   }

/* Set WIN32_FIND_DATA from Java plugins.wfx.Win32FindData fields. */
void SetFindData(jclass _win32FindDataCls, jobject _wlxWin32FindDataObject, WIN32_FIND_DATA *FindData)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);	
    FILETIME ft;

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	// Rectangle class
	jclass _rectangleCls = _env->FindClass(CLASS_RECTANGLE);
	if (_rectangleCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, CLASS_RECTANGLE);
		return;
	}

	jfieldID _dwFileAttributesID = _env->GetFieldID (_win32FindDataCls, FIELD_DWFILEATTRIBUTES, FIELD_DWFILEATTRIBUTES_SIGNATURE);
	if (_dwFileAttributesID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_DWFILEATTRIBUTES, FIELD_DWFILEATTRIBUTES_SIGNATURE);
		return;
	}

	FindData->dwFileAttributes = _env->GetLongField(_wlxWin32FindDataObject, _dwFileAttributesID);

	jfieldID _ftLastWriteTimeID = _env->GetFieldID (_win32FindDataCls, FIELD_FTLASTWRITETIME, FIELD_FILETIME_SIGNATURE);
	if (_ftLastWriteTimeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_FTLASTWRITETIME, FIELD_FILETIME_SIGNATURE);
		return;
	}

	jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

	jfieldID _dwLowDateTimeID = _env->GetFieldID (_fileTimeCls, FIELD_DWLOWDATETIME, FIELD_DWLOWDATETIME_SIGNATURE);
	if (_dwLowDateTimeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_DWLOWDATETIME, FIELD_DWLOWDATETIME_SIGNATURE);
		return;
	}

	jlong dwLowDateTime = _env->GetLongField(_env->GetObjectField(_wlxWin32FindDataObject, _ftLastWriteTimeID), _dwLowDateTimeID);
    UnixTimeToFileTime(dwLowDateTime/1000, &ft);
	FindData->ftLastWriteTime.dwLowDateTime= ft.dwLowDateTime;
	FindData->ftLastWriteTime.dwHighDateTime= ft.dwHighDateTime;

	jfieldID _ftCreationTimeID = _env->GetFieldID (_win32FindDataCls, FIELD_FTCREATIONTIME, FIELD_FILETIME_SIGNATURE);
	if (_ftCreationTimeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_FTCREATIONTIME, FIELD_FILETIME_SIGNATURE);
		return;
	}

	jlong ftCreationTime = _env->GetLongField(_env->GetObjectField(_wlxWin32FindDataObject, _ftCreationTimeID), _dwLowDateTimeID);
    UnixTimeToFileTime(dwLowDateTime/1000, &ft);
	FindData->ftCreationTime.dwLowDateTime= ft.dwLowDateTime;
	FindData->ftCreationTime.dwHighDateTime= ft.dwHighDateTime;

	jfieldID _ftLastAccessTimeID = _env->GetFieldID (_win32FindDataCls, FIELD_FTLASTACCESSTIME, FIELD_FILETIME_SIGNATURE);
	if (_ftLastAccessTimeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_FTLASTACCESSTIME, FIELD_FILETIME_SIGNATURE);
		return;
	}

	jlong ftLastAccessTime = _env->GetLongField(_env->GetObjectField(_wlxWin32FindDataObject, _ftLastAccessTimeID), _dwLowDateTimeID);
    UnixTimeToFileTime(dwLowDateTime/1000, &ft);
	FindData->ftLastAccessTime.dwLowDateTime= ft.dwLowDateTime;
	FindData->ftLastAccessTime.dwHighDateTime= ft.dwHighDateTime;

	jfieldID _nFileSizeHighID = _env->GetFieldID (_win32FindDataCls, FIELD_NFILESIZEHIGH, FIELD_NFILESIZEHIGH_SIGNATURE);
	if (_nFileSizeHighID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_NFILESIZEHIGH, FIELD_NFILESIZEHIGH_SIGNATURE);
		return;
	}

	jfieldID _nFileSizeLowID = _env->GetFieldID (_win32FindDataCls, FIELD_NFILESIZELOW, FIELD_NFILESIZELOW_SIGNATURE);
	if (_nFileSizeLowID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_NFILESIZELOW, FIELD_NFILESIZELOW_SIGNATURE);
		return;
	}

	FindData->nFileSizeHigh= _env->GetLongField(_wlxWin32FindDataObject, _nFileSizeHighID);
	FindData->nFileSizeLow= _env->GetLongField(_wlxWin32FindDataObject, _nFileSizeLowID);

	jfieldID _dwReserved0ID = _env->GetFieldID (_win32FindDataCls, FIELD_DWRESERVED0, FIELD_DWRESERVED0_SIGNATURE);
	if (_dwReserved0ID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_DWRESERVED0, FIELD_DWRESERVED0_SIGNATURE);
		return;
	}

	FindData->dwReserved0= _env->GetLongField(_wlxWin32FindDataObject, _dwReserved0ID);

	jfieldID _dwReserved1ID = _env->GetFieldID (_win32FindDataCls, FIELD_DWRESERVED1, FIELD_DWRESERVED1_SIGNATURE);
	if (_dwReserved1ID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_DWRESERVED1, FIELD_DWRESERVED1_SIGNATURE);
		return;
	}

	FindData->dwReserved1= _env->GetLongField(_wlxWin32FindDataObject, _dwReserved1ID);

	jfieldID _cFileNameID = _env->GetFieldID (_win32FindDataCls, FIELD_CFILENAME, FIELD_CFILENAME_SIGNATURE);
	if (_cFileNameID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_CFILENAME, FIELD_CFILENAME_SIGNATURE);
		return;
	}

	copyJstringBytes(_env, FindData->cFileName, (jstring)_env->GetObjectField(_wlxWin32FindDataObject, _cFileNameID), MAX_PATH);

	jfieldID _cAlternateFileNameID = _env->GetFieldID (_win32FindDataCls, FIELD_CALTERNATEFILENAME, FIELD_CALTERNATEFILENAME_SIGNATURE);
	if (_cAlternateFileNameID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_CALTERNATEFILENAME, FIELD_CALTERNATEFILENAME_SIGNATURE);
		return;
	}

	copyJstringBytes(_env, FindData->cAlternateFileName, (jstring)_env->GetObjectField(_wlxWin32FindDataObject, _cAlternateFileNameID), MAX_PATH);

	// Set last error message
	jfieldID _lastErrorMessageID = _env->GetFieldID (_win32FindDataCls, FIELD_LASTERRORMESSAGE, FIELD_LASTERRORMESSAGE_SIGNATURE);
	if (_lastErrorMessageID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_LASTERRORMESSAGE, FIELD_LASTERRORMESSAGE_SIGNATURE);
		return;
	}

	SetLastError(_env->GetLongField(_wlxWin32FindDataObject, _lastErrorMessageID));
}

int __stdcall FsInit(int PluginNr,tProgressProc pProgressProc,tLogProc pLogProc,tRequestProc pRequestProc)
{
	JNIEnv *_env = startJVM();
	if (_env == NULL) {
		return 0;
	}

	ProgressProc=pProgressProc;
    LogProc=pLogProc;
    RequestProc=pRequestProc;

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsInitMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSINIT, METHOD_WFXPLUGIN_FSINIT_SIGNATURE);
	if (_fsInitMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSINIT, METHOD_WFXPLUGIN_FSINIT_SIGNATURE);
		return 0;
	}

	/* fsInit(PluginNr) */
	jint pluginNr = PluginNr;
	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsInitMethodID, pluginNr);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSINIT, METHOD_WFXPLUGIN_FSINIT_SIGNATURE)) {
      return 0;
    }
	return 0;
}


HANDLE __stdcall FsFindFirst(char* Path,WIN32_FIND_DATA *FindData)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return INVALID_HANDLE_VALUE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsFindFirstMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSFINDFIRST, METHOD_WFXPLUGIN_FSFINDFIRST_SIGNATURE);
	if (_fsFindFirstMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSFINDFIRST, METHOD_WFXPLUGIN_FSFINDFIRST_SIGNATURE);
		return INVALID_HANDLE_VALUE;
	}

	/* Set Win32Data */
	memset(FindData,0,sizeof(WIN32_FIND_DATA));

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _win32FindDataCls = GetClass(_env, CLASS_WIN32FINDDATA);

	jmethodID _win32FindDataMethodID = _env->GetMethodID(_win32FindDataCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_win32FindDataMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_WIN32FINDDATA, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return INVALID_HANDLE_VALUE;
	}

	/* new Win32FindData() */
    jobject _wlxWin32FindDataObject = _env->NewObject(_win32FindDataCls, _win32FindDataMethodID);
    if (_wlxWin32FindDataObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_WIN32FINDDATA, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return INVALID_HANDLE_VALUE;
    }

	/* findFirst(Path, FindData) */
	jstring path = (_env)->NewStringUTF(Path);
	jobject findData = _wlxWin32FindDataObject;
	jobject _output = (jobject)_env->CallObjectMethod(_wfxJPluginObject, _fsFindFirstMethodID, path, findData);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSFINDFIRST, METHOD_WFXPLUGIN_FSFINDFIRST_SIGNATURE)) {
		return INVALID_HANDLE_VALUE;
    }

	SetFindData(_win32FindDataCls, _wlxWin32FindDataObject, FindData);

	if (_output==NULL) {
		return INVALID_HANDLE_VALUE;
	}
	
	return _output;
}

BOOL __stdcall FsFindNext(HANDLE Hdl,WIN32_FIND_DATA *FindData)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsFindNextMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSFINDNEXT, METHOD_WFXPLUGIN_FSFINDNEXT_SIGNATURE);
	if (_fsFindNextMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSFINDNEXT, METHOD_WFXPLUGIN_FSFINDNEXT_SIGNATURE);
		return FALSE;
	}

	/* Set Win32Data */
	memset(FindData,0,sizeof(WIN32_FIND_DATA));

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _win32FindDataCls = GetClass(_env, CLASS_WIN32FINDDATA);

	jmethodID _win32FindDataMethodID = _env->GetMethodID(_win32FindDataCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_win32FindDataMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_WIN32FINDDATA, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

    /* new Win32FindData() */
	jobject _wlxWin32FindDataObject = _env->NewObject(_win32FindDataCls, _win32FindDataMethodID);
    if (_wlxWin32FindDataObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_WIN32FINDDATA, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return FALSE;
    }

	/* findNext(Hdl, FindData) */
	jobject handle = (jobject)Hdl;
	jobject findData = _wlxWin32FindDataObject;
	jboolean _output = (jboolean)_env->CallObjectMethod(_wfxJPluginObject, _fsFindNextMethodID, handle, findData);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSFINDNEXT, METHOD_WFXPLUGIN_FSFINDNEXT_SIGNATURE)) {
		return FALSE;
    }

	SetFindData(_win32FindDataCls, _wlxWin32FindDataObject, FindData);

	if (_output==JNI_FALSE) {
		return FALSE;
	}
	return TRUE;
}

int __stdcall FsFindClose(HANDLE Hdl)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsFindCloseMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSFINDCLOSE, METHOD_WFXPLUGIN_FSFINDCLOSE_SIGNATURE);
	if (_fsFindCloseMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSFINDCLOSE, METHOD_WFXPLUGIN_FSFINDCLOSE_SIGNATURE);
		return 0;
	}

	/* fsFindClose(Hdl); */
	jobject handle = (jobject)Hdl;
	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsFindCloseMethodID, handle);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSFINDCLOSE, METHOD_WFXPLUGIN_FSFINDCLOSE_SIGNATURE)) {
		return 0;
    }
	return _output;
}

BOOL __stdcall FsMkDir(char* Path)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsMkDirMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSMKDIR, METHOD_WFXPLUGIN_FSMKDIR_SIGNATURE);
	if (_fsMkDirMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSMKDIR, METHOD_WFXPLUGIN_FSMKDIR_SIGNATURE);
		return FALSE;
	}

	/* fsMkDir(Path); */
	jstring path = _env->NewStringUTF(Path);

	jboolean _output = _env->CallBooleanMethod(_wfxJPluginObject, _fsMkDirMethodID, path);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSMKDIR, METHOD_WFXPLUGIN_FSMKDIR_SIGNATURE)) {
		if (path != NULL) {
			_env->ReleaseStringUTFChars(path, 0);
		}
		return FALSE;
    }

	if (path != NULL) {
		_env->ReleaseStringUTFChars(path, 0);
	}
	return _output;
}

int __stdcall FsExecuteFile(HWND MainWin,char* RemoteName,char* Verb)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 1;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsExecuteFileMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSEXECUTEFILE, METHOD_WFXPLUGIN_FSEXECUTEFILE_SIGNATURE);
	if (_fsExecuteFileMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSEXECUTEFILE, METHOD_WFXPLUGIN_FSEXECUTEFILE_SIGNATURE);
		return 1;
	}

	/* fsExecuteFile(MainWin, RemoteName, Verb); */
	jint handle = (jint)MainWin;
	jstring name = _env->NewStringUTF(RemoteName);
	jstring verb = _env->NewStringUTF(Verb);
	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsExecuteFileMethodID, handle, name, verb);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSEXECUTEFILE, METHOD_WFXPLUGIN_FSEXECUTEFILE_SIGNATURE)) {
		if (name != NULL) {
			_env->ReleaseStringUTFChars(name, 0);
		}
		if (verb != NULL) {
			_env->ReleaseStringUTFChars(verb, 0);
		}
		return 1;
    }

	if (name != NULL) {
		_env->ReleaseStringUTFChars(name, 0);
	}
	if (verb != NULL) {
		_env->ReleaseStringUTFChars(verb, 0);
	}
	return _output;
}

int __stdcall FsRenMovFile(char* OldName,char* NewName,BOOL Move,BOOL OverWrite,RemoteInfoStruct* ri)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 2;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsRenMovFileMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSRENMOVFILE, METHOD_WFXPLUGIN_FSRENMOVFILE_SIGNATURE);
	if (_fsRenMovFileMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSRENMOVFILE, METHOD_WFXPLUGIN_FSRENMOVFILE_SIGNATURE);
		return 2;
	}

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

	jmethodID _fileTimeMethodID = _env->GetMethodID(_fileTimeCls, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
	if (_fileTimeMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
		return NULL;
	}

	/* new FileTime(ri->LastWriteTime.dwLowDateTime, ri->LastWriteTime.dwHighDateTime) */
	jobject _wfxFileTimeObject = _env->NewObject(_fileTimeCls, _fileTimeMethodID, (jlong)ri->LastWriteTime.dwLowDateTime, (jlong)ri->LastWriteTime.dwHighDateTime);
    if (_wfxFileTimeObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_FILETIME, CLASS_INIT, CLASS_FILETIME_INIT_SIGNATURE);
		return 2;
    }

	jclass _remoteInfoCls = GetClass(_env, CLASS_REMOTEINFO);

	jmethodID _remoteInfoMethodID = _env->GetMethodID(_remoteInfoCls, CLASS_INIT,CLASS_REMOTEINFO_INIT_SIGNATURE);
	if (_remoteInfoMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_REMOTEINFO, CLASS_INIT,CLASS_REMOTEINFO_INIT_SIGNATURE);
		return 2;
	}

	/* new RemoteInfo() */
	jobject _wlxRemoteInfoObject = _env->NewObject(_remoteInfoCls, _remoteInfoMethodID, (jlong)ri->SizeLow, (jlong)ri->SizeHigh, _wfxFileTimeObject, (jint)ri->Attr);
    if (_wlxRemoteInfoObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_REMOTEINFO, CLASS_INIT, CLASS_REMOTEINFO_INIT_SIGNATURE);
		return 2;
    }
	/* fsRenMovFile(OldName, NewName, Move, OverWrite, ri); */
	jstring oldName = _env->NewStringUTF(OldName);
	jstring newName = _env->NewStringUTF(NewName);
	jboolean move = Move;
	jboolean overWrite = OverWrite;

	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsRenMovFileMethodID, oldName, newName, move, overWrite, _wlxRemoteInfoObject);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSRENMOVFILE, METHOD_WFXPLUGIN_FSRENMOVFILE_SIGNATURE)) {
		if (oldName != NULL) {
			_env->ReleaseStringUTFChars(oldName, 0);
		}
		if (newName != NULL) {
			_env->ReleaseStringUTFChars(newName, 0);
		}
		return 2;
    }

	if (oldName != NULL) {
		_env->ReleaseStringUTFChars(oldName, 0);
	}
	if (newName != NULL) {
		_env->ReleaseStringUTFChars(newName, 0);
	}

	return _output;
}

int __stdcall FsGetFile(char* RemoteName,char* LocalName,int CopyFlags,RemoteInfoStruct* ri)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 2;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsGetFileMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSGETFILE, METHOD_WFXPLUGIN_FSGETFILE_SIGNATURE);
	if (_fsGetFileMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSGETFILE, METHOD_WFXPLUGIN_FSGETFILE_SIGNATURE);
		return 2;
	}

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

	jmethodID _fileTimeMethodID = _env->GetMethodID(_fileTimeCls, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
	if (_fileTimeMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
		return NULL;
	}

	/* new FileTime(ri->LastWriteTime.dwLowDateTime, ri->LastWriteTime.dwHighDateTime) */
	jobject _wfxFileTimeObject = _env->NewObject(_fileTimeCls, _fileTimeMethodID, (jlong)ri->LastWriteTime.dwLowDateTime, (jlong)ri->LastWriteTime.dwHighDateTime);
    if (_wfxFileTimeObject == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
		return 2;
    }

	jclass _remoteInfoCls = GetClass(_env, CLASS_REMOTEINFO);

	jmethodID _remoteInfoMethodID = _env->GetMethodID(_remoteInfoCls, CLASS_INIT,CLASS_REMOTEINFO_INIT_SIGNATURE);
	if (_remoteInfoMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_REMOTEINFO, CLASS_INIT,CLASS_REMOTEINFO_INIT_SIGNATURE);
		return 2;
	}

	/* new RemoteInfo() */
    jobject _wlxRemoteInfoObject = _env->NewObject(_remoteInfoCls, _remoteInfoMethodID, (jlong)ri->SizeLow, (jlong)ri->SizeHigh, _wfxFileTimeObject, (jint)ri->Attr);
    if (_wlxRemoteInfoObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_REMOTEINFO, CLASS_INIT,CLASS_REMOTEINFO_INIT_SIGNATURE);
		return 2;
    }
	/* fsRenMovFile(OldName, NewName, Move, OverWrite, ri); */
	jstring oldName = _env->NewStringUTF(RemoteName);
	jstring newName = _env->NewStringUTF(LocalName);
	jint copyFlags = CopyFlags;
	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsGetFileMethodID, oldName, newName, copyFlags, _wlxRemoteInfoObject);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSGETFILE, METHOD_WFXPLUGIN_FSGETFILE_SIGNATURE)) {
		if (oldName != NULL) {
			_env->ReleaseStringUTFChars(oldName, 0);
		}
		if (newName != NULL) {
			_env->ReleaseStringUTFChars(newName, 0);
		}
		return 2;
    }

	if (oldName != NULL) {
		_env->ReleaseStringUTFChars(oldName, 0);
	}
	if (newName != NULL) {
		_env->ReleaseStringUTFChars(newName, 0);
	}
	return _output;
}


int __stdcall FsPutFile(char* LocalName,char* RemoteName,int CopyFlags)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 2;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsPutFileMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSPUTFILE, METHOD_WFXPLUGIN_FSPUTFILE_SIGNATURE);
	if (_fsPutFileMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSPUTFILE, METHOD_WFXPLUGIN_FSPUTFILE_SIGNATURE);
		return 2;
	}

	/* fsRenMovFile(OldName, NewName, Move, OverWrite, ri); */
	jstring oldName = _env->NewStringUTF(LocalName);
	jstring newName = _env->NewStringUTF(RemoteName);
	jint copyFlags = CopyFlags;

	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsPutFileMethodID, oldName, newName, copyFlags);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSPUTFILE, METHOD_WFXPLUGIN_FSPUTFILE_SIGNATURE)) {
		if (oldName != NULL) {
			_env->ReleaseStringUTFChars(oldName, 0);
		}
		if (newName != NULL) {
			_env->ReleaseStringUTFChars(newName, 0);
		}
		return 2;
    }

	if (oldName != NULL) {
		_env->ReleaseStringUTFChars(oldName, 0);
	}
	if (newName != NULL) {
		_env->ReleaseStringUTFChars(newName, 0);
	}
	return _output;
}

BOOL __stdcall FsDeleteFile(char* RemoteName)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsDeleteFileMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSDELETEFILE, METHOD_WFXPLUGIN_FSDELETEFILE_SIGNATURE);
	if (_fsDeleteFileMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSDELETEFILE, METHOD_WFXPLUGIN_FSDELETEFILE_SIGNATURE);
		return FALSE;
	}

	/* fsDeleteFile(RemoteName); */
	jstring remoteName = _env->NewStringUTF(RemoteName);

	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsDeleteFileMethodID, remoteName);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSDELETEFILE, METHOD_WFXPLUGIN_FSDELETEFILE_SIGNATURE)) {
		if (remoteName != NULL) {
			_env->ReleaseStringUTFChars(remoteName, 0);
		}
		return FALSE;
    }

	if (remoteName != NULL) {
		_env->ReleaseStringUTFChars(remoteName, 0);
	}
	return _output;
}

BOOL __stdcall FsRemoveDir(char* RemoteName)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsRemoveDirMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSREMOVEDIR, METHOD_WFXPLUGIN_FSREMOVEDIR_SIGNATURE);
	if (_fsRemoveDirMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSREMOVEDIR, METHOD_WFXPLUGIN_FSREMOVEDIR_SIGNATURE);
		return FALSE;
	}

	/* fsRemoveDir(RemoteName); */
	jstring remoteName = _env->NewStringUTF(RemoteName);

	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsRemoveDirMethodID, remoteName);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSREMOVEDIR, METHOD_WFXPLUGIN_FSREMOVEDIR_SIGNATURE)) {
		if (remoteName != NULL) {
			_env->ReleaseStringUTFChars(remoteName, 0);
		}
		return FALSE;
    }

	if (remoteName != NULL) {
		_env->ReleaseStringUTFChars(remoteName, 0);
	}
	return _output;
}

BOOL __stdcall FsSetAttr(char* RemoteName,int NewAttr)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsSetAttrMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSSETATTR, METHOD_WFXPLUGIN_FSSETATTR_SIGNATURE);
	if (_fsSetAttrMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSSETATTR, METHOD_WFXPLUGIN_FSSETATTR_SIGNATURE);
		return FALSE;
	}

	/* fsSetAttr(RemoteName, NewAttr); */
	jstring remoteName = _env->NewStringUTF(RemoteName);
	jint newAttr = NewAttr;

	jboolean _output = _env->CallBooleanMethod(_wfxJPluginObject, _fsSetAttrMethodID, remoteName, newAttr);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSSETATTR, METHOD_WFXPLUGIN_FSSETATTR_SIGNATURE)) {
		if (remoteName != NULL) {
			_env->ReleaseStringUTFChars(remoteName, 0);
		}
		return FALSE;
    }

	if (remoteName != NULL) {
		_env->ReleaseStringUTFChars(remoteName, 0);
	}
	return _output;
}

BOOL __stdcall FsSetTime(char* RemoteName,FILETIME *CreationTime,
      FILETIME *LastAccessTime,FILETIME *LastWriteTime)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsSetTimeMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSSETTIME, METHOD_WFXPLUGIN_FSSETTIME_SIGNATURE);
	if (_fsSetTimeMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSSETTIME, METHOD_WFXPLUGIN_FSSETTIME_SIGNATURE);
		return FALSE;
	}

	/* new FileTime(CreationTime->dwLowDateTime, CreationTime->dwHighDateTime) */
	jobject _wfxCreationTimeObject = NULL;
    /* new FileTime(CreationTime->dwLowDateTime, CreationTime->dwHighDateTime) */
	if (CreationTime!=NULL) {
		jobject _jClassLoaderObject = GetPluginClassLoader(_env);
		
		jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

		jmethodID _fileTimeMethodID = _env->GetMethodID(_fileTimeCls, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
		if (_fileTimeMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
			return NULL;
		}

		_wfxCreationTimeObject = _env->NewObject(_fileTimeCls, _fileTimeMethodID, (jlong)CreationTime->dwLowDateTime, (jlong)CreationTime->dwHighDateTime);
		if (_wfxCreationTimeObject == NULL) {
			errorDialog(ERR_CLASS_INIT_FAILED, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
			return FALSE;
		}
	}
	jobject _wfxLastAccessTimeObject = NULL;
    /* new FileTime(LastAccessTime->dwLowDateTime, LastAccessTime->dwHighDateTime) */
	if (LastAccessTime!=NULL) {
		jobject _jClassLoaderObject = GetPluginClassLoader(_env);
		
		jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

		jmethodID _fileTimeMethodID = _env->GetMethodID(_fileTimeCls, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
		if (_fileTimeMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
			return NULL;
		}

		/* new FileTime(LastAccessTime->dwLowDateTime, LastAccessTime->dwHighDateTime) */
		_wfxLastAccessTimeObject = _env->NewObject(_fileTimeCls, _fileTimeMethodID, (jlong)LastAccessTime->dwLowDateTime, (jlong)LastAccessTime->dwHighDateTime);
		if (_wfxLastAccessTimeObject == NULL) {
			errorDialog(ERR_CLASS_INIT_FAILED, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
			return FALSE;
		}
	}
	jobject _wfxLastWriteTimeObject = NULL;
    /* new FileTime(LastWriteTime->dwLowDateTime, LastWriteTime->dwHighDateTime) */
	if (LastWriteTime!=NULL) {

		jobject _jClassLoaderObject = GetPluginClassLoader(_env);
		
		jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

		jmethodID _fileTimeMethodID = _env->GetMethodID(_fileTimeCls, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
		if (_fileTimeMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
			return NULL;
		}

		/* new FileTime(LastWriteTime->dwLowDateTime, LastWriteTime->dwHighDateTime) */
		_wfxLastWriteTimeObject = _env->NewObject(_fileTimeCls, _fileTimeMethodID, (jlong)LastWriteTime->dwLowDateTime, (jlong)LastWriteTime->dwHighDateTime);
		if (_wfxLastWriteTimeObject == NULL) {
			errorDialog(ERR_CLASS_INIT_FAILED, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
			return FALSE;
		}
	}
	/* fsSetAttr(RemoteName, NewAttr); */
	jstring remoteName = _env->NewStringUTF(RemoteName);
	jobject creationTime = _wfxCreationTimeObject;
	jobject lastAccessTime = _wfxLastAccessTimeObject;
	jobject lastWriteTime = _wfxLastWriteTimeObject;
	jboolean _output = _env->CallBooleanMethod(_wfxJPluginObject, _fsSetTimeMethodID, remoteName, creationTime, lastAccessTime, lastWriteTime);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSSETTIME, METHOD_WFXPLUGIN_FSSETTIME_SIGNATURE)) {
		return FALSE;
    }
	return _output;
}

void __stdcall FsGetDefRootName(char* DefRootName,int maxlen)
{
	JNIEnv *_env= startJVM();
	if (_env == NULL) {
		return;
	}

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsGetDefRootNameMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSGETDEFROOTNAME, METHOD_WFXPLUGIN_FSGETDEFROOTNAME_SIGNATURE);
	if (_fsGetDefRootNameMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSGETDEFROOTNAME, METHOD_WFXPLUGIN_FSGETDEFROOTNAME_SIGNATURE);
		return;
	}

	/* WFXPlugin.getDefRootName(DefRootName, maxlen); */
	jstring rootName = (jstring)_env->CallObjectMethod(_wfxJPluginObject, _fsGetDefRootNameMethodID, maxlen);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSGETDEFROOTNAME, METHOD_WFXPLUGIN_FSGETDEFROOTNAME_SIGNATURE)) {
		return;
    }
	copyJstringBytes(_env, DefRootName, rootName, MAX_PATH);
}

void __stdcall FsSetDefaultParams(FsDefaultParamStruct* dps)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsSetDefaultParamsMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSSETDEFAULTPARAMS, METHOD_WFXPLUGIN_FSSETDEFAULTPARAMS_SIGNATURE);
	if (_fsSetDefaultParamsMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSSETDEFAULTPARAMS, METHOD_WFXPLUGIN_FSSETDEFAULTPARAMS_SIGNATURE);
		return;
	}

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _defaultParamCls = GetClass(_env, CLASS_DEFAULTPARAM);

	jmethodID _defaultParamMethodID = _env->GetMethodID(_defaultParamCls, CLASS_INIT,CLASS_DEFAULTPARAM_INIT_SIGNATURE);
	if (_defaultParamMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_DEFAULTPARAM, CLASS_INIT,CLASS_DEFAULTPARAM_INIT_SIGNATURE);
		return;
	}
	/* new DefaultParam() */
	jstring iniName= _env->NewStringUTF(dps->DefaultIniName);
	jobject _wfxFsDefaultParamObject = _env->NewObject(_defaultParamCls, _defaultParamMethodID, (jint)dps->size, (jlong)dps->PluginInterfaceVersionLow, (jlong)dps->PluginInterfaceVersionHi, iniName);
    if (_wfxFsDefaultParamObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_DEFAULTPARAM, CLASS_INIT,CLASS_DEFAULTPARAM_INIT_SIGNATURE);
		return;
    }

	/* WLXPlugin.FsSetDefaultParams(dps); */
	_env->CallVoidMethod(_wfxJPluginObject, _fsSetDefaultParamsMethodID, _wfxFsDefaultParamObject);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSSETDEFAULTPARAMS, METHOD_WFXPLUGIN_FSSETDEFAULTPARAMS_SIGNATURE)) {
		return;
    }
}

BOOL __stdcall FsDisconnect(char* DisconnectRoot)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return TRUE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsDisconnectMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSDISCONNECT, METHOD_WFXPLUGIN_FSDISCONNECT_SIGNATURE);
	if (_fsDisconnectMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSDISCONNECT, METHOD_WFXPLUGIN_FSDISCONNECT_SIGNATURE);
		return TRUE;
	}

	/* fsDisconnect(DisconnectRoot); */
	jstring disconnectRoot = _env->NewStringUTF(DisconnectRoot);

	jboolean _output = _env->CallBooleanMethod(_wfxJPluginObject, _fsDisconnectMethodID, disconnectRoot);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSDISCONNECT, METHOD_WFXPLUGIN_FSDISCONNECT_SIGNATURE)) {
		if (disconnectRoot != NULL) {
			_env->ReleaseStringUTFChars(disconnectRoot, 0);
		}
		return TRUE;
    }

	if (disconnectRoot != NULL) {
		_env->ReleaseStringUTFChars(disconnectRoot, 0);
	}
	return _output;
}

void __stdcall FsStatusInfo(char* RemoteDir,int InfoStartEnd,int InfoOperation)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsStatusInfoMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSSTATUSINFO, METHOD_WFXPLUGIN_FSSTATUSINFO_SIGNATURE);
	if (_fsStatusInfoMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSSTATUSINFO, METHOD_WFXPLUGIN_FSSTATUSINFO_SIGNATURE);
		return;
	}

	/* fsStatusInfo(RemoteDir, InfoStartEnd, InfoOperation); */
	jstring remoteDir = _env->NewStringUTF(RemoteDir);
	jint infoStartEnd = InfoStartEnd;
	jint infoOperation = InfoOperation;

	_env->CallBooleanMethod(_wfxJPluginObject, _fsStatusInfoMethodID, remoteDir, infoStartEnd, infoOperation);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSSTATUSINFO, METHOD_WFXPLUGIN_FSSTATUSINFO_SIGNATURE)) {
		if (remoteDir != NULL) {
			_env->ReleaseStringUTFChars(remoteDir, 0);
		}
		return;
    }

	if (remoteDir != NULL) {
		_env->ReleaseStringUTFChars(remoteDir, 0);
	}
}

int __stdcall FsExtractCustomIcon(char* RemoteName,int ExtractFlags,HICON* TheIcon) {
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsExtractCustomIconMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSEXTRACTCUSTOMICON, METHOD_WFXPLUGIN_FSEXTRACTCUSTOMICON_SIGNATURE);
	if (_fsExtractCustomIconMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSEXTRACTCUSTOMICON, METHOD_WFXPLUGIN_FSEXTRACTCUSTOMICON_SIGNATURE);
		return 0;
	}

	// Retrieve StringBuffer
	jclass _stringBufferCls = _env->FindClass(CLASS_STRINGBUFFER);
	if (_stringBufferCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, CLASS_STRINGBUFFER);
		return 0;
	}
	jmethodID _stringBufferMethodID = _env->GetMethodID(_stringBufferCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_stringBufferMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRINGBUFFER, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}
	jmethodID _toStringMethodID = _env->GetMethodID(_stringBufferCls, METHOD_STRINGBUFFER_TOSTRING, METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
	if (_toStringMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRINGBUFFER, METHOD_STRINGBUFFER_TOSTRING,METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
		return 0;
	}
	jobject _stringBufferObject = _env->NewObject(_stringBufferCls, _stringBufferMethodID);
    if (_stringBufferObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_STRINGBUFFER, CLASS_INIT, CLASS_INIT_SIGNATURE);
 		return 0;
    }

	// fsExtractCustomIcon(RemoteName, ExtractFlags, TheIcon);
	jstring remoteName = _env->NewStringUTF(RemoteName);
	jint extractFlags = ExtractFlags;
	jobject iconBuffer = _stringBufferObject;

	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsExtractCustomIconMethodID, remoteName, extractFlags, iconBuffer);
	if (exceptionHandling(_env, pluginClass, METHOD_WFXPLUGIN_FSEXTRACTCUSTOMICON, METHOD_WFXPLUGIN_FSEXECUTEFILE_SIGNATURE)) {
		if (remoteName != NULL) {
			_env->ReleaseStringUTFChars(remoteName, 0);
		}
		return 0;
    }
	if (remoteName != NULL) {
		_env->ReleaseStringUTFChars(remoteName, 0);
	}

	char iconName[MAX_PATH];
	jstring icon = (jstring)_env->CallObjectMethod(iconBuffer, _toStringMethodID);
	copyJstringBytes(_env, iconName, icon, MAX_PATH);

	if (strlen(iconName) > 0) {

		HICON myIcon = NULL; 
		char *sep = strchr(iconName, '|');
		if (sep != NULL) {
			// 1. icon of a resource: "252|shell32.dll"
			HINSTANCE theIconFile= (HINSTANCE)LoadLibraryEx((char *)(sep+1), 0, LOAD_LIBRARY_AS_DATAFILE);
			*sep = '\0';
			myIcon = (HICON)LoadIcon(theIconFile, MAKEINTRESOURCE(atoi(iconName))); 
		} else {
			// 2. full path name: "%CWD%\\test.ico"
			// change current working directory
			char *lastBSlash = strrchr(moduleFilename, '\\');
			char cwd[MAX_PATH];
			memset(cwd,0,MAX_PATH);
			strncpy( cwd, moduleFilename, (lastBSlash-moduleFilename) );
			
			strcpy(iconName, replace_str(iconName, "%CWD%", cwd));
			if(ExtractFlags & 1) {
				myIcon = (HICON)LoadImage(0, iconName, IMAGE_ICON, 16, 16, LR_LOADFROMFILE);
			} else {
				myIcon = (HICON)LoadImage(0, iconName, IMAGE_ICON, 32, 32, LR_LOADFROMFILE);
			}
		}
		*TheIcon = myIcon; 
	   }
	return _output;
}

int __stdcall FsGetPreviewBitmap(char* RemoteName,int width,int height,
  HBITMAP* ReturnedBitmap)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	// Retrieve StringBuffer
	jclass _stringBufferCls = _env->FindClass(CLASS_STRINGBUFFER);
	if (_stringBufferCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, CLASS_STRINGBUFFER);
		return 0;
	}
	jmethodID _stringBufferMethodID = _env->GetMethodID(_stringBufferCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_stringBufferMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRINGBUFFER, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}
	jmethodID _toStringMethodID = _env->GetMethodID(_stringBufferCls, METHOD_STRINGBUFFER_TOSTRING, METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
	if (_toStringMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRINGBUFFER, METHOD_STRINGBUFFER_TOSTRING,METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
		return 0;
	}
	jobject _stringBufferObject = _env->NewObject(_stringBufferCls, _stringBufferMethodID);
    if (_stringBufferObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_STRINGBUFFER, CLASS_INIT, CLASS_INIT_SIGNATURE);
 		return 0;
    }

	jmethodID _fsGetPreviewBitmapMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSGETPREVIEWBITMAP, METHOD_WFXPLUGIN_FSGETPREVIEWBITMAP_SIGNATURE);
	if (_fsGetPreviewBitmapMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSGETPREVIEWBITMAP, METHOD_WFXPLUGIN_FSGETPREVIEWBITMAP_SIGNATURE);
		return 0;
	}

	/* WFXPlugin.fsGetPreviewBitmap(FileToLoad, width, height, contentbuf, contentbuflen); */
	jstring input = _env->NewStringUTF(RemoteName);
	jint wdt = width;
	jint hgt = height;
	jobject iconBuffer = _stringBufferObject;

	int ret = _env->CallIntMethod(_wfxJPluginObject, _fsGetPreviewBitmapMethodID, input, wdt, hgt, iconBuffer);
	if (exceptionHandling(_env, pluginClass, METHOD_WFXPLUGIN_FSGETPREVIEWBITMAP, METHOD_WFXPLUGIN_FSGETPREVIEWBITMAP_SIGNATURE)) {
		if (input != NULL) {
			_env->ReleaseStringUTFChars(input, 0);
		}
		return 0;
    }

	if (input != NULL) {
		_env->ReleaseStringUTFChars(input, 0);
	}

	char *iconName = (char*)malloc(MAX_PATH);
	jstring icon = (jstring)_env->CallObjectMethod(iconBuffer, _toStringMethodID);
	copyJstringBytes(_env, iconName, icon, MAX_PATH);
	if (strlen(iconName) > 0) { 
		//Bitmap handle 
		HBITMAP      hBitMap; 
		char *sep = strchr(iconName, '|');
		if (sep != NULL) {
			// 1. icon of a resource: "252|shell32.dll"
			HINSTANCE theIconFile= (HINSTANCE)LoadLibraryEx((char *)(sep+1), 0, LOAD_LIBRARY_AS_DATAFILE);
			*sep = '\0';
			hBitMap  = (HBITMAP)LoadImage(theIconFile, MAKEINTRESOURCE(atoi(iconName)),IMAGE_BITMAP, width, height, LR_DEFAULTSIZE|LR_LOADFROMFILE|LR_CREATEDIBSECTION);   
		} else {
			// 2. full path name: "%CWD%\\test.ico"
			// change current working directory
			char *lastBSlash = strrchr(moduleFilename, '\\');
			char cwd[MAX_PATH];
			memset(cwd,0,MAX_PATH);
			strncpy( cwd, moduleFilename, (lastBSlash-moduleFilename) );

			iconName = replace_str(iconName, "%CWD%", cwd);
			// Load a bitmap 
            hBitMap  = (HBITMAP)LoadImage(0,iconName,IMAGE_BITMAP, width, height, LR_DEFAULTSIZE|LR_LOADFROMFILE|LR_CREATEDIBSECTION);   
		}
		*ReturnedBitmap = hBitMap;
	}
	return ret;
}

BOOL __stdcall FsLinksToLocalFiles(void)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsLinksToLocalFilesMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSLINKSTOLOCALFILES, METHOD_WFXPLUGIN_FSLINKSTOLOCALFILES_SIGNATURE);
	if (_fsLinksToLocalFilesMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSLINKSTOLOCALFILES, METHOD_WFXPLUGIN_FSLINKSTOLOCALFILES_SIGNATURE);
		return FALSE;
	}

	/* FsLinksToLocalFiles(); */
	jboolean _output = _env->CallBooleanMethod(_wfxJPluginObject, _fsLinksToLocalFilesMethodID);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSLINKSTOLOCALFILES, METHOD_WFXPLUGIN_FSLINKSTOLOCALFILES_SIGNATURE)) {
		return FALSE;
    }
	return _output;
}

BOOL __stdcall FsGetLocalName(char* RemoteName,int Maxlen)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsGetLocalNameMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSGETLOCALNAME, METHOD_WFXPLUGIN_FSGETLOCALNAME_SIGNATURE);
	if (_fsGetLocalNameMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSGETLOCALNAME, METHOD_WFXPLUGIN_FSGETLOCALNAME_SIGNATURE);
		return FALSE;
	}

	jstring remoteName = _env->NewStringUTF(RemoteName);
	jint maxlen = Maxlen;

	/* FsGetLocalName(RemoteName,Maxlen); */
	jboolean _output = _env->CallBooleanMethod(_wfxJPluginObject, _fsGetLocalNameMethodID);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSGETLOCALNAME, METHOD_WFXPLUGIN_FSGETLOCALNAME_SIGNATURE)) {
		return FALSE;
    }
	return _output;
}

int __stdcall FsContentGetSupportedField(int FieldIndex,char* FieldName,
                char* Units,int maxlen)
{
	JNIEnv *_env= startJVM();
	if (_env == NULL) {
		return ft_nomorefields;
	}

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsContentGetSupportedFieldMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTFIELD, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTFIELD_SIGNATURE);
	if (_fsContentGetSupportedFieldMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTFIELD, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTFIELD_SIGNATURE);
		return ft_nomorefields;
	}

	jint index = (jint)FieldIndex;

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	// Retrieve StringBuffer
	jclass _stringBufferCls = _env->FindClass(CLASS_STRINGBUFFER);
	if (_stringBufferCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, CLASS_STRINGBUFFER);
		return 0;
	}

	jmethodID _stringBufferMethodID = _env->GetMethodID(_stringBufferCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_stringBufferMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRINGBUFFER, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
	}
		
	jobject _stringBufferNameObject = _env->NewObject(_stringBufferCls, _stringBufferMethodID);
    if (_stringBufferNameObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_STRINGBUFFER, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
    }
	jobject _stringBufferUnitsObject = _env->NewObject(_stringBufferCls, _stringBufferMethodID);
    if (_stringBufferUnitsObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_STRINGBUFFER, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
    }
	jint max = (jint)maxlen;

	/* WFXPlugin.FsContentGetSupportedField(FieldIndex, FieldName, Units, maxlen); */
	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsContentGetSupportedFieldMethodID, index, _stringBufferNameObject, _stringBufferUnitsObject, max);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTFIELD, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTFIELD_SIGNATURE)) {
		return ft_nomorefields;
    }

	jmethodID _toStringMethodID = _env->GetMethodID(_stringBufferCls, METHOD_STRINGBUFFER_TOSTRING, METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
	if (_toStringMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRINGBUFFER, METHOD_STRINGBUFFER_TOSTRING,METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
		return ft_nomorefields;
	}

	jstring name = (jstring)_env->CallObjectMethod(_stringBufferNameObject, _toStringMethodID);
	copyJstringBytes(_env, FieldName, name, maxlen);

	jstring units = (jstring)_env->CallObjectMethod(_stringBufferUnitsObject, _toStringMethodID);
	copyJstringBytes(_env, Units, units, maxlen);

	return _output;
}

int __stdcall FsContentGetValue(char* FileName,int FieldIndex,int UnitIndex,

                void* FieldValue,int maxlen,int flags)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsContentGetValueMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSCONTENTGETVALUE, METHOD_WFXPLUGIN_FSCONTENTGETVALUE_SIGNATURE);
	if (_fsContentGetValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETVALUE, METHOD_WFXPLUGIN_FSCONTENTGETVALUE_SIGNATURE);
		return ft_nomorefields;
	}

	jstring name = _env->NewStringUTF(FileName);
	jint index = (jint)FieldIndex;
	jint uindex = (jint)UnitIndex;

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _fieldValueCls = GetClass(_env, CLASS_FIELDVALUE);

	jmethodID _fieldValueMethodID = _env->GetMethodID(_fieldValueCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_fieldValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FIELDVALUE, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
	}

	/* new FieldValue() */
	jobject _wfxFieldValueObject = _env->NewObject(_fieldValueCls, _fieldValueMethodID);
    if (_wfxFieldValueObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_FIELDVALUE, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
    }
	jint max = (jint)maxlen;
	jint flag= (jint)flags;

	/* WFXPlugin.fsContentGetValue(FileName); */
	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsContentGetValueMethodID, name, index, uindex, _wfxFieldValueObject, max, flag);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETVALUE, METHOD_WFXPLUGIN_FSCONTENTGETVALUE_SIGNATURE)) {
		if (name != NULL) {
			_env->ReleaseStringUTFChars(name, 0);
		}
		return ft_nomorefields;
    }

	if (name != NULL) {
		_env->ReleaseStringUTFChars(name, 0);
	}

	jfieldID _fieldTypeID = _env->GetFieldID (_fieldValueCls, FIELD_FIELDTYPE, FIELD_FIELDTYPE_SIGNATURE);
	if (_fieldTypeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_FIELDTYPE, FIELD_FIELDTYPE_SIGNATURE);
		return NULL;
	}

	jint fieldType = _env->GetIntField(_wfxFieldValueObject, _fieldTypeID);
	if ((int)fieldType==ft_numeric_32) {

		jfieldID _intValueID = _env->GetFieldID (_fieldValueCls, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
		if (_intValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
 			return ft_nomorefields;
		}

		*(int *)FieldValue= (int)_env->GetIntField(_wfxFieldValueObject, _intValueID);
	} else if ((int)fieldType==ft_numeric_64) {

		jfieldID _longValueID = _env->GetFieldID (_fieldValueCls, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
		if (_longValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
 			return ft_nomorefields;
		}

		*(__int64*)FieldValue = (__int64)_env->GetLongField(_wfxFieldValueObject, _longValueID);
	} else if ((int)fieldType==ft_numeric_floating) {

		jfieldID _doubleValueID = _env->GetFieldID (_fieldValueCls, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
		if (_doubleValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
 			return ft_nomorefields;
		}

		*(double *)FieldValue= (double)_env->GetDoubleField(_wfxFieldValueObject, _doubleValueID);
	} else if ((int)fieldType==ft_date) {
		jfieldID _dateValueID = _env->GetFieldID (_fieldValueCls, FIELD_DATEVALUE, FIELD_DATEVALUE_SIGNATURE);
		if (_dateValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_DATEVALUE, FIELD_DATEVALUE_SIGNATURE);
 			return ft_nomorefields;
		}
		jclass _localDateCls = GetClass(_env, CLASS_LOCALDATE);

		jmethodID _localDateMethodID = _env->GetMethodID(_localDateCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
		if (_localDateMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_LOCALDATE, CLASS_INIT,CLASS_INIT_SIGNATURE);
 			return ft_nomorefields;
		}
		jfieldID _fYearID = _env->GetFieldID (_localDateCls, FIELD_YEAR, FIELD_YEAR_SIGNATURE);
		if (_fYearID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_YEAR, FIELD_YEAR_SIGNATURE);
 			return NULL;
		}
		jfieldID _fMonthID = _env->GetFieldID (_localDateCls, FIELD_MONTH, FIELD_MONTH_SIGNATURE);
		if (_fMonthID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_MONTH, FIELD_MONTH_SIGNATURE);
 			return NULL;
		}
		jfieldID _fDayID = _env->GetFieldID (_localDateCls, FIELD_DAY, FIELD_DAY_SIGNATURE);
		if (_fDayID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_DAY, FIELD_DAY_SIGNATURE);
 			return NULL;
		}

		int Year = (int)_env->GetIntField(_env->GetObjectField(_wfxFieldValueObject, _dateValueID), _fYearID);
		int Month =(int)_env->GetIntField(_env->GetObjectField(_wfxFieldValueObject, _dateValueID), _fMonthID);
		int Day =(int)_env->GetIntField(_env->GetObjectField(_wfxFieldValueObject, _dateValueID), _fDayID);
		((pdateformat)FieldValue)->wYear = Year;
		((pdateformat)FieldValue)->wMonth = Month;
		((pdateformat)FieldValue)->wDay = Day;
	} else if ((int)fieldType==ft_time) {
		jfieldID _timeValueID = _env->GetFieldID (_fieldValueCls, FIELD_TIMEVALUE, FIELD_TIMEVALUE_SIGNATURE);
		if (_timeValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_TIMEVALUE, FIELD_TIMEVALUE_SIGNATURE);
 			return ft_nomorefields;
		}

		jclass _localTimeCls = GetClass(_env, CLASS_LOCALTIME);

		jmethodID _localTimeMethodID = _env->GetMethodID(_localTimeCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
		if (_localTimeMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_LOCALTIME, CLASS_INIT,CLASS_INIT_SIGNATURE);
 			return NULL;
		}
		jfieldID _fHourID = _env->GetFieldID (_localTimeCls, FIELD_HOUR, FIELD_HOUR_SIGNATURE);
		if (_fHourID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_HOUR, FIELD_HOUR_SIGNATURE);
 			return NULL;
		}
		jfieldID _fMinuteID = _env->GetFieldID (_localTimeCls, FIELD_MINUTE, FIELD_MINUTE_SIGNATURE);
		if (_fMinuteID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_MINUTE, FIELD_MINUTE_SIGNATURE);
 			return NULL;
		}
		jfieldID _fSecondID = _env->GetFieldID (_localTimeCls, FIELD_SECOND, FIELD_SECOND_SIGNATURE);
		if (_fSecondID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_SECOND, FIELD_SECOND_SIGNATURE);
 			return NULL;
		}

		int Hour = (int)_env->GetIntField(_env->GetObjectField(_wfxFieldValueObject, _timeValueID), _fHourID);
    	int Minute = (int)_env->GetIntField(_env->GetObjectField(_wfxFieldValueObject, _timeValueID), _fMinuteID);
    	int Second = (int)_env->GetIntField(_env->GetObjectField(_wfxFieldValueObject, _timeValueID), _fSecondID);
		((ptimeformat)FieldValue)->wHour = Hour;
		((ptimeformat)FieldValue)->wMinute = Minute;
		((ptimeformat)FieldValue)->wSecond = Second;
	} else if ((int)fieldType==ft_boolean) {
		jfieldID _booleanValueID = _env->GetFieldID (_fieldValueCls, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
		if (_booleanValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
 			return ft_nomorefields;
		}

		*(bool *)FieldValue= (bool)_env->GetBooleanField(_wfxFieldValueObject, _booleanValueID);
	} else if ((int)fieldType==ft_datetime) {
		FILETIME ft;

		jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

		jfieldID _fileTimeID = _env->GetFieldID (_fieldValueCls, FIELD_FILETIME, FIELD_FILETIME_SIGNATURE);
		if (_fileTimeID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_FILETIME, FIELD_FILETIME_SIGNATURE);
 			return ft_nomorefields;
		}

		jfieldID _dwLowDateTimeID = _env->GetFieldID (_fileTimeCls, FIELD_DWLOWDATETIME, FIELD_DWLOWDATETIME_SIGNATURE);
		if (_dwLowDateTimeID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_WIN32FINDDATA, FIELD_DWLOWDATETIME, FIELD_DWLOWDATETIME_SIGNATURE);
			return ft_nomorefields;
		}
		jlong fileTime = _env->GetLongField(_env->GetObjectField(_wfxFieldValueObject, _fileTimeID), _dwLowDateTimeID);
		UnixTimeToFileTime(fileTime/1000, &ft);
		*(LPFILETIME)FieldValue= ft;
	} else if ((int)fieldType==ft_string || (int)fieldType==ft_multiplechoice || (int)fieldType==ft_fulltext) {
		jfieldID _strID = _env->GetFieldID (_fieldValueCls, FIELD_STR, FIELD_STR_SIGNATURE);
		if (_strID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_STR, FIELD_STR_SIGNATURE);
 			return ft_nomorefields;
		}

    	jstring str = (jstring)_env->GetObjectField(_wfxFieldValueObject, _strID);
		copyJstringBytes(_env, (char *)FieldValue, str, maxlen);
	}
	return _output;
}

void __stdcall FsContentStopGetValue(char* FileName)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsContentStopGetValueMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSCONTENTSTOPGETVALUE, METHOD_WFXPLUGIN_FSCONTENTSTOPGETVALUE_SIGNATURE);
	if (_fsContentStopGetValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTSTOPGETVALUE, METHOD_WFXPLUGIN_FSCONTENTSTOPGETVALUE_SIGNATURE);
		return;
	}

	/* WFXPlugin.fsContentStopGetValue(FileName); */
	jstring name = _env->NewStringUTF(FileName);

	_env->CallVoidMethod(_wfxJPluginObject, _fsContentStopGetValueMethodID, name);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTSTOPGETVALUE, METHOD_WFXPLUGIN_FSCONTENTSTOPGETVALUE_SIGNATURE)) {
		if (name != NULL) {
			_env->ReleaseStringUTFChars(name, 0);
		}
		return;
    }

	if (name != NULL) {
		_env->ReleaseStringUTFChars(name, 0);
	}
}

int __stdcall FsContentGetDefaultSortOrder(int FieldIndex)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsContentGetDefaultSortOrderMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTSORTORDER, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTSORTORDER_SIGNATURE);
	if (_fsContentGetDefaultSortOrderMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTSORTORDER, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTSORTORDER_SIGNATURE);
		return 0;
	}

	/* WFXPlugin.fsContentGetDefaultSortOrder(FieldIndex); */
	jint index = (jint)FieldIndex;
	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsContentGetDefaultSortOrderMethodID, index);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTSORTORDER, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTSORTORDER_SIGNATURE)) {
		return 0;
    }
	return _output;
}

void __stdcall FsContentPluginUnloading(void) {
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsContentPluginUnloadingMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSCONTENTPLUGINUNLOADING, METHOD_WFXPLUGIN_FSCONTENTPLUGINUNLOADING_SIGNATURE);
	if (_fsContentPluginUnloadingMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTPLUGINUNLOADING, METHOD_WFXPLUGIN_FSCONTENTPLUGINUNLOADING_SIGNATURE);
		return;
	}

	/* WFXPlugin.FsContentPluginUnloading(); */
	_env->CallVoidMethod(_wfxJPluginObject, _fsContentPluginUnloadingMethodID);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTPLUGINUNLOADING, METHOD_WFXPLUGIN_FSCONTENTPLUGINUNLOADING_SIGNATURE)) {
		return;
    }
}

int __stdcall FsContentGetSupportedFieldFlags(int FieldIndex)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsContentGetSupportedFieldFlagsMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTEDFIELDFLAGS, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTEDFIELDFLAGS_SIGNATURE);
	if (_fsContentGetSupportedFieldFlagsMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTEDFIELDFLAGS, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTEDFIELDFLAGS);
		return 0;
	}

	/* WFXPlugin.FsContentGetSupportedFieldFlags(FieldIndex); */
	jint index = (jint)FieldIndex;
	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsContentGetSupportedFieldFlagsMethodID, index);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTEDFIELDFLAGS, METHOD_WFXPLUGIN_FSCONTENTGETSUPPORTEDFIELDFLAGS_SIGNATURE)) {
		return 0;
    }
	return _output;
}

int __stdcall FsContentSetValue(char* FileName,int FieldIndex,int UnitIndex,int FieldType,
                void* FieldValue,int flags)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nosuchfield;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsContentSetValueMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSCONTENTSETVALUE, METHOD_WFXPLUGIN_FSCONTENTSETVALUE_SIGNATURE);
	if (_fsContentSetValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTSETVALUE, METHOD_WFXPLUGIN_FSCONTENTSETVALUE_SIGNATURE);
		return ft_nosuchfield;
	}

	jstring name = _env->NewStringUTF(FileName);
	jint index = (jint)FieldIndex;
	jint uindex = (jint)UnitIndex;
	jint fieldType = (jint)FieldType;

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _fieldValueCls = GetClass(_env, CLASS_FIELDVALUE);

	jmethodID _fieldValueMethodID = _env->GetMethodID(_fieldValueCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_fieldValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FIELDVALUE, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
	}

	/* new FieldValue() */
	jobject _wfxFieldValueObject = _env->NewObject(_fieldValueCls, _fieldValueMethodID);
    if (_wfxFieldValueObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_FIELDVALUE, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return ft_nosuchfield;
    }
	jobject _fieldValue;
	if ((int)fieldType==ft_numeric_32) {
		signed int val = *((signed int*)FieldValue);

		jfieldID _intValueID = _env->GetFieldID (_fieldValueCls, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
		if (_intValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}
		_env->SetIntField(_wfxFieldValueObject, _intValueID, (jint) val);
	} else if ((int)fieldType==ft_numeric_64) {
		__int64 val = *((__int64*)FieldValue);

		jfieldID _longValueID = _env->GetFieldID (_fieldValueCls, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
		if (_longValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetLongField(_wfxFieldValueObject, _longValueID, (jlong) val);
	} else if ((int)fieldType==ft_numeric_floating) {
		double val = *((double*)FieldValue);

		jfieldID _doubleValueID = _env->GetFieldID (_fieldValueCls, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
		if (_doubleValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetDoubleField(_wfxFieldValueObject, _doubleValueID, (jdouble) val);
	} else if ((int)fieldType==ft_date) {
		jclass _localDateCls = GetClass(_env, CLASS_LOCALDATE);

		jmethodID _localDateMethodID = _env->GetMethodID(_localDateCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
		if (_localDateMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_LOCALDATE, CLASS_INIT,CLASS_INIT_SIGNATURE);
 			return ft_nomorefields;
		}
		jfieldID _fYearID = _env->GetFieldID (_localDateCls, FIELD_YEAR, FIELD_YEAR_SIGNATURE);
		if (_fYearID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_YEAR, FIELD_YEAR_SIGNATURE);
 			return NULL;
		}
		jfieldID _fMonthID = _env->GetFieldID (_localDateCls, FIELD_MONTH, FIELD_MONTH_SIGNATURE);
		if (_fMonthID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_MONTH, FIELD_MONTH_SIGNATURE);
 			return NULL;
		}
		jfieldID _fDayID = _env->GetFieldID (_localDateCls, FIELD_DAY, FIELD_DAY_SIGNATURE);
		if (_fDayID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_DAY, FIELD_DAY_SIGNATURE);
 			return NULL;
		}

		jobject _localDate = _env->NewObject(_localDateCls, _localDateMethodID);
		_env->SetIntField(_localDate, _fYearID, ((pdateformat)FieldValue)->wYear);
		_env->SetIntField(_localDate, _fMonthID, ((pdateformat)FieldValue)->wMonth);
		_env->SetIntField(_localDate, _fDayID, ((pdateformat)FieldValue)->wDay);

		jfieldID _dateValueID = _env->GetFieldID (_fieldValueCls, FIELD_DATEVALUE, FIELD_DATEVALUE_SIGNATURE);
		if (_dateValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_DATEVALUE, FIELD_DATEVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}
		_env->SetObjectField(_wfxFieldValueObject, _dateValueID, _localDate);
	} else if ((int)fieldType==ft_time) {

				jclass _localTimeCls = GetClass(_env, CLASS_LOCALTIME);

		jmethodID _localTimeMethodID = _env->GetMethodID(_localTimeCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
		if (_localTimeMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_LOCALTIME, CLASS_INIT,CLASS_INIT_SIGNATURE);
 			return NULL;
		}
		jfieldID _fHourID = _env->GetFieldID (_localTimeCls, FIELD_HOUR, FIELD_HOUR_SIGNATURE);
		if (_fHourID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_HOUR, FIELD_HOUR_SIGNATURE);
 			return NULL;
		}
		jfieldID _fMinuteID = _env->GetFieldID (_localTimeCls, FIELD_MINUTE, FIELD_MINUTE_SIGNATURE);
		if (_fMinuteID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_MINUTE, FIELD_MINUTE_SIGNATURE);
 			return NULL;
		}
		jfieldID _fSecondID = _env->GetFieldID (_localTimeCls, FIELD_SECOND, FIELD_SECOND_SIGNATURE);
		if (_fSecondID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_LOCALDATE, FIELD_SECOND, FIELD_SECOND_SIGNATURE);
 			return NULL;
		}

		jobject _localTime = _env->NewObject(_localTimeCls, _localTimeMethodID);
		_env->SetIntField(_localTime, _fHourID, ((ptimeformat)FieldValue)->wHour);
		_env->SetIntField(_localTime, _fMinuteID, ((ptimeformat)FieldValue)->wMinute);
		_env->SetIntField(_localTime, _fSecondID, ((ptimeformat)FieldValue)->wSecond);

		jfieldID _timeValueID = _env->GetFieldID (_fieldValueCls, FIELD_TIMEVALUE, FIELD_TIMEVALUE_SIGNATURE);
		if (_timeValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_TIMEVALUE, FIELD_TIMEVALUE_SIGNATURE);
			return ft_nosuchfield;
		}
		_env->SetObjectField(_wfxFieldValueObject, _timeValueID, _localTime);
	} else if ((int)fieldType==ft_boolean) {
		int val = *((int*)FieldValue);

		jfieldID _booleanValueID = _env->GetFieldID (_fieldValueCls, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
		if (_booleanValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetBooleanField(_wfxFieldValueObject, _booleanValueID, (jboolean) val);
	} else if ((int)fieldType==ft_datetime) {
		FILETIME ft = *((FILETIME*)FieldValue);

		jobject _jClassLoaderObject = GetPluginClassLoader(_env);
		
		jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

		jmethodID _fileTimeMethodID = _env->GetMethodID(_fileTimeCls, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
		if (_fileTimeMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
			return ft_nosuchfield;
		}

		jobject _fileTime = _env->NewObject(_fileTimeCls, _fileTimeMethodID, (jlong)ft.dwLowDateTime, (jlong)ft.dwHighDateTime);

		jfieldID _fileTimeID = _env->GetFieldID (_fieldValueCls, FIELD_FILETIME, FIELD_FILETIME_SIGNATURE);
		if (_fileTimeID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_FILETIME, FIELD_FILETIME_SIGNATURE);
 			return ft_nosuchfield;
		}
		_env->SetObjectField(_wfxFieldValueObject, _fileTimeID, _fileTime);
	} else if ((int)fieldType==ft_string || (int)fieldType==ft_fulltext) {
		jstring str = _env->NewStringUTF((char *)FieldValue);

		jfieldID _strID = _env->GetFieldID (_fieldValueCls, FIELD_STR, FIELD_STR_SIGNATURE);
		if (_strID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_STR, FIELD_STR_SIGNATURE);
 			return ft_nosuchfield;
		}
		_env->SetObjectField(_wfxFieldValueObject, _strID, str);
	} else if ((int)fieldType==ft_multiplechoice) {
		char *val = *((char **)FieldValue);
		jstring str = _env->NewStringUTF(val);

		jfieldID _strID = _env->GetFieldID (_fieldValueCls, FIELD_STR, FIELD_STR_SIGNATURE);
		if (_strID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_STR, FIELD_STR_SIGNATURE);
 			return ft_nosuchfield;
		}
		_env->SetObjectField(_wfxFieldValueObject, _strID, str);
	}
	jfieldID _fieldTypeID = _env->GetFieldID (_fieldValueCls, FIELD_FIELDTYPE, FIELD_FIELDTYPE_SIGNATURE);
	if (_fieldTypeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_FIELDTYPE, FIELD_FIELDTYPE_SIGNATURE);
		return ft_nosuchfield;
	}
	_env->SetIntField(_wfxFieldValueObject, _fieldTypeID, fieldType);
	jint flag= (jint)flags;

	/* WFXPlugin.FsContentSetValue(FileName, FieldIndex, UnitIndex, FieldType, FieldValue, flags); */
	jint _output = _env->CallIntMethod(_wfxJPluginObject, _fsContentSetValueMethodID, name, index, uindex, fieldType, _wfxFieldValueObject, flag);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTSETVALUE, METHOD_WFXPLUGIN_FSCONTENTSETVALUE_SIGNATURE)) {
		if (name != NULL) {
			_env->ReleaseStringUTFChars(name, 0);
		}
		return ft_nosuchfield;
    }

	if (name != NULL) {
		_env->ReleaseStringUTFChars(name, 0);
	}
	return _output;
}

BOOL __stdcall FsContentGetDefaultView(char* ViewContents,char* ViewHeaders,char* ViewWidths,char* ViewOptions,int Maxlen)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wfxJPluginObject = GetPlugin(_env, wfxPluginClass);
	if (_wfxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wfxCls = _env->GetObjectClass(_wfxJPluginObject);

	jmethodID _fsContentGetDefaultViewMethodID = _env->GetMethodID(_wfxCls, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTVIEW, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTVIEW_SIGNATURE);
	if (_fsContentGetDefaultViewMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTVIEW, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTVIEW_SIGNATURE);
		return FALSE;
	}		

	jstring viewContents = _env->NewStringUTF(ViewContents);
	jstring viewHeaders = _env->NewStringUTF(ViewHeaders);
	jstring viewWidths = _env->NewStringUTF(ViewWidths);
	jstring viewOptions = _env->NewStringUTF(ViewOptions);
	jint maxlen = (jint)Maxlen;

	/* WFXPlugin.FsContentGetDefaultView(ViewContents, ViewHeaders, ViewWidths, ViewOptions, Maxlen); */
	jboolean _output = _env->CallBooleanMethod(_wfxJPluginObject, _fsContentGetDefaultViewMethodID, viewContents, viewHeaders, viewWidths, viewOptions, maxlen);
	if (exceptionHandling(_env, wfxPluginClass, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTVIEW, METHOD_WFXPLUGIN_FSCONTENTGETDEFAULTVIEW_SIGNATURE)) {
		return FALSE;
    }
	return _output;
}

/*
 * Class:     plugins_wfx_WFXPluginAdapter
 * Method:    fsSetProgress
 * Signature: (ILjava/lang/String;Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_plugins_wfx_WFXPluginAdapter_fsSetProgress
  (JNIEnv *_env, jobject obj, jint pluginNr, jstring sourceName, jstring targetName, jint percentDone)
{
	int PluginNr= pluginNr;
    char SourceName[MAX_MESSAGE]; copyJstringBytes(_env, SourceName, sourceName, MAX_PATH);
	char TargetName[MAX_MESSAGE]; copyJstringBytes(_env, TargetName, targetName, MAX_PATH);
	int PercentDone = percentDone;
	int ret = ProgressProc(PluginNr, (char *)SourceName, (char *)TargetName, PercentDone);

	return ret;
}

/*
 * Class:     plugins_wfx_WFXPluginAdapter
 * Method:    fsLog
 * Signature: (IILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_plugins_wfx_WFXPluginAdapter_fsLog
  (JNIEnv *_env, jobject obj, jint pluginNr, jint msgType, jstring logString)
{
	int PluginNr= (int)pluginNr;
	int MsgType= (int)msgType;
	const char *LogString = NULL;
	if (logString!=NULL) {
		LogString = _env->GetStringUTFChars(logString, 0);
	}
	LogProc(PluginNr, MsgType, (char *)LogString);

	if (logString != NULL) {
		_env->ReleaseStringUTFChars(logString, 0);
	}
}

/*
 * Class:     plugins_wfx_WFXPluginAdapter
 * Method:    fsRequest
 * Signature: (IILjava/lang/String;Ljava/lang/String;Ljava/lang/StringBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_plugins_wfx_WFXPluginAdapter_fsRequest
  (JNIEnv *_env, jobject obj, jint pluginNr, jint requestType, jstring customTitle, jstring customText, jobject returnedText)
{
	int PluginNr= (int)pluginNr;
	int RequestType= (int)requestType;
	jboolean iscopy;
	const char *CustomTitle= NULL;
	const char *CustomText= NULL;
	char *ReturnedText= (char *)malloc(MAX_MESSAGE);
	ReturnedText[0]='\0';
	if (customTitle!=NULL) {
		CustomTitle = _env->GetStringUTFChars(customTitle, &iscopy);
	}
	if (customText!=NULL) {
		CustomText = _env->GetStringUTFChars(customText, &iscopy);
	}
	jstring _toString;
	if (returnedText != NULL) {

		jobject _jClassLoaderObject = GetPluginClassLoader(_env);
		
		// Retrieve StringBuffer
		jclass _stringBufferCls = _env->FindClass(CLASS_STRINGBUFFER);
		if (_stringBufferCls == NULL) {
			errorDialog(ERR_CLASS_NOT_FOUND, CLASS_STRINGBUFFER);
			return 0;
		}

		jmethodID _toStringMethodID = _env->GetMethodID(_stringBufferCls, METHOD_STRINGBUFFER_TOSTRING, METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
		if (_toStringMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRINGBUFFER, METHOD_STRINGBUFFER_TOSTRING,METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
			return 0;
		}
			
		_toString = (jstring)_env->CallObjectMethod(returnedText, _toStringMethodID);
		strcpy(ReturnedText, _env->GetStringUTFChars( _toString, &iscopy ));
	}

	bool ok = RequestProc(PluginNr, RequestType, (char *)CustomTitle, (char *)CustomText, (char *)ReturnedText, MAX_MESSAGE);

	jstring retReturnedText = _env->NewStringUTF(ReturnedText);

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	// Retrieve StringBuffer
	jclass _stringBufferCls = _env->FindClass(CLASS_STRINGBUFFER);
	if (_stringBufferCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, CLASS_STRINGBUFFER);
		return 0;
	}

	jmethodID _appendMethodID = _env->GetMethodID(_stringBufferCls, METHOD_STRINGBUFFER_APPEND, METHOD_STRINGBUFFER_APPEND_SIGNATURE);
	if (_appendMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRINGBUFFER, METHOD_STRINGBUFFER_APPEND,METHOD_STRINGBUFFER_APPEND_SIGNATURE);
		return 0;
	}

	_env->CallObjectMethod(returnedText, _appendMethodID, retReturnedText);
	return ok;
}
