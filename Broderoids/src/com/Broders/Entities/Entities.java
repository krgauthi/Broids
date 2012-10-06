package com.Broders.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Entities {
	
	private String identity;
	private EntityType type;
	private Body body;
	
	
	public Entities(String id, EntityType type) {
		identity = id;
	}

	public Body getBody() {
		return this.body;
	}
	
	public String getIdentity() {
		return this.identity;
	}
	
	public EntityType getType() {
		return this.type;
	}
	
	
	public double getAngle(){
		return this.body.getAngle()*(180.00/Math.PI);
	}
}
