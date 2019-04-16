/* this is a 3x3 array of single tic-tac-toe board*/
public class TicTacToeMap {
    private Board[][] boards;
    private GameState state;

    private void initBoard() {
        boards = new Board[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boards[i][j] = new Board();
            }
        }
    }
}
