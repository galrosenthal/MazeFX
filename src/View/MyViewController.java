package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


import javax.swing.event.MenuEvent;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;


public class MyViewController implements IView, Observer {
    @FXML
    public MenuButton menuCharacter;
    public MenuItem daveboy;
    public MenuItem davegirl;


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

    private MediaPlayer mediaPlayer;

    @FXML
    private CheckBox BGM_checkBox;

    @FXML
    public MazeDisplayer mazeDisplayer;
    public SolutionDisplayer solDisplayer;
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

    public String name = "Dave";

    public StringProperty characterRow = new SimpleStringProperty();
    public StringProperty characterColumn = new SimpleStringProperty();

    public BorderPane mainPane;
    public Pane mazePane;
    private boolean finishedAlready;


    public void initialize(MyViewModel myViewModel) {
        finishedAlready = false;
        //Set Binding for Properties
        lbl_characterRow.textProperty().bind(characterRow);
        lbl_characterColumn.textProperty().bind(characterColumn);

        mazePane.prefHeightProperty().bind(mainPane.heightProperty());
        mazePane.prefWidthProperty().bind(mainPane.widthProperty());
        mazeDisplayer.heightProperty().bind(mazePane.heightProperty());
        mazeDisplayer.widthProperty().bind(mazePane.widthProperty());
        mazeDisplayer.heightProperty().addListener((obs,oldVal,newVal)->{
            redrawMazeOnZoom();
        });
        mazeDisplayer.widthProperty().addListener((obs,oldVal,newVal)->{
            redrawMazeOnZoom();
        });




    }

    //    @FXML
//    private Label valueError;


    int[][] mazeData;
//            = { // a stub...
////        {0, 1, 1, 1,},
////        {0, 0, 0, 0},
////        {0, 0, 1, 1},
////        {1, 1, 1, 0}
//            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
//            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1},
//            {0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1},
//            {1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1},
//            {1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1},
//            {1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1},
//            {1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1},
//            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1},
//            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1},
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1}
//    };

    public void solveMaze()
    {
        myViewModel.solveGame();

    }

    public void generateMaze() {
        //int rows = Integer.valueOf(txtfld_rowsNum.getText());
        //int columns = Integer.valueOf(txtfld_columnsNum.getText());
        //this.mazeDisplayer.setMaze(getRandomMaze(rows,columns));
        myViewModel.generateMaze(Integer.parseInt(heightField.getText()),Integer.parseInt(widthField.getText()));
        finishedAlready = false;

//        this.mazeDisplayer.setMaze(mazeData);
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


    public void openInstructions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("HOW TO PLAY?");
        alert.setContentText("דייב המסוכן גירסת המבוך\n מטרת המשחק: עיזרו לדייב להגיע לדלת הסתרים \n אופן יצירת מבוך: כדי ליצור מבוך עליך להכניס את מידות המבוך הרצוי עליך.\n נא בחר מספר שלם וחיובי עבור הגובה(height)ורוחב(width) המבוך.\n" +
                "נא ללחוץ על כפתור Generate Maze (או Ctrl + G לקיצור).\nבמידה ואתם נתקעים או סתם עצלנים, ניתן לקבל את מסלול המבוך על ידי לחיצה על Solve Maze (או Ctrl + F לקיצור) והפתרון יוצג על גבי המסך.\n\nתוכלו לשמור ולטעון מבוכים שמורים ע\"י לחיצה על File ובחירת Save Maze או Load Maze בהתאמה. (Ctrl + S לשמירה, Ctrl + L לטעינה)\n\nעל מנת לזוז יש להשתמש במקשי החיצים, או לחילופין בלחצני המספרים ב-NumPad (2,4,6,8 בהתאמה). במידה ונתקעתם בקיר, תיפסלו ותחזרו לתחילת המשחק!\nניתן לזוז באלכסון כדי לקצר (1,3,7,9 בהתאמה), אם הדרך חסומה משני הצדדים בקירות תיפסלו ותחזרו להתחלה.\n");
        alert.show();
    }


    private int[] getValues() {
        String rowSizeFromUser = heightField.getText();
        String colSizeFromUser = widthField.getText();
        int[] mazeSizes = new int[2];
        if (rowSizeFromUser.isEmpty() || colSizeFromUser.isEmpty() || Integer.parseInt(rowSizeFromUser) > 300 ||
                Integer.parseInt(rowSizeFromUser) < 0 || Integer.parseInt(colSizeFromUser) > 300 || Integer.parseInt(colSizeFromUser) < 0) {
//            valueError.setVisible(true);
            return null;
        } else {
//            valueError.setText("Well Done, Creating Your Maze");
//            valueError.setVisible(true);
            mazeSizes[0] = Integer.parseInt(rowSizeFromUser);
            mazeSizes[1] = Integer.parseInt(colSizeFromUser);
//            newWindow.close();
//            isUsed = true;

        }
        return mazeSizes;
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

    public void exitFromTheGame(WindowEvent event) throws InterruptedException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            myViewModel.playSound("resources/Audio/goodBye.mp3");
            myViewModel.closeGame();
            Thread.sleep(855);
            Platform.exit();
        }
    }
    


     public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    public void KeyPressedEasy(KeyEvent keyEvent) {
        int level = 1;
        if (!(keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.UP
                || keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD8 || keyEvent.getCode() == KeyCode.NUMPAD2
                || keyEvent.getCode() == KeyCode.NUMPAD4 || keyEvent.getCode() == KeyCode.NUMPAD6)) {
            keyEvent.consume();
            Alert alert = new Alert(Alert.AlertType.WARNING, "You pressed on illegal button.\n Please read the instructions and try again. ", ButtonType.OK);
            alert.setTitle("WARNING");
            alert.showAndWait();
            return;
        }

        if (levelEasy.isSelected()) {
            level = 1;

            if(keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD6)
            {
                mazeDisplayer.setImageFileNameCharacter("resources/Images/" + name + ".png");
            }
            else if(keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.NUMPAD4)
            {
                mazeDisplayer.setImageFileNameCharacter("resources/Images/" + name + "Left.jpg");
            }


        } else if (levelHard.isSelected()){
            level = -1;
            if(keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD4)
            {
                mazeDisplayer.setImageFileNameCharacter("resources/Images/" + name + "Left.jpg");
            }
            else if(keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.NUMPAD6)
            {
                mazeDisplayer.setImageFileNameCharacter("resources/Images/" + name + ".png");
            }
        }
        myViewModel.moveCharacter(keyEvent,level);

        if(myViewModel.gameWon() && !finishedAlready)
        {
            mediaPlayer.stop();
            playSpecificSound("resources/Audio/woohoo.wav");
            Alert EndGame = new Alert(Alert.AlertType.INFORMATION,"Congratulations!!! You have Won the Game, Dave is Resuced =)");
            EndGame.setTitle("Congratulations");
            EndGame.showAndWait();
            finishedAlready = true;


        }
//        mazeDisplayer.setCharacterPosition(ch);


//        System.out.println(characterRowNewPosition + "," + characterColumnNewPosition);
        this.characterRow.setValue(String.valueOf(mazeDisplayer.getCharacterPositionRow()));
        this.characterColumn.setValue(String.valueOf(mazeDisplayer.getCharacterPositionColumn()));
        keyEvent.consume();

    }

    private void playSpecificSound(String musicPath) {
        AudioClip sound = new AudioClip(new File(musicPath).toURI().toString());
        sound.play();
    }



    /*
    private void moveDave(KeyEvent keyEvent) {
        int characterRowCurrentPosition = mazeDisplayer.getCharacterPositionRow();
        int characterColumnCurrentPosition = mazeDisplayer.getCharacterPositionColumn();
        int characterRowNewPosition = characterRowCurrentPosition;
        int characterColumnNewPosition = characterColumnCurrentPosition;
        boolean isLegal = false;

        if (keyEvent.getCode() == KeyCode.UP) {
            if (levelEasy.isSelected() && checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1, "upOrDown")) {
                characterRowNewPosition = characterRowCurrentPosition - 1;
                isLegal = true;
            }
            if (levelHard.isSelected() && checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1, "upOrDown")) {
                characterRowNewPosition = characterRowCurrentPosition + 1;
                isLegal = true;

            }
        } else if (keyEvent.getCode() == KeyCode.DOWN) {
            if (levelEasy.isSelected() && checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1, "upOrDown")) {
                characterRowNewPosition = characterRowCurrentPosition + 1;
                isLegal = true;

            }
            if (levelHard.isSelected() && checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1, "upOrDown")) {
                characterRowNewPosition = characterRowCurrentPosition - 1;
                isLegal = true;
            }
        } else if (keyEvent.getCode() == KeyCode.RIGHT) {
            if (levelEasy.isSelected() && checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1, "leftOrRight")) {
                characterColumnNewPosition = characterColumnCurrentPosition + 1;
                mazeDisplayer.setImageFileNameCharacter("resources/Images/" + name + ".png");
                isLegal = true;
            }
            if (levelHard.isSelected() && checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1, "leftOrRight")) {
                mazeDisplayer.setImageFileNameCharacter("resources/Images/" + name + "Left.jpg");
                characterColumnNewPosition = characterColumnCurrentPosition - 1;
                isLegal = true;
            }
        } else if (keyEvent.getCode() == KeyCode.LEFT) {
            if (levelEasy.isSelected() && checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1, "leftOrRight")) {
                characterColumnNewPosition = characterColumnCurrentPosition - 1;
                mazeDisplayer.setImageFileNameCharacter("resources/Images/" + name + "Left.jpg");
                isLegal = true;
            }
            if (levelHard.isSelected() && checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1, "leftOrRight")) {
                characterColumnNewPosition = characterColumnCurrentPosition + 1;
                mazeDisplayer.setImageFileNameCharacter("resources/Images/" + name + ".png");
                isLegal = true;
            }
        }

        if (!isLegal && (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.RIGHT)) {
            playSound("resources/Audio/punchWall.mp3");
            characterRowNewPosition = 0;
            characterColumnNewPosition = 0;
        }

//        else if(!isLegal){
//            Alert alert = new Alert(Alert.AlertType.WARNING, "You pressed on illegal button.\n Please read the instructions and try again. ", ButtonType.OK);
//            alert.setTitle("WARNING");
//            alert.showAndWait();
//        }

        //Updates the MazeDisplayer
        mazeDisplayer.setCharacterPosition(characterRowNewPosition, characterColumnNewPosition);

        //Updates the labels
        System.out.println(characterRowNewPosition + "," + characterColumnNewPosition);
        this.characterRow.setValue(characterRowNewPosition + "");
        this.characterColumn.setValue(characterColumnNewPosition + "");
        keyEvent.consume();
    }*/




    public void changeStyleToBlue() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/blueWall.jpg");
        mazeDisplayer.redraw();
    }

    public void changeStyleToRed() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/redWall.jpg");
        mazeDisplayer.redraw();
    }

    public void changeStyleTobrown() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/brownWall.jpg");
        mazeDisplayer.redraw();
    }

    public void changeStyleToColorful() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/ColorfulWall.jpg");
        mazeDisplayer.redraw();
    }

    public void changeToDave(ActionEvent actionEvent) {
        name = "dave";
        mazeDisplayer.setImageFileNameCharacter("resources/Images/dave.png");
        mazeDisplayer.redraw();
    }

    public void changeToLily(ActionEvent actionEvent) {
        name = "lily";
        mazeDisplayer.setImageFileNameCharacter("resources/Images/lily.png");
        mazeDisplayer.redraw();
    }

    public void saveMazeView() {
        TextInputDialog saveDialog = new TextInputDialog("");
        saveDialog.setTitle("Save");
        saveDialog.setHeaderText("Please enter Maze name:");
        Optional<String> result = saveDialog.showAndWait();
        result.ifPresent((name) -> {
            try {
                finishToSave(name);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void finishToSave(String name) throws FileNotFoundException {
        myViewModel.viewModelSaveMazeToTheDisc(name);
    }

    public void setViewModel(MyViewModel viewModel) {
        myViewModel = viewModel;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == myViewModel)
        {
            Maze testMaze = myViewModel.getMaze();
            if(testMaze != null) {
                mazeDisplayer.setMaze(myViewModel.getMaze());
                mazeDisplayer.setCharacterPosition(myViewModel.getPosition());
                System.out.println("New Row=" + myViewModel.getPosition().getRowIndex() + ", New Col=" + myViewModel.getPosition().getColumnIndex());
            }
            Solution testSolution = myViewModel.getSolution();

            if(testSolution != null)
            {
                solDisplayer.drawSolution(myViewModel.getMaze());
            }


        }

    }

    public void redrawMazeOnZoom()
    {
        mazeDisplayer.redraw();
    }


}


