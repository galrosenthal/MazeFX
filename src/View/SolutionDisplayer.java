package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.io.FileInputStream;

public class SolutionDisplayer extends Canvas {


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

    public void drawSolution(Maze maze)
    {
        if(maze != null && visibleMaze)
        {

            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getMazeArray().length;
            double cellWidth = canvasWidth / maze.getMazeArray()[0].length;
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
        getGraphicsContext2D().clearRect(0,0,getHeight(),getWidth());
        getGraphicsContext2D().beginPath();
//        sol = null;
    }
}
