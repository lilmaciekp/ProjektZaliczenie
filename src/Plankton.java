public class Plankton {
    private int energy;
    private int maxEnergy;

    public Plankton(int maxEnergy) {
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }
    public void regenerate(){
        if(energy < maxEnergy){
            energy++;
        }
    }
    public boolean isDepleated(){
        return energy <=0;
    }
    public void consume(int amount){
        energy = Math.max(0, energy - amount);
    }
    public int getEnergy(){
        return energy;
    }
    public int getMaxEnergy(){
        return maxEnergy;
    }
}
