package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import algorithms.mazeGenerators.Maze;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;

public class MyViewController implements IView,Initializable, Observer {
    public MenuItem newbutton;
    public MenuItem savebutton;
    public MenuItem loadbutton;
    public Button solveMaze;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label lbl_playerRow;
    public Label lbl_playerCol;
    private MyViewModel myviewmodel;
    private Maze mymaze;
    private int playerrow;
    private int playercol;
    private int targetrow;
    private int targetcol;
    //public static Stage window;
    private static MediaPlayer mediaPlayer;
    private static MediaPlayer mediaWin;
    //public Pane mazepane;

    private boolean songisinoff;
    private boolean winsongonoff;
    private boolean showvictoryimage;

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

        solveMaze.setDisable(false);
        showvictoryimage = false;
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
                mazeDisplayer.setSolutionNull();
                mazeDisplayer.setPosition(mymaze.getStartPosition().getRowIndex(),mymaze.getStartPosition().getColumnIndex(),mymaze.getGoalPosition().getRowIndex(),mymaze.getGoalPosition().getColumnIndex());
                setUpdatePlayerRow(mymaze.getStartPosition().getRowIndex());
                setUpdatePlayerCol(mymaze.getStartPosition().getColumnIndex());

                mazeDisplayer.drawMaze(myviewmodel.getMaze());

            }
        }
        //mazeDisplayer.requestFocus();

    }

    public void solveMaze(ActionEvent actionEvent) {
        if (mymaze == null)
            NewAlert("Please generate new maze first!","WARNING");
        myviewmodel.solveMaze();
        mazeDisplayer.drawMazeSolution(myviewmodel.getSolution());
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

            try {
                FileInputStream fileinputstream = new FileInputStream(loadedFile);
                ObjectInputStream objectinputstream = new ObjectInputStream(fileinputstream);
                Maze newmaze = (Maze) objectinputstream.readObject();
                this.mymaze = newmaze;
                myviewmodel.setmazenotnull(newmaze);
            }
            catch(IOException | ClassNotFoundException e ){
                e.printStackTrace();
            }

            this.myviewmodel.Load(loadedFile);
            mazeDisplayer.requestFocus();
        }
    }

    public void PropertiesInfo(ActionEvent actionEvent) {
        try (InputStream inputstream = new FileInputStream("resources/Properties/config.properties"))
        {
            Properties properties = new Properties();
            properties.load(inputstream);
            String MessageToDisplay = "The searching algorithm is " + properties.getProperty("mazeSearchingAlgorithm") + "\n";
            MessageToDisplay += "The generate algorithm is " + properties.getProperty("mazeGeneratingAlgorithm") + "\n";
            MessageToDisplay += "The number of  threads is " + properties.getProperty("threadPoolSize") + "\n";
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Properties");
            alert.setHeaderText(null);
            alert.setContentText(MessageToDisplay);
            alert.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void ExitGame(ActionEvent actionEvent) {
        this.myviewmodel.ExitGame();
        //Platform.exit();
    }

    public void HelpButton(ActionEvent actionEvent) {
        String MessageToDisplay = "Welcome to the maze game!\n" +
                "your goal is to move Harry Potter to the snitch.\n" +
                "you can use the arrow buttons or all numbers in the numpad\n" +
                "press 5 to see the solution\npress g to generate new maze\npress m to start / stop the music";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText(MessageToDisplay);
        alert.show();
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
        if (!solveMaze.isDisable())
            if (keyEvent.getCode() == KeyCode.NUMPAD5)
                solveMaze(new ActionEvent());
        if (keyEvent.getCode() == KeyCode.M)
            startmusic(new ActionEvent());
        if (keyEvent.getCode() == KeyCode.G)
            generateMaze(new ActionEvent());
        if (keyEvent.getCode() == KeyCode.ESCAPE)
            ExitGame(new ActionEvent());
        myviewmodel.movePlayer(keyEvent);
        keyEvent.consume(); // tell the event we already handled it, no more keypressed
    }

    public void MouseEvent(MouseEvent mouseEvent) {
//        System.out.println("X: " + mouseevent.getX());
//        System.out.println("Y: " + mouseevent.getY());
//        System.out.println("getSceneX: " + mouseevent.getSceneX());
//        System.out.println("getSceney: " + mouseevent.getSceneY());
//        System.out.println("getScreenX: " + mouseevent.getScreenX());
//        System.out.println("getScreenY: " + mouseevent.getScreenY());

//        if (myviewmodel.getMaze() != null) {
//
//            double quarterCellHeight = mazeDisplayer.get() / 4; // padding
//            double quarterCellWidth = mazeDisplayCellWidth.get() / 4;
//            boolean hasMoved = false;
//
//
//            if (mouseEvent.getY() >= oldY + quarterCellHeight && mouseEvent.getY() < oldY + 3 * quarterCellHeight && mouseEvent.getX() > oldX + mazeDisplayCellWidth.get() + quarterCellWidth && mouseEvent.getX() < oldX + mazeDisplayCellWidth.get() * 2 - quarterCellWidth) {
//                oldX += mazeDisplayCellWidth.get();
//                hasMoved = viewModel.moveCharacter(KeyCode.RIGHT);
//                if (!hasMoved) {
//                    oldX -= mazeDisplayCellWidth.get();
//                }
//            } else if (mouseEvent.getY() >= oldY + quarterCellHeight && mouseEvent.getY() < oldY + 3 * quarterCellHeight && mouseEvent.getX() >= oldX - mazeDisplayCellWidth.get() + quarterCellWidth && mouseEvent.getX() < oldX - quarterCellWidth) {
//                oldX -= mazeDisplayCellWidth.get();
//                hasMoved = viewModel.moveCharacter(KeyCode.LEFT);
//                if (!hasMoved) {
//                    oldX += mazeDisplayCellWidth.get();
//                }
//            } else if (mouseEvent.getX() >= oldX + quarterCellWidth && mouseEvent.getX() < oldX + 3 * quarterCellWidth && mouseEvent.getY() > oldY + mazeDisplayCellHeight.get() + quarterCellHeight && mouseEvent.getY() < oldY + mazeDisplayCellHeight.get() * 2 - quarterCellHeight) {
//                oldY += mazeDisplayCellHeight.get();
//                hasMoved = viewModel.moveCharacter(KeyCode.DOWN);
//                if (!hasMoved) {
//                    oldY -= mazeDisplayCellHeight.get();
//                }
//            } else if (mouseEvent.getX() >= oldX + quarterCellWidth && mouseEvent.getX() < oldX + 3 * quarterCellWidth && mouseEvent.getY() > oldY - mazeDisplayCellHeight.get() + quarterCellHeight && mouseEvent.getY() < oldY - quarterCellHeight) {
//                oldY -= mazeDisplayCellHeight.get();
//                hasMoved = viewModel.moveCharacter(KeyCode.UP);
//                if (!hasMoved) {
//                    oldY += mazeDisplayCellHeight.get();
//                }
//            }
//            //UP LEFT
//            else if (mouseEvent.getY() > oldY - 3 * quarterCellHeight && mouseEvent.getY() < oldY - quarterCellHeight && mouseEvent.getX() >= oldX - 3 * quarterCellWidth && mouseEvent.getX() < oldX - quarterCellWidth) {
//                oldX -= mazeDisplayCellWidth.get();
//                oldY -= mazeDisplayCellHeight.get();
//                hasMoved = viewModel.moveCharacter(KeyCode.NUMPAD7);
//                if (!hasMoved) {
//                    oldX += mazeDisplayCellWidth.get();
//                    oldY += mazeDisplayCellHeight.get();
//                }
//            }
//            //UP RIGHT
//            else if (mouseEvent.getY() > oldY - 3 * quarterCellHeight && mouseEvent.getY() < oldY - quarterCellHeight && mouseEvent.getX() > oldX + mazeDisplayCellWidth.get() + quarterCellWidth && mouseEvent.getX() < oldX + mazeDisplayCellWidth.get() * 2 - quarterCellWidth) {
//                oldX += mazeDisplayCellWidth.get();
//                oldY -= mazeDisplayCellHeight.get();
//                hasMoved = viewModel.moveCharacter(KeyCode.NUMPAD9);
//                if (!hasMoved) {
//                    oldX -= mazeDisplayCellWidth.get();
//                    oldY += mazeDisplayCellHeight.get();
//                }
//            }
//            //DOWN RIGHT
//            else if (mouseEvent.getY() > oldY + mazeDisplayCellHeight.get() + quarterCellHeight && mouseEvent.getY() < oldY + mazeDisplayCellHeight.get() * 2 - quarterCellHeight && mouseEvent.getX() > oldX + mazeDisplayCellWidth.get() + quarterCellWidth && mouseEvent.getX() < oldX + mazeDisplayCellWidth.get() * 2 - quarterCellWidth) {
//                oldX += mazeDisplayCellWidth.get();
//                oldY += mazeDisplayCellHeight.get();
//                hasMoved = viewModel.moveCharacter(KeyCode.NUMPAD3);
//                if (!hasMoved) {
//                    oldX -= mazeDisplayCellWidth.get();
//                    oldY -= mazeDisplayCellHeight.get();
//                }
//            }
//            //DOWN LEFT
//            else if (mouseEvent.getY() > oldY + mazeDisplayCellHeight.get() + quarterCellHeight && mouseEvent.getY() < oldY + mazeDisplayCellHeight.get() * 2 - quarterCellHeight && mouseEvent.getX() >= oldX - mazeDisplayCellWidth.get() + quarterCellWidth && mouseEvent.getX() < oldX - quarterCellWidth) {
//                oldX -= mazeDisplayCellWidth.get();
//                oldY += mazeDisplayCellHeight.get();
//                hasMoved = viewModel.moveCharacter(KeyCode.NUMPAD1);
//                if (!hasMoved) {
//                    oldX += mazeDisplayCellWidth.get();
//                    oldY -= mazeDisplayCellHeight.get();
//                }
//            }
//        }
    }

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
        //System.out.println("hi");

        if (o instanceof MyViewModel) {
            if (winsongonoff == true)
                mediaWin.stop();
            if (mymaze == null)//generateMaze
            {

                mymaze = myviewmodel.getMaze();
                playerrow = myviewmodel.getPlayerRow();
                playercol = myviewmodel.getPlayerCol();
                targetrow = myviewmodel.getTargetRow();
                targetcol = myviewmodel.getTargetCol();
                mazeDisplayer.setPosition(playerrow,playercol,targetrow,targetcol);
                mazeDisplayer.drawMaze(mymaze);
            }
            else {
                Maze maze = myviewmodel.getMaze();
                if (maze == mymaze) // maze not generated yet
                {
                    playerrow = myviewmodel.getPlayerRow();
                    playercol = myviewmodel.getPlayerCol();
                    targetrow = myviewmodel.getTargetRow();
                    targetcol = myviewmodel.getTargetCol();
                    if (playerrow==targetrow && playercol==targetcol) //player reached the target
                    {
                        NewAlert("Congratulations! You have solved the maze!","INFORMATION");
                        mazeDisplayer.setsolvedtrue();
                        mazeDisplayer.draw();
                        WinSound(new ActionEvent());
                        mymaze = null;
                        solveMaze.setDisable(true);
                        showvictoryimage = true;
                    }
                    else {
                        mazeDisplayer.setPosition(playerrow, playercol, targetrow, targetcol);
                        setUpdatePlayerRow(playerrow);
                        setUpdatePlayerCol(playercol);
                        mazeDisplayer.drawMaze(maze);
                    }

                }
                else
                {
                    mymaze = maze;
                    playerrow = myviewmodel.getPlayerRow();
                    playercol = myviewmodel.getPlayerCol();
                    targetrow = myviewmodel.getTargetRow();
                    targetcol = myviewmodel.getTargetCol();
                    mazeDisplayer.drawMaze(maze);
                }
        }
    }
    }


    public void setMyViewModel( MyViewModel mvm){
        this.myviewmodel = mvm;
        this.myviewmodel.addObserver(this);
    }
    public Solution getSolutionFromViewModel() {
        return myviewmodel.getSolution();
    }

    public void startmusic(ActionEvent actionEvent) {
        if (winsongonoff == true)
            mediaWin.stop();
        if (mediaPlayer == null) {
            Media song = new Media(new File("resources/sound/harry_potter.mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(song);
            mediaPlayer.play();
            songisinoff = true;
        }
        else if (songisinoff == true)
        {
            mediaPlayer.pause();
            songisinoff = false;
        }
        else
        {
            mediaPlayer.play();
            songisinoff = true;
        }

    }

    public void WinSound(ActionEvent actionEvent){
        if (mediaWin == null) {
            Media song = new Media(new File("resources/sound/win_sound.wav").toURI().toString());
            mediaWin = new MediaPlayer(song);
            mediaWin.play();

        }
        else
        {
            if (mediaPlayer != null)
                mediaPlayer.stop();
            mediaWin.play();
        }
        winsongonoff = true;

    }

    public void scroll(ScrollEvent scroll) {
        if(scroll.isControlDown() || scroll.isShiftDown()){
            if(scroll.getDeltaY()>0 || scroll.getDeltaX()>0){
                mazeDisplayer.setscroll(mazeDisplayer.getscroll()*1.1);
            }
            if(scroll.getDeltaY()<0 || scroll.getDeltaX()<0){
                mazeDisplayer.setscroll(mazeDisplayer.getscroll()/1.1);
            }
            scroll.consume();
            mazeDisplayer.draw();

        }
    }

    public void applyNewSize(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                if (!showvictoryimage)
                    mazeDisplayer.draw();
                else
                    mazeDisplayer.drawwin();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                if (!showvictoryimage)
                    mazeDisplayer.draw();
                else
                    mazeDisplayer.drawwin();
            }
        });
    }

}
