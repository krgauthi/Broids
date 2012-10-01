package com.Broders.mygdxgame;

import com.Broders.Screens.*;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.graphics.OrthographicCamera;





public class BaseGame extends Game {
	
	
	
	
	OrthographicCamera Cam;
	
	public BaseGame(){
	
	
	}

	
	@Override
	public void create() {	
		
		Cam = new OrthographicCamera();
		Cam.setToOrtho(false, 1024, 576);
		this.setScreen(new SplashScreen(this));
		super.render();
		
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
