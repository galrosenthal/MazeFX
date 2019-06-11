package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.io.FileInputStream;

public class SolutionDisplayer extends Canvas {


    private double offSetWidth;
    private double offSetHeight;

    public boolean isVisibleMaze() {
        return visibleMaze;
    }

    public void setVisibleMaze(boolean visibleMaze) {
        this.visibleMaze = visibleMaze;
    }

    private boolean visibleMaze = false;
    private StringProperty solutionImageName = new SimpleStringProperty("resources/Images/diamond.png");

    public String getSolutionImageName() {
        return solutionImageName.get();
    }

    public StringProperty solutionImageNameProperty() {
        return solutionImageName;
    }

    public void setSolutionImageName(String solutionImageName) {
        this.solutionImageName.set(solutionImageName);
    }

    private Solution sol;

//    public SolutionDisplayer() {
//        this.sol = sol;
//    }



    public Solution getSol() {
        return sol;
    }

    public void setSol(Solution sol) {
        this.sol = sol;
    }

    public void drawSolution(Maze maze, double zoomFactor, int characterPositionColumn, int characterPositionRow)
    {
        if(maze != null && visibleMaze)
        {

            double canvasHeight = getHeight() * zoomFactor;
            double canvasWidth = getWidth() * zoomFactor;
            double cellHeight = canvasHeight / maze.getMazeArray().length;
            double cellWidth = canvasWidth / maze.getMazeArray()[0].length;

            if (zoomFactor <= 1.0D) {
                offSetWidth = 0.0D;
                offSetHeight = 0.0D;
            } else {
                offSetWidth = (double)(-1 * characterPositionColumn) * cellWidth * (zoomFactor - 1.0D);
                offSetHeight = (double)(-1 * characterPositionRow) * cellHeight * (zoomFactor - 1.0D);
            }

            try {
                Image solImage = new Image(new FileInputStream(solutionImageName.get()));
//                clearSolution();
                for (AState state :
                        sol.getSolutionPath()) {

                    MazeState myState = (MazeState)state;
                    getGraphicsContext2D().drawImage(solImage, myState.getPosition().getColumnIndex() * cellWidth, myState.getPosition().getRowIndex() * cellHeight, cellWidth , cellHeight);
                }

            }
            catch (Exception exc)
            {
                exc.printStackTrace();
            }
        }
    }

    public void clearSolution()
    {
        getGraphicsContext2D().clearRect(0,0,getWidth(),getHeight());
        getGraphicsContext2D().beginPath();
//        sol = null;
    }

}
