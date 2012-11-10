package com.Broders.Logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.Broders.Entities.Asteroid;
import com.Broders.Entities.Bullet;
import com.Broders.Entities.Entity;
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

/**
 * 
 * @author ntpeters
 *
 */
public class CoreLogic {

	private static World world;
	private static ArrayList<Entity> entities;
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
	public static void initCore(BaseGame game){
		myGame = game;
		Vector2 gravity = new Vector2(0.0f, 0.0f);
		world = new World(gravity, false);
		entities = new ArrayList<Entity>();

		int gcd = gcd(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		widthScreen = Gdx.graphics.getWidth() / gcd * 10;
		heightScreen = Gdx.graphics.getHeight() / gcd * 10;

		if(game.multiplayer){
			width = 1000f;
			height = 1000f;
		}else{
			width = widthScreen;
			height = heightScreen;
		}
		
		viewPortX = (width/2)-(widthScreen/2f);
		viewPortY = (height/2)-(heightScreen/2f);

		/* forget this
		 * Just putting these here as an example.
		 * entity IDs will be of the following format:
		 * EntityID + TypeID + ClientID + InstanceID = 00 00 000 0000
		 */
		//String clientID = "000";		// possibly let server handle clientID generation somehow?
		//String instanceID = "0000";		// check map to see how many of this type of entity already exist
		localPlayer = new Ship("classic",myGame.playerColor);
		entities.add(localPlayer);
		
		for (int i=0; i<6; i++) {
			float x = (float) (CoreLogic.getWidth() * Math.random());
			float y = (float) (CoreLogic.getHeight() * Math.random());
			
			Asteroid roid = new Asteroid("large", x, y);
			entities.add(roid);//TODO CHANGE PLZ!!
		}
		
	}

	public static void update(float delta){

		if(!myGame.multiplayer){

			if(getAsteroids().size() <= 0){
				for(int i = 0; i < myGame.difficulty; i++){
					//Spawn Broids
				}
			}



		}

		//viewport logic
		if((localPlayer.getX()-viewPortX)/widthScreen > (1-myGame.bounds)){
			if(viewPortX < width-widthScreen){
				float target = (((localPlayer.getX()-viewPortX)/widthScreen)-(1-myGame.bounds))/(myGame.bounds);
				adjViewPortX(10*target);
			}
		}

		if((localPlayer.getX()-viewPortX)/widthScreen < myGame.bounds){
			if(viewPortX > 0){
				float target = ((myGame.bounds-(localPlayer.getX()-viewPortX)/widthScreen))/(myGame.bounds);
				adjViewPortX(-10*target);
			}
		}

		if((localPlayer.getY()-viewPortY)/heightScreen > (1-myGame.bounds)){
			if(viewPortY < height-heightScreen){
				float target = (((localPlayer.getY()-viewPortY)/heightScreen)-(1-myGame.bounds))/(myGame.bounds);
				adjViewPortY(10*target);
			}
		}

		if((localPlayer.getY()-viewPortY)/heightScreen < myGame.bounds){
			if(viewPortY > 0){
				float target = ((myGame.bounds-(localPlayer.getY()-viewPortY)/heightScreen))/(myGame.bounds);
				adjViewPortY(-10*target);
			}
		}



		//Screen wrapping
		if(localPlayer.getX() < -4){			//make it the size of the ship
			localPlayer.teleport(width+3, localPlayer.getY());
			viewPortX = width-widthScreen;
		}

		if(localPlayer.getX() > width+4){
			localPlayer.teleport(-3f, localPlayer.getY());
			viewPortX = 0;
		}

		if(localPlayer.getY() < -4){
			localPlayer.teleport(localPlayer.getX(), height+3);
			viewPortY = height-heightScreen;
		}

		if(localPlayer.getY() > height+4){
			localPlayer.teleport(localPlayer.getX(), -3f);
			viewPortY = 0;
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
			localPlayer.getBody().applyTorque(500.0f);				//20 was obnoxious on android device make this adjustable in settings?
		}else if(in.equals("right")){
			localPlayer.getBody().applyTorque(-500.0f);
		}

		if(in.equals("backward")){
			Vector2 f = localPlayer.getBody().getWorldVector(new Vector2(0.0f, 30.0f));
			Vector2 p = localPlayer.getBody().getWorldCenter();
			localPlayer.getBody().applyForce(f, p);
		}

		if(in.equals("shoot")){
			
			float dir = localPlayer.getAngle();
			float x = (float) (localPlayer.getY() + (4.1 * Math.sin(dir)));
			float y = (float) (localPlayer.getY() + (4.1 * Math.cos(dir)));

			Bullet shot = new Bullet("bullet", x, y, dir);
			entities.add(shot);
			
			Vector2 f = localPlayer.getBody().getWorldVector(new Vector2(0.0f, -5.0f));
			Vector2 p = localPlayer.getBody().getWorldPoint(shot.getBody().getLocalCenter().add(new Vector2(0.0f,0.0f)));
			localPlayer.getBody().applyForce(f, p);
			
			System.out.println("BZZZAP!!");
		}

		if(in.equals("forward")){
			Vector2 f = localPlayer.getBody().getWorldVector(new Vector2(0.0f, -35.0f));
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
	public static ArrayList<Entity> getEntities(){
		return entities;
	}

	/**
	 * Returns a map of all Ships
	 * 
	 * @return Map of Ships
	 */
	public static ArrayList<Ship> getShips(){
		ArrayList<Ship> ships = new ArrayList<Ship>();

		for(Entity entity : entities){
			if(entity.getEnt().equals("ship")){
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
	public static ArrayList<Asteroid> getAsteroids(){
		ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();

		for(Entity entity : entities){
			if(entity.getEnt().equals("asteroid")){
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
	public static ArrayList<Bullet> getBullets(){
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();

		for(Entity entity : entities){
			if(entity.getEnt().equals("bullet")){
				bullets.add((Bullet) entity);
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
	
	public static void removeEntity(Entity ent) {
		//TODO REmove entity from map/arraylist
		//entities.remove(ent.toString());
	}

}
