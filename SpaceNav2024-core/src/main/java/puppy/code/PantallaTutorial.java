package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaTutorial implements Screen {
    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    
    public PantallaTutorial(SpaceNavigation game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();

        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        font.draw(batch, "TUTORIAL: SPACE NAVIGATION", width * 0.25f, height * 0.9f);
        font.draw(batch, "Objetivo:", width * 0.1f, height * 0.8f);
        font.draw(batch, "- Sobrevive a las oleadas de asteroides.", width * 0.15f, height * 0.75f);
        font.draw(batch, "- Destruyelos para sumar puntos.", width * 0.15f, height * 0.7f);

        font.draw(batch, "Controles:", width * 0.1f, height * 0.6f);
        font.draw(batch, "- Moverse Arriba/Abajo: W / S", width * 0.15f, height * 0.55f);
        font.draw(batch, "- Moverse Izq./Der.: A / D", width * 0.15f, height * 0.5f);
        font.draw(batch, "- Disparar: Barra Espaciadora", width * 0.15f, height * 0.45f);

        font.draw(batch, "Toca la pantalla o presiona cualquier tecla para comenzar...", width * 0.1f, height * 0.15f);

        batch.end();


        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new PantallaMenu(game));
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