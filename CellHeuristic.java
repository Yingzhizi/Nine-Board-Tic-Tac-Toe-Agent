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

        /* declare a rule that if for a cell, player win, got 10 grade */
        if (board.cellCheckPlayerWin(cellNumber, player)) {
            result += 100;
        } else if (board.cellCheckPlayerWin(cellNumber, opponent(player))) {
            result -= 100;
        }

        return result;
    }

    // start doing alpha, beta pruning, return the best move.
    // notice, this method only useful to specific cell
    public static int alphaBeta(char player, int cellNumber, AgentBoard board, int level) {
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
        ArrayList<Integer> bestMoves = board.canMove(cellNumber);

        // initialize the score;
        int score;

        // when game is over or depth reached, evaluate score.
        if (board.gameOver() || level == 0) {
            score = cellEvaluation(board, cellNumber, player);
            return new int[] {score, indexOfBestMove};
        }

        // how I get to know the current turn is who???
        // assume we always player o first
        for (Integer move : bestMoves) {
            // try the move for current player
            board.setVal(cellNumber, move, player);
            // if player is 'o', maximizing player
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
