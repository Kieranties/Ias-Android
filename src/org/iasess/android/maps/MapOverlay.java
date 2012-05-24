package org.iasess.android.maps;

import java.util.ArrayList;

import org.iasess.android.IasessApp;
import org.iasess.android.R;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapOverlay extends ItemizedOverlay<OverlayItem> {

	private static Drawable _imageMarker = IasessApp.getContext().getResources().getDrawable(R.drawable.marker);
	private static Drawable _customeMarker = null;
	private ArrayList<OverlayItem> _overlays = new ArrayList<OverlayItem>();
	
	public MapOverlay(Drawable marker) {
		super(marker);
		// TODO Auto-generated constructor stub
	}
	
	public static MapOverlay getImageOverlay(){
		return new MapOverlay(_imageMarker);
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return _overlays.get(i);
	}
	
	@Override
	public int size() {
		return _overlays.size();
	}

	public void addOverlay(OverlayItem item){
		_overlays.add(item);
		this.populate();
	}
}
