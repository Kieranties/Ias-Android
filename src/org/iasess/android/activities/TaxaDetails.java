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
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TaxaDetails extends Activity {

	private Cursor _cursor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taxa_details);
		
		long taxaPk = getIntent().getLongExtra(IasessApp.SELECTED_TAXA, -1);
		if(taxaPk != -1){
			TaxaStore store = new TaxaStore(this);
			_cursor = store.getByPk(taxaPk);
			startManagingCursor(_cursor);
			
			_cursor.moveToFirst();
			String imgUrl = _cursor.getString(_cursor.getColumnIndex(TaxaStore.COL_LARGE_IMAGE));
			String description = _cursor.getString(_cursor.getColumnIndex(TaxaStore.COL_KEY_TEXT));	
			String name = _cursor.getString(_cursor.getColumnIndex(TaxaStore.COL_COMMON_NAME));
			_cursor.close();
			
			TextView tv = (TextView)findViewById(R.id.textDescription);
			tv.setText(Html.fromHtml(description));
			
			TextView tvTitle = (TextView)findViewById(R.id.textBanner);
			tvTitle.setText(name);
			
			if(imgUrl == null || imgUrl == ""){
				ImageView imgView = (ImageView) findViewById(R.id.imageView);
				imgView.setVisibility(View.GONE);
			} else {
				new PopulateDetails().execute(imgUrl);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		_cursor.close();
	}
	
	/*
	 * Separate thread action to fetch list details
	 */
	private class PopulateDetails extends AsyncTask<String, Void, byte[]> {
		/*
		 * The progress dialog to display to the user
		 */
		private ProgressDialog _dlg;

		/*
		 * Executed before any processing of the task itself
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

		/*
		 * The actual execution method ran in a background thread
		 */
		protected byte[] doInBackground(String... params) {
			return ApiHandler.getByteArray(params[0], false);
		}

		/*
		 * Fired when all processing has finished
		 */
		protected void onPostExecute(byte[] result) {
			ImageView imgView = (ImageView) findViewById(R.id.imageView);
			if(result == null){
				IasessApp.makeToast("No image found...");
				imgView.setVisibility(View.GONE);
			} else {
				Bitmap bm = BitmapFactory.decodeByteArray(result, 0, result.length);
				imgView.setImageBitmap(bm);
			}
			_dlg.dismiss();
		}
	}
}
