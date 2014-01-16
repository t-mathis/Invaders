 

/**
 * Curso B‡sico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducci—n
 * 
 * http://www.planetalia.com
 * 
 */
 

import java.awt.image.ImageObserver;

public interface Stage extends ImageObserver {
	public static final int WIDTH=800;
	public static final int HEIGHT=620;
	public static final int PLAY_HEIGHT = 500; 
	public static final int SPEED=100;
	public SpriteCache getSpriteCache();
	public SoundCache getSoundCache();
	public void addActor(Actor a);
	public Player getPlayer();
	public void gameOver();
}