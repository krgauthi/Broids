package com.Broders.Logic;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.Broders.Entities.Asteroid;
import com.Broders.Entities.Bullet;
import com.Broders.Entities.Entity;
import com.Broders.Entities.Ship;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author ntpeters
 * @author ejrinkus
 * @author rdbaumei
 * @author krgauthi
 * @author kelwert
 * 
 * This is the main class for handling all of the backend logic behind
 * the game that libgdx/box2d doesn't already do.
 */
public class CoreLogic {

	/*
	 * Global variables.  World is the actual world for the game (for
	 * box2d) and myGame is for libgdx,structures for storing all the
	 * existing entities and ones we want to remove, reference to the
	 * local player object (for this specific client), and the
	 * ContactListener to be added to the world.
	 */
	private static World world;
	private static HashMap<String, Entity> entities;
	private static LinkedList<Entity> rmEntities;
	private static Ship localPlayer;
	private static BaseGame myGame;
	private static ContactListener collisions;

	private static float width; // this is the map size in meters
	private static float height;

	private static float widthScreen; // screen size in meters
	private static float heightScreen;

	private static float viewPortX; // viewPort is what the player can see
	private static float viewPortY;

	private static float bulletCooldown; // To limit rapid-fire

	private static int nextEntityId; // ID to give to the next Entity created
	private static int clientId; // ID given to client by server
	
	private static long score;	// Total player score
	private static float bonus; // Accuracy bonus (increases every 5 hits in a row, resets on miss)
	
	private static int round;

	/**
	 * Generates a full ID for an entity and increments nextEntityId
	 * to prepare for more Entities
	 * 
	 * @return String 
	 */
	public static String nextId() {
		while (entities.containsKey(Integer.toString(clientId) + "-"
				+ Integer.toString(nextEntityId))) {
			nextEntityId++;
		}
		return Integer.toString(clientId) + "-"
		+ Integer.toString(nextEntityId);
	}

	public static BaseGame getGame() {
		return myGame;
	}

	public static Map<String, Entity> getEntityMap() {
		return entities;
	}

	/**
	 * By the way, this is awful, but it calculates the GCD
	 * 
	 * @param p
	 * @param q
	 * 
	 * @return The greatest common denominator between p and q
	 */
	public static int gcd(int p, int q) {
		if (q == 0) {
			return p;
		}
		return gcd(q, p % q);
	}

	/**
	 * Initializes the game core for use.
	 * 
	 * @param game - The game object for libgdx
	 */
	public static void initCore(BaseGame game) {
		//Global initializations
		myGame = game;
		score = 0;
		bonus = 1.0f;
		Vector2 gravity = new Vector2(0.0f, 0.0f);
		world = new World(gravity, false);
		entities = new HashMap<String, Entity>();
		collisions = new CollisionLogic();
		world.setContactListener(collisions);
		entities = new HashMap<String, Entity>();
		rmEntities = new LinkedList<Entity>();

		int gcd = gcd(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		widthScreen = Gdx.graphics.getWidth() / gcd * 10;
		heightScreen = Gdx.graphics.getHeight() / gcd * 10;

		bulletCooldown = 0;
		round = 0;
		
		//Switch cases for different multiplayer sizes
		if (game.multiplayer) {
			switch (myGame.gameSize) {
			case 0:
				width = widthScreen;
				height = heightScreen;
				System.out.println("small");
				break;
			case 1:
				width = 500;
				height = 500;
				System.out.println("medium");
				break;
			case 2:
				width = 1000;
				height = 1000;
				System.out.println("large");
				break;
			default:
				width = widthScreen;
				height = heightScreen;
			}
			//For a single player game
		} else {
			width = widthScreen;
			height = heightScreen;
		}

		//Set up the viewport (what the player can see)
		viewPortX = (width / 2) - (widthScreen / 2f);
		viewPortY = (height / 2) - (heightScreen / 2f);

		/*
		 * forget this Just putting these here as an example. entity IDs will be
		 * of the following format: EntityID + TypeID + ClientID + InstanceID =
		 * 00 00 000 0000
		 */
		// String clientID = "000"; // possibly let server handle clientID
		// generation somehow?
		// String instanceID = "0000"; // check map to see how many of this type
		// of entity already exist
		localPlayer = new Ship("classic", myGame.playerColor,width/2,height/2);
		entities.put(localPlayer.getId(), localPlayer);
	}

	/**
	 * This is constantly being called in the update loop in GameScreen.
	 * It updates all of the entities, bodies, and the world every timestep.
	 * The timestep (world.step()) is performed at the end of this loop.  Also
	 * handles adding/removing entities, screen wrapping and viewport movement.
	 * 
	 * @param delta - Frequency (in Hz) to update for world.step()
	 */
	public static void update(float delta) {
		bulletCooldown += delta;

		//Single player
		if (!myGame.multiplayer) {
			// Spawn more asteroids when none are left on the screen
			if (getAsteroids().size() <= 0) {
				for (int i = 0; i < myGame.difficulty; i++) {
					float x = (float) (CoreLogic.getWidth() * Math.random());
					float y = (float) (CoreLogic.getHeight() * Math.random());
					float dir = (float) (Math.PI * Math.random());

					//Prevent spawning on the player
					if(localPlayer.getX()-16 <= x && x <= localPlayer.getX()+16){
						if(x <= localPlayer.getX())
							x = localPlayer.getX()-16;
						else
							x = localPlayer.getX()+16;
					}
					if(localPlayer.getY()-16 <= x && x <= localPlayer.getY()+16){
						if(x <= localPlayer.getY())
							x = localPlayer.getY()-16;
						else
							x = localPlayer.getY()+16;
					}
					
					//Construct the asteroid
					Asteroid roid = new Asteroid("large",myGame.gameColor, x, y);
					
					/*
					 * Apply an initial force and torque (in random directions) to
					 * get the asteroid moving after it is spawned
					 */
					float initForce = (float) (4000f + (2000f * Math.random()))*(round/10+1f);
					x = (float) (initForce * Math.cos(dir));
					y = (float) (initForce * Math.sin(dir));

					Vector2 f = roid.getBody().getWorldVector(new Vector2(x, y));
					Vector2 p = roid.getBody().getWorldPoint(
							roid.getBody().getLocalCenter());
					roid.getBody().applyForce(f, p);

					float spin = (float) (300 + (250 * Math.random()));
					if (Math.random() >= 0.5f)
						spin *= -1;

					roid.getBody().applyTorque(spin);

					entities.put(roid.getId(), roid);
				}
			}
		}else{
			//asteroids
			if (getAsteroids().size() <= 0) {
				int mod = (myGame.gameSize*15);
				if(mod == 0)
					mod = 1;
				for (int i = 0; i < myGame.difficulty*mod; i++) {
					float x = (float) (CoreLogic.getWidth() * Math.random());
					float y = (float) (CoreLogic.getHeight() * Math.random());
					float dir = (float) (Math.PI * Math.random());

					//Prevent spawning on the player
					if(localPlayer.getX()-16 <= x && x <= localPlayer.getX()+16){
						if(x <= localPlayer.getX())
							x = localPlayer.getX()-16;
						else
							x = localPlayer.getX()+16;
					}
					if(localPlayer.getY()-16 <= x && x <= localPlayer.getY()+16){
						if(x <= localPlayer.getY())
							x = localPlayer.getY()-16;
						else
							x = localPlayer.getY()+16;
					}

					Asteroid roid = new Asteroid("large",myGame.gameColor, x, y);

					float initForce = (float) (4000 + (2000 * Math.random()));
					x = (float) (initForce * Math.cos(dir));
					y = (float) (initForce * Math.sin(dir));

					Vector2 f = roid.getBody().getWorldVector(new Vector2(x, y));
					Vector2 p = roid.getBody().getWorldPoint(
							roid.getBody().getLocalCenter());
					roid.getBody().applyForce(f, p);

					float spin = (float) (8000 + (4000 * Math.random()));
					if (Math.random() >= 0.5f)
						spin *= -1;

					roid.getBody().applyTorque(spin);
					
					//Store in entities
					entities.put(roid.getId(), roid);
				}
			}
		}

		// viewport logic (move the viewport when the player is close to the edge of the screen
		if ((localPlayer.getX() - viewPortX) / widthScreen > (1 - myGame.bounds)) {
			if (viewPortX < width - widthScreen) {
				float target = (((localPlayer.getX() - viewPortX) / widthScreen) - (1 - myGame.bounds))
						/ (myGame.bounds);
				adjViewPortX(10 * target);
			}
		}

		if ((localPlayer.getX() - viewPortX) / widthScreen < myGame.bounds) {
			if (viewPortX > 0) {
				float target = ((myGame.bounds - (localPlayer.getX() - viewPortX)
						/ widthScreen))
						/ (myGame.bounds);
				adjViewPortX(-10 * target);
			}
		}

		if ((localPlayer.getY() - viewPortY) / heightScreen > (1 - myGame.bounds)) {
			if (viewPortY < height - heightScreen) {
				float target = (((localPlayer.getY() - viewPortY) / heightScreen) - (1 - myGame.bounds))
						/ (myGame.bounds);
				adjViewPortY(10 * target);
			}
		}

		if ((localPlayer.getY() - viewPortY) / heightScreen < myGame.bounds) {
			if (viewPortY > 0) {
				float target = ((myGame.bounds - (localPlayer.getY() - viewPortY)
						/ heightScreen))
						/ (myGame.bounds);
				adjViewPortY(-10 * target);
			}
		}

		//viewport in relation to ship
		if (localPlayer.getX() < -4) { // make it the size of the ship
			viewPortX = width - widthScreen;
		}

		if (localPlayer.getX() > width + 4) {
			viewPortX = 0;
		}

		if (localPlayer.getY() < -4) {
			viewPortY = height - heightScreen;
		}

		if (localPlayer.getY() > height + 4) {
			viewPortY = 0;
		}
		
		// Screen wrapping
		for (Entity E : getEntities()) {
			if (E.getX() + (E.getSize()/2f) < 0) { 
				E.teleport(width + (E.getSize()/2f), E.getY());
			}

			if (E.getX() - (E.getSize()/2f) > width) {
				E.teleport(-(E.getSize()/2f), E.getY());
			}

			if (E.getY() + (E.getSize()/2f) < 0) {
				E.teleport(E.getX(), height + (E.getSize()/2f));
			}

			if (E.getY() - (E.getSize()/2f) > height) {
				E.teleport(E.getX(), -(E.getSize()/2f));
			}
			E.update(); /*THIS IS THE UPDATE! DO NOT PUT ELSEWHERE. Or at least make
						  sure that there is only one. */
		}

		//Remove any entities that need to be removed & destroy their bodies
		cleanEntities();

		/*
		 * Step the world forward using the given delta value, and have the solver iterate
		 * over the velocities and positions of all the bodies the given number of times
		 */
		world.step(delta, 10, 10);
	}

	/**
	 * Executes the game logic.
	 * 
	 * @param delta - Delta time passed from LibGDX
	 * @param in - The input direction
	 */
	public static void execute(float delta, InputDir in) {
		//Turn left
		if (in.equals("left")) {
			localPlayer.getBody().applyTorque(1000.0f); // 20 was obnoxious on
			// android device make
			// this adjustable in
			// settings?
		} 
		//Turn right (ship will not turn if both are input)
		if (in.equals("right")) {
			localPlayer.getBody().applyTorque(-1000.0f);
		}
		//Move backward (cannot be accessed through user input as of yet)
		if (in.equals("backward")) {
			Vector2 f = localPlayer.getBody().getWorldVector(
					new Vector2(0.0f, 60.0f));
			Vector2 p = localPlayer.getBody().getWorldCenter();
			localPlayer.getBody().applyForce(f, p);
		}
		//Fire a bullet (use bullet cooldown to limit rapid-fire to 5 shots/second)
		if (in.equals("shoot")) {
			if (bulletCooldown >= 0.2f) {
				//Get the angle of the player to & solve for initial bullet position
				float dir = localPlayer.getAngle() - 90.0f;
				float x = (float) (localPlayer.getX() + (2.805 * Math.cos(Math
						.toRadians(dir))));
				float y = (float) (localPlayer.getY() + (2.805 * Math.sin(Math
						.toRadians(dir))));
				
				//Create the bullet and place in entities
				Bullet shot = new Bullet("bullet", dir, localPlayer.getLinearVelocity(), x, y);
				entities.put(shot.getId(), shot);
				bulletCooldown = 0;
			}
		}
		//Thrust forward
		if (in.equals("forward")) {
			Vector2 f = localPlayer.getBody().getWorldVector(
					new Vector2(0.0f, -200.0f));
			Vector2 p = localPlayer.getBody().getWorldPoint(
					localPlayer.getBody().getLocalCenter()
					.add(new Vector2(0.0f, 0.0f)));
			localPlayer.getBody().applyForce(f, p);
			localPlayer.setThrust(true);
		} else {
			localPlayer.setThrust(false);
		}

	}

	/**
	 * Returns the map of all entities.
	 * 
	 * @return Entities Map
	 */
	public static Iterable<Entity> getEntities() {
		return entities.values();
	}

	/**
	 * Returns a map of all Ships
	 * 
	 * @return Map of Ships
	 */
	public static ArrayList<Ship> getShips() {
		ArrayList<Ship> ships = new ArrayList<Ship>();

		for (Entity entity : getEntities()) {
			if (entity.getEnt().equals("ship")) {
				ships.add((Ship) entity);
			}
		}

		return ships;
	}

	/**
	 * Returns a map of all Asteroids
	 * 
	 * @return Map of Asteroids
	 */
	public static ArrayList<Asteroid> getAsteroids() {
		ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();

		for (Entity entity : getEntities()) {
			if (entity.getEnt().equals("asteroid")) {
				asteroids.add((Asteroid) entity);
			}
		}

		return asteroids;
	}

	/**
	 * Returns a map of all Bullets
	 * 
	 * @return Map of Bullets
	 */
	public static ArrayList<Bullet> getBullets() {
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();

		for (Entity entity : getEntities()) {
			if (entity.getEnt().equals("bullet")) {
				bullets.add((Bullet) entity);
			}
		}

		return bullets;
	}

	/**
	 * Returns the ship for the local player.
	 * 
	 * @return Local player ship
	 */
	public static Ship getLocalShip() {
		return localPlayer;
	}

	/**
	 * Returns the world.
	 * 
	 * @return The world
	 */
	public static World getWorld() {
		return world;
	}

	/**
	 * Gets the world width.
	 * 
	 * @return World width
	 */
	public static float getWidth() {
		return width;
	}

	/**
	 * Gets the world height.
	 * 
	 * @return World height
	 */
	public static float getHeight() {
		return height;
	}

	/**
	 * Gets the screen width.
	 * 
	 * @return Screen width
	 */
	public static float getWidthScreen() {
		return widthScreen;
	}

	/**
	 * Gets the screen height.
	 * 
	 * @return Screen height
	 */
	public static float getHeightScreen() {
		return heightScreen;
	}

	/**
	 * Gets the viewport x-coordinates.
	 * 
	 * @return viewport x-coordinates
	 */
	public static float getViewPortX() {
		return viewPortX;
	}

	/**
	 * Gets the viewport y-coordinates.
	 * 
	 * @return viewport y-coordinates
	 */
	public static float getViewPortY() {
		return viewPortY;
	}

	/**
	 * Move the viewport laterally.
	 * 
	 * @param adj - How far to move the viewport in meters
	 */
	public static void adjViewPortX(float adj) {
		viewPortX = viewPortX + adj;
	}

	/**
	 * Move the viewport vertically.
	 * 
	 * @param adj - How far to move the viewport in meters
	 */
	public static void adjViewPortY(float adj) {
		viewPortY = viewPortY + adj;
	}

	/**
	 * Place an entity in rmEntities for removal
	 * 
	 * @param ent - Entity to be removed
	 */
	public static void removeEntity(Entity ent) {
		if(!rmEntities.contains(ent))
			rmEntities.add(ent);
	}
	
	/**
	 * Remove all the entities in the rmEntities linked list
	 * and destroy their bodies
	 */
	public static void cleanEntities() {
		for (Entity i : rmEntities) {
			entities.remove(i.getId());
			world.destroyBody(i.getBody());
			i.destroy();
		}
		rmEntities.clear();	
	}
	
	/**
	 * Get the player's score.
	 * 
	 * @return The player's score
	 */
	public static long getScore(){
		return score;
	}
	
	/**
	 * Change the player's score
	 * 
	 * @param add - amount to add (or subtract if negative) from the score
	 */
	public static void setScore(long add){
		score += add;
	}
	
	/**
	 * Get the player's accuracy bonus.
	 * 
	 * @return The player's bonus
	 */
	public static int getBonus(){
		return (int) Math.floor(bonus);
	}
	
	/**
	 * Reset the bonus
	 */
	public static void resetBonus(){
		bonus = 1.0f;
	}
	
	/**
	 * Increment the bonus
	 * 
	 * @param inc - Amount to increment (or decrement if negative) by
	 */
	public static void setBonus(float inc){
		bonus += inc;
	}

	public static void dispose() {
		//world.dispose();
		//entities.clear();
		//rmEntities.clear();
		//localPlayer.destroy();
	}
}
