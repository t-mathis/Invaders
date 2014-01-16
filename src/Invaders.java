
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Invaders extends Canvas implements Stage, KeyListener {

    private BufferStrategy strategy;
    private long usedTime;
    public long timePlayed = System.currentTimeMillis() / 1000;
    public long timePlayed2;
    private SpriteCache spriteCache;
    private SoundCache soundCache;
    private ArrayList actors;
    private Player player;
    private BufferedImage background, backgroundTile;
    private int backgroundY;
    public int level, score, spawnSaucer, saucerCount, saucerCount2;
    private boolean gameLost = false, gameWin = false, bossFight = false, setLevel = false;

    public Invaders() {
        score = 0;
        spriteCache = new SpriteCache();
        soundCache = new SoundCache();
        level = 1;
        final JFrame ventana = new JFrame("Invaders");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = (JPanel) ventana.getContentPane();
        setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
        panel.setPreferredSize(new Dimension(Stage.WIDTH, Stage.HEIGHT));
        panel.setLayout(null);
        panel.add(this);
        ventana.setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
        ventana.setVisible(true);
        ventana.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                ventana.dispose();
                gameLost = true;
                System.exit(0);
            }
        });
        saucerCount = 0;
        saucerCount2 = 0;
        ventana.setResizable(false);
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        requestFocus();
        addKeyListener(this);
        spawnSaucer = 0;
        setIgnoreRepaint(true);

        BufferedImage cursor = spriteCache.createCompatible(10, 10, Transparency.BITMASK);
        Toolkit t = Toolkit.getDefaultToolkit();
        Cursor c = t.createCustomCursor(cursor, new Point(5, 5), "null");
        setCursor(c);
    }

    public void gameOver() {
        gameLost = true;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int i) {
        setLevel = true;
        level = i;
        actors.clear();

        if (i < 4 || i > 4) {
            newLevel();
            initWorld();
        } else if (i == 4) {

            initBossLevel();
        }
        setLevel = false;
    }

    public void initWorld() {
        bossFight = false;
        actors = new ArrayList();
        saucerCount = 0;
        for (int i = 0; i < 10; i++) {
            Monster m = new Monster(this, level);
            m.setX((int) (Math.random() * Stage.WIDTH));
            m.setY(i * 20);
            m.setVx((int) (Math.random() * 20 - 10));
            m.setNum(0);
            actors.add(m);
        }

        if (level < 2) {
            player = new Player(this, level, score);
        } else {
            score = getPlayer().getScore();
            player = new Player(this, level, score);
        }
        player.setX(Stage.WIDTH / 2);
        player.setY(Stage.PLAY_HEIGHT - 2 * player.getHeight());

        if (level < 4) {

            soundCache.loopSound("Fengtau.wav");
            backgroundTile = spriteCache.getSprite("oceano.gif");
        } else if (level > 3) {
            backgroundTile = spriteCache.getSprite("Tan Stucco.gif");
        }

        background = spriteCache.createCompatible(
                Stage.WIDTH,
                Stage.HEIGHT + backgroundTile.getHeight(),
                Transparency.OPAQUE);
        Graphics2D g = (Graphics2D) background.getGraphics();
        g.setPaint(new TexturePaint(backgroundTile,
                new Rectangle(0, 0, backgroundTile.getWidth(), backgroundTile.getHeight())));
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        backgroundY = backgroundTile.getHeight();

    }

    public void initBossLevel() {
        actors = new ArrayList();
        saucerCount2 = 0;
        bossFight = true;
        Boss m = new Boss(this, level, "CUBE.GIF", 100);
        m.setX((int) (Math.random() * Stage.WIDTH));
        m.setY(20);
        m.setVx(6);
        actors.add(m);
        player = new Player(this, level, score);
        player.setX(Stage.WIDTH / 2);
        player.setY(Stage.PLAY_HEIGHT - 2 * player.getHeight());


        if (level == 4) {
            //soundCache.loopSound("musica.wav");
            backgroundTile = spriteCache.getSprite("oceano.gif");
        }

        background = spriteCache.createCompatible(
                Stage.WIDTH,
                Stage.HEIGHT + backgroundTile.getHeight(),
                Transparency.OPAQUE);
        Graphics2D g = (Graphics2D) background.getGraphics();
        g.setPaint(new TexturePaint(backgroundTile,
                new Rectangle(0, 0, backgroundTile.getWidth(), backgroundTile.getHeight())));
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        backgroundY = backgroundTile.getHeight();

    }

    public void addActor(Actor a) {
        actors.add(a);
    }

    public Player getPlayer() {
        return player;
    }

    public void updateWorld() {
        int i = 0;
        while (i < actors.size()) {
            Actor m = (Actor) actors.get(i);
            if (m.isMarkedForRemoval()) {
                actors.remove(i);

            } else {
                m.act();
                i++;
            }
        }
        if (level > 2 && saucerCount2 < 2) {
            spawnSaucer = (int) (Math.random() * 250 + 1);
            if (spawnSaucer == 50 && level > 1) {
                Saucer m = new Saucer(this, level);
                actors.add(m);
                m.setVx((int) (Math.random() * 20 - 10));
                getPlayer().setVx(0);
                saucerCount2++;
            }
        } else if (level < 3 && saucerCount == 0) {
            spawnSaucer = (int) (Math.random() * 250 + 1);
            if (spawnSaucer == 50 && level > 1) {
                Saucer m = new Saucer(this, level);

                m.setVx((int) (Math.random() * 20 - 10));
                m.setY(15);
                actors.add(m);
                getPlayer().setVx(0);
                saucerCount++;
            }
        }
        player.act();


    }

    public void checkCollisions() {
        Rectangle playerBounds = player.getBounds();
        for (int i = 0; i < actors.size(); i++) {
            Actor a1 = (Actor) actors.get(i);
            Rectangle r1 = a1.getBounds();
            if (r1.intersects(playerBounds)) {
                player.collision(a1);
                a1.collision(player);
            }
            for (int j = i + 1; j < actors.size(); j++) {
                Actor a2 = (Actor) actors.get(j);
                Rectangle r2 = a2.getBounds();
                if (r1.intersects(r2)) {
                    a1.collision(a2);
                    a2.collision(a1);
                }
            }
        }
        if (actors.size() == 0 && level < 6 && !setLevel) {
            level++;
            if (level == 4) {
                bossLevel();

                initBossLevel();
            } else if (level == 6) {
                gameLost = false;
                gameWin = true;
            } else {
                newLevel();

                initWorld();
            }
        }
    }

    public void paintShields(Graphics2D g) {
        paintBar(g, 7, Player.MAX_SHIELDS, player.getShields(), 1, "S: ", 20);
    }

    public void paintHealth(Graphics2D g) {
        paintBar(g, 26, Player.HEALTH, player.getHealth(), 5, "H: ", 40);
    }

    public void paintArmor(Graphics2D g) {
        paintBar(g, 45, Player.ARMOR, player.getArmor(), 1, "A: ", 60);
    }
    
    public void paintBar(Graphics2D g, int stageOffset, int playerBarMax, int playerBarCur, int playerBarOffset, String barStr, int distance) {
        g.setPaint(Color.red);
        g.fillRect(250, Stage.PLAY_HEIGHT + stageOffset, playerBarMax * playerBarOffset / 2, 15);
        g.setPaint(Color.blue);
        g.fillRect(250 + ((playerBarMax - playerBarCur) * playerBarOffset) / 2, Stage.PLAY_HEIGHT + stageOffset, playerBarCur * playerBarOffset/ 2, 15);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setPaint(Color.green);
        g.drawString(barStr + playerBarCur, 150, Stage.PLAY_HEIGHT + distance);
    }

    public void paintScore(Graphics2D g) {
        paintText(g, "Score: ", String.valueOf(player.getScore()), 20);
    }

    public void paintLevel(Graphics2D g) {
        paintText(g, "Level: ", String.valueOf(level), 40);
    }
    
    public void paintText(Graphics2D g, String text, String value, int stageOffset) {
        int y = Stage.PLAY_HEIGHT + stageOffset;
        
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setPaint(Color.green);
        g.drawString(text, 20, y);
        g.setPaint(Color.red);
        g.drawString(value, 100, y);
    }

    public void paintAmmo(Graphics2D g) {
        int xBase = 280 + (Player.MAX_SHIELDS) / 2 + 10;
        for (int i = 0; i < player.getClusterBombs(); i++) {
            BufferedImage bomb = spriteCache.getSprite("bombUL.gif");
            g.drawImage(bomb, xBase + i * bomb.getWidth(), Stage.PLAY_HEIGHT, this);
        }
    }
    
    public void paintfps(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.white);
        if (usedTime > 0) {
            g.drawString(String.valueOf(1000 / usedTime) + " fps", Stage.WIDTH - 50, Stage.PLAY_HEIGHT);
        } else {
            g.drawString("--- fps", Stage.WIDTH - 50, Stage.PLAY_HEIGHT);
        }
    }

    public void paintTime(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.white);
        g.drawString("" + timePlayed2 + " ms", Stage.WIDTH - 100, Stage.PLAY_HEIGHT - 100);
    }

    public void paintStatus(Graphics2D g) {
        paintScore(g);
        paintShields(g);
        paintHealth(g);
        paintAmmo(g);
        paintArmor(g);
        paintfps(g);
        paintTime(g);
        paintLevel(g);
        if (bossFight == true) {
            paintBossHealth(g);
        }
    }

    public void paintBossHealth(Graphics2D g) {
        g.setPaint(Color.blue);
        g.fillRect(0, 573, Boss.health * 8, 15);
        g.setPaint(Color.red);
        g.fillRect(0, 573, Boss.deathCount * 8, 15);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setPaint(Color.green);
        g.drawString("Boss:", 20, Stage.PLAY_HEIGHT + 60);

    }

    public void paintWorld() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.drawImage(background,
                0, 0, Stage.WIDTH, Stage.HEIGHT,
                0, backgroundY, Stage.WIDTH, backgroundY + Stage.HEIGHT, this);
        for (int i = 0; i < actors.size(); i++) {
            Actor m = (Actor) actors.get(i);
            m.paint(g);
        }
        player.paint(g);


        paintStatus(g);
        strategy.show();
    }

    public void newLevel() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Level " + level, Stage.WIDTH / 2 - 50, Stage.HEIGHT / 2);
        strategy.show();

    }

    public void bossLevel() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("BOSS FIGHT" + level, Stage.WIDTH / 2 - 50, Stage.HEIGHT / 2);
        strategy.show();

    }

    public void paintGameOver() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("You Lose", Stage.WIDTH / 2 - 50, Stage.HEIGHT / 2);
        strategy.show();
    }

    public void paintGameWin() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("You Won", Stage.WIDTH / 2 - 50, Stage.HEIGHT / 2);
        strategy.show();
    }

    public SpriteCache getSpriteCache() {
        return spriteCache;
    }

    public SoundCache getSoundCache() {
        return soundCache;
    }

    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
                setLevel(1);
                break;
            case KeyEvent.VK_2:
                setLevel(2);
                break;
            case KeyEvent.VK_3:
                setLevel(3);
                break;
            case KeyEvent.VK_4:
                setLevel(4);
                break;
            case KeyEvent.VK_5:
                setLevel(5);
                break;
            default:
                player.keyReleased(e);
                break;
        }
    }

    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void game() {
        usedTime = 1000;
        initWorld();
        while (isVisible() && !gameLost && !gameWin) {
            long startTime = System.currentTimeMillis();
            backgroundY--;
            if (backgroundY < 0) {
                backgroundY = backgroundTile.getHeight();
            }
            updateWorld();
            checkCollisions();
            paintWorld();
            timePlayed2 += 1;
            usedTime = System.currentTimeMillis() - startTime;
            do {
                Thread.yield();
            } while (System.currentTimeMillis() - startTime < 17);

        }
        if (gameLost) {
            paintGameOver();
        } else if (gameWin) {
            paintGameWin();
        }
    }

    public static void main(String[] args) {
        Invaders inv = new Invaders();
        inv.game();
    }
}