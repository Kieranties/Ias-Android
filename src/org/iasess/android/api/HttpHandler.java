package org.iasess.android.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

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

import android.util.Log;

public class HttpHandler {

	public static void executeMultipartPost(String url, String imgPath, HashMap<String, String> strings) throws Throwable {
		try {
			//client init
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost poster = new HttpPost(url);
			
			//populate submission content from strings
			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (HashMap.Entry<String, String> entry : strings.entrySet()) {
				multipartEntity.addPart(entry.getKey(), new StringBody(entry.getValue()));
			}
			
			//populate submission content with file
			File image = new File(imgPath);
			multipartEntity.addPart("photo", new FileBody(image));
			poster.setEntity(multipartEntity);

			//perform the actual post
			httpclient.execute(poster, new ResponseHandler<Object>() {
				public Object handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity r_entity = response.getEntity();
					String responseString = EntityUtils.toString(r_entity);
					Log.d("UPLOAD", responseString);
					return null;
				}
			});
		} catch (Throwable e) {
			throw e;
			// TODO: error handling you tool
		}
	}

	public static String getResponseString(String url) throws Exception {
		try {
			return executeGet(url);
		} catch (Exception e) {
			throw new Exception("Errorered", e);
		}
	}

	private static String executeGet(String url) throws Exception {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet getter = new HttpGet(url);
			HttpResponse response = client.execute(getter);
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
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
		} finally {
			try {
				stream.close();
			} catch (Exception e) {

			}
		}
		return sb.toString();
	}

}
