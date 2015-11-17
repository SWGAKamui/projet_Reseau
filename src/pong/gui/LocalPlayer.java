package pong.gui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LocalPlayer extends Player {
	
	private Pong pong;
	
	private PongServerSocket pss; // Serveur créé par le nouveau joueur
	protected Set<PongClientSocket> setPs; // Ensemble contenant les sockets de connexion vers les autres serveurs
	
	/* Premier joueur (local) */
	public LocalPlayer(Pong pong) {
		super(PlayerID.ONE);
		
		this.pong = pong;
				
		this.setPs = new HashSet<PongClientSocket>();
		this.pss = new PongServerSocket(this);
		pss.initServer();
	}
	
	/* Les autres joueurs (local) */
	public LocalPlayer(Pong pong, String host, int port) {
		super();
		
		this.pong = pong;
		
		this.setPs = new HashSet<PongClientSocket>();
		this.pss = new PongServerSocket(this);
		pss.initServer();
		
		/* On se connecte au joueur donné pour récupérer les informations sur la partie */
		PongClientSocket ps = new PongClientSocket(host, port);
		ps.connect();
		setPs.add(ps);
	}
	
	public String getInitialGameProtocol() {
		return pong.getInitialGameProtocol();
	}
		
	public PlayerID selectPlayerID() {
		int cpt = 0;
		Iterator<Player> it = pong.setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			cpt++;
		}
		switch (cpt) {
			case 0:
				return PlayerID.ONE;
			case 1:
				return PlayerID.TWO;
			case 2:
				return PlayerID.THREE;
			case 3:
				return PlayerID.FOUR;
			default:
				return PlayerID.FULL;
		}
	}
	
	public String readFromAll() {
		String payload = null;
		Iterator<PongClientSocket> it = setPs.iterator();
		while(it.hasNext()) {
			PongClientSocket ps = it.next();
			payload = ps.readBr();
		}
		return payload;
	}
	
	public void sendToAll(String string) {
		Iterator<PongClientSocket> it = setPs.iterator();
		while(it.hasNext()) {
			PongClientSocket ps = it.next();
			ps.writePs(string);
		}			
	}
	
	public void updateLocalPlayer() {
		this.connectToAll();
		this.setPlayerID(selectPlayerID());
		this.setRacket(new Racket(getPlayerID()));
	}
	
	/**
	 * On parcourt tous les joueurs, et on se connecte à chacun d'eux.
	 */
	public void connectToAll() {
		/* On ferme le socket qu'on avait initialement ouvert pour récupérer les données */
		Iterator<PongClientSocket> ite = setPs.iterator();
		while(ite.hasNext()) {
			PongClientSocket ps = ite.next();
			ps.disconnect();
		}			
		
		/* Puis on se connecte à tous les autres joueurs */
		Iterator<Player> it = pong.setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			
			String host = player.getHost();
			int port = player.getPort();
			
			PongClientSocket ps = new PongClientSocket(host, port);
			setPs.add(ps);
			
			ps.connect();
			
			/* On envoie les informations du nouveau joueur à chaque joueur auquel on se connecte */
			ps.writePs(pong.getLocalPlayerProtocol());
		}
	}
}
