package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Pos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class Ship extends Entities{	

	private Boolean thrust;

	private Texture texture;
	private Sprite sprite;


	public Ship(String id, EntityType type, World world) {		
		super(id, type, world);

		float xx = Gdx.graphics.getWidth();
		float yy = Gdx.graphics.getHeight();
		
		float meter = Gdx.graphics.getHeight()/90;			//TODO ref from core

		this.texture = new Texture(Gdx.files.internal("data/ship2.png"));
		
		this.sprite = new Sprite(this.texture);
		this.sprite.flip(false, true);
		this.sprite.setOrigin((meter*6)/2, (meter*6)/2);
		this.sprite.setSize(meter*6, meter*6);
		
		this.sprite.setColor(Color.MAGENTA);
		

		this.thrust = false;
		super.setSprite("data/ship1.png");
		super.getSprite().flip(false, true);
		//super.getSprite().setScale((meter*5)/yy);	
		super.getSprite().setOrigin((meter*6)/2, (meter*6)/2);
		super.getSprite().setSize(meter*6, meter*6);

		
		
	
		super.getSprite().setColor(Color.MAGENTA);
		super.getBody().getAngle();
	}

	public float getX(){
		return this.getBody().getPosition().x;
	}
	
	public float getY(){
		return this.getBody().getPosition().y;
	}
	
	public Boolean getThrust(){
		return this.thrust;
	}
	
	public void setThrust(boolean bool){
		this.thrust = bool;
	}

	@Override
	public void Draw(SpriteBatch sb, CoreLogic cl) {

		float screenWidth =  Gdx.graphics.getWidth();
		float screenHeight =  Gdx.graphics.getHeight();

		float x = super.getBody().getPosition().x;
		float y = super.getBody().getPosition().y;

		

		float posX;
		float posY;

		//this will only work for single player
		posX = screenWidth*(x/cl.getWidth());
		posY =  screenHeight*(y/cl.getHeight());


		if(this.getThrust()){
			this.sprite.setPosition(posX, posY);
			this.sprite.setRotation((float)super.getAngle());
			this.sprite.draw(sb);
		}else{
			super.getSprite().setPosition(posX, posY);
			super.getSprite().setRotation((float)super.getAngle());
			super.getSprite().draw(sb);
		}
	}

}
