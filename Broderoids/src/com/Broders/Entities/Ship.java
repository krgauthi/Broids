package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
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
 * Creates a Ship Entity.
 * 
 * @author ntpeters
 * @author krgauthi
 * @author ejrinkus
 *
 */
public class Ship extends Entity{	

	private Boolean thrust;
	private Boolean shooting;
	private Sprite sprite;
	
	//TODO implement method for sound shooting and death sound


	/**
	 * Initializes a Ship by creating the appropriate physical body and sprite set.
	 * 
	 * @param	id		Used to uniquely identify this entity
	 * @param	type	The type of this entity
	 * @param playerColor 
	 */
	public Ship(String id, EntityType type, Color playerColor) {
		super(id, type);		

		Vector2 vertices[] = new Vector2[3];
		vertices[0] = new Vector2(0.0f, 1.0f);
		vertices[1] = new Vector2(0.0f, -1.0f);
		vertices[2] = new Vector2(0.44f, 0.0f);

		PolygonShape shape = new PolygonShape();
		shape.set(vertices);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 6.0f;

		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.angularDamping = 40.0f;
		bodDef.linearDamping = 0.0f;

		bodDef.position.set(CoreLogic.getWidth()/2, CoreLogic.getHeight()/2);
		bodDef.angle = MathUtils.PI;
		bodDef.allowSleep = false;
		super.createBody(bodDef, fixDef);
		
		super.setSize(((ShipType)type.getSubType()).getSize());
		super.setColor(playerColor);
		
		float meter = Gdx.graphics.getHeight()/CoreLogic.getHeightScreen();			

		System.out.println(meter);
		
		super.setSprite(((ShipType)type.getSubType()).getSprite1Path());
		super.getSprite().flip(false, true);
		super.getSprite().setOrigin((meter*this.getSize())/2, (meter*this.getSize())/2);
		super.getSprite().setSize(meter*this.getSize(), meter*this.getSize());
		super.getSprite().setColor(playerColor);

		this.thrust = false;
		Texture tempTexture = new Texture(Gdx.files.internal(((ShipType)type.getSubType()).getSprite2Path()));
		this.sprite = new Sprite(tempTexture);
		this.sprite.flip(false, true);
		this.sprite.setOrigin((meter*this.getSize())/2, (meter*this.getSize())/2);
		this.sprite.setSize(meter*this.getSize(), meter*this.getSize());							//size needs to come from the type
		this.sprite.setColor(playerColor);

	}

	/**
	 * Checks if the thrust is engaged/disengaged
	 * 
	 * @return	True if thrusting, false if not
	 */
	public Boolean getThrust(){
		return this.thrust;
	}

	/**
	 * Sets whether the thrust is engaged or disengaged
	 * 
	 * @param bool	True to enable, false to disable
	 */
	public void setThrust(boolean bool){
		this.thrust = bool;
	}
	/**
	 * Checks if the ship is currently shooting
	 * 
	 * @return true if shooting false if not
	 */
	public Boolean getShooting(){
		return this.shooting;
	}
	
	/**
	 * Sets if the the ship is shooting or not
	 * 
	 * @param bool true if shooting false if not shooting
	 */
	public void setShooting(boolean bool){
		this.shooting = bool;
	}

	/**
	 * @see Entity#Draw(SpriteBatch)
	 */
	@Override
	public void Draw(SpriteBatch sb) {

		float screenWidth =  Gdx.graphics.getWidth();
		float screenHeight =  Gdx.graphics.getHeight();

		float x = super.getBody().getPosition().x;
		float y = super.getBody().getPosition().y;

		float posX;
		float posY;

		//this will only work for single player
		posX = screenWidth*((x-CoreLogic.getViewPortX())/CoreLogic.getWidthScreen());
		posY =  screenHeight*((y-CoreLogic.getViewPortY())/CoreLogic.getHeightScreen());


		if(this.getThrust()){
			this.sprite.setPosition(posX, posY);
			this.sprite.setRotation((float)super.getAngle());
			this.sprite.draw(sb);
		}else{
			super.getSprite().setPosition(posX, posY);
			super.getSprite().setRotation((float)super.getAngle());
			super.getSprite().draw(sb);
		}
	}

}