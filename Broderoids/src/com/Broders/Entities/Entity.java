package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * All entities will be derived from this class.
 * 
 * @author ntpeters
 * @author krgauthi
 * 
 */
public abstract class Entity {

	private String ent;
	private String type;
	protected Body body;
	private Sprite sprite;

	// Extras
	private float size;
	private Color color;

	// save these for teleportation
	private BodyDef bodDef;
	private FixtureDef fixDef;
	
	private String id;

	/**
	 * Initializes the Entity with an identity and a type
	 * 
	 * @param id
	 *            Used to uniquely identify this entity
	 * @param type
	 *            The type of this entity
	 */
	public Entity(String type) {
		id = CoreLogic.nextId();
	}
	
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

	public void setEnt(String ent) {
		this.ent = ent;
	}

	public String getEnt() {
		return ent;
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
		this.bodDef = bodDef;
		this.fixDef = fixDef;
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
		return this.sprite;
	}

	/**
	 * Sets the sprite for this entity.
	 * 
	 * @param sp
	 *            The file path to the sprite
	 */
	protected void setSprite(String sp) {
		Texture texture = new Texture(Gdx.files.internal(sp));
		this.sprite = new Sprite(texture);
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

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	/**
	 * Draws this Entity on the screen
	 * 
	 * @param sb
	 */
	public void Draw(SpriteBatch sb){
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		float x = this.getBody().getPosition().x - (this.getSize() / 2f);
		float y = this.getBody().getPosition().y - (this.getSize() / 2f);

		float posX;
		float posY;

		posX = screenWidth
				* ((x - CoreLogic.getViewPortX()) / CoreLogic.getWidthScreen());
		posY = screenHeight
				* ((y - CoreLogic.getViewPortY()) / CoreLogic.getHeightScreen());

		this.getSprite().setPosition(posX, posY);
		this.getSprite().setRotation(this.getBody().getAngle());
		this.getSprite().draw(sb);
	}

	/**
	 * The same as getIdentity(). Entities should only ever be referenced using
	 * their unique ID.
	 * 
	 * @see #getIdentity()
	 */
	public String toString() {
		return this.type;
	}

	/**
	 * Determines the equality between this Entity and the given Entity
	 * 
	 * @param entity
	 *            Type to compare against
	 * @return True if entities are the same, false otherwise
	 */
	public boolean equals(Entity entity) {
		return entity.toString().equals(this.type);
	}

	/**
	 * Teleports this entity to the specified coordinates on the screen. This is
	 * used for screen wrapping.
	 * 
	 * @param x
	 *            X-Coordinate in the world (meters)
	 * @param y
	 *            Y-Coordinate in the world (meters)
	 */
	public void teleport(float x, float y) {
		Vector2 linV = this.body.getLinearVelocity();
		float angV = this.body.getAngularVelocity();
		float angle = this.body.getAngle();

		CoreLogic.getWorld().destroyBody(this.body);

		this.bodDef.position.set(x, y);
		this.bodDef.angle = angle;
		this.body = CoreLogic.getWorld().createBody(bodDef);
		this.body.createFixture(this.fixDef);
		this.body.setAngularVelocity(angV);
		this.body.setLinearVelocity(linV);
		
		this.body.setUserData(this);
	}

	public Vector2 getLinearVelocity() {
		return this.body.getLinearVelocity();
	}
	
	public abstract void update();

	public abstract void destroy();
}
