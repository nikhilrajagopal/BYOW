package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Engine {
    private static TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 70;
    public static final int HEIGHT = 40;
    private static long SEED;
    private static ArrayList<Room> roomies;
    public static final String GAME = "saved-game.txt";
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

     public Engine() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
    }

    public static void interactWithKeyboard() {
        mainMenu();
        String appendThing = ""; boolean f = false;
        while (true) {
            boolean b = false;
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'n' || key == 'N') {
                    helper(key, appendThing, b);
                } else if (key == 'l' || key == 'L') {
                    StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 15));
                    String s = loadGame();
                    if (s == null) {
                        errorMessage();
                        interactWithKeyboard();
                    }
                    helper2(appendThing, s, b);
                } else if (key == 'r' || key == 'R') {
                    StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 15));
                    String s = loadGame();
                    if (s == null) {
                        errorMessage2();
                        interactWithKeyboard();
                    }
                    appendThing = s;
                    SEED = readSeed(s.substring(1));
                    Random randSeed = new Random(SEED);
                    TETile[][] finalWorldFrame = makeGame(randSeed);
                    String slimy = readUserMovements(s);
                    Avatar a = putPlayer(finalWorldFrame);
                    putFlower(finalWorldFrame);
                    ter.renderFrame(finalWorldFrame);
                    for (int i = 0; i < slimy.length(); i++) {
                        makeHUD(finalWorldFrame);
                        a.moveAvatar1(slimy.charAt(i), finalWorldFrame, WIDTH, HEIGHT);
                        StdDraw.show();
                        ter.renderFrame(finalWorldFrame);
                        StdDraw.pause(500);
                    }
                    ter.renderFrame(finalWorldFrame);
                    while (true) {
                        try {
                            makeHUD(finalWorldFrame);
                        } catch (ArrayIndexOutOfBoundsException o) {
                            continue;
                        }
                        if (StdDraw.hasNextKeyTyped()) {
                            char key1 = StdDraw.nextKeyTyped();
                            appendThing += key1;
                            if (key1 == 'w' || key1 == 'W' || key1 == 'a'
                                    || key1 == 'A' || key1 == 's' || key1 == 'S'
                                    || key1 == 'd' || key1 == 'D') {
                                boolean u = a.moveAvatar1(key1, finalWorldFrame, WIDTH, HEIGHT);
                                if (u) {
                                    saveEmptyFile();
                                    interactWithKeyboard();
                                }
                                ter.renderFrame(finalWorldFrame);
                                b = false;
                            } else if (key1 == ':') {
                                b = true;
                            } else if ((key1 == 'q' || key1 == 'Q') && b) {
                                saveGame2(appendThing.substring(0, appendThing.length() - 1));
                                System.exit(0);
                                return;
                            }
                        }
                    }
                } else if ((key == 'q' || key == 'Q') && f) {
                    System.exit(0);
                    return;
                } else if (key == ':') {
                    f = true;
                } else if (key == 'b' || key == 'B') {
                    backgroundStory();
                    interactWithKeyboard();
                }
            }
        }
    }

    public static void backgroundStory() {
        StdDraw.clear();
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 25));
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0) + 10, "THE STORY:");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 + 6, "Once upon a time, there was a CS 61B "
                + "student working on his BYOW project.");
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0) + 4, "Everything was going well, until "
                + "during one of his tests, he came across a major bug.");
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0) + 2, "The problem was that he was using "
                + "INFINITE RECURSION.");
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0), "He is now trapped inside his "
                + "own game as an avatar (@).");
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0) - 2, "Your mission, should you choose to "
                + "accept, is to collect the flower to bring him back!");
        StdDraw.setFont(new Font("Times New Roman", Font.ITALIC, 19));
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0) - 8, "(type in 's' to go back to the main menu)");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 's' || input == 'S') {
                    return;
                }
            }
        }
    }

    public static void helper2(String appendThing, String s, boolean b) {
        appendThing = s;
        SEED = readSeed(s.substring(1));
        Random randSeed = new Random(SEED);
        TETile[][] finalWorldFrame = makeGame(randSeed);
        String slimy = readUserMovements(s);
        Avatar a = putPlayer(finalWorldFrame);
        putFlower(finalWorldFrame);
        movements(slimy, a, finalWorldFrame);
        ter.renderFrame(finalWorldFrame);
        while (true) {
            try {
                makeHUD(finalWorldFrame);
                if (StdDraw.hasNextKeyTyped()) {
                    char key1 = StdDraw.nextKeyTyped();
                    appendThing += key1;
                    if (key1 == 'w' || key1 == 'W' || key1 == 'a'
                            || key1 == 'A' || key1 == 's' || key1 == 'S'
                            || key1 == 'd' || key1 == 'D') {
                        boolean u = a.moveAvatar1(key1, finalWorldFrame, WIDTH, HEIGHT);
                        if (u) {
                            saveEmptyFile();
                            interactWithKeyboard();
                        }
                        ter.renderFrame(finalWorldFrame);
                        b = false;
                    } else if (key1 == ':') {
                        b = true;
                    } else if ((key1 == 'q' || key1 == 'Q') && b) {
                        saveGame2(appendThing.substring(0, appendThing.length() - 1));
                        System.exit(0);
                        return;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException o) {
                if (StdDraw.hasNextKeyTyped()) {
                    char key1 = StdDraw.nextKeyTyped();
                    appendThing += key1;
                    if (key1 == 'w' || key1 == 'W' || key1 == 'a'
                            || key1 == 'A' || key1 == 's' || key1 == 'S'
                            || key1 == 'd' || key1 == 'D') {
                        boolean u = a.moveAvatar1(key1, finalWorldFrame, WIDTH, HEIGHT);
                        if (u) {
                            saveEmptyFile();
                            interactWithKeyboard();
                        }
                        ter.renderFrame(finalWorldFrame);
                        b = false;
                    } else if (key1 == ':') {
                        b = true;
                    } else if ((key1 == 'q' || key1 == 'Q') && b) {
                        saveGame2(appendThing.substring(0, appendThing.length() - 1));
                        System.exit(0);
                        return;
                    }
                }
            }
        }
    }

    public static void helper(char key, String appendThing, boolean b) {
        appendThing += key;
        String input = typeInSeed();
        appendThing += input;
        SEED = readSeed(input);
        Random randSeed = new Random(SEED);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 15));
        TETile[][] finalWorldFrame = makeGame(randSeed);
        Avatar a = putPlayer(finalWorldFrame);
        putFlower(finalWorldFrame);
        ter.renderFrame(finalWorldFrame);
        while (true) {
            try {
                makeHUD(finalWorldFrame);
            } catch (ArrayIndexOutOfBoundsException o) {
                o.printStackTrace();
                continue;
            }
            if (StdDraw.hasNextKeyTyped()) {
                char key1 = StdDraw.nextKeyTyped();
                appendThing += key1;
                if (key1 == 'w' || key1 == 'W' || key1 == 'a'
                        || key1 == 'A' || key1 == 's' || key1 == 'S'
                        || key1 == 'd' || key1 == 'D') {
                    boolean u = a.moveAvatar1(key1, finalWorldFrame, WIDTH, HEIGHT);
                    if (u) {
                        saveEmptyFile();
                        interactWithKeyboard();
                        //return;
                    }
                    ter.renderFrame(finalWorldFrame);
                    b = false;
                } else if (key1 == ':') {
                    b = true;
                } else if ((key1 == 'q' || key1 == 'Q') && b) {
                    saveGame2(appendThing.substring(0, appendThing.length() - 1));
                    System.exit(0);
                    return;
                }
            }
        }
    }

    public static void errorMessage() {
        StdDraw.clear();
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 35));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "Can't use load because no game was saved, or");
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0) - 2, "you either won/lost the game.");
        StdDraw.show();
        StdDraw.pause(4000);
    }

    public static void errorMessage2() {
        StdDraw.clear();
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 35));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "Can't use replay because no game was saved.");
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0) - 2, "you either won/lost the game.");
        StdDraw.show();
        StdDraw.pause(4000);
    }

    public static void putFlower(TETile[][] world) {
        Position p = null;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (world[x][y] == Tileset.FLOOR) {
                    p = new Position(x, y);
                }
            }
        }
        if (world[p.getX() + 1][p.getY()] == Tileset.SAND) {
            world[p.getX() + 1][p.getY()] = Tileset.FLOOR;
        } else if (world[p.getX() - 1][p.getY()] == Tileset.SAND) {
            world[p.getX() - 1][p.getY()] = Tileset.FLOOR;
        } else if (world[p.getX()][p.getY() + 1] == Tileset.SAND) {
            world[p.getX()][p.getY() + 1] = Tileset.FLOOR;
        } else if (world[p.getX()][p.getY() - 1] == Tileset.SAND) {
            world[p.getX()][p.getY() - 1] = Tileset.SAND;
        }
        world[p.getX()][p.getY()] = Tileset.FLOWER;
    }

    public static void makeHUD(TETile[][] world) {
        int xCord = (int) StdDraw.mouseX();
        int yCord = (int) StdDraw.mouseY();
        if (xCord < WIDTH && yCord < HEIGHT) {
            StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 15));
            ter.renderFrame(world);
            if (world[xCord][yCord] == Tileset.FLOOR) {
                helper("a floor.");
            } else if (world[xCord][yCord] == Tileset.WALL) {
                helper("a wall.");
            } else if (world[xCord][yCord] == Tileset.AVATAR) {
                helper("an avatar.");
            } else if (world[xCord][yCord] == Tileset.FLOWER) {
                helper("a flower.");
            } else if (world[xCord][yCord] == Tileset.SAND) {
                helper("a sand.");
            } else {
                helper("nothing.");
            }
            StdDraw.show();
        }
    }

    public static void helper(String type) {
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(4, HEIGHT - 2, "This is " + type);
    }

    public static String typeInSeed() {
        StdDraw.clear();
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 35));
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0) + 7, "Objective: Collect the flower, "
                + "while avoiding the sand.");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "Enter a Seed.");
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0) - 3, "After inputting, "
                + "type in s to initialize world.");
        StdDraw.show();
        String inputSeed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                StdDraw.clear();
                StdDraw.setXscale(0.0, WIDTH);
                StdDraw.setYscale(0.0, HEIGHT);
                if (Character.isDigit(input)) {
                    inputSeed += input;
                    StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0), inputSeed);
                    StdDraw.show();
                } else if (input == 's' || input == 'S') {
                    StdDraw.clear();
                    return inputSeed;
                }
            }
        }
    }

    public static void mainMenu() {
        StdDraw.clear();
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT - 1);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 65));
        StdDraw.text(WIDTH / 2.0, ((HEIGHT / 2.0) + 11), "CS 61B: THE GAME");
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 35));
        StdDraw.text(WIDTH / 2.0, ((HEIGHT / 2.0) - 4), "Load Game (L)");
        StdDraw.text(WIDTH / 2.0, (HEIGHT / 2.0), "New Game (N)");
        StdDraw.text(WIDTH / 2.0, ((HEIGHT / 2.0) - 8), "Quit (Q)");
        StdDraw.text(WIDTH / 2.0, ((HEIGHT / 2.0) - 12), "Replay Game (R)");
        StdDraw.text(WIDTH / 2.0, ((HEIGHT / 2.0) + 4), "Background (B)");
        StdDraw.show();
    }

    /* Source: https://stackoverflow.com/questions/2338790/
    get-int-from-string-also-containing-letters-in-java*/
    public static long readSeed(String input) {
        String s = "";
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                s += input.charAt(i);
            }
        }
        return Long.parseLong(s);
    }

    public static String readUserMovements(String input) {
        input.substring(1);
        String s = "";
        for (int i = 0; i < input.length(); i++) {
            if (!(Character.isDigit(input.charAt(i)))) {
                s += input.charAt(i);
            }
        }
        return s;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        char x = input.charAt(0);
        if (x == 'n' || x == 'N') {
            SEED = readSeed(input);
            Random randSeed = new Random(SEED);
            String chars = readUserMovements(input);
            TETile[][] finalWorldFrame = makeGame(randSeed);
            Avatar a = putPlayer(finalWorldFrame);
            movements(chars, a, finalWorldFrame);
            //ter.renderFrame(finalWorldFrame);
            return finalWorldFrame;
        } else if (x == 'l' || x == 'L') {
            String previousInputs = loadGame();
            SEED = readSeed(previousInputs);
            Random randSeed = new Random(SEED);
            String chars = readUserMovements(previousInputs);
            TETile[][] finalWorldFrame = makeGame(randSeed);
            Avatar a = putPlayer(finalWorldFrame);
            movements(chars + input.substring(1), a, finalWorldFrame);
            //ter.renderFrame(finalWorldFrame);
            return finalWorldFrame;
        } else if (x == ':') {
            if (input.charAt(1) == 'q' || input.charAt(1) == 'Q') {
                System.exit(0);
                return null;
            }
        }
        return null;
    }

    //Source: https://www.vogella.com/tutorials/JavaSerialization/article.html
    public static String loadGame() {
        FileInputStream fis;
        ObjectInputStream in;
        try {
            fis = new FileInputStream(GAME);
            in = new ObjectInputStream(fis);
            Object obj = in.readObject();
            in.close();
            return (String) obj;
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }

    public static void saveEmptyFile() {
        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = new FileOutputStream(GAME);
            out = new ObjectOutputStream(fos);
            out.writeObject(null);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Source: https://www.vogella.com/tutorials/JavaSerialization/article.html
    public static void saveGame2(String movements) {
        movements.toLowerCase(Locale.ROOT);
        String newMovements = "";
        for (int i = 0; i < movements.length(); i++) {
            char x = movements.charAt(i);
            if (!(x == ':' || x == 'l' || x == 'q')) {
                newMovements += x;
            }
        }
        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = new FileOutputStream(GAME);
            out = new ObjectOutputStream(fos);
            out.writeObject(newMovements.toString());
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Source: https://www.vogella.com/tutorials/JavaSerialization/article.html
    public static void saveGame(String movements) {
        movements.toLowerCase(Locale.ROOT);
        String newMovements = "n" + String.valueOf(SEED);
        for (int i = 0; i < movements.length(); i++) {
            char x = movements.charAt(i);
            if (!(x == ':' || x == 'l' || x == 'q')) {
                newMovements += x;
            }
        }
        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = new FileOutputStream(GAME);
            out = new ObjectOutputStream(fos);
            out.writeObject(newMovements.toString());
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void movements(String input, Avatar a, TETile[][] world) {
        for (int i = 0; i < input.length(); i++) {
            char movement = input.charAt(i);
            if (movement == ':') {
                if (input.charAt(i + 1) == 'q' || input.charAt(i + 1) == 'Q') {
                    saveGame(input);
                    return;
                }
            }
            a.moveAvatar1(movement, world, WIDTH, HEIGHT);
        }
    }

    public static Avatar putPlayer(TETile[][] world) {
        boolean b = false;
        Position p = null;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (world[x][y] == Tileset.FLOOR) {
                    p = new Position(x, y);
                    b = true;
                    break;
                }
            }
            if (b) {
                break;
            }
        }
        Avatar avatar = new Avatar(p);
        world[avatar.getLocation().getX()][avatar.getLocation().getY()] = Tileset.AVATAR;
        return avatar;
    }

    public static TETile[][] makeGame(Random rand) {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        ArrayList<Room> mapOfRooms = new ArrayList<>();
        for (int i = 0; i < RandomUtils.uniform(rand, 12, 20); i++) {
            Room x = drawRoom2(rand);
            if (!overlapChecker(x, mapOfRooms)) {
                mapOfRooms.add(x);
            }
        }
        for (Room r : mapOfRooms) {
            drawRoom(r, world, rand);
        }
        roomies = mapOfRooms;
        ArrayList<Room> rooms = mapOfRooms;
        int min = 10000;
        Room saver = null;
        for (Room r : rooms) {
            int check = r.getP().getX() + r.getP().getY();
            if (check < min) {
                min = check;
                saver = r;
            }
        }
        recursiveHelper(world, saver, mapOfRooms);
        addWalls(world);
        return world;
    }

    public static void recursiveHelper(TETile[][] world, Room first, ArrayList<Room> rooms) {
        boolean baseCase = true;
        for (Room r : rooms) {
            if (!r.getIsConnected()) {
                baseCase = false;
            }
        }
        if (baseCase) {
            return;
        }
        generateHallWay(world, first, findShortestDistance(first, rooms));
        first.setConnected(true);
        recursiveHelper(world, findShortestDistance(first, rooms), rooms);
    }

    public static Room findShortestDistance(Room r1, ArrayList<Room> rooms) {
        Room smallest = null;
        double distance = 1000;
        double placeholder;
        for (int i = 0; i < rooms.size(); i++) {
            if (!rooms.get(i).equals(r1)) {
                placeholder = getPythogorean(r1, rooms.get(i));
                if (placeholder < distance && !rooms.get(i).getIsConnected()) {
                    distance = placeholder;
                    smallest = rooms.get(i);
                }
            }
        }
        return smallest;

    }

    public static double getPythogorean(Room r1, Room r2) {
        double distance = Math.sqrt(Math.pow((r1.centerCoordinates().getX()
                - r2.centerCoordinates().getX()), 2)
                + Math.pow((r1.centerCoordinates().getY()
                - r2.centerCoordinates().getY()), 2));
        return distance;
    }

    public static void generateHallWay(TETile[][] world, Room r1, Room r2) {
        if (r1 == null || r2 == null) {
            return;
        }
        Position coord1 = r1.getCenter();
        Position coord2 = r2.getCenter();
        drawHallway(world, coord1, coord2);
    }

    public static void drawHallway(TETile[][] world, Position coord1, Position coord2) {
        Position updatedHorizontal1;
        Position updatedHorizontal2;
        int n1 = Math.min(coord1.getX(), coord2.getX());
        if (n1 == coord1.getX()) {
            updatedHorizontal1 = coord1;
            updatedHorizontal2 = coord2;
        } else {
            updatedHorizontal1 = coord2;
            updatedHorizontal2 = coord1;
        }
        Position updatedVertical1;
        Position updatedVertical2;
        int n2 = Math.min(coord1.getY(), coord2.getY());
        if (n2 == coord1.getY()) {
            updatedVertical1 = coord1;
            updatedVertical2 = coord2;
        } else {
            updatedVertical1 = coord2;
            updatedVertical2 = coord1;
        }
        if (updatedHorizontal1.getY() > updatedHorizontal2.getY()) {
            for (int i = updatedVertical1.getY(); i < updatedVertical2.getY() + 1; i++) {
                world[updatedVertical1.getX() + 1][i + 1] = Tileset.FLOOR;
            }
        } else {
            for (int i = updatedVertical2.getY(); i >= updatedVertical1.getY() + 1; i--) {
                world[updatedVertical1.getX() + 1
                        + (updatedHorizontal2.getX() - updatedHorizontal1.getX())]
                        [i + 1] = Tileset.FLOOR;
            }
        }

        for (int i = updatedHorizontal1.getX(); i < updatedHorizontal2.getX() + 1; i++) {
            world[i + 1][updatedHorizontal1.getY() + 1] = Tileset.FLOOR;
        }
    }


    public static boolean overlapChecker(Room curr, ArrayList<Room> otherRooms) {
        if (otherRooms.isEmpty()) {
            return false;
        }
        for (Room other : otherRooms) {
            if (isOverlap(curr, other)) {
                return true;
            }
        }
        return false;
    }

    /* Source: https://www.baeldung.com/java-check-if-two-rectangles-overlap*/
    public static boolean isOverlap(Room curr, Room other) {
        int topRightX = curr.getP().getX() + curr.getWidth();
        int topRightY = curr.getP().getY() + curr.getHeight();
        int bottomLeftX = curr.getP().getX();
        int bottomLeftY = curr.getP().getY();
        int otherTopRightX = other.getP().getX() + curr.getWidth();
        int otherTopRightY = other.getP().getY() + other.getHeight();
        int otherBottomLeftX = other.getP().getX();
        int otherBottomLeftY = other.getP().getY();
        if (topRightY <= otherBottomLeftY - 3 || bottomLeftY - 3 >= otherTopRightY) {
            return false;
        }
        return topRightX + 3 > otherBottomLeftX && bottomLeftX < otherTopRightX + 3;
    }

    public static Room drawRoom2(Random rand) {
        int heightRoom = rand.nextInt(4) + 5;
        int widthRoom = rand.nextInt(4) + 5;
        Position p = new Position(rand.nextInt(WIDTH - widthRoom - 4),
                rand.nextInt(HEIGHT - heightRoom - 4));
        return new Room(heightRoom, widthRoom, p, false);
    }

    public static void drawRoom(Room r, TETile[][] world, Random rand) {
        int heightRoom = r.getHeight();
        int widthRoom = r.getWidth();
        int xCord = r.getP().getX();
        int yCord = r.getP().getY();

        for (int i = 1; i <= heightRoom; i++) {
            for (int j = 1; j <= widthRoom; j++) {
                Random rand1 = new Random(i * j);
                int x = rand1.nextInt(10);
                if (x == 1) {
                    world[j + xCord][i + yCord] = Tileset.SAND;
                } else {
                    world[j + xCord][i + yCord] = Tileset.FLOOR;
                }
            }
            System.out.println(); //Jump to the next row
        }
    }
    public static void addWalls(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (world[x][y] == Tileset.FLOOR || world[x][y] == Tileset.SAND) {
                    if (world[x + 1][y] == Tileset.NOTHING) {
                        world[x + 1][y] = Tileset.WALL;
                    }
                    if (world[x - 1][y] == Tileset.NOTHING) {
                        world[x - 1][y] = Tileset.WALL;
                    }
                    if (world[x][y + 1] == Tileset.NOTHING) {
                        world[x][y + 1] = Tileset.WALL;
                    }
                    if (world[x][y - 1] == Tileset.NOTHING) {
                        world[x][y - 1] = Tileset.WALL;
                    }
                }
            }
        }
    }
}
