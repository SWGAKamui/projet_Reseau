package pong;

import pong.gui.Window;
import pong.gui.Menu;
import pong.gui.PlayerID;
import pong.gui.Pong;

/**
 * Starting point of the Pong application
 */
public class Main  {
	
	/* javac Main host port */
	public static void main(String[] args) {
		boolean pressed = false;
		Pong pong = new Pong();
		
		Window window = new Window(pong);
		Menu menu = new Menu();
		
		while(true){
			if(pressed){	
				menu.dispose();
				window.displayOnscreen();			
			}
			pressed = menu.getPressed();
		}
	}
}
