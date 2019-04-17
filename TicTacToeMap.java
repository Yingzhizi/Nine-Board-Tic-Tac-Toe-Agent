/* this is a 3x3 array of single tic-tac-toe board*/
public class TicTacToeMap {
    /* TODO: fix this to char[][] boards */
    /*
    * should be 11x11 board
    * empty use '.' to present
    * player x - 'x'
    * player o - 'o';
    * obstacle use '+';
    * e.g. at the beginning
    *  ...+...+...
    *  ...+...+...
    *  ...+...+...
    *  +++++++++++
    *  ...+...+...
    *  ...+...+...
    *  ...+...+...
    *  +++++++++++
    *  ...+...+...
    *  ...+...+...
    *  ...+...+...
    */
    /* each agent has has a map about all the action for now */
    /* */
    private Board[][] boards;

    private GameState state;

    public TicTacToeMap() {
        initBoard();
        state = GameState.InProgress;
    }

    private void initBoard() {
        boards = new Board[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boards[i][j] = new Board();
            }
        }
    }


    public void updateBoard(int index, int position, Player p) {
        /* get the coordinate of which tic-tac-top among 9 boards was chosen */
        /* this is get from the server */
        Coordinate chosenBoardCoordinate = getCoordinate(index);

        /* conduct the action in the chosen board */
        Coordinate actionCoordinate = getCoordinate(position);

        /* locate in the board chosen */
        Board chosenBoard = boards[chosenBoardCoordinate.getX()][chosenBoardCoordinate.getY()];
        boolean result = chosenBoard.updateMap(actionCoordinate.getX(), actionCoordinate.getY(), p);

        /* change the state of the game */
        if (result) {
            state = GameState.GameOver;
        }


    }

    public Coordinate getCoordinate(int num) {
        Coordinate result;
        switch (num) {
            case 1:
                result = new Coordinate(0,0);
                break;
            case 2:
                result = new Coordinate(0,1);
                break;
            case 3:
                result = new Coordinate(0,2);
                break;
            case 4:
                result = new Coordinate(1,0);
                break;
            case 5:
                result = new Coordinate(1,1);
                break;
            case 6:
                result = new Coordinate(1,2);
                break;
            case 7:
                result = new Coordinate(2,0);
                break;
            case 8:
                result = new Coordinate(2,1);
                break;
            case 9:
                result = new Coordinate(2,2);
                break;
            /* can only from 1 to 9 */
            default:
                return null;
        }
        return result;
    }

    public boolean gameOver() {
        if (state.equals(GameState.GameOver)) {
            return true;
        } else {
            return false;
        }
    }

    /* Testing */
    public static void main(String[] args){
        TicTacToeMap test = new TicTacToeMap();
        Coordinate hello = test.getCoordinate(4);
        System.out.println(hello.getX());
        System.out.println(hello.getY());
    }
}
