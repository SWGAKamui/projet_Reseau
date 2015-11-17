package pong.gui;

import java.awt.Point;
import java.util.Iterator;

/* Code à factoriser */

public class ProtocolHandler {
	
	String[] tab;
	Pong pong;
		
	public ProtocolHandler(Pong pong) {
		this.pong = pong;
	}
	
	/* TRAITEMENT EN RECEPTION */
	
	public void setPayload(String payload) {
		this.tab = payload.split(",");
	}
	
	public void run() {
		switch (tab[0]) {
			case "init":
				protocolInit();
				break;
			case "newplayer":
				protocolNewPlayer();
				break;
			case "ball":
				protocolBall();
				break;
			case "player":
				protocolPlayer();
				break;
			default:
				System.err.println("Protocole invalide : " + tab[0]);
				break;
		}		
	}
	
	public void protocolInit() {
		/* On place la balle à la bonne position */
		int positionBallX = Integer.parseInt(tab[1]);
		int positionBallY = Integer.parseInt(tab[2]);
		Ball ball = pong.getBall();
		ball.setPosition(new Point(positionBallX, positionBallY));
		pong.setBall(ball);
		
		/* On instancie tous les joueurs distants */
		for(int i = 3 ; i < tab.length ; i++) {
			String[] curPlayer = tab[i].split(";");
			PlayerID playerID = PlayerID.valueOf(curPlayer[0]);
			int score = Integer.parseInt(curPlayer[1]);
			int positionRacketX = Integer.parseInt(curPlayer[2]);
			int positionRacketY = Integer.parseInt(curPlayer[3]);
			String host = curPlayer[4];
			int port = Integer.parseInt(curPlayer[5]);
			
			Player player = new Player(playerID, score, positionRacketX, positionRacketY, host, port);
			pong.setPlayers.add(player);
		}
	}
	
	public void protocolNewPlayer() {
		/* On instancie un nouveau joueur distant */
		String[] curPlayer = tab[1].split(";");
		PlayerID playerID = PlayerID.valueOf(curPlayer[0]);
		int score = Integer.parseInt(curPlayer[1]);
		int positionRacketX = Integer.parseInt(curPlayer[2]);
		int positionRacketY = Integer.parseInt(curPlayer[3]);
		String host = curPlayer[4];
		int port = Integer.parseInt(curPlayer[5]);
		
		Player player = new Player(playerID, score, positionRacketX, positionRacketY, host, port);
		pong.setPlayers.add(player);
	}
	
	public void protocolBall() {
		/* On met à jour la position de la balle */
		int positionBallX = Integer.parseInt(tab[1]);
		int positionBallY = Integer.parseInt(tab[2]);
		Ball ball = pong.getBall();
		ball.setPosition(new Point(positionBallX, positionBallY));
		pong.setBall(ball);
		
		/* On met à jour les scores */
		for(int i = 3 ; i < tab.length ; i++) {
			String[] curPlayer = tab[i].split(";");
			PlayerID playerID = PlayerID.valueOf(curPlayer[0]);
			int score = Integer.parseInt(curPlayer[1]);
			
			Iterator<Player> it = pong.setPlayers.iterator();
			while(it.hasNext()) {
				Player player = it.next();
				if (player.getPlayerID() == playerID) {
					player.setScore(score);
				}
			}
		}
	}
	
	public void protocolPlayer() {
		String[] curPlayer = tab[1].split(";");
		PlayerID playerID = PlayerID.valueOf(curPlayer[0]);
		int positionRacketX = Integer.parseInt(curPlayer[2]);
		int positionRacketY = Integer.parseInt(curPlayer[3]);
		
		Iterator<Player> it = pong.setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			if (player.getPlayerID() == playerID) {
				Racket racket = player.getRacket();
				racket.setPosition(new Point(positionRacketX, positionRacketY));
				player.setRacket(racket);
			}
		}
	}
	
	/* TRAITEMENT EN ENVOI */

	/**
	 * Send position of the racket of the local player and position of the ball (if it is in control area of local player)
	 */	
	public void sendNewInfoProtocol() {
		pong.getLocalPlayer().sendToAll("player" + "," + pong.getLocalPlayer().getPlayerInfo());
		
		if (pong.getLocalPlayer().getPlayerID() == PlayerID.ONE) {
			StringBuffer sb = new StringBuffer();
			sb.append("ball" + "," + 
					   pong.getBall().getPositionX() + "," + 
					   pong.getBall().getPositionY());
			/* On ajoute aussi les scores */
			Iterator<Player> it = pong.setPlayers.iterator();
			while(it.hasNext()) {
				Player player = it.next();
				sb.append("," + player.getPlayerInfo());
			}
			
			pong.getLocalPlayer().sendToAll(sb.toString());
		}
	}
	
	/**
	 * This method is used when a player is connecting to another player for the first time
	 */
	public String getInitialGameProtocol() {
		StringBuffer sb = new StringBuffer();
		sb.append("init" + "," +
				   pong.getBall().getPositionX() + "," + 
				   pong.getBall().getPositionY());
		Iterator<Player> it = pong.setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			sb.append("," + player.getNetworkPlayerInfo());
		}					
		return sb.toString();
	}
	
	/**
	 * This method is used to warn other players that this player is new in the game
	 */
	public String getLocalPlayerProtocol() {
		return ("newplayer" + "," + pong.getLocalPlayer().getNetworkPlayerInfo());
	}
}
			
