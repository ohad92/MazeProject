package View;

import javafx.event.ActionEvent;

public interface IView {
    void generateMaze(ActionEvent actionEvent);
    void solveMaze(ActionEvent actionEvent);
    void SaveMaze(ActionEvent actionEvent);
    void LoadMaze(ActionEvent actionEvent);
    void ExitGame(ActionEvent actionEvent);
    void HelpButton(ActionEvent actionEvent);
    void AboutUsButton(ActionEvent actionEvent);
}
