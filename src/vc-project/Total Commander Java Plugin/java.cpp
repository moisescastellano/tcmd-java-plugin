#define JAVA

#include "stdafx.h"
#include "java.h"

EXTERN jclass __stdcall GetClass(JNIEnv *, char *);

char *replace_str(char *str, char *orig, char *rep)
{
  char *buffer;
  char *p;

  if(!(p = strstr(str, orig)))  // Is 'orig' even in 'str'?
    return str;

  buffer = (char *)malloc((p-str)+1);
  strncpy(buffer, str, p-str); // Copy characters from 'str' start to 'orig' st$
  buffer[p-str] = '\0';
  buffer = (char *)realloc(buffer, strlen(buffer) + strlen(rep) + strlen(p+strlen(orig)) + 1);
  sprintf(buffer+(p-str), "%s%s", rep, p+strlen(orig));

  return buffer;
}

void errorDialog(char *errorKey, ...)
{
	va_list arguments;
	va_start(arguments, errorKey);

	// change current working directory
	char *lastBSlash = strrchr(moduleFilename, '\\');
	char cwd[MAX_PATH];
	memset(cwd,0,MAX_PATH);
	strncpy( cwd, moduleFilename, (lastBSlash-moduleFilename) );
	int ok = _chdir(cwd);
	if (ok!=0) {
		MessageBox(GetActiveWindow(), "_chdir failed!", "TC Java Plugin Error", MB_OK | MB_ICONERROR);
		return;
	}

	// read language setting
	GetPrivateProfileString("GENERAL","LANGUAGE","EN",language,sizeof(language),PLUGIN_INI);
	
	char nlsTitle[MAX_MESSAGE];
	GetPrivateProfileString(language,TLE_ERROR,TLE_ERROR,nlsTitle,sizeof(nlsTitle),ERROR_INI);
	char nlsMessage[MAX_MESSAGE];
	GetPrivateProfileString(language,errorKey,errorKey,nlsMessage,sizeof(nlsMessage),ERROR_INI);

	char errorMessage[MAX_STACKTRACE];
	_vsnprintf(errorMessage, MAX_STACKTRACE - 1, nlsMessage, arguments);
	errorMessage[MAX_STACKTRACE - 1] = '\0';
	/* Display it in a message dialog box */
	MessageBox(GetActiveWindow(), errorMessage, nlsTitle, MB_OK | MB_ICONERROR);
}

// true: exception occured,false otherwise
bool exceptionHandling(JNIEnv *_env, char *pluginClass, char *method, char *methodSignature)
{
	// The CharArrayWriter class
	jclass _charArrayWriterCls;
	jmethodID _charArrayWriterMethodID;
	jmethodID _charArrayWriterToStringMethodID;
	// The Throwable class
	jclass _throwableCls;
	jmethodID _printStackTraceMethodID;
	// The PrintWriter class
	jclass _printWriterCls;
	jmethodID _printWriterMethodID;

   // exception occured?
   if (_env->ExceptionCheck()) {
       // get Throwable instance
	   jthrowable exc = _env->ExceptionOccurred();
       // immediately clear pending exception
	   _env->ExceptionClear();

		// Exception class
		_throwableCls = _env->FindClass(CLASS_THROWABLE);
		if (_throwableCls == NULL) {
			errorDialog(ERR_CLASS_NOT_FOUND, CLASS_THROWABLE);
 			return NULL;
		}

		_printStackTraceMethodID = _env->GetMethodID(_throwableCls, METHOD_THROWABLE_PRINTSTACKTRACE, METHOD_THROWABLE_PRINTSTACKTRACE_SIGNATURE);
		if (_printStackTraceMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_THROWABLE, METHOD_THROWABLE_PRINTSTACKTRACE,METHOD_THROWABLE_PRINTSTACKTRACE_SIGNATURE);
			return NULL;
		}

		// CharArrayWriter class
		_charArrayWriterCls = _env->FindClass(CLASS_CHARARRAYWRITER);
		if (_charArrayWriterCls == NULL) {
			errorDialog(ERR_CLASS_NOT_FOUND, CLASS_CHARARRAYWRITER);
 			return NULL;
		}

		_charArrayWriterMethodID = _env->GetMethodID(_charArrayWriterCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
		if (_charArrayWriterMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_CHARARRAYWRITER, CLASS_INIT,CLASS_INIT_SIGNATURE);
 			return NULL;
		}
		_charArrayWriterToStringMethodID = _env->GetMethodID(_charArrayWriterCls, METHOD_CHARARRAYWRITER_TOSTRING,METHOD_CHARARRAYWRITER_TOSTRING_SIGNATURE);
		if (_charArrayWriterToStringMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_CHARARRAYWRITER, METHOD_CHARARRAYWRITER_TOSTRING,METHOD_CHARARRAYWRITER_TOSTRING_SIGNATURE);
 			return NULL;
		}
		// PrintWriter class
		_printWriterCls = _env->FindClass(CLASS_PRINTWRITER);
		if (_printWriterCls == NULL) {
			errorDialog(ERR_CLASS_NOT_FOUND, CLASS_PRINTWRITER);
 			return NULL;
		}

		_printWriterMethodID = _env->GetMethodID(_printWriterCls, CLASS_INIT,CLASS_PRINTWRITER_INIT_SIGNATURE);
		if (_printWriterMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_PRINTWRITER, CLASS_INIT,CLASS_PRINTWRITER_INIT_SIGNATURE);
 			return NULL;
		}
		
		// CharArrayWriter wr = new CharArrayWriter();
		jobject _charArrayWriterObject = _env->NewObject(_charArrayWriterCls, _charArrayWriterMethodID);
		if (_charArrayWriterObject == NULL) {
			errorDialog(ERR_CLASS_INIT_FAILED, CLASS_CHARARRAYWRITER, CLASS_INIT, CLASS_INIT_SIGNATURE);
 			return true;
		}
		// PrintWriter pw = new PrintWriter(wr)
		jobject _printWriterObject = _env->NewObject(_printWriterCls, _printWriterMethodID, _charArrayWriterObject);
		if (_printWriterObject == NULL) {
			errorDialog(ERR_CLASS_INIT_FAILED, CLASS_PRINTWRITER, CLASS_INIT, CLASS_PRINTWRITER_INIT_SIGNATURE);
 			return true;
		}
		// e.printStackTrace(pw);
		_env->CallVoidMethod(exc, _printStackTraceMethodID, _printWriterObject);
		// wr.toString();
	   jstring message = (jstring)_env->CallObjectMethod(_charArrayWriterObject, _charArrayWriterToStringMethodID);

	   // print error dialogue
	   const char *msg = _env->GetStringUTFChars( message, 0 );
	   errorDialog(ERR_EXCEPTION, pluginClass, method, methodSignature, msg);
	   _env->ReleaseStringUTFChars(message, msg);
	   _env->DeleteLocalRef(exc);
       // exception occured
	   return true;
   }
   // no exception occured
   return false;
}

// create JVM, get plugin objects and methods
JNIEnv *startJVM()
{
  JNIEnv *_env;
  // path name of the JVM DLL
  char jvm_lib[MAX_PATH];
  // path name of the JVM HOME
  char jvm_home[MAX_PATH];
  // JVM installed in registry?
  bool reg = false;

  // Create the JVM one time only
  if (init) {
		// change current working directory
		char *lastBSlash = strrchr(moduleFilename, '\\');
		char cwd[MAX_PATH];
		memset(cwd,0,MAX_PATH);
		strncpy( cwd, moduleFilename, (lastBSlash-moduleFilename) );
		int ok = _chdir(cwd);
		if (ok!=0) {
			errorDialog(ERR_PLUGIN_NOT_FOUND, cwd);
			return NULL;
		}

		// load JVM infos

		// first: get global configuration file %COMMANDER_PATH%/javalib/tc_javaplugin.ini
		// (e.g. Java on an USB stick, global for all plugins)
		GetPrivateProfileString("JVM","JVM_DLL","",jvm_lib,sizeof(jvm_lib),replace_str(GLB_PLUGIN_INI,    "%COMMANDER_PATH%", getenv("COMMANDER_PATH")));
		GetPrivateProfileString("JVM","JVM_HOME","",jvm_home,sizeof(jvm_home),replace_str(GLB_PLUGIN_INI,    "%COMMANDER_PATH%", getenv("COMMANDER_PATH")));

		// second: search the registry to find the appropriate Java Runtime Environment
		// (Java installation on local computer, global for all plugins)
		if (strlen(jvm_lib)==0) {
			HKEY hKey;
			char version[MAX_PATH];
			long retval = RegOpenKeyEx(HKEY_LOCAL_MACHINE,REG_PATH_JRE,0,KEY_READ,&hKey);
			if(retval==ERROR_SUCCESS)
			{
				DWORD versionLen=MAX_PATH;
				retval = RegQueryValueEx( hKey, REG_KEY_JRE_VERSION, NULL, NULL,
						(LPBYTE) version, &versionLen);
				if (retval==ERROR_SUCCESS) {
					reg = true;
				}
				retval = RegCloseKey(hKey);
			}
			if (reg == true) {
				reg= false;
				char runtimeLibKey[MAX_PATH];
				strcpy(runtimeLibKey, REG_PATH_JRE);
				strcat(runtimeLibKey, "\\");
				strcat(runtimeLibKey, version);
				retval = RegOpenKeyEx(HKEY_LOCAL_MACHINE,runtimeLibKey,0,KEY_READ,&hKey);
				if(retval==ERROR_SUCCESS)
				{
					DWORD jvm_libLen=MAX_PATH;
					retval = RegQueryValueEx( hKey, REG_KEY_JRE_RTLIB, NULL, NULL,
						(LPBYTE) jvm_lib, &jvm_libLen);
					if(retval==ERROR_SUCCESS)
					{
						DWORD jvm_homeLen=MAX_PATH;
						retval = RegQueryValueEx( hKey, REG_KEY_JRE_HOME, NULL, NULL,
							(LPBYTE) jvm_home, &jvm_homeLen);
						if(retval==ERROR_SUCCESS)
						{
							reg = true;
						}
					}
				}
				retval = RegCloseKey(hKey);
			}
		}
		// third: get plugin configuration file <plugindir>/tc_javaplugin.ini
		// (e.g. specific Java of one plugin)
		if (strlen(jvm_lib)==0) {
			GetPrivateProfileString("JVM","JVM_DLL","",jvm_lib,sizeof(jvm_lib),PLUGIN_INI);
			GetPrivateProfileString("JVM","JVM_HOME","",jvm_home,sizeof(jvm_home),PLUGIN_INI);
		}
		// no Java Runtime Environment detected :-(
		if (strlen(jvm_lib)==0) {
			errorDialog(ERR_JRE_NOT_FOUND);
			return NULL;
		}

		HINSTANCE hJvmDll = LoadLibrary(jvm_lib);
		JVMHandle = hJvmDll;
		if (hJvmDll==NULL) {
			errorDialog(ERR_RUNTIME_NOT_FOUND, jvm_lib);
			return NULL;
		}
		LPFNDLLCREATEJVM pfnCreateVM = (LPFNDLLCREATEJVM)GetProcAddress(hJvmDll, PROC_CREATE_JVM);
		if (!pfnCreateVM) {
			errorDialog(ERR_PROC_UNDEFINED, jvm_lib);
			return NULL;
		}
		LPFNDLLGETCREATEDJVM pfnGetCreatedJavaVMs = (LPFNDLLGETCREATEDJVM)GetProcAddress(hJvmDll, PROC_GETCREATED_JVM);
		if (!pfnGetCreatedJavaVMs) {
			errorDialog(ERR_PROC_UNDEFINED, jvm_lib);
			return NULL;
		}

		// check if JVM is already running
		JavaVM *currentJVMs[1];
		jsize jvmCount;
		jint status = (*pfnGetCreatedJavaVMs)(currentJVMs, (jint)1, &jvmCount);
		if (status != JNI_OK || jvmCount == 0) {
			char jvm_classpath[MAX_PATH+MAX_PATH+MAX_PATH];
			GetPrivateProfileString("JVM","JAVA.CLASS.PATH","",jvm_classpath,sizeof(jvm_classpath),PLUGIN_INI);
			char jvm_option[MAX_PATH];
			GetPrivateProfileString("JVM","JAVA.OPTIONS","",jvm_option,sizeof(jvm_option),PLUGIN_INI);
			char javalib[MAX_PATH];
			GetPrivateProfileString("JVM","JAVA.TC.JAVALIB","",javalib,sizeof(javalib),PLUGIN_INI);

			JavaVMInitArgs vm_args;
			JavaVMOption options[4];

			/* JVM option */
			options[0].optionString = replace_str(jvm_option,    "%COMMANDER_PATH%", getenv("COMMANDER_PATH"));
			/* JVM classpath */
			options[1].optionString = replace_str(jvm_classpath, "%COMMANDER_PATH%", getenv("COMMANDER_PATH"));
			/* TC java lib */
			options[2].optionString = replace_str(javalib,       "%COMMANDER_PATH%", getenv("COMMANDER_PATH"));
			/* java.ext.dirs */
			options[3].optionString = replace_str(
											replace_str("-Djava.ext.dirs=%COMMANDER_PATH%\\javalib\\lib\\ext;%JAVA_HOME%\\lib\\ext",
												"%COMMANDER_PATH%",
												getenv("COMMANDER_PATH")),
											"%JAVA_HOME%",
											jvm_home);
			options[4].optionString = "-Xcheck:jni";
			vm_args.version = JNI_VERSION_1_4;
			vm_args.options = options;
			vm_args.nOptions = 5;
			vm_args.ignoreUnrecognized = JNI_TRUE;    
		
			// there is not an already started JVM, so start a brand new JVM
			status = (*pfnCreateVM)(&_jvm, (void **)&_env, &vm_args);
			if (status != JNI_OK) {
				errorDialog(ERR_JVM_START_FAILED, (int)status);
				return NULL;
			}
		} else {
			// reuse existing JVM and attach this process to a new Java Thread
			_jvm = currentJVMs[0];
			_jvm->AttachCurrentThread((void**) &_env, NULL);
		}

		// ERR_CLASS_NOT_FOUND

		// WLX
		GetPrivateProfileString("WLX","CLASS","",pluginClass,sizeof(pluginClass),PLUGIN_INI);

		// WFX
		GetPrivateProfileString("WFX","CLASS","",wfxPluginClass,sizeof(wfxPluginClass),PLUGIN_INI);
		
		// WDX
		GetPrivateProfileString("WDX","CLASS","",wdxPluginClass,sizeof(wdxPluginClass),PLUGIN_INI);

		// WCX
		GetPrivateProfileString("WCX","CLASS","",wcxPluginClass,sizeof(wcxPluginClass),PLUGIN_INI);

		if (strstr(moduleFilename, ".wlx") != NULL && strlen(pluginClass) == 0) {
			errorDialog(ERR_CLASS_NOT_FOUND, "[WLX] CLASS=???");
			return NULL;
		} else if (strstr(moduleFilename, ".wfx") != NULL && strlen(wfxPluginClass) == 0) {
			errorDialog(ERR_CLASS_NOT_FOUND, "[WFX] CLASS=???");
			return NULL;
		} else if (strstr(moduleFilename, ".wdx") != NULL && strlen(wdxPluginClass) == 0) {
			errorDialog(ERR_CLASS_NOT_FOUND, "[WDX] CLASS=???");
			return NULL;
		} else if (strstr(moduleFilename, ".wcx") != NULL && strlen(wcxPluginClass) == 0) {
			errorDialog(ERR_CLASS_NOT_FOUND, "[WCX] CLASS=???");
			return NULL;
		}
		init = false;
	} else {
		// JVM already initialized?
		// Get java environment of the current thread
		_jvm->AttachCurrentThread((void**) &_env, NULL);	
	}
	return _env;
}

/* Call String.getBytes() on source object and copy
   max bytes to dest buffer, then release Java byte array elements.
*/
void copyJstringBytes(JNIEnv *_env, char *dest, jstring source, int max)
{
	if (source!=NULL) {
		jboolean isCopy = JNI_TRUE;

		// String class
		jclass _stringCls = _env->FindClass(CLASS_STRING);
		if (_stringCls == NULL) {
			errorDialog(ERR_CLASS_NOT_FOUND, CLASS_STRING);
 			return;
		}

		// String.getBytes()
		jmethodID _getBytesMethodID = _env->GetMethodID(_stringCls, METHOD_STRING_GETBYTES,METHOD_STRING_GETBYTES_SIGNATURE);
		if (_getBytesMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRING, METHOD_STRING_GETBYTES,METHOD_STRING_GETBYTES_SIGNATURE);
			return;
		}

		jbyteArray chs = (jbyteArray) _env->CallObjectMethod(source, _getBytesMethodID);
		int len = _env->GetArrayLength(chs);
		jbyte* msgP = _env->GetByteArrayElements(chs, &isCopy);
		strncpy(dest,(char *)msgP,__min(max-1, len));
		dest[__min(max-1, len)]='\0';
		_env->ReleaseByteArrayElements(chs, msgP, JNI_ABORT);
	}
}

jobject __stdcall GetPlugin(JNIEnv *_env, char *className)
{
	// Retrieve PluginClassLoader
	jclass _loaderCls = _env->FindClass(CLASS_PLUGINCLASSLOADER);
	if (_loaderCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, CLASS_PLUGINCLASSLOADER);
		return NULL;
	}
	jmethodID _getPluginMethodID = _env->GetStaticMethodID(_loaderCls, METHOD_PLUGINCLASSLOADER_GETPLUGIN, METHOD_PLUGINCLASSLOADER_GETPLUGIN_SIGNATURE);
	// Retrieve the plugin
	jstring jmoduleFilename = _env->NewStringUTF(moduleFilename);
	jstring jpluginClass = _env->NewStringUTF(className);
	jobject _JPluginObject = _env->CallStaticObjectMethod(_loaderCls, _getPluginMethodID, jmoduleFilename, jpluginClass);
	if (exceptionHandling(_env, CLASS_PLUGINCLASSLOADER, METHOD_PLUGINCLASSLOADER_GETPLUGIN, METHOD_PLUGINCLASSLOADER_GETPLUGIN_SIGNATURE)) {
	  return NULL;
	}
	if (_JPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, className, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return NULL;
	}
	return _JPluginObject;
}

jobject __stdcall GetPluginClassLoader(JNIEnv *_env)
{
	// Retrieve PluginClassLoader
	jclass _loaderCls = _env->FindClass(CLASS_PLUGINCLASSLOADER);
	if (_loaderCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, CLASS_PLUGINCLASSLOADER);
		return NULL;
	}
	jmethodID _getPluginClassLoaderMethodID = _env->GetStaticMethodID(_loaderCls, METHOD_PLUGINCLASSLOADER_GETPLUGINCLASSLOADER, METHOD_PLUGINCLASSLOADER_GETPLUGINCLASSLOADER_SIGNATURE);
	// Retrieve the plugin classloader
	jstring jmoduleFilename = _env->NewStringUTF(moduleFilename);
	jobject _JPluginClassLoaderObject = _env->CallStaticObjectMethod(_loaderCls, _getPluginClassLoaderMethodID, jmoduleFilename);
	if (exceptionHandling(_env, CLASS_PLUGINCLASSLOADER, METHOD_PLUGINCLASSLOADER_GETPLUGINCLASSLOADER, METHOD_PLUGINCLASSLOADER_GETPLUGINCLASSLOADER_SIGNATURE)) {
	  return NULL;
	}
	return _JPluginClassLoaderObject;
}

jclass __stdcall GetClass(JNIEnv *_env, char *className)
{
	// Retrieve PluginClassLoader
	jclass _loaderCls = _env->FindClass(CLASS_PLUGINCLASSLOADER);
	if (_loaderCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, CLASS_PLUGINCLASSLOADER);
		return NULL;
	}
	/* Retrieve findClass of PluginClassLoader */
	jmethodID _jfindclassmid = _env->GetMethodID(_loaderCls, METHOD_PLUGINCLASSLOADER_LOADCLASS, METHOD_PLUGINCLASSLOADER_LOADCLASS_SIGNATURE);
	if (_jfindclassmid == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_PLUGINCLASSLOADER, METHOD_PLUGINCLASSLOADER_LOADCLASS, METHOD_PLUGINCLASSLOADER_LOADCLASS_SIGNATURE);
		return NULL;
	}

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);

	jstring _tmpclassname = _env->NewStringUTF(className);
	jclass _loadedCls = (jclass)_env->CallObjectMethod(_jClassLoaderObject, _jfindclassmid, _tmpclassname);
	if (exceptionHandling(_env, CLASS_PLUGINCLASSLOADER, METHOD_PLUGINCLASSLOADER_LOADCLASS, METHOD_PLUGINCLASSLOADER_LOADCLASS_SIGNATURE)) {
		return NULL;
	}
	if (_loadedCls == NULL) {
		errorDialog(ERR_CLASS_NOT_FOUND, className);
		return NULL;
	}
	_env->ReleaseStringUTFChars(_tmpclassname, 0);
	return _loadedCls;
}