package org.iasess.android.activities;

import java.net.URI;

import org.iasess.android.IasessApp;
import org.iasess.android.ImageHandler;
import org.iasess.android.R;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.api.SubmissionResponse;
import org.iasess.android.api.TaxaItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * Controls the 'Summary' Activity view
 */
public class Summary extends Activity{
    
	/**
	 * Request Code for the GPS intent
	 */
	private static final int GPS_INTENT = 948484;
	
	/**
	 * The {@link URI} for the user selected image
	 */
	private Uri _selectedImage;
	
	/**
	 * The primary key identifier for the user selected {@link TaxaItem}
	 */
	private long _selectedTaxa;
	
	/**
	 * The {@link MapController} used to manage the users location
	 */
	private MapController _mapController;
	
	/**
	 * The {@link MyLocationOverlay} used to manage the users location
	 */
	private MyLocationOverlay _locationOverlay;
	
	/**
	 * The {@link LocationManager} used to manage the users location 
	 */
	private LocationManager _locationManager;
	
	/**
	 * The {@link MapView} used to manage the users location 
	 */
	private MapView _mapView;

    /**
     * Initialises the content of the Activity
     * 
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        
        initMapComponents();
        setTaxa();
        setImageView();	    
    }
       
    /**
     * Executes an {@link AsyncTask} to submit a sighting
     * 
     * @param v The {@link View} which fired the event handler
     */
    public void onDoneClick(View v){
    	new SubmitSightingTask().execute("");
    }
    
    /**
     * Reinstates the mapping functionality when the view
     * is bought back into focus
     * 
     * @see com.google.android.maps.MapActivity#onResume()
     */
    @Override
    protected void onResume() {
    	super.onResume();   	
    	renderMapView();
    }
    
    /**
     * Pauses the mapping functionality when the view loses
     * focus
     * 
     * @see com.google.android.maps.MapActivity#onPause()
     */
    @Override
    protected void onPause() {
    	super.onPause();
    	_locationOverlay.disableMyLocation();
    }
    
	/**
	 * Handlesthe response of an ActivityResult fired in the context
	 * of this Activity
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && requestCode == GPS_INTENT){
			renderMapView();
		}		
	}
	
	/**
	 * Initialises the map components of this Activity
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
	
	/**
	 * Performs the rendering functions for the {@link MapView} contained
	 * in this Activity
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
	
	/**
	 * Sets the details for the selected Taxa on the page
	 */
	private void setTaxa(){
		Bundle extras = getIntent().getExtras();
		_selectedTaxa = extras.getLong(IasessApp.SELECTED_TAXA);
		String taxaName = extras.getString(IasessApp.SELECTED_TAXA_NAME);
		
		TextView tv = (TextView)findViewById(R.id.textSelectedTaxa);
		tv.setText(taxaName);
	}
    
    /**
     * Sets the details for the selected image on the page
     */
    private void setImageView(){
    	_selectedImage = getIntent().getData();
    	Bitmap bm = ImageHandler.getBitmap(_selectedImage);
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	iv.setImageBitmap(bm);   
    }
    
    /**
     * Class to handle the submission of details in a separate thread
     *
     */
    private class SubmitSightingTask extends AsyncTask<String, Void, SubmissionResponse> {	
		
		/**
		 * The progress dialog to display while processing
		 */
		private ProgressDialog _dlg;
				
		/**
		 * Displays a progress dialog prior to the start of any processing
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
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
        		
	    /**
	     * Submits the details of a sighting through the API
	     * 
	     * @see android.os.AsyncTask#doInBackground(Params[])
	     */
	    protected SubmissionResponse doInBackground(String... params) {
	    	//don't need params
	    	String imgPath = ImageHandler.getPath(_selectedImage);    
	    	Location fix = _locationOverlay.getLastFix(); 
        	return ApiHandler.submitSighting(imgPath, _selectedTaxa, fix.getLatitude(), fix.getLongitude(), IasessApp.getPreferenceString(IasessApp.PREFS_USERNAME));
	    }
	    	    
	    /**
	     * Processes the result of the submission
	     * 
	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
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
	            //add in device query string param
	            Uri uri = Uri.parse(result.getUrl());
	            Builder uriBuilder = uri.buildUpon().appendQueryParameter("device", "android");
	            Intent browse = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
	    		startActivity(Intent.createChooser(browse,"Select Browser"));
	    	}  
	    	else
	    	{
	    		IasessApp.makeToast("Sorry, please try again later :-(");
	    	}
	    }
	}
}