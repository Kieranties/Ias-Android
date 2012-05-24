package org.iasess.android.activities;

import java.util.List;

import org.iasess.android.ImageHandler;
import org.iasess.android.R;
import org.iasess.android.SubmitParcel;
import org.iasess.android.maps.MapOverlay;

import android.os.Bundle;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SetLocation extends MapActivity {

	private SubmitParcel _parcel;
	private MapView _mapView;
	private MapController _mapController;
	private MyLocationOverlay _locationOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_location);

		_parcel = getIntent().getParcelableExtra(
				SubmitParcel.SUBMIT_PARCEL_EXTRA);

		// init mapview
		_mapView = (MapView) findViewById(R.id.mapView);
		_mapView.setBuiltInZoomControls(true);
		_mapView.setSatellite(true);
		// init controller
		_mapController = _mapView.getController();
		_mapController.setZoom(18);
		// add overlay to map view
		List<Overlay> overlays = _mapView.getOverlays();

		// attempt to set location based on image data
		GeoPoint imagePoint = getImageLocation();
		if (imagePoint != null) {
			MapOverlay imageOverlay = MapOverlay.getImageOverlay();
			OverlayItem imageItem = new OverlayItem(imagePoint,
					"Image location",
					"The image contains data for this locations");
			imageOverlay.addOverlay(imageItem);
			overlays.add(imageOverlay);
			_mapController.animateTo(imagePoint);
		} else {
			findViewById(R.id.btnImageLocation).setEnabled(false);
		}

		// init the my location overlay
		_locationOverlay = new MyLocationOverlay(this, _mapView);
		_locationOverlay.enableMyLocation();
		if (imagePoint == null) {
			_locationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					// set location and navigate to it
					GeoPoint loc = _locationOverlay.getMyLocation();
					_mapController.animateTo(loc);

				}
			});
		}
		overlays.add(_locationOverlay);

		// if no gps, set to last known location
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onImageLocationClick(View v) {
	}

	public void onCurrentLocationClick(View v) {
	}

	private GeoPoint getImageLocation() {
		float[] locationData = ImageHandler.getImageLocation(_parcel
				.getImagePath());
		if (locationData == null)
			return null;
		return new GeoPoint((int) (locationData[0] * 1E6),
				(int) (locationData[1] * 1E6));
	}
}
