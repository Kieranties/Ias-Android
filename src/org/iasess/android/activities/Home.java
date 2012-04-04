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

/*
 * Activity to handle the Home screen
 */
public class Home extends Activity {
	
	/*
	 * Initializer
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        //check we have a username saved
        if(IasessApp.getPreferenceString(IasessApp.PREFS_USERNAME).equals("")){
        	displaySettings();
        }
    }
    
    
   /*
    * Handles the click event to request a photo
    */
    public void onAddPhotoClick(View v) {
    	ImageHandler.getImage(this);
    }
    
    /*
     * Handles the click event to view the gallery
     */
    public void onViewGalleryClick(View v) {
    	Intent intent = new Intent(this, Gallery.class);
    	startActivity(intent);
    }
    
    /*
     * Handles the click event to view the settings
     */
    public void onSettingsClick(View v) {
    	displaySettings();
    }
    
    /*
     * Handles the click event to view the about activity
     */
    public void onAboutClick(View v) {    	
    	Intent intent = new Intent(this, About.class);
    	startActivity(intent);
    }
    
    /*
     * Handles the result of an intent
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
    
    private void displaySettings(){
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
}