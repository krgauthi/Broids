package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.Broders.mygdxgame.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

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
	private boolean invincible;

	private boolean thrustLast;
	private float smokeInterval;
	private float shieldRegen;
	
	private BaseGame game;

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
	public Ship(String id, Player owner, float x, float y) {
		super(id, owner);

		this.game = owner.getGame();
		
		Vector2 vertices[] = new Vector2[3];
		vertices[0] = new Vector2(-1.5f, 1.39f);
		vertices[1] = new Vector2(-1.5f, -1.39f);
		vertices[2] = new Vector2(2.67f, 0.0f);

		PolygonShape shape = new PolygonShape();
		shape.set(vertices);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 1.0f;
		fixDef.restitution = 0f;

		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;

		bodDef.angularDamping = 14.0f;
		bodDef.linearDamping = 0.2f;

		bodDef.position.set(x, y);
		bodDef.angle = MathUtils.PI;
		bodDef.allowSleep = false;
		super.createBody(bodDef, fixDef);

		super.setSize(6f);
		super.setColor();

		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		super.setSprite("Ship1");
		//super.getSprite().flip(false, true);
		super.getSprite().setOrigin((meter * this.getSize()) / 2,(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(), meter * this.getSize());
		super.getSprite().setColor(super.getColor());

		this.thrust = false;
		this.thrustLast = false;
		this.smokeInterval = 0;
		this.shieldRegen = 0;

		//TextureManager.getSprites("Ship2").flip(false, true);

		TextureManager.getSprites("Ship2").setOrigin((meter * this.getSize()) / 2,	(meter * this.getSize()) / 2);
		TextureManager.getSprites("Ship2").setSize(meter * this.getSize(), meter * this.getSize());

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

		Sound zoom = SoundManager.get("zoom");
		long soundId;
		if (!thrustLast && bool) {
			soundId = zoom.loop(Settings.getSoundVol() * 0.1f);
			zoom.setPitch(soundId, (float) (0.8f + Math.random() * 0.4f));
		} else if (thrustLast && !bool) {
			zoom.stop();
		}

		this.thrustLast = this.thrust;
		this.thrust = bool;

		if (thrust) {
			shieldRegen += Gdx.graphics.getDeltaTime();
		}

		if (shieldRegen >= 1.0f) {
			getOwner().modShield(1);
			shieldRegen = 0;
		}
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

		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		if(posX > -this.getSize()*8 && posX < (screenWidth+this.getSize()*8) 
				&& posY > -this.getSize()*8 && posY < (screenHeight+this.getSize()*8)){

			if (this.getThrust()) {
				TextureManager.getSprites("Ship2").setSize(meter * this.getSize(), meter * this.getSize());
				TextureManager.getSprites("Ship2").setOrigin((meter*this.getSize())/2, (meter*this.getSize())/2);
				TextureManager.getSprites("Ship2").setColor(this.getColor());
				TextureManager.getSprites("Ship2").setPosition(posX, posY);
				TextureManager.getSprites("Ship2").setRotation((float) super.getAngle()+180f);
				TextureManager.getSprites("Ship2").draw(sb);

			} else {
				super.getSprite().setSize(meter * this.getSize(),meter * this.getSize());
				super.getSprite().setOrigin((meter*this.getSize())/2, (meter*this.getSize())/2);
				super.getSprite().setColor(super.getColor());
				super.getSprite().setPosition(posX, posY);
				super.getSprite().setRotation((float) super.getAngle() + 180);
				super.getSprite().draw(sb);
			}
		}
	}

	@Override
	public void update() {
		if (!CoreLogic.getGame().multiplayer)
			return;

		float smoke;
		int health = getOwner().getHealth();

		if (health <= 50) {
			smoke = 0.6f;
			if (health <= 25)
				smoke = 0.3f;
			if (health <= 15)
				smoke = 0.1f;
			if (health <= 5)
				smoke = 0.05f;

			smokeInterval +=  Gdx.graphics.getDeltaTime();

			if (smokeInterval >= smoke) {
				float dir = (float) ((super.getAngle() + 90) + (Math.random() * 30 - 15));
				double fire = Math.random();
				Color color = new Color(Color.GRAY);
				if (fire <= 0.2)
					color.set(Color.YELLOW);
				if (fire > 0.15 && fire < 0.3)
					color.set(Color.RED);

				Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), dir ,
						this.getX(), this.getY(), 5, color);
				CoreLogic.getScratch().getEntitiesMap().put(D.getId(), D);

				smokeInterval = 0;
			}
		}
	}

	@Override
	public void destroy() {
		if(CoreLogic.multiplayer){ 
			this.getOwner().modScore(-500);
			this.getOwner().modBonus(1.0f);
		}
		float temp = (float) (10+Math.random()%10);
		setThrust(false);
		SoundManager.play("death", 1f, 0.85f);
		for(int i = 0; i < temp;i++){
			temp = 360/temp;

			Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), 
					(float)(Math.random()%10)+(temp*i), this.getX(), this.getY(), 30, super.getColor());
			CoreLogic.getScratch().getEntitiesMap().put(D.getId(), D);
		}
	}

	public void setInvincible(boolean b){
		invincible = b;
	}

	public boolean isInvincible(){
		return invincible;
	}

}