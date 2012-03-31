package org.iasess.android.adapters;

import java.util.ArrayList;

import org.iasess.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
 * Adapter class to populate a ListView with TaxaItems
 */
public class TaxaItemAdapter extends ArrayAdapter<TaxaItem> {

	/*
	 * Constructor
	 */
	public TaxaItemAdapter(Context context, int textViewResourceId, ArrayList<TaxaItem> items) {
		super(context, textViewResourceId, items);
	}

	/*
	 * Inflates each item in the view with details from TaxaItem at the given position
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		//use the default TaxaItem list view if not defined
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.image_list_item, null);
		}

		TaxaItem item = this.getItem(position);
		if (item != null) {
			TextView primaryTitle = (TextView) v.findViewById(R.id.textPrimary);
			TextView secondaryTitle = (TextView) v.findViewById(R.id.textSecondary);
			//ImageView imageSpot = (ImageView) v.findViewById(R.id.icon);

			primaryTitle.setText(item.getCommonName());
			secondaryTitle.setText(item.getScientificName());
			//imageSpot.setImageURI(item.getImageUri());
			
			//set tag to item identifier and common name for later reference
			v.setTag(item.getPk() + "|" + item.getCommonName());
		}
		return v;
	}
}