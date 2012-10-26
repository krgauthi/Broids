package com.Broders.Screens;

import com.Broders.Entities.Ship;
import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SettingsScreen implements Screen {

	
	// Array of basic settings, implemented to draw on even intervals
	private static final String[] settings = {"Music", "Sounds", "Background",
		"Screen Resolution", "Single Player Difficulty"};
	
	private MainMenu main;
	private BaseGame game;
	private Tail tail;
	
	private SpriteBatch spriteBatch;
	
	private Texture titleTex;
	private Texture musicVolTex;
	private Texture soundVolTex;
	private Texture backTex;
	private Texture backgroundTex;
	private Texture resTex;
	private Texture debugTex;
	private Texture colorTex;
	private Texture diffTex;
	private Texture nameTex;
	
	private Sprite titleSprite;
	private Sprite musicVolSprite;
	private Sprite soundVolSprite;
	private Sprite backSprite;
	private Sprite backgroundSprite;
	private Sprite resSprite;
	private Sprite debugSprite;
	private Sprite colorSprite;
	private Sprite diffSprite;
	private Sprite nameSprite;
	
	private BitmapFont font;
	
	private float buff;
	
	
	public SettingsScreen(BaseGame g, MainMenu m) {
		this.main = m;
		this.game = g;
		game.setSettings(this);
		tail = new Tail(game.tailLength);
	}
	
	@Override
	public void render(float delta) {
		handleInput(delta);
		update(delta);
		paint(delta);		
	}

	private void paint(float delta) {
		GL10 gl = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		
		// Title
		font.setScale(.5f);
		font.draw(spriteBatch, "Settings", (float) (game.screenWidth * .4), (float) (game.screenHeight * .95));
		font.setScale(.25f);
		
		// Music
		font.draw(spriteBatch, "Music: songTitle", (float) (game.screenWidth * .08), (float) (game.screenHeight * .2));
		
		// Sounds
		font.draw(spriteBatch, "Sounds: soundPack", (float) (game.screenWidth * .08), (float) (game.screenHeight * .4));

		// Volume
		font.draw(spriteBatch, "Volume: volLevel", (float) (game.screenWidth * .08), (float) (game.screenHeight * .6));
		
		// Background Image
		font.draw(spriteBatch, "Background: backgroundTitle", (float) (game.screenWidth * .08), (float) (game.screenHeight * .8));
		
		// Screen Resolution
		font.draw(spriteBatch, "Screen Resolution: screenRes", (float) (game.screenWidth * .50), (float) (game.screenHeight * .8));
		
		// Debug Text
		font.draw(spriteBatch, "Music: musicTitle", (float) (game.screenWidth * .50), (float) (game.screenHeight * .6));
		
		// Single Player Difficulty
		font.draw(spriteBatch, "Single Player Difficulty: spDiff", (float) (game.screenWidth * .50), (float) (game.screenHeight * .4));

		// User Name
		font.setScale(.25f);
		font.draw(spriteBatch, "Ship Color", (float) (game.screenWidth * .70), (float) (game.screenHeight * .94));
		
		// ShipColor
		
		
		spriteBatch.end();		
	}
	
	private void update(float delta) {
		
		tail.Update();		
	
	}

	private void handleInput(float delta) {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			game.setScreen(game.GetMain());
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		buff = 0;
		
		spriteBatch = new SpriteBatch();
		
		
		font = game.font;
		
		

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
