package com.Broders.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
		
		float xx = Gdx.graphics.getWidth();
		float yy = Gdx.graphics.getHeight();
		
		this.thrust = false;
		ShipTex = new Texture(Gdx.files.internal("data/Test.png"));
		ShipSprite = new Sprite(ShipTex);
		ShipSprite.setScale(.05f, .05f);
		
		//setposition should be handled by the body pos?
		ShipSprite.setPosition(xx*.5f, (yy*.5f));
		ShipSprite.setColor(Color.MAGENTA);
		//
	
	}
	
	public Boolean getThrust(){
		return this.thrust;
	}
	
	public void Draw(SpriteBatch sb){
		ShipSprite.draw(sb);
	}
}
