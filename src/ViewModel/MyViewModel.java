package ViewModel;

import Model.IModel;
import Model.MyModel;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyEvent;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;

    public MyViewModel(MyModel model) {
        this.model = model;
    }

//    public void generateMaze(int[] mazeSizes) {
//        this.m_model.generateMaze(mazeSizes[0], mazeSizes[1]);
//    }


    public Maze getMaze(){
        return model.getMaze();
    }

    public void generateMaze(int row,int col) {
        model.generateMaze(row,col);

    }
    @Override
    public void update(Observable o, Object arg) {
        if(o == model)
        {
            setChanged();
            notifyObservers();
        }

    }

    public void viewModelSaveMazeToTheDisc(String file) throws FileNotFoundException {
        model.modelSaveMazeToDisk(file);
    }


    public void moveCharacter(KeyEvent keyEvent,int level) {
        model.MoveCharacterEasy(keyEvent, level);
    }

    public Position getPosition(){
        return model.getPosition();
    }

    public void playSound(String s) {
        model.playSound(s);
    }

    public boolean gameWon() {
        return model.isWon();
    }

    public void closeGame() {
        model.closeGame();
    }

    public void solveGame() {
        model.solveGame();
    }

    public Solution getSolution() {
        return model.getSolution();
    }

    public boolean getGolToken(){
        return model.getGolToken();
    }


    public Position getrandomPos(){
       return model.getrandomPos();
    }

    public void setCharacterRowCurrentPosition(int characterRowCurrentPosition) {
        model.setCharacterRowCurrentPosition(characterRowCurrentPosition);
    }

    public void setCharacterColumnCurrentPosition(int characterColumnCurrentPosition) {
        model.setCharacterColumnCurrentPosition(characterColumnCurrentPosition);
    }

    public void loadMazeFromDisk(String pathToFileLoaded) {
        model.loadMaze(pathToFileLoaded);
    }

    public void moveChar(String s) {
        model.moveChar(s);
    }

    public void getLevel(int levelFromControl){
        model.getLevel(levelFromControl);
    }
}



