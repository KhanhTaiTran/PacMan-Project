import java.util.HashSet;

public class PacmanMovementStrategy implements MovementStrategy {
    @Override
    public void move(Entity entity, int tileSize, HashSet<Entity> walls) {
        entity.updateVelocity(tileSize);
        entity.x += entity.velocityX;
        entity.y += entity.velocityY;
        for (Entity wall : walls) {
            if (Checker.collision(entity, wall)) {
                entity.x -= entity.velocityX;
                entity.y -= entity.velocityY;
            }
        }
    }

}