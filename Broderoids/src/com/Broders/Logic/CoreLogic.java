package com.Broders.Logic;

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
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.Json.Serializer;

public class CoreLogic {
	public World world;
	
	private Ship playerShip;
	
	private OrderedMap<String,Entities> EntityMap;
	
	Body ground;
	
	float width;
	float height;
	
	public CoreLogic(){
		
	}
	
	public void initCore(){
		
		Vector2 gravity = new Vector2(0.0f, 0.0f);
		world = new World(gravity, true);
		
		EntityMap = new OrderedMap<String, Entities>();
		playerShip = new Ship("player", EntityType.SHIP, world);
		
		EntityMap.put("player", playerShip);
		
		
		final float k_restitution = 0.4f;
		
		//make these scale to the aspect ratio
		width = 160f;
		height = 90f;
		
		//this block should set up the ship
		{
			Transform xf1 = new Transform();
			xf1.setRotation(0.3524f * MathUtils.PI);
			//need to figure out an alternative way to do the following:
			//Mat22.mulToOut(xf1.getRotation(), new Vector2(1.0f, 0.0f), xf1.getPosition());
			
			Vector2 vertices[] = new Vector2[3];
			vertices[0] = xf1.mul(new Vector2(-1.0f, 0.0f));
			vertices[1] = xf1.mul(new Vector2(1.0f, 0.0f));
			vertices[2] = xf1.mul(new Vector2(0.0f, 0.5f));
			
			PolygonShape poly1 = new PolygonShape();
			poly1.set(vertices);
			
			FixtureDef sd1 = new FixtureDef();
			sd1.shape = poly1;
			sd1.density = 4.0f;
			
			Transform xf2 = new Transform();
			xf2.setRotation(-0.3524f * MathUtils.PI);
			// same here:
			//Mat22.mulToOut(xf2.getRotation(), new Vector2(-1.0f, 0.0f), xf2.getPosition());
			
			vertices[0] = xf2.mul(new Vector2(-1.0f, 0.0f));
			vertices[1] = xf2.mul(new Vector2(1.0f, 0.0f));
			vertices[2] = xf2.mul(new Vector2(0.0f, 0.5f));
			
			PolygonShape poly2 = new PolygonShape();
			poly2.set(vertices);
			
			FixtureDef sd2 = new FixtureDef();
			sd2.shape = poly2;
			sd2.density = 2.0f;
			
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.angularDamping = 5.0f;
			bd.linearDamping = 0.1f;
			
			bd.position.set(width/2, height/2);   //center player ship
			bd.angle = MathUtils.PI;
			bd.allowSleep = false;
			playerShip.setBody(world.createBody(bd));
			playerShip.getBody().createFixture(sd1);
			playerShip.getBody().createFixture(sd2);
		}
		//end example code
	}
	
	public void execute(float delta, InputDir in){
		
		
		if(in.equals("forward")){
			Vector2 f = playerShip.getBody().getWorldVector(new Vector2(0.0f, -30.0f));
			Vector2 p = playerShip.getBody().getWorldPoint(playerShip.getBody().getLocalCenter().add(new Vector2(0f,2f)));
			playerShip.getBody().applyForce(f, p);
		}else if(in.equals("backward")){
			Vector2 f = playerShip.getBody().getWorldVector(new Vector2(0.0f, 30.0f));
			Vector2 p = playerShip.getBody().getWorldCenter();
			playerShip.getBody().applyForce(f, p);
		}else if(in.equals("left")){
			playerShip.getBody().applyTorque(10.0f);
		}else if(in.equals("right")){
			playerShip.getBody().applyTorque(-10.0f);
		}
		
		
	
		//world.step(delta, 3, 8);
	}
	
	//this method is for testing purposes only
	public Ship getShip(){
		return this.playerShip;
	}
	
	public World getWorld(){
		return this.world;
	}
	
	public Body getGround(){
		return ground;
	}
	
	public OrderedMap<String, Entities> getEntitiyMap(){
		return EntityMap;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
}
