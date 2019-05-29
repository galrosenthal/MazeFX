package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application {
   Button button;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("~Dangerous Dave - Maze!~");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = (Parent)fxmlLoader.load(this.getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root,800.0D, 650.0D);
//        scene.getStylesheets().clear();
//
//        button = new Button();
//        button.setText("Start");
//        StackPane layout = new StackPane();
//        layout.getChildren().add(button);
        primaryStage.setScene(scene);
        MyViewController controller = (MyViewController)fxmlLoader.getController();

//        primaryStage.show();
//        scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
