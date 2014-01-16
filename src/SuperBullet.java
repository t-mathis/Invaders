 

/**
 * Curso B‡sico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducci—n
 * 
 * http://www.planetalia.com
 * 
 */
 


public class SuperBullet extends Actor {
	protected static final int SuperBullet_SPEED=15;
	
	public SuperBullet(Stage stage) {
		super(stage);
		setSpriteNames( new String[] {"DISPARO.GIF"});
	}
	
	public void act() {
		super.act();
		y+=SuperBullet_SPEED;
		if (y > Stage.PLAY_HEIGHT)
		  remove();
	}
}