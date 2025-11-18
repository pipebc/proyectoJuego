package puppy.code;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array; 
import com.badlogic.gdx.math.MathUtils;
import java.util.Iterator;


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;	
	
	private Texture txNave;
    private Texture txBala;
    private Texture txAsteroide;
    private Sound soundExplosion;
    private Sound soundHerido;
    private Sound soundDisparo;
    private Music gameMusic;
    
    private Texture txNaveEnemiga;
    private Texture txBalaEnemiga;
    private Sound soundDisparoEnemigo;
    
    private Nave4 nave;
    private Array<Ball2> asteroides;
    private Array<Bullet> balas;
    
    private Array<NaveEnemiga> navesEnemigas;
    private Array<BalaEnemiga> balasEnemigas;
    
    private int score;
    private int ronda;
    private int vidasIniciales;
    
    private float velXAsteroides;
    private float velYAsteroides;
    private int cantAsteroides;
    
    private float tiempoParaSpawnEnemigo;
    private final float TIEMPO_SPAWN_ENEMIGO = 5.0f;


	public PantallaJuego(SpaceNavigation game, int vidas, int ronda, float velX, float velY, int cant) {
		this.game = game;
        this.vidasIniciales = vidas;
        this.ronda = ronda;
        this.velXAsteroides = velX;
        this.velYAsteroides = velY;
        this.cantAsteroides = cant;
        this.score = 0;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        txNave = new Texture(Gdx.files.internal("MainShip3.png"));
        txBala = new Texture(Gdx.files.internal("Rocket2.png"));
        txAsteroide = new Texture(Gdx.files.internal("aGreyMedium4.png"));
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        soundHerido = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        soundDisparo = Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        
        txNaveEnemiga = new Texture(Gdx.files.internal("EnemyShip.png"));
        txBalaEnemiga = new Texture(Gdx.files.internal("EnemyRocket.png"));
        soundDisparoEnemigo = Gdx.audio.newSound(Gdx.files.internal("enemy-shot.mp3"));
        
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();
        
        balas = new Array<>();
        asteroides = new Array<>();
        
        navesEnemigas = new Array<>();
        balasEnemigas = new Array<>();
        tiempoParaSpawnEnemigo = TIEMPO_SPAWN_ENEMIGO;
        
        nave = new Nave4(txNave, soundHerido, txBala, soundDisparo, this);
        nave.setVidas(vidasIniciales);
        nave.setPosition(Gdx.graphics.getWidth() / 2 - nave.getWidth() / 2, Gdx.graphics.getHeight() / 2 - nave.getHeight() / 2);
        
        crearAsteroides(cantAsteroides);
	}
		private void crearAsteroides(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            float x = MathUtils.random(0, Gdx.graphics.getWidth());
            float y = MathUtils.random(0, Gdx.graphics.getHeight());
            float xSpeed = MathUtils.random(-velXAsteroides, velXAsteroides);
            float ySpeed = MathUtils.random(-velYAsteroides, velYAsteroides);
            
     
           
            Ball2 asteroide = new Ball2(txAsteroide, xSpeed, ySpeed);
            asteroide.setPosition(x, y);
            asteroides.add(asteroide);
        }
    }
		
	private void spawnNaveEnemiga() {
		float x = MathUtils.random(0, Gdx.graphics.getWidth() - txNaveEnemiga.getWidth());
	    float y = Gdx.graphics.getHeight();
	        
	    NaveEnemiga enemigo = new NaveEnemiga(txNaveEnemiga, txBalaEnemiga, soundDisparoEnemigo, this);
	    enemigo.setPosition(x, y);
	        
	    navesEnemigas.add(enemigo);
	}
	
	
	public void agregarBala(Bullet bala) {
	    balas.add(bala);
	}
	
	public void agregarBalaEnemiga(BalaEnemiga bala) {
        balasEnemigas.add(bala);
    }
	
	@Override
    public void render(float delta) {
        
		nave.update(delta);
        for (Bullet b : balas) {
            b.update(delta);
        }
        for (Ball2 a : asteroides) {
            a.update(delta);
        }
        
        for (NaveEnemiga ne : navesEnemigas) {
            ne.update(delta);
        }
        for (BalaEnemiga be : balasEnemigas) {
            be.update(delta);
        }
        
        for (Bullet b : balas) {
            for (Ball2 a : asteroides) {
                if (b.getBoundingBox().overlaps(a.getBoundingBox())) {
                    if (!b.isDestroyed() && !a.isDestroyed()) {
                        a.serGolpeado(1); 
                        b.destroy();      
                        soundExplosion.play();
                        score += 10;
                    }
                }
            }
        }
        
        for (Bullet b : balas) {
            for (NaveEnemiga ne : navesEnemigas) {
                if (b.getBoundingBox().overlaps(ne.getBoundingBox())) {
                    if (!b.isDestroyed() && !ne.estaDestruido()) {
                        ne.serGolpeado(1);
                        b.destroy();      
                        soundExplosion.play();
                        score += 50;
                    }
                }
            }
        }

        
        if (!nave.estaHerido()) {
             for (Ball2 a : asteroides) {
                 if (a.getBoundingBox().overlaps(nave.getBoundingBox())) {
                     if (!a.isDestroyed()) {
                         a.serGolpeado(1);
                         nave.serGolpeado(1); 
                     }
                 }
             }
             
             for (BalaEnemiga be : balasEnemigas) {
                 if (be.getBoundingBox().overlaps(nave.getBoundingBox())) {
                     if (!be.isDestroyed()) {
                         nave.serGolpeado(1);
                         be.destroy();
                     }
                 }
             }
             
             for (NaveEnemiga ne : navesEnemigas) {
                 if (ne.getBoundingBox().overlaps(nave.getBoundingBox())) {
                     if (!ne.estaDestruido()) {
                         nave.serGolpeado(1); 
                         ne.serGolpeado(1); 
                     }
                 }
             }
        }
       

        
        Iterator<Bullet> balaIter = balas.iterator();
        while (balaIter.hasNext()) {
            if (balaIter.next().isDestroyed()) {
                balaIter.remove();
            }
        }

        Iterator<Ball2> asteroideIter = asteroides.iterator();
        while (asteroideIter.hasNext()) {
            if (asteroideIter.next().isDestroyed()) {
                asteroideIter.remove();
            }
        }

        Iterator<NaveEnemiga> enemigoIter = navesEnemigas.iterator();
        while (enemigoIter.hasNext()) {
                    NaveEnemiga enemigo = enemigoIter.next(); 
                    if (enemigo.getY() < 0) {
                        if (ronda >= 3) {
                            if (score > game.getHighScore()) {
                                game.setHighScore(score);
                            }
                            gameMusic.stop();
                            game.setScreen(new PantallaGameOver(game));
                            dispose();
                            return;
                        }
                    }
        }
        
        Iterator<BalaEnemiga> balaEnemigaIter = balasEnemigas.iterator();
        while (balaEnemigaIter.hasNext()) {
            if (balaEnemigaIter.next().isDestroyed()) {
                balaEnemigaIter.remove();
            }
        }
        
        if (nave.estaDestruido()) {
            if (score > game.getHighScore()) {
                game.setHighScore(score);
            }
            gameMusic.stop();
            game.setScreen(new PantallaGameOver(game));
            dispose();
            return;
        }
        
        
        if (asteroides.size == 0) {
            ronda++;
            crearAsteroides(cantAsteroides + ronda);
            
            nave.activarInvencibilidad();
            
            if (ronda >= 3) {
                tiempoParaSpawnEnemigo = TIEMPO_SPAWN_ENEMIGO / 2;
            }
        }
        
        if (ronda >= 3) {
            tiempoParaSpawnEnemigo -= delta;
            if (tiempoParaSpawnEnemigo <= 0) {
                spawnNaveEnemiga();
                tiempoParaSpawnEnemigo = MathUtils.random(TIEMPO_SPAWN_ENEMIGO * 0.8f, TIEMPO_SPAWN_ENEMIGO * 1.2f);
            }
        }

        
        ScreenUtils.clear(0, 0, 0.2f, 1); 
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        
        
        for (Ball2 a : asteroides) {
            a.draw(game.getBatch());
        }
        for (Bullet b : balas) {
            b.draw(game.getBatch());
        }
        
        for (NaveEnemiga ne : navesEnemigas) {
            ne.draw(game.getBatch());
        }
        for (BalaEnemiga be : balasEnemigas) {
            be.draw(game.getBatch());
        }
        
        nave.draw(game.getBatch());

        
        dibujaEncabezado();
        
        game.getBatch().end();
    }
    
    
    private void dibujaEncabezado() {
        game.getFont().draw(game.getBatch(), "Vidas: " + nave.getVidas(), 20, Gdx.graphics.getHeight() - 20);
        game.getFont().draw(game.getBatch(), "Ronda: " + ronda, Gdx.graphics.getWidth() * 0.4f, Gdx.graphics.getHeight() - 20);
        game.getFont().draw(game.getBatch(), "Score: " + score, Gdx.graphics.getWidth() * 0.8f, Gdx.graphics.getHeight() - 20);
        game.getFont().draw(game.getBatch(), "HighScore: " + game.getHighScore(), Gdx.graphics.getWidth() * 0.75f, 40);
    }    
    @Override
    public void dispose() {
        txNave.dispose();
        txBala.dispose();
        txAsteroide.dispose();
        soundExplosion.dispose();
        soundHerido.dispose();
        soundDisparo.dispose();
        gameMusic.dispose();
        
        txNaveEnemiga.dispose();
        txBalaEnemiga.dispose();
        soundDisparoEnemigo.dispose();
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
}	
