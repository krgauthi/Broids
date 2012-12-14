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
	 * This method gets called at the beginning of a contact, and deals with
	 * getting the Entities involved in the collision.
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

	/**
	 * This method determines the types of the Entities involved in the collision, and
	 * directs the logic flow to the appropriate methods.
	 * 
	 * @param eA
	 * 			The first Entity
	 * @param eB
	 * 			The second Entity
	 */
	public static void entityContact(Entity eA, Entity eB) {
		if (eA == null || eB == null) {
			return;
		}

		// Multiplayer
		if (CoreLogic.multiplayer && CoreLogic.isHost()) {
			Net.collision(eA, eB);
		}

		// Single Player
		else if (!CoreLogic.getGame().multiplayer) {
			// Ship-Asteroid
			if (eA instanceof Ship && eB instanceof Asteroid && !((Ship) eA).isInvincible()) {
				CollisionLogic.shipDanger(eA);
			}
			// Asteroid-Ship
			if (eA instanceof Asteroid && eB instanceof Ship && !((Ship) eB).isInvincible()) {
				CollisionLogic.shipDanger(eB);
			}

			// Bullet-Asteroid
			if (eA instanceof Bullet && eB instanceof Asteroid) {
				CollisionLogic.bulletAsteroid(eA, eB.getPoints());
				CollisionLogic.asteroidBullet(eB);
			}

			// Asteroid-Bullet
			if (eA instanceof Asteroid && eB instanceof Bullet) {
				CollisionLogic.bulletAsteroid(eB, eA.getPoints());
				CollisionLogic.asteroidBullet(eA);
			}
			
			// Ship-Ship
			//	if (eA instanceof Ship && eB instanceof Ship) {
			//		CollisionLogic.shipShip()
			//	}
		}
	}

	/**
	 * This method is unused.
	 */
	@Override
	public void endContact(Contact contact) {

	}

	/**
	 * This method simply determines if the Entities in contact should collide or pass through each other.
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

		// Collisions to ignore: invincible ships, bullets, and dust.
		if ((eA instanceof Ship && ((Ship) eA).isInvincible()) || (eB instanceof Ship && ((Ship) eB).isInvincible()) || eA instanceof Bullet || eB instanceof Bullet || eA instanceof Dust || eB instanceof Dust) {

			contact.setEnabled(false);
		}
	}

	/**
	 * This method is unused.
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	/**
	 * Called to remove Bullets in Bullet-Asteroid collisions.
	 * 
	 * @param bullet
	 * 			The Bullet in question
	 * @param score
	 * 			The point value of the Asteroid
	 */
	public static void bulletAsteroid(Entity bullet, int score) {
		bullet.getOwner().modScore(score);
		CoreLogic.removeEntity(bullet);
	}

	/**
	 * Called to remove Asteroids in Bullet-Asteroid collisions.
	 * @param asteroid
	 * 			The Asteroid in question
	 */
	public static void asteroidBullet(Entity asteroid) {
		CoreLogic.removeEntity(asteroid);
	}

	/**
	 * Determines damage in Ship collisions.
	 * 
	 * @param ship
	 * 			The Ship being damaged.
	 */
	public static void shipDanger(Entity ship) {
		if (CoreLogic.multiplayer) {

			ship.getOwner().takeDamage(20);
			Net.modifyPlayer(ship.getOwner());
		} else
			CoreLogic.removeEntity(ship);
	}

//	public static void shipShip(Entity shipA, Entity shipB) {
//		
//	}
	
	/**
	 * Removes the Bullet in Bullet-Ship collisions
	 * 
	 * @param bullet
	 * 			The Bullet in question
	 * @param score
	 * 			The point value being awarded.
	 */
	public static void bulletShip(Entity bullet, int score) {
		bullet.getOwner().modScore(score);
		CoreLogic.removeEntity(bullet);
	}

	/**
	 * Calculates damage done to Entities based on their relative velocities.
	 * 
	 * @param A
	 * 			The first Body
	 * @param B
	 * 			The second Body
	 * @return int
	 * 			The damage to be dealt to each Entity.
	 */
	public static int calculateDamage(Body A, Body B) {
		float x = A.getLinearVelocity().x - B.getLinearVelocity().x;
		float y = A.getLinearVelocity().y - B.getLinearVelocity().y;
		float vel = (float) Math.sqrt(x * x + y * y);
		int damages = (int) Math.round(vel * vel);
		damages = (int) Math.round(Math.pow(damages, 0.48) * Math.log10(B.getMass() * 10) / 2);

		// int[] damages = {Math.round((B.getMass() * vel*vel)/2),
		// Math.round((A.getMass() * vel*vel)/2)};

		return damages;
	}
}
