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
public final class ImageHandler {
	
	/*
	 * The request code to handle camera image selection intents
	 */
	public static final int CAMERA_OPTION = 1000;
	
	/*
	 * The request code to handle gallery image selection intents
	 */
	public static final int GALLERY_OPTION = 1001;
	
	/*
	 * The location in which the last created image was placed.
	 * Work around for Samsung devices 
	 */
	private static Uri lastCreatedImageUri;
	
	/*
	 * Static constructor - Don't want instances of this class to be created
	 */
	private ImageHandler() {}

	/*
	 * Request an image
	 */
	public static void getImage(Activity activity) {
		// get the user selection
		initCameraDialog(activity).show();
	}

	/*
	 * Processes intent result data for an image
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
	
	/*
	 * Given a uri, returns the image bitmap
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
	
	/*
	 * Given a uri, returns the actual path to the item on the device
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
	
	/*
	 * Creates the dialog displayed to the user to select an image
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

	/*
	 * Executes an intent for the selection of an image from the devices camera
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

	/*
	 * Executes an intent for the selection of an image from the devices storage
	 * (through a gallery application)
	 */
	private static void galleryIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		
		activity.startActivityForResult(intent, GALLERY_OPTION);
	}

	/*
	 * Get the degrees required to rotate the image.
	 * (Work around for Samsung devices)
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