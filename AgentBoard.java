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
    private static char[][][] board;
    /* store the heuristic value of the 9 cell */
    private int [] heuristic;
    private int sumHeuristic=0;
    private char currentTurn;

    GameState unitState;

    public AgentBoard() {
        initGame();
        initHeuristic();
        unitState = GameState.InProgress;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        AgentBoard newAgentBoard = (AgentBoard) super.clone();
        return newAgentBoard;
    }

    /* initialize an empty map before the game start */
    public void initGame() {
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
    public void clearUp() {
        sumHeuristic = 0;
        unitState = GameState.InProgress;
        initGame();

    }

    /* initialize an empty heuristic array before the game start */
    public void initHeuristic(){
        heuristic = new int[9];
        for (int i=0; i<9;i++){
            heuristic[i] = 0;
        }
    }

    /* set the current player */
    public void setCurrentTurn(char player) {
        currentTurn = player;
    }

    /* return the current player */
    public char getCurrentTurn() {
        return currentTurn;
    }

    /* 
    *  Judge which position in that cell can move
    *  get all the positions can move for a particular cell
    *  return :- ArrayList 
    *  ie:  o x .
    *       . x .
    *       o o .
    *  so canMove return [3, 4, 6, 9]
    */
    public ArrayList<Integer> canMove(int cellNumber){
        ArrayList<Integer> legalPositions = new ArrayList<>();
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if (board[cellNumber-1][i][j] == '.'){
                    legalPositions.add(i*3+j+1);
                }
            }
        }

        return legalPositions;
    }

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
    public int oppnentWinningHeuristic(int cellNumber){
        return 0;
    }

    // TODO resetVal
    public void undoSetVal(int num, int positionNumber){
        board[num-1][(positionNumber-1)/3][(positionNumber-1)%3] = '.';
    }

    /* update the map based on the player's action */
    public void setVal(int num, int positionNumber, char val) {
        board[num-1][(positionNumber-1)/3][(positionNumber-1)%3] = val;
    }

    public char getPositionPlayer(int cellNumber, int positionNumber){
        return board[cellNumber-1][(positionNumber-1)/3][(positionNumber-1)%3];
    }

    public ArrayList moveWin(int cellNumber, char player){
        ArrayList<Integer> winMoves = new ArrayList<Integer>();
        return winMoves;

    }


    /* check if a specific player win the games or not */
    public boolean checkPlayerWin(char player){
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
    public boolean cellCheckPlayerWin(int cell, char player){
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

    public boolean isFull() {

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

    public boolean cellIsFull(int cell){
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
    public boolean gameOver(){
        /* player x win the game */
        if (checkPlayerWin('x')){
            unitState = GameState.GameOver;
            return true;

        /* player o win the game */
        }else if(checkPlayerWin('o')){
            unitState = GameState.GameOver;
            return true;
        }

        /* game is tie */
        else if (isFull()) {
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
            count = evaluateConnectedTwo(cell, player);
        } else if (connected == 1) {
            for (int i = 1; i < 10; i++) {
                if (getPositionPlayer(cell, i) == player) {
                    if (evaluateRow(cell, i)) {
                        count++;
                    }
                    if (evaluateCol(cell, i)) {
                        count++;
                    }
                    if (evaluateDiagonal(cell, i)) {
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
    /* check if it can occupy the whole column */
    public boolean evaluateCol(int cell, int position) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        if (row == 0) {
            if (board[cell-1][row+1][col] == board[cell-1][row+2][col] && board[cell-1][row+1][col] == '.') {
                return true;
            }
        } else if (row == 1) {
            if (board[cell-1][row-1][col] == board[cell-1][row+1][col] && board[cell-1][row-1][col] == '.') {
                return true;
            }
        } else if (col == 2) {
            if (board[cell-1][row-1][col] == board[cell-1][row-2][col] && board[cell-1][row-1][col] == '.') {
                return true;
            }
        }
        return false;
    }

    /* give the position of the player in a specific cell */
    /* check if it can occupy the whole diagonal */
    public boolean evaluateDiagonal(int cell, int position) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        if (row == 0 && col == 0) {
            if (board[cell-1][row+1][col+1] == board[cell-1][row+2][col+2] && board[cell-1][row+1][col+1] == '.') {
                return true;
            }
        } else if (row == 0 && col == 2) {
            if (board[cell - 1][row + 1][col - 1] == board[cell - 1][row + 2][col - 2] && board[cell - 1][row + 1][col - 1] == '.') {
                return true;
            }
        } else if (row == 1 && col == 1) {
            if (board[cell-1][row-1][col-1] == board[cell-1][row+1][col+1] && board[cell-1][row-1][col-1] == '.') {
                return true;
            } else if (board[cell-1][row-1][col+1] == board[cell-1][row+1][col-1] && board[cell-1][row-1][col+1] == '.') {
                return true;
            }
        } else if (row == 2 && col == 0) {
            if (board[cell-1][row-1][col+1] == board[cell-1][row-2][col+2] && board[cell-1][row-1][col+1] == '.') {
                return true;
            }
        }else if (row == 2 && col == 2) {
            if (board[cell-1][row-1][col-1] == board[cell-1][row-2][col-2] && board[cell-1][row-1][col-1] == '.') {
                return true;
            }
        }
        return false;
    }

    // check the number of row, columns or diagonals with exactly 2 players
    // then return the value;
    public int evaluateConnectedTwo(int cell, char player) {
        // check row
        int count = 0;
        for (int row = 0; row < 3; row++) {
            int rowCount = 0;
            for (int col = 0; col < 3; col++) {
                if (board[cell-1][row][col] == player) {
                    rowCount++;
                }
            }
            if (rowCount == 2) {
                count += 1;
            }
        }

        // check column
        for (int col = 0; col < 3; col++) {
            int colCount = 0;
            for (int row = 0; row < 3; row++) {
                if (board[cell-1][row][col] == player) {
                    colCount++;
                }
            }
            if (colCount == 2) {
                count += 1;
            }
        }

        // check diagonal from top left
        int diaCountL = 0;
        for (int row = 0; row < 3; row++) {
            if (board[cell-1][row][row] == player) {
                //System.out.println("LRow is : " + row);
                diaCountL++;
            }
        }

        if (diaCountL == 2) {
            count += 1;
        }

        //check diagonal from top right;
        int diaCountR = 0;
        for (int row = 2; row >= 0; row--) {
            if (board[cell-1][row][2-row] == player) {
                //System.out.println("RRow is : " + row);
                diaCountR++;
            }
        }
        if (diaCountR == 2) {
            count += 1;
        }
        return count;

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

    public void displayBoard(){

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
        //agent_board.setVal(5, 3, 'o');
        //agent_board.setVal(2, 1, 'x');
        //agent_board.setVal(2, 2, 'o');
        agent_board.setVal(3, 1, 'x');
        //agent_board.setVal(7, 1, 'o');
        //agent_board.setVal(7, 5, 'o');
        //agent_board.setVal(7, 9, 'o');
        //agent_board.setVal(5, 2, 'o');
        //agent_board.setVal(5, 5, 'o');
        //agent_board.setVal(5, 8, 'o');
        agent_board.displayBoard();
        System.out.println(agent_board.canMove(5));
        System.out.println(agent_board.canMove(7));

        System.out.println(agent_board.checkPlayerWin('o'));
        System.out.println(agent_board.gameOver());

        System.out.println(agent_board.isFull());
//        System.out.println(agent_board.evaluateHelper(1, 2,'x'));
//        System.out.println(agent_board.evaluateHelper(1, 2,'o'));
//        System.out.println(agent_board.evaluateHelper(2, 5,'o'));
//        System.out.println(agent_board.evaluateHelper(1, 5,'o'));

        CellHeuristic test = new CellHeuristic();
        agent_board.setCurrentTurn('x');
        int result = test.cellEvaluation(agent_board, 3, 'x');
        System.out.println("score of board 2 is " + result);

        agent_board.setCurrentTurn('o');
        int bestMove = test.getBestMove('o', 1, agent_board, 2);
        agent_board.setVal(1, bestMove, 'o');
        System.out.println("best move is " + bestMove);
        agent_board.displayBoard();

        agent_board.setCurrentTurn('x');
        bestMove = test.getBestMove('x', 3, agent_board, 2);
        System.out.println("best move is " + bestMove);
        agent_board.setVal(3, bestMove, 'x');
        agent_board.displayBoard();

//        bestMove = test.getBestMove('o', 3, agent_board, 2);
//        System.out.println("best move is " + bestMove);
//        agent_board.setVal(3, bestMove, 'o');
//        agent_board.displayBoard();
//
//        bestMove = test.getBestMove('x', 3, agent_board, 2);
//        System.out.println("best move is " + bestMove);
//        agent_board.setVal(3, bestMove, 'x');
//        agent_board.displayBoard();
//
//        bestMove = test.getBestMove('o', 3, agent_board, 2);
//        System.out.println("best move is " + bestMove);
//        agent_board.setVal(3, bestMove, 'o');
//        agent_board.displayBoard();

    }


}
