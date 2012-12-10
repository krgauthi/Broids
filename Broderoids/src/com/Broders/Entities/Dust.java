package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Dust extends Entity{
	
	private float age;
	private static float deathTime = 0.3f;
	
	public Dust(String id, Player owner, float dir, float x, float y, float vel, Color color) {

		super(id, owner);

		// sprite
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		super.setSize((float) Math.random() * 6 + 7f);
		super.setColor();

		super.setSprite("bullet");
		super.getSprite().setOrigin((meter * this.getSize()) / 2,
				(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(),
				meter * this.getSize());
		//Trying to add a bit of variety to the color scheme.
		super.setColor(color);

		//BodyDef
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.linearDamping = 0.8f;
		bodDef.position.set(x, y);
		bodDef.angle = dir;
		bodDef.allowSleep = false;

		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.0f);
		fixDef.shape = shape;
		fixDef.density = 0f;

		super.createBody(bodDef, fixDef);

		vel = (float) ((vel*0.85f) + Math.random()*(vel*0.3f));
		
		// Set the velocity
		float vX = (float) (vel * Math.cos(Math.toRadians(dir)));
		float vY = (float) (vel * Math.sin(Math.toRadians(dir)));
		super.body.setLinearVelocity(vX, vY);

		//Set type data
		super.getBody().setUserData(this);
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
		
	}
}
