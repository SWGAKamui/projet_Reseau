package pong.gui;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.nio.channels.Selector;
import java.util.Iterator;

public class PongServerSocket {
	
	private ServerSocket listen;
	private LocalPlayer localPlayer;
	
	public PongServerSocket(LocalPlayer localPlayer) {
		this.localPlayer = localPlayer;
	}
	
	/* A supprimer ? */
	public ServerSocket getServerSocket() {
		return this.listen;
	}
		
	/** On utilise le système de channel avec select pour ne pas rendre le accept et le read bloquants et éviter d'utiliser les
	 * threads (problème de synchronisation)
	 */
	public void initServer() {
		try {
			ServerSocketChannel scc = ServerSocketChannel.open();
			listen = scc.socket();
			listen.bind(new InetSocketAddress(localPlayer.getPort()));
			localPlayer.setHost(listen.getInetAddress().getHostAddress());
			
			scc.configureBlocking(false);
			Selector selector = Selector.open();
			scc.register(selector, SelectionKey.OP_ACCEPT);
					
			/* Ajouter un délai de connexion initial, on sort de la boucle quand ce délai est passé */
			while(true) {
				selector.select(); // Attend collectivement sur toutes les connexions ouvertes
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {
					SelectionKey selectionKey = it.next();
					if (selectionKey.isAcceptable()) {
						Socket client = listen.accept();
						PongClientSocket ps = new PongClientSocket(client);
						localPlayer.setPs.add(ps);
						ps.writePs(localPlayer.getInitialGameProtocol());
					}
				}
			}
		} catch(Exception e){
            System.err.println("Cannot create server. Invalid port error : " + localPlayer.getPort());
            System.exit(1);
		}
	}
	
	public void checkNewConnexion() {
		
	}
}
