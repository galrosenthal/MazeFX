package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {


    private Maze maze;
    public int golRow;
    public int golCol;
    public double zoomFactor = 1.0D;

    public int mh;
    public int mw;

    public boolean randomValue = true;

    @FXML
    public SolutionDisplayer solDisplayer;

    private StringProperty ImageFileGolblet = new SimpleStringProperty("resources/Images/gobletMaze.png");
    private StringProperty ImageFileNameWall = new SimpleStringProperty("resources/Images/redWall.jpg");
    public boolean golToken = false;
    private double offSetWidth;
    private double offSetHeight;

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
        this.mh = maze.getHeight();
        this.mw = maze.getWidth();
//        redraw(characterPositionRow,characterPositionColumn);
    }

    public void redraw(double zoomDelta, int characterPositionColumn, int characterPositionRow) {
        if (maze != null) {
            zoomFactor = zoomDelta;
            double canvasHeight = getHeight();
            double canvasWidth = getWidth() ;
            double cellHeight = (canvasHeight / maze.getHeight()) * zoomDelta;
            double cellWidth = (canvasWidth / maze.getWidth()) * zoomDelta;
            System.out.println("CanvasH=" + canvasHeight);
            System.out.println("CanvasW=" + canvasWidth);
            System.out.println("MazeH=" + maze.getHeight());
            System.out.println("MazeW=" + maze.getWidth());

            int numOfRows = (int)((canvasHeight/maze.getHeight())/zoomFactor);
            int numOfCols = (int)((canvasWidth/maze.getWidth()) /zoomFactor);
            System.out.println("NumOfRows=" + numOfRows);
            System.out.println("NumOfCols=" + numOfCols);

            if(numOfRows < 0)
                numOfRows = 1;
            if(numOfCols < 0)
                numOfCols = 1;

            int startRowToDisplay = characterPositionRow - (numOfRows / 2);
            int startColToDisplay = characterPositionColumn - (numOfCols / 2);
            System.out.println("StartRow=" + startRowToDisplay);
            System.out.println("StartCols=" + startColToDisplay);
            int endRowToDisplay = numOfRows - startRowToDisplay ;
            int endColToDisplay = numOfCols - startColToDisplay ;

            if(startRowToDisplay <= 0)
                startRowToDisplay = 0;
            if(startColToDisplay <= 0)
                startColToDisplay = 0;
            if(endRowToDisplay >= maze.getHeight())
                endRowToDisplay = maze.getHeight();
            if(endColToDisplay > maze.getWidth())
                endColToDisplay = maze.getWidth();


            System.out.println("Start Row To Display = " + startRowToDisplay + "| End Row To Display = " + endRowToDisplay);
            System.out.println("Start Col To Display = " + startColToDisplay + "| End Col To Display = " + endColToDisplay);



            if (zoomFactor <= 1.0D) {
                offSetWidth = 0.0D;
                offSetHeight = 0.0D;
            }
            else
            {
                offSetWidth = (double)(-1 * characterPositionColumn) * cellWidth * (zoomFactor - 1.0D);
                offSetHeight = (double)(-1 * characterPositionRow) * cellHeight * (zoomFactor - 1.0D);
            }

            System.out.println("OffsetW = " + offSetWidth);
            System.out.println("OffsetH = " + offSetHeight);


            try {
                clearMaze();
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));

                //Draw Maze
                for (int i = 0; i < maze.getMazeArray().length; i++) {
                    for (int j = 0; j < maze.getMazeArray()[i].length; j++) {
                        if (maze.getMazeArray()[i][j] == 1) {
                            //graphicsContext2D.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            getGraphicsContext2D().drawImage(wallImage,  (double) j * cellWidth + offSetWidth/zoomFactor, (double) i * cellHeight + offSetHeight/zoomFactor, cellWidth, cellHeight);
                        }
                    }
                }
                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                Image doorImage = new Image(new FileInputStream(ImageFileDoor.get()));
                getGraphicsContext2D().drawImage(doorImage, maze.getGoalPosition().getColumnIndex() * cellWidth + offSetWidth/zoomFactor, maze.getGoalPosition().getRowIndex() * cellHeight + offSetHeight/zoomFactor, cellWidth , cellHeight);
                if(!golToken) {
                    Image goblet = new Image(new FileInputStream(ImageFileGolblet.get()));
                    getGraphicsContext2D().drawImage(goblet, golCol * cellWidth + offSetWidth/zoomFactor, golRow * cellHeight + offSetHeight/zoomFactor, cellWidth, cellHeight);
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
        redraw(zoomFactor, col, row);
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
