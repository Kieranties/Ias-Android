package com.iasess.android.activities;

import org.iasess.android.ImageHandler;
import org.iasess.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddPhoto extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
    }
    
    public void onNextClick(View v){
    	//TODO: Implement taxa listing view and hook in activity
    	//Intent intent = new Intent(this, SelectTaxa.class);
    	Intent intent = new Intent(this, Summary.class);
    	startActivity(intent);
    }
    
    public void onImageClick(View v){
    	ImageHandler.getImage(this);
    }
}

