import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Simulation{
    public Simulation(int width, int height, int planktonMaxEnergy, double planktonProbability) {
        this.grid = new Grid(width, height, planktonMaxEnergy, planktonProbability);
        this.allFish = new ArrayList<>();
        this.planktonMaxEnergy = planktonMaxEnergy;
        this.temperature = 10;
        this.pollutionLevel = 0.1;
    }
    private Grid grid;
    private List<Fish> allFish;
    private int planktonMaxEnergy;
    private int temperature;
    private double pollutionLevel;
    private int turn=0;
    private Statistic statistics = new Statistic();
    private ChartExporter chartExporter = new ChartExporter();
    private final int maxSpawnPerTurn = 15;
    private String season = "Wiosna";
    private int seasonRound = 0;
    private String lastEventMessage = "Brak zdarzeń.";
    private final File fishLogFile = new File("fish_log.txt");

    public void RunTurn() {
        updateSeason();
        printTurn();
        if (turn == 0) {
            runInitialSetup();
        } else {
            runFullTurn();
        }

        turn++;
    }
    private void runInitialSetup() {
        regeneratePlankton();
        deleteDeadPlankton();
        deleteDeadFish();

        spawnPlankton();
        grid.planktonDead();

        updateFishListsAndStats();
    }

    private void runFullTurn() {
        checkReproduce();
        applyEnvironmentalEffects();
        handleRandomEvents();

        regeneratePlankton();
        deleteDeadPlankton();
        deleteDeadFish();
        spawnPlankton();

        grid.moveFish();
        grid.planktonDead();

        updateFishListsAndStats();
    }

    private void updateFishListsAndStats() {
        grid.gatherFish();
        grid.gatherFishCarnivous();
        grid.gatherFishHerbivorous();

        this.allFish = grid.getFishList();

        statistics.countEverything(grid, allFish, grid.gatherFishCarnivous(), grid.gatherFishHerbivorous());
        statistics.print();
        chartExporter.addDataPoint(turn,
                statistics.getTotalHerbivorous(),
                statistics.getTotalCarnivorous());
        printFishSummary();
        endCheck(allFish);
        logFishCountToFile(turn, statistics.getTotalHerbivorous(), statistics.getTotalCarnivorous());

    }
    public void checkReproduce(){
        for (int i = 0; i < allFish.size(); i++) {
            Fish fish = allFish.get(i);
            fish.setReproduced(false);
        }
    }
    public void printTurn(){
        System.out.println("========TURA NUMER "+(turn+1)+"==========");
    }
    public void printFishSummary() {
        System.out.println("Liczba żywych ryb: " + allFish.size());
        for (Fish fish : allFish) {
            String type = fish instanceof HerbivorousFish ? "Roślinożerna" : "Mięsożerna";
            System.out.println("- " + type + " na (" + fish.getX() + "," + fish.getY() + "), energia: " + fish.getEnergy()+" wiek: "+fish.getAge());
        }
    }
    public void applyEnvironmentalEffects(){
        for (int i = 0; i<allFish.size(); i++){
            Fish fish = allFish.get(i);
            if(temperature!=10){
                fish.decreaseEnergy(1);
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
        if(Math.random()<0.015) {
            System.out.println("Ryba (" + fish.getX() + "," + fish.getY()+") "+fish+" zmarła");
            allFish.get(rand).decreaseEnergy(fish.getEnergy());
            lastEventMessage="Rybak złowił rybe na polu: "+fish.getX()+", "+fish.getY()+".";
        }
        if(Math.random()<0.05){
            pollutionLevel = pollutionLevel+0.1;
            lastEventMessage="Wylano scieki do jeziora";
        }
    }

    public void updateSeason() {
        seasonRound++;
        if (seasonRound >= 5) {
            seasonRound = 0;

            switch (season) {
                case "Wiosna":
                    season = "Lato";
                    temperature = 20;
                    System.out.println("🌞 Nadeszło lato! Temperatura: " + temperature + "°C");
                    break;
                case "Lato":
                    season = "Jesień";
                    temperature = 10;
                    System.out.println("🍂 Nadeszła jesień! Temperatura: " + temperature + "°C");
                    break;
                case "Jesień":
                    season = "Zima";
                    temperature = 5;
                    System.out.println("❄️ Nadeszła zima! Temperatura: " + temperature + "°C");
                    break;
                case "Zima":
                    season = "Wiosna";
                    temperature = 10;
                    System.out.println("🌱 Nadeszła wiosna! Temperatura: " + temperature + "°C");
                    break;
            }
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
        if (fishList.isEmpty() || statistics.getTotalHerbivorous()==0 || statistics.getTotalCarnivorous() == 0) {
            chartExporter.exportChart("populacja_ryb.png");
            System.exit(0);
            return;
        }
    }
    public void stopSimulation() {
        chartExporter.exportChart("populacja_ryb.png");
        System.exit(0);
    }
    private void logFishCountToFile(int round, int herbivores, int carnivores) {
        try (FileWriter fw = new FileWriter(fishLogFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(round + "," + herbivores + "," + carnivores);
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku: " + e.getMessage());
        }
    }
    public int getTemperature(){
        return temperature;
    }
    public double getPollutionLevel(){
        return pollutionLevel;
    }
    public String getSeason() {
        return season;
    }
    public String getLastEventMessage() {
        return lastEventMessage;
    }
}
