package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;



public class MyModel extends Observable implements IModel{
    private Maze mymaze;
    private Solution solution;
    private int playerRow;
    private int playerCol;
    private static Server serverMazeGenerator;
    private static Server serverSolveMaze;
    private ArrayList<Position> solutionpath;
    private Position PlayerPosision;


    public MyModel() {
        this.mymaze = null;
        PlayerPosision = new Position(0,0);
        solutionpath = new ArrayList<>();
        //propperties = Server.Configurations.loadConfig();
        }

    public void startServers(){
        this.serverMazeGenerator = new Server(5400, 1000, (IServerStrategy)new ServerStrategyGenerateMaze());
        this.serverSolveMaze = new Server(5401, 1000, (IServerStrategy)new ServerStrategySolveSearchProblem());
        this.serverMazeGenerator.start();
        this.serverSolveMaze.start();

    }

    public void generateMaze(int rows, int cols) {
        System.out.println("generating maze");

        solutionpath = new ArrayList<>();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[12 + (rows+2) * (cols+2)];
                        is.read(decompressedMaze);
                        MyModel.this.mymaze = new Maze(decompressedMaze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            PlayerPosision.setRowIndex(this.mymaze.getStartPosition().getRowIndex());
            PlayerPosision.setColIndex(this.mymaze.getStartPosition().getColumnIndex());

        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    public Maze getMaze() {
        return mymaze;
    }

    public void solveMaze() {

    }

    public Solution getSolution() {
        return null;
    }

    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP -> {
                if (playerRow > 0)
                    movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < mymaze.getRows() - 1)
                    movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0)
                    movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < mymaze.getCols() - 1)
                    movePlayer(playerRow, playerCol + 1);
            }
        }
    }

    private void movePlayer(int row, int col){
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
    }

    public int getPlayerRow() {
        return 0;
    }

    public int getPlayerCol() {
        return 0;
    }

    public void assignObserver(Observer o) {

    }

    public void Save(File file) {

    }
}
