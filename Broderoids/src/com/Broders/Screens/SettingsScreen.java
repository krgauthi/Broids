package com.Broders.Screens;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.Broders.mygdxgame.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SettingsScreen implements Screen {

	// Array of basic settings, implemented to draw on even intervals
	private static final String hexColorPattern = "[A-Fa-f0-9]{6}";
	private BaseGame game;

	private SpriteBatch spriteBatch;

	private int screenResSelection;
	private int[][] screenResOptions = {{1024, 768}, {1024, 576},
			{640, 480}, {600, 800}}; // will be two separate integers

	private BitmapFont font;

	private double perc_X;
	private double perc_Y;	

	public SettingsScreen(BaseGame g) {
		this.game = g;
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();	
	}

	@Override
	public void render(float delta) {
		handleInput(delta);
		update(delta);
		paint(delta);		
	}

	private void paint(float delta) {

		spriteBatch.begin();

		if(Settings.getRetro()){
			GL10 gl = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}else{
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(.19f, .19f, .19f, 1f);	 
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		// Debug Text

		if (Settings.getDebug()) {
			font.draw(spriteBatch, Settings.getWidth() + " x " + Settings.getHeight(), Settings.getWidth() * .01f,
					Settings.getHeight() * .94f);
			font.draw(spriteBatch, perc_X + " " + perc_Y, Settings.getWidth() * .01f,
					Settings.getHeight() * .99f);
		}

		// Option Texts

		// Title
		float dpi = Gdx.graphics.getDensity()*160f;
		float inchHeight = ((float)Gdx.graphics.getHeight())/dpi;
		font.setScale(1.5f*dpi*(inchHeight/22f)/72f);
		font.draw(spriteBatch, "Settings", (float) (Settings.getWidth() * .4), (float) (Settings.getHeight() * .95));
		game.font.setScale(dpi*(inchHeight/22f)/72f);

		// Music
		font.draw(spriteBatch, "Music: " + Settings.getMusicVol(), (float) (Settings.getWidth() * .08),
				(float) (Settings.getHeight() * .2));

		font.setColor(Color.WHITE);
		// Sounds
		font.draw(spriteBatch, "Sounds: " + Settings.getSoundVol(), (float) (Settings.getWidth() * .08),
				(float) (Settings.getHeight() * .4));

		// Volume
		//		font.draw(spriteBatch, "Volume: " + volBool, (float) (Settings.getWidth() * .08),
		//				(float) (Settings.getHeight() * .6));

		// Background Image
		font.draw(spriteBatch, "Retro Mode: " + (Settings.getRetro() ? "On" : "Off"),
				(float) (Settings.getWidth() * .08), (float) (Settings.getHeight() * .8));

		// Screen Resolution
		font.draw(spriteBatch, "Screen Resolution: " + screenResOptions[screenResSelection][0] + 
				" x " + screenResOptions[screenResSelection][1],
				(float) (Settings.getWidth() * .52), (float) (Settings.getHeight() * .8));

		// Debug Text Option
		font.draw(spriteBatch, "Debug Text: " + (Settings.getDebug() ? "On" : "Off"),
				(float) (Settings.getWidth() * .52), (float) (Settings.getHeight() * .6));

		// Single Player Difficulty
		font.draw(spriteBatch, "Single Player Difficulty: " + sPDiffString(Settings.getDifficulty()),
				(float) (Settings.getWidth() * .52), (float) (Settings.getHeight() * .4));

		// Epileptic Mode
		font.draw(spriteBatch, "Epileptic Mode: " + (Settings.getEpileptic() ? "On" : "Off"), 
				(float) (Settings.getWidth() * .52), (float) (Settings.getHeight() * .2));

		// User Name
		font.setScale(.25f);
		font.draw(spriteBatch, "Ship Color:", (float) (Settings.getWidth() * .70),
				(float) (Settings.getHeight() * .98));

		font.setScale(.25f);
		font.draw(spriteBatch, "World Color:", Settings.getWidth() * .845f,
				Settings.getHeight() * .98f);

		font.draw(spriteBatch, "Username: " + Settings.getUsername(), (float) (Settings.getWidth() * .70),
				(float) (Settings.getHeight() * .93));

		// ShipColor		


		TextureManager.getSprites("whitePixel").setSize(Settings.getWidth() * .06f, Settings.getHeight() * .05f);
		TextureManager.getSprites("whitePixel").setPosition(Settings.getWidth() * .775f, Settings.getHeight() * .94f);
		TextureManager.getSprites("whitePixel").setColor(Settings.getShipColor());
		TextureManager.getSprites("whitePixel").draw(spriteBatch);

		//World Color
		TextureManager.getSprites("whitePixel").setSize(Settings.getWidth() * .06f, Settings.getHeight() * .05f);
		TextureManager.getSprites("whitePixel").setPosition(Settings.getWidth() * .93f, Settings.getHeight() * .94f);
		TextureManager.getSprites("whitePixel").setColor(Settings.getWorldColor());
		TextureManager.getSprites("whitePixel").draw(spriteBatch);

		spriteBatch.end();		
	}

	private void update(float delta) {

	}

	private void handleInput(float delta) {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACKSPACE) ||
				(Gdx.app.getVersion() > 0 && Gdx.input.isKeyPressed(Keys.BACK))){
			game.setScreen(BaseGame.screens.get("main"));
		}

		if(Gdx.input.justTouched()){
			double x = ((float)Gdx.input.getX()/(float)Settings.getWidth());
			double y = (1.00 - (float)Gdx.input.getY()/(float)Settings.getHeight());

			DecimalFormat twoDForm = new DecimalFormat("##.##");

			perc_X = Math.abs(Double.valueOf(twoDForm.format(x)));
			perc_Y = Math.abs(Double.valueOf(twoDForm.format(y)));

			if ( x >= .7 && x <= .83 && y <= .93 && y >= .89 ) {

				String pretext = "";

				if (Settings.getUsername().equals("") || Settings.getUsername().length() > 28) {
					pretext = "New Bro";
				} else {
					pretext = Settings.getUsername();
				}

				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						if (text.equals("") || text.length() > 28) {
							Settings.setUsername("New Bro");
						} else {
							Settings.setUsername(text);
						}
					}

					@Override
					public void canceled () {}
				}, "Enter new username", pretext);	
			}

			if (x >= .08 && x <= .46 && y >= .12 && y <= .2) {

				if (Settings.getMusicVol() == 10) {
					Settings.setMusicVol(0);
				} else {
					Settings.setMusicVol(Settings.getMusicVol() + 1);
				}
				SoundManager.setMusicVolume(Settings.getMusicVol());
				System.out.println("Music Option set to " + Settings.getMusicVol());
				SoundManager.play("click", 0.7f);

			} else if (x >= .08 && x <= .46 && y >= .32 && y <= .4) {

				if (Settings.getSoundVol() == 10) {
					Settings.setSoundVol(0);
				} else {
					Settings.setSoundVol(Settings.getSoundVol() + 1);
				}
				System.out.println("Sound Option set to " + Settings.getSoundVol());
				SoundManager.play("click", 0.7f);

			} else if (x >= .08 && x <= .46 && y >= .52 && y <= .6) {

				//				volBool = volBool ? false : true;
				//				System.out.println("Volume Option set to " + volBool);

			} else if (x >= .08 && x <= .46 && y >= .72 && y <= .8) {

				if (Settings.getRetro()) {
					Settings.setRetro(false);
				} else {
					Settings.setRetro(true);
				}
				System.out.println("Retro Mode Option set to " + (Settings.getRetro() ? "On" : "Off"));	
				TextureManager.init();
			} else if (x >= .51 && x <= .96 && y >= .72 && y <= .8) {

				if (screenResSelection == (screenResOptions.length - 1)) {
					screenResSelection = 0;
				} else {
					screenResSelection++;
				}

				// We need to set the BaseGame screen width + height to the new res so clicks will work correctly,
				// as they are based off of a percentage of screen height + width, which are set statically.

				if(Gdx.app.getVersion() <= 0){
					Settings.setWidth(screenResOptions[screenResSelection][0]);
					Settings.setHeight(screenResOptions[screenResSelection][1]);
				}

				Gdx.graphics.setDisplayMode(screenResOptions[screenResSelection][0],
						screenResOptions[screenResSelection][1], false);
				System.out.println("Screen Resolution Option set to " + Settings.getWidth() +
						" x " + Settings.getHeight());

			} else if (x >= .51 && x <= .96 && y >= .52 && y <= .6) {

				if (Settings.getDebug()) {
					Settings.setDebug(false);
				} else {
					Settings.setDebug(true);
				}

				System.out.println("Debug Text Option set to " + Settings.getDebug());

			} else if (x >= .51 && x <= .96 && y >= .32 && y <= .4) {

				if (Settings.getDifficulty() >= 2) {
					Settings.setDifficulty(0);
				} else {
					Settings.setDifficulty(Settings.getDifficulty() + 1);
				}

				System.out.println("SP Difficulty Option set to " + Settings.getDifficulty());

			} else if (x >= .51 && x <= .96 && y >= .12 && y <= .2) {

				if (Settings.getEpileptic()) {
					Settings.setEpileptic(false);
				} else {
					Settings.setEpileptic(true);
				}

				System.out.println("Epileptic Mode set to " + Settings.getEpileptic());

			} else if (x >= .70 && x<= .84 && y >= .94 && y <= .98) {

				String pretext = "";

				if (Settings.getShipColor().equals("")) {
					pretext = "#OOFF33";
				} else {
					pretext = swapHex(Settings.getShipColor().toString());
				}

				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						if (!Pattern.matches(hexColorPattern, text) &&
								!Pattern.matches("#" + hexColorPattern, text)) {
							text = "FFFFFF";
						}
						text = text.replaceAll("#", "").trim();
						Settings.setShipColor(text);
					}

					@Override
					public void canceled () {}
				}, "Enter new player color", pretext);	

			} else if (x >= .85 && x<= .99 && y >= .94 && y <= .98) {
				String pretext = "";

				if (Settings.getWorldColor().equals("")) {
					pretext = "#BFBFBF";
				} else {
					pretext = swapHex(Settings.getWorldColor().toString());
				}

				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						if (!Pattern.matches(hexColorPattern, text) &&
								!Pattern.matches("#" + hexColorPattern, text)) {
							text = "FFFFFF";
						}
						text = text.replaceAll("#", "").trim();
						Settings.setWorldColor(text);
					}

					@Override
					public void canceled () {}
				}, "Enter new game color", pretext);

			}
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {		
		spriteBatch = new SpriteBatch();

		font = game.font;
	}

	@Override
	public void hide() {}


	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	public String sPDiffString(int sPDiff) {
		switch (sPDiff) {
		case 0: return "Easy";
		case 1: return "Medium"; 
		case 2: return "Hard";
		default: return Integer.toString(sPDiff);
		}
	}

	public static String swapHex(String hex) {
		String bgr = hex.substring(2); // Why libgdx???
		String red = bgr.substring(4);
		String green = bgr.substring(2, 4);
		String blue = bgr.substring(0, 2);
		return red + green + blue;
	}
}
