package pong.gui;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public abstract class PongItem implements Cloneable {
	/**
	 * Width of pong area
	 */
	protected static final int SIZE_PONG_X = 800;
	/**
	 * Height of pong area
	 */
	protected static final int SIZE_PONG_Y = 600;
	
	/**
	 * PongItem to be displayed
	 */
	private Image img;
	
	/**
	 * icon related to img
	 */
	private ImageIcon icon;
	
	/**
	 * Width of PongItem in pixels
	 */
	private int width;
	
	/**
	 * Height of PongItem in pixels
	 */
	private int height;
	
	/**
	 * Position of PongItem
	 */
	private Point position;
		
	public PongItem(String path) {
		this.img = Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource(path));
		this.icon = new ImageIcon(img);		
		this.width = icon.getIconWidth();
		this.height = icon.getIconHeight();
	}
	
	public Object clone() {
		PongItem pongItem = null;
		try {
			pongItem = (PongItem) super.clone();
			pongItem.position = (Point) position.clone();
			return pongItem;			
		} catch(CloneNotSupportedException cnse) {
			System.err.println(cnse);
		}
		return pongItem;					
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight () {
		return this.height;
	}
	
	public Image getImg() {
		return this.img;
	}
	
	public Point getPosition() {
		return (Point) this.position.clone();
	}
	
	public int getPositionX() {
		return this.position.x;
	}
	
	public int getPositionY() {
		return this.position.y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setPosition(Point position) {
		this.position = (Point) position.clone();
	}
	
	public void setPositionX(int x) {
		this.position.x = x;
	}
	
	public void setPositionY(int y) {
		this.position.y = y;
	}
	
	public Rectangle getHitBox() {
		return new Rectangle(position.x, position.y, width, height);
	}
}
