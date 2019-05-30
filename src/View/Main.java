package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application {

    @FXML
    private ChoiceBox<String> levelBox;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("~Dangerous Dave - Maze!~");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = (Parent)fxmlLoader.load(this.getClass().getResource("MyView.fxml").openStream());
//        scene.getStylesheets().clear();
//
//        button = new Button();
//        button.setText("Start");
//        StackPane layout = new StackPane();
//        layout.getChildren().add(button);

//        primaryStage.show();
//        scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());

//        primaryStage.setScene(scene);
//        MyViewController controller =fxmlLoader.getController();

        MyViewController controller = fxmlLoader.getController();
        controller.createLevel();
//
//         levelBox = new ChoiceBox<>();

        Scene scene = new Scene(root,800.0D, 650.0D);
        primaryStage.setScene(scene);

//        levelBox.getItems().add("gal");
//        levelBox.setValue("gal");
//        choiceBox.getItems().addAll("Easy", "Hard");
        controller.playMusic();
        primaryStage.show();
    }


}
