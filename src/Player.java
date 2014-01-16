 

/**
 * Curso B�sico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducci�n
 * 
 * http://www.planetalia.com
 * 
 */
 

import java.awt.event.KeyEvent;

public class Player extends Actor {
    public static final int MAX_SHIELDS = 500;
    public static final int HEALTH = 100;
    public static final int ARMOR = 1000;
    public static int bombs;
    protected static final int PLAYER_SPEED = 5;
    protected int vx;
    protected int vy;
    private boolean up,down,left,right;
    private int clusterBombs; 
    private int score;
    private int shields,health,armor;
    private int oneBombFireCount;
    public int myLevel;
        
    public Player(Stage stage, int level,int myScore) {
        super(stage);
        setSpriteNames( new String[] {"voySHEILDS.png"});
        if(level == 1)
            clusterBombs = 2;
        else if(level ==2)
            clusterBombs = 4;
        else if(level ==3)
            clusterBombs = 6;
        else
            clusterBombs = 5;
        shields = MAX_SHIELDS;
        health = HEALTH;
        if(level>3)
         armor = ARMOR;
        else
         armor = 0;   
        score = myScore;
        oneBombFireCount=0;
        myLevel = level;
    }
    public void act() {
        super.act();
        x+=vx;
        y+=vy;
        if (x < 0 ) 
          x = 0;
        if (x > Stage.WIDTH - getWidth())
          x = Stage.WIDTH - getWidth();
        if (y < 0 )
          y = 0;
        if ( y > Stage.PLAY_HEIGHT-getHeight())
          y = Stage.PLAY_HEIGHT - getHeight();
        if(shields <= 0)
            setSpriteNames( new String[] {"voyNO.png"});
    }

    public int getVx() { return vx; }
    public void setVx(int i) {vx = i;   }
    public int getVy() { return vy; }
  public void setVy(int i) {vy = i; }
  
  
  protected void updateSpeed() {
    vx=0;vy=0;
    if (down) vy = PLAYER_SPEED;
    if (up) vy = -PLAYER_SPEED;
    if (left) vx = -PLAYER_SPEED;
    if (right) vx = PLAYER_SPEED;
  }
  
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
        case KeyEvent.VK_DOWN : down = false;break;
          case KeyEvent.VK_UP : up = false; break;
          case KeyEvent.VK_LEFT : left = false; break; 
          case KeyEvent.VK_RIGHT : right = false;break;
    }
    updateSpeed();
  }
  
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
          case KeyEvent.VK_UP : up = true; break;
          case KeyEvent.VK_LEFT : left = true; break;
          case KeyEvent.VK_RIGHT : right = true; break;
          case KeyEvent.VK_DOWN : down = true;break;
          case KeyEvent.VK_V : fireTwo(); break;
          case KeyEvent.VK_SPACE : fire(); break;
          case KeyEvent.VK_B : fireCluster(); break;
          case KeyEvent.VK_N : fireOneBomb(); break;
    }
    updateSpeed();
  }
  
  public void fire() {
    Bullet b = new Bullet(stage);
    b.setX(x+19);
    b.setY(y - b.getHeight());
    stage.addActor(b);
    stage.getSoundCache().playSound("missile.wav");
  }
  public void fireTwo() {
    Bullet b = new Bullet(stage);
    Bullet c = new Bullet(stage);
    b.setX(x+8);
    b.setY(y - b.getHeight());
    c.setX(x+30);
    c.setY(y - b.getHeight());
    stage.addActor(b);
    stage.addActor(c);
    stage.getSoundCache().playSound("missile.wav");
  }
  public void fireCluster() {
    if (clusterBombs == 0)
      return;

    clusterBombs--;
    stage.addActor( new Bomb(stage, Bomb.UP_LEFT, x,y));
      stage.addActor( new Bomb(stage, Bomb.UP,x,y));
      stage.addActor( new Bomb(stage, Bomb.UP_RIGHT,x,y));
      stage.addActor( new Bomb(stage, Bomb.LEFT,x,y));
      stage.addActor( new Bomb(stage, Bomb.RIGHT,x,y));

  }
  public void fireOneBomb() {
    if (clusterBombs == 0)
      return;
     oneBombFireCount ++;
     if(oneBombFireCount ==5)
     {
    clusterBombs--;
    oneBombFireCount =0;
}
    stage.addActor( new Bomb(stage, Bomb.UP, x,y));
  }
  /*
   * Scoring Methods
   */
  public int getScore() {       return score;   }
  public void setScore(int i) { score = i;  }
  public void addScore(int i) { score += i;  }
  /*
   * Health Methods
   */
  public int getHealth() {   return health; }
  public void addHealth(int i) {
        health += i;
        if (health > HEALTH) shields = HEALTH;
    }
  /*
   * Shield Methods
   */
    public int getShields() {   return shields; }
    public void setShields(int i) { shields = i;    }
    public void addShields(int i) {
        shields += i;
        if (shields > MAX_SHIELDS) shields = MAX_SHIELDS;
    }
    /*
     * Armor Methods
     */
    public int getArmor() {   return armor; }
    public void setArmor(int i) { armor = i;    }
    public void addArmor(int i) {
        armor += i;
        if (armor > ARMOR) armor = ARMOR;
    }
    
    public void collision(Actor a) {
        //Monster Collision
        if (a instanceof Monster ) {
          a.remove();
          if(shields >0)
          {
              addShields((shields*-1)-1);
              if(score>39)
                score -=40;
          }
          else
          {
              if(armor>0)
              {
                addArmor(-50);
                if(score>39)
                score -=40;
              }  
              else
              {
                addHealth(-50);
                if(score>39)
                  score -=40;
              }
          }
        }
        // Laser Collision
        if (a instanceof Laser ) {
            a.remove();
            if(shields >0)
            {
                addShields(-20);
                if(score>9 )
                    score -=10;
            }
            else
          {
              if(armor>0)
              {
                addArmor(-10);
                if(score>39)
                score -=40;
              }  
              else
              {
                addHealth(-20);
                if(score>39)
                  score -=40;
              }
          }
        }
        // Boss Shot Collision
        if (a instanceof BossBullet ) {
            a.remove();
            if(shields > 0)
            {
                addShields(-40);
                if(score>29)
                    score -=30;
            }
            else
          {
              if(armor>0)
              {
                addArmor(-30);
                if(score>39)
                score -=40;
              }  
              else
              {
                addHealth(-50);
                if(score>39)
                  score -=40;
              }
          }
        }
        // UFO Shot Collision
        if (a instanceof SuperBullet ) {
            a.remove();
            if(armor >0)
            {
                addArmor(-100);
            }
            else
            {
                if(shields > 0)
                {
                    addShields(-250);
                }
                else
                {
                    addHealth((health*-1)-1);
                }
            }
            if(score>=0)
                score -= 50;
        }
        
        if (getHealth() <= 0)
          stage.gameOver();
    }   

    public int getClusterBombs() {  return clusterBombs;    }

}