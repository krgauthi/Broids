package com.Broders.Screens;

import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {

	private SpriteBatch spriteBatch;
	private Texture splsh;
	private Sprite sprite;
	private BaseGame myGame;

	private float timeout = 3.0f;

	// constructor
	public SplashScreen(BaseGame g) {
		this.myGame = g;
		SoundManager.setMuzakId(SoundManager.get("muzak").loop(Settings.getMusicVol() * 0.1f));
	}

	@Override
	public void render(float delta) {
		timeout -= delta;
		if (timeout <= 0 || this.handleInput(delta)) {
			myGame.setScreen(BaseGame.screens.get("main"));
			return;
		}

		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		sprite.draw(spriteBatch);
		spriteBatch.end();
	}

	public boolean handleInput(float delta) {
		if (Gdx.input.justTouched()) {
			return true;
		}

		return false;
	}

	@Override
	public void show() {
		splsh = new Texture(Gdx.files.internal("data/brocoders.png"));
		sprite = new Sprite(splsh);
		sprite.setSize(Settings.getHeight() * 1.5f, Settings.getHeight() * 1.5f);
		sprite.setPosition(Settings.getWidth() * 1.5f * .05f, (Settings.getHeight() * .75f) - Settings.getHeight());
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
		splsh.dispose();
		spriteBatch.dispose();
	}

	@Override
	public void resize(int width, int height) {

	}

}
