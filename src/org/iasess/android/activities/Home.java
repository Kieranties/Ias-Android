package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.ImageHandler;
import org.iasess.android.Logger;
import org.iasess.android.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * Controls the 'Home' Activity view
 */
public class Home extends Activity {
	
    /**
     * Initialises the content of the Activity
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        //check we have a username saved
        if(IasessApp.getPreferenceString(IasessApp.PREFS_USERNAME).equals("")){
        	//if we don't, show the settings screen.
        	displaySettings();
        }
    }
    
    
    /**
     * Handler to pass control to the image selection process
     * 
     * @param v The {@link View} which fired the event handler
     */
    public void onAddPhotoClick(View v) {
    	ImageHandler.getImage(this);
    }
    
    
    /**
     * Handler to populate and process an Intent to 
     * pass control to the gallery view of the application
     * 
     * @param v The {@link View} which fired the event handler
     */
    public void onViewGalleryClick(View v) {
    	Intent intent = new Intent(this, TaxaListing.class);
    	intent.putExtra("gallery", true);
    	startActivity(intent);
    }
    
    /**
     * Handler to populate and process an Intent
     * to pass control of the application to the settings Activity
     * 
     * @param v The {@link View} which fired the event handler
     */
    public void onSettingsClick(View v) {
    	displaySettings();
    }
    
    /**
     * Handler to populate and process an Intent to 
     * pass control of the application to the About Activity
     * 
     * @param v The {@link View} which fired the event handler
     */
    public void onAboutClick(View v) {    	
    	Intent intent = new Intent(this, About.class);
    	startActivity(intent);
    }
    
    /**
     * Handles the response of an ActivityResult fired in the context of
     * this Activity
     * 
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			//we're expecting the intent to be an image intent initiated by this app
			Uri selected = ImageHandler.getImageUriFromIntentResult(resultCode, requestCode, data);
			if(selected != null){
				//pass data to next activity
				Intent intent = new Intent(this, AddPhoto.class);
				intent.setData(selected);
				startActivity(intent);
			}
			else{
				Logger.warn("Could not get a selected image");
			}
		}		
	}
    
    /**
     * Creates and starts a new Settings Activity
     */
    private void displaySettings(){
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
}