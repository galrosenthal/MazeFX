package ViewModel;

import Model.IModel;
import Model.MyModel;
import View.MazeDisplayer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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



    public void generateMaze() {

    }
    @Override
    public void update(Observable o, Object arg) {

    }

    public void viewModelSaveMazeToTheDisc(String file) throws FileNotFoundException {
        model.modelSaveMazeToDisk(file);
    }



}
