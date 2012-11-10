package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet extends Entity{

	public Bullet(String type) {
		super(type);
		// TODO Write initialization for Bullet body/sprite
		
		super.setEnt("bullet");
		//sprite
		float meter = Gdx.graphics.getHeight()/CoreLogic.getHeight();			
		
		super.setSprite(Settings.data_path + "bullet.png");
		super.getSprite().setOrigin((meter)/2, (meter)/2);
		super.getSprite().setSize(meter, meter);
		super.getSprite().setColor(Color.WHITE);

		
		
	}

	@Override
	public void Draw(SpriteBatch sb) {
		float screenWidth =  Gdx.graphics.getWidth();
		float screenHeight =  Gdx.graphics.getHeight();

		float x = super.getBody().getPosition().x - (this.getSize()/2f);
		float y = super.getBody().getPosition().y - (this.getSize()/2f);

		float posX;
		float posY;

	
		posX = screenWidth*((x-CoreLogic.getViewPortX())/CoreLogic.getWidthScreen());
		posY =  screenHeight*((y-CoreLogic.getViewPortY())/CoreLogic.getHeightScreen());


		super.getSprite().setPosition(posX, posY);
		super.getSprite().setRotation(super.getBody().getAngle());
		super.getSprite().draw(sb);
		
	}

}
