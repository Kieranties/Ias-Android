package org.iasess.android.activities;

import org.iasess.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		//Load the HTML content from resources
		TextView tv = (TextView)findViewById(R.id.textAboutBlurb);
		String content = getResources().getString(R.string.about_blurb);
		tv.setText(Html.fromHtml(content));
	}

}
