package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class BalaEnemiga extends EntidadJuego {

    public BalaEnemiga(Texture tx, float xSpeed, float ySpeed) {
        super(new Sprite(tx), xSpeed, ySpeed);
    }

    @Override
    public void update(float delta) {
        if (destroyed) return;

        
        float newX = getX() + (xSpeed * delta);
        float newY = getY() + (ySpeed * delta);
        setPosition(newX, newY);

        
        if (newY + getHeight() < 0 || newY > Gdx.graphics.getHeight()) {
            this.destroy(); 
        }
    }
}
