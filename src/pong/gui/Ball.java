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
		
		if (getPosition().x < 0) {
			setPosition(new Point(0, getPosition().y));
			this.speed.x = -this.speed.x;
		}
		
		if (getPosition().y < 0) {
			setPosition(new Point(getPosition().x, 0));
			this.speed.y = -this.speed.y;
		}
		
		if (getPosition().x > SIZE_PONG_X - getWidth()) {
			setPosition(new Point(getPosition().x - getWidth(), getPosition().y));
			this.speed.x = -this.speed.x;
		}
		
		if (getPosition().y > SIZE_PONG_Y - getHeight()) {
			setPosition(new Point(getPosition().x, getPosition().y - getHeight()));
			this.speed.y = -this.speed.y;
		}		
	}
}
