package edu.up.cs301.pusoydos;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Contains the state of a Slapjack game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 * 
 * @author Steven R. Vegdahl 
 * @version July 2013
 */
public class SJState extends GameState
{
	private static final long serialVersionUID = -8269749892027578792L;

    ///////////////////////////////////////////////////
    // ************** instance variables ************
    ///////////////////////////////////////////////////

	// the three piles of cards:
    //  - 0: pile for player 0
    //  - 1: pile for player 1
    //  - 2: the "up" pile, where the top card
	// Note that when players receive the state, all but the top card in all piles
	// are passed as null.
    private Deck[] piles;
    private int[] pileSizes;

    // whose turn is it to turn a card?
    private int turnNum;

	private int perspective;

    /**
     * Constructor for objects of class SJState. Initializes for the beginning of the
     * game, with a random player as the first to turn card
     *  
     */
    public SJState() {

		perspective = 4;

    	// randomly pick the player who starts
    	turnNum = (int)(4*Math.random());
    	
    	// initialize the decks as follows:
    	// - each player deck (#0 and #1) gets half the cards, randomly
    	//   selected
    	// - the middle deck (#2) is empty
    	piles = new Deck[5];
    	piles[0] = new Deck(); // create empty deck
    	piles[1] = new Deck(); // create empty deck
    	piles[2] = new Deck(); // create empty deck
		piles[3] = new Deck(); // create empty deck
		piles[4] = new Deck(); // create empty deck
    	piles[0].add52(); // give all cards to player whose turn it is, in order
    	piles[0].shuffle(); // shuffle the cards


    	// move cards to opponents, until to piles have ~same size
    	while (piles[0].size() >= 14 ){

			piles[0].moveTopCardTo(piles[1]);
			piles[0].moveTopCardTo(piles[2]);
			piles[0].moveTopCardTo(piles[3]);

    	}
    }
    
    /**
     * Copy constructor for objects of class SJState. Makes a copy of the given state
     *  
     * @param orig  the state to be copied
     */
    public SJState(SJState orig, int playerNum) {
    	// set index of player whose turn it is
    	turnNum = orig.turnNum;
    	// create new deck array, making copy of each deck
    	piles = new Deck[5];


    	piles[playerNum] = new Deck(orig.piles[playerNum]);


		pileSizes = new int[5];
        for( int i = 0; i < 5; i++ ){

            pileSizes[i] = orig.piles[i].size();
			Log.i("Sizes",orig.piles[i].size()+"");

        }

		perspective = playerNum;



    }
    
    /**
     * Gives the given deck.
     * 
     * @return  the deck for the given player, or the middle deck if the
     *   index is 2
     */
    public Deck getDeck(int num) {
        if (num < 0 || num > 4) return null;
        return piles[num];
    }
    
    /**
     * Tells which player's turn it is.
     * 
     * @return the index (0 or 1) of the player whose turn it is.
     */
    public int toPlay() {
        return turnNum;
    }
    
    /**
     * change whose move it is
     * 
     * @param idx
     * 		the index of the player whose move it now is
     */
    public void setToPlay(int idx) {
    	turnNum = idx;
    }
 
    /**
     * Replaces all cards with null, except for the top card of deck 2
     */
    public void nullAllButTopOf2() {
    	// see if the middle deck is empty; remove top card from middle deck
    	boolean empty2 = piles[2].size() == 0;
    	Card c = piles[2].removeTopCard();
    	
    	// set all cards in deck to null
    	for (Deck d : piles) {
    		d.nullifyDeck();
    	}
    	
    	// if middle deck had not been empty, add back the top (non-null) card
    	if (!empty2) {
    		piles[2].add(c);
    	}
    }

    public String toString() {
		String gameInfo = "";
		if( perspective != 4 ) {
			gameInfo =  "Your cards: " + piles[perspective].toString() + "\n"
					+ "Player 1 has " + pileSizes[0] + " cards remaining. \n"
					+ "Player 2 has " + pileSizes[1] + " cards remaining. \n"
					+ "Player 3 has " + pileSizes[2] + " cards remaining. \n"
					+ "Player 4 has " + pileSizes[3] + " cards remaining. \n";


		} else {
			gameInfo =  "Your cards: " + piles[0].toString();
			/*gameInfo = "Player 1 has " + piles[0].toString() + "\n"
					+ "Player 2 has " + piles[1].toString() + "\n"
					+ "Player 3 has " + piles[2].toString() + "\n"
					+ "Player 4 has " + piles[3].toString() + "\n";*/
		}

		return gameInfo;
	}
}
