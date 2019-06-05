package Model;

import algorithms.mazeGenerators.Maze;

import javax.swing.*;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.io.FileNotFoundException;

public interface IModel {
    void generateMaze(int height, int width);

    void modelSaveMazeToDisk(String file);

    Maze getMaze();

    void MoveCharacterEasy(KeyEvent key, int level);

    Position getPosition();
    boolean isWon();
    void playSound(String s);

    void closeGame();

    void solveGame();

    Solution getSolution();

    void loadMaze(String Path);

    Position getrandomPos();

    boolean getGolToken();

    void setCharacterRowCurrentPosition(int characterRowCurrentPosition);

    void setCharacterColumnCurrentPosition(int characterColumnCurrentPosition);

    void moveChar(String s);

    void getLevel(int levelFromControl);

    String getConfigFile();

//    void generateMaze(int height, int width);
}
