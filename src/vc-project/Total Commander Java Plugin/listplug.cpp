// listplug.cpp : Defines the entry point for the DLL application.
//
 
#include "stdafx.h"
#include "listplug.h"
#include "java.h"
#include "plugins_wlx_WLXPluginAdapter.h"

using namespace std;

EXTERN jobject __stdcall GetPlugin(JNIEnv *, char *);

EXTERN jobject __stdcall GetPluginClassLoader(JNIEnv *);

EXTERN jclass __stdcall GetClass(JNIEnv *, char *);

BOOL APIENTRY DllMain( HINSTANCE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
    switch (ul_reason_for_call)
	{
		case DLL_PROCESS_ATTACH:
			if (theDll==NULL) {
				theDll = hModule;
				GetModuleFileName((HMODULE)hModule,moduleFilename, sizeof(moduleFilename));
			}
			break;
		case DLL_PROCESS_DETACH:
			// destroying the JVM will lead into deadlock problems:
			// since TC library started JVM and wants to exit the JVM,
			// but JVM loaded TC library itself and this causes the deadlock
			// each of them is waiting for freed resources.
			/*
			  // Hard exit: Runtime.getRuntime().halt(0);
			  jclass runtimeCls= _env->FindClass("java/lang/Runtime");
			  jmethodID getRuntimeMethodID = _env->GetStaticMethodID(runtimeCls, "getRuntime", "()Ljava/lang/Runtime;");
			  jobject runtimeObj = (jobject)_env->CallStaticObjectMethod(runtimeCls, getRuntimeMethodID);
			  jmethodID haltMethodID = _env->GetMethodID(runtimeCls, "halt", "(I)V");
			  _env->CallStaticIntMethod(runtimeCls, haltMethodID, (jint)0);
			  
			  // Soft exit: System.exit(0);
			  jclass systemCls= _env->FindClass("java/lang/System");
			  jmethodID exitMethodID = _env->GetStaticMethodID(systemCls, "halt", "(I)V");
			  _env->CallStaticVoidMethod(systemCls, exitMethodID, (jint)0);
			  
			  if (JVMHandle)
				FreeLibrary(JVMHandle);
			  _env= NULL;
			  _jvm->DestroyJavaVM();*/
			break;
		case DLL_THREAD_ATTACH:
			break;
		case DLL_THREAD_DETACH:
			break;
    }
    return TRUE;
}

int __stdcall ListNotificationReceived(HWND ListWin,int Message,WPARAM wParam,LPARAM lParam)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listNotificationReceivedMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTNOTIFICATIONRECEIVED, METHOD_WLXPLUGIN_LISTNOTIFICATIONRECEIVED_SIGNATURE);
	if (_listNotificationReceivedMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTNOTIFICATIONRECEIVED, METHOD_WLXPLUGIN_LISTNOTIFICATIONRECEIVED_SIGNATURE);
		return 0;
	}

	// WLXPlugin.listNotificationReceived(ListWin, Message, wParam, lParam);
	jint handle = (jint)ListWin;
	jint msg = Message;
	jint wPar= wParam;
	jint lPar= lParam;
	jint ret = _env->CallIntMethod(_wlxJPluginObject, _listNotificationReceivedMethodID, handle, msg, wPar, lPar);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTNOTIFICATIONRECEIVED, METHOD_WLXPLUGIN_LISTNOTIFICATIONRECEIVED_SIGNATURE)) {
      return 0;
    }

	return ret;
}

HWND __stdcall ListLoad(HWND ParentWin,char* FileToLoad,int ShowFlags)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	// WLXPlugin.listLoad(FileToLoad, ShowFlags);
	jstring input = _env->NewStringUTF(FileToLoad);
	if (input==NULL) {
		return 0;
	}

	jmethodID _listLoadMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTLOAD, METHOD_WLXPLUGIN_LISTLOAD_SIGNATURE);
	if (_listLoadMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTLOAD, METHOD_WLXPLUGIN_LISTLOAD_SIGNATURE);
		return 0;
	}

	jint handle = _env->CallIntMethod(_wlxJPluginObject, _listLoadMethodID, (jint)ParentWin, input, (jint)ShowFlags);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTLOAD, METHOD_WLXPLUGIN_LISTLOAD_SIGNATURE)) {
		if (input != NULL) {
			_env->ReleaseStringUTFChars(input, 0);
		}
		return NULL;
    }
	SetParent((HWND)handle, ParentWin);

	if (input != NULL) {
		_env->ReleaseStringUTFChars(input, 0);
	}
	return (HWND)handle;
}

int __stdcall ListLoadNext(HWND ParentWin,HWND ListWin,char* FileToLoad,int ShowFlags)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listLoadNextMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTLOADNEXT, METHOD_WLXPLUGIN_LISTLOADNEXT_SIGNATURE);
	if (_listLoadNextMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTLOADNEXT, METHOD_WLXPLUGIN_LISTLOADNEXT_SIGNATURE);
		return 0;
	}

	jstring input = _env->NewStringUTF(FileToLoad);
	if (input==NULL) {
		return 1;
	}
	// WLXPlugin.listLoadNext(ParentWin, ListWin, FileToLoad, ShowFlags);
	jint ret = _env->CallIntMethod(_wlxJPluginObject, _listLoadNextMethodID, (jint)ParentWin, (jint)ListWin, input, (jint)ShowFlags);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTLOADNEXT, METHOD_WLXPLUGIN_LISTLOADNEXT_SIGNATURE)) {
		if (input != NULL) {
			_env->ReleaseStringUTFChars(input, 0);
		}
		return 1;
    }

	if (input != NULL) {
		_env->ReleaseStringUTFChars(input, 0);
	}
	return ret;
}


int __stdcall ListSendCommand(HWND ListWin,int Command,int Parameter)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listSendCommandMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTSENDCOMMAND, METHOD_WLXPLUGIN_LISTSENDCOMMAND_SIGNATURE);
	if (_listSendCommandMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTSENDCOMMAND, METHOD_WLXPLUGIN_LISTSENDCOMMAND_SIGNATURE);
		return 0;
	}

	// WLXPlugin.listSendCommand(ListWin, Command, Parameter);
	jint handle = (jint)ListWin;
	jint cmd= Command;
	jint param= Parameter;
	jint ret = _env->CallIntMethod(_wlxJPluginObject, _listSendCommandMethodID, handle, cmd, param);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTSENDCOMMAND, METHOD_WLXPLUGIN_LISTSENDCOMMAND_SIGNATURE)) {
      return 1;
    }

	return ret;
}

int __stdcall ListSearchDialog(HWND ListWin,int FindNext)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listSearchDialogMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTSEARCHDIALOG, METHOD_WLXPLUGIN_LISTSEARCHDIALOG_SIGNATURE);
	if (_listSearchDialogMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTSEARCHDIALOG, METHOD_WLXPLUGIN_LISTSEARCHDIALOG_SIGNATURE);
		return 0;
	}

	// WLXPlugin.listSearchDialog(ListWin, FindNext);
	jint handle = (jint)ListWin;
	jint findNext = (jint)FindNext;

	jint ret = _env->CallIntMethod(_wlxJPluginObject, _listSearchDialogMethodID, handle, findNext);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTSEARCHDIALOG, METHOD_WLXPLUGIN_LISTSEARCHDIALOG_SIGNATURE)) {
      return 1;
    }
	return ret;
}

int _stdcall ListSearchText(HWND ListWin,char* SearchString,int SearchParameter)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listSearchTextMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTSEARCHTEXT, METHOD_WLXPLUGIN_LISTSEARCHTEXT_SIGNATURE);
	if (_listSearchTextMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTSEARCHTEXT, METHOD_WLXPLUGIN_LISTSEARCHTEXT_SIGNATURE);
		return NULL;
	}

	// WLXPlugin.listSearchText(ListWin, SearchString, SearchParameter);
	jint handle = (jint)ListWin;
	jstring searchText = _env->NewStringUTF(SearchString);
	jint flags= SearchParameter;

	jint ret = _env->CallIntMethod(_wlxJPluginObject, _listSearchTextMethodID, handle, searchText, flags);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTSEARCHTEXT, METHOD_WLXPLUGIN_LISTSEARCHTEXT_SIGNATURE)) {
		if (searchText != NULL) {
			_env->ReleaseStringUTFChars(searchText, 0);
		}
		return 1;
    }

	if (searchText != NULL) {
		_env->ReleaseStringUTFChars(searchText, 0);
	}
	return ret;
}

int __stdcall ListPrint(HWND ListWin,char* FileToPrint,char* DefPrinter,int PrintFlags,RECT* Margins)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 1;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listPrintMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTPRINT, METHOD_WLXPLUGIN_LISTPRINT_SIGNATURE);
	if (_listPrintMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTPRINT, METHOD_WLXPLUGIN_LISTPRINT_SIGNATURE);
		return 1;
	}

	// WLXPlugin.listPrint(ListWin, FileToPrint, DefPrinter, PrintFlags, Margins);
	jint handle = (jint)ListWin;
	jstring file = _env->NewStringUTF(FileToPrint);
	jstring printer= _env->NewStringUTF(DefPrinter);
	jint flags= PrintFlags;

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	// Rectangle class
	jclass _rectangleCls = _env->FindClass(CLASS_RECTANGLE);
	if (_rectangleCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, CLASS_RECTANGLE);
		return 1;
	}

	jmethodID _rectangleMethodID = _env->GetMethodID(_rectangleCls, CLASS_INIT,CLASS_RECTANGLE_INIT_SIGNATURE);
	if (_rectangleMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_RECTANGLE, CLASS_INIT,CLASS_RECTANGLE_INIT_SIGNATURE);
		return 1;
	}
		
	jobject _rectObject = _env->NewObject(_rectangleCls, _rectangleMethodID, Margins->left, Margins->top, Margins->bottom, Margins->right);
    if (_rectObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_RECTANGLE, CLASS_INIT, CLASS_RECTANGLE_INIT_SIGNATURE);
 		return 1;
    }

	jint ret = _env->CallIntMethod(_wlxJPluginObject, _listPrintMethodID, handle, file, printer, flags, _rectObject);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTPRINT, METHOD_WLXPLUGIN_LISTPRINT_SIGNATURE)) {
		if (file != NULL) {
			_env->ReleaseStringUTFChars(file, 0);
		}
		if (printer != NULL) {
			_env->ReleaseStringUTFChars(printer, 0);
		}
		return 1;
    }

	if (file != NULL) {
		_env->ReleaseStringUTFChars(file, 0);
	}
	if (printer != NULL) {
		_env->ReleaseStringUTFChars(printer, 0);
	}
	return ret;
}

void __stdcall ListGetDetectString(char* DetectString,int maxlen)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listGetDetectStringMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTGETDETECTSTRING, METHOD_WLXPLUGIN_LISTGETDETECTSTRING_SIGNATURE);
	if (_listGetDetectStringMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTGETDETECTSTRING, METHOD_WLXPLUGIN_LISTGETDETECTSTRING_SIGNATURE);
		return;
	}

	//WLXPlugin.listGetDetectString(DetectString, maxlen);
	jstring detectString = (jstring)_env->CallObjectMethod(_wlxJPluginObject, _listGetDetectStringMethodID, maxlen);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTGETDETECTSTRING, METHOD_WLXPLUGIN_LISTGETDETECTSTRING_SIGNATURE)) {
      return;
    }
	copyJstringBytes(_env, DetectString, detectString, maxlen);
}

void __stdcall ListCloseWindow(HWND ListWin)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listCloseWindowMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTCLOSEWINDOW, METHOD_WLXPLUGIN_LISTCLOSEWINDOW_SIGNATURE);
	if (_listCloseWindowMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTCLOSEWINDOW, METHOD_WLXPLUGIN_LISTCLOSEWINDOW_SIGNATURE);
		return;
	}

	int listWin = (int) ListWin;
	//WLXPlugin.listCloseWindow(ListWin);
	_env->CallObjectMethod(_wlxJPluginObject, _listCloseWindowMethodID, (jint)listWin);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTCLOSEWINDOW, METHOD_WLXPLUGIN_LISTCLOSEWINDOW_SIGNATURE)) {
      return;
    }
}


HBITMAP __stdcall ListGetPreviewBitmap(char* FileToLoad,int width,int height,
									   char* contentbuf,int contentbuflen)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

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

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listGetPreviewBitmapMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTGETPREVIEWBITMAP, METHOD_WLXPLUGIN_LISTGETPREVIEWBITMAP_SIGNATURE);
	if (_listGetPreviewBitmapMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTGETPREVIEWBITMAP, METHOD_WLXPLUGIN_LISTGETPREVIEWBITMAP_SIGNATURE);
		return 0;
	}

	/* WLXPlugin.listGetPreviewBitmap(FileToLoad, width, height, contentbuf, contentbuflen); */
	jstring input = _env->NewStringUTF(FileToLoad);
	jint wdt = width;
	jint hgt = height;
    jstring content = _env->NewStringUTF(contentbuf);
    jint len= contentbuflen;
	jobject iconBuffer = _stringBufferObject;

	int ret = _env->CallIntMethod(_wlxJPluginObject, _listGetPreviewBitmapMethodID, input, wdt, hgt, content, len, iconBuffer);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTGETPREVIEWBITMAP, METHOD_WLXPLUGIN_LISTGETPREVIEWBITMAP_SIGNATURE)) {
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
		return hBitMap;
	}
	return 0;
}

void __stdcall ListSetDefaultParams(ListDefaultParamStruct* dps)
{
	JNIEnv *_env= startJVM();
	if (_env == NULL) {
		return;
	}

	// Get plugin instance
	jobject _wlxJPluginObject = GetPlugin(_env, pluginClass);
	if (_wlxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, pluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wlxCls = _env->GetObjectClass(_wlxJPluginObject);

	jmethodID _listDefaultGetParamsMethodID = _env->GetMethodID(_wlxCls, METHOD_WLXPLUGIN_LISTDEFAULTGETPARAMS, METHOD_WLXPLUGIN_LISTDEFAULTGETPARAMS_SIGNATURE);
	if (_listDefaultGetParamsMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, pluginClass, METHOD_WLXPLUGIN_LISTDEFAULTGETPARAMS, METHOD_WLXPLUGIN_LISTDEFAULTGETPARAMS_SIGNATURE);
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
	jobject _wlxListDefaultParamObject = _env->NewObject(_defaultParamCls, _defaultParamMethodID, (jint)dps->size, (jlong)dps->PluginInterfaceVersionLow, (jlong)dps->PluginInterfaceVersionHi, iniName);
    if (_wlxListDefaultParamObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_DEFAULTPARAM, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return;
    }

	// WLXPlugin.listSetDefaultParams(dps);
	_env->CallVoidMethod(_wlxJPluginObject, _listDefaultGetParamsMethodID, _wlxListDefaultParamObject);
	if (exceptionHandling(_env, pluginClass, METHOD_WLXPLUGIN_LISTDEFAULTGETPARAMS, METHOD_WLXPLUGIN_LISTDEFAULTGETPARAMS_SIGNATURE)) {
		if (iniName != NULL) {
			_env->ReleaseStringUTFChars(iniName, 0);
		}
		return;
    }

	if (iniName != NULL) {
		_env->ReleaseStringUTFChars(iniName, 0);
	}
}

/*
 * Class:     plugins_wlx_WLXPluginAdapter
 * Method:    getHWND
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_plugins_wlx_WLXPluginAdapter_getHWND
  (JNIEnv *_env, jobject obj, jobject comp, jstring jawtPath, jstring awtPath)
{
	jboolean iscopy;
	const char *JawtPath = NULL;
	const char *AwtPath = NULL;
    HWND hWnd = 0;
    JAWT awt;
    JAWT_DrawingSurface* ds;
    JAWT_DrawingSurfaceInfo* dsi;
    JAWT_Win32DrawingSurfaceInfo* dsi_win;
    jboolean result;
    jint lock;

	if (jawtPath!=NULL) {
		JawtPath = _env->GetStringUTFChars(jawtPath, &iscopy);
	}
	if (awtPath!=NULL) {
		AwtPath = _env->GetStringUTFChars(awtPath, &iscopy);
	}
    //Load AWT Library
    if(!_hAWT)
        //for Java 1.4
        _hAWT = LoadLibrary(JawtPath);
    if(!_hAWT)
        //for Java 1.3
        _hAWT = LoadLibrary(AwtPath);
    if(_hAWT)
    {
        PJAWT_GETAWT JAWT_GetAWT = (PJAWT_GETAWT)GetProcAddress(_hAWT, "_JAWT_GetAWT@8");
        if(JAWT_GetAWT)
        {
            awt.version = JAWT_VERSION_1_4; // Init here with JAWT_VERSION_1_3 or JAWT_VERSION_1_4 
            //Get AWT API Interface
            result = JAWT_GetAWT(_env, &awt);
            if(result != JNI_FALSE)
            {
                ds = awt.GetDrawingSurface(_env, comp);
                if(ds != NULL)
                {
                    lock = ds->Lock(ds);
                    if((lock & JAWT_LOCK_ERROR) == 0)
                    {
                        dsi = ds->GetDrawingSurfaceInfo(ds);
                        if(dsi)
                        {
                            dsi_win = (JAWT_Win32DrawingSurfaceInfo*)dsi->platformInfo;
                            if(dsi_win)
                            {
                                hWnd = dsi_win->hwnd;
                            }
                            ds->FreeDrawingSurfaceInfo(dsi);
                        }
                        ds->Unlock(ds);
                    }
                    awt.FreeDrawingSurface(ds);
                }
            }
        }
    }
    return (jint)hWnd;
}

/*
 * Class:     plugins_wlx_WLXPluginAdapter
 * Method:    getHWND2
 * Signature: (Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_plugins_wlx_WLXPluginAdapter_getHWND2
  (JNIEnv *_env, jobject obj, jobject frame)
{
	jclass frameClass;
	jmethodID getTitleMethod;
	jstring title;
	jobject titleObject;
	HWND hWnd;
	LPCTSTR frameTitle;
 
	frameClass = _env->GetObjectClass (frame);
	getTitleMethod = _env->GetMethodID (frameClass, "getTitle", "()Ljava/lang/String;");
	titleObject = _env->CallObjectMethod (frame, getTitleMethod);
	title = (jstring)titleObject;
 
	frameTitle = _env->GetStringUTFChars (title, NULL);
	hWnd = FindWindow (NULL, frameTitle);
	return (jint)hWnd;
}

/*
 * Class:     plugins_wlx_WLXPluginAdapter
 * Method:    setSize
 * Signature: (III)I
 */
JNIEXPORT void JNICALL Java_plugins_wlx_WLXPluginAdapter_setSize
  (JNIEnv *_env, jobject obj, jint hWnd, jint nWidth, jint nHeight)
{
	RECT rcClient, rcWindow;
	POINT ptDiff;
	GetClientRect((HWND)hWnd, &rcClient);
	GetWindowRect((HWND)hWnd, &rcWindow);
	ptDiff.x = (rcWindow.right - rcWindow.left) - rcClient.right;
	ptDiff.y = (rcWindow.bottom - rcWindow.top) - rcClient.bottom;
	SetWindowPos((HWND)hWnd, HWND_TOP, rcWindow.left, rcWindow.top, nWidth + ptDiff.x, nHeight + ptDiff.y, SWP_SHOWWINDOW);
	UpdateWindow((HWND)hWnd);
//	MoveWindow((HWND)hWnd,rcWindow.left, rcWindow.top, nWidth + ptDiff.x, nHeight + ptDiff.y, FALSE);
//	SetFocus((HWND)hWnd);
}
