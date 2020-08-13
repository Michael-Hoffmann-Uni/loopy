package jpp.infinityloop.gui;

import java.io.File;

import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import jpp.infinityloop.logic.Board;
import jpp.infinityloop.logic.Tile;
import jpp.infinityloop.logic.TileType;
import jpp.infinityloop.logic.Orient;

public class MetaBoard {
    private Board board;
    private MetaTile[][] styleBoard;
    private String style;
    private Color bColor;

    public MetaBoard(Board board, String style) {
        Image bend, cross, deadend, empty, straight, tee;
        bend = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "bend.png"));
        cross = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "cross.png"));
        deadend = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "deadend.png"));
        empty = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "empty.png"));
        straight = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "straight.png"));
        tee = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "tee.png"));
        switch (style) {
            case "standard":
                bColor = Color.rgb(120, 140, 194);
                break;
            case "desert":
                bColor = Color.rgb(224, 145, 90);
                break;
            case "brown":
                bColor = Color.rgb(139, 69, 19);
                break;
            case "green":
                bColor = Color.rgb(128, 128, 0);
                break;
            case "red":
                bColor = Color.rgb(178, 34, 34);
                break;
            default:
                bColor = Color.rgb(120, 140, 194);
                break;
        }

        this.style = style;
        this.board = board;
        Tile[][] tiles = board.getTiles();
        if (this.style != "test") {
            styleBoard = new MetaTile[tiles.length][tiles[0].length];
            Image img = empty;
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    if (tiles[i][j].getTileType() == TileType.BEND)
                        img = bend;
                    if (tiles[i][j].getTileType() == TileType.CROSS)
                        img = cross;
                    if (tiles[i][j].getTileType() == TileType.DEAD_END)
                        img = deadend;
                    if (tiles[i][j].getTileType() == TileType.STRAIGHT)
                        img = straight;
                    if (tiles[i][j].getTileType() == TileType.TEE)
                        img = tee;
                    if (tiles[i][j].getTileType() == TileType.EMPTY)
                        img = empty;

                    styleBoard[i][j] = new MetaTile(img, j, i);
                    styleBoard[i][j].setSmooth(true);

                    if (tiles[i][j].getOrientation() == Orient.UP) {
                        styleBoard[i][j].setRotate(90);
                        styleBoard[i][j].setAngle(90);
                    }
                    if (tiles[i][j].getOrientation() == Orient.RIGHT) {
                        styleBoard[i][j].setRotate(180);
                        styleBoard[i][j].setAngle(180);
                    }
                    if (tiles[i][j].getOrientation() == Orient.DOWN) {
                        styleBoard[i][j].setRotate(270);
                        styleBoard[i][j].setAngle(270);
                    }
                }
            }
        } else {
            styleBoard = null;
        }
    }

    public int rotate(MetaTile metaTile) {
        this.getBoard().rotate(metaTile.getCol(), metaTile.getRow());
        RotateTransition rt = new RotateTransition(Duration.millis(300), metaTile);
        rt.setToAngle(metaTile.getAngle() + 90);
        metaTile.setAngle(metaTile.getAngle() + 90);
        rt.setCycleCount(1);
        rt.setAutoReverse(false);
        rt.play();
        return this.getBoard().getOpenCons();
    }

    public void solvedStyle() {
        Image bend, cross, deadend, empty, straight, tee;
        bend = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "bend_o.png"));
        cross = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "cross_o.png"));
        deadend = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "deadend_o.png"));
        empty = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "empty.png"));
        straight = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "straight_o.png"));
        tee = new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "tee_o.png"));

        Tile[][] tiles = board.getTiles();
        Image img = empty;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].getTileType() == TileType.BEND)
                    img = bend;
                if (tiles[i][j].getTileType() == TileType.CROSS)
                    img = cross;
                if (tiles[i][j].getTileType() == TileType.DEAD_END)
                    img = deadend;
                if (tiles[i][j].getTileType() == TileType.STRAIGHT)
                    img = straight;
                if (tiles[i][j].getTileType() == TileType.TEE)
                    img = tee;
                if (tiles[i][j].getTileType() == TileType.EMPTY)
                    img = empty;

                styleBoard[i][j].setImage(img);
                styleBoard[i][j].setSmooth(true);
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public MetaTile[][] getStyleBoard() {
        return styleBoard;
    }

    public String getStyle() {
        return style;
    }

    public Color getbColor() {
        return bColor;
    }

    public void setStyle(String style) {
        this.style = style;
    }

}
