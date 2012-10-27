package com.Broders.Entities;

public enum EntityType {
	SHIP ("00", ShipType.CLASSIC),
	BULLET("01", BulletType.BASIC),
	ASTEROID ("02", AsteroidType.LARGE);
	
	private String id;
	private Type type;
	
	private EntityType(String id, Type type){
		this.id = id;
		this.type = type;
	}
	
	public String toString(){
		return this.id + type.toString();
	}
	
	public boolean equals(EntityType type){
		return this.toString().equals(type.toString());
	}
}