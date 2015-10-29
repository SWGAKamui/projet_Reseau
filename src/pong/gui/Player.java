package pong.gui;

/**
 * Un joueur possède un identifiant parmis {ONE, TWO, THREE, FOUR}, un score et une raquette.
 */

public class Player {
	
	public PlayerID playerID;
	public int score;
	public Racket racket;
	
	public Player(PlayerID playerID) {
		this.playerID = playerID;
		this.score = 0;
		this.racket = new Racket("image/racket.png", playerID);
	}
	
	public Racket getRacket() {
		return (Racket) this.racket.clone();
	}
	
	public void setRacket(Racket racket) {
		this.racket = (Racket) racket.clone();
	}
	
	public void increaseScore() {
		this.score++;
	}

}
