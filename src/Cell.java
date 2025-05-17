public class Cell {
    private int x;
    private int y;
    private Fish fish;

    public Plankton plankton;

    public boolean containsFish(){
        return fish != null;
    }
    public Fish getFish(){
        return fish;
    }
    public boolean containsHerbivorousFish(){
        return false;
    }
    public HerbivorousFish getHerbivorousFish(){
        return null;
    }
    public boolean isEmpty(){
        return false;
    }
    public void setFish(Fish fish){
        this.fish = fish;
    }
}
