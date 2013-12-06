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
public abstract class Entity {

	// The physical object that interacts in the physics engine.
	protected Body body;
		
	// This is needed by networking.
	private boolean dead;

	// These are properties used by the Sprite.
	private String spriteID;
	private float size;
	private Color color;
	
	// The point value of the Entity.
	private int points;

	// The ID is unique among Entities owned by the Player. Together will
	// Player ID gives a completely unique ID.
	protected String id;
	protected Player owner;

	/**
	 * Constructor for the Entity class. The inherited classes do most of the work here,
	 * but this sets the id and the owner.
	 * 
	 * @param id
	 *            Entity id, get this from the owner.
	 * @param owner
	 *            Entity owner.
	 */
	public Entity(String id, Player owner) {
		this.id = id;
		this.owner = owner;
		dead = false;
		// System.out.println(id);
	}

	/**
	 * @return the full id of the Entity
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return This Entity's physical Body
	 */
	public Body getBody() {
		return this.body;
	}

	/**
	 * @return the owner of the Entity
	 */
	public Player getOwner() {
		return this.owner;
	}

	/**
	 * Gets the current X-Coordinate of the Body (meters).
	 * 
	 * @return Body X-Coordinate in meters
	 */
	public float getX() {
		return this.getBody().getPosition().x;
	}

	/**
	 * Gets the current Y-Coordinate of the Body (meters).
	 * 
	 * @return Body Y-Coordinate in meters
	 */
	public float getY() {
		return this.getBody().getPosition().y;
	}

	/**
	 * Creates the Entity's Body from the Body Definition and Fixture Definition.
	 * They are then added to the World to be handled.
	 * 
	 * @param bodDef
	 *            The Body Definition for the Body to be created
	 * @param fixDef
	 *            The Fixture Definition for the Body to be created
	 */
	protected void createBody(BodyDef bodDef, FixtureDef fixDef) {
		this.body = CoreLogic.getWorld().createBody(bodDef);
		this.body.createFixture(fixDef);
	}

	/**
	 * Returns the angle of the Body in degrees.
	 * 
	 * @return Body angle in degrees
	 */
	public float getAngle() {
		return (float) (this.body.getAngle() * (180.00f / Math.PI));
	}

	/**
	 * Returns the Sprite for this Entity.
	 * 
	 * @return This Entity's Sprite
	 */
	public Sprite getSprite() {
		return TextureManager.getSprites(this.spriteID);
	}

	/**
	 * Sets the Sprite for this Entity.
	 * 
	 * @param sp
	 *            The file path to the Sprite
	 */
	protected void setSprite(String sp) {
		this.spriteID = sp;
	}

	/**
	 * Returns the size of the Entity in meters. Size is used
	 * for Sprite rendering.
	 * 
	 * @return size in meters
	 */
	public float getSize() {
		return this.size;
	}

	/**
	 * Sets the size of the Entity in Meters. Size is used for
	 * Sprite rendering.
	 * 
	 * @param s
	 *            size of Entity in meters
	 */
	public void setSize(float s) {
		this.size = s;
	}

	/**
	 * @return color of the Entity
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Sets the color of the Entity to the owner's color.
	 */
	public void setColor() {
		this.color = this.owner.getColor();
	}

	/**
	 * @param color
	 *            Color to make the Entity change to.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Draws this Entity on the screen. Gets the position and rotation of the Body, and
	 * the color and size from this Entity, then draw the Sprite on top of the Body.
	 * 
	 * @param sb
	 * 			Things to be drawn are sent to the SpriteBatch
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
	 * Teleports this entity to the specified coordinates on the screen. This is
	 * used for screen wrapping.
	 * 
	 * @param x
	 *            X-Coordinate to teleport to (meters)
	 * @param y
	 *            Y-Coordinate to teleport to (meters)
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
	 * @return linear velocity of the entity's body
	 */
	public Vector2 getLinearVelocity() {
		return this.body.getLinearVelocity();
	}

	/**
	 * Advanced teleport for server use.
	 * 
	 * @param x
	 *            X position to teleport to
	 * @param y
	 *            Y position to teleport to
	 * @param angle
	 *            angle of the Entity
	 * @param angleVel
	 *            angular velocity of the Entity's rotation
	 * @param vX
	 *            x-component of the Entity's velocity
	 * @param vY
	 *            y-component of the Entity's velocity
	 */
	public void teleport(float x, float y, float angle, float angleVel, float vX, float vY) {

		// Creates a new Body with the info in the given parameters
		this.body.setTransform(x, y, this.body.getAngle());
		this.body.setAngularVelocity(angleVel);
		this.body.setLinearVelocity(new Vector2(vX, vY));
	}

	/**
	 * This method is called every tick and can be used to update entities in
	 * nonstandard ways. Must be overridden.
	 */
	public abstract void update();

	/**
	 * This is used by the server.
	 * 
	 * @return any extra data needed for networking
	 */
	public int extra() {
		return 0;
	}

	/**
	 * This is called when an Entity dies, in order to clean up and initiate death-events.
	 * Must be overriden.
	 */
	public abstract void destroy();

	/**
	 * Sets the point value of the Entity.
	 * 
	 * @param v
	 *            How many points the entity is worth
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
