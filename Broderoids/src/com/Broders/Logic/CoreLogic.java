package com.Broders.Logic;

import java.util.HashMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.Broders.Entities.Asteroid;
import com.Broders.Entities.Bullet;
import com.Broders.Entities.Entity;
import com.Broders.Entities.EntityType;
import com.Broders.Entities.Ship;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * 
 * @author ntpeters
 *
 */
public class CoreLogic {
<<<<<<< HEAD
	private World world;
	
	//This is for testing purposes only
	private Body testShip;
	
	private Body ground;
	
	private float width;
	private float height;
	
	public CoreLogic(World w){
		world = w;
	}
	
	public void initCore(){
	
		//make these scale to the aspect ratio
		width = 160f;
		height = 90f;
		
		//this block should set up the ship
		/*
		 * You will notice I commented out a bunch below.  We shouldn't
		 * need two transforms/fixtures for the ship.  If you look at the
		 * example, their ship is built from two triangles, with two different
		 * densities.  I believe the only reason behind the two triangles was
		 * to show off having 2 sides with different densities.  As a result,
		 * our ship only needs to be one fixture, in the shape of a triangle
		 * that I defined below.  Info on code that I shuffled around is in
		 * a comment block in GameScreen, check that out too.
		 * 
		 * -Rinkus
		 */
		{
//			Transform xf1 = new Transform();
//			xf1.setRotation(0.3524f * MathUtils.PI);
			//need to figure out an alternative way to do the following:
			//Mat22.mulToOut(xf1.getRotation(), new Vector2(1.0f, 0.0f), xf1.getPosition());
			
//			vertices[0] = xf1.mul(new Vector2(-1.0f, 0.0f));
//			vertices[1] = xf1.mul(new Vector2(1.0f, 0.0f));
//			vertices[2] = xf1.mul(new Vector2(0.0f, 0.5f));			
			Vector2 vertices[] = new Vector2[3];
			vertices[0] = new Vector2(0.0f, 1.0f);
			vertices[1] = new Vector2(0.0f, -1.0f);
			vertices[2] = new Vector2(0.44f, 0.0f);
			
			PolygonShape poly1 = new PolygonShape();
			poly1.set(vertices);
			
			FixtureDef sd1 = new FixtureDef();
			sd1.shape = poly1;
			sd1.density = 6.0f;
			
//			Transform xf2 = new Transform();
//			xf2.setRotation(-0.3524f * MathUtils.PI);
//			// same here:
//			//Mat22.mulToOut(xf2.getRotation(), new Vector2(-1.0f, 0.0f), xf2.getPosition());
//			
//			vertices[0] = xf2.mul(new Vector2(-1.0f, 0.0f));
//			vertices[1] = xf2.mul(new Vector2(1.0f, 0.0f));
//			vertices[2] = xf2.mul(new Vector2(0.0f, 0.5f));
//			
//			PolygonShape poly2 = new PolygonShape();
//			poly2.set(vertices);
//			
//			FixtureDef sd2 = new FixtureDef();
//			sd2.shape = poly2;
//			sd2.density = 2.0f;
			
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.angularDamping = 5.0f;
			bd.linearDamping = 0.1f;
			
			bd.position.set(0.0f, 0.0f);
			bd.angle = MathUtils.PI;
			bd.allowSleep = false;
			testShip = world.createBody(bd);
			testShip.createFixture(sd1);
//			testShip.getBody().createFixture(sd2);
=======

	private static World world;
	private static OrderedMap<String, Entity> entities;
	private static Ship localPlayer;
	private static float width;
	private static float height;

	private CoreLogic(){};

	/**
	 * Initializes the game core for use.
	 */
	public static void initCore(){
		Vector2 gravity = new Vector2(0.0f, 0.0f);
		world = new World(gravity, false);
		entities = new OrderedMap<String, Entity>();

		//make these scale to the aspect ratio
		width = 160f;
		height = 90f;

		/* Just putting these here as an example.
		 * entity IDs will be of the following format:
		 * EntityID + TypeID + ClientID + InstanceID = 00 00 000 0000
		 */
		String clientID = "000";		// possibly let server handle clientID generation somehow?
		String instanceID = "0000";		// check map to see how many of this type of entity already exist
		localPlayer = new Ship(clientID + instanceID, EntityType.SHIP);
		entities.put(localPlayer.toString(), localPlayer);
	}

	/**
	 * Executes the game logic.
	 * 
	 * @param	delta	Delta time passed from LibGDX
	 * @param	in		The input direction
	 */
	public static void execute(float delta, InputDir in){
		
		if(in.equals("forward")){
			Vector2 f = localPlayer.getBody().getWorldVector(new Vector2(0.0f, -30.0f));
			Vector2 p = localPlayer.getBody().getWorldPoint(localPlayer.getBody().getLocalCenter().add(new Vector2(0.0f,0.0f)));
			localPlayer.getBody().applyForce(f, p);
		}
		
		if(in.equals("backward")){
			Vector2 f = localPlayer.getBody().getWorldVector(new Vector2(0.0f, 30.0f));
			Vector2 p = localPlayer.getBody().getWorldCenter();
			localPlayer.getBody().applyForce(f, p);
		}

		if(in.equals("left")){
			localPlayer.getBody().applyTorque(20.0f);
		}else if(in.equals("right")){
			localPlayer.getBody().applyTorque(-20.0f);
		}else{

		}
		
		//Screen wrapping
		if(localPlayer.getX() < -6){			//make it the size of the ship
			localPlayer.teleport(width+5, localPlayer.getY());
		}
		
		if(localPlayer.getX() > width+6){
			localPlayer.teleport(-5f, localPlayer.getY());
>>>>>>> fb70709187f3d48340d633fada0daba95d50c6eb
		}
		
		if(localPlayer.getY() < -6){
			localPlayer.teleport(localPlayer.getX(), height+5);
		}
		
<<<<<<< HEAD
		if(in.equals("forward")){
			Vector2 f = testShip.getWorldVector(new Vector2(0.0f, -30.0f));
			Vector2 p = testShip.getWorldPoint(testShip.getLocalCenter().add(new Vector2(0f,2f)));
			testShip.applyForce(f, p);
		}else if(in.equals("backward")){
			Vector2 f = testShip.getWorldVector(new Vector2(0.0f, 30.0f));
			Vector2 p = testShip.getWorldCenter();
			testShip.applyForce(f, p);
		}else if(in.equals("left")){
			testShip.applyTorque(10.0f);
		}else if(in.equals("right")){
			testShip.applyTorque(-10.0f);
=======
		if(localPlayer.getY() > height+6){
			localPlayer.teleport(localPlayer.getX(), -5f);
>>>>>>> fb70709187f3d48340d633fada0daba95d50c6eb
		}
		
		// find a way to get the world.step call back into core logic in a way that it won't recreate the velocity bug
		//world.step(delta, 3, 8);

	}

	/**
	 * Returns the map of all entities.
	 * 
	 * @return	Entities Map
	 */
	public static OrderedMap<String, Entity> getEntities(){
		return entities;
	}
	
<<<<<<< HEAD
	//this method is for testing purposes only
	public Body getShip(){
		return this.testShip;
=======
	/**
	 * Returns a map of all Ships
	 * 
	 * @return Map of Ships
	 */
	public static OrderedMap<String, Ship> getShips(){
		OrderedMap<String, Ship> ships = new OrderedMap<String, Ship>();
		
		for(Entity entity : entities.values()){
			if(entity.getType().equals(EntityType.SHIP)){
				ships.put(entity.toString(), (Ship) entity);
			}
		}
		
		return ships;
>>>>>>> fb70709187f3d48340d633fada0daba95d50c6eb
	}
	
	/**
	 * Returns a map of all Asteroids
	 * 
	 * @return Map of Asteroids
	 */
	public static OrderedMap<String, Asteroid> getAsteroids(){
		OrderedMap<String, Asteroid> asteroids = new OrderedMap<String, Asteroid>();
		
		for(Entity entity : entities.values()){
			if(entity.getType().equals(EntityType.ASTEROID)){
				asteroids.put(entity.toString(), (Asteroid) entity);
			}
		}
		
		return asteroids;
	}
	
	/**
	 * Returns a map of all Bullets
	 * 
	 * @return Map of Bullets
	 */
	public static OrderedMap<String, Bullet> getBullets(){
		OrderedMap<String, Bullet> bullets = new OrderedMap<String, Bullet>();
		
		for(Entity entity : entities.values()){
			if(entity.getType().equals(EntityType.BULLET)){
				bullets.put(entity.toString(), (Bullet) entity);
			}
		}
		
		return bullets;
	}

	/**
	 * Returns the ship for the local player.
	 * 
	 * @return	Local player ship
	 */
	public static Ship getLocalShip(){
		return localPlayer;
	}
	
	/**
	 * Returns the world.
	 * 
	 * @return	The world
	 */
	public static World getWorld(){
		return world;
	}

	/**
	 * Gets the world width.
	 * 
	 * @return World width
	 */
	public static float getWidth(){
		return width;
	}

	/**
	 * Gets the world height.
	 * 
	 * @return	World height
	 */
	public static float getHeight(){
		return height;
	}

}
