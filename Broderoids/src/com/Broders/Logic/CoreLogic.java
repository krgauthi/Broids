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
		}
		//end example code
	}
	
	public void execute(float delta, InputDir in){
		
		
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
		}
		
		world.step(delta, 3, 8);
	}
	
	//this method is for testing purposes only
	public Body getShip(){
		return this.testShip;
	}
	
	public World getWorld(){
		return this.world;
	}
	
	public Body getGround(){
		return ground;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
}
