import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(20, 20, 20, 0.3);
        Grid grid = simulation.getGrid();

        grid.placeFish(new HerbivorousFish(2, 3, 50, 100, 25));
        grid.placeFish(new HerbivorousFish(2, 4, 50, 100, 25));
        grid.placeFish(new HerbivorousFish(2, 5, 50, 100, 25));


        grid.placeFish(new CarnivorousFish(3, 4, 60, 200, 50));
        grid.placeFish(new CarnivorousFish(3, 5, 52, 200, 50));


        for (int n = 0; n < 5; n++) {
            System.out.println("=== Tura " + (n + 1) + " ===");
            simulation.RunTurn();
        }
    }
}
