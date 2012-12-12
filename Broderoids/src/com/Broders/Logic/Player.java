package com.Broders.Logic;

import java.util.HashMap;

import com.Broders.Entities.Entity;
import com.Broders.Entities.Ship;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Player {
	private int id;
	
	private Color playerColor;
	protected HashMap<String, Entity> entities;
	private Ship playerShip;
	private int score;
	private float bonus;
	private int lives;
	private int shield;
	private int health;
	private int nextEntityId;

	public String nextId() {
		while (entities.containsKey(Integer.toString(nextEntityId))) {
			nextEntityId++;
		}
		return Integer.toString(id) + "-" + Integer.toString(nextEntityId);
	}

	public int getId() {
		return this.id;
	}

	public Player(boolean local, String name, int id) {
		this.id = id;
		nextEntityId = 0;
		entities = new HashMap<String, Entity>();

		shield = 100;
		health = 100;
		lives = 3;
		bonus = 1.0f;
		score = 0;
		
		if (local) {
			playerColor = Settings.getShipColor();
			
			String sid = this.nextId();
			
			String[] idParts = sid.split("-");
			playerShip = new Ship(sid, this, CoreLogic.getWidth() / 2, CoreLogic.getHeight() / 2);
			entities.put(idParts[1], playerShip);

			if (CoreLogic.multiplayer) {
				Net.createEntity(playerShip);
			}
		} else {
			playerColor = Settings.getWorldColor();
		}
	}

	public int getShield() {
		return shield;
	}

	public int getHealth() {
		return health;
	}

	public int getLives() {
		return lives;
	}

	public int getScore() {
		return score;
	}

	public float getBonus() {
		return bonus;
	}

	public Color getColor() {
		return playerColor;
	}

	public void modShield(int s) {
		shield += s;

		if (shield > 100)
			shield = 100;
		if (shield <= 0) {
			shield = 0;
		}
	}

	public void modHealth(int s) {
		health += s;

		if (health > 100)
			health = 100;
		if (health <= 0) {
			if (this.playerShip != null) {
				CoreLogic.removeEntity(playerShip);
			}
			health = 0;
		}
	}

	public void modLives(int s) {
		lives += s;
		if (Gdx.app.getVersion() <= 0) {
			switch (lives) {
			case 2:	SoundManager.setMusicPitch(1.1f); break;
			case 1: SoundManager.setMusicPitch(1.5f); break;
			default: SoundManager.setMusicPitch(1f);
			}
		}
	}

	public void modScore(int s) {
		int modBonus = (int) Math.floor(bonus);
		if (modBonus != 5) {
			bonus += 0.2f;
		}
		score += (modBonus * s);
	}

	public void modBonus(float s) {
		bonus = s;
	}

	public Ship getShip() {
		return playerShip;
	}

	public void setShip(Ship s) {
		playerShip = s;
	}

	public HashMap<String, Entity> getEntitiesMap() {
		return entities;
	}

	public Iterable<Entity> getEntities() {
		return entities.values();
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void createEntity(Entity ent, String id) {
		this.entities.put(id, ent);
	}

	public void takeDamage(int damage) {
		int hit = shield - damage;
		if (hit < 0) {
			modHealth(hit);
		}
		modShield(0 - damage);
	}
}
