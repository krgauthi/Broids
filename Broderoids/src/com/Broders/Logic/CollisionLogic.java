package com.Broders.Logic;

import com.Broders.Entities.*;
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
		Entity eA = null;
		Entity eB = null;
		Body bA = contact.getFixtureA().getBody();
		if (bA.getUserData() != null) {
			eA = (Entity) bA.getUserData();
		}
		Body bB = contact.getFixtureB().getBody();
		if (bB.getUserData() != null) {
			eB = (Entity) bB.getUserData();
		}
		
		// Ship-Asteroid
		if (eA instanceof Ship && eB instanceof Asteroid && !((Ship) eA).isInvincible()) {
			CoreLogic.removeEntity(eA);
			// Uncomment if we want the asteroid to get destroyed
			// CoreLogic.removeEntity(((Entity) bB.getUserData()));
		}
		// Asteroid-Ship
		if (eA instanceof Asteroid && eB instanceof Ship && !((Ship) eB).isInvincible()) {
			// Uncomment if we want the asteroid to get destroyed
			// CoreLogic.removeEntity(((Entity) bB.getUserData()));
			CoreLogic.removeEntity(eB);
		}

		// Bullet-Asteroid
		if (eA instanceof Bullet && eB instanceof Asteroid) {
			CoreLogic.removeEntity(eA);
			CoreLogic.removeEntity(eB);

			// Call score method for the player here
			eA.getOwner().modScore(eB.getPoints());
		}

		// Asteroid-Bullet
		if (eA instanceof Asteroid && eB instanceof Bullet) {
			CoreLogic.removeEntity(eA);
			CoreLogic.removeEntity(eB);

			// Call score method for the player here
			eB.getOwner().modScore(eA.getPoints());
		}

		// Ship-Bullet

		// Bullet-Ship
	}

	/**
	 * 
	 */
	@Override
	public void endContact(Contact contact) {

	}

	/**
	 * 
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Entity eA = null;
		Entity eB = null;
		Body bA = contact.getFixtureA().getBody();
		if (bA.getUserData() != null) {
			eA = (Entity) bA.getUserData();
		}
		Body bB = contact.getFixtureB().getBody();
		if (bB.getUserData() != null) {
			eB = (Entity) bB.getUserData();
		}

		// Collisions to ignore (a.k.a. only asteroid-asteroid should bounce
		if (!(eA instanceof Asteroid && eB instanceof Asteroid)) {
			contact.setEnabled(false);
		}
	}

	/**
	 * 
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
