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
	private static HashMap<String, Player> players;

	private static LinkedList<Entity> rmEntities;
	private static Player local;
	private static Player comp;

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

	private static int round;

	private static boolean host;
	private static float delay;
	private static boolean display;

	public static BaseGame getGame() {
		return myGame;
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
	public static void initCore(BaseGame game, Boolean h) {
		myGame = game;
		Vector2 gravity = new Vector2(0.0f, 0.0f);
		world = new World(gravity, false);

		collisions = new CollisionLogic();
		world.setContactListener(collisions);
		players = new HashMap<String, Player>();
		rmEntities = new LinkedList<Entity>();
		host = h;

		int gcd = gcd(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		widthScreen = Gdx.graphics.getWidth() / gcd / Gdx.graphics.getDensity()
				* 5;
		heightScreen = Gdx.graphics.getHeight() / gcd
				/ Gdx.graphics.getDensity() * 5;

		bulletCooldown = 0;
		round = -1;

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
		local = new Player("Player", clientId);
		players.put("local", local);
		// if(host){
		comp = new Player("Comp", 0);
		players.put("0", comp);
		// }

		Player temp = new Player("temp", -1);
		players.put("scratch", temp);

		// localPlayer = new Ship("classic",
		// myGame.playerColor,width/2,height/2);

		// entities.put(localPlayer.getId(), localPlayer);
	}

	/**
	 * NOTE: The call to each entities update at the method is at the bottom of
	 * one of the wrapping loops.
	 * 
	 * @param delta
	 */
	public static void update(float delta) {
		bulletCooldown += Gdx.graphics.getDeltaTime();

		int mod = 0;

		// asteroids
		if (getAsteroids().size() <= 0) {
			if (delay < 5) {
				display = true;
				delay = delay + delta;
			} else {
				if (host) {
					if (myGame.multiplayer) {
						mod = (myGame.gameSize * 15);
					}
					if (mod == 0)
						mod = 1;
					round++;
					for (int i = 0; i < myGame.difficulty * mod; i++) {
						while (spawnBroid() == -1); // lols
					}
				}
				delay = 0;
				display = false;
			}

		}

		// viewport logic
		if ((local.getShip().getX() - viewPortX) / widthScreen > (1 - myGame.bounds)) {
			if (viewPortX < width - widthScreen) {
				float target = (((local.getShip().getX() - viewPortX) / widthScreen) - (1 - myGame.bounds))
						/ (myGame.bounds);
				adjViewPortX(10 * target);
			}
		}

		if ((local.getShip().getX() - viewPortX) / widthScreen < myGame.bounds) {
			if (viewPortX > 0) {
				float target = ((myGame.bounds - (local.getShip().getX() - viewPortX)
						/ widthScreen))
						/ (myGame.bounds);
				adjViewPortX(-10 * target);
			}
		}

		if ((local.getShip().getY() - viewPortY) / heightScreen > (1 - myGame.bounds)) {
			if (viewPortY < height - heightScreen) {
				float target = (((local.getShip().getY() - viewPortY) / heightScreen) - (1 - myGame.bounds))
						/ (myGame.bounds);
				adjViewPortY(10 * target);
			}
		}

		if ((local.getShip().getY() - viewPortY) / heightScreen < myGame.bounds) {
			if (viewPortY > 0) {
				float target = ((myGame.bounds - (local.getShip().getY() - viewPortY)
						/ heightScreen))
						/ (myGame.bounds);
				adjViewPortY(-10 * target);
			}
		}

		// viewport in relation to ship
		if (local.getShip().getX() < -4) { // make it the size of the ship
			viewPortX = width - widthScreen;
		}

		if (local.getShip().getX() > width + 4) {
			viewPortX = 0;
		}

		if (local.getShip().getY() < -4) {
			viewPortY = height - heightScreen;
		}

		if (local.getShip().getY() > height + 4) {
			viewPortY = 0;
		}

		// Screen wrapping
		for (Player p : players.values()) {
			for (Entity E : getEntities(p)) {
				if (E.getX() + (E.getSize() / 2f) < 0) { // make it the size of
															// the ship
					E.teleport(width + (E.getSize() / 2f), E.getY());
				}

				if (E.getX() - (E.getSize() / 2f) > width) {
					E.teleport(-(E.getSize() / 2f), E.getY());
				}

				if (E.getY() + (E.getSize() / 2f) < 0) {
					E.teleport(E.getX(), height + (E.getSize() / 2f));
				}

				if (E.getY() - (E.getSize() / 2f) > height) {
					E.teleport(E.getX(), -(E.getSize() / 2f));
				}
				E.update(); // THIS IS THE UPDATE! DO NOT PUT ELSEWHERE. Or at
							// least make
				// sure that there is only one.
			}

		}
		cleanEntities();
		local.getShip().setThrust(false);
		local.getShip().setShooting(false);
		world.step(delta, 1, 8);
		
		//System.out.println(local.getShip());
		//System.out.println(local.getShip().getBody());
	}

	public static int spawnBroid() {
		float x = (float) (CoreLogic.getWidth() * Math.random());
		float y = (float) (CoreLogic.getHeight() * Math.random());
		float dir = (float) (Math.PI * Math.random());

		// Prevent spawning on the player(s)
		for (Ship S : getShips()) {
			if (local.getShip().getX() - 16 <= x
					&& x <= local.getShip().getX() + 16) {
				return -1; // lols Lazy logic TODO make better lazy logic
			}
			if (local.getShip().getY() - 16 <= x
					&& x <= local.getShip().getY() + 16) {
				return -1;
			}
		}

		Asteroid roid = new Asteroid("large", getComp().nextId(), getComp(),
				myGame.gameColor, x, y);

		float initForce = (float) (4000 + (2000 * Math.random()));
		x = (float) (initForce * Math.cos(dir) * ((round / 10) + 1f));
		y = (float) (initForce * Math.sin(dir) * ((round / 10) + 1f));

		Vector2 f = roid.getBody().getWorldVector(new Vector2(x, y));
		Vector2 p = roid.getBody().getWorldPoint(
				roid.getBody().getLocalCenter());
		roid.getBody().applyForce(f, p);

		float spin = (float) (300 + (250 * Math.random()));
		if (Math.random() >= 0.5f)
			spin *= -1;

		roid.getBody().applyTorque(spin);

		comp.getEntitiesMap().put(roid.getId(), roid);
		return 0;
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
			local.getShip().getBody().applyTorque(500.0f);
		} else if (in.equals("right")) {
			local.getShip().getBody().applyTorque(-500.0f);
		}

		if (in.equals("backward")) {
			Vector2 f = local.getShip().getBody()
					.getWorldVector(new Vector2(0.0f, 30.0f));
			Vector2 p = local.getShip().getBody().getWorldCenter();
			local.getShip().getBody().applyForce(f, p);
		}

		if (in.equals("shoot")) {
			if (bulletCooldown >= 0.2f) {
				float dir = local.getShip().getAngle() - 90.0f;

				float x = (float) (local.getShip().getX() + (2.805 * Math
						.cos(Math.toRadians(dir))));
				float y = (float) (local.getShip().getY() + (2.805 * Math
						.sin(Math.toRadians(dir))));

				Bullet shot = new Bullet("bullet", getSelf().nextId(),
						getSelf(), dir, x, y);
				local.getEntitiesMap().put(shot.getId(), shot);
				bulletCooldown = 0;
				local.getShip().setShooting(true);
			}
		}

		if (in.equals("forward")) {
			Vector2 f = local.getShip().getBody()
					.getWorldVector(new Vector2(0.0f, -100.0f));
			Vector2 p = local
					.getShip()
					.getBody()
					.getWorldPoint(
							local.getShip().getBody().getLocalCenter()
									.add(new Vector2(0.0f, 0.0f)));
			local.getShip().getBody().applyForce(f, p);
			local.getShip().setThrust(true);
		}

	}

	/**
	 * Returns the map of all entities.
	 * 
	 * @return Entities Map
	 */
	public static Iterable<Entity> getEntities(Player p) {
		return p.getEntities();
	}

	/**
	 * Returns a map of all Ships
	 * 
	 * @return Map of Ships
	 */
	public static LinkedList<Ship> getShips() {
		LinkedList<Ship> ships = new LinkedList<Ship>();
		for (Player p : players.values()) {
			for (Entity entity : getEntities(p)) {
				if (entity.getEnt().equals("ship")) {
					ships.add((Ship) entity);
				}
			}
		}

		return ships;
	}

	/**
	 * Returns a map of all Asteroids
	 * 
	 * @return Map of Asteroids
	 */
	public static LinkedList<Asteroid> getAsteroids() {
		LinkedList<Asteroid> asteroids = new LinkedList<Asteroid>();
		for (Player p : players.values()) {
			for (Entity entity : getEntities(p)) {
				if (entity.getEnt().equals("asteroid")) {
					asteroids.add((Asteroid) entity);
				}
			}
		}

		return asteroids;
	}

	/**
	 * Returns a map of all Bullets
	 * 
	 * @return Map of Bullets
	 */
	public static LinkedList<Bullet> getBullets() {
		LinkedList<Bullet> bullets = new LinkedList<Bullet>();
		for (Player p : players.values()) {
			for (Entity entity : getEntities(p)) {
				if (entity.getEnt().equals("bullet")) {
					bullets.add((Bullet) entity);
				}
			}
		}

		return bullets;
	}

	public static LinkedList<Entity> getAllEntities() {
		LinkedList<Entity> entities = new LinkedList<Entity>();
		for (Player p : players.values()) {
			for (Entity entity : getEntities(p)) {

				entities.add(entity);

			}
		}
		return entities;
	}

	/**
	 * Returns the ship for the local player.
	 * 
	 * @return Local player ship
	 */
	public static Ship getLocalShip() {
		return local.getShip();
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
		System.out.println(ent);
		if (!rmEntities.contains(ent))
			rmEntities.add(ent);
	}

	public static void setClientId(int id) {
		clientId = id;
	}

	public static void cleanEntities() {
		for (Entity i : rmEntities) {
			for (Player p : players.values()) {
				if (p.getEntitiesMap().containsKey(i.getId())) {
					if (i.equals("ship")) {
						if (!myGame.multiplayer) {
							getSelf().modLives(-1);
						}
					}
					p.getEntitiesMap().remove(i.getId());
					world.destroyBody(i.getBody());
					i.destroy();
				}
			}
		}
		rmEntities.clear();
	}

	public static void dispose() {
		// world.dispose();
		// entities.clear();
		// rmEntities.clear();
		// local.getShip().destroy();
	}

	public static Player getComp() {
		return players.get("0");
	}

	public static Player getScratch() {
		return players.get("scratch");
	}

	public static Player getSelf() {
		return players.get("local");
	}

	public static Player getLocal() {
		return local;
	}

	public static boolean getRoundBool() {
		return display;
	}

	public static int getRound() {
		return round;
	}
}
