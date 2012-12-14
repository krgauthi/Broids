package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.Broders.mygdxgame.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * All entities will be derived from this class. It contains data such
 * as color, Sprite size, unique ID, owner, point worth, and the Body. It
 * also contains methods that deal with drawing and wrapping.
 */
/**
 * @author belak
 * 
 */
/**
 * @author belak
 * 
 */
public abstract class Entity {

<<<<<<< HEAD
	// The physical object that interacts in the physics engine.
=======
	// private String ent;
	// private String type;
>>>>>>> dee60836b21198f5f69c516408b38570128cd5aa
	protected Body body;
		
	// This is needed by networking.
	private boolean dead;

<<<<<<< HEAD
	// These are properties used by the Sprite.
	private String spriteID;
=======
	// Extras
>>>>>>> dee60836b21198f5f69c516408b38570128cd5aa
	private float size;
	private Color color;
	
	// The point value of the Entity.
	private int points;

<<<<<<< HEAD
	// These are needed for teleporting the Entity. Don't delete.
	// TODO Do we still need these?
	private BodyDef bodDef;
	private FixtureDef fixDef;

	// The ID is unique among Entities owned by the Player. Together will
	// Player ID gives a completely unique ID.
=======
>>>>>>> dee60836b21198f5f69c516408b38570128cd5aa
	protected String id;
	protected Player owner;

	/**
	 * @param id
	 *            Entity id
	 * @param owner
	 *            Entity owner
	 */
	public Entity(String id, Player owner) {
		this.id = id;
		this.owner = owner;
		dead = false;
		// System.out.println(id);
	}

	/**
	 * @return the full id of the entity
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the physical body of this entity
	 * 
	 * @return This entity's body
	 */
	public Body getBody() {
		return this.body;
	}

	/**
	 * @return the owner of the entity
	 */
	public Player getOwner() {
		return this.owner;
	}

	/**
	 * Gets the current X-Coordinate of the body (meters)
	 * 
	 * @return Body X-Coordinate in meters
	 */
	public float getX() {
		return this.getBody().getPosition().x;
	}

	/**
	 * Gets the current Y-Coordinate of the body (meters)
	 * 
	 * @return Body Y-Coordinate in meters
	 */
	public float getY() {
		return this.getBody().getPosition().y;
	}

	/**
	 * Creates the entity's body.
	 * 
	 * @param bodDef
	 *            The body definition for the body to be created
	 * @param fixDef
	 *            The fixture definition for the body to be created
	 */
	protected void createBody(BodyDef bodDef, FixtureDef fixDef) {
		this.body = CoreLogic.getWorld().createBody(bodDef);
		this.body.createFixture(fixDef);
	}

	/**
	 * Returns the angle of the body in degrees.
	 * 
	 * @return Body angle in degrees
	 */
	public float getAngle() {
		return (float) (this.body.getAngle() * (180.00f / Math.PI));
	}

	/**
	 * Returns the sprite for this entity.
	 * 
	 * @return This Entity's sprite
	 */
	public Sprite getSprite() {
		return TextureManager.getSprites(this.spriteID);
	}

	/**
	 * Sets the sprite for this entity.
	 * 
	 * @param sp
	 *            The file path to the sprite
	 */
	protected void setSprite(String sp) {
		this.spriteID = sp;
	}

	/**
	 * returns the size of the Entity in meters
	 * 
	 * @return size in meters
	 */
	public float getSize() {
		return this.size;
	}

	/**
	 * Sets the size of the Entity in meters
	 * 
	 * @param s
	 *            size of Entity in meters
	 */
	public void setSize(float s) {
		this.size = s;
	}

	/**
	 * @return color of the entity
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Sets the color to the owner's color
	 */
	public void setColor() {
		this.color = this.owner.getColor();
	}

	/**
	 * @param color
	 *            Color the entity should change to
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Draws this Entity on the screen
	 * 
	 * @param sb
	 */
	public void Draw(SpriteBatch sb) {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		float x = this.getBody().getPosition().x - (this.getSize() / 2f);
		float y = this.getBody().getPosition().y - (this.getSize() / 2f);

		float posX;
		float posY;

		posX = screenWidth * ((x - CoreLogic.getViewPortX()) / CoreLogic.getWidthScreen());
		posY = screenHeight * ((y - CoreLogic.getViewPortY()) / CoreLogic.getHeightScreen());

		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		if (posX > -(this.getSize() * 8) && posX < (screenWidth + (this.getSize() * 8)) && posY > -(this.getSize() * 8) && posY < (screenHeight + (this.getSize() * 8))) {
			this.getSprite().setOrigin((meter * this.getSize()) / 2, (meter * this.getSize()) / 2);
			this.getSprite().setSize(meter * this.getSize(), meter * this.getSize());
			this.getSprite().setColor(this.getColor());
			this.getSprite().setPosition(posX, posY);
			this.getSprite().setRotation(this.getBody().getAngle());
			this.getSprite().setColor(getColor());
			this.getSprite().draw(sb);
		}
	}

	/**
<<<<<<< HEAD
	 * The same as getIdentity(). Entities should only ever be referenced using
	 * their unique ID.
	 * 
	 * @see #getIdentity()
	 */
	// public String toString() {
	// 	return this.type;
	// }

	/**
	 * Determines the equality between this Entity and the given Entity
	 * 
	 * @param entity
	 *            Type to compare against
	 * @return True if entities are the same, false otherwise
	 */
	// public boolean equals(Entity entity) {
	// return entity.toString().equals(this.type);
	// }

	/**
=======
>>>>>>> dee60836b21198f5f69c516408b38570128cd5aa
	 * Teleports this entity to the specified coordinates on the screen. This is
	 * used for screen wrapping.
	 * 
	 * @param x
	 *            X-Coordinate in the world (meters)
	 * @param y
	 *            Y-Coordinate in the world (meters)
	 */
	public void teleport(float x, float y) {
		this.body.setTransform(x, y, this.body.getAngle());
	}

	/**
	 * @return angular velocity of the entity's body
	 */
	public float getAngularVelocity() {
		return this.body.getAngularVelocity();
	}

	/**
	 * @return linear velociry of the entity's body
	 */
	public Vector2 getLinearVelocity() {
		return this.body.getLinearVelocity();
	}

	/**
	 * Advanced teleport for server use
	 * 
	 * @param x
	 *            x-position of the entity
	 * @param y
	 *            y-position of the entity
	 * @param angle
	 *            angle of the entity
	 * @param angleVel
	 *            angular velocity of the entity's rotation
	 * @param vX
	 *            x-component of the entity's velocity
	 * @param vY
	 *            y-component of the entity's velocity
	 */
	public void teleport(float x, float y, float angle, float angleVel, float vX, float vY) {

		// Creates a new Body with the info in the given parameters
		this.body.setTransform(x, y, this.body.getAngle());
		this.body.setAngularVelocity(angleVel);
		this.body.setLinearVelocity(new Vector2(vX, vY));
	}

	/**
	 * This method is called every tick and can be used to update entities in
	 * nonstandard ways.
	 */
	public abstract void update();

	/**
	 * @return any extra data needed for networking
	 */
	public int extra() {
		return 0;
	}

	/**
	 * Used when we no longer need the entity for cleaning up
	 */
	public abstract void destroy();

	/**
	 * @param v
	 *            how many points the entity is worth
	 */
	public void setPoints(int v) {
		points = v;
	}

	/**
	 * @return number of points the entity is worth
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @return whether we have already tried to destroy this entity
	 */
	public boolean isDead() {
		return this.dead;
	}

	/**
	 * Mark this entity as destroyed
	 */
	public void setDead() {
		this.dead = true;
	}
}
