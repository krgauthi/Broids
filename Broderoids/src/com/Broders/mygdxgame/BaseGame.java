package com.Broders.mygdxgame;

import com.Broders.Entities.Ship;
import com.Broders.Logic.Net;
import com.Broders.Logic.Settings;
import com.Broders.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.*;

public class BaseGame extends Game {

	SettingsScreen settingsScreen;
	public Settings settings;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
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

		this.setScreen(new SplashScreen(this));
		
		settings = new Settings(this);	
		
		try {
			settings.loadSettings();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find settings file, make sure a file " +
					"named 'broids.cfg' is located in the assets/data folder.");
			e.printStackTrace();
		}
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
