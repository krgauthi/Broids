package com.Broders.Logic;

import java.util.HashMap;
import java.util.LinkedList;

import com.Broders.Entities.Entity;
import com.Broders.Entities.Ship;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.graphics.Color;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

public class Player{

	private BaseGame myGame;
	private Color playerColor;
	private HashMap<String, Entity> entities;
	private Ship playerShip;
	private int score;
	private float bonus;
	private int lives;
	private int shield;
	private int health;
	
	
	
	public Player(String type){
		myGame = CoreLogic.getGame();
		entities = new HashMap<String, Entity>();
		
		if(type.equals("Player")){
			playerColor = myGame.playerColor;

			playerShip = new Ship("classic", this,CoreLogic.getWidth()/2,CoreLogic.getHeight()/2);
			entities.put(playerShip.getId(), playerShip);
			playerShip.setPlayer(this);
			
			score = 0;
			
			if(myGame.multiplayer){
				shield = 100;
				health = 100;
			}else{
				lives = 3;
			}
		}else{
			playerColor = myGame.gameColor;
		}
		
		bonus = 1.0f;
		score = 0;

	}
	
	public int getShield(){
		return shield;
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getLives(){
		return lives;
	}
	
	public int getScore(){
		return score;
	}
	
	public float getBonus(){
		return bonus;
	}
	
	public Color getColor(){
		return playerColor;
	}
	
	public void modShield(int s){
		shield += s;
	}
	
	public void modHealth(int s){
		health += s;
	}
	
	public void modLives(int s){
		lives += s;
	}
	
	public void modScore(int s){
		int modBonus = (int) Math.floor(bonus);
		if(modBonus != 5){
			bonus += 0.2f;
		}
		score += (modBonus * s);
	}
	
	public void modBonus(float s){
		bonus = s;
	}
	
	public Ship getShip(){
		return playerShip;
	}
	
	public void setShip(Ship s){
		playerShip = s;
	}
	
	public HashMap getEntitiesMap(){
		return entities;
	}
	
	public Iterable<Entity> getEntities(){
		return entities.values();
	}
	
}
