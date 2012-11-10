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





public class BaseGame extends Game {
	
	
	MainMenu main;
	SettingsScreen settings;
	

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
	public Color playerColor;	//TODO ref/move in new player class?
	public float bounds;
	public int gameSize;		//multi only
	
	
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {	
	screenHeight = Gdx.graphics.getHeight();
	screenWidth =  Gdx.graphics.getWidth();
	tailLength = 5;
	exitBuffer = 1;
	difficulty = 5;
	multiplayer = false;
	gameColor = Color.WHITE;
	playerColor = Color.CYAN;
	bounds = .25f;  									//max of .5
	gameSize = 0;

	
	font = new BitmapFont(Gdx.files.internal(Settings.data_path + "smallfonts.fnt"), Gdx.files.internal(Settings.data_path + "smallfonts_0.png"), false);
	
	Gdx.input.setCatchBackKey(true);
		
	this.setScreen(new SplashScreen(this));
	super.render();
	
	}

	public void setMain(MainMenu m){
		main = m;
	}
	
	public MainMenu getMain(){
		return main;
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

	public void setSettings(SettingsScreen s) {
		settings = s;		
	}
}
