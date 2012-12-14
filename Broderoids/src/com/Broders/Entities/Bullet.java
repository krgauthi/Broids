package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Inherited from the Entity class.
 * Bullets get spawned at the tip of the ship when the "shoot" button is pressed. They
 * move in a straight line at a uniform velocity. After a set amount of time, they
 * disappear. Hitting anything but other Bullets or Dust will cause the bullet to
 * disappear. Asteroids will be destroyed, and ships owned by other players will
 * take damage.
 */
public class Bullet extends Entity {

	// The current age of the bullet.
	private float age; 
	// How old the bullet will be before it is destroyed.
	private static float deathTime = 0.7f; 

	/**
	 * Constructor for the Bullet class. Defines the Body and its
	 * Fixtures for the physics World. Also defines the Sprite to be used
	 * by the Ship.
	 * 
	 * @param id
	 *            Entity id. Get this from the owner.
	 * @param owner
	 *            Entity owner, should be Local Player.
	 * @param dir
	 *            Initial direction
	 * @param x
	 *            Initial X position
	 * @param y
	 *            Initial Y position
	 */
	public Bullet(String id, Player owner, float dir, float x, float y) {
		
		// Setting unique ID and the player that owns the Bullet.
		super(id, owner);
		
		// Setting the image that shows up on screen.
		super.setSprite("bullet");
		
		// Setting the color of the image.
		super.setColor();
		
		// Setting the size of the image. Does not affect physical properties.
		super.setSize(6f);
		
		// Defines the ratio between the game and screen size. Used for graphics.
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		// Setting the properties of the Sprite to be displayed.
		super.getSprite().setOrigin((meter * this.getSize()) / 2, (meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(), meter * this.getSize());
		super.getSprite().setColor(super.getColor());

		// Defining the Body and its physical properties.
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.KinematicBody;
		bodDef.linearDamping = 0.0f;
		bodDef.position.set(x, y);
		bodDef.angle = dir;
		bodDef.allowSleep = false;

		// Define the Fixture, its shape, and their physical properties.
		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f);
		fixDef.shape = shape;
		fixDef.density = 0f;

		// Giving both Definitions to the World to create and track.
		super.createBody(bodDef, fixDef);

		// Setting the x and y velocities of the Bullet.
		float vX = (float) (70 * Math.cos(Math.toRadians(dir)) + super.getOwner().getShip().getLinearVelocity().x);
		float vY = (float) (70 * Math.sin(Math.toRadians(dir)) + super.getOwner().getShip().getLinearVelocity().y);
		super.body.setLinearVelocity(vX, vY);

		// Allowing the Body to reference the Entity.
		super.getBody().setUserData(this);
		
		// Pew-Pew!
		SoundManager.play("pew", 1f, (float) (0.85f + Math.random() * 0.3));
	}

	/**
	 * Overridden from Entity class.
	 * This gets called every game loop. It keeps track of the age of the bullet,
	 * and destroys it if it gets too old.
	 */
	@Override
	public void update() {
		// Adding the time between updates to the age.
		age += Gdx.graphics.getDeltaTime();

		// If the Bullet dies of old age, it is removed from the Map of Entities.
		if (age >= deathTime) {
			if (!CoreLogic.multiplayer || this.getOwner() == CoreLogic.getLocal()) {
				CoreLogic.removeEntity(this);
			}
			// You lose your bonus multiplier if you miss an asteroid.
			super.owner.modBonus(1.0f);
		}
	}

	/**
	 * Overridden from Entity class. Unused.
	 */
	@Override
	public void destroy() {

	}
}
