package View;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class StartPage {


    @FXML
    private GridPane startGrid;

    @FXML
    private ImageView smallIV;
    @FXML
    private ImageView mediumIV;
    @FXML
    private ImageView largeIV;
    @FXML
    private ImageView startButton;

    private Stage primary;

    private Scene nextScene;
    private MyViewController primCntrl;
    int buttonClicked = 0;

    public void Initialize(Stage prmStg, Scene scene, MyViewController primControl)
    {
        primary = prmStg;
        nextScene = scene;
        primCntrl = primControl;
    }


    public void startOnClick()
    {
        if(buttonClicked == 1)
        {
            primCntrl.setRow(10);
            primCntrl.setCol(10);
            primary.setScene(nextScene);
        }

        if(buttonClicked == 2)
        {
            primCntrl.setRow(25);
            primCntrl.setCol(25);
            primary.setScene(nextScene);
        }

        if(buttonClicked == 3)
        {
            primCntrl.setRow(50);
            primCntrl.setCol(50);
            primary.setScene(nextScene);
        }



    }

    public void smallOnClick()
    {

        restoreFirstImages();
        smallIV.setImage(new Image("Images/small-crop-wave.gif"));
//        smallIV.setFitWidth(80);
        smallIV.setFitHeight(30);
        buttonClicked = 1;
    }

    public void mediumOnClick()
    {
        restoreFirstImages();

        mediumIV.setImage(new Image("Images/Medium-crop-wave.gif"));
        mediumIV.setFitWidth(180);
        mediumIV.setFitHeight(50);
        buttonClicked = 2;

    }

    public void largeOnClick()
    {
        restoreFirstImages();
        largeIV.setImage(new Image("Images/Large-crop-wave.gif"));
        largeIV.setFitHeight(35);
        buttonClicked = 3;
    }

    private void restoreFirstImages()
    {
        smallIV.setImage(new Image("Images/small-crop.gif"));
        smallIV.setFitWidth(130);
        smallIV.setFitHeight(50);
        mediumIV.setImage(new Image("Images/Medium-crop.gif"));
        mediumIV.setFitWidth(130);
        mediumIV.setFitHeight(50);
        largeIV.setImage(new Image("Images/Large-crop.gif"));
        largeIV.setFitWidth(130);
        largeIV.setFitHeight(50);

        buttonClicked = 0;
    }
}