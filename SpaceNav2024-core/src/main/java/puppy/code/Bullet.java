package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Bullet extends EntidadJuego {

    public Bullet(Texture tx, float xSpeed, float ySpeed) {
        
        super(new Sprite(tx), xSpeed, ySpeed);
    }

    
    @Override
    public void update(float delta) {
        if (destroyed) return;

        
        float newX = getX() + (xSpeed * delta);
        float newY = getY() + (ySpeed * delta);
        setPosition(newX, newY);

        
        if (newY > Gdx.graphics.getHeight() || newY < 0 || newX > Gdx.graphics.getWidth() || newX < 0) {
            this.destroy(); 
        }
    }
}
