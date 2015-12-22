package creeperworld;

public class CreeperWorldModelFacade {
    private Thread gameThread;
    private boolean theadPaused = false;
    private CreeperWorldGame game;
    private CreeperWorldView view;
    
    private int FRAME_DURATION = 200;
    
    public CreeperWorldModelFacade(CreeperWorldView CWV) {
        view = CWV;
        game = new CreeperWorldGame(this);
        
        
        gameThread = new Thread("gameThread"){
            public void run(){
                while(true){
                    try {
                        while(theadPaused){
                            sleep(0);
                        }
                        sleep(FRAME_DURATION);
                        game.stepForward();
                        update();
                        }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        };
        gameThread.start();
    }
    public void update() {
        view.updateMap(game.getMap());
    }
    public void play() {
        theadPaused = false;
    }

    public void pause() {
        theadPaused = true;
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
    public void setFrameDuration(int fs){
        FRAME_DURATION = fs;
    }
}

