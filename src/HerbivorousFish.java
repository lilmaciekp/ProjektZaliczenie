import java.util.List;

public class HerbivorousFish extends Fish {
    public HerbivorousFish(int x, int y, int energy, int maxEnergy, int maxAge) {
        super(x, y, energy, maxEnergy, maxAge);
    }

    @Override
    public void move(Grid grid){
        Cell currentCell = grid.getCell(getX(), getY());
        Cell freePlanktonCells= freePlanktonCells(currentCell, grid);
        Cell predatorCell = detectCarnivor(grid, currentCell);
        if (predatorCell != null) {
            runAway(grid, predatorCell, currentCell);
            increaseAge();
            decreaseEnergy(1);
        } else {
            if (freePlanktonCells != null) {
                Cell target = freePlanktonCells;
                currentCell.setFish(null);
                moveTo(target);
                eatPlankton(target);
                decreaseEnergy(1);
                increaseAge();
                return;
            }

            Cell partner = getParentCell(grid, currentCell);
            if (partner != null && canReproduce()) {
                reproduce(currentCell, partner, grid);
            }

            moveRandomly(grid);
            increaseAge();
            decreaseEnergy(1);
        }
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
                Cell freeCell = grid.findEmptyNeighborCell(fish1.getX(), fish1.getY());
                if (freeCell != null) {
                    int babyEnergy = parent1.getEnergy() / 2;
                    int maxEnergy = parent1.getMaxEnergy();
                    int maxAge = parent1.getMaxAge();

                    HerbivorousFish baby = new HerbivorousFish(freeCell.getX(), freeCell.getY(), babyEnergy, maxEnergy, maxAge);
                    grid.placeFish(baby);

                    parent1.reduceEnergyForReproduce();
                    parent2.reduceEnergyForReproduce();
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
    public Cell runAway(Grid grid, Cell hunter,Cell prey) {
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
        int NewX = prey.getX()+m;
        int NewY = prey.getY()+n;
        Cell newCell = grid.getCell(NewX, NewY);
        if (newCell == null || !newCell.isEmpty()) {
            return grid.getCell(prey.getX(), prey.getY());
        }
        return newCell;
    }
}
