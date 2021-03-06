package fr.jerome.mario.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.jerome.mario.Assets;
import fr.jerome.mario.MarioGame;

/**
 * SplashScreen 3 sec, permet de charger les éléments de l'assetManager
 * Created by jerome on 01/11/14.
 */
public class IntroScreen implements Screen {

    private MarioGame game;
    private Texture intro;
    private SpriteBatch batch;
    private float time = 0;

    public IntroScreen(MarioGame marioGame) {

        game = marioGame;
    }

    @Override
    public void show() {

        Assets.load();
//        Assets.manager.finishLoading();

        intro = new Texture(Gdx.files.internal("images/splash.jpg"));
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 432, 600);
    }

    @Override
    public void render(float delta) {

        batch.begin();
        batch.draw(intro, 0, 0);
        batch.end();

        time += delta;

        if (time > 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
                game.setScreen(new MenuScreen(game));
            }
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {

        batch.dispose();
        intro.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
