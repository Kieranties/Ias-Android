package org.iasess.android.activities;

import java.net.URI;

import org.iasess.android.ImageHandler;
import org.iasess.android.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Controls the 'AddPhoto' Activity view
 */
public class AddPhoto extends Activity {
    
	/**
	 * The {@link URI} selected by the user
	 */
	private Uri _selectedUri = null;
	
	/**
	 * Initialises the content of the Activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        
        //if we have a selected image, set it
        Uri selected = getIntent().getData();
        if(selected != null) setImageView(selected);
    }    
	
    /**
     * Handler to populate and execute an Intent
     * to pass control to the next stage of the application
     * 
     * @param v The {@link View} which fired the event handler
     */
    public void onNextClick(View v){
    	Intent intent = new Intent(this, TaxaListing.class);
    	intent.setData(_selectedUri);
    	startActivity(intent);
    }
    
    /**
     * Handler to pass control to the image selection process
     * 
     * @param v The {@link View} which fired the event handler
     */
    public void onImageClick(View v){
    	new ImageHandler(this).showChooser();
    }
     
    /**
     * Handles the response from an ActivityResult fired in the context
     * of this Activity
     * 
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			//we're expecting the intent to have been an image type initiated by this app
			Uri uri = ImageHandler.getImageUriFromIntentResult(resultCode, requestCode, data);
			setImageView(uri);
		}			
	}
   
    /**
     * Populates the {@link ImageView} in this activity with the image
     * from the given {@link URI}
     * 
     * @param uri The {@link URI} to be displayed
     */
    private void setImageView(Uri uri){
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	Bitmap bm = ImageHandler.getBitmap(uri);    	
    	iv.setImageBitmap(bm);
    	//cache for sending to later activities/intents
    	_selectedUri = uri;
    }
}