package com.Broders.Screens;

import java.util.Random;
import com.Broders.Entities.*;
import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.InputDir;
import com.Broders.Logic.Net;
import com.Broders.Logic.Pos;
import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
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

	private Texture dPadTexture;
	private Texture fireButtonTexture;
	private Texture thrusterButtonTexture;

	private Texture healthBarTexture;
	private Texture healthBlockTexture;
	private Texture shieldBarTexture;
	private Texture shieldBlockTexture;

	private Texture livesTexture;
	private Texture white;

	private Texture whitePixel;
	private Sprite whitePixelSprite;

	private Sprite dPad;
	private Sprite fireButton;
	private Sprite thrusterButton;

	private Sprite healthBar;
	private Sprite healthBlock;
	private Sprite shieldBar;
	private Sprite shieldBlock;
	private Sprite whiteSprite;

	private Sprite lives;
	
	private boolean paused;
	private float pauseWait;
	private ShapeRenderer overlay;

	private SpriteBatch spriteBatch;
	
	private Tail debug1;
	private Tail debug2;

	private BitmapFont font;

	float xx; // Clean reference for screen width
	float yy; // Clean reference for screen height

	float width2;
	float height2;
	boolean h2;
	int id2;

	boolean first;

	public GameScreen(BaseGame game, int id, float width, float height, boolean h) {
		this.first = true;
		this.myGame = game;
		this.multiplayer = id != 0;
		this.width2 = width;
		this.height2 = height;
		this.h2 = h;
		this.id2 = id;
		this.paused = false;
		this.pauseWait = 0;
		this.overlay = new ShapeRenderer();

		if (this.multiplayer) {
			System.out.println("Multi");
		} else {
			System.out.println("Single");
		}

		font = this.myGame.font;
		font.setScale(.25f);

		myGame.multiplayer = this.multiplayer;

		if (myGame.debugMode) {
			debug1 = new Tail(50, Color.MAGENTA);
			debug2 = new Tail(50, Color.CYAN);
		}

		xx = myGame.screenWidth;
		yy = myGame.screenHeight;

		// this.myGame.epileptic = false;
		rand = new Random();
	}

	@Override
	public void render(float delta) {
		if(first){

			if (this.multiplayer) {
				CoreLogic.setClientId(id2);

				// This starts up the thread for async networking
				Net.handleGame();
			} else {
				CoreLogic.setClientId(2);
			}
			first = false;
			CoreLogic.initCore(myGame, width2, height2, h2, multiplayer);
		}

		delta = (float) (1.0/30.0);

		// Update stuff
		update(delta);

		// Handle input
		handleInput(delta);

		// update the models on the screen
		paint(delta);

	}

	/*
	 * Method is called every frame to paint the sprites to the screen
	 */
	private void paint(float delta) {
		GL10 g1 = Gdx.graphics.getGL10();
		if (myGame.epileptic) {
			Gdx.gl.glClearColor((rand.nextInt() % 200), rand.nextInt() % 200,
					rand.nextInt() % 200, 1);
		} else if(myGame.retroGraphics){
			Gdx.gl.glClearColor(0, 0, 0, 1);
		}else{

			Gdx.gl.glClearColor(.19f, .19f, .19f, 1f);	 
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
			dPad.draw(spriteBatch, .45f); // TODO @mike .45f make this a setting
			// for how transparent to have
			// android controls
			fireButton.draw(spriteBatch, .45f);
			thrusterButton.draw(spriteBatch, .45f);
		}

		if (multiplayer) {
			// Draw HUD
			healthBar.draw(spriteBatch);
			// healthBlock.draw(spriteBatch);
			shieldBar.draw(spriteBatch);
			// shieldBlock.draw(spriteBatch);

			float health = (float) CoreLogic.getLocal().getHealth() / 100;
			float shield = 1f;

			spriteBatch.draw(healthBlockTexture, xx * .01f, yy * .5f,
					((yy * .5f) * .88f) * health, yy * .5f, 0, 0,
					(int) ((512f * .88f) * health), 512, false, false);
			spriteBatch.draw(shieldBlockTexture, xx * .01f, yy * .5f,
					(yy * .5f) * shield, yy * .5f, 0, 0, (int) (512f * shield),
					512, false, false);

			String healthText = "HEALTH: " + CoreLogic.getLocal().getHealth() + " / 100";
			
			font.draw(spriteBatch, healthText, xx * .05f, yy * .92f);
			font.draw(spriteBatch, "SHIELD", xx * .08f, yy * .975f);

			String out;
			out = String.format("Score: %d ", CoreLogic.getLocal().getScore()); 
			// player
			font.draw(spriteBatch, out, xx * .01f, yy * .87f);

		} else {
			// Single player hud
			String out;
			out = String.format("Score: %d ", CoreLogic.getLocal().getScore());
			// player
			font.draw(spriteBatch, out, xx * .01f, yy * .98f);

			String out2 = String.format("Bonus: x%d ", (int)Math.floor(CoreLogic.getLocal().getBonus()));
			font.draw(spriteBatch, out2, xx * .01f, yy * .94f);

			int heartcount = CoreLogic.getLocal().getLives();
			for (int i = 0; i < heartcount; i++) {
				lives.setPosition(xx * (.005f + (i * .02f)), yy * .85f);
				lives.draw(spriteBatch);
			}

		}

		if (myGame.debugMode) {
			font.setScale(.25f);
			String out;
			if (CoreLogic.getLocalShip() != null) {
				out = String.format("Ship Pos in Meters: (%f,%f) ", CoreLogic
						.getLocalShip().getX(), CoreLogic.getLocalShip().getY());
				font.draw(spriteBatch, out, xx * .01f, yy - (yy * .21f));
				out = String.format("ViewPort Pos in Meters: (%f,%f) ",
						CoreLogic.getViewPortX(), CoreLogic.getViewPortY());
				font.draw(spriteBatch, out, xx * .01f, yy - (yy * .25f));
				out = String.format("Ship angle in Radians: %f", CoreLogic
						.getLocalShip().getBody().getAngle());
				font.draw(spriteBatch, out, xx * .01f, yy - (yy * .29f));
				out = String.format("FPS: %d", Gdx.graphics.getFramesPerSecond());
				font.draw(spriteBatch, out, xx * .01f, yy - (yy * .33f));
				if (CoreLogic.getLocalShip().getThrust())
					font.draw(spriteBatch, "Thruster", xx * .01f, yy - (yy * .37f));
				if (CoreLogic.getLocalShip().getShooting())
					font.draw(spriteBatch, "Pew Pew", xx * .01f, yy - (yy * .4f));
			}

			debug1.draw(spriteBatch);
			debug2.draw(spriteBatch);

			float offsetX = CoreLogic.getViewPortX() % 10;
			float offsetY = CoreLogic.getViewPortY() % 10;

			whitePixelSprite.setSize(xx, 1);
			for (int i = 0; i <= CoreLogic.getHeightScreen(); i = i + 10) {
				whitePixelSprite.setPosition(0,
						yy * ((i - offsetY) / CoreLogic.getHeightScreen()));
				whitePixelSprite.draw(spriteBatch);
			}
			whitePixelSprite.setSize(1, yy);
			for (int i = 0; i <= CoreLogic.getWidthScreen(); i = i + 10) {
				whitePixelSprite.setPosition(
						xx * ((i - offsetX) / CoreLogic.getWidthScreen()), 0);
				whitePixelSprite.draw(spriteBatch);
			}

		}

		if(CoreLogic.getRoundBool()){
			font .setScale(2f);
			String out;
			out = String.format("Round: %d! ", CoreLogic.getRound()+2); 
			font.draw(spriteBatch, out, xx*.25f, yy*.65f);
			font.setScale(.25f);
		}
		spriteBatch.end();

		if (paused) {
			
			//this.overlay = new ShapeRenderer();
			overlay.begin(ShapeType.FilledRectangle);
			Color temp = new Color(51f,51f,51f,0.1f);
			overlay.setColor(temp);
			overlay.filledRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			overlay.end();
			
		}
	}

	/*
	 * Method is called every frame used to update logic
	 */
	private void update(float delta) {

		if (!paused || multiplayer) {
			CoreLogic.update(delta);

			if (myGame.debugMode) {
				debug1.update();
				debug2.update();
			}
		}
	}

	private void handleInput(float delta) {

		if (myGame.debugMode) {
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

			this.first = true;
			SoundManager.get("muzak").setPitch(SoundManager.getMuzakId(), 1f);
			myGame.setScreen(BaseGame.screens.get("main"));
		}
		
		pauseWait += Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.ESCAPE) && pauseWait >= 0.2f) {
			if (paused)
				resume();
			else pause();
			pauseWait = 0;
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

		spriteBatch.begin();
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 1, 1);
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.end();

		healthBarTexture = new Texture(Gdx.files.internal("data/healthbracket.png"));
		healthBlockTexture = new Texture(
				Gdx.files.internal("data/healthbar.png"));
		shieldBarTexture = new Texture(
				Gdx.files.internal("data/shieldbracket.png"));
		shieldBlockTexture = new Texture(
				Gdx.files.internal("data/shieldbar.png"));

		healthBar = new Sprite(healthBarTexture, 512, 512);
		healthBlock = new Sprite(healthBlockTexture, 512, 512);
		shieldBar = new Sprite(shieldBarTexture, 512, 512);
		shieldBlock = new Sprite(shieldBlockTexture, 512, 512);

		healthBar.setPosition(xx * .01f, yy * .5f);
		healthBlock.setPosition(xx * .01f, yy * .5f);
		shieldBar.setPosition(xx * .01f, yy * .5f);
		shieldBlock.setPosition(xx * .01f, yy * .5f);

		healthBar.setSize(yy * .5f, yy * .5f);
		healthBlock.setSize(yy * .5f, yy * .5f);
		shieldBar.setSize(yy * .5f, yy * .5f);
		shieldBlock.setSize(yy * .5f, yy * .5f);

		livesTexture = new Texture(Gdx.files.internal("data/ship1.png"));
		lives = new Sprite(livesTexture, 1024, 1024);
		lives.setSize(yy * .05f, yy * .05f);

		white = new Texture(Gdx.files.internal("data/whitebox.png"));
		whiteSprite = new Sprite(white, 32, 32);

		whitePixel = new Texture(Gdx.files.internal("data/whitepixel.png"));
		whitePixelSprite = new Sprite(whitePixel, 1, 1);

		if (Gdx.app.getVersion() > 0) {
			dPadTexture = new Texture(
					Gdx.files.internal("data/leftrightpad.png"));
			dPad = new Sprite(dPadTexture, 512, 512);
			dPad.setPosition(xx * (0), yy * (-.1f));
			dPad.setSize(yy * .6f, yy * .6f);

			fireButtonTexture = new Texture(
					Gdx.files.internal("data/fireButton.png"));
			fireButton = new Sprite(fireButtonTexture, 512, 512);
			fireButton.setPosition(xx * (.82f), yy * (.25f));
			fireButton.setSize(yy * .32f, yy * .32f);

			thrusterButtonTexture = new Texture(
					Gdx.files.internal("data/thrustButton.png"));
			thrusterButton = new Sprite(thrusterButtonTexture, 512, 512);
			thrusterButton.setPosition(xx * (.69f), yy * 0);
			thrusterButton.setSize(yy * .32f, yy * .32f);
		}
		font.setScale(.25f);
	}

	@Override
	public void hide() {
		myGame.multiplayer = false;
		//this.dispose();
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

		if (Gdx.app.getVersion() > 0) {
			this.dPadTexture.dispose();
			this.fireButtonTexture.dispose();
			this.thrusterButtonTexture.dispose();
		}

		this.healthBarTexture.dispose();
		this.healthBlockTexture.dispose();
		this.shieldBarTexture.dispose();
		this.shieldBlockTexture.dispose();

		this.livesTexture.dispose();
		this.white.dispose();

		this.whitePixel.dispose();
		this.overlay.dispose();
		
		CoreLogic.dispose();
	}
}
