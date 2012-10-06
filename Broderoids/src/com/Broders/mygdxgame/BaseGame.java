package com.Broders.mygdxgame;

import com.Broders.Entities.Ship;
import com.Broders.Screens.*;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;





public class BaseGame extends Game {
	
	
	MainMenu main;
	
	OrthographicCamera Cam;
	

	
	@Override
	public void create() {	
		
		Cam = new OrthographicCamera();
		Cam.setToOrtho(false, 1280, 720);
		this.setScreen(new SplashScreen(this));
		super.render();
	
	}

	public void setMain(MainMenu m){
		main = m;
	}
	
	public MainMenu GetMain(){
		return main;
	}
	
	
	public OrthographicCamera getCam(){
		return Cam;
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
