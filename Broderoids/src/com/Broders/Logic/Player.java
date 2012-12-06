package com.Broders.Logic;

import java.util.HashMap;

import com.Broders.Entities.Entity;
import com.Broders.Entities.Ship;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.graphics.Color;

public class Player {
	private int id;
	private BaseGame myGame;
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
		while (entities.containsKey(Integer.toString(id) + "-"
				+ Integer.toString(nextEntityId))) {
			nextEntityId++;
		}
		return Integer.toString(id) + "-" + Integer.toString(nextEntityId);
	}

	public int getId() {
		return this.id;
	}
	
	public Player(String type, int id) {
		this.id = id;
		nextEntityId = 0;
		myGame = CoreLogic.getGame();
		entities = new HashMap<String, Entity>();

		if (type.equals("Player")) {
			playerColor = myGame.playerColor;

			playerShip = new Ship(this.nextId(), this,
					CoreLogic.getWidth() / 2, CoreLogic.getHeight() / 2);
			entities.put(playerShip.getId(), playerShip);

			score = 0;

			if (myGame.multiplayer) {
				shield = 100;
				health = 100;
			} else {
				lives = 3;
			}
		} else {
			playerColor = myGame.gameColor;
		}

		bonus = 1.0f;
		score = 0;

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
	}

	public void modHealth(int s) {
		health += s;
	}

	public void modLives(int s) {
		lives += s;
		switch (lives) {
		case 2:	SoundManager.get("muzak").setPitch(SoundManager.getMuzakId(), 1.1f); break;
		case 1: SoundManager.get("muzak").setPitch(SoundManager.getMuzakId(), 1.5f); break;
		default: SoundManager.get("muzak").setPitch(SoundManager.getMuzakId(), 1f);
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

}
