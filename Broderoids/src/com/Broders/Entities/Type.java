package com.Broders.Entities;

/**
 * All Type enums will inherit from this interface
 *  
 * @author	ntpeters
 *
 */
public interface Type {

	/**
	 * Returns the numeric value of this type as a String
	 * 
	 * @return	A two digit number as a String
	 */
	public String getTypeValue();
	
	/**
	 * Returns the file path to the sprite for this Type
	 * 
	 * @return	File Path to the sprite
	 */
	public String getSpritePath();
	
	/**
	 * Returns the name of this Type as a String
	 * 
	 * @return	The Type name
	 */
	public String toString();
	
	/**
	 * Determines the equality between this Type and the given Type
	 * 
	 * @param	type	Type to compare against
	 * @return			True if types are the same, false otherwise
	 */
	public boolean equals(Type type);
		
}
