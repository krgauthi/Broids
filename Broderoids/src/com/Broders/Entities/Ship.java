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
		this.sprite.setScale(.05f, .05f);
		this.sprite.setColor(Color.MAGENTA);
		

		this.thrust = false;
		super.setSprite("data/ship1.png");
		super.getSprite().flip(false, true);
		super.getSprite().setScale(.05f, .05f);		
		super.getSprite().setSize(meter*5, meter*5);
		//super.SetPos(new Pos(xx*.5f, yy*.5f));				//NO
	
		super.getSprite().setColor(Color.MAGENTA);
		//super.getBody().getPosition().set(0, 0);
		//super.getBody().setTransform(0, 0, 0);
		super.getBody().getAngle();
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
