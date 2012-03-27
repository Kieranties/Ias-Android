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

public class AddPhoto extends Activity {
    
	private Uri selectedUri = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        
        //check we have an image to process
        Uri selected = getIntent().getData();
        if(selected != null) setImageView(selected);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			Uri uri = ImageHandler.getImageUriFromIntentResult(resultCode, requestCode, data);
			setImageView(uri);
		}			
	}
    
    private void setImageView(Uri uri){
    	Bitmap bm = ImageHandler.getImageFromUri(uri, this);
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	iv.setImageBitmap(bm);
    	
    	selectedUri = uri;
    }
    
    public void onNextClick(View v){
    	//Intent intent = new Intent(this, Summary.class);
    	Intent intent = new Intent(this, SelectTaxa.class);

    	intent.setData(selectedUri);
    	startActivity(intent);
    }
    
    public void onImageClick(View v){
    	ImageHandler.getImage(this);
    }
}

