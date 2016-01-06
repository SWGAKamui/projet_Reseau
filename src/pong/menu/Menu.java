package pong.menu;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pong.game.Pong;
/**
 * 
 * This code was found at http://openclassrooms.com
 *
 */
public class Menu extends JFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	private JPanel pan = new JPanel();
	private JButton bouton = new JButton("Jouer");
	private JButton boutonHow = new JButton("Comment jouer ?");
	private JLabel strPong = new JLabel("Star Pong");
	private JLabel strautor = new JLabel("Kinda AL CHAHID - Thibault PARPAITE");
	private boolean pressedPlay = false;
	private boolean pressedHow = false;
	
	public Menu(){
		
		this.setTitle("Pong");
		this.setSize((Pong.SIZE_PONG_X/2)+(Pong.SIZE_PONG_X/10), Pong.SIZE_PONG_Y/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		bouton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	pressedPlay = true;
		    }
		});
		
		boutonHow.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	pressedHow = true;
		    }
		});
		
		strPong.setFont(new Font("Verdana",1,20));
		pan.setLayout(new GridLayout(5,1));
		strPong.setHorizontalAlignment(SwingConstants.CENTER);
		strautor.setHorizontalAlignment(SwingConstants.CENTER);

		pan.add(strPong);
		pan.add(strautor);
		pan.add(boutonHow);
    	pan.add(bouton);
    	
    	
	}
	public void print(){
		this.setContentPane(pan);
    	this.setVisible(true);
	}

	public void setPressedHow(boolean b){
		pressedHow = b;
	}
	public boolean getPressedHow(){
		return pressedHow;
	}
	public void setPressedPlay(boolean b){
		pressedPlay = b;
	}
	public boolean getPressedPlay(){
		return pressedPlay;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}	
}