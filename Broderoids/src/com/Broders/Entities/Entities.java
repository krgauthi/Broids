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
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entities {
	
	private String identity;
	private EntityType type;
	private Body body;
	private Texture texture;
	private Sprite sprite;
	
	
	public Entities(String id, EntityType type, World world) {
		identity = id;
		this.type = type;	


		BodyDef bd = new BodyDef();
		Pos xy = new Pos(.5f,.5f); //default center of screen
		bd.position.set(xy.Getx(), xy.Gety());
		body = world.createBody(bd);
		texture = new Texture(Gdx.files.internal("data/ship1.png"));
		
		//float x = ((float)Gdx.graphics.getWidth());
		//float y = ((float)Gdx.graphics.getHeight());
		sprite = new Sprite(texture,512,200);

		//this.SetPos(xy); Set the initial position with the bodydefinition
	}

	public Body getBody() {
		return this.body;
	}
	
	//I think this method is unnecessary with the physics engine.
	//You only need to set the position on initialization.
	public void SetPos(Pos xy){
		body.setTransform(xy.Getx(), xy.Gety(), body.getAngle()); //Sets the new position of the entity
		sprite.setPosition(xy.Getx(), xy.Gety());
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
	
	protected void setSprite(String sp){
		this.texture = new Texture(Gdx.files.internal(sp));
		this.sprite = new Sprite(this.texture);
	}
	
	public Sprite getSprite(){
		return this.sprite;
	}
	
	// possibly only for testing...
	// Yeah, I don't think this is needed. -R
	public void setBody(Body body){
		this.body = body;
	}
	
	public abstract void Draw(SpriteBatch sb, CoreLogic cl);
}
