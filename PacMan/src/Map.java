import java.awt.Image;
import java.util.HashSet;

public class Map {
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image pacmanRightImage;

    // X = wall, O = Skip(nothing), b = blue ghost, o = orange ghost, p = pink
    // ghost, r = red ghost, P = pacman
    // ' ' = food
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X                 X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X   X   X    X",
            "XXXX XXX X XXX XXXX",
            "OOOX X   r   X XOOO",
            "XXXX X XX XX X XXXX",
            "O      XbpoX      O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };
    // HashSet is a collection that contains no duplicate elements
    public HashSet<Entity> walls;
    public HashSet<Entity> foods;
    public HashSet<Ghost> ghosts;
    public Entity pacman;

    public Map(Image wallImage, Image blueGhostImage, Image orangeGhostImage, Image pinkGhostImage, Image redGhostImage,
            Image pacmanRightImage) {
        this.wallImage = wallImage;
        this.blueGhostImage = blueGhostImage;
        this.orangeGhostImage = orangeGhostImage;
        this.pinkGhostImage = pinkGhostImage;
        this.redGhostImage = redGhostImage;
        this.pacmanRightImage = pacmanRightImage;
        loadMap();
    }

    public void loadMap() { // Load the map
        walls = new HashSet<Entity>(); // Create a new HashSet
        foods = new HashSet<Entity>();
        ghosts = new HashSet<Ghost>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r]; // Get the row
                char tileMapChar = row.charAt(c); // Get the cell

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') { // block wall
                    Entity wall = new Entity(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'b') { // blue ghost
                    Ghost ghost = new Ghost(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'o') { // orange ghost
                    Ghost ghost = new Ghost(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'p') { // pink ghost
                    Ghost ghost = new Ghost(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'r') { // red ghost
                    Ghost ghost = new Ghost(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'P') { // pacman
                    pacman = new Entity(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == ' ') { // food
                    Entity food = new Entity(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }
}