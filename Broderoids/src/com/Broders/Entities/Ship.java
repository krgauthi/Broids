package com.Broders.Entities;

import com.badlogic.gdx.physics.box2d.Body;

public class Ship extends Entities{
	private Boolean thrust;
	
	public Ship(String id, EntityType type) {
		super(id, type);
		this.thrust = false;
	}
	
	public Boolean getThrust(){
		return this.thrust;
	}
}
