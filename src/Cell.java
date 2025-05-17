public class Cell {
    private int x;
    private int y;
    private Fish fish;
    public Plankton plankton;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.fish = null;
        this.plankton = null;
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
    public boolean containsHerbivorousFish(){
        return fish instanceof HerbivorousFish;
    }
    public Plankton getPlankton(){
        return plankton;
    }
    public HerbivorousFish getHerbivorousFish(){
        if (fish instanceof HerbivorousFish) {
            return (HerbivorousFish) fish;
        }
        return null;
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