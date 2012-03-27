package org.iasess.android.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.iasess.android.adapters.TaxaItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonClient {

    public static ArrayList<TaxaItem> getJsonArray(String url) throws Exception
    {
        try {
        	Gson gson = new Gson();
        	Type collectionType = new TypeToken<ArrayList<TaxaItem>>(){}.getType();
        	return gson.fromJson(processRequestStream(url), collectionType);        	
        } catch (Exception e) {
            throw new Exception("Errorered", e);     
        }
    }
    
    
    private static String processRequestStream(String url) throws Exception{
        try {
        	HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url); 
        	HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream stream = entity.getContent();
                return convertStreamToString(stream);
            }
        } catch (Exception e) {
            throw new Exception("Errorered", e);     
        }
        return null;
    }
    
    
    private static String convertStreamToString(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (Exception e) {
        } 
        finally {
            try {
                stream.close();
            } catch (Exception e) {
                
            }
        }
        return sb.toString();
    }

}
