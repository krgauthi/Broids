package com.Broders.Screens;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SettingsScreen implements Screen {

	// Array of basic settings, implemented to draw on even intervals
	private static final String hexColorPattern = "[A-Fa-f0-9]{6}";
	private BaseGame game;

	private SpriteBatch spriteBatch;

	private boolean volBool; // not currently implemented
	private int screenResSelection;
	private int[][] screenResOptions = {{1920, 1200},
			{640, 480}, {1600, 900}}; // will be two separate integers
	private int sPDiff;  // 1: easy 2: med 3: hard

	private BitmapFont font;

	private double perc_X;
	private double perc_Y;	

	String message;

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
		
		if(game.retroGraphics){
			GL10 gl = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			spriteBatch.begin();
		}else{
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(.19f, .19f, .19f, 1f);	 
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		// Debug Text

		if (game.debugMode) {
			font.draw(spriteBatch, perc_X + " " + perc_Y, (float) (game.screenWidth * .01),
					(float) (game.screenHeight * .99));
		}

		// Option Texts

		// Title
		font.setScale(.75f);
		font.draw(spriteBatch, "Settings", (float) (game.screenWidth * .4), (float) (game.screenHeight * .95));
		font.setScale(.5f);

		// Music
		font.draw(spriteBatch, "Music: " + game.musicVolume, (float) (game.screenWidth * .08),
				(float) (game.screenHeight * .2));

		font.setColor(Color.WHITE);
		// Sounds
		font.draw(spriteBatch, "Sounds: " + game.soundVolume, (float) (game.screenWidth * .08),
				(float) (game.screenHeight * .4));

		// Volume
		font.draw(spriteBatch, "Volume: " + volBool, (float) (game.screenWidth * .08),
				(float) (game.screenHeight * .6));

		// Background Image
		font.draw(spriteBatch, "Retro Mode: " + game.retroGraphics,
				(float) (game.screenWidth * .08), (float) (game.screenHeight * .8));

		// Screen Resolution
		font.draw(spriteBatch, "Screen Resolution: " + screenResOptions[screenResSelection][0] + 
				" x " + screenResOptions[screenResSelection][1],
				(float) (game.screenWidth * .52), (float) (game.screenHeight * .8));

		// Debug Text Option
		font.draw(spriteBatch, "Debug Text: " + (game.debugMode ? "On" : "Off"),
				(float) (game.screenWidth * .52), (float) (game.screenHeight * .6));

		// Single Player Difficulty
		font.draw(spriteBatch, "Single Player Difficulty: " + sPDiffString(sPDiff),
				(float) (game.screenWidth * .52), (float) (game.screenHeight * .4));

		// Epileptic Mode
		font.draw(spriteBatch, "Epileptic Mode: " + (game.epileptic ? "On" : "Off"), 
				(float) (game.screenWidth * .52), (float) (game.screenHeight * .2));

		// User Name
		font.setScale(.25f);
		font.draw(spriteBatch, "Ship Color:", (float) (game.screenWidth * .70),
				(float) (game.screenHeight * .98));

		font.setScale(.25f);
		font.draw(spriteBatch, "World Color:", game.screenWidth * .845f,
				game.screenHeight * .98f);

		font.draw(spriteBatch, "Username: " + game.playerName, (float) (game.screenWidth * .70),
				(float) (game.screenHeight * .93));

		// ShipColor		

		Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		p.setColor(game.playerColor);
		p.fillRectangle(0,  0, 1, 1);
		Texture tex = new Texture(p, true);
		p.dispose();

		spriteBatch.draw(tex, game.screenWidth * .775f, game.screenHeight * .94f,
				0, 0, 60, 26);

		// WorldColor

		p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		p.setColor(game.gameColor);
		p.fillRectangle(0,  0, 1, 1);
		tex = new Texture(p, true);
		p.dispose();

		spriteBatch.draw(tex, game.screenWidth * .93f, game.screenHeight * .94f,
				0, 0, 60, 26);

		spriteBatch.end();		
	}

	private void update(float delta) {



	}

	private void handleInput(float delta) {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACKSPACE)){
			game.setScreen(BaseGame.screens.get("main"));
		}

		if(Gdx.input.justTouched()){
			double x = ((float)Gdx.input.getX()/(float)game.screenWidth);
			double y = (1.00 - (float)Gdx.input.getY()/(float)game.screenHeight);

			DecimalFormat twoDForm = new DecimalFormat("##.##");

			perc_X = Math.abs(Double.valueOf(twoDForm.format(x)));
			perc_Y = Math.abs(Double.valueOf(twoDForm.format(y)));

			if ( x >= .7 && x <= .83 && y <= .93 && y >= .89 ) {

				String pretext = "";

				if (game.playerName.equals("") || game.playerName.length() > 28) {
					pretext = "New Bro";
				} else {
					pretext = game.playerName;
				}

				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						if (text.equals("") || text.length() > 28) {
							game.playerName = "New Bro";
						} else {
							game.playerName = text;
						}
					}

					@Override
					public void canceled () {}
				}, "Enter new username", pretext);	
			}

			if (x >= .08 && x <= .46 && y >= .12 && y <= .2) {
				
				game.musicVolume = game.musicVolume == 10 ? 0 : game.musicVolume + 1;
				SoundManager.get("muzak").setVolume(SoundManager.getMuzakId(), game.musicVolume * .1f);
				System.out.println("Music Option set to " + game.musicVolume);
				
			} else if (x >= .08 && x <= .46 && y >= .32 && y <= .4) {
				
				game.soundVolume = game.soundVolume == 10 ? 0 : game.soundVolume + 1;
				System.out.println("Sound Option set to " + game.soundVolume);
				
			} else if (x >= .08 && x <= .46 && y >= .52 && y <= .6) {

				volBool = volBool ? false : true;
				System.out.println("Volume Option set to " + volBool);

			} else if (x >= .08 && x <= .46 && y >= .72 && y <= .8) {

				
				game.retroGraphics = game.retroGraphics ? false : true;
				System.out.println("Retro Mode Option set to " + game.retroGraphics);		

			} else if (x >= .51 && x <= .96 && y >= .72 && y <= .8) {

				if (screenResSelection == (screenResOptions.length - 1)) {
					screenResSelection = 0;
				} else {
					screenResSelection++;
				}

				System.out.println("Screen Resolution Option set to " + screenResOptions[screenResSelection][0] +
						" x " + screenResOptions[screenResSelection][1]);

			} else if (x >= .51 && x <= .96 && y >= .52 && y <= .6) {

				game.debugMode = game.debugMode ? false : true;
				System.out.println("Debug Text Option set to " + game.debugMode);

			} else if (x >= .51 && x <= .96 && y >= .32 && y <= .4) {

				sPDiff = sPDiff == 2 ? 0 : ++sPDiff;
				System.out.println("SP Difficulty Option");

			} else if (x >= .51 && x <= .96 && y >= .12 && y <= .2) {

				game.epileptic = game.epileptic ? false : true;
				System.out.println("Epileptic Mode set to " + game.epileptic);

			} else if (x >= .70 && x<= .84 && y >= .94 && y <= .98) {

				String pretext = "";

				System.out.println(game.playerColor.toString());

				if (game.playerColor.equals("")) {
					pretext = "#OOFF33";
				} else {
					pretext = swapHex(game.playerColor.toString());
				}

				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						if (!Pattern.matches(hexColorPattern, text) &&
								!Pattern.matches("#" + hexColorPattern, text)) {
							text = "FFFFFF";
						}
						text = text.replaceAll("#", "").trim();
						text = "FF" + text;
						game.playerColor.set(game.settings.colorFromHexString(text));
					}

					@Override
					public void canceled () {}
				}, "Enter new player color", pretext);	

			} else if (x >= .85 && x<= .99 && y >= .94 && y <= .98) {
				String pretext = "";

				if (game.playerColor.equals("")) {
					pretext = "#BFBFBF";
				} else {
					pretext = swapHex(game.gameColor.toString());
				}

				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						if (!Pattern.matches(hexColorPattern, text) &&
								!Pattern.matches("#" + hexColorPattern, text)) {
							text = "FFFFFF";
						}
						text = text.replaceAll("#", "").trim();
						text = "FF" + text;
						game.gameColor.set(game.settings.colorFromHexString(text));
					}

					@Override
					public void canceled () {}
				}, "Enter new game color", pretext);

			}
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {		
		spriteBatch = new SpriteBatch();

		font = game.font;
	}

	@Override
	public void hide() {
		try {
			saveSettings();
		} catch (FileNotFoundException e) {
			System.out.println("Could not save settings: config/broids.cfg not found");
			e.printStackTrace();
		}		
	}

	private void saveSettings() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File("config/broids.cfg"));

		pw.println("Username: " + game.playerName);
		pw.printf("ShipColor: %s%n", swapHex(game.playerColor.toString()));
		pw.printf("WorldColor: %s%n", swapHex(game.gameColor.toString()));
		pw.println("Retro: " + game.retroGraphics);
		pw.println("SoundVolume: " + this.game.soundVolume);
		pw.println("MusicVolume: " + this.game.musicVolume);
		pw.println("Resolution: " + game.screenWidth + " x " + game.screenHeight);
		pw.println("Debug: " + game.debugMode);
		pw.println("SPDifficulty: " + this.sPDiff);
		pw.println("Epileptic: " + game.epileptic);

		pw.close();
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

	public String sPDiffString(int sPDiff) {
		switch (sPDiff) {
		case 0: return "Easy";
		case 1: return "Medium"; 
		case 2: return "Hard";
		default: return null;
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
