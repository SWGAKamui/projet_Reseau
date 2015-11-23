package pong.gui;

/**
 * Un joueur possède un identifiant parmis {ONE, TWO, THREE, FOUR}, un score et une raquette.
 * Un joueur est à la fois un client (pour tous les autres joueurs) et un serveur (car notre jeu fonctionne sur un principe de décentralisation)
 * Chaque joueur doit donc se connecter à tous les serveurs (il y a donc un ensemble qui contient chaque socket de connexion à un autre joueur)
 */

public class Player {
	
	private PlayerID playerID;
	private int score;
	private Racket racket;
	private String host;
	private int port;
	
	/* Utilisé pour instancier le premier joueur local */
	public Player(PlayerID playerID, String host, int port) {
		this.playerID = playerID;
		this.score = 0;
		this.racket = new Racket(playerID);
		this.host = host;
		this.port = port;
	}
		
	/* Utilisé pour instancier les joueurs distants */
	public Player(PlayerID playerID, int score, int positionRacketX, int positionRacketY, String host, int port) {
		this.playerID = playerID;
		this.score = score;
		this.racket = new Racket(playerID, positionRacketX, positionRacketY);
		this.host = host;
		this.port = port;
	}
		
	public PlayerID getPlayerID() {
		return this.playerID;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public Racket getRacket() {
		return (Racket) this.racket.clone();
	}
	
	public String getHost() {
		return this.host;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void setPlayerID(PlayerID playerID) {
		this.playerID = playerID;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setRacket(Racket racket) {
		this.racket = (Racket) racket.clone();
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void increaseScore() {
		this.score++;
	}
}
