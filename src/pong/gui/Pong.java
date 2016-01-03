package pong.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

/**
 * An Pong is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */
public class Pong extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Constant (c.f. final) common to all Pong instances (c.f. static)
	 * defining the background color of the Pong
	 */
	private static final Color backgroundColor = new Color(0x66, 0x99, 0xce);
	private static final Color fontColor = new Color(0xFF, 0xFF, 0xFF);
    

	/**
	 * Score à obtenir pour remporter la partie
	 */
	private static final int SCORE_TO_WIN = 10;
	
	/**
	 * Width of pong area
	 */
	public static final int SIZE_PONG_X = 800;
	/**
	 * Height of pong area
	 */
	public static final int SIZE_PONG_Y = 600;
	/**
	 * Time step of the simulation (in ms)
	 */
	public static final int timestep = 10;
	
	/**
	 * Object Ball 
	 */
	private Ball ball;
	
	/**
	 * Ensemble contenant les joueurs. 
	 * Chaque joueur est composé de différents champs (score, socket, etc.), notamment le champ Racket (une raquette par joueur)
	 */
	private Set<Player> setPlayers;

	/**
	 * Pixel data buffer for the Pong rendering
	 */
	private Image buffer = null;
	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;
	
	private Image background = Background();
	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	
	public Pong() {

		this.ball = new Ball();
		
		this.player1 = new Player(PlayerID.ONE);
		this.player2 = new Player(PlayerID.TWO);
		this.player3 = new Player(PlayerID.THREE);
		this.player4 = new Player(PlayerID.FOUR);
		this.setPlayers = new HashSet<Player>();
		setPlayers.add(player1);
		setPlayers.add(player2);
		
		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(this);
	}
	
	public int getNbPlayers(){
		return 4;
	}
	/**
     * Proceeds to the movement of the rackets, the ball and updates the screen
	 */
	public void animate() {
		
		/* L'iterateur sert à parcourir l'ensemble des joueurs, donc l'ensemble des raquettes */
		Iterator<Player> it = setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			Racket racket = player.getRacket();
			racket.animateRacket();
			player.setRacket(racket);
		}
		ball.animateBall(setPlayers);
		/* Update output */
		updateScreen();
	}

	public void keyPressed(KeyEvent e) {
		Iterator<Player> it = setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			Racket racket = player.getRacket();
			racket.keyPressedRacket(e);
			player.setRacket(racket);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		Iterator<Player> it = setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			Racket racket = player.getRacket();
			racket.keyReleasedRacket(e);
			player.setRacket(racket);
		}
	}
	
	public void keyTyped(KeyEvent e) { }

	/*
	 * (non-Javadoc) This method is called by the AWT Engine to paint what
	 * appears in the screen. The AWT engine calls the paint method every time
	 * the operative system reports that the canvas has to be painted. When the
	 * window is created for the first time paint is called. The paint method is
	 * also called if we minimize and after we maximize the window and if we
	 * change the size of the window with the mouse.
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}
	
	public Image Background(){
		try {
			return ImageIO.read(new File("src/image/background.png"));
		} catch (IOException exp) {
	        exp.printStackTrace();
	    }
		return background;
	}
	/**
	 * Draw each Pong item based on new positions
	 */
	public void updateScreen() {
		Font f = new Font("Dialog", Font.PLAIN, 20);
		if (buffer == null) {
			/* First time we get called with all windows initialized */
			buffer = createImage(SIZE_PONG_X, SIZE_PONG_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}
		
		/* Fill the area with blue */
		graphicContext.setColor(backgroundColor);
		graphicContext.fillRect(0, 0, SIZE_PONG_X, SIZE_PONG_Y);
		graphicContext.setColor(fontColor);
		graphicContext.setFont(f);
		graphicContext.drawImage(background, 0, 0, SIZE_PONG_X, SIZE_PONG_Y, null);
		/* Draw items */
		displayScore();
		graphicContext.drawImage(ball.getImg(), ball.getPosition().x, ball.getPosition().y, ball.getWidth(), ball.getHeight(), null);
		
		
		Iterator<Player> it = setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			Racket racket = player.getRacket();
			graphicContext.drawImage(racket.getImg(), racket.getPosition().x, racket.getPosition().y, racket.getWidth(), racket.getHeight(), null);
		}
		
		this.repaint();
	}
	public void displayScore(){
		if(getNbPlayers() == 2){
			graphicContext.drawString("P1 : "+Integer.toString(player1.getScore()),SIZE_PONG_X/3,SIZE_PONG_Y/2 - 15);
			graphicContext.drawString("P2 : "+Integer.toString(player2.getScore()),2*(SIZE_PONG_X)/3,SIZE_PONG_Y/2 - 15);
			graphicContext.drawString("Score", (SIZE_PONG_X/2)-4*("Score".length()), SIZE_PONG_Y/2 - 15);
		}
		if(getNbPlayers() == 3){
			graphicContext.drawString("P1 : "+Integer.toString(player1.getScore()),SIZE_PONG_X/3,SIZE_PONG_Y/2 - 15);
			graphicContext.drawString("P2 : "+Integer.toString(player2.getScore()),2*(SIZE_PONG_X)/3,SIZE_PONG_Y/2 - 15);
			graphicContext.drawString("P3 : "+Integer.toString(player3.getScore()),(SIZE_PONG_X)/2 -(SIZE_PONG_X)/50 - 4, SIZE_PONG_Y/2 + 15);
			graphicContext.drawString("Score", (SIZE_PONG_X/2)-4*("Score".length()), SIZE_PONG_Y/2 - 15);
		}
		if(getNbPlayers() == 4){
			graphicContext.drawString("P1 : "+Integer.toString(player1.getScore()),SIZE_PONG_X/3,SIZE_PONG_Y/2 - 15);
			graphicContext.drawString("P2 : "+Integer.toString(player2.getScore()),2*(SIZE_PONG_X)/3, SIZE_PONG_Y/2 - 15);
			graphicContext.drawString("P3 : "+Integer.toString(player3.getScore()),SIZE_PONG_X/3, SIZE_PONG_Y/2 + 15);
			graphicContext.drawString("P4 : "+Integer.toString(player4.getScore()),2*(SIZE_PONG_X)/3, SIZE_PONG_Y/2 + 15);
			graphicContext.drawString("Score", (SIZE_PONG_X/2)-4*("Score".length()), SIZE_PONG_Y/2);
		}
	}
	
	public boolean checkVictory() {
		Iterator<Player> it = setPlayers.iterator();

		while(it.hasNext()) {
			Player player = it.next();

			if (player.score == SCORE_TO_WIN) {
				//JOptionPane.showMessageDialog(null, "Player" + player.playerID.toString() + "wins", "Pong", JOptionPane.PLAIN_MESSAGE);
				Victoire vic = new Victoire(player);
				vic.print();
				return true;
			}
		}
		return false;	
	}
}
