package pong.gui;

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

public class Comment extends JFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	private JPanel pan = new JPanel();
	private JButton boutonMenu = new JButton("Menu");
	private JLabel strcomment1 = new JLabel("Principe du Pong classique");
	private JLabel strcomment2 = new JLabel("Utiliser les fleches du clavier pour ce deplacer vers le haut ou vers le bas");
	
	private boolean pressed = false;
	
	public Comment(){
		
		this.setTitle("Pong");
		this.setSize((Pong.SIZE_PONG_X/2)+(Pong.SIZE_PONG_X/10), Pong.SIZE_PONG_Y/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		boutonMenu.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent ae) {
		    	pressed = true;
		    	System.out.println("OK! Comment");
		    }
		});
		pan.setLayout(new GridLayout(4,1));
		strcomment1.setHorizontalAlignment(SwingConstants.CENTER);
		strcomment2.setHorizontalAlignment(SwingConstants.CENTER);
		pan.add(strcomment1);
		pan.add(strcomment2);
		
		pan.add(boutonMenu);
    	
	}
	public void print(){
		this.setContentPane(pan);
	   	this.setVisible(true);
	}

	public void setPressed(boolean b){
		pressed = b;
	}
	public boolean getPressed(){
		return pressed;
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