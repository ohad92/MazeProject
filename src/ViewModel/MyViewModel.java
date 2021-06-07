package ViewModel;

import Model.IModel;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import algorithms.mazeGenerators.Maze;

import Model.MovementDirection;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private Maze mymaze;


    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);  // when the model update, the viewmodel will know it.
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o == this.model)
        {
            setChanged();
            notifyObservers();
        }

    }

    public void generateMaze(int rows, int cols){
        System.out.println("mymodel");

        model.generateMaze(rows,cols);
    }

    public Maze getMaze(){
        return model.getMaze();
    }
    public void solveMaze(){
        model.solveMaze();
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void movePlayer(KeyEvent keyEvent){
        MovementDirection direction;
        switch (keyEvent.getCode()){
            case NUMPAD8:
            case UP:
                direction = MovementDirection.UP;
                break;
            case NUMPAD2:
            case DOWN:
                direction = MovementDirection.DOWN;
                break;
            case NUMPAD4:
            case LEFT:
                direction = MovementDirection.LEFT;
                break;
            case NUMPAD6:
            case RIGHT:
                direction = MovementDirection.RIGHT;
                break;

            case NUMPAD9:
                direction = MovementDirection.UPRIGHT;
                break;
            case NUMPAD7:
                direction = MovementDirection.UPLEFT;
                break;
            case NUMPAD3:
                direction = MovementDirection.DOWNRIGHT;
                break;
            case NUMPAD1:
                direction = MovementDirection.DOWNLEFT;
                break;
            default : {
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public void Save(File file){
        this.model.SaveMaze(file);
    }
    public void Load(File file){
        try{this.model.LoadMaze(file);        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    public void ExitGame(){
        this.model.ExitGame();
    }

}
