package org.iasess.android;

import android.util.Log;

/*
 * Application wide instance to handle logging
 */
public final class Logger {
	
	/*
	 * The application wide tag name used for entris to LogCat
	 */
	private static String _tagName = IasessApp.getContext().getResources().getString(R.string.app_name);
	
	/*
	 * Static constructor - Don't want instances of this class created
	 */
	private Logger(){}
	
	/*
	 * Logs the given DEBUG message to LogCat
	 */
	public static void debug(String message){
		Log.d(_tagName, message);
	}
	
	/*
	 * Logs the given WARNING message to LogCat
	 */
	public static void warn(String message){
		Log.w(_tagName, message);
	}
}