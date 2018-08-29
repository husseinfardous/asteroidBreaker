package com.husseinfardous.asteroidbreaker.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.husseinfardous.asteroidbreaker.AsteroidBreaker;

public class GameOverState extends GameState {

    private Texture background;
    private int score, highscore;
    private BitmapFont font;
    private TextButton tryAgainButton, menuButton;
    private TextButton.TextButtonStyle buttonStyle;
    private TextureAtlas buttonAtlas;
    private Skin buttonSkin;

    public GameOverState(GameStateManager gsm, int score) {
        super(gsm);
        cam.setToOrtho(false, AsteroidBreaker.WIDTH / 2, AsteroidBreaker.HEIGHT / 2);
        cam.update();
        this.score = score;

        background = new Texture("background.png");

        font = new BitmapFont();

        Gdx.input.setInputProcessor(AsteroidBreaker.stage);
        buttonSkin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        buttonSkin.addRegions(buttonAtlas);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.RED;
        buttonStyle.up = buttonSkin.getDrawable("default-round");
        buttonStyle.down = buttonSkin.getDrawable("default-round-down");

        tryAgainButton = new TextButton("Try Again", buttonStyle);
        tryAgainButton.setSize(Gdx.graphics.getHeight() / 10, Gdx.graphics.getHeight() / 10);
        for (int i = 2; i < 11; i++) {
            tryAgainButton.getLabel().setFontScale(i);
            if (tryAgainButton.getLabel().getPrefWidth() > tryAgainButton.getWidth()) {
                tryAgainButton.getLabel().setFontScale(i - 1);
                break;
            }
        }
        tryAgainButton.setPosition((Gdx.graphics.getWidth() / 2) - (tryAgainButton.getWidth() / 2), (Gdx.graphics.getHeight() / 4));
        AsteroidBreaker.stage.addActor(tryAgainButton);

        menuButton = new TextButton("Menu", buttonStyle);
        menuButton.setSize(Gdx.graphics.getHeight() / 10, Gdx.graphics.getHeight() / 10);
        for (int i = 2; i < 11; i++) {
            menuButton.getLabel().setFontScale(i);
            if (menuButton.getLabel().getPrefWidth() > menuButton.getWidth()) {
                menuButton.getLabel().setFontScale(i - 1);
                break;
            }
        }
        menuButton.setPosition((Gdx.graphics.getWidth() / 2) - (menuButton.getWidth() / 2), (Gdx.graphics.getHeight() / 18));
        AsteroidBreaker.stage.addActor(menuButton);

        Preferences prefs = Gdx.app.getPreferences("savedAsteroidBreakerData");
        this.highscore = prefs.getInteger("highscore", 0);

        if (score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }
    }

    @Override
    public void handleInput() {
        if (tryAgainButton.isPressed()) {
            tryAgainButton.remove();
            menuButton.remove();
            gsm.set(new PlayState(gsm));
        }

        if (menuButton.isPressed()) {
            tryAgainButton.remove();
            menuButton.remove();
            gsm.set(new MenuState(gsm));
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

        font.setColor(Color.RED);
        font.getData().setScale(2);

        GlyphLayout gameOverLayout = new GlyphLayout(font, "GAME OVER");
        font.draw(sb, gameOverLayout, cam.position.x - (gameOverLayout.width / 2), cam.position.y + 150);

        font.getData().setScale(1);

        GlyphLayout highscoreLayout = new GlyphLayout(font, "Highscore: " + highscore);
        font.draw(sb, highscoreLayout, cam.position.x - (highscoreLayout.width / 2), cam.position.y + 75);

        GlyphLayout scoreLayout = new GlyphLayout(font, "Score: " + score);
        font.draw(sb, scoreLayout, cam.position.x - (scoreLayout.width / 2), cam.position.y);

        sb.end();
        AsteroidBreaker.stage.act();
        AsteroidBreaker.stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
        buttonAtlas.dispose();
        buttonSkin.dispose();
    }
}
