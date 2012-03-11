package org.iasess.android;

import android.content.ContextWrapper;
import android.util.Log;

public final class Logger {
	private Logger(){}
	
	private static String getTagName(ContextWrapper context){
		return context.getResources().getString(R.string.app_name);
	}
	
	public static void debug(ContextWrapper context, String message){
		Log.d(getTagName(context), message);
	}
	
	public static void warn(ContextWrapper context, String message){
		Log.w(getTagName(context), message);
	}
}
