package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Settings;
import com.Broders.Logic.Pos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Bullet extends Entity{
	
	//private Pos pos;
	//private float dir;
	private float age;
	private static float deathTime = 3f;

	public Bullet(String type, float dir, float x, float y) {
		super(type);
		super.setEnt("bullet");
		
		//FixtureDef & shape
		FixtureDef fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.1f, 0.1f, new Vector2(0f,0f), 0f);
		fixDef.shape = shape;
		fixDef.density = 1f;

		//BodyDef
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.KinematicBody;
		bodDef.linearDamping = 0.0f;
		bodDef.position.set(x, y);
		bodDef.angle = dir;
		bodDef.allowSleep = false;
		
		//Create the body
		super.createBody(bodDef, fixDef);
		super.getBody().setBullet(true);
		
		//Set the velocity
		float vX = (float) (20*Math.sin(dir));
		float vY = (float) (-20*Math.cos(dir));
		super.body.setLinearVelocity(vX, vY);
		//sprite
		float meter = Gdx.graphics.getHeight()/CoreLogic.getHeightScreen();			
		
		super.setSize(3.5f);
		
		super.setSprite(Settings.data_path + "bullet.png");
		super.getSprite().setOrigin((meter*this.getSize())/2, (meter*this.getSize())/2);
		super.getSprite().setSize(meter*this.getSize(), meter*this.getSize());
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

	@Override
	public void update() {
		age += Gdx.graphics.getDeltaTime();
		
		if (age >= deathTime) {
			CoreLogic.removeEntity(this);
		}
		
	}

	@Override
	public void destroy() {
		CoreLogic.removeEntity(this);
		
	}

}
