package Model;

import algorithms.mazeGenerators.Maze;

import javax.swing.*;

import algorithms.mazeGenerators.Position;
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
    boolean isWon(Position nextPos);
    void playSound(String s);

//    void generateMaze(int height, int width);
}
