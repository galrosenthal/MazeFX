package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.MazeState;

import java.awt.*;

public class GameDisplayer  {
    MazeDisplayer mazeDisplayer;
    SolutionDisplayer solDisplayer;

    public GameDisplayer() {
        this.mazeDisplayer = new MazeDisplayer();
        this.solDisplayer = new SolutionDisplayer();
    }

    public MazeDisplayer getMazeDisplayer() {
        return mazeDisplayer;
    }

    public void setGameDisplayer(MazeDisplayer mzdsplyr) {
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
        mazeDisplayer.redraw();
    }

    public void drawSolution(Maze mz)
    {
        solDisplayer.drawSolution(mz);
    }
}
