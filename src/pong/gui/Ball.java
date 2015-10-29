package pong.gui;

import java.awt.Point;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

public class Ball extends PongItem {
	
	/**
	 * Speed of ball (in pixels per second)
	 */
	private static final int BALL_SPEED = 2;
		
	private Point speed;
	
	public Ball (String path) {
		super(path);
		this.speed = new Point(BALL_SPEED, BALL_SPEED);
	}
	
	public Point getSpeed() {
		return (Point) this.speed.clone();
	}
	
	public void setSpeed(Point speed) {
		this.speed = (Point) speed.clone();
	}
	
	public void animateBall(Set<Player> setPlayers) {
		updatePosition();
		checkCollisionWithSides(setPlayers);
		checkCollisionWithRackets(setPlayers);
	}
			
	public void updatePosition() {
		setPositionX(getPositionX() + speed.x);
		setPositionY(getPositionY() + speed.y);
	}
	
	/**
	 * Gestion du rebond sur les bords de l'écran 
	 */
	public void checkCollisionWithSides(Set<Player> setPlayers) {
		if (getPositionX() < 0) {
			updateScore(setPlayers, PlayerID.ONE);
			setPositionX(0);
			this.speed.x = -this.speed.x;
		}
		
		if (getPositionY() < 0) {
			setPositionY(0);
			this.speed.y = -this.speed.y;
		}
		
		if (getPositionX() > SIZE_PONG_X - getWidth()) {
			updateScore(setPlayers, PlayerID.TWO);
			setPositionX(getPositionX() - getWidth()/2);
			this.speed.x = -this.speed.x;
		}
		
		if (getPositionY() > SIZE_PONG_Y - getHeight()) {
			setPositionY(getPositionY() - getHeight()/2);
			this.speed.y = -this.speed.y;
		}
	}
	
	/**
	 * On parcourt l'ensemble des joueurs, et on incrémente le score de chacun d'eux sauf celui
	 * dont la bordure vient d'être touchée
	 */
	public void updateScore(Set<Player> setPlayers, PlayerID playerID) {
		Iterator<Player> it = setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			if (player.playerID != playerID) {
				player.increaseScore();
			}
		}	
	}
	
	/**
	 * Gestion du rebond sur les raquettes
	 */
	public void checkCollisionWithRackets(Set<Player> setPlayers) {
		Iterator<Player> it = setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			Racket racket = player.getRacket();
			if (this.getHitBox().intersects(racket.getHitBox())) {
				setPositionCollision(racket);
				this.speed.x = -this.speed.x;
				this.speed.y = -this.speed.y;
			}
		}
	}
	
	/**
	 * Met à jour la position de la balle après un rebond sur la raquette
	 */
	public void setPositionCollision(Racket racket) {
		if (racket.getPlayerID() == PlayerID.ONE) {
			setPositionX(racket.getPositionX() + racket.getWidth());
		}
		
		if (racket.getPlayerID() == PlayerID.TWO) {
			setPositionX(racket.getPositionX() - getWidth());
		}
	}
	
}
