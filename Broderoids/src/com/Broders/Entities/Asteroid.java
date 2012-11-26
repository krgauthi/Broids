package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * This is the Asteroid(s) class, which extends Entity. Asteroids are created
 * by CoreLogic and owned by the server (in multiplayer). Asteroids float in
 * space and present both a target and a hazard to the ship. There are currently
 * three different sizes of Asteroid: small, medium, and large. Large asteroids
 * are spawned initially, but being hit by a Bullet will cause it to be destroyed
 * and spawn two Asteroids of the next size down (if there is one).
 * 
 * @author rdbaumei 
 * @author ejrinkus 
 * @author krgauthi
 *
 */
public class Asteroid extends Entity {

	//Enumeration of the Asteroid sizes
	public static final int LARGE = 0;
	public static final int MEDIUM = 1;
	public static final int SMALL = 2;

	//Holds the size of the current Asteroid.
	private int type;

	/**
	 * Initializes the superclass Entity with the "asteroid" type, then
	 * creates a Body Definition object at whatever location is applicable
	 * and a Fixture Definition object with certain physical properties.
	 * A new body is created from those definitions.
	 * 
	 * The type passed in should be "small", "medium", or "large", depending
	 * upon the required size.
	 * 
	 * @param id
	 *            Used to uniquely identify this Entity
	 * @param type
	 *            The type of this Entity, specifically its size.
	 * @param c
	 * 			  The color of the Asteroid.
	 */
	public Asteroid(String type, Color c, float x, float y) {
		//Sets the Entity types.
		super(type);
		this.setEnt("asteroid");

		//Sets visual properties of the Asteroid.
		super.setColor(c);
		super.setSprite(Settings.data_path + "broid.png");

		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();

		//These physical properties of the Asteroid are dependent upon
		//its size.
		if (type.equals("small")) {
			this.type = SMALL;
			super.setSize(3.75f);
			shape.setRadius(1.5f);
			fixDef.density = 0.125f;
		} else if (type.equals("medium")) {
			this.type = MEDIUM;
			super.setSize(7.5f);
			shape.setRadius(3f);
			fixDef.density = 0.5f;
		} else {
			this.type = LARGE;
			super.setSize(15.0f);
			shape.setRadius(6f);
			fixDef.density = 2.0f;
		}

		fixDef.shape = shape;
		fixDef.restitution = 1f;

		//Setting how the Body interacts with the physics engine
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.angularDamping = 0.0f;
		bodDef.linearDamping = 0.0f;
		bodDef.allowSleep = false;

		//Setting position, given by the parameters, and direction.
		bodDef.position.set(x, y);
		bodDef.angle = (float) (MathUtils.PI * Math.random());

		super.createBody(bodDef, fixDef);

		//Getting the value that is used to scale the sprite.
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		//Setting the position of the sprite with respect to the body and
		//size of the Asteroid.
		super.getSprite().setOrigin(meter * (this.getSize() / 2),
				meter * (this.getSize() / 2));
		super.getSprite().setSize(meter * this.getSize(),
				meter * this.getSize());
		super.getSprite().setColor(this.getColor());

		//Allowing the Asteroid to be referenced by the Body.
		super.getBody().setUserData(this);
	}

	/**
	 * Overridden from Entity. If any information about the Asteroid needs to change
	 * with time, the required code will go here. Asteroids are pretty inert, so this
	 * is empty.
	 */
	@Override
	public void update() {

	}

	/**
	 * Overridden from Entity. When the Asteroid is it by a Bullet, this method gets
	 * called. For large and medium Asteroids, it saves the physical information, then
	 * creates two new Asteroids of the next size smaller where this Asteroid used to be.
	 * 
	 */
	@Override
	public void destroy() {

		//If this is a small Asteroid, then skip the rest of this method.
		if (this.type == SMALL) {
			//Score logic, change limit of if conditional to change max multiplier
			if(CoreLogic.getBonus() >= 5){
				CoreLogic.setScore(20*5);
			}
			else{
				CoreLogic.setBonus(0.2f);
				CoreLogic.setScore(20*CoreLogic.getBonus());
			}
			return;
		}

		Asteroid roid1;
		Asteroid roid2;

		//These will have the coordinates for the new Asteroids
		float x1;
		float x2;
		float y1;
		float y2;
		float dir;

		//Setting the physical properties of the new Asteroids, depending
		//upon the size of this Asteroid.
		if (this.type == LARGE) {
			//Setting the positions of the new Asteroids.
			dir = (float) Math.toRadians(this.getAngle());
			x1 = (float) (this.getX() + 7.5 * Math.cos(dir));
			x2 = (float) (this.getX() + 7.5 * Math.cos(dir));
			y1 = (float) (this.getY() - 7.5 * Math.sin(dir));
			y2 = (float) (this.getY() - 7.5 * Math.sin(dir));

			//BEGIN DEFINITION: the physical properties of roid1
			roid1 = new Asteroid("medium", CoreLogic.getGame().gameColor, x1,
					y1);

			//Setting the initial force applied to roid1 to get it moving.
			float initForce = (float) (8000 + (4000 * Math.random()));
			float x = (float) (initForce * Math.cos(dir));
			float y = (float) (initForce * Math.sin(dir));

			//Applying the initial force to roid1
			Vector2 f = roid1.getBody().getWorldVector(new Vector2(x, y));
			Vector2 p = roid1.getBody().getWorldPoint(
					roid1.getBody().getLocalCenter());
			roid1.getBody().applyForce(f, p);

			//Giving it some spin
			float spin = (float) (4000 + (2000 * Math.random()));
			if (Math.random() >= 0.5f)
				spin *= -1;
			roid1.getBody().applyTorque(spin);

			//Giving the new Body to the physics engine.
			CoreLogic.getEntityMap().put(roid1.getId(), roid1);
			//END DEFINITION

			//BEGIN DEFINITION: the physical properties of roid2
			roid2 = new Asteroid("medium", CoreLogic.getGame().gameColor, x2,
					y2);
			//Setting the initial force applied to roid2 to get it moving.
			initForce = (float) (8000 + (2000 * Math.random()));
			x = (float) (initForce * Math.cos(dir));
			y = (float) (initForce * Math.sin(dir));

			//Applying the initial force to roid2
			f = roid2.getBody().getWorldVector(new Vector2(x, y));
			p = roid2.getBody().getWorldPoint(roid2.getBody().getLocalCenter());
			roid2.getBody().applyForce(f, p);

			//Giving it some spin
			spin = (float) (4000 + (2000 * Math.random()));
			if (Math.random() >= 0.5f)
				spin *= -1;
			roid2.getBody().applyTorque(spin);

			//Giving the new Body to the physics engine.
			CoreLogic.getEntityMap().put(roid2.getId(), roid2);
			//END DEFINITION
			
			//Score logic, change limit of if conditional to change max multiplier
			if(CoreLogic.getBonus() >= 5){
				CoreLogic.setScore(10*5);
			}
			else{
				CoreLogic.setBonus(0.2f);
				CoreLogic.setScore(10*CoreLogic.getBonus());
			}

		} else if (this.type == MEDIUM) {
			//Setting the positions of the new Asteroids.
			dir = (float) Math.toRadians(this.getAngle());
			x1 = (float) (this.getX() + 3.75 * Math.cos(dir));
			x2 = (float) (this.getX() + 3.75 * Math.cos(dir));
			y1 = (float) (this.getY() - 3.75 * Math.sin(dir));
			y2 = (float) (this.getY() - 3.75 * Math.sin(dir));

			//BEGIN DEFINITION: the physical properties of roid1
			roid1 = new Asteroid("small", CoreLogic.getGame().gameColor, x1, y1);

			//Setting the initial force applied to roid1 to get it moving.
			float initForce = (float) (1000 + (500 * Math.random()));
			float x = (float) (initForce * Math.cos(Math.random()*2*Math.PI));
			float y = (float) (initForce * Math.sin(Math.random()*2*Math.PI));

			//Applying the initial force to roid1
			Vector2 f = roid1.getBody().getWorldVector(new Vector2(x, y));
			Vector2 p = roid1.getBody().getWorldPoint(
					roid1.getBody().getLocalCenter());
			roid1.getBody().applyForce(f, p);

			//Giving it some spin
			float spin = (float) (100 + (50 * Math.random()));
			if (Math.random() >= 0.5f)
				spin *= -1;
			roid1.getBody().applyTorque(spin);

			//Giving the new Body to the physics engine.
			CoreLogic.getEntityMap().put(roid1.getId(), roid1);
			//END DEFINITION

			//BEGIN DEFINITION: the physical properties of roid2
			roid2 = new Asteroid("small", CoreLogic.getGame().gameColor, x2, y2);

			//Setting the initial force applied to roid2 to get it moving.
			initForce = (float) (1000 + (500 * Math.random()));
			x = (float) (initForce * Math.cos(dir));
			y = (float) (initForce * Math.sin(dir));

			//Applying the initial force to roid1
			f = roid2.getBody().getWorldVector(new Vector2(x, y));
			p = roid2.getBody().getWorldPoint(roid2.getBody().getLocalCenter());
			roid2.getBody().applyForce(f, p);

			//Giving it some spin
			spin = (float) (100 + (50 * Math.random()));
			if (Math.random() >= 0.5f)
				spin *= -1;
			roid2.getBody().applyTorque(spin);

			//Giving the new Body to the physics engine.
			CoreLogic.getEntityMap().put(roid2.getId(), roid2);
			//END DEFINITION
			
			//Score logic, change limit of if conditional to change max multiplier
			if(CoreLogic.getBonus() >= 5){
				CoreLogic.setScore(15*5);
			}
			else{
				CoreLogic.setBonus(0.2f);
				CoreLogic.setScore(15*CoreLogic.getBonus());
			}
		}
		
	}
}
