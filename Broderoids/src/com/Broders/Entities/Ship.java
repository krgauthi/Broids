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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Creates a Ship Entity.
 * 
 * @author ntpeters
 * @author krgauthi
 * @author ejrinkus
 * 
 */
public class Ship extends Entity {

	private Boolean thrust;
	private Boolean shooting;
	private Sprite sprite;

	// TODO implement method for sound shooting and death sound

	/**
	 * Just pass in "classic" Initializes a Ship by creating the appropriate
	 * physical body and sprite set.
	 * 
	 * @param id
	 *            Used to uniquely identify this entity
	 * @param type
	 *            The type of this entity
	 * @param playerColor
	 */
	public Ship(String type, Color playerColor, float x, float y) {
		super(type);
		super.setEnt("ship");
		Vector2 vertices[] = new Vector2[3];
		vertices[0] = new Vector2(-1.5f, 1.39f);
		vertices[1] = new Vector2(-1.5f, -1.39f);
		vertices[2] = new Vector2(2.67f, 0.0f);

		PolygonShape shape = new PolygonShape();
		shape.set(vertices);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 0.75f;

		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.angularDamping = 15.0f;
		bodDef.linearDamping = 0.2f;

		bodDef.position.set(x, y);
		bodDef.angle = MathUtils.PI;
		bodDef.allowSleep = false;
		super.createBody(bodDef, fixDef);

		super.setSize(6f);
		super.setColor(playerColor);

		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		super.setSprite(Settings.data_path + "ship1.png");
		super.getSprite().flip(false, true);
		super.getSprite().setOrigin((meter * this.getSize()) / 2,(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(), meter * this.getSize());
		super.getSprite().setColor(playerColor);

		this.thrust = false;
		Texture tempTexture = new Texture(Gdx.files.internal(Settings.data_path	+ "ship2.png"));
		this.sprite = new Sprite(tempTexture, 1024, 1024);
		this.sprite.flip(false, true);
		this.sprite.setOrigin((meter * this.getSize()) / 2,	(meter * this.getSize()) / 2);
		this.sprite.setSize(meter * this.getSize(), meter * this.getSize());
		this.sprite.setColor(this.getColor());

		//Set type data
		super.getBody().setUserData(this);
	}



	/**
	 * Checks if the thrust is engaged/disengaged
	 * 
	 * @return True if thrusting, false if not
	 */
	public Boolean getThrust() {
		return this.thrust;
	}

	/**
	 * Sets whether the thrust is engaged or disengaged
	 * 
	 * @param bool
	 *            True to enable, false to disable
	 */
	public void setThrust(boolean bool) {
		this.thrust = bool;
	}

	/**
	 * Checks if the ship is currently shooting
	 * 
	 * @return true if shooting false if not
	 */
	public Boolean getShooting() {
		return this.shooting;
	}

	/**
	 * Sets if the the ship is shooting or not
	 * 
	 * @param bool
	 *            true if shooting false if not shooting
	 */
	public void setShooting(boolean bool) {
		this.shooting = bool;
	}

	/**
	 * @see Entity#Draw(SpriteBatch)
	 */
	@Override
	public void Draw(SpriteBatch sb) {

		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		float x = super.getBody().getPosition().x - (this.getSize() / 2f);
		float y = super.getBody().getPosition().y - (this.getSize() / 2f);

		float posX;
		float posY;

		posX = screenWidth
				* ((x - CoreLogic.getViewPortX()) / CoreLogic.getWidthScreen());
		posY = screenHeight
				* ((y - CoreLogic.getViewPortY()) / CoreLogic.getHeightScreen());


		if(posX > -this.getSize()*8 && posX < (screenWidth+this.getSize()*8) 
				&& posY > -this.getSize()*8 && posY < (screenHeight+this.getSize()*8)){

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
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		float temp = (float) (10+Math.random()%10);
		for(int i = 0; i < temp;i++){
			temp = 360/temp;
			
			Dust D = new Dust("debris", (float)(Math.random()%10)+(temp*i) , this.getX(), this.getY());
			CoreLogic.getComp().getEntitiesMap().put(D.getId(), D);
		}
	}

}