package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Bullet extends Entity {

	// private Pos pos;
	// private float dir;
	private float age;
	private static float deathTime = 0.5f;

	private BaseGame game;

	public Bullet(String id, Player owner, float dir, float x, float y) {
		super(id, owner);
		this.game = owner.getGame();
		// sprite
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		super.setSize(6f);
		super.setColor();

		super.setSprite("bullet");
		super.getSprite().setOrigin((meter * this.getSize()) / 2,
				(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(),
				meter * this.getSize());
		super.getSprite().setColor(super.getColor());

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
		float vX = (float) (70 * Math.cos(Math.toRadians(dir)) +
				super.getOwner().getShip().getLinearVelocity().x);
		float vY = (float) (70 * Math.sin(Math.toRadians(dir)) +
				super.getOwner().getShip().getLinearVelocity().y);
		super.body.setLinearVelocity(vX, vY);

		//Set type data
		super.getBody().setUserData(this);
		
		SoundManager.play("pew", 1f, (float) (0.9f + Math.random() * 0.2));
	}

	

	@Override
	public void update() {
		age += Gdx.graphics.getDeltaTime();

		if (age >= deathTime) {
			CoreLogic.removeEntity(this);
			super.owner.modBonus(1.0f);
		}

	}

	@Override
	public void destroy() {
		
	}
}
