package com.Broders.Screens;

import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.ScoresManager;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoresScreen implements Screen {

	private SpriteBatch spriteBatch;
	private BaseGame myGame;
	private String scores [][];
	private BitmapFont font;
	private float xx;
	private float yy;

	// constructor
	public ScoresScreen(BaseGame g) {
		this.myGame = g;
		scores = ScoresManager.getScores();
		font = this.myGame.font;
		font.setScale(.40f);

		xx = Settings.getWidth();
		yy = Settings.getHeight();
	}

	@Override
	public void render(float delta) {
		if (this.handleInput(delta)) {
			myGame.setScreen(BaseGame.screens.get("main"));
			return;
		}

		//Make a black background
		if(Settings.getRetro()){
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}else{
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(.19f, .19f, .19f, 1f);	 
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		spriteBatch.begin();
		font.setScale(.60f);
		font.draw(spriteBatch, "High Scores", xx * .40f, yy * .98f);
		font.setScale(.50f);
		for(int i = 0; i < 10; i++){
			font.draw(spriteBatch, scores[i][0], xx * .05f, yy * (.85f - i*.08f));
			font.draw(spriteBatch, scores[i][1], xx * .20f, yy * (.85f - i*.08f));
			font.draw(spriteBatch, scores[i][2], xx * .70f, yy * (.85f - i*.08f));
		}
		spriteBatch.end();
	}

	public boolean handleInput(float delta) {
		if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACKSPACE) ||
				(Gdx.app.getVersion() > 0 && Gdx.input.isKeyPressed(Keys.BACK))) {
			SoundManager.get("click").play(Settings.getSoundVol() * 0.1f);
			return true;
		}

		return false;
	}

	@Override
	public void show() {
		font.setScale(.40f);
		spriteBatch = new SpriteBatch();
	}

	@Override
	public void hide() {
		this.dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}

	@Override
	public void resize(int width, int height) {
	
	}

}
