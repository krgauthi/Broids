package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * This is the Ship, which is an extension of Entity. The Ship created by the 
 * client is owned by the client and controlled by the player. Each client 
 * owns exactly one Ship. The Ship can thrust forward, rotate left and right,
 * and shoot Bullets.
 * 
 * @author ntpeters
 * @author krgauthi
 * @author ejrinkus
 * 
 */
public class Ship extends Entity {

	private Boolean thrust; //Is the ship thrusting right now?
	private Boolean shooting; // Is the ship shooting right now?
	private Sprite sprite;

	/**
	 * Initializes the superclass Entity with the "ship" type, then
	 * creates a Body Definition object at the center of the screen
	 * and a Fixture Definition object with certain physical properties.
	 * A new body is created from those definitions.
	 * 
	 * For now, the type passed in should be "ship". If we implement multiple
	 * kinds of ships, this will change.
	 * 
	 * @param id
	 *            Used to uniquely identify this entity
	 * @param type
	 *            The type of this entity
	 * @param playerColor
	 * 			  The Ship's color, which visually identifies it in multiplayer.
	 */
	public Ship(String type, Color playerColor, float x, float y) {
		super(type);
		super.setEnt("ship"); //Temporary, until we add different ship kinds.
		
		//Defining the outline vertices of the Body.
		Vector2 vertices[] = new Vector2[3];
		vertices[0] = new Vector2(-1.5f, 1.39f);
		vertices[1] = new Vector2(-1.5f, -1.39f);
		vertices[2] = new Vector2(2.67f, 0.0f);

		//Making the shape of the Ship be defined by the vertices.
		PolygonShape shape = new PolygonShape();
		shape.set(vertices);

		//Defining the physical properties of the Body
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 0.75f;

		//Defining how the Body will interact with the physics engine.
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.angularDamping = 15.0f;
		bodDef.linearDamping = 0.2f;
		bodDef.allowSleep = false;
		
		//Defining the initial position.
		bodDef.position.set(x, y);
		bodDef.angle = MathUtils.PI;

		super.createBody(bodDef, fixDef);

		//Setting graphical attributes
		super.setSize(6f);
		super.setColor(playerColor);
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		//Setting the default sprite position with respect to the Body.
		super.setSprite(Settings.data_path + "ship1.png");
		super.getSprite().flip(false, true);
		super.getSprite().setOrigin((meter * this.getSize()) / 2,(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(), meter * this.getSize());
		super.getSprite().setColor(playerColor);

		//Setting the thrusting sprite position with respect to the Body
		this.thrust = false;
		Texture tempTexture = new Texture(Gdx.files.internal(Settings.data_path	+ "ship2.png"));
		this.sprite = new Sprite(tempTexture, 1024, 1024);
		this.sprite.flip(false, true);
		this.sprite.setOrigin((meter * this.getSize()) / 2,	(meter * this.getSize()) / 2);
		this.sprite.setSize(meter * this.getSize(), meter * this.getSize());
		this.sprite.setColor(this.getColor());
		
		//Allows the Entity to be reference from the Body.
		super.getBody().setUserData(this);
	}

	/**
	 * Checks if the ship is currently thrusting.
	 * 
	 * @return True if thrusting, false if not
	 */
	public Boolean getThrust() {
		return this.thrust;
	}

	/**
	 * Sets whether the thrust is engaged or disengaged.
	 * 
	 * @param bool
	 *            True to enable, false to disable
	 */
	public void setThrust(boolean bool) {
		this.thrust = bool;
	}

	/**
	 * Checks if the ship is currently shooting.
	 * 
	 * @return true if shooting, false if not
	 */
	public Boolean getShooting() {
		return this.shooting;
	}

	/**
	 * Sets if the the ship is shooting or not.
	 * 
	 * @param bool
	 *            true if shooting, false if not shooting
	 */
	public void setShooting(boolean bool) {
		this.shooting = bool;
	}

	/**
	 * Overridden from Entity. This method gets the current position and
	 * rotation of the Body, and draws the sprite on top of it.
	 * 
	 * @see Entity#Draw(SpriteBatch)
	 */
	@Override
	public void Draw(SpriteBatch sb) {

		//Getting the screen dimensions
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		//Getting the center of the Body.
		float x = super.getBody().getPosition().x - (this.getSize() / 2f);
		float y = super.getBody().getPosition().y - (this.getSize() / 2f);

		//Getting the absolute screen location to draw the sprite to.
		float posX = screenWidth
				* ((x - CoreLogic.getViewPortX()) / CoreLogic.getWidthScreen());
		float posY = screenHeight
				* ((y - CoreLogic.getViewPortY()) / CoreLogic.getHeightScreen());

		//Draws the sprite on top of the Body. Which sprite is drawn depends upon
		//if the ship is thrusting or not.
		if (this.getThrust()) {
			this.sprite.setPosition(posX, posY);
			this.sprite.setRotation((float) super.getAngle());
			this.sprite.draw(sb);
		} else {
			super.getSprite().setPosition(posX, posY);
			super.getSprite().setRotation(
					(float) super.getAngle() + (float) (Math.PI / 2));
			super.getSprite().draw(sb);
		}
	}

	/**
	 * Overridden from Entity. If any information about the Ship needs to change
	 * with time, the required code will go here. It is currently unneeded by the
	 * Ship, so it is empty.
	 */
	@Override
	public void update() {

	}

	/**
	 * Overridden from Entity. If any code needs to execute upon destruction of
	 * the ship, the required code will go here. Once we implement ship death,
	 * then things like resetting position and losing a life will be here.
	 */
	@Override
	public void destroy() {
		
	}

}