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
		
		CollisionLogic.entityContact(eA, eB);
	}
	
	public static void entityContact(Entity eA, Entity eB) {
		if (eA == null || eB == null) {
			return;
		}

		if (CoreLogic.getGame().multiplayer && CoreLogic.getHost()) {
			// TODO: Send frame
		}
		
		if (!CoreLogic.getGame().multiplayer || CoreLogic.getHost()) {
			// Ship-Asteroid
			if (eA instanceof Ship && eB instanceof Asteroid && !((Ship) eA).isInvincible()) {
				CollisionLogic.shipAsteroid(eA, eB);
			}
			// Asteroid-Ship
			if (eA instanceof Asteroid && eB instanceof Ship && !((Ship) eB).isInvincible()) {
				CollisionLogic.shipAsteroid(eB, eA);
			}
	
			// Bullet-Asteroid
			if (eA instanceof Bullet && eB instanceof Asteroid) {
				CollisionLogic.bulletAsteroid(eA, eB);
			}
	
			// Asteroid-Bullet
			if (eA instanceof Asteroid && eB instanceof Bullet) {
				CollisionLogic.bulletAsteroid(eB, eA);
			}
	
			// Ship-Bullet
	
			// Bullet-Ship
		}
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
	
	public static void shipAsteroid(Entity ship, Entity asteroid){
		CoreLogic.removeEntity(ship);
		// Uncomment if we want the asteroid to get destroyed
		// CoreLogic.removeEntity(asteroid);
		
		
	}
	
	public static void bulletAsteroid(Entity bullet, Entity asteroid){
		CoreLogic.removeEntity(bullet);
		CoreLogic.removeEntity(asteroid);

		// Call score method for the player here
		bullet.getOwner().modScore(asteroid.getPoints());
		
	}
	
	public static void shipBullet(Entity ship, Entity bullet){
		
	}
}
