package Game;

import java.util.Random;
import java.util.Scanner;


public class MainGame {

    static Scanner input = new Scanner(System.in);

    // Emojis for Creatures
    static String[] creatureEmoji = {"🐉", "🦊", "🐢", "🦑", "🦀", "🐠"};
    static String powerUpEmoji = "💰";
    static String trapEmoji = "☠️";

    // ANSI COLOUR CODES
    static String RESET = "\u001B[0m";
    static String RED = "\u001B[31m";
    static String GREEN = "\u001B[32m";
    static String YELLOW = "\u001B[33m";
    static String BLUE = "\u001B[36m";
    static String BRIGHT_RED = "\u001B[91m";

    public void play() { //Game Start

        System.out.println("\n--- ProjectNova ---");
        System.out.println("1. Two Player");
        System.out.println("2. Single Player vs AI");
        System.out.println(" Enter 10 to exit");
        System.out.print("Choose mode: ");
      
        int mode = input.nextInt();
    
        Player player1 = new Player("Player 1");
        Player player2 = (mode == 1) ? new Player("Player 2") : new Player("AI");
      
        Player current = player1;
        boolean vsAI = (mode == 2);

        int attempts = 0;
        int totalCreatures = 12;
        int creaturesFound = 0;

        do {
            System.out.println("\n----------------------------------");
            System.out.println(current.name + "'s Turn");
            System.out.println("----------------------------------");

            showBoard(current.board);

            int[] move;
            if (vsAI && current.name.equals("AI")) {
                move = aiMove(current);
                System.out.println("AI chooses: "
                        + (move[0] + 1) + ", " + (move[1] + 1));
            } else {
                move = getMove(current);
            }

            attempts++;
            int result = checkHit(move, current);

            // RESULTS
            if (result >= 1 && result <= 6) {
                System.out.println(GREEN + "Creature Found! "
                        + creatureEmoji[result - 1] + RESET);

                current.board[move[0]][move[1]] = result;
                current.score += 5;
                creaturesFound++;

            } else if (result == 7) {
                System.out.println(YELLOW + "POWER-UP FOUND! " + powerUpEmoji + RESET);

                current.board[move[0]][move[1]] = 7;
                current.score += 15;

            } else if (result == 8) {
                System.out.println(BRIGHT_RED + "BOOBY TRAP! " + trapEmoji
                        + "  -10 points!" + RESET);

                current.board[move[0]][move[1]] = 8;
                current.score -= 10;

            } else {
                System.out.println(RED + "Miss!" + RESET);
                current.board[move[0]][move[1]] = 0;
            }

            System.out.println("Points: " + current.score);

            giveHint(move, current.creatures);

            current = (current == player1) ? player2 : player1;

        } while (creaturesFound < totalCreatures);

        // END GAME
        System.out.println("\n--- GAME OVER ---");
        System.out.println("Total attempts: " + attempts);

        System.out.println("\nFinal Scores:");
        System.out.println(player1.name + ": " + player1.score);
        System.out.println(player2.name + ": " + player2.score);

        if (player1.score > player2.score)
            System.out.println("\nWinner: " + player1.name + "! 🎉");
        else if (player2.score > player1.score)
            System.out.println("\nWinner: " + player2.name + "! 🎉");
        else
            System.out.println("\nIt's a tie!");

        System.out.println("\nFinal Boards:");
        System.out.println(player1.name + "'s board:");
        showBoard(player1.board);

        System.out.println(player2.name + "'s board:");
        showBoard(player2.board);
        
    }

 // Display Board - fixed alignment that handles ANSI colours 
    public static void showBoard(int[][] board) {
        // column header: print a space for the row labels area, then 8 columns
        System.out.print("   "); // space for row labels
        for (int col = 1; col <= 8; col++) {
            System.out.printf("%4d", col); // each column is 4 characters wide
        }
        System.out.println();

        for (int i = 0; i < 8; i++) {
         
            System.out.printf("%3d", i + 1);

            for (int j = 0; j < 8; j++) {
                int cell = board[i][j];
                String symbol;

                switch (cell) {
                    case -1 -> symbol = "~";             // unexplored 
                    case 0  -> symbol = "X";             // miss
                    case 7  -> symbol = powerUpEmoji;    // power-up
                    case 8  -> symbol = trapEmoji;       // trap
                    default -> {
                        if (cell >= 1 && cell <= 6)
                            symbol = creatureEmoji[cell - 1]; // creatures 1..6
                        else
                            symbol = "?";
                    }
                }

                // choose colour based on symbol (this applies the colour codes outside the formatted width)
                String prefix = "";
                String suffix = RESET;
                if (cell >= 1 && cell <= 6) prefix = GREEN;
                else if (cell == 0) prefix = RED;
                else if (cell == 7) prefix = YELLOW;
                else if (cell == 8) prefix = BRIGHT_RED;
                else prefix = ""; // unexplored and unknown use default colour

          
                System.out.print(prefix);
                System.out.printf("%4s", symbol);
                System.out.print(suffix);
            }
            System.out.println();
        }
    }


    // Player Input
    public static int[] getMove(Player p) {
        int row, col;

        while (true) {
            row = getValidatedNumber("Latitude (1–8): ");
            col = getValidatedNumber("Longitude (1–8): ");

            if (!isCellAvailable(p, row, col)) {
                System.out.println("That cell was already chosen! Pick another.");
                continue;
            }

            return new int[]{row, col};
        }
    }


    // AI Move
    public static int[] aiMove(Player p) {
        Random r = new Random();

        int x, y;
        do {
            x = r.nextInt(8);
            y = r.nextInt(8);
        } while (p.board[x][y] != -1); // Avoid repeats

        return new int[]{x, y};
    }

    // Check Hit
    public static int checkHit(int[] move, Player p) {

        // power-ups
        if (move[0] == p.powerUps[0][0] && move[1] == p.powerUps[0][1])
            return 7;

        // booby trap
        if (move[0] == p.traps[0][0] && move[1] == p.traps[0][1])
            return 8;

        // creatures
        for (int c = 0; c < p.creatures.length; c++) {
            if (p.creatures[c][0] == move[0] &&
                p.creatures[c][1] == move[1])
                return c + 1;
        }
        return -1;
    }

    // Hints
    public static void giveHint(int[] move, int[][] creatures) {
        int lat = 0, lon = 0;

        for (int[] c : creatures) {
            if (c[0] == move[0]) lat++;
            if (c[1] == move[1]) lon++;
        }

        System.out.printf(BLUE + """

        Hint:
        Latitude %d -> %d creatures
        Longitude %d -> %d creatures

        """ + RESET, move[0] + 1, lat, move[1] + 1, lon);
    }
    public void saveGame(Player p1, Player p2, boolean vsAI, Player current) {
        LoadSave.saveFullGame(p1, p2, vsAI, current);
    }

    public void loadGame() {
        GameState state = LoadSave.loadFullGame();

        if (state == null) {
            System.out.println("No saved game found!");
            return;
        }

        System.out.println("\nLoaded previous game!");

        continueLoadedGame(state);
    }

    public void continueLoadedGame(GameState s) {
        Player player1 = s.p1;
        Player player2 = s.p2;
        Player current = s.current;
        boolean vsAI = s.vsAI;

        System.out.println("\n=== Game Loaded! Resuming... ===\n");
    }

      
    
    // Input Validation

	public static int getValidatedNumber(String message) {
	    int num;

	    while (true) {
	        System.out.print(message);

	        if (!input.hasNextInt()) {
	            System.out.println("Invalid input! Please enter a number between 1 and 8 (or 10 to EXIT).");
	            input.next();  // clear invalid token
	            continue;
	        }

	        num = input.nextInt();
	        
	        //exit condition
	        if (num == 10) {
	            System.out.println("Exiting game... Goodbye!");
	            System.exit(0);
	        }

	        if (num < 1 || num > 8) {
	            System.out.println("Out of range! Enter a number between 1 and 8.");
	            continue;
	        }

	        return num - 1; // convert to 0–7 for the board
	    }
	}
	public static boolean isCellAvailable(Player p, int row, int col) {
	    return p.board[row][col] == -1; // unexplored
	}
		
	}
