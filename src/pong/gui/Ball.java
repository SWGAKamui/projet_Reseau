package pong.gui;

import java.awt.Point;
import java.util.Iterator;
import java.util.Set;

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
	
	public void animateBall(Set<Racket> setRacket) {
		updatePosition();
		checkCollisionWithSides();
		checkVictory();
		checkCollisionWithRackets(setRacket);
	}
			
	public void updatePosition() {
		setPositionX(getPositionX() + speed.x);
		setPositionY(getPositionY() + speed.y);
	}
	
	/**
	 * Gestion du rebond sur les bords de l'écran 
	 */
	public void checkCollisionWithSides() {
		if (getPositionX() < 0) {
			setPositionX(0);
			this.speed.x = -this.speed.x;
		}
		
		if (getPositionY() < 0) {
			setPositionY(0);
			this.speed.y = -this.speed.y;
		}
		
		if (getPositionX() > SIZE_PONG_X - getWidth()) {
			setPositionX(getPositionX() - getWidth()/2);
			this.speed.x = -this.speed.x;
		}
		
		if (getPositionY() > SIZE_PONG_Y - getHeight()) {
			setPositionY(getPositionY() - getHeight()/2);
			this.speed.y = -this.speed.y;
		}
	}
	
	public void checkVictory() {
	}

	/**
	 * Gestion du rebond sur les raquettes
	 */
	public void checkCollisionWithRackets(Set<Racket> setRacket) {
		Iterator<Racket> it = setRacket.iterator();
		while(it.hasNext()) {
			Racket racket = it.next();
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
		if (racket.getPlayer() == 1) {
			setPositionX(racket.getPositionX() + racket.getWidth());
		}
		
		if (racket.getPlayer() == 2) {
			setPositionX(racket.getPositionX() - getWidth());
		}
	}
	
}
