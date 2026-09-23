package Game;

import java.io.*;
import java.util.Scanner;

public class LoadSave {

    private static final String FILE_PATH =
        "C:\\Users\\Windows\\OneDrive - University of Dundee\\CS11002\\JAVA\\Project NOVA\\Project NOVA\\res\\previousGame.txt";

    // ========================
    // SAVE PLAYER
    // ========================
    private static void savePlayer(PrintWriter pw, Player p) {
        pw.println("PLAYER");
        pw.println(p.name);
        pw.println(p.score);

        // board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pw.print(p.board[i][j] + " ");
            }
            pw.println();
        }

        // creatures
        pw.println(p.creatures.length);
        for (int[] c : p.creatures) {
            pw.println(c[0] + " " + c[1]);
        }

        // traps
        pw.println(p.traps[0][0] + " " + p.traps[0][1]);

        // power ups
        pw.println(p.powerUps[0][0] + " " + p.powerUps[0][1]);
    }

    // LOAD A SINGLE PLAYER
    private static Player loadSinglePlayer(Scanner sc) {

        sc.nextLine(); // consume "PLAYER"

        String name = sc.nextLine();
        Player p = new Player(name);

        p.score = Integer.parseInt(sc.nextLine());

        // board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                p.board[i][j] = sc.nextInt();
            }
        }

        int creatureCount = sc.nextInt();
        for (int i = 0; i < creatureCount; i++) {
            p.creatures[i][0] = sc.nextInt();
            p.creatures[i][1] = sc.nextInt();
        }

        p.traps[0][0] = sc.nextInt();
        p.traps[0][1] = sc.nextInt();

        p.powerUps[0][0] = sc.nextInt();
        p.powerUps[0][1] = sc.nextInt();

        return p;
    }

    // SAVE FULL GAME
    public static void saveFullGame(Player p1, Player p2, boolean vsAI, Player current) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {

            pw.println(vsAI);
            pw.println(current.name);

            savePlayer(pw, p1);
            savePlayer(pw, p2);

            System.out.println("Game saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            
            LoadSave.saveFullGame(p1, p2, vsAI, current);
        }
    }

    // LOAD FULL GAME
    public static GameState loadFullGame() {
        try (Scanner sc = new Scanner(new File(FILE_PATH))) {

            boolean vsAI = Boolean.parseBoolean(sc.nextLine());
            String currentName = sc.nextLine();

            Player p1 = loadSinglePlayer(sc);
            Player p2 = loadSinglePlayer(sc);

            Player current = p1.name.equals(currentName) ? p1 : p2;
            
            GameState state = LoadSave.loadFullGame();

            if (state == null) {
                System.out.println("No saved game found!");
                return state;
            }

            System.out.println("\nLoaded previous game!");

            continueLoadedGame(state);
            return new GameState(p1, p2, current, vsAI);

        } catch (Exception e) {
            return null;
        }
        
    }

    public static void continueLoadedGame(GameState s) {
        Player player1 = s.p1;
        Player player2 = s.p2;
        Player current = s.current;
        boolean vsAI = s.vsAI;

        System.out.println("\n=== Game Loaded! Resuming... ===\n");
    }

}
