package pong.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

/* A REVOIR */

public class PongClientSocket {

	private Socket socket;
	private String host;
	private int port;
	private BufferedReader br;
	private PrintStream ps;
	
	public PongClientSocket(String host, int port) {
		this.host = host;
		this.port = port;
		connect();
		createStream();
		}
	
	/* Utilisé pour les connexions créées par le serveur */
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
			this.socket = new Socket(host, port);
			socket.setTcpNoDelay(true);
			} catch (IOException e) {
				System.err.println("Cannot connect. Host :" + host + "\tPort : " + port);
	            System.exit(1);
			}
	}
		
	public void disconnect() {
		try {
			ps.close();
			br.close();
			socket.close();
		} catch (IOException e){
            System.err.println("Could not close I/O Streams or socket.");
            System.exit(1);
		}
	}
	
	public String readIn() {
		String payload = null;
		try {
			payload = br.readLine();
		} catch (IOException e) {
			System.err.println("Cannot read from server " + host);
			System.exit(1);
		}
		return payload;
	}
		
	public void writeOut(String payload) {
		System.out.println("Envoi :" + payload);
		ps.println(payload);
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
