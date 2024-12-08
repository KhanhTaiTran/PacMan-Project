import java.awt.Image;
import java.util.Objects;
import javax.swing.ImageIcon;

public class ImageFactory {
    // Load image from the path
    public static Image loadImage(String path) {
        return new ImageIcon(Objects.requireNonNull(ImageFactory.class.getResource(path))).getImage();
    }
}