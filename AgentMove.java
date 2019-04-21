/**
 * AgentMove
 */
public class AgentMove {
        /* current player */
    private char player = 'o';

    /* default agent, modify at the beginning */
    private char agent = 'o';
    private char opponent = 'x';
    private AgentBoard bd;

    public AgentMove(char agent_char){
        if (agent_char == 'x'){
            player = 'x';
            agent = 'x';
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
        if(bd.cell_check_player_win(cell, agent)){
            return 100;
        }else if(bd.cell_check_player_win(cell, opponent)){
            return -100;
        }else if (bd.cell_is_full(cell)){
            return 0;
        }

        if (player == opponent){
            System.out.println(bd.can_move(cell));
            locations = bd.can_move(cell);
            for(int i=0; i<locations.size(); i++){
                System.out.println(locations[i]);
            }
        }else{
            System.out.println(bd.can_move(cell));

        }
        return 0;

    }

    public static void main(String[] args) {
        System.out.println("agent move!");
        AgentMove move = new AgentMove('o');
        int alpha = -999999;
        int beta = 999999;
        int cell = 5;
        move.alpha_beta(move.bd, cell, move.player, alpha, beta);
    }

}