package pong.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;


public class PongClientSocket {

	private SocketChannel sc;
	private Socket socket;
	private String host;
	private int port;
	private ByteBuffer buf;
	
	public PongClientSocket(String host, int port) {
		this.host = host;
		this.port = port;
		this.buf = ByteBuffer.allocate(256);
		connect();
		disableNagleBlocking();
	}
	
	/* Utilisé pour instancier un PongClientSocket à partir d'une connexion déjà créée par le serveur */
	public PongClientSocket(SocketChannel sc) {
		this.sc = sc;
		this.socket = sc.socket();
		this.host = socket.getInetAddress().getHostAddress();
		this.port = socket.getPort();
		this.buf = ByteBuffer.allocate(256);
		disableNagleBlocking();
	}
	
	public SocketChannel getSocketChannel() {
		return this.sc;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void connect() {	
		try {
			this.sc = SocketChannel.open();
			this.socket = sc.socket();
			socket.connect(new InetSocketAddress(host, port));
			} catch (IOException e) {
				System.err.println("Cannot connect. Host :" + host + "\tPort : " + port);
	            System.exit(1);
			}
	}
	
	public void disableNagleBlocking() {
		try {
			sc.configureBlocking(false);
			socket.setTcpNoDelay(true);
		} catch (IOException e) {
			System.err.println("Cannot disable Nagle or set Socket Channel in unblocking mode.");
			System.exit(1);
		}
	}
		
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e){
            System.err.println("Could not close I/O Streams or socket.");
            System.exit(1);
		}
	}
	
	/* Read à modifier, ne lit pas forcement toute la requête */
	public String readIn() {
		String payload = new String();
		ByteBuffer bufReader = ByteBuffer.allocate(1);
		
		try {
			sc.read(bufReader);
			String c = convertBufToString(bufReader);
			while(!(c.equals("\n"))) {
				payload = payload + c;
				bufReader.clear();
				sc.read(bufReader);
				c = convertBufToString(bufReader);
			}
		} catch (IOException e) {
			System.err.println("Cannot read from server " + host);
			System.exit(1);
		}
		
		return payload;
	}
		
	
	public void writeOut(String payload) {
		try {
			payload = payload + "\n";
			buf.clear();
			buf.put(payload.getBytes("UTF-8"));
			buf.flip();
			
			while(buf.hasRemaining()) {
				sc.write(buf);
			}
		} catch (Exception e) {
			System.err.println("Cannot write to server " + host);
			System.exit(1);
		}
	}
	
	public String convertBufToString(ByteBuffer buffer) {
		String payload = null;
		
		buffer.flip();
		
		try {
		Charset charset = Charset.forName( "UTF-8" );
		CharsetDecoder decoder = charset.newDecoder();
		payload = decoder.decode(buffer).toString();
		} catch (CharacterCodingException e) {
			System.err.println("Cannot convert from ByteBuffer to String.");
			System.exit(1);
		}
		return payload;
	}
}
