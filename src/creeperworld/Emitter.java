package creeperworld;

public class Emitter {
    private int xCoord;
    private int yCoord;
    private int emitAmmount;
    public Emitter(int x, int y){
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
    
}
