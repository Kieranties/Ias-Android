package org.iasess.android.activities;

import org.iasess.android.R;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class SetLocation extends MapActivity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_location);
        
        //attempt to set location based on image data
        
        //else if no gps ask to switch it on
        
        //if no gps, set to last known location
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
