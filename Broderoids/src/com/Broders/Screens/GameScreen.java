package com.Broders.Screens;

import java.util.Random;
import com.Broders.Entities.*;
import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.InputDir;
import com.Broders.Logic.Net;
import com.Broders.Logic.Pos;
import com.Broders.Logic.Settings;
import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.Broders.mygdxgame.TextureManager;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameScreen implements Screen {

	private BaseGame myGame;

	private boolean multiplayer;

	private Random rand;



	private boolean paused;
	private float pauseWait;


	private float gameOver;

	private SpriteBatch spriteBatch;

	private Tail debug1;
	private Tail debug2;

	

	float xx; // Clean reference for screen width
	float yy; // Clean reference for screen height

	float width2;
	float height2;
	boolean h2;
	int id2;

	public GameScreen(BaseGame game, int id, float width, float height, boolean h) {
		this.myGame = game;
		this.multiplayer = id != 0;
		this.width2 = width;
		this.height2 = height;
		this.h2 = h;
		this.id2 = id;
		this.paused = false;
		this.pauseWait = 0;
		
		this.gameOver = 0;

		if (this.multiplayer) {
			System.out.println("Multi");
		} else {
			System.out.println("Single");
		}

		if (this.multiplayer) {
			CoreLogic.setClientId(id2);
		} else {
			CoreLogic.setClientId(2);
		}

		CoreLogic.initCore(myGame, width2, height2, h2, multiplayer);

	
		myGame.font.setScale(.25f);

		myGame.multiplayer = this.multiplayer;

		if (Settings.getDebug()) {
			debug1 = new Tail(50, Color.MAGENTA);
			debug2 = new Tail(50, Color.CYAN);
		}

		xx = Settings.getWidth();
		yy = Settings.getHeight();

		// this.myGame.epileptic = false;
		rand = new Random();
	}

	@Override
	public void render(float delta) {
		delta = (float) (1.0/30.0);

		Net.lock();
		// Update stuff
		update(delta);
		Net.unlock();

		Net.lock();
		// Handle input
		handleInput(delta);
		Net.unlock();

		Net.lock();
		// update the models on the screen
		paint(delta);
		Net.unlock();
	}

	/*
	 * Method is called every frame to paint the sprites to the screen
	 */
	private void paint(float delta) {
		GL10 g1 = Gdx.graphics.getGL10();
		if (Settings.getEpileptic()) {
			Gdx.gl.glClearColor((rand.nextInt() % 200), rand.nextInt() % 200,
					rand.nextInt() % 200, 1);
		} else if(Settings.getRetro()){
			Gdx.gl.glClearColor(0, 0, 0, 1);
		}else{

			Gdx.gl.glClearColor(.19f, .19f, .19f, 19f);
			//Gdx.gl.glClearColor(0, 0, 0, 1f);
		}
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Start Drawing
		spriteBatch.begin();
		spriteBatch.enableBlending();

		// loop through all Entities
		for (Entity E : CoreLogic.getAllEntities()) {
			E.Draw(spriteBatch);
		}

		// If Android
		if (Gdx.app.getVersion() > 0) {
			TextureManager.getSprites("dPad").draw(spriteBatch, .45f); // TODO @mike .45f make this a setting
			// for how transparent to have
			// android controls
			TextureManager.getSprites("fireButton").draw(spriteBatch, .45f);
			TextureManager.getSprites("thrustButton").draw(spriteBatch, .45f);
		}

		if (multiplayer) {
			// Draw HUD
			TextureManager.getSprites("healthBar").draw(spriteBatch);
			// healthBlock.draw(spriteBatch);
			TextureManager.getSprites("shieldBar").draw(spriteBatch);
			// shieldBlock.draw(spriteBatch);

			int health = 0;
			int shield = 0;
			int score = 0;
			if (CoreLogic.getLocal() != null) {
				health = CoreLogic.getLocal().getHealth();
				shield = CoreLogic.getLocal().getShield();
				score = CoreLogic.getLocal().getScore();
			}

			spriteBatch.draw(TextureManager.getTexture("healthBlock"), xx * .01f, yy * .5f,
					((yy * .5f) * .88f) * health / 100, yy * .5f, 0, 0,
					(int) ((512f * .88f) * health / 100), 512, false, false);
			spriteBatch.draw(TextureManager.getTexture("shieldBlock"), xx * .01f, yy * .5f,
					(yy * .5f) * shield / 100, yy * .5f, 0, 0, (int) (512f * shield / 100),
					512, false, false);


			//String healthText = "HEALTH: " + health + " / 100";
			//String shieldText = "SHIELD: " + shield + " / 100";


			String healthText = "HEALTH: " + CoreLogic.getLocal().getHealth() + " / 100";
			String shieldText = "SHIELD: " + CoreLogic.getLocal().getShield() + " / 100";

			myGame.font.draw(spriteBatch, healthText, xx * .05f, yy * .92f);
			myGame.font.draw(spriteBatch, shieldText, xx * .08f, yy * .975f);

			String out;
			out = String.format("Score: %d ", score); 
			// player
			myGame.font.draw(spriteBatch, out, xx * .01f, yy * .87f);

		} else {
			// Single player hud
			String out;
			if (CoreLogic.getLocal() != null) {
				out = String.format("Score: %d ", CoreLogic.getLocal().getScore());

				// player
				myGame.font.draw(spriteBatch, out, xx * .01f, yy * .98f);

				String out2 = String.format("Bonus: x%d ", (int)Math.floor(CoreLogic.getLocal().getBonus()));
				myGame.font.draw(spriteBatch, out2, xx * .01f, yy * .94f);

				int heartcount = CoreLogic.getLocal().getLives();
				for (int i = 0; i < heartcount; i++) {
					TextureManager.getSprites("lives").setPosition(xx * (.005f + (i * .02f)), yy * .85f);
					TextureManager.getSprites("lives").draw(spriteBatch);
				}
			}

		}

		if (Settings.getDebug()) {
			myGame.font.setScale(.25f);
			String out;
			if (CoreLogic.getLocalShip() != null) {
				out = String.format("Ship Pos in Meters: (%f,%f) ", CoreLogic
						.getLocalShip().getX(), CoreLogic.getLocalShip().getY());
				myGame.font.draw(spriteBatch, out, xx * .01f, yy - (yy * .21f));
				out = String.format("ViewPort Pos in Meters: (%f,%f) ",
						CoreLogic.getViewPortX(), CoreLogic.getViewPortY());
				myGame.font.draw(spriteBatch, out, xx * .01f, yy - (yy * .25f));
				out = String.format("Ship angle in Radians: %f", CoreLogic
						.getLocalShip().getBody().getAngle());
				myGame.font.draw(spriteBatch, out, xx * .01f, yy - (yy * .29f));
				out = String.format("FPS: %d", Gdx.graphics.getFramesPerSecond());
				myGame.font.draw(spriteBatch, out, xx * .01f, yy - (yy * .33f));
				if (CoreLogic.getLocalShip().getThrust())
					myGame.font.draw(spriteBatch, "Thruster", xx * .01f, yy - (yy * .37f));
				if (CoreLogic.getLocalShip().getShooting())
					myGame.font.draw(spriteBatch, "Pew Pew", xx * .01f, yy - (yy * .4f));
			}

			debug1.draw(spriteBatch);
			debug2.draw(spriteBatch);

			float offsetX = CoreLogic.getViewPortX() % 10;
			float offsetY = CoreLogic.getViewPortY() % 10;

			TextureManager.getSprites("whitePixel").setSize(xx, 1);
			for (int i = 0; i <= CoreLogic.getHeightScreen(); i = i + 10) {
				TextureManager.getSprites("whitePixel").setPosition(0,
						yy * ((i - offsetY) / CoreLogic.getHeightScreen()));
				TextureManager.getSprites("whitePixel").draw(spriteBatch);
			}
			TextureManager.getSprites("whitePixel").setSize(1, yy);
			for (int i = 0; i <= CoreLogic.getWidthScreen(); i = i + 10) {
				TextureManager.getSprites("whitePixel").setPosition(
						xx * ((i - offsetX) / CoreLogic.getWidthScreen()), 0);
				TextureManager.getSprites("whitePixel").draw(spriteBatch);
			}



		}


		if(Gdx.input.isKeyPressed(Keys.F1)){
			double x = ((float)Gdx.input.getX()/(float)Gdx.graphics.getWidth());
			double y = ((float)Gdx.input.getY()/(float)Gdx.graphics.getHeight());
			System.out.println("Mouse Pos: " + x + " " + y);
		}

		if(CoreLogic.getRoundBool()){
			myGame.font .setScale(2f);
			String out;
			out = String.format("Round: %d! ", CoreLogic.getRound()+2); 
			myGame.font.draw(spriteBatch, out, xx*.25f, yy*.65f);
			myGame.font.setScale(.25f);
		}

		if (!CoreLogic.multiplayer && CoreLogic.getLocal().getLives() == 0) {
			myGame.font .setScale(2f);
			String out;
			out = String.format("Game Over!"); 
			myGame.font.draw(spriteBatch, out, xx*.20f, yy*.65f);
			myGame.font.setScale(.25f);
		}



		if (paused && !multiplayer) {

			TextureManager.getSprites("whitePixel").setSize(xx * .5f, yy * .5f);
			TextureManager.getSprites("whitePixel").setPosition((xx * .5f)-((xx * .5f)/2), (yy * .5f)-((yy * .5f)/2));
			TextureManager.getSprites("whitePixel").setColor(.1f, .1f, .1f, .7f);
			TextureManager.getSprites("whitePixel").draw(spriteBatch);

			TextureManager.getSprites("pause").setSize(yy * .75f, yy * .75f);
			TextureManager.getSprites("pause").setPosition((xx * .5f) - (yy * .375f), yy - (yy * .7f));
			TextureManager.getSprites("pause").draw(spriteBatch);

			TextureManager.getSprites("mainMenu").setSize(yy * .75f, yy * .75f);
			TextureManager.getSprites("mainMenu").setPosition((xx * .5f) - (yy * .375f), yy - (yy * .85f));
			TextureManager.getSprites("mainMenu").draw(spriteBatch);

			TextureManager.getSprites("resume").setSize(yy * .75f, yy * .75f);
			TextureManager.getSprites("resume").setPosition((xx * .5f) - (yy * .375f),0);
			TextureManager.getSprites("resume").draw(spriteBatch);

		}

		spriteBatch.end();
	}

	/*
	 * Method is called every frame used to update logic
	 */
	private void update(float delta) {

		if (!paused || multiplayer) {
			CoreLogic.update(delta);

			if (Settings.getDebug()) {
				debug1.update();
				debug2.update();
			}
		}

		if (!multiplayer && CoreLogic.getLocal().getLives() == 0) {
			gameOver += Gdx.graphics.getDeltaTime();
			if (gameOver >= 4f) {
				myGame.setScreen(BaseGame.screens.get("scores"));
			}
		}
	}

	private void handleInput(float delta) {

		if (Settings.getDebug()) {
			// Special Debug keys
			if (Gdx.input.isKeyPressed(Keys.F1)) {
				double x = ((float) Gdx.input.getX() / (float) Gdx.graphics
						.getWidth());
				double y = ((float) Gdx.input.getY() / (float) Gdx.graphics
						.getHeight());
				System.out.println("Mouse Pos: " + x + " " + y);
			}

			if (Gdx.input.isKeyPressed(Keys.F2)) {

				System.out.println("Mouse Pos: " + Gdx.input.getX() + " "
						+ Gdx.input.getY());
			}

			if (Gdx.input.isKeyPressed(Keys.F3)) {
				System.out.println("Resize: " + Gdx.graphics.getWidth() + " "
						+ Gdx.graphics.getHeight());
			}

			if (Gdx.input.isTouched(0)) {
				debug1.add(new Pos(Gdx.input.getX(0), Gdx.input.getY(0)));
			}

			if (Gdx.input.isTouched(1)) {
				debug2.add(new Pos(Gdx.input.getX(1), Gdx.input.getY(1)));
			}

			if (Gdx.input.isKeyPressed(Keys.A)) {
				if (CoreLogic.getViewPortX() > 0)
					CoreLogic.adjViewPortX(-1f);
			}

			if (Gdx.input.isKeyPressed(Keys.D)) {
				if (CoreLogic.getViewPortX() < CoreLogic.getWidth())
					CoreLogic.adjViewPortX(1f);
			}

			if (Gdx.input.isKeyPressed(Keys.W)) {
				if (CoreLogic.getViewPortY() < CoreLogic.getHeight())
					CoreLogic.adjViewPortY(1f);
			}

			if (Gdx.input.isKeyPressed(Keys.S)) {
				if (CoreLogic.getViewPortY() > 0)
					CoreLogic.adjViewPortY(-1f);
			}

		}

		if (!paused) {
			// arrow keys
			if (Gdx.input.isKeyPressed(Keys.LEFT)
					&& !Gdx.input.isKeyPressed(Keys.RIGHT)) {
				CoreLogic.execute(delta, InputDir.LEFT);
			}

			if (Gdx.input.isKeyPressed(Keys.RIGHT)
					&& !Gdx.input.isKeyPressed(Keys.LEFT)) {
				CoreLogic.execute(delta, InputDir.RIGHT);
			}

			if (Gdx.input.isKeyPressed(Keys.SPACE)) {
				CoreLogic.execute(delta, InputDir.SHOOT);

			}

			if (Gdx.input.isKeyPressed(Keys.UP)) {
				CoreLogic.execute(delta, InputDir.FORWARD);
			}
		}

		// Backout to main menu
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {

			if (multiplayer) {
				Net.leaveGame();
			}

			SoundManager.setPitch("muzak", SoundManager.getMuzakId(), 1f);
			myGame.setScreen(BaseGame.screens.get("main"));
		}

		pauseWait += Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.ESCAPE) && pauseWait >= 0.2f) {
			if (paused)
				resume();
			else pause();
			pauseWait = 0;
		}

		if(paused){
			if(Gdx.input.justTouched()){
				double x = ((float)Gdx.input.getX()/xx);
				double y = ((float)Gdx.input.getY()/yy);

				if(x >= .326 && x <= .672){

					if(y >= .428 && y <= .519){
						if (multiplayer) {
							Net.leaveGame();
						}

						SoundManager.setPitch("muzak", SoundManager.getMuzakId(), 1f);
						myGame.setScreen(BaseGame.screens.get("main"));
					}else if(y >= .578 && y <= .666){
						resume();
					}

				}


			}

		}


		// if Android
		if (Gdx.app.getVersion() > 0) {
			if (Gdx.input.isTouched(0) || Gdx.input.isTouched(1)
					|| Gdx.input.isTouched(2)) {

				float x1, x2, x3, y1, y2, y3;
				if (Gdx.input.isTouched(0)) {
					x1 = ((float) Gdx.input.getX(0) / (float) xx);
					y1 = ((float) Gdx.input.getY(0) / (float) yy);
				} else {
					x1 = -1;
					y1 = -1;
				}
				if (Gdx.input.isTouched(1)) {
					x2 = ((float) Gdx.input.getX(1) / (float) xx);
					y2 = ((float) Gdx.input.getY(1) / (float) yy);
				} else {
					x2 = -1;
					y2 = -1;
				}
				if (Gdx.input.isTouched(2)) {
					x3 = ((float) Gdx.input.getX(2) / (float) xx);
					y3 = ((float) Gdx.input.getY(2) / (float) yy);
				} else {
					x3 = -1;
					y3 = -1;
				}

				if ((.06 < x1 && x1 < .3 || .06 < x2 && x2 < .3 || .06 < x3
						&& x3 < .3)
						&& (.67 < y1 && y1 < .9 || .67 < y2 && y2 < .9 || .67 < y3
								&& y3 < .9)) {
					// hitbox for left dpad overall
					if (.06 < x1 && x1 < .166 || .06 < x2 && x2 < .166
							|| .06 < x3 && x3 < .166) {

						CoreLogic.execute(delta, InputDir.LEFT);

					} else {
						CoreLogic.execute(delta, InputDir.RIGHT);
					}
				}

				// check for button touch
				if ((.7 < x1 && x1 < .85 || .7 < x2 && x2 < .85 || .7 < x3
						&& x3 < .85)
						&& (.72 < y1 && y1 < .98 || .72 < y2 && y2 < .98 || .72 < y3
								&& y3 < .98)) {

					CoreLogic.execute(delta, InputDir.FORWARD);

				}
				if ((.83 < x1 && x1 < .98 || .83 < x2 && x2 < .98 || .83 < x3
						&& x3 < .98)
						&& (.47 < y1 && y1 < .73 || .47 < y2 && y2 < .73 || .47 < y3
								&& y3 < .73)) {
					// pew pew
					CoreLogic.execute(delta, InputDir.SHOOT);
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

		TextureManager.getSprites("healthBar").setPosition(xx * .01f, yy * .5f);
		TextureManager.getSprites("healthBlock").setPosition(xx * .01f, yy * .5f);
		TextureManager.getSprites("shieldBar").setPosition(xx * .01f, yy * .5f);
		TextureManager.getSprites("shieldBlock").setPosition(xx * .01f, yy * .5f);

		TextureManager.getSprites("healthBar").setSize(yy * .5f, yy * .5f);
		TextureManager.getSprites("healthBlock").setSize(yy * .5f, yy * .5f);
		TextureManager.getSprites("shieldBar").setSize(yy * .5f, yy * .5f);
		TextureManager.getSprites("shieldBlock").setSize(yy * .5f, yy * .5f);

		TextureManager.getSprites("lives").setSize(yy * .05f, yy * .05f);

		if (Gdx.app.getVersion() > 0) {

			TextureManager.getSprites("dPad").setPosition(xx * (0), yy * (-.1f));
			TextureManager.getSprites("dPad").setSize(yy * .6f, yy * .6f);


			TextureManager.getSprites("fireButton").setPosition(xx * (.82f), yy * (.25f));
			TextureManager.getSprites("fireButton").setSize(yy * .32f, yy * .32f);

			TextureManager.getSprites("thrustButton").setPosition(xx * (.69f), yy * 0);
			TextureManager.getSprites("thrustButton").setSize(yy * .32f, yy * .32f);
		}
		myGame.font.setScale(.25f);

		SoundManager.play("start");
	}

	@Override
	public void hide() {
		myGame.multiplayer = false;
		paused = false;
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void dispose() {
		this.spriteBatch.dispose();
	

		CoreLogic.dispose();
	}
}
