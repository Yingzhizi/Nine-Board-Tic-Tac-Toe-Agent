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
    private char[][][] board;
    GameState unitState;

    public void init_game(){
        board = new char[9][3][3];
        for(int i=0; i<9; i++){
            for(int j=0; j<3; j++){
                for(int k=0; k<3; k++){
                    board[i][j][k] = '.';
                }
            }
        }
    }

    public AgentBoard() {
        init_game();
        unitState = GameState.InProgress;
    }

    public void set_val(int row, int col, int val){

    }

    public void display_board(){

        for(int row=0; row<9; row++){ 
            for(int i=0; i<3; i++){
                for(int k=0;k<3;k++){
                    System.out.print(board[row/3+i][row % 3][k] + " ");
                }
                if(i != 2){
                    System.out.print("* ");
                }
                
                System.out.println();
            
            if (row % 3 == 2){
                for (int j=0; j<11;j++){
                    System.out.print("* ");
                }
                System.out.println();
            }
            }
        }
    }
    
    public static void main(String[] args) {
        AgentBoard agent_board = new AgentBoard();
        agent_board.display_board();
    }


}