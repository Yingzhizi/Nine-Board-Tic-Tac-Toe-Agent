import java.util.ArrayList;

/**
 * AgentMove
 */
public class AgentMove {
        /* current player */
    private char player = 'o';

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

    public int playNextMove(int opponentMove){
        board.setVal(lastMove, opponentMove, opponent);
        int bestMove = getBestMove(opponentMove);
        board.setVal(opponentMove, bestMove, agent);
        lastMove = bestMove;

        return bestMove;
    }


    public int getBestMove(int opponentMove){

    /* Rule 1: IF can win, then choose the win move  */
        ArrayList<Integer> moves = board.CellGetTwo(opponentMove, agent);
        if (moves.size() > 0){
            Integer winMove = moves.get(0);
            board.setVal(opponentMove, winMove, agent);
            lastMove = winMove;
            return winMove;
        }

    /* Rule 2: IF opponent has two connected, and the block position don't cause
        opponent win, then block */
        ArrayList<Integer> opponentMoves = board.CellGetTwo(opponentMove, opponent);
        if (opponentMoves.size() > 0){
            for (Integer oppoMove: opponentMoves){
                if (board.CellGetTwo(oppoMove, opponent).size() == 0){
                    Integer blockMove = oppoMove;
                    board.setVal(opponentMove, blockMove, agent);
                    lastMove = blockMove;
                    return blockMove;
                }
            }
        }
    
    /* Rule 3: IF Agent can play on the central of the cell, then move to 5 */
        ArrayList<Integer> cellFiveMoves = board.CellGetTwo(5, opponent);
        if (opponentMoves.size() == 0){
            Integer moveFive = 5;
            board.setVal(opponentMove, moveFive, agent);
            lastMove = moveFive;
            return moveFive;
        }
        
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int[] score = alphaBeta(board, opponentMove, agent, alpha, beta, 9);

        return score[1];

    }


    public int cellEvaluation(int cell, char player){
        return 0;
    }

    public ArrayList<Integer> getTwo(int cell, char player){
        ArrayList<Integer> moves = board.CellGetTwo(cell, player);



        return moves;
    }

    /* alpha-beta pruning  */
    public int[] alphaBeta(AgentBoard board, int cell, char player, int alpha, int beta, int level){

        int move = 0;

        if (board.cellCheckPlayerWin(cell ,agent)){
            return new int[] {100, cell};
        }else if(board.cellCheckPlayerWin(cell, opponent)){
            return new int[] {-100, cell};
        }else if(board.cellIsFull(cell)){
            return new int[] {0, cell}; 
        }

        if (level == 0 || board.isFull()){
            return new int[] {0, move};
        }

        // board.displayBoard();
        // System.out.println();

        if (player == opponent){
            ArrayList locations = board.canMove(cell);
            for(int i=0; i<locations.size(); i++){
                // board.displayBoard();
                // System.out.println();  
                board.setVal(cell, (Integer)locations.get(i), opponent);
                // moveBd.displayBoard();
                int score = alphaBeta(board, (Integer)locations.get(i), agent, alpha, beta, level-1)[0];
                board.undoSetVal(cell, (Integer)locations.get(i));
                if(score < beta){
                    move = (Integer)locations.get(i);
                    beta = score;
                    if (alpha >= beta){
                        return new int[] {alpha, move};
                    }
                } 
            }
            return new int[] {beta, move};
        }else{
            ArrayList locations = board.canMove(cell);
            for(int i=0; i<locations.size(); i++){
                // board.displayBoard();
                // System.out.println();
            
                
                board.setVal(cell, (Integer)locations.get(i), agent);
                // board.displayBoard();
                int score = alphaBeta(board, (Integer)locations.get(i), opponent, alpha, beta, level-1)[0];
                board.undoSetVal(cell, (Integer)locations.get(i));

                if(score < beta){
                    alpha = score;
                    move = (Integer)locations.get(i);
                    if (alpha >= beta){
                        return new int[] {beta, move};
                    }
                } 
                
            }
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
        AgentMove move = new AgentMove();
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int cell = 5;
        int[] scoreMove = move.alphaBeta(move.board, cell, 'o', alpha, beta, 4);
        System.out.println(scoreMove[0]);
        System.out.println(scoreMove[1]);
        move.board.displayBoard();
    }

}
