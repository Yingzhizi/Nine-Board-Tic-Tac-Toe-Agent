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
        /*
        感觉这个heuristic 应该改改， 如果一个cell里对面有两个连成一条线， 我方也是两个连成一条线
        比如此时有1, 2, 3, 5这几个点可以下 但是cell 5如下所示
        比如  . x x
             o o .
             . . .
        评估可能得到heuristic只有1, 但是此时不能下到cell 5， 因为这已经是对方赢的局面，
        所以我在另一个把o2的系数调大了 调成了30
        */


        result = 10 * x2 + x1 - (10 * o2 + o1);

        /* declare a rule that if for a cell, player win, got 100 grade */
        if (board.cellCheckPlayerWin(cellNumber, player)) {
            result += 100;
        } else if (board.cellCheckPlayerWin(cellNumber, opponent(player))) {
            result -= 100;
        }

        return result;
    }

    public static int boardEvaluation(AgentBoard board, int cellNumber, char player) {
        if (player != 'x' && player != 'o') {
            throw new IllegalArgumentException("there is no valid player");
        }

        int result;
        char opponent = opponent(player);
        int connectedTwo = board.evaluateHelper(2, cellNumber, opponent);
        int connectedOne = board.evaluateHelper(1, cellNumber, opponent);

        result = 10*connectedTwo + 10*connectedOne;
        // can't let opponent has chance to win :)
        return result;
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
        int connectedTwo = board.evaluateHelper(2, cellNumber, player);
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


//        /* Rule2, if the opponent has a winning move, block it */
//        int opponentConnectedTwo = board.evaluateConnectedTwo(cellNumber, opponent(player));
//        if (opponentConnectedTwo != 0) {
//            // take the winning move
//            // get available move for current player
//            ArrayList<Integer> canMoves = board.canMove(cellNumber);
//            for (Integer move : canMoves) {
//                board.setVal(cellNumber, move, opponent(player));
//                if (board.checkPlayerWin(opponent(player))) {
//                    if (board.evaluateConnectedTwo(move, opponent(player)) == 0 && board.evaluateHelper(1, move, opponent(player)) == 0) {
//                        board.undoSetVal(cellNumber, move);
//                        return move;
//                    }
//                }
//                board.undoSetVal(cellNumber, move);
//            }
//        }

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

        char currPlayer = board.getCurrentTurn();
        for (Integer move : canMoves) {
            // try the move for current player  
            board.setVal(cellNumber, move, player);
            // if player is the player in current agent, maximizing player
            if (player == currPlayer) {
                int connectedTwo = board.evaluateHelper(2, move, opponent(player));
                if (connectedTwo == 2) {
                    continue;
                }

                score = alphaBetaHelper(player, cellNumber, board, alpha, beta, level-1)[0] - boardEvaluation(board, move, player);
                if (score > alpha) {
                    alpha = score;
                    //alpha -= boardEvaluation(board, move, player);
                    indexOfBestMove = move;
                }
            } else if (player == opponent(currPlayer)) {
                int connectedTwo = board.evaluateHelper(2, move, player);
                if (connectedTwo == 2) {
                    continue;
                }

                score = alphaBetaHelper(player, cellNumber, board, alpha, beta, level-1)[0] + boardEvaluation(board, move, opponent(player));
                if (score < beta) {
                    beta = score;
                    //beta += boardEvaluation(board, move, player);
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

        if (player == currPlayer) {
            return new int[] {alpha, indexOfBestMove};
        }

        return new int[] {beta, indexOfBestMove};
    }


//    public static void main(String[] args) {
//        /* test if opponent works :) */
//        CellHeuristic test = new CellHeuristic();
//        char player1 = 'x';
//        System.out.println(test.opponent(player1));
//        char player2 = 'o';
//        System.out.println(test.opponent(player2));
//    }
}
