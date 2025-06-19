import java.util.List;

public class HerbivorousFish extends Fish {
    public HerbivorousFish(int x, int y, int energy, int maxEnergy, int maxAge) {
        super(x, y, energy, maxEnergy, maxAge);
    }

    @Override
    public void move(Grid grid){

        Cell currentCell = grid.getCell(getX(), getY());

        Cell partner = getParentCell(grid, currentCell);

        if (partner != null && Math.random() < 0.7) {
            reproduce(currentCell, partner, grid);
            increaseAge();
            decreaseEnergy(1);
            return;
        }

        Cell predatorCell = detectCarnivor(grid, currentCell);
        if (predatorCell != null) {
            currentCell.setFish(null);
            moveRandomly(grid);
            increaseAge();
            decreaseEnergy(1);
            return;
        }

        Cell food = freePlanktonCells(currentCell, grid);
        if (food != null) {
            currentCell.setFish(null);
            moveTo(food);
            eatPlankton(food);
            decreaseEnergy(1);
            increaseAge();
            return;
        }

        moveRandomly(grid);
        increaseAge();
        decreaseEnergy(1);
    }
    @Override
    public void eat(Cell cell, Grid grid) {
        if(cell.getPlankton()!=null && !cell.getPlankton().isDepleted()){
            eatPlankton(cell);
        }
    }
    @Override
    public void reproduce(Cell fish1, Cell fish2, Grid grid) {

        if(fish1.getFish() instanceof HerbivorousFish && fish2.getFish() instanceof HerbivorousFish){
            Fish parent1 = fish1.getFish();
            Fish parent2 = fish2.getFish();

            if(parent1.canReproduce() && parent2.canReproduce()){
                System.out.println("Ryby na : ("+fish1.getX()+","+fish1.getY()+") i ("+fish2.getX()+","+fish2.getY()+") " + this.getClass().getSimpleName()+" rozmznożyły się");
                Cell freeCell = grid.findEmptyNeighborCell(fish1.getX(), fish1.getY());
                if (freeCell != null) {
                    int maxEnergy = parent1.getMaxEnergy();
                    int maxAge = parent1.getMaxAge();

                    HerbivorousFish baby = new HerbivorousFish(freeCell.getX(), freeCell.getY(), maxEnergy, maxEnergy, maxAge);
                    grid.placeFish(baby);
                    System.out.println("Dziecko na: ("+baby.getX()+","+baby.getY()+")");
                    parent1.reduceEnergyForReproduce();
                    parent2.reduceEnergyForReproduce();

                    if (!parent1.hasReproduced() && !parent2.hasReproduced()) {
                        parent1.setReproduced(true);
                        parent2.setReproduced(true);
                    }
                }
            }
        }
    }
    public Cell getParentCell(Grid grid, Cell currentCell) {
        List<Cell> neighbors = grid.getAdjacentCells(currentCell.getX(), currentCell.getY());
        for (int i = 0; i < neighbors.size(); i++) {
            Cell curr = neighbors.get(i);
            if(!curr.isEmpty() && curr.getFish() instanceof HerbivorousFish){
                Fish parentFish = curr.getFish();
                if(parentFish.canReproduce()){
                    return curr;
                }
            }
        }
        return null;
    }
}
