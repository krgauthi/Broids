package com.Broders.Screens;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Net;
import com.Broders.Logic.Pos;
import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.Broders.mygdxgame.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MultiLobby implements Screen {

	private Tail tail;

	private SpriteBatch spriteBatch;

	private BaseGame myGame;
	private BitmapFont font;

	private Texture white;
	private Texture arrow;

	private Sprite whiteSprite;
	private Sprite arrowSprite;

	private String gamePassword;

	private ArrayList<String[]> games;

	float xx;
	float yy;
	float buff;

	float rotation;

	// Test Variables?
	int gameCount;
	int page;
	int curPage;
	int selectedGame;
	float testing = 0;;

	String out;

	public MultiLobby(BaseGame game) {
		this.myGame = game;

		tail = new Tail(5, Color.WHITE);

		font = this.myGame.font;
		font.setScale(.5f);

		xx = Gdx.graphics.getWidth();
		yy = Gdx.graphics.getHeight();

		games = Net.listGames();

		// temp
		gameCount = games.size();
		page = gameCount / 5;
		curPage = 0;

		selectedGame = -1;

		rotation = 0;
	}

	@Override
	public void render(float delta) {

		update(delta);
		handleInput(delta);
		// server interactions here?

		paint(delta);
	}

	private void paint(float delta) {
		// Make a black background
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
		// Boxes

		whiteSprite.setColor(Color.WHITE);
		// horizontal line
		whiteSprite.setSize(xx * .85f, yy * .01f);
		whiteSprite.setPosition(xx * .15f, yy * .8f);
		whiteSprite.draw(spriteBatch);

		// vertical line
		whiteSprite.setSize(yy * .01f, yy * .8f);
		whiteSprite.setPosition(xx * .15f, 0);
		whiteSprite.draw(spriteBatch);


		// tabs
		if (page > 0) {

			arrowSprite.setSize(yy * .25f, yy * .25f);
			arrowSprite.setOrigin((yy * .25f) / 2f, (yy * .25f) / 2f);

			if (curPage < page) { // display both tabs
				arrowSprite.setColor(Color.WHITE);
				arrowSprite.setRotation(180);
				arrowSprite.setPosition(xx * .01f, yy * .19f);
				arrowSprite.draw(spriteBatch);

				out = String.format("%d ", curPage + 1);

				font.draw(spriteBatch, out, xx * .078f, yy * .32f);
				font.setColor(Color.WHITE);
			}

			if (curPage > 0) {
				// you are on the last tab display the top
				arrowSprite.setColor(Color.WHITE);
				arrowSprite.setRotation(0);
				arrowSprite.setPosition(xx * .01f, yy * .5f);
				arrowSprite.draw(spriteBatch);


				out = String.format("%d ", curPage);

				font.draw(spriteBatch, out, xx * .078f, yy * .64f);
				font.setColor(Color.WHITE);
			}
		}



		TextureManager.getSprites("hostGame").draw(spriteBatch);
		TextureManager.getSprites("joinGame").draw(spriteBatch);
		TextureManager.getSprites("refresh").draw(spriteBatch);


		// game list
		for (int i = 0; i < 5 && (i + curPage * 5) < this.games.size(); i++) {
			String[] temp = this.games.get(i + curPage * 5);

			whiteSprite.setPosition(xx * .15f, yy
					* (.8f - (.16f * ((float) i + 1))));
			whiteSprite.draw(spriteBatch);

			out = String.format("Total Players: %s / %s ", temp[2], temp[3]); // TODO
			// ref
			// total
			// players
			font.draw(spriteBatch, out, xx * .7f, yy * (.73f - (.16f * i)));
			String priv = "";
			if (temp[1].equals("true")) {
				priv = " (p)";
			}

			font.draw(spriteBatch, temp[0] + priv, xx * .2f, yy
					* (.73f - (.16f * i))); // TODO ref Name of Game

			if(i == selectedGame){
				arrowSprite.setRotation(0);
				arrowSprite.setSize(xx * .05f, xx * .05f);
				arrowSprite.setPosition( xx * .936f, yy* (.68f - (.16f * i)));
				arrowSprite.setColor(myGame.playerColor);
				arrowSprite.draw(spriteBatch);
			}

		}

		tail.draw(spriteBatch);

		spriteBatch.end();

	}

	private void update(float delta) {
		tail.update();
		rotation += 1;

	}

	private void handleInput(float delta) {

		if (Gdx.input.justTouched()) {
			double x = ((float) Gdx.input.getX() / (float) myGame.screenWidth);
			double y = ((float) Gdx.input.getY() / (float) myGame.screenHeight);

			// make hit boxes
			if (y >= .118 && y <= .177) {

				// join
				if (x >= .425 && x <= .655) {
					if (selectedGame >= 0 && selectedGame < gameCount) {
						
						String[] name = this.games.get(selectedGame + curPage * 5);

						if (Boolean.parseBoolean(name[1])) {
							// Enter password					
							Gdx.input.getTextInput(new TextInputListener() {
								@Override
								public void input (String text) {
									gamePassword = text;
								}
								@Override
								public void canceled () {}
							}, "Password for game:", "");	
						} else {
							gamePassword = "";
						}

						System.out.println("Joining Game with password " + gamePassword);
						Screen temp = Net.joinGame(name[0], gamePassword);

						if (temp != null) {
							myGame.setScreen(temp);
							// TODO: Dispose of this screen
						} else {
							System.out.println("Shit broke yo");
						}
						//host
					} else {
						Sound error = SoundManager.get("error");
						long Id = error.play(myGame.soundVolume * 0.1f);
						error.setPitch(Id, (float) (1.5f));
					}

				} else if (x >= .174 && x <= .404) {
					myGame.setScreen(BaseGame.screens.get("host"));
					//refresh
				}else if(x >= .174 && x <= .404){
					games.clear();
					games = Net.listGames();
				}
			} else if (y >= .55 && y <= .77) {
				if (x >= .037 && x <= .116) {
					if (curPage < page) {
						curPage++;
						selectedGame = -1;
					}
				}
			} else if (y >= .288 && y <= .498) {
				if (x >= .038 && x <= .116) {
					if (curPage > 0) {
						selectedGame = -1;
						curPage--;
					}
				}
			}
			//Game boxes
			if(x >= .155 && x <= .996){
				if(y >= .199 && y <=.343){
					selectedGame = 0;
				} else if(y > .343 && y <= .519){
					selectedGame = 1;
				} else if(y > .519 && y <= .680){
					selectedGame = 2;
				} else if(y > .680 && y <= .838){
					selectedGame = 3;
				} else if(y > .838 && y <= .986){
					selectedGame = 4;
				}
			}
		}

		if (Gdx.input.isTouched()) {
			tail.add(new Pos(Gdx.input.getX(), Gdx.input.getY()));
		}

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (curPage > 0) {
				curPage--;
				testing = testing + 1;
			}
		}

		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			if (curPage < page) {
				curPage++;
			}
		}

		if (Gdx.input.isKeyPressed(Keys.ENTER)) {
			if (selectedGame >= 0 && selectedGame < gameCount) {
				
				String[] name = this.games.get(selectedGame + curPage * 5);

				if (Boolean.parseBoolean(name[1])) {
					// Enter password					
					Gdx.input.getTextInput(new TextInputListener() {
						@Override
						public void input (String text) {
							gamePassword = text;
						}
						@Override
						public void canceled () {}
					}, "Password for game:", "");	
				}

				System.out.println("Joining Game with password " + gamePassword);
				Screen temp = Net.joinGame(name[0], gamePassword);

				if (temp != null) {
					myGame.setScreen(temp);
					// TODO: Dispose of this screen
				} else {
					System.out.println("Shit broke yo");
				}
			} else {
				Sound error = SoundManager.get("error");
				long Id = error.play(myGame.soundVolume * 0.1f);
				error.setPitch(Id, (float) (1.5f));
			}
		}

		if (Gdx.input.isKeyPressed(Keys.F1)) {
			double x = ((float) Gdx.input.getX() / (float) Gdx.graphics
					.getWidth());
			double y = ((float) Gdx.input.getY() / (float) Gdx.graphics
					.getHeight());
			System.out.println("Mouse Pos: " + x + " " + y);
		}

		// Backout to main menu
		if ((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input
				.isKeyPressed(Keys.BACKSPACE)) && buff > myGame.exitBuffer) {
			myGame.setScreen(BaseGame.screens.get("main"));
		} else {
			if (buff < myGame.exitBuffer) {
				buff = buff + delta;
			}
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		buff = 0;

		double x = ((float) Gdx.input.getX() / (float) myGame.screenWidth);
		double y = ((float) Gdx.input.getY() / (float) myGame.screenHeight);

		myGame.multiplayer = true;

		white = new Texture(Gdx.files.internal("data/whitebox.png"));
		whiteSprite = new Sprite(white, 32, 32);
		whiteSprite.setColor(Color.WHITE);


		arrow = new Texture(Gdx.files.internal("data/ship1.png"));
		arrowSprite = new Sprite(arrow, 1024, 1024);

		TextureManager.getSprites("hostGame").setSize(yy * .5f, yy * .5f);
		TextureManager.getSprites("hostGame").setPosition(xx * .15f, yy * .6f);

		TextureManager.getSprites("joinGame").setSize(yy * .5f, yy * .5f);
		TextureManager.getSprites("joinGame").setPosition(xx * .4f, yy * .6f);

		TextureManager.getSprites("refresh").setSize(yy * .5f, yy * .5f);
		TextureManager.getSprites("refresh").setPosition(xx * .73f, yy * .6f);

		spriteBatch = new SpriteBatch();
	}

	@Override
	public void hide() {
		//this.dispose();
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
		this.arrow.dispose();
		this.white.dispose();
	}

}
