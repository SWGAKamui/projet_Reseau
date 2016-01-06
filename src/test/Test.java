package test;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import pong.game.*;

public class Test {	
	public static void Display_result (Boolean cond){
		System.out.print("..................................");
		if(cond){
			System.out.println("Passed");
		}
		else{
			System.out.println("Failed");
		}
		System.out.println("\n");
	}
	
	public static void main(String[] args) {
		
		int SIZE_PONG_X = 800;
		Ball ball = new Ball();
		Racket racket = new Racket (PlayerID.ONE);
		int pos = 5;
		
		 
		Player player1 = new Player(PlayerID.ONE, "0:0:0:0:0:0:0:0", 7777); 
		Player player2 = new Player(PlayerID.TWO, "0:0:0:0:0:0:0:0", 7778);
		
		
		Set<Player> setPlayers;
		setPlayers = new HashSet<Player>();

		setPlayers.add(new Player(PlayerID.ONE, null, 0));
		setPlayers.add(player1);
		setPlayers.add(player2);
		
		int initscore = 0;
		
		racket.setPositionY(pos);
		ball.setPositionX(racket.getPositionX());
		ball.setPositionY(pos);
		double ball_speed_X = ball.getSpeed().getX();
		double ball_speed_Y = ball.getSpeed().getY();

		
		System.out.println("Racket pos Y == " + pos +" : ");
		Display_result(racket.getPositionY() == pos);
		
		System.out.println("Racket pos X == 0 : ");
		Display_result(racket.getPositionX() == 0);
		
				
		System.out.println("Racket pos X == Ball pos X : ");
		Display_result(racket.getPositionX() == ball.getPositionX());
		
		System.out.println("Ball pos Y == " + pos +" : ");
		Display_result(ball.getPositionY() == pos);
		
		System.out.println("Racket pos Y == Ball pos Y : ");
		Display_result(ball.getPositionY() == racket.getPositionY());
		
		System.out.println("Player 1 score == " + player1.getScore() +" : ");
		Display_result(player1.getScore() == initscore);
		
		System.out.println("Player 2 score == " + player2.getScore() +" : ");
		Display_result(player2.getScore() == initscore);
		
		ball.animateBall(setPlayers);	
		
		System.out.println("Ball/Racket collision ? : ");
		Display_result(ball.getHitBox().intersects(racket.getHitBox()) );
		
		System.out.println("Ball speed X == " + ball_speed_X +" : ");
		Display_result(ball.getSpeed().getX() == ball_speed_X);

		System.out.println("Ball speed Y == " + ball_speed_Y +" : ");
		Display_result(ball.getSpeed().getY() == ball_speed_Y);
		
		Point point = ball.getSpeed();
		point.setLocation(- point.getX(), - point.getY()); 
		
		racket.setPositionY(50);
		ball.setPositionX(0);
		ball.setSpeed(point);
		ball.animateBall(setPlayers);
		
		
		System.out.println("update score player two : ");
		Display_result(player2.getScore() != initscore);
		
		ball.setPositionX(SIZE_PONG_X - ball.getWidth());
		ball.animateBall(setPlayers);
		
		System.out.println("update score player one : ");
		Display_result(player1.getScore() != initscore);
		


	}

}
