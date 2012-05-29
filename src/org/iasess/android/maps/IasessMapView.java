package org.iasess.android.maps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;

import com.google.android.maps.MapView;

/**
 * Custom MapView class to allow custom implementation for touch events
 */
public class IasessMapView extends MapView {

	private GestureDetector _gestureDetector;
	
	public IasessMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
				
		_gestureDetector = new GestureDetector(context, (OnGestureListener)context);
		_gestureDetector.setOnDoubleTapListener((OnDoubleTapListener)context);		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(_gestureDetector.onTouchEvent(ev))
			return true;
		
		return super.onTouchEvent(ev);	
	}
}
