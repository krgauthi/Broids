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
 * 
 * @author ntpeters
 * 
 */
public class CoreLogic {

	private static World world;
	private static HashMap<String, Entity> entities;
	private static LinkedList<Entity> rmEntities;
	private static Ship localPlayer;
	private static BaseGame myGame;
	private static ContactListener collisions;

	private static float width; // this is the map size
	private static float height;

	private static float widthScreen; // screen size in meters
	private static float heightScreen;

	private static float viewPortX;
	private static float viewPortY;

	private static float bulletCooldown;

	private static int nextEntityId;
	private static int clientId;

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
	 */
	public static int gcd(int p, int q) {
		if (q == 0) {
			return p;
		}
		return gcd(q, p % q);
	}

	/**
	 * Initializes the game core for use.
	 */
	public static void initCore(BaseGame game) {
		myGame = game;
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

		} else {
			width = widthScreen;
			height = heightScreen;
		}

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
	 * NOTE: The call to each entities update at the method is at the bottom
	 * of one of the wrapping loops.
	 * 
	 * @param delta
	 */
	public static void update(float delta) {
		bulletCooldown += Gdx.graphics.getDeltaTime();

		if (!myGame.multiplayer) {
			//asteroids
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

					Asteroid roid = new Asteroid("large",myGame.gameColor, x, y);

					float initForce = (float) (450 + (150 * Math.random()));
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
		}

		// viewport logic
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

		// Screen wrapping
		if (localPlayer.getX() < -4) { // make it the size of the ship
			localPlayer.teleport(width + 3, localPlayer.getY());
			viewPortX = width - widthScreen;
		}

		if (localPlayer.getX() > width + 4) {
			localPlayer.teleport(-3f, localPlayer.getY());
			viewPortX = 0;
		}

		if (localPlayer.getY() < -4) {
			localPlayer.teleport(localPlayer.getX(), height + 3);
			viewPortY = height - heightScreen;
		}

		if (localPlayer.getY() > height + 4) {
			localPlayer.teleport(localPlayer.getX(), -3f);
			viewPortY = 0;
		}

		for (Entity E : getEntities()) {
			if (E.getX() + (E.getSize()/2f) < 0) { // make it the size of the ship
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
			E.update(); //THIS IS THE UPDATE! DO NOT PUT ELSEWHERE. Or at least make
			//sure that there is only one. 
		}

		for (Entity i : rmEntities) {
			entities.remove(i.getId());
			world.destroyBody(i.getBody());
			i.destroy();
		}
		rmEntities.clear();
		localPlayer.setThrust(false);

		world.step(delta, 3, 8);

	}

	/**
	 * Executes the game logic.
	 * 
	 * @param delta
	 *            Delta time passed from LibGDX
	 * @param in
	 *            The input direction
	 */
	public static void execute(float delta, InputDir in) {
		if (in.equals("left")) {
			localPlayer.getBody().applyTorque(500.0f); // 20 was obnoxious on
			// android device make
			// this adjustable in
			// settings?
		} else if (in.equals("right")) {
			localPlayer.getBody().applyTorque(-500.0f);
		}

		if (in.equals("backward")) {
			Vector2 f = localPlayer.getBody().getWorldVector(
					new Vector2(0.0f, 30.0f));
			Vector2 p = localPlayer.getBody().getWorldCenter();
			localPlayer.getBody().applyForce(f, p);
		}

		if (in.equals("shoot")) {
			if (bulletCooldown >= 0.2f) {
				float dir = localPlayer.getAngle() - 90.0f;

				float x = (float) (localPlayer.getX() + (2.805 * Math.cos(Math
						.toRadians(dir))));
				float y = (float) (localPlayer.getY() + (2.805 * Math.sin(Math
						.toRadians(dir))));

				Bullet shot = new Bullet("bullet", dir, localPlayer.getLinearVelocity(), x, y);
				entities.put(shot.getId(), shot);
				bulletCooldown = 0;
			}
		}

		if (in.equals("forward")) {
			Vector2 f = localPlayer.getBody().getWorldVector(
					new Vector2(0.0f, -100.0f));
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

	public static float getWidthScreen() {
		return widthScreen;
	}

	public static float getHeightScreen() {
		return heightScreen;
	}

	public static float getViewPortX() {
		return viewPortX;
	}

	public static float getViewPortY() {
		return viewPortY;
	}

	public static void adjViewPortX(float adj) {
		viewPortX = viewPortX + adj;
	}

	public static void adjViewPortY(float adj) {
		viewPortY = viewPortY + adj;
	}

	public static void removeEntity(Entity ent) {

		//entities.remove(ent);
		rmEntities.add(ent);
	}
}
