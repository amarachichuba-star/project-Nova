package Game;

public class GameState {
    public Player p1, p2, current;
    public boolean vsAI;

    public GameState(Player p1, Player p2, Player current, boolean vsAI) {
        this.p1 = p1;
        this.p2 = p2;
        this.current = current;
        this.vsAI = vsAI;
    }
}
