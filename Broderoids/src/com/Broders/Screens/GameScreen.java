package com.Broders.Screens;

import java.util.LinkedList;

import com.Broders.Entities.*;
import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.InputDir;
import com.Broders.Logic.Pos;
import com.Broders.Logic.Tail;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;




public class GameScreen implements Screen{

	private BaseGame myGame;
	
	private boolean Multiplayer;
	private boolean DEBUG;
	
	private Texture dPadTexture;
	private Texture fireButtonTexture;
	private Texture thrusterButtonTexture;
	
	private Sprite dPad;
	private Sprite fireButton;
	private Sprite thrusterButton;

	private Ship PlayerShip;

	private SpriteBatch spriteBatch;

	private Tail Tail;

	private BitmapFont font;

	private CoreLogic core;

	float xx;
	float yy;



	public GameScreen(BaseGame game, boolean m){
		this.myGame = game;
		this.Multiplayer = m;

		if(m){
			System.out.println("Multi");
		}else{
			System.out.println("Single");
		}


		Tail = new Tail(5);
		font = this.myGame.font;
		font.setScale(.25f);
		DEBUG = true;
		
		CoreLogic.initCore();

		xx = Gdx.graphics.getWidth();
		yy = Gdx.graphics.getHeight();

	}

	@Override
	public void render(float delta) {

		//handle Input and update Backend
		//it is up to the backend team to decide if they want to handle input seperatly or not
		handleInput(delta);
		update(delta);
		
		//this really should be put back in CoreLogic if possible
		CoreLogic.getWorld().step(delta, 3, 8);

		//server interactions here?

		//update the models on the screen
		paint(delta);


	}

	private void paint(float delta) {
		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0, 1); //its blue so you know you changed screens
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Start Drawing
		spriteBatch.begin();


		Tail.draw(spriteBatch);

		//loop through all Entities
		for(Entry<String, Entities> E : CoreLogic.getEntityMap().entries()){
			E.value.Draw(spriteBatch, core);
		}


		if(DEBUG){
			String out;

			out = String.format("Ship Pos in Meters: (%f,%f) ", CoreLogic.getEntityMap().get("player").getBody().getPosition().x, CoreLogic.getEntityMap().get("player").getBody().getPosition().y);
			font.draw(spriteBatch, out, xx * .01f, yy-(yy * .01f));

			out = String.format("Ship angle in Radians: %f", CoreLogic.getEntityMap().get("player").getBody().getAngle());
			font.draw(spriteBatch, out, xx * .01f, yy-(yy * .05f));
			if(((Ship)CoreLogic.getEntityMap().get("player")).getThrust())
				font.draw(spriteBatch, "Thruster", xx * .01f, yy-(yy * .09f));

		}
		
		dPad.draw(spriteBatch);
		fireButton.draw(spriteBatch);
		thrusterButton.draw(spriteBatch);
		

		spriteBatch.end();




	}

	private void update(float delta) {

		//EntityMap.get("player").SetPos(new Pos(.45f, .25f));
		CoreLogic.execute(delta, InputDir.NULL);
		Tail.Update();

	}

	private void handleInput(float delta) {

		//Special Debug keys
		if(Gdx.input.isKeyPressed(Keys.F1)){
			double x = ((float)Gdx.input.getX()/(float)Gdx.graphics.getWidth());
			double y = ((float)Gdx.input.getY()/(float)Gdx.graphics.getHeight());
			System.out.println("Mouse Pos: "+x+" "+y);
		}

		if(Gdx.input.isKeyPressed(Keys.F2)){

			System.out.println("Mouse Pos: "+Gdx.input.getX()+" "+Gdx.input.getY());
		}

		if(Gdx.input.isKeyPressed(Keys.F3)){
			System.out.println("Resize: "+Gdx.graphics.getWidth()+" "+Gdx.graphics.getHeight());
		}


		//touch tail
		if(Gdx.input.isTouched()){
			Tail.add(new Pos(Gdx.input.getX(),Gdx.input.getY()));
		}


		//arrow keys
		if(Gdx.input.isKeyPressed(Keys.UP)){
			CoreLogic.execute(delta, InputDir.FORWARD);
		}

		if(Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)){
			CoreLogic.execute(delta, InputDir.LEFT);
		}

		if(Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT)){
			CoreLogic.execute(delta, InputDir.RIGHT);
		}


		//Backout to main menu
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)){
			myGame.setScreen(myGame.GetMain());
		}
	}

	@Override
	public void resize(int width, int height) {


	}

	@Override
	public void show() {

		dPadTexture = new Texture(Gdx.files.internal("data/leftrightpad.png"));
		dPad = new Sprite(dPadTexture,512,512);
		dPad.setPosition(myGame.screenWidth*(0),myGame.screenHeight*(-.1f));
		dPad.setSize(myGame.screenHeight*.6f, myGame.screenHeight*.6f);
		
		fireButtonTexture = new Texture(Gdx.files.internal("data/fireButton.png"));
		fireButton = new Sprite(fireButtonTexture,512,512);
		fireButton.setPosition(myGame.screenWidth*(.82f),myGame.screenHeight*(.25f));
		fireButton.setSize(myGame.screenHeight*.32f, myGame.screenHeight*.32f);
		
		thrusterButtonTexture = new Texture(Gdx.files.internal("data/thrustButton.png"));
		thrusterButton = new Sprite(thrusterButtonTexture,512,512);
		thrusterButton.setPosition(myGame.screenWidth*(.69f),myGame.screenHeight*0);
		thrusterButton.setSize(myGame.screenHeight*.32f, myGame.screenHeight*.32f);

		spriteBatch = new SpriteBatch();

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



	}

}
