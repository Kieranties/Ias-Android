package org.iasess.android.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.iasess.android.IasessApp;
import org.iasess.android.Logger;
import org.iasess.android.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/*
 * Handler for interactions with the IAS API.
 */
public class ApiHandler {

	/*
	 * The base component of request URIs
	 */
	private static String API_BASE = IasessApp.getResourceString(R.string.ias_base);
	
	/*
	 * The API version component of request URIs
	 */
	private static String API_VERSION = IasessApp.getResourceString(R.string.ias_api_version);
	
	/*
	 * The gallery endpoint component of request URIs
	 */
	private static String API_TAXA_GALLERY = IasessApp.getResourceString(R.string.ias_taxa_gallery);
	
	/*
	 * The user check component of request URIs
	 */
	private static String API_USER_CHECK = IasessApp.getResourceString(R.string.ias_user_check);
	
	/*
	 * The sighting component of request URIs
	 */
	private static String API_SIGHTING = IasessApp.getResourceString(R.string.ias_sighting);
		
	/*
	 * Fetches a list of Taxa from the API service
	 */
	public static ArrayList<TaxaItem> getTaxa(){
		try {
			//get service response
			String resp = HttpHandler.getResponseString(composeApiUri(API_TAXA_GALLERY));
			
			//process through gson
			Gson gson = new Gson();
        	Type collectionType = new TypeToken<ArrayList<TaxaItem>>(){}.getType();
        	ArrayList<TaxaItem> items = gson.fromJson(resp, collectionType);
        	return items;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Submits a sighting to the service
	 */
	public static SubmissionResponse submitSighting(String img, long taxa, double lat, double lon, String user){
		try {			
			String url = composeApiUri(API_SIGHTING);
			//compose field map
			HashMap<String, String> fields = new HashMap<String, String>();
			fields.put("email", user);
			fields.put("location", "POINT(" + lon +" " + lat +")");			
			fields.put("taxon", Long.toString(taxa));			
			String resp = HttpHandler.executeMultipartPost(url, img, fields);
			Gson gson = new Gson();
			return gson.fromJson(resp, SubmissionResponse.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Submits a check for a username
	 */
	public static UserCheckResponse checkUser(String username){
		try {
			//get service response
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("un_or_e", username));
			String resp = HttpHandler.getResponseString(composeApiUri(API_USER_CHECK), params);
			
			//process through gson
			Gson gson = new Gson();
			return gson.fromJson(resp, UserCheckResponse.class);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Returns a byte array for the given request
	 */
	public static byte[] getByteArray(String url, boolean isRelative){
		try {
			if(isRelative) url = API_BASE + url;
			
			return HttpHandler.getResponseByteArray(url);
		} catch (Exception e) {
			Logger.debug("Failed fetching image at: " + url);
		}
		return null;
	}
	
	/*
	 * Composes the full API URI for requests
	 */
 	private static String composeApiUri(String string){
		return API_BASE + API_VERSION + string;
	}
}