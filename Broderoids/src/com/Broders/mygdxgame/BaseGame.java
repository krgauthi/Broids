package com.Broders.mygdxgame;

import com.Broders.Entities.Ship;
import com.Broders.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;





public class BaseGame extends Game {
	
	
	MainMenu main;

	
	public int screenHeight;
	public int screenWidth;
	public int tailLength;
	public double exitBuffer;
	
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {	
	screenHeight = Gdx.graphics.getHeight();
	screenWidth =  Gdx.graphics.getWidth();
	tailLength = 5;
	exitBuffer = 1;
	
		
	this.setScreen(new SplashScreen(this));
	super.render();
	
	}

	public void setMain(MainMenu m){
		main = m;
	}
	
	public MainMenu GetMain(){
		return main;
	}
	
	

	
	@Override
	public void dispose() {

	}



	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
