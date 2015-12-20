package creeperworld;

public class Driver {
    public static void main(String[] args){
        CreeperWorldMap map = new CreeperWorldMap(20,20);
        map.createTerrain();
        map.populateEmitters();
        for(int i =0; i<100; i++){
            map.update();
            if(i%10 == 0){map.displayTerrain();}
        }
    }

}
