package com.husseinfardous.asteroidbreaker.gameSprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import java.util.Random;

public class Asteroid {

    public static int gravity = -2;
    private static final int ASTEROID_WIDTH = 40;
    private static final int ASTEROID_HEIGHT = 30;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Texture asteroid;
    private Random random;

    public Asteroid() {
        asteroid = new Texture("asteroid.png");
        random = new Random();
        position = new Vector3(random.nextInt(241 - ASTEROID_WIDTH), random.nextInt(201 - ASTEROID_HEIGHT) + 200, 0);
        velocity = new Vector3(0, 0, 0);
        bounds = new Rectangle(position.x, position.y, ASTEROID_WIDTH + 30, ASTEROID_HEIGHT + 30);
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
        position.set(random.nextInt(241 - ASTEROID_WIDTH), random.nextInt(201 - ASTEROID_HEIGHT) + 200, 0);
        bounds.setPosition(position.x, position.y);
    }

    public Texture getTexture() {
        return asteroid;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        asteroid.dispose();
    }
}
