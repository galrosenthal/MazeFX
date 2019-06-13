package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Box;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.*;
import java.util.*;


public class MyViewController implements IView, Observer {
    @FXML
    public MenuButton menuCharacter;
    public MenuItem daveboy;
    public MenuItem davegirl;
    @FXML
    public MenuItem instructions;
    public MenuItem about;
    private Thread winningThread;

    @FXML
    private Menu levelChange;

    private int row,col;

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @FXML
    private TextField heightField;

    @FXML
    private TextField widthField;
    @FXML
    private ToggleGroup toggleLevel;

    @FXML
    private RadioButton levelEasy;
    @FXML
    private RadioButton levelHard;


    @FXML
    private Pane solveMazePane;
    @FXML
    private Pane generateMazePane;


    @FXML
    private MenuItem solveMazeButton;

    private MediaPlayer mediaPlayer;

    @FXML
    private CheckBox BGM_checkBox;

    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public SolutionDisplayer solDisplayer;
    @FXML
    public DaveDisplayer daveDisplayer;

    private GameDisplayer gameDisplayer;

    @FXML
    public Label lbl_characterRow;
    public Label lbl_characterColumn;


    @FXML
    public MenuButton menuStyle;
    public MenuItem redStyle;
    public MenuItem blueStyle;
    public MenuItem colorfulStyle;
    public MenuItem brownStyle;

    private MyViewModel myViewModel;

    private String name = "Dave";

    private StringProperty characterRow = new SimpleStringProperty();
    private StringProperty characterColumn = new SimpleStringProperty();

    public BorderPane mainPane;
    public Pane mazePane;
    private boolean finishedAlready;
    private boolean wasSounded = false;
    private boolean isCtrlPressed = false;

    private double origDaveX, origDaveY, orgSceneX, orgSceneY;

    private boolean loadMaze;

    private double characterMinX;
    private double cellWidth;
    private double characterMinY;
    private double cellHeight;
    private double lastX = -1.0D;
    private double lastY = -1.0D;

    private Stage primStage;
    private Scene strtScene;

    public void initialize(MyViewModel myViewModel, Stage primaryStage, Scene startScene) {
        this.myViewModel = myViewModel;
        finishedAlready = false;
        primStage = primaryStage;
        strtScene = startScene;
        loadMaze = false;
        gameDisplayer = new GameDisplayer();
        gameDisplayer.setMazeDisplayer(mazeDisplayer);
        gameDisplayer.setSolDisplayer(solDisplayer);
        gameDisplayer.setDaveDisplayer(daveDisplayer);



        lbl_characterRow.textProperty().bind(characterRow);
        lbl_characterColumn.textProperty().bind(characterColumn);

        mazePane.prefHeightProperty().bind(mainPane.heightProperty());
        mazePane.prefWidthProperty().bind(mainPane.widthProperty());
        mazeDisplayer.heightProperty().bind(mazePane.heightProperty());
        mazeDisplayer.widthProperty().bind(mazePane.widthProperty());
        mazeDisplayer.heightProperty().addListener((obs, oldVal, newVal) -> {
            redrawMazeOnZoom();
        });
        mazeDisplayer.widthProperty().addListener((obs, oldVal, newVal) -> {
            redrawMazeOnZoom();
        });


        solDisplayer.heightProperty().bind(mazeDisplayer.heightProperty());
        solDisplayer.widthProperty().bind(mazeDisplayer.widthProperty());
        solDisplayer.widthProperty().addListener(((observable, oldValue, newValue) -> {
            redrawSolutionOnZoom();
        }));
        solDisplayer.heightProperty().addListener((obs, old, newV) -> {
            redrawSolutionOnZoom();
        });


        daveDisplayer.heightProperty().bind(mazeDisplayer.heightProperty());
        daveDisplayer.widthProperty().bind(mazeDisplayer.widthProperty());
        daveDisplayer.heightProperty().addListener((obs, oldVal, newVal) -> {
            redrawMazeOnZoom();
        });
        daveDisplayer.widthProperty().addListener((obs, oldVal, newVal) -> {
            redrawMazeOnZoom();
        });

        setDisableSolveButtons(true);


    }

    public void changeScene()
    {
        System.out.println("Changing Scene");
        mazeDisplayer.clearMaze();
        daveDisplayer.clearDave();
        solDisplayer.clearSolution();
        gameDisplayer.setZoomFactor(1.0D);
        finishedAlready = false;
        primStage.setScene(strtScene);
    }



    private void redrawSolutionOnZoom() {
        gameDisplayer.drawSolution(myViewModel.getMaze());
    }

    private void setDisableSolveButtons(Boolean doDisabled) {
        solveMazeButton.setDisable(doDisabled);
        solveMazePane.setDisable(doDisabled);
    }


    public void solveMaze() {
        gameDisplayer.solDisplayer.setVisibleMaze(true);
        solDisplayer.setSol(myViewModel.getSolution());
        solDisplayer.drawSolution(myViewModel.getMaze(), gameDisplayer.getZoomFactor(), daveDisplayer.getCharacterPositionColumn(), daveDisplayer.getCharacterPositionRow());
        setDisableSolveButtons(true);
    }

    public void generateMaze() {

        mazeDisplayer.golToken = false;
        gameDisplayer.cleanGameBoard();
        gameDisplayer.setZoomFactor(1.0D);
        gameDisplayer.solDisplayer.setVisibleMaze(false);
        myViewModel.generateMaze(row, col);
        setPositonGoblet(mazeDisplayer.getRandomPost(myViewModel.getrandomPos()));
        gameDisplayer.drawOnZoom();
        wasSounded = false;
        finishedAlready = false;
        stopWiningThread();
        setDisableAllButtons(false);
    }


    public void createLevel() {
        toggleLevel = new ToggleGroup();
        levelEasy.setSelected(true);
        levelEasy.setUserData("Easy");
        levelHard.setUserData("Hard");

        levelEasy.setToggleGroup(toggleLevel);
        levelHard.setToggleGroup(toggleLevel);
        toggleLevel.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton last = (RadioButton) oldValue;
                RadioButton next = (RadioButton) newValue;
                //TODO: Add a real function to RadioButtons
                System.out.println(last.getText() + " was changed to " + next.getText());
            }
        });

    }






    public void playMusic() {
        String BGM = "resources/Audio/mazemusic.mp3";
        Media hit = new Media(new File(BGM).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        //loop
        mediaPlayer.setOnEndOfMedia(this::playMusic);

    }


    public void backgroundMusicStatus() {
        if (BGM_checkBox.isSelected()) {
            mediaPlayer.play();
        } else {
            mediaPlayer.pause();
        }
    }

    public void exitWithExitButton() throws InterruptedException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        mediaPlayer.stop();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            finishedAlready = false;
            stopWiningThread();
            myViewModel.playSound("resources/Audio/goodBye.mp3");
            myViewModel.closeGame();
            Thread.sleep(855);
            Platform.exit();
        }
    }

    private void stopWiningThread() {
        if (winningThread != null) {
            try {
                winningThread.join();
                winningThread = null;

            } catch (Exception e) {

            }
        }
    }

    public void exitWithXButton(WindowEvent event) {
        try {
            exitWithExitButton();
        } catch (InterruptedException e) {
        }
    }


    public void mouseClicked(MouseEvent mouseEvent) {

        mazeDisplayer.requestFocus();
    }





    public void KeyPressedEasy(KeyEvent keyEvent) {
        int level = 1;


        if (keyEvent.getCode().getName().toLowerCase().equals("ctrl")) {
            isCtrlPressed = true;
            keyEvent.consume();
            return;
        }
        if (levelEasy.isSelected()) {
            level = 1;

            if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD6) {
                daveDisplayer.setImageFileNameCharacter("resources/Images/" + name + ".png");
            } else if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.NUMPAD4) {
                daveDisplayer.setImageFileNameCharacter("resources/Images/" + name + "Left.png");
            }


        } else if (levelHard.isSelected()) {
            level = -1;
            if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD4) {
                daveDisplayer.setImageFileNameCharacter("resources/Images/" + name + "Left.png");
            } else if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.NUMPAD6) {
                daveDisplayer.setImageFileNameCharacter("resources/Images/" + name + ".png");
            }
        }
        myViewModel.moveCharacter(keyEvent, level);
        reachedTheDoor();

        keyEvent.consume();
    }

    private void reachedTheDoor() {
        if (myViewModel.gameWon() && !finishedAlready && mazeDisplayer.golToken) {
            mediaPlayer.stop();
            playSpecificSound("resources/Audio/woohoo.wav");
            finishedAlready = true;
            gameDisplayer.setCharacterPosition(new Position(0,0));
            winningThread = new Thread(this::characterZoomInAndOut);
            winningThread.start();
            setDisableAllButtons(true);


            Alert EndGame = new Alert(Alert.AlertType.INFORMATION, "Congratulations!!! You have Won the Game, Dave is Resuced =)");
            EndGame.setTitle("Congratulations");
            EndGame.showAndWait();
            if (BGM_checkBox.isSelected())
                mediaPlayer.play();
        } else if (myViewModel.gameWon() && !mazeDisplayer.golToken) {
            Alert cantEnd = new Alert(Alert.AlertType.ERROR, "Please take the goblet!");
            cantEnd.showAndWait();
            myViewModel.setCharacterRowCurrentPosition(myViewModel.getMaze().getStartPosition().getRowIndex());
            myViewModel.setCharacterColumnCurrentPosition(myViewModel.getMaze().getStartPosition().getColumnIndex());
            gameDisplayer.setCharacterPosition(myViewModel.getMaze().getStartPosition());
            daveDisplayer.setCharacterPosition(myViewModel.getMaze().getStartPosition());
            solDisplayer.drawSolution(mazeDisplayer.getMaze(), gameDisplayer.getZoomFactor(), daveDisplayer.getCharacterPositionColumn(), daveDisplayer.getCharacterPositionRow());
            this.characterRow.set(myViewModel.getMaze().getStartPosition().getRowIndex() + "");
            this.characterColumn.set(myViewModel.getMaze().getStartPosition().getColumnIndex() + "");

        }
    }

    private void setDisableAllButtons(boolean isDisabled) {

        setDisableSolveButtons(isDisabled);
        setDisableRadioButtons(isDisabled);
        setDisableStyleButton(isDisabled);
        setDisableCharacterChooser(isDisabled);


    }

    private void setDisableCharacterChooser(boolean isDisabled) {
        menuCharacter.setDisable(isDisabled);
    }

    private void setDisableStyleButton(boolean isDisabled) {
        menuStyle.setDisable(isDisabled);
    }

    private void setDisableRadioButtons(boolean isDisabled) {
        levelEasy.setDisable(isDisabled);
        levelHard.setDisable(isDisabled);
    }


    private void characterZoomInAndOut() {
        double zoomDaveOnWin = gameDisplayer.getZoomFactor();
        int dir = 1;

        while (finishedAlready) {
            gameDisplayer.getDaveDisplayer().drawDave(gameDisplayer.getMazeDisplayer().getMaze(), zoomDaveOnWin);
            if (zoomDaveOnWin >= 10D * gameDisplayer.getZoomFactor()) {
                dir = -1;
            } else if (zoomDaveOnWin <= 1D) {
                dir = 1;
            }
            zoomDaveOnWin += 0.5D * dir;
            try {
                Thread.sleep(50);

            } catch (Exception e) {

            }
        }
    }

    public void setPositonGoblet(Position p) {
        mazeDisplayer.setGolRow(p.getRowIndex());
        mazeDisplayer.setGolCol(p.getColumnIndex());
    }

    private void playSpecificSound(String musicPath) {
        AudioClip sound = new AudioClip(new File(musicPath).toURI().toString());
        sound.play();
    }

    public void changeStyleToBlue() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/blueWall.jpg");
        gameDisplayer.redrawMaze();
    }

    public void changeStyleToRed() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/redWall.jpg");
        gameDisplayer.redrawMaze();
    }

    public void changeStyleTobrown() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/brownWall.jpg");
        gameDisplayer.redrawMaze();
    }

    public void changeStyleToColorful() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/ColorfulWall.jpg");
        gameDisplayer.redrawMaze();
    }

    public void changeToDave(ActionEvent actionEvent) {
        name = "dave";
        daveDisplayer.setImageFileNameCharacter("resources/Images/dave.png");
        gameDisplayer.redrawMaze();
    }

    public void changeToLily(ActionEvent actionEvent) {
        name = "lily";
        daveDisplayer.setImageFileNameCharacter("resources/Images/lily.png");
        gameDisplayer.redrawMaze();
    }

    public void saveMazeView() {
        Stage saveWindow = new Stage();
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(saveWindow);


        if (file != null) {
            try {
                finishToSave(file.toString());

            } catch (FileNotFoundException fileNfound) {
                fileNfound.printStackTrace();
            }
        }

    }

    public void LoadMaze() {
        finishedAlready = false;

        Stage loadWindow = new Stage();
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(loadWindow);


        if (file != null)
            myViewModel.loadMazeFromDisk(file.toString());


    }

    private void finishToSave(String name) throws FileNotFoundException {
        myViewModel.viewModelSaveMazeToTheDisc(name);
    }

    public void setViewModel(MyViewModel viewModel) {
        myViewModel = viewModel;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == myViewModel) {
            Maze testMaze = myViewModel.getMaze();
            if (testMaze != null) {
                mazeDisplayer.setMaze(myViewModel.getMaze());
                gameDisplayer.setCharacterPosition(myViewModel.getPosition());

                this.cellWidth = this.mazeDisplayer.getWidth() / (double) this.mazeDisplayer.mw;
                this.cellHeight = this.mazeDisplayer.getHeight() / (double) this.mazeDisplayer.mh;
                this.characterMinX = (double) myViewModel.getPosition().getColumnIndex() * this.cellWidth;
                this.characterMinY = (double) myViewModel.getPosition().getRowIndex() * this.cellHeight;
                this.characterRow.set(myViewModel.getPosition().getRowIndex() + "");
                this.characterColumn.set(myViewModel.getPosition().getColumnIndex() + "");


                if (mazeDisplayer.golCol == myViewModel.getPosition().getColumnIndex() && mazeDisplayer.golRow == myViewModel.getPosition().getRowIndex()) {
                    mazeDisplayer.isGobletVisible(myViewModel.getPosition().getColumnIndex(), myViewModel.getPosition().getRowIndex());
                    if (!wasSounded) {
                        myViewModel.playSound("resources/Audio/yes-2.wav");
                        wasSounded = true;
                    }
                }

            } else {
                Alert mazeNotFoundAlert = new Alert(Alert.AlertType.ERROR);
                mazeNotFoundAlert.setTitle("Maze Not Found!");
                mazeNotFoundAlert.setContentText("Please Try Reloading or Regenerating your maze!");
                mazeNotFoundAlert.show();

            }

            Solution testSolution = myViewModel.getSolution();

            if (testSolution != null) {
                solDisplayer.setSol(myViewModel.getSolution());
                solDisplayer.drawSolution(myViewModel.getMaze(), gameDisplayer.getZoomFactor(), daveDisplayer.getCharacterPositionColumn(), daveDisplayer.getCharacterPositionRow());
            }


        }

    }

    public void keyReleased(KeyEvent kyRlsd) {
        if (kyRlsd.getCode().getName().toLowerCase().equals("ctrl"))
            isCtrlPressed = false;
    }


    public void redrawMazeOnZoom() {
        gameDisplayer.redrawMaze();
    }

    public void zoom(ScrollEvent scEvent) {
        if (isCtrlPressed && mazeDisplayer.getMaze() != null) {
            double zoomDelta = 1.1D;
            double deltaY = scEvent.getDeltaY();
            if (deltaY > 0.0D) {
                gameDisplayer.setZoomFactor(gameDisplayer.getZoomFactor() * zoomDelta);
            } else if (deltaY < 0.0D) {
                gameDisplayer.setZoomFactor(gameDisplayer.getZoomFactor() / zoomDelta);

            }

        }
        gameDisplayer.drawOnZoom();

    }

    public void MouseDrag(MouseEvent event) {
        if (levelHard.isSelected() || gameDisplayer.getZoomFactor() != 1.0D) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("You can't move the mouse in hard level or when in Zoom!");
            alert.show();
            levelEasy.setSelected(true);
        }
        myViewModel.getLevel(1);
        if (this.mazeDisplayer.hasMaze()) {
            if (this.lastX <= this.characterMinX + this.cellWidth && this.lastX >= this.characterMinX && this.lastY >= this.characterMinY && this.lastY <= this.characterMinY + this.cellHeight) {
                if (event.getX() < this.characterMinX && event.getY() > this.cellHeight + this.characterMinY) {
                    myViewModel.moveChar("Numpad 1");
                } else if (event.getX() > this.cellWidth + this.characterMinX && event.getY() > this.cellHeight + this.characterMinY) {
                    myViewModel.moveChar("Numpad 3");
                } else if (event.getX() < this.characterMinX && event.getY() < this.characterMinY) {
                    myViewModel.moveChar("Numpad 7");
                } else if (event.getX() > this.cellWidth + this.characterMinX && event.getY() < this.characterMinY) {
                    myViewModel.moveChar("Numpad 9");
                } else if (event.getX() > this.cellWidth + this.characterMinX) {
                    myViewModel.moveChar("Right");
                } else if (event.getX() < this.characterMinX) {
                    myViewModel.moveChar("Left");
                } else if (event.getY() > this.cellHeight + this.characterMinY) {
                    myViewModel.moveChar("Down");
                } else if (event.getY() < this.characterMinY) {
                    myViewModel.moveChar("Up");
                }
            }

            this.lastX = event.getX();
            this.lastY = event.getY();

            reachedTheDoor();
            event.consume();
        }
    }


    public void openProp(ActionEvent actionEvent) {
        try
        {
            FXMLLoader propLoader = new FXMLLoader();
            Parent root = (Parent)propLoader.load(this.getClass().getResource("propFile.fxml").openStream());
            Stage propStage = new Stage();
            PropFile propController = propLoader.getController();
            Scene propScene = new Scene(root);
            propController.initProp(propStage);

            propStage.setScene(propScene);
            propStage.showAndWait();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        Properties properties = new Properties();
//        try {
//            InputStream propFile = new FileInputStream("resources/config.properties");
//            properties.load(propFile);
//
//            FileReader propReader = new FileReader("resources/config.properties");
//            BufferedReader bufferedReader = new BufferedReader(propReader);
//
//            StringBuilder textProp = new StringBuilder();
//            String line = bufferedReader.readLine();
//            while (line != null) {
//                if (!line.contains("#")) {
//                    String[] myLine = line.split("=");
//                    textProp.append(myLine[0]);
//                    textProp.append(" = ");
//                    textProp.append(myLine[1]);
//                    textProp.append("\n");
//
//                }
//                line = bufferedReader.readLine();
//            }
//            textProp.append("\n\n");
//            bufferedReader.close();
//            propReader.close();
//            Alert propAlert = new Alert(Alert.AlertType.INFORMATION);
//            propAlert.setContentText(textProp.toString());
//            propAlert.setTitle("Properties Of The Game");
//            propAlert.setHeaderText("PROPERTIES");
//
//            propAlert.showAndWait();
//
//        } catch (Exception e) {
//
//        }
    }
}


