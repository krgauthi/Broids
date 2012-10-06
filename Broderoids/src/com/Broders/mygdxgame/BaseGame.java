package com.Broders.mygdxgame;

import com.Broders.Entities.Ship;
import com.Broders.Screens.*;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;





public class BaseGame extends Game {
	
	
	MainMenu main;

	//Declare Constants here
	public int TailLength = 5;
	//public int ShipSize = 32; never called but cool to implement
	
	
	@Override
	public void create() {	
		
	
		
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
