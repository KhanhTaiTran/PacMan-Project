public class Level {
    private String[] layout;

    public Level(String[] layout) {
        this.layout = layout;
    }

    public String[] getTileMap() {
        return layout;
    }
}