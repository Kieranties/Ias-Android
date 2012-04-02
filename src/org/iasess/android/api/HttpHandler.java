package org.iasess.android.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/*
 * Handles Http requests, returning JSON response strings
 */
public class HttpHandler {

	/*
	 * Executes a multi part post 
	 */
	public static String executeMultipartPost(String url, String imgPath, HashMap<String, String> fields) throws Exception {
		try {
			//init client
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost poster = new HttpPost(url);
			
			//populate submission content from field map
			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (HashMap.Entry<String, String> entry : fields.entrySet()) {
				multipartEntity.addPart(entry.getKey(), new StringBody(entry.getValue()));
			}
			
			//populate submission content with file
			File image = new File(imgPath);
			multipartEntity.addPart("photo", new FileBody(image));
			poster.setEntity(multipartEntity);

			//perform the actual post
			return httpclient.execute(poster, new ResponseHandler<String>() {
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity r_entity = response.getEntity();
					return EntityUtils.toString(r_entity);
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * Returns the response string for the given url
	 */
	public static String getResponseString(String url) throws Exception {
		try {
			return getResponseString(url, null);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * Returns the response string for the given url and params
	 */
	public static String getResponseString(String url, ArrayList<NameValuePair> qsParams) throws Exception {
		try {
			return EntityUtils.toString(executeGet(url, qsParams));
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * Returns a byte array for the given url
	 */
	public static byte[] getResponseByteArray(String url) throws Exception {
		try {
			return EntityUtils.toByteArray(executeGet(url, null));
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	/*
	 * Performs a get request
	 */
	private static HttpEntity executeGet(String url, ArrayList<NameValuePair> qsParams) throws Exception {
		try {
			//init client
			HttpClient client = new DefaultHttpClient();			
			if(qsParams != null){
				url += "?" + URLEncodedUtils.format(qsParams, "UTF-8");
			}
			HttpGet getter = new HttpGet(url);
			//execute
			HttpResponse response = client.execute(getter);
			return response.getEntity();
		} catch (Exception e) {
			throw e;
		}
	}
}
