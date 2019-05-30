package Model;

import Server.*;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.search.MazeState;
import algorithms.search.Solution;

import java.awt.image.renderable.RenderableImage;
import java.util.Observable;

public class MyModel extends Observable implements IModel {
    private  Maze maze;
    private Server generator;
    private Server solver;
    public MyModel(){
        int generatePortNum = Integer.parseInt(Configurations.getInstance().getProperty("GeneratorTypePort"));
        int generateInterval = Integer.parseInt(Configurations.getInstance().getProperty("GeneratorTypeListeningInterval"));
        int solverPortNum = Integer.parseInt(Configurations.getInstance().getProperty("SolverTypePort"));
        int solverInterval = Integer.parseInt(Configurations.getInstance().getProperty("SolverTypeListeningInterval"));
        solver = new Server(solverPortNum, solverInterval, new ServerStrategySolveSearchProblem());
        solver.start();
        generator = new Server(generatePortNum,generateInterval,new ServerStrategyGenerateMaze());
        generator.start();
    }


    @Override
    public void generateMaze(int height, int width) { }
}
