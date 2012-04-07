package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.ImageHandler;
import org.iasess.android.R;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.api.SubmissionResponse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/*
 * Activity to handle the Summary screen
 */
public class Summary extends MapActivity{
    /*
     * Request code for GPS intent
     */
	private static final int GPS_INTENT = 948484;
	
	/*
	 * The selected image URI
	 */
	private Uri _selectedImage;
	
	/*
	 * The selected Taxa details [i|name]
	 */
	private long _selectedTaxa;
	
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
	 * The MapView which renders the location details
	 */
	private MapView _mapView;

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
    	new SubmitSightingTask().execute("");
    }
    
    /*
     * Re-instates the location functionality
     */
    @Override
    protected void onResume() {
    	super.onResume();   	
    	renderMapView();
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
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && requestCode == GPS_INTENT){
			renderMapView();
		}		
	}
	
	/*
	 * Initializes the map components and checks for GPS permissions
	 */
	private void initMapComponents(){
		//init map related properties for pause/resume events
		_mapView = (MapView) findViewById(R.id.mapView);
		_mapView.setSatellite(true);		
		_mapController = _mapView.getController();
		_locationOverlay = new MyLocationOverlay(this, _mapView);		
		
		//check gps is enabled
		_locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		if (!_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			IasessApp.makeToast(this, "Please enable GPS");
			Intent gpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(gpsIntent, GPS_INTENT);
		} else{
			renderMapView();
		}		
	}
	
	/*
	 * Renders the map view components when GPS is enabled
	 */
	private void renderMapView(){		
		if(!_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return;
		
		//inform user we are waiting for a fix
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Finding location...", true);
		
		//prepare the default overlay to fetch current location	
		_locationOverlay.enableMyLocation();
		_locationOverlay.runOnFirstFix(new Runnable(){
			public void run() {				
				//get the location and set properties
				GeoPoint loc = _locationOverlay.getMyLocation();
				_mapController.animateTo(loc);
				_mapController.setZoom(18);
				
				//we're done fetching initial fix
				dialog.cancel();
			}			
		});
		
		//add overlays to map view
		_mapView.getOverlays().add(_locationOverlay);
	}
	
	/*
	 * Populates the taxa details in the view
	 */
	private void setTaxa(){
		Bundle extras = getIntent().getExtras();
		_selectedTaxa = extras.getLong(IasessApp.SELECTED_TAXA);
		String taxaName = extras.getString(IasessApp.SELECTED_TAXA_NAME);
		
		TextView tv = (TextView)findViewById(R.id.textSelectedTaxa);
		tv.setText(taxaName);
	}
    
	/*
	 * Populates the image details in the view
	 */
    private void setImageView(){
    	_selectedImage = getIntent().getData();
    	Bitmap bm = ImageHandler.getBitmap(_selectedImage);
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	iv.setImageBitmap(bm);   
    }
    
    /*
     * Separate thread action to submit contents
     */
    private class SubmitSightingTask extends AsyncTask<String, Void, SubmissionResponse> {	
		/*
		 * The progress dialog to display to the user
		 */
		private ProgressDialog _dlg;
		
		/*
		 * Executed before any processing of the task itself
		 */
		protected void onPreExecute() {
			//display the dialog to the user
			_dlg = ProgressDialog.show(Summary.this, "", "Submitting...", true,true, new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					SubmitSightingTask.this.cancel(true);	
					finish();
				}
			});
	    }
        
		/*
		 * The actual execution method ran in a background thread 
		 */
	    protected SubmissionResponse doInBackground(String... params) {
	    	//don't need params
	    	String imgPath = ImageHandler.getPath(_selectedImage);    
	    	Location fix = _locationOverlay.getLastFix(); 
        	return ApiHandler.submitSighting(imgPath, _selectedTaxa, fix.getLatitude(), fix.getLongitude(), IasessApp.getPreferenceString(IasessApp.PREFS_USERNAME));
	    }
	    
	    /*
	     * Fired when all processing has finished
	     */
	    protected void onPostExecute(SubmissionResponse result) { 
	    	_dlg.dismiss();
	    	if(result.getId() != Integer.MIN_VALUE){
	    		IasessApp.makeToast("Submitted!");    		
	    		
	    		//we're done for this submission so return the app to the start
	    		Intent home = new Intent(Summary.this, Home.class);
	            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //resets the activity stack
	            startActivity(home);
	            
	            //display the success page to the user
	            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getUrl()));
	    		startActivity(Intent.createChooser(browse,"Select Browser"));
	    	}  
	    	else
	    	{
	    		IasessApp.makeToast("Sorry, please try again later :-(");
	    	}
	    }
	}
}