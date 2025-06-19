import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid {
    private Cell[][] cells;
    private int width;
    private int height;
    private List<Fish> allFish;
    private List<Fish> Herbivorous;
    private List<Fish> Carnivorous;

    public Grid(int width, int height, int planktonMaxEnergy, double planktonProbability) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = new Cell(i, j);

                if (Math.random() < planktonProbability) {
                    cell.setPlankton(new Plankton(planktonMaxEnergy));
                }
                cells[i][j] = cell;
            }
        }
        this.allFish = new ArrayList<>();
        this.Carnivorous = new ArrayList<>();
        this.Herbivorous = new ArrayList<>();
    }
    public void gatherFish() {
        allFish.clear();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = cells[i][j];
                if (!cell.isEmpty() && !cell.getFish().isDead()) {
                    allFish.add(cell.getFish());
                }
            }
        }
    }
    public List<Fish> gatherFishCarnivous() {
        if (Carnivorous != null) {
            Carnivorous.clear();
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = cells[i][j];
                if (!cell.isEmpty() && !cell.getFish().isDead() && cell.getFish() instanceof CarnivorousFish) {
                    Carnivorous.add(cell.getFish());
                }
            }
        }
        return Carnivorous;
    }
    public List<Fish> gatherFishHerbivorous() {
        if (Herbivorous != null){
            Herbivorous.clear();
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = cells[i][j];
                if (!cell.isEmpty() && !cell.getFish().isDead() && cell.getFish() instanceof HerbivorousFish) {
                    Herbivorous.add(cell.getFish());
                }
            }
        }
        return Herbivorous;
    }

    public void placeFish(Fish fish) {
        Cell cell = getCell(fish.getX(), fish.getY());
        if (cell != null && cell.isEmpty()) {
            cell.setFish(fish);
        }
    }

    public void moveFish() {
        List<Fish> CarnivorousFish = gatherFishCarnivous();
        List<Fish> HerbivorousFish = gatherFishHerbivorous();

        for(int i = 0; i < CarnivorousFish.size(); i++) {
            Fish fish = CarnivorousFish.get(i);
            if(!fish.isDead()){
                fish.move(this);
            }
        }
        for(int i = 0; i < HerbivorousFish.size(); i++) {
            Fish fish = HerbivorousFish.get(i);
            if(!fish.isDead()){
                fish.move(this);
            }
        }

    }
    public Cell getCell(int x, int y) {
        if (isValid(x, y)) {
            return cells[x][y];
        }
        return null;
    }
    public void planktonDead(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = getCell(i, j);
                if (cell.getPlankton()!=null && cell.getPlankton().isDepleted()) {
                    cell.setPlankton(null);
                }
            }
        }
    }
    public List<Cell> getAdjacentCells(int x, int y) {
        List<Cell> neighbors = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int newX = x + dx;
                int newY = y + dy;

                if (isValid(newX, newY)) {
                    neighbors.add(cells[newX][newY]);
                }
            }
        }

        return neighbors;
    }
    public Cell findEmptyNeighborCell(int x, int y) {
        List<Cell> neighbors = getAdjacentCells(x, y);
        Collections.shuffle(neighbors);
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i).isEmpty()) {
                return neighbors.get(i);
            }
        }
        return null;
    }
    private boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public List<Fish> getFishList() {
        List<Fish> list = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = cells[i][j];
                if (!cell.isEmpty() && !cell.getFish().isDead()) {
                    list.add(cell.getFish());
                }
            }
        }
        return list;
    }

}
