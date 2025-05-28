import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Simulation{
    public Simulation(int width, int height, int planktonMaxEnergy, double planktonProbability) {
        this.grid = new Grid(width, height, planktonMaxEnergy, planktonProbability);
        this.allFish = new ArrayList<>();
        this.planktonMaxEnergy = planktonMaxEnergy;
        this.temperature = 20;
        this.pollutionLevel = 0.1;
    }

    private Grid grid;
    private List<Fish> allFish;
    private int planktonMaxEnergy;
    private int temperature;
    private double pollutionLevel;
    private int turn=0;
    private Statistic statistics = new Statistic();
    private final int maxSpawnPerTurn = 2;
    public void RunTurn(){
        if(turn!=0) {
            checkReproduce();
        }
        applyEnvironmentalEffects();
        handleRandomEvents();
        regeneratePlankton();
        deleteDeadPlankton();
        deleteDeadFish();
        spawnPlankton();
        grid.moveFish();
        grid.planktonDead();
        grid.gatherFish();
        this.allFish = grid.getFishList();
        statistics.countEverything(grid, grid.getFishList());
        statistics.print();
        printFishSummary();
        endCheck(allFish);
        turn++;
    }

    public void checkReproduce(){
        for (int i = 0; i < allFish.size(); i++) {
            Fish fish = allFish.get(i);
            fish.setReproduced(false);
        }
    }
    public void printFishSummary() {
        System.out.println("Liczba żywych ryb: " + allFish.size());
        for (Fish fish : allFish) {
            String type = fish instanceof HerbivorousFish ? "Roślinożerna" : "Mięsożerna";
            System.out.println("- " + type + " na (" + fish.getX() + "," + fish.getY() + "), energia: " + fish.getEnergy());
        }
    }
    public void applyEnvironmentalEffects(){
        for (int i = 0; i<allFish.size(); i++){
            Fish fish = allFish.get(i);
            if(temperature<10 || temperature>30){
                fish.decreaseEnergy(2);
            }
        }
        if(pollutionLevel>0.5){
            for (int i = 0; i<allFish.size(); i++){
                Fish fish = allFish.get(i);
                fish.decreaseEnergy(1);
            }
        }
    }
    public void handleRandomEvents(){
        if (allFish.isEmpty()) return;
        int rand = ThreadLocalRandom.current().nextInt(0, allFish.size());
        Fish fish = allFish.get(rand);
        if(Math.random()<0.05) {
            System.out.println("Ryba " + fish.getX() + "," + fish.getY()+" "+fish+" zmarła");
            allFish.get(rand).decreaseEnergy(fish.getEnergy());
        }
        if(Math.random()<0.1){
            pollutionLevel = pollutionLevel+0.1;
        }
    }
    public void deleteDeadFish() {
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Cell cell = grid.getCell(x, y);
                if (!cell.isEmpty() && cell.getFish().isDead()) {
                    cell.setFish(null);
                }
            }
        }
    }
    public void deleteDeadPlankton(){
        for(int i = 0; i < grid.getWidth();i++){
            for(int j = 0; j < grid.getHeight(); j++){
                Cell currCell = grid.getCell(i, j);
                if(currCell.getPlankton() != null && currCell.getPlankton().isDepleted()){
                    currCell.setPlankton(null);
                }
            }
        }
    }
    public void regeneratePlankton(){
        for(int i = 0; i<grid.getWidth(); i++){
            for(int j = 0; j<grid.getHeight(); j++){
                Cell currCell = grid.getCell(i, j);
                if(currCell.getPlankton() != null){
                    currCell.getPlankton().regenerate();
                }
            }
        }
    }
    public void spawnPlankton() {
        for (int i=0; i<maxSpawnPerTurn; i++) {
            int x = ThreadLocalRandom.current().nextInt(0, grid.getWidth());
            int y = ThreadLocalRandom.current().nextInt(0, grid.getHeight());
            Cell cell = grid.getCell(x, y);
            cell.setPlankton(new Plankton(planktonMaxEnergy));
        }
    }
    public Grid getGrid() {
        return grid;
    }
    public void endCheck(List<Fish> fishList) {
        if (fishList.isEmpty()){
            System.exit(0);
            return;
        }
    }
}
