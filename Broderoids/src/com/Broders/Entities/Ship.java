package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.SoundManager;
import com.Broders.mygdxgame.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Inherited from Entity class.
 * The Ship is immediately controlled by the user. The user can
 * make it shoot Bullets, move forward, and rotate left and right. Each
 * Player owns one Ship, and the Bullets it spawns. In singleplayer,
 * the Ship is destroyed upon contact with an Asteroid. In multiplayer,
 * the Player takes damage when its Ship is hit by anything but Dust
 * and its own Bullets. 
 */
public class Ship extends Entity {

	//Booleans indicating what the Ship is doing right now.
	private boolean thrust;
	private boolean shooting;
	private boolean invincible;

	//These manage events that happen periodically.
	private boolean thrustLast;
	private float smokeInterval;
	private float shieldRegen;

	//These control the sound made when the ship thrusts.
	private long zoomId;
	private Sound zoom;

	/**
	 * Constructor for the Ship class. Defines the Body and its
	 * Fixtures for the physics World. Also defines the Sprites to be used
	 * by the Ship.
	 * 
	 * @param The entity's unique ID. Get this from the player.
	 * @param The player that owns the Ship (should be the local Player).
	 * @param The x-coordinate where it will spawn.
	 * @param The y-coordinate where it will spawn.
	 */
	public Ship(String id, Player owner, float x, float y) {

		//Setting unique ID and the player that owns the Ship.
		super(id, owner);

		//Setting the image that shows when the ship is not thrusting.
		super.setSprite("Ship1");

		//Setting the color of the image.
		super.setColor();

		//Setting the size of the image. Does not affect physical properties.
		super.setSize(6f);

		//Defining the shape of the Ship's Body.
		Vector2 vertices[] = new Vector2[3];
		vertices[0] = new Vector2(-1.5f, 1.39f);
		vertices[1] = new Vector2(-1.5f, -1.39f);
		vertices[2] = new Vector2(2.67f, 0.0f);

		PolygonShape shape = new PolygonShape();
		shape.set(vertices);

		//Defining the Fixture and its physical properties.
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 1.0f;
		fixDef.restitution = 0f;

		//Defining the Body and its physical properties.
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;

		bodDef.angularDamping = 14.0f;
		bodDef.linearDamping = 0.2f;

		bodDef.position.set(x, y);
		bodDef.angle = MathUtils.PI;
		bodDef.allowSleep = false;

		//Giving both Definitions to the World to create and track
		super.createBody(bodDef, fixDef);

		//Defines the ratio between the game and screen size. Used for graphics.
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		//Setting the properties of the Sprite that displays when the Ship is still.
		super.getSprite().setOrigin((meter * this.getSize()) / 2,(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(), meter * this.getSize());
		super.getSprite().setColor(super.getColor());

		//Initializing the control variables
		this.thrust = false;
		this.thrustLast = false;
		this.smokeInterval = 0;
		this.shieldRegen = 0;

		//Setting the properties of the Sprite that displays when the Ship is thrusting.
		TextureManager.getSprites("Ship2").setOrigin((meter * this.getSize()) / 2,	(meter * this.getSize()) / 2);
		TextureManager.getSprites("Ship2").setSize(meter * this.getSize(), meter * this.getSize());

		//Allows the Body to reference the Entity.
		super.getBody().setUserData(this);

		//Gets the sound that plays when the Ship is thrusting.
		zoom = SoundManager.get("zoom");
	}

	/**
	 * Checks if the thrust is engaged/disengaged.
	 * 
	 * @return True if thrusting, false if not.
	 */
	public Boolean getThrust() {
		return this.thrust;
	}

	/**
	 * Sets whether the thrust is engaged or disengaged. Also controls the sound
	 * played when the ship is thrusting and the regeneration of the shield.
	 * 
	 * @param True to enable, false to disable.
	 */
	public void setThrust(boolean bool) {

		/* Because thrusting is set to false every update loop, thrustLast stores
		 * the state from the previous loop. If the ship started thrusting this
		 * iteration, then it starts looping the zoom sound. If it stopped this
		 * iteration, then it stops playing the sound.
		 */
		if (!this.thrustLast && !this.thrust && bool) {
			zoomId = zoom.play();
			zoom.setVolume(zoomId, Settings.getSoundVol());
			zoom.setLooping(zoomId, true);
		} else if(!this.thrustLast && !this.thrust && !bool){
			zoom.stop();
		}

		//Setting the thrusting values for future reference.
		this.thrustLast = this.thrust;
		this.thrust = bool;

		//This controls shield regeneration, which only occurs in multiplayer.
		if (!CoreLogic.getGame().multiplayer) {
			//If the Ship is thrusting, then shieldRegen adds the time since the last
			//iteration.
			if (bool) {
				shieldRegen += Gdx.graphics.getDeltaTime();
			}

			//Every second the Ship is thrusting, it regains one point of shield.
			if (shieldRegen >= 1.0f) {
				getOwner().modShield(1);
				shieldRegen = 0;
			}
		}
	}

	/**
	 * Checks if the ship is currently shooting.
	 * 
	 * @return true if shooting, false if not.
	 */
	public Boolean getShooting() {
		return this.shooting;
	}

	/**
	 * Sets if the the ship is shooting or not
	 * 
	 * @param bool
	 *            true if shooting, false if not shooting
	 */
	public void setShooting(boolean bool) {
		this.shooting = bool;
	}

	/**
	 * Overridden from Entity.
	 * This method is called every game loop. It controls how the Ship is drawn on
	 * the screen. The Ship is drawn differently depending upon whether or not it is
	 * thrusting right now.
	 *
	 * @see Entity#Draw(SpriteBatch)
	 * 
	 * @param Spritebatch, which is something magic involving graphics.
	 * TODO @krgauthi: define the Spritebatch for our audience please.
	 */
	@Override
	public void Draw(SpriteBatch sb) {

		//Dimensions of the game area.
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		//The current position of the Ship, adjusted to find the corner to draw from.
		float x = super.getBody().getPosition().x - (this.getSize() / 2f);
		float y = super.getBody().getPosition().y - (this.getSize() / 2f);

		//The current position of the corner of the ship, adjusted for screen size.
		float posX;
		float posY;

		posX = screenWidth
				* ((x - CoreLogic.getViewPortX()) / CoreLogic.getWidthScreen());
		posY = screenHeight
				* ((y - CoreLogic.getViewPortY()) / CoreLogic.getHeightScreen());

		//Defines the ratio between the game and screen size. Used for graphics.
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		//TODO @krgauthi: Kriiiiiiiiiis? What does this dooooooo....?
		if(posX > -this.getSize()*8 && posX < (screenWidth+this.getSize()*8) 
				&& posY > -this.getSize()*8 && posY < (screenHeight+this.getSize()*8)){

			Sprite image;
			//Sets the properties of the Sprite to be drawn, depending upon the thrusting
			//state of the Ship.			
			if (this.getThrust()) {
				//This draws a ship with a flame coming out the back, because it is thrusting.
				image = TextureManager.getSprites("Ship2");
				image.setSize(meter * this.getSize(), meter * this.getSize());
				image.setOrigin((meter*this.getSize())/2, (meter*this.getSize())/2);
				image.setColor(this.getColor());
				image.setPosition(posX, posY);
				image.setRotation((float) super.getAngle()+180f);
				image.draw(sb);

			} else {
				//This draws a normal ship, no flame, because it is not thrusting.
				image = super.getSprite();
				image.setSize(meter * this.getSize(),meter * this.getSize());
				image.setOrigin((meter*this.getSize())/2, (meter*this.getSize())/2);
				image.setColor(super.getColor());
				image.setPosition(posX, posY);
				image.setRotation((float) super.getAngle() + 180);
				image.draw(sb);
			}
		}
	}

	/**
	 * Overridden from Entity.
	 * This is called every game loop. The only thing that needs to be tracked every
	 * loop iteration is the damage done to the ship, and whether or not it should be
	 * leaking smoke.
	 */
	@Override
	public void update() {
		//Smoking is unnecessary in singleplayer.
		if (!CoreLogic.getGame().multiplayer)
			return;

		//Controls how often a Dust entity should be spawned to simulate smoke. 
		float smoke;
		
		//Damage is actually tracked by the Player class, not the Ship, for ease
		//of programming of multiplayer.
		int health = getOwner().getHealth();

		//Smoking starts at half health, and gets faster the less health the Player
		//has.
		if (health <= 50) {
			smoke = 0.6f;
			if (health <= 25)
				smoke = 0.3f;
			if (health <= 15)
				smoke = 0.1f;
			if (health <= 5)
				smoke = 0.05f;

			//Holds the time since the last Dust entity spawned.
			smokeInterval +=  Gdx.graphics.getDeltaTime();

			//Spawns a new Dust entity once the smokeInterval times out.
			if (smokeInterval >= smoke) {
				//The Dust moves (roughly) out from the rear of the ship.
				float dir = (float) ((super.getAngle() + 90) + (Math.random() * 30 - 15));
				
				//Add a bit of fiery color to emphasize the fact that YOU ARE ABOUT TO DIE.
				double fire = Math.random();
				Color color = new Color(Color.GRAY);
				if (fire <= 0.2)
					color.set(Color.YELLOW);
				if (fire > 0.15 && fire < 0.3)
					color.set(Color.RED);

				//Making a new Dust entity with parameters defined from the ship and the
				//Scratch Player.
				Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), dir ,
						this.getX(), this.getY(), 4, color);
				
				//Identifying the Dust object and adding it to the Scratch Player's Entity Map.
				String[] idParts = D.getId().split("-");
				CoreLogic.getScratch().getEntitiesMap().put(idParts[1], D);

				smokeInterval = 0;
			}
		}
	}

	/**
	 * Overridden from Entity class.
	 * In singleplayer, this is called when the Ship hits an Asteroid. In multiplayer,
	 * this gets called when the health value tracked by the local Player hits 0. When
	 * the Ship is destroyed, the Player loses a life (in singleplayer), the Player 
	 * loses points (in mulitplayer), and it spawns a bunch of Dust shrapnel.
	 */
	@Override
	public void destroy() {
		
		//The Player loses points if the ship dies.
		if(CoreLogic.multiplayer){ 
			this.getOwner().modScore(-500);
		}
		
		//You also lose your bonus multiplier.
		this.getOwner().modBonus(1.0f);
		
		//It's hard to thrust when you don't have engines.
		setThrust(false);

		//That is the sound of inevitability, Mr. Anderson.
		SoundManager.play("death", 1f, 0.85f);
		zoom.stop();
		
		//This loop spawns a bunch of random Dust shrapnel from the Ship explosion.
		float temp = (float) (10+Math.random()%10);
		for(int i = 0; i < temp;i++){
			temp = 360/temp;

			Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), 
					(float)(Math.random()%10)+(temp*i), this.getX(), this.getY(), 30, super.getColor());
			String[] idParts = D.getId().split("-");
			CoreLogic.getScratch().getEntitiesMap().put(idParts[1], D);
		}
	}

	/**
	 * Sets whether the Ship will or will not collide with other Bodies.
	 * 
	 * @param True if invincible, false if not.
	 */
	public void setInvincible(boolean b){
		invincible = b;
	}

	/**
	 * Checks whether or not the Ship will collide with other Bodies. This is true for
	 * the first 3 seconds after respawn.
	 * 
	 * @return True if invincible, false if not.
	 */
	public boolean isInvincible(){
		return invincible;
	}

	/**
	 * Allows the sound of the thrusting to be shut off if the game exits when the
	 * Ship isn't dead.
	 */
	public void killZoom() {
		zoom.stop();
	}
}