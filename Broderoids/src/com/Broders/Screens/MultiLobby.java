package com.Broders.Screens;

import com.Broders.Logic.Pos;
import com.Broders.Logic.Tail;
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

public class MultiLobby implements Screen{

	private Tail tail;

	private SpriteBatch spriteBatch;

	private BaseGame myGame;
	private BitmapFont font;

	private Texture white;

	float buff;
	
	private Sprite whiteSprite;

	float xx;
	float yy;

	public MultiLobby(BaseGame game){
		this.myGame = game;

		tail = new Tail(5,Color.WHITE);

		font = this.myGame.font;
		font.setScale(.5f);

		xx = Gdx.graphics.getWidth();
		yy = Gdx.graphics.getHeight();

	}

	@Override
	public void render(float delta) {
		handleInput(delta);
		update(delta);

		//server interactions here?

		paint(delta);
	}

	private void paint(float delta) {
		//Make a black background
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);


		spriteBatch.begin();
		//Boxes

		whiteSprite.setColor(Color.WHITE);
		//horizontal line
		whiteSprite.setSize(xx*.85f, yy*.01f);
		whiteSprite.setPosition(xx*.15f, yy*.8f);
		whiteSprite.draw(spriteBatch);

		//vertical line
		whiteSprite.setSize(yy*.01f, yy*.8f);
		whiteSprite.setPosition(xx*.15f,0);
		whiteSprite.draw(spriteBatch);

		//host and Join game button white
		whiteSprite.setSize(xx*.2f, yy*.1f);
		whiteSprite.setPosition(xx*.02f, yy*.85f);
		whiteSprite.draw(spriteBatch);
		
		whiteSprite.setSize(xx*.2f, yy*.1f);
		whiteSprite.setPosition(xx*.24f, yy*.85f);
		whiteSprite.draw(spriteBatch);
		

		//host and join game button negative
		whiteSprite.setColor(Color.BLACK);
		whiteSprite.setSize(xx*.18f, yy*.08f);
		whiteSprite.setPosition(xx*.03f, yy*.86f);
		whiteSprite.draw(spriteBatch);
		
		whiteSprite.setSize(xx*.18f, yy*.08f);
		whiteSprite.setPosition(xx*.25f, yy*.86f);
		whiteSprite.draw(spriteBatch);
		
		
		




		//text
		//join
		font.draw(spriteBatch, "Join", xx*.31f, yy*.93f);

		font.draw(spriteBatch, "Host", xx*.09f, yy*.93f);

		tail.draw(spriteBatch);


		spriteBatch.end();


	}

	private void update(float delta) {
		tail.update();

	}

	private void handleInput(float delta) {

		if(Gdx.input.justTouched()){
			double x = ((float)Gdx.input.getX()/(float)myGame.screenWidth);
			double y = ((float)Gdx.input.getY()/(float)myGame.screenHeight);

			//make hit boxes
			if(y >= .05 && y <= .15){

				//join
				if(x >= .24 && x <= .44){
					myGame.setScreen(new GameScreen(this.myGame,true));
				}else if(x >= .02 && x <= .22){
					myGame.setScreen(new MultiHost(this.myGame));
				}
			}
		}

		if(Gdx.input.isTouched()){
			tail.add(new Pos(Gdx.input.getX(),Gdx.input.getY()));
		}

		//Backout to main menu
		if((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)) && buff > myGame.exitBuffer){
			myGame.setScreen(myGame.getMain());
		}else{
			if(buff < myGame.exitBuffer){
				buff = buff + delta;
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

		white = new Texture(Gdx.files.internal("data/whitebox.png"));
		whiteSprite = new Sprite(white,32,32);
		whiteSprite.setColor(Color.WHITE);

		spriteBatch = new SpriteBatch();

	}

	@Override
	public void hide() {
		//font.setScale(.25f);
		font.setColor(Color.WHITE);

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
