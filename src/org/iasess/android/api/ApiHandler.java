package org.iasess.android.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.adapters.TaxaItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApiHandler {

	private static String API_BASE = IasessApp.getResourceString(R.string.ias_base);
	private static String API_VERSION = IasessApp.getResourceString(R.string.ias_api_version);
	private static String API_TAXA_GALLERY = IasessApp.getResourceString(R.string.ias_taxa_gallery);
	private static String API_USER_CHECK = IasessApp.getResourceString(R.string.ias_user_check);
	private static String API_SIGHTING = IasessApp.getResourceString(R.string.ias_sighting);
	
	private static String getFullApiUrl(String string){
		return API_BASE + API_VERSION + string;
	}
	
	public static ArrayList<TaxaItem> getTaxa(boolean fromCache){
		//TODO: implement fromCache
		try {
			String resp = HttpHandler.getResponseString(getFullApiUrl(API_TAXA_GALLERY));
			Gson gson = new Gson();
        	Type collectionType = new TypeToken<ArrayList<TaxaItem>>(){}.getType();
        	return gson.fromJson(resp, collectionType); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void submitSighting(String img, int taxa, double lat, double lon, String user){
		try {
			String url = getFullApiUrl(API_SIGHTING);
			HashMap<String, String> fields = new HashMap<String, String>();
			fields.put("email", user);
			fields.put("location", "POINT(" + lat +" " + lon +")");			
			fields.put("taxon", Integer.toString(taxa));			
			HttpHandler.executeMultipartPost(url, img, fields);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
