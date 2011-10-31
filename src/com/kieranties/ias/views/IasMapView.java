package com.kieranties.ias.views;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import com.kieranties.ias.LocationMarkerOverlay;
import com.kieranties.ias.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class IasMapView extends com.google.android.maps.MapView {

	private Drawable _markerIcon = null;
	private Context _ctx = null;
	private GestureDetector _gd = null;
	private MyLocationOverlay _myLoc = null;
	private LocationMarkerOverlay _markers = null;

	public IasMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBuiltInZoomControls(true);
		setSatellite(true);
		_markerIcon = this.getResources().getDrawable(R.drawable.marker);
		_ctx = context;
		_gd = new GestureDetector((OnGestureListener) _ctx);
		_gd.setOnDoubleTapListener((OnDoubleTapListener) _ctx);
		
		final MapController controller = this.getController();
		
		_myLoc = new MyLocationOverlay(_ctx, this); 
		_markers = new LocationMarkerOverlay(_markerIcon, _ctx);
		_myLoc.enableCompass();
		_myLoc.enableMyLocation();		
		_myLoc.runOnFirstFix(new Runnable(){
			public void run() {				
				controller.animateTo(_myLoc.getMyLocation());
				controller.setZoom(18);
			}			
		});
		getOverlays().add(_myLoc);
		getOverlays().add(_markers);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (_gd.onTouchEvent(ev))
			return true;
		else
			return super.onTouchEvent(ev);
	}

	
	public void drawMarker(int x, int y) {
		drawMarker(translateCoordsToGeo(x, y));
	}

	public void drawMarker(GeoPoint point) {
		OverlayItem markerItem = new OverlayItem(point, null, null);
		_markers.setSingleMarker(markerItem);		
	}

	public GeoPoint translateCoordsToGeo(int x, int y) {
		return getProjection().fromPixels(x, y);
	}
}
