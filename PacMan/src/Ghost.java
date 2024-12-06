import java.awt.Image;
import java.util.HashSet;
import java.util.Random;

public class Ghost extends Entity {
    char[] directions = { 'U', 'D', 'L', 'R' };
    private Random random = new Random();
    private Image normalImage;
    private Image scaredImage;

    public Ghost(Image normalImage, Image scaredImage, int x, int y, int width, int height) {
        super(normalImage, x, y, width, height);
        this.normalImage = normalImage;
        this.scaredImage = scaredImage;
        updateDirection(directions[random.nextInt(4)], 32, new HashSet<>());
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

    public void setScared() {
        this.image = scaredImage;
    }

    public void resetImage() {
        this.image = normalImage;
    }
}