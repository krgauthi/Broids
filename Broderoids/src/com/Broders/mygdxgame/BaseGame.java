package com.Broders.mygdxgame;

import com.Broders.Entities.Ship;
import com.Broders.Logic.Settings;
import com.Broders.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.io.FileNotFoundException;
import java.util.concurrent.*;

public class BaseGame extends Game {

	MainMenu main;
	SettingsScreen settingsScreen;
	Settings settings;

	public BitmapFont font;

	public int screenHeight;
	public int screenWidth;
	public int tailLength;
	public double exitBuffer;
	public boolean epileptic;
	public boolean debugMode;
	public boolean multiplayer;
	public int difficulty;
	public Color gameColor;
	public Color playerColor; // TODO ref/move in new player class?
	public float bounds;
	public int gameSize; // multi only

	public Semaphore entitiesLock;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		tailLength = 5;
		exitBuffer = 1;
		difficulty = 5;
		multiplayer = false;
		gameColor = Color.GREEN;
		playerColor = Color.GREEN;
		bounds = .25f; // max of .5
		gameSize = 0;
		entitiesLock = new Semaphore(1);
		
		font = new BitmapFont(Gdx.files.internal(Settings.data_path
				+ "smallfonts.fnt"), Gdx.files.internal(Settings.data_path
				+ "smallfonts_0.png"), false);

		Gdx.input.setCatchBackKey(true);

		this.setScreen(new SplashScreen(this));
		
		settings = new Settings(this);	
		try {
			settings.loadSettings();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find settings file, make sure a file " +
					"named 'broids.cfg' is located in the config folder.");
			e.printStackTrace();
		}

	}

	public void setMain(MainMenu m) {
		main = m;
	}

	public MainMenu getMain() {
		return main;
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
}
