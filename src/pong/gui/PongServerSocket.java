package pong.gui;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/* A REVOIR */

public class PongServerSocket {
	
	private ServerSocketChannel ssc;
	private Network network;
	
	public PongServerSocket(Network network) {
		this.network = network;
	}
			
	/** On utilise le système de channel avec select pour ne pas rendre le accept et le read bloquants et éviter d'utiliser les
	 * threads (problème de synchronisation)
	 */
	public void initServer() {
		try {
			ssc = ServerSocketChannel.open();
			ServerSocket listen = ssc.socket();
			listen.bind(new InetSocketAddress(network.getLocalPort()));
			network.setLocalHost(listen.getInetAddress().getHostAddress());
			ssc.configureBlocking(false);
		} catch(Exception e){
            System.err.println("Cannot create server. Invalid port error : " + network.getLocalPort());
            System.exit(1);
		}
	}
	
	public void checkNewConnexion() {
		try {
			SocketChannel sc = ssc.accept();
			if (sc != null) {
				Socket client = sc.socket();
				client.setTcpNoDelay(true);
				PongClientSocket ps = new PongClientSocket(client);
				network.setPs.add(ps);
			} 
		} catch (Exception e) {
			System.err.println("Cannot connect.");
		}
	}
}
