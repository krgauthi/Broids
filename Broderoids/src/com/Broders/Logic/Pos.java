package com.Broders.Logic;

import com.badlogic.gdx.Gdx;


/*
 * The Pos object stores our Virtual Positions
 * 
 * the virtual Position it the percentage of x and y
 * 
 * therefore it is touch input int number of pixels == X
 * and the screen Width or Height = K
 * 
 * Pos = X/k
 */
public class Pos{
	float x;
	float y;
	
	public Pos(float xx, float yy){
		x = xx;
		y = yy;
	}
	
	public Pos(int xx,int yy){
		x = ((float)xx/(float)Gdx.graphics.getWidth());
		y = ((float)yy/(float)Gdx.graphics.getHeight());
	}
	
	
	public float Getx(){
		return x;
	}
	
	public float Gety(){
		return y;
	}
	
	public void Setx(float xx){
		x = xx;
	}
	
	public void Sety(float yy){
		y = yy;
	}
	
	/*
	 * Give the touch input int and it automaticly makes the Virtual pos
	 */
	public void Setx(int xx){
		x = ((float)xx/(float)Gdx.graphics.getWidth());
	}
	
	public void Sety(int yy){
		y = ((float)yy/(float)Gdx.graphics.getHeight());
	}
	
	
}