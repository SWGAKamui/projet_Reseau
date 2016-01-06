package pong.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;

import pong.network.*;
import pong.menu.Son;
import pong.menu.Victoire;

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
	private static final Color backgroundColor = new Color(0x00, 0x00, 0x00);
	private static final Color fontColor = new Color(0xFF, 0xFF, 0xFF);
    

	/**
	 * Score à obtenir pour remporter la partie
	 */
	private static final int SCORE_TO_WIN = 5;
	
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
	 * Joueur local
	 */
	private Player localPlayer;
	
	/**
	 * Gestion du réseau
	 */
	private Network network;

	private Son audio;
	
	public static final String PATH_SOUND = "res/sound/duel_of_the_fates.wav";
	
	/**
	 * Ensemble contenant les joueurs. 
	 * Chaque joueur est composé de différents champs (score, socket, etc.), notamment le champ Racket (une raquette par joueur)
	 */
	public Set<Player> setPlayers;
	
	/**
	 * Gère l'envoi et la reception des requêtes, implémentation du protocole (délégation)
	 */
	private ProtocolHandler protocolHandler;
	
	/**
	 * Pixel data buffer for the Pong rendering
	 */
	private Image buffer = null;
	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;

        private Image background = background();
    
	/* Instanciation de la partie du premier joueur */
	public Pong(int localPort) {
		this.protocolHandler = new ProtocolHandler(this);
		this.ball = new Ball();
		this.setPlayers = new HashSet<Player>();	
		this.network = new Network(this, localPort);
		this.audio = new Son(PATH_SOUND);
		audio.play();
		this.localPlayer = new Player(PlayerID.ONE, network.getLocalHost(), network.getLocalPort());
		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(this);
		
		setPlayers.add(localPlayer);
	}
	
	/* Instanciation de la partie des autres joueurs */
	public Pong(int localPort, String host, int port) {
		this.protocolHandler = new ProtocolHandler(this);
		this.ball = new Ball();
		this.setPlayers = new HashSet<Player>();		
		this.network = new Network(this, localPort, host, port);
		this.audio = new Son(PATH_SOUND);
		audio.play();
		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(this);
		
		/* On initialise la partie */
		protocolHandler.initGame();

		this.localPlayer = new Player(selectPlayerID(), network.getLocalHost(), network.getLocalPort());
		network.connectToAll();	
		setPlayers.add(localPlayer);
	}
	
	/**
	 * Mainloop
	 */
	public void mainLoop() {
		calculate();
		sendNewInfo();
		
		long t = System.currentTimeMillis();
		long end = t + 5;
		while(System.currentTimeMillis() < end) {
			String payload = receiveNewInfo();
			updateGame(payload);
		}
		
		updateScreen();
	}
		
	/**
	 * Proceeds to the movement of the racket of the local player and the ball (only if it is in control area of local player)
	 */
	public void calculate() {
		Racket racket = localPlayer.getRacket();
		racket.animateRacket();
		localPlayer.setRacket(racket);

		if (this.localPlayer.getPlayerID() == PlayerID.ONE) {
			ball.animateBall(setPlayers);
		}
	}
	
	public void sendNewInfo() {
		protocolHandler.sendNewInfoProtocol();
	}
	
	public String receiveNewInfo() {
		return network.receiveNewInfo();
	}
		
	/**
	 * On traite la requête reçue à l'aide de la classe ProtocolHandler
	 */
	public void updateGame(String payload) {
		if (payload != null) {
			protocolHandler.setPayload(payload);
			protocolHandler.run();
		}
	}
			
	public void keyPressed(KeyEvent e) {
		Racket racket = localPlayer.getRacket();
		racket.keyPressedRacket(e);
		localPlayer.setRacket(racket);
	}
	
	public void keyReleased(KeyEvent e) {
		Racket racket = localPlayer.getRacket();
		racket.keyReleasedRacket(e);
		localPlayer.setRacket(racket);
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
	
	public Image background(){
		try {
		    return ImageIO.read(new File("res/image/background.png"));
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
		//graphicContext.setColor(backgroundColor);
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
	
	public void displayScore() {
		Iterator<Player> it = setPlayers.iterator();

		while(it.hasNext()) {
			Player player = it.next();
			switch (player.getPlayerID()) {
				case ONE:
					graphicContext.drawString("P1 : " + Integer.toString(player.getScore()), SIZE_PONG_X/3, SIZE_PONG_Y/2 - 15);
					break;
				case TWO:
					graphicContext.drawString("P2 : " + Integer.toString(player.getScore()), 2*(SIZE_PONG_X)/3, SIZE_PONG_Y/2 - 15);
					break;
				case THREE:
					graphicContext.drawString("P2 : " + Integer.toString(player.getScore()), SIZE_PONG_X/3, SIZE_PONG_Y/2 + 15);
					break;
				case FOUR:
					graphicContext.drawString("P2 : " + Integer.toString(player.getScore()), 2*(SIZE_PONG_X)/3, SIZE_PONG_Y/2 + 15);
					break;
				default:
					break;
			}
		}
		graphicContext.drawString("Score", (SIZE_PONG_X/2)-4*("Score".length()), SIZE_PONG_Y/2);
	}
    
	public boolean checkVictory() {
		Iterator<Player> it = setPlayers.iterator();

		while(it.hasNext()) {
			Player player = it.next();
			if (player.getScore() == SCORE_TO_WIN) {
			    Victoire vic = new Victoire(player);
			    vic.print();
			    audio.stop();
			    return true;
			}
		}
		return false;	
	}

    	public int getNbPlayers(){
	        int cpt = 0;
		Iterator<Player> it = setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			cpt++;
		}
		return cpt;
	}
    
	public PlayerID selectPlayerID() {
	        int cpt = getNbPlayers();
		switch (cpt) {
			case 0:
				return PlayerID.ONE;
			case 1:
				return PlayerID.TWO;
			case 2:
				return PlayerID.THREE;
			case 3:
				return PlayerID.FOUR;
			default:
				return PlayerID.FULL;
		}
	}
	
	/* Accesseurs */
	
	public Ball getBall() {
		return (Ball) this.ball.clone();
	}
	
	public void setBall(Ball ball) {
		this.ball = (Ball) ball.clone();
	}
	
	public Network getNetwork() {
		return this.network;
	}
	
	public Player getLocalPlayer() {
		return this.localPlayer;
	}
	
	public String getLocalPlayerProtocol() {
		return protocolHandler.getLocalPlayerProtocol();
	}
}
