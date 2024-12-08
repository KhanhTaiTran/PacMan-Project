import java.awt.Image;

public class Ghost extends Entity {
    char[] directions = { 'U', 'D', 'L', 'R' };

    private Image normalImage;
    private Image scaredImage;

    public Ghost(Image normalImage, Image scaredImage, int x, int y, int width, int height) {
        super(normalImage, x, y, width, height);
        this.normalImage = normalImage;
        this.scaredImage = scaredImage;
        setMovementStrategy(new GhostMovementStrategy());
    }

    public void setScared() {
        this.image = scaredImage;
    }

    public void resetImage() {
        this.image = normalImage;
    }
}