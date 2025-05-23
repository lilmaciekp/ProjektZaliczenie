import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Fish{
    private int x;
    private int y;
    private int energy;
    private int maxEnergy;
    private int age;
    private int maxAge;

    public Fish(int x, int y, int energy, int maxEnergy, int maxAge) {
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        this.maxAge = maxAge;
        this.age = 1;
        this.reproduce = null;
    }


    public void moveTo(Cell cell){
        this.x = cell.getX();
        this.y = cell.getY();
        cell.setFish(this);
    }
    public void moveRandomly(Grid grid){
        Cell curCell = grid.getCell(this.x, this.y);
        Cell nextCell = grid.findEmptyNeighborCell(x, y);
        if(nextCell != null){
            curCell.setFish(null);
            moveTo(nextCell);
        }
    }
    public abstract void move(Grid grid);
    public abstract void eat(Cell cell, Grid grid);
    public void eatFish(Fish prey, Cell cell){
        int fishEnergy = prey.getEnergy();
        increaseEnergy(fishEnergy);
        prey.decreaseEnergy(fishEnergy);
        if(prey.isDead()){
            cell.setFish(null);
        }
    }
    public void increaseEnergy(int amount){
        energy = Math.min(maxEnergy, energy + amount);
    }
    public void eatPlankton(Cell cell) {
            int planktonEnergy = cell.getPlankton().getEnergy();
            cell.getPlankton().consume(planktonEnergy);
            increaseEnergy(planktonEnergy);
            if(cell.getPlankton().isDepleted()){
                cell.setPlankton(null);
            }
    }
    public boolean canEatC(Cell cell,Grid grid){
        List<Cell> NeigbourCells = grid.getAdjacentCells(cell.getX(), cell.getY());
        for (int i = 0; i < NeigbourCells.size(); i++) {
            Cell curCell = NeigbourCells.get(i);
            if (!curCell.isEmpty() && curCell.getFish() instanceof HerbivorousFish && !curCell.getFish().isDead()){
                return true;
            }
        }
        return false;
    }
    public Cell freePlanktonCells(Cell cell, Grid grid) {
        List<Cell> NeigbourCells = grid.getAdjacentCells(cell.getX(), cell.getY());
        for (int i = 0; i < NeigbourCells.size(); i++) {
            Cell curCell = NeigbourCells.get(i);
            if (curCell.isEmpty() && curCell.getPlankton()!=null && !curCell.getPlankton().isDepleted()) {
                return curCell;
            }
        }
        return null;
    }



    // Detecty

    public Cell detectCarnivor(Grid grid,Cell cell) {
        List<Cell> NeigbourCells = cell.neigbourCell(cell, grid);
        for(int i = 0; i < NeigbourCells.size(); i++){
            Cell curCell = NeigbourCells.get(i);
            if(!curCell.isEmpty() && curCell.getFish() instanceof CarnivorousFish){
                return curCell;
            }
        }
        return null;
    }
    public Cell detectHerbivorous(Grid grid,Cell cell) {
        List<Cell> Cells = grid.getAdjacentCells(cell.getX(), cell.getY());
        for(int i = 0; i < Cells.size(); i++){
            Cell curCell = Cells.get(i);
            if(!curCell.isEmpty() && curCell.getFish() instanceof HerbivorousFish){
                return curCell;
            }
        }
        return null;
    }


    public boolean canReproduce(){
        return energy > maxEnergy/2 && age > 10;
    }
    public void reduceEnergyForReproduce(){
        energy/=2;
    }
    public abstract void reproduce(Cell fish1, Cell fish2, Grid grid);
    public void increaseAge(){
        age+=0.5;
    }
    public void decreaseEnergy(int amount){
        energy-=amount;
    }
    public Fish reproduce;
    public boolean isDead(){
        return energy <=0 || age >= maxAge;
    }
    public void decreaseMaxAge(int amount) {
        maxAge = Math.max(1, maxAge - amount);
    }


    // Gettery


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getEnergy() {
        return energy;
    }
    public int getAge() {
        return age;
    }
    public int getMaxAge() {
        return maxAge;
    }
    public int getMaxEnergy() {
        return maxEnergy;
    }

}
