package com.Broders.Logic;

import java.util.HashMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.Broders.Entities.Entities;
import com.Broders.Entities.EntityType;
import com.Broders.Entities.Ship;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

public class CoreLogic {
	
	private static World world;
	private static OrderedMap<String, Entities> entityMap;
	private static float width;
	private static float height;
	
	private CoreLogic(){};
	
	public static void initCore(){
		Vector2 gravity = new Vector2(0.0f, 0.0f);
		world = new World(gravity, false);
		entityMap = new OrderedMap<String, Entities>();
			
		//make these scale to the aspect ratio
		width = 160f;
		height = 90f;
		
		/* just putting these here as an example.
		 * entity IDs will be of the following format:
		 * EntityID + TypeID + ClientID + InstanceID = 00 00 000 0000
		 */
		String clientID = "000";
		String instanceID = "0000";
		Ship player = new Ship(clientID + instanceID, EntityType.SHIP);
		entityMap.put("player", player);
	}
	
	public static void execute(float delta, InputDir in){
		
		
		if(in.equals("forward")){
			Vector2 f = entityMap.get("player").getBody().getWorldVector(new Vector2(0.0f, -30.0f));
			Vector2 p = entityMap.get("player").getBody().getWorldPoint(entityMap.get("player").getBody().getLocalCenter().add(new Vector2(0.0f,0.0f)));
			entityMap.get("player").getBody().applyForce(f, p);
			((Ship)entityMap.get("player")).setThrust(true);
		}else if(in.equals("backward")){
			Vector2 f = entityMap.get("player").getBody().getWorldVector(new Vector2(0.0f, 30.0f));
			Vector2 p = entityMap.get("player").getBody().getWorldCenter();
			entityMap.get("player").getBody().applyForce(f, p);
		}else{
			((Ship)entityMap.get("player")).setThrust(false);
		}
		
		if(in.equals("left")){
			entityMap.get("player").getBody().applyTorque(20.0f);
		}else if(in.equals("right")){
			entityMap.get("player").getBody().applyTorque(-20.0f);
		}else{
			
		}
		
		// find a way to get the world.step call back into core logic
		//world.step(delta, 3, 8);

	}
	
	public static OrderedMap<String, Entities> getEntityMap(){
		return entityMap;
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
