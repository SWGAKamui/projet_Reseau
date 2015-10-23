package pong.gui;

import java.awt.Point;

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
	
	/* Update ball position */
	public void animateBall() {
		Point pos = getPosition(); // pos is a local variable
		pos.translate(speed.x, speed.y);
		setPosition(pos);
		
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
}
