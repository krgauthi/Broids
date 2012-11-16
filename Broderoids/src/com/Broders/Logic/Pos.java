package com.Broders.Logic;

import com.badlogic.gdx.Gdx;

/**
 * @author krgauthi
 * 
 * The Pos object converts meters to percentages that can be used to get pixels
 * 
 * the virtual Position it the percentage of x and y
 * 
 * therefore it is touch input int number of pixels == X
 * and the screen Width or Height = K
 * 
 * Pos = X/k
 */
public class Pos {
	float x;
	float y;

	/**
	 * Class constructor (float parameters)
	 * 
	 * @param xx
	 * @param yy
	 */
	public Pos(float xx, float yy) {
		x = xx;
		y = yy;
	}

	/**
	 * Class constructor (int parameters)
	 * 
	 * @param xx
	 * @param yy
	 */
	public Pos(int xx, int yy) {
		x = ((float) xx / (float) Gdx.graphics.getWidth());
		y = ((float) yy / (float) Gdx.graphics.getHeight());
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float xx) {
		x = xx;
	}

	public void setY(float yy) {
		y = yy;
	}

	/*
	 * Give the touch input int and it automatically makes the Virtual pos
	 */
	public void setX(int xx) {
		x = ((float) xx / (float) Gdx.graphics.getWidth());
	}

	public void setY(int yy) {
		y = ((float) yy / (float) Gdx.graphics.getHeight());
	}

}