package jpp.infinityloop.logic;

public class BacktrackingSolver {
    private Board solvedBoard;

    public BacktrackingSolver() {
        this.solvedBoard = null;

    }

    public boolean solve(Board board) {
        if (board.checkSolved() == true) {
            this.solvedBoard = board;
            return true;
        } else {
            boolean retVal = bSolve(board, 0, 0);
            return retVal;
        }
    }

    //Board solve
    private boolean bSolve(Board clone, int x, int y) {
        boolean retVal;
        Tile[][] tiles = clone.getTiles();
        int newX = 0;
        int newY = 0;
        TileType type;
        for (int i = 0; i < 4; i++) {
            type = tiles[y][x].getTileType();
            if (!fit(tiles, x, y)) {
                if (type == TileType.CROSS) {
                    i = 4;
                } else if (type == TileType.STRAIGHT) {
                    tiles[y][x].rotate();
                    i++;
                } else if (type == TileType.EMPTY) {
                    i = 4;
                } else {
                    tiles[y][x].rotate();
                }
            } else {
                if (x == tiles[y].length - 1 && y == tiles.length - 1) {
                    this.solvedBoard = clone;
                    return true;
                } else if (x == tiles[y].length - 1) {
                    newX = 0;
                    newY = y + 1;
                } else {
                    newX = x + 1;
                    newY = y;
                }
                retVal = bSolve(clone, newX, newY);
                if (retVal == true) {
                    return retVal;
                } else {
                    if (type == TileType.CROSS) {
                        i = 4;
                    } else if (type == TileType.STRAIGHT) {
                        tiles[y][x].rotate();
                        i++;
                    } else if (type == TileType.EMPTY) {
                        i = 4;
                    } else {
                        tiles[y][x].rotate();
                    }
                }
            }
        }
        return false;
    }


    private boolean fit(Tile[][] tiles, int x, int y) {
        //check left side
        if (x == 0) {
            if (tiles[y][x].hasLeftCon())
                return false;
        } else if (tiles[y][x - 1].hasRightCon()) {
            if (!tiles[y][x].hasLeftCon())
                return false;
        } else if (tiles[y][x].hasLeftCon()) {
            if (!tiles[y][x - 1].hasRightCon())
                return false;
        }

        //check right side
        if (x == tiles[y].length - 1) {
            if (tiles[y][x].hasRightCon())
                return false;
        }

        //check up side
        if (y == 0) {
            if (tiles[y][x].hasUpCon())
                return false;
        } else if (tiles[y - 1][x].hasDownCon()) {
            if (!tiles[y][x].hasUpCon())
                return false;
        } else if (tiles[y][x].hasUpCon()) {
            if (!tiles[y - 1][x].hasDownCon())
                return false;
        }

        //check down side
        if (y == tiles.length - 1) {
            if (tiles[y][x].hasDownCon())
                return false;
        }
        return true;
    }

    public Board getSolvedBoard() {
        return solvedBoard;
    }

}
