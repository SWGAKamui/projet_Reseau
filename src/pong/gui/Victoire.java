package pong.gui;


import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sound.Son;



public class Victoire extends JFrame{
	private static final long serialVersionUID = 1L;
	private JFrame frame = new JFrame("Victoire !");

	
	public Victoire(Player player){
		Son audio = new Son("sound/victory_theme.mp3");
		audio.play();
		
		try {			
            BufferedImage img = ImageIO.read(new File("image/red-panda.jpg"));            
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                    
            frame.setContentPane(new JLabel(new ImageIcon(img)));
            frame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            
            JLabel label = new JLabel("Player  " + player.getPlayerID().toString() + "  Wins !");
            label.setFont(new Font("Serif", Font.BOLD, 40));
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));// places at the left
            panel.add( label );

            frame.add( panel );
            frame.pack();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
	}
	public void print(){
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
}
