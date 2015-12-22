package creeperworld;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JPanel;
public class CreeperWorldGame{

    private CreeperWorldModelFacade model;
    
    //private BufferedReader keyboard;
    
    private CreeperWorldMap map;
    
    public CreeperWorldGame(CreeperWorldModelFacade model) {
        this.model = model;
        //keyboard = new BufferedReader(new InputStreamReader(System.in));
    }
    public JPanel getMap(){
        return map.getMap();
    }
    public void newMap(){
        map = new CreeperWorldMap(50,50);
    }
    public void restartMap(){
        map.restart();
    }
    public void stepForward(){
        map.update();
    }
}

