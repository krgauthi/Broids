package com.Broders.Screens;

import com.Broders.Logic.CoreLogic;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MultiHost implements Screen{

	private BaseGame myGame;
	private SpriteBatch spriteBatch;
	private BitmapFont font;

	private Texture white;
	private Sprite whiteSprite;

	int worldSize[] = {1,1,0};

	private float xx;
	private float yy;


	public MultiHost(BaseGame game){
		this.myGame = game;

		font = this.myGame.font;
		font.setScale(.5f);

		xx = Gdx.graphics.getWidth();
		yy = Gdx.graphics.getHeight();
	}

	@Override
	public void render(float delta) {
		handleInput(delta);
		update(delta);
		paint(delta);
	}

	private void paint(float delta) {
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0, 1); 
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();

		//Box Selections
		if(worldSize[0] == 1){
			whiteSprite.setColor(Color.WHITE);
			whiteSprite.setSize(xx * .085f, yy * .08f);
			whiteSprite.setPosition(xx * .095f, yy * .63f);
			whiteSprite.draw(spriteBatch);
			whiteSprite.setColor(Color.BLACK);
			whiteSprite.setSize(xx * .08f, yy * .073f);
			whiteSprite.setPosition(xx * .097f, yy * .634f);
			whiteSprite.draw(spriteBatch);
			
		}

		if(worldSize[1] == 1){
			whiteSprite.setColor(Color.WHITE);
			whiteSprite.setSize(xx * .13f, yy * .08f);
			whiteSprite.setPosition(xx * .186f, yy * .63f);
			whiteSprite.draw(spriteBatch);
			whiteSprite.setColor(Color.BLACK);
			whiteSprite.setSize(xx * .11f, yy * .073f);
			whiteSprite.setPosition(xx * .188f, yy * .634f);
			whiteSprite.draw(spriteBatch);
		}

		if(worldSize[2] == 1){

		}

		//text
		font.draw(spriteBatch, "Muliplayer Options", xx*.4f, yy*.9f);
		font.draw(spriteBatch, "World Size", xx*.17f, yy*.8f);
		font.draw(spriteBatch, "Small", xx*.1f, yy*.7f);
		font.draw(spriteBatch, "Medium", xx*.19f, yy*.7f);
		font.draw(spriteBatch, "Large", xx*.32f, yy*.7f);

		spriteBatch.end();

	}

	private void update(float delta) {
		// TODO Auto-generated method stub

	}

	private void handleInput(float delta) {
		//TODO go back to multilobby
		if((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK))){
			myGame.setScreen(new MultiLobby(this.myGame));
		}


	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();

		white = new Texture(Gdx.files.internal("data/whitebox.png"));
		whiteSprite = new Sprite(white,32,32);

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
