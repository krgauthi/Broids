package com.Broders.Entities;

import com.Broders.Logic.Settings;

/**
 * This enum will be used to hold different Asteroid types and their corresponding default values for initialization.
 * 
 * @author	ntpeters
 *
 */
public enum AsteroidType implements Type{

	SMALL	("00", "small", Settings.data_path + "broid.png"),
	MEDIUM	("01", "medium", Settings.data_path + "broid2.png"),
	LARGE	("02", "large", Settings.data_path + "broid3.png");
	
	private String type;
	private String str;
	private String sprite;
	
	/**
	 * Carries default values for asteroid initialization.
	 * These can be changed in the asteroid after creation if desired.
	 * 
	 * @param	type	The two digit value representing the asteroid type
	 * @param	str		The string value of the asteroid type name
	 * @param	sprite	Sprite file path
	 */
	private AsteroidType(String type, String str, String sprite){
		this.type = type;
		this.str = str;
		this.sprite = sprite;
	}
	
	/**
	 * @see	Type#getTypeValue()
	 */
	public String getTypeValue(){
		return type;
	}
	
	/**
	 * @see	Type#getSpritePath()
	 */
	public String getSpritePath(){
		return sprite;
	}
	
	/**
	 * @see	Type#toString()
	 */
	public String toString(){
		return this.str;
	}
	
	/**
	 * @see	Type#equals(Type)
	 */
	public boolean equals(Type type) {
		return type.toString().equals(this.str);
	}	
}