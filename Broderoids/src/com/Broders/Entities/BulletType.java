package com.Broders.Entities;

import com.Broders.Logic.Settings;

/**
 * This enum will be used to hold different Bullet types and their corresponding default values for initialization.
 * 
 * @author	ntpeters
 *
 */
public enum BulletType implements Type{

	BASIC ("00", "basic", Settings.data_path + "bullet.png");
	
	private String type;
	private String str;
	private String sprite;
	
	/**
	 * Carries default values for bullet initialization.
	 * These can be changed in the bullet after creation if desired.
	 * 
	 * @param	type	The two digit value representing the bullet type
	 * @param	str		The string value of the bullet name
	 * @param	sprite	Sprite file path
	 */
	private BulletType(String type, String str, String sprite){
		this.type = type;
		this.str = str;
		this.sprite = sprite;
	}
	
	/**
	 * @see Type#getTypeValue()
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
	public boolean equals(Type t) {
		return str.toString().equals(this.str);
	}
}
