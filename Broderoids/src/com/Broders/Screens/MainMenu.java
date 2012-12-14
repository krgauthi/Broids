package com.Broders.Screens;

import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.Broders.mygdxgame.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenu implements Screen {

	private SpriteBatch spriteBatch;

	private BitmapFont font;

	private float buff;

	private float xx;
	private float yy;

	private BaseGame myGame;

	public MainMenu(BaseGame g) {

		this.myGame = g;

		font = this.myGame.font;
		font.setScale(0.97f, 0.75f);

		xx = Settings.getWidth();
		yy = Settings.getHeight();
	}

	@Override
	public void render(float delta) {

		// handle Input and update Backend
		// it is up to the backend team to decide if they want to handle input
		// separately or not
		handleInput(delta);
		update(delta);

		// server interactions here?

		// update the models on the screen
		paint(delta);
	}

	private void paint(float delta) {

		// Make a black background
		if (Settings.getRetro()) {
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		} else {
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(.19f, .19f, .19f, 1f);
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		// add buttons and logo to this layer
		spriteBatch.begin();

		TextureManager.getSprites("titleLeft").draw(spriteBatch);
		TextureManager.getSprites("titleRight").draw(spriteBatch);

		TextureManager.getSprites("singlePlayer").draw(spriteBatch);
		if (myGame.isConnected())
			TextureManager.getSprites("multiplayer").draw(spriteBatch);

		TextureManager.getSprites("settings").draw(spriteBatch);
		TextureManager.getSprites("highScores").draw(spriteBatch);
		TextureManager.getSprites("quit").draw(spriteBatch);

		/*
		 * titleSprite.draw(spriteBatch); singleSprite.draw(spriteBatch); if
		 * (myGame.isConnected()) { multiSprite.draw(spriteBatch); }
		 * settingsSprite.draw(spriteBatch); font.setScale(0.97f, 0.75f);
		 * font.draw(spriteBatch, "High Scores", xx * .56f, yy * .36f);
		 */
		spriteBatch.end();

	}

	/*
	 * This method is where all of the backend will be calculated
	 */
	private void update(float delta) {

	}

	public void handleInput(float delta) {

		if (Gdx.input.justTouched()) {
			double x = ((float) Gdx.input.getX() / xx);
			double y = ((float) Gdx.input.getY() / yy);

			// make hit boxes
			if (x >= .116 && x <= .458) {

				// single player game X 650-850 Y 180 - 230
				if (y >= .480 && y <= .567) {
					// Single Player
					SoundManager.play("click", 0.7f);
					SoundManager.play("start");
					myGame.setScreen(new GameScreen(this.myGame, 0, 0, 0, true));
				} else if (myGame.isConnected() && y >= .628 && y <= .718) {
					// Multiplayer
					SoundManager.play("click", 0.7f);
					myGame.setScreen(BaseGame.screens.get("lobby"));
				}
			} else if (x >= .537 && x <= .882) {
				if (y >= .480 && y <= .567) {
					// HighScore
					SoundManager.play("click", 0.7f);
					myGame.setScreen(BaseGame.screens.get("scores"));
				} else if (y >= .628 && y <= .718) {
					// Settings
					SoundManager.play("click", 0.7f);
					myGame.setScreen(BaseGame.screens.get("settings"));
				}
			}

			if (x >= .312 && x <= .688) {
				if (y >= .777 && y <= .868) {
					// Settings
					SoundManager.play("click", 0.7f);
					Gdx.app.exit();
				}
			}
		}

		if (Gdx.input.isKeyPressed(Keys.F1)) {
			double x = ((float) Gdx.input.getX() / (float) Gdx.graphics.getWidth());
			double y = ((float) Gdx.input.getY() / (float) Gdx.graphics.getHeight());
			System.out.println("Mouse Pos: " + x + " " + y);
		}

		// backout Fix the quick exit from gamescreen
		if ((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACKSPACE)) && buff > myGame.exitBuffer || (Gdx.app.getVersion() > 0 && Gdx.input.isKeyPressed(Keys.BACK) && buff > myGame.exitBuffer)) {
			Gdx.app.exit();
		} else {
			if (buff < myGame.exitBuffer) {
				buff = buff + delta;
			}
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	/*
	 * Called when the MainMenu is first started (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		font.setScale(0.97f, 0.75f);
		buff = 0;

		myGame.multiplayer = false;

		TextureManager.getSprites("titleLeft").setSize(yy * .5f, yy * .5f);
		TextureManager.getSprites("titleLeft").setPosition((xx * .5f) - (yy * .5f), yy - (yy * .5f));

		TextureManager.getSprites("titleRight").setSize(yy * .5f, yy * .5f);
		TextureManager.getSprites("titleRight").setPosition(xx * .5f, yy - (yy * .5f));

		TextureManager.getSprites("singlePlayer").setSize(yy * .75f, yy * .75f);
		TextureManager.getSprites("singlePlayer").setPosition((xx * .5f) - (yy * .75f), (yy * .1f));

		TextureManager.getSprites("multiplayer").setSize(yy * .75f, yy * .75f);
		TextureManager.getSprites("multiplayer").setPosition((xx * .5f) - (yy * .75f), 0 - (yy * .05f));

		TextureManager.getSprites("highScores").setSize(yy * .75f, yy * .75f);
		TextureManager.getSprites("highScores").setPosition(xx * .5f, (yy * .1f));

		TextureManager.getSprites("settings").setSize(yy * .75f, yy * .75f);
		TextureManager.getSprites("settings").setPosition(xx * .5f, 0 - (yy * .05f));

		TextureManager.getSprites("quit").setSize(yy * .75f, yy * .75f);
		TextureManager.getSprites("quit").setPosition((xx * .5f) - (yy * .4f), 0 - (yy * .2f));

		spriteBatch = new SpriteBatch();
	}

	@Override
	public void hide() {
		// this.dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		this.spriteBatch.dispose();

	}

}
