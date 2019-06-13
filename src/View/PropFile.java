package View;

import Server.Configurations;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PropFile {

    private Configurations myConf = Configurations.getInstance();
    private final String solverType="SolverType",poolNumS="ThreadPoolNum",genType="GeneratorType";

    private String oldSolver,oldGen,oldTnum;

    @FXML
    private MenuButton genButton;
    @FXML
    private MenuButton solveButton;
    @FXML
    private MenuItem smplMz;
    @FXML
    private MenuItem myMz;
    @FXML
    private MenuItem dfsMenu;
    @FXML
    private MenuItem bfsMenu;
    @FXML
    private MenuItem bestMenu;

    @FXML
    private Button saveBtn;
    @FXML
    private Button cnclBtn;

    private Stage theStage;

    @FXML
    private TextField poolNum;

    public void initProp(Stage myStage)
    {
        theStage = myStage;
        setPropValues();
        smplMz.setOnAction(e -> {
            setSimpleMaze();
        });
        myMz.setOnAction(event -> {
            setMyMaze();
        });
        dfsMenu.setOnAction(event -> {
            setDFS();
        });
        bfsMenu.setOnAction(event -> {
            setBFS();
        });
        bestMenu.setOnAction(event -> {
            setBest();
        });

        setOldValues();




        poolNum.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    poolNum.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void setPropValues() {
        genButton.setText(myConf.getProperty(genType));
        solveButton.setText(myConf.getProperty(solverType));
        poolNum.setText(myConf.getProperty(poolNumS));
    }

    private void setOldValues() {
        oldGen = myConf.getProperty(genType);
        oldSolver = myConf.getProperty(solverType);
        oldTnum = myConf.getProperty(poolNumS);
    }

    public void saveProp()
    {
        myConf.setProperty(genType,genButton.getText());
        myConf.setProperty(solverType,solveButton.getText());


        theStage.close();

    }

    public void cancelAll()
    {
        genButton.setText(oldGen);
        solveButton.setText(oldSolver);
        poolNum.setText(oldTnum);
        theStage.close();
    }

    private void setBest() {
        solveButton.setText(bestMenu.getText());
    }

    private void setBFS() {
        solveButton.setText(bfsMenu.getText());
    }

    private void setDFS() {
        solveButton.setText(dfsMenu.getText());
    }

    private void setMyMaze() {
        genButton.setText(myMz.getText());
//        myConf.setProperty(GeneratorType,simpleMaze);
    }

    private void setSimpleMaze() {
        genButton.setText(smplMz.getText());
//        myConf.setProperty(GeneratorType,simpleMaze);
    }

    public void getProps()
    {
        System.out.println(myConf.getProperty(solverType));
        System.out.println(myConf.getProperty(poolNumS));
        System.out.println(myConf.getProperty(genType));
    }
}
