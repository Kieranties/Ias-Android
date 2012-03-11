package com.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Settings extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);      
        
        getUsernameText().setText(IasessApp.getPreferenceString(IasessApp.PREFS_USERNAME));
    }
    
    public void onDoneClick(View v){
    	IasessApp.setPreferenceString(IasessApp.PREFS_USERNAME, getUsernameText().getText().toString());
    	finish();
    }
    
    private EditText getUsernameText(){
    	return (EditText)findViewById(R.id.editUsername);
    }
}

