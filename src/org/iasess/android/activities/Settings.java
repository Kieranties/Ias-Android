package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/*
 * Activity for the Settings screen
 */
public class Settings extends Activity {
    
	/*
	 * The text control containing the user details
	 */
	private EditText _editText = (EditText)findViewById(R.id.editUsername);
	
	/*
	 * Initializer
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);      
        
        //set from application preferences
        _editText.setText(IasessApp.getPreferenceString(IasessApp.PREFS_USERNAME));
    }
    
    /*
     * Handles the click event to set preference and end activity
     */
    public void onDoneClick(View v){
    	IasessApp.setPreferenceString(IasessApp.PREFS_USERNAME, _editText.getText().toString());
    	finish();
    }
}