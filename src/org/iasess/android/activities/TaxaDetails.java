package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.TaxonParcel;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.data.TaxaStore;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Controls the 'TaxaDetails' Activity view
 */
public class TaxaDetails extends InvadrActivityBase {
	
	/**
	 * Initialises the content of this Activity
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taxa_details);
		
		TaxonParcel parcel = getIntent().getParcelableExtra(TaxonParcel.TAXON_PARCEL_EXTRA);
		long taxonId = parcel.getTaxonId();
		if(taxonId != -1){
			TaxaStore store = new TaxaStore(this);
			Cursor cursor = store.getByPk(taxonId);
			
			cursor.moveToFirst();
			String imgUrl = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_LARGE_IMAGE));
			String description = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_KEY_TEXT));	
			String name = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_COMMON_NAME));
			String scientific = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_SCIENTIFIC_NAME));
			String sightings = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_SIGHTINGS_COUNT));
			String rank = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_RANK));
			cursor.close();
			store.close();
			
			TextView tvDesc = (TextView)findViewById(R.id.textDescription);
			tvDesc.setText(description);
			
			TextView tvTitle = (TextView)findViewById(R.id.textBanner);
			tvTitle.setText(name);
			
			TextView tvScientific = (TextView)findViewById(R.id.scientific_name);
			tvScientific.setText(scientific);
			
			TextView tvSightings = (TextView)findViewById(R.id.sightings_count);
			String sightingsStub = getResources().getString(R.string.sightings);
			tvSightings.setText(sightingsStub + " " + sightings);
			
			TextView tvRank = (TextView)findViewById(R.id.rank);
			String rankStub = getResources().getString(R.string.rank);
			tvRank.setText(rankStub + " " + rank);
			
			if(imgUrl != null && imgUrl != ""){
				new PopulateDetails().execute(imgUrl);
			}
		}
	}
	
	
	
	/**
	 * Class to handle the population of taxa details in a separate thread
	 */
	private class PopulateDetails extends AsyncTask<String, Void, byte[]> {
		
		/**
		 * The progress dialog to display while processing
		 */
		private ProgressDialog _dlg;

		/**
		 * Display the progress dialog to the user before processing the AsyncTask
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		protected void onPreExecute() {
			// display the dialog to the user
			_dlg = ProgressDialog.show(TaxaDetails.this, "", "Fetching image...", true,true, new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					PopulateDetails.this.cancel(true);	
					finish();
				}
			});
		}
		
		/**
		 * Fetches an image from the API
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected byte[] doInBackground(String... params) {
			return ApiHandler.getByteArray(params[0], false);
		}

		/**
		 * Process the results of the AsyncTask
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(byte[] result) {
			ImageView imgView = (ImageView) findViewById(R.id.imageView);
			if(result == null){
				IasessApp.makeToast("No image found...");
			} else {
				Bitmap bm = BitmapFactory.decodeByteArray(result, 0, result.length);
				imgView.setImageBitmap(bm);
				imgView.setVisibility(View.VISIBLE);
			}
			_dlg.dismiss();
		}
	}
}
