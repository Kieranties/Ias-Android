package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.ImageHandler;
import org.iasess.android.R;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.maps.IasessOverlay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class Summary extends MapActivity{
    
	private Uri selectedImage;
	private int selectedTaxa;
	private MapController mapController;	
	private MyLocationOverlay locationOverlay;
	private LocationManager locationManager;
	Drawable marker = this.getResources().getDrawable(R.drawable.marker);
	final IasessOverlay iOverlay = new IasessOverlay(marker, this);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        
        initMapComponents();
        setTaxa();
        setImageView();	    
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    		locationOverlay.enableMyLocation();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	locationOverlay.disableMyLocation();
    }
           
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
    
	private void initMapComponents(){
		//check gps is enabled
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
			Intent gpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(gpsIntent);
		}					
		
		//prepare the map componentsO
		MapView mapView = (MapView) findViewById(R.id.mapView);
		mapView.setSatellite(true);		
		mapController = mapView.getController();
				
		//prepare the default overlay to fetch current location
		locationOverlay.enableMyLocation();
		locationOverlay = new MyLocationOverlay(this, mapView);
		locationOverlay.runOnFirstFix(new Runnable(){
			public void run() {				
				GeoPoint loc = locationOverlay.getMyLocation();
				//add additional marker to custom overlay
				iOverlay.add(new OverlayItem(loc, "Current", "Current location"));
				mapController.animateTo(loc);
				mapController.setZoom(18);
			}			
		});
		mapView.getOverlays().add(locationOverlay);
		mapView.getOverlays().add(iOverlay);
	}
	
	private void setTaxa(){
		String taxaData = getIntent().getExtras().getString(IasessApp.SELECTED_TAXA);
		String[] vals = taxaData.split("\\|");
		
		TextView tv = (TextView)findViewById(R.id.textSelectedTaxa);
		tv.setText(vals[1]);
		selectedTaxa = Integer.parseInt(vals[0]);
	}
    
    private void setImageView(){
    	selectedImage = getIntent().getData();
    	Bitmap bm = ImageHandler.getBitmap(selectedImage, this);
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	iv.setImageBitmap(bm);   	
    }
    
    private void processDetails(){
    	String imgPath = ImageHandler.getPath(selectedImage, this);
    	GeoPoint loc = iOverlay.getItem(0).getPoint();
    	ApiHandler.submitSighting(imgPath, selectedTaxa, loc.getLatitudeE6()/1E6, loc.getLongitudeE6()/1E6, IasessApp.getPreferenceString(IasessApp.PREFS_USERNAME));
   }
    
    public void onDoneClick(View v){
    	processDetails();
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
}

