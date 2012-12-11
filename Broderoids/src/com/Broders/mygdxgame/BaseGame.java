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

	public double exitBuffer;		//TODO Ref in Settings
	public boolean godMode;			//TODO Ref in Settings

	public boolean multiplayer;

	public float bounds;
	public int gameSize; // multi only
	public boolean connected;

	public int asteroidsCount;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {

		// TODO: Put this in a better place, remove the method
		CoreLogic.setGame(this);

		exitBuffer = 1;
		multiplayer = false;
		bounds = .25f; // max of .5
		gameSize = 0;

		font = new BitmapFont(Gdx.files.internal(Settings.data_path
				+ "smallfonts.fnt"), Gdx.files.internal(Settings.data_path
						+ "smallfonts_0.png"), false);

		Gdx.input.setCatchBackKey(true);

		Settings.init(this);

		Settings.setWidth(Gdx.graphics.getWidth());
		Settings.setHeight(Gdx.graphics.getHeight());
		
		Net.init(this);
		ScoresManager.init(this);
		TextureManager.init(this);
		SoundManager.init(this);

		screens = new HashMap<String,Screen>();
		screens.put("splash", new SplashScreen(this));
		screens.put("main", new MainMenu(this));
		screens.put("settings", new SettingsScreen(this));
		if (connected) {
			screens.put("host", new MultiHost(this));
			screens.put("lobby", new MultiLobby(this));
		}

		screens.put("scores", new ScoresScreen(this));


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
