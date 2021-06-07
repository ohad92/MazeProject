package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import IO.SimpleDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;



public class MyModel extends Observable implements IModel{
    private Maze mymaze;
    private Solution solution;
    private int playerRow;
    private int playerCol;
    private int TargetRow;
    private int TargetCol;
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
    public void stopServers(){
        this.serverMazeGenerator.stop();
        this.serverSolveMaze.stop();
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
                        InputStream is = new SimpleDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[12 + (rows+2) * (cols+2)];
                        is.read(decompressedMaze);
                        MyModel.this.mymaze = new Maze(decompressedMaze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            //PlayerPosision.setRowIndex(this.mymaze.getStartPosition().getRowIndex());
            //PlayerPosision.setColIndex(this.mymaze.getStartPosition().getColumnIndex());
            movePlayer(mymaze.getStartPosition().getRowIndex(),mymaze.getStartPosition().getColumnIndex());
            setTargetOnMaze(mymaze.getGoalPosition().getRowIndex(),mymaze.getGoalPosition().getColumnIndex());

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
                if (playerRow > 0 && mymaze.getMaze()[playerRow-1][playerCol] != 1)
                    movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < mymaze.getRows() - 1 && mymaze.getMaze()[playerRow+1][playerCol] != 1)
                    movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0 && mymaze.getMaze()[playerRow][playerCol-1] != 1)
                    movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < mymaze.getCols() - 1 && mymaze.getMaze()[playerRow][playerCol+1] != 1 )
                    movePlayer(playerRow, playerCol + 1);
            }
            case UPRIGHT -> {
                if (playerRow > 0 && playerCol < mymaze.getCols() - 1 && mymaze.getMaze()[playerRow-1][playerCol+1] != 1)
                    movePlayer(playerRow - 1, playerCol + 1);
            }
            case UPLEFT -> {
                if (playerRow > 0 && playerCol > 0 && mymaze.getMaze()[playerRow-1][playerCol-1] != 1)
                    movePlayer(playerRow - 1, playerCol - 1);
            }
            case DOWNRIGHT -> {
                if (playerRow < mymaze.getRows() - 1 && playerCol < mymaze.getCols() - 1 && mymaze.getMaze()[playerRow+1][playerCol+1] != 1)
                    movePlayer(playerRow + 1, playerCol + 1);
            }
            case DOWNLEFT -> {
                if (playerRow < mymaze.getRows() - 1 && playerCol > 0 && mymaze.getMaze()[playerRow+1][playerCol-1] != 1 )
                    movePlayer(playerRow + 1, playerCol - 1);
            }
        }
    }

    public void movePlayer(int row, int col){
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers();
    }

    public void setTargetOnMaze(int row, int col){
        this.TargetRow = row;
        this.TargetCol = col;
        setChanged();
        notifyObservers();
    }

    public int getPlayerRow() {
        return this.playerRow;
    }

    public int getPlayerCol() {
        return this.playerCol;
    }

    public int getTargetRow() {
        return this.TargetRow;
    }

    public int getTargetCol() {
        return this.TargetCol;
    }

    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    public void SaveMaze(File file) {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(this.mymaze);
            out.flush();
            out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void LoadMaze(File file) {
        try {
            FileInputStream fileinputstream = new FileInputStream(file);
            ObjectInputStream objectinputstream = new ObjectInputStream(fileinputstream);
            Maze loadedMaze = (Maze)objectinputstream.readObject();
            this.mymaze = loadedMaze;
            setChanged();
            notifyObservers();
            objectinputstream.close();
        }
        catch(IOException | ClassNotFoundException e ){
            e.printStackTrace();
        }
    }

    public void ExitGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thank you for playing with our application!");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        // if the user accept to exit
        if (result.get() == ButtonType.OK) {
            this.stopServers();
            System.exit(0);

        }
    }

}
