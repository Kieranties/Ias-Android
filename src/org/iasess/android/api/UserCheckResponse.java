package org.iasess.android.api;

import com.google.gson.annotations.SerializedName;

/*
 * Represents details that can be sent back by a username check
 * through the API
 */
public class UserCheckResponse {

	/*
	 * The answer from the server
	 */
	@SerializedName("answer")
	private String _answer;
	
	/*
	 * The username sent back from the server
	 */
	@SerializedName("username")
	private String _username;
	
	/*
	 * Returns the response answer
	 */
	public String getAnswer(){ return _answer; }
	
	/*
	 * Returns the response username
	 */
	public String getUsername(){ return _username; }
	
}
