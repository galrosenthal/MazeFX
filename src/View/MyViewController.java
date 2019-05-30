package View;

import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MyViewController implements IView {


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
    private MenuItem exitButton;

    @FXML
    public MazeDisplayer mazeDisplayer;

    public Label locationLabel;




    public StringProperty characterRow = new SimpleStringProperty();
    public StringProperty characterColumn = new SimpleStringProperty();

    public void initialize(URL location, ResourceBundle resources) {
        //Set Binding for Properties
        locationLabel.textProperty().bind( characterRow);
        locationLabel.textProperty().bind( characterColumn);

    }

    //    @FXML
//    private Label valueError;

//    @FXML
//    private ChoiceBox<String> levelType;


//    @FXML
//    public void initChoiceBox(){
//        levelType.setValue("Easy");
//        levelType.setItems(types);
//
//    }
int[][] mazeData = { // a stub...
//        {0, 1, 1, 1,},
//        {0, 0, 0, 0},
//        {0, 0, 1, 1},
//        {1, 1, 1, 0}
        {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1},
        {0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1},
        {1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1},
        {1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1},
        {1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1},
        {1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1},
        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1}
};
    public void generateMaze() {
        //int rows = Integer.valueOf(txtfld_rowsNum.getText());
        //int columns = Integer.valueOf(txtfld_columnsNum.getText());
        //this.mazeDisplayer.setMaze(getRandomMaze(rows,columns));
        this.mazeDisplayer.setMaze(mazeData);
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

    public void exitFromTheGame() throws InterruptedException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            playSound("resources/Audio/goodBye.mp3");
            Thread.sleep(850);
            System.exit(0);
        }
    }

    public void playSound(String fileName) {
        AudioClip sound = new AudioClip(new File(fileName).toURI().toString());
        sound.play();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    public void KeyPressedEasy(KeyEvent keyEvent) {
        int characterRowCurrentPosition = mazeDisplayer.getCharacterPositionRow();
        int characterColumnCurrentPosition = mazeDisplayer.getCharacterPositionColumn();
        int characterRowNewPosition = characterRowCurrentPosition;
        int characterColumnNewPosition = characterColumnCurrentPosition;

        if (keyEvent.getCode() == KeyCode.UP && mazeData[characterRowCurrentPosition - 1][characterColumnCurrentPosition] != 1 && characterRowCurrentPosition - 1 >= 0 ) {
            characterRowNewPosition = characterRowCurrentPosition - 1;
        }
        else if (keyEvent.getCode() == KeyCode.DOWN && mazeData[characterRowCurrentPosition + 1][characterColumnCurrentPosition] != 1 && characterRowCurrentPosition + 1 < mazeData[0].length) {
            characterRowNewPosition = characterRowCurrentPosition + 1;
        }
        else if (keyEvent.getCode() == KeyCode.RIGHT && mazeData[characterRowCurrentPosition][characterColumnCurrentPosition + 1] != 1 && characterColumnCurrentPosition + 1 < mazeData.length) {
            characterColumnNewPosition = characterColumnCurrentPosition + 1;
        }
        else if (keyEvent.getCode() == KeyCode.LEFT && mazeData[characterRowCurrentPosition][characterColumnCurrentPosition - 1] != 1 && characterColumnCurrentPosition - 1 >= 0) {
            characterColumnNewPosition = characterColumnCurrentPosition -1;
        }
        else{
            playSound("resources/Audio/punchWall.mp3");
            characterRowNewPosition = 0;
            characterColumnNewPosition = 0;
        }


        //Updates the MazeDisplayer
        mazeDisplayer.setCharacterPosition(characterRowNewPosition, characterColumnNewPosition);

        //Updates the labels
        this.characterRow.set(characterRowNewPosition + "");
        this.characterColumn.set(characterColumnNewPosition + "");
        keyEvent.consume();
    }



    public void KeyPressedHard(KeyEvent keyEvent) {
        int characterRowCurrentPosition = mazeDisplayer.getCharacterPositionRow();
        int characterColumnCurrentPosition = mazeDisplayer.getCharacterPositionColumn();
        int characterRowNewPosition = characterRowCurrentPosition;
        int characterColumnNewPosition = characterColumnCurrentPosition;

        if (keyEvent.getCode() == KeyCode.UP && mazeData[characterRowCurrentPosition + 1][characterColumnCurrentPosition] != 1 && characterRowCurrentPosition + 1 >= 0 ) {
            characterRowNewPosition = characterRowCurrentPosition + 1;
        }
        else if (keyEvent.getCode() == KeyCode.DOWN && mazeData[characterRowCurrentPosition - 1][characterColumnCurrentPosition] != 1 && characterRowCurrentPosition - 1 < mazeData[0].length) {
            characterRowNewPosition = characterRowCurrentPosition - 1;
        }
        else if (keyEvent.getCode() == KeyCode.RIGHT && mazeData[characterRowCurrentPosition][characterColumnCurrentPosition - 1] != 1 && characterColumnCurrentPosition - 1 < mazeData.length) {
            characterColumnNewPosition = characterColumnCurrentPosition - 1;
        }
        else if (keyEvent.getCode() == KeyCode.LEFT && mazeData[characterRowCurrentPosition][characterColumnCurrentPosition + 1] != 1 && characterColumnCurrentPosition + 1 >= 0) {
            characterColumnNewPosition = characterColumnCurrentPosition + 1;
        }
        else{
            playSound("resources/Audio/punchWall.mp3");
            characterRowNewPosition = 0;
            characterColumnNewPosition = 0;
        }


        //Updates the MazeDisplayer
        mazeDisplayer.setCharacterPosition(characterRowNewPosition, characterColumnNewPosition);

        //Updates the labels
        this.characterRow.set(characterRowNewPosition + "");
        this.characterColumn.set(characterColumnNewPosition + "");
        keyEvent.consume();
    }


}
