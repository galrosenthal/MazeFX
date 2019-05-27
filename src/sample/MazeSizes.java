package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MazeSizes {

    private static final String title = "Maze Sizes";
    private static final String message = "Please Enter the maze Sizes:";
    private static final String rowSizeDefault = "50";
    private static final String colSizeDefault = "50";

    public static int[] getMazeSizes()
    {
        int[] mazeSizes = new int[2];

        Stage newWindow = new Stage();
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setTitle(title);
        newWindow.setMinWidth(250);
        Label userGuideText = new Label();
        userGuideText.setText(message);

        TextField rowSize = new TextField(rowSizeDefault);
        TextField colSize = new TextField(colSizeDefault);

        Button createMaze = new Button("Create The Maze");

        Label valueError = new Label();
        valueError.setText("Wrong Values, Please Enter Maze Sizes again!");
        valueError.setVisible(false);

        createMaze.setOnAction(e -> {
            String rowSizeFromUser = rowSize.getText();
            String colSizeFromUser = colSize.getText();

            if(rowSizeFromUser.isEmpty() || colSizeFromUser.isEmpty() || Integer.parseInt(rowSizeFromUser) > 300 ||
                    Integer.parseInt(rowSizeFromUser) < 0 || Integer.parseInt(colSizeFromUser) > 300 || Integer.parseInt(colSizeFromUser) < 0)
            {
                    valueError.setVisible(true);
            }
            else
            {
                valueError.setText("Well Done, Creating Your Maze");
                valueError.setVisible(true);
                mazeSizes[0] = Integer.parseInt(rowSizeFromUser);
                mazeSizes[1] = Integer.parseInt(colSizeFromUser);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                newWindow.close();
            }
        });

        GridPane grid = new GridPane();
//        grid.setGridLinesVisible(true);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0,10,10,10));

        grid.add(userGuideText,1,0,5,1);
        grid.add(rowSize, 1,1,5,1);
        grid.add(colSize, 1,2,5,1);
        grid.add(createMaze,1,3,5,1);
        grid.add(valueError,1,5,5,1);





        /*
        VBox layoutLines = new VBox(20);
        HBox layout = new HBox(20);

        //Add buttons
        layout.getChildren().add(label);
        layout.getChildren().addAll(yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        */

        Scene scene = new Scene(grid);
        newWindow.setScene(scene);
        newWindow.showAndWait();



        return mazeSizes;
    }
}
