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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;




public class GameScreen implements Screen{

<<<<<<< HEAD
	/*
	 * I pushed up a lot of stuff from CoreLogic to here, mostly the
	 * ship and world stuff.  Seemed like their could be redundancies,
	 * but as far as I can tell the changes didn't affect anything.  Code
	 * looks cleaner though.  Now CoreLogic receives the World object from
	 * here, and only deals with the BODY of the ship, which is passed up
	 * to the Ship Entity here.  Check out CoreLogic for some other
	 * changes I made.
	 * 
	 * -Rinkus
	 */


=======
>>>>>>> fb70709187f3d48340d633fada0daba95d50c6eb
	private BaseGame myGame;

	private boolean Multiplayer;
	private boolean DEBUG;

	private Texture dPadTexture;
	private Texture fireButtonTexture;
	private Texture thrusterButtonTexture;

	private Sprite dPad;
	private Sprite fireButton;
	private Sprite thrusterButton;

	//private Ship PlayerShip;

	private SpriteBatch spriteBatch;

	private Tail Tail;

	private Tail debug1;
	private Tail debug2;

	private BitmapFont font;
<<<<<<< HEAD
	
	private CoreLogic core;
	
	private World world;
	
=======

>>>>>>> fb70709187f3d48340d633fada0daba95d50c6eb
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


<<<<<<< HEAD
		Tail = new tail(5);
		font = new BitmapFont();
		DEBUG = true;
		THRUSTER = false;
		
		world = new World(new Vector2(0.0f, 0.0f), true);
		EntityMap = new OrderedMap<String, Entities>();
		core = new CoreLogic(world);
		core.initCore();
		PlayerShip = new Ship("player", EntityType.SHIP, world);
		PlayerShip.setBody(core.getShip());
		EntityMap.put("player", PlayerShip);
=======
		Tail = new Tail(5,Color.WHITE);
		font = this.myGame.font;
		font.setScale(.25f);
>>>>>>> fb70709187f3d48340d633fada0daba95d50c6eb
		
		CoreLogic.initCore();
		
		DEBUG = m;				//multiplayer will be debug until settings is finished

		if(DEBUG){
			debug1 = new Tail(50,Color.MAGENTA);
			debug2 = new Tail(50,Color.CYAN);
		}

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
		for(Entry<String, Entity> E : CoreLogic.getEntities().entries()){
			E.value.Draw(spriteBatch);
		}


		if(DEBUG){
			String out;
			out = String.format("Ship Pos in Meters: (%f,%f) ", CoreLogic.getLocalShip().getBody().getPosition().x, CoreLogic.getLocalShip().getBody().getPosition().y);
			font.draw(spriteBatch, out, xx * .01f, yy-(yy * .01f));

			out = String.format("Ship angle in Radians: %f", CoreLogic.getLocalShip().getBody().getAngle());
			font.draw(spriteBatch, out, xx * .01f, yy-(yy * .05f));
			if(CoreLogic.getLocalShip().getThrust())
				font.draw(spriteBatch, "Thruster", xx * .01f, yy-(yy * .09f));
			debug1.draw(spriteBatch);
			debug2.draw(spriteBatch);


		}


		//If Android
		if(Gdx.app.getVersion() > 0){
			dPad.draw(spriteBatch);
			fireButton.draw(spriteBatch);
			thrusterButton.draw(spriteBatch);
		}


		spriteBatch.end();




	}

	private void update(float delta) {

		//EntityMap.get("player").SetPos(new Pos(.45f, .25f));
		CoreLogic.execute(delta, InputDir.NULL);
		Tail.update();
		
		if(DEBUG){
			debug1.update();
			debug2.update();
		}

	}

	private void handleInput(float delta) {


		if(DEBUG){
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

			if(Gdx.input.isTouched(0)){
				debug1.add(new Pos(Gdx.input.getX(0),Gdx.input.getY(0)));
			}

			if(Gdx.input.isTouched(1)){
				debug2.add(new Pos(Gdx.input.getX(1),Gdx.input.getY(1)));
			}

		}




		//arrow keys
		if(Gdx.input.isKeyPressed(Keys.UP)){
			CoreLogic.execute(delta, InputDir.FORWARD);
			CoreLogic.getLocalShip().setThrust(true);
		}else{
			CoreLogic.getLocalShip().setThrust(false);
		}

		if(Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)){
			CoreLogic.execute(delta, InputDir.LEFT);
		}

		if(Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT)){
			CoreLogic.execute(delta, InputDir.RIGHT);
		}


		//Backout to main menu
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)){				
			myGame.setScreen(myGame.getMain());
		}

		//if Android
		if(Gdx.app.getVersion() > 0){
			if(Gdx.input.isTouched(0) || Gdx.input.isTouched(1) || Gdx.input.isTouched(2)){

				float x1,x2,x3,y1,y2,y3;
				if(Gdx.input.isTouched(0)){
					x1 = ((float)Gdx.input.getX(0)/(float)myGame.screenWidth);
					y1 = ((float)Gdx.input.getY(0)/(float)myGame.screenHeight);
				}else{
					x1 = -1;
					y1 = -1;
				}
				if(Gdx.input.isTouched(1)){
					x2 = ((float)Gdx.input.getX(1)/(float)myGame.screenWidth);
					y2 = ((float)Gdx.input.getY(1)/(float)myGame.screenHeight);
				}else{
					x2 = -1;
					y2 = -1;
				}
				if(Gdx.input.isTouched(2)){
					x3 = ((float)Gdx.input.getX(2)/(float)myGame.screenWidth);
					y3 = ((float)Gdx.input.getY(2)/(float)myGame.screenHeight);
				}else{
					x3 = -1;
					y3 = -1;
				}


				if((.06 < x1 && x1 < .3
						|| .06 < x2 && x2 < .3
						|| .06 < x3 && x3 < .3) &&
						(.67 < y1 && y1 < .9
								|| .67 < y2 && y2 < .9
								|| .67 < y3 && y3 < .9)){
					//hitbox for left dpad overall
					if(.06 < x1 && x1 < .166
							|| .06 < x2 && x2 < .166
							|| .06 < x3 && x3 < .166){

						CoreLogic.execute(delta, InputDir.LEFT);

					}else{
						CoreLogic.execute(delta, InputDir.RIGHT);
					}
				}else{
					//touch tail
					Tail.add(new Pos(Gdx.input.getX(),Gdx.input.getY()));
				}

				//check for button touch
				if((.7 < x1 && x1 < .85
						|| .7 < x2 && x2 < .85
						|| .7 < x3 && x3 < .85) &&
						(.72 < y1 && y1 < .98
								|| .72 < y2 && y2 < .98
								|| .72 < y3 && y3 < .98)){

					CoreLogic.execute(delta, InputDir.FORWARD);	
				}
				if((.83 < x1 && x1 < .98
						|| .83 < x2 && x2 < .98
						|| .83 < x3 && x3 < .98) &&
						(.47 < y1 && y1 < .73
								|| .47 < y2 && y2 < .73
								|| .47 < y3 && y3 < .73)){
									//pew pew

				}

			}

		}




	}

	@Override
	public void resize(int width, int height) {


	}

	@Override
	public void show() {



		if(Gdx.app.getVersion() > 0){
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

		}
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
