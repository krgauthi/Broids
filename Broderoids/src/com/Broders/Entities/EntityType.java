package com.Broders.Entities;

public enum EntityType {
	SHIP (0),
	BULLET (1),
	ASTEROID (2);
	
	private int type;
	
	EntityType(int type){
		this.type = type;
	}
}
