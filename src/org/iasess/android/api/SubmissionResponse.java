package org.iasess.android.api;

import com.google.gson.annotations.SerializedName;

/*
 * Implementation of the response given by API submissions
 */
public class SubmissionResponse {
	
	/*
	 * The url of the submission
	 */
	@SerializedName("url")
	private String _url;
	
	/*
	 * The unique identifier for the submission
	 */
	@SerializedName("id")
	private int _id;
	
	/*
	 * Gets the url for the submission
	 */
	public String getUrl(){ return _url; }
	
	/*
	 * Gets the unique identifier for the submission
	 */
	public int getId(){ return _id;}
	
}
