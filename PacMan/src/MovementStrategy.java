import java.util.HashSet;

public interface MovementStrategy {
    void move(Entity entity, int tileSize, HashSet<Entity> walls);
}