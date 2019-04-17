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

    public void set_val(int num, int num_cell, char val){
        System.out.println(board.length);
        System.out.println(board[0].length);
        System.out.println(num_cell/4 + ""+ num_cell%4);
        board[num-1][(num_cell-1)/3][(num_cell-1)%3] = val;
    }

    public void display_board(){

        for(int row=0; row<9; row++){ 
            for(int i=0; i<3; i++){
                for(int k=0;k<3;k++){
                    System.out.print(board[row/3*3+i][row % 3][k] + " ");
                }
                if(i != 2){
                    System.out.print("* ");
                }
            }

            System.out.println();
            
            if (row % 3 == 2 && row != 8){
                for (int j=0; j<11;j++){
                    System.out.print("* ");
                }
                System.out.println();
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Testing begin");
        AgentBoard agent_board = new AgentBoard();
        agent_board.set_val(5, 3, 'o');
        agent_board.set_val(3, 9, 'o');
        agent_board.set_val(7, 1, 'o');
        agent_board.display_board();
    }


}