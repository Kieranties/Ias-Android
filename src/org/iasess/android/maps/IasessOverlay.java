package org.iasess.android.maps;

import java.util.ArrayList;

import org.iasess.android.IasessApp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class IasessOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private Context context;
	
	public IasessOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public IasessOverlay(Drawable defaultMarker, Context context) {
	  super(boundCenterBottom(defaultMarker));		  
	  this.context = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}
	
	public void add(OverlayItem overlay) {
	    overlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = overlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	  dialog.setMessage("Would you like to use this point?");
	  //change to yes/no	  
	  dialog.show();
	  return true;
	}

}
