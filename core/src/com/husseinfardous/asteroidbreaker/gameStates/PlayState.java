package com.husseinfardous.asteroidbreaker.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.husseinfardous.asteroidbreaker.AsteroidBreaker;
import com.husseinfardous.asteroidbreaker.gameSprites.Asteroid;
import com.husseinfardous.asteroidbreaker.gameSprites.Bomb;

public class PlayState extends GameState {

    public static boolean isPaused = false;
    public static boolean returnToPlayState = false;
    private boolean gameOver = false;
    private Array<Asteroid> asteroids;
    private Array<Bomb> bombs;
    private Texture background;
    private Sound shot, explosion;
    private int score, lives;
    private long startTime, returnStartTime, secondsToIncreaseAsteroids, secondsToIncreaseBombs, secondsToIncreaseAsteroidGravity;
    private BitmapFont font;
    private TextButton pauseButton;
    private TextButton.TextButtonStyle pauseButtonStyle;
    private TextureAtlas pauseButtonAtlas;
    private Skin pauseButtonSkin;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, AsteroidBreaker.WIDTH / 2, AsteroidBreaker.HEIGHT / 2);
        cam.update();

        background = new Texture("background.png");

        score = 0;
        lives = 5;

        startTime = System.currentTimeMillis();
        secondsToIncreaseAsteroids = 10;
        secondsToIncreaseBombs = 50;
        secondsToIncreaseAsteroidGravity = 100;

        font = new BitmapFont();

        Gdx.input.setInputProcessor(AsteroidBreaker.stage);
        pauseButtonSkin = new Skin();
        pauseButtonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        pauseButtonSkin.addRegions(pauseButtonAtlas);
        pauseButtonStyle = new TextButton.TextButtonStyle();
        pauseButtonStyle.font = font;
        pauseButtonStyle.fontColor = Color.RED;
        pauseButtonStyle.up = pauseButtonSkin.getDrawable("default-round");
        pauseButtonStyle.down = pauseButtonSkin.getDrawable("default-round-down");
        pauseButton = new TextButton("Pause", pauseButtonStyle);
        pauseButton.setSize(Gdx.graphics.getHeight() / 10, Gdx.graphics.getHeight() / 10);
        for (int i = 2; i < 11; i++) {
            pauseButton.getLabel().setFontScale(i);
            if (pauseButton.getLabel().getPrefWidth() > pauseButton.getWidth()) {
                pauseButton.getLabel().setFontScale(i - 1);
                break;
            }
        }
        pauseButton.setPosition(0, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 10));
        AsteroidBreaker.stage.addActor(pauseButton);

        asteroids = new Array<Asteroid>();
        for (int i = 0; i < 3; i++) {
            asteroids.add(new Asteroid());
        }

        bombs = new Array<Bomb>();
        bombs.add(new Bomb());

        shot = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
    }

    @Override
    public void handleInput() {
        if (pauseButton.isPressed()) {
            pauseButton.remove();
            gsm.push(new PauseState(gsm, this, pauseButton, score));
        }

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);

            for (Asteroid asteroid: asteroids) {
                if (asteroid.getBounds().contains(touchPos.x, touchPos.y)) {
                    shot.play(0.5f);
                    score++;
                    asteroid.reposition();
                }
            }

            for (Bomb bomb: bombs) {
                if (bomb.getBounds().contains(touchPos.x, touchPos.y)) {
                    explosion.play(0.5f);
                    gameOver = true;
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        if (!isPaused) {
            handleInput();

            if (lives <= 0) {
                gameOver = true;
            }

            if (gameOver) {
                pauseButton.remove();
                gsm.set(new GameOverState(gsm, score));
            }

            for (Asteroid asteroid : asteroids) {
                asteroid.update(dt);
                if (asteroid.getPosition().y == 0) {
                    lives--;
                    asteroid.reposition();
                }
            }

            for (Bomb bomb : bombs) {
                bomb.update(dt);
                if (bomb.getPosition().y == 0) {
                    bomb.reposition();
                }
            }

            long timeElapsed = (System.currentTimeMillis() - startTime) / 1000;

            if (timeElapsed == secondsToIncreaseAsteroids) {
                asteroids.add(new Asteroid());
                secondsToIncreaseAsteroids *= 2;
            }

            if (timeElapsed == secondsToIncreaseBombs) {
                bombs.add(new Bomb());
                secondsToIncreaseBombs *= 2;
            }

            if ((timeElapsed == secondsToIncreaseAsteroidGravity) && (Asteroid.gravity != -5)) {
                Asteroid.gravity += -1;
                secondsToIncreaseAsteroidGravity *= 2;
            }
        }

        if (returnToPlayState) {
            returnStartTime = System.currentTimeMillis();
            returnToPlayState = false;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(background, 0, 0);

        for (Asteroid asteroid: asteroids) {
            sb.draw(asteroid.getTexture(), asteroid.getPosition().x, asteroid.getPosition().y, 40, 30);
        }

        for (Bomb bomb: bombs) {
            sb.draw(bomb.getTexture(), bomb.getPosition().x, bomb.getPosition().y, 20, 40);
        }

        font.setColor(Color.WHITE);
        font.getData().setScale(1);

        GlyphLayout scoreLayout = new GlyphLayout(font, Integer.toString(score));
        font.draw(sb, scoreLayout, cam.position.x - (scoreLayout.width / 2), cam.position.y + 150);

        font.setColor(Color.RED);

        GlyphLayout livesLayout = new GlyphLayout(font, "Lives: " + lives);
        font.draw(sb, livesLayout, 240 - (livesLayout.width), 400 - (livesLayout.height));

        if (isPaused) {
            long timeElapsed = (System.currentTimeMillis() - returnStartTime) / 1000;
            if (timeElapsed == 3) {
                isPaused = false;
            }

            font.getData().setScale(2);
            GlyphLayout resumeLayout = new GlyphLayout(font, "Resuming in: " + (3 - timeElapsed));
            font.draw(sb, resumeLayout, cam.position.x - (resumeLayout.width / 2), cam.position.y);
        }

        sb.end();
        AsteroidBreaker.stage.act();
        AsteroidBreaker.stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
        pauseButtonAtlas.dispose();
        pauseButtonSkin.dispose();
        shot.dispose();
        explosion.dispose();

        for (Asteroid asteroid: asteroids) {
            asteroid.dispose();
        }

        for (Bomb bomb: bombs) {
            bomb.dispose();
        }
    }
}
