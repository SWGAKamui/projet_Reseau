package pong;

import pong.gui.Window;
import son.Son;
import pong.gui.Comment;
import pong.gui.Menu;
import pong.gui.Pong;



/**
 * Starting point of the Pong application
 */
public class Main  {
	
	/* javac Main host port */
	public static void main(String[] args) {
		boolean pressedMain = false;
		boolean pressedComment = false;
		boolean pressedMenu = false;
		Comment comment = new Comment();;
		Pong pong = new Pong();

		Window window = new Window(pong);
		Menu menu = new Menu();
		menu.print();
		
		
		Son audio = new Son("src/son/Transistor.wav");
		audio.play();
		while(true){
			pressedMenu = comment.getPressed();
			pressedMain = menu.getPressedPlay();
			pressedComment = menu.getPressedHow();
			if(pressedMain){				
				pressedMain = false;
				menu.setPressedHow(false);
				menu.setPressedPlay(false);
				menu.dispose();
				audio.stop();
				window.displayOnscreen();
				
				
			}

			if(pressedComment){	
				pressedComment = false;
				menu.setPressedHow(false);
				
				menu.dispose();
				comment.print();		
			}
			if(pressedMenu){
				pressedMenu = false;
				comment.setPressed(false);
				comment.dispose();
				menu.print();		
			}
			System.out.println("OK!");
			
		}

	}
}
