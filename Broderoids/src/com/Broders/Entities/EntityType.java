package com.Broders.Entities;

/**
 * This enum will be used to hold different Entity types and their corresponding default values for initialization.
 * 
 * @author ntpeters
 *
 */
public enum EntityType {
	SHIP ("00", ShipType.CLASSIC),
	BULLET("01", BulletType.BASIC),
	ASTEROID ("02", AsteroidType.LARGE);
	
	private String id;
	private Type type;
	
	/**
	 * Initializes a new EntityType with a given subType and type ID.
	 * SubTypes should not be not be changed after Entity initialization.
	 * 
	 * @param	id		The ID of this type
	 * @param 	type	The subType of this type
	 */
	private EntityType(String id, Type type){
		this.id = id;
		this.type = type;
	}
	
	/**
	 * Gets the subType of this type.
	 * 
	 * Example:
	 * Type = SHIP
	 * SubType = ShipType.CLASSIC
	 * 
	 * @return	The subType of this Type
	 */
	public Type getSubType(){
		return type;
	}
	
	/**
	 * Returns the numeric value representing this type
	 * Example: Type + SubType => Ship + ShipType.CLASSIC => 0000
	 * 
	 * @return	The the type ID
	 */
	public String toString(){
		return this.id + type.getTypeValue();
	}
	
	/**
	 * Determines the equality between this Type and the given Type
	 * 
	 * @param	type	The Type to compare against
	 * @return			True if Types are the same, false otherwise
	 */
	public boolean equals(EntityType type){
		return this.toString().equals(type.toString());
	}
}