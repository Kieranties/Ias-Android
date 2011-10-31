package com.kieranties.ias;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class LocationMarkerOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> _overlays = new ArrayList<OverlayItem>();
	private Context _ctx = null;
	private AlertDialog _selectDlg = null;
	private int _selectedIndex = -1;
	
	public LocationMarkerOverlay(Drawable defaultMarker, Context context) {
		super(boundCenter(defaultMarker));
		_ctx = context;
		initDlg();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return _overlays.get(i);
	}

	@Override
	public int size() {
		return _overlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		_selectedIndex = index;
		_selectDlg.show();
		return true;
	}

	public void addOverlay(OverlayItem overlay) {
		_overlays.add(overlay);
		populate();
	}
	
	public void setSingleMarker(OverlayItem overlay){
		_overlays.clear();
		addOverlay(overlay);
	}
	
	private void initDlg(){
		AlertDialog.Builder builder = new AlertDialog.Builder(_ctx);
		builder.setMessage("Would you like to use this location?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //return the bitmap of the map view
		        	   //after centering on the icon
		        	   if(_selectedIndex > -1){
		        		   _overlays.get(_selectedIndex).routableAddress();
		        		   
		        	   }
		                //MyActivity.this.finish();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		_selectDlg = builder.create();
	}
}
