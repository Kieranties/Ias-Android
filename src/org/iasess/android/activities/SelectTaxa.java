package org.iasess.android.activities;

import java.util.ArrayList;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.adapters.TaxaItem;
import org.iasess.android.adapters.TaxaItemAdapter;
import org.iasess.android.api.ApiHandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class SelectTaxa extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_taxa);
        fillList();
    }
    
    public void fillList(){
    	ListView listView = (ListView)findViewById(R.id.listTaxa);
    	ArrayList<TaxaItem> taxa = ApiHandler.getTaxa(true);
    	//populate list
    	TaxaItemAdapter adapter = new TaxaItemAdapter(this, R.layout.image_list_item, taxa);
    	listView.setAdapter(adapter);    	
    }
    
    public void onNextClick(View v){        
    	Intent intent = new Intent(this, Summary.class);
        //set the selected taxa        
    	intent.putExtra(IasessApp.SELECTED_TAXA, (String)v.getTag());
    	//set the selected image
    	Uri selected = getIntent().getData();
    	intent.setData(selected);
    	startActivity(intent);
    }
}

