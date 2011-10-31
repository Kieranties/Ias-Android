package com.kieranties.ias;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Ias extends Activity {

	private ImageHandler _imgHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_imgHandler = new ImageHandler(this);
		setContentView(R.layout.ias);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		_imgHandler.handleSelection(requestCode, resultCode, data);
	}

	public void onLocationClick(View v) {
		startActivity(new Intent(this, LocationActivity.class));
	}

	// click event to set a selected image
	public void onImageClick(View v) {
		_imgHandler.setImage();
	}
}