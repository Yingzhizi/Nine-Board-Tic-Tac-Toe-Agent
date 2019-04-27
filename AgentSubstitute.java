import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class AgentSubstitute {

    // save the cell we need to locate
    static int prevMove = 0;
    //static int[][] boards = new int[10][10];
    static AgentBoard boards;
    static char player;
    static CellHeuClone agentMove;
    static Random rand = new Random();
    static int count = 2;

    public AgentSubstitute() {
        boards = new AgentBoard();
    }

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
            boards = new AgentBoard();

        }else if(line.contains("start")) {
            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");
            // now we get the agent type
            String list = line.substring(argsStart+1, argsEnd);
            // System.out.println(line);
            char type = list.charAt(0);
            boards.setCurrentTurn(type);
            System.out.println("Player is " + type);
            player = type;

        }else if(line.contains("second_move")) {
            System.out.println("second move:" + line);
            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");

            String list = line.substring(argsStart+1, argsEnd);
            String[] numbers = list.split(",");

            char opponent = agentMove.opponent(player);

            // update the agent board
            prevMove = Integer.parseInt(numbers[1]);
            boards.setVal(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]), opponent);
            //place(Integer.parseInt(numbers[0]),Integer.parseInt(numbers[1]), 2);

            // get best move
            int bestMove = agentMove.getBestMove(player, prevMove, boards, 80);
            boards.setVal(prevMove, bestMove, player);
            System.out.println(bestMove);
            System.out.println(boards.canMove(prevMove));
            prevMove = bestMove;
            count+=2;
            return bestMove;

        }else if(line.contains("third_move")) {
            System.out.println("third move:" + line);
            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");

            String list = line.substring(argsStart+1, argsEnd);
            String[] numbers = list.split(",");

            char opponent = agentMove.opponent(player);
            // update agent board
            prevMove = Integer.parseInt(numbers[1]);
            boards.setVal(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]), player);
            prevMove = Integer.parseInt(numbers[2]);
            boards.setVal(Integer.parseInt(numbers[1]), Integer.parseInt(numbers[2]), opponent);
            //place(Integer.parseInt(numbers[0]),Integer.parseInt(numbers[1]), 1);
            //place(Integer.parseInt(numbers[1]),Integer.parseInt(numbers[2]), 2);

            // get the best move
            int bestMove = agentMove.getBestMove(player, Integer.parseInt(numbers[2]), boards, 10);
            boards.setVal(Integer.parseInt(numbers[2]), bestMove, player);
            System.out.println(bestMove);
            System.out.println(boards.canMove(prevMove));
            prevMove = bestMove;
            count+=2;
            return bestMove;

        }else if(line.contains("next_move")) {

            int argsStart = line.indexOf("(");
            int argsEnd = line.indexOf(")");

            String list = line.substring(argsStart+1, argsEnd);

            char opponent = agentMove.opponent(player);

            // update the agent board
            boards.setVal(prevMove, Integer.parseInt(list), opponent);
            prevMove = Integer.parseInt(list);
            //check performance
            long startTime = System.currentTimeMillis();

            // get the best move
            int bestMove = agentMove.getBestMove(player, Integer.parseInt(list), boards, 80);
            boards.setVal(Integer.parseInt(list), bestMove, player);
            System.out.println(bestMove);
            System.out.println(boards.canMove(prevMove));
            prevMove = bestMove;
            count+=2;

            long endTime = System.currentTimeMillis();
            System.out.println("next_move(" + bestMove + ")" + "runs "+ (endTime-startTime) + " ms");

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

//    public static void place(int board, int num, int player) {
//
//	prevMove = num;
//	boards[board][num] = player;
//    }
//
//    public static int makeRandomMove() {
//
//	int n = rand.nextInt(9) + 1;
//
//	while(boards[prevMove][n] != 0) {
//	    n = rand.nextInt(9) + 1;
//	}
//
//    place(prevMove, n, 1);
//    System.out.println(n);
//	return n;
//    }

}
