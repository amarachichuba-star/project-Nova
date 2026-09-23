package Game;

import java.util.Random;

class Player {
    String name;
    int[][] board;
    int[][] creatures;
    int[][] powerUps;
    int[][] traps;
    int score;

    public Player(String name) {
        this.name = name;
        this.board = new int[8][8];
        this.creatures = new int[6][2];
        this.powerUps = new int[1][2];
        this.traps = new int[1][2];
        this.score = 0;

        initBoard();
        initCreatures();
        initPowerUps();
        initTraps();
    }

    private void initBoard() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = -1;
    }

    private void initCreatures() {
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            creatures[i][0] = r.nextInt(8);
            creatures[i][1] = r.nextInt(8);

            for (int j = 0; j < i; j++) {
                while (creatures[i][0] == creatures[j][0] &&
                       creatures[i][1] == creatures[j][1]) {

                    creatures[i][0] = r.nextInt(8);
                    creatures[i][1] = r.nextInt(8);
                }
            }
        }
    }

    private void initPowerUps() {
        Random r = new Random();

        powerUps[0][0] = r.nextInt(8);
        powerUps[0][1] = r.nextInt(8);

        // avoid overlapping with creatures
        for (int[] c : creatures) {
            while (c[0] == powerUps[0][0] && c[1] == powerUps[0][1]) {
                powerUps[0][0] = r.nextInt(8);
                powerUps[0][1] = r.nextInt(8);
            }
        }
    }

    private void initTraps() {
        Random r = new Random();

        traps[0][0] = r.nextInt(8);
        traps[0][1] = r.nextInt(8);

        for (int[] c : creatures) {
            while ((c[0] == traps[0][0] && c[1] == traps[0][1]) ||
                   (powerUps[0][0] == traps[0][0] && powerUps[0][1] == traps[0][1])) {

                traps[0][0] = r.nextInt(8);
                traps[0][1] = r.nextInt(8);
            }
        }
    }
}

