package pong.gui;

import java.awt.Point;
import java.awt.event.KeyEvent;

public class Racket extends PongItem implements Cloneable {
	
	/**
	 * Speed of racket (in pixels per second)
	 */
	private static final String PATH = "image/racket.png"; 
	public static final int RACKET_SPEED = 4;
	
	private PlayerID playerID;
	private int speed;
	
	public Racket(PlayerID playerID) {
		super(PATH);
		this.playerID = playerID;
		this.speed = 0;
		
		if (this.playerID == PlayerID.TWO) {
			setPositionX(SIZE_PONG_X - getWidth());
		}
	} 
	
	public Object clone() {
	    try {
	    	return super.clone();
	    } catch (CloneNotSupportedException cnse) {
	    	System.out.println(cnse);
	    	return null;
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
		setPositionY(getPositionY() + speed);
	}
	
	/**
	 * Gestion du rebond sur les bords de l'écran 
	 */
	public void checkCollisionWithSides() {
		if (getPositionY() < 0)
			setPositionY(0);
		if (getPositionY() > SIZE_PONG_Y - getHeight())
			setPositionY(SIZE_PONG_Y - getHeight());
	}
	
	/**
	 * Event (key pressed) 
	 */
	public void keyPressedRacket(KeyEvent e) {
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
	
	/**
	 *  Event (key released) 
	 */
	public void keyReleasedRacket(KeyEvent e) {
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
				System.out.println("got release "+e);
			}
	}
}
