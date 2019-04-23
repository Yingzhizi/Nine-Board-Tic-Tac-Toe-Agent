import java.util.*;

public class CellHeuristic {

    private static int maxVal;


    public static char opponent(char player) {
        char result = (player == 'x') ? 'o' : 'x';
        return result;
    }

    /* we can treat the whole game has 9 sub games. */
    /* count the score of each sub board */
    /* Eval(s) = 10X2(s) + X1(s) - (10O2(s) + O1(s)) */
    /* this help to fo alpha-beta pruning */
    public static int cellEvaluation (AgentBoard board, int cellNumber, char player) {
        if (player != 'x' && player != 'o') {
            throw new IllegalArgumentException("there is no valid player");
        }
        int result;
        char opponent = opponent(player);
        /* according to the evaluation function, get the score of each cell */
        int x2 = board.evaluateHelper(2, cellNumber, player);
        int x1 = board.evaluateHelper(1, cellNumber, player);
        int o2 = board.evaluateHelper(2, cellNumber, opponent);
        int o1 = board.evaluateHelper(1, cellNumber, opponent);
        result = 10 * x2 + x1 - (10 * o2 + o1);

        /* declare a rule that if for a cell, player win, got 100 grade */
        if (board.cellCheckPlayerWin(cellNumber, player)) {
            result += 100;
        } else if (board.cellCheckPlayerWin(cellNumber, opponent(player))) {
            result -= 100;
        }

        return result;
    }

    public static boolean boardEvaluation(AgentBoard board, int cellNumber, char player) {
        if (player != 'x' && player != 'o') {
            throw new IllegalArgumentException("there is no valid player");
        }
        int result;
        char opponent = opponent(player);
        int connectedTwo = board.evaluateHelper(2, cellNumber, opponent);
        // can't let opponent has chance to win :)
        if (connectedTwo != 0) {
            return true;
        }

        return false;
    }

    // start doing alpha, beta pruning, return the best move.
    // notice, this method only useful to specific cell
    // TODO: need to add evaluation to check if conduct the best move, opponent has a chance to win.
    public static int getBestMove(char player, int cellNumber, AgentBoard board, int level) {
        /* Rule 1: If I have a winning move, take it.
           Rule 2: If the opponent has a winning move, block it.
           Rule 3: If I can create a fork (two winning ways) after this move, do it. - use alpha-beta pruning
           Rule 4: Do not let the opponent creating a fork after my move. (Opponent may block your winning move and create a fork.)
           Rule 5: Place in the position such as I may win in the most number of possible ways.
        */

        /* Rule1, check if I have any winning move */
        int connectedTwo = board.evaluateConnectedTwo(cellNumber, player);
        if (connectedTwo != 0) {
            // take the winning move
            // get available move for current player
            ArrayList<Integer> canMoves = board.canMove(cellNumber);
            for (Integer move : canMoves) {
                board.setVal(cellNumber, move, player);
                if (board.checkPlayerWin(player)) {
                    board.undoSetVal(cellNumber, move);
                    return move;
                }
                board.undoSetVal(cellNumber, move);
            }
        }

        /* Rule2, if the opponent has a winning move, block it */
        int opponentConnectedTwo = board.evaluateConnectedTwo(cellNumber, opponent(player));
        if (opponentConnectedTwo != 0) {
            // take the winning move
            // get available move for current player
            ArrayList<Integer> canMoves = board.canMove(cellNumber);
            for (Integer move : canMoves) {
                board.setVal(cellNumber, move, opponent(player));
                if (board.checkPlayerWin(opponent(player))) {
                    board.undoSetVal(cellNumber, move);
                    return move;
                }
                board.undoSetVal(cellNumber, move);
            }
        }

        /* if the move chosen corresponding to the sub cell that opponent has a chance to win
        *  cannot take this move
        * */

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int[] result = alphaBetaHelper(player, cellNumber, board, alpha, beta, level);

        // return the best move
        // best move store in index1 of return value;
        return result[1];
    }

    public static int[] alphaBetaHelper(char player, int cellNumber, AgentBoard board, int alpha, int beta, int level) {
        int indexOfBestMove = -1;

        // find the best move
        ArrayList<Integer> canMoves = board.canMove(cellNumber);

        // initialize the score;
        int score;

        // when game is over or depth reached, evaluate score.
        if (board.gameOver() || level == 0) {
            score = cellEvaluation(board, cellNumber, player);
            return new int[] {score, indexOfBestMove};
        }

        // how I get to know the current turn is who???
        // In agent.java If you receive second_move(6,5) command
        // that means you play 'o' and the first move is 'x' in the 5th of the cell 6
        // And If you receive third_move(6,5,7) command
        // that means you play 'x' and your first move is 'x' in 5th of the cell 6 randomly set by teacher
        // and the opponent 'o' move is the 7th of the cell 5
        // After that, you receive next_move(5), next_move(6) and you decide which cell it should be set
        // see http://www.cse.unsw.edu.au/~cs3411/19t1/hw3/
        // assume we always player o first
        for (Integer move : canMoves) {
            // try the move for current player
            board.setVal(cellNumber, move, player);
            // if player is 'o', maximizing player
            if (move != cellNumber && boardEvaluation(board, move, opponent(player))) {
                /* if the move chosen corresponding to the sub cell that opponent has a chance to win
                 *  cannot take this move, jump to next one */
                continue;
            }
            if (player == 'o') {
                score = alphaBetaHelper(player, cellNumber, board, alpha, beta, level-1)[0];
                if (score > alpha) {
                    alpha = score;
                    indexOfBestMove = move;
                }
            } else if (player == 'x') {
                score = alphaBetaHelper(player, cellNumber, board, alpha, beta, level-1)[0];
                if (score < beta) {
                    beta = score;
                    indexOfBestMove = move;
                }
            }

            //undo move
            board.undoSetVal(cellNumber, move);

            // do the pruning
            if (alpha >= beta) {
                break;
            }
        }

        if (player == 'o') {
            return new int[] {alpha, indexOfBestMove};
        }

        return new int[] {beta, indexOfBestMove};
    }

    public static void main(String[] args) {
        /* test if opponent works :) */
        CellHeuristic test = new CellHeuristic();
        char player1 = 'x';
        System.out.println(test.opponent(player1));
        char player2 = 'o';
        System.out.println(test.opponent(player2));
    }
}
