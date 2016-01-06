package pong.network;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import pong.game.Player;
import pong.game.Pong;

/** On utilise le système de channel avec select pour ne pas rendre le accept et le read bloquants et éviter d'utiliser les
 * threads (problème de synchronisation)
 */

public class Network {
	
	private Pong pong;
	
	private PongServerSocket pss; // Serveur créé 
	protected Set<PongClientSocket> setPs; // Ensemble contenant les sockets de connexion vers les autres serveurs
	private Selector selector;
	private PongClientSocket initConnexion;
	
	private String localHost;
	private int localPort;
	
	/* Premier joueur */
	public Network(Pong pong, int localPort) {
		this.pong = pong;
		this.localPort = localPort;
		this.setPs = new HashSet<PongClientSocket>();
		
		try {
			this.selector = Selector.open();
		} catch (IOException e) {
			System.err.println("Cannot create selector.");
			System.exit(1);
		}
		
		this.pss = new PongServerSocket(localPort);
		this.localHost = pss.getLocalHost();
		bindServerSocketChannel(pss.getServerSocketChannel());
	}
	
	/* Les autres joueurs */
	public Network(Pong pong, int localPort, String host, int port) {
		this.pong = pong;
		this.localPort = localPort;
		this.setPs = new HashSet<PongClientSocket>();
		
		try {
			this.selector = Selector.open();
		} catch (IOException e) {
			System.err.println("Cannot create selector.");
			System.exit(1);
		}
		
		this.pss = new PongServerSocket(localPort);
		this.localHost = pss.getLocalHost();
		bindServerSocketChannel(pss.getServerSocketChannel());

		/* On se connecte au joueur donné pour récupérer les informations sur la partie */
		PongClientSocket ps = new PongClientSocket(host, port);
		bindSocketChannel(ps.getSocketChannel());
		this.setPs.add(ps);
		this.initConnexion = ps;
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
	
	public String receiveNewInfo() {
		String payload = null;
		
		try {
			selector.selectNow();
		} catch (IOException e) {
			System.err.println("Cannot selectNow.");
			System.exit(1);
		}
		
		Set<SelectionKey> keys = selector.selectedKeys();
		Iterator<SelectionKey> it = keys.iterator();
		while (it.hasNext()) {
			SelectionKey key = it.next();
			it.remove();
			
			/* Check d'une nouvelle connexion */
			if (key.isAcceptable()) {
				SocketChannel sc = pss.accept();
				bindSocketChannel(sc);
				PongClientSocket ps = new PongClientSocket(sc);
				setPs.add(ps);
			}
			
			/* Check d'une nouvelle requête à lire */
			if (key.isReadable()) {
				SocketChannel sc = (SocketChannel) key.channel();
				payload = readRequest(sc);
				/* On a pu lire notre requête, on la retourne donc directement pour la traiter */
				break;
			}
		}
		return payload;
	}
	
	public void bindSocketChannel(SocketChannel sc) {
		try {
			sc.register(selector, SelectionKey.OP_READ);
		} catch (ClosedChannelException e) {
			System.err.println("Cannot bind SocketChannel to selector : " + sc);
			System.exit(1);
		}
	}
	
	public void bindServerSocketChannel(ServerSocketChannel ssc) {
		try {
			ssc.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			System.err.println("Cannot bind ServerSocketChannel to selector : " + ssc);
			System.exit(1);
		}
	}
	
	/* On parcourt l'ensemble des PongClientSocket jusqu'à trouver le socket recherché, et on lit dessus */
	public String readRequest(SocketChannel sc) {
		String payload = null;
		Iterator<PongClientSocket> it = setPs.iterator();
		while(it.hasNext()) {
			PongClientSocket ps = it.next();
			if (ps.getSocketChannel() == sc) {
				payload = ps.readIn();
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
	
	public void disconnectInitConnexion() {
		Iterator<PongClientSocket> ite = setPs.iterator();
		while(ite.hasNext()) {
			PongClientSocket ps = ite.next();
			ps.disconnect();	
			setPs.remove(ps);	
			System.out.println(setPs.toString());
		}
	}
	
	/**
	 * On parcourt tous les joueurs, et on se connecte à chacun d'eux.
	 */
	public void connectToAll() {
		/* On ferme le socket qu'on avait initialement ouvert pour récupérer les données */ 
		//disconnectInitConnexion();
		
		/* On se connecte à tous les autres joueurs sauf celui auquel on s'était initialement connecté pour récupérer les données */
		String initHost = initConnexion.getHost();
		int initPort = initConnexion.getPort();
		PongClientSocket ps;
		
		Iterator<Player> it = pong.setPlayers.iterator();
		while(it.hasNext()) {
			Player player = it.next();
			
			String host = player.getHost();
			int port = player.getPort();
			
			if ((host != initHost) && (port != initPort)) {
				ps = new PongClientSocket(host, port);
				bindSocketChannel(ps.getSocketChannel());
				setPs.add(ps);
			}
			else {
				ps = initConnexion;
			}
			
			/* On envoie les informations du nouveau joueur à chaque joueur auquel on se connecte */
			ps.writeOut(pong.getLocalPlayerProtocol());
		}
	}
}
