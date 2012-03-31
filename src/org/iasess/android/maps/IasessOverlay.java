package org.iasess.android.maps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/*
 * Custom overlay class to allow custom markers and positioning
 */
public class IasessOverlay extends ItemizedOverlay<OverlayItem> {

	/*
	 * The colection of overlay items (markers) controlled by this overlay
	 */
	private ArrayList<OverlayItem> _overlays = new ArrayList<OverlayItem>();
	
	/*
	 * The context this overlay operates within
	 */
	private Context _context;
	
	/*
	 * Constructor - sets the default marker
	 */
	public IasessOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	/*
	 * Constructor - Sets the default marker and context
	 */
	public IasessOverlay(Drawable defaultMarker, Context context) {
	  super(boundCenterBottom(defaultMarker));		  
	  _context = context;
	}
	
	/*
	 * Returns the item from the collection at the given index
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return _overlays.get(i);
	}

	/*
	 * Returns the size of this overlays item collection
	 */
	@Override
	public int size() {
		return _overlays.size();
	}
	
	/*
	 * Adds the given item to this overlays item collection
	 */
	public void add(OverlayItem overlay) {
	    _overlays.add(overlay);
	    populate();
	}
	
	/*
	 * Handles tab events for items in the collection
	 */
	@Override
	protected boolean onTap(int index) {
	  //OverlayItem item = overlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(_context);
	  dialog.setMessage("Would you like to use this point?");
	  //change to yes/no	  
	  dialog.show();
	  return true;
	}
}