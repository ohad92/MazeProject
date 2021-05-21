package View;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class Controller {
    private MazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;

    public void generateMaze(ActionEvent actionEvent) {
        if (generator == null)
            generator = new MazeGenerator();
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());

        int[][] maze = generator.generateRandomMaze(rows,cols);

        mazeDisplayer.drawMaze(maze);
    }

    public void solveMaze(ActionEvent actionEvent) {
    }
}