package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Observer;

import static javafx.application.Application.launch;

public class Main extends Application {

    @FXML
    private ChoiceBox<String> levelBox;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);

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

//        primaryStage.setScene(scene);
//        MyViewController controller =fxmlLoader.getController();
        MyViewController controller = fxmlLoader.getController();
        controller.setViewModel(viewModel);
        viewModel.addObserver(controller);
        controller.createLevel();
        Scene scene = new Scene(root,800.0D, 600.0D);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());

//        levelBox.getItems().add("gal");
//        levelBox.setValue("gal");
//        choiceBox.getItems().addAll("Easy", "Hard");
        controller.playMusic();
        primaryStage.show();
    }


}
