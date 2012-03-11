package com.iasess.android.activities;

import org.iasess.android.ImageHandler;
import org.iasess.android.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Summary extends Activity {
    
	private Uri selectedUri = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        
        checkGpsStatus();
        setImageView();	    
    }
    
    private void checkGpsStatus(){
    	LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
    	boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

    	if (!enabled) {
    		Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    		startActivity(intent);
    	}
    }
    
    private void setImageView(){
    	selectedUri = getIntent().getData();
    	Bitmap bm = ImageHandler.getImageFromUri(selectedUri, this);
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	iv.setImageBitmap(bm);   	
    }
    
    private void processDetails(){
    	
    }
    
    public void onDoneClick(View v){
    	processDetails();
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
}

