package pong.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import java.util.HashSet;
import java.util.Iterator;
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
    private static final Color backgroundColor = new Color(0xC, 0x2D, 0x4E);

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
	 * Joueur local
	 */
	private LocalPlayer localPlayer;
	
	/**
	 * Ensemble contenant les joueurs. 
	 * Chaque joueur est composé de différents champs (score, socket, etc.), notamment le champ Racket (une raquette par joueur)
	 */
	protected Set<Player> setPlayers;
	
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

	/* Instanciation de la partie du premier joueur */
	public Pong() {
		this.protocolHandler = new ProtocolHandler(this);
		this.setPlayers = new HashSet<Player>();
		this.localPlayer = new LocalPlayer(this);
		setPlayers.add(localPlayer);
		
		this.ball = new Ball();
		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(this);
	}
	
	/* Instanciation de la partie des autres joueurs */
	public Pong(String host, int port) {
		this.protocolHandler = new ProtocolHandler(this);
		this.ball = new Ball();
		this.setPlayers = new HashSet<Player>();
		this.localPlayer = new LocalPlayer(this, host, port);
		setPlayers.add(localPlayer);
		
		String payload = receiveNewInfo();
		this.updateGame(payload);
		
		/* On connait maintenant l'ensemble des autres joueurs distants auxquels on doit se connecter */
		localPlayer.updateLocalPlayer();
		
		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(this);
	}

	/**
	 * Mainloop
	 */
	public void mainLoop() {
		checkNewConnexion();
		calculate();
		sendNewInfo();
		String payload = receiveNewInfo();
		checkNewInfo(payload);
		updateGame(payload);
		updateScreen();
	}
	
	public void checkNewConnexion() {
	}
	
	/**
     * Proceeds to the movement of the racket of the local player and the ball (only if it is in control area of local player)
	 */
	public void calculate() {
		
		Racket racket = localPlayer.getRacket();
		racket.animateRacket();
		localPlayer.setRacket(racket);
		
		if (localPlayer.getPlayerID() == PlayerID.ONE) {
			ball.animateBall(setPlayers);
		}
	}
	
	public void sendNewInfo() {
		protocolHandler.sendNewInfoProtocol();
	}
	
	public String receiveNewInfo() {
		return localPlayer.readFromAll();
	}
	
	public void checkNewInfo(String payload) {
		// On vérifie la légitimité des données reçues
	}
	
	/**
	 * On traite la requête reçue à l'aide de la classe ProtocolHandler
	 */
	public void updateGame(String payload) {
		protocolHandler.setPayload(payload);
		protocolHandler.run();
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

	/**
	 * Draw each Pong item based on new positions
	 */
	public void updateScreen() {
		
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

		/* Draw items */
		graphicContext.drawImage(ball.getImg(), ball.getPosition().x, ball.getPosition().y, ball.getWidth(), ball.getHeight(), null);
		
		Iterator<Player> it = setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			Racket racket = player.getRacket();
			graphicContext.drawImage(racket.getImg(), racket.getPosition().x, racket.getPosition().y, racket.getWidth(), racket.getHeight(), null);
		}
		
		this.repaint();
	}
	
	public boolean checkVictory() {
		Iterator<Player> it = setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			if (player.getScore() == SCORE_TO_WIN) {
				JOptionPane.showMessageDialog(null, "Player" + player.getPlayerID() + "wins", "Pong", JOptionPane.PLAIN_MESSAGE);
				return true;
			}
		}
		return false;	
	}
	
	/* Accesseurs */
	
	public Ball getBall() {
		return (Ball) this.ball.clone();
	}
	
	public void setBall(Ball ball) {
		this.ball = (Ball) ball.clone();
	}
	
	/* Pas d'encapsulation volontairement */
	public LocalPlayer getLocalPlayer() {
		return this.localPlayer;
	}
	
	public String getInitialGameProtocol() {
		return protocolHandler.getInitialGameProtocol();
	}
	
	public String getLocalPlayerProtocol() {
		return protocolHandler.getLocalPlayerProtocol();
	}
}
