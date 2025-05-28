import java.util.List;

public class Statistic {
    private int totalHerbivorous = 0;
    private int totalCarnivorous = 0;
    private int totalPlankton=0;
    private int totalFish=0;
    public void countEverything(Grid grid, List<Fish> allFish) {
        totalFish = 0;
        totalHerbivorous = 0;
        totalCarnivorous = 0;
        totalPlankton = 0;

        for (Fish fish : allFish) {
            totalFish++;
            if (fish instanceof HerbivorousFish) totalHerbivorous++;
            if (fish instanceof CarnivorousFish) totalCarnivorous++;
        }

        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                if (grid.getCell(i, j).getPlankton() != null && !grid.getCell(i, j).getPlankton().isDepleted()) {
                    totalPlankton++;
                }
            }
        }
    }
    public int getTotalHerbivorous() {
        return totalHerbivorous;
    }
    public int getTotalCarnivorous() {
        return totalCarnivorous;
    }
    public int getTotalPlankton() {
        return totalPlankton;
    }
    public int getTotalFish() {
        return totalFish;
    }
    public void print() {
        System.out.println("Roślinożerne: " + totalHerbivorous);
        System.out.println("Mięsożerne: " + totalCarnivorous);
        System.out.println("Planktonów aktywnych: " + totalPlankton);
        System.out.println("Wszystkie: " + totalFish);
    }
}
