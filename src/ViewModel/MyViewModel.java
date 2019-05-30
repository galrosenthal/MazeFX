package ViewModel;

import Model.IModel;
import View.MazeDisplayer;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel m_model;

//    public void generateMaze(int[] mazeSizes) {
//        this.m_model.generateMaze(mazeSizes[0], mazeSizes[1]);
//    }

    private MazeDisplayer mazeDisplayer;

    public void generateMaze() {

    }
    @Override
    public void update(Observable o, Object arg) {

    }
}
