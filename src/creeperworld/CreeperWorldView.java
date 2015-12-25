package creeperworld;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreeperWorldView extends JFrame implements MouseListener,ActionListener,KeyListener{

    private static final long serialVersionUID = 1L;

    JPanel userInputPanel = new JPanel();
        
    JButton stepForwardButton = new JButton("StepForward");
    JButton playButton = new JButton("Play");
    JButton pauseButton = new JButton("Pause");
    JButton restartButton = new JButton("Restart");
    JButton newMapButton = new JButton("New Map");
    JButton speedX2Button = new JButton("X2");
    JButton speedX1Button = new JButton("X1");
    JButton speedX10Button = new JButton("X10");
    JButton addLaserTurretButton = new JButton("Add Laser Turret");
    JButton addMortarTurretButton = new JButton("Add Mortar Turret");
    
    BorderLayout myBorderLayout = new BorderLayout();
    CreeperWorldModelFacade model;
    
    public void updateMap(JPanel myPanel){
        if(myBorderLayout.getLayoutComponent(BorderLayout.CENTER) != null){
                remove(myBorderLayout.getLayoutComponent(BorderLayout.CENTER));
        }
        myPanel.addMouseListener(this);
        add(myPanel,BorderLayout.CENTER);
        revalidate();
    }
    public void displayCurrentMap(){
        System.out.println("displaying current map");
    }
    public CreeperWorldView() {
        setTitle("CreeperWorld");
        setVisible(true);
        setBounds(0,0,1000,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(myBorderLayout);
        
        stepForwardButton.addActionListener(this);
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        restartButton.addActionListener(this);
        newMapButton.addActionListener(this);
        speedX2Button.addActionListener(this);
        speedX1Button.addActionListener(this);
        speedX10Button.addActionListener(this);
        addLaserTurretButton.addActionListener(this);
        addMortarTurretButton.addActionListener(this);

        
        userInputPanel.add(stepForwardButton);
        userInputPanel.add(playButton);
        userInputPanel.add(pauseButton);
        userInputPanel.add(restartButton);
        userInputPanel.add(newMapButton);
        userInputPanel.add(speedX2Button);
        userInputPanel.add(speedX1Button);
        userInputPanel.add(speedX10Button);
        userInputPanel.add(addLaserTurretButton);
        userInputPanel.add(addMortarTurretButton);

        
        userInputPanel.setBackground(Color.red);
        add(userInputPanel,BorderLayout.SOUTH);

        
        model = new CreeperWorldModelFacade(this);
        model.newMap();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == stepForwardButton){
            model.stepForward();
        }if(ae.getSource() == playButton){
            model.play();
        }if(ae.getSource() == pauseButton){
            model.pause();
        }if(ae.getSource() == restartButton){
            model.restart();
        }if(ae.getSource() == newMapButton){
            model.newMap();
        }if(ae.getSource() == speedX2Button){
            model.setFrameDuration(100);
        }if(ae.getSource() == speedX1Button){
            model.setFrameDuration(200);
        }if(ae.getSource() == speedX10Button){
            model.setFrameDuration(20);
        }if(ae.getSource() == addLaserTurretButton){
            model.setBuildingTurret(0);
        }if(ae.getSource() == addMortarTurretButton){
            model.setBuildingTurret(1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        //System.out.println("X = "+me.getX());
        //System.out.println("Y = "+me.getY());
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent me) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void keyPressed(KeyEvent arg0) {
        System.out.println("key pressed");
    }
    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }
}
