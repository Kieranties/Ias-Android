package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.ImageHandler;
import org.iasess.android.Logger;
import org.iasess.android.R;
import org.iasess.android.api.ApiHandler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class Summary extends MapActivity{
    
	private Uri selectedImage;
	private int selectedTaxa;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        
        initMapComponents();
        setTaxa();
        setImageView();	    
    }
           
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
    
	private void initMapComponents(){
		MapView mapView = (MapView) findViewById(R.id.mapView);
		//mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		
		final MapController controller = mapView.getController();
		final MyLocationOverlay locationOverlay = new MyLocationOverlay(this, mapView);
		
		locationOverlay.enableCompass();
		locationOverlay.enableMyLocation();
		locationOverlay.runOnFirstFix(new Runnable(){
			public void run() {				
				controller.animateTo(locationOverlay.getMyLocation());
				controller.setZoom(18);
			}			
		});
		mapView.getOverlays().add(locationOverlay);
	}
	
	private void setTaxa(){
		String taxaData = getIntent().getExtras().getString(IasessApp.SELECTED_TAXA);
		String[] vals = taxaData.split("\\|");
		
		TextView tv = (TextView)findViewById(R.id.textSelectedTaxa);
		tv.setText(vals[1]);
		selectedTaxa = Integer.parseInt(vals[0]);
	}
    
    private void setImageView(){
    	selectedImage = getIntent().getData();
    	Bitmap bm = ImageHandler.getImageFromUri(selectedImage, this);
    	ImageView iv = (ImageView)findViewById(R.id.imageView);
    	iv.setImageBitmap(bm);   	
    }
    
    private void processDetails(){
    	String imgPath = ImageHandler.getPath(selectedImage, this);
    	ApiHandler.submitSighting(imgPath, 100000, 1, 2, "Kieranties");
    	
//    	String img = ImageHandler.getEncodedImage(selectedImage);
//    	String dataPack = "{photo: '" + img + "', taxon:100000, email: 'Kieranties', location: 'POINT(-2.543989419937134 51.46212918583009)'}";
//    	Logger.debug(this, img);
    }
    
    public void onDoneClick(View v){
    	processDetails();
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
}

