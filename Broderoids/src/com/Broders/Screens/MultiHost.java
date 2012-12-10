package com.Broders.Screens;

import com.Broders.Logic.Net;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MultiHost implements Screen {

	private BaseGame myGame;
	private SpriteBatch spriteBatch;
	private BitmapFont font;

	private Texture white;
	private Sprite whiteSprite;

	private Texture ship;
	private Sprite shipSprite;

	private String gameName;
	private String password;

	int worldSize = 0;

	private float xx;
	private float yy;

	public MultiHost(BaseGame game) {
		this.myGame = game;

		font = this.myGame.font;
		font.setScale(.5f);

		gameName = "";
		password = "";

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
		if(myGame.retroGraphics){
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}else{
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(.19f, .19f, .19f, 1f);	 
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		spriteBatch.begin();

		// Box Selections
		if (worldSize == 0) {
			shipSprite.setPosition(xx * .12f, yy * .58f);
			shipSprite.draw(spriteBatch);
		}

		if (worldSize == 1) {
			shipSprite.setPosition(xx * .23f, yy * .58f);
			shipSprite.draw(spriteBatch);
		}

		if (worldSize == 2) {
			shipSprite.setPosition(xx * .33f, yy * .58f);
			shipSprite.draw(spriteBatch);
		}

		// text
		font.draw(spriteBatch, "Game Name: " + this.gameName, xx * .4f, yy * .9f);
		font.draw(spriteBatch, "Password: " + this.password.replaceAll(".", "*"), xx * .4f, yy *.86f);
		font.draw(spriteBatch, "World Size", xx * .17f, yy * .8f);
		font.draw(spriteBatch, "Small", xx * .1f, yy * .7f);
		font.draw(spriteBatch, "Medium", xx * .19f, yy * .7f);
		font.draw(spriteBatch, "Large", xx * .31f, yy * .7f);

		font.draw(spriteBatch, "Play!", xx * .8f, yy * .9f);

		spriteBatch.end();

	}

	private void update(float delta) {
	}

	private void handleInput(float delta) {

		float inputx = Gdx.input.getX() / xx;
		float inputy = Gdx.input.getY() / yy;

		// TODO go back to multilobby
		if ((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input
				.isKeyPressed(Keys.BACKSPACE))) {
			myGame.setScreen(BaseGame.screens.get("lobby"));
		}

		if (Gdx.input.isKeyPressed(Keys.F1)) {
			double x = ((float) Gdx.input.getX() / (float) Gdx.graphics
					.getWidth());
			double y = ((float) Gdx.input.getY() / (float) Gdx.graphics
					.getHeight());
			System.out.println("Mouse Pos: " + x + " " + y);
		}

		if (Gdx.input.justTouched()) {
			// world size
			if (inputy < .364 && inputy > .289) {
				// small
				if (inputx > .098 && inputx < .173) {
					worldSize = 0;
				}
				// medium
				if (inputx > .187 && inputx < .292) {
					worldSize = 1;
				}
				// large
				if (inputx > .306 && inputx < .382) {
					worldSize = 2;
				}
			}

			// Game name text

			if (inputx >= .39 && inputx <= .52 && inputy >= .092 && inputy <= .135) {

				String pretext = "";

				if (this.gameName == null || this.gameName.equals("") ||
						this.gameName.length() > 28) {
					pretext = myGame.playerName + "'s Game";
				} else {
					pretext = this.gameName;
				}

				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						if (text.equals("") || text.length() > 28) {
							gameName = myGame.playerName + "'s Game";
						} else {
							gameName = text;
						}
					}
					@Override
					public void canceled () {}
				}, "Enter Game Name", pretext);	
			}

			// Game password text

			if (inputx >= .39 && inputx <= .52 && inputy >= .141 && inputy <= .177) {

				String pretext = "";

				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						if (text.equals("") || text.length() > 28) {
							password = "";
						} else {
							password = text;
						}
					}
					@Override
					public void canceled () {}
				}, "Enter Game Password", pretext);	
			}

			if (inputy > .09 && inputy < .173) {
				if (inputx > .795 && inputx < .863) {
					myGame.gameSize = worldSize;
					int x;
					int y;
					if (worldSize == 0) {
						x = Gdx.graphics.getWidth();
						y = Gdx.graphics.getHeight();
					} else if (worldSize == 1) {
						x = 500;
						y = 500;
					} else {
						x = 1000;
						y = 1000;
					}
					// TODO: Pass the right values
					Screen s = Net.newGame(this.gameName, 5, x, y, this.password);
					if (s != null) {
						SoundManager.play("click", 0.7f);
						myGame.setScreen(s);
					} else {
						// Error
					}
				}
			}
		}

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();

		white = new Texture(Gdx.files.internal("data/whitebox.png"));
		whiteSprite = new Sprite(white, 32, 32);

		ship = new Texture(Gdx.files.internal("data/ship1.png"));
		shipSprite = new Sprite(ship, 1024, 1024);
		shipSprite.setSize(yy * .05f, yy * .05f);
	}

	@Override
	public void hide() {
		//this.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		this.spriteBatch.dispose();
		this.white.dispose();
		this.ship.dispose();
	}

}
