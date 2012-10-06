package com.Broders.Screens;


import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;




public class MainMenu implements Screen{


	private SpriteBatch spriteBatch;

	private Texture Title;
	private Texture Single;
	private Texture Multiplayer;
	private Texture Settings;


	private BaseGame myGame;

	public MainMenu(BaseGame g){

		this.myGame = g;
		myGame.setMain(this);



	}



	@Override
	public void render(float delta) {

		//handle Input and update Backend
		//it is up to the backend team to decide if they want to handle input seperatly or not
		HandleInput();
		Update();

		//server interactions here?

		//update the models on the screen
		Paint();



	}


	private void Paint() {
		//Make a black background
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);


		//add buttons and logo to this layer
		spriteBatch.begin();
		spriteBatch.draw(Title, 0, -175);
		spriteBatch.draw(Single, 650, 200,200,200);
		spriteBatch.draw(Multiplayer, 650, 100,200,200);
		spriteBatch.draw(Settings, 650, 0,200,200);
		spriteBatch.end();


		//end with updating the cam
		myGame.getCam().update();

	}



	/*
	 * This method is where all of the backend will be calculated
	 */
	private void Update() {


	}



	public void HandleInput(){

		if(Gdx.input.justTouched()){
			//make hit boxes
			if(Gdx.input.getX() >= 650 && Gdx.input.getX() <= 850){
				
				//single player game X 650-850 Y 180 - 230
				if(Gdx.input.getY() >= 180 && Gdx.input.getY() <= 230){
					myGame.setScreen(new GameScreen(this.myGame,false));
				}else if(Gdx.input.getY() >= 280 && Gdx.input.getY() <= 330){
					myGame.setScreen(new GameScreen(this.myGame,true));
				}else if(Gdx.input.getY() >= 380 && Gdx.input.getY() <= 430){
					System.out.println("Settings");
				}
				
				
			}
			
			
			
			
		}

		if(Gdx.input.isKeyPressed(Keys.F1)){
			System.out.println("Mouse Pos: "+Gdx.input.getX()+" "+Gdx.input.getY());
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
		Title = new Texture(Gdx.files.internal("data/Broderoids.png"));
		Single = new Texture(Gdx.files.internal("data/SinglePlayer.png"));
		Multiplayer = new Texture(Gdx.files.internal("data/Multiplayer.png"));
		Settings = new Texture(Gdx.files.internal("data/Settings.png"));
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
