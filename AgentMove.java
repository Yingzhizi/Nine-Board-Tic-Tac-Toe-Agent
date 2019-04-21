/**
 * AgentMove
 */
public class AgentMove {
        /* current player */
    private char player = 'o';

    /* default agent, modify at the beginning */
    private char agent = 'o';
    private char opponent = 'x';

    public AgentMove(char agent_char){
        if (agent_char == 'x'){
            player = 'x';
            agent = 'x';
            opponent = 'o';
        }
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

//    public int alpha_beta(char player, int alpha, int beta){
//        if(check_player_win(agent)){
//            return 100;
//        }else if(check_player_win(opponent)){
//            return -100;
//        }else if (is_full()){
//            return 0;
//        }
//
//        if (player == opponent){
//
//        }
//
//
//    }

    public static void main(String[] args) {
        
    }

}