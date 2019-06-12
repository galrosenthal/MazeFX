package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

public class GameDisplayer  {
    public MazeDisplayer mazeDisplayer;
    public SolutionDisplayer solDisplayer;
    public DaveDisplayer daveDisplayer;
    private double zoomFactor = 1.0D;
    private double offSetWidth;
    private double offSetHeight;

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

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
//        redrawMaze();
        daveDisplayer.drawDave(mazeDisplayer.getMaze(),zoomFactor);
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

//    public void drawGame()
//    {
//        solDisplayer.setVisible(false);
//        solDisplayer.clearSolution();
//        daveDisplayer.clearDave();
//        redrawMaze();
//        drawSolution(mazeDisplayer.getMaze());
//    }

    public void redrawMaze()
    {
        mazeDisplayer.clearMaze();
        solDisplayer.clearSolution();

        mazeDisplayer.redraw(zoomFactor, characterPositionColumn, characterPositionRow);
//        daveDisplayer.drawDave(mazeDisplayer.getMaze(),zoomFactor);
        drawSolution(mazeDisplayer.getMaze());
    }

    public void drawOnZoom()
    {
        mazeDisplayer.clearMaze();
        daveDisplayer.clearDave();
        solDisplayer.clearSolution();


        mazeDisplayer.redraw(zoomFactor, characterPositionColumn, characterPositionRow);
        daveDisplayer.drawDave(mazeDisplayer.getMaze(),zoomFactor);
        solDisplayer.drawSolution(mazeDisplayer.getMaze(),zoomFactor, characterPositionColumn, characterPositionRow);
    }


    public void drawSolution(Maze mz)
    {
//        solDisplayer.setVisible(true);
//        redrawMaze();
        if(solDisplayer.getSol() != null)
            solDisplayer.drawSolution(mz,zoomFactor, characterPositionColumn, characterPositionRow);
    }

}
