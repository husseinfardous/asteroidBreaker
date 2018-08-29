package com.husseinfardous.asteroidbreaker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.husseinfardous.asteroidbreaker.AsteroidBreaker;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = AsteroidBreaker.WIDTH;
		config.height = AsteroidBreaker.HEIGHT;
		config.title = AsteroidBreaker.TITLE;
		new LwjglApplication(new AsteroidBreaker(), config);
	}
}
