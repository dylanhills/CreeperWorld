package creeperworld;

//import esof322.a2.CreeperWorld;
//import esof322.a2.CreeperWorldView;
//import esof322.a2.InputListener;
public class CreeperWorldModelFacade {
    
    private CreeperWorldGame game;
    private CreeperWorldView view;
    
    CreeperWorldModelFacade(CreeperWorldView CWV) { // we initialize
        view = CWV;
        game = new CreeperWorldGame(this);
    }
    public void update() {
        //listener.send("u");
        view.updateMap(game.getMap());
    }
    public void start() {
        game.start();
        
    }
    public void play() {
        // TODO Auto-generated method stub
        
    }

    public void pause() {
        // TODO Auto-generated method stub
        
    }
    public void stepForward(){
        game.stepForward();
        update();
    }
    public void restart() {
        game.restartMap();
        update();
    }

    public void newMap() {
        game.newMap();
        update();
    }
}

