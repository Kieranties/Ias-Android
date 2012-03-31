package org.iasess.android;

import android.app.Application;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/*
 * Application wide methods
 */
public class IasessApp extends Application {

	private static ContextWrapper _context;
	
	/*
	 * Reference for username property
	 */
	public static final String PREFS_USERNAME = "username";
	
	/*
	 * Reference for selected taxa property
	 * (used when passing between Activities/Intents
	 */
	public static final String SELECTED_TAXA = "selectedTaxa";
	
	/*
	 * Getter for application context
	 */
	public static final ContextWrapper getContext(){
		return _context;
	}
	
	/*
	 * Returns the default shared preferences for the application
	 */
	public static final SharedPreferences getPreferences(){
		return PreferenceManager.getDefaultSharedPreferences(_context);
	}
	
	/*
	 * Returns the requested preference string.  If not found returns the
	 * given default value
	 */
	public static final String getPreferenceString(String key, String defValue){
		return getPreferences().getString(key, defValue);
	}
	
	/*
	 * Returns the request preference string.  If not found returns a
	 * default value of ""
	 */
	public static final String getPreferenceString(String key){
		return getPreferenceString(key, "");
	}
	
	/*
	 * Sets the given preference string with the given value
	 */
	public static final void setPreferenceString(String key, String value){
		Editor editor = getPreferences().edit();
		editor.putString(key, value);
		editor.commit();
	}	
	
	/*
	 * Returns the requested resource string from the application context
	 */
	public static final String getResourceString(int id){
		return _context.getString(id);
	}
	
	@Override
	/*
	 * Initializer
	 */
	public void onCreate() {
		super.onCreate();
		_context = (ContextWrapper) getApplicationContext();		
	}
}
