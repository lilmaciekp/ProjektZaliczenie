public class Plankton {
    private int energy;
    private int maxEnergy;

    public Plankton(int maxEnergy) {
        this.maxEnergy = maxEnergy;
        this.energy = 1;
    }
    public void regenerate(){
        if(energy < maxEnergy){
            energy++;
        }
    }
    public boolean isDepleted(){
        return energy <=0;
    }
    public void consume(int amount){
        if(amount < energy){
            energy -= amount;
        }else{
            energy = 0;
        }
    }
    public int getEnergy(){
        return energy;
    }
    public int getMaxEnergy(){
        return maxEnergy;
    }
}
