package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.util.Observer;

import static javafx.application.Application.launch;

public class Main extends Application {

    @FXML
    private ChoiceBox<String> levelBox;
    Scene scene1, scene2;
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        primaryStage.setTitle("~Dangerous Dave - Maze!~");

        //Button 1

        Button button1 = new Button("Start");
        button1.setOnAction(e -> primaryStage.setScene(scene2));

        //Layout 1 - children laid out in vertical column
        VBox layout1 = new VBox(0);
        Image imageStart = new Image(new FileInputStream("resources/Images/start.gif"));
        ImageView sImage = new ImageView(imageStart);
        Image imageLoad = new Image(new FileInputStream("resources/Images/StartIcon.png"));
        ImageView lImage = new ImageView(imageLoad);
        Image imageMaze = new Image(new FileInputStream("resources/Images/mazeCover.png"));
        ImageView mImage = new ImageView(imageMaze);
        lImage.setOnMouseClicked(e -> primaryStage.setScene(scene2));

        layout1.getChildren().addAll(sImage,mImage,lImage);
        layout1.setAlignment(Pos.CENTER);

        // create a background image
        BackgroundFill backgroundColor = new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY);
        // create Background
        Background background = new Background(backgroundColor);
        // set background
        layout1.setBackground(background);
        scene1 = new Scene(layout1, 600, 600);

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = (Parent)fxmlLoader.load(this.getClass().getResource("MyView.fxml").openStream());

//        scene.getStylesheets().clear();

        MyViewController controller = fxmlLoader.getController();
        controller.initialize(viewModel);
        controller.setViewModel(viewModel);
        viewModel.addObserver(controller);
        controller.createLevel();
        scene2 = new Scene(root,800.0D, 600.0D);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(650);
        primaryStage.setScene(scene1);
        scene2.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());

//        levelBox.getItems().add("gal");
//        levelBox.setValue("gal");
//        choiceBox.getItems().addAll("Easy", "Hard");
        controller.playMusic();
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            try {
                controller.exitFromTheGame();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });
    }


}
