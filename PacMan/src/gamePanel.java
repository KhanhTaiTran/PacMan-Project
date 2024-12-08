import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class gamePanel extends JPanel implements ActionListener, KeyListener {

    // instance of gamePanel
    private static gamePanel instance;

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private final int boardWidth = columnCount * tileSize;

    private Image wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, scaredGhostImage;
    private Image powerFoodImage;
    private Image pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;

    private Map map;
    private final Timer gameLoop;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private boolean canEatGhost = false;

    private SoundEffect soundEffect;
    private Random random = new Random();

    public gamePanel() {
        int boardHeight = rowCount * tileSize;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        loadImages();
        map = new Map(wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage,
                powerFoodImage, scaredGhostImage);
        initializeGhosts();

        gameLoop = new Timer(50, this); // 20fps (1000/50)
        // gameLoop.start();

        // initial sound effect
        soundEffect = new SoundEffect();

        // play the start sound
        playStartSound();

        // play background sound
        playBackgroundSound();
    }

    // singleton pattern
    public static synchronized gamePanel getInstance() {
        if (instance == null) {
            instance = new gamePanel();
        }
        return instance;
    }

    // load images using ImageFactory
    private void loadImages() {
        wallImage = ImageFactory.loadImage("resource/wall.png");
        blueGhostImage = ImageFactory.loadImage("resource/blueGhost.png");
        orangeGhostImage = ImageFactory.loadImage("resource/orangeGhost.png");
        pinkGhostImage = ImageFactory.loadImage("resource/pinkGhost.png");
        redGhostImage = ImageFactory.loadImage("resource/redGhost.png");
        scaredGhostImage = ImageFactory.loadImage("resource/scaredGhost.png");

        powerFoodImage = ImageFactory.loadImage("resource/powerFood.png");

        pacmanUpImage = ImageFactory.loadImage("resource/pacmanUp.png");
        pacmanDownImage = ImageFactory.loadImage("resource/pacmanDown.png");
        pacmanLeftImage = ImageFactory.loadImage("resource/pacmanLeft.png");
        pacmanRightImage = ImageFactory.loadImage("resource/pacmanRight.png");
    }

    private void initializeGhosts() {
        for (Ghost ghost : map.ghosts) {
            char newDirection = ghost.directions[random.nextInt(4)];
            ghost.updateDirection(newDirection, tileSize, map.walls);
        }
    }

    // paint the game
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(map.pacman.image, map.pacman.x, map.pacman.y, map.pacman.width, map.pacman.height, null);

        for (Ghost ghost : map.ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Entity wall : map.walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Entity food : map.foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        for (Entity powerFood : map.powerFoods) {
            g.drawImage(powerFood.image, powerFood.x, powerFood.y, powerFood.width, powerFood.height, null);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize / 2, tileSize / 2);
            g.drawString("Press any key to restart", tileSize / 2, tileSize / 2 + 30);
        } else {
            g.drawString(String.format("x%d Score: %d", lives, score), tileSize / 2, tileSize / 2);
        }
    }

    private void run() {
        map.pacman.move(tileSize, map.walls);
        moveGhosts();
        checkCollisions();
        checkFoodCollision();
        checkPowerFoodCollision();
        checkTeleport();

        if (map.foods.isEmpty()) {
            gameLoop.stop();
            JOptionPane.showMessageDialog(this, "You win!");
            resetPositions();
            map.loadMap();
            gameLoop.start();
        }
    }

    private void checkCollisions() {
        for (Entity wall : map.walls) {
            if (Checker.collision(map.pacman, wall)) {
                map.pacman.x -= map.pacman.velocityX;
                map.pacman.y -= map.pacman.velocityY;
                break;
            }
        }

        for (Ghost ghost : map.ghosts) {
            if (Checker.collision(ghost, map.pacman)) {
                if (canEatGhost) {
                    ghost.reset();
                    score += 200;
                    soundEffect.playSound("gs_eatghost.wav");
                    soundEffect.playSound("gs_returnghost.wav");
                } else {
                    lives--;
                    soundEffect.playSound("gs_pacmandies.wav");
                    if (lives == 0) {
                        gameOver = true;
                    } else {
                        resetPositions();
                    }
                }
            }
        }
    }

    private long lastTeleportTime = 0;
    private final long teleportDelay = 200; // 0.2s

    private void checkTeleport() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTeleportTime < teleportDelay) {
            return;
        }

        for (TeleportGate teleportGate : map.teleportGates) {
            if (Checker.collision(map.pacman, teleportGate)) {
                for (TeleportGate otherGate : map.teleportGates) {
                    if (teleportGate != otherGate) {
                        map.pacman.x = otherGate.x;
                        map.pacman.y = otherGate.y;
                        break;
                    }
                }
                lastTeleportTime = currentTime;
                break;
            }
        }
    }

    // move ghosts
    private void moveGhosts() {
        for (Ghost ghost : map.ghosts) {
            ghost.move(tileSize, map.walls);
        }
    }

    private void checkFoodCollision() {
        for (Entity food : map.foods) {
            if (Checker.collision(map.pacman, food)) {
                map.foods.remove(food);
                score += 10;
                soundEffect.playSound("gs_chomp.wav");
                break;
            }
        }
    }

    private void checkPowerFoodCollision() {
        for (Entity powerFood : map.powerFoods) {
            if (Checker.collision(map.pacman, powerFood)) {
                map.powerFoods.remove(powerFood);
                score += 50;
                canEatGhost = true;
                soundEffect.playSound("gs_siren_soft.wav");
                for (Ghost ghost : map.ghosts) {
                    ghost.setScared();
                }
                // 7 seconds
                Timer powerFoodTimer = new Timer(7000, e -> {
                    for (Ghost ghost : map.ghosts) {
                        // return ghost to normal
                        ghost.resetImage();
                    }
                    canEatGhost = false;
                });

                powerFoodTimer.setRepeats(false);
                powerFoodTimer.start();
                break;
            }
        }
    }

    // play start sound and start the game loop after 4 seconds
    private void playStartSound() {
        soundEffect.playSound("gs_start.wav");
        Timer startSoundTimer = new Timer(4000, e -> {
            gameLoop.start();
        });
        startSoundTimer.setRepeats(false);
        startSoundTimer.start();
    }

    private void playBackgroundSound() {
        Timer backgroundSoundTimer = new Timer(4000, e -> {
            soundEffect.playLoopingSound("background.wav");
        });
        backgroundSoundTimer.setRepeats(false);
        backgroundSoundTimer.start();
    }

    private void resetPositions() {
        map.pacman.reset();
        map.pacman.velocityX = 0;
        map.pacman.velocityY = 0;
        initializeGhosts();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        run();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            map.loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> map.pacman.updateDirection('U', tileSize, map.walls);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> map.pacman.updateDirection('D', tileSize, map.walls);
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> map.pacman.updateDirection('L', tileSize, map.walls);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> map.pacman.updateDirection('R', tileSize, map.walls);
        }

        switch (map.pacman.direction) {
            case 'U' -> map.pacman.image = pacmanUpImage;
            case 'D' -> map.pacman.image = pacmanDownImage;
            case 'L' -> map.pacman.image = pacmanLeftImage;
            case 'R' -> map.pacman.image = pacmanRightImage;
        }
    }
}