package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.Broders.Logic.Settings;
import com.Broders.Logic.Pos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Bullet extends Entity {

	// private Pos pos;
	// private float dir;
	private float age;
	private static float deathTime = 1.0f;


	public Bullet(String type, String id, Player owner, float dir, float x, float y) {
		super(type, id, owner);
		super.setEnt("bullet");

		// sprite
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		super.setSize(6f);

		super.setSprite(Settings.data_path + "bullet.png");
		super.getSprite().setOrigin((meter * this.getSize()) / 2,
				(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(),
				meter * this.getSize());
		super.getSprite().setColor(CoreLogic.getGame().playerColor);

		//BodyDef
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.KinematicBody;
		bodDef.linearDamping = 0.0f;
		bodDef.position.set(x, y);
		bodDef.angle = dir;
		bodDef.allowSleep = false;

		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f);
		fixDef.shape = shape;
		fixDef.density = 0f;

		super.createBody(bodDef, fixDef);

		// Set the velocity
		float vX = (float) (37.5 * Math.cos(Math.toRadians(dir)));
		float vY = (float) (37.5 * Math.sin(Math.toRadians(dir)));
		super.body.setLinearVelocity(vX, vY);

		//Set type data
		super.setPlayer(player);
		super.getBody().setUserData(this);
	}

	

	@Override
	public void update() {
		age += Gdx.graphics.getDeltaTime();

		if (age >= deathTime) {
			CoreLogic.removeEntity(this);
			super.belongsTo().modBonus(1.0f);
		}

	}

	@Override
	public void destroy() {
		
	}
}
