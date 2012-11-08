package com.Broders.Logic;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
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
import com.Broders.mygdxgame.BaseGame;
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

	private static World world;
	private static OrderedMap<String, Entity> entities;
	private static Ship localPlayer;
	private static BaseGame myGame;
	
	private static float width;						//this is the map size
	private static float height;

	private static float widthScreen;				//screen size in meters
	private static float heightScreen;
	
	private static float viewPortX;
	private static float viewPortY;
	
	private CoreLogic(){};

	/**
	 * Initializes the game core for use.
	 */
	public static void initCore(BaseGame game){
		myGame = game;
		Vector2 gravity = new Vector2(0.0f, 0.0f);
		world = new World(gravity, false);
		entities = new OrderedMap<String, Entity>();

		
		//make these scale to the aspect ratio
		if(game.debugMode){
			width = 1000f;
			height = 1000f;
			
			widthScreen = 160f;
			heightScreen = 90f;
			
			viewPortX = (width/2)-(widthScreen/2f);
			viewPortY = (height/2) - (heightScreen/2f);
		}else{
			width = 160f;
			height = 90f;
			
			widthScreen = 160f;
			heightScreen = 90f;
			
			viewPortX = (width/2) - (widthScreen/2f);
			viewPortY = (height/2) - (heightScreen/2f);
		}
		
		
		

		/* Just putting these here as an example.
		 * entity IDs will be of the following format:
		 * EntityID + TypeID + ClientID + InstanceID = 00 00 000 0000
		 */
		String clientID = "000";		// possibly let server handle clientID generation somehow?
		String instanceID = "0000";		// check map to see how many of this type of entity already exist
		localPlayer = new Ship(clientID + instanceID, EntityType.SHIP,myGame.playerColor);
		entities.put(localPlayer.toString(), localPlayer);
	}

	public static void update(float delta){

		if(!myGame.multiplayer){

			if(getAsteroids().size <= 0){
				for(int i = 0; i < myGame.difficulty; i++){
					//Spawn Broids
				}
			}
			
			

		}

		//Screen wrapping
		if(localPlayer.getX() < -4){			//make it the size of the ship
			localPlayer.teleport(width+3, localPlayer.getY());
		}

		if(localPlayer.getX() > width+4){
			localPlayer.teleport(-3f, localPlayer.getY());
		}

		if(localPlayer.getY() < -4){
			localPlayer.teleport(localPlayer.getX(), height+3);
		}

		if(localPlayer.getY() > height+4){
			localPlayer.teleport(localPlayer.getX(), -3f);
		}

		localPlayer.setThrust(false);


		world.step(delta, 3, 8);

	}


	/**
	 * Executes the game logic.
	 * 
	 * @param	delta	Delta time passed from LibGDX
	 * @param	in		The input direction
	 */
	public static void execute(float delta, InputDir in){


		if(in.equals("left")){
			localPlayer.getBody().applyTorque(200.0f);				//20 was obnoxious on android device make this adjustable in settings?
		}else if(in.equals("right")){
			localPlayer.getBody().applyTorque(-200.0f);
		}

		if(in.equals("backward")){
			Vector2 f = localPlayer.getBody().getWorldVector(new Vector2(0.0f, 30.0f));
			Vector2 p = localPlayer.getBody().getWorldCenter();
			localPlayer.getBody().applyForce(f, p);
		}

		if(in.equals("shoot")){
			//TODO have the localplayer shoot a bullet
		}

		if(in.equals("forward")){
			Vector2 f = localPlayer.getBody().getWorldVector(new Vector2(0.0f, -30.0f));
			Vector2 p = localPlayer.getBody().getWorldPoint(localPlayer.getBody().getLocalCenter().add(new Vector2(0.0f,0.0f)));
			localPlayer.getBody().applyForce(f, p);
			localPlayer.setThrust(true);
		}else{
			localPlayer.setThrust(false);
		}


	}

	/**
	 * Returns the map of all entities.
	 * 
	 * @return	Entities Map
	 */
	public static OrderedMap<String, Entity> getEntities(){
		return entities;
	}

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
	
	public static float getWidthScreen(){
		return widthScreen;
	}
	
	public static float getHeightScreen(){
		return heightScreen;
	}
	
	public static float getViewPortX(){
		return viewPortX;
	}
	
	public static float getViewPortY(){
		return viewPortY;
	}
	
	public static void adjViewPortX(float adj){
		viewPortX = viewPortX + adj;
	}
	
	public static void adjViewPortY(float adj){
		viewPortY = viewPortY + adj;
	}

}
