/**
 * AgentHeuristic
 * This file displays the heuristic value for agent
 * 
 *    Eval(s) = 3X2(s) + X1(s) - (3O2(s) + O1(s))   
 * 
 * To calculate heuristic value us
 * 
 */


public class AgentHeuristic {
 // TODO

    public static void main(String[] args) {
        AgentBoard agent_board = new AgentBoard();
        agent_board.set_val(5, 3, 'o');
        agent_board.set_val(3, 9, 'o');
        agent_board.set_val(7, 1, 'o');
        agent_board.set_val(7, 5, 'o');
        
        agent_board.display_board();
        System.out.println(agent_board.check_player_win('o'));
        System.out.println(agent_board.game_over());
    }
    
}