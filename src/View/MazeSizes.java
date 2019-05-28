package View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MazeSizes {

    private static final String title = "Maze Sizes";
    private static final String message = "Please Enter the maze Sizes:";
    private static final String rowSizeDefault = "50";
    private static final String colSizeDefault = "50";
    private static TextField rowSize;
    private static TextField colSize;
    private static boolean isUsed;

    public static int[] getMazeSizes()
    {
        int[] mazeSizes = new int[2];

        Stage newWindow = new Stage();
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setTitle(title);
        newWindow.setMinWidth(250);
        Label userGuideText = new Label();
        userGuideText.setText(message);

        rowSize = new TextField(rowSizeDefault);
        colSize = new TextField(colSizeDefault);

        HBox rowNcol = new HBox(10);

        rowNcol.getChildren().addAll(rowSize,colSize);

        Button createMaze = new Button("Create The Maze");

        Label valueError = new Label();
        valueError.setText("Wrong Values, Please Enter Maze Sizes again!");
        valueError.setVisible(false);


        createMaze.setOnAction(e -> {

            getValues(mazeSizes, newWindow, valueError);

        });

        GridPane grid = new GridPane();
//        grid.setGridLinesVisible(true);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0,10,10,10));

        grid.add(userGuideText,1,0,5,1);
        grid.add(rowNcol, 1,1,5,1);
        grid.add(createMaze,1,3,5,1);
        grid.add(valueError,1,5,5,1);




        Scene scene = new Scene(grid);
        newWindow.setScene(scene);
        newWindow.setOnCloseRequest(e -> {
            e.consume();
            newWindow.close();
            isUsed = false;
        });
        newWindow.showAndWait();



        if(!isUsed)
            return null;
        else
            return mazeSizes;
    }

    private static void getValues(int[] mazeSizes, Stage newWindow, Label valueError) {
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
            newWindow.close();
            isUsed = true;
        }
    }
}
