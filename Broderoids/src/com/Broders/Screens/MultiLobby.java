package com.Broders.Screens;

import java.util.ArrayList;
import com.Broders.Logic.Net;
import com.Broders.Logic.Pos;
import com.Broders.Logic.Settings;
import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.Broders.mygdxgame.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MultiLobby implements Screen {

	private Tail tail;

	private SpriteBatch spriteBatch;

	private BaseGame myGame;

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
		// Boxes

		TextureManager.getSprites("whitePixel").setColor(Color.WHITE);
		// horizontal line
		TextureManager.getSprites("whitePixel").setSize(xx * .85f, yy * .01f);
		TextureManager.getSprites("whitePixel").setPosition(xx * .15f, yy * .8f);
		TextureManager.getSprites("whitePixel").draw(spriteBatch);

		// vertical line
		TextureManager.getSprites("whitePixel").setSize(yy * .01f, yy * .8f);
		TextureManager.getSprites("whitePixel").setPosition(xx * .15f, 0);
		TextureManager.getSprites("whitePixel").draw(spriteBatch);


		// tabs
		if (page > 0) {
			
			TextureManager.getSprites("Ship1").setSize(yy * .25f, yy * .25f);
			TextureManager.getSprites("Ship1").setOrigin((yy * .25f)/2f, (yy * .25f)/2f);			

			if (curPage < page) { // Arrow Down
				TextureManager.getSprites("Ship1").setColor(Color.WHITE);
				TextureManager.getSprites("Ship1").setRotation(180);
				TextureManager.getSprites("Ship1").setPosition(xx * .01f, yy * .18f);
				TextureManager.getSprites("Ship1").draw(spriteBatch);

				out = String.format("%d ", curPage + 1);

				myGame.font.setColor(Color.BLACK);
				myGame.font.setScale(.5f);
				myGame.font.draw(spriteBatch, out, xx * .073f, yy * .33f);
				
			}

			if (curPage > 0) {			//Arrow Up
				TextureManager.getSprites("Ship1").setColor(Color.WHITE);
				TextureManager.getSprites("Ship1").setRotation(0);
				TextureManager.getSprites("Ship1").setPosition(xx * .01f, yy * .48f);
				TextureManager.getSprites("Ship1").draw(spriteBatch);

				out = String.format("%d ", curPage);

				myGame.font.setColor(Color.BLACK);
				myGame.font.setScale(.5f);
				myGame.font.draw(spriteBatch, out, xx * .073f, yy * .64f);
				
			}
		}



		TextureManager.getSprites("hostGame").draw(spriteBatch);
		TextureManager.getSprites("joinGame").draw(spriteBatch);
		TextureManager.getSprites("mainMenu").draw(spriteBatch);
		TextureManager.getSprites("refresh").draw(spriteBatch);


		// game list
		TextureManager.getSprites("whitePixel").setSize(xx * .85f, yy * .01f);
		for (int i = 0; i < 5 && (i + curPage * 5) < this.games.size(); i++) {
			String[] temp = this.games.get(i + curPage * 5);
			
			TextureManager.getSprites("whitePixel").setPosition(xx * .15f, yy
					* (.8f - (.16f * ((float) i + 1))));
			TextureManager.getSprites("whitePixel").draw(spriteBatch);

			out = String.format("Total Players: %s / %s ", temp[2], temp[3]); // TODO
			// ref
			// total
			// players
			myGame.font.setColor(Color.WHITE);
			myGame.font.setScale(.4f);
			myGame.font.draw(spriteBatch, out, xx * .7f, yy * (.73f - (.16f * i)));
			String priv = "";
			if (temp[1].equals("true")) {
				priv = " (p)";
			}

			myGame.font.draw(spriteBatch, temp[0] + priv, xx * .2f, yy
					* (.73f - (.16f * i))); // TODO ref Name of Game

			if(i == selectedGame){
			//	System.out.println("Game Selected: "+selectedGame);
				TextureManager.getSprites("Ship1").setSize(xx * .05f, xx * .05f);
				TextureManager.getSprites("Ship1").setPosition( xx * .936f, yy* (.68f - (.16f * i)));
				TextureManager.getSprites("Ship1").setRotation(rotation);
				TextureManager.getSprites("Ship1").setOrigin((xx * .05f)/2f	, (xx * .05f)/2f);
				TextureManager.getSprites("Ship1").setColor(Settings.getShipColor());
				TextureManager.getSprites("Ship1").draw(spriteBatch);
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
			double x = ((float) Gdx.input.getX() / (float) Settings.getWidth());
			double y = ((float) Gdx.input.getY() / (float) Settings.getHeight());

			// make hit boxes
			if (y >= .0486 && y <= .104) {

				// join 
				if (x >= .272 && x <= .476) {
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
							SoundManager.play("click", 0.7f);
							myGame.setScreen(temp);
							// TODO: Dispose of this screen
						} else {
							System.out.println("Shit broke yo");
						}
						//host
					} else {
						SoundManager.play("error", 1f, 1.5f);
					}
					//Join
				} else if (x >= .0214 && x <= .225) {
					SoundManager.play("click", 0.7f);
					myGame.setScreen(BaseGame.screens.get("host"));
					//Main Menu
				}else if(x >= .520 && x <= .726){
					myGame.setScreen(BaseGame.screens.get("main"));
					//refresh
				}else if(x >= .770 && x <= .975){
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
					SoundManager.play("click", 0.7f);
					myGame.setScreen(temp);
					// TODO: Dispose of this screen
				} else {
					System.out.println("Shit broke yo");
				}
			} else {
				SoundManager.play("error", 1f, 1.5f);
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
		if ((Gdx.input.isKeyPressed(Keys.ESCAPE) ||
				Gdx.input.isKeyPressed(Keys.BACKSPACE)) && buff > myGame.exitBuffer || 
				(Gdx.app.getVersion() > 0 && Gdx.input.isKeyPressed(Keys.BACK) && buff > myGame.exitBuffer)) {
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

		myGame.multiplayer = true;


		TextureManager.getSprites("whitePixel").setColor(Color.WHITE);

		TextureManager.getSprites("hostGame").setSize(xx * .25f, xx * .25f);
		TextureManager.getSprites("hostGame").setPosition(0, yy * .7f);

		TextureManager.getSprites("joinGame").setSize(xx * .25f, xx * .25f);
		TextureManager.getSprites("joinGame").setPosition((xx * .25f), yy * .7f);
		
		TextureManager.getSprites("mainMenu").setSize(xx * .25f, xx * .25f);
		TextureManager.getSprites("mainMenu").setPosition(((xx * .25f)*2), yy * .7f);

		TextureManager.getSprites("refresh").setSize(xx * .25f, xx * .25f);
		TextureManager.getSprites("refresh").setPosition(((xx * .25f)*3), yy * .7f);

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

	}

}
