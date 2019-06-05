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
import javafx.scene.control.Alert;
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
import java.util.Random;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private Solution solution;
    private boolean golToken;
    private boolean randomValue = false;
    public int level;
    private boolean isLegal = false;

    public Solution getSolution() {
        if (solution != null)
            return solution;
        else
            return null;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    private Server generator;
    private Server solver;

    public void setCharacterRowCurrentPosition(int characterRowCurrentPosition) {
        this.characterRowCurrentPosition = characterRowCurrentPosition;
    }

    public void setCharacterColumnCurrentPosition(int characterColumnCurrentPosition) {
        this.characterColumnCurrentPosition = characterColumnCurrentPosition;
    }

    private int characterRowCurrentPosition;
    private int characterColumnCurrentPosition;
    private boolean finishedGame = false;

    public MyModel() {

        int generatePortNum = Integer.parseInt(Configurations.getInstance().getProperty("GeneratorTypePort"));
        int generateInterval = Integer.parseInt(Configurations.getInstance().getProperty("GeneratorTypeListeningInterval"));
        int solverPortNum = Integer.parseInt(Configurations.getInstance().getProperty("SolverTypePort"));
        int solverInterval = Integer.parseInt(Configurations.getInstance().getProperty("SolverTypeListeningInterval"));
        solver = new Server(solverPortNum, solverInterval, new ServerStrategySolveSearchProblem());
        solver.start();
        generator = new Server(generatePortNum, generateInterval, new ServerStrategyGenerateMaze());
        generator.start();
    }


    public boolean isWon()
    {
        return characterRowCurrentPosition == maze.getGoalPosition().getRowIndex() && characterColumnCurrentPosition == maze.getGoalPosition().getColumnIndex();
    }

    public void moveChar(String buttonName)
    {
        if (buttonName.equals("Up")) {
            if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1 * level, "upOrDown")) {
                characterRowCurrentPosition = characterRowCurrentPosition - 1 * level;
                isLegal = true;
            }
        }
        else if (buttonName.equals("Down")){
            if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1 * level, "upOrDown")) {
                characterRowCurrentPosition = characterRowCurrentPosition + 1 * level;
                isLegal = true;
            }else if (buttonName.equals("Left")) {
                if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1 * level, "leftOrRight")) {
                    characterColumnCurrentPosition = characterColumnCurrentPosition - 1 * level;
                    isLegal = true;
                }
            } else if (buttonName.equals("Right")) {
                if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1 * level, "leftOrRight")) {
                    characterColumnCurrentPosition = characterColumnCurrentPosition + 1 * level;
                    isLegal = true;
                }
            }
        } else if (buttonName.equals("Numpad 1")) {
            if (checkIfLegalDiagonalMove(characterRowCurrentPosition, characterColumnCurrentPosition, level, "leftDown")) {
                characterRowCurrentPosition = characterRowCurrentPosition + 1 * level;
                characterColumnCurrentPosition = characterColumnCurrentPosition - 1 * level;
                isLegal = true;
            }
        } else if (buttonName.equals("Numpad 3")) {
            if (checkIfLegalDiagonalMove(characterRowCurrentPosition, characterColumnCurrentPosition, level, "rightDown")) {
                characterRowCurrentPosition = characterRowCurrentPosition + 1 * level;
                characterColumnCurrentPosition = characterColumnCurrentPosition + 1 * level;
                isLegal = true;
            }

        } else if (buttonName.equals("Numpad 7")) {
            if (checkIfLegalDiagonalMove(characterRowCurrentPosition, characterColumnCurrentPosition, level, "leftUp")) {
                characterRowCurrentPosition = characterRowCurrentPosition - 1 * level;
                characterColumnCurrentPosition = characterColumnCurrentPosition - 1 * level;
                isLegal = true;
            }

        } else if (buttonName.equals("Numpad 9")) {
            if (checkIfLegalDiagonalMove(characterRowCurrentPosition, characterColumnCurrentPosition, level, "rightUp")) {
                characterRowCurrentPosition = characterRowCurrentPosition - 1 * level;
                characterColumnCurrentPosition = characterColumnCurrentPosition + 1 * level;
                isLegal = true;
            }
        }
        setChanged();
        notifyObservers();
    }

    public void getLevel(int levelFromControl){
        this.level = levelFromControl;
    }

    @Override
    public void MoveCharacterEasy(KeyEvent keyEvent, int level) {

        isLegal = false;
        if (isWon()) {
            keyEvent.consume();
            finishedGame = true;
        }

        if (!finishedGame) {
            if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.NUMPAD8) {
                if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1 * level, "upOrDown")) {
                    characterRowCurrentPosition = characterRowCurrentPosition - 1 * level;
                    isLegal = true;
                }

            } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.NUMPAD2) {
                if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1 * level, "upOrDown")) {
                    characterRowCurrentPosition = characterRowCurrentPosition + 1 * level;
                    isLegal = true;

                }

            } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD6) {
                if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, 1 * level, "leftOrRight")) {
                    characterColumnCurrentPosition = characterColumnCurrentPosition + 1 * level;
                    isLegal = true;
                }

            } else if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.NUMPAD4) {
                if (checkIfLegalMove(characterRowCurrentPosition, characterColumnCurrentPosition, -1 * level, "leftOrRight")) {
                    characterColumnCurrentPosition = characterColumnCurrentPosition - 1 * level;
                    isLegal = true;
                }

            } else if (keyEvent.getCode() == KeyCode.NUMPAD1) {
                if (checkIfLegalDiagonalMove(characterRowCurrentPosition, characterColumnCurrentPosition, level, "leftDown")) {
                    characterRowCurrentPosition = characterRowCurrentPosition + 1 * level;
                    characterColumnCurrentPosition = characterColumnCurrentPosition - 1 * level;
                    isLegal = true;
                }

            } else if (keyEvent.getCode() == KeyCode.NUMPAD3) {
                if (checkIfLegalDiagonalMove(characterRowCurrentPosition, characterColumnCurrentPosition, level, "rightDown")) {
                    characterRowCurrentPosition = characterRowCurrentPosition + 1 * level;
                    characterColumnCurrentPosition = characterColumnCurrentPosition + 1 * level;
                    isLegal = true;
                }

            } else if (keyEvent.getCode() == KeyCode.NUMPAD7) {
                if (checkIfLegalDiagonalMove(characterRowCurrentPosition, characterColumnCurrentPosition, level, "leftUp")) {
                    characterRowCurrentPosition = characterRowCurrentPosition - 1 * level;
                    characterColumnCurrentPosition = characterColumnCurrentPosition - 1 * level;
                    isLegal = true;
                }

            } else if (keyEvent.getCode() == KeyCode.NUMPAD9) {
                if (checkIfLegalDiagonalMove(characterRowCurrentPosition, characterColumnCurrentPosition, level, "rightUp")) {
                    characterRowCurrentPosition = characterRowCurrentPosition - 1 * level;
                    characterColumnCurrentPosition = characterColumnCurrentPosition + 1 * level;
                    isLegal = true;
                }
            }

            if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.UP
                    || keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD8 || keyEvent.getCode() == KeyCode.NUMPAD2
                    || keyEvent.getCode() == KeyCode.NUMPAD4 || keyEvent.getCode() == KeyCode.NUMPAD6
                    || keyEvent.getCode() == KeyCode.NUMPAD1 || keyEvent.getCode() == KeyCode.NUMPAD3
                    || keyEvent.getCode() == KeyCode.NUMPAD7 || keyEvent.getCode() == KeyCode.NUMPAD9) {
                if (!isLegal) {
                    playSound("resources/Audio/punchWall.mp3");
                    characterRowCurrentPosition = maze.getStartPosition().getRowIndex();
                    characterColumnCurrentPosition = maze.getStartPosition().getColumnIndex();
                    golToken = false;
                }
            }

            setChanged();
            notifyObservers();


            //Updates the MazeDisplayer

            //Updates the labels
        }

    }

    private boolean checkIfLegalDiagonalMove(int characterRowCurrentPosition, int characterColumnCurrentPosition, int level, String diagSide) {
        if ((characterRowCurrentPosition + 1 * level > 0 || characterRowCurrentPosition + 1 * level <= maze.getMazeArray().length
                || characterRowCurrentPosition + 2 * level > 0 || characterRowCurrentPosition + 2 * level > maze.getMazeArray().length
                || characterRowCurrentPosition + 1 * level > 0 || characterRowCurrentPosition + 1 * level <= maze.getMazeArray().length
                || characterRowCurrentPosition + 2 * level > 0 || characterRowCurrentPosition + 2 * level > maze.getMazeArray().length)) {
            int[][] mazeArray = maze.getMazeArray();
            int diagCellRow, diagCellCol, nextCellRow, nextCellCol, scndCellRow, scndCellCol;
            if (diagSide.toLowerCase().equals("leftdown")) {
                diagCellRow = characterRowCurrentPosition + 1 * level;
                diagCellCol = characterColumnCurrentPosition - 1 * level;
                if (checkLegalCell(diagCellRow, diagCellCol)) {

                    if (checkLegalCell(characterRowCurrentPosition, diagCellCol) ||
                            checkLegalCell(diagCellRow, characterColumnCurrentPosition)) {

                        return true;
                    }


                }


            } else if (diagSide.toLowerCase().equals("rightdown")) {
                diagCellRow = characterRowCurrentPosition + 1 * level;
                diagCellCol = characterColumnCurrentPosition + 1 * level;
                if (checkLegalCell(diagCellRow, diagCellCol)) {

                    if (checkLegalCell(characterRowCurrentPosition, diagCellCol) ||
                            checkLegalCell(diagCellRow, characterColumnCurrentPosition)) {

                        return true;
                    }


                }

            } else if (diagSide.toLowerCase().equals("leftup")) {
                diagCellRow = characterRowCurrentPosition - 1 * level;
                diagCellCol = characterColumnCurrentPosition - 1 * level;
                if (checkLegalCell(diagCellRow, diagCellCol)) {

                    if (checkLegalCell(characterRowCurrentPosition, diagCellCol) ||
                            checkLegalCell(diagCellRow, characterColumnCurrentPosition)) {

                        return true;
                    }


                }

            } else if (diagSide.toLowerCase().equals("rightup")) {
                diagCellRow = characterRowCurrentPosition - 1 * level;
                diagCellCol = characterColumnCurrentPosition + 1 * level;
                if (checkLegalCell(diagCellRow, diagCellCol)) {

                    if (checkLegalCell(characterRowCurrentPosition, diagCellCol) ||
                            checkLegalCell(diagCellRow, characterColumnCurrentPosition)) {

                        return true;
                    }
                }
            } else {
                return false;
            }


        }


        return false;

    }

    private boolean checkLegalCell(int cellRow, int cellCol) {
        return maze.getMazeArray()[cellRow][cellCol] != 1
                && maze.getMazeArray()[cellRow][cellCol] >= 0
                && maze.getMazeArray()[cellRow][cellCol] < maze.getMazeArray()[0].length;
    }

    public Position getPosition() {
        Position curr = new Position(characterRowCurrentPosition, characterColumnCurrentPosition);
        return curr;
    }


    public boolean checkIfLegalMove(int characterRowCurrentPosition, int characterColumnCurrentPosition, int num, String side) {
        System.out.println("Row=" + characterRowCurrentPosition + ", Col=" + characterColumnCurrentPosition + ", num=" + num);


        if (!(characterRowCurrentPosition + num >= maze.getMazeArray().length || characterRowCurrentPosition + num < 0)) {
            if (side.equals("upOrDown") && maze.getMazeArray()[characterRowCurrentPosition + num][characterColumnCurrentPosition] != 1
                    && maze.getMazeArray()[characterRowCurrentPosition + num][characterColumnCurrentPosition] >= 0
                    && maze.getMazeArray()[characterRowCurrentPosition + num][characterColumnCurrentPosition] < maze.getMazeArray()[0].length) {
                return true;
            }
        }
        if (!(characterColumnCurrentPosition + num >= maze.getMazeArray()[0].length || characterColumnCurrentPosition + num < 0)) {
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
        callClientGenerateMaze(height, width);
        finishedGame = false;
        characterRowCurrentPosition = maze.getStartPosition().getRowIndex();
        characterColumnCurrentPosition = maze.getStartPosition().getColumnIndex();
        setChanged();
        notifyObservers();
    }

    public void solveGame() {
        callClientSolveMaze();
        setChanged();
        notifyObservers();
    }

    private void callClientSolveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        solution = (Solution) fromServer.readObject();

                        if (solution == null)
                            System.out.println("ERRORRRRRRRRRRRRRRRRR***********************");
                        System.out.println(String.format("Solution steps: %s", solution));
                    } catch (Exception var6) {
                        var6.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var2) {
            var2.printStackTrace();
        }
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
                        byte[] compressedMaze = (byte[]) ((byte[]) fromServer.readObject());
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
        ObjectOutputStream out = null;
        try {
            if(!fileName.toLowerCase().contains(".maze"))
                fileName = fileName + ".maze";
            out = new ObjectOutputStream(new FileOutputStream(fileName));
            if(maze != null)
            {
                out.writeObject(maze);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadMaze(String pathToFile)
    {
        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(pathToFile));
            Object loadedMaze = inputStream.readObject();

            if(loadedMaze == null)
                maze = null;
            else if(loadedMaze instanceof Maze)
            {
                maze = (Maze)loadedMaze;
                characterRowCurrentPosition = maze.getStartPosition().getRowIndex();
                characterColumnCurrentPosition = maze.getStartPosition().getColumnIndex();

            }
            setChanged();
            notifyObservers();
        } catch (IOException ioExcp) {
            ioExcp.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void playSound(String fileName) {
        AudioClip sound = new AudioClip(new File(fileName).toURI().toString());
        sound.play();

    }

    @Override
    public void closeGame() {
        generator.stop();
        solver.stop();
    }

    public Position getrandomPos() {
        Random randomGenerator = new Random();
        int indexRow = randomGenerator.nextInt(maze.getHeight());
        int indexCol = randomGenerator.nextInt(maze.getWidth());
        boolean found = false;
        while (!found) {
            if ((indexCol != maze.getGoalPosition().getColumnIndex() && indexRow != maze.getGoalPosition().getRowIndex()) && (indexCol != maze.getStartPosition().getColumnIndex() && indexRow != maze.getStartPosition().getRowIndex())) {
                if (maze.getMazeArray()[indexRow][indexCol] == 0) {
                    Position gobletPosition = new Position(indexRow, indexCol);
                    return gobletPosition;
                }
            }
            indexRow = randomGenerator.nextInt(maze.getHeight());
            indexCol = randomGenerator.nextInt(maze.getWidth());
        }
        return null;
    }

    public boolean getGolToken(){
        return golToken;
    }

}