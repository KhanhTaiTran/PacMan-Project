import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import java.util.Random;
import java.util.TimerTask;

import javax.sound.sampled.Clip;

public class gamePanel extends JPanel implements ActionListener, KeyListener {

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    Image wallImage;
    Image blueGhostImage;
    Image orangeGhostImage;
    Image pinkGhostImage;
    Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    private Map map;
    private Timer gameLoop;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    Random random = new Random();

    public gamePanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // load images
        wallImage = new ImageIcon(getClass().getResource("resource/wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("resource/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("resource/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("resource/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("resource/redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("resource/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("resource/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("resource/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("resource/pacmanRight.png")).getImage();

        // load map
        map = new Map(wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanRightImage);
        for (Ghost ghost : map.ghosts) {
            char newDirection = ghost.directions[random.nextInt(4)];
            ghost.updateDirection(newDirection, tileSize, map.walls);
        }
        // how long it takes to start timer, milliseconds gone between frames
        gameLoop = new Timer(50, this); // 20fps (1000/50)
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Draw the game
    public void draw(Graphics g) {
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
        // score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize / 2, tileSize / 2);
        } else {
            g.drawString(String.format("x%d Score: %d", lives, score), tileSize / 2, tileSize / 2);
        }
    }

    // game logic
    public void run() {

        map.pacman.x += map.pacman.velocityX;
        map.pacman.y += map.pacman.velocityY;

        // check wall collisions
        for (Entity wall : map.walls) {
            if (Checker.collision(map.pacman, wall)) {
                map.pacman.x -= map.pacman.velocityX;
                map.pacman.y -= map.pacman.velocityY;
                break;
            }
        }

        // check ghost collisions
        for (Ghost ghost : map.ghosts) {
            if (Checker.collision(ghost, map.pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            ghost.ghostMove(map.walls, boardWidth, tileSize);
        }

        // check food collision
        for (Entity food : map.foods) {
            if (Checker.collision(map.pacman, food)) {
                map.foods.remove(food);
                score += 10;
                break;
            }
        }

    }

    public void resetPositions() {
        map.pacman.reset();
        map.pacman.velocityX = 0;
        map.pacman.velocityY = 0;
        for (Ghost ghost : map.ghosts) {
            ghost.reset();
            char newDirection = ghost.directions[random.nextInt(4)];
            ghost.updateDirection(newDirection, tileSize, map.walls);
        }
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
        System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            map.pacman.updateDirection('U', tileSize, map.walls);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            map.pacman.updateDirection('D', tileSize, map.walls);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            map.pacman.updateDirection('L', tileSize, map.walls);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            map.pacman.updateDirection('R', tileSize, map.walls);
        }

        if (map.pacman.direction == 'U') {
            map.pacman.image = pacmanUpImage;
        } else if (map.pacman.direction == 'D') {
            map.pacman.image = pacmanDownImage;
        } else if (map.pacman.direction == 'L') {
            map.pacman.image = pacmanLeftImage;
        } else if (map.pacman.direction == 'R') {
            map.pacman.image = pacmanRightImage;
        }
    }
}