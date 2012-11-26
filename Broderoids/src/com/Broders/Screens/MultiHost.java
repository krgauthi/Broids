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

	private Texture ship;
	private Sprite shipSprite;

	int worldSize = 0;

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
		if(worldSize == 0){
			shipSprite.setPosition(xx*.12f, yy*.58f);
			shipSprite.draw(spriteBatch);
		}

		if(worldSize == 1){
			shipSprite.setPosition(xx*.23f, yy*.58f);
			shipSprite.draw(spriteBatch);
		}

		if(worldSize == 2){
			shipSprite.setPosition(xx*.33f, yy*.58f);
			shipSprite.draw(spriteBatch);
		}

		//text
		font.draw(spriteBatch, "Muliplayer Options", xx*.4f, yy*.9f);
		font.draw(spriteBatch, "World Size", xx*.17f, yy*.8f);
		font.draw(spriteBatch, "Small", xx*.1f, yy*.7f);
		font.draw(spriteBatch, "Medium", xx*.19f, yy*.7f);
		font.draw(spriteBatch, "Large", xx*.31f, yy*.7f);
		
		font.draw(spriteBatch, "Play!", xx*.8f, yy*.9f);

		spriteBatch.end();

	}

	private void update(float delta) {
		// TODO Auto-generated method stub

	}

	private void handleInput(float delta) {

		float inputx = Gdx.input.getX()/xx;
		float inputy = Gdx.input.getY()/yy;

		//TODO go back to multilobby
		if((Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK))){
			myGame.setScreen(new MultiLobby(this.myGame));
		}

		if(Gdx.input.isKeyPressed(Keys.F1)){
			double x = ((float)Gdx.input.getX()/(float)Gdx.graphics.getWidth());
			double y = ((float)Gdx.input.getY()/(float)Gdx.graphics.getHeight());
			System.out.println("Mouse Pos: "+x+" "+y);
		}

		if(Gdx.input.justTouched()){
			//world size
			if(inputy < .364 && inputy > .289){
				//small
				if(inputx > .098 && inputx < .173){
					worldSize = 0;
				}
				//medium
				if(inputx > .187 && inputx < .292){
					worldSize = 1;
				}
				//large
				if(inputx > .306 && inputx < .382){
					worldSize = 2;
				}
			}
			
			
			if(inputy > .09 && inputy < .173){
				if(inputx > .795 && inputx < .863){
					myGame.gameSize = worldSize;
					myGame.setScreen(new GameScreen(myGame, true));
				}
			}
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

		ship = new Texture(Gdx.files.internal("data/ship1.png"));
		shipSprite = new Sprite(ship,1024,1024);
		shipSprite.setSize(yy*.05f, yy*.05f);

	}

	@Override
	public void hide() {
		this.dispose();
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
		this.spriteBatch.dispose();
		this.white.dispose();
		this.ship.dispose();

	}

}
