package com.Broders.Screens;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class SplashScreen implements Screen{

	private MainMenu Main;
	
	private SpriteBatch spriteBatch;
    private Texture splsh;
    private BaseGame myGame;
	
    //constructor
    public SplashScreen(BaseGame g){
    	
    	this.myGame = g;
    	
    	
    }
    
    
	@Override
	public void render(float delta) {
		
	
		
		HandleInput();

		GL10 g1 = Gdx.graphics.getGL10();
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		g1.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		myGame.getCam().update();
		
        spriteBatch.begin();
        spriteBatch.draw(splsh, 0, -175);
        spriteBatch.end();
        
        
		
		
	}

	public void HandleInput(){
		  if(Gdx.input.justTouched()){
              myGame.setScreen(new MainMenu(this.myGame));
		  //this.dispose();
		  }
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
        splsh = new Texture(Gdx.files.internal("data/Brocoders.png"));
		spriteBatch = new SpriteBatch();
	}

	@Override
	public void hide() {
		 // called when current screen changes from this to a different screen
		
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
		  splsh.dispose();
		  spriteBatch.dispose();
		
	}

}
