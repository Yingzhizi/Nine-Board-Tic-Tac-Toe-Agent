import java.util.ArrayList;
/**
 * AgentMove
 */
public class AgentMove {
        /* current player */
    private char player = 'o';

    /* default agent, modify at the beginning */
    private char agents = 'o';
    private char opponent = 'x';
    private AgentBoard bd;

    public AgentMove(char agent_char){
        if (agent_char == 'x'){
            player = 'x';
            agents = 'x';
            opponent = 'o';
        }
        bd = new AgentBoard();
    }

    public char switch_player(){
        if (player == 'x'){
            player = 'o';
            return 'o';
        }else{
            player = 'x';
            return 'x';
        }
    }

    public int alpha_beta(AgentBoard bd, int cell, char player, int alpha, int beta){
        if(bd.cell_check_player_win(cell, agents)){
            return 10;
        }else if(bd.cell_check_player_win(cell, opponent)){
            return -10;
        }else if (bd.cell_is_full(cell)){
            return 0;
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
                int score = alpha_beta(bd, (Integer)locations.get(i), agents, alpha, beta);
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
            
                
                bd.set_val(cell, (Integer)locations.get(i), agents);
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
        AgentMove move = new AgentMove('o');
        int alpha = -999999;
        int beta = 999999;
        int cell = 5;
        System.out.println(move.alpha_beta(move.bd, cell, 'o', alpha, beta));
    }

}