package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Inherited from the Entity class.
 * Dust is an all-purpose particle effect. It gets spawned when Asteroids and Ships
 * are destroyed, and from Ships when they have taken a lot of damage. All Dust entities
 * are owned by the Scratch player.
 */
public class Dust extends Entity{
	
	// The current age of the Dust
	private float age;
	// The age the dust will be when it dies.
	private static float deathTime = 0.3f;
	
	/**
	 * Constructor for the Dust entity. Defines the Body and its
	 * Fixtures for the physics World. Also defines the Sprite to be used
	 * by the Dust. This is just about all the code there is to it.
	 * 
	 * @param The entity's unique ID. Get this from the Scratch Player.
	 * @param The player that owns the Asteroid (should be the Host).
	 * @param The direction the Dust will move.
	 * @param The x coordinate it will spawn from.
	 * @param The y coordinate it will spawn from.
	 * @param How fast the Dust will move initially.
	 * @param The color of the Dust.
	 */
	public Dust(String id, Player owner, float dir, float x, float y, float vel, Color color) {

		// Setting the unique ID and owner of the Dust. The owner should be Scratch.
		super(id, owner);

		// Defines the ratio between the game and screen size. Used for graphics.
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		// Setting the image that will be shown on screen.
		super.setSprite("bullet");
		
		// Setting the color of the image;
		super.setSize((float) Math.random() * 6 + 7f);
		
		// Setting the color of the image;
		super.setColor();

		// Setting properties of the Sprite that will be displayed.
		super.getSprite().setOrigin((meter * this.getSize()) / 2,
				(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(),
				meter * this.getSize());
		super.setColor(color);

		// Defining the Body and its physical properties.
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.linearDamping = 0.8f;
		bodDef.position.set(x, y);
		bodDef.angle = dir;
		bodDef.allowSleep = false;

		// Defining the Fixture, Shape, and their physical properties.
		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.0f);
		fixDef.shape = shape;
		fixDef.density = 0f;

		// Creating the Body with the Definitions, and adds it to the world.
		super.createBody(bodDef, fixDef);

		// Adds a bit of variety to the velocity.
		vel = (float) ((vel*0.85f) + Math.random()*(vel*0.3f));
		
		// Sets the velocity in terms of x and y components.
		float vX = (float) (vel * Math.cos(Math.toRadians(dir)));
		float vY = (float) (vel * Math.sin(Math.toRadians(dir)));
		super.body.setLinearVelocity(vX, vY);

		// Allows the Body to reference the Entity.
		super.getBody().setUserData(this);
	}

	/**
	 * Overridden from Entity.
	 * This is called every game loop. The only thing it handles in this case
	 * is the lifetime of the Dust.
	 */
	@Override
	public void update() {
		
		// Adding the time between updates to the age.
		age += Gdx.graphics.getDeltaTime();

		// This kills the Dust after the set amount of time.
		if (age >= deathTime) {
			CoreLogic.removeEntity(this);
		}
	}

	/**
	 * Overridden from Entity class. Unused.
	 */
	@Override
	public void destroy() {
		
	}
}
