package org.iasess.android.activities;

import java.util.ArrayList;

import org.iasess.android.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Controls the 'About' Activity view
 */
public class IasGallery extends InvadrActivityBase {
	
	private ArrayList<String> images;
	private Gallery gallery;
	/**
	 * Initialises the content of the Activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		
		Bundle extras = getIntent().getExtras();
		images = extras.getStringArrayList("images");
		
		gallery = (Gallery) findViewById(R.id.galleryView);
		gallery.setAdapter(new ImageAdapter(this));
	}
	
	public class ImageAdapter extends BaseAdapter {
		   int mGalleryItemBackground;
		   private Context mContext;

		   public ImageAdapter(Context c) {
		       mContext = c;
		   }

		   public int getCount() {
		       return images.size();
		   }

		   public Object getItem(int position) {
		       return position;
		   }

		   public long getItemId(int position) {
		       return position;
		   }

		   public View getView(int position,
		       View convertView, ViewGroup parent) {
		       ImageView i = new ImageView(mContext);
		       
		       ImageLoader.getInstance().displayImage(images.get(position),i);

		       return i;
		   }
		}
}