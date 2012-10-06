package com.Broders.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class Ship extends Entities{
	
	private Texture ShipTex;
	
	private Sprite ShipSprite;
	
	
	private Boolean thrust;
	
	public Ship(String id, EntityType type) {
		super(id, type);
		this.thrust = false;
		ShipTex = new Texture(Gdx.files.internal("data/Test.png"));
		ShipSprite = new Sprite(ShipTex);
	}
	
	public Boolean getThrust(){
		return this.thrust;
	}
	
	public void Draw(SpriteBatch sb){
		ShipSprite.draw(sb);
	}
}
