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
    private static AgentMove singleton_agent = new AgentMove();
    private AgentMove(){}
    public static AgentMove get_agent(){
        return singleton_agent;
    }

    /*  use at the beginning of game, 
    after receive second_move or third_move  */
    public void set_agent(char x){
        agent = (x == 'o') ? 'o' : 'x';
        opponent = (x == 'o') ? 'x': 'o';
    }

    /* switch player for alpha-beta's min-max node */
    public char switch_player(){
        player = (player == 'x') ? 'o': 'x';
        return player;
    }

    /* main function for agent to play the game 
    *  including:
    *       decesion making, return integer, winner juding
    */
    public int play_game(AgentBoard bd, int first_move){
        
        return 0;
    }

    /* alpha-beta pruning  */
    public int alpha_beta(AgentBoard bd, int cell, char player, int alpha, int beta){
        if (bd.unitState != GameState.GameOver){
            if(bd.cell_check_player_win(cell, agent)){
                return 10;
            }else if(bd.cell_check_player_win(cell, opponent)){
                return -10;
            }else if (bd.cell_is_full(cell)){
                return 0;
            }
        }

        bd.display_board();
        System.out.println();

        if (player == opponent){
            ArrayList locations = bd.can_move(cell);
            for(int i=0; i<locations.size(); i++){
                // bd.display_board();
                // System.out.println();  
                bd.set_val(cell, (Integer)locations.get(i), opponent);
                // move_bd.display_board();
                int score = alpha_beta(bd, (Integer)locations.get(i), agent, alpha, beta);
                bd.undo_set_val(cell, (Integer)locations.get(i));
                if(score < beta){
                    beta = score;
                    if (alpha >= beta){
                        return alpha;
                    }
                } 
            }
            return beta;
        }else{
            ArrayList locations = bd.can_move(cell);
            for(int i=0; i<locations.size(); i++){
                bd.display_board();
                // System.out.println();
            
                
                bd.set_val(cell, (Integer)locations.get(i), agent);
                // bd.display_board();
                int score = alpha_beta(bd, (Integer)locations.get(i), opponent, alpha, beta);
                bd.undo_set_val(cell, (Integer)locations.get(i));

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
        int alpha = -999999;
        int beta = 999999;
        int cell = 5;
        System.out.println(move.alpha_beta(move.bd, cell, 'o', alpha, beta));
    }

}