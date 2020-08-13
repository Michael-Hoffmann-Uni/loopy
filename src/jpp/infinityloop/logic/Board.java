package jpp.infinityloop.logic;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Board implements Cloneable {
    private int width;
    private int height;
    private Tile[][] tiles;
    private int openCons;

    public Board(int width, int height, Tile[][] tiles) {
        this.width = width;
        this.height = height;
        this.tiles = tiles;
        this.openCons = calcOpenCons();
    }

    public void rotate(int x, int y) {
        int preRot = 0;
        int postRot = 0;
        Tile cTile = this.tiles[y][x];

        //count connections pre-rotation
        if (cTile.hasUpCon()) {
            if (y == 0) {
                preRot++;
            } else if (!tiles[y - 1][x].hasDownCon()) {
                preRot++;
            }
        }
        if (cTile.hasRightCon()) {
            if (x == tiles[y].length - 1) {
                preRot++;
            } else if (!tiles[y][x + 1].hasLeftCon()) {
                preRot++;
            }
        }
        if (cTile.hasDownCon()) {
            if (y == tiles.length - 1) {
                preRot++;
            } else if (!tiles[y + 1][x].hasUpCon()) {
                preRot++;
            }
        }
        if (cTile.hasLeftCon()) {
            if (x == 0) {
                preRot++;
            } else if (!tiles[y][x - 1].hasRightCon()) {
                preRot++;
            }
        }

        //rotate
        this.tiles[y][x].rotate();
        cTile = this.tiles[y][x];

        //count connections post-rotation
        if (cTile.hasUpCon()) {
            if (y == 0) {
                postRot++;
            } else if (!tiles[y - 1][x].hasDownCon()) {
                postRot++;
            }
        }
        if (cTile.hasRightCon()) {
            if (x == tiles[y].length - 1) {
                postRot++;
            } else if (!tiles[y][x + 1].hasLeftCon()) {
                postRot++;
            }
        }
        if (cTile.hasDownCon()) {
            if (y == tiles.length - 1) {
                postRot++;
            } else if (!tiles[y + 1][x].hasUpCon()) {
                postRot++;
            }
        }
        if (cTile.hasLeftCon()) {
            if (x == 0) {
                postRot++;
            } else if (!tiles[y][x - 1].hasRightCon()) {
                postRot++;
            }
        }

        //calculate difference
        postRot = postRot - preRot;
        this.openCons = openCons + 2 * postRot;
    }

    public void shuffle() {
        int iRot = 0;
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[0].length; j++) {
                iRot = ThreadLocalRandom.current().nextInt(0, 4);
                for (int k = 0; k < iRot; k++) {
                    this.tiles[i][j].rotate();
                }
            }
        }
        this.openCons = calcOpenCons();
    }

    public boolean checkSolved() {
        if (this.openCons == 0) {
            return true;
        } else {
            return false;
        }
    }

    private int calcOpenCons() {
        Tile cTile;
        int count = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                cTile = tiles[i][j];
                if (cTile.hasUpCon()) {
                    if (i == 0) {
                        count++;
                    } else if (!tiles[i - 1][j].hasDownCon()) {
                        count++;
                    }
                }
                if (cTile.hasRightCon()) {
                    if (j == tiles[i].length - 1) {
                        count++;
                    } else if (!tiles[i][j + 1].hasLeftCon()) {
                        count++;
                    }
                }
                if (cTile.hasDownCon()) {
                    if (i == tiles.length - 1) {
                        count++;
                    } else if (!tiles[i + 1][j].hasUpCon()) {
                        count++;
                    }
                }
                if (cTile.hasLeftCon()) {
                    if (j == 0) {
                        count++;
                    } else if (!tiles[i][j - 1].hasRightCon()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getOpenCons() {
        return openCons;
    }

    @Override
    public Board clone() {
        Tile[][] localTiles = getTiles();
        Tile[][] copyTiles = new Tile[this.getHeight()][this.getWidth()];
        for (int i = 0; i < copyTiles.length; i++) {
            for (int j = 0; j < copyTiles[i].length; j++) {
                copyTiles[i][j] = new Tile(localTiles[i][j].getOrientation(), localTiles[i][j].getTileType());
            }
        }
        return new Board(this.getWidth(), this.getHeight(), copyTiles);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + Arrays.deepHashCode(tiles);
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        if (height != other.height)
            return false;
        if (!Arrays.deepEquals(tiles, other.tiles))
            return false;
        if (width != other.width)
            return false;
        return true;
    }
}
