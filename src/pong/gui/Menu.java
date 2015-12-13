package pong.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * 
 * This code was found in http://openclassrooms.com
 *
 */
public class Menu extends JFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	private JPanel pan = new JPanel();
	private JButton bouton = new JButton("Play");
	public boolean pressed = false;
	
	public Menu(){
		this.setTitle("Pong");
		this.setSize(300, 150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		bouton.addMouseListener(this);
		bouton.setPreferredSize(new Dimension (250, 100));
    	pan.add(bouton);
    	this.setContentPane(pan);
    	this.setVisible(true);
	}
	public void setPressed(){
		pressed = true;
	}
	public boolean getPressed(){
		return pressed;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		setPressed();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		//Doit être nulle
	}	
	
	@Override
	public void mouseExited(MouseEvent e) {
		//Doit être nulle
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//Doit être nulle
	}
			@Override
	public void mouseReleased(MouseEvent e) {
		//Doit être nulle
	} 	
}