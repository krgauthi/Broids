package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Pos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity {
	
	private String identity;
	private EntityType type;
	private Body body;
	private Sprite sprite;
	
	//save these for teleportation
	private BodyDef bodDef;
	private FixtureDef fixDef;
	
	public Entity(String id, EntityType type) {
		this.identity = type.toString() + id;
		this.type = type;
	}

	public Body getBody() {
		return this.body;
	}
	
	protected void createBody(BodyDef bodDef, FixtureDef fixDef){
		this.bodDef = bodDef;
		this.fixDef = fixDef;
		this.body = CoreLogic.getWorld().createBody(bodDef);
		this.body.createFixture(fixDef);
	}
	
	public String getIdentity() {
		return this.identity;
	}
	
	public EntityType getType() {
		return this.type;
	}
	
	public float getAngle(){
		return (float) (this.body.getAngle()*(180.00f/Math.PI));
	}
	
	public Sprite getSprite(){
		return this.sprite;
	}
	
	protected void setSprite(String sp){
		Texture texture = new Texture(Gdx.files.internal(sp));
		this.sprite = new Sprite(texture);
	}

	public abstract void Draw(SpriteBatch sb);
	
	public String toString(){
		return this.identity;
	}
	
	public boolean equals(Entity entity){
		return entity.toString().equals(this.identity);
	}
	
	public void teleport(float x, float y){
		Vector2 linV = this.body.getLinearVelocity();
		float angV = this.body.getAngularVelocity();
		float angle = this.body.getAngle();
		
		CoreLogic.getWorld().destroyBody(this.body);
		
		this.bodDef.position.set(x, y);
		this.bodDef.angle = angle;
		this.body = CoreLogic.getWorld().createBody(bodDef);
		this.body.createFixture(this.fixDef);
		this.body.setAngularVelocity(angV);
		this.body.setLinearVelocity(linV);
	}
}
