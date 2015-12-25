package creeperworld;

public class AntiEmitter {

    private int xCoord;
    private int yCoord;
    private int emitAmmount;
    private double emitProbability;
    public AntiEmitter(int x, int y){
        setxCoord(x);
        setyCoord(y);
    }
    public int getxCoord() {
        return xCoord;
    }
    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }
    public int getyCoord() {
        return yCoord;
    }
    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }
    public int getEmitAmmount() {
        return emitAmmount;
    }
    public void setEmitAmmount(int emitAmmount) {
        this.emitAmmount = emitAmmount;
    }
    public double getEmitProbability() {
        return emitProbability;
    }
    public void setEmitProbability(double emitProbibility) {
        this.emitProbability = emitProbibility;
    }
    
}
