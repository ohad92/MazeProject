package Model;

import java.io.File;
import java.io.IOException;
import java.util.Observer;
import algorithms.mazeGenerators.*;

import algorithms.search.Solution;

public interface IModel {
    void startServers();
    void generateMaze(int rows, int cols);
    Maze getMaze();
    void solveMaze();
    Solution getSolution();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o); // because interface cannot extend class
    void SaveMaze(File file);
    void LoadMaze(File file)throws IOException, ClassNotFoundException;
    void ExitGame();

}
