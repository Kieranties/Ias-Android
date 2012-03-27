package org.iasess.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class IasessApp extends Application {

	private static Context context;
	
	/** Application wide constants **/
	public static final String PREFS_USERNAME = "username";
	
	/** Application wide properties **/
	public static final Context getContext(){
		return context;
	}
	
	public static final SharedPreferences getPreferences(){
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static final String getPreferenceString(String key, String defValue){
		return getPreferences().getString(key, defValue);
	}
	public static final String getPreferenceString(String key){
		return getPreferenceString(key, "");
	}
	
	public static final void setPreferenceString(String key, String value){
		Editor editor = getPreferences().edit();
		editor.putString(key, value);
		editor.commit();
	}	
	
	public static final String getResourceString(int id){
		return context.getString(id);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = getApplicationContext();		
	}
}
