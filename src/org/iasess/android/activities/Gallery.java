package org.iasess.android.activities;

import org.iasess.android.R;
import android.app.Activity;
import android.os.Bundle;

/*
 * Activity to handle the gallery screen
 */
public class Gallery extends Activity {
	
    /*
     * Initializer
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
    }
}