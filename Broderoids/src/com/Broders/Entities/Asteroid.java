package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Asteroid extends Entity{

	public Asteroid(String type, float x, float y) {
		super(type);
		super.setEnt("asteroid");

		super.setSize(15.0f);
		super.setColor(Color.YELLOW);
		
		// TODO Write initialization for Asteroid body/sprite

		
		// if we implement separate files just uncomment the lines above and comment this line out
		super.setSprite(Settings.data_path + "broid.png");
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(6f,6f);
		//shape.set(vertices);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 0.5f;
		fixDef.restitution = 1;

		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.angularDamping = 0.0f;
		bodDef.linearDamping = 0.0f;

		bodDef.position.set(x, y);
		bodDef.angle = (float) (MathUtils.PI * Math.random());
		bodDef.allowSleep = false;
		super.createBody(bodDef, fixDef);

		//sprite
		float meter = Gdx.graphics.getHeight()/CoreLogic.getHeightScreen();			

		super.getSprite().setOrigin(meter*(this.getSize()/2), meter*(this.getSize()/2)); 
		super.getSprite().setSize(meter * this.getSize(), meter * this.getSize());
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
		super.getSprite().setRotation((float)super.getAngle());
		super.getSprite().draw(sb);

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		CoreLogic.removeEntity(this);
	}

}
