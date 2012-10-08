package com.Broders.Logic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.Broders.Entities.EntityType;
import com.Broders.Entities.Ship;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json.Serializer;

public class CoreLogic {
	private World world;
	private Body groundBody;
	
	//This is for testing purposes only
	private Ship testShip;
	
	public CoreLogic(){
		
	}
	
	public void initCore(){
		testShip = new Ship("player", EntityType.SHIP);
		
		Vector2 gravity = new Vector2(0.0f, 0.0f);
		world = new World(gravity, true);
		BodyDef bodyDef = new BodyDef();
		groundBody = world.createBody(bodyDef);
		
		//example code pasted below for testing:
		
		// this block creates the 'ground'
		final float k_restitution = 0.4f;
		Body ground;
		{
			BodyDef bd = new BodyDef();
			bd.position.set(0.0f, 20.0f);
			ground = world.createBody(bd);
			
			PolygonShape shape = new PolygonShape();
			
			FixtureDef sd = new FixtureDef();
			sd.shape = shape;
			sd.density = 0.0f;
			sd.restitution = k_restitution;
			
			// sets up size of 'ground' ?
			shape.setAsBox(20.0f, 20.0f);
			ground.createFixture(sd);
			
		}
		
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
			
			bd.position.set(0.0f, 2.0f);
			bd.angle = MathUtils.PI;
			bd.allowSleep = false;
			testShip.setBody(world.createBody(bd));
			testShip.getBody().createFixture(sd1);
			testShip.getBody().createFixture(sd2);
		}
		//end example code
	}
	
	public void execute(double delta, InputDir in){
		
	}
	
	//this method is for testing purposes only
	public Ship getShip(){
		return this.testShip;
	}
}
