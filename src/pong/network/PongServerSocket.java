package pong.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class PongServerSocket {
	
	private ServerSocketChannel ssc;
	private String localHost;
	private int localPort;
	
	public PongServerSocket(int localPort) {
		this.localPort = localPort;
		initServer();
	}
	
	public ServerSocketChannel getServerSocketChannel() {
		return this.ssc;
	}
	
	public String getLocalHost() {
		return this.localHost;
	}
	
	public void initServer() {
		try {
			this.ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ServerSocket listen = ssc.socket();
			listen.bind(new InetSocketAddress(localPort));
			this.localHost = listen.getInetAddress().getHostAddress();
		} catch(Exception e){
            System.err.println("Cannot create server. Invalid port error : " + localPort);
            System.exit(1);
		}
	}
	
	public SocketChannel accept() {
		SocketChannel sc = null;
		try {
			sc = ssc.accept();
			sc.configureBlocking(false);
		} catch (IOException e) {
			System.err.println("Cannot connect (accept failed).");
			System.exit(1);
		}
		return sc;
	}
}
