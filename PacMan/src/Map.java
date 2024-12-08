import java.awt.Image;
import java.util.HashSet;

public class Map {

    private int rowCount = 22;
    private int columnCount = 19;
    private int tileSize = 32;

    private Image wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage,
            redGhostImage, pacmanRightImage, powerFoodImage, scaredGhostImage;
    private String[] tileMap = {
            "0000000000000000000",
            "XXXXXXXXXXXXXXXXXXX",
            "X                 X",
            "XfXX XXX X XXX XXfX",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X   X   X    X",
            "XXXX XXX X XXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X X r X X XXXX",
            "T      XbpoX      T",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "Xf X     P     X fX",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    public HashSet<Entity> walls;
    public HashSet<Entity> foods;
    public HashSet<Ghost> ghosts;
    public HashSet<Entity> powerFoods;
    public Pacman pacman;
    public HashSet<TeleportGate> teleportGates;

    public Map(Image wallImage, Image blueGhostImage, Image orangeGhostImage, Image pinkGhostImage,
            Image redGhostImage, Image pacmanRightImage, Image powerFoodImage, Image scaredGhostImage) {
        this.wallImage = wallImage;
        this.blueGhostImage = blueGhostImage;
        this.orangeGhostImage = orangeGhostImage;
        this.pinkGhostImage = pinkGhostImage;
        this.redGhostImage = redGhostImage;
        this.pacmanRightImage = pacmanRightImage;
        this.powerFoodImage = powerFoodImage;
        this.scaredGhostImage = scaredGhostImage;
        loadMap();
    }

    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();
        powerFoods = new HashSet<>();
        teleportGates = new HashSet<>();

        TeleportGate firstGate = null;

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                char tileMapChar = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                switch (tileMapChar) {
                    case 'X' -> walls.add(new Entity(wallImage, x, y, tileSize, tileSize));
                    case 'b' -> ghosts.add(new Ghost(blueGhostImage, scaredGhostImage, x, y, tileSize, tileSize));
                    case 'o' -> ghosts.add(new Ghost(orangeGhostImage, scaredGhostImage, x, y, tileSize, tileSize));
                    case 'p' -> ghosts.add(new Ghost(pinkGhostImage, scaredGhostImage, x, y, tileSize, tileSize));
                    case 'r' -> ghosts.add(new Ghost(redGhostImage, scaredGhostImage, x, y, tileSize, tileSize));
                    case 'P' -> pacman = new Pacman(pacmanRightImage, x, y, tileSize, tileSize);
                    case ' ' -> foods.add(new Entity(null, x + 14, y + 14, 4, 4));
                    case 'f' -> powerFoods.add(new Entity(powerFoodImage, x + 8, y + 8, tileSize - 16, tileSize - 16));
                    case 'T' -> {
                        TeleportGate gate = new TeleportGate(x, y, tileSize, tileSize);
                        teleportGates.add(gate);
                        if (firstGate == null) {
                            firstGate = gate;
                        } else {
                            gate.setTargetGate(firstGate);
                            firstGate.setTargetGate(gate);
                            firstGate = null;
                        }
                    }
                }
            }
        }
    }
}