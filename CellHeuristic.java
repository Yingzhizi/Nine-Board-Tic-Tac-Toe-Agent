public class CellHeuristic {

    private static int maxVal;

    public static void alphaBeta(char player, AgentBoard board, int alpha, int beta, int level) {

    }

    public char opponent(char player) {
        char result = (player == 'x') ? 'o' : 'x';
        return result;
    }

    /* we can treat the whole game has 9 sub games. */
    /* count the score of each sub board */
    /* Eval(s) = 3X2(s) + X1(s) - (3O2(s) + O1(s)) */
    public int cellEvaluation (AgentBoard board, int cellNumber, char player) {
        if (player != 'x' || player != 'o') {
            throw new IllegalArgumentException("there is no valid player");
        }
        int result;
        /* declare a rule that if for a cell, player win, got 10 grade */
        if (board.cell_check_player_win(cellNumber, player)) {
            result = 10;
        } else if (board.cell_check_player_win(cellNumber, opponent(player))) {
            result = -10;
        } else {
            result = 0;
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
