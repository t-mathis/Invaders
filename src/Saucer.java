 

/**
 * Curso B‡sico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducci—n
 * 
 * http://www.planetalia.com
 * 
 */
 

public class Saucer extends Actor {
    protected int vx;
    public int levels;
    public int deathCount;
    protected static double fireingRate;
    public Saucer(Stage stage, int level) {
        super(stage);
        setSpriteNames( new String[] {"SAUCER0.GIF","SAUCER0.GIF"});
        setFrameSpeed(90);
        deathCount = 0;
    }
    public void act() {
        super.act();
        x+=vx;
        if (x < 0 || x > Stage.WIDTH)
          vx = -vx;
        if(x>(stage.getPlayer().getX()-20) && x < stage.getPlayer().getX()+52 )
        {
            fire();
        }
    }
    public int getVx() { return vx; }
    public void setVx(int i) {vx = i;   }
    
    public void collision(Actor a) {
        if (a instanceof Bullet) {
            a.remove();
          deathCount++;
          if(deathCount==10)
          remove();
          stage.getSoundCache().playSound("explosion.wav");
          if(deathCount==10)
          stage.getPlayer().addScore(100);
        }
        if (a instanceof Bomb) {
            a.remove();
          deathCount+=10;
          if(deathCount==10)
          remove();
          stage.getSoundCache().playSound("explosion.wav");
          if(deathCount==10)
          stage.getPlayer().addScore(100);
        }
    }
    public void fire() {
            SuperBullet m = new SuperBullet(stage);
            m.setX(x+getWidth()/2-15);
            m.setY(y + getHeight());
            stage.addActor(m);
            stage.getSoundCache().playSound("photon.wav");

    }
}