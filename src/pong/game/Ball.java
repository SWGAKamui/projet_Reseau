package pong.game;

import java.awt.Point;
import java.util.Iterator;
import java.util.Set;

import pong.menu.Son;

public class Ball extends PongItem {
	
	/**
	 * Path of the ball img
	 */
	public static final String PATH_BALL = "res/image/death_star.png";
	
	/**
	 * Speed of ball (in pixels per second)
	 */
	private static final int BALL_SPEED = 2;
		
	private Point speed;
		
	public Ball() {
		super(PATH_BALL);
		this.speed = new Point(BALL_SPEED, BALL_SPEED);
		
		/* On place la balle au milieu de l'écran par défaut */
		setPosition(new Point(SIZE_PONG_X/2, SIZE_PONG_Y/2));
	}
	
	public Object clone() {
		Ball ball = (Ball) super.clone();
		ball.speed = (Point) speed.clone();
		return ball;
	}
		
	public Point getSpeed() {
		return (Point) this.speed.clone();
	}
	
	public int getSpeedX() {
		return this.speed.x;
	}
	
	public int getSpeedY() {
		return this.speed.y;
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
			setPositionX(getPositionX());
			this.speed.x = -this.speed.x;
		}
		
		if (getPositionY() > SIZE_PONG_Y - getHeight()) {
			setPositionY(getPositionY());
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
			if (player.getPlayerID() != playerID) {
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
				setCollision(racket);
			}
		}
	}
	
	/**
	 * Met à jour la position et la vitesse de la balle après un rebond sur la raquette
	 */
	public void setCollision(Racket racket) {
		Son audio = new Son("res/sound/collision.wav");
		audio.play();
		
		switch (racket.getPlayerID()) {
			case ONE:
				setPositionX(racket.getPositionX() + racket.getWidth());
				this.speed.x = -this.speed.x;
				break;
		
			case TWO:
				setPositionX(racket.getPositionX() - getWidth());
				this.speed.x = -this.speed.x;
				break;
				
			case THREE:
				setPositionY(racket.getPositionY() + racket.getHeight());
				this.speed.y = -this.speed.y;
				break;
				
			case FOUR:
				setPositionY(racket.getPositionY() - getHeight());
				this.speed.y = -this.speed.y;
				break;
				
			default:
				break;
		}
	}	
}
