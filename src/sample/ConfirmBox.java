package sample;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ConfirmBox {

    //Create variable
    static boolean answer;

    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        //Create two buttons
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        GridPane grid = new GridPane();
//        grid.setGridLinesVisible(true);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0,10,10,10));

        grid.add(label,1,0,5,1);

        grid.add(yesButton,1,5,1,1);
        grid.add(noButton,2,5,5,1);



        /*
        VBox layoutLines = new VBox(20);
        HBox layout = new HBox(20);

        //Add buttons
        layout.getChildren().add(label);
        layout.getChildren().addAll(yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        */

        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

}