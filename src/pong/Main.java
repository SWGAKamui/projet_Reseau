package pong;

import pong.gui.Window;
import pong.menu.Son;
import pong.menu.Comment;
import pong.menu.Menu;
import pong.game.Pong;


/**
 * Starting application
 */
public class Main  {
    
    /**
     * Starting pong
     */
    public static Pong startPong(String[] args) {
	Pong pong;
	if (args.length == 1) {
	    pong = new Pong(Integer.parseInt(args[0]));
	}
	else {
	    pong = new Pong(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
	}
	return pong;
    }
	
	
    /* javac Main host port */
    public static void main(String[] args) {
	Pong pong;
	Window window;
	boolean pressedMain = false;
	boolean pressedComment = false;
	boolean pressedMenu = false;
	Comment comment = new Comment();

	Menu menu = new Menu();
	menu.print();

		
	Son audio = new Son("res/sound/main_theme.wav");
	audio.play();
		
	while(true){
	    try {
		Thread.sleep(1);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    pressedMenu = comment.getPressed();
	    pressedMain = menu.getPressedPlay();
	    pressedComment = menu.getPressedHow();

	    if(pressedMain) {				
		pressedMain = false;
		menu.setPressedHow(false);
		menu.setPressedPlay(false);
		menu.dispose();
		audio.stop();
		pong = startPong(args);
		window = new Window(pong);
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
	}
    }
}
