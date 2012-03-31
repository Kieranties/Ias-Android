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

/*
 * Activity to handle the Summary screen
 */
public class Summary extends MapActivity{
    
	/*
	 * The selected image URI
	 */
	private Uri _selectedImage;
	
	/*
	 * The selected Taxa details [i|name]
	 */
	private int _selectedTaxa;
	
	/*
	 * The map controller for the position of the sighting
	 */
	private MapController _mapController;
	
	/*
	 * The location overlay for the users current position
	 */
	private MyLocationOverlay _locationOverlay;
	
	/*
	 * The location manager to communicate with the GPS device
	 */
	private LocationManager _locationManager;
	
	/*
	 * The marker displayed for the selected sighting location
	 */
	private Drawable _marker = this.getResources().getDrawable(R.drawable.marker);
	
	/*
	 * The overlay used for custom markers
	 */
	final IasessOverlay _customOverlay = new IasessOverlay(_marker, this);
	
	/*
	 * Initializer
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        
        initMapComponents();
        setTaxa();
        setImageView();	    
    }
    
    /*
     * Handles the Done click event to submit details
     */
    public void onDoneClick(View v){
    	submitDetails();
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
    
    /*
     * Re-instates the location functionality
     */
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if(_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    		_locationOverlay.enableMyLocation();
    }
    
    /*
     * Pause the location functionality
     */
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	_locationOverlay.disableMyLocation();
    }
         
    /*
     * Required override
     */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
    
	/*
	 * Initializes the map components and checks for GPS permissions
	 */
	private void initMapComponents(){
		//check gps is enabled
		_locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		if (!_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
			Intent gpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(gpsIntent);
		}					
		
		//prepare the map componentsO
		MapView mapView = (MapView) findViewById(R.id.mapView);
		mapView.setSatellite(true);		
		_mapController = mapView.getController();
				
		//prepare the default overlay to fetch current location
		_locationOverlay.enableMyLocation();
		_locationOverlay = new MyLocationOverlay(this, mapView);
		_locationOverlay.runOnFirstFix(new Runnable(){
			public void run() {				
				GeoPoint loc = _locationOverlay.getMyLocation();
				//add additional marker to custom overlay
				_customOverlay.add(new OverlayItem(loc, "Current", "Current location"));
				_mapController.animateTo(loc);
				_mapController.setZoom(18);
			}			
		});
		
		//add overlays to map view
		mapView.getOverlays().add(_locationOverlay);
		mapView.getOverlays().add(_customOverlay);
	}
	
	/*
	 * Populates the taxa details in the view
	 */
	private void setTaxa(){
		String taxaData = getIntent().getExtras().getString(IasessApp.SELECTED_TAXA);
		String[] vals = taxaData.split("\\|");
		
		TextView tv = (TextView)findViewById(R.id.textSelectedTaxa);
		tv.setText(vals[1]);
		_selectedTaxa = Integer.parseInt(vals[0]);
	}
    
	/*
	 * Populates the image details in the view
	 */
    private void setImageView(){
    	_selectedImage = getIntent().getData();
    	Bitmap bm = ImageHandler.getBitmap(_selectedImage, this);
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	iv.setImageBitmap(bm);   	
    }
    
    /*
     * Submits the selected details to the site
     */
    private void submitDetails(){
    	String imgPath = ImageHandler.getPath(_selectedImage, this);
    	GeoPoint loc = _customOverlay.getItem(0).getPoint();
    	ApiHandler.submitSighting(imgPath, _selectedTaxa, loc.getLatitudeE6()/1E6, loc.getLongitudeE6()/1E6, IasessApp.getPreferenceString(IasessApp.PREFS_USERNAME));
    }
}