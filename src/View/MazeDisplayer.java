package View;

import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import algorithms.mazeGenerators.Maze;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameTarget = new SimpleStringProperty();

    private int playerRow = 0;
    private int playerCol = 0;
    private int TargetRow = 0;
    private int TargetCol = 0;

    private Solution solution;
    private Maze maze;

    // getter and setters
    public int getPlayerRow() {
        return playerRow;
    }
    public int getPlayerCol() { return playerCol; }
    public int getTargetRow() {
        return TargetRow;
    }
    public int getTargetCol() { return TargetCol; }

    public void setPosition(int playerrow, int playercol, int targetrow, int targetcol){
        this.playerRow = playerrow;
        this.playerCol = playercol;
        this.TargetRow = targetrow;
        this.TargetCol = targetcol;
        draw(); // we call draw here because we want to draw the new position
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }
    public String getImageFileNameTarget() {
        return imageFileNameTarget.get();
    }

    public void setImageFileNameTarget(String imageFileNameTarget) {
        this.imageFileNameTarget.set(imageFileNameTarget);
    }

    public void drawMaze(Maze maze) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("Generating maze: " + maze.length + ", " + maze[0].length);
//        alert.show();
        this.maze = maze;
        draw();
        setPosition(playerRow,playerCol,TargetRow,TargetCol); // set again the position of the player when pressing generatemaze
    }

    private void draw(){
        if (maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getRows();
            int cols = maze.getCols();

            double cellHight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);


            drawMazeWalls(graphicsContext,rows,cols,cellHight,cellWidth);
            if (solution != null)
                drawSolution(graphicsContext,cellHight,cellWidth);
            drawPlayer(graphicsContext,cellHight,cellWidth);
            drawTarget(graphicsContext,cellHight,cellWidth);

        }
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHight, double cellWidth) {

    }


    private void drawMazeWalls(GraphicsContext graphicsContext, int rows, int cols, double cellHight, double cellWidth) {
        Image wallImage = null;
        try {
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image");
        }
        graphicsContext.setFill(Color.RED);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze.getMaze()[i][j] == 1){
                    double x = j * cellWidth;
                    double y = i * cellHight;
                    if (wallImage == null)
                        graphicsContext.fillRect(x,y,cellWidth,cellHight);
                    else
                        graphicsContext.drawImage(wallImage,x,y,cellWidth,cellHight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHight, double cellWidth) {
        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image");
        }
        graphicsContext.setFill(Color.GREEN);

        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHight;

        if (playerImage == null)
            graphicsContext.fillRect(x,y,cellWidth,cellHight);
        else
            graphicsContext.drawImage(playerImage,x,y,cellWidth,cellHight);
    }

    private void drawTarget(GraphicsContext graphicsContext, double cellHight, double cellWidth) {
        Image TargetImage = null;
        try {
            TargetImage = new Image(new FileInputStream(getImageFileNameTarget()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no target image");
        }
        graphicsContext.setFill(Color.BLACK);

        double x = getTargetRow() * cellWidth;
        double y = getTargetCol() * cellHight;

        if (TargetImage == null)
            graphicsContext.fillRect(x,y,cellWidth,cellHight);
        else
            graphicsContext.drawImage(TargetImage,x,y,cellWidth,cellHight);
    }

    public void setSolution(Solution sol) {
        this.solution = sol;
        draw();
    }
}
