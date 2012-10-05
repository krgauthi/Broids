package com.Broders.Screens;

import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;




public class GameScreen implements Screen{

	private BaseGame myGame;
	
	
	public GameScreen(BaseGame game){
		this.myGame = game;
	}
	
	@Override
	public void render(float delta) {
		
		//handle Input and update Backend
		//it is up to the backend team to decide if they want to handle input seperatly or not
		HandleInput();
		Update();
		
		//server interactions here?
		
		//update the models on the screen
		Paint();
		
		
	}

	private void Paint() {
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0.2f, 1); //its blue so you know you changed screens
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);

		myGame.getCam().update();
		
	}

	private void Update() {
		
		
	}

	private void HandleInput() {
		
		//Special Debug keys
		if(Gdx.input.isKeyPressed(Keys.F1)){
			System.out.println("Mouse Pos: "+Gdx.input.getX()+" "+Gdx.input.getY());
		}
		
		
		//Backout to main menu
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
