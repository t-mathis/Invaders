 

/**
 * Curso B�sico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducci�n
 * 
 * http://www.planetalia.com
 * 
 */
 

public class Monster extends Actor {
    protected int vx;
    public int levels;
    protected static double fireingRate;
    protected static int enemies, myMonsterCount; // new int
    public Monster(Stage stage) {
        super(stage);
        setSpriteNames( new String[] {"bicho0.gif","bicho1.gif"});
        setFrameSpeed(35);
        enemies++;
        myMonsterCount = 0;
    }
    public Monster(Stage stage, int level) {
        super(stage);
        myMonsterCount = 0;
            if (level==1)
            {
                setSpriteNames( new String[] {"bicho0.gif","bicho1.gif"});
                levels = 1;
                fireingRate = .005;
            }
            else if(level ==2)
            {
                setSpriteNames( new String[] {"bicho0.gif","bicho1.gif"});
                //setSpriteNames( new String[] {"Computer0.gif","Computer1.gif"});
                levels = 2;
                fireingRate = .01;
            }
            else 
            {
                setSpriteNames( new String[] {"bicho0.gif","bicho1.gif"});
                //setSpriteNames( new String[] {"MOUSE0.gif","MOUSE1.gif"});
                levels = 3;
                fireingRate = .02;
            }
        setFrameSpeed(35);
        enemies++;
    }
    public void act() {
        super.act();
        x+=vx;
        if (x < 0 || x > Stage.WIDTH)
          vx = -vx;
        if (Math.random()<fireingRate)
          fire();
    }
    public void setNum(int x)
    {
        enemies = x;
    }
    public int getVx() { return vx; }
    public void setVx(int i) {vx = i;   }
    
    public void collision(Actor a) {
        if (a instanceof Bullet || a instanceof Bomb) {
          a.remove();
          remove();
          myMonsterCount--;
          stage.getSoundCache().playSound("explosion.wav");
          if(enemies < 70&& myMonsterCount<10)
          {
            spawn(levels);
            enemies++;
          }
          stage.getPlayer().addScore(20);
        }
    }
    
    public void spawn(int level) {
        Monster m = new Monster(stage,level);
        myMonsterCount++;
        m.setX( (int)(Math.random()*Stage.WIDTH) );
        m.setY( (int)(Math.random()*Stage.PLAY_HEIGHT/2) );
        m.setVx( (int)(Math.random()*20-10) );
        stage.addActor(m);
    }
    
    public void fire() {
        Laser m = new Laser(stage);
        m.setX(x+getWidth()/2);
        m.setY(y + getHeight());
        stage.addActor(m);
        stage.getSoundCache().playSound("photon.wav");

    }
}