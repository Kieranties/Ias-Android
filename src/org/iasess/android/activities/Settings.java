package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.api.UserCheckResponse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/*
 * Activity for the Settings screen
 */
public class Settings extends Activity implements OnEditorActionListener {

	/*
	 * The text control containing the user details
	 */
	private EditText _editText;

	/*
	 * Initializer
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.settings);
		
		//find the edit box
		_editText = (EditText) findViewById(R.id.editUsername);

		// set from application preferences
		_editText.setText(IasessApp.getPreferenceString(IasessApp.PREFS_USERNAME));

		// attach listener for focus lost events
		_editText.setOnEditorActionListener(this);
	}

	/*
	 * Handles the click event to set preference and end activity
	 */
	public void onDoneClick(View v) {
		IasessApp.setPreferenceString(IasessApp.PREFS_USERNAME, _editText.getText().toString());
		finish();
	}

	/*
	 * Capture events from the soft keyboard for the username edit box
	 */
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE){
			new UsernameCheckTask().execute(_editText.getText().toString());			
		}
		return false;
	}
	
	/*
	 * Class to handle async requests to server
	 */
	private class UsernameCheckTask extends AsyncTask<String, Void, UserCheckResponse> {	
		/*
		 * The progress dialog to display to the user
		 */
		private ProgressDialog _dlg;
		
		/*
		 * Executed before any processing of the task itself
		 */
		protected void onPreExecute() {
			//display the dialog to the user
			_dlg = ProgressDialog.show(Settings.this, "", "Checking Username...", true);
	    }
        
		/*
		 * The actual execution method ran in a background thread 
		 */
	    protected UserCheckResponse doInBackground(String... textValue) {
	    	//return the response from the api
	        return ApiHandler.checkUser(textValue[0]);
	    }
	    
	    /*
	     * Fired when all processing has finished
	     */
	    protected void onPostExecute(UserCheckResponse result) {
	    	_dlg.dismiss();
	    	
	    	//inform the user of the response
	    	IasessApp.makeToast(result.getAnswer());
	    	
	    	//update ui if required
			String username = result.getUsername();
			if(username != null && username != ""){
				_editText.setText(username);
			}
			_editText.clearFocus();			
	    }
	}
}