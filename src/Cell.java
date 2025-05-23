import java.util.ArrayList;
import java.util.List;

public class Cell {
    private int x;
    private int y;
    private Fish fish;
    private Plankton plankton;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.fish = null;
        this.plankton = null;
    }
    public List<Cell> neigbourCell(Cell cell,Grid grid) {
        List<Cell> NeigbourCells = new ArrayList<>();
        int x = cell.getX();
        int y = cell.getY();
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int newX = x + i;
                int newY = y + j;
                Cell neighbor = grid.getCell(newX, newY);
                if (neighbor != null) {
                    NeigbourCells.add(neighbor);
                }
            }
        }
        return NeigbourCells;
    }
    public boolean isEmpty() {
        return fish == null;
    }
    public Fish getFish(){
        return fish;
    }
    public void setFish(Fish fish){
        this.fish = fish;
    }
    public Plankton getPlankton(){
        return plankton;
    }
    public void setPlankton(Plankton plankton) {
        this.plankton = plankton;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

}
