package org.iasess.android;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

/*
 * Handles the retrieval and processing of images
 */
/**
 * Singleton class to manage all image interactions with
 * the device camera or media storage
 */
public final class ImageHandler {
	
	/**
	 * The intent request code for intents returning an image
	 * from the users camera
	 */
	public static final int CAMERA_OPTION = 1000;
	
	/**
	 * The intent request code for intents returning an image
	 * from the users gallery
	 */
	public static final int GALLERY_OPTION = 1001;
	
	
	/**
	 * Work around for Samsung devices.
	 * <p>
	 * Holds the URI of the last created image by the devices camera
	 */
	private static Uri lastCreatedImageUri;
	
	/**
	 * Private constructor to promote singleton use of class 
	 */
	private ImageHandler() {}

	
	/**
	 * Display a dialog to the user to allow them to select an image.
	 * <p>
	 * Fires of the relevant intents. 
	 * 
	 * @param activity The activity context to display the dialog
	 */
	public static void getImage(Activity activity) {
		// get the user selection
		initCameraDialog(activity).show();
	}

	
	/**
	 * Returns the URI selected by the user in an image selection intent
	 * 
	 * @param resultCode The result of the Intent
	 * @param requestCode The identity of the Intent
	 * @param data The actual data of the Intent
	 * @return The URI of the selected image
	 */
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
	
	
	/**
	 * Fetches the Bitmap of the given URI
	 * 
	 * @param uri The URI to return a Bitmap for
	 * @return The Bitmap of the URI correctly orientated
	 */
	public static Bitmap getBitmap(Uri uri) {
		Bitmap bm = null;
		try {
			//find in the media store
			bm = MediaStore.Images.Media.getBitmap(IasessApp.getContext().getContentResolver(),uri);
			
			//check it's orientation
			int rotation = getImageRotation(uri);
			if (rotation != 0){
				//rotate if required
				Matrix mtx = new Matrix();
				mtx.postRotate(rotation);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),mtx, true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}
	
	/**
	 * Gets the physical path to a given URI on the device
	 * 
	 * @param uri The URI to find the physical path for
	 * @return The physical path to the URI
	 */
	public static String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		ContentResolver resolver = IasessApp.getContext().getContentResolver();
		Cursor cursor = resolver.query(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		cursor.close();
		return path;
	}	
	
	
	/**
	 * Creates an AlertDialog allowing the user to select where/how
	 * they would select an image for the application
	 * 
	 * @param activity The Context for which the dialog should be displayed
	 * @return The AlertDialog instance
	 */
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

	
	/**
	 * Creates and executes and Intent to process an image selection
	 * based on a newly created image in the devices camera application
	 * 
	 * @param activity The context with which Intent occurs within
	 */
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


	/**
	 * Creates and executes and Intent to capture and process an image selection
	 * from the users gallery/device
	 * 
	 * @param activity The Activity within which the Intent occurs 
	 */
	private static void galleryIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		
		activity.startActivityForResult(intent, GALLERY_OPTION);
	}

	
	/**
	 * Returns the number of degrees an image should be rotated to 
	 * provide its correct orientation
	 * 
	 * @param uri The URI of the image
	 * @return The number if degrees to rotate the image
	 */
	private static int getImageRotation(Uri uri) {
		try {
			// check orientation
			ExifInterface exif = new ExifInterface(getPath(uri));
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
}