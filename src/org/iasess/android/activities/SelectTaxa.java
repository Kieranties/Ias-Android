package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.data.TaxaStore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

/*
 * Ativity to handle the Taxa selection screen
 */
public class SelectTaxa extends Activity {

	/*
	 * Initializer
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_taxa);
		new PopulateList().execute(""); // <- TODO: ugly!
	}

	/*
	 * Hook in refresh options
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.refresh_list, menu);
		return true;
	}

	/*
	 * Handle menu selection
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			new PopulateList().execute("refresh");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * Handles the passing of selected data to next activity
	 */
	public void onNextClick(View v) {
		Intent intent = new Intent(this, Summary.class);
		// set the selected image
		Uri selected = getIntent().getData();
		intent.setData(selected);

		// set the selected taxa
		intent.putExtra(IasessApp.SELECTED_TAXA, (String) v.getTag());

		startActivity(intent);
	}

	/*
	 * Separate thread action to fetch list details
	 */
	private class PopulateList extends AsyncTask<String, Void, Cursor> {
		/*
		 * The progress dialog to display to the user
		 */
		private ProgressDialog _dlg;

		/*
		 * Executed before any processing of the task itself
		 */
		protected void onPreExecute() {
			// display the dialog to the user
			_dlg = ProgressDialog.show(SelectTaxa.this, "", "Fetching taxa...",
					true);
		}

		/*
		 * The actual execution method ran in a background thread
		 */
		protected Cursor doInBackground(String... params) {
			TaxaStore store = new TaxaStore(SelectTaxa.this);
			if (params.length > 0 && params[0].equals("refresh")) {
				store.updateTaxa(ApiHandler.getTaxa());
				return store.getAllItems();
			} else {
				Cursor taxaCursor = store.getAllItems();
				if (!taxaCursor.moveToFirst()) { // is an empty set
					taxaCursor.close();
					// get from the api as not initialised
					store.addTaxa(ApiHandler.getTaxa());

					// re-fetch cursor data
					taxaCursor = store.getAllItems();
				}

				return taxaCursor;
			}
		}

		/*
		 * Fired when all processing has finished
		 */
		protected void onPostExecute(Cursor result) {
			// populate list
			ListView listView = (ListView) findViewById(R.id.listTaxa);
			if (listView.getAdapter() != null) {
				SimpleCursorAdapter adapter = (SimpleCursorAdapter) listView.getAdapter();
				adapter.changeCursor(result);
			} else {

				final int pkIndex = result.getColumnIndex(TaxaStore.COL_PK);
				final int commonIndex = result.getColumnIndex(TaxaStore.COL_COMMON_NAME);
				String[] columns = new String[] { TaxaStore.COL_COMMON_NAME, TaxaStore.COL_SCIENTIFIC_NAME,	TaxaStore.COL_LISTING_IMAGE, TaxaStore.COL_PK };
				int[] to = new int[] { R.id.textPrimary, R.id.textSecondary, R.id.icon };

				SimpleCursorAdapter adapter = new SimpleCursorAdapter(
						SelectTaxa.this, R.layout.image_list_item, result,
						columns, to);
				adapter.setViewBinder(new ViewBinder() {
					public boolean setViewValue(View view, Cursor cursor,
							int columnIndex) {
						if (view.getId() == R.id.icon) {
							ImageView imageSpot = (ImageView) view;
							Bitmap bm = null;
							byte[] bytes = cursor.getBlob(columnIndex);
							if (bytes == null) {
								bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.launcher);
							} else {
								bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
							}
							imageSpot.setImageBitmap(bm);

							// set the tag of the parent view
							View parent = (View) view.getParent();
							parent.setTag(cursor.getString(pkIndex) + "|" + cursor.getString(commonIndex));

							// return true to say we handled to binding
							return true;
						}
						return false;
					}
				});
				listView.setAdapter(adapter);
			}
			_dlg.dismiss();
		}
	}
}