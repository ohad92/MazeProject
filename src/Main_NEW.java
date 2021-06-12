import Model.IModel;
import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;




public class Main_NEW extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel mymodel = new MyModel();
        mymodel.startServers();
        MyViewModel myviewmodel = new MyViewModel(mymodel);
        mymodel.addObserver(myviewmodel);

        primaryStage.setTitle("Maze Game");
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/View/MyView.fxml"));
        Parent root = fxmlloader.load();
        Scene scene = new Scene(root,800,600);
        primaryStage.setScene(scene);


        MyViewController myviewcontroller = fxmlloader.getController();
        myviewcontroller.applyNewSize(scene);
        myviewcontroller.setMyViewModel(myviewmodel);


        myviewmodel.addObserver(myviewcontroller);
        CloseProgram(primaryStage, mymodel);
        primaryStage.show();


    }
    public static void main(String[] args) {
        launch(args);
    }

    private void CloseProgram(Stage primaryStage, MyModel mymodel) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to close the game?");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                // if the user accept to exit
                if (result.get() == ButtonType.OK){
                    mymodel.stopServers();
                    primaryStage.close();
                // if the user denied the exit
                }
                else
                    {
                    windowEvent.consume();
                }
            }
        });
    }


}