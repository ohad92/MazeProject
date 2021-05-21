package View;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    public void drawMaze(int[][] maze) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("Generating maze: " + maze.length + ", " + maze[0].length);
//        alert.show();
        this.maze = maze;
        draw();
    }

    private void draw(){
        if (maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);

            graphicsContext.setFill(Color.RED);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if(maze[i][j] == 1){
                        double x = j * cellWidth;
                        double y = i * cellHight;

                        graphicsContext.fillRect(x,y,cellWidth,cellHight);
                    }



                }

            }
        }
    }
}
