package org.iasess.android.adapters;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class TaxaItem {
	
	@SerializedName("key_text")
	private String keyText;
	@SerializedName("scientific_name")
	private String scientificName;
	private int[] sightings;
	private String rank;
	@SerializedName("key_images")
	private Map<String, String[]> keyImages;
	@SerializedName("common_name")
	private String commonName;
	private int pk;	
	
	public String getKeyTxt(){		
		return keyText;
	}
	
	public String getScientificName(){
		return scientificName;
	}
	
	public int[] getSightings(){
		return sightings;
	}
	
	public String getRank(){
		return rank;
	}
	
	public Map<String, String[]> getKeyImages(){
		return keyImages;
	}
	
	public String getCommonName(){
		return commonName;
	}
	
	public int getPk(){
		return pk;
	}
}
