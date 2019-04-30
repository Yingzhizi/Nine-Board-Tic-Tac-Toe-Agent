/*********************************************************************
*  Agent.java & AgentMove.java & AgentBoard.java
*  Written for COMP3411/9414/9814 Artificial Intelligence
*  Authors: Yingzhi Zhou(z5125679)  Qian Chen(z5222755)
*  How the agent works: 
*
*  Firstly, we created a three-dimensional array to store the 9x9 
*  in the class AgentBoard, and we divided the board into 9 sub-boards.
*  The sub-board, also named cell by us, has index 1-9 in board[0-8][i][j]
*  and i is the index for row, j for column. We set every position 
*
*/



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Agent {

    // save the cell we need to locate
    static int prevMove = 0;
    static AgentBoard boards;
    static char player;
    static char opponent;
    static AgentMove agentMove = AgentMove.getAgent();

    public Agent() {}

    /* 0 = Empty
     * 1 = I played here
     * 2 = They played here
     * Zero index not used
     */
    public static void main(String args[]) throws IOException {

    AgentSubstitute newPlayer = new AgentSubstitute();

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

    public AgentBoard getBoards() {
        return boards;
    }

    public static int parse(String line) {

        // In agent.java If you receive second_move(6,5) command
        // that means you play 'o' and the first move is 'x' in the 5th of the cell 6
        // And If you receive third_move(6,5,7) command
        // that means you play 'x' and your first move is 'x' in 5th of the cell 6 randomly set by teacher

        if(line.contains("init")) {
            // initialize the agent board
            

        }else if(line.contains("start")) {
            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");
            // now we get the agent type
            String list = line.substring(argsStart+1, argsEnd);
            // System.out.println(line);
            char type = list.charAt(0);
            
            agentMove.setAgent(type);
            System.out.println("Player is " + type);
            

        }else if(line.contains("second_move")) {
            System.out.println("second move:" + line);
            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");

            String list = line.substring(argsStart+1, argsEnd);
            String[] numbers = list.split(",");

            // get best move
            int bestMove = agentMove.playSecondMove(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
            System.out.println("Best move: " + bestMove);
            return bestMove;

        }else if(line.contains("third_move")) {
            System.out.println("third move:" + line);
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

            // opponent = agentMove.opponent(player);

            // update the agent board
            int opponentMove =  Integer.parseInt(list);
            System.out.println("Real Opponent Move: " + opponentMove);

            long startTime = System.currentTimeMillis();

            System.out.println("Oppo move: " + opponentMove);
            // get the best move
            int bestMove = agentMove.playNextMove(opponentMove);
            System.out.println("Best move: " + bestMove);

            long endTime = System.currentTimeMillis();
            System.out.println("next_move(" + bestMove + ")" + "runs "+ (endTime-startTime) + " ms");
            

            return bestMove;

        }else if(line.contains("last_move")) {
            //TODO
            if (agentMove.checkWinner() != "draw"){
                System.out.println("win win win");
            }
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
