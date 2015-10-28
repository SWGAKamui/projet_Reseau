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
		this.speed = (Point) new Point(BALL_SPEED, BALL_SPEED);
	}
	
	public Point getSpeed() {
		return (Point) this.speed.clone();
	}
	
	public void setSpeed(Point speed) {
		this.speed = (Point) speed.clone();
	}
	
	/**
	 * Update ball position 
	 */
	public void animateBall(Set<Racket> setRacket) {
		Point pos = getPosition(); // pos is a local variable
		pos.translate(speed.x, speed.y);
		setPosition(pos);
		
		/* Gestion du rebond sur les raquettes */
		Iterator<Racket> it = setRacket.iterator();
		while(it.hasNext()) {
			Racket racket = it.next();
			if (checkCollision(racket)) {
				setPositionCollision(racket);
				this.speed.x = -this.speed.x;
				this.speed.y = -this.speed.y;
			}
		}
		
		/* Gestion du rebond sur les bords de l'écran */
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

	/**
	 *  Vérifie si une collision a eu lieu avec la raquette (chaque raquette a une zone de collision différente)
	 *  Code à factoriser
	 */
	public boolean checkCollision(Racket racket) {
		if (racket.getPlayer() == 1) {
			return (getPositionX() <= racket.getPositionX() + racket.getWidth()) 
					&& (getPositionY() + getHeight() >= racket.getPositionY())
					&& (getPositionY() <= racket.getPositionY() + racket.getHeight());
		}
		
		
		if (racket.getPlayer() == 2) {
			return (getPositionX() + getWidth() >= racket.getPositionX())
					&& (getPositionY() + getHeight() >= racket.getPositionY())
					&& (getPositionY() <= racket.getPositionY() + racket.getHeight());
		}
		
		return false; // tmp
	}
	
	public void setPositionCollision(Racket racket) {
		if (racket.getPlayer() == 1) {
			setPositionX(racket.getPositionX() + racket.getWidth());
		}
		
		if (racket.getPlayer() == 2) {
			setPositionX(racket.getPositionX() - getWidth());
		}
	}
	
}
