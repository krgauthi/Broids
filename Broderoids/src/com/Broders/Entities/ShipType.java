package com.Broders.Entities;

import com.Broders.Logic.Settings;
import com.badlogic.gdx.graphics.Color;

/**
 * This enum will be used to hold different Ship types and their corresponding default values for initialization.
 * 
 * @author	ntpeters
 *
 */
public enum ShipType implements Type{

	CLASSIC	("00", "classic", Settings.data_path + "ship1.png", Settings.data_path + "ship2.png",6f);
	
	private String type;
	private String str;
	private String sprite1;
	private String sprite2;
	private float size;

	
	
	/**
	 * Carries default values for ship initialization.
	 * These can be changed in the ship after creation if desired.
	 * 
	 * @param	type		The two digit value representing the ship type
	 * @param	str			The string value of the ship type name
	 * @param	sprite1		Non-thruster ship sprite file path
	 * @param	sprite2		Thruster ship sprite file path
	 * @param	size		Size of the ship (Relative to what?)
	 */
	private ShipType(String type, String str, String sprite1, String sprite2,float s){
		this.type = type;
		this.str = str;
		this.sprite1 = sprite1;
		this.sprite2 = sprite2;
		this.size = s;
	}
	
	/**
	 * @see	Type#getTypeValue()
	 */
	public String getTypeValue(){
		return type;
	}
	
	/**
	 * Do not use.  Ship has two sprites, which must be returned separately.
	 * Possibly change 'getSpritePath' method to return String[] array instead?
	 * 
	 * @see	Type#getSpritePath()
	 */
	@Deprecated
	public String getSpritePath() {
		return null;
	}
	
	/**
	 * Gets the path to the standard ship sprite, without thrusters
	 * 
	 * @return	File path to sprite
	 */
	public String getSprite1Path(){
		return sprite1;
	}
	
	/**
	 * Gets the path to the ship sprite with thrusters
	 * 
	 * @return	File path to sprite
	 */
	public String getSprite2Path(){
		return sprite2;
	}
	
	public float getSize(){
		return this.size;
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
