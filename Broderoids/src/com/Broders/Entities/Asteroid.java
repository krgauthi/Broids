package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Net;
import com.Broders.Logic.Player;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Inherited from the Entity Class. Asteroids are the floating hunks of rock
 * that act as both target and hazard to the player. In singleplayer, contact
 * with the ship will destroy it instantly. In multiplayer, Asteroids will
 * damage ship. When hit by a bullet, the Asteroid will spawn a bunch of dust
 * and two asteroids of the next size down. Small asteroids simply spawn the
 * dust.
 */
public class Asteroid extends Entity {

	// Enumerating the Asteroid sizes
	public static final int LARGE = 0;
	public static final int MEDIUM = 1;
	public static final int SMALL = 2;

	// Holds the size of the asteroid, as indicated above.
	private int type;

	/**
	 * This is for networking, I assume.
	 * 
	 * @return The enumerated size of the asteroid.
	 */
	@Override
	public int extra() {
		return type;
	}

	/**
	 * Constructor for the Asteroid class. Defines the Body and its Fixtures for
	 * the physics World. Also defines the Sprite to be used by the Asteroid.
	 * The size of the Asteroid is passed in as the first parameter. Large = 0,
	 * Medium = 1, Small = 2;
	 * 
	 * @param type
	 *            The enumerated size of the Asteroid. (see Asteroid.type())
	 * @param id
	 *            Entity id (Get this from the owner)
	 * @param owner
	 *            The owner. Should be Host Player.
	 * @param x
	 *            Initial X position
	 * @param y
	 *            Initial Y position
	 */
	public Asteroid(int type, String id, Player owner, float x, float y) {

		// Setting the unique ID and the owner of the Asteroid.
		super(id, owner);

		// Setting the image that will be shown on screen.
		super.setSprite("Broid");

		// Setting the color of the image;
		super.setColor();

		// Defining the Fixture Definition and Shape.
		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();

		// Initializing the properties that depend upon the size of the
		this.type = type;
		if (this.type == SMALL) {
			super.setSize(3.75f);
			shape.setRadius(1.5f);
			fixDef.density = 0.2f;
			super.setPoints(100);
		} else if (this.type == MEDIUM) {
			super.setSize(7.5f);
			shape.setRadius(3f);
			fixDef.density = 0.5f;
			super.setPoints(50);
		} else {
			super.setSize(15.0f);
			shape.setRadius(6f);
			fixDef.density = 1.0f;
			super.setPoints(20);
		}

		// Initializing physical properties used by all Asteroids
		fixDef.shape = shape;
		fixDef.restitution = 0.1f;

		// Defining and initializing the Body and its physical properties.
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.angularDamping = 0.0f;
		bodDef.linearDamping = 0.0f;
		bodDef.allowSleep = false;

		// Giving it position and rotation, and creating the Body from
		// the Definitions.
		bodDef.position.set(x, y);
		bodDef.angle = (float) ((2 * MathUtils.PI) * Math.random());
		super.createBody(bodDef, fixDef);

		// Defines the ratio between the game and screen size. Used for
		// graphics.
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		// Setting properties of the Sprite that will be displayed.
		super.getSprite().setOrigin(meter * (this.getSize() / 2), meter * (this.getSize() / 2));
		super.getSprite().setSize(meter * this.getSize(), meter * this.getSize());
		super.getSprite().setColor(this.getColor());

		// Allows the Body to reference the Entity.
		super.getBody().setUserData(this);
	}

	/**
	 * Overridden from Entity. Unused here.
	 */
	@Override
	public void update() {
	}

	/**
	 * Overridden from Entity class. Called when the Asteroid is to be
	 * destroyed. If it is a Large or Medium Asteroid, then it will spawn two
	 * Asteroids of the next size down. It also spawns some Dust as particle
	 * effect.
	 */
	@Override
	public void destroy() {
		// Defining the new Asteroids and their positions.
		Asteroid roid1;
		Asteroid roid2;
		float x1;
		float x2;
		float y1;
		float y2;
		float dir;

		// Spawning new Asteroids depending upon the size of this one.
		if (this.type == LARGE) {
			// This loop creates particle effects for the exploding Asteroids
			float temp = (float) (10 + Math.random() % 10);
			for (int i = 0; i < temp; i++) {
				temp = 360 / temp;

				Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), (float) (Math.random() % 10) + (temp * i), this.getX(), this.getY(), 25, getColor());
				String[] idParts = D.getId().split("-");
				CoreLogic.getScratch().getEntitiesMap().put(idParts[1], D);
			}

			// Only the player in control of the Asteroids (the Host, or in
			// singleplayer) will
			// spawn Asteroids.
			if (!CoreLogic.multiplayer || CoreLogic.isHost()) {
				dir = (float) Math.toRadians(this.getAngle());
				x1 = (float) (this.getX() + 7.5 * Math.cos(dir));
				x2 = (float) (this.getX() + 7.5 * Math.cos(dir));
				y1 = (float) (this.getY() - 7.5 * Math.sin(dir));
				y2 = (float) (this.getY() - 7.5 * Math.sin(dir));

				// MEDIUM ASTEROID 1
				roid1 = new Asteroid(MEDIUM, this.owner.nextId(), this.owner, x1, y1);

				// Initial position and force
				float initForce = (float) (3200 + (3000 * Math.random()));
				float x = (float) (initForce * Math.cos(dir));
				float y = (float) (initForce * Math.sin(dir));

				// Get it moving by applying the force.

				Vector2 f = roid1.getBody().getWorldVector(new Vector2(x, y));
				Vector2 p = roid1.getBody().getWorldPoint(roid1.getBody().getLocalCenter());
				roid1.getBody().applyForce(f, p);

				// Set it spinning.
				float spin = (float) (25 * Math.random());
				if (Math.random() >= 0.5f)
					spin *= -1;
				roid1.getBody().setAngularVelocity(spin);

				// Put it in the entities map and make Networking aware of it.
				String[] idParts = roid1.getId().split("-");
				CoreLogic.getComp().getEntitiesMap().put(idParts[1], roid1);
				if (CoreLogic.multiplayer && Net.ownedByLocal(roid1.getId())) {
					Net.createEntity(roid1);
				}

				// MEDIUM ASTEROID 2
				roid2 = new Asteroid(MEDIUM, this.owner.nextId(), this.owner, x2, y2);

				// Initial position and force
				initForce = (float) (3200 + (3000 * Math.random()));
				x = (float) (initForce * Math.cos(dir));
				y = (float) (initForce * Math.sin(dir));

				// Get it moving by applying the force.
				f = roid2.getBody().getWorldVector(new Vector2(x, y));
				p = roid2.getBody().getWorldPoint(roid2.getBody().getLocalCenter());
				roid2.getBody().applyForce(f, p);

				// Set it spinning.
				spin = (float) (25 * Math.random());
				if (Math.random() >= 0.5f)
					spin *= -1;
				roid1.getBody().setAngularVelocity(spin);

				// Put it in the entities map and make Networking aware of it.
				idParts = roid2.getId().split("-");
				CoreLogic.getComp().getEntitiesMap().put(idParts[1], roid2);
				if (CoreLogic.multiplayer && Net.ownedByLocal(roid2.getId())) {
					Net.createEntity(roid2);
				}

				// Play the destruction noise at a lower pitch.
				sound(0.7f);
			}
		} else if (this.type == MEDIUM) {
			// This loop creates particle effects for the exploding Asteroids
			float temp = (float) (5 + Math.random() % 10);
			for (int i = 0; i < temp; i++) {
				temp = 360 / temp;

				Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), (float) (Math.random() % 10) + (temp * i), this.getX(), this.getY(), 20, getColor());
				String[] idParts = D.getId().split("-");
				CoreLogic.getScratch().getEntitiesMap().put(idParts[1], D);
			}

			// Only the player in control of the Asteroids (the Host, or in
			// singleplayer) will
			// spawn Asteroids.

			if (!CoreLogic.multiplayer || CoreLogic.isHost()) {
				dir = (float) Math.toRadians(this.getAngle());
				x1 = (float) (this.getX() + 3.75 * Math.cos(dir));
				x2 = (float) (this.getX() + 3.75 * Math.cos(dir));
				y1 = (float) (this.getY() - 3.75 * Math.sin(dir));
				y2 = (float) (this.getY() - 3.75 * Math.sin(dir));

				// SMALL ASTEROID 1
				roid1 = new Asteroid(SMALL, this.owner.nextId(), this.owner, x1, y1);

				// Initial position and force
				float initForce = (float) (440 + (230 * Math.random()));
				float x = (float) (initForce * Math.cos(Math.random() * 2 * Math.PI));
				float y = (float) (initForce * Math.sin(Math.random() * 2 * Math.PI));

				// Get it moving by applying the force.
				Vector2 f = roid1.getBody().getWorldVector(new Vector2(x, y));
				Vector2 p = roid1.getBody().getWorldPoint(roid1.getBody().getLocalCenter());
				roid1.getBody().applyForce(f, p);

				// Set it spinning.
				float spin = (float) (20 * Math.random());
				if (Math.random() >= 0.5f)
					spin *= -1;
				roid1.getBody().setAngularVelocity(spin);

				// Put it in the entities map and make Networking aware of it.
				String[] idParts = roid1.getId().split("-");
				CoreLogic.getComp().getEntitiesMap().put(idParts[1], roid1);
				if (CoreLogic.multiplayer && Net.ownedByLocal(roid1.getId())) {
					Net.createEntity(roid1);
				}

				// SMALL ASTEROID 2
				roid2 = new Asteroid(SMALL, this.owner.nextId(), this.owner, x2, y2);

				// Initial position and force
				initForce = (float) (420 + (220 * Math.random()));
				x = (float) (initForce * Math.cos(dir));
				y = (float) (initForce * Math.sin(dir));

				// Get it moving by applying the force.
				f = roid2.getBody().getWorldVector(new Vector2(x, y));
				p = roid2.getBody().getWorldPoint(roid2.getBody().getLocalCenter());
				roid2.getBody().applyForce(f, p);

				// Set it spinning.
				spin = (float) (300 + (250 * Math.random()));
				if (Math.random() >= 0.5f)
					spin *= -1;
				roid2.getBody().applyTorque(spin);

				// Put it in the entities map and make Networking aware of it.
				idParts = roid2.getId().split("-");
				CoreLogic.getComp().getEntitiesMap().put(idParts[1], roid2);

				if (CoreLogic.multiplayer && Net.ownedByLocal(roid2.getId())) {
					Net.createEntity(roid2);
				}

				// Play the destruction noise at a medium pitch.
				sound(1f);
			}

			// This executes if this Asteroid is small, and thus does not spawn
			// any
			// other Asteroids on destruction
		} else {
			// This block creates particle effects for the exploding Asteroids
			float temp = (float) (3 + Math.random() % 10);
			for (int i = 0; i < temp; i++) {
				temp = 360 / temp;

				Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), (float) (Math.random() % 10) + (temp * i), this.getX(), this.getY(), 15, getColor());
				String[] idParts = D.getId().split("-");
				CoreLogic.getScratch().getEntitiesMap().put(idParts[1], D);
			}
			// Play the destruction noise at a higher pitch.
			sound(1.3f);
		}
	}

	/**
	 * This method takes a pitch and plays a random Asteroid-destruction noise
	 * at that pitch.
	 * 
	 * @param pitch
	 *            Must be between 0.5f and 2.0f.
	 */
	private void sound(float pitch) {
		int pick = (int) Math.floor(Math.random() * 3);
		SoundManager.play("roidBreak" + Integer.toString(pick + 1), 1f, pitch);
	}

	/**
	 * Returns the enumerated size of the Asteroid. 0 = Large 1 = Medium 2 =
	 * Small
	 * 
	 * @return size in int.
	 */
	public int getType() {
		return type;
	}
}
