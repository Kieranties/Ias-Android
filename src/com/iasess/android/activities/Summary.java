package com.iasess.android.activities;

import org.iasess.android.ImageHandler;
import org.iasess.android.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class Summary extends MapActivity{
    
	private Uri selectedUri = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        
        initMapComponents();        
        setImageView();	    
    }
           
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
    
	private void initMapComponents(){
		MapView mapView = (MapView) findViewById(R.id.mapView);
		//mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		
		final MapController controller = mapView.getController();
		final MyLocationOverlay locationOverlay = new MyLocationOverlay(this, mapView);
		
		locationOverlay.enableCompass();
		locationOverlay.enableMyLocation();
		locationOverlay.runOnFirstFix(new Runnable(){
			public void run() {				
				controller.animateTo(locationOverlay.getMyLocation());
				controller.setZoom(18);
			}			
		});
		mapView.getOverlays().add(locationOverlay);
	}
	
    
    private void setImageView(){
    	selectedUri = getIntent().getData();
    	Bitmap bm = ImageHandler.getImageFromUri(selectedUri, this);
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	iv.setImageBitmap(bm);   	
    }
    
    private void processDetails(){
    	//TODO: construct packet to send
    	//include
    	//location
    	//taxa
    	//image
    	//user
    }
    
    public void onDoneClick(View v){
    	processDetails();
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
}

