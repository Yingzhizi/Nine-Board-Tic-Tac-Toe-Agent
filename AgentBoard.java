/**
 * Inspired by Board.java, Cell.java 
 * Dependency: GameState.java
 * 
 * 
 *TODO 1.Simulate the board
 *TODO 2.Simplify board operation
 *TODO 3.Display the board
 * 
 * AgentBoard
 */

public class AgentBoard {
    private char[][] board;
    GameState unitState;

    public void init_game(){
        board = new char[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                board[i][j] = '.';
            }
        }
    }

    public AgentBoard() {
        init_game();
        unitState = GameState.InProgress;
    }

    public void display_board(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                System.out.printf(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        AgentBoard agent_board = new AgentBoard();
        agent_board.display_board();
    }


}