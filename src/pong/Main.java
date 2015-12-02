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
		if (args.length == 1) {
			pong = new Pong(Integer.parseInt(args[0]));
		}
		else {
			pong = new Pong(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
		}
		Window window = new Window(pong);
		window.displayOnscreen();
	}
}
