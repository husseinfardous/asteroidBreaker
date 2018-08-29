package com.husseinfardous.asteroidbreaker.gameSprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import java.util.Random;

public class Bomb {

    public static int gravity = -2;
    private static final int BOMB_WIDTH = 20;
    private static final int BOMB_HEIGHT = 40;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Texture bomb;
    private Random random;

    public Bomb() {
        bomb = new Texture("bomb.png");
        random = new Random();
        position = new Vector3(random.nextInt(241 - BOMB_WIDTH), random.nextInt(201 - BOMB_HEIGHT) + 200, 0);
        velocity = new Vector3(0, 0, 0);
        bounds = new Rectangle(position.x, position.y, BOMB_WIDTH, BOMB_HEIGHT);
    }

    public void update(float dt) {
        if (position.y > 0) {
            velocity.add(0, gravity, 0);
        }

        velocity.scl(dt);
        position.add(0, velocity.y, 0);

        if (position.y < 0) {
            position.y = 0;
        }

        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public void reposition() {
        velocity.set(0, 0, 0);
        position.set(random.nextInt(241 - BOMB_WIDTH), random.nextInt(201 - BOMB_HEIGHT) + 200, 0);
        bounds.setPosition(position.x, position.y);
    }

    public Texture getTexture() {
        return bomb;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        bomb.dispose();
    }
}
