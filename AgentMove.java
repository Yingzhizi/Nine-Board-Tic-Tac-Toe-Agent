import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * AgentMove
 */
public class AgentMove {
        /* current player */
    private char player = 'o';
    private int count = 0;
    /* default agent, modify at the beginning */
    private char agent = 'o';
    private char opponent = 'x';
    private int lastMove = 0;
    
    /* store alpha value that have been searched */
    private int search_alpha = Integer.MIN_VALUE;

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
        board.displayBoard();

        return bestMove;
    }

    public int playThirdMove(int first, int second, int third){
        setAgent('x');
        board.setVal(first, second, agent);
        board.setVal(second, third, opponent);
        int bestMove = getBestMove(third);
        lastMove = bestMove;

        return bestMove;
    }

    public int playNextMove(int opponentMove){
        count += 1;
        board.setVal(lastMove, opponentMove, opponent);
        int bestMove = getBestMove(opponentMove);
        if (!board.isLegal(opponentMove, bestMove)){
            int substituteMove = findSubstituteMove(opponentMove);
            board.setVal(opponentMove, substituteMove, agent);
            lastMove = substituteMove;
            System.out.println("Total count: " + count);
            board.displayBoard();
            return substituteMove;
        }
        board.setVal(opponentMove, bestMove, agent);
        if(board.cellCheckPlayerWin(opponentMove, agent)){
            lastMove = bestMove;
            return bestMove;
        }
        if(board.cellIsFull(opponentMove)){
            lastMove = bestMove;
            return bestMove;
        }
        if(board.CellGetTwo(bestMove, opponent).size()>0){
            int substituteMove = findSubstituteMove(opponentMove);
            board.undoSetVal(opponentMove, bestMove);
            System.out.println("Substitute move and board");
            board.displayBoard();
            board.setVal(opponentMove, substituteMove, agent);
            lastMove = substituteMove;
            System.out.println("Total count: " + count);
            board.displayBoard();

            return substituteMove;
        }
        lastMove = bestMove;
        board.displayBoard();
        System.out.println("Total count: " + count);
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
        printArray("heuristic: ", substituteMoves);
        printArray("values: ", heuristics);
        
        return maxMove;
    }

    public int getBestMove(int opponentMove){

    /* Rule 1: IF can win, then choose the win move  */

        ArrayList<Integer> moves = board.CellGetTwo(opponentMove, agent);
        printArray("moves:", moves);
        if (moves.size() > 0){
            Integer winMove = moves.get(0);
            System.out.println("from winMoves");
            return winMove;
        }
        
        
        

    /* Rule 2: IF opponent has two connected, and the block position don't cause
        opponent win, then block */
        ArrayList<Integer> opponentMoves = board.CellGetTwo(opponentMove, opponent);
        printArray("opponentMoves:", opponentMoves);
        if (opponentMoves.size() > 0){
            for (Integer oppoMove: opponentMoves){
                if (board.CellGetTwo(oppoMove, opponent).size() == 0){
                    Integer blockMove = oppoMove;
                    System.out.println("from blockMoves");
                    return blockMove;
                }
            }
        }
    
    
    /* Rule 3: IF Agent can play on the central of the cell, then move to 5 */
        ArrayList<Integer> cellFiveMoves = board.CellGetTwo(5, opponent);
        printArray("cellFiveMoves:", cellFiveMoves);
        if (cellFiveMoves.size() == 0){
            ArrayList<Integer> canMoves = board.canMove(opponentMove);
            if (canMoves.contains(5)){
                Integer moveFive = 5;
                // board.setVal(opponentMove, moveFive, agent);
                // lastMove = moveFive;
                System.out.println("from cellFiveMoves");
                return moveFive;
            }
        }
        
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        boolean [][] killerMove = getNewKillerMove();

        int[] score = alphaBeta(board, opponentMove, agent, alpha, beta, 8, killerMove);

        return score[1];

    }

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
        /* 
        * winCell heuristic is that if agent choose this move and lead to this cell,
        * the remain positions in this cell will lead agent to win. If lots of positions
        * in this cell when opponent choose to move, the heuristic could become very high.
        */

        ArrayList<Integer> winCell = new ArrayList<Integer>();
        for(int i=1; i<10;i++){
            if(board.CellGetTwo(i, agent).size() > 0){
                winCell.add(i);
            }
        }
        for(Integer move: board.canMove(cell)){
            if (winCell.contains(move)){
                sumHeuristic += 5;
            }
        }
        /* 
        * oppoTwo heuristic is that if agent take a move that goes to this cell
        * and in this cell, the opponent already have two connected pieces. That
        * means this move is a bad move. Thus, the heuristic value should decrease
        */
        ArrayList<Integer> oppoTwo = board.CellGetTwo(cell, opponent);
        if (oppoTwo.size() >0){
            sumHeuristic -= 10;
        }

        /*
        * winLine heuristic is that if the opponent has many ways to win,
        * that is through rows, columns or diagonals. The more win ways the
        * opponent have, the heuristic value would decrease more. 
        * This heuristic aim is to let agent to block the opponent in the
        * early and middle game stage.
        */
        sumHeuristic -= board.winLine(cell, opponent) * 1;
        sumHeuristic += board.winLine(cell, agent) * 1;

        /*
        * 
        *
        *
        *
        */
        // Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        // for(Integer move: board.getPositionsCell(cell, agent)){
        //     if (map.get(move) != null){
        //         map.put(move, map.get(move)+1);
        //     }else{
        //         map.put(move, 0);
        //     }
        // }
        // for(Integer key:map.keySet()){
        //     if(key == cell){
        //         sumHeuristic += map.get(key);
        //     }
        // }

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

    public int cellChainHeuristic(int cell){
        int chainHeuristic = 0;
        ArrayList<Integer> canMoves = board.canMove(cell);
        for(Integer move: canMoves){
            chainHeuristic += cellHeuristic(move);
            for(Integer chainMove: board.canMove(move)){
                chainHeuristic += cellHeuristic(chainMove);
            }
        }
        return chainHeuristic;
    }

    public int boardHeuristic(){
        int sumHeuristic = 0;
        for(int cell=1; cell<10; cell++){
            sumHeuristic += cellHeuristic(cell);
        }

        return sumHeuristic;
    }

    public ArrayList<Integer> getTwo(int cell, char player){
        ArrayList<Integer> moves = board.CellGetTwo(cell, player);



        return moves;
    }

    /* alpha-beta pruning  */
    public int[] alphaBeta(AgentBoard board, int cell, char player, int alpha, int beta, int level, boolean [][] killerMove){

        int move = 0;

        if(board.cellCheckPlayerWin(cell, agent)){
            return new int[] {25, cell};
        }else if(board.cellCheckPlayerWin(cell, opponent)){
            return new int[] {-25, cell};
        }else if(board.cellIsFull(cell)){
            return new int[] {0, cell}; 
        }


        if (level == 0 || board.cellIsFull(cell)){
            // return new int[] {boardHeuristic(), cell};
            return new int[] {cellHeuristic(cell), cell};
            // return new int[] {cellChainHeuristic(cell), cell};
        }

        boolean[][] newKillerMove = getNewKillerMove();
        if (player == opponent){
            ArrayList locations = board.canMove(cell);
            int swapLoc = 0;
            for(int i=0; i<locations.size();i++){
                if(killerMove[(Integer)locations.get(i)][cell] == true){
                    Collections.swap(locations, swapLoc, i);
                    swapLoc += 1;
                }
            }

            for(int i=0; i<locations.size(); i++){
                // board.displayBoard();
                // System.out.println();  
                int nextMove = (Integer)locations.get(i);
                board.setVal(cell,nextMove, opponent);
                // moveBd.displayBoard();
                // int score = alphaBeta(board, nextMove, agent, alpha, beta, level-1, killerMove)[0] - cellHeuristic((Integer)locations.get(i));
                int score = alphaBeta(board, nextMove, agent, alpha, beta, level-1, newKillerMove)[0];
                board.undoSetVal(cell, nextMove);
                if(score < beta){
                    move = (Integer)locations.get(i);
                    beta = score;
                    if (alpha >= beta){
                        // return new int[] {alpha, move};
                        killerMove[nextMove][cell] = true;                        
                        break;
                    }
                } 
            }
            // System.out.println("player: " + player + " cell: "+ cell+" alpha: "+ alpha + " beta: "+beta+" level: "+ level);
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
                // board.displayBoard();
                // System.out.println();
                int nextMove = (Integer)locations.get(i);
                board.setVal(cell, nextMove, agent);
                // board.displayBoard();
                // int score = alphaBeta(board, nextMove, opponent, alpha, beta, level-1, killerMove)[0]+ cellHeuristic((Integer) locations.get(i));
                int score = alphaBeta(board, nextMove, opponent, alpha, beta, level-1, newKillerMove)[0];
                board.undoSetVal(cell, nextMove);

                if(score > alpha){
                    alpha = score;
                    move = nextMove;
                    if (alpha >= beta){
                        // return new int[] {beta, move};
                        killerMove[nextMove][cell] = true;
                        break;
                    }
                } 
                
            }
            ArrayList<Integer> c = new ArrayList<Integer>();
            for (int i = 0; i<9; i++){
                c.add(cellHeuristic(i+1));
            }
            // printArray("cell heuristics:", c);
            // System.out.println("player: " + player + " cell: "+ cell+" alpha: "+ alpha + " beta: "+beta+" level: "+ level);
            // printKillerMove("KillerMove:", killerMove);
            return new int[] {alpha, move};
        }
    }


    public String checkWinner() {
        if (board.checkPlayerWin(agent)){
            return "agent";
        }else if(board.checkPlayerWin(opponent)){
            return "opponent";
        }

        return "draw";
    }


    public static void main(String[] args) {
        System.out.println("agent move!");
        AgentMove mv = AgentMove.getAgent();
        
        mv.setVal(5, 5, 'o');
        mv.setVal(5, 9, 'o');
        mv.displayBoard();
        mv.printArray("move:", mv.board.CellGetTwo(5, 'o'));
    }

}
