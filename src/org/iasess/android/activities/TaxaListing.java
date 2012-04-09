package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.data.TaxaStore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

/**
 * Controls the 'TaxaListing' Activity view
 */
public class TaxaListing extends Activity {

	/**
	 * Initialises the content of this Activity
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taxa_listing);
		new PopulateList().execute(""); // <- TODO: ugly!
		
		ListView lv = (ListView) findViewById(R.id.listTaxa);
		//check to see if we are display in a gallery view
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.containsKey("gallery") && extras.getBoolean("gallery")){
			lv.setOnItemClickListener(new GalleryViewListener());
		} else {
			lv.setOnItemClickListener(new SightingSubmissionListener());	
		}
	}

	/**
	 * Handler for the display of the menu
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.refresh_list, menu);
		return true;
	}

	/**
	 * Handler for the selection of a menu option
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
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

	/**
	 * Clean up the resources of this Activity when destroyed
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		ListView listView = (ListView) findViewById(R.id.listTaxa);
		ListAdapter currentAdapter = listView.getAdapter();
		if ( currentAdapter != null) {
			((SimpleCursorAdapter)currentAdapter).getCursor().close();
		}
	}
		
	/**
	 * Class to listen to ListView item selection events when
	 * the user is in the process of submitting a sighting
	 */
	private class SightingSubmissionListener implements  AdapterView.OnItemClickListener{

		/**
		 * Handler to capture and process the selection of a ListView item
		 * 
		 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
		 */
		public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId) {				
			Intent intent = new Intent(IasessApp.getContext(), Summary.class);
			// set the selected image
			Uri selected = getIntent().getData();
			intent.setData(selected);

			
			// set the selected taxa
			Cursor cursor = (Cursor) adapter.getItemAtPosition(position);
			String name = cursor.getString(cursor.getColumnIndex(TaxaStore.COL_COMMON_NAME));
			
			intent.putExtra(IasessApp.SELECTED_TAXA, rowId);
			intent.putExtra(IasessApp.SELECTED_TAXA_NAME, name);

			startActivity(intent);				
		}		
	}
	
	/**
	 * Class to listen to ListView item selection events when
	 * the user is in the process of viewing the details of a Taxa
	 */
	private class GalleryViewListener implements  AdapterView.OnItemClickListener{

		/**
		 * Handler to capture and process the selection of a ListView item
		 * 
		 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
		 */
		public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId) {				
			Intent intent = new Intent(IasessApp.getContext(), TaxaDetails.class);			
			intent.putExtra(IasessApp.SELECTED_TAXA, rowId);

			startActivity(intent);				
		}		
	}
	
	
	
	/**
	 * Class to handle the processing of the list content in a separate thread
	 */
	private class PopulateList extends AsyncTask<String, Void, Cursor> {
		
		/**
		 * The progress dialog to display  to the user during processing
		 */
		private ProgressDialog _dlg;
		
		/**
		 * Displays the progress dialog to the user before processing the taxa items
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		protected void onPreExecute() {
			// display the dialog to the user
			_dlg = ProgressDialog.show(TaxaListing.this, "", "Fetching taxa...", true,true, new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					PopulateList.this.cancel(true);	
					finish();
				}
			});
		}
		
		/**
		 * Performs a query against the cached data store.
		 * Fetches from the API if the store is empty or 'refresh' has been passed in params
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected Cursor doInBackground(String... params) {
			TaxaStore store = new TaxaStore(TaxaListing.this);
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

		/**
		 * Processes the results of the AsyncTask
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(Cursor result) {
			// populate list
			startManagingCursor(result);
			ListView listView = (ListView) findViewById(R.id.listTaxa);
			ListAdapter currentAdapter = listView.getAdapter();
			if ( currentAdapter != null) {
				((SimpleCursorAdapter)currentAdapter).changeCursor(result);
			} else {
				String[] columns = new String[] { TaxaStore.COL_COMMON_NAME, TaxaStore.COL_SCIENTIFIC_NAME,	TaxaStore.COL_LISTING_IMAGE, TaxaStore.COL_PK };
				int[] to = new int[] { R.id.textPrimary, R.id.textSecondary, R.id.icon };

				SimpleCursorAdapter adapter = new SimpleCursorAdapter(
						TaxaListing.this, R.layout.image_list_item, result,
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