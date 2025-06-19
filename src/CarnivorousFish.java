import java.util.List;

public class CarnivorousFish extends Fish {

    public CarnivorousFish(int x, int y, int energy, int maxEnergy, int maxAge) {
        super(x, y, energy, maxEnergy, maxAge);
    }
    @Override
    public void move(Grid grid){
        Cell currentCell = grid.getCell(getX(), getY());
        Cell preyCell = detectHerbivorous(grid, currentCell);
        Cell partner = getParentCell(grid, currentCell);

        if (preyCell != null) {
            eat(preyCell, grid);
            currentCell.setFish(null);
            moveTo(preyCell);
            System.out.println("Drapieżnik zjada rybę na (" + preyCell.getX() + "," + preyCell.getY() + ")");
            return;
        }
        if (partner != null) {
            reproduce(currentCell, partner, grid);
            return;
        }
        Cell closestPrey = detectCarnivor(grid, currentCell);
        if (closestPrey != null) {
            Cell nextStep = goCloser(grid, currentCell, closestPrey);
            if (nextStep != null) {
                System.out.println("Drapieżnik idzie bliżej na "+closestPrey.getX() + "," + closestPrey.getY());
                return;
            }
        }

        moveRandomly(grid);
        increaseAge();
    }

    @Override
    public void eat(Cell cell, Grid grid) {
        Fish prey = cell.getFish();
        if (prey instanceof HerbivorousFish && !prey.isDead()) {
            eatFish(prey, cell);
        }
    }

    @Override
    public void reproduce(Cell fish1, Cell fish2, Grid grid) {
            if (fish1.getFish() instanceof CarnivorousFish && fish2.getFish() instanceof CarnivorousFish) {
                Fish parent1 = fish1.getFish();
                Fish parent2 = fish2.getFish();

                if (parent1.canReproduce() && parent2.canReproduce()) {
                    Cell freeCell = grid.findEmptyNeighborCell(fish1.getX(), fish1.getY());
                    if (freeCell != null) {
                        int babyEnergy = parent1.getEnergy() / 2;
                        int maxEnergy = parent1.getMaxEnergy();
                        int maxAge = parent1.getMaxAge();

                        CarnivorousFish baby = new CarnivorousFish(freeCell.getX(), freeCell.getY(), babyEnergy, maxEnergy, maxAge);
                        grid.placeFish(baby);

                        parent1.reduceEnergyForReproduce();
                        parent2.reduceEnergyForReproduce();

                        System.out.println("Mięsożerne się rozmnożyły na (" + freeCell.getX() + "," + freeCell.getY() + ")");
                    }
                }
            }
    }


        public Cell getParentCell(Grid grid, Cell currentCell) {
        List<Cell> neighbors = grid.getAdjacentCells(currentCell.getX(), currentCell.getY());
        for (int i = 0; i < neighbors.size(); i++) {
            Cell curr = neighbors.get(i);
            if(!curr.isEmpty() && curr.getFish() instanceof CarnivorousFish){
                Fish parentFish = curr.getFish();
                if(parentFish.canReproduce()){
                    return curr;
                }
            }
        }
        return null;
    }
    public Cell goCloser(Grid grid, Cell hunter,Cell prey) {
        int m = 0;
        int n = 0;
        if(hunter.getX()<prey.getX()){
            m = 1;
        }else if(hunter.getX()>prey.getX()){
            m = -1;
        }
        if(hunter.getY()>prey.getY()){
            n = -1;
        }else if(hunter.getY()<prey.getY()){
            n = 1;
        }
        int NewX = hunter.getX()+m;
        int NewY = hunter.getY()+n;
        Cell newCell = grid.getCell(NewX, NewY);
        if (newCell != null && newCell.isEmpty()) {
            hunter.setFish(null);
            moveTo(newCell);
            return newCell;
        }
        return null;
    }
}
