package com.Broders.Screens;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.BaseGame;
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
	private static final String[] settings = { "Music", "Sounds", "Background",
		"Screen Resolution", "Single Player Difficulty" };
	
	private BaseGame game;

	
	private SpriteBatch spriteBatch;

	private boolean musicBool; // not currently implemented
	private boolean soundBool; // not currently implemented
	private boolean volBool; // not currently implemented
	private boolean screenResBool; // will be two seperate integers
	private boolean backgroundBool; // currently unused
	private int sPDiff;  // 1: easy 2: med 3: hard
	private boolean usernameBool; // need to implement the textfield
	
	private BitmapFont font;
	
	private float buff;
	
	private double perc_X;
	private double perc_Y;	
	
	String message;
	
	public SettingsScreen(BaseGame g) {
		this.game = g;
		
		message = "Touch screen for dialog";
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
		font.draw(spriteBatch, "Screen Resolution: " + game.screenWidth + " x " + game.screenHeight,
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
		
		if (game.playerName == null) {
			game.playerName = "";
		}
		
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
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)){
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
				
				if (game.playerName.equals("")) {
					pretext = "New Bro";
				} else {
					pretext = game.playerName;
				}
				
				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						game.playerName = text;
					}
						
					@Override
					public void canceled () {}
				}, "Enter new username", pretext);	
			}
			
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
				System.out.println("CYAN " + Color.CYAN.toString());
				System.out.println("RED " + Color.RED.toString());
				System.out.println("GREEN " + Color.GREEN.toString());
				System.out.println("BLUE " + Color.BLUE.toString());
				System.out.println("WHITE " + Color.WHITE.toString());
				System.out.println("BLACK " + Color.BLACK.toString());
				
				
			} else if (x >= .51 && x <= .96 && y >= .72 && y <= .8) {
			
				screenResBool = screenResBool ? false : true;
				System.out.println("Screen Resolution Optio set to " + screenResBool);
			
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
						if (!text.matches("#[0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F]") &&
								!text.matches("[0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F]")) {
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
				System.out.println("you did it ");
				
				String pretext = "";
				
				if (game.playerColor.equals("")) {
					pretext = "#BFBFBF";
				} else {
					pretext = swapHex(game.gameColor.toString());
				}
				
				Gdx.input.getTextInput(new TextInputListener() {
					@Override
					public void input (String text) {
						if (!text.matches("#[0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F]") &&
								!text.matches("[0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F][0-9A-F]")) {
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
		buff = 0;
		
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
		pw.printf("Ship Color: %s%n", swapHex(game.playerColor.toString()));
		pw.printf("World Color: %s%n", swapHex(game.gameColor.toString()));
		pw.println("Background: " + this.backgroundBool);
		pw.println("Volume: " + this.volBool);
		pw.println("Sounds: " + this.soundBool);
		pw.println("Music: " + this.musicBool);
		pw.println("Resolution: " + game.screenWidth + " x " + game.screenHeight);
		pw.println("Debug: " + game.debugMode);
		pw.println("SP Difficulty: " + this.sPDiff);
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
