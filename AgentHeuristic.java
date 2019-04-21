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
    //TODO

    public static void main(String[] args) {
        AgentBoard agentBoard = new AgentBoard();
        agentBoard.setVal(5, 3, 'o');
        agentBoard.setVal(3, 9, 'o');
        agentBoard.setVal(7, 1, 'o');
        agentBoard.setVal(7, 5, 'o');
        
        agentBoard.displayBoard();
        System.out.println(agentBoard.checkPlayerWin('o'));
        System.out.println(agentBoard.gameOver());
    }
    
}
