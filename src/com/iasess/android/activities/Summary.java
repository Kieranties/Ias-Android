package com.iasess.android.activities;

import org.iasess.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Summary extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
    }
    
    public void onDoneClick(View v){
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
}

