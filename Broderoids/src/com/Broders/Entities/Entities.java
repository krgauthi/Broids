package com.Broders.Entities;

import com.Broders.Logic.Pos;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Entities {
	
	private String identity;
	private EntityType type;
	private Body body;
	
	
	public Entities(String id, EntityType type) {
		identity = id;
		Pos xy = new Pos(.5f,.5f);
		body.getPosition().set(xy.Getx(),xy.Gety());
	}

	public Body getBody() {
		return this.body;
	}
	
	public void SetPos(Pos xy){
		body.getPosition().set(xy.Getx(),xy.Gety());
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
