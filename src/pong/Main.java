package pong;

import pong.gui.Window;
import pong.gui.Pong;

/**
 * Starting point of the Pong application
 */
public class Main  {
	
	/* javac Main host port */
	public static void main(String[] args) {
		Pong pong = new Pong();
		Window window = new Window(pong);
		window.displayOnscreen();
	}
}
