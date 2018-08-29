package com.husseinfardous.asteroidbreaker.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.husseinfardous.asteroidbreaker.AsteroidBreaker;

public class PauseState extends GameState {

    private PlayState savedPlayState;
    private Texture background;
    private int score;
    private BitmapFont font;
    private TextButton resumeButton, pauseButton;
    private TextButton.TextButtonStyle resumeButtonStyle;
    private TextureAtlas resumeButtonAtlas;
    private Skin resumeButtonSkin;

    public PauseState(GameStateManager gsm, PlayState savedPlayState, TextButton pauseButton, int score) {
        super(gsm);
        cam.setToOrtho(false, AsteroidBreaker.WIDTH / 2, AsteroidBreaker.HEIGHT / 2);
        cam.update();
        this.savedPlayState = savedPlayState;
        this.pauseButton = pauseButton;
        this.score = score;

        background = new Texture("background.png");

        font = new BitmapFont();

        Gdx.input.setInputProcessor(AsteroidBreaker.stage);
        resumeButtonSkin = new Skin();
        resumeButtonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        resumeButtonSkin.addRegions(resumeButtonAtlas);
        resumeButtonStyle = new TextButton.TextButtonStyle();
        resumeButtonStyle.font = font;
        resumeButtonStyle.fontColor = Color.RED;
        resumeButtonStyle.up = resumeButtonSkin.getDrawable("default-round");
        resumeButtonStyle.down = resumeButtonSkin.getDrawable("default-round-down");
        resumeButton = new TextButton("Resume", resumeButtonStyle);
        resumeButton.setSize(Gdx.graphics.getHeight() / 10, Gdx.graphics.getHeight() / 10);
        for (int i = 2; i < 11; i++) {
            resumeButton.getLabel().setFontScale(i);
            if (resumeButton.getLabel().getPrefWidth() > resumeButton.getWidth()) {
                resumeButton.getLabel().setFontScale(i - 1);
                break;
            }
        }
        resumeButton.setPosition((Gdx.graphics.getWidth() / 2) - (resumeButton.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (Gdx.graphics.getHeight() / 20));
        AsteroidBreaker.stage.addActor(resumeButton);
    }

    @Override
    public void handleInput() {
        if (resumeButton.isPressed()) {
            resumeButton.remove();
            AsteroidBreaker.stage.addActor(pauseButton);
            PlayState.isPaused = true;
            PlayState.returnToPlayState = true;
            gsm.set(savedPlayState);
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

        GlyphLayout pausedLayout = new GlyphLayout(font, "PAUSED");
        font.draw(sb, pausedLayout, cam.position.x - (pausedLayout.width / 2), cam.position.y + 150);

        font.getData().setScale(1);

        GlyphLayout scoreLayout = new GlyphLayout(font, "Score: " + score);
        font.draw(sb, scoreLayout, cam.position.x - (scoreLayout.width / 2), cam.position.y + 75);

        sb.end();
        AsteroidBreaker.stage.act();
        AsteroidBreaker.stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
        resumeButtonAtlas.dispose();
        resumeButtonSkin.dispose();
    }
}
