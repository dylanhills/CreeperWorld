package creeperworld;

public class Turret {
    private int ammo;
    private int health;
    private int XCoord;
    private int YCoord;
    private int coolDownTime;
    private int fireRange;
    private String turretType;
    private int currentCoolDownTime;

    public Turret(int x, int y) {
        XCoord = x;
        YCoord = y;
        fireRange = 5;
        coolDownTime = 10;
        currentCoolDownTime = 0;
        setTurretType("Mortar");
    }
    public void coolDown(){
        currentCoolDownTime --;
    }
    public int getAmmo() {
        return ammo;
    }
    public int getCoolDownTime() {
        return coolDownTime;
    }
    public int getCurrentCoolDownTime(){
        return currentCoolDownTime;
    }
    public int getFireRange() {
        return fireRange;
    }
    public int getHealth() {
        return health;
    }
    public String getTurretType() {
        return turretType;
    }
    public int getXCoord() {
        return XCoord;
    }
    public int getYCoord() {
        return YCoord;
    }
    public void resetCoolDown(){
        currentCoolDownTime = coolDownTime;
    }
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }
    public void setCoolDownTime(int coolDownTime) {
        this.coolDownTime = coolDownTime;
    }
    public void setFireRange(int fireRange) {
        this.fireRange = fireRange;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setTurretType(String turretType) {
        this.turretType = turretType;
    }
    public void setXCoord(int xCoord) {
        XCoord = xCoord;
    }
    public void setYCoord(int yCoord) {
        YCoord = yCoord;
    }
}