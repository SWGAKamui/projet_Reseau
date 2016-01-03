package pong.gui;

import java.awt.Point;
import java.awt.event.KeyEvent;

public class Racket extends PongItem {
	
	/**
	 * Path of the racket img
	 */
	public static final String PATH_RACKET_DEFAULT = "image/sabre_vert.png";
	public static final String PATH_RACKET_ONE = "image/sabre_vert.png";
	public static final String PATH_RACKET_TWO = "image/sabre_rouge.png";
	public static final String PATH_RACKET_THREE = "image/sabre_bleu.png";
	public static final String PATH_RACKET_FOUR = "image/sabre_violet.png";
	
	/**
	 * Speed of racket (in pixels per second)
	 */
	public static final int RACKET_SPEED = 4;
	
	private PlayerID playerID;
	private int speed;
	
	public Racket(PlayerID playerID) {
		super(PATH_RACKET_DEFAULT);
		this.playerID = playerID;
		this.speed = 0;
		
		/* Position par défaut de la raquette en fonction du joueur */
		switch (this.playerID) {
			case ONE:
				this.setPath(PATH_RACKET_ONE);
				setPosition(new Point(0, SIZE_PONG_Y/2));
				break;
			
		    case TWO:
		    	this.setPath(PATH_RACKET_TWO);
		    	setPosition(new Point(SIZE_PONG_X - getWidth(), SIZE_PONG_Y/2));
		    	break;
		    
		    case THREE:
		    	this.setPath(PATH_RACKET_THREE);
		    	setPosition(new Point(SIZE_PONG_X/2, 0));
		    	break;
		    	
		    case FOUR:
		    	this.setPath(PATH_RACKET_FOUR);
		    	setPosition(new Point(SIZE_PONG_X/2, SIZE_PONG_Y - getHeight()));
		    	break;
		    	
		    default:
		    	break;
		}
	}
	
	public Racket(PlayerID playerID, int positionRacketX, int positionRacketY) {
		super(PATH_RACKET_DEFAULT);
		this.playerID = playerID;
		this.speed = 0;
		setPosition(new Point(positionRacketX, positionRacketY));
		
		/* Chargement de la bonne couleur de raquette */
		switch (this.playerID) {
			case ONE:
				this.setPath(PATH_RACKET_ONE);
				break;
			
		    case TWO:
		    	this.setPath(PATH_RACKET_TWO);
		    	break;
		    
		    case THREE:
		    	this.setPath(PATH_RACKET_THREE);
		    	break;
		    	
		    case FOUR:
		    	this.setPath(PATH_RACKET_FOUR);
		    	break;
		    	
		    default:
		    	break;
		}
	}
		
	public PlayerID getPlayerID() {
		return this.playerID;
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void animateRacket() {
		updatePosition();
		checkCollisionWithSides();	
	}
	
	public void updatePosition() {
		if (this.playerID == PlayerID.ONE || this.playerID == PlayerID.TWO) {
			setPositionY(getPositionY() + speed);
		}
		else {
			setPositionX(getPositionX() + speed);
		}
	}
	
	/**
	 * Gestion des collisions sur les bords de l'écran 
	 */
	public void checkCollisionWithSides() {
		if (this.playerID == PlayerID.ONE || this.playerID == PlayerID.TWO) {
			if (getPositionY() < 0)
				setPositionY(0);
			if (getPositionY() > SIZE_PONG_Y - getHeight())
				setPositionY(SIZE_PONG_Y - getHeight());
		}
		else {
			if (getPositionX() < 0)
				setPositionX(0);
			if (getPositionX() > SIZE_PONG_X - getWidth())
				setPositionX(SIZE_PONG_X - getWidth());
		}
	}
	
	/**
	 * Event (key pressed) 
	 */
	public void keyPressedRacket(KeyEvent e) {
		if (this.playerID == PlayerID.ONE || this.playerID == PlayerID.TWO) { 
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					this.speed = -RACKET_SPEED;
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					this.speed = RACKET_SPEED;
					break;
				default:
					System.out.println("got press "+e);
			}
		}
		else {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_KP_LEFT:
					this.speed = -RACKET_SPEED;
					break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_KP_RIGHT:
					this.speed = RACKET_SPEED;
					break;
				default:
					System.out.println("got press "+e);
			}
		}
	}
	
	/**
	 *  Event (key released) 
	 */
	public void keyReleasedRacket(KeyEvent e) {
		if (this.playerID == PlayerID.ONE || this.playerID == PlayerID.TWO) { 
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					this.speed = 0;
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					this.speed = 0;
					break;
				default:
					System.out.println("got press "+e);
			}
		}
		else {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_KP_LEFT:
					this.speed = 0;
					break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_KP_RIGHT:
					this.speed = 0;
					break;
				default:
					System.out.println("got press "+e);
			}
		}
	}
}
