package com.Broders.Screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.Broders.Logic.Net;
import com.Broders.Logic.NetException;
import com.Broders.Logic.Pos;
import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MultiLobby implements Screen {

	private Tail tail;

	private SpriteBatch spriteBatch;

	private BaseGame myGame;
	private BitmapFont font;

	private Texture white;
	private Texture arrow;

	private Sprite whiteSprite;
	private Sprite arrowSprite;

	private ArrayList<String[]> games;

	float xx;
	float yy;
	float buff;

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
		
		try {
			games = Net.listGames();
		} catch (NetException e) {
			// Trouble
			System.out.println(e);
		}

		// temp
		gameCount = games.size();
		page = gameCount / 5;
		curPage = 0;

		selectedGame = -1;

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
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

		// host and Join game button white
		whiteSprite.setSize(xx * .2f, yy * .1f);
		whiteSprite.setPosition(xx * .02f, yy * .85f);
		whiteSprite.draw(spriteBatch);

		whiteSprite.setSize(xx * .2f, yy * .1f);
		whiteSprite.setPosition(xx * .24f, yy * .85f);
		whiteSprite.draw(spriteBatch);

		// host and join game button negative
		whiteSprite.setColor(Color.BLACK);
		whiteSprite.setSize(xx * .18f, yy * .08f);
		whiteSprite.setPosition(xx * .03f, yy * .86f);
		whiteSprite.draw(spriteBatch);

		whiteSprite.setSize(xx * .18f, yy * .08f);
		whiteSprite.setPosition(xx * .25f, yy * .86f);
		whiteSprite.draw(spriteBatch);

		// tabs
		if (page > 0) { // TODO Ref Games from server

			if (curPage < page) { // display both tabs

				arrowSprite.setPosition(xx * .005f, yy * .19f);
				arrowSprite.draw(spriteBatch);

				out = String.format("%d ", curPage + 1);
				font.setColor(Color.BLACK);
				font.draw(spriteBatch, out, xx * .07f, yy * .4f);
				font.setColor(Color.WHITE);

			}

			if (curPage > 0) {
				// you are on the last tab display the top
				arrowSprite.setRotation(180);
				arrowSprite.setPosition(xx * .01f, yy * .5f);
				arrowSprite.draw(spriteBatch);
				arrowSprite.setRotation(0);

				out = String.format("%d ", curPage);
				font.setColor(Color.BLACK);
				font.draw(spriteBatch, out, xx * .07f, yy * .6f);
				font.setColor(Color.WHITE);
			}

		}

		// text
		// join
		font.draw(spriteBatch, "Join", xx * .31f, yy * .93f);

		font.draw(spriteBatch, "Host", xx * .09f, yy * .93f);

		whiteSprite.setSize(xx * .85f, yy * .01f);
		whiteSprite.setColor(Color.WHITE);
		// game list
		// for (int i = 0; i < (gameCount - (curPage * 5)) && i < 5; i++) {

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
			if (temp[1].equals("false")) {
				priv = " (p)";
			}

			font.draw(spriteBatch, temp[0] + priv, xx * .2f, yy
					* (.73f - (.16f * i))); // TODO ref Name of Game

		}

		tail.draw(spriteBatch);

		spriteBatch.end();

	}

	private void update(float delta) {
		tail.update();

	}

	private void handleInput(float delta) {

		if (Gdx.input.justTouched()) {
			double x = ((float) Gdx.input.getX() / (float) myGame.screenWidth);
			double y = ((float) Gdx.input.getY() / (float) myGame.screenHeight);

			// make hit boxes
			if (y >= .05 && y <= .15) {

				// join
				if (x >= .24 && x <= .44) {
					Screen temp = Net.joinGame("broids", "");
					if (temp != null) {
						myGame.setScreen(temp);
						// TODO: Dispose of this screen
					} else {
						// Trouble - failed to join game
					}
				} else if (x >= .02 && x <= .22) {
					myGame.setScreen(BaseGame.screens.get("host"));
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

		if (Gdx.input.isKeyPressed(Keys.F1)) {
			double x = ((float) Gdx.input.getX() / (float) Gdx.graphics
					.getWidth());
			double y = ((float) Gdx.input.getY() / (float) Gdx.graphics
					.getHeight());
			System.out.println("Mouse Pos: " + x + " " + y);
		}

		// Backout to main menu
		if ((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input
				.isKeyPressed(Keys.BACK)) && buff > myGame.exitBuffer) {
			myGame.setScreen(BaseGame.screens.get("main"));
		} else {
			if (buff < myGame.exitBuffer) {
				buff = buff + delta;
			}
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		buff = 0;

		white = new Texture(Gdx.files.internal("data/whitebox.png"));
		whiteSprite = new Sprite(white, 32, 32);
		whiteSprite.setColor(Color.WHITE);

		arrow = new Texture(Gdx.files.internal("data/Arrow.png"));
		arrowSprite = new Sprite(arrow, 1024, 1024);
		arrowSprite.setOrigin((yy * .25f) / 2f, (yy * .25f) / 2f);
		arrowSprite.setSize(yy * .25f, yy * .25f);

		spriteBatch = new SpriteBatch();

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
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		this.spriteBatch.dispose();
		this.arrow.dispose();
		this.white.dispose();

	}

}
