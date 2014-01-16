 

/**
 * Curso B‡sico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducci—n
 * 
 * http://www.planetalia.com
 * 
 */
 

public class Boss extends Actor {
    protected int vx;
    public int levels;
    public static int deathCount;
    public static int health;
    protected static double fireingRate;
    public Boss(Stage stage, int level, String boss, int healthy) {
        super(stage);
        setSpriteNames( new String[] {boss,boss});
        setFrameSpeed(90);
        deathCount = 0;
        health = healthy;
    }
    public void act() {
        super.act();
        x+=vx;
        if (x < 0 || x > Stage.WIDTH)
          vx = -vx;
        if (Math.random()<.1)
          fire();
    }
    public int getVx() { return vx; }
    public void setVx(int i) {vx = i;   }
    
    public void collision(Actor a) {
        if (a instanceof Bullet) {
            a.remove();
          deathCount++;
          if(deathCount>=health)
          remove();
          stage.getSoundCache().playSound("explosion.wav");
          if(deathCount>=health)
          stage.getPlayer().addScore(100);
          stage.getPlayer().addScore(1);
        }
        else if (a instanceof Bomb) {
            a.remove();
          deathCount+=10;
          if(deathCount>=health)
          remove();
          stage.getSoundCache().playSound("explosion.wav");
          if(deathCount>=health)
          stage.getPlayer().addScore(100);
          stage.getPlayer().addScore(2);
        }
    }
    public void fire() {
            BossBullet m = new BossBullet(stage);
            m.setX(x+getWidth()/2-15);
            m.setY(y + getHeight());
            stage.addActor(m);
            stage.getSoundCache().playSound("photon.wav");

    }
}