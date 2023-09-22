package byow.Core;

import byow.TileEngine.Tileset;

public class Light extends Engine {
    int x;
    int y;
    public Light(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void turnOn() {
        world[x][y] = Tileset.LIGHTCENTER;
        if (world[x + 1][y] == Tileset.FLOOR) {
            world[x + 1][y] = Tileset.LIGHTRAD1;
        }
        if (world[x + 1][y + 1] == Tileset.FLOOR) {
            world[x + 1][y + 1] = Tileset.LIGHTRAD1;
        }
        if (world[x + 1][y - 1] == Tileset.FLOOR) {
            world[x + 1][y - 1] = Tileset.LIGHTRAD1;
        }
        if (world[x][y + 1] == Tileset.FLOOR) {
            world[x][y + 1] = Tileset.LIGHTRAD1;
        }
        if (world[x][y - 1] == Tileset.FLOOR) {
            world[x][y - 1] = Tileset.LIGHTRAD1;
        }
        if (world[x - 1][y + 1] == Tileset.FLOOR) {
            world[x - 1][y + 1] = Tileset.LIGHTRAD1;
        }
        if (world[x - 1][y - 1] == Tileset.FLOOR) {
            world[x - 1][y - 1] = Tileset.LIGHTRAD1;
        }
        if (world[x - 1][y] == Tileset.FLOOR) {
            world[x - 1][y] = Tileset.LIGHTRAD1;
        }
    }
    public void turnOff(int x, int y) {
        world[x][y] = Tileset.FLOOR;
        if (world[x + 1][y] == Tileset.LIGHTRAD1) {
            world[x + 1][y] = Tileset.FLOOR;
        }
        if (world[x + 1][y + 1] == Tileset.LIGHTRAD1) {
            world[x + 1][y + 1] = Tileset.FLOOR;
        }
        if (world[x + 1][y - 1] == Tileset.LIGHTRAD1) {
            world[x + 1][y - 1] = Tileset.FLOOR;
        }
        if (world[x][y + 1] == Tileset.LIGHTRAD1) {
            world[x][y + 1] = Tileset.FLOOR;
        }
        if (world[x][y - 1] == Tileset.LIGHTRAD1) {
            world[x][y - 1] = Tileset.FLOOR;
        }
        if (world[x - 1][y + 1] == Tileset.LIGHTRAD1) {
            world[x - 1][y + 1] = Tileset.FLOOR;
        }
        if (world[x - 1][y - 1] == Tileset.LIGHTRAD1) {
            world[x - 1][y - 1] = Tileset.FLOOR;
        }
        if (world[x - 1][y] == Tileset.LIGHTRAD1) {
            world[x - 1][y] = Tileset.FLOOR;
        }
    }

}
