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
    private void initMap() {
        map = new Cell[3][3];
        mapClearUp();
    }

    /* clean up the 3x3 tic-tac-toe board */
    private void mapClearUp() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                map[i][j] = null;
            }
        }
    }

    /* update the map by a specific move */
    /* move can vary from 1 to 9 */
    /* check valid when read the move */
    /* TODO: rewirte, so ugly */
    private boolean updateMap(int move, Player p) {
        if (move == 1) {
            map[0][0].setPlayer(p);
        } else if (move == 2) {
            map[0][1].setPlayer(p);
        } else if (move == 3) {
            map[0][2].setPlayer(p);
        } else if (move == 4) {
            map[1][0].setPlayer(p);
        } else if (move == 5) {
            map[1][1].setPlayer(p);
        } else if (move == 6) {
            map[1][2].setPlayer(p);
        } else if (move == 7) {
            map[2][0].setPlayer(p);
        } else if (move == 8) {
            map[2][1].setPlayer(p);
        } else if (move == 9) {
            map[2][2].setPlayer(p);
        } else {
            return false;
        }

        /* check if this movement change the game state */
        if (checkWinning(p)) {
            unitState = GameState.GameOver;
            winner = p;
        }

        return true;
//        switch(move) {
//            case 1:
//                map[0][0].setPlayer(p);
//                break;
//            case 2:
//                map[0][1].setPlayer(p);
//                break;
//            case 3:
//                map[0][2].setPlayer(p);
//                break;
//            case 4:
//                map[1][0].setPlayer(p);
//            case 5:
//                map[1][1].setPlayer(p);
//            case 6:
//                map[1][2].setPlayer(p);
//            case 7:
//                map[2][0].setPlayer(p);
//            case 8:
//                map[2][1].setPlayer(p);
//            case 9:
//                map[2][2].setPlayer(p);
//        }
    }

    /* to check if in a unit borad there exist winner */
    /* need it or not?? */
    private boolean checkWinning(Player p) {
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
