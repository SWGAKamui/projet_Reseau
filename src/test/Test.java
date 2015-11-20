package test;

import java.util.HashSet;
import java.util.Set;

import pong.gui.Ball;
import pong.gui.Player;
import pong.gui.PlayerID;
import pong.gui.Racket;

public class Test {	
	public static void main(String[] args) {
		Ball ball = new Ball();
		Racket racket = new Racket (PlayerID.ONE);
		Set<Player> setPlayers;
		setPlayers = new HashSet<Player>();
		setPlayers.add(new Player(PlayerID.ONE));
		
		racket.setPositionY(5);
		ball.setPositionX(0);
		ball.setPositionY(5);
		double ball_speed_X = ball.getSpeed().getX();
		double ball_speed_Y = ball.getSpeed().getY();
		
		
		if(racket.getPositionX() == ball.getPositionX()){
			if(racket.getPositionY() == ball.getPositionY()){
				ball.animateBall(setPlayers);
				if((racket.getPositionX() + racket.getWidth()) == ball.getPositionX()){
					if(ball.getSpeed().getX() == (- ball_speed_X)){
						if(ball.getSpeed().getY() == (- ball_speed_Y)){
							System.out.println("Tout va bien");
						}
						else{
							System.out.println("SpeedY erreur");
						}
					}	
					else{
						System.out.println("SpeedX erreur");
					}
				}
				else{
					System.out.println("Nouvelle Position X erreur");
				}
			}
			else{
				System.out.println("Position de base Y erreur");
			}
		}
		else{
			System.out.println("Position de base X erreur");
		}
	}

}
