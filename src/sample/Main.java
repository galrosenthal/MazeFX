package sample;

import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategySolveSearchProblem;
import Server.ServerStrategyGenerateMaze;
import Client.IClientStrategy;
import View.MazeSizes;
import Client.Client;
import algorithms.mazeGenerators.Maze;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Main extends Application  {

    private static Server solveSearchProblemServer,generateServer ;

    private Button button;
    private Button button1;
    private Maze appMaze;
    private Stage myAppWindow;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../View/MyView.fxml"));
        myAppWindow = primaryStage;
        myAppWindow.setTitle("Maze Client");

        myAppWindow.setOnCloseRequest(e -> {
            e.consume();
            close();
        });
//        primaryStage.setScene(new Scene(root, 300, 275));
        button = new Button();
        button1 = new Button("Cancel");
        button.setText("Ok");
        button.setOnAction(e -> {
            System.out.println("Ok was Pressed");
        });
        button1.setOnAction(e -> {
            System.out.println("Cancel was pressed");
        });
//        StackPane layout = new StackPane();
//
//        layout.getChildren().addAll(button,button1);
//        layout.setAlignment(button,Pos.CENTER_LEFT);
//        layout.setAlignment(button1,Pos.CENTER_RIGHT);

        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");
        Menu editMenu = new Menu("Edit");

        //File Menu items
        MenuItem Save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");
        MenuItem newGame = new MenuItem("Start New Game");





        exit.setOnAction(e -> close());
        newGame.setOnAction(event -> {
            int[] mazeSizes = MazeSizes.getMazeSizes();

            if(mazeSizes != null)
                callClientGenerateMaze(mazeSizes[0],mazeSizes[1]);
        });
        //Help Menu Items
        MenuItem m3 = new MenuItem("About Us");

        //Edit Menu Items
        MenuItem m4 = new MenuItem("Change Maze Size");

        fileMenu.getItems().addAll(newGame,Save,new SeparatorMenuItem(),exit);
        Save.setDisable(true);
        helpMenu.getItems().add(m3);
        editMenu.getItems().add(m4);

        MenuBar mBar = new MenuBar();

        mBar.getMenus().addAll(fileMenu,editMenu,helpMenu);



        BorderPane mylayout = new BorderPane();

        mylayout.setTop(mBar);


        Scene scene = new Scene(mylayout, 300,250);

        myAppWindow.setScene(scene);
        myAppWindow.show();
    }

    private void callClientGenerateMaze(int row, int col) {
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
                        appMaze = new Maze(decompressedMaze);
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
            appMaze.print();
        } catch (UnknownHostException var3) {
            var3.printStackTrace();
        }

    }

    private void close() {
        Boolean answer = ConfirmBox.display("Exit Window","Are You sure you want to exit?");

        if(answer)
        {
            solveSearchProblemServer.stop();
            generateServer.stop();
            myAppWindow.close();

        }
    }


    public static void main(String[] args) {
//        int generatePortNum = Integer.parseInt(Configurations.getInstance().getProperty("GeneratorTypePort"));
//        int generateInterval = Integer.parseInt(Configurations.getInstance().getProperty("GeneratorTypeListeningInterval"));
//        int solverPortNum = Integer.parseInt(Configurations.getInstance().getProperty("SolverTypePort"));
//        int solverInterval = Integer.parseInt(Configurations.getInstance().getProperty("SolverTypeListeningInterval"));
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());

        solveSearchProblemServer.start();
        generateServer.start();

        launch(args);




    }
}
