import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.*;
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


public class AgentBoard implements Cloneable{
    private char[][][] board;
    /* store the heuristic value of the 9 cell */
    private int [] heuristic;
    private int sum_heuristic=0;

    GameState unitState;

    public AgentBoard() {
        init_game();
        init_heuristic();
        unitState = GameState.InProgress;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        AgentBoard new_agent_board = (AgentBoard) super.clone();
        return new_agent_board;
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

    /* clear up the whole tile board */
    /* reset the count of heuristic and game state */
    public void clear_up() {
        sum_heuristic = 0;
        unitState = GameState.InProgress;
        init_game();
    }

    /* initialize an empty heuristic array before the game start */
    public void init_heuristic(){
        heuristic = new int[9];
        for (int i=0; i<9;i++){
            heuristic[i] = 0;
        }
    }

    /* 
    *  Judge which position in that cell can move
    *  get all the positions can move for a particular cell
    *  return :- ArrayList 
    *  ie:  o x .
    *       . x .
    *       o o .
    *  so can_move return [3, 4, 6, 9]
    */
    public ArrayList can_move(int cell_number){
        ArrayList<Integer> legal_positions = new ArrayList<>();
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if (board[cell_number-1][i][j] == '.'){
                    legal_positions.add(i*3+j+1);
                }
            }
        }

        return legal_positions;
    }

    // 好像不用实现这个 直接写ab剪枝或者minmax
    /* MAY INVENT LOTS OF METHOD TO CALCULATE HEURISTIC TO DO SOME TEST
    *  THE FINAL HEURISTIC MAY USE OTHER METHODS
    *  This is a simple heuristic value calculation method, to calculate 
    *  whether the opponent is going to win.
    *  ie('x' is the opponent):
    *  cell 2:
    *  . . x   IF this is cell 2, we notice that the position 5 is a  
    *  . . o   legal position, if we extend this position, but cell 5
    *  . . .   like this 
    *
    *  cell 5:
    *  . o o   In this cell, the opponent 'x' is about to win, so we 
    *  . x .   can't move to 5 in cell 2, this cell's heuristic value
    *  o . x   should be the minimal
    *
    */
    public int oppnent_winning_heuristic(int cell_number){
        return 0;
    }

    // TODO reset_val
    public void undo_set_val(int num, int position_number){
        board[num-1][(position_number-1)/3][(position_number-1)%3] = '.';
    }

    /* update the map based on the player's action */
    public void set_val(int num, int position_number, char val) {
        board[num-1][(position_number-1)/3][(position_number-1)%3] = val;
    }

    public char get_position_player(int cell_number, int position_number){
        return board[cell_number-1][(position_number-1)/3][(position_number-1)%3];
    }

    public ArrayList move_win(int cell_number, char player){
        ArrayList<Integer> win_moves = new ArrayList<Integer>();
        return win_moves;

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

    // check in that cell whether player win
    public boolean cell_check_player_win(int cell, char player){
        for(int j=0; j<3; j++){
            // check whether x is win on ROW
            if(board[cell-1][j][0] == board[cell-1][j][1] && board[cell-1][j][0] == board[cell-1][j][2] && board[cell-1][j][0] == player){
                return true;
            }// check whether x is win on COLUMN
            else if(board[cell-1][0][j] == board[cell-1][1][j] && board[cell-1][0][j] == board[cell-1][2][j] && board[cell-1][0][j] == player){
                return true;
            }// check whether x is win on SLOPE
            else if(board[cell-1][0][0] == board[cell-1][1][1] && board[cell-1][0][0] == board[cell-1][2][2] && board[cell-1][0][0] == player){
                return true;
            }
            else if(board[cell-1][0][2] == board[cell-1][1][1] && board[cell-1][0][2] == board[cell-1][2][0] && board[cell-1][0][2] == player){
                return true;
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

    public boolean cell_is_full(int cell){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[cell-1][i][j] == '.') {
                    return false;
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

    /* help to write evaulation function check */
    /* check the possible way that a specific player can win */
    /* e.g.  x . .
             . o .
             . . .
       for x, there has 2 possible way that x can win, hence return 2 in this example
    */

    public int evaluateHelper(int connected, int cell, char player) {
        int count = 0;
        if (connected == 2) {

        } else if (connected == 1) {
            for (int i = 1; i < 10; i++) {
                if (get_position_player(cell, i) == player) {
                    if (evaluateRow(cell, i)) {
                        count++;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("please enter valid number of connected you want to check!");
        }
        return count;
    }

    /* give the position of the player in a specific cell */
    /* check if it can occupy the whole row */
    public boolean evaluateRow(int cell, int position) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        if (col == 0) {
            if (board[cell-1][row][col+1] == board[cell-1][row][col+2] && board[cell-1][row][col+1] == '.') {
                return true;
            }
        } else if (col == 1) {
            if (board[cell-1][row][col-1] == board[cell-1][row][col+1] && board[cell-1][row][col+1] == '.') {
                return true;
            }
        } else if (col == 2) {
            if (board[cell-1][row][col-1] == board[cell-1][row][col-2] && board[cell-1][row][col-2] == '.') {
                return true;
            }
        }
        return false;
    }

    /* give the position of the player in a specific cell */
    /* check if it can occupy the whole row */
    public boolean evaluateCol(int cell, int position) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        if (col == 0) {
            if (board[cell-1][row][col+1] == board[cell-1][row][col+2] && board[cell-1][row][col+1] == '.') {
                return true;
            }
        } else if (col == 1) {
            if (board[cell-1][row][col-1] == board[cell-1][row][col+1] && board[cell-1][row][col+1] == '.') {
                return true;
            }
        } else if (col == 2) {
            if (board[cell-1][row][col-1] == board[cell-1][row][col-2] && board[cell-1][row][col-2] == '.') {
                return true;
            }
        }
        return false;
    }

    /* check if there has two connected player already, like */
    /* if there does, return the position of each one */
    /* e.g.  x x .
             . o .
             . . .
    */


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
        System.out.println(agent_board.can_move(5));
        System.out.println(agent_board.can_move(7));

        System.out.println(agent_board.check_player_win('o'));
        System.out.println(agent_board.game_over());

        System.out.println(agent_board.is_full());
        System.out.println(agent_board.evaluateHelper(1, 3,'o'));
    }


}