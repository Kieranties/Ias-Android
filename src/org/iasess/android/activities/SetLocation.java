package org.iasess.android.activities;

import org.iasess.android.ImageHandler;
import org.iasess.android.R;
import org.iasess.android.SubmitParcel;
import org.iasess.android.maps.MapOverlay;

import android.os.Bundle;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class SetLocation extends MapActivity implements OnDoubleTapListener {

	private SubmitParcel _parcel;
	private MapView _mapView;
	private MapController _mapController;
	private MyLocationOverlay _locationOverlay;
	private GeoPoint _imgLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_location);

		_parcel = getIntent().getParcelableExtra(SubmitParcel.SUBMIT_PARCEL_EXTRA);

		// init mapview
		_mapView = (MapView) findViewById(R.id.mapView);
		_mapView.setSatellite(true);
				
		// init controller
		_mapController = _mapView.getController();
		_mapController.setZoom(18);
		
		// attempt to set location based on image data
		_imgLocation = getImageLocation();		
		if (_imgLocation != null) {
			setImageLocationOverlay();
		} else {
			findViewById(R.id.btnImageLocation).setEnabled(false);
		}

		//set my location tracker
		setMyLocationOverlay();

		// if no gps, set to last known location
	}

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onImageLocationClick(View v) {
		navigateToImageLocation();
	}

	public void onCurrentLocationClick(View v) {
		navigateToMyLocation();
	}

	private GeoPoint getImageLocation() {
		float[] locationData = ImageHandler.getImageLocation(_parcel
				.getImagePath());
		if (locationData == null)
			return null;
		return new GeoPoint((int) (locationData[0] * 1E6),
				(int) (locationData[1] * 1E6));
	}
	
	private void setImageLocationOverlay(){
		MapOverlay imageOverlay = MapOverlay.getImageOverlay(this);
		OverlayItem imageItem = new OverlayItem(_imgLocation,
				"Image location",
				"The image contains data for this locations");
		imageOverlay.addOverlay(imageItem);
		_mapView.getOverlays().add(imageOverlay);
		navigateToImageLocation();
	}
	
	private void navigateToImageLocation(){
		if(_imgLocation != null){			
			_mapController.animateTo(_imgLocation);
		}
	}
	
	private void setMyLocationOverlay(){
		// init the my location overlay
		_locationOverlay = new MyLocationOverlay(this, _mapView);
		_locationOverlay.enableMyLocation();
		if (_imgLocation == null) {
			_locationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					navigateToMyLocation();
				}
			});
		}
		_mapView.getOverlays().add(_locationOverlay);
	}
	
	private void navigateToMyLocation(){
		_mapController.animateTo(_locationOverlay.getMyLocation());
	}


	public boolean onDoubleTap(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean onDoubleTapEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean onSingleTapConfirmed(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
