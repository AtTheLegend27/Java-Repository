package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avatar extends Engine {
    //instantiates a new Avatar at location (0,0)
    protected static int x;
    protected static int y;
    protected TETile tileset;
    protected String name;

    public Avatar(int x, int y, TETile tileset, String name) {
        this.x = x;
        this.y = y;
        this.tileset = tileset;
        this.name = name;
    }

    public static void spawn() {
        boolean go = true;
        for (int row = 0; row < WIDTH && go; row++) {
            for (int col = 0; col < availHEIGHT && go; col++) {
                if (world[row][col] == Tileset.FLOOR) {
                    avatarXPostion = row;
                    avatarYPostion = col;
                    go = false;
                }
            }
        }
        world[avatarXPostion][avatarYPostion] = Tileset.AVATAR;
    }
    public static void move(int newX, int newY) {
        world[avatarXPostion][avatarYPostion] = Tileset.FLOOR;
        world[newX][newY] = Tileset.AVATAR;
        avatarXPostion = newX;
        avatarYPostion = newY;
    }
    public static void lightMoveCenterEnter(int newX, int newY) {
        world[avatarXPostion][avatarYPostion] = Tileset.LIGHTRAD1;
        world[newX][newY] = Tileset.AVATAR;
        avatarXPostion = newX;
        avatarYPostion = newY;
    }
    public static void lightMoveCenterExit(int newX, int newY) {
        world[avatarXPostion][avatarYPostion] = Tileset.LIGHTCENTER;
        world[newX][newY] = Tileset.AVATAR;
        avatarXPostion = newX;
        avatarYPostion = newY;
    }
    public static void lightMoveRadExit(int newX, int newY) {
        world[avatarXPostion][avatarYPostion] = Tileset.LIGHTRAD1;
        world[newX][newY] = Tileset.AVATAR;
        avatarXPostion = newX;
        avatarYPostion = newY;
    }
    public static void lightMoveRadEnter(int newX, int newY) {
        world[avatarXPostion][avatarYPostion] = Tileset.FLOOR;
        world[newX][newY] = Tileset.AVATAR;
        avatarXPostion = newX;
        avatarYPostion = newY;
    }
    public static void lightMoveRadSame(int newX, int newY) {
        world[avatarXPostion][avatarYPostion] = Tileset.LIGHTRAD1;
        world[newX][newY] = Tileset.AVATAR;
        avatarXPostion = newX;
        avatarYPostion = newY;
    }

}



