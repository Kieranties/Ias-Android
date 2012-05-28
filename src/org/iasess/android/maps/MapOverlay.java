package org.iasess.android.maps;

import java.util.ArrayList;

import org.iasess.android.IasessApp;
import org.iasess.android.R;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapOverlay extends ItemizedOverlay<OverlayItem> {

	private static Drawable _imageMarker = IasessApp.getContext().getResources().getDrawable(R.drawable.da_marker_attractions);
	private ArrayList<OverlayItem> _overlays = new ArrayList<OverlayItem>();
	private Context _context;
	
	public MapOverlay(Drawable marker, Context context) {
		super(boundCenterBottom(marker));
		
		_context = context;
	}
	
	public static MapOverlay getImageOverlay(Context context){
		return new MapOverlay(_imageMarker, context);
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
	  OverlayItem item = _overlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(_context);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	
	@Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow)
    {
        if(!shadow)
        {
            super.draw(canvas, mapView, false);
        }
    }
	
	public void addOverlay(OverlayItem item){
		_overlays.add(item);
		this.populate();
	}
}
