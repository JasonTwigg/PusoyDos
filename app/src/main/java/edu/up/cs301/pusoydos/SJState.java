package edu.up.cs301.pusoydos;

import android.util.Log;

import java.util.ArrayList;

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

	private boolean[] cardsSelected;

	private int currPos;
	private int selectedVal;
	private int totalVal;
	private int randCenter;

	private int playerLastPlayed;


	// 0 - open hand/control
	// 1 - singles
	// 2 - doubles
	// 3 - hands - straight
	// 4 - hands - flush
	// 5 - hands - four of a kind
	private int modeType;

	boolean isFirst;

    /**
     * Constructor for objects of class SJState. Initializes for the beginning of the
     * game, with a random player as the first to turn card
     *  
     */
    public SJState() {

		perspective = 4;
		modeType = 0;


		isFirst = true;


    	
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

    	piles[0].sort();
		piles[1].sort();
		piles[2].sort();
		piles[3].sort();

		for( int i = 0; i < 4; i++ ) {
			if( piles[i].getCards().get(12).getPower() == 0 ) {
				turnNum =i;
			}
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

	public String selectCard( int playerNum, int pos ){

		if( playerNum == turnNum ) {

			if( piles[playerNum].getCards().get(pos) != null) {

				Card c = piles[playerNum].getCards().get(pos);
				c.getRank();
				if (c.isSelected()) {
					c.setSelected(false);
					return "Card " + c.toString() + " was deselected! \n";
				} else {
					c.setSelected(true);
					return "Card " + c.toString() + " was selected by Player "+(playerNum+1)+".\n" +
							"The rank of this card is "+ c.getRank().shortName() +". \n";
				}

			}

		}

		return "It is not your turn "+playerNum+". Please stop trying to select a card.\n";
	}

	public String playCard( int playerNum ){

		//setCenterVal();

		ArrayList<Card> selectedCards = new ArrayList<Card>();
		ArrayList<Integer> selectedPos = new ArrayList<Integer>();


		//for( Card c : piles[playerNum].getCards() ) {
		for( int i = 0; i < piles[playerNum].getCards().size(); i++){

			Card c = piles[playerNum].getCards().get(i);

			if( c.isSelected() == true ) {

				/*
				piles[playerNum].moveSelectedCard( piles[4], i );
				changeTurn();
				return "Player " + (playerNum+1) + " just played their "+ c.toString()+ " to the center pile.\n";
				*/
				/*
				if( c.getRank().shortName() > randCenter ) {
					piles[playerNum].moveSelectedCard( piles[4], i );
					changeTurn();
					//selectedVal++;
					return "Center value is "+randCenter+" \n"+
							"Player " + (playerNum+1) + " just played their "+ c.toString()+ " to the center pile.\n";

				}
				else {
					return "Center value is "+randCenter+ ".\nILLEGAL MOVE\n";
				}
				*/

				selectedCards.add(c);
				selectedPos.add(i);


			}

		}

		if( selectedCards.size() > 0 ){
			if (canPlay(selectedCards) ){
				String returnValue = "";
				int count = 0;
				for(int i = 0; i < selectedCards.size(); i++) {
					Card c = piles[turnNum].getCards().get(selectedPos.get(i)-count);
					piles[playerNum].moveSelectedCard(piles[4], selectedPos.get(i)-count);
					returnValue += "Player " + (playerNum+1) + " just played their "+ c.toString()+ " to the center pile.\n";
					count++;

				}
				changeTurn();
				return returnValue;

			} else {
				return "Cannot play selected Cards!\n";
			}
		}
		else {
			return "No cards selected! \n";
		}

	}

	public String passString( int playerNum ){

		String playerPassed = "";
		if(playerNum == turnNum){
			changeTurn();
			playerPassed = "Player " + (playerNum+1) + " passed \n";
		}else if(playerNum != turnNum){
			return "It is not your turn Player "+(playerNum+1)+", It is player " + (turnNum+1) + "'s Turn!\n";
		}
		return playerPassed;

	}



    public String toString() {
		String gameInfo = "";
		gameInfo = "Player " + (turnNum+1) +"'s turn.\n";
		if( perspective != 4 ) {
			gameInfo +=  "Your cards: " + piles[perspective].toString() + "\n"
					+ "Player 1 has " + pileSizes[0] + " cards remaining. \n"
					+ "Player 2 has " + pileSizes[1] + " cards remaining. \n"
					+ "Player 3 has " + pileSizes[2] + " cards remaining. \n"
					+ "Player 4 has " + pileSizes[3] + " cards remaining. \n";


		} else {
			//gameInfo +=  "Your cards: " + piles[0].toString() +"\n";

			gameInfo += "Player 1 has " + piles[0].toString() + "\n"
					+ "Player 2 has " + piles[1].toString() + "\n"
					+ "Player 3 has " + piles[2].toString() + "\n"
					+ "Player 4 has " + piles[3].toString() + "\n"
					+ "Middle Pile has " + piles[4].toString() + "\n";
		}

		return gameInfo;
	}

	public void changeTurn(){

		if( turnNum == 3 ) {

			turnNum = 0;

			if( turnNum == playerLastPlayed) {

				modeType = 0;

			}

		}else{
			turnNum++;

			if( turnNum == playerLastPlayed) {

				modeType = 0;

			}


		}

	}



	/**
	 * Determines whether or not the move is legal.
	 *
	 * @param Cards
	 * 			relevant cards to current play
	 *
	 * @return true if move is legal or false if it is illegal.
	 */
	public boolean canPlay( ArrayList<Card> Cards ){

		int size = Cards.size();
		int firstCardPower = Cards.get(0).getPower();


		/*
		"power" refers to the value of a card. The power is dependent on the rank and suit of the
		card. The 3 Clubs (lowest ranking card in game) has a power of 0. Each card increases by
		power of 1 until the 2 of Diamonds (highest card) which has a power of 51.
		 */
		int[] powers = new int[size];

		int count = 0;

		//Creates an array of the power values of each card
		for( Card c : Cards){
			powers[count] = c.getPower();
			count++;
		}

		//Ensures that the first play of the game contains the 3 of Clubs
		if( isFirst ) {
			boolean has3Club = false;
			for (Card c : Cards) {
				if( c.getPower() == 0 ){
					has3Club = true;
					isFirst=false;
				}
			}
			if(!has3Club){
				return false;
			}
		}

		//The following considers plays of single cards
		if( size == 1 && (modeType == 0 || modeType == 1) ){



			/*
			If the center pile is empty, and the current player plays a single card,
			set the modeType to 1
			 */
			if( piles[4].getCards().size() == 0 ) {
				modeType = 1;
				return true;
			}
			/*
			If the center pile has live cards, and the power of the card being played by the
			current user is greater than the power of the current (top) card of the center pile,
			set modeType to 1
			 */
			else if( firstCardPower > piles[4].getCards().get(0).getPower() ){
				Log.i(firstCardPower+"",piles[4].getCards().get(0).getPower()+"");
				modeType = 1;
				return true;
			}
			//If neither of the two above, return false. The move is illegal.
			else {
				return false;
			}
		}

		//the following considers plays of pairs (two cards)
		else if ( size == 2 && (modeType == 0 || modeType == 2)){

			/*
			If the two cards do not match (not of the same rank), return false. The move is
			illegal!
			 */
			if(Cards.get(0).getRank() != Cards.get(1).getRank() ){
				return false;
			}

			/*
			If the player is in control (and the pair matches), the play is legal (return true)
			 */
			if( modeType == 0 ){
				return true;
			}

			/*
			If the center pile has no cards, change the modeType to 2 (to match the play of a pair)
			 */
			if( piles[4].getCards().size() == 0 ) {
				modeType = 2;
				return true;
			}
			/*
			If the power of the first card (higher of the two) is greater than the power of the
			first card of the two currently in play, change the modeType to match the play of a
			pair. The move is legal.
			 */
			else if( firstCardPower > piles[4].getCards().get(0).getPower() ){
				modeType = 2;
				return true;
			}
			/*
			If neither of the two cases above, the move is illegal (return false).
			 */
			else {
				return false;
			}
		}

		//the following considers plays of poker hands (5 cards)
		else if ( size == 5 && (modeType == 0 || modeType == 3 || modeType == 4 || modeType == 5 )){

			//below: booleans to determine the type of poker hand trying to be played
			boolean isFlush = true;
			boolean isStraight = true;
			boolean is4ofKind = true;
			boolean secondChance4 = false;

			Card tempCard = Cards.get(0); //to get the first card of the 5
			Card nextCard; //to keep track of the cards through the loop

			//loop to go through the last 4 cards in the 5 card hand
			for(int i = 1; i < 5; i++ ){

				//set nextCard equal to the current card being evaluated in the loop
				nextCard = Cards.get(i);

				//Check for a straight
				if( tempCard.getPower()%4 != nextCard.getPower()%4+1 ){

					//not a straight if rank of cards are NOT consecutive
					isStraight = false;

				}
                //check for a flush
				if(tempCard.getSuit() != nextCard.getSuit() ) {

					//not a flush if all suits do NOT match
					isFlush = false;
				}

				//"Second chance" to check for four-of-a-kind
				if(tempCard.getRank() != nextCard.getRank()) {

					//if one of the 4 cards are not the same rank, and secondChance4 is true,
					//set secondChange4 to false (it is still not 4-of-a-kind)
					if(secondChance4) {

						secondChance4 = false;

					}
					//if one of the 4 cards are not the same rank, and secondChance4 is false,
					//set is4ofKind to false (it is not 4-of-a-kind)
					else {

						is4ofKind = false;

					}
				}

				//set tempCard to the card in the next position (to the right of it)
				tempCard = Cards.get(i);

			}


			//to consider plays with a straight
			if( isStraight){
				/*
				if the modeType matches (indicates straights are currently being played), and
				the power of the first card (hand being played) is greater than the lowest card
				currently played (center pile). The play is legal (return true)
				 */
				if(modeType == 3 ) {
					if (Cards.get(0).getPower() > piles[4].getCards().get(4).getPower()) {
						modeType = 3;
						return true;
					}
				}
				/*
				if the modeType does not match, but the player is in control, the
				play is legal (return true).
				 */
				else if( modeType == 0 ){
					return true;
				}
				//otherwise return false (the play is illegal)
				return false;
			}

			//to consider plays with a flush
			else if ( isFlush ){
				/*
				If the a straight is currently in play, the move is legal (return true) because a
				flush is of greater value than any straight.
				 */
				if(modeType == 3 ) {
					return true;
				}
				/*
				If a flush is currently in play and the first card of the hand being played is
				of greater power than the lowest card of the hand in the center pile, the move is
				legal (return true).
				 */
				else if( modeType == 4 ){
					if (Cards.get(0).getPower() > piles[4].getCards().get(4).getPower()) {
						modeType = 4;
						return true;
					}
				}
				/*
				If neither of the previous two cases, but the player is in control, the move is
				legal, return true.
				 */
				else if( modeType == 0 ){
					return true;
				}
				//Otherwise, return false
				return false;
			}

			//to consider plays with four-of-a-kind hands
			else if ( is4ofKind ){
				/*
				If a straight or flush is currently in play, the play is legal (return true)
				because a 4-of-a-kind is higher than all straights or flushes
				 */
				if(modeType == 3 || modeType == 4 ) {
					return true;
				}
				/*
				If a 4-of-a-kind is currently in play, and the power of the 4 cards that are
				matching (without regard to the random fifth of the players choice) in your hand
				is greater than the power of the 4 cards that are currently played in the center
				pile, the move is legal (return true).
				 */
				else if( modeType == 5 ){
					if (Cards.get(2).getPower() > piles[4].getCards().get(4).getPower()) ;
					modeType = 4;
					return true;
				}
				/*
				If none of the two cases above, but the player is in control, then the play is
				legal, return true.
				 */
				else if( modeType == 0 ){
					return true;
				}
				//Otherwise, return false
				return false;
			}

			return false;


		}
		else {
			return false;
		}


	}

	public Deck[] getPiles() {
		return piles;
	}

	public void setPiles(Deck[] piles) {
		this.piles = piles;
	}

	public int[] getPileSizes() {
		return pileSizes;
	}

	public void setPileSizes(int[] pileSizes) {
		this.pileSizes = pileSizes;
	}

	public int getTurnNum() {
		return turnNum;
	}

	public void setTurnNum(int turnNum) {
		this.turnNum = turnNum;
	}

	public int getPerspective() {
		return perspective;
	}

	public void setPerspective(int perspective) {
		this.perspective = perspective;
	}

	public boolean[] getCardsSelected() {
		return cardsSelected;
	}

	public void setCardsSelected(boolean[] cardsSelected) {
		this.cardsSelected = cardsSelected;
	}

	public int getCurrPos() {
		return currPos;
	}

	public void setCurrPos(int currPos) {
		this.currPos = currPos;
	}

	public int getSelectedVal() {
		return selectedVal;
	}

	public void setSelectedVal(int selectedVal) {
		this.selectedVal = selectedVal;
	}

	public int getTotalVal() {
		return totalVal;
	}

	public void setTotalVal(int totalVal) {
		this.totalVal = totalVal;
	}

	public int getRandCenter() {
		return randCenter;
	}

	public void setRandCenter(int randCenter) {
		this.randCenter = randCenter;
	}
}
