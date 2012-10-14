package com.Broders.Screens;


import com.Broders.Logic.Pos;
import com.Broders.Logic.tail;
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

	private Texture Title;
	private Texture Single;
	private Texture Multiplayer;
	private Texture Settings;
	
	private Sprite Titleb;
	private Sprite Singleb;
	private Sprite Multib;
	private Sprite Settingsb;
	
	private tail Tail;


	private BaseGame myGame;

	public MainMenu(BaseGame g){

		this.myGame = g;
		myGame.setMain(this);


		Tail = new tail(5);

	}



	@Override
	public void render(float delta) {

		//handle Input and update Backend
		//it is up to the backend team to decide if they want to handle input separately or not
		HandleInput();
		Update();

		//server interactions here?

		//update the models on the screen
		Paint();



	}


	private void Paint() {
		
		float x = ((float)Gdx.graphics.getWidth());
		float y = ((float)Gdx.graphics.getHeight());
		
		//Make a black background
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		
		//myGame.getCam().update();

		//add buttons and logo to this layer
		spriteBatch.begin();
		Titleb.draw(spriteBatch);
		Singleb.draw(spriteBatch);
		Multib.draw(spriteBatch);
		Settingsb.draw(spriteBatch);
		Tail.draw(spriteBatch);
		spriteBatch.end();

		
		

		

	}



	/*
	 * This method is where all of the backend will be calculated
	 */
	private void Update() {

		Tail.Update();

	}



	public void HandleInput(){

		if(Gdx.input.isTouched()){
			Tail.add(new Pos(Gdx.input.getX(),Gdx.input.getY()));
		}
		
		if(Gdx.input.justTouched()){
			double x = ((float)Gdx.input.getX()/(float)Gdx.graphics.getWidth());
			double y = ((float)Gdx.input.getY()/(float)Gdx.graphics.getHeight());
			
			
			
			
			//make hit boxes
			if(x >= .57 && x <= .81){
				
				//single player game X 650-850 Y 180 - 230
				if(y >= .16 && y <= .26){
					myGame.setScreen(new GameScreen(this.myGame,false));
				}else if(y >= .36 && y <= .46){
					myGame.setScreen(new GameScreen(this.myGame,true));
				}else if(y >= .56 && y <= .66){
					System.out.println("Settings");
				}
			}
			
			
			
			
		}

		if(Gdx.input.isKeyPressed(Keys.F1)){
			double x = ((float)Gdx.input.getX()/(float)Gdx.graphics.getWidth());
			double y = ((float)Gdx.input.getY()/(float)Gdx.graphics.getHeight());
			System.out.println("Mouse Pos: "+x+" "+y);
		}


		
		
		//backout Fix the quick exit from gamescreen
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Gdx.app.exit();
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
		float x = ((float)Gdx.graphics.getWidth());
		float y = ((float)Gdx.graphics.getHeight());
		
		Title = new Texture(Gdx.files.internal("data/Broderoids.png"));
		Titleb = new Sprite(Title,512,200);
		Titleb.setPosition(0, (float) (y*.1));
		//Titleb.setScale(.5f, .5f);
		
		Single = new Texture(Gdx.files.internal("data/SinglePlayer.png"));
		Singleb = new Sprite(Single,512,200);
		Singleb.setPosition((float)(x*.45),(float) (y*.6));
		Singleb.setScale(.5f,.5f);
		
		Multiplayer = new Texture(Gdx.files.internal("data/Multiplayer.png"));
		Multib = new Sprite(Multiplayer,512,200);
		Multib.setPosition((float)(x*.45), (float)(y*.4));
		Multib.setScale(.5f,.5f);
		
		Settings = new Texture(Gdx.files.internal("data/Settings.png"));
		Settingsb = new Sprite(Settings,512,200);
		Settingsb.setPosition((float)(x*.45),(float)(y*.2));
		Settingsb.setScale(.5f,.5f);
		
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
