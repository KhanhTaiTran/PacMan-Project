import java.util.HashSet;
import java.util.Random;

public class GhostMovementStrategy implements MovementStrategy {
    private Random random = new Random();
    private char[] directions = { 'U', 'D', 'L', 'R' };

    @Override
    public void move(Entity entity, int tileSize, HashSet<Entity> walls) {
        Ghost ghost = (Ghost) entity;
        if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
            ghost.updateDirection('U', tileSize, walls);
        }
        ghost.x += ghost.velocityX;
        ghost.y += ghost.velocityY;
        for (Entity wall : walls) {
            if (Checker.collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= tileSize * 19) {
                ghost.x -= ghost.velocityX;
                ghost.y -= ghost.velocityY;
                char newDirection = directions[random.nextInt(4)];
                ghost.updateDirection(newDirection, tileSize, walls);
            }
        }
    }
}