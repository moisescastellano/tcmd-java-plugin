#include "stdafx.h"
#include "wcxhead.h"
#include "java.h"
#include "plugins_wcx_WCXPluginAdapter.h"

// change volume dialogue dialogue
tChangeVolProc ChangeVolProc1;
// progress bar dialogue
tProcessDataProc ProcessDataProc;

EXTERN jobject __stdcall GetPlugin(JNIEnv *, char *);

EXTERN jobject __stdcall GetPluginClassLoader(JNIEnv *);

EXTERN jclass __stdcall GetClass(JNIEnv *, char *);

HANDLE __stdcall OpenArchive (tOpenArchiveData *ArchiveData)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return NULL;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxOpenArchiveMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_OPENARCHIVE, METHOD_WCXPLUGIN_OPENARCHIVE_SIGNATURE);
	if (_wcxOpenArchiveMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_OPENARCHIVE, METHOD_WCXPLUGIN_OPENARCHIVE_SIGNATURE);
		return NULL;
	}

	/* new OpenArchiveData(archiveName, openMode, openResult) */
	jstring archiveName= _env->NewStringUTF(ArchiveData->ArcName);
	jint openMode= ArchiveData->OpenMode;
	jint openResult= ArchiveData->OpenResult;

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _openArchiveDataCls = GetClass(_env, CLASS_OPENARCHIVEDATA);

	jmethodID _openArchiveDataMethodID = _env->GetMethodID(_openArchiveDataCls, CLASS_INIT,CLASS_OPENARCHIVEDATA_INIT_SIGNATURE);
	if (_openArchiveDataMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_OPENARCHIVEDATA, CLASS_INIT,CLASS_OPENARCHIVEDATA_INIT_SIGNATURE);
		return NULL;
	}

	jobject _wcxOpenArchiveDataObject = _env->NewObject(_openArchiveDataCls, _openArchiveDataMethodID, archiveName, openMode, openResult);
    if (_wcxOpenArchiveDataObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_OPENARCHIVEDATA, CLASS_INIT, CLASS_OPENARCHIVEDATA_INIT_SIGNATURE);
		return NULL;
    }

	// WCXPlugin.openArchive(_wcxOpenArchiveDataObject);
	jobject ret =_env->CallObjectMethod(_wcxJPluginObject, _wcxOpenArchiveMethodID, _wcxOpenArchiveDataObject);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_OPENARCHIVE, METHOD_WCXPLUGIN_OPENARCHIVE_SIGNATURE)) {
		if (archiveName != NULL) {
			_env->ReleaseStringUTFChars(archiveName, 0);
		}
		return NULL;
    }
	if (archiveName != NULL) {
		_env->ReleaseStringUTFChars(archiveName, 0);
	}

	jfieldID _fOpenResultID = _env->GetFieldID (_openArchiveDataCls, FIELD_OPENRESULT, FIELD_OPENRESULT_SIGNATURE);
	if (_fOpenResultID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_OPENARCHIVEDATA, FIELD_OPENRESULT, FIELD_OPENRESULT_SIGNATURE);
		return NULL;
	}
	ArchiveData->OpenResult = _env->GetIntField(_wcxOpenArchiveDataObject, _fOpenResultID);

	return ret;
}

int __stdcall ReadHeader (HANDLE hArcData, tHeaderData *HeaderData)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return E_END_ARCHIVE;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxReadHeaderMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_READHEADER, METHOD_WCXPLUGIN_READHEADER_SIGNATURE);
	if (_wcxReadHeaderMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_READHEADER, METHOD_WCXPLUGIN_READHEADER_SIGNATURE);
		return E_END_ARCHIVE;
	}

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _headerDataCls = GetClass(_env, CLASS_HEADERDATA);

	jmethodID _headerDataMethodID = _env->GetMethodID(_headerDataCls, CLASS_INIT,CLASS_HEADERDATA_INIT_SIGNATURE);
	if (_headerDataMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_HEADERDATA, CLASS_INIT,CLASS_HEADERDATA_INIT_SIGNATURE);
		return E_END_ARCHIVE;
	}
	jfieldID _fArcNameID = _env->GetFieldID (_headerDataCls, FIELD_FARCNAME, FIELD_FARCNAME_SIGNATURE);
	if (_fArcNameID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FARCNAME, FIELD_FARCNAME_SIGNATURE);
		return NULL;
	}
	jfieldID _fFileNameID = _env->GetFieldID (_headerDataCls, FIELD_FFILENAME, FIELD_FFILENAME_SIGNATURE);
	if (_fFileNameID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FFILENAME, FIELD_FFILENAME_SIGNATURE);
		return NULL;
	}
	jfieldID _fFlagsID = _env->GetFieldID (_headerDataCls, FIELD_FFLAGS, FIELD_FFLAGS_SIGNATURE);
	if (_fFlagsID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FFLAGS, FIELD_FFLAGS_SIGNATURE);
		return NULL;
	}
	jfieldID _fPackSizeID = _env->GetFieldID (_headerDataCls, FIELD_FPACKSIZE, FIELD_FPACKSIZE_SIGNATURE);
	if (_fPackSizeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FPACKSIZE, FIELD_FPACKSIZE_SIGNATURE);
		return NULL;
	}
	jfieldID _fUnpSizeID = _env->GetFieldID (_headerDataCls, FIELD_FUNPSIZE, FIELD_FUNPSIZE_SIGNATURE);
	if (_fUnpSizeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FUNPSIZE, FIELD_FUNPSIZE_SIGNATURE);
		return NULL;
	}
	jfieldID _fHostOsID = _env->GetFieldID (_headerDataCls, FIELD_FHOSTOS, FIELD_FHOSTOS_SIGNATURE);
	if (_fHostOsID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FHOSTOS, FIELD_FHOSTOS_SIGNATURE);
		return NULL;
	}
	jfieldID _fFileCRCID = _env->GetFieldID (_headerDataCls, FIELD_FFILECRC, FIELD_FFILECRC_SIGNATURE);
	if (_fFileCRCID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FFILECRC, FIELD_FFILECRC_SIGNATURE);
		return NULL;
	}
	jfieldID _fFileTimeID = _env->GetFieldID (_headerDataCls, FIELD_FFILETIME, FIELD_FFILETIME_SIGNATURE);
	if (_fFileTimeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FFILETIME, FIELD_FFILETIME_SIGNATURE);
		return NULL;
	}
	jfieldID _fUnpVerID = _env->GetFieldID (_headerDataCls, FIELD_FUNPVER, FIELD_FUNPVER_SIGNATURE);
	if (_fUnpVerID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FUNPVER, FIELD_FUNPVER_SIGNATURE);
		return NULL;
	}
	jfieldID _fMethodID = _env->GetFieldID (_headerDataCls, FIELD_FMETHOD, FIELD_FMETHOD_SIGNATURE);
	if (_fMethodID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FMETHOD, FIELD_FMETHOD_SIGNATURE);
		return NULL;
	}
	jfieldID _fFileAttrID = _env->GetFieldID (_headerDataCls, FIELD_FFILEATTR, FIELD_FFILEATTR_SIGNATURE);
	if (_fFileAttrID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATA, FIELD_FFILEATTR, FIELD_FFILEATTR_SIGNATURE);
		return NULL;
	}

	jobject _wcxHeaderDataObject = _env->NewObject(_headerDataCls, _headerDataMethodID);
    if (_wcxHeaderDataObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_HEADERDATA, CLASS_INIT, CLASS_HEADERDATA_INIT_SIGNATURE);
		return E_END_ARCHIVE;
    }
	// WCXPlugin.readHeader(_wcxheaderDataObject);
	jint ret = _env->CallIntMethod(_wcxJPluginObject, _wcxReadHeaderMethodID, (jobject)hArcData, _wcxHeaderDataObject);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_READHEADER, METHOD_WCXPLUGIN_READHEADER_SIGNATURE)) {
		return E_END_ARCHIVE;
    }

	copyJstringBytes(_env, HeaderData->ArcName, (jstring)_env->GetObjectField(_wcxHeaderDataObject, _fArcNameID), MAX_PATH);
	copyJstringBytes(_env, HeaderData->FileName, (jstring)_env->GetObjectField(_wcxHeaderDataObject, _fFileNameID), MAX_PATH);
	HeaderData->Flags = _env->GetIntField(_wcxHeaderDataObject, _fFlagsID);
	HeaderData->PackSize = _env->GetLongField(_wcxHeaderDataObject, _fPackSizeID);
	HeaderData->UnpSize = _env->GetLongField(_wcxHeaderDataObject, _fUnpSizeID);
	HeaderData->HostOS = _env->GetIntField(_wcxHeaderDataObject, _fHostOsID);
	HeaderData->FileCRC = _env->GetLongField(_wcxHeaderDataObject, _fFileCRCID);
	HeaderData->FileTime = _env->GetLongField(_wcxHeaderDataObject, _fFileTimeID);
	HeaderData->UnpVer = _env->GetIntField(_wcxHeaderDataObject, _fUnpVerID);
	HeaderData->Method = _env->GetIntField(_wcxHeaderDataObject, _fMethodID);
	HeaderData->FileAttr = _env->GetIntField(_wcxHeaderDataObject, _fFileAttrID);

	return ret;
}

int __stdcall ProcessFile (HANDLE hArcData, int Operation, char *DestPath, char *DestName)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxProcessFileMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_PROCESSFILE, METHOD_WCXPLUGIN_PROCESSFILE_SIGNATURE);
	if (_wcxProcessFileMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_PROCESSFILE, METHOD_WCXPLUGIN_PROCESSFILE_SIGNATURE);
		return 0;
	}

	jobject archiveData = (jobject)hArcData;
	jint operation = Operation;
	jstring destPath = _env->NewStringUTF(DestPath);
	jstring destName = _env->NewStringUTF(DestName);

	// WCXPlugin.processFile(hArcData, Operation, DestPath, DestName);
	jint ret = _env->CallIntMethod(_wcxJPluginObject, _wcxProcessFileMethodID, archiveData, operation, destPath, destName);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_PROCESSFILE, METHOD_WCXPLUGIN_PROCESSFILE_SIGNATURE)) {
		if (destPath != NULL) {
			_env->ReleaseStringUTFChars(destPath, 0);
		}
		if (destName != NULL) {
			_env->ReleaseStringUTFChars(destName, 0);
		}
		return 0;
    }

	if (destPath != NULL) {
		_env->ReleaseStringUTFChars(destPath, 0);
	}
	if (destName != NULL) {
		_env->ReleaseStringUTFChars(destName, 0);
	}
	return ret;
}

int __stdcall CloseArchive (HANDLE hArcData)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxCloseArchiveMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_CLOSEARCHIVE, METHOD_WCXPLUGIN_CLOSEARCHIVE_SIGNATURE);
	if (_wcxCloseArchiveMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_CLOSEARCHIVE, METHOD_WCXPLUGIN_CLOSEARCHIVE_SIGNATURE);
		return 0;
	}

	jobject archiveData = (jobject) hArcData;

	// WCXPlugin.closeArchive(hArcData);
	jint ret = _env->CallIntMethod(_wcxJPluginObject, _wcxCloseArchiveMethodID, archiveData);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_CLOSEARCHIVE, METHOD_WCXPLUGIN_CLOSEARCHIVE_SIGNATURE)) {
		return 0;
    }
	return ret;
}

int __stdcall PackFiles (char *PackedFile, char *SubPath, char *SrcPath, char *AddList, int Flags)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxPackFilesMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_PACKFILES, METHOD_WCXPLUGIN_PACKFILES_SIGNATURE);
	if (_wcxPackFilesMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_PACKFILES, METHOD_WCXPLUGIN_PACKFILES_SIGNATURE);
		return NULL;
	}

	ULONG i = 0;
	while ( AddList[i] )
	{
		while ( AddList[i] ) i++;
		AddList[i] = ':';
		i++;
	}
	jstring packedFile = _env->NewStringUTF(PackedFile);
	jstring subPath = _env->NewStringUTF(SubPath);
	jstring srcPath = _env->NewStringUTF(SrcPath);
	jstring addList = _env->NewStringUTF(AddList);
	jint flags = Flags;

	// WCXPlugin.packFiles(PackedFile, SubPath, SrcPath, AddList, Flags);
	jint ret = _env->CallIntMethod(_wcxJPluginObject, _wcxPackFilesMethodID, packedFile, subPath, srcPath, addList, flags);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_PACKFILES, METHOD_WCXPLUGIN_PACKFILES_SIGNATURE)) {
		if (packedFile != NULL) {
			_env->ReleaseStringUTFChars(packedFile, 0);
		}
		if (subPath != NULL) {
			_env->ReleaseStringUTFChars(subPath, 0);
		}
		if (srcPath != NULL) {
			_env->ReleaseStringUTFChars(srcPath, 0);
		}
		if (addList != NULL) {
			_env->ReleaseStringUTFChars(addList, 0);
		}
		return 0;
    }

	if (packedFile != NULL) {
		_env->ReleaseStringUTFChars(packedFile, 0);
	}
	if (subPath != NULL) {
		_env->ReleaseStringUTFChars(subPath, 0);
	}
	if (srcPath != NULL) {
		_env->ReleaseStringUTFChars(srcPath, 0);
	}
	if (addList != NULL) {
		_env->ReleaseStringUTFChars(addList, 0);
	}
	return ret;
}

int __stdcall DeleteFiles (char *PackedFile, char *DeleteList)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxDeleteFilesMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_DELETEFILES, METHOD_WCXPLUGIN_DELETEFILES_SIGNATURE);
	if (_wcxDeleteFilesMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_DELETEFILES, METHOD_WCXPLUGIN_DELETEFILES_SIGNATURE);
		return 0;
	}

	jstring packedFile = _env->NewStringUTF(PackedFile);
	jstring deleteList = _env->NewStringUTF(DeleteList);

	// WCXPlugin.deleteFiles(PackedFile, DeleteList);
	jint ret = _env->CallIntMethod(_wcxJPluginObject, _wcxDeleteFilesMethodID, packedFile, deleteList);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_DELETEFILES, METHOD_WCXPLUGIN_DELETEFILES_SIGNATURE)) {
		if (packedFile != NULL) {
			_env->ReleaseStringUTFChars(packedFile, 0);
		}
		if (deleteList != NULL) {
			_env->ReleaseStringUTFChars(deleteList, 0);
		}
		return 0;
    }

	if (packedFile != NULL) {
		_env->ReleaseStringUTFChars(packedFile, 0);
	}
	if (deleteList != NULL) {
		_env->ReleaseStringUTFChars(deleteList, 0);
	}
	return ret;
}

int __stdcall GetPackerCaps()
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxGetPackerCapsMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_GETPACKERCAPS, METHOD_WCXPLUGIN_GETPACKERCAPS_SIGNATURE);
	if (_wcxGetPackerCapsMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_GETPACKERCAPS, METHOD_WCXPLUGIN_GETPACKERCAPS_SIGNATURE);
		return 0;
	}

	// WCXPlugin.GetPackerCaps();
	jint ret = _env->CallIntMethod(_wcxJPluginObject, _wcxGetPackerCapsMethodID);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_GETPACKERCAPS, METHOD_WCXPLUGIN_GETPACKERCAPS_SIGNATURE)) {
		return 0;
    }
	return ret;
}

void __stdcall ConfigurePacker (HWND Parent, HINSTANCE DllInstance)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxConfigurePackerMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_CONFIGUREPACKER, METHOD_WCXPLUGIN_CONFIGUREPACKER_SIGNATURE);
	if (_wcxConfigurePackerMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_CONFIGUREPACKER, METHOD_WCXPLUGIN_CONFIGUREPACKER_SIGNATURE);
		return;
	}

	// WCXPlugin.configurePacker(Parent);
	jint handle = _env->CallIntMethod(_wcxJPluginObject, _wcxConfigurePackerMethodID, (jint)Parent);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_CONFIGUREPACKER, METHOD_WCXPLUGIN_CONFIGUREPACKER_SIGNATURE)) {
		return;
    }
	SetParent((HWND)handle, Parent);
}

void __stdcall SetChangeVolProc (HANDLE hArcData, tChangeVolProc pChangeVolProc1)
{
	ChangeVolProc1=pChangeVolProc1;
}

void __stdcall SetProcessDataProc (HANDLE hArcData, tProcessDataProc pProcessDataProc)
{
	ProcessDataProc=pProcessDataProc;
}

int __stdcall StartMemPack (int Options, char *FileName)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxStartMemPackMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_STARTMEMPACK, METHOD_WCXPLUGIN_STARTMEMPACK_SIGNATURE);
	if (_wcxStartMemPackMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_STARTMEMPACK, METHOD_WCXPLUGIN_STARTMEMPACK_SIGNATURE);
		return 0;
	}

	jint options = (jint)Options;
	jstring filename = _env->NewStringUTF(FileName);

	// WCXPlugin.startPakToMem();
	jobject ret = _env->CallObjectMethod(_wcxJPluginObject, _wcxStartMemPackMethodID, options, filename);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_STARTMEMPACK, METHOD_WCXPLUGIN_STARTMEMPACK_SIGNATURE)) {
		if (filename != NULL) {
			_env->ReleaseStringUTFChars(filename, 0);
		}
		return 0;
    }

	if (filename != NULL) {
		_env->ReleaseStringUTFChars(filename, 0);
	}
	return (int)ret;
}

int __stdcall PackToMem (int hMemPack,char* BufIn,int InLen,int* Taken,char* BufOut,
                         int OutLen,int* Written,int SeekBy)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return MEMPACK_DONE;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxPackToMemMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_PACKTOMEM, METHOD_WCXPLUGIN_PACKTOMEM_SIGNATURE);
	if (_wcxPackToMemMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_PACKTOMEM, METHOD_WCXPLUGIN_PACKTOMEM_SIGNATURE);
		return MEMPACK_DONE;
	}

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _packToMemCls = GetClass(_env, CLASS_PACKTOMEM);

	jmethodID _packToMemMethodID = _env->GetMethodID(_packToMemCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_packToMemMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_PACKTOMEM, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return MEMPACK_DONE;
	}
	jfieldID _fTakenID = _env->GetFieldID (_packToMemCls, FIELD_FTAKEN, FIELD_FTAKEN_SIGNATURE);
	if (_fTakenID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_PACKTOMEM, FIELD_FTAKEN, FIELD_FTAKEN_SIGNATURE);
		return NULL;
	}
	jfieldID _fWrittenID = _env->GetFieldID (_packToMemCls, FIELD_FWRITTEN, FIELD_FWRITTEN_SIGNATURE);
	if (_fWrittenID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_PACKTOMEM, FIELD_FWRITTEN, FIELD_FWRITTEN_SIGNATURE);
		return NULL;
	}

	jobject _wcxPackToMemObject = _env->NewObject(_packToMemCls, _packToMemMethodID);
    if (_wcxPackToMemObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_PACKTOMEM, CLASS_INIT, CLASS_PACKTOMEM_INIT_SIGNATURE);
		return MEMPACK_DONE;
    }
	jstring bufIn = _env->NewStringUTF(BufIn);
	jint inLen = (jint)InLen;
	jstring bufOut = _env->NewStringUTF(BufOut);
	jint outLen = (jint)OutLen;
	jint seekBy = (jint)SeekBy;

	// WCXPlugin.packToMem(hMemPack, BufIn, InLen, BufOut, OutLen, SeekBy, _wcxPackToMemObject);
	jint ret = _env->CallIntMethod(_wcxJPluginObject, _wcxPackToMemMethodID, (jobject)hMemPack, bufIn, inLen, bufOut, outLen, seekBy, _wcxPackToMemObject);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_PACKTOMEM, METHOD_WCXPLUGIN_PACKTOMEM_SIGNATURE)) {
		if (bufIn != NULL) {
			_env->ReleaseStringUTFChars(bufIn, 0);
		}
		if (bufOut != NULL) {
			_env->ReleaseStringUTFChars(bufOut, 0);
		}
		return MEMPACK_DONE;
    }

	if (bufIn != NULL) {
		_env->ReleaseStringUTFChars(bufIn, 0);
	}
	if (bufOut != NULL) {
		_env->ReleaseStringUTFChars(bufOut, 0);
	}

	*Taken = (int)_env->GetIntField(_wcxPackToMemObject, _fTakenID);
	*Written = (int)_env->GetIntField(_wcxPackToMemObject, _fWrittenID);

	return ret;
}

int __stdcall DoneMemPack (int hMemPack)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return MEMPACK_DONE;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxDoneMemPackMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_DONEMEMPACK, METHOD_WCXPLUGIN_DONEMEMPACK_SIGNATURE);
	if (_wcxDoneMemPackMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_DONEMEMPACK, METHOD_WCXPLUGIN_DONEMEMPACK_SIGNATURE);
		return MEMPACK_DONE;
	}

	// WCXPlugin.doneMemPack(hMemPack);
	jint ret = _env->CallIntMethod(_wcxJPluginObject, _wcxDoneMemPackMethodID, (jobject)hMemPack);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_DONEMEMPACK, METHOD_WCXPLUGIN_DONEMEMPACK_SIGNATURE)) {
		return MEMPACK_DONE;
    }
	return ret;
}

BOOL __stdcall CanYouHandleThisFile (char *FileName)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return FALSE;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxCanYouHandleThisFileMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_CANYOUHANDLETHISFILE, METHOD_WCXPLUGIN_CANYOUHANDLETHISFILE_SIGNATURE);
	if (_wcxCanYouHandleThisFileMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_CANYOUHANDLETHISFILE, METHOD_WCXPLUGIN_CANYOUHANDLETHISFILE_SIGNATURE);
		return FALSE;
	}

	jstring fileName = _env->NewStringUTF(FileName);

	// WCXPlugin.canYouHandleThisFile(FileName);
	jboolean ret = _env->CallBooleanMethod(_wcxJPluginObject, _wcxCanYouHandleThisFileMethodID, fileName);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_CANYOUHANDLETHISFILE, METHOD_WCXPLUGIN_CANYOUHANDLETHISFILE_SIGNATURE)) {
		if (fileName != NULL) {
			_env->ReleaseStringUTFChars(fileName, 0);
		}
		return FALSE;
    }

	if (fileName != NULL) {
		_env->ReleaseStringUTFChars(fileName, 0);
	}
	return ret;
}

void __stdcall PackSetDefaultParams(PackDefaultParamStruct* dps)
{
	JNIEnv *_env= startJVM();
	if (_env == NULL) {
		return;
	}

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxPackSetDefaultParamsMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_PACKSETDEFAULTPARAMS, METHOD_WCXPLUGIN_PACKSETDEFAULTPARAMS_SIGNATURE);
	if (_wcxPackSetDefaultParamsMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_PACKSETDEFAULTPARAMS, METHOD_WCXPLUGIN_PACKSETDEFAULTPARAMS_SIGNATURE);
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
	jobject _wcxPackDefaultParamObject = _env->NewObject(_defaultParamCls, _defaultParamMethodID, (jint)dps->size, (jlong)dps->PluginInterfaceVersionLow, (jlong)dps->PluginInterfaceVersionHi, iniName);
    if (_wcxPackDefaultParamObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_DEFAULTPARAM, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return;
    }

	// WCXPlugin.packSetDefaultParams(dps);
	_env->CallVoidMethod(_wcxJPluginObject, _wcxPackSetDefaultParamsMethodID, _wcxPackDefaultParamObject);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_PACKSETDEFAULTPARAMS, METHOD_WCXPLUGIN_PACKSETDEFAULTPARAMS_SIGNATURE)) {
		if (iniName != NULL) {
			_env->ReleaseStringUTFChars(iniName, 0);
		}
		return;
    }

	if (iniName != NULL) {
		_env->ReleaseStringUTFChars(iniName, 0);
	}
}

int __stdcall ReadHeaderEx (HANDLE hArcData, tHeaderDataEx *HeaderDataEx)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wcxJPluginObject = GetPlugin(_env, wcxPluginClass);
	if (_wcxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return E_END_ARCHIVE;
	}

	// Get plugin class
	jclass _wcxCls = _env->GetObjectClass(_wcxJPluginObject);

	jmethodID _wcxReadHeaderExMethodID = _env->GetMethodID(_wcxCls, METHOD_WCXPLUGIN_READHEADEREX, METHOD_WCXPLUGIN_READHEADEREX_SIGNATURE);
	if (_wcxReadHeaderExMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wcxPluginClass, METHOD_WCXPLUGIN_READHEADEREX, METHOD_WCXPLUGIN_READHEADEREX_SIGNATURE);
		return E_END_ARCHIVE;
	}

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _headerDataExCls = GetClass(_env, CLASS_HEADERDATAEX);

	jmethodID _headerDataExMethodID = _env->GetMethodID(_headerDataExCls, CLASS_INIT,CLASS_HEADERDATAEX_INIT_SIGNATURE);
	if (_headerDataExMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_HEADERDATAEX, CLASS_INIT,CLASS_HEADERDATAEX_INIT_SIGNATURE);
		return E_END_ARCHIVE;
	}
	jfieldID _fArcNameID = _env->GetFieldID (_headerDataExCls, FIELD_FARCNAME, FIELD_FARCNAME_SIGNATURE);
	if (_fArcNameID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FARCNAME, FIELD_FARCNAME_SIGNATURE);
		return NULL;
	}
	jfieldID _fFileNameID = _env->GetFieldID (_headerDataExCls, FIELD_FFILENAME, FIELD_FFILENAME_SIGNATURE);
	if (_fFileNameID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FFILENAME, FIELD_FFILENAME_SIGNATURE);
		return NULL;
	}
	jfieldID _fFlagsID = _env->GetFieldID (_headerDataExCls, FIELD_FFLAGS, FIELD_FFLAGS_SIGNATURE);
	if (_fFlagsID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FFLAGS, FIELD_FFLAGS_SIGNATURE);
		return NULL;
	}
	jfieldID _fPackSizeID = _env->GetFieldID (_headerDataExCls, FIELD_FPACKSIZE, FIELD_FPACKSIZE_SIGNATURE);
	if (_fPackSizeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FPACKSIZE, FIELD_FPACKSIZE_SIGNATURE);
		return NULL;
	}
	jfieldID _fUnpSizeID = _env->GetFieldID (_headerDataExCls, FIELD_FUNPSIZE, FIELD_FUNPSIZE_SIGNATURE);
	if (_fUnpSizeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FUNPSIZE, FIELD_FUNPSIZE_SIGNATURE);
		return NULL;
	}
	jfieldID _fHostOsID = _env->GetFieldID (_headerDataExCls, FIELD_FHOSTOS, FIELD_FHOSTOS_SIGNATURE);
	if (_fHostOsID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FHOSTOS, FIELD_FHOSTOS_SIGNATURE);
		return NULL;
	}
	jfieldID _fFileCRCID = _env->GetFieldID (_headerDataExCls, FIELD_FFILECRC, FIELD_FFILECRC_SIGNATURE);
	if (_fFileCRCID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FFILECRC, FIELD_FFILECRC_SIGNATURE);
		return NULL;
	}
	jfieldID _fFileTimeID = _env->GetFieldID (_headerDataExCls, FIELD_FFILETIME, FIELD_FFILETIME_SIGNATURE);
	if (_fFileTimeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FFILETIME, FIELD_FFILETIME_SIGNATURE);
		return NULL;
	}
	jfieldID _fUnpVerID = _env->GetFieldID (_headerDataExCls, FIELD_FUNPVER, FIELD_FUNPVER_SIGNATURE);
	if (_fUnpVerID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FUNPVER, FIELD_FUNPVER_SIGNATURE);
		return NULL;
	}
	jfieldID _fMethodID = _env->GetFieldID (_headerDataExCls, FIELD_FMETHOD, FIELD_FMETHOD_SIGNATURE);
	if (_fMethodID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FMETHOD, FIELD_FMETHOD_SIGNATURE);
		return NULL;
	}
	jfieldID _fFileAttrID = _env->GetFieldID (_headerDataExCls, FIELD_FFILEATTR, FIELD_FFILEATTR_SIGNATURE);
	if (_fFileAttrID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_HEADERDATAEX, FIELD_FFILEATTR, FIELD_FFILEATTR_SIGNATURE);
		return NULL;
	}

	jobject _wcxHeaderDataExObject = _env->NewObject(_headerDataExCls, _headerDataExMethodID);
    if (_wcxHeaderDataExObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_HEADERDATAEX, CLASS_INIT, CLASS_HEADERDATAEX_INIT_SIGNATURE);
		return E_END_ARCHIVE;
    }
	// WCXPlugin.readHeader(_wcxheaderDataObject);
	jint ret = _env->CallIntMethod(_wcxJPluginObject, _wcxReadHeaderExMethodID, (jobject)hArcData, _wcxHeaderDataExObject);
	if (exceptionHandling(_env, wcxPluginClass, METHOD_WCXPLUGIN_READHEADEREX, METHOD_WCXPLUGIN_READHEADEREX_SIGNATURE)) {
		return E_END_ARCHIVE;
    }

	copyJstringBytes(_env, HeaderDataEx->ArcName, (jstring)_env->GetObjectField(_wcxHeaderDataExObject, _fArcNameID), 1024);
	copyJstringBytes(_env, HeaderDataEx->FileName, (jstring)_env->GetObjectField(_wcxHeaderDataExObject, _fFileNameID), 1024);
	HeaderDataEx->Flags = _env->GetIntField(_wcxHeaderDataExObject, _fFlagsID);
	HeaderDataEx->PackSize = _env->GetLongField(_wcxHeaderDataExObject, _fPackSizeID);
	HeaderDataEx->UnpSize = _env->GetLongField(_wcxHeaderDataExObject, _fUnpSizeID);
	HeaderDataEx->HostOS = _env->GetIntField(_wcxHeaderDataExObject, _fHostOsID);
	HeaderDataEx->FileCRC = _env->GetLongField(_wcxHeaderDataExObject, _fFileCRCID);
	HeaderDataEx->FileTime = _env->GetLongField(_wcxHeaderDataExObject, _fFileTimeID);
	HeaderDataEx->UnpVer = _env->GetIntField(_wcxHeaderDataExObject, _fUnpVerID);
	HeaderDataEx->Method = _env->GetIntField(_wcxHeaderDataExObject, _fMethodID);
	HeaderDataEx->FileAttr = _env->GetIntField(_wcxHeaderDataExObject, _fFileAttrID);
	memset (HeaderDataEx->Reserved, 0, 1024);
	return ret;
}

/*
 * Class:     plugins_wcx_WCXPluginAdapter
 * Method:    packerSetChangeVol
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_plugins_wcx_WCXPluginAdapter_packerSetChangeVol
  (JNIEnv *_env, jobject obj, jstring arcName, jint mode)
{
	const char *ArcName= NULL;
	if (arcName!=NULL) {
		ArcName = _env->GetStringUTFChars(arcName, 0);
	}
	int Mode = (int)mode;
	int ret = ChangeVolProc1((char*)ArcName, Mode);
	return ret;
}

/*
 * Class:     plugins_wcx_WCXPluginAdapter
 * Method:    packerSetProcessData
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_plugins_wcx_WCXPluginAdapter_packerSetProcessData
(JNIEnv * _env, jobject obj, jstring filename, jint size)
{
	const char *FileName= NULL;
	if (filename!=NULL) {
		FileName = _env->GetStringUTFChars(filename, 0);
	}
	int Size = (int)size;
	int ret = ProcessDataProc((char*)FileName, Size);
	return ret;
}
