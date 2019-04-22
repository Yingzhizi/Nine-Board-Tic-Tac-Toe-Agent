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

    public static int alphaBeta(char player, int cellNumber, AgentBoard board, int level) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        alphaBetaHelper(player, cellNumber, board, alpha, beta, level);
    }

    public static int alphaBetaHelper(char player, int cellNumber, AgentBoard board, int alpha, int beta, int level) {
        if (board.gameOver() || level == 0) {
            return cellEvaluation(board, cellNumber, player);
        }

        // how I get to know the current turn is who???
        // assume we always player o first
        // TODO: has duplicated???
        // find the best move
        ArrayList<Integer> bestMoves = board.canMove(cellNumber);
        int result = -1;
        if (player == 'o') {
            int indexOfBestMove = -1;
            for (Integer move : bestMoves) {
                AgentBoard copyBoard = board;
                copyBoard.setVal(cellNumber, move, player);
                int score = alphaBetaHelper(player, cellNumber, copyBoard, alpha, beta, level-1);
                if (score > alpha) {
                    alpha = score;
                    indexOfBestMove = move;
                }

                // do the pruning
                if (alpha >= beta) {
                    break;
                }
            }
            if (indexOfBestMove != -1) {
                board.setVal(cellNumber, indexOfBestMove, player);
            }
            result = alpha;

        } else if (player == 'x') {
            int indexOfBestMove = -1;
            for (Integer move : bestMoves) {
                // make a copy of the current move
                AgentBoard copyBoard = board;
                copyBoard.setVal(cellNumber, move, player);
                int score = alphaBetaHelper(player, cellNumber, copyBoard, alpha, beta, level-1);
                if (score < beta) {
                    beta = score;
                    indexOfBestMove = move;
                }

                // do the pruning
                if (alpha >= beta) {
                    break;
                }
            }
            if (indexOfBestMove != -1) {
                // TODO: somehow return the index of the best move
                board.setVal(cellNumber, indexOfBestMove, player);
            }
            result = beta;

        }
        return result;
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
