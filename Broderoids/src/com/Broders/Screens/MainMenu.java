package com.Broders.Screens;


import com.Broders.Logic.Pos;
import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;




public class MainMenu implements Screen{


	private SpriteBatch spriteBatch;

	private Texture titleTex;
	private Texture singleTex;
	private Texture multiTex;
	private Texture settingsTex;
	
	private Sprite titleSprite;
	private Sprite singleSprite;
	private Sprite multiSprite;
	private Sprite settingsSprite;
	
	private Tail tail;
	
	private float buff;


	private BaseGame myGame;

	public MainMenu(BaseGame g){

		this.myGame = g;
		myGame.setMain(this); //now all screens can reference back to this main menu
		tail = new Tail(myGame.tailLength);

	}



	@Override
	public void render(float delta) {

		//handle Input and update Backend
		//it is up to the backend team to decide if they want to handle input separately or not
		handleInput(delta);
		update(delta);

		//server interactions here?

		//update the models on the screen
		paint(delta);



	}


	private void paint(float delta) {

		//Make a black background
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//add buttons and logo to this layer
		spriteBatch.begin();
		titleSprite.draw(spriteBatch);
		singleSprite.draw(spriteBatch);
		multiSprite.draw(spriteBatch);
		settingsSprite.draw(spriteBatch);
		tail.draw(spriteBatch);
		spriteBatch.end();

	}

	/*
	 * This method is where all of the backend will be calculated
	 */
	private void update(float delta) {

		tail.Update();

	}



	public void handleInput(float delta){

		
		
		if(Gdx.input.isTouched()){
			tail.add(new Pos(Gdx.input.getX(),Gdx.input.getY()));
		}
		
		if(Gdx.input.justTouched()){
			double x = ((float)Gdx.input.getX()/(float)myGame.screenWidth);
			double y = ((float)Gdx.input.getY()/(float)myGame.screenHeight);
			
			//make hit boxes
			if(x >= .55 && x <= .82){
				
				//single player game X 650-850 Y 180 - 230
				if(y >= .20 && y <= .28){
					myGame.setScreen(new GameScreen(this.myGame,false));
				}else if(y >= .32 && y <= .43){
					myGame.setScreen(new GameScreen(this.myGame,true));
				}else if(y >= .48 && y <= .60){
					myGame.setScreen(new SettingsScreen(this.myGame, this));
				}
			}
			
			
			
			
		}

		if(Gdx.input.isKeyPressed(Keys.F1)){
			double x = ((float)Gdx.input.getX()/(float)Gdx.graphics.getWidth());
			double y = ((float)Gdx.input.getY()/(float)Gdx.graphics.getHeight());
			System.out.println("Mouse Pos: "+x+" "+y);
		}


		
		
		
		//backout Fix the quick exit from gamescreen
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) && buff > myGame.exitBuffer){
			Gdx.app.exit();
		}else{
			if(buff < myGame.exitBuffer){
				buff = buff + delta;
			}
		}

	}


	@Override
	public void resize(int width, int height) {


	}

	/*
	 * Called when the MainMenu is first started
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		buff = 0;
		
		titleTex = new Texture(Gdx.files.internal("data/Broderoids.png"));
		titleSprite = new Sprite(titleTex,512,512);
		titleSprite.setPosition(0,myGame.screenHeight*(-.5f));
		titleSprite.setSize(myGame.screenHeight, myGame.screenHeight);
		
		singleTex = new Texture(Gdx.files.internal("data/SinglePlayer.png"));
		singleSprite = new Sprite(singleTex,512,512);
		singleSprite.setPosition(myGame.screenWidth*.55f,myGame.screenHeight*.35f);
		singleSprite.setSize(myGame.screenHeight*.5f,myGame.screenHeight*.5f);
		
		multiTex = new Texture(Gdx.files.internal("data/Multiplayer.png"));
		multiSprite = new Sprite(multiTex,512,512);
		multiSprite.setPosition(myGame.screenWidth*.55f,myGame.screenHeight*.2f);
		multiSprite.setSize(myGame.screenHeight*.5f,myGame.screenHeight*.5f);
		
		settingsTex = new Texture(Gdx.files.internal("data/Settings.png"));
		settingsSprite = new Sprite(settingsTex,512,512);
		settingsSprite.setPosition(myGame.screenWidth*.55f,myGame.screenHeight*.05f);
		settingsSprite.setSize(myGame.screenHeight*.5f,myGame.screenHeight*.5f);
		
		spriteBatch = new SpriteBatch();

	}

	@Override
	public void hide() {


	}

	@Override
	public void pause() {


	}

	@Override
	public void resume() {


	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
