package org.iasess.android.api;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

/*
 * Object implementation of the data returned by the API
 * for Taxon requests.  Can be serialized/deserialized with Gson 
 */
public class TaxaItem {
	
	/*
	 * The text details of the item
	 */
	@SerializedName("key_text")
	private String _keyText;
	
	/*
	 * The full scientific name of the item
	 */
	@SerializedName("scientific_name")
	private String _scientificName;
	
	/*
	 * List of sightings for the item
	 */
	@SerializedName("sightings")
	private int[] _sightings;
	
	/*
	 * The rank of the item
	 */
	@SerializedName("rank")
	private String _rank;
	
	/*
	 * Map of images for the item.
	 * Each Map entry relates to a size and set of images
	 * of that size
	 */
	@SerializedName("key_images")
	private Map<String, String[]> _keyImages;
	
	/*
	 * The common/simple name for the item
	 */
	@SerializedName("common_name")
	private String _commonName;
	
	/*
	 * The unique identifier for the item
	 */
	@SerializedName("pk")
	private int _pk;	
	
	
	/*
	 * Gets the item key text	
	 */
	public String getKeyTxt(){ return _keyText; }
	
	/*
	 * Gets the item scientific name	
	 */
	public String getScientificName(){ return _scientificName; }
	
	/*
	 * Gets the item sightings	
	 */
	public int[] getSightings(){ return _sightings; }
	
	/*
	 * Gets the item rank
	 */
	public String getRank(){ return _rank; }
	
	/*
	 * Gets the item key images
	 */
	public Map<String, String[]> getKeyImages(){ return _keyImages; }
	
	/*
	 * Gets the item common name
	 */
	public String getCommonName(){ return _commonName; }
	
	/*
	 * Gets the item unique identifier	
	 */
	public int getPk(){ return _pk;	}
}