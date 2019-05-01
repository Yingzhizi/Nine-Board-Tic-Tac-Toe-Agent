/*******************************************************************************
*  Agent.java & AgentMove.java & AgentBoard.java
*  Written for COMP3411/9414/9814 Artificial Intelligence
*  Authors: Yingzhi Zhou(z5125679)  Qian Chen(z5222755)
*  How the agent works: 
*
*  Firstly, we created a three-dimensional array to store the 9x9 
*  in the class AgentBoard, and we divided the board into 9 sub-boards.
*  The sub-board, also named cell by us, has index 1-9 in board[0-8][i][j]
*  and i is the index for row, j for column. We gave every position a 
*  initial value '.', and we manipulate the board by directly set the array
*  value. Moreover, we created a singleton agent which is the instance
*  of class AgentMove, it has a 9x9 board as member variable, and every
*  move is recorded in this board.
*  
*  Basically, the agent's reaction towards the opponent move is to get the
*  move through searching the game tree by alpha-beta pruning algorithm, we
*  use recursion to implement this algorithm and tried to implement killer
*  heuristic. In order to speed up, we search and store killer moves using
*  a two-dimensional array for every level since the game situation is similar
*  when it has the same level. Moreover, we set a few rules to get move before
*  alpha-beta searching. For example, if the agent have a winning more, then
*  move it and if the opponent has a winning move and this move wouldn't lead
*  to an opponent winning board, then this move may be blocked by the agent.
*  After alpha-beta searching, if the move is illegal or lead to the opponent
*  win, the agent will find a substitute move by evaluate the move outcome both
*  in current cell and the target cell. Thus, our heuristic is the sum of the 
*  current cell(sub-board)'s heuristic value and the target cell's heuristic 
*  and find the highest heuristic value. Furthermore, during alpha-beta pruning, 
*  if the search level equals to 0, we use heuristic function to evaluate the board. 
*  We invented heuristic function and adjust heuristic parameters by analysing the 
*  move outcome and observing the move the lookt made through the final board when 
*  Agent playing against the lookt. The heuristic function is a combination of 
*  different heuristic functions. e.g. when in the cell opponent has two connected, 
*  then we punish the move to that cell and decrease heuristic value and if the remain
*  positions of the cell which the move went to could lead the opponent to win,
*  then we award the move and increase the heuristic value. Moreover, we observed
*  that the winning positions diversity which means winning positions are different 
*  between cells may be a good heuristic through using the agent play with lookt.
************************************************************************************/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Agent {

    // save the cell we need to locate
    static AgentMove agentMove;
    static char player;
    static char opponent;

    public Agent() {}

    public static void main(String args[]) throws IOException {

    Agent newPlayer = new Agent();

	if(args.length < 2) {
	    System.out.println("Usage: java Agent -p (port)");
	    return;
	}
		
	final String host = "localhost";
	final int portNumber = Integer.parseInt(args[1]);

	Socket socket = new Socket(host, portNumber);
	BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

	String line;

	while (true) {
        // start listen from server
	    line = br.readLine();

	    int move = newPlayer.parse(line);
	    if(move == -1) {
		socket.close();
		return;
	    }else if(move == 0){
		//TODO
	    }else {
        out.println(move);
	    }

	}
    }

    public static int parse(String line) {
        if(line.contains("init")) {
            // initialize the Agent
            agentMove = AgentMove.getAgent();

        }else if(line.contains("start")) {
            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");

            String list = line.substring(argsStart+1, argsEnd);
            char type = list.charAt(0);
            agentMove.setAgent(type);
            // System.out.println("Player is " + type);
            

        }else if(line.contains("second_move")) {
            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");

            String list = line.substring(argsStart+1, argsEnd);
            String[] numbers = list.split(",");

            // get best move
            int bestMove = agentMove.playSecondMove(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
            
            return bestMove;

        }else if(line.contains("third_move")) {
            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");

            String list = line.substring(argsStart+1, argsEnd);
            String[] numbers = list.split(",");

            int bestMove = agentMove.playThirdMove(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]), Integer.parseInt(numbers[2]));
            return bestMove;

        }else if(line.contains("next_move")) {

            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");
            String list = line.substring(argsStart+1, argsEnd);

            int opponentMove =  Integer.parseInt(list);

            // get the best move
            int bestMove = agentMove.playNextMove(opponentMove);
            
            return bestMove;

        }else if(line.contains("last_move")) {
            //TODO
        }else if(line.contains("win")) {
            //TODO
        }else if(line.contains("loss")) {
            //TODO
        }else if(line.contains("end")) {

            return -1;
        }
        return 0;
        }

}
