package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;




public class Nave4 extends EntidadJuego implements Destructible {
	
	public static final int ModoDisparoNormal = 0;
    public static final int ModoDisparoRapido = 1;
    public static final int ModoDisparoRafaga = 2;
    private int modoDisparo = ModoDisparoNormal;
    
    private float cooldownNormal = 0.5f;
    private float cooldownRafaga = 0.15f;
    private float tiempoDesdeUltimoDisparo = 0f;
    private int rafagaRestante = 0;
	
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
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            modoDisparo = (modoDisparo + 1) % 3;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            modoDisparo = ModoDisparoNormal;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            modoDisparo = ModoDisparoRapido;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            modoDisparo = ModoDisparoRafaga;
        }

        tiempoDesdeUltimoDisparo -= delta;
        switch (modoDisparo) {
	        case ModoDisparoNormal:
	            manejarDisparoNormal(delta);
	            break;
	        case ModoDisparoRapido:
	            manejarDisparoRapido(delta);
	            break;
	        case ModoDisparoRafaga:
	            manejarDisparoRafaga(delta);
	            break;
	        default:
	            manejarDisparoNormal(delta);
	            break;
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
    
    public void setModoDisparo(int modo) {
        if (modo >= ModoDisparoNormal && modo <= ModoDisparoRafaga) {
            this.modoDisparo = modo;
        }
    }

    public int getModoDisparo() {
        return this.modoDisparo;
    }
    
    private void manejarDisparoNormal(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && tiempoDesdeUltimoDisparo <= 0f) {
            disparar();
            tiempoDesdeUltimoDisparo = cooldownNormal;
        }
    }

    private void manejarDisparoRapido(float delta) {
        float cooldown = cooldownNormal * 0.4f;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && tiempoDesdeUltimoDisparo <= 0f) {
            disparar();
            tiempoDesdeUltimoDisparo = cooldown;
        }
    }

    private void manejarDisparoRafaga(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && rafagaRestante == 0) {
            rafagaRestante = 3;
            tiempoDesdeUltimoDisparo = 0f;
        }
        if (rafagaRestante > 0 && tiempoDesdeUltimoDisparo <= 0f) {
            disparar();
            rafagaRestante--;
            tiempoDesdeUltimoDisparo = cooldownRafaga;
        }
    }
}


