package org.iasess.android.data;

import java.util.ArrayList;

import org.iasess.android.api.ApiHandler;
import org.iasess.android.api.TaxaItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaxaStore  extends SQLiteOpenHelper {

	/** Key table details **/
	private static final String DATABASE_NAME = "IAS.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "taxa";
    
    /** Table columns **/
    public static final String COL_PK  = "_id"; // NEEDS to be called _id otherwise cursors don't work
    public static final String COL_COMMON_NAME = "common_name";
    public static final String COL_SCIENTIFIC_NAME = "scientfic_name";
    public static final String COL_RANK = "rank";
    public static final String COL_KEY_TEXT = "key_text";
    //private static final String COL_SIGHTINGS = "sightings"; // TODO: save sightings?
    public static final String COL_LISTING_IMAGE = "listing_image";
    public static final String COL_LARGE_IMAGE = "large_image";
    
    /** Table scripts **/
    private static final String TABLE_CREATE =
    		"CREATE TABLE " + TABLE_NAME 
    		+ " ( " 
    		+ COL_PK + " integer primary key,"
            + COL_COMMON_NAME + " text, " 
    		+ COL_SCIENTIFIC_NAME + " text, "
    		+ COL_RANK + " text, "
    		+ COL_KEY_TEXT + " text, "
            + COL_LISTING_IMAGE + " blob,"
            + COL_LARGE_IMAGE + " text );";

    
    public TaxaStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//drop and recreate db
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		
	}
	
	public void addTaxa(TaxaItem item){
		SQLiteDatabase db = this.getWritableDatabase();		 
	    insertTaxa(item, db);
	    db.close();
	}
	
	public void addTaxa(ArrayList<TaxaItem> taxa){
		SQLiteDatabase db = this.getWritableDatabase();	
		for(TaxaItem item : taxa){
			insertTaxa(item, db);
		}
		db.close();
	}
	
	public void udpateTaxa(TaxaItem item){
		
	}
	
	public void updateTaxa(ArrayList<TaxaItem> taxa){
		
	}
	
	public Cursor getAllItems() {
	    String selectQuery = "SELECT * FROM " + TABLE_NAME;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    return db.rawQuery(selectQuery, null);
	}
	
	private void insertTaxa(TaxaItem item, SQLiteDatabase db){
		
		//populate the storage values
		ContentValues values = new ContentValues();
	    values.put(COL_PK, item.getPk());
	    values.put(COL_COMMON_NAME, item.getCommonName());
	    values.put(COL_SCIENTIFIC_NAME, item.getScientificName());
	    values.put(COL_RANK, item.getRank());
	    values.put(COL_KEY_TEXT, item.getKeyTxt());
	    values.put(COL_LARGE_IMAGE, item.getLargeImagePath());
	    byte[] imageData = getListingImage(item);
	    if(imageData != null){
	    	values.put(COL_LISTING_IMAGE, imageData);
	    }
	    
	    //insert the entry
	    db.insert(TABLE_NAME, null, values);
	}
	
	private byte[] getListingImage(TaxaItem item){
		String imagePath = item.getListingImagePath();
	    if(imagePath != null){
	    	return ApiHandler.getByteArray(imagePath, false);
	    }
		return null;
	}
}
