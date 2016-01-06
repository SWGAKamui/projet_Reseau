package pong.network;

import java.awt.Point;
import java.util.Iterator;

import javax.swing.JOptionPane;

import pong.game.*;


/* Code à factoriser */

public class ProtocolHandler {
	
	private String[] tab;
	private Pong pong;
		
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
				protocolInitGame();		
			case "gameinfo":
				protocolGameInfo();
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
	
	public void protocolInitGame() {
		StringBuffer sb = new StringBuffer();
		sb.append("gameinfo" + "," + getBallInfo(pong.getBall()));
		
		Iterator<Player> it = pong.setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			sb.append("," + getNetworkPlayerInfo(player));
		}					
		pong.getNetwork().sendToAll(sb.toString());
	}
	
	public void protocolGameInfo() {
		/* On vérifie que c'est bien la première fois qu'on reçoit les informations (pour cela on regarde 
		 * si l'ensemble des joueurs est vide) */
		if (pong.setPlayers.isEmpty()) {
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
		
		/* Anti-cheat */
		if ((positionBallX > (ball.getPositionX() + ball.getSpeedX()) && ball.getSpeedX() > 0) || 
			(positionBallX < (ball.getPositionX() + ball.getSpeedX()) && ball.getSpeedX() < 0) ||
			(positionBallY > (ball.getPositionY() + ball.getSpeedY()) && ball.getSpeedY() > 0) || 
			(positionBallY < (ball.getPositionY() + ball.getSpeedY()) && ball.getSpeedY() < 0)) {
			errorCheat("Balle avec coordonnées invalides (cheat)");
		}
		
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
		
		/* On met à jour la position du joueur */
		Iterator<Player> it = pong.setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			if (player.getPlayerID() == playerID) {
				Racket racket = player.getRacket();
				
				/* Anti-cheat */
				if ((positionRacketX > (racket.getPositionX() + racket.getSpeed()) && racket.getSpeed() > 0) || 
					(positionRacketX < (racket.getPositionX() + racket.getSpeed()) && racket.getSpeed() < 0) ||
					(positionRacketY > (racket.getPositionY() + racket.getSpeed()) && racket.getSpeed() > 0) || 
					(positionRacketY < (racket.getPositionY() + racket.getSpeed()) && racket.getSpeed() < 0)) {
						errorCheat("Raquette avec coordonnées invalides (cheat)");
					}
				
				racket.setPosition(new Point(positionRacketX, positionRacketY));
				player.setRacket(racket);
			}
		}
	}
	
	/* TRAITEMENT EN ENVOI */

	/* Dans les méthodes suivantes, le passage d'un player en paramètre est volontaire afin de rendre 
	 * le protocole indépendant du reste du code
	 */
	
	public String getPlayerInfo(Player player) {
		return (player.getPlayerID() + ";" +
				player.getScore() + ";" +
				player.getRacket().getPositionX() + ";" +
				player.getRacket().getPositionY());
	}
	
	public String getNetworkPlayerInfo(Player player) {
		return (getPlayerInfo(player) + ";" +
				player.getHost() + ";" +
				player.getPort());
	}
	
	public String getBallInfo(Ball ball) {
		return (ball.getPositionX() + "," + 
				ball.getPositionY());
	}
	
	/**
	 * Send position of the racket of the local player and position of the ball (if it is in control area of local player)
	 */	
	public void sendNewInfoProtocol() {
		pong.getNetwork().sendToAll("player" + "," + getPlayerInfo(pong.getLocalPlayer()));
		
		if (pong.getLocalPlayer().getPlayerID() == PlayerID.ONE) {
			StringBuffer sb = new StringBuffer();
			sb.append("ball" + "," + getBallInfo(pong.getBall()));
			
			/* On ajoute aussi les scores */
			Iterator<Player> it = pong.setPlayers.iterator();
			while(it.hasNext()) {
				Player player = it.next();
				sb.append("," + getPlayerInfo(player));
			}
			pong.getNetwork().sendToAll(sb.toString());
		}
	}
	
	public void initGame() { 
		/* On indique au joueur auquel on vient de se connecter qu'on veut récupérer les informations sur la partie */
		this.initGameInfo();
		
		/* On récupère les informations de la partie (requête "gameinfo") */
		String payload = pong.receiveNewInfo();
		while (payload == null || !(payload.split(",")[0].equals("gameinfo"))) {
			payload = pong.receiveNewInfo();
		}
		
		/* Puis on met celle-ci à jour */
		this.setPayload(payload);
		this.run();
	}
	
	/**
	 * Used to warn other player that you want to get initial game info (first time)
	 * Actually sendToAll send it to one player (the one you connected to)
	 */
	public void initGameInfo() {
		pong.getNetwork().sendToAll("init");
	}
	
	/**
	 * This method is used to warn other players that this player is new in the game
	 */
	public String getLocalPlayerProtocol() {
		return ("newplayer" + "," + getNetworkPlayerInfo(pong.getLocalPlayer()));
	}

	public void errorCheat(String message) {
		JOptionPane.showMessageDialog(null, "Tentative de triche : " + message, "Pong", JOptionPane.PLAIN_MESSAGE);
	}
}
			
