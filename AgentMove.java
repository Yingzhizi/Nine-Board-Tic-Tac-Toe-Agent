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
    public int playGame(AgentBoard bd, int firstMove){
        
        return 0;
    }

    /* alpha-beta pruning  */
    public int alphaBeta(AgentBoard bd, int cell, char player, int alpha, int beta){
        if (bd.unitState != GameState.GameOver){
            if(bd.cellCheckPlayerWin(cell, agent)){
                return 10;
            }else if(bd.cellCheckPlayerWin(cell, opponent)){
                return -10;
            }else if (bd.cellIsFull(cell)){
                return 0;
            }
        }

        bd.displayBoard();
        System.out.println();

        if (player == opponent){
            ArrayList locations = bd.canMove(cell);
            for(int i=0; i<locations.size(); i++){
                // bd.displayBoard();
                // System.out.println();  
                bd.setVal(cell, (Integer)locations.get(i), opponent);
                // moveBd.displayBoard();
                int score = alphaBeta(bd, (Integer)locations.get(i), agent, alpha, beta);
                bd.undoSetVal(cell, (Integer)locations.get(i));
                if(score < beta){
                    beta = score;
                    if (alpha >= beta){
                        return alpha;
                    }
                } 
            }
            return beta;
        }else{
            ArrayList locations = bd.canMove(cell);
            for(int i=0; i<locations.size(); i++){
                // bd.displayBoard();
                // System.out.println();
            
                
                bd.setVal(cell, (Integer)locations.get(i), agent);
                // bd.displayBoard();
                int score = alphaBeta(bd, (Integer)locations.get(i), opponent, alpha, beta);
                bd.undoSetVal(cell, (Integer)locations.get(i));

                if(score < beta){
                    alpha = score;
                    if (alpha >= beta){
                        return beta;
                    }
                } 
                
            }
            return alpha;
        }
        // System.out.println();

    }

    public static void main(String[] args) {
        System.out.println("agent move!");
        AgentMove move = new AgentMove();
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int cell = 5;
        System.out.println(move.alphaBeta(move.bd, cell, 'o', alpha, beta));
    }

}
