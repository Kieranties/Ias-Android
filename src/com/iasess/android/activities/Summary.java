package com.iasess.android.activities;

import org.iasess.android.ImageHandler;
import org.iasess.android.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Summary extends Activity implements LocationListener {
    
	private Uri selectedUri = null;
	private LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	private String provider;
	private Location currentLocation;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        
        if(checkGpsStatus()){
        	//get location with default criteria
    		provider = locationManager.getBestProvider(new Criteria(), false);
    		currentLocation = locationManager.getLastKnownLocation(provider); //get immediate location
        }
        
        setImageView();	    
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		//request an update to location
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}
    
    @Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this); //stop listening
	}
       
    
    private boolean checkGpsStatus(){
    	boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    	if (!enabled) {
    		//TODO: display dialog informing the user we need gps
    		Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    		startActivity(intent);
    	}
    	return enabled;
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

    /** Location Listener implementation **/
    public void onLocationChanged(Location location) {
    	//TODO: update map view 
    	currentLocation = location;
	}
    
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}

