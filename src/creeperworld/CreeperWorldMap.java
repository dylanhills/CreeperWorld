package creeperworld;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreeperWorldMap{
    //global variables
    
    //the map
    private int height = 0;
    private int width = 0;
    private int[][] terrain;

    //creeper
    private double[][] creeperAmount;
    private double[][] antiCreeperAmount;
    private double totalCreeperOnMap;
    private double totalAntiCreeperOnMap;
    private final double viscosity = 20;
    private final double flowRate = .5;//probability 0.0 to 1.0
    
    
    //terrain
    
    //mountains
    private final int highNumMountains = 5;
    private final int lowNumMountains = 3;
    private final int highHeightMountains = 10;
    private final int lowHeightMountains = 6;
    
    private final int highNumFTM = 10;
    private final int lowNumFTM = 3;
    private final int highRadiusFTM = 10;
    private final int lowRadiusFTM = 4;
    private final int highHeightFTM = 10;
    private final int lowHeightFTM = 5;
    
    
    private final int highNumCraters = 10;
    private final int lowNumCraters = 3;
    private final int highDepthCraters = 10;
    private final int lowDepthCraters = 3;
    
    private final int highNumPlateu = 10;
    private final int lowNumPlateu = 3;
    private final int highRadiusPlateu = 10;
    private final int lowRadiusPlateu = 5;
    private final int highHeightPlateu = 10;
    private final int lowHeightPlateu = 5;
    
    private final int highNumFBC = 0;
    private final int lowNumFBC = 0;
    private final int highRadiusFBC = 0;
    private final int lowRadiusFBC = 0;
    private final int highDepthFBC = 0;
    private final int lowDepthFBC = 0;
    
    //emitters
    private ArrayList<Emitter> emitters = new ArrayList<Emitter>();
    private ArrayList<AntiEmitter> antiEmitters = new ArrayList<AntiEmitter>();
    private final int highNumEmitters = 7;
    private final int lowNumEmitters = 1;
    private final int highEmitAmount = 3;
    private final int lowEmitAmount = 1;
    private final int highEmitProbability = 50;
    private final int lowEmitProbability = 30;
    private final int highNumAntiEmitters = 7;
    private final int lowNumAntiEmitters = 1;
    private final int highAntiEmitAmount = 3;
    private final int lowAntiEmitAmount = 1;
    private final int highAntiEmitProbability = 50;
    private final int lowAntiEmitProbability = 30;    
    
    private ArrayList<Turret> turrets = new ArrayList<Turret>();
    
    //gui stuff
    private final float creeperColorOpacity = 0.2f;//between .3 and .8
    private final double expectedCreeperHeight = 10.0;//used for computing color of squares with creeper
    private final Color creeperColor = new Color(0,0,255);
    private final Color antiCreeperColor = new Color(255,0, 0);
    private int mouseOverX = 0;
    private int mouseOverY = 0;
    private boolean selecting = false;
    
    //turrets
    private int typeOfTurretToBuild = -1;
    public final int laserTurretCode = 0;
    public final int mortarTurretCode = 1;
    public final int otherTurretCode = 2;
    private final double turretDestroyAmount = 10;

    
    
    //constructors
    public CreeperWorldMap(){
        this(30,30);
    }

    public CreeperWorldMap(int h, int w){
            height = h;
            width = w;
            terrain = new int[h][w];
            creeperAmount = new double[h][w];
            antiCreeperAmount = new double[h][w];
    
            for(int i = 0; i<terrain.length; i++){
                for(int j = 0; j<terrain[0].length; j++){
                    terrain[i][j] = 3;
                }
            }
            createTerrain();
            populateEmitters();
            populateAntiEmitters();
        }

    private void addCrater(int craterDepth){
        if(craterDepth>9 || craterDepth<1){return;}
        int x = randomNumberInclusive(0,width-1);
        int y = randomNumberInclusive(0,height-1);
        //System.out.println("Crater centered at "+x+","+y+". Depth = "+craterDepth);
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
    //System.out.println("FBC centered at "+x+","+y+". Depth = "+craterDepth+" Radius = "+bottomRadius);
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
            if(inTerrainBounds(y+radius,i)){terrain[y+radius][i] -= 9-craterDepth-radius+bottomRadius;}
            if(inTerrainBounds(y-radius,i)){terrain[y-radius][i] -= 9-craterDepth-radius+bottomRadius;}
        }
        for(int i =y-radius; i<=y+radius; i++){
            if(inTerrainBounds(i,x+radius)){terrain[i][x+radius] -= 9-craterDepth-radius+bottomRadius;}
            if(inTerrainBounds(i,x-radius)){terrain[i][x-radius] -= 9-craterDepth-radius+bottomRadius;}
        }
    }
    for(int i = 0; i<terrain.length; i++){
        for(int j = 0; j<terrain.length; j++){
            if(terrain[i][j]<1){terrain[i][j] = 1;}
        }
    }
}
   
    private void addFlatTopMountain(int topRadius, int mtnHeight){
    if(mtnHeight>9 || mtnHeight<1){return;}
    int x = randomNumberInclusive(0,width-1);
    int y = randomNumberInclusive(0,height-1);
    //System.out.println("FTM centered at "+x+","+y+". Height = "+mtnHeight+" Radius = "+topRadius);
    
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

    public void addLaserTurret(int x, int y){
       LaserTurret lt = new LaserTurret(x,y);
       turrets.add(lt);
       setSelecting(false);
   }

    public void addMortarTurret(int x, int y){
       MortarTurret mt = new MortarTurret(x,y);
       turrets.add(mt);
       setSelecting(false);
   }
   
    private void addMountain(int mtnHeight){
        if(mtnHeight>9 || mtnHeight<1){return;}
        int x = randomNumberInclusive(0,width-1);
        int y = randomNumberInclusive(0,height-1);
        //System.out.println("Mountain centered at "+x+","+y+". Height = "+mtnHeight);
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
        //System.out.println("Plateu centered at "+x+","+y+". Height = "+pHeight+" Radius = "+pRadius);
        for(int i =0; i<pRadius; i++){
            for(int j =0; j<pRadius; j++){
                if(inTerrainBounds(y+i,x+j)){terrain[y+i][x+j] = pHeight;}
                if(inTerrainBounds(y+i,x-j)){terrain[y+i][x-j] = pHeight;}
                if(inTerrainBounds(y-i,x+j)){terrain[y-i][x+j] = pHeight;}
                if(inTerrainBounds(y-i,x-j)){terrain[y-i][x-j] = pHeight;}
            }
        }
    }

    private Color blend( Color c1, Color c2, float ratio ) {
        if ( ratio > 1f ) ratio = 1f;
        else if ( ratio < 0f ) ratio = 0f;
        float iRatio = 1.0f - ratio;

        int i1 = c1.getRGB();
        int i2 = c2.getRGB();

        int a1 = (i1 >> 24 & 0xff);
        int r1 = ((i1 & 0xff0000) >> 16);
        int g1 = ((i1 & 0xff00) >> 8);
        int b1 = (i1 & 0xff);

        int a2 = (i2 >> 24 & 0xff);
        int r2 = ((i2 & 0xff0000) >> 16);
        int g2 = ((i2 & 0xff00) >> 8);
        int b2 = (i2 & 0xff);

        int a = (int)((a1 * iRatio) + (a2 * ratio));
        int r = (int)((r1 * iRatio) + (r2 * ratio));
        int g = (int)((g1 * iRatio) + (g2 * ratio));
        int b = (int)((b1 * iRatio) + (b2 * ratio));

        return new Color( a << 24 | r << 16 | g << 8 | b );
    }

    //line of sight doesn't actually work. just ensures that the target is no higher than turret
    private int[] closestCreeperAroundLOS(int x, int y,int radius){
        //returns a point with creeper on it
        int theHeight = terrain[y][x];
        if(creeperAmount[y][x]>0){return new int[] {x,y};}
        for(int tempRadius = 1;tempRadius<radius;tempRadius++){
            for(int i =x-tempRadius+1; i<x+tempRadius; i++){
                if(inTerrainBounds(i,y+tempRadius)&&terrain[y+tempRadius][i]<=theHeight&&creeperAmount[y+tempRadius][i]>0){return new int[] {i,y+tempRadius};}
                if(inTerrainBounds(i,y-tempRadius)&&terrain[y-tempRadius][i]<=theHeight&&creeperAmount[y-tempRadius][i]>0){return new int[] {i,y-tempRadius};}
            }
            for(int i =y-tempRadius; i<=y+tempRadius; i++){
                if(inTerrainBounds(x+tempRadius,i)&&terrain[i][x+tempRadius]<=theHeight&&creeperAmount[i][x+tempRadius]>0){return new int[] {x+tempRadius,i};}
                if(inTerrainBounds(x-tempRadius,i)&&terrain[i][x-tempRadius]<=theHeight&&creeperAmount[i][x-tempRadius]>0){return new int[] {x-tempRadius,i};}
            }
        }
        return new int[] {-1,-1};
    }

    private double computeCreeperColorRatio(double depth){
        double ratio =  depth/expectedCreeperHeight;

        if(ratio<.1){ratio = .4+creeperColorOpacity;}
        else if(ratio<.2){ratio = .45+creeperColorOpacity;}
        else if(ratio<.3){ratio = .5+creeperColorOpacity;}
        else if(ratio<.4){ratio = .55+creeperColorOpacity;}
        else if(ratio<.5){ratio = .6+creeperColorOpacity;}
        else if(ratio<.6){ratio = .65+creeperColorOpacity;}
        else if(ratio<.7){ratio = .7+creeperColorOpacity;}
        else{ratio = 1.0;}
        return ratio;
    }

    private double computeCreeperHeightAverage(int a, int b,double cA[][]){
        double avg = cA[a][b];
        int num = 1;
        if(inTerrainBounds(a-1,b)){avg = avg+cA[a-1][b];num++;}
        if(inTerrainBounds(a+1,b)){avg = avg+cA[a+1][b];num++;}
        if(inTerrainBounds(a-1,b+1)){avg = avg+cA[a-1][b+1];num++;}
        if(inTerrainBounds(a+1,b+1)){avg = avg+cA[a+1][b+1];num++;}
        if(inTerrainBounds(a,b+1)){avg = avg+cA[a][b+1];num++;}
        if(inTerrainBounds(a-1,b-1)){avg = avg+cA[a-1][b-1];num++;}
        if(inTerrainBounds(a+1,b-1)){avg = avg+cA[a+1][b-1];num++;}
        if(inTerrainBounds(a,b-1)){avg = avg+cA[a][b-1];num++;}
        avg = avg/num;
        return avg;
    }

    private Color computeMapColor(int myInt){
        //return new Color(0,0,myInt*20);
        switch(myInt){
            case 1:
                return new Color(51, 26, 0);
            case 2:
                return new Color(102, 51, 0);
            case 3:
                return new Color(204, 153, 0);
            case 4:
                return new Color(204, 204, 0);
            case 5:
                return new Color(153, 204, 0);
            case 6:
                return new Color(153, 255, 51);
            case 7:
                return new Color(153, 255, 102);
            case 8:
                return new Color(128, 255, 128);
            case 9:
                return new Color(204, 255, 204);
            default:
                return new Color(255,255,255);
        }
    }

    public void createTerrain(){
        int numMountains = randomNumberInclusive(lowNumMountains,highNumMountains);
        int mountainHeight;
        
        int numPlateus = randomNumberInclusive(lowNumPlateu,highNumPlateu);
        int plateuRadius;
        int plateuHeight;
        
        int numFlatTopMountains = randomNumberInclusive(lowNumFTM,highNumFTM);
        int flatTopMountainRadius;
        int flatTopMountainHeight;
        
        int numCraters = randomNumberInclusive(lowNumCraters,highNumCraters);
        int craterDepth;
        
        int numFlatBottomCraters = randomNumberInclusive(lowNumFBC,highNumFBC);
        int flatBottomCraterDepth;
        int flatBottomCraterRadius;
        for(int i = 0; i<numMountains; i++){
            mountainHeight = randomNumberInclusive(lowHeightMountains,highHeightMountains);
            addMountain(mountainHeight);
        }
        for(int i = 0; i<numPlateus; i++){
            plateuRadius = randomNumberInclusive(lowRadiusPlateu,highRadiusPlateu);
            plateuHeight = randomNumberInclusive(lowHeightPlateu,highHeightPlateu);
            addPlateu(plateuRadius,plateuHeight);
        }
        for(int i = 0; i<numFlatTopMountains; i++){
            flatTopMountainRadius = randomNumberInclusive(lowRadiusFTM,highRadiusFTM);
            flatTopMountainHeight = randomNumberInclusive(lowHeightFTM,highHeightFTM);
            addFlatTopMountain(flatTopMountainRadius,flatTopMountainHeight);
        }
        for(int i = 0; i<numCraters; i++){
            craterDepth = randomNumberInclusive(lowDepthCraters,highDepthCraters);
            addCrater(craterDepth);
        }
        for(int i = 0; i<numFlatBottomCraters; i++){
            flatBottomCraterDepth = randomNumberInclusive(lowDepthFBC,highDepthFBC);
            flatBottomCraterRadius = randomNumberInclusive(lowRadiusFBC,highRadiusFBC);
            addFlatBottomCrater(flatBottomCraterDepth,flatBottomCraterRadius);
        }        
    }

    public void createTerrain(int a){
        for(int i = 0; i<terrain.length; i++){
            for(int j = 0; j<terrain[0].length; j++){
            terrain[i][j] = a;
            }
        }
    }

    private int[] deepestCreeperAround(int x, int y,int radius){
        //returns a point with creeper on it
        int[] deepestPoint = {0,0};
        double deepestAmount = 0;
        for(int tempRadius = 1;tempRadius<radius;tempRadius++){
            for(int i =x-tempRadius+1; i<x+tempRadius; i++){
                if(inTerrainBounds(i,y+tempRadius) && creeperAmount[y+tempRadius][i]>deepestAmount){
                    deepestPoint[0] = i; deepestPoint[1] = y+tempRadius;
                    deepestAmount = creeperAmount[y+tempRadius][i];
                }
                if(inTerrainBounds(i,y-tempRadius)&&creeperAmount[y-tempRadius][i]>deepestAmount){
                    deepestPoint[0] = i; deepestPoint[1] = y-tempRadius;
                    deepestAmount = creeperAmount[y-tempRadius][i];
                }
            }
            for(int i =y-tempRadius+1; i<=y+tempRadius; i++){
                if(inTerrainBounds(x+tempRadius,i)&&creeperAmount[i][x+tempRadius]>deepestAmount){
                    deepestPoint[0] = x+tempRadius; deepestPoint[1] = i;
                    deepestAmount = creeperAmount[i][x+tempRadius];
                }
                if(inTerrainBounds(x-tempRadius,i)&&creeperAmount[i][x-tempRadius]>deepestAmount){
                    deepestPoint[0] = x-tempRadius; deepestPoint[1] = i;
                    deepestAmount = creeperAmount[i][x-tempRadius];
                }
            }
        }
        return deepestPoint;
    }

    public double difference(int a, int b, int c, int d, double[][] asdf){
        double diff = asdf[a][b]+terrain[a][b]-asdf[c][d]-terrain[c][d];
        //System.out.println("The difference between ("+a+","+b+") and ("+c+","+d+") is "+diff);
        return diff;
    }

    public JPanel getMap(){
        JPanel myPanel = new JPanel(new GridLayout(height,width));
        JPanel[][] thePanels = new JPanel[height][width];
        for(int i = 0; i<height; i++){
            for(int j = 0; j<width; j++){
                thePanels[i][j] = new JPanel();
                final int x = j;
                final int y = i;
                thePanels[i][j].addMouseListener(new MouseAdapter() { 
                    public void mouseEntered(MouseEvent me){
                        mouseOverX = x;
                        mouseOverY = y;
                    }
                    public void mousePressed(MouseEvent me) { 
                        switch(typeOfTurretToBuild){
                        case laserTurretCode:
                            addLaserTurret(x,y);
                            break;
                        case mortarTurretCode:
                            addMortarTurret(x,y);
                            break;
                        case otherTurretCode:
                            break;
                         default:
                             break;
                        }

                     }
                    });
                Color mapColor = computeMapColor(terrain[i][j]);
                Color theColor = mapColor;
                float creeperColorRatio = 0f;
                if(creeperAmount[i][j] > 0){theColor = creeperColor; creeperColorRatio = (float)computeCreeperColorRatio(creeperAmount[i][j]);}
                if(antiCreeperAmount[i][j] > 0){theColor = antiCreeperColor; creeperColorRatio = (float)computeCreeperColorRatio(antiCreeperAmount[i][j]);}
                thePanels[i][j].setBackground(blend(mapColor,theColor,creeperColorRatio));
                myPanel.add(thePanels[i][j]);
            }
        }
        for(int i = 0; i<emitters.size(); i++){
            thePanels[emitters.get(i).getyCoord()][emitters.get(i).getxCoord()].add(new JLabel("E"));
        }
        for(int i = 0; i<antiEmitters.size(); i++){
            thePanels[antiEmitters.get(i).getyCoord()][antiEmitters.get(i).getxCoord()].add(new JLabel("A"));
        }
        for(int i = 0; i<turrets.size(); i++){
            if(turrets.get(i).getTurretType().equals("Laser")){
                thePanels[turrets.get(i).getYCoord()][turrets.get(i).getXCoord()].add(new JLabel("L"));
            }
            if(turrets.get(i).getTurretType().equals("Mortar")){
                thePanels[turrets.get(i).getYCoord()][turrets.get(i).getXCoord()].add(new JLabel("M"));
            }
        }
        if(selecting){thePanels[mouseOverY][mouseOverX].add(new JLabel("S"));}
        return myPanel;
    }

    public int[][] getTerrain(){
        return terrain;
    }

    public int getTypeOfTurretToBuild(){
        return typeOfTurretToBuild;
    }

    private boolean inTerrainBounds(int x, int y){
        boolean inbounds = true;
        if(x<0 || x>=terrain.length){inbounds = false;}
        if(y<0 || y>=terrain[0].length){inbounds = false;}
        return inbounds;
    }

    public void populateAntiEmitters(){
        int numAntiEmitters = randomNumberInclusive(lowNumAntiEmitters,highNumAntiEmitters);
        for(int i = 0; i<numAntiEmitters; i++){
            boolean badLocation = true;
            int tempY = 0;
            int tempX = 0;
            while(badLocation){
                badLocation = false;
                tempY = randomNumberInclusive(0,height-1);
                tempX = randomNumberInclusive(0,width-1);
                for(int j = 0; j<antiEmitters.size(); j++){
                    if(antiEmitters.get(j).getxCoord() == tempX &&antiEmitters.get(j).getyCoord() == tempY){
                        badLocation = true;
                    }
                }
                for(int j = 0; j<emitters.size(); j++){
                    if(emitters.get(j).getxCoord() == tempX &&emitters.get(j).getyCoord() == tempY){
                        badLocation = true;
                    }
                }
            }
            AntiEmitter temp = new AntiEmitter(tempY,tempX);

            temp.setEmitAmmount(randomNumberInclusive(lowAntiEmitAmount,highAntiEmitAmount));
            temp.setEmitProbability((((double)randomNumberInclusive(lowAntiEmitProbability,highAntiEmitProbability))/100));
            antiEmitters.add(temp);
        }
    }

    private void populateEmitters() {
        int numEmitters = randomNumberInclusive(lowNumEmitters,highNumEmitters);
        for(int i = 0; i<numEmitters; i++){
            
            
            boolean badLocation = true;
            int tempY = 0;
            int tempX = 0;
            while(badLocation){
                badLocation = false;
                tempY = randomNumberInclusive(0,height-1);
                tempX = randomNumberInclusive(0,width-1);
                for(int j = 0; j<antiEmitters.size(); j++){
                    if(antiEmitters.get(j).getxCoord() == tempX &&antiEmitters.get(j).getyCoord() == tempY){
                        badLocation = true;
                    }
                }
                for(int j = 0; j<emitters.size(); j++){
                    if(emitters.get(j).getxCoord() == tempX &&emitters.get(j).getyCoord() == tempY){
                        badLocation = true;
                    }
                }
            }
            
            Emitter temp = new Emitter(tempY,tempX);
            temp.setEmitAmmount(randomNumberInclusive(lowEmitAmount,highEmitAmount));
            temp.setEmitProbability((((double)randomNumberInclusive(lowEmitProbability,highEmitProbability))/100));
            emitters.add(temp);
        }    
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
    
    public void pullAmt(int a, int b, int c, int d, double[][] asdf, double PULL_AMT){
        asdf[a][b] -=PULL_AMT;
        asdf[c][d] +=PULL_AMT;
    }

    private int randomNumberInclusive(int min,int max){
        return (int)((Math.random()*(max-min+1))+min);
    }

    public void restart(){
        for(int i = 0; i<height; i++){
            for(int j = 0; j<width; j++){
                creeperAmount[i][j] = 0;
                antiCreeperAmount[i][j] = 0;
            }
        }
        
    }

    public void setSelecting(boolean bool){
           selecting = bool;
       }

    public void setTerrain(int[][] t){
        terrain = t;
    }

    public void setTypeOfTurretToBuild(int input){
        typeOfTurretToBuild = input;
    }

    public void update(){    
        int x,y;
        final double SOME_AMOUNT = .5;
        double[][] future = new double[height][width];
        double[][] antiFuture = new double[height][width];
        
        double u = 0;
        double d = 0;
        double l = 0;
        double r = 0;
        double ul = 0;
        double ur = 0;
        double dl = 0;
        double dr = 0;
        double totalDiff = 0;
        double ratio = 0;
        //cause emitters to emit
        for(int i = 0; i<emitters.size(); i++){
            if(Math.random()<emitters.get(i).getEmitProbability()){
                x = emitters.get(i).getxCoord();
                y = emitters.get(i).getyCoord();
                creeperAmount[y][x] += emitters.get(i).getEmitAmmount();
            }
        }
        for(int i = 0; i<antiEmitters.size(); i++){
            if(Math.random()<antiEmitters.get(i).getEmitProbability()){
                x = antiEmitters.get(i).getxCoord();
                y = antiEmitters.get(i).getyCoord();
                antiCreeperAmount[y][x] += antiEmitters.get(i).getEmitAmmount();
            }
        }
        for(int i = 0; i<turrets.size(); i++){
            Turret tempTurret = turrets.get(i);
            if(tempTurret.getCurrentCoolDownTime()>0){
                tempTurret.coolDown();
            }
            else{
                tempTurret.resetCoolDown();
                if(tempTurret.getTurretType().equals("Laser")){
                    int[] closestCreeper = closestCreeperAroundLOS(tempTurret.getXCoord(),tempTurret.getYCoord(),tempTurret.getFireRange());
                    if(closestCreeper[0] != -1){//creeper in range of turret
                        int closestCreeperX = closestCreeper[0];
                        int closestCreeperY = closestCreeper[1];
                        creeperAmount[closestCreeperY][closestCreeperX] -= turretDestroyAmount;
                        if(creeperAmount[closestCreeperY-1][closestCreeperX]<0){creeperAmount[closestCreeperY-1][closestCreeperX] = 0;}
                    }
                }
                else if(tempTurret.getTurretType().equals("Mortar")){
                    int[] deepestCreeper = deepestCreeperAround(tempTurret.getXCoord(),tempTurret.getYCoord(),tempTurret.getFireRange());
                    if(deepestCreeper[0] != -1){//creeper in range of turret
                        int deepestCreeperX = deepestCreeper[0];
                        int deepestCreeperY = deepestCreeper[1];
                        
                        creeperAmount[deepestCreeperY][deepestCreeperX] -= turretDestroyAmount;
                        if(creeperAmount[deepestCreeperY][deepestCreeperX]<0){creeperAmount[deepestCreeperY][deepestCreeperX] = 0;}
                        if(inTerrainBounds(deepestCreeperY-1,deepestCreeperX)){
                            creeperAmount[deepestCreeperY-1][deepestCreeperX] -= turretDestroyAmount/2;
                            if(creeperAmount[deepestCreeperY-1][deepestCreeperX]<0){creeperAmount[deepestCreeperY-1][deepestCreeperX] = 0;}
                        }
                        if(inTerrainBounds(deepestCreeperY+1,deepestCreeperX)){
                            creeperAmount[deepestCreeperY+1][deepestCreeperX] -= turretDestroyAmount/2;
                            if(creeperAmount[deepestCreeperY+1][deepestCreeperX]<0){creeperAmount[deepestCreeperY+1][deepestCreeperX] = 0;}
                        }
                        if(inTerrainBounds(deepestCreeperY,deepestCreeperX-1)){
                            creeperAmount[deepestCreeperY][deepestCreeperX-1] -= turretDestroyAmount/2;
                            if(creeperAmount[deepestCreeperY][deepestCreeperX-1]<0){creeperAmount[deepestCreeperY][deepestCreeperX-1] = 0;}
                        }
                        if(inTerrainBounds(deepestCreeperY,deepestCreeperX+1)){
                            creeperAmount[deepestCreeperY][deepestCreeperX+1] -= turretDestroyAmount/2;
                            if(creeperAmount[deepestCreeperY][deepestCreeperX+1]<0){creeperAmount[deepestCreeperY][deepestCreeperX+1] = 0;}
                        }
                        if(inTerrainBounds(deepestCreeperY-1,deepestCreeperX-1)){
                            creeperAmount[deepestCreeperY-1][deepestCreeperX-1] -= turretDestroyAmount/2;
                            if(creeperAmount[deepestCreeperY-1][deepestCreeperX-1]<0){creeperAmount[deepestCreeperY-1][deepestCreeperX-1] = 0;}
                        }
                        if(inTerrainBounds(deepestCreeperY-1,deepestCreeperX+1)){
                            creeperAmount[deepestCreeperY-1][deepestCreeperX+1] -= turretDestroyAmount/2;
                            if(creeperAmount[deepestCreeperY-1][deepestCreeperX+1]<0){creeperAmount[deepestCreeperY-1][deepestCreeperX+1] = 0;}
                        }
                        if(inTerrainBounds(deepestCreeperY+1,deepestCreeperX+1)){
                            creeperAmount[deepestCreeperY+1][deepestCreeperX+1] -= turretDestroyAmount/2;
                            if(creeperAmount[deepestCreeperY+1][deepestCreeperX+1]<0){creeperAmount[deepestCreeperY+1][deepestCreeperX+1] = 0;}
                        }
                        if(inTerrainBounds(deepestCreeperY+1,deepestCreeperX-1)){
                            creeperAmount[deepestCreeperY+1][deepestCreeperX-1] -= turretDestroyAmount/2;
                            if(creeperAmount[deepestCreeperY+1][deepestCreeperX-1]<0){creeperAmount[deepestCreeperY+1][deepestCreeperX-1] = 0;}
                        }
                    }
                }
            }
        }
        for(int i = 0; i<terrain.length; i++){
            for(int j = 0; j<terrain[0].length; j++){
                future[i][j] = creeperAmount[i][j];
                antiFuture[i][j] = antiCreeperAmount[i][j];
            }
        }

        for(int i = 0; i<terrain.length; i++){
            for(int j = 0; j<terrain[0].length; j++){
                if(creeperAmount[i][j]>SOME_AMOUNT && Math.random()<flowRate){
                    u = 0;d = 0;l = 0;r = 0;
                    ul = 0;ur = 0;dl = 0;dr = 0;
                    
                    if(inTerrainBounds(i+1,j)){d = difference(i,j,i+1,j,creeperAmount);}
                        if(d<0){d = 0;}
                    if(inTerrainBounds(i-1,j)){u = difference(i,j,i-1,j,creeperAmount);}
                        if(u<0){u = 0;}
                    if(inTerrainBounds(i,j-1)){l = difference(i,j,i,j-1,creeperAmount);}
                        if(l<0){l = 0;}
                    if(inTerrainBounds(i,j+1)){r = difference(i,j,i,j+1,creeperAmount);}
                        if(r<0){r = 0;}
                    
                    if(inTerrainBounds(i+1,j+1)){dr = difference(i,j,i+1,j+1,creeperAmount);}
                        if(dr<0){dr = 0;}
                    if(inTerrainBounds(i-1,j-1)){ul = difference(i,j,i-1,j-1,creeperAmount);}
                        if(ul<0){ul = 0;}
                    if(inTerrainBounds(i+1,j-1)){dl = difference(i,j,i+1,j-1,creeperAmount);}
                        if(dl<0){dl = 0;}
                    if(inTerrainBounds(i-1,j+1)){ur = difference(i,j,i-1,j+1,creeperAmount);}
                        if(ur<0){ur = 0;}
                    totalDiff = u+d+l+r+ul+ur+dl+dr;
                    ratio = 0;
                    if(totalDiff != 0){ratio = creeperAmount[i][j]/(viscosity*totalDiff);}
                    
                    if(inTerrainBounds(i-1,j)){pullAmt(i,j,i-1,j,future,u*ratio);}
                    if(inTerrainBounds(i+1,j)){pullAmt(i,j,i+1,j,future,d*ratio);}
                    if(inTerrainBounds(i,j-1)){pullAmt(i,j,i,j-1,future,l*ratio);}
                    if(inTerrainBounds(i,j+1)){pullAmt(i,j,i,j+1,future,r*ratio);}
                    
                    if(inTerrainBounds(i-1,j-1)){pullAmt(i,j,i-1,j-1,future,ul*ratio);}
                    if(inTerrainBounds(i-1,j+1)){pullAmt(i,j,i-1,j+1,future,ur*ratio);}
                    if(inTerrainBounds(i+1,j-1)){pullAmt(i,j,i+1,j-1,future,dl*ratio);}
                    if(inTerrainBounds(i+1,j+1)){pullAmt(i,j,i+1,j+1,future,dr*ratio);}
                }
                if(antiCreeperAmount[i][j]>SOME_AMOUNT && Math.random()<flowRate){
                    u = 0;d = 0;l = 0;r = 0;
                    ul = 0;ur = 0;dl = 0;dr = 0;
                    
                    if(inTerrainBounds(i+1,j)){d = difference(i,j,i+1,j,antiCreeperAmount);}
                        if(d<0){d = 0;}
                    if(inTerrainBounds(i-1,j)){u = difference(i,j,i-1,j,antiCreeperAmount);}
                        if(u<0){u = 0;}
                    if(inTerrainBounds(i,j-1)){l = difference(i,j,i,j-1,antiCreeperAmount);}
                        if(l<0){l = 0;}
                    if(inTerrainBounds(i,j+1)){r = difference(i,j,i,j+1,antiCreeperAmount);}
                        if(r<0){r = 0;}
                    
                    if(inTerrainBounds(i+1,j+1)){dr = difference(i,j,i+1,j+1,antiCreeperAmount);}
                        if(dr<0){dr = 0;}
                    if(inTerrainBounds(i-1,j-1)){ul = difference(i,j,i-1,j-1,antiCreeperAmount);}
                        if(ul<0){ul = 0;}
                    if(inTerrainBounds(i+1,j-1)){dl = difference(i,j,i+1,j-1,antiCreeperAmount);}
                        if(dl<0){dl = 0;}
                    if(inTerrainBounds(i-1,j+1)){ur = difference(i,j,i-1,j+1,antiCreeperAmount);}
                        if(ur<0){ur = 0;}
                    totalDiff = u+d+l+r+ul+ur+dl+dr;
                    ratio = 0;
                    if(totalDiff != 0){ratio = antiCreeperAmount[i][j]/(viscosity*totalDiff);}
                    
                    if(inTerrainBounds(i-1,j)){pullAmt(i,j,i-1,j,antiFuture,u*ratio);}
                    if(inTerrainBounds(i+1,j)){pullAmt(i,j,i+1,j,antiFuture,d*ratio);}
                    if(inTerrainBounds(i,j-1)){pullAmt(i,j,i,j-1,antiFuture,l*ratio);}
                    if(inTerrainBounds(i,j+1)){pullAmt(i,j,i,j+1,antiFuture,r*ratio);}
                    
                    if(inTerrainBounds(i-1,j-1)){pullAmt(i,j,i-1,j-1,antiFuture,ul*ratio);}
                    if(inTerrainBounds(i-1,j+1)){pullAmt(i,j,i-1,j+1,antiFuture,ur*ratio);}
                    if(inTerrainBounds(i+1,j-1)){pullAmt(i,j,i+1,j-1,antiFuture,dl*ratio);}
                    if(inTerrainBounds(i+1,j+1)){pullAmt(i,j,i+1,j+1,antiFuture,dr*ratio);}
                }
            }
        }
        totalCreeperOnMap = 0;
        totalAntiCreeperOnMap = 0;
        for(int i = 0; i<terrain.length; i++){
            for(int j = 0; j<terrain[0].length; j++){
                 creeperAmount[i][j] = future[i][j];
                 antiCreeperAmount[i][j] = antiFuture[i][j];
                 if(creeperAmount[i][j]>0 && antiCreeperAmount[i][j]>0){
                     if(creeperAmount[i][j]>antiCreeperAmount[i][j]){
                         creeperAmount[i][j] = creeperAmount[i][j]-antiCreeperAmount[i][j];
                         antiCreeperAmount[i][j] = 0;
                     }
                     else{
                         antiCreeperAmount[i][j] = antiCreeperAmount[i][j]-creeperAmount[i][j];
                         creeperAmount[i][j] = 0;
                     }
                 }
                 totalCreeperOnMap = totalCreeperOnMap + creeperAmount[i][j];
                 totalAntiCreeperOnMap = totalAntiCreeperOnMap + antiCreeperAmount[i][j];
                 
            }
        }
    }
}
