package creeperworld;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CreeperWorldMap {
    private int height = 0;
    private int width = 0;
    private int[][] terrain;
    public CreeperWorldMap(int h, int w){
        height = h;
        width = w;
        terrain = new int[h][w];
        for(int i = 0; i<terrain.length; i++){
            for(int j = 0; j<terrain[0].length; j++){
                terrain[i][j] = 3;
            }    
        }
        createTerrain();
        displayTerrain();
        printTerrain();
    }
    public void createTerrain(){
        int numMountains = calculateNumMountains();
        int mountainHeight;
        
        int numPlateus = calculateNumPlateus();
        int plateuRadius;
        int plateuHeight;
        
        int numFlatTopMountains = calculateNumFlatTopMountains();
        int flatTopMountainRadius;
        int flatTopMountainHeight;
        
        int numCraters = calculateNumCraters();
        int craterDepth;
        
        int numFlatBottomCraters = calculateNumFlatBottomCraters();
        int flatBottomCraterDepth;
        int flatBottomCraterRadius;
        
        for(int i = 0; i<numMountains; i++){
            mountainHeight = randomNumberInclusive(4,9);
            addMountain(mountainHeight);
        }for(int i = 0; i<numPlateus; i++){
            plateuRadius = randomNumberInclusive(3,6);
            plateuHeight = randomNumberInclusive(4,9);
            addPlateu(plateuRadius,plateuHeight);
        }
        for(int i = 0; i<numFlatTopMountains; i++){
            flatTopMountainRadius = randomNumberInclusive(3,6);
            flatTopMountainHeight = randomNumberInclusive(4,9);
            addFlatTopMountain(flatTopMountainRadius,flatTopMountainHeight);
        }
        for(int i = 0; i<numCraters; i++){
            craterDepth = randomNumberInclusive(1,5);
            craterDepth = 6;
            addCrater(craterDepth);
        }
        for(int i = 0; i<numFlatBottomCraters; i++){
            flatBottomCraterDepth = randomNumberInclusive(2,5);
            flatBottomCraterRadius = randomNumberInclusive(3,5);
            addFlatBottomCrater(flatBottomCraterDepth,flatBottomCraterRadius);
        }        
    }
    public int[][] getTerrain(){
        return terrain;
    }
    public void setTerrain(int[][] t){
        terrain = t;
    }
    private void addMountain(int mtnHeight){
        if(mtnHeight>9 || mtnHeight<1){return;}
        int x = randomNumberInclusive(0,width-1);
        int y = randomNumberInclusive(0,height-1);
        System.out.println("Mountain centered at "+x+","+y+". Height = "+mtnHeight);
        if(terrain[y][x]<mtnHeight){terrain[y][x] = mtnHeight;}
        for(int radius = 1;radius<mtnHeight;radius++){
            for(int i =x-radius+1; i<x+radius; i++){
                if(inTerrainBounds(y+radius,i)&&terrain[y+radius][i]<mtnHeight-radius){terrain[y+radius][i] = mtnHeight-radius;}
                if(inTerrainBounds(y-radius,i)&&terrain[y-radius][i]<mtnHeight-radius){terrain[y-radius][i] = mtnHeight-radius;}
            }
            for(int i =y-radius; i<=y+radius; i++){
                if(inTerrainBounds(i,x+radius)&&terrain[i][x+radius]<mtnHeight-radius){terrain[i][x+radius] = mtnHeight-radius;}
                if(inTerrainBounds(i,x-radius)&&terrain[i][x-radius]<mtnHeight-radius){terrain[i][x-radius] = mtnHeight-radius;}
            }
        }
    }
    private void addPlateu(int pRadius, int pHeight) {
        if(pHeight>9 || pHeight<1 || pRadius<1){return;}
        int x = randomNumberInclusive(0,width-1);
        int y = randomNumberInclusive(0,height-1);
        System.out.println("Plateu centered at "+x+","+y+". Height = "+pHeight+" Radius = "+pRadius);
        for(int i =0; i<pRadius; i++){
            for(int j =0; j<pRadius; j++){
                if(inTerrainBounds(y+i,x+j)){terrain[y+i][x+j] = pHeight;}
                if(inTerrainBounds(y+i,x-j)){terrain[y+i][x-j] = pHeight;}
                if(inTerrainBounds(y-i,x+j)){terrain[y-i][x+j] = pHeight;}
                if(inTerrainBounds(y-i,x-j)){terrain[y-i][x-j] = pHeight;}
            }
        }
    }
    private void addFlatTopMountain(int topRadius, int mtnHeight){
        if(mtnHeight>9 || mtnHeight<1){return;}
        int x = randomNumberInclusive(0,width-1);
        int y = randomNumberInclusive(0,height-1);
        System.out.println("FTM centered at "+x+","+y+". Height = "+mtnHeight+" Radius = "+topRadius);
        
        //flat top
        for(int i =0; i<topRadius; i++){
            for(int j =0; j<topRadius; j++){
                if(inTerrainBounds(y+i,x+j)&&terrain[y+i][x+j]<mtnHeight){terrain[y+i][x+j] = mtnHeight;}
                if(inTerrainBounds(y+i,x-j)&&terrain[y+i][x-j]<mtnHeight){terrain[y+i][x-j] = mtnHeight;}
                if(inTerrainBounds(y-i,x+j)&&terrain[y-i][x+j]<mtnHeight){terrain[y-i][x+j] = mtnHeight;}
                if(inTerrainBounds(y-i,x-j)&&terrain[y-i][x-j]<mtnHeight){terrain[y-i][x-j] = mtnHeight;}
            }
        }
        
        
        //slope of mountain
        for(int radius = topRadius;radius<mtnHeight+topRadius;radius++){
            for(int i =x-radius+1; i<x+radius; i++){
                if(inTerrainBounds(y+radius,i)&&terrain[y+radius][i]<mtnHeight-radius+topRadius){terrain[y+radius][i] = mtnHeight-radius+topRadius;}
                if(inTerrainBounds(y-radius,i)&&terrain[y-radius][i]<mtnHeight-radius+topRadius){terrain[y-radius][i] = mtnHeight-radius+topRadius;}
            }
            for(int i =y-radius; i<=y+radius; i++){
                if(inTerrainBounds(i,x+radius)&&terrain[i][x+radius]<mtnHeight-radius+topRadius){terrain[i][x+radius] = mtnHeight-radius+topRadius;}
                if(inTerrainBounds(i,x-radius)&&terrain[i][x-radius]<mtnHeight-radius+topRadius){terrain[i][x-radius] = mtnHeight-radius+topRadius;}
            }
        }
    }
    private void addCrater(int craterDepth){
        if(craterDepth>9 || craterDepth<1){return;}
        int x = randomNumberInclusive(0,width-1);
        int y = randomNumberInclusive(0,height-1);
        System.out.println("Crater centered at "+x+","+y+". Depth = "+craterDepth);
        terrain[y][x] -= craterDepth;
        for(int radius = 1;radius<craterDepth;radius++){
            for(int i =x-radius+1; i<x+radius; i++){
                if(inTerrainBounds(y+radius,i)){terrain[y+radius][i] -= craterDepth-radius;}
                if(inTerrainBounds(y-radius,i)){terrain[y-radius][i] -= craterDepth-radius;}
            }
            for(int i =y-radius; i<=y+radius; i++){
                if(inTerrainBounds(i,x+radius)){terrain[i][x+radius] -= craterDepth-radius;}
                if(inTerrainBounds(i,x-radius)){terrain[i][x-radius] -= craterDepth-radius;}
            }
        }
        for(int i = 0; i<terrain.length; i++){
            for(int j = 0; j<terrain.length; j++){
                if(terrain[i][j]<1){terrain[i][j] = 1;}
            }
        }
    }
    private void addFlatBottomCrater(int craterDepth,int bottomRadius){
        if(craterDepth>9 || craterDepth<1){return;}
        int x = randomNumberInclusive(0,width-1);
        int y = randomNumberInclusive(0,height-1);
        System.out.println("FBC centered at "+x+","+y+". Depth = "+craterDepth+" Radius = "+bottomRadius);
        //flat bottom
        for(int i =0; i<bottomRadius; i++){
            for(int j =0; j<bottomRadius; j++){
                if(inTerrainBounds(y+i,x+j)){terrain[y+i][x+j] = 9-craterDepth;}
                if(inTerrainBounds(y+i,x-j)){terrain[y+i][x-j] = 9-craterDepth;}
                if(inTerrainBounds(y-i,x+j)){terrain[y-i][x+j] = 9-craterDepth;}
                if(inTerrainBounds(y-i,x-j)){terrain[y-i][x-j] = 9-craterDepth;}
            }
        }
        
        //slope of crater
        for(int radius = bottomRadius;radius<craterDepth+bottomRadius;radius++){
            for(int i =x-radius+1; i<x+radius; i++){
                if(inTerrainBounds(y+radius,i)){terrain[y+radius][i] -= craterDepth-radius+bottomRadius;}
                if(inTerrainBounds(y-radius,i)){terrain[y-radius][i] -= craterDepth-radius+bottomRadius;}
            }
            for(int i =y-radius; i<=y+radius; i++){
                if(inTerrainBounds(i,x+radius)){terrain[i][x+radius] -= craterDepth-radius+bottomRadius;}
                if(inTerrainBounds(i,x-radius)){terrain[i][x-radius] -= craterDepth-radius+bottomRadius;}
            }
        }
        for(int i = 0; i<terrain.length; i++){
            for(int j = 0; j<terrain.length; j++){
                if(terrain[i][j]<1){terrain[i][j] = 1;}
            }
        }
    }
    private int calculateNumCraters() {
        return randomNumberInclusive(1,3);
    }
    private int calculateNumMountains() {
        return randomNumberInclusive(1,3);
    }
    private int calculateNumPlateus() {
        return randomNumberInclusive(1,3);
    }
    private int calculateNumFlatTopMountains(){
        return randomNumberInclusive(1,3);
    }
    private int calculateNumFlatBottomCraters(){
        return randomNumberInclusive(1,3);
    }
    private boolean inTerrainBounds(int x, int y){
        boolean inbounds = true;
        if(x<0 || x>=terrain.length){inbounds = false;}
        if(y<0 || y>=terrain[0].length){inbounds = false;}
        return inbounds;
    }
    public void printTerrain(){
        for(int i = 0; i<terrain.length; i++){
            for(int j = 0; j<terrain[0].length; j++){
                System.out.print(terrain[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    public void displayTerrain(){
        JFrame myFrame = new JFrame();
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel gridPanel = new JPanel(new GridLayout(height,width));
        JPanel[][] thePanels = new JPanel[height][width];
        for(int i = 0; i<height; i++){
            for(int j = 0; j<width; j++){
                thePanels[i][j] = new JPanel();
                thePanels[i][j].setBackground(getMapColor(terrain[i][j]));
                thePanels[i][j].setBorder(BorderFactory.createLineBorder(Color.white));
                gridPanel.add(thePanels[i][j]);
            }
        }
        myFrame.add(gridPanel);
        myFrame.setBounds(0,0,700,700);
        myFrame.setVisible(true);
        
    }
    private Color getMapColor(int myInt){
        return new Color(0,0,myInt*20);
//        switch(myInt){
//            case 1:
//                return new Color(0,0,20);
//            case 2:
//                return new Color(0,0,20);
//            case 3:
//                return new Color(0,0,20);
//            case 4:
//                return new Color(0,0,20);
//            case 5:
//                return new Color(0,0,20);
//            case 6:
//                return new Color(0,0,20);
//            case 7:
//                return new Color(0,0,20);
//            case 8:
//                return new Color(0,0,20);
//            case 9:
//                return new Color(0,0,20);
//            default:
//                return new Color(0,0,20);
//        }
    }
    private int randomNumberInclusive(int min,int max){
        return (int)((Math.random()*(max-min+1))+min);
    }
}
