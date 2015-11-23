package pong.gui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Network {
	
	private Pong pong;
	
	private PongServerSocket pss; // Serveur créé 
	protected Set<PongClientSocket> setPs; // Ensemble contenant les sockets de connexion vers les autres serveurs
	
	private String localHost;
	private int localPort;
	
	/* Premier joueur */
	public Network(Pong pong) {	
		this.pong = pong;
		this.localPort = 7777;
		this.setPs = new HashSet<PongClientSocket>();
		this.pss = new PongServerSocket(this);
		pss.initServer();
	}
	
	/* Les autres joueurs */
	public Network(Pong pong, String host, int port) {
		this.pong = pong;
		this.localPort = 7778;
		this.setPs = new HashSet<PongClientSocket>();
		this.pss = new PongServerSocket(this);
		pss.initServer();

		/* On se connecte au joueur donné pour récupérer les informations sur la partie */
		PongClientSocket ps = new PongClientSocket(host, port);
		ps.connect();
		setPs.add(ps);
	}
	
	
	public String getLocalHost() {
		return this.localHost;
	}
	
	public int getLocalPort() {
		return this.localPort;
	}
	
	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}
	
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}		
	
	public String readFromAll() {
		String payload = null;
		Iterator<PongClientSocket> it = setPs.iterator();
		while(it.hasNext()) {
			PongClientSocket ps = it.next();
			payload = ps.readIn();
			
			/* On a reçu une requête */
			if (payload != null) {
				return payload;
			}
		}
		return payload;
	}
	
	public void sendToAll(String string) {
		Iterator<PongClientSocket> it = setPs.iterator();
		while(it.hasNext()) {
			PongClientSocket ps = it.next();
			ps.writeOut(string);
		}			
	}
	
	public void checkNewConnexion() {
		pss.checkNewConnexion();
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
			
			/* On envoie les informations du nouveau joueur à chaque joueur auquel on se connecte */
			ps.writeOut(pong.getLocalPlayerProtocol());
		}
	}
}
