package puppy.code;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public abstract class EntidadJuego {

    protected Sprite spr;
    protected float xSpeed, ySpeed;
    protected boolean destroyed = false;

    
    public EntidadJuego(Sprite spr, float xSpeed, float ySpeed) {
        this.spr = spr;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    
    public abstract void update(float delta);

    
    public void draw(SpriteBatch batch) {
        if (!destroyed) {
            spr.draw(batch);
        }
    }

   
    public Rectangle getBoundingBox() {
        return spr.getBoundingRectangle();
    }


    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        this.destroyed = true;
    }

    public float getX() {
        return spr.getX();
    }

    public float getY() {
        return spr.getY();
    }
    
    public float getWidth() {
        return spr.getWidth();
    }

    public float getHeight() {
        return spr.getHeight();
    }

    public void setPosition(float x, float y) {
        spr.setPosition(x, y);
    }
}
