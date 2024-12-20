import java.awt.Image;
import java.util.HashSet;

public class Entity {

    int x;
    int y;
    int width;
    int height;
    Image image;

    int startX;
    int startY;
    char direction = 'U'; // U D L R
    int velocityX = 0;
    int velocityY = 0;

    private MovementStrategy movementStrategy;

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public void move(int tileSize, HashSet<Entity> walls) {
        if (movementStrategy != null) {
            movementStrategy.move(this, tileSize, walls);
        }
    }

    public Entity(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }

    public void updateDirection(char direction, int tileSize, HashSet<Entity> walls) {
        char prevDirection = this.direction; // save the previous direction
        this.direction = direction;
        updateVelocity(tileSize); // update the velocity based on the new direction
        this.x += this.velocityX;
        this.y += this.velocityY;
        for (Entity wall : walls) {
            if (Checker.collision(this, wall)) {
                // if there is a collision, revert the position and direction
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                this.direction = prevDirection;
                updateVelocity(tileSize); // update the velocity based on the previous direction
            }
        }
    }

    public void updateVelocity(int tileSize) {
        if (this.direction == 'U') {
            this.velocityX = 0;
            this.velocityY = -tileSize / 4;
        } else if (this.direction == 'D') {
            this.velocityX = 0;
            this.velocityY = tileSize / 4;
        } else if (this.direction == 'L') {
            this.velocityX = -tileSize / 4;
            this.velocityY = 0;
        } else if (this.direction == 'R') {
            this.velocityX = tileSize / 4;
            this.velocityY = 0;
        }
    }

    public void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }
}