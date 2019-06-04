package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.PhantomReference;

public class MazeDisplayer extends Canvas {


    private Maze maze;



    private StringProperty ImageFileNameWall = new SimpleStringProperty("resources/Images/redWall.jpg");

    public String getImageFileDoor() {
        return ImageFileDoor.get();
    }

    public StringProperty imageFileDoorProperty() {
        return ImageFileDoor;
    }

    public void setImageFileDoor(String imageFileDoor) {
        this.ImageFileDoor.set(imageFileDoor);
    }

    private StringProperty ImageFileDoor = new SimpleStringProperty("resources/Images/doorImage.jpeg");


    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }


    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
//        redraw(characterPositionRow,characterPositionColumn);
    }

    public void redraw(int characterPositionRow, int characterPositionColumn) {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getMazeArray().length;
            double cellWidth = canvasWidth / maze.getMazeArray()[0].length;

            try {
                GraphicsContext graphicsContext2D = getGraphicsContext2D();
                graphicsContext2D.clearRect(0, 0, getWidth(), getHeight()); //Clears the canvas
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));

                //Draw Maze
                for (int i = 0; i < maze.getMazeArray().length; i++) {
                    for (int j = 0; j < maze.getMazeArray()[i].length; j++) {
                        if (maze.getMazeArray()[i][j] == 1) {
                            //graphicsContext2D.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            graphicsContext2D.drawImage(wallImage,  j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                    }
                }

                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                Image doorImage = new Image(new FileInputStream(ImageFileDoor.get()));
                graphicsContext2D.drawImage(doorImage, maze.getGoalPosition().getColumnIndex() * cellWidth, maze.getGoalPosition().getRowIndex() * cellHeight, cellWidth , cellHeight);


            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Image doesn't exist: %s",e.getMessage()));
                alert.show();
            }
        }
    }



//    public void cleanGame() {
//        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight()); //Clears the canvas
//    }
}
