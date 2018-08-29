package com.husseinfardous.asteroidbreaker.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.husseinfardous.asteroidbreaker.AsteroidBreaker;

public class MenuState extends GameState {

    private Texture background, asteroids;
    private BitmapFont font;
    private int highscore;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, AsteroidBreaker.WIDTH / 2, AsteroidBreaker.HEIGHT / 2);
        cam.update();

        background = new Texture("background.png");
        asteroids = new Texture("asteroids.png");
        font = new BitmapFont();

        Preferences prefs = Gdx.app.getPreferences("savedAsteroidBreakerData");
        this.highscore = prefs.getInteger("highscore", 0);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(background, 0, 0);
        sb.draw(asteroids, cam.position.x - (asteroids.getWidth() / 2), cam.position.y + (cam.position.y / 6));

        font.setColor(Color.RED);
        font.getData().setScale(1);

        GlyphLayout playLayout = new GlyphLayout(font, "Touch to Play");
        font.draw(sb, playLayout, cam.position.x - (playLayout.width / 2), cam.position.y - (cam.position.y / 5));

        GlyphLayout highscoreLayout = new GlyphLayout(font, "Highscore: " + highscore);
        font.draw(sb, highscoreLayout, cam.position.x - (highscoreLayout.width / 2), cam.position.y);

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        asteroids.dispose();
        font.dispose();
    }
}
