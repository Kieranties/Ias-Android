package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.data.TaxaStore;

import android.app.Activity;
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
public class TaxaDetails extends Activity {
	
	/**
	 * Initialises the content of this Activity
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taxa_details);
		
		long taxaPk = getIntent().getLongExtra(IasessApp.SELECTED_TAXA, -1);
		if(taxaPk != -1){
			TaxaStore store = new TaxaStore(this);
			Cursor cursor = store.getByPk(taxaPk);
			
			cursor.moveToFirst();
			String imgUrl = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_LARGE_IMAGE));
			String description = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_KEY_TEXT));	
			String name = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_COMMON_NAME));
			cursor.close();
			
			TextView tv = (TextView)findViewById(R.id.textDescription);
			tv.setText(description);
			
			TextView tvTitle = (TextView)findViewById(R.id.textBanner);
			tvTitle.setText(name);
			
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
