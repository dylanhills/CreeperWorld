package creeperworld;

public class Driver {
    public static void main(String[] args){
        CreeperWorldMap map = new CreeperWorldMap(50,50);
        map.createTerrain();
        map.populateEmitters();
        //map.printTerrain();
        //map.displayTerrain();
        for(int i =0; i<100; i++){
            map.update();
            if(i%5 == 0){map.displayTerrain();}
        }
    }

}
