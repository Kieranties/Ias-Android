package org.iasess.android;

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

public final class ImageHandler {
	// Don't want instances of this class created
	private ImageHandler() {}

	public static final int CAMERA_OPTION = 1000;
	public static final int GALLERY_OPTION = 1001;
	public static final String IMAGE_PATH = "ias_image_path";
	private static Uri lastCreatedImageUri;

	
	private static AlertDialog initCameraDialog(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		// set items from resource list
		final String[] options = activity.getResources().getStringArray(R.array.camera_options);
		builder.setItems(options, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) 
					cameraIntent(activity);
				else 
					galleryIntent(activity);
			}
		});
		return builder.create();
	}

	private static void cameraIntent(Activity activity) {
		String fileName = "ias-" + System.currentTimeMillis() + ".jpg";
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION, "Taken for invadr");
		lastCreatedImageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        
		// create intent with extra output to grab uri later
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, lastCreatedImageUri);
		
		activity.startActivityForResult(Intent.createChooser(intent,"Select Picture"), CAMERA_OPTION);
	}

	private static void galleryIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		
		activity.startActivityForResult(intent, GALLERY_OPTION);
	}

	public static void getImage(Activity activity) {
		// get the user selection
		AlertDialog dlg = initCameraDialog(activity);
		dlg.show();
	}

	public static Uri getImageUriFromIntentResult(int resultCode, int requestCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return null;
		
		switch (requestCode){
			case ImageHandler.GALLERY_OPTION:
				return data.getData();
			case ImageHandler.CAMERA_OPTION:
				return lastCreatedImageUri;
		}
		
		return null;
	}

	
	public static Bitmap getImageFromUri(Uri uri, Activity activity) {		
		return getBitmap(uri, activity);
	}
	
	private static Bitmap getBitmap(Uri uri, Activity activity) {
		Bitmap bm = null;
		try {
			bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),uri);
			int rotation = getImageRotation(uri, activity);
			if (rotation == 0) return bm;

			Matrix mtx = new Matrix();
			mtx.postRotate(rotation);
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),mtx, true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}

	private static int getImageRotation(Uri uri, Activity activity) {
		try {
			// check orientation
			ExifInterface exif = new ExifInterface(getPath(uri, activity));
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

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
	
	private static String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		cursor.close();
		return path;
	}
}
