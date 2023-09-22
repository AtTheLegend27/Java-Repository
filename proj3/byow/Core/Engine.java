package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
//import org.antlr.v4.runtime.misc.Utils;
//import org.reflections.vfs.Vfs;

//import java.awt.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.io.*;
import java.util.Date;

public class Engine implements Serializable {
    //Instance Variables
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 90;
    public static final int WORLDHEIGHT = 50;
    public static int availHEIGHT = 45;
    public static int HUDDimensions = 4;

    static TETile[][] world = new TETile[WIDTH][WORLDHEIGHT];
    int minPartitions = 3;
    int maxPartitions = 5;

    int partitions;
    int xDimensions;
    int yDimensions;
    static Room[][] roomList;

    static Long seedNumber;
    static Random rand;

    static String seedNumberLoadMovements = "";
    static String finalSeedNumberLoad;
    protected static int avatarXPostion;
    protected static int avatarYPostion;
    protected static String name;
    protected static Date d;
    protected static String stringDate;
    static String currentTile;
    static Boolean enterLight = false;
    static Boolean enterLightRadHorizonal = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    //Main Umbrella method that allows Avatar to move on map
    public void interactWithKeyboard() {
        //Need to make opening Menu
        StdDraw.setCanvasSize(WIDTH * (9 + 1), WORLDHEIGHT * (9 + 1));
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, WORLDHEIGHT);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
        //game name
        openingMenuDrawFrame("DK needs some AC", WORLDHEIGHT / 2);
        //game options
        openingMenuDrawFrame("Start New Game (N)", (WORLDHEIGHT / 2) - 2);
        openingMenuDrawFrame("Load Game (L)", (WORLDHEIGHT / 2) - 4);
        openingMenuDrawFrame("Quit Game (Q)", (WORLDHEIGHT / 2) - 6);
        StdDraw.show();
        //now for the option picked
        boolean currentlyTrue = true;
        String testAsString = null;
        //get the typed key to compare to the menu options
        while (currentlyTrue) {
            if (StdDraw.hasNextKeyTyped()) {
                char test = StdDraw.nextKeyTyped();
                testAsString = Character.toString(test);
                currentlyTrue = false;
            }
        }
        if (testAsString.equals("N") || testAsString.equals("n")) {
            startTheName();
            createNewSeedNumber();
            startTheGame();
        } else if (testAsString.equals("L") || testAsString.equals("l")) {
            //For load I need to generate the world with the seed number that I got from the last new generate world
            //this means I need to save the seed number and put that seed number into the inputString method which
            // generates the world in the image of the seed number and pass it through the engine
            readingHelper();
            loadWorld(finalSeedNumberLoad);
            enterKeysLoading();
        } else if (testAsString.equals("Q") || testAsString.equals("q")) {
            System.exit(0);
        }
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
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    //subumbrella method that builds world based upon input however doesn't allow Avatar movement

    public TETile[][] interactWithInputString(String input) {
        // : Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        TETile[][] finalWorldFrame = world;
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < WORLDHEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        boolean checkSeed = false;
        boolean seedInitialized = false;
        boolean quit = false;
        boolean currentLoadStatus = false;
        boolean sLastChecker = true;
        String seedString = "";
        String newInput = input;
        String checker = "";
        if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            readingHelper();
            int fSeedNumberLength = finalSeedNumberLoad.length();
            if (finalSeedNumberLoad.substring(fSeedNumberLength - 1, fSeedNumberLength).equals(":")) {
                checker = finalSeedNumberLoad.substring(0, fSeedNumberLength);
            } else {
                checker = finalSeedNumberLoad.substring(0, fSeedNumberLength - 2);
            }
            newInput = (checker + input.substring(1));
            currentLoadStatus = true;
        }
        for (int i = 0; i < newInput.length(); i++) {
            char currentChar = newInput.charAt(i);
            // check if first char is load or new
            // right now, we are able to make new world with new seed
            if (i == 0) {
                if (currentChar == 'n' || currentChar == 'N') {
                    checkSeed = true;
                    if (currentLoadStatus) {
                        checkSeed = true;
                    }
                } else {
                    System.out.println("Error in input string: First letter is not N or L.");
                }
            } else if ((checkSeed && currentChar == 'S' || checkSeed && currentChar == 's') && sLastChecker) {
                checkSeed = false;
                seedNumber = Long.parseLong(seedString);
                rand = new Random(seedNumber);
                seedInitialized = true;
                makeWorld();
                Avatar.spawn();
                sLastChecker = false;
            } else if (i == newInput.length() - 1 && !sLastChecker && (currentChar == 'S' || currentChar == 's')) {
                loadHelper2();
            } else if (!seedInitialized && checkSeed) {
                if (Character.isDigit(newInput.charAt(i))) {
                    seedString = seedString + newInput.charAt(i);
                } else {
                    System.out.println("Seed string doesn't end with s");
                }
            } else if (currentChar == ':') {
                quit = true;
            } else if ((currentChar == 'Q' || currentChar == 'q')  && quit) {
                loadHelper2();
            } else if (seedInitialized && currentChar != ':') {
                updateWorld(String.valueOf(newInput.charAt(i)));
            }
        }
        ter.initialize(WIDTH, WORLDHEIGHT);
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }


    //All Helper Methods
    public static void HUDFrame() {
        for (int row = 0; row < HUDDimensions; row++) {
            for (int col = 2; col < 18; col++) {
                String empty = " ";
                char emptyVal = empty.charAt(0);
                world[col][availHEIGHT + row] = new TETile(emptyVal, StdDraw.BLACK, StdDraw.LIGHT_GRAY, "HUD");
            }
        }
    }
    public static void HUDWriter(String input, int height) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(10, height, input);
        StdDraw.show();
    }
    public static void clearSpecificArea(double x, double y, double width, double height) {
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        StdDraw.filledRectangle(x, y, width, height);
    }
    //Essentially a copy of inputString however allows movement for the load method in Keyboard
    public void loadWorld(String input) {
        TETile[][] finalWorldFrame = world;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < WORLDHEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        boolean checkSeed = false;
        boolean seedInitialized = false;
        String seedStr = "";
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            // check if first char is load or new
            // right now, we are able to make new world with new seed
            //Need Load to be completed
            if (i == 0) {
                if (currentChar == 'n' || currentChar == 'N') {
                    checkSeed = true;
                } else {
                    System.out.println("Error in input string: First letter is not N or L.");
                }
            } else if (checkSeed && currentChar == 'S' || checkSeed && currentChar == 's') {
                checkSeed = false;
                seedNumber = Long.parseLong(seedStr);
                rand = new Random(seedNumber);
                seedInitialized = true;
                makeWorld();
                Avatar.spawn();
            } else if (!seedInitialized && checkSeed) {
                if (Character.isDigit(input.charAt(i))) {
                    seedStr = seedStr + input.charAt(i);
                } else {
                    System.out.println("Seed string doesn't end with s");
                }
            } else if (seedInitialized) {
                updateWorld(String.valueOf(input.charAt(i)));
            }
        }
        ter.initialize(WIDTH, WORLDHEIGHT);
        ter.renderFrame(finalWorldFrame);
    }
    public void clearScreenBlack() {
        StdDraw.setCanvasSize(WIDTH * (9 + 1), WORLDHEIGHT * (9 + 1));
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, WORLDHEIGHT);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
    }

    //Sets the opening menu
    public void openingMenuDrawFrame(String inputs, int height) {
        //refrenced lab BYOW intro
        StdDraw.setPenColor(Color.WHITE);
        //location of text
        StdDraw.text(WIDTH / 2, height, inputs);
        StdDraw.show();
    }
    //https://stackoverflow.com/questions/12575990/calendar-date-to-yyyy-mm-dd-format-in-java
    public static void currentDate() {
        d = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        stringDate = dateFormat.format(d);
    }
    //creates the seed number and inputs it into static variables
    public void createNewSeedNumber() {
        clearScreenBlack();
        openingMenuDrawFrame("Enter your seed number: ", (WORLDHEIGHT / 2));
        boolean currentlyTrue = true;
        String seedCharStr = "";

        while (currentlyTrue) {
            if (StdDraw.hasNextKeyTyped()) {
                char seedChar = StdDraw.nextKeyTyped();
                if (Character.isDigit(seedChar)) {
                    seedCharStr = seedCharStr + seedChar;
                    clearScreenBlack();
                    openingMenuDrawFrame(seedCharStr, (WORLDHEIGHT / 2 - 2));
                } else if (Character.toString(seedChar).equals("s") || Character.toString(seedChar).equals("S")) {
                    currentlyTrue = false;
                    seedNumber = Long.parseLong(seedCharStr);
                    rand = new Random(seedNumber);
                } else {
                    clearScreenBlack();
                    String formattedString = String.format("Invalid input you dummy:%c", seedChar);
                    openingMenuDrawFrame(formattedString, (WORLDHEIGHT / 2 - 4));
                }
            }
        }
    }
    //starts the game for new game menu
    public void startTheGame() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < WORLDHEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        makeWorld();
        Avatar.spawn();
        clearSpecificArea(10, WORLDHEIGHT - 3, 6, 2);
        ter.initialize(WIDTH, WORLDHEIGHT);
        ter.renderFrame(world);
        boolean currentlyTrue = true;
        //continuing the game
        while (currentlyTrue) {
            if (StdDraw.hasNextKeyTyped()) {
                char test = StdDraw.nextKeyTyped();
                String testAsString = Character.toString(test);
                //need to make a method that updates the world
                updateWorld(testAsString);
                ter.renderFrame(world);
            }
            HUDWriter(name, WORLDHEIGHT - 2);
            currentDate();
            HUDWriter(stringDate, WORLDHEIGHT - 3);
            tileFinder();
            clearSpecificArea(10, WORLDHEIGHT - 3, 6, 2);
            StdDraw.pause(10);
        }
        System.exit(0);
    }
    public static void tileFinder() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        TETile test = world[mouseX][mouseY];
        currentTile = test.description();
        HUDWriter("Current Tile: " + currentTile, WORLDHEIGHT - 4);

    }
    //sets the name after seed instantiation
    //currently doesn't support updating name onto page
    public void startTheName() {
        clearScreenBlack();
        openingMenuDrawFrame("Enter your Name: ", (WORLDHEIGHT / 2));
        openingMenuDrawFrame("press 1 when completed", (WORLDHEIGHT / 2 - 2));
        boolean currentlyTrue = true;
        String totalCharStr = "";
        while (currentlyTrue) {
            if (StdDraw.hasNextKeyTyped()) {
                char currentChar = StdDraw.nextKeyTyped();
                if (Character.isLetter(currentChar)) {
                    String currentString = Character.toString(currentChar);
                    totalCharStr = totalCharStr + currentString;
                    clearScreenBlack();
                    openingMenuDrawFrame(totalCharStr, (WORLDHEIGHT / 2 - 4));
                } else if ((Character.isDigit(currentChar)) && currentChar == '1') {
                    currentlyTrue = false;
                    name = totalCharStr;
                    loadHelperName();
                }
            }
        }
    }
    //method that checks keys and inputs them into uodateworld
    public void enterKeysLoading() {
        boolean currentlyTrue = true;
        //continuing the game
        while (currentlyTrue) {
            if (StdDraw.hasNextKeyTyped()) {
                char test = StdDraw.nextKeyTyped();
                String testAsString = Character.toString(test);
                //need to make a method that updates the world
                updateWorld(testAsString);
            }
        }
    }
    public void makeAllRoomLight() {
        // I need to change a random spot in a room tile's into a different color
        Random roomRandom = new Random();
        int go = 5;
        while (go > 0) {
            int lightX = roomRandom.nextInt(WIDTH);
            int lightY = roomRandom.nextInt(availHEIGHT);
            if (world[lightX][lightY] == Tileset.FLOOR) {
                Light newlight = new Light(lightX, lightY);
                newlight.turnOn();
                //lightArray[go - 1] = newlight;
                go --;
            }
        }
    }

    //method that moves the avatar and simultaneously checks the inputs
    //method that moves the avatar and simultaneously checks the inputs
    public void updateWorld(String tester) {
        //essentially changing the position of the avatar
        //tester is either W(up), A(left), S(down), D(right)
        if (tester.equals("D") || tester.equals("d")) {
            //condition to check if moving will go out of bounds
            if (world[avatarXPostion + 1][avatarYPostion] == Tileset.LIGHTCENTER) {
                Avatar.lightMoveCenterEnter(avatarXPostion + 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "D";
                enterLight = true;
            } else if (world[avatarXPostion + 1][avatarYPostion] == Tileset.LIGHTRAD1 && enterLight) {
                Avatar.lightMoveCenterExit(avatarXPostion + 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "D";
                enterLight = false;
            } else if (world[avatarXPostion + 1][avatarYPostion] == Tileset.LIGHTRAD1 && !enterLightRadHorizonal) {
                Avatar.lightMoveRadEnter(avatarXPostion + 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "D";
                enterLightRadHorizonal = true;
            } else if (world[avatarXPostion + 1][avatarYPostion] == Tileset.FLOOR && enterLightRadHorizonal) {
                Avatar.lightMoveRadExit(avatarXPostion + 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "D";
                enterLightRadHorizonal = false;
            } else if (world[avatarXPostion + 1][avatarYPostion] == Tileset.FLOOR && !enterLightRadHorizonal) {
                Avatar.move(avatarXPostion + 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "D";
            } else if (world[avatarXPostion + 1][avatarYPostion] == Tileset.LIGHTRAD1 && enterLightRadHorizonal) {
                Avatar.lightMoveRadSame(avatarXPostion + 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "D";
            }
        } else if (tester.equals("S") || tester.equals("s")) {
            if (world[avatarXPostion][avatarYPostion - 1] == Tileset.LIGHTCENTER) {
                Avatar.lightMoveCenterEnter(avatarXPostion, avatarYPostion - 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "S";
                enterLight = true;
            } else if (world[avatarXPostion][avatarYPostion - 1] == Tileset.LIGHTRAD1 && enterLight) {
                Avatar.lightMoveCenterExit(avatarXPostion, avatarYPostion - 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "S";
                enterLight = false;
            } else if (world[avatarXPostion][avatarYPostion - 1] == Tileset.LIGHTRAD1 && !enterLightRadHorizonal) {
                Avatar.lightMoveRadEnter(avatarXPostion, avatarYPostion - 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "S";
                enterLightRadHorizonal = true;
            } else if (world[avatarXPostion][avatarYPostion - 1] == Tileset.FLOOR && enterLightRadHorizonal) {
                Avatar.lightMoveRadExit(avatarXPostion, avatarYPostion - 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "S";
                enterLightRadHorizonal = false;
            } else if (world[avatarXPostion][avatarYPostion - 1] == Tileset.FLOOR && !enterLightRadHorizonal) {
                Avatar.move(avatarXPostion, avatarYPostion - 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "S";
            } else if (world[avatarXPostion][avatarYPostion - 1] == Tileset.LIGHTRAD1 && enterLightRadHorizonal) {
                Avatar.lightMoveRadSame(avatarXPostion, avatarYPostion - 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "S";
            }
        } else if (tester.equals("W") || tester.equals("w")) {
            if (world[avatarXPostion][avatarYPostion + 1] == Tileset.LIGHTCENTER) {
                Avatar.lightMoveCenterEnter(avatarXPostion, avatarYPostion + 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "W";
                enterLight = true;
            } else if (world[avatarXPostion][avatarYPostion + 1] == Tileset.LIGHTRAD1 && enterLight) {
                Avatar.lightMoveCenterExit(avatarXPostion, avatarYPostion + 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "W";
                enterLight = false;
            } else if (world[avatarXPostion][avatarYPostion + 1] == Tileset.LIGHTRAD1 && !enterLightRadHorizonal) {
                Avatar.lightMoveRadEnter(avatarXPostion, avatarYPostion + 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "W";
                enterLightRadHorizonal = true;
            } else if (world[avatarXPostion][avatarYPostion + 1] == Tileset.FLOOR && enterLightRadHorizonal) {
                Avatar.lightMoveRadExit(avatarXPostion, avatarYPostion + 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "W";
                enterLightRadHorizonal = false;
            } else if (world[avatarXPostion][avatarYPostion + 1] == Tileset.FLOOR && !enterLightRadHorizonal) {
                Avatar.move(avatarXPostion, avatarYPostion + 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "W";
            } else if (world[avatarXPostion][avatarYPostion + 1] == Tileset.LIGHTRAD1 && enterLightRadHorizonal) {
                Avatar.lightMoveRadSame(avatarXPostion, avatarYPostion + 1);
                seedNumberLoadMovements = seedNumberLoadMovements + "W";
            }
        } else if (tester.equals("A") || tester.equals("a")) {
            if (world[avatarXPostion - 1][avatarYPostion] == Tileset.LIGHTCENTER) {
                Avatar.lightMoveCenterEnter(avatarXPostion - 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "A";
                enterLight = true;
            } else if (world[avatarXPostion - 1][avatarYPostion] == Tileset.LIGHTRAD1 && enterLight) {
                Avatar.lightMoveCenterExit(avatarXPostion - 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "A";
                enterLight = false;
            } else if (world[avatarXPostion - 1][avatarYPostion] == Tileset.LIGHTRAD1 && !enterLightRadHorizonal) {
                Avatar.lightMoveRadEnter(avatarXPostion - 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "A";
                enterLightRadHorizonal = true;
            } else if (world[avatarXPostion - 1][avatarYPostion] == Tileset.FLOOR && enterLightRadHorizonal) {
                Avatar.lightMoveRadExit(avatarXPostion - 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "A";
                enterLightRadHorizonal = false;
            } else if (world[avatarXPostion - 1][avatarYPostion] == Tileset.FLOOR && !enterLightRadHorizonal) {
                Avatar.move(avatarXPostion - 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "A";
            } else if (world[avatarXPostion - 1][avatarYPostion] == Tileset.LIGHTRAD1 && enterLightRadHorizonal) {
                Avatar.lightMoveRadSame(avatarXPostion - 1, avatarYPostion);
                seedNumberLoadMovements = seedNumberLoadMovements + "A";
            }
            //checks if :q
            //maybe need to check if not one of the options
        } else if (tester.equals(":")) {
            boolean currentlyTrue = true;
            while (currentlyTrue) {
                if (StdDraw.hasNextKeyTyped()) {
                    char test = StdDraw.nextKeyTyped();
                    String testAsString = Character.toString(test);
                    if (testAsString.equals("q")) {
                        loadHelper();
                        System.exit(0);
                    }
                    currentlyTrue = false;
                }
            }
        }
    }
    public void loadHelper2() {
        File newFile = new File("SeedNumberAndLocation.txt");
        String seedNumberLoad = "N" + seedNumber + "s" + seedNumberLoadMovements + ":q";
        try (PrintWriter save = new PrintWriter("SeedNumberAndLocation.txt")) {
            save.println(seedNumberLoad);
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //https://stackoverflow.com/questions/4716503/-a-plain-text-file-in-java
    // creates the txt file that saves the seed number
    public void loadHelper() {
        File newFile = new File("SeedNumberAndLocation.txt");
        String seedNumberLoad = "N" + seedNumber + "s" + seedNumberLoadMovements;
        try (PrintWriter save = new PrintWriter("SeedNumberAndLocation.txt")) {
            save.println(seedNumberLoad);
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadHelperName() {
        File newFile = new File("Name.txt");
        String seedNumberLoad =  name;
        try (PrintWriter save = new PrintWriter("Name.txt")) {
            save.println(seedNumberLoad);
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //https://stackoverflow.com/questions/1053467/how-do-i-save-a-string-to-a-text-file-using-java
    //allows the txt file to be read as a string
    public void readingHelper() {
        String fileName = "SeedNumberAndLocation.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            finalSeedNumberLoad = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Methods below actually create the world
    //puts together all the methods to create the world
    public void makeWorld() {
        partitions = rand.nextInt(minPartitions, maxPartitions);
        xDimensions = WIDTH / partitions;
        yDimensions = availHEIGHT / partitions;
        roomList = new Room[partitions][partitions];
        makeAllRooms();
        makeHallways();
        cleanUp();
        makeAllRoomLight();
    }
    //creates all the rooms randomly in the partitions
    public void makeAllRooms() {
        for (int rowPartition = 0; rowPartition < partitions; rowPartition++) {
            for (int colPartition = 0; colPartition < partitions; colPartition++) {
                int xOrigin = colPartition * xDimensions + 5;
                int xBound = (colPartition + 1) * xDimensions - 5;
                int yOrigin = rowPartition * yDimensions + 5;
                int yBound = (rowPartition + 1) * yDimensions - 5;

                int x = rand.nextInt(xOrigin, xBound);
                int y = rand.nextInt(yOrigin, yBound);

                int roomWidth = rand.nextInt(5, xDimensions - x + (colPartition * xDimensions));
                int roomHeight = rand.nextInt(5, yDimensions - y + (rowPartition * yDimensions));
                Room save = Room.makeRoom(x, y, roomWidth, roomHeight);
                roomList[rowPartition][colPartition] = save;
            }
        }
    }
    //creates the hallways to connect to the randomly created rooms
    //uses the X and Y Hallways
    public void makeHallways() {
        //if rooms are at most 4 apart, make corresponding hallway
        for (int col = 0; col < partitions - 1; col++) {
            for (int row = 0; row < partitions; row++) {
                Room currentRoom = roomList[row][col];
                Room rightRoom = roomList[row][col + 1];

                if (col == 0) {
                    if (row + 1 != partitions) {
                        Room topRoom = roomList[row + 1][col];
                        makeYHallPath(currentRoom, topRoom);
                        makeXHallPath(currentRoom, rightRoom);
                    } else {
                        makeXHallPath(currentRoom, rightRoom);
                    }
                } else {
                    makeXHallPath(currentRoom, rightRoom);
                }
            }
        }
    }
    public void makeXHallway(int xOrigin, int yOrigin, int length, int hallWidth) {
        for (int x = 0; x < length; x++) {
            for (int wide = 0; wide < hallWidth; wide++) {
                if (xOrigin + x >= WIDTH || yOrigin + wide >= availHEIGHT) {
                    break;
                }
                if (wide == 0 || wide == hallWidth - 1) {
                    world[xOrigin + x][yOrigin + wide] = Tileset.WALL;
                } else {
                    world[xOrigin + x][yOrigin + wide] = Tileset.FLOOR;
                }
            }
        }
    }
    public void makeYHallway(int xOrigin, int yOrigin, int height, int hallWidth) {
        for (int y = 0; y < height; y++) {
            for (int wide = 0; wide < hallWidth; wide++) {
                if (xOrigin + wide >= WIDTH || yOrigin + y >= availHEIGHT) {
                    break;
                }
                if (wide == 0 || wide == hallWidth - 1) {
                    world[xOrigin + wide][yOrigin + y] = Tileset.WALL;
                } else {
                    world[xOrigin + wide][yOrigin + y] = Tileset.FLOOR;
                }
            }
        }
    }
    public void makeXHallPath(Room leftRoom, Room rightRoom) {
        int leftX = leftRoom.x + leftRoom.roomWidth;
        int leftY = rand.nextInt(leftRoom.y + 1, leftRoom.y + leftRoom.roomHeight - 3);
        int rightX = rightRoom.x;
        int rightY = rand.nextInt(rightRoom.y + 1, rightRoom.y + rightRoom.roomHeight - 3);

        int hallLength = Math.abs(rightX - leftX);
        if (hallLength <= 4) {
            return;
        }
        int hallHeight = Math.abs(rightY - leftY);
        int firstXHallLength = rand.nextInt(4, hallLength);
        int secondXHallLength = hallLength - firstXHallLength;
        int hallWidth = rand.nextInt(3, 5);
        int hallwayStart = leftX + firstXHallLength;

        //build firstXHallway
        makeXHallway(leftX, leftY, firstXHallLength, hallWidth);
        //build secondXHallway
        makeXHallway(hallwayStart, rightY, secondXHallLength, hallWidth);
        //build yHallway down
        if (leftY > rightY) {
            if (hallwayStart - 3 > leftX) {
                makeYHallway(hallwayStart - 3, rightY, hallHeight, hallWidth);
            }
            //build yHallway up
        } else if (rightY > leftY) {
            if (hallwayStart - 3 > leftX) {
                makeYHallway(hallwayStart - 3, leftY, hallHeight + hallWidth, hallWidth);
            }
        }
    }
    public void makeYHallPath(Room botRoom, Room topRoom) {
        int hallWidth = rand.nextInt(3, 5);
        int botX = rand.nextInt(botRoom.x, botRoom.x + botRoom.roomWidth - hallWidth);
        int botY = botRoom.y + botRoom.roomHeight;
        int topX = rand.nextInt(topRoom.x, topRoom.x + topRoom.roomWidth - hallWidth);
        int topY = topRoom.y;

        int hallHeight = Math.abs(topY - botY);
        if (hallHeight == 0) {
            return;
        }
        int hallLength = Math.abs(botX - topX);
        int firstYHallHeight = rand.nextInt(2, hallHeight);
        int secondYHallHeight = hallHeight - firstYHallHeight;
        int hallwayStart = botY + firstYHallHeight;

        //build first Y hallway
        makeYHallway(botX, botY, firstYHallHeight, hallWidth);
        //build second Y Hallway
        makeYHallway(topX, hallwayStart, secondYHallHeight, hallWidth);
        //build leftXHallway
        if (botX > topX) {
            makeXHallway(topX, hallwayStart - hallWidth, hallLength, hallWidth);
            //build rightXHallway
        } else if (topX > botX) {
            makeXHallway(botX, hallwayStart, hallLength, hallWidth);
        }
    }
    public void cleanUp() {
        //scan all tiles
        for (int col = 0; col < WIDTH - 1; col++) {
            for (int row = 0; row < availHEIGHT - 1; row++) {
                TETile test = world[col][row];
                if (test == Tileset.FLOOR) {
                    //if floor has null, change into wall
                    cleanUpFloor(col, row);
                } else if (test == Tileset.WALL) {
                    //if wall doesn't have null, change into floor
                    cleanUpWall(col, row);
                }
            }
        }
    }
    public void cleanUpFloor(int col, int row) {
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                if (xOffset == 0 && yOffset == 0) {
                    continue;
                } else if (world[col + yOffset][row + xOffset] == Tileset.NOTHING) {
                    world[col][row] = Tileset.WALL;
                    break;
                }
            }
        }
    }
    public void cleanUpWall(int col, int row) {
        int nullCount = 0;
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                if (xOffset == 0 && yOffset == 0) {
                    continue;
                } else if (world[col + yOffset][row + xOffset] == Tileset.NOTHING) {
                    nullCount++;
                }
            }
        }
        if (nullCount == 0) {
            world[col][row] = Tileset.FLOOR;
        }
    }

    public static class Room {
        private int x;
        private int y;
        private int roomWidth;
        private int roomHeight;

        public Room(int x, int y, int roomWidth, int roomHeight) {
            this.x = x;
            this.y = y;
            this.roomWidth = roomWidth;
            this.roomHeight = roomHeight;
        }
        public static Room makeRoom(int x, int y, int roomWidth, int roomHeight) {
            for (int row = 0; row < roomWidth; row++) {
                for (int col = 0; col < roomHeight; col++) {
                    if (x + row >= WIDTH || y + col >= availHEIGHT) {
                        break;
                    }
                    world[x + row][y + col] = Tileset.WALL;
                }
            }
            for (int row = 1; row < roomWidth - 1; row++) {
                for (int col = 1; col <= roomHeight - 2; col++) {
                    if (x + row >= WIDTH - 1 || y + col >= availHEIGHT - 1) {
                        break;
                    }
                    world[x + row][y + col] = Tileset.FLOOR;
                }
            }
            return new Room(x, y, roomWidth, roomHeight);
        }
    }
}


