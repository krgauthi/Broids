package com.Broders.mygdxgame;

import com.Broders.Entities.Ship;
import com.Broders.Screens.*;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;





public class BaseGame extends Game {
	
	
	MainMenu main;
	
	private static final int VIRTUAL_WIDTH = 480;
    private static final int VIRTUAL_HEIGHT = 320;
    private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
	
	private Camera Cam;
	

	
	@Override
	public void create() {	
		
		Cam = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		
		this.setScreen(new SplashScreen(this));
		super.render();
	
	}

	public void setMain(MainMenu m){
		main = m;
	}
	
	public MainMenu GetMain(){
		return main;
	}
	
	
	public Camera getCam(){
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
