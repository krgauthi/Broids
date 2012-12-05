package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Dust extends Entity{
	
	private float age;
	private static float deathTime = 0.3f;
	
	public Dust(String id, Player owner, float dir, float x, float y) {

		super(id, owner);

		// sprite
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		super.setSize(10f);
		super.setColor();

		super.setSprite(Settings.data_path + "bullet.png");
		super.getSprite().setOrigin((meter * this.getSize()) / 2,
				(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(),
				meter * this.getSize());
		super.getSprite().setColor(super.getColor());

		//BodyDef
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.linearDamping = 0.0f;
		bodDef.position.set(x, y);
		bodDef.angle = dir;
		bodDef.allowSleep = false;

		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.0f);
		fixDef.shape = shape;
		fixDef.density = 0f;

		super.createBody(bodDef, fixDef);

		// Set the velocity
		float vX = (float) (30 * Math.cos(Math.toRadians(dir)));
		float vY = (float) (30 * Math.sin(Math.toRadians(dir)));
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
		// TODO Auto-generated method stub
		
	}
}
