package com.Broders.Logic;

import java.util.HashMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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

public class CoreLogic {

	private static World world;
	private static OrderedMap<String, Entity> entities;
	private static Ship localPlayer;
	private static float width;
	private static float height;

	private CoreLogic(){};

	public static void initCore(){
		Vector2 gravity = new Vector2(0.0f, 0.0f);
		world = new World(gravity, false);
		entities = new OrderedMap<String, Entity>();

		//make these scale to the aspect ratio
		width = 160f;
		height = 90f;

		/* just putting these here as an example.
		 * entity IDs will be of the following format:
		 * EntityID + TypeID + ClientID + InstanceID = 00 00 000 0000
		 */
		String clientID = "000";
		String instanceID = "0000";
		localPlayer = new Ship(clientID + instanceID, EntityType.SHIP);
		entities.put(localPlayer.toString(), localPlayer);
	}

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
		}
		
		if(localPlayer.getY() < -6){
			localPlayer.teleport(localPlayer.getX(), height+5);
		}
		
		if(localPlayer.getY() > height+6){
			localPlayer.teleport(localPlayer.getX(), -5f);
		}
		
		// find a way to get the world.step call back into core logic
		//world.step(delta, 3, 8);

	}

	public static OrderedMap<String, Entity> getEntities(){
		return entities;
	}
	
	public static OrderedMap<String, Ship> getShips(){
		OrderedMap<String, Ship> ships = new OrderedMap<String, Ship>();
		
		for(Entity entity : entities.values()){
			if(entity.getType().equals(EntityType.SHIP)){
				ships.put(entity.toString(), (Ship) entity);
			}
		}
		
		return ships;
	}

	public static Ship getLocalShip(){
		return localPlayer;
	}
	
	public static World getWorld(){
		return world;
	}

	public static float getWidth(){
		return width;
	}

	public static float getHeight(){
		return height;
	}

}
