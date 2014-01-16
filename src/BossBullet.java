 

/**
 * Curso B‡sico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducci—n
 * 
 * http://www.planetalia.com
 * 
 */
public class BossBullet extends Actor {
	protected static final int BULLET_SPEED=8;
	
	public BossBullet(Stage stage) {
		super(stage);
		setSpriteNames( new String[] {"bombGD.gif","bombD.gif"});
		setFrameSpeed(2);
	}
	
	public void act() {
		super.act();
		y+=BULLET_SPEED;
		if (y > Stage.PLAY_HEIGHT)
		  remove();
	}
}