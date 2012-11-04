package com.Broders.Screens;

import java.util.Random;

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


	//Test Variables?
	int gameCount;
	int page;
	int curPage;
	int selectedGame;

	String out;


	public MultiLobby(BaseGame game){
		this.myGame = game;

		tail = new Tail(5,Color.WHITE);

		font = this.myGame.font;
		font.setScale(.5f);

		xx = Gdx.graphics.getWidth();
		yy = Gdx.graphics.getHeight();

		//temp
		gameCount = 17;
		page = gameCount/5;
		curPage = 0;
		
		selectedGame = -1;



	}

	@Override
	public void render(float delta) {

		update(delta);
		handleInput(delta);
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

		//tabs
		if(page > 0){							//TODO Ref Games from server

			if(curPage < page){			//display both tabs

				whiteSprite.setColor(Color.WHITE);
				whiteSprite.setSize(xx*.08f, xx*.08f);
				whiteSprite.setPosition(xx*.0375f, yy*.3f);
				whiteSprite.draw(spriteBatch);

				whiteSprite.setRotation(45f);
				whiteSprite.setSize(xx*.0565685f, xx*.0565685f);			
				whiteSprite.setPosition(xx*.0622f, yy*.239f);	
				whiteSprite.draw(spriteBatch);
				whiteSprite.setRotation(0f);

				whiteSprite.setColor(Color.BLACK);
				whiteSprite.setSize(xx*.065f, xx*.065f);
				whiteSprite.setPosition(xx*.045f, yy*.315f);
				whiteSprite.draw(spriteBatch);

				whiteSprite.setRotation(45f);
				whiteSprite.setSize(xx*.0459619f, xx*.0459619f);			
				whiteSprite.setPosition(xx*.0622f, yy*.266f);	
				whiteSprite.draw(spriteBatch);
				whiteSprite.setRotation(0f);

				out = String.format("%d ", curPage+1);
				font.draw(spriteBatch, out, xx*.07f, yy*.4f);

			}

			if(curPage > 0){
				//you are on the last tab display the top
				whiteSprite.setColor(Color.WHITE);
				whiteSprite.setSize(xx*.08f, xx*.08f);
				whiteSprite.setPosition(xx*.0375f, yy*.5f);
				whiteSprite.draw(spriteBatch);

				whiteSprite.setRotation(45f);
				whiteSprite.setSize(xx*.0565685f, xx*.0565685f);			
				whiteSprite.setPosition(xx*.0622f, yy*.584f);									//	A = 32^.5
				whiteSprite.draw(spriteBatch);
				whiteSprite.setRotation(0f);

				whiteSprite.setColor(Color.BLACK);
				whiteSprite.setSize(xx*.065f, xx*.065f);
				whiteSprite.setPosition(xx*.045f, yy*.51f);
				whiteSprite.draw(spriteBatch);

				whiteSprite.setRotation(45f);
				whiteSprite.setSize(xx*.0459619f, xx*.0459619f);			
				whiteSprite.setPosition(xx*.0622f, yy*.58f);									//	A = 21.125^.5
				whiteSprite.draw(spriteBatch);
				whiteSprite.setRotation(0f);
				whiteSprite.setColor(Color.WHITE);

				out = String.format("%d ", curPage);
				font.draw(spriteBatch, out, xx*.07f, yy*.6f);
			}

		}



		//text
		//join
		font.draw(spriteBatch, "Join", xx*.31f, yy*.93f);

		font.draw(spriteBatch, "Host", xx*.09f, yy*.93f);

		whiteSprite.setSize(xx*.85f, yy*.01f);
		whiteSprite.setColor(Color.WHITE);
		//game list
		for(int i = 0; i < (gameCount-(curPage*5)) && i < 5;i++){


			whiteSprite.setPosition(xx*.15f, yy*(.8f - (.16f*((float)i+1))));
			whiteSprite.draw(spriteBatch);

			out = String.format("Total Players: %d ", i);							//TODO ref total players
			font.draw(spriteBatch, out, xx*.7f,yy*(.73f - (.16f*i)));
			
			font.draw(spriteBatch, "Name of Game", xx*.2f,yy*(.73f - (.16f*i)));	//TODO ref Name of Game

		}




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
			}else if(y >= .55 && y <= .77){
				if(x >= .037 && x <= .116){
					if(curPage < page){
						curPage++;
						selectedGame = -1;
					}
				}
			}else if(y >= .288 && y <= .498){
				if(x >= .038 && x <= .116){
					if(curPage > 0){
						selectedGame = -1;
						curPage--;
					}
						
				}
			}
		}

		if(Gdx.input.isTouched()){
			tail.add(new Pos(Gdx.input.getX(),Gdx.input.getY()));
		}


		if(Gdx.input.isKeyPressed(Keys.UP)){
			if(curPage > 0){
				curPage--;
			}
		}

		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			if(curPage < page){
				curPage++;
			}
		}

		if(Gdx.input.isKeyPressed(Keys.F1)){
			double x = ((float)Gdx.input.getX()/(float)Gdx.graphics.getWidth());
			double y = ((float)Gdx.input.getY()/(float)Gdx.graphics.getHeight());
			System.out.println("Mouse Pos: "+x+" "+y);
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
