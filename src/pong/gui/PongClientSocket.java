package pong.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class PongClientSocket {

	private Socket socket;
	private String host;
	private int port;
	private BufferedReader br;
	private PrintStream ps;
	
	public PongClientSocket(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public PongClientSocket(Socket socket) {
		this.socket = socket;
		this.host = socket.getInetAddress().getHostAddress();
		this.port = socket.getPort();
	
		createStream();
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public void connect() {
		try {
		socket = new Socket(host, port);
		} catch (Exception e) {
			System.err.println("Cannot connect. Host :" + host + "\tPort : " + port);
            System.exit(1);
		}
		createStream();
	}
	
	public void disconnect() {
		try {
			br.close();
			ps.close();
			socket.close();
		} catch (IOException e){
            System.err.println("Could not close I/O Streams or socket");
            System.exit(1);
		}
	}
	
	public String readBr() {
	/*	try {
		SocketChannel sc = socket.getChannel();
		sc.configureBlocking(false);
		Selector selector = Selector.open();
		sc.register(selector, SelectionKey.OP_READ);
		if (key.isReadable()) {
			(SocketChannel) key.channel()
		}
		return br.readLine();	
		} catch(IOException e){
            System.err.println("Cannot read from server " + host);
            System.exit(1);
		}*/
		return null;
	}
	
	public void sendGameInfo() {
		
	}
	
	public void writePs(String message) {
		ps.println(message);
		ps.flush();
	}
	
	public void createStream() {
		try {
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			ps = new PrintStream(os, false, "utf-8");
		} catch(IOException e){
            System.err.println("Cannot create stream or buffer. Host :" + host + "\tPort : " + port);
            System.exit(1);
		}		
	}
}
