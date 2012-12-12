package com.Broders.Screens;

import com.Broders.Logic.Net;
import com.Broders.Logic.Settings;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.Broders.mygdxgame.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MultiHost implements Screen {

	private BaseGame myGame;
	private SpriteBatch spriteBatch;




	private String gameName;
	private String password;
	private String gameSize;
	private String gameDiff;
	private int limit;

	int worldSize = 0;

	private float xx;
	private float yy;

	public MultiHost(BaseGame game) {
		this.myGame = game;

		limit = 5;
		
		gameSize = "Small";
		worldSize = 0;
		switch(Settings.getDifficulty()){

		case 0:	gameDiff = "Easy";
		break;

		case 1:	gameDiff = "Medium";
		break;

		case 2:	gameDiff = "Hard";
		break;

		}

		gameName = Settings.getUsername()+"'s Game";
		password = "";

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
		if(Settings.getRetro()){
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}else{
			GL10 g1 = Gdx.graphics.getGL10();
			Gdx.gl.glClearColor(.19f, .19f, .19f, 1f);	 
			g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		spriteBatch.begin();

		TextureManager.getSprites("Ship1").setRotation(270);
		TextureManager.getSprites("Ship1").setPosition(xx * .28f, yy * .29f);
		TextureManager.getSprites("Ship1").draw(spriteBatch);
		
		TextureManager.getSprites("Ship1").setRotation(90);
		TextureManager.getSprites("Ship1").setPosition(xx * .075f, yy * .19f);
		TextureManager.getSprites("Ship1").draw(spriteBatch);


		// text
		myGame.font.setScale(.5f);
		myGame.font.draw(spriteBatch, "Game Name: " + this.gameName, xx * .1f, yy * .9f);
		myGame.font.draw(spriteBatch, "Password: " + this.password.replaceAll(".", "*"), xx * .1f, yy *.75f);
		myGame.font.draw(spriteBatch, "World Size : "+this.gameSize, xx * .1f, yy * .6f);
		myGame.font.draw(spriteBatch, "Difficulty: "+this.gameDiff, xx * .1f, yy * .45f);
		myGame.font.draw(spriteBatch, "Max Players: "+this.limit, xx * .1f, yy * .3f);
		myGame.font.draw(spriteBatch, "Play!", xx * .8f, yy * .9f);

		spriteBatch.end();

	}

	private void update(float delta) {


	}

	private void handleInput(float delta) {

		float inputx = Gdx.input.getX() / xx;
		float inputy = Gdx.input.getY() / yy;

		if ((Gdx.input.isKeyPressed(Keys.ESCAPE) || 
				Gdx.input.isKeyPressed(Keys.BACKSPACE)) ||
				(Gdx.app.getVersion() > 0 && Gdx.input.isKeyPressed(Keys.BACK))) {
			myGame.setScreen(BaseGame.screens.get("lobby"));
		}

		if (Gdx.input.isKeyPressed(Keys.F1)) {
			double x = ((float) Gdx.input.getX() / (float) Gdx.graphics
					.getWidth());
			double y = ((float) Gdx.input.getY() / (float) Gdx.graphics
					.getHeight());
			System.out.println("Mouse Pos: " + x + " " + y);
		}

		if (Gdx.input.justTouched()) {
			
			//arrows
			if(inputy >= .678 && inputy <= .772){
				
				if(inputx >= .016 && inputx <= .095){
					
				}else if(inputx >= .291 && inputx <= .367){
					
				}
				
			}
			
			
			

			if (inputx >= .096 && inputx <= .590) {
				//Game name Text
				if(inputy >= 0.092 && inputy <= .173){
					String pretext = "";

					if (this.gameName == null || this.gameName.equals("") ||
							this.gameName.length() > 28) {
						pretext = Settings.getUsername() + "'s Game";
					} else {
						pretext = this.gameName;
					}

					Gdx.input.getTextInput(new TextInputListener() {
						@Override
						public void input (String text) {
							if (text.equals("") || text.length() > 28) {
								gameName = Settings.getUsername() + "'s Game";
							} else {
								gameName = text;
							}
						}
						@Override
						public void canceled () {}
					}, "Enter Game Name", pretext);	

				}

				// Game password text
				if(inputy >= .246 && inputy <= .315){

					String pretext = "";

					Gdx.input.getTextInput(new TextInputListener() {
						@Override
						public void input (String text) {
							if (text.equals("") || text.length() > 28) {
								password = "";
							} else {
								password = text;
							}
						}
						@Override
						public void canceled () {}
					}, "Enter Game Password", pretext);	
				}
				//world size
				if(inputy >= .394 && inputy <= .470){
					worldSize = (worldSize + 1)%3;
					switch(worldSize){

					case 0:	gameSize = "Small";
					break;

					case 1:	gameSize = "Medium";
					break;

					case 2:	gameSize = "Large";
					break;

					}
				}

				//diff
				if(inputy >= .543 && inputy <= .616){
					Settings.setDifficulty((Settings.getDifficulty() + 1)%3);

					switch(Settings.getDifficulty()){

					case 0:	gameDiff = "Easy";
					break;

					case 1:	gameDiff = "Medium";
					break;

					case 2:	gameDiff = "Hard";
					break;

					}
				}

			}



			//play Button
			if (inputy > .09 && inputy < .173) {
				if (inputx > .795 && inputx < .863) {
					myGame.gameSize = worldSize;
					int x;
					int y;
					if (worldSize == 0) {
						x = Gdx.graphics.getWidth();
						y = Gdx.graphics.getHeight();
					} else if (worldSize == 1) {
						x = 500;
						y = 500;
					} else {
						x = 1000;
						y = 1000;
					}
					// TODO: Pass the right values
					Screen s = Net.newGame(this.gameName, 5, x, y, this.password);
					if (s != null) {
						SoundManager.play("click", 0.7f);
						myGame.setScreen(s);
					} else {
						// Error
					}
				}
			}
		}

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();

		TextureManager.getSprites("Ship1").setSize(yy * .15f, yy * .15f);
		TextureManager.getSprites("Ship1").setOrigin((yy * .05f)/2f, (yy * .05f)/2f);
		TextureManager.getSprites("Ship1").setRotation(0);
	}

	@Override
	public void hide() {
		//this.dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		this.spriteBatch.dispose();

	}

}
