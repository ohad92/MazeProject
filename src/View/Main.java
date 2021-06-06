package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // this 9 rows stage 2 working
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlloader.load();
        primaryStage.setTitle("Maze Game");
        primaryStage.setScene(new Scene(root, 500, 500));

        primaryStage.show();

        IModel mymodel = new MyModel();
        MyViewModel myviewmodel = new MyViewModel(mymodel);
        MyViewController myviewcontroller = fxmlloader.getController();
        myviewcontroller.setMyViewModel(myviewmodel);



        // this 4 rows was at start stage 1 working
//        Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
//        primaryStage.setTitle("Maze Game");
//        primaryStage.setScene(new Scene(root, 500, 500));
//
//        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
