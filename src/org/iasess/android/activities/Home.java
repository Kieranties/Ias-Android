package org.iasess.android.activities;

import org.iasess.android.ImageHandler;
import org.iasess.android.Logger;
import org.iasess.android.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class Home extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			Uri selected = ImageHandler.getImageUriFromIntentResult(resultCode, requestCode, data);
			if(selected != null){
				Intent intent = new Intent(this, AddPhoto.class);
				intent.setData(selected);
				startActivity(intent);
			}
			else{
				Logger.warn("Could not get a selected image");
			}
		}		
	}
    
    /** Events **/
    public void onAddPhotoClick(View v) {
    	ImageHandler.getImage(this);
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

