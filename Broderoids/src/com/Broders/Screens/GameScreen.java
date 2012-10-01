package com.Broders.Screens;

import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;




public class GameScreen implements Screen{

	private BaseGame myGame;
	
	
	public GameScreen(BaseGame game){
		this.myGame = game;
	}
	
	@Override
	public void render(float delta) {
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);

		myGame.getCam().update();
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
