package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.MazeState;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class GameDisplayer  {
    public MazeDisplayer mazeDisplayer;
    public SolutionDisplayer solDisplayer;
    public DaveDisplayer daveDisplayer;

    public DaveDisplayer getDaveDisplayer() {
        return daveDisplayer;
    }

    public void setDaveDisplayer(DaveDisplayer daveDisplayer) {
        this.daveDisplayer = daveDisplayer;
    }

    private int characterPositionRow = 0;
    private int characterPositionColumn = 0;

//    public GameDisplayer() {
//        this.mazeDisplayer = new MazeDisplayer();
//        this.solDisplayer = new SolutionDisplayer();
//    }

    public void setCharacterPosition(Position currPos) {
        daveDisplayer.setCharacterPosition(currPos);
        characterPositionColumn = currPos.getColumnIndex();
        characterPositionRow = currPos.getRowIndex();
        redrawMaze();
    }
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public MazeDisplayer getMazeDisplayer() {
        return mazeDisplayer;
    }

    public void setMazeDisplayer(MazeDisplayer mzdsplyr) {
        this.mazeDisplayer = mzdsplyr;
    }

    public SolutionDisplayer getSolDisplayer() {
        return solDisplayer;
    }

    public void setSolDisplayer(SolutionDisplayer solDisplayer) {
        this.solDisplayer = solDisplayer;
    }

    public void drawGame()
    {
        solDisplayer.setVisible(false);
        solDisplayer.clearSolution();
        daveDisplayer.clearDave();
        redrawMaze();
    }

    public void redrawMaze()
    {
        getMazeDisplayer().getGraphicsContext2D().clearRect(0, 0, getMazeDisplayer().getWidth(), getMazeDisplayer().getHeight());
        solDisplayer.clearSolution();
        mazeDisplayer.redraw(characterPositionRow,characterPositionColumn);
        daveDisplayer.drawDave(mazeDisplayer.getMaze());
    }


    public void drawSolution(Maze mz)
    {
//        solDisplayer.setVisible(true);
//        redrawMaze();
        solDisplayer.drawSolution(mz);
    }

}
