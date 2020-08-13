package jpp.infinityloop.gui;

import java.io.File;

import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jpp.infinityloop.logic.Board;
import jpp.infinityloop.logic.Tile;

public class GuiView extends Application {
    private Controller controller;
    Tile[][] init = null;
    Board initBoard = null;
    private MetaBoard currMetaBoard = null;
    double paneWidth;
    double paneHeight;
    private Pane field = new Pane();
    private BorderPane borderPane = new BorderPane();
    private VBox menuBox = new VBox();
    HBox topBox = new HBox();
    private ToolBar menuBar = new ToolBar();
    private SequentialTransition seqT = null;
    private Button btnSolve = new Button();
    private Button btnNew = new Button();
    private Button btnSaveAs = new Button();
    private Button btnLoad = new Button();
    private Button btnFull = new Button();
    Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // init

        Controller controller = new Controller(btnSolve);
        final Node block = (Node) menuBar;
        Font arial = new Font("Arial", 16);

        //----------------------- create buttons for menuBar -----------------------------------------
        //needed: loadFile, saveAsFile, generateNew, solveBoard, fullscreen + (shuffle current board, options)

        //generate new board
        ImageView imgGenNew = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "Random_my.png")));
        imgGenNew.setFitWidth(40);
        imgGenNew.setPickOnBounds(true);
        imgGenNew.setPreserveRatio(true);
        btnNew.setGraphic(imgGenNew);
        btnNew.setStyle("-fx-background-color: transparent;");
        btnNew.setOnMouseClicked(e -> this.setCurrBoard(controller.generateMetaBoard()));
        Tooltip ttNew = new Tooltip("Generate a new random board");
        ttNew.setFont(arial);
        btnNew.setTooltip(ttNew);

        //saveAs
        ImageView imgSaveAs = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "Save_my2.png")));
        imgSaveAs.setFitWidth(40);
        imgSaveAs.setPickOnBounds(true);
        imgSaveAs.setPreserveRatio(true);
        btnSaveAs.setGraphic(imgSaveAs);
        btnSaveAs.setStyle("-fx-background-color: transparent;");
        btnSaveAs.setOnMouseClicked(e -> controller.saveFile(currMetaBoard, block));
        Tooltip ttSave = new Tooltip("Save the current state of the board in a file");
        ttSave.setFont(arial);
        btnSaveAs.setTooltip(ttSave);

        //loadFile
        ImageView imgLoadFile = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "Load_my.png")));
        imgLoadFile.setFitWidth(40);
        imgLoadFile.setPickOnBounds(true);
        imgLoadFile.setPreserveRatio(true);
        btnLoad.setGraphic(imgLoadFile);
        btnLoad.setStyle("-fx-background-color: transparent;");
        btnLoad.setOnMouseClicked(e -> this.setCurrBoard(controller.loadFile(block)));
        Tooltip ttLoad = new Tooltip("Load a board from a file");
        ttLoad.setFont(arial);
        btnLoad.setTooltip(ttLoad);

        //solve
        ImageView imgSolve = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "Solve_my.png")));
        imgSolve.setFitWidth(40);
        imgSolve.setPickOnBounds(true);
        imgSolve.setPreserveRatio(true);
        btnSolve.setGraphic(imgSolve);
        btnSolve.setStyle("-fx-background-color: transparent;");
        btnSolve.setOnMouseClicked(e -> this.playAnimation(controller.solve(currMetaBoard)));
        Tooltip ttSolve = new Tooltip("Let the programm solve the board");
        ttSolve.setFont(arial);
        btnSolve.setTooltip(ttSolve);

        //fullscreen
        ImageView imgFull = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("resources" + File.separator + "Full_my.png")));
        imgFull.setFitWidth(40);
        imgFull.setPickOnBounds(true);
        imgFull.setPreserveRatio(true);
        btnFull.setGraphic(imgFull);
        btnFull.setStyle("-fx-background-color: transparent;");
        btnFull.setOnMouseClicked(e -> this.setFullscreen(stage));
        Tooltip ttFull = new Tooltip("Switch between fullscreen and window mode");
        ttFull.setFont(arial);
        btnFull.setTooltip(ttFull);

        menuBar.getItems().add(btnNew);
        menuBar.getItems().add(btnLoad);
        menuBar.getItems().add(btnSaveAs);
        menuBar.getItems().add(btnSolve);
        menuBar.getItems().add(btnFull);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.getChildren().add(menuBar);
        borderPane.setRight(menuBox);
        //--------------------------------------------------------------------------------------------------------

        mainScene = new Scene(borderPane, 1200, 800);
        field.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldPaneWidth, Number newPaneWidth) {
                paneWidth = (double) newPaneWidth;
//		        System.out.println("Width: " + paneWidth);
            }
        });
        field.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldPaneHeight, Number newPaneHeight) {
                paneHeight = (double) newPaneHeight;
//		    	System.out.println("Height: " + paneHeight);
            }
        });

        this.setStyle();
        borderPane.setCenter(field);
        this.controller = controller;
        menuBar.setOrientation(Orientation.VERTICAL);
        stage.setTitle("Infinity Loop");
        stage.setScene(mainScene);
        stage.setMinHeight(310);
        stage.show();
        this.setCurrBoard(controller.generateMetaBoard());
    }

    public void setCurrBoard(MetaBoard metaBoard) {
        if (metaBoard == null) {
//			btnSaveAs.setDisable(true);
        } else {
//			btnSaveAs.setDisable(false);
            if (seqT != null)
                this.seqT.stop();
            this.currMetaBoard = metaBoard;
            this.setStyle();
            if (currMetaBoard.getBoard().getOpenCons() != 0)
                controller.setRotLocked(false);
            controller.setSolving(false);

            field.getChildren().clear();
            MetaTile[][] styleBoard = this.currMetaBoard.getStyleBoard();
            ObservableNumberValue bWidth = Bindings.min(field.heightProperty().divide(currMetaBoard.getBoard().getHeight()), field.widthProperty().divide(currMetaBoard.getBoard().getWidth()));
            ObservableNumberValue bHeight = Bindings.min(field.heightProperty().divide(currMetaBoard.getBoard().getHeight()), field.widthProperty().divide(currMetaBoard.getBoard().getWidth()));
            ObservableNumberValue xStart = Bindings.subtract(field.widthProperty(), Bindings.multiply(bWidth, styleBoard[0].length)).divide(2);
            ObservableNumberValue yStart = Bindings.subtract(field.heightProperty(), Bindings.multiply(bHeight, styleBoard.length)).divide(2);

            for (int i = 0; i < styleBoard.length; i++) {
                for (int j = 0; j < styleBoard[0].length; j++) {
                    styleBoard[i][j].setPickOnBounds(true);
                    styleBoard[i][j].fitWidthProperty().bind(bWidth);
                    styleBoard[i][j].fitHeightProperty().bind(bHeight);

                    styleBoard[i][j].layoutXProperty().bind(Bindings.add(xStart, styleBoard[i][j].fitWidthProperty().multiply(j)));
                    styleBoard[i][j].layoutYProperty().bind(Bindings.add(yStart, styleBoard[i][j].fitHeightProperty().multiply(i)));
                    styleBoard[i][j].setOnMouseClicked(e -> this.checkSolved(controller.rotate(currMetaBoard, e)));
                    field.getChildren().add(styleBoard[i][j]);
                }
            }
        }
    }

    private void setFullscreen(Stage stage) {
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        } else {
            stage.setFullScreen(true);
        }
    }

    private void checkSolved(int openCons) {
        if (controller.isSolving() == false) {
            setStyle();
        }
    }

    public void setStyle() {
        if (currMetaBoard == null) {
            field.setStyle("-fx-background-color: #788CC2;");
            menuBox.setStyle("-fx-background-color: #788CC2;");
            menuBar.setStyle("-fx-background-color: #788CC2;");
            topBox.setStyle("-fx-background-color: #788CC2;");
        } else if (currMetaBoard.getBoard().getOpenCons() == 0) {
            btnSolve.setDisable(true);
            controller.setRotLocked(true);
            field.setStyle("-fx-background-color: #28524F;");
            menuBar.setStyle("-fx-background-color: #28524F;");
            menuBox.setStyle("-fx-background-color: #28524F;");
            topBox.setStyle("-fx-background-color: #28524F;");
            currMetaBoard.solvedStyle();
        } else {
            String bColor = currMetaBoard.getbColor().toString();
            bColor = "#" + bColor.substring(2, 8);
            field.setStyle("-fx-background-color: " + bColor + ";");
            menuBox.setStyle("-fx-background-color: " + bColor + ";");
            menuBar.setStyle("-fx-background-color: " + bColor + ";");
            topBox.setStyle("-fx-background-color: " + bColor + ";");
        }
    }

    public void playAnimation(SequentialTransition seqT) {
        if (seqT != null) {
            this.seqT = seqT;
            seqT.setOnFinished(e -> stillActive(seqT));
            seqT.play();
        } else {
            if (!controller.isSolvable()) {
                //error message not solvable
            }
        }
    }

    public void stillActive(SequentialTransition seqT) {
        if (this.seqT.equals(seqT)) {
            this.setStyle();
        }
    }
}












