package org.iasess.android.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.iasess.android.adapters.TaxaItem;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonClient {

	public static void executeMultipartPost(String url, String img, String user)throws Throwable
    {
        try {
        	HttpClient httpclient = new DefaultHttpClient();
            HttpPost poster = new HttpPost(url);
            
            File image = new File(img);
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE); 
           
            multipartEntity.addPart("taxon", new StringBody("100000"));
            multipartEntity.addPart("email", new StringBody(user));
            multipartEntity.addPart("location", new StringBody("POINT(-2.543989419937134 51.46212918583009)"));
            multipartEntity.addPart("photo", new FileBody(image));
            poster.setEntity(multipartEntity);
            
            httpclient.execute(poster, new ResponseHandler<Object>() {
				public Object handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity r_entity = response.getEntity();
		            String responseString = EntityUtils.toString(r_entity);
		            Log.d("UPLOAD", responseString);
					return null;
				}
			});
        } catch (Throwable e)
        {
        	throw e;
            //TODO: error handling you tool
        }
    }
	
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
