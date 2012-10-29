package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Asteroid extends Entity{

	public Asteroid(String id, EntityType type) {
		super(id, type);
		// TODO Write initialization for Asteroid body/sprite
		
		
		//sprite
				float meter = Gdx.graphics.getHeight()/CoreLogic.getHeight();			
				
				super.setSprite(type.getSubType().getSpritePath());
				super.getSprite().setOrigin((meter)/2, (meter)/2);
				super.getSprite().setSize(meter, meter);
				super.getSprite().setColor(Color.WHITE);
	}

	@Override
	public void Draw(SpriteBatch sb) {
		// TODO Kris - Implement this draw method
		float screenWidth =  Gdx.graphics.getWidth();
		float screenHeight =  Gdx.graphics.getHeight();

		float x = super.getBody().getPosition().x;
		float y = super.getBody().getPosition().y;

		float posX;
		float posY;

		//this will only work for single player
		posX = screenWidth*(x/CoreLogic.getWidth());
		posY =  screenHeight*(y/CoreLogic.getHeight());

			super.getSprite().setPosition(posX, posY);
			super.getSprite().setRotation(super.getBody().getAngle());
			super.getSprite().draw(sb);
	
	}

}
