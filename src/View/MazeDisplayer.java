package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.MazeState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.PhantomReference;
import java.util.Random;

public class MazeDisplayer extends Canvas {


    private Maze maze;
    public int golRow;
    public int golCol;
    public double zoomFactor = 1.0D;



    public boolean randomValue = true;

    @FXML
    public SolutionDisplayer solDisplayer;

    private StringProperty ImageFileGolblet = new SimpleStringProperty("resources/Images/gobletMaze.png");
    private StringProperty ImageFileNameWall = new SimpleStringProperty("resources/Images/redWall.jpg");
    public boolean golToken = false;

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

    public void redraw(double zoomDelta) {
        if (maze != null) {
            zoomFactor = zoomDelta;
            double canvasHeight = getHeight() * zoomDelta;
            double canvasWidth = getWidth() * zoomDelta;
            double cellHeight = canvasHeight / maze.getMazeArray().length;
            double cellWidth = canvasWidth / maze.getMazeArray()[0].length;

            try {
                clearMaze();
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));

                //Draw Maze
                for (int i = 0; i < maze.getMazeArray().length; i++) {
                    for (int j = 0; j < maze.getMazeArray()[i].length; j++) {
                        if (maze.getMazeArray()[i][j] == 1) {
                            //graphicsContext2D.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            getGraphicsContext2D().drawImage(wallImage,  j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                    }
                }
                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                Image doorImage = new Image(new FileInputStream(ImageFileDoor.get()));
                getGraphicsContext2D().drawImage(doorImage, maze.getGoalPosition().getColumnIndex() * cellWidth, maze.getGoalPosition().getRowIndex() * cellHeight, cellWidth , cellHeight);
                if(!golToken) {
                    Image goblet = new Image(new FileInputStream(ImageFileGolblet.get()));
                    getGraphicsContext2D().drawImage(goblet, golCol * cellWidth, golRow * cellHeight, cellWidth, cellHeight);
                }


            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Image doesn't exist: %s",e.getMessage()));
                alert.show();
            }
        }
    }

    public void clearMaze() {
        GraphicsContext graphicsContext2D = getGraphicsContext2D();
        graphicsContext2D.clearRect(0, 0, getWidth(), getHeight()); //Clears the canvas
    }


//    public Position getrandomPosFromSol(){
//        Random randomGenerator = new Random();
//        int index = randomGenerator.nextInt(solDisplayer.getSol().getSolutionPath().size());
//        MazeState m = (MazeState)solDisplayer.getSol().getSolutionPath().get(index);
//
//        return null;
//    }

    public void isGobletVisible(int col, int row) {
        golToken = true;
        redraw(zoomFactor);
    }

    public Position getRandomPost(Position p) {
        golCol = p.getColumnIndex();
        golRow = p.getRowIndex();
        randomValue = false;
        return p;
    }
    public void setGolRow(int row){
        golRow = row;
    }
    public void setGolCol(int col){
        golCol = col;
    }

    public boolean hasMaze() {
        if (maze != null){
            return true;
        }
        return false;
    }


//    public void cleanGame() {
//        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight()); //Clears the canvas
//    }
}
