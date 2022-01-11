package byow.Core;

public class Room {
    private Position p;
    private int width;
    private int height;
    private Position topRightCorner;
    private boolean isConnected;
    private Position center;


    public Room(int height, int width, Position p, boolean isConnected) {
        this.p = p;
        this.width = width;
        this.height = height;
        this.isConnected = isConnected;
        this.topRightCorner = new Position(p.getX() + width, p.getY() + height);
        this.center = centerCoordinates();
    }

    public Position centerCoordinates() {
        int x = (p.getX() + topRightCorner.getX()) / 2;
        int y = (p.getY() + topRightCorner.getY()) / 2;
        return new Position(x, y);
    }

    public Position getP() {
        return this.p;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Position getTopRightCorner() {
        return this.topRightCorner;
    }

    public boolean getIsConnected() {
        return this.isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public Position getCenter() {
        return this.center;
    }
}
