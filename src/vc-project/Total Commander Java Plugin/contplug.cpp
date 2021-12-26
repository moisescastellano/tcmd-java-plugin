#include "stdafx.h"
#include "contplug.h"
#include "java.h"

EXTERN jobject __stdcall GetPlugin(JNIEnv *, char *);

EXTERN jobject __stdcall GetPluginClassLoader(JNIEnv *);

EXTERN jclass __stdcall GetClass(JNIEnv *, char *);

/*time_t FileTimeToUnixTime( FILETIME FileTime, long *nsec );*/
void UnixTimeToFileTime(time_t t, LPFILETIME ft);

void __stdcall ContentGetDetectString(char* DetectString,int maxlen)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentGetDetectStringMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTGETDETECTSTRING, METHOD_WDXPLUGIN_CONTENTGETDETECTSTRING_SIGNATURE);
	if (_wdxContentGetDetectStringMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETDETECTSTRING, METHOD_WDXPLUGIN_CONTENTGETDETECTSTRING_SIGNATURE);
		return;
	}

	//WDXPlugin.contentGetDetectString(DetectString, maxlen);
	jstring detectstring = (jstring)_env->CallObjectMethod(_wdxJPluginObject, _wdxContentGetDetectStringMethodID, maxlen);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETDETECTSTRING, METHOD_WDXPLUGIN_CONTENTGETDETECTSTRING_SIGNATURE)) {
		return;
    }
	copyJstringBytes(_env, DetectString, detectstring, maxlen);
}
int __stdcall ContentGetSupportedField(int FieldIndex,char* FieldName,char* Units,int maxlen)
{
	JNIEnv *_env= startJVM();
	if (_env == NULL) {
		return ft_nomorefields;
	}

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentGetSupportedFieldMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTGETSUPPORTFIELD, METHOD_WDXPLUGIN_CONTENTGETSUPPORTFIELD_SIGNATURE);
	if (_wdxContentGetSupportedFieldMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETSUPPORTFIELD, METHOD_WDXPLUGIN_CONTENTGETSUPPORTFIELD_SIGNATURE);
		return ft_nomorefields;
	}

	/* WDXPlugin.ContentGetSupportedField(FieldIndex, FieldName, Units, maxlen); */
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
	jmethodID _toStringMethodID = _env->GetMethodID(_stringBufferCls, METHOD_STRINGBUFFER_TOSTRING, METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
	if (_toStringMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_STRINGBUFFER, METHOD_STRINGBUFFER_TOSTRING,METHOD_STRINGBUFFER_TOSTRING_SIGNATURE);
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

	jint _output = _env->CallIntMethod(_wdxJPluginObject, _wdxContentGetSupportedFieldMethodID, index, _stringBufferNameObject, _stringBufferUnitsObject, max);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETSUPPORTFIELD, METHOD_WDXPLUGIN_CONTENTGETSUPPORTFIELD_SIGNATURE)) {
		return ft_nomorefields;
    }

	jstring name = (jstring)_env->CallObjectMethod(_stringBufferNameObject, _toStringMethodID);
	copyJstringBytes(_env, FieldName, name, maxlen);

	jstring units = (jstring)_env->CallObjectMethod(_stringBufferUnitsObject, _toStringMethodID);
	copyJstringBytes(_env, Units, units, maxlen);

	return _output;
}

int __stdcall ContentGetValue(char* FileName,int FieldIndex,int UnitIndex,void* FieldValue,int maxlen,int flags)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentGetValueMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTGETVALUE, METHOD_WDXPLUGIN_CONTENTGETVALUE_SIGNATURE);
	if (_wdxContentGetValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETVALUE, METHOD_WDXPLUGIN_CONTENTGETVALUE_SIGNATURE);
		return ft_nomorefields;
	}

	/* WDXPlugin.ContentGetValue(FileName); */
	jstring name = _env->NewStringUTF(FileName);
	jint index = (jint)FieldIndex;
	jint uindex = (jint)UnitIndex;

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _fieldValueCls = GetClass(_env, CLASS_FIELDVALUE);

	jmethodID _fieldValueMethodID = _env->GetMethodID(_fieldValueCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_fieldValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_RECTANGLE, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
	}
	
	/* new FieldValue() */
	jobject _wdxFieldValueObject = _env->NewObject(_fieldValueCls, _fieldValueMethodID);
    if (_wdxFieldValueObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_FIELDVALUE, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
    }
	jint max = (jint)maxlen;
	jint flag= (jint)flags;

	jint _output = _env->CallIntMethod(_wdxJPluginObject, _wdxContentGetValueMethodID, name, index, uindex, _wdxFieldValueObject, max, flag);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETVALUE, METHOD_WDXPLUGIN_CONTENTGETVALUE_SIGNATURE)) {
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
		return ft_nomorefields;
	}
	jint fieldType = _env->GetIntField(_wdxFieldValueObject, _fieldTypeID);
	if ((int)fieldType==ft_numeric_32) {
		jfieldID _intValueID = _env->GetFieldID (_fieldValueCls, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
		if (_intValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
 			return ft_nomorefields;
		}
		*(int *)FieldValue= (int)_env->GetIntField(_wdxFieldValueObject, _intValueID);
	} else if ((int)fieldType==ft_numeric_64) {
		jfieldID _longValueID = _env->GetFieldID (_fieldValueCls, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
		if (_longValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
 			return ft_nomorefields;
		}
		*(__int64*)FieldValue = (__int64)_env->GetLongField(_wdxFieldValueObject, _longValueID);
	} else if ((int)fieldType==ft_numeric_floating) {
		jfieldID _doubleValueID = _env->GetFieldID (_fieldValueCls, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
		if (_doubleValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
 			return ft_nomorefields;
		}
		*(double *)FieldValue= (double)_env->GetDoubleField(_wdxFieldValueObject, _doubleValueID);
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

		int Year = (int)_env->GetIntField(_env->GetObjectField(_wdxFieldValueObject, _dateValueID), _fYearID);
		int Month =(int)_env->GetIntField(_env->GetObjectField(_wdxFieldValueObject, _dateValueID), _fMonthID);
		int Day =(int)_env->GetIntField(_env->GetObjectField(_wdxFieldValueObject, _dateValueID), _fDayID);
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

		int Hour = (int)_env->GetIntField(_env->GetObjectField(_wdxFieldValueObject, _timeValueID), _fHourID);
    	int Minute = (int)_env->GetIntField(_env->GetObjectField(_wdxFieldValueObject, _timeValueID), _fMinuteID);
    	int Second = (int)_env->GetIntField(_env->GetObjectField(_wdxFieldValueObject, _timeValueID), _fSecondID);
		((ptimeformat)FieldValue)->wHour = Hour;
		((ptimeformat)FieldValue)->wMinute = Minute;
		((ptimeformat)FieldValue)->wSecond = Second;
	} else if ((int)fieldType==ft_boolean) {
		jfieldID _booleanValueID = _env->GetFieldID (_fieldValueCls, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
		if (_booleanValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
 			return ft_nomorefields;
		}

		*(bool *)FieldValue= (bool)_env->GetBooleanField(_wdxFieldValueObject, _booleanValueID);
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
		jlong fileTime = _env->GetLongField(_env->GetObjectField(_wdxFieldValueObject, _fileTimeID), _dwLowDateTimeID);
		UnixTimeToFileTime(fileTime/1000, &ft);
		*(LPFILETIME)FieldValue= ft;
	} else if ((int)fieldType==ft_string || (int)fieldType==ft_multiplechoice || (int)fieldType==ft_fulltext) {
		jfieldID _strID = _env->GetFieldID (_fieldValueCls, FIELD_STR, FIELD_STR_SIGNATURE);
		if (_strID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_STR, FIELD_STR_SIGNATURE);
 			return ft_nomorefields;
		}

		jstring str = (jstring)_env->GetObjectField(_wdxFieldValueObject, _strID);
		copyJstringBytes(_env, (char *)FieldValue, str, maxlen);
	}
	return _output;
}

void __stdcall ContentSetDefaultParams(ContentDefaultParamStruct* dps)
{
	JNIEnv *_env= startJVM();
	if (_env == NULL) {
		return;
	}

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentSetDefaultParamsMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTDEFAULTGETPARAMS, METHOD_WDXPLUGIN_CONTENTDEFAULTGETPARAMS_SIGNATURE);
	if (_wdxContentSetDefaultParamsMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTDEFAULTGETPARAMS, METHOD_WDXPLUGIN_CONTENTDEFAULTGETPARAMS_SIGNATURE);
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
	jobject _wdxDefaultParamObject = _env->NewObject(_defaultParamCls, _defaultParamMethodID, (jint)dps->size, (jlong)dps->PluginInterfaceVersionLow, (jlong)dps->PluginInterfaceVersionHi, iniName);
    if (_wdxDefaultParamObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_DEFAULTPARAM, CLASS_INIT,CLASS_DEFAULTPARAM_INIT_SIGNATURE);
		return;
    }

	// WDXPlugin.ContentSetDefaultParams(dps);
	_env->CallVoidMethod(_wdxJPluginObject, _wdxContentSetDefaultParamsMethodID, _wdxDefaultParamObject);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTDEFAULTGETPARAMS, METHOD_WDXPLUGIN_CONTENTDEFAULTGETPARAMS_SIGNATURE)) {
		if (iniName != NULL) {
			_env->ReleaseStringUTFChars(iniName, 0);
		}
		return;
    }

	if (iniName != NULL) {
		_env->ReleaseStringUTFChars(iniName, 0);
	}
}

void __stdcall ContentStopGetValue(char* FileName)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentStopGetValueMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTSTOPGETVALUE, METHOD_WDXPLUGIN_CONTENTSTOPGETVALUE_SIGNATURE);
	if (_wdxContentStopGetValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTSTOPGETVALUE, METHOD_WDXPLUGIN_CONTENTSTOPGETVALUE_SIGNATURE);
		return;
	}

	/* WDXPlugin.ContentStopGetValue(FileName); */
	jstring name = _env->NewStringUTF(FileName);

	_env->CallVoidMethod(_wdxJPluginObject, _wdxContentStopGetValueMethodID, name);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTSTOPGETVALUE, METHOD_WDXPLUGIN_CONTENTSTOPGETVALUE_SIGNATURE)) {
		if (name != NULL) {
			_env->ReleaseStringUTFChars(name, 0);
		}
		return;
    }

	if (name != NULL) {
		_env->ReleaseStringUTFChars(name, 0);
	}
}

int __stdcall ContentGetDefaultSortOrder(int FieldIndex)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentGetDefaultSortOrderMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTGETDEFAULTSORTORDER, METHOD_WDXPLUGIN_CONTENTGETDEFAULTSORTORDER_SIGNATURE);
	if (_wdxContentGetDefaultSortOrderMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETDEFAULTSORTORDER, METHOD_WDXPLUGIN_CONTENTGETDEFAULTSORTORDER_SIGNATURE);
		return 0;
	}

	/* WDXPlugin.ContentGetDefaultSortOrder(FieldIndex); */
	jint index = (jint)FieldIndex;
	jint _output = _env->CallIntMethod(_wdxJPluginObject, _wdxContentGetDefaultSortOrderMethodID, index);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETDEFAULTSORTORDER, METHOD_WDXPLUGIN_CONTENTGETDEFAULTSORTORDER_SIGNATURE)) {
		return 0;
    }
	return _output;
}

void __stdcall ContentPluginUnloading(void) {
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentPluginUnloadingMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTPLUGINUNLOADING, METHOD_WDXPLUGIN_CONTENTPLUGINUNLOADING_SIGNATURE);
	if (_wdxContentPluginUnloadingMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTPLUGINUNLOADING, METHOD_WDXPLUGIN_CONTENTPLUGINUNLOADING_SIGNATURE);
		return;
	}

	/* WDXPlugin.ContentPluginUnloading(); */
	_env->CallVoidMethod(_wdxJPluginObject, _wdxContentPluginUnloadingMethodID);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTPLUGINUNLOADING, METHOD_WDXPLUGIN_CONTENTPLUGINUNLOADING_SIGNATURE)) {
		return;
    }
}

int __stdcall ContentGetSupportedFieldFlags(int FieldIndex)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return 0;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentGetSupportedFieldFlagsMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTGETSUPPORTEDFIELDFLAGS, METHOD_WDXPLUGIN_CONTENTGETSUPPORTEDFIELDFLAGS_SIGNATURE);
	if (_wdxContentGetSupportedFieldFlagsMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETSUPPORTEDFIELDFLAGS, METHOD_WDXPLUGIN_CONTENTGETSUPPORTEDFIELDFLAGS);
		return 0;
	}

	/* WDXPlugin.ContentGetSupportedFieldFlags(FieldIndex); */
	jint index = (jint)FieldIndex;
	jint _output = _env->CallIntMethod(_wdxJPluginObject, _wdxContentGetSupportedFieldFlagsMethodID, index);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTGETSUPPORTEDFIELDFLAGS, METHOD_WDXPLUGIN_CONTENTGETSUPPORTEDFIELDFLAGS_SIGNATURE)) {
		return 0;
    }
	return _output;
}

int __stdcall ContentSetValue(char* FileName,int FieldIndex,int UnitIndex,int FieldType,
                void* FieldValue,int flags)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nosuchfield;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentSetValueMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTSETVALUE, METHOD_WDXPLUGIN_CONTENTSETVALUE_SIGNATURE);
	if (_wdxContentSetValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTSETVALUE, METHOD_WDXPLUGIN_CONTENTSETVALUE_SIGNATURE);
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
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_RECTANGLE, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nomorefields;
	}

	/* new FieldValue() */
	jobject _wdxFieldValueObject = _env->NewObject(_fieldValueCls, _fieldValueMethodID);
    if (_wdxFieldValueObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_FIELDVALUE, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return ft_nosuchfield;
    }
	jobject _fieldValue;
	if ((int)fieldType==ft_numeric_32) {
		// OK
		signed int val = *((signed int*)FieldValue);

		jfieldID _intValueID = _env->GetFieldID (_fieldValueCls, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
		if (_intValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}
		_env->SetIntField(_wdxFieldValueObject, _intValueID, (jint) val);
	} else if ((int)fieldType==ft_numeric_64) {
		// OK
		__int64 val = *((__int64*)FieldValue);

		jfieldID _longValueID = _env->GetFieldID (_fieldValueCls, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
		if (_longValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetLongField(_wdxFieldValueObject, _longValueID, (jlong) val);
	} else if ((int)fieldType==ft_numeric_floating) {
		// OK
		double val = *((double*)FieldValue);

		jfieldID _doubleValueID = _env->GetFieldID (_fieldValueCls, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
		if (_doubleValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetDoubleField(_wdxFieldValueObject, _doubleValueID, (jdouble) val);
	} else if ((int)fieldType==ft_date) {
		// OK

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

		_env->SetObjectField(_wdxFieldValueObject, _dateValueID, _localDate);
	} else if ((int)fieldType==ft_time) {
		// OK

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

		_env->SetObjectField(_wdxFieldValueObject, _timeValueID, _localTime);
	} else if ((int)fieldType==ft_boolean) {
		// OK
		int val = *((int*)FieldValue);

		jfieldID _booleanValueID = _env->GetFieldID (_fieldValueCls, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
		if (_booleanValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetBooleanField(_wdxFieldValueObject, _booleanValueID, (jboolean) val);
	} else if ((int)fieldType==ft_datetime) {
		// OK
		FILETIME ft = *((FILETIME*)FieldValue);

		jobject _jClassLoaderObject = GetPluginClassLoader(_env);
		
		jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

		jmethodID _fileTimeMethodID = _env->GetMethodID(_fileTimeCls, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
		if (_fileTimeMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
			return NULL;
		}

		jobject _fileTime = _env->NewObject(_fileTimeCls, _fileTimeMethodID, (jlong)ft.dwLowDateTime, (jlong)ft.dwHighDateTime);

		jfieldID _fileTimeID = _env->GetFieldID (_fieldValueCls, FIELD_FILETIME, FIELD_FILETIME_SIGNATURE);
		if (_fileTimeID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_FILETIME, FIELD_FILETIME_SIGNATURE);
 			return ft_nosuchfield;
		}
		_env->SetObjectField(_wdxFieldValueObject, _fileTimeID, _fileTime);
	} else if ((int)fieldType==ft_string || (int)fieldType==ft_fulltext) {
		jstring str = _env->NewStringUTF((char *)FieldValue);

		jfieldID _strID = _env->GetFieldID (_fieldValueCls, FIELD_STR, FIELD_STR_SIGNATURE);
		if (_strID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_STR, FIELD_STR_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetObjectField(_wdxFieldValueObject, _strID, str);
	} else if ((int)fieldType==ft_multiplechoice) {
		// OK
		char *val = *((char **)FieldValue);
		jstring str = _env->NewStringUTF(val);

		jfieldID _strID = _env->GetFieldID (_fieldValueCls, FIELD_STR, FIELD_STR_SIGNATURE);
		if (_strID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_STR, FIELD_STR_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetObjectField(_wdxFieldValueObject, _strID, str);
	}
	jfieldID _fieldTypeID = _env->GetFieldID (_fieldValueCls, FIELD_FIELDTYPE, FIELD_FIELDTYPE_SIGNATURE);
	if (_fieldTypeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_FIELDTYPE, FIELD_FIELDTYPE_SIGNATURE);
		return ft_nosuchfield;
	}
	_env->SetIntField(_wdxFieldValueObject, _fieldTypeID, fieldType);
	jint flag= (jint)flags;

	/* WDXPlugin.ContentSetValue(FileName, FieldIndex, UnitIndex, FieldType, FieldValue, flags); */
	jint _output = _env->CallIntMethod(_wdxJPluginObject, _wdxContentSetValueMethodID, name, index, uindex, fieldType, _wdxFieldValueObject, flag);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTSETVALUE, METHOD_WDXPLUGIN_CONTENTSETVALUE_SIGNATURE)) {
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

int __stdcall ContentEditValue(HWND ParentWin,int FieldIndex,int UnitIndex,int FieldType,
                void* FieldValue,int maxlen,int flags,char* langidentifier)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nosuchfield;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentEditValueMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTEDITVALUE, METHOD_WDXPLUGIN_CONTENTEDITVALUE_SIGNATURE);
	if (_wdxContentEditValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTEDITVALUE, METHOD_WDXPLUGIN_CONTENTEDITVALUE_SIGNATURE);
		return ft_nosuchfield;
	}

	jint parentWin = (jint)ParentWin;
	jint index = (jint)FieldIndex;
	jint uindex = (jint)UnitIndex;
	jint fieldType = (jint)FieldType;

	jobject _jClassLoaderObject = GetPluginClassLoader(_env);
	
	jclass _fieldValueCls = GetClass(_env, CLASS_FIELDVALUE);

	jmethodID _fieldValueMethodID = _env->GetMethodID(_fieldValueCls, CLASS_INIT,CLASS_INIT_SIGNATURE);
	if (_fieldValueMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, CLASS_RECTANGLE, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return ft_nosuchfield;
	}

	/* new FieldValue() */
	jobject _wdxFieldValueObject = _env->NewObject(_fieldValueCls, _fieldValueMethodID);
    if (_wdxFieldValueObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, CLASS_FIELDVALUE, CLASS_INIT, CLASS_INIT_SIGNATURE);
		return ft_nosuchfield;
    }
	jobject _fieldValue;
	if ((int)fieldType==ft_numeric_32) {
		// OK
		signed int val = *((signed int*)FieldValue);

		jfieldID _intValueID = _env->GetFieldID (_fieldValueCls, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
		if (_intValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetIntField(_wdxFieldValueObject, _intValueID, (jint) val);
	} else if ((int)fieldType==ft_numeric_64) {
		// OK
		__int64 val = *((__int64*)FieldValue);

		jfieldID _longValueID = _env->GetFieldID (_fieldValueCls, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
		if (_longValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetLongField(_wdxFieldValueObject, _longValueID, (jlong) val);
	} else if ((int)fieldType==ft_numeric_floating) {
		// OK
		double val = *((double*)FieldValue);

		jfieldID _doubleValueID = _env->GetFieldID (_fieldValueCls, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
		if (_doubleValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetDoubleField(_wdxFieldValueObject, _doubleValueID, (jdouble) val);
	} else if ((int)fieldType==ft_date) {
		// OK

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

		_env->SetObjectField(_wdxFieldValueObject, _dateValueID, _localDate);
	} else if ((int)fieldType==ft_time) {
		// OK

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

		_env->SetObjectField(_wdxFieldValueObject, _timeValueID, _localTime);
	} else if ((int)fieldType==ft_boolean) {
		int val = *((int*)FieldValue);

		jfieldID _booleanValueID = _env->GetFieldID (_fieldValueCls, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
		if (_booleanValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_BOOLEANVALUE, FIELD_BOOLEANVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetBooleanField(_wdxFieldValueObject, _booleanValueID, (jboolean) val);
	} else if ((int)fieldType==ft_datetime) {
		// OK
		FILETIME ft = *((FILETIME*)FieldValue);

		jobject _jClassLoaderObject = GetPluginClassLoader(_env);
		
		jclass _fileTimeCls = GetClass(_env, CLASS_FILETIME);

		jmethodID _fileTimeMethodID = _env->GetMethodID(_fileTimeCls, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
		if (_fileTimeMethodID == NULL) {
			errorDialog(ERR_METHOD_NOT_FOUND, CLASS_FILETIME, CLASS_INIT,CLASS_FILETIME_INIT_SIGNATURE);
			return NULL;
		}

		jobject _fileTime = _env->NewObject(_fileTimeCls, _fileTimeMethodID, (jlong)ft.dwLowDateTime, (jlong)ft.dwHighDateTime);

		jfieldID _fileTimeID = _env->GetFieldID (_fieldValueCls, FIELD_FILETIME, FIELD_FILETIME_SIGNATURE);
		if (_fileTimeID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_FILETIME, FIELD_FILETIME_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetObjectField(_wdxFieldValueObject, _fileTimeID, _fileTime);
	} else if ((int)fieldType==ft_string || (int)fieldType==ft_fulltext) {
		jstring str = _env->NewStringUTF((char *)FieldValue);

		jfieldID _strID = _env->GetFieldID (_fieldValueCls, FIELD_STR, FIELD_STR_SIGNATURE);
		if (_strID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_STR, FIELD_STR_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetObjectField(_wdxFieldValueObject, _strID, str);
	} else if ((int)fieldType==ft_multiplechoice) {
		// OK
		char *val = *((char **)FieldValue);
		jstring str = _env->NewStringUTF(val);

		jfieldID _strID = _env->GetFieldID (_fieldValueCls, FIELD_STR, FIELD_STR_SIGNATURE);
		if (_strID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_STR, FIELD_STR_SIGNATURE);
 			return ft_nosuchfield;
		}

		_env->SetObjectField(_wdxFieldValueObject, _strID, str);
	}
	jfieldID _fieldTypeID = _env->GetFieldID (_fieldValueCls, FIELD_FIELDTYPE, FIELD_FIELDTYPE_SIGNATURE);
	if (_fieldTypeID == NULL) {
		errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_FIELDTYPE, FIELD_FIELDTYPE_SIGNATURE);
		return ft_nosuchfield;
	}
	_env->SetIntField(_wdxFieldValueObject, _fieldTypeID, fieldType);
	jint max= (jint)maxlen;
	jint flag= (jint)flags;
	jstring lang = _env->NewStringUTF(langidentifier);

	/* WDXPlugin.ContentEditValue(parentWin, FieldIndex, UnitIndex, FieldType, FieldValue, maxlen, flags, langidentifier); */
	jint _output = _env->CallIntMethod(_wdxJPluginObject, _wdxContentEditValueMethodID, parentWin, index, uindex, fieldType, _wdxFieldValueObject, maxlen, flag, lang);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTEDITVALUE, METHOD_WDXPLUGIN_CONTENTEDITVALUE_SIGNATURE)) {
		return ft_nosuchfield;
    }
	fieldType = _env->GetIntField(_wdxFieldValueObject, _fieldTypeID);
	if ((int)fieldType==ft_numeric_32) {
		// OK

		jfieldID _intValueID = _env->GetFieldID (_fieldValueCls, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
		if (_intValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_INTVALUE, FIELD_INTVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		jint val = _env->GetIntField(_wdxFieldValueObject, _intValueID);
		*((int *)FieldValue)= (int)val;
	} else if ((int)fieldType==ft_numeric_64) {
		// OK
		
		jfieldID _longValueID = _env->GetFieldID (_fieldValueCls, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
		if (_longValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_LONGVALUE, FIELD_LONGVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		jlong val = _env->GetLongField(_wdxFieldValueObject, _longValueID);
		*((__int64*)FieldValue) = (__int64)val;
	} else if ((int)fieldType==ft_numeric_floating) {
		// OK
		
		jfieldID _doubleValueID = _env->GetFieldID (_fieldValueCls, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
		if (_doubleValueID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_DOUBLEVALUE, FIELD_DOUBLEVALUE_SIGNATURE);
 			return ft_nosuchfield;
		}

		jdouble val = _env->GetDoubleField(_wdxFieldValueObject, _doubleValueID);
		*((double *)FieldValue)= (double)val;
	} else if ((int)fieldType==ft_string || (int)fieldType==ft_fulltext) {
		// OK

		jfieldID _strID = _env->GetFieldID (_fieldValueCls, FIELD_STR, FIELD_STR_SIGNATURE);
		if (_strID == NULL) {
			errorDialog(ERR_FIELD_NOT_FOUND, CLASS_FIELDVALUE, FIELD_STR, FIELD_STR_SIGNATURE);
 			return ft_nosuchfield;
		}

		jstring val = (jstring) _env->GetObjectField(_wdxFieldValueObject, _strID);
		copyJstringBytes(_env, (char *)FieldValue, val, maxlen);
	}

	return _output;
}

void __stdcall ContentSendStateInformation(int State,char* Path)
{
	JNIEnv *_env;
	_jvm->AttachCurrentThread((void**) &_env, NULL);

	// Get plugin instance
	jobject _wdxJPluginObject = GetPlugin(_env, wdxPluginClass);
	if (_wdxJPluginObject == NULL) {
		errorDialog(ERR_CLASS_INIT_FAILED, wfxPluginClass, CLASS_INIT,CLASS_INIT_SIGNATURE);
		return;
	}

	// Get plugin class
	jclass _wdxCls = _env->GetObjectClass(_wdxJPluginObject);

	jmethodID _wdxContentSendStateInformationMethodID = _env->GetMethodID(_wdxCls, METHOD_WDXPLUGIN_CONTENTSENDSTATEINFORMATION, METHOD_WDXPLUGIN_CONTENTSENDSTATEINFORMATION_SIGNATURE);
	if (_wdxContentSendStateInformationMethodID == NULL) {
		errorDialog(ERR_METHOD_NOT_FOUND, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTSENDSTATEINFORMATION, METHOD_WDXPLUGIN_CONTENTSENDSTATEINFORMATION_SIGNATURE);
		return;
	}

	/* WDXPlugin.ContentSendStateInformation(state, path); */
	jint state = (jint)State;
	jstring path = _env->NewStringUTF(Path);
	jint _output = _env->CallIntMethod(_wdxJPluginObject, _wdxContentSendStateInformationMethodID, state, path);
	if (exceptionHandling(_env, wdxPluginClass, METHOD_WDXPLUGIN_CONTENTSENDSTATEINFORMATION, METHOD_WDXPLUGIN_CONTENTSENDSTATEINFORMATION_SIGNATURE)) {
		return;
    }
}
