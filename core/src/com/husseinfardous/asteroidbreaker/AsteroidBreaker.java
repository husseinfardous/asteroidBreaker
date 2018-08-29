package com.husseinfardous.asteroidbreaker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.husseinfardous.asteroidbreaker.gameStates.GameStateManager;
import com.husseinfardous.asteroidbreaker.gameStates.MenuState;
import com.husseinfardous.asteroidbreaker.gameStates.PlayState;

public class AsteroidBreaker extends ApplicationAdapter {

	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "Asteroid Breaker";

	public static Stage stage;

	private GameStateManager gsm;
	private SpriteBatch batch;

	public static boolean beginAdStartTime = false;
	public static boolean adLoaded = false;
	private AdHandler handler;
	private long adStartTime;

	public AsteroidBreaker(AdHandler handler) {
		this.handler = handler;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render() {
		if (beginAdStartTime) {
			handler.showAds(true);
			adStartTime = System.currentTimeMillis();
			beginAdStartTime = false;
		}

		if (adLoaded) {
			long timeElapsed = (System.currentTimeMillis() - adStartTime) / 1000;
			if (timeElapsed == 10) {
				handler.showAds(false);
			}

			if (timeElapsed == 58) {
				handler.showAds(true);
				adLoaded = false;
			}
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
		gsm.render(batch);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
		PlayState.isPaused = true;
		PlayState.returnToPlayState = true;
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		stage.dispose();
	}
}
