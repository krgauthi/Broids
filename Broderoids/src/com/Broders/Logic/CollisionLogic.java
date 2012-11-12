package com.Broders.Logic;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionLogic implements ContactListener {
	
	/**
	 * 
	 */
	@Override
	public void beginContact(Contact contact) {
		Body bA = contact.getFixtureA().getBody();
		Body bB = contact.getFixtureB().getBody();
		
		//General collision (testing only)
		if(bA.isBullet() || bB.isBullet()){
			System.out.println("found bullet collision");
			CoreLogic.destroyBody(bA);
			CoreLogic.destroyBody(bB);
		}
		else{
			System.out.println("found non-bullet collision");
//			CoreLogic.destroyBody(bA);
//			CoreLogic.destroyBody(bB);
		}
	}

	/**
	 * 
	 */
	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
