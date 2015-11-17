package pong;

import pong.gui.Window;
import pong.gui.Pong;

/**
 * Starting point of the Pong application
 */
public class Main  {
	
	/* javac Main host port */
	public static void main(String[] args) {
		Pong pong;
		if (args.length == 0) {
			pong = new Pong();
		}
		else {
			pong = new Pong(args[0], Integer.parseInt(args[1]));
		}
		Window window = new Window(pong);
		window.displayOnscreen();
	}
}
