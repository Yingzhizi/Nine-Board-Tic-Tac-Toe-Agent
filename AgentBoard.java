import java.util.*;


/**
 * AgentBoard contains a map keep track of each movement
 */
public class AgentBoard implements Cloneable{
    private static char[][][] board;
    /* store the heuristic value of the 9 cell */
    private int [] heuristic;
    private int sumHeuristic=0;
    private char currentTurn;
    /* track the state of the board, in progress or game over */
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


    /**
     * initialize an empty map before the game start
     */
    public void initGame() {
        board = new char[9][3][3];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < 3; k++){
                    board[i][j][k] = '.';
                }
            }
        }
    }

    /**
     * clear up the whole board
     * reset the count of heuristic and game state
     */
    public void clearUp() {
        sumHeuristic = 0;
        unitState = GameState.InProgress;
        initGame();

    }

    /**
     * initialize an empty heuristic array before the game start
     */
    public void initHeuristic(){
        heuristic = new int[9];
        for (int i = 0; i < 9; i++){
            heuristic[i] = 0;
        }
    }

    /**
     * set the current player
     * @param player the current player for this board
     */
    public void setCurrentTurn(char player) {
        currentTurn = player;
    }

    /**
     * return the current player
     * @return char
     */
    public char getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Judge which position in that cell can move
     * get all the positions can move for a particular cell
     * @param cellNumber
     * @return ArrayList
     */
    public ArrayList<Integer> canMove(int cellNumber){
        ArrayList<Integer> legalPositions = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (board[cellNumber-1][i][j] == '.'){
                    legalPositions.add(i * 3 + j + 1);
                }
            }
        }

        return legalPositions;
    }

    /**
     * get the index of moves where each row, column or diagonal with exactly 2 given player
     * and no opponent, then return the array list of available moves;
     * @param cell
     * @param player
     * @return ArrayList
     */
    public ArrayList<Integer> cellGetTwo(int cell, char player){
        ArrayList<Integer> moves = new ArrayList<Integer>();
        // check whether row get two in one row
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (j == 0){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i][j + 1] == board[cell - 1][i][j] && board[cell - 1][i][j + 2] == '.'){
                        moves.add(i * 3 + j + 1 + 2);
                    }
                    if (board[cell - 1][i][j] == player && board[cell - 1][i][j + 2] == board[cell - 1][i][j] && board[cell - 1][i][j + 1] == '.'){
                        moves.add(i * 3 + j + 1 + 1);
                    }
                }else if(j == 1){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i][j + 1] == board[cell - 1][i][j] && board[cell - 1][i][j - 1] == '.'){
                        moves.add(i * 3 + j + 1 - 1);
                    }
                }
            }
        }

        // check whether column get two in one column
        for(int j = 0; j < 3; j++){
            for(int i = 0; i < 3; i++){
                if (i == 0){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i + 1][j] == board[cell - 1][i][j] && board[cell - 1][i + 2][j] == '.'){
                        moves.add(7 + j);
                    }
                    if (board[cell - 1][i][j] == player && board[cell - 1][i + 2][j] == board[cell - 1][i][j] && board[cell - 1][i + 1][j] == '.'){
                        moves.add(4 + j);
                    }
                }else if(i == 1){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i + 1][j] == board[cell - 1][i][j] && board[cell - 1][i - 1][j] == '.'){
                        moves.add(1 + j);
                    }
                }
            }
        }

        // check whether diagonal get two in one diagonal
        if(board[cell - 1][1][1] == player && board[cell - 1][0][0] == board[cell - 1][1][1] && board[cell - 1][2][2] == '.'){
            moves.add(9);
        }
        if(board[cell - 1][1][1] == player && board[cell - 1][2][2] == board[cell - 1][1][1] && board[cell - 1][0][0] == '.'){
            moves.add(1);
        }
        if(board[cell - 1][1][1] == '.' && board[cell - 1][2][2] == board[cell - 1][0][0] && board[cell - 1][0][0] == player){
            moves.add(5);
        }
        if(board[cell - 1][1][1] == '.' && board[cell - 1][2][0] == board[cell - 1][0][2] && board[cell - 1][2][0] == player){
            moves.add(5);
        }
        if(board[cell - 1][1][1] == player && board[cell - 1][0][2] == board[cell - 1][1][1] && board[cell - 1][2][0] == '.'){
            moves.add(7);
        }
        if(board[cell - 1][1][1] == player && board[cell - 1][2][0] == board[cell - 1][1][1] && board[cell - 1][0][2] == '.'){
            moves.add(3);
        }

        return moves;
    }

    /**
     * MAY INVENT LOTS OF METHOD TO CALCULATE HEURISTIC TO DO SOME TEST
     * THE FINAL HEURISTIC MAY USE OTHER METHODS
     * This is a simple heuristic value calculation method, to calculate
     * whether the opponent is going to win.
     * ie('x' is the opponent):
     * cell 2:
     * . . x   IF this is cell 2, we notice that the position 5 is a
     * . . o   legal position, if we extend this position, but cell 5
     * . . .   like this
     *
     * cell 5:
     * . o o   In this cell, the opponent 'x' is about to win, so we
     * . x .   can't move to 5 in cell 2, this cell's heuristic value
     * o . x   should be the minimal
     *
     */
    public int oppnentWinningHeuristic(int cellNumber){
        return 0;
    }

    /**
     * clear up the value in given location of a given cell number
     * set it to empty
     * @param num the cell number among 9 different cells
     * @param positionNumber the position of a given cell
     */
    public void undoSetVal(int num, int positionNumber){
        board[num - 1][(positionNumber - 1) / 3][(positionNumber - 1) % 3] = '.';
    }

    /**
     * update the map based on the player's action
     */
    public void setVal(int num, int positionNumber, char val) {
        board[num - 1][(positionNumber - 1) / 3][(positionNumber - 1) % 3] = val;
    }

    /**
     * check each movement is legal or not,
     */
    public boolean isLegal(int cell, int positionNumber){
        if (board[cell - 1][(positionNumber - 1) / 3][(positionNumber - 1) % 3] == '.'){
            return true;
        }
        return false;
    }

    /**
     * get the value of a given position from a specific cell
     */
    public char getPositionPlayer(int cellNumber, int positionNumber){
        return board[cellNumber-1][(positionNumber-1)/3][(positionNumber-1)%3];
    }

    public ArrayList<Integer> getPositionsCell(int cell, char player){
        ArrayList<Integer> res = new ArrayList<Integer>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (board[cell - 1][i][j] == player) {
                    res.add(i * 3 + j + 1);
                }
            }
        }
        return res;
    }

    public int winLine(int cell, char player){
        int sumWinLine = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (i == 0){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i + 1][j] == '.' && board[cell - 1][i + 2][j] == '.'){
                        sumWinLine += 1;
                    }
                    if (j == 0){
                        if (board[cell - 1][i][j] == player && board[cell - 1][i + 1][j + 1] == '.' && board[cell - 1][i + 2][j + 2] == '.'){
                            sumWinLine += 1;
                        }
                    }
                    if (j == 2){
                        if (board[cell - 1][i][j] == player && board[cell - 1][i + 1][j - 1] == '.' && board[cell - 1][i + 2][j - 2] == '.'){
                            sumWinLine += 1;
                        }
                    }
                }else if(i == 1){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i + 1][j] == '.' && board[cell - 1][i - 1][j] == '.'){
                        sumWinLine += 1;
                    }
                    if (j == 1){
                        if (board[cell - 1][i][j] == player && board[cell - 1][i+1][j-1] == '.' && board[cell - 1][i-1][j+1] == '.'){
                            sumWinLine += 1;
                        }
                        if (board[cell - 1][i][j] == player && board[cell - 1][i-1][j-1] == '.' && board[cell - 1][i+1][j+1] == '.'){
                            sumWinLine += 1;
                        }
                    }
                }else if(i == 2){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i-1][j] == '.' && board[cell - 1][i-2][j] == '.'){
                        sumWinLine += 1;
                    }
                    if (j == 2){
                        if (board[cell - 1][i][j] == player && board[cell - 1][i-1][j-1] == '.' && board[cell - 1][i-2][j-2] == '.'){
                            sumWinLine += 1;
                        }
                    }
                }

                if (j == 0){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i][j+1] == '.' && board[cell - 1][i][j+2] == '.'){
                        sumWinLine += 1;
                    }
                }else if(j == 1){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i][j-1] == '.' && board[cell - 1][i][j+1] == '.'){
                        sumWinLine += 1;
                    }
                }else if(j == 2){
                    if (board[cell - 1][i][j] == player && board[cell - 1][i][j-1] == '.' && board[cell - 1][i][j-2] == '.'){
                        sumWinLine += 1;
                    }
                }

            }
        }
        return sumWinLine;
    }

    public ArrayList moveWin(int cellNumber, char player){
        ArrayList<Integer> winMoves = new ArrayList<Integer>();
        return winMoves;

    }

    /**
     * check if a specific player win the game or not
     * @param player
     * @return boolean
     */
    public boolean checkPlayerWin(char player){
        for(int cell = 1; cell < 10; cell++){
            if (!cellCheckPlayerWin(cell, player)) {
                return false;
            }
        }
        return true;
    }

    /**
     * check in a specific cell whether a player win or not
     * @param cell
     * @param player
     * @return boolean
     */
    public boolean cellCheckPlayerWin(int cell, char player){
        for(int j = 0; j < 3; j++){
            // check whether x is win on ROW
            if(board[cell - 1][j][0] == board[cell - 1][j][1] && board[cell - 1][j][0] == board[cell - 1][j][2] && board[cell - 1][j][0] == player){
                return true;
            }
            // check whether x is win on COLUMN
            else if(board[cell - 1][0][j] == board[cell - 1][1][j] && board[cell - 1][0][j] == board[cell - 1][2][j] && board[cell - 1][0][j] == player){
                return true;
            }
            // check whether x is win on SLOPE
            else if(board[cell - 1][0][0] == board[cell - 1][1][1] && board[cell - 1][0][0] == board[cell - 1][2][2] && board[cell - 1][0][0] == player){
                return true;
            }
            else if(board[cell - 1][0][2] == board[cell - 1][1][1] && board[cell - 1][0][2] == board[cell - 1][2][0] && board[cell - 1][0][2] == player){
                return true;
            }
        }
        return false;
    }

    /**
     * check if the whole 3*3 array contains tic tac toe board is full
     * @return board
     */
    public boolean isFull() {
        for (int num = 0; num < 9; num++) {
            if (!cellIsFull(num)) {
                return false;
            }
        }
        return true;
    }

    /**
     * for a specific cell, check if this cell if full
     * @param cell cell number
     * @return boolean
     */
    public boolean cellIsFull(int cell){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[cell - 1][i][j] == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * check if game status finish or not
     * @return boolean
     */
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

    /* e.g.  x . .
             . o .
             . . .
       for x, there has 2 possible way that x can win, hence return 2 in this example
    */

    /**
     * help to write evaluation function check
     * check the possible way that a specific player can win
     * @param connected the possible way of "connected" number of players in all row, column or diagonal
     * @param cell cell number
     * @param player the player we need to check
     * @return int
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

    /**
     * give the position of the player in a specific cell
     * check if it can occupy the whole row
     */
    public boolean evaluateRow(int cell, int position) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        if (col == 0) {
            if (board[cell - 1][row][col + 1] == board[cell - 1][row][col + 2] && board[cell - 1][row][col + 1] == '.') {
                return true;
            }
        } else if (col == 1) {
            if (board[cell - 1][row][col - 1] == board[cell - 1][row][col + 1] && board[cell - 1][row][col + 1] == '.') {
                return true;
            }
        } else if (col == 2) {
            if (board[cell - 1][row][col - 1] == board[cell - 1][row][col - 2] && board[cell - 1][row][col - 2] == '.') {
                return true;
            }
        }
        return false;
    }

    /**
     * give the position of the player in a specific cell
     * check if it can occupy the whole column
     */
    public boolean evaluateCol(int cell, int position) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        if (row == 0) {
            if (board[cell - 1][row + 1][col] == board[cell - 1][row + 2][col] && board[cell - 1][row + 1][col] == '.') {
                return true;
            }
        } else if (row == 1) {
            if (board[cell - 1][row - 1][col] == board[cell - 1][row + 1][col] && board[cell - 1][row - 1][col] == '.') {
                return true;
            }
        } else if (col == 2) {
            if (board[cell - 1][row - 1][col] == board[cell - 1][row - 2][col] && board[cell - 1][row - 1][col] == '.') {
                return true;
            }
        }
        return false;
    }

    /**
     * give the position of the player in a specific cell
     * check if it can occupy the whole diagonal
     */
    public boolean evaluateDiagonal(int cell, int position) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        if (row == 0 && col == 0) {
            if (board[cell - 1][row + 1][col + 1] == board[cell - 1][row + 2][col + 2] && board[cell - 1][row + 1][col + 1] == '.') {
                return true;
            }
        } else if (row == 0 && col == 2) {
            if (board[cell - 1][row + 1][col - 1] == board[cell - 1][row + 2][col - 2] && board[cell - 1][row + 1][col - 1] == '.') {
                return true;
            }
        } else if (row == 1 && col == 1) {
            if (board[cell - 1][row - 1][col - 1] == board[cell - 1][row + 1][col + 1] && board[cell - 1][row - 1][col - 1] == '.') {
                return true;
            } else if (board[cell - 1][row - 1][col + 1] == board[cell - 1][row + 1][col - 1] && board[cell - 1][row - 1][col + 1] == '.') {
                return true;
            }
        } else if (row == 2 && col == 0) {
            if (board[cell - 1][row - 1][col + 1] == board[cell - 1][row - 2][col + 2] && board[cell - 1][row - 1][col + 1] == '.') {
                return true;
            }
        }else if (row == 2 && col == 2) {
            if (board[cell - 1][row - 1][col - 1] == board[cell - 1][row - 2][col - 2] && board[cell - 1][row - 1][col - 1] == '.') {
                return true;
            }
        }
        return false;
    }

    /**
     * check the number of row, columns or diagonals with exactly 2 players
     * then return the value
     */
    public int evaluateConnectedTwo(int cell, char player) {
        // check row
        int count = 0;
        for (int row = 0; row < 3; row++) {
            int rowCount = 0;
            for (int col = 0; col < 3; col++) {
                if (board[cell - 1][row][col] == player) {
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
                if (board[cell - 1][row][col] == player) {
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
            if (board[cell - 1][row][row] == player) {
                diaCountL++;
            }
        }

        if (diaCountL == 2) {
            count += 1;
        }

        // check diagonal from top right;
        int diaCountR = 0;
        for (int row = 2; row >= 0; row--) {
            if (board[cell - 1][row][2-row] == player) {
                diaCountR++;
            }
        }
        if (diaCountR == 2) {
            count += 1;
        }
        return count;

    }


    /**
     * Display the board
     */
    public void displayBoard(){

        for(int row = 0; row < 9; row++){
            for(int i = 0; i < 3; i++){
                for(int k = 0; k < 3; k++){
                    System.out.print(board[row / 3 * 3 + i][row % 3][k] + " ");
                }
                if(i != 2){
                    System.out.print("* ");
                }
            }
            System.out.println();
            if (row % 3 == 2 && row != 8){
                for (int j = 0; j < 11; j++){
                    System.out.print("* ");
                }
                System.out.println();
            }
        }
    }
}
