package pong.gui;

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
/**
 * 
 * This code was found in http://openclassrooms.com
 *
 */
public class Menu extends JFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	private JPanel pan = new JPanel();
	private JButton bouton = new JButton("Play");
	private JButton boutonHow = new JButton("Comment jouer ?");
	private JLabel strPong = new JLabel("Pong Reseau");
	private JLabel strautor = new JLabel("Kinda AL CHAHID - Thibault PARPAITE");
	public boolean pressedPlay = false;
	public boolean pressedHow = false;
	
	public Menu(){
		
		this.setTitle("Pong");
		this.setSize((Pong.SIZE_PONG_X/2)+(Pong.SIZE_PONG_X/10), Pong.SIZE_PONG_Y/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		bouton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent ae) {
		    	setPressedPlay(true);
		    }
		});
		
		boutonHow.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent ae) {
		    	setPressedHow(true);
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
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	
}