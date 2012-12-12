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

		//Multiplayer
		if (CoreLogic.multiplayer && CoreLogic.isHost()) {
			Net.collision(eA, eB);
		}

		//Single Player
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

		//Collisions to ignore: invincible ships and bullets
		if ((eA instanceof Ship && ((Ship)eA).isInvincible()) ||
				(eB instanceof Ship && ((Ship)eB).isInvincible()) ||
				eA instanceof Bullet || eB instanceof Bullet	||
				eA instanceof Dust 	 || eB instanceof Dust)
		{

			contact.setEnabled(false);
		}
	}

	/**
	 * 
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	public static void bulletAsteroid(Entity bullet, int score){
		bullet.getOwner().modScore(score);
		CoreLogic.removeEntity(bullet);
	}

	public static void asteroidBullet(Entity asteroid){
		CoreLogic.removeEntity(asteroid);
	}

	public static void shipDanger(Entity ship) {
		if (CoreLogic.multiplayer){
			ship.getOwner().takeDamage(-10);
			Net.modifyPlayer(ship.getOwner());
		}
		else
			CoreLogic.removeEntity(ship);
	}
	
	public static void bulletShip(Entity bullet, int score) {
		bullet.getOwner().modScore(score);
		CoreLogic.removeEntity(bullet);
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
		int damages = (int) Math.round(vel*vel);
		damages = (int) Math.round(Math.pow(damages, 0.48) * Math.log10(B.getMass()*10)/2);

		//int[] damages = {Math.round((B.getMass() * vel*vel)/2),
		//				Math.round((A.getMass() * vel*vel)/2)};

		return damages;
	}
}
