package com.iasess.android.activities;

import org.iasess.android.ImageHandler;
import org.iasess.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }
    
    /** Events **/
    public void onAddPhotoClick(View v) {
    	//get the user to select photo first
    	ImageHandler.getImage(this);
    	//Intent intent = new Intent(this, AddPhoto.class);
    	//startActivity(intent);
    }
    public void onViewGalleryClick(View v) {
    	Intent intent = new Intent(this, Gallery.class);
    	startActivity(intent);
    }
    public void onSettingsClick(View v) {
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
    public void onAboutClick(View v) {    	
    	Intent intent = new Intent(this, About.class);
    	startActivity(intent);
    }
}

