import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Simulation{
    public Simulation(int width, int height, int planktonMaxEnergy, double planktonProbability) {
        this.grid = new Grid(width, height, planktonMaxEnergy, planktonProbability);
        this.allFish = new ArrayList<>();
        this.temperature = 20;
        this.pollutionLevel = 0.1;
    }
    private Grid grid;
    private List<Fish> allFish;
    private int temperature;
    private double pollutionLevel;

    public void RunTurn(){
        applyEnvironmentalEffects();
        handleRandomEvents();
        regeneratePlankton();
        deleteDeadPlankton();
        grid.moveFish();
        grid.planktonDead();
        grid.gatherFish();
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
                fish.decreaseMaxAge(1);
            }
        }
    }
    public void handleRandomEvents(){
        if (allFish.isEmpty()) return;
        int rand = ThreadLocalRandom.current().nextInt(0, allFish.size());
        Fish fish = allFish.get(rand);
        allFish.get(rand).decreaseEnergy(fish.getEnergy());
        if(Math.random()<0.1){
            pollutionLevel = pollutionLevel+0.1;
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

}
