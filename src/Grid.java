import java.util.ArrayList;
import java.util.List;

public class Grid {
    private Cell[][] cells;

    public Cell getCell(int x, int y){
        return cells[x][y];
    }
    public List<Cell> getAdjacentCells(int x, int y){
        List<Cell> adjacentCells = new ArrayList<>();
        return adjacentCells;
    }
    public List<Cell>getVisibleCellsInDirection(int x, int y , Direction dir, int range){
        List<Cell> adjacentCells = new ArrayList<>();
        return adjacentCells;
    }
    public Cell findEmptyNeighborCell(int x, int y){
        return null;
    }
}
