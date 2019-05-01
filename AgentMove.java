import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * class AgentMove
 * This class indicate th agent
 */
public class AgentMove {
    /* current player */
    private char player = 'o';
    private int count = 0;

    /* default agent, modify at the beginning */
    private char agent = 'o';
    private char opponent = 'x';
    private int lastMove = 0;
    
    /* change agent to singleton design pattern
    *  set global entry for agent
    */
    private AgentBoard board = new AgentBoard();
    private static AgentMove singletonAgent = new AgentMove();
    private AgentMove(){}
    public static AgentMove getAgent(){
        return singletonAgent;
    }

    public void printArray(String s, ArrayList<Integer> arr){
        System.out.print(s + " [");
        for (int i=0; i<arr.size(); i++){
            System.out.print(arr.get(i) + " ");
        }
        System.out.println("]");
    }

    public void printKillerMove(String s, boolean[][] arr){
        System.out.print(s + " [");
        for(int i=0; i<arr.length;i++){
            for (int j=0; j<arr[0].length; j++){
                if(arr[i][j] == true){
                    System.out.print(i + " <- " + j + " ");
                } 
            }
        }
        System.out.println("]");
    }

    public void setVal(int cell, int positionNumber, char val){
        board.setVal(cell, positionNumber, val);
    }

    public void displayBoard(){
        board.displayBoard();
    }

    /*  use at the beginning of game, 
    after receive secondMove or thirdMove  */
    public void setAgent(char x){
        agent = (x == 'o') ? 'o' : 'x';
        opponent = (x == 'o') ? 'x': 'o';
    }

    /* get opponent */
    public char getOpponent(char player){
        char oppo = (player == 'o') ? 'x': 'o';
        return oppo;
    }

    /* switch player for alpha-beta's min-max node */
    public char switchPlayer(){
        player = (player == 'x') ? 'o': 'x';
        return player;
    }

    /* main function for agent to play the game 
    *  including:
    *       decesion making, return integer, winner juding
    */
    public int playSecondMove(int cell, int firstMove){
        board.setVal(cell, firstMove, 'x');
        setAgent('o');
        board.setVal(cell, firstMove, opponent);
        int bestMove = getBestMove(firstMove);
        board.setVal(firstMove, bestMove, agent);
        lastMove = bestMove;

        return bestMove;
    }

    /**
     * IF receive the third_move() function, then call this
     * set Agent to 'x' and opponent to 'o'
     * @param first indicate the first move
     * @param second 
     * @param third
     * @return the best move
     */
    public int playThirdMove(int first, int second, int third){
        setAgent('x');
        board.setVal(first, second, agent);
        board.setVal(second, third, opponent);
        int bestMove = getBestMove(third);
        lastMove = bestMove;

        return bestMove;
    }

    /**
     * IF find best move returns an illegal move or the move that lead to agent lose
     * THEN find substitute move replace it, the substitute move has the highest 
     * heuristic value.
     * @param opponentMove
     * @return the best move
     */
    public int playNextMove(int opponentMove){
        count += 1;
        board.setVal(lastMove, opponentMove, opponent);
        int bestMove = getBestMove(opponentMove);
        if (!board.isLegal(opponentMove, bestMove)){
            int substituteMove = findSubstituteMove(opponentMove);
            board.setVal(opponentMove, substituteMove, agent);
            lastMove = substituteMove;
            return substituteMove;
        }
        /**
         * set best move
         */
        board.setVal(opponentMove, bestMove, agent);
        if(board.cellCheckPlayerWin(opponentMove, agent)){
            lastMove = bestMove;
            return bestMove;
        }
        if(board.cellIsFull(opponentMove)){
            lastMove = bestMove;
            return bestMove;
        }

        /* cellGetTwo returns the win moves of the cell  */ 
        if(board.cellGetTwo(bestMove, opponent).size()>0){
            int substituteMove = findSubstituteMove(opponentMove);
            board.undoSetVal(opponentMove, bestMove);
            board.setVal(opponentMove, substituteMove, agent);
            lastMove = substituteMove;

            return substituteMove;
        }
        lastMove = bestMove;
        return bestMove;
    }
    /* deals when agent find the move which is illegal or could make opponent win,
        then call this function to find a substitute move which has the highest
        heuristic value.  */
    public int findSubstituteMove(int opponentMove){ 
        ArrayList<Integer> substituteMoves = board.canMove(opponentMove);
        ArrayList<Integer> heuristics = new ArrayList<Integer>();
        int maxHeuristic = Integer.MIN_VALUE;
        int maxMove = 0;
        for(int i = 0; i<substituteMoves.size(); i++){
            if(cellHeuristic(substituteMoves.get(i)) > maxHeuristic){
                maxHeuristic = cellHeuristic(substituteMoves.get(i));
                maxMove = (Integer)substituteMoves.get(i);
            }
            heuristics.add(cellHeuristic(substituteMoves.get(i)));
        } 
        
        return maxMove;
    }

    public int getBestMove(int opponentMove){

    /* Rule 1: IF can win, then choose the win move  */

        ArrayList<Integer> moves = board.cellGetTwo(opponentMove, agent);
        if (moves.size() > 0){
            Integer winMove = moves.get(0);
            return winMove;
        }
        
        
        

    /* Rule 2: IF opponent has two connected, and the block position don't cause
        opponent win, then block */
        ArrayList<Integer> opponentMoves = board.cellGetTwo(opponentMove, opponent);
        if (opponentMoves.size() > 0){
            for (Integer oppoMove: opponentMoves){
                if (board.cellGetTwo(oppoMove, opponent).size() == 0){
                    Integer blockMove = oppoMove;
                    return blockMove;
                }
            }
        }
    
    
    /* Rule 3: IF Agent can play on the central of the cell, then move to 5 */
    /* This might be a prior knowledge since central of the cell have more winning chance*/
        ArrayList<Integer> cellFiveMoves = board.cellGetTwo(5, opponent);
        if (cellFiveMoves.size() == 0){
            ArrayList<Integer> canMoves = board.canMove(opponentMove);
            if (canMoves.contains(5)){
                Integer moveFive = 5;
                return moveFive;
            }
        }
        
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        boolean [][] killerMove = getNewKillerMove();

        int[] score = alphaBeta(board, opponentMove, agent, alpha, beta, 8, killerMove, lastMove);

        return score[1];

    }

    /** 
    * since we consider game board in the same level are similar 
    * This function helps to generate a new killermove 2d array.
    * @return boolean[][]
    */
    public boolean[][] getNewKillerMove(){
        boolean [][] killerMove = new boolean[10][10];
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                killerMove[i][j] = false;
            }
        }
        return killerMove;
    }

    public int cellHeuristic(int cell){
        int sumHeuristic = 0;
        /**
        * winCell heuristic is that if agent choose this move and lead to this cell,
        * the remain positions in this cell will lead agent to win. If lots of positions
        * in this cell when opponent choose to move, the heuristic could become very high.
        */

        ArrayList<Integer> winCell = new ArrayList<Integer>();
        for(int i=1; i<10;i++){
            if(board.cellGetTwo(i, agent).size() > 0){
                winCell.add(i);
            }
        }
        for(Integer move: board.canMove(cell)){
            if (winCell.contains(move)){
                sumHeuristic += 5;
            }
        }
        /**
        * oppoTwo heuristic is that if agent take a move that goes to this cell
        * and in this cell, the opponent already have two connected pieces. That
        * means this move is a bad move. Thus, the heuristic value should decrease
        */
        ArrayList<Integer> oppoTwo = board.cellGetTwo(cell, opponent);
        if (oppoTwo.size() >0){
            sumHeuristic -= 15;
        }

        /**
        * winLine heuristic is that if the opponent has many ways to win,
        * that is through rows, columns or diagonals. The more win ways the
        * opponent have, the heuristic value would decrease more. 
        * This heuristic aim is to let agent to block the opponent in the
        * early and middle game stage.
        */
        sumHeuristic -= board.winLine(cell, opponent) * 1;
        sumHeuristic += board.winLine(cell, agent) * 1;

        /*
        * diversityMoves heuristic is try to make agent win different positions
        * in different cells. During the agent play games with lookt, we observed
        * that the lookt usually have different winning positions in different cells,
        * for example, x is the opponent, in cell 1, the opponent win position 8,
        * and in cell 2, it wins position 1
        * cell 1:        cell 2:
        *    . x o          . x x
        *    . x .          . . o
        *    . . .          . . .
        * Therefore, this may be a pattern that leads to win.
        */
        ArrayList<Integer> diversityMoves = new ArrayList<Integer>();
        for(int i=1; i<10;i++){
            if(cell != i){
                for(Integer winMove: board.cellGetTwo(i, agent)){
                    diversityMoves.add(winMove);
                }
            }
        }
        for(Integer winMove: board.cellGetTwo(cell, agent)){
            if(!diversityMoves.contains(winMove)){
                sumHeuristic += 2;
            }
        }

        /*
        * Except the center of the cell, usually the corner moves have
        * more ways (chances) to win.
        * This cornerMove heuristic is to guide the agent to choose corner
        * move which may have more winning chance at the end.
        */
        int[] cornerMove = {1,3,7,9};
        for(int corner: cornerMove){
            if(board.getPositionPlayer(cell, corner) == agent){
                sumHeuristic += 2;
            }
        }

        /*
        * This is the basic move heuristic, that is to guide
        * agent to move.
        */
        int o1 = board.evaluateHelper(1, cell, agent);
        int x1 = board.evaluateHelper(1, cell, opponent);
        sumHeuristic += 1 * o1;
        sumHeuristic -= 1 * x1;

        return sumHeuristic;
    }

    /**
     * 
     * @return heuristic value of the whole 9x9 board
     */
    public int boardHeuristic(){
        int sumHeuristic = 0;
        for(int cell=1; cell<10; cell++){
            sumHeuristic += cellHeuristic(cell);
        }

        return sumHeuristic;
    }

    /**
     * Alpha-Beta pruning algorithm, using recurrsion to implement and use killer heuristic, to sotre
     * killer move in the same level and try to search killer move first in order to speed up.
     * @param board 3d array, the game board
     * @param cell [target cell] int between 1-9 from left to the right and from top to the bottom
     * @param player current player
     * @param alpha
     * @param beta
     * @param level
     * @param killerMove since we consider game nodes in the same level have the similar situation
     *        Due to the array is passed by reference.
     * @param from [move cell] int between 1-9  move cell->move->target cell
     * @return the score of the sum heuristic value of from cell and target cell & index of move
     */
    public int[] alphaBeta(AgentBoard board, int cell, char player, int alpha, int beta, int level, boolean [][] killerMove, int from){

        int move = 0;
        /**
         * this cell lead to a final state, returns
         */
        if(board.cellCheckPlayerWin(cell, agent)){
            return new int[] {60, cell};
        }else if(board.cellCheckPlayerWin(cell, opponent)){
            return new int[] {-60, cell};
        }else if(board.cellIsFull(cell)){
            return new int[] {0, cell}; 
        }

        /**
         * IF search level is 0, returns the sum heuristic value of
         * the from cell and the target cell. We want to maximise the
         * move outcome, it should maximise both current cell(from cell)
         * and the target cell.
         */
        if (level == 0 || board.cellIsFull(cell)){
            return new int[] {cellHeuristic(cell) + cellHeuristic(from), cell};
        }

        /**
         * store killer move, make sure it has the same
         * killer move in the same level.
         */
        boolean[][] newKillerMove = getNewKillerMove();
        if (player == opponent){
            ArrayList locations = board.canMove(cell);
            int swapLoc = 0;
            for(int i=0; i<locations.size();i++){
                /**
                 * change the canMove arraylist to make sure traverse killer move first
                 */
                if(killerMove[(Integer)locations.get(i)][cell] == true){
                    Collections.swap(locations, swapLoc, i);
                    swapLoc += 1;
                }
            }

            for(int i=0; i<locations.size(); i++){
                int nextMove = (Integer)locations.get(i);
                board.setVal(cell,nextMove, opponent);
                int score = alphaBeta(board, nextMove, agent, alpha, beta, level-1, newKillerMove, cell)[0];
                board.undoSetVal(cell, nextMove);
                if(score < beta){
                    move = (Integer)locations.get(i);
                    beta = score;
                    /**
                     * pruning and store killer move
                     */
                    if (alpha >= beta){
                        killerMove[nextMove][cell] = true;                        
                        break;
                    }
                } 
            }
            return new int[] {beta, move};
        }else{
            ArrayList locations = board.canMove(cell);
            int swapLoc = 0;
            for(int i=0; i<locations.size();i++){
                if(killerMove[(Integer)locations.get(i)][cell] == true){
                    Collections.swap(locations, swapLoc, i);
                    swapLoc += 1;
                }
            }

            for(int i=0; i<locations.size(); i++){
                int nextMove = (Integer)locations.get(i);
                board.setVal(cell, nextMove, agent);
                int score = alphaBeta(board, nextMove, opponent, alpha, beta, level-1, newKillerMove, cell)[0];
                board.undoSetVal(cell, nextMove);

                if(score > alpha){
                    alpha = score;
                    move = nextMove;
                    if (alpha >= beta){
                        killerMove[nextMove][cell] = true;
                        break;
                    }
                } 
                
            }

            return new int[] {alpha, move};
        }
    }

    /**
     * @return the String of the winner
     */
    public String checkWinner() {
        if (board.checkPlayerWin(agent)){
            return "agent";
        }else if(board.checkPlayerWin(opponent)){
            return "opponent";
        }
        return "draw";
    }
}