/**
 * Inspired by Board.java, Cell.java 
 * Dependency: GameState.java
 * 
 * 
 *1.Simulate the board
 *2.Simplify board operation
 *3.Display the board
 * 
 * AgentBoard
 */

public class AgentBoard {
    private char[][][] board;
    /* store the heuristic value of the 9 cell */
    private int [] heuristic;
    private int sum_heuristic;
    
    private char agent = "o";

    GameState unitState;

    public AgentBoard() {
        init_game();
        init_heuristic();
        unitState = GameState.InProgress;
    }

    /* initialize an empty map before the game start */
    public void init_game() {
        board = new char[9][3][3];
        for(int i=0; i<9; i++){
            for(int j=0; j<3; j++){
                for(int k=0; k<3; k++){
                    board[i][j][k] = '.';
                }
            }
        }
    }

    /* initialize an empty heuristic array before the game start */
    public void init_heuristic(){
        heuristic = new int[9];
        for (int i=0; i<9;i++){
            heuristic = 0;
        }
    }

    /* update the map based on the player's action*/
    public void set_val(int num, int num_cell, char val) {
        board[num-1][(num_cell-1)/3][(num_cell-1)%3] = val;
    }

    /* check if a specific player win the games or not */
    public boolean check_player_win(char player){
        for(int i=0; i<9; i++){
            for(int j=0; j<3; j++){
                // check whether x is win on ROW
                if(board[i][j][0] == board[i][j][1] && board[i][j][0] == board[i][j][2] && board[i][j][0] == player){
                    return true;
                }// check whether x is win on COLUMN
                else if(board[i][0][j] == board[i][1][j] && board[i][0][j] == board[i][2][j] && board[i][0][j] == player){
                    return true;
                }// check whether x is win on SLOPE
                else if(board[i][0][0] == board[i][1][1] && board[i][0][0] == board[i][2][2] && board[i][0][0] == player){
                    return true;
                }
                else if(board[i][0][2] == board[i][1][1] && board[i][0][2] == board[i][2][0] && board[i][0][2] == player){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean is_full() {

        for (int num = 0; num < 9; num++) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[num][i][j] == '.') {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /* check if game status finish or not*/
    public boolean game_over(){
        /* player x win the game */
        if (check_player_win('x')){
            unitState = GameState.GameOver;
            return true;

        /* player o win the game */
        }else if(check_player_win('o')){
            unitState = GameState.GameOver;
            return true;
        }

        /* game is tie */
        else if (is_full()) {
            unitState = GameState.GameOver;
            return true;
        }
        return false;
    }

    // Display the board like this:
    /*
        . . . * . . . * . . . 
        . . . * . . . * . . . 
        . . . * . . . * . . o 
        * * * * * * * * * * * 
        . . . * . . o * . . . 
        . . . * . . . * . . . 
        . . . * . . . * . . . 
        * * * * * * * * * * * 
        o . . * . . . * . . . 
        . . . * . . . * . . . 
        . . . * . . . * . . . 
    */

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
        agent_board.set_val(7, 5, 'o');
        agent_board.set_val(7, 9, 'o');
        agent_board.display_board();
        System.out.println(agent_board.check_player_win('o'));
        System.out.println(agent_board.game_over());

        /* test function is full */
        for (int index = 1; index < 10; index++) {
            for (int i = 1; i < 10; i++) {
                agent_board.set_val(index, i, 'o');
            }
        }
        agent_board.display_board();
        System.out.println(agent_board.is_full());
    }


}