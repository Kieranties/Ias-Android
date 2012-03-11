package org.iasess.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public final class ImageHandler {
	//Don't want instances of this class created
	private ImageHandler(){}
	
	private final int CAMERA_DLG = 1000;
	private static AlertDialog cameraDlg;
	
	private static void showCameraDialog(final Activity activity){
		//showDialog(CAMERA_DLG);
		if(cameraDlg == null) initCameraDlg(activity);	
		
		cameraDlg.show();
	}
	
	private static void initCameraDlg(final Activity activity){
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		//set items from resource list
		final String[] options = activity.getResources().getStringArray(R.array.camera_options);
		builder.setItems(options, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        Toast.makeText(activity.getApplicationContext(), options[item], Toast.LENGTH_SHORT).show();
		    }
		});
		cameraDlg = builder.create();
	}
	
	public static void getImage(final Activity activity){
		//get the user selection
		showCameraDialog(activity);
	}

}
