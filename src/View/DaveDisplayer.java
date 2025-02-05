package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;

import java.awt.*;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class DaveDisplayer extends Canvas {


    private StringProperty ImageFileNameCharacter = new SimpleStringProperty("resources/Images/dave.png");

    private int characterPositionRow = 0;
    private int characterPositionColumn = 0;
    private double offSetWidth;
    private double offSetHeight;


    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void setCharacterPosition(Position currPos) {
        characterPositionRow = currPos.getRowIndex();
        characterPositionColumn = currPos.getColumnIndex();

    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public StringProperty imageFileNameCharacterProperty() {
        return ImageFileNameCharacter;
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public void drawDave(Maze maze , double zoomFactor)
    {
        if(maze != null)
        {
            double canvasHeight = getHeight() * zoomFactor;
            double canvasWidth = getWidth() * zoomFactor;
            double cellHeight = canvasHeight / maze.getMazeArray().length;
            double cellWidth = canvasWidth / maze.getMazeArray()[0].length;

            if (zoomFactor <= 1.0D) {
                offSetWidth = 0.0D;
                offSetHeight = 0.0D;
            } else {
                offSetWidth = (double)(-1 * characterPositionColumn) * cellWidth * (zoomFactor-1.0D);
                offSetHeight = (double)(-1 * characterPositionRow) * cellHeight * (zoomFactor-1.0D);
            }

            try {
                clearDave();
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                getGraphicsContext2D().drawImage(characterImage,  characterPositionColumn * cellWidth + offSetWidth/zoomFactor, characterPositionRow * cellHeight + offSetHeight/zoomFactor, cellWidth , cellHeight );
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    public void clearDave() {
        getGraphicsContext2D().clearRect(0,0,getWidth(),getHeight());
    }
}
