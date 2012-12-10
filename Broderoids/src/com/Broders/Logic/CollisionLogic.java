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
			Net.collision(eA, eB);
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
			if (eA instanceof Bullet && eB instanceof Ship) {
				CollisionLogic.shipBullet(eB, eA);
			}

			// Bullet-Ship
			if (eA instanceof Ship && eB instanceof Bullet) {
				CollisionLogic.shipBullet(eA, eB);
			}

			//Ship-Ship, for multiplayer
			if (eA instanceof Ship && eB instanceof Ship) {
				CollisionLogic.shipShip(eA, eB);
			}
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

		// Collisions to ignore. Only Asteroid-Asteroid and Ship-Asteroid
		// matter for singleplayer, Ship-Ship also relevant for multiplayer.
		if (!(	eA instanceof Asteroid && eB instanceof Asteroid ||
				eA instanceof Asteroid && eB instanceof Ship ||
				eA instanceof Ship && eB instanceof Asteroid) ||
				eA instanceof Ship && eB instanceof Ship) {

			contact.setEnabled(false);
		}
	}

	/**
	 * 
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	public static void shipAsteroid(Entity ship, Entity asteroid){

		if (CoreLogic.getGame().multiplayer) {
			Ship craft = (Ship) ship;

			//This is how the ship gets damage from asteroids. If you can think of a better way to
			//calculate it, please tell me, because this is bloody awful. -R
			int damages = calculateDamage(ship.getBody(), asteroid.getBody());
			damages = (int) Math.round(Math.pow(damages, 0.48) * Math.log10(asteroid.getBody().getMass()*10)/2);
			craft.getOwner().takeDamage(damages);

		} else {
			CoreLogic.removeEntity(ship);
		}
	}

	public static void bulletAsteroid(Entity bullet, Entity asteroid){
		CoreLogic.removeEntity(bullet);
		CoreLogic.removeEntity(asteroid);

		// Call score method for the player here
		bullet.getOwner().modScore(asteroid.getPoints());
	}

	public static void shipBullet(Entity ship, Entity bullet) {
		if (CoreLogic.getGame().multiplayer && !(ship.getOwner().equals(bullet.getOwner())))
			ship.getOwner().modHealth(-10);
	}
	
	public static void shipShip(Entity shipA, Entity shipB) {
		int damages = calculateDamage(shipA.getBody(), shipB.getBody());
		damages = 0 - (int) Math.round(Math.pow(damages, 0.55));
		shipA.getOwner().modHealth(damages);
		shipB.getOwner().modHealth(damages);
	}

	/**
	 * Calculates damage done to each entity based on their relative velocities.
	 * 
	 * @param A
	 * @param B
	 * @return int[]
	 * 			Index 0 is damage to Entity A, Index 1 is damage to Entity B
	 */
	public static int calculateDamage(Body A, Body B) {
		float x = A.getLinearVelocity().x - B.getLinearVelocity().x;
		float y = A.getLinearVelocity().y - B.getLinearVelocity().y;
		float vel = (float) Math.sqrt(x*x + y*y);

		//int[] damages = {Math.round((B.getMass() * vel*vel)/2),
		//				Math.round((A.getMass() * vel*vel)/2)};

		return Math.round(vel*vel);
	}
}
