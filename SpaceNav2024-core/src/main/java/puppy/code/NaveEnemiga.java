package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;


public class NaveEnemiga extends EntidadJuego implements Destructible{
	private int vidas;
    private PantallaJuego juego;
    
    private Texture txBalaEnemiga;
    private Sound soundDisparoEnemigo;

    private float tiempoParaDisparar;
    private final float CADENCIA_DISPARO = 2.0f;

    public NaveEnemiga(Texture tx, Texture txBala, Sound soundDisparo, PantallaJuego juego) {
        super(new Sprite(tx), 0, -50); 
        
        this.juego = juego;
        this.txBalaEnemiga = txBala;
        this.soundDisparoEnemigo = soundDisparo;
        this.vidas = 3;
        this.tiempoParaDisparar = MathUtils.random(0.5f, 2.0f);
    }
    
    @Override
    public void update(float delta) {
        if (destroyed) return;

        
        float newY = getY() + (ySpeed * delta);
        setPosition(getX(), newY);

        
        if (getY() + getHeight() < 0) {
            this.destroy();
        }

        
        tiempoParaDisparar -= delta;
        if (tiempoParaDisparar <= 0) {
            disparar();
            tiempoParaDisparar = CADENCIA_DISPARO;
        }
    }
    
    private void disparar() {
        if (destroyed) return;

        
        float balaX = getX() + getWidth() / 2 - txBalaEnemiga.getWidth() / 2;
        float balaY = getY(); 
        
        BalaEnemiga bala = new BalaEnemiga(txBalaEnemiga, 0, -200); 
        bala.setPosition(balaX, balaY);
        
        juego.agregarBalaEnemiga(bala);
        soundDisparoEnemigo.play();
    }
    
    @Override
    public void serGolpeado(int dano) {
        this.vidas -= dano;
        if (vidas <= 0) {
            this.destroy();
        }
    }

    @Override
    public boolean estaDestruido() {
        return this.isDestroyed();
    }

    @Override
    public int getVidas() {
        return this.vidas;
    }
}    
