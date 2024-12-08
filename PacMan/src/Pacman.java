import java.awt.Image;
import java.util.HashSet;

public class Pacman extends Entity {
    public Pacman(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
        setMovementStrategy(new PacmanMovementStrategy());
    }

    public void updateDirection(char direction, int tileSize, HashSet<Entity> walls) {
        super.updateDirection(direction, tileSize, walls);
    }
}