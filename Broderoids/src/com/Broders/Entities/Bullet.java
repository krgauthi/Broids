package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * This is the Bullet class, which is an extension of Entity. Bullets are created
 * by the input of the player, and owned by the client. When the player presses
 * the "shoot" button, a Bullet is created and moves forward in the direction the
 * Ship is pointing. When the Bullet collides with an Asteroid or a Ship (in
 * multiplayer), both that Entity and the Bullet are destroyed. Bullets have a
 * limited lifespan, they will destroy themselves after a certain amount of time.
 * 
 * @author rdbaumei 
 * @author ejrinkus 
 * @author krgauthi
 *
 */
public class Bullet extends Entity {

	//The current age, in seconds, of this Bullet.
	private float age;
	//The maximum length of time, in seconds, that this Bullet can exist before
	//it destroys itself.
	private static float deathTime = 1.0f;

	/**
	 * Initializes the superclass Entity with the "bullet" type, then
	 * creates a Body Definition object at front end of the Ship
	 * and a Fixture Definition object with certain physical properties.
	 * A new body is created from those definitions.
	 * 
	 * For now, the type passed in should be "bullet". If we implement multiple
	 * kinds of Bullets, this will change.
	 * 
	 * @param id
	 *            Used to uniquely identify this Entity
	 * @param type
	 *            The type of this Entity
	 */
	public Bullet(String type, float dir, Vector2 velocity, float x, float y) {

		super(type);
		super.setEnt("bullet");

		//Setting scaling information for the sprite
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();
		super.setSize(6f);

		//Getting the sprite and setting it to the center of the Body.
		super.setSprite(Settings.data_path + "bullet.png");
		super.getSprite().setOrigin((meter * this.getSize()) / 2,
				(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(),
				meter * this.getSize());
		super.getSprite().setColor(CoreLogic.getGame().playerColor);

		//Defining how the Body interacts with the physics engine.
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.KinematicBody;
		bodDef.linearDamping = 0.0f;
		bodDef.allowSleep = false;
		
		//Setting the orientation of the Body
		bodDef.position.set(x, y);
		bodDef.angle = dir;

		//Defining the physical properties of the Bullet.
		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f);
		fixDef.shape = shape;
		fixDef.density = 0f;

		super.createBody(bodDef, fixDef);

		//Setting the velocity of the Bullet.
		float vX = (float) (velocity.x + 75 * Math.cos(Math.toRadians(dir)));
		float vY = (float) (velocity.y + 75 * Math.sin(Math.toRadians(dir)));
		super.body.setLinearVelocity(vX, vY);

		//Allows the Entity to be referenced from the Body.
		super.getBody().setUserData(this);
	}

	/**
	 * Overridden from Entity. In order to make the Bullets die after a certain
	 * amount of time, this method gets called every iteration of the game. If the
	 * Bullet has been on the screen for a certain amount of time, it removes itself.
	 */
	@Override
	public void update() {
		//Continuously sums the time since the last frame.
		age += Gdx.graphics.getDeltaTime();

		//If the Bullet has reached its age limit, it gets itself removed from the game.
		if (age >= deathTime) {
			CoreLogic.resetBonus();
			CoreLogic.removeEntity(this);
		}
	}

	/**
	 * Overridden from Entity. If any code needs to execute upon destruction of
	 * the Ship, the required code will go here. This is currently unnecessary for
	 * Bullets, so the method is empy.
	 */
	@Override
	public void destroy() {
		
	}
}
