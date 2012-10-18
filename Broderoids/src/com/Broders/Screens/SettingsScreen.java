package com.Broders.Screens;

import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SettingsScreen implements Screen {

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
		
	}
	
	private void update(float delta) {
		
		tail.Update();		
	
	}

	private void handleInput(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		buff = 0;
		
		spriteBatch = new SpriteBatch();
//		 font = new BitmapFont(Gdx.files.internal("data/nameOfFont.fnt"),
//		         Gdx.files.internal("data/nameOfFont.png"), false);
		 
		 
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
