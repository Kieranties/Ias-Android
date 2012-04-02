package org.iasess.android.activities;

import org.iasess.android.ImageHandler;
import org.iasess.android.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/*
 * Activity to handle the AddPhoto screen
 */
public class AddPhoto extends Activity {
    
	/*
	 * The image URI selected by the user
	 */
	private Uri _selectedUri = null;
	
    
    /*
     * Initializer
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        
        //check we have an image to process
        Uri selected = getIntent().getData();
        if(selected != null) setImageView(selected);
    }
    
    /*
     * Handles the passing of data from this activity to the next
     */
    public void onNextClick(View v){
    	Intent intent = new Intent(this, SelectTaxa.class);
    	intent.setData(_selectedUri);
    	startActivity(intent);
    }
    
    /*
     * Handles image selection for this activity
     */
    public void onImageClick(View v){
    	ImageHandler.getImage(this);
    }
    
    @Override
    /*
     * Handle the result of an intent
     */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			//we're expecting the intent to have been an image type initiated by this app
			Uri uri = ImageHandler.getImageUriFromIntentResult(resultCode, requestCode, data);
			setImageView(uri);
		}			
	}
   
    /*
     * Sets the image view for this activity based on the given URI
     */
    private void setImageView(Uri uri){
    	Bitmap bm = ImageHandler.getBitmap(uri);
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	iv.setImageBitmap(bm);
    	
    	//cache for sending to later activities/intents
    	_selectedUri = uri;
    }
}