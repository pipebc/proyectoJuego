package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaGameOver implements Screen {

    
    private SpaceNavigation game;
    private OrthographicCamera camera;

    public PantallaGameOver(SpaceNavigation game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0, 0, 1);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), "Game Over !!!", Gdx.graphics.getWidth() * 0.4f, Gdx.graphics.getHeight() * 0.7f);
        game.getFont().draw(game.getBatch(), "Pincha en cualquier lado para reiniciar...", Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.3f);
        game.getBatch().end();

        
        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new PantallaJuego(game, 3, 1, 150, 150, 5));
            dispose();
        }
    }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
}