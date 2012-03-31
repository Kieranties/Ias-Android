package org.iasess.android.activities;

import org.iasess.android.IasessApp;
import org.iasess.android.R;
import org.iasess.android.api.ApiHandler;
import org.iasess.android.api.UserCheckResponse;

import android.app.Activity;
import android.app.ProgressDialog;
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

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE){
			// fire off the validation check
			final ProgressDialog dialog = ProgressDialog.show(Settings.this, "", "Checking Username...", true);
			UserCheckResponse userCheck = ApiHandler.checkUser(_editText.getText().toString());
			dialog.cancel();
			IasessApp.makeToast(this, userCheck.getAnswer());
			String username = userCheck.getUsername();
			if(username != null && username != ""){
				_editText.setText(username);
			}
			_editText.clearFocus();
		}
		return false;
	}
}