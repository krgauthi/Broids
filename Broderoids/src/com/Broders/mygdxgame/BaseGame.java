package com.Broders.mygdxgame;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Net;
import com.Broders.Logic.Settings;
import com.Broders.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class BaseGame extends Game {

	SettingsScreen settingsScreen;
	public Settings settings;

	public static HashMap<String,Screen> screens;

	public BitmapFont font;

	public int screenHeight;		//TODO Ref in Settings
	public int screenWidth;			//TODO Ref in Settings
	
		
	public double exitBuffer;		//TODO Ref in Settings
	public boolean epileptic;		//is Ref'd in Settings
	public boolean debugMode;		//TODO Ref in Settings
	public int difficulty;			//TODO Ref in Settings
	public String playerName;		//TODO Ref in Settings
	public boolean godMode;			//TODO Ref in Settings
	
	public boolean multiplayer;
	
	public Color gameColor;			//TODO Ref in Settings
	public Color playerColor; 		//TODO Ref in Settings
	
	public float bounds;
	public int gameSize; // multi only
	public boolean connected;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
		
		// TODO: Put this in a better place, remove the method
		CoreLogic.setGame(this);
		
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		exitBuffer = 1;
		difficulty = 5;
		multiplayer = false;
		gameColor = Color.GREEN;
		playerColor = Color.MAGENTA;
		bounds = .25f; // max of .5
		gameSize = 0;
		godMode = false;

		font = new BitmapFont(Gdx.files.internal(Settings.data_path
				+ "smallfonts.fnt"), Gdx.files.internal(Settings.data_path
				+ "smallfonts_0.png"), false);

		Gdx.input.setCatchBackKey(true);
		
		Net.init(this);
		
		SoundManager.init();
		
		settings = new Settings(this);	
		
		try {
			settings.loadSettings();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find settings file, make sure a file " +
					"named 'broids.cfg' is located in the assets/data folder.");
			e.printStackTrace();
		}
		
		screens = new HashMap<String,Screen>();
		screens.put("splash", new SplashScreen(this));
		screens.put("main", new MainMenu(this));
		screens.put("settings", new SettingsScreen(this));
		if (connected) {
			screens.put("host", new MultiHost(this));
			screens.put("lobby", new MultiLobby(this));
		}
		screens.put("single", new GameScreen(this, 0, 0, 0, true));
		screens.put("multi", new GameScreen(this, 0, 0, 0, true));

		this.setScreen(BaseGame.screens.get("splash"));
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public void setConnected(boolean c) {
		connected = c;
	}

	public boolean isConnected() {
		return connected;
	}
}
