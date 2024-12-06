public class TeleportGate extends Entity {
    private TeleportGate targetGate;

    public TeleportGate(int x, int y, int width, int height) {
        super(null, x, y, width, height);
    }

    public void setTargetGate(TeleportGate targetGate) {
        this.targetGate = targetGate;
    }

    public TeleportGate getTargetGate() {
        return targetGate;
    }
}