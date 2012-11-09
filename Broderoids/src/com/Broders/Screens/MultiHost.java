package com.Broders.Screens;

import com.Broders.Logic.CoreLogic;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MultiHost implements Screen{

	private BaseGame myGame;
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	
	private float xx;
	private float yy;


	public MultiHost(BaseGame game){
		this.myGame = game;
		
		xx = Gdx.graphics.getWidth();
		yy = Gdx.graphics.getHeight();
	}

	@Override
	public void render(float delta) {
		handleInput(delta);
		update(delta);

		paint(delta);

	}

	private void paint(float delta) {
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0, 1); 
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		font.draw(spriteBatch, "Mulipler Options", xx*.3f, yy*.9f);

	}

	private void update(float delta) {
		// TODO Auto-generated method stub

	}

	private void handleInput(float delta) {
		//TODO go back to multilobby
		if((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK))){
			myGame.setScreen(new MultiLobby(this.myGame));
		}


	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();

		font = myGame.font;

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
