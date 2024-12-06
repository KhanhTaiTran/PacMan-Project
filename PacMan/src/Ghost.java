import java.awt.Image;
import java.util.HashSet;
import java.util.Random;

public class Ghost extends Entity {
    char[] directions = { 'U', 'D', 'L', 'R' }; // up down left right
    private Random random = new Random();

    public Ghost(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
        updateDirection(directions[random.nextInt(4)], 32, new HashSet<Entity>());
    }

    public void ghostMove(HashSet<Entity> walls, int boardWidth, int tileSize) {
        if (this.y == tileSize * 9 && this.direction != 'U' && this.direction != 'D') {
            updateDirection('U', tileSize, walls);
        }
        this.x += this.velocityX;
        this.y += this.velocityY;
        for (Entity wall : walls) {
            if (Checker.collision(this, wall) || this.x <= 0 || this.x + this.width >= boardWidth) {
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                char newDirection = directions[random.nextInt(4)];
                updateDirection(newDirection, tileSize, walls);
            }
        }
    }

}
