package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;



public class Ball2 extends EntidadJuego implements Destructible{
	

    public Ball2(Texture tx, float xSpeed, float ySpeed) {
    	super(new Sprite(tx), xSpeed, ySpeed);
    }
    
    @Override
    public void update(float delta) {
    	if (destroyed) return;

        
        float newX = getX() + (xSpeed * delta);
        float newY = getY() + (ySpeed * delta);
        
        
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        if (newX + getWidth() < 0) newX = screenWidth;
        if (newX > screenWidth) newX = -getWidth();
        if (newY + getHeight() < 0) newY = screenHeight;
        if (newY > screenHeight) newY = -getHeight();

        setPosition(newX, newY); 
    }
    
    @Override
    public void serGolpeado(int dano) {
        this.destroy();
    }

    @Override
    public boolean estaDestruido() {
        return this.isDestroyed(); 
    }

    @Override
    public int getVidas() {
        return this.isDestroyed() ? 0 : 1; 
    }
    
}
