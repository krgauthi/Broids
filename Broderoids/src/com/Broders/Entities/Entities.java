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
		body = world.createBody(bd);

		
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
	public void setBody(Body bod){
		this.body = bod;
	}
	
	public abstract void Draw(SpriteBatch sb, CoreLogic cl);
}
