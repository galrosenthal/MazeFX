package Model;

import Client.*;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;

import java.awt.image.renderable.RenderableImage;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private Server generator;
    private Server solver;
    private int characterRowCurrentPosition;
    private int characterColumnCurrentPosition;

    public MyModel() {

        int generatePortNum = Integer.parseInt(Configurations.getInstance().getProperty("GeneratorTypePort"));
        int generateInterval = Integer.parseInt(Configurations.getInstance().getProperty("GeneratorTypeListeningInterval"));
        int solverPortNum = Integer.parseInt(Configurations.getInstance().getProperty("SolverTypePort"));
        int solverInterval = Integer.parseInt(Configurations.getInstance().getProperty("SolverTypeListeningInterval"));
        solver = new Server(solverPortNum, solverInterval, new ServerStrategySolveSearchProblem());
        solver.start();
        generator = new Server(generatePortNum,generateInterval,new ServerStrategyGenerateMaze());
        generator.start();
    }


    public boolean isWon(Position pos)
    {
        return pos.equals(maze.getGoalPosition());
    }

    @Override
    public void MoveCharacterEasy(KeyEvent keyEvent, int level) {

        boolean isLegal = false;

        if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.NUMPAD8) {
            if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1*level, "upOrDown")) {
                characterRowCurrentPosition = characterRowCurrentPosition - 1*level;
                isLegal = true;
            }

        } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.NUMPAD2) {
            if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1*level, "upOrDown")) {
                characterRowCurrentPosition = characterRowCurrentPosition + 1*level;
                isLegal = true;

            }

        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD6) {
            if ( checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1*level, "leftOrRight")) {
                characterColumnCurrentPosition = characterColumnCurrentPosition + 1*level;
                isLegal = true;
            }

        } else if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.NUMPAD4) {
            if ( checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1*level, "leftOrRight")) {
                characterColumnCurrentPosition = characterColumnCurrentPosition - 1*level;
                isLegal = true;
            }

        } else if (keyEvent.getCode() == KeyCode.NUMPAD7) {
            if ( checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1*level, "leftOrRight")) {
                characterColumnCurrentPosition = characterColumnCurrentPosition + 1*level;
                isLegal = true;
            }

        }

        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.UP
                || keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD8 || keyEvent.getCode() == KeyCode.NUMPAD2
                || keyEvent.getCode() == KeyCode.NUMPAD4 || keyEvent.getCode() == KeyCode.NUMPAD6)
        {
            if (!isLegal) {
                playSound("resources/Audio/punchWall.mp3");
                characterRowCurrentPosition = maze.getStartPosition().getRowIndex();
                characterColumnCurrentPosition = maze.getStartPosition().getColumnIndex();
            }
        }

        setChanged();
        notifyObservers();


        //Updates the MazeDisplayer

        //Updates the labels


    }

    public Position getPosition(){
        Position curr = new Position(characterRowCurrentPosition,characterColumnCurrentPosition);
        return curr;
    }



    public boolean checkIfLegalMove(int characterRowCurrentPosition, int characterColumnCurrentPosition, int num, String side) {
        System.out.println("Row=" + characterRowCurrentPosition + ", Col=" + characterColumnCurrentPosition + ", num=" + num);


        if(!(characterRowCurrentPosition + num >= maze.getMazeArray().length || characterRowCurrentPosition + num < 0))
        {
            if (side.equals("upOrDown") && maze.getMazeArray()[characterRowCurrentPosition + num][characterColumnCurrentPosition] != 1
                    && maze.getMazeArray()[characterRowCurrentPosition + num][characterColumnCurrentPosition] >= 0
                    && maze.getMazeArray()[characterRowCurrentPosition + num][characterColumnCurrentPosition] < maze.getMazeArray()[0].length) {
                return true;
            }
        }
        if( !(characterColumnCurrentPosition + num >= maze.getMazeArray()[0].length || characterColumnCurrentPosition + num < 0)) {
            if (side.equals("leftOrRight") && maze.getMazeArray()[characterRowCurrentPosition][characterColumnCurrentPosition + num] != 1
                    && maze.getMazeArray()[characterRowCurrentPosition][characterColumnCurrentPosition + num] >= 0
                    && maze.getMazeArray()[characterRowCurrentPosition][characterColumnCurrentPosition + num] < maze.getMazeArray().length
                    ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public void generateMaze(int height, int width) {
        callClientGenerateMaze(height,width);
        characterRowCurrentPosition = maze.getStartPosition().getRowIndex();
        characterColumnCurrentPosition = maze.getStartPosition().getColumnIndex();
        setChanged();
        notifyObservers();


    }

    private void callClientGenerateMaze(final int row, final int col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[row * col + 12];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var3) {
            var3.printStackTrace();
        }

    }

    public void modelSaveMazeToDisk(String fileName){
        MyCompressorOutputStream out = null;
        try {
            out = new MyCompressorOutputStream(new FileOutputStream(fileName + ".maze"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.write(maze.toByteArray());
    }

    public void playSound(String fileName) {
        AudioClip sound = new AudioClip(new File(fileName).toURI().toString());
        sound.play();
    }
}