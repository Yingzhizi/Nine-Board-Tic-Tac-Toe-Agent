public class Board {
    /* contains the map has 9 3x3 tic-tac-toe boards */
    private Cell[][] map;
    Player winner;
    GameState unitState;

    public Board() {
        initMap();
        winner = null;
        unitState = GameState.InProgress;
    }
    /* initialize an empty map before the game start */
    public void initMap() {
        map = new Cell[3][3];
        mapClearUp();
    }

    /* clean up the 3x3 tic-tac-toe board */
    public void mapClearUp() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                map[i][j] = new Cell();
            }
        }
    }

    /* update the map by a specific move */
    /* move can vary from 1 to 9 */
    /* if new move change the state of the game, return true */
    /* check valid when read the move */
    public boolean updateMap(int x, int y, Player p) {
        map[x][y].setPlayer(p);
        /* check if this movement change the game state */
        if (checkWinning(p)) {
            unitState = GameState.GameOver;
            winner = p;
            return true;
        } else {
            return false;
        }
    }

    /* to check if in a unit borad there exist winner */
    /* need it or not?? */
    public boolean checkWinning(Player p) {
        if (this.map[0][0].getPlayer() == p && this.map[1][1].getPlayer() == p && this.map[2][2].getPlayer() == p) {
            return true;
        }
        else if (this.map[2][0].getPlayer() == p && this.map[1][1].getPlayer() == p && this.map[0][2].getPlayer() == p) {
            return true;
        }
        else if (this.map[0][0].getPlayer() == p && this.map[1][0].getPlayer() == p && this.map[2][0].getPlayer() == p) {
            return true;
        }
        else if (this.map[0][1].getPlayer() == p && this.map[1][1].getPlayer() == p && this.map[2][1].getPlayer() == p) {
            return true;
        }
        else if (this.map[0][2].getPlayer() == p && this.map[1][2].getPlayer() == p && this.map[2][2].getPlayer() == p) {
            return true;
        }
        else if (this.map[0][0].getPlayer() == p && this.map[0][1].getPlayer() == p && this.map[0][2].getPlayer() == p) {
            return true;
        }
        else if (this.map[1][0].getPlayer() == p && this.map[1][1].getPlayer() == p && this.map[1][2].getPlayer() == p) {
            return true;
        }
        else if (this.map[2][0].getPlayer() == p && this.map[2][1].getPlayer() == p && this.map[2][2].getPlayer() == p) {
            return true;
        }
        else {
            return false;
        }

    }
}
