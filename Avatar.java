package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

//import java.awt.*;
import java.awt.Font;
import java.awt.Color;

public class Avatar {
    private Position location;

    public Avatar(Position location) {
        this.location = location;
    }

    public boolean moveAvatar1(char input, TETile[][] world, int w, int h) {
        boolean b = false;
        boolean l = false;
        if (input == 'w' || input == 'W') {
            if (!(world[this.location.getX()][this.location.getY() + 1] == Tileset.WALL)) {
                removeCurr(world);
                if (world[this.location.getX()][this.location.getY() + 1] == Tileset.FLOWER) {
                    b = true;
                } else if (world[this.location.getX()][this.location.getY() + 1] == Tileset.SAND) {
                    l = true;
                }
                this.location.setY(this.location.getY() + 1);
                updateCurr(world);
                boolean x = checkWinLose(b, l, w, h);
                return x;
            }
        } else if (input == 'a' || input == 'A') {
            if (!(world[this.location.getX() - 1][this.location.getY()] == Tileset.WALL)) {
                removeCurr(world);
                if (world[this.location.getX() - 1][this.location.getY()] == Tileset.FLOWER) {
                    b = true;
                } else if (world[this.location.getX() - 1][this.location.getY()] == Tileset.SAND) {
                    l = true;
                }
                this.location.setX(this.location.getX() - 1);
                updateCurr(world);
                boolean x = checkWinLose(b, l, w, h);
                return x;
            }
        } else if (input == 's' || input == 'S') {
            if (!(world[this.location.getX()][this.location.getY() - 1] == Tileset.WALL)) {
                removeCurr(world);
                if (world[this.location.getX()][this.location.getY() - 1] == Tileset.FLOWER) {
                    b = true;
                } else if (world[this.location.getX()][this.location.getY() - 1] == Tileset.SAND) {
                    l = true;
                }
                this.location.setY(this.location.getY() - 1);
                updateCurr(world);
                boolean x = checkWinLose(b, l, w, h);
                return x;
            }
        } else if (input == 'd' || input == 'D') {
            if (!(world[this.location.getX() + 1][this.location.getY()] == Tileset.WALL)) {
                removeCurr(world);
                if (world[this.location.getX() + 1][this.location.getY()] == Tileset.FLOWER) {
                    b = true;
                } else if (world[this.location.getX() + 1][this.location.getY()] == Tileset.SAND) {
                    l = true;
                }
                this.location.setX(this.location.getX() + 1);
                updateCurr(world);
                boolean x = checkWinLose(b, l, w, h);
                return x;
            }
        }
        return false;
    }

    public static boolean checkWinLose(boolean b, boolean l, int w, int h) {
        if (l) {
            drawLose(w, h);
            return true;
        }
        if (b) {
            drawWin(w, h);
            return true;
        }
        return false;
    }
    public static void drawWin(int width, int height) {
        StdDraw.clear();
        StdDraw.setXscale(0.0, width);
        StdDraw.setYscale(0.0, height);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 55));
        StdDraw.text(width / 2.0, (height / 2.0), "YOU WIN!");
        StdDraw.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        StdDraw.text(width / 2.0, (height / 2.0) - 5, "(You will now go back to the main menu.)");
        StdDraw.show();
        StdDraw.pause(5000);
    }
    public static void drawLose(int width, int height) {
        StdDraw.clear();
        StdDraw.setXscale(0.0, width);
        StdDraw.setYscale(0.0, height);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 55));
        StdDraw.text(width / 2.0, (height / 2.0), "YOU LOSE!");
        StdDraw.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        StdDraw.text(width / 2.0, (height / 2.0) - 5, "(You will now go back to the main menu.)");
        StdDraw.show();
        StdDraw.pause(5000);
    }


    public void removeCurr(TETile[][] world) {
        world[this.location.getX()][this.location.getY()] = Tileset.FLOOR;
    }

    public void updateCurr(TETile[][] world) {
        world[this.location.getX()][this.location.getY()] = Tileset.AVATAR;
    }

    public Position getLocation() {
        return this.location;
    }
}
