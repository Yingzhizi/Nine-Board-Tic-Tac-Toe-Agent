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
    private AgentBoard bd = new AgentBoard();
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
        bd.setVal(cell, firstMove, 'x');
        setAgent('o');
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int[] scoreMove = alphaBeta(bd, firstMove, 'o', alpha, beta, 9);
        bd.setVal(firstMove, scoreMove[1], agent);
        search_alpha = scoreMove[0];
        lastMove = scoreMove[1];

        return scoreMove[1];
    }

    public int playNextMove(int opponentMove){
        bd.setVal(lastMove, opponentMove, opponent);
        int beta = Integer.MAX_VALUE;
        int[] scoreMove = alphaBeta(bd, opponentMove, 'o', search_alpha, beta, 9);
        bd.setVal(opponentMove, scoreMove[1], agent);
        search_alpha = scoreMove[0];
        lastMove = scoreMove[1];

        return scoreMove[1];
    }

    /* alpha-beta pruning  */
    public int[] alphaBeta(AgentBoard bd, int cell, char player, int alpha, int beta, int level){

        int move = 0;

        if (bd.unitState != GameState.GameOver){
            if(bd.cellCheckPlayerWin(cell, agent)){
                bd.unitState = GameState.GameOver;
                return new int[] {10, move};
            }else if(bd.cellCheckPlayerWin(cell, opponent)){
                bd.unitState = GameState.GameOver;
                return new int[] {-10, move};
            }else if (bd.cellIsFull(cell)){
                bd.unitState = GameState.GameOver;
                return new int[] {0, move};
            }
        }

        if (level == 0 || bd.isFull()){
            return new int[] {0, move};
        }

        // bd.displayBoard();
        // System.out.println();

        if (player == opponent){
            ArrayList locations = bd.canMove(cell);
            for(int i=0; i<locations.size(); i++){
                // bd.displayBoard();
                // System.out.println();  
                bd.setVal(cell, (Integer)locations.get(i), opponent);
                // moveBd.displayBoard();
                int score = alphaBeta(bd, (Integer)locations.get(i), agent, alpha, beta, level-1)[0];
                bd.undoSetVal(cell, (Integer)locations.get(i));
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
            ArrayList locations = bd.canMove(cell);
            for(int i=0; i<locations.size(); i++){
                // bd.displayBoard();
                // System.out.println();
            
                
                bd.setVal(cell, (Integer)locations.get(i), agent);
                // bd.displayBoard();
                int score = alphaBeta(bd, (Integer)locations.get(i), opponent, alpha, beta, level-1)[0];
                bd.undoSetVal(cell, (Integer)locations.get(i));

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

    public static void main(String[] args) {
        System.out.println("agent move!");
        AgentMove move = new AgentMove();
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int cell = 5;
        int[] scoreMove = move.alphaBeta(move.bd, cell, 'o', alpha, beta, 4);
        System.out.println(scoreMove[0]);
        System.out.println(scoreMove[1]);
        move.bd.displayBoard();
    }

}
