package com.Broders.Screens;

import java.text.DecimalFormat;

import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SettingsScreen implements Screen {

	
	// Array of basic settings, implemented to draw on even intervals
	private static final String[] settings = { "Music", "Sounds", "Background",
		"Screen Resolution", "Single Player Difficulty" };
	
	private MainMenu main;
	private BaseGame game;
	private Tail tail;
	
	private SpriteBatch spriteBatch;

	private boolean musicBool;
	private boolean soundBool;
	private boolean volBool;
	private boolean debugBool;
	private boolean screenResBool;
	private boolean backgroundBool;
	private boolean sPDiffBool;
	private boolean usernameBool;
	
	private BitmapFont font;
	
	private float buff;
	
	private double perc_X;
	private double perc_Y;	
	
	public SettingsScreen(BaseGame g, MainMenu m) {
		this.main = m;
		this.game = g;
		game.setSettings(this);
		tail = new Tail(game.tailLength, Color.WHITE);
		
		// loadSettings();
		
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
		
		if (game.debugMode) {
		// Debug Text
		font.draw(spriteBatch, perc_X + " " + perc_Y, (float) (game.screenWidth * .01),
				(float) (game.screenHeight * .99));
		}

		// Option Texts

		// Title
		font.setScale(.75f);
		font.draw(spriteBatch, "Settings", (float) (game.screenWidth * .4), (float) (game.screenHeight * .95));
		font.setScale(.5f);
		
		// Left Column Options
		
		// Music
		font.draw(spriteBatch, "Music: " + musicBool, (float) (game.screenWidth * .08),
				(float) (game.screenHeight * .2));
		
		font.setColor(Color.WHITE);
		// Sounds
		font.draw(spriteBatch, "Sounds: " + soundBool, (float) (game.screenWidth * .08),
				(float) (game.screenHeight * .4));

		// Volume
		font.draw(spriteBatch, "Volume: " + volBool, (float) (game.screenWidth * .08),
				(float) (game.screenHeight * .6));
		 
		// Background Image
		font.draw(spriteBatch, "Background: " + backgroundBool,
				(float) (game.screenWidth * .08), (float) (game.screenHeight * .8));
		
		// Right Column Options
		
		// Screen Resolution
		font.draw(spriteBatch, "Screen Resolution: " + screenResBool,
				(float) (game.screenWidth * .52), (float) (game.screenHeight * .8));
		
		// Debug Text Option
		font.draw(spriteBatch, "Debug Text: " + game.debugMode,
				(float) (game.screenWidth * .52), (float) (game.screenHeight * .6));
		
		// Single Player Difficulty
		font.draw(spriteBatch, "Single Player Difficulty: " + sPDiffBool,
				(float) (game.screenWidth * .52), (float) (game.screenHeight * .4));
		
		// Epileptic Mode
		font.draw(spriteBatch, "Epileptic Mode: " + game.epileptic,
				(float) (game.screenWidth * .52), (float) (game.screenHeight * .2));
		
		// User Name
		font.setScale(.25f);
		font.draw(spriteBatch, "Ship Color", (float) (game.screenWidth * .70),
				(float) (game.screenHeight * .94));
		
		// ShipColor		
		
		spriteBatch.end();		
	}
	
	private void update(float delta) {
		
		tail.update();		
	
	}

	private void handleInput(float delta) {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)){
			game.setScreen(game.getMain());
		}
		
		if(Gdx.input.justTouched()){
			double x = ((float)Gdx.input.getX()/(float)game.screenWidth);
			double y = (1.00 - (float)Gdx.input.getY()/(float)game.screenHeight);
						
			DecimalFormat twoDForm = new DecimalFormat("##.##");
			
			perc_X = Math.abs(Double.valueOf(twoDForm.format(x)));
			perc_Y = Math.abs(Double.valueOf(twoDForm.format(y)));
			
			if (x >= .08 && x <= .46 && y >= .12 && y <= .2) {
				
				musicBool = musicBool ? false : true;
				System.out.println("Music Option set to " + musicBool );
				
			} else if (x >= .08 && x <= .46 && y >= .32 && y <= .4) {
				
				soundBool = soundBool ? false : true;
				System.out.println("Sound Option set to " + soundBool);
				
			} else if (x >= .08 && x <= .46 && y >= .52 && y <= .6) {
				
				volBool = volBool ? false : true;
				System.out.println("Volume Option set to " + volBool);
				
			} else if (x >= .08 && x <= .46 && y >= .72 && y <= .8) {
				
				backgroundBool = backgroundBool ? false : true;
				System.out.println("Background Option set to " + backgroundBool);
				
			} else if (x >= .51 && x <= .96 && y >= .72 && y <= .8) {
			
				screenResBool = screenResBool ? false : true;
				System.out.println("Screen Resolution Optio set to " + screenResBool);
			
			} else if (x >= .51 && x <= .96 && y >= .52 && y <= .6) {
				
				game.debugMode = game.debugMode ? false : true;
				System.out.println("Debug Text Option set to " + game.debugMode);
				
			} else if (x >= .51 && x <= .96 && y >= .32 && y <= .4) {
				
				sPDiffBool = sPDiffBool ? false : true;
				System.out.println("SP Difficulty Option");
				
			} else if (x >= .51 && x <= .96 && y >= .12 && y <= .2) {
								
				game.epileptic = game.epileptic ? false : true;
				System.out.println("Epileptic Mode set to " + game.epileptic);
				
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
