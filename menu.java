package Game;

import java.util.Scanner;
import javax.swing.JOptionPane;

public class menu {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        menu menu = new menu();
        char Menu;

        do {
            Menu = JOptionPane.showInputDialog("Menu\n"
                    + "1. Start a New Game\n"
                    + "2. Load a Previous Game\n"
                    + "3. View Help\n"
                    + "4. Exit\n").charAt(0);

            switch (Menu) {

                case '1':
                    MainGame game = new MainGame();
                    game.play();
                    break;

                case '2':
                    menu.previousGame();   // FIXED
                    break;

                case '3':
                    menu.help();
                    break;

                case '4':
                    System.out.println("Exiting... till next time 👋");
                    break;

                default:
                    System.out.println("Please choose a valid option 😇");
            }

        } while (Menu != '4');
    }

    public void help() {
		System.out.println("""
			    ---------------------------------------------------------------------------
			    Project Nova is an ocean-exploration puzzle game
			    where you play as a mystical deep-sea explorer on an important mission
			    to rescue the last surviving mystical underwater species. The ocean is 
			    represented as a two-dimensional grid, where each cell corresponds to a 
			    specific [latitude, longitude] coordinate. Hidden somewhere within 
			    this grid are a variety of creatures — but you cannot see them directly. 
			    Instead, you must go on a deep dive by entering coordinates you wish to
			    investigate, such as [4, 3]. If your guess is correct, your creature will 
			    be revealed and you will earn points. Creatures are represented using 
			    simple emoji symbol. Your goal is to uncover every creature
			    before your hostile rival hunter finds them and drives them to extinction.
			    But beware, because you and your opponent are not alone.
			    In these waters are pirates, also in search of a certain treasure.
			    Can you find the treasure AND save all the creatures before the bad guys do...
			    ---------------------------------------------------------------------------
			    """);

        MainGame game = new MainGame();
        game.play();
    }

    // FIXED VERSION — no parameters
    public void previousGame() {
        MainGame game = new MainGame();
        game.loadGame();
    }
}
