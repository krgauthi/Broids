package com.Broders.Logic;

import java.util.LinkedList;

import com.Broders.Entities.Entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * 
 * @author ejrinkus
 * 
 *         This class is our implementation of a contact listener. It is created
 *         and added to the world in CoreLogic and is used to "listen" for
 *         collisions. Always remember that collisions occur between fixtures
 *         and not bodies. The methods contained are to allow us to add extra
 *         functionality to collisions on top of (or instead of) what the solver
 *         does.
 * 
 */
public class CollisionLogic implements ContactListener {

	public CollisionLogic() {
	}

	/**
	 * This method gets called whenever there is a contact between two fixtures.
	 * Our code adds any extra functionality to collisions that doesn't happen
	 * during the physics solver. (aka, anything other than bouncing). Keep in
	 * mind that when the AABB boundaries (imaginary boxes surrounding the
	 * fixtures) intersect, the contact object is created, but isTouching is set
	 * to false. This method is not called until isTouching is set to true.
	 * 
	 * @param contact
	 *            This object contains all of the collision information
	 *            between the fixtures
	 */
	@Override
	public void beginContact(Contact contact) {
		/*
		 * Get the bodies and the entity types involved in the collision
		 */
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

		// Asteroid-Ship

		// Bullet-Asteroid (send both to removeEntity for destruction)
		if (sA.equals("bullet") && sB.equals("asteroid")) {
			CoreLogic.removeEntity(((Entity) bA.getUserData()));
			CoreLogic.removeEntity(((Entity) bB.getUserData()));
		}

		// Asteroid-Bullet (send both to removeEntity for destruction)
		if (sA.equals("asteroid") && sB.equals("bullet")) {
			CoreLogic.removeEntity(((Entity) bB.getUserData()));
			CoreLogic.removeEntity(((Entity) bA.getUserData()));
		}

		// Ship-Bullet

		// Bullet-Ship
	}

	/**
	 * This method is called when a collision ends, i.e. as soon as isTouching
	 * goes from true to false. We have no need for this functionality as of
	 * yet, thus no code here.
	 * 
	 * @param contact
	 *            Same as above, holds relevant info for the collision.
	 */
	@Override
	public void endContact(Contact contact) {

	}

	/**
	 * This method is called right before the solver (the part of box2d that
	 * calculates physics) begins its calculations on the collision.
	 * 
	 * @param contact
	 *            Holds collision info
	 * @param oldManifold
	 *            Fancy name for an object holding the information about the
	 *            normal vector of the collision (in world coordinates, contact
	 *            has that info in local coordinates)
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
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

		/*
		 * Bullet-Asteroid (make sure the solver doesn't bother calculating
		 * physics on this collision)
		 */
		if (sA.equals("bullet") && sB.equals("asteroid")) {
			contact.setEnabled(false);
		}

		// Asteroid-Bullet (no solver here either)
		if (sA.equals("asteroid") && sB.equals("bullet")) {
			contact.setEnabled(false);
		}
	}

	/**
	 * Another method we aren't currently using. This is called when the solver
	 * has finished performing calculations on a collision.
	 * 
	 * @param contact
	 *            Holds contact info
	 * @param impulse
	 *            Object holding impulse information that is going to be
	 *            applied to the objects (i.e. what would cause them to "bounce"
	 *            off each other)
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
