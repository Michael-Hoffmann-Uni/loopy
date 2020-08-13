package jpp.infinityloop.logic;

import java.util.concurrent.ThreadLocalRandom;

public class Generator {
    int minWidth;
    int maxWidth;
    int minHeight;
    int maxHeight;

    public Generator(int minWidth, int maxWidth, int minHeight, int maxHeight) {
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public Board generate() {
        int width = ThreadLocalRandom.current().nextInt(minWidth, maxWidth + 1);
        int height = ThreadLocalRandom.current().nextInt(minHeight, maxHeight + 1);
        Tile[][] tiles = new Tile[height][width];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles.length == 1 && tiles[i].length == 1) {
                    //just one tile
                    tiles[i][j] = new Tile(Orient.ALL, TileType.EMPTY);
                } else if (tiles.length == 1 && tiles[i].length > 1) {
                    //just one row
                    if (j == 0) {
                        // first tile right/deadend, all/empty,
                        tiles[i][j] = oneRowA();
                    } else if (j == tiles[i].length - 1) {
                        //last tile if(left has con) -> left/deadend, else -> all/empty
                        if (tiles[i][j - 1].hasRightCon()) {
                            tiles[i][j] = new Tile(Orient.LEFT, TileType.DEAD_END);
                        } else {
                            tiles[i][j] = new Tile(Orient.ALL, TileType.EMPTY);
                        }
                    } else {
                        //other tiles
                        //if(left has Con) -> left/deadend or left/straight, else -> all/empty or right/deadend
                        if (tiles[i][j - 1].hasRightCon()) {
                            tiles[i][j] = oneRowB();
                        } else {
                            tiles[i][j] = oneRowC();
                        }
                    }
                } else if (tiles[i].length == 1 && tiles.length > 1) {
                    //just one col ...
                    if (i == 0) {
                        //first tile all/empty, down/deadend
                        tiles[i][j] = oneColA();
                    } else if (i == tiles.length - 1) {
                        //last tile if(upper has con) -> up/deadend, else -> all/empty
                        if (tiles[i - 1][j].hasDownCon()) {
                            tiles[i][j] = new Tile(Orient.UP, TileType.DEAD_END);
                        } else {
                            tiles[i][j] = new Tile(Orient.ALL, TileType.EMPTY);
                        }
                    } else {
                        //other tiles
                        //if(upper has con) -> up/straight or up/deadend, else -> down/deadend or all/empty
                        if (tiles[i - 1][j].hasDownCon()) {
                            tiles[i][j] = oneColB();
                        } else {
                            tiles[i][j] = oneColC();
                        }
                    }
                } else {
                    //Upper left Corner
                    //empty/all (1), bend/right(2), deadend/right(3), deadend/down(4)
                    if (i == 0 && j == 0)
                        tiles[i][j] = ulCorner();

                    //top edge
                    //fulfill requirement from left and respect top edge
                    if (i == 0 && j != 0 && j != tiles[i].length - 1) {
                        if (tiles[i][j - 1].hasRightCon()) {
                            //right/tee(1), down/bend(2), right/straight(3), left/deadend(4)
                            tiles[i][j] = topEdge();
                        } else {
                            //all/empty(1), down/deadend(2), right/deadend(3), right/bend(4)
                            tiles[i][j] = ulCorner();
                        }
                    }

                    //Upper right Corner
                    //fulfill requirement from left and respect top edge and right edge
                    if (i == 0 && j == tiles[i].length - 1 && j != 0) {
                        if (tiles[i][j - 1].hasRightCon()) {
                            //left/deadend(1), down/bend(2)
                            tiles[i][j] = urCornerA();
                        } else {
                            //all/empty(1), down/deadend(2)
                            tiles[i][j] = urCornerB();
                        }
                    }

                    //Left edge
                    //fulfill requirement from top and respect left edge
                    if (j == 0 && i != 0 && i != tiles.length - 1) {
                        if (tiles[i - 1][j].hasDownCon()) {
                            //up/deadend(1), up/straight(2), up/bend(3), up/tee(4)
                            tiles[i][j] = lEdge();
                        } else {
                            //same as upper left corner
                            tiles[i][j] = ulCorner();

                        }
                    }

                    //Right edge
                    //fulfill requirements from left and top and respect right edge
                    if (j == tiles[i].length - 1 && i != 0 && i != tiles.length - 1) {
                        if (!tiles[i][j - 1].hasRightCon() && !tiles[i - 1][j].hasDownCon()) {
                            //all/empty(1), down/deadend(2)
                            tiles[i][j] = urCornerB();
                        } else if (tiles[i][j - 1].hasRightCon() && !tiles[i - 1][j].hasDownCon()) {
                            //left/deadend(1), down/bend(2)
                            tiles[i][j] = urCornerA();
                        } else if (tiles[i][j - 1].hasRightCon() && tiles[i - 1][j].hasDownCon()) {
                            //left/bend(1), down/tee(2)
                            tiles[i][j] = rEdgeA();
                        } else {
                            //up/deadend(1), up/straight(2)
                            tiles[i][j] = rEdgeB();
                        }
                    }

                    //Lower left corner
                    //fulfill requirements from top and respect left edge
                    if (j == 0 && i == tiles.length - 1) {
                        if (tiles[i - 1][j].hasDownCon()) {
                            //up/deadend (1), up/bend (2)
                            tiles[i][j] = llCornerA();
                        } else {
                            //all/empty (1), right/deadend (2)
                            tiles[i][j] = llCornerB();
                        }
                    }

                    //lower edge
                    //fulfill requirements from left and top and respect lower edge
                    if (i == tiles.length - 1 && j != 0 && j != tiles[i].length - 1) {
                        if (tiles[i][j - 1].hasRightCon() && tiles[i - 1][j].hasDownCon()) {
                            //left/bend(1), left/tee(2)
                            tiles[i][j] = lowerEdgeA();
                        } else if (!tiles[i][j - 1].hasRightCon() && !tiles[i - 1][j].hasDownCon()) {
                            //all/empty(1), right/deadend(2)
                            tiles[i][j] = llCornerB();
                        } else if (tiles[i][j - 1].hasRightCon() && !tiles[i - 1][j].hasDownCon()) {
                            //left/deadend(1), right/straight(2)
                            tiles[i][j] = lowerEdgeB();
                        } else {
                            //up/deadend(1), up/bend(2)
                            tiles[i][j] = llCornerA();
                        }
                    }

                    //lower right corner
                    //fulfill requiremets from left and top and respect lower and right edge
                    if (i == tiles.length - 1 && j == tiles[i].length - 1) {
                        if (tiles[i][j - 1].hasRightCon() && tiles[i - 1][j].hasDownCon()) {
                            //left/bend
                            tiles[i][j] = new Tile(Orient.LEFT, TileType.BEND);
                        } else if (!tiles[i][j - 1].hasRightCon() && !tiles[i - 1][j].hasDownCon()) {
                            //all/empty
                            tiles[i][j] = new Tile(Orient.ALL, TileType.EMPTY);
                        } else if (tiles[i][j - 1].hasRightCon() && !tiles[i - 1][j].hasDownCon()) {
                            //left/deadend
                            tiles[i][j] = new Tile(Orient.LEFT, TileType.DEAD_END);
                        } else {
                            //up/deadend
                            tiles[i][j] = new Tile(Orient.UP, TileType.DEAD_END);
                        }
                    }

                    //center
                    //fulfill requirements from left and top
                    if (i != 0 && j != 0 && i != tiles.length - 1 && j != tiles[i].length - 1) {
                        if (tiles[i][j - 1].hasRightCon() && tiles[i - 1][j].hasDownCon()) {
                            //left/bend(1), down/tee(2), left/tee(3), all/cross(4)
                            tiles[i][j] = centerA();
                        } else if (!tiles[i][j - 1].hasRightCon() && !tiles[i - 1][j].hasDownCon()) {
                            //all/empty(1), down/deadend(2), right/deadend(3), right/bend (4)
                            tiles[i][j] = ulCorner();
                        } else if (tiles[i][j - 1].hasRightCon() && !tiles[i - 1][j].hasDownCon()) {
                            //left/deadend(1), down/bend(2), left/straight(3), right/tee(4)
                            tiles[i][j] = centerB();
                        } else {
                            //up/deadend(1), up/straight(2), up/bend(3), up/tee(4)
                            tiles[i][j] = lEdge();
                        }
                    }
                }
            }
        }

        Board board = new Board(width, height, tiles);
        board.shuffle();
        return board;
    }

    private Tile ulCorner() {
        int roll;
        Tile tile = null;
        //Upper left Corner
        //empty/all (1), bend/right(2), deadend/right(3), deadend/down(4)
        roll = ThreadLocalRandom.current().nextInt(1, 5);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.ALL, TileType.EMPTY);
                break;
            case 2:
                tile = new Tile(Orient.RIGHT, TileType.BEND);
                break;
            case 3:
                tile = new Tile(Orient.RIGHT, TileType.DEAD_END);
                break;
            case 4:
                tile = new Tile(Orient.DOWN, TileType.DEAD_END);
                break;
        }
        return tile;
    }

    private Tile topEdge() {
        int roll;
        Tile tile = null;
        //top edge
        //right/tee(1), down/bend(2), right/straight(3), left/deadend(4)
        roll = ThreadLocalRandom.current().nextInt(1, 5);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.RIGHT, TileType.TEE);
                break;
            case 2:
                tile = new Tile(Orient.DOWN, TileType.BEND);
                break;
            case 3:
                tile = new Tile(Orient.RIGHT, TileType.STRAIGHT);
                break;
            case 4:
                tile = new Tile(Orient.LEFT, TileType.DEAD_END);
                break;
        }
        return tile;
    }

    private Tile urCornerA() {
        int roll;
        Tile tile = null;
        //Upper right Corner (when left tile has Con)
        //left/deadend(1), down/bend(2)
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.LEFT, TileType.DEAD_END);
                break;
            case 2:
                tile = new Tile(Orient.DOWN, TileType.BEND);
                break;
        }
        return tile;
    }

    private Tile urCornerB() {
        int roll;
        Tile tile = null;
        //Upper right Corner (when left tile has Con)
        //all/empty(1), down/deadend(2)
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.ALL, TileType.EMPTY);
                break;
            case 2:
                tile = new Tile(Orient.DOWN, TileType.DEAD_END);
                break;
        }
        return tile;
    }

    private Tile lEdge() {
        int roll;
        Tile tile = null;
        //left edge
        //up/deadend(1), up/straight(2), up/bend(3), up/tee(4)
        roll = ThreadLocalRandom.current().nextInt(1, 5);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.UP, TileType.DEAD_END);
                break;
            case 2:
                tile = new Tile(Orient.UP, TileType.STRAIGHT);
                break;
            case 3:
                tile = new Tile(Orient.UP, TileType.BEND);
                break;
            case 4:
                tile = new Tile(Orient.UP, TileType.TEE);
                break;
        }
        return tile;
    }

    private Tile rEdgeA() {
        int roll;
        Tile tile = null;
        //right edge (when left tile has Con, but top tile has no Con)
        //left/bend(1), down/tee(2)
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.LEFT, TileType.BEND);
                break;
            case 2:
                tile = new Tile(Orient.DOWN, TileType.TEE);
                break;
        }
        return tile;
    }

    private Tile rEdgeB() {
        int roll;
        Tile tile = null;
        //right edge (when left tile has no Con, but top tile has Con)
        //up/deadend(1), up/straight(2)
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.UP, TileType.DEAD_END);
                break;
            case 2:
                tile = new Tile(Orient.UP, TileType.STRAIGHT);
                break;
        }
        return tile;
    }

    private Tile llCornerA() {
        int roll;
        Tile tile = null;
        //left lower corner (when upper tile has Con)
        //up/deadend (1), up/bend (2)
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.UP, TileType.DEAD_END);
                break;
            case 2:
                tile = new Tile(Orient.UP, TileType.BEND);
                break;
        }
        return tile;
    }

    private Tile llCornerB() {
        int roll;
        Tile tile = null;
        //left lower corner (when upper tile has no Con)
        //all/empty (1), right/deadend (2)
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.ALL, TileType.EMPTY);
                break;
            case 2:
                tile = new Tile(Orient.RIGHT, TileType.DEAD_END);
                break;
        }
        return tile;
    }

    private Tile lowerEdgeA() {
        int roll;
        Tile tile = null;
        //lower edge (when left tile has Con and upper tile has Con)
        //left/bend(1), left/tee(2)
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.LEFT, TileType.BEND);
                break;
            case 2:
                tile = new Tile(Orient.LEFT, TileType.TEE);
                break;
        }
        return tile;
    }

    private Tile lowerEdgeB() {
        int roll;
        Tile tile = null;
        //lower edge (when left tile has Con and upper tile has no Con)
        //left/deadend(1), right/straight(2)
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.LEFT, TileType.DEAD_END);
                break;
            case 2:
                tile = new Tile(Orient.RIGHT, TileType.STRAIGHT);
                break;
        }
        return tile;
    }

    private Tile centerA() {
        int roll;
        Tile tile = null;
        //center (where left and top neighbours have Cons)
        //left/bend(1), down/tee(2), left/tee(3), all/cross(4)
        roll = ThreadLocalRandom.current().nextInt(1, 5);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.LEFT, TileType.BEND);
                break;
            case 2:
                tile = new Tile(Orient.DOWN, TileType.TEE);
                break;
            case 3:
                tile = new Tile(Orient.LEFT, TileType.TEE);
                break;
            case 4:
                tile = new Tile(Orient.ALL, TileType.CROSS);
                break;
        }
        return tile;
    }

    private Tile centerB() {
        int roll;
        Tile tile = null;
        //center (where left tile has Con and top tile has no Con)
        //left/deadend(1), down/bend(2), left/straight(3), right/tee(4)
        roll = ThreadLocalRandom.current().nextInt(1, 5);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.LEFT, TileType.DEAD_END);
                break;
            case 2:
                tile = new Tile(Orient.DOWN, TileType.BEND);
                break;
            case 3:
                tile = new Tile(Orient.LEFT, TileType.STRAIGHT);
                break;
            case 4:
                tile = new Tile(Orient.RIGHT, TileType.TEE);
                break;
        }
        return tile;
    }

    private Tile oneRowA() {
        int roll;
        Tile tile = null;
        //right/deadend(1), all/empty(2)
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.RIGHT, TileType.DEAD_END);
                break;
            case 2:
                tile = new Tile(Orient.ALL, TileType.EMPTY);
                break;
        }
        return tile;
    }

    private Tile oneRowB() {
        int roll;
        Tile tile = null;
        //left/deadend or left/straight
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.LEFT, TileType.DEAD_END);
                break;
            case 2:
                tile = new Tile(Orient.LEFT, TileType.STRAIGHT);
                break;
        }
        return tile;
    }

    private Tile oneRowC() {
        int roll;
        Tile tile = null;
        //all/empty or right/deadend
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.ALL, TileType.EMPTY);
                break;
            case 2:
                tile = new Tile(Orient.RIGHT, TileType.DEAD_END);
                break;
        }
        return tile;
    }

    private Tile oneColA() {
        int roll;
        Tile tile = null;
        //first tile all/empty, down/deadend
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.ALL, TileType.EMPTY);
                break;
            case 2:
                tile = new Tile(Orient.DOWN, TileType.DEAD_END);
                break;
        }
        return tile;
    }

    private Tile oneColB() {
        int roll;
        Tile tile = null;
        //up/straight or up/deadend
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.UP, TileType.STRAIGHT);
                break;
            case 2:
                tile = new Tile(Orient.UP, TileType.DEAD_END);
                break;
        }
        return tile;
    }

    private Tile oneColC() {
        int roll;
        Tile tile = null;
        //down/deadend or all/empty
        roll = ThreadLocalRandom.current().nextInt(1, 3);
        switch (roll) {
            case 1:
                tile = new Tile(Orient.ALL, TileType.EMPTY);
                break;
            case 2:
                tile = new Tile(Orient.DOWN, TileType.DEAD_END);
                break;
        }
        return tile;
    }
}
