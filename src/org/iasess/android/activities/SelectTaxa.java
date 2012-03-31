package org.iasess.android.activities;

import java.util.ArrayList;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.adapters.TaxaItemAdapter;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.api.TaxaItem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
        fillList();
    }
    
    /*
     * Requests and populates the taxa selection
     */
    public void fillList(){
    	ListView listView = (ListView)findViewById(R.id.listTaxa);
    	
    	//request items from api
    	ArrayList<TaxaItem> taxa = ApiHandler.getTaxa(true);
    	
    	//populate list
    	TaxaItemAdapter adapter = new TaxaItemAdapter(this, R.layout.image_list_item, taxa);
    	listView.setAdapter(adapter);    	
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
}