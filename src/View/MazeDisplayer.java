package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import algorithms.mazeGenerators.Maze;
import java.util.ArrayList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    private Solution solutionObj;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameTarget = new SimpleStringProperty();
    StringProperty imageFileNameWin = new SimpleStringProperty();

    private int playerRow = 0;
    private int playerCol = 0;
    private int TargetRow = 0;
    private int TargetCol = 0;
    private double scroll = 1;

    private Solution solution;
    private Maze maze;
    private boolean solved = false;

    // getter and setters
    public int getPlayerRow() {
        return playerRow;
    }
    public int getPlayerCol() { return playerCol; }
    public int getTargetRow() { return TargetRow; }
    public int getTargetCol() { return TargetCol; }
    public void setSolutionNull() {this.solution = null;}
    public void setsolvedtrue() {this.solved = true;}
    public boolean getsolvedtrue(){return this.solved;}

    public void setPosition(int playerrow, int playercol, int targetrow, int targetcol){
        this.playerRow = playerrow;
        this.playerCol = playercol;
        this.TargetRow = targetrow;
        this.TargetCol = targetcol;
        draw(); // we call draw here because we want to draw the new position
    }

    // getter and setter images

    //wall
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    //player
    public String getImageFileNamePlayer() { return imageFileNamePlayer.get(); }
    public void setImageFileNamePlayer(String imageFileNamePlayer) { this.imageFileNamePlayer.set(imageFileNamePlayer); }

    //target
    public String getImageFileNameTarget() {
        return imageFileNameTarget.get();
    }
    public void setImageFileNameTarget(String imageFileNameTarget) { this.imageFileNameTarget.set(imageFileNameTarget); }

    //win
    public String getImageFileNameWin() { return imageFileNameWin.get(); }
    public void setImageFileNameWin(String imageFileNameWin) { this.imageFileNameWin.set(imageFileNameWin); }


    public void drawMaze(Maze maze) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("Generating maze: " + maze.length + ", " + maze[0].length);
//        alert.show();
        this.maze = maze;
        draw();
        setPosition(playerRow,playerCol,TargetRow,TargetCol); // set again the position of the player and the target when pressing generatemaze
    }

    public void draw(){
        if (maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getRows();
            int cols = maze.getCols();

            double cellHight = canvasHeight / rows * scroll;
            double cellWidth = canvasWidth / cols * scroll;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);

            drawMazeWalls(graphicsContext,rows,cols,cellHight,cellWidth);
            if (this.solved == false){
                if (solution != null)
                    drawSolution(graphicsContext,rows,cols,cellHight,cellWidth);
                drawPlayer(graphicsContext,cellHight,cellWidth);
                drawTarget(graphicsContext,cellHight,cellWidth);
                graphicsContext.stroke();
            }
            //if the player reached the goal target
            else {
                graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
                drawwin(graphicsContext,canvasWidth,canvasHeight);
                this.solved = false;
            }

        }
    }

    public void drawwin(GraphicsContext graphicsContext, double cellHight, double cellWidth) {
        Image winImage = null;
        try {
            winImage = new Image(new FileInputStream(getImageFileNameWin()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no win image");
        }
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.drawImage(winImage,0,0,cellHight,cellWidth);
    }
    public void drawwin() {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();

        GraphicsContext graphicsContext = getGraphicsContext2D();
        //clear the canvas
        graphicsContext.clearRect(0,0,canvasHeight,canvasWidth);
        Image winImage = null;
        try {
            winImage = new Image(new FileInputStream(getImageFileNameWin()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no win image");
        }
        graphicsContext.drawImage(winImage,0,0,canvasWidth,canvasHeight);
    }

        public void drawMazeSolution(Solution solution) {
        if (this.solution == null){
            this.solution = solution;
            draw();
        }
        else{
            this.solution = null;
            draw();
        }

    }

    public void drawSolution(GraphicsContext graphicsContext,int rows, int cols, double cellHight, double cellWidth){
        graphicsContext.setFill(Color.YELLOW);
        //maze.printRealMaze();
        ArrayList<AState> b = this.solution.getSolutionPath();

        for (int i = 0; i < b.size(); i++) {
            Position p = ((MazeState) b.get(i)).getCurrent();
            double x = p.getColumnIndex() * cellWidth;
            double y = p.getRowIndex() * cellHight;
            graphicsContext.fillRect(x,y,cellWidth,cellHight);
        }

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

        double x = getTargetCol() * cellWidth;
        double y = getTargetRow() * cellHight;

        if (TargetImage == null)
            graphicsContext.fillRect(x,y,cellWidth,cellHight);
        else
            graphicsContext.drawImage(TargetImage,x,y,cellWidth,cellHight);
    }
    public void setscroll(double scroll) { this.scroll = scroll; }

    public double getscroll() {
        return scroll;
    }

}
