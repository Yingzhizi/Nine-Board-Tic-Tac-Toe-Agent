import java.util.ArrayList;

/**
 * AgentMove
 */
public class AgentMove {
        /* current player */
    private char player = 'o';
    private int count = 0;
    /* default agent, modify at the beginning */
    private char agent = 'o';
    private char opponent = 'x';
    private int lastMove = 0;
    
    /* store alpha value that have been searched */
    private int search_alpha = Integer.MIN_VALUE;

    /* change agent to singleton design pattern
    *  set global entry for agent
    */
    private AgentBoard board = new AgentBoard();
    private static AgentMove singletonAgent = new AgentMove();
    private AgentMove(){}
    public static AgentMove getAgent(){
        return singletonAgent;
    }

    public void printArray(String s, ArrayList<Integer> arr){
        System.out.print(s + " [");
        for (int i=0; i<arr.size(); i++){
            System.out.print(arr.get(i) + " ");
        }
        System.out.println("]");
    }

    public void setVal(int cell, int positionNumber, char val){
        board.setVal(cell, positionNumber, val);
    }

    public void displayBoard(){
        board.displayBoard();
    }

    /*  use at the beginning of game, 
    after receive secondMove or thirdMove  */
    public void setAgent(char x){
        agent = (x == 'o') ? 'o' : 'x';
        opponent = (x == 'o') ? 'x': 'o';
    }

    /* get opponent */
    public char getOpponent(char player){
        char oppo = (player == 'o') ? 'x': 'o';
        return oppo;
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
    public int playSecondMove(int cell, int firstMove){
        board.setVal(cell, firstMove, 'x');
        setAgent('o');
        board.setVal(cell, firstMove, opponent);
        int bestMove = getBestMove(firstMove);
        board.setVal(firstMove, bestMove, agent);
        lastMove = bestMove;
        board.displayBoard();

        return bestMove;
    }

    public int playThirdMove(int first, int second, int third){
        setAgent('x');
        board.setVal(first, second, agent);
        board.setVal(second, third, opponent);
        int bestMove = getBestMove(third);
        lastMove = bestMove;

        return bestMove;
    }

    public int playNextMove(int opponentMove){
        count += 1;
        board.setVal(lastMove, opponentMove, opponent);
        int bestMove = getBestMove(opponentMove);
        if (!board.isLegal(opponentMove, bestMove)){
            int substituteMove = findSubstituteMove(opponentMove);
            board.setVal(opponentMove, substituteMove, agent);
            lastMove = substituteMove;
            System.out.println("Total count: " + count);
            board.displayBoard();
            return substituteMove;
        }
        board.setVal(opponentMove, bestMove, agent);
        if(board.CellGetTwo(bestMove, opponent).size()>0){
            int substituteMove = findSubstituteMove(opponentMove);
            board.undoSetVal(opponentMove, bestMove);
            board.setVal(opponentMove, substituteMove, agent);
            lastMove = substituteMove;
            System.out.println("Total count: " + count);
            board.displayBoard();

            return substituteMove;
        }
        lastMove = bestMove;
        board.displayBoard();
        System.out.println("Total count: " + count);
        return bestMove;
    }

    public int findSubstituteMove(int opponentMove){ 
        ArrayList<Integer> substituteMoves = board.canMove(opponentMove);
        ArrayList<Integer> heuristics = new ArrayList<Integer>();
        int maxHeuristic = Integer.MIN_VALUE;
        int maxMove = 0;
        for(int i = 0; i<substituteMoves.size(); i++){
            if(cellHeuristic(substituteMoves.get(i)) > maxHeuristic){
                maxHeuristic = cellHeuristic(substituteMoves.get(i));
                maxMove = (Integer)substituteMoves.get(i);
            }
            heuristics.add(cellHeuristic(substituteMoves.get(i)));
        } 
        printArray("heuristic: ", substituteMoves);
        printArray("values: ", heuristics);
        
        return maxMove;
    }

    public int getBestMove(int opponentMove){

    /* Rule 1: IF can win, then choose the win move  */

        ArrayList<Integer> moves = board.CellGetTwo(opponentMove, agent);
        printArray("moves:", moves);
        if (moves.size() > 0){
            Integer winMove = moves.get(0);
            // board.setVal(opponentMove, winMove, agent);
            // lastMove = winMove;
            System.out.println("from winMoves");
            return winMove;
        }
        
        
        

    /* Rule 2: IF opponent has two connected, and the block position don't cause
        opponent win, then block */
        ArrayList<Integer> opponentMoves = board.CellGetTwo(opponentMove, opponent);
        printArray("opponentMoves:", opponentMoves);
        if (opponentMoves.size() > 0){
            for (Integer oppoMove: opponentMoves){
                if (board.CellGetTwo(oppoMove, opponent).size() == 0){
                    Integer blockMove = oppoMove;
                    // board.setVal(opponentMove, blockMove, agent);
                    // lastMove = blockMove;
                    System.out.println("from blockMoves");
                    return blockMove;
                }
            }
        }
    
    
    /* Rule 3: IF Agent can play on the central of the cell, then move to 5 */
        ArrayList<Integer> cellFiveMoves = board.CellGetTwo(5, opponent);
        printArray("cellFiveMoves:", cellFiveMoves);
        if (cellFiveMoves.size() == 0){
            ArrayList<Integer> canMoves = board.canMove(opponentMove);
            if (canMoves.contains(5)){
                Integer moveFive = 5;
                // board.setVal(opponentMove, moveFive, agent);
                // lastMove = moveFive;
                System.out.println("from cellFiveMoves");
                return moveFive;
            }
        }

        /* Rule 4: IF in a cell that opponent has two connected, don't move  */
        // ArrayList<Integer> canMoves = board.canMove(opponentMove);
        // for(Integer move: canMoves){
        //     if (board.CellGetTwo(move, opponent).size() == 0){
        //         return move;
        //     }
        // }

        
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int[] score = alphaBeta(board, opponentMove, agent, alpha, beta, 8);

        return score[1];

    }




    public int cellHeuristic(int cell){
        int sumHeuristic = 0;
        ArrayList<Integer> oppoTwo = board.CellGetTwo(cell, opponent);
        if (oppoTwo.size() >0){
            sumHeuristic -= 300;
        }
        ArrayList<Integer> agentTwo = board.CellGetTwo(cell, agent);
        if (agentTwo.size() >0){
            sumHeuristic += 100 ;
            for(Integer move: agentTwo){
                ArrayList<Integer> nextAgentTwo = board.CellGetTwo(move, agent);
                if (nextAgentTwo.size() > 0){
                    sumHeuristic += 100;
                }
            }
        }
        sumHeuristic -= board.winLine(cell, opponent) * 50;

        int o1 = board.evaluateHelper(1, cell, agent);
        int x1 = board.evaluateHelper(1, cell, opponent);
        sumHeuristic += 10 * o1;
        sumHeuristic -= 10 * x1;

        return sumHeuristic;
    }

    public int boardHeuristic(){
        int sumHeuristic = 0;
        for(int cell=1; cell<10; cell++){
            sumHeuristic += cellHeuristic(cell);
        }

        return sumHeuristic;
    }

    public ArrayList<Integer> getTwo(int cell, char player){
        ArrayList<Integer> moves = board.CellGetTwo(cell, player);



        return moves;
    }

    /* alpha-beta pruning  */
    public int[] alphaBeta(AgentBoard board, int cell, char player, int alpha, int beta, int level){

        int move = 0;
        if (board.CellGetTwo(cell, opponent).size() > 0){
            return new int[] {-1000, cell};
        }else if(board.CellGetTwo(cell, agent).size() > 0){
            return new int[] {1000, cell};
        }else if(board.cellIsFull(cell)){
            return new int[] {0, cell}; 
        }

        if (level == 0 || board.isFull()){
            // return new int[] {cellHeuristic(cell), cell};
            return new int[] {cellHeuristic(cell), cell};
        }

        // board.displayBoard();
        // System.out.println();

        if (player == opponent){
            ArrayList locations = board.canMove(cell);
            for(int i=0; i<locations.size(); i++){
                // board.displayBoard();
                // System.out.println();  
                board.setVal(cell, (Integer)locations.get(i), opponent);
                // moveBd.displayBoard();
                int score = alphaBeta(board, (Integer)locations.get(i), agent, alpha, beta, level-1)[0] - cellHeuristic((Integer)locations.get(i));
                board.undoSetVal(cell, (Integer)locations.get(i));
                if(score < beta){
                    move = (Integer)locations.get(i);
                    beta = score;
                    if (level == 9){
                        // System.out.println("score: "+ score);
                    }
                    if (alpha >= beta){
                        // return new int[] {alpha, move};
                        break;
                    }
                } 
            }
            // System.out.println("player: " + player + " cell: "+ cell+" alpha: "+ alpha + " beta: "+beta+" level: "+ level);
            return new int[] {beta, move};
        }else{
            ArrayList locations = board.canMove(cell);
            for(int i=0; i<locations.size(); i++){
                // board.displayBoard();
                // System.out.println();
                board.setVal(cell, (Integer)locations.get(i), agent);
                // board.displayBoard();
                int score = alphaBeta(board, (Integer)locations.get(i), opponent, alpha, beta, level-1)[0] + cellHeuristic((Integer)locations.get(i));
                board.undoSetVal(cell, (Integer)locations.get(i));

                if(score > alpha){
                    alpha = score;
                    move = (Integer)locations.get(i);
                    if (level == 9){
                        // System.out.println("score: "+ score);
                    }
                    if (alpha >= beta){
                        // return new int[] {beta, move};
                        break;
                    }
                } 
                
            }
            ArrayList<Integer> c = new ArrayList<Integer>();
            for (int i = 0; i<9; i++){
                c.add(cellHeuristic(i+1));
            }
            // printArray("cell heuristics:", c);
            // System.out.println("player: " + player + " cell: "+ cell+" alpha: "+ alpha + " beta: "+beta+" level: "+ level);
            return new int[] {alpha, move};
        }
    }


    public String checkWinner() {
        if (board.checkPlayerWin(agent)){
            return "agent";
        }else if(board.checkPlayerWin(opponent)){
            return "opponent";
        }

        return "draw";
    }


    public static void main(String[] args) {
        System.out.println("agent move!");
        AgentMove mv = AgentMove.getAgent();
        
        mv.setVal(5, 5, 'o');
        mv.setVal(5, 9, 'o');
        mv.displayBoard();
        mv.printArray("move:", mv.board.CellGetTwo(5, 'o'));
    }

}
