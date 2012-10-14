package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Pos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class Ship extends Entities{	
	
	private Boolean thrust;
	
	public Ship(String id, EntityType type, World world) {		
		super(id, type, world);
		
		float xx = Gdx.graphics.getWidth();
		float yy = Gdx.graphics.getHeight();
		
		this.thrust = false;
		super.setSprite("data/Test.png");
		super.getSprite().setScale(.05f, .05f);
		super.SetPos(new Pos(xx*.5f, yy*.5f));
		super.getSprite().setColor(Color.MAGENTA);
		//super.getBody().getPosition().set(0, 0);
		//super.getBody().setTransform(0, 0, 0);
		super.getBody().getAngle();
	}
	
	public Boolean getThrust(){
		return this.thrust;
	}

	@Override
	public void Draw(SpriteBatch sb, CoreLogic cl) {
		
		float screenWidth =  Gdx.graphics.getWidth();
		float screenHeight =  Gdx.graphics.getHeight();
		
		float x = super.getBody().getPosition().x;
		float y = super.getBody().getPosition().y;
		
		cl.getWidth();
		
		float posX;
		float posY;
		
		posX = screenWidth*(x/cl.getWidth());
		posY =  screenHeight*(y/cl.getHeight());
		
		super.getSprite().setPosition(posX, posY);
		
		super.getSprite().setRotation((float)super.getAngle());
		
		super.getSprite().draw(sb);
	}
	
}
