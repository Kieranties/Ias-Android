package com.kieranties.ias;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

public class ImageHandler {

	private static final int CAMERA_PIC_REQUEST = 3009;
	private static final int GALLERY_PIC_REQUEST = 3010;
	private Activity _act;
	private AlertDialog _cameraDlg = null;
	private Uri _capturedImg = null;

	public ImageHandler(Activity act) {
		_act = act;
		initCameraDlg();
	}

	public void setImage() {
		_cameraDlg.show();
	}

	// handler for call back from camera
	public void handleSelection(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;

		ImageView image = (ImageView) _act.findViewById(R.id.selectedImageView);
		if (requestCode == GALLERY_PIC_REQUEST) {
			// we need to set the capturedImg uri
			_capturedImg = data.getData();
		}
		Bitmap bm = getBitmap(_capturedImg);
		image.setImageBitmap(bm);

	}
	
	private void initCameraDlg() {
		if (_cameraDlg == null) {
			// set up camera type
			CharSequence[] items = { "Camera", "Gallery" };
			AlertDialog.Builder builder = new AlertDialog.Builder(_act);
			builder.setTitle("Set picture from");

			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Intent cameraIntent = null;
					int requestType;
					if (item == 0) {// perform camera action
						// prep the file output
						String fileName = "ias-" + System.currentTimeMillis()
								+ ".jpg";
						ContentValues values = new ContentValues();
						values.put(MediaStore.Images.Media.TITLE, fileName);
						values.put(MediaStore.Images.Media.DESCRIPTION,
								"Taken for Ias");
						_capturedImg = _act.getContentResolver().insert(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								values);

						// create intent with extra output to grab uri later
						cameraIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								_capturedImg);
						requestType = CAMERA_PIC_REQUEST;

					} else { // perform gallery action
						cameraIntent = new Intent();
						cameraIntent.setType("image/*");
						cameraIntent.setAction(Intent.ACTION_GET_CONTENT);
						requestType = GALLERY_PIC_REQUEST;
					}
					_act.startActivityForResult(cameraIntent, requestType);
				}
			});
			_cameraDlg = builder.create();
		}
	}

	// processes a given uri for the full item path
	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = _act.managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private Bitmap getBitmap(Uri uri) {
		Bitmap bm = null;
		try {
			bm = MediaStore.Images.Media.getBitmap(_act.getContentResolver(),
					uri);
			int rotation = getImageRotation(uri);
			if (rotation == 0)
				return bm;

			Matrix mtx = new Matrix();
			mtx.postRotate(rotation);
			return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					mtx, true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}

	private int getImageRotation(Uri uri) {
		try {
			// check orientation
			ExifInterface exif = new ExifInterface(getPath(uri));
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				return -90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
