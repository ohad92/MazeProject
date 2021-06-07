package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import algorithms.mazeGenerators.Maze;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements Initializable, Observer {
    public MenuItem newbutton;
    public MenuItem savebutton;
    public MenuItem loadbutton;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label lbl_playerRow;
    public Label lbl_playerCol;
    private MyViewModel myviewmodel;
    private Maze mymaze;
    private int playerrow;
    private int playercol;
    public static Stage window;

    //private MazeGenerator generator;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int row) {
        this.updatePlayerRow.set("" + row);
    }

    public StringProperty updatePlayerColProperty() {
        return updatePlayerCol;
    }

    public void setUpdatePlayerCol(int col) {
        this.updatePlayerCol.set("" + col);
    }

    /* to update the player row and col in the scene (text box under solve maze) */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_playerRow.textProperty().bind(updatePlayerRow);
        lbl_playerCol.textProperty().bind(updatePlayerCol);
    }


    public void generateMaze(ActionEvent actionEvent) {

//        int rows = Integer.valueOf(textField_mazeRows.getText());
//        int cols = Integer.valueOf(textField_mazeColumns.getText());
//        myviewmodel.generateMaze(rows,cols);
//        mazeDisplayer.drawMaze(myviewmodel.getMaze());
//
        String row = textField_mazeRows.getText();
        String column = textField_mazeColumns.getText();
        if (!row.matches("\\d*") || !column.matches("\\d*") || row.equals("") || column.equals("")){
            NewAlert("Please insert only integers between 2 - 1000","WARNING");
        }
        else {

            int rows = Integer.valueOf(textField_mazeRows.getText());
            int cols = Integer.valueOf(textField_mazeColumns.getText());
            if (rows < 2 || rows > 1000 || cols < 2 || cols > 1000)
                NewAlert("Please insert only integers between 2 - 1000","WARNING");
            else{
                myviewmodel.generateMaze(rows,cols);
                mazeDisplayer.drawMaze(myviewmodel.getMaze());

            }
            savebutton.setDisable(false);
        }
        //mazeDisplayer.requestFocus();

    }

    public void solveMaze(ActionEvent actionEvent) {
        myviewmodel.solveMaze();
    }

    public void NewMaze(ActionEvent actionEvent) {
//        if (generator == null)
//            generator = new MazeGenerator();

        String row = textField_mazeRows.getText();
        String column = textField_mazeColumns.getText();
        if (!row.matches("\\d*") || !column.matches("\\d*") || row.equals("") || column.equals("")){
            NewAlert("Please insert only integers between 2 - 1000","WARNING");
        }
        else {

            int rows = Integer.valueOf(textField_mazeRows.getText());
            int cols = Integer.valueOf(textField_mazeColumns.getText());
            if (rows < 2 || rows > 1000 || cols < 2 || cols > 1000)
                NewAlert("Please insert only integers between 2 - 1000","WARNING");
            else{
                myviewmodel.generateMaze(rows,cols);
                mazeDisplayer.drawMaze(myviewmodel.getMaze());

            }
            savebutton.setDisable(false);
        }
        //mazeDisplayer.requestFocus();

    }

    public void SaveMaze(ActionEvent actionEvent) {
        if (mymaze == null){
            NewAlert("There is no maze to save!", "WARNING");
        }
        FileChooser file = new FileChooser();
        file.setInitialDirectory(new File(System.getProperty("user.home")));
        file.getExtensionFilters().addAll(new FileChooser.ExtensionFilter[] { new FileChooser.ExtensionFilter("*.maze", new String[] { "*.maze" }) });
        file.setTitle("we have successfully saved your maze!");
        File savefile = file.showSaveDialog(this.mazeDisplayer.getScene().getWindow());
        if (savefile != null) {
            try { this.myviewmodel.Save(savefile); }
            catch(Exception e) {
                e.printStackTrace();
                NewAlert("An error occured while saving your maze","WARNING");
                return;
            }
            NewAlert("The file " + savefile.toString() + "has been saved successfully","INFORMATION");
        }
    }

    public void LoadMaze(ActionEvent actionEvent) {
        FileChooser file = new FileChooser();
        file.setInitialDirectory(new File(System.getProperty("user.home")));
        file.getExtensionFilters().addAll(new FileChooser.ExtensionFilter[] { new FileChooser.ExtensionFilter("*.maze", new String[] { "*.maze" }) });
        file.setTitle("Choose maze file to open");
        File loadedFile = file.showOpenDialog(this.mazeDisplayer.getScene().getWindow());
        if (loadedFile != null) {
            this.myviewmodel.Load(loadedFile);
            mazeDisplayer.requestFocus();
        }
    }

    public void PropertiesInfo(ActionEvent actionEvent) {
    }

    public void ExitGame(ActionEvent actionEvent) {
        this.myviewmodel.ExitGame();
        Platform.exit();
    }

    public void HelpButton(ActionEvent actionEvent) {
    }

    public void AboutUsButton(ActionEvent actionEvent) {
        String MessageToDisplay = "The Algorithm we used to generate the maze is Prim's algorithm\n" +
                "The Algorithms we used to solve the maze are: BFS, DFS, BestFirstSearch\n" +
                "Presented by: Inbar Zacaim, Ohad Shriki";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText(MessageToDisplay);
        alert.show();
    }

    public void keyPressed(KeyEvent keyEvent) {

//        int row = mazeDisplayer.getPlayerRow();
//        int col = mazeDisplayer.getPlayerCol();
//
//        //getcode is the key code of the botton pressed
//        switch (keyEvent.getCode()) {
//            case UP -> row -= 1;
//            case DOWN -> row += 1;
//            case LEFT -> col -= 1;
//            case RIGHT -> col += 1;
//        }
//        mazeDisplayer.setPosition(row,col); // need to update the new row and col
//        setUpdatePlayerRow(row);
//        setUpdatePlayerCol(col);
//        keyEvent.consume(); // tell the event we already handled it, no more keypressed

        myviewmodel.movePlayer(keyEvent);
        keyEvent.consume(); // tell the event we already handled it, no more keypressed
    }

//    public void setPlayerPosition(int row, int col){
//        mazeDisplayer.setPosition(row,col);
//        setUpdatePlayerRow(row);
//        setUpdatePlayerCol(col);
//    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void NewAlert(String message,String typeofError){
        if (typeofError.equals("WARNING")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(message);
            alert.show();
        }
        else if (typeofError.equals("INFORMATION")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
            alert.show();
        }
        else if (typeofError.equals("CONFIRMATION")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(message);
            alert.show();
        }
        else if (typeofError.equals("ERROR")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(message);
            alert.show();
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("hi");

        if (o instanceof MyViewModel) {
            if (mymaze == null)//generateMaze
            {
                mymaze = myviewmodel.getMaze();
                playerrow = myviewmodel.getPlayerRow();
                playercol = myviewmodel.getPlayerCol();
                mazeDisplayer.drawMaze(mymaze);
            }
            else {
                Maze maze = myviewmodel.getMaze();
                if (maze == mymaze) // maze not generated yet
                {
                    playerrow = myviewmodel.getPlayerRow();
                    playercol = myviewmodel.getPlayerCol();
                    if (playerrow==9954)//Solve Maze
                    {
//                        solution = viewModel.getSolution();
//                        if (solution != null)
//                        this.mazeDisplayer.solve(solution);
                        System.out.println("hi");
                    }
//                    else {
                    mazeDisplayer.setPosition(playerrow, playercol);
                    setUpdatePlayerRow(playerrow);
                    setUpdatePlayerCol(playercol);
                    mazeDisplayer.drawMaze(maze);


                        //if ((viewmodelcol == mymaze.getGoalPosition().getColumnIndex()) && (viewmodelrow == mymaze.getGoalPosition().getRowIndex()))
                        //finish();
                    //}
                }
                else
                {
                    mymaze = maze;
                    playerrow = myviewmodel.getPlayerRow();
                    playercol = myviewmodel.getPlayerCol();
                    mazeDisplayer.drawMaze(maze);
                }
        }
    }
    }


    public void setMyViewModel( MyViewModel mvm){
        this.myviewmodel = mvm;
        this.myviewmodel.addObserver(this);
    }


}
