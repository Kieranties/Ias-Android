package org.iasess.android.activities;

import java.util.ArrayList;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.adapters.TaxaItemAdapter;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.api.TaxaItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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
     * Handles the passing of selected data to next activity
     */
    public void onNextClick(View v){        
    	Intent intent = new Intent(this, Summary.class);
    	//set the selected image
    	Uri selected = getIntent().getData();
    	intent.setData(selected);
    	
        //set the selected taxa        
    	intent.putExtra(IasessApp.SELECTED_TAXA, (String)v.getTag());
    	
    	startActivity(intent);
    }
    
    /*
     * Separate thread action to fetch list details
     */
    private class PopulateList extends AsyncTask<String, Void, ArrayList<TaxaItem>> {	
		/*
		 * The progress dialog to display to the user
		 */
		private ProgressDialog _dlg;
		
		/*
		 * Executed before any processing of the task itself
		 */
		protected void onPreExecute() {
			//display the dialog to the user
			_dlg = ProgressDialog.show(SelectTaxa.this, "", "Fetching taxa...", true);
	    }
        
		/*
		 * The actual execution method ran in a background thread 
		 */
	    protected ArrayList<TaxaItem> doInBackground(String... params) {        	
        	//request items from api - don't need params...
        	return ApiHandler.getTaxa(true);
	    }
	    
	    /*
	     * Fired when all processing has finished
	     */
	    protected void onPostExecute(ArrayList<TaxaItem> result) {    	
	    	//populate list
	    	ListView listView = (ListView)findViewById(R.id.listTaxa);	    	
        	TaxaItemAdapter adapter = new TaxaItemAdapter(SelectTaxa.this, R.layout.image_list_item, result);
        	listView.setAdapter(adapter);   		
        	_dlg.dismiss();
	    }
	}
}