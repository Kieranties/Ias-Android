package org.iasess.android.api;

import java.util.ArrayList;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.adapters.TaxaItem;

public class ApiHandler {

	private static String API_BASE = IasessApp.getResourceString(R.string.ias_base);
	private static String API_VERSION = IasessApp.getResourceString(R.string.ias_api_version);
	private static String API_TAXA_GALLERY = IasessApp.getResourceString(R.string.ias_taxa_gallery);
	private static String API_USER_CHECK = IasessApp.getResourceString(R.string.ias_user_check);
	
	private static String getFullApiUrl(String string){
		return API_BASE + API_VERSION + string;
	}
	
	public static ArrayList<TaxaItem> getTaxa(boolean fromCache){
		//TODO: implement fromCache
		try {
			return JsonClient.getJsonArray(getFullApiUrl(API_TAXA_GALLERY));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
