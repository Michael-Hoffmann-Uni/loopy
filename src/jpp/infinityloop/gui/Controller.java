package jpp.infinityloop.gui;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import jpp.infinityloop.impexp.BlobCoder;
import jpp.infinityloop.impexp.FileExchanger;
import jpp.infinityloop.logic.BacktrackingSolver;
import jpp.infinityloop.logic.Board;
import jpp.infinityloop.logic.Generator;
import jpp.infinityloop.logic.Tile;

public class Controller {
    private int minWidth;
    private int maxWidth;
    private int minHeight;
    private int maxHeight;
    private boolean rotLocked;
    private boolean solving;
    private boolean isSolvable;
    private int levelCount = 0;
    private String lastStyle;
    private Button btnSolve;

    public Controller(Button btnSolve) {
        this.minWidth = 10;
        this.maxWidth = 15;
        this.minHeight = 10;
        this.maxHeight = 15;
        this.rotLocked = false;
        this.btnSolve = btnSolve;
    }

    public MetaBoard loadFile(Node node) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import File");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("BIN", "*.bin"));
        File f = chooser.showOpenDialog(node.getScene().getWindow());

        Alert alertInvData = new Alert(AlertType.ERROR);
        alertInvData.setTitle("Invalid Data");
        alertInvData.setContentText("The File you tried to load is not a valid Infinity Loop file.");

        try {
            if (f == null) {
                return null;
            } else {
                btnSolve.setDisable(false);
                FileExchanger fEx = new FileExchanger();
                byte[] data = fEx.importFile(f);
                BlobCoder bc = new BlobCoder();
                Board board = bc.decode(data);
                MetaBoard metaBoard = new MetaBoard(board, nextStyle());
                return metaBoard;
            }
        } catch (IllegalArgumentException e) {
            alertInvData.showAndWait();
            return null;
        }
    }

    public void saveFile(MetaBoard metaBoard, Node node) {
        if (metaBoard != null) {
            Board board = metaBoard.getBoard();
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save Board");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("BIN", "*.bin"));
            File f = chooser.showSaveDialog(node.getScene().getWindow());
            if (f != null) {
                FileExchanger fEx = new FileExchanger();
                BlobCoder bc = new BlobCoder();
                fEx.exportFile(bc.encode(board), f.getPath());
            }

        } else {
            //maybe error message
        }
    }

    public MetaBoard generateMetaBoard() {
        btnSolve.setDisable(false);
        Generator generator = new Generator(minWidth, maxWidth, minHeight, maxHeight);
        Board board = generator.generate();
        MetaBoard metaBoard = new MetaBoard(board, nextStyle());
        levelCount++;
        return metaBoard;
    }

    public int rotate(MetaBoard metaBoard, MouseEvent e) {
        if (!isRotLocked()) {
            MetaTile metaTile = (MetaTile) e.getSource();
            int openCons = metaBoard.rotate(metaTile);
            if (openCons == 0) {
                this.setRotLocked(true);
                this.levelCount++;
            }
            return openCons;
        } else {
            return metaBoard.getBoard().getOpenCons();
        }
    }

    public SequentialTransition solve(MetaBoard metaBoard) {
        if (this.isSolving() == false && metaBoard != null) {
            btnSolve.setDisable(true);
            this.setRotLocked(true);
            this.setSolving(true);
            setSolvable(false);
            BacktrackingSolver bs = new BacktrackingSolver();
            Board clone = metaBoard.getBoard().clone();
            setSolvable(bs.solve(clone));

            if (!isSolvable) {
                // ErrorMessage
                Alert alertNotSolvable = new Alert(AlertType.ERROR);
                alertNotSolvable.setTitle("Unsolvable");
                alertNotSolvable.setContentText("The current board cannot be solved.");
                alertNotSolvable.showAndWait();
                this.setRotLocked(false);
                this.setSolving(false);
                return null;
            } else {
                SequentialTransition seqT = new SequentialTransition();

                Board solvedBoard = bs.getSolvedBoard();
                Board orgBoard = metaBoard.getBoard();
                Tile[][] solvedTiles = solvedBoard.getTiles();
                Tile[][] orgTiles = orgBoard.getTiles();
                MetaTile[][] orgStyleBoard = metaBoard.getStyleBoard();

                for (int i = 0; i < solvedTiles.length; i++) {
                    for (int j = 0; j < solvedTiles[0].length; j++) {
                        while (solvedTiles[i][j].getOrientation() != orgTiles[i][j].getOrientation()) {

                            //add rotation
                            MetaTile mT = orgStyleBoard[i][j];
                            RotateTransition rt = new RotateTransition(Duration.millis(100), mT);
                            rt.setToAngle(mT.getAngle() + 90);
                            mT.setAngle(mT.getAngle() + 90);
                            rt.setCycleCount(1);
                            rt.setAutoReverse(false);
                            seqT.getChildren().add(rt);
                            orgBoard.rotate(j, i);
                        }
                    }
                }
                return seqT;
            }
        } else {
            return null;
        }
    }

    public String nextStyle() {
        String retStyle;
        String[] styles = new String[5];
        //styles here defined
        styles[0] = "standard";
        styles[1] = "desert";
        styles[2] = "brown";
        styles[3] = "green";
        styles[4] = "red";
        do {
            int roll = ThreadLocalRandom.current().nextInt(0, styles.length);
            retStyle = styles[roll];
        } while (retStyle == lastStyle);
        lastStyle = retStyle;
        return retStyle;
    }

    public boolean isRotLocked() {
        return rotLocked;
    }

    public void setRotLocked(boolean locked) {
        this.rotLocked = locked;
    }

    public boolean isSolving() {
        return solving;
    }

    public void setSolving(boolean solving) {
        this.solving = solving;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public void setSolvable(boolean isSolvable) {
        this.isSolvable = isSolvable;
    }

    public int getLevelCount() {
        return levelCount;
    }
}
