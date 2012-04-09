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

	
	/**
	 * Database instance name 
	 */
	private static final String DATABASE_NAME = "IAS.db";
    /**
     * Current database version
     */
    private static final int DATABASE_VERSION = 1;
    
    /**
     * Database table for this store
     */
    private static final String TABLE_NAME = "taxa";
    
    
    /**
     * The column name for the primary key of a taxa
     */
    public static final String COL_PK  = "_id"; // NEEDS to be called _id otherwise cursors don't work
    /**
     * The column name for the common name property of a taxa
     */
    public static final String COL_COMMON_NAME = "common_name";
    /**
     * The column name for the scientific name of a taxa
     */
    public static final String COL_SCIENTIFIC_NAME = "scientfic_name";
    /**
     * The column name for the rank of a taxa
     */
    public static final String COL_RANK = "rank";
    /**
     * The column name for the key text of a taxa
     */
    public static final String COL_KEY_TEXT = "key_text"; 
    /**
     * The column name for the listing image for a taxa
     */
    public static final String COL_LISTING_IMAGE = "listing_image";
    /**
     * The column name for the large image for a taxa
     */
    public static final String COL_LARGE_IMAGE = "large_image";
    
    
    /**
     * The table create scripts 
     */
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

    
    /**
     * Constructor method
     * @param context The context to create this instance against
     */
    public TaxaStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Executes the script to create the table for this datastore
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

	/**
	 * Executes the script to update the table for this data store
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//drop and recreate db
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		
	}
	
	/**
	 * Stores the give {@link TaxaItem} in this store
	 * 
	 * @param item The item to save
	 */
	public void addTaxa(TaxaItem item){
		SQLiteDatabase db = this.getWritableDatabase();		 
	    insertTaxaItem(item, db);
	    db.close();
	}
	
	/**
	 * Stores the given collection of {@link TaxaItem} in this store
	 * 
	 * @param collection The collection to save
	 */
	public void addTaxa(ArrayList<TaxaItem> collection){
		SQLiteDatabase db = this.getWritableDatabase();	
		for(TaxaItem item : collection){
			insertTaxaItem(item, db);
		}
		db.close();
	}
	
	/**
	 * Updates the store with details of the given item.
	 * <p>
	 * Updates are performed based on the items PK field.
	 * 
	 * @param item The {@link TaxaItem} to update.
	 */
	public void udpateTaxa(TaxaItem item){
		SQLiteDatabase db = this.getWritableDatabase();		 
		updateTaxaItem(item, db);
	    db.close();
	}
	
	/**
	  * Updates the store with details of the given collection of {@link TaxaItem}.
	 * <p>
	 * Updates are performed based on an items PK field.
	 * 
	 * @param collection The {@link TaxaItem} collection to update.
	 */
	public void updateTaxa(ArrayList<TaxaItem> collection){
		SQLiteDatabase db = this.getWritableDatabase();	
		for(TaxaItem item : collection){
			updateTaxaItem(item, db);
		}
		db.close();
	}
	
	/**
	 * Fetches a {@link Cursor} referencing all {@link TaxaItem}s in the store.
	 * <p>
	 * Ordered by their Common Name
	 * 
	 * @return a {@link Cursor} of the items from the store
	 */
	public Cursor getAllItems() {	 
	    return executeStringQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_COMMON_NAME + " ASC");
	}
	
	/**
	 * Fetches a {@link Cursor} referencing the {@link TaxaItem} matched by pk
	 * 
	 * @param pk the primary key identifier of the item to return
	 * @return The {@link Cursor} containing the references item
	 */
	public Cursor getByPk(long pk){
		return executeStringQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_PK + " = " + pk);
	}
	
	/**
	 * Executes the given query against the database
	 * 
	 * @param q the query to execute
	 * @return a {@link Cursor} of the results
	 */
	private Cursor executeStringQuery(String q){
		SQLiteDatabase db = this.getReadableDatabase();
	    return db.rawQuery(q, null);
	}
	
	/**
	 * Executes the update script for a given {@link TaxaItem}
	 * 
	 * @param item The {@link TaxaItem} being updated
	 * @param db The {@link SQLiteDatabase} the update should be executed against
	 */
	private void updateTaxaItem(TaxaItem item, SQLiteDatabase db){
		ContentValues values = getContent(item);
		db.update(TABLE_NAME, values, COL_PK + " = " + item.getPk(), null);
	}
	
	/**
	 * Executes the insert script for a given {@link TaxaItem}
	 * 
	 * @param item The {@link TaxaItem} being inserted
	 * @param db The {@link SQLiteDatabase} the item is being inserted into
	 */
	private void insertTaxaItem(TaxaItem item, SQLiteDatabase db){
	    //insert the entry
	    db.insert(TABLE_NAME, null, getContent(item));
	}
	
	/**
	 * Gets the {@link ContentValues} to be stored for a given {@link TaxaItem}
	 * 
	 * @param item The {@link TaxaItem} to process
	 * @return a {@link ContentValues} instance containing the values to store
	 */
	private ContentValues getContent(TaxaItem item){
		ContentValues values = new ContentValues();
	    values.put(COL_PK, item.getPk());
	    values.put(COL_COMMON_NAME, item.getCommonName());
	    values.put(COL_SCIENTIFIC_NAME, item.getScientificName());
	    values.put(COL_RANK, item.getRank());
	    values.put(COL_KEY_TEXT, item.getKeyTxt());
	    values.put(COL_LARGE_IMAGE, item.getLargeImagePath());
	    //fetch the listing image bytes to be saved
	    byte[] imageData = getListingImage(item);
	    if(imageData != null){
	    	values.put(COL_LISTING_IMAGE, imageData);
	    }
	    
	    return values;
	}
	
	/**
	 * Fetches the details of a {@link TaxaItem} listing image
	 * 
	 * @param item The {@link TaxaItem to fetch the image for
	 * @return A byte[] representing the image, or null
	 */
	private byte[] getListingImage(TaxaItem item){
		String imagePath = item.getListingImagePath();
	    if(imagePath != null){
	    	return ApiHandler.getByteArray(imagePath, false);
	    }
		return null;
	}
}
