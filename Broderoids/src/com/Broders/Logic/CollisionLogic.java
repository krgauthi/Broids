package com.Broders.Logic;

import com.Broders.Entities.Entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionLogic implements ContactListener {

	public CollisionLogic() {
	}

	/**
	 * This method gets called
	 */
	@Override
	public void beginContact(Contact contact) {
		String sA = "";
		String sB = "";
		Body bA = contact.getFixtureA().getBody();
		if (bA.getUserData() != null) {
			sA = ((Entity) bA.getUserData()).getEnt();
		}
		Body bB = contact.getFixtureB().getBody();
		if (bB.getUserData() != null) {
			sB = ((Entity) bB.getUserData()).getEnt();
		}
		
		// Ship-Asteroid
		if (sA.equals("ship") && sB.equals("asteroid")) {
			CoreLogic.removeEntity(((Entity) bA.getUserData()));
			//CoreLogic.removeEntity(((Entity) bB.getUserData()));
		}
		// Asteroid-Ship
		if (sA.equals("asteroid") && sB.equals("ship")) {
			//CoreLogic.removeEntity(((Entity) bB.getUserData()));
			CoreLogic.removeEntity(((Entity) bA.getUserData()));
		}
		
		// Bullet-Asteroid
		if (sA.equals("bullet") && sB.equals("asteroid")) {
			CoreLogic.removeEntity(((Entity) bA.getUserData()));
			CoreLogic.removeEntity(((Entity) bB.getUserData()));
			
			//Call score method for the player here
			((Entity) bA.getUserData()).getOwner().modScore(((Entity) bB.getUserData()).getPoints());
		}

		// Asteroid-Bullet
		if (sA.equals("asteroid") && sB.equals("bullet")) {
			CoreLogic.removeEntity(((Entity) bB.getUserData()));
			CoreLogic.removeEntity(((Entity) bA.getUserData()));
			
			//Call score method for the player here
			((Entity) bB.getUserData()).getOwner().modScore(((Entity) bA.getUserData()).getPoints());
		}

		// Ship-Bullet

		// Bullet-Ship
	}

	/**
	 * 
	 */
	@Override
	public void endContact(Contact contact) {
		String sA = "";
		String sB = "";
		Body bA = contact.getFixtureA().getBody();
		if (bA.getUserData() != null) {
			sA = ((Entity) bA.getUserData()).getEnt();
		}
		Body bB = contact.getFixtureB().getBody();
		if (bB.getUserData() != null) {
			sB = ((Entity) bB.getUserData()).getEnt();
		}
		// Bullet-Asteroid
		if (sA.equals("bullet") && sB.equals("asteroid")) {
			contact.setEnabled(false);
		}

		// Asteroid-Bullet
		if (sA.equals("asteroid") && sB.equals("bullet")) {
			contact.setEnabled(false);
		}
	}

	/**
	 * 
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	/**
	 * 
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
