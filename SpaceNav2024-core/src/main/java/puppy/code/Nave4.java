package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;




public class Nave4 extends EntidadJuego implements Destructible {
	
	private int vidas;
    private boolean herido;
    private float tiempoHerido;
    private final float tiempoHeridoMax = 2.0f;
    private boolean visibleParpadeo = true;
    private float tiempoParpadeo = 0f;
    
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    
    private PantallaJuego juego;
    
    public Nave4(Texture tx, Sound sonidoHerido, Texture txBala, Sound soundBala, PantallaJuego juego) {
    	super(new Sprite(tx), 0, 0); 
        
        this.sonidoHerido = sonidoHerido;
        this.txBala = txBala;
        this.soundBala = soundBala;
        this.juego = juego;
        this.herido = false;
        this.tiempoHerido = 0f;

    }
    
    
    @Override
    public void update(float delta) {
        if (destroyed) return;

        
        float velocidad = 300.0f; 
        xSpeed = 0;
        ySpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            ySpeed = velocidad;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            ySpeed = -velocidad;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            xSpeed = -velocidad;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            xSpeed = velocidad;
        }

        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            disparar();
        }

        
        float newX = getX() + (xSpeed * delta);
        float newY = getY() + (ySpeed * delta);
        
        
        if (newX < 0) newX = 0;
        if (newX + getWidth() > Gdx.graphics.getWidth()) newX = Gdx.graphics.getWidth() - getWidth();
        if (newY < 0) newY = 0;
        if (newY + getHeight() > Gdx.graphics.getHeight()) newY = Gdx.graphics.getHeight() - getHeight();
        
        setPosition(newX, newY); 

        
        if (herido) {
            tiempoHerido -= delta;
            tiempoParpadeo -= delta;
            
            if (tiempoParpadeo <= 0) {
                visibleParpadeo = !visibleParpadeo;
                tiempoParpadeo = 0.1f;
            }
            
            if (tiempoHerido <= 0) {
                herido = false;
                visibleParpadeo = true; 
            }
        }
    }
      
    private void disparar() {
        
        float balaX = getX() + getWidth() / 2 - txBala.getWidth() / 2;
        float balaY = getY() + getHeight(); 
        
        Bullet bala = new Bullet(txBala, 0, 400); 
        bala.setPosition(balaX, balaY);
        
        juego.agregarBala(bala); 
        soundBala.play();
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        if (visibleParpadeo) {
            super.draw(batch); 
        }
    }
    
    
    @Override
    public void serGolpeado(int dano) {
        if (herido) return; 

        this.vidas -= dano;
        this.herido = true;
        this.tiempoHerido = tiempoHeridoMax;
        sonidoHerido.play();
        
        if (vidas <= 0) {
            this.destroy(); 
        }
    }
    
    public void activarInvencibilidad() {

        this.herido = true;
        this.tiempoHerido = this.tiempoHeridoMax; 

        this.visibleParpadeo = true;
        this.tiempoParpadeo = 0f;

    }
    
    @Override
    public boolean estaDestruido() {
        return this.destroyed; 
    }

    @Override
    public int getVidas() {
        return this.vidas;
    }
    
    
    public void setVidas(int vidas) {
        this.vidas = vidas;
    }
    
    public boolean estaHerido() {
        return herido;
    }
}
