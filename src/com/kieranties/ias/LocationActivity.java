package com.kieranties.ias;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.kieranties.ias.views.IasMapView;

import android.os.Bundle;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class LocationActivity extends MapActivity implements OnGestureListener,
		OnDoubleTapListener {

	private IasMapView _view = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		_view = (IasMapView) findViewById(R.id.mapview);

		GeoPoint point = new GeoPoint(19240000, -99120000);
		_view.drawMarker(point);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		if (e.getPointerCount() == 1) {
			int x = (int) (e.getXPrecision() * e.getX());
			int y = (int) (e.getYPrecision() * e.getY());

			_view.drawMarker(x, y);
		}
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onDoubleTap(MotionEvent e) {
		return false;
	}

	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
