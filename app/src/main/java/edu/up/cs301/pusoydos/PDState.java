package edu.up.cs301.pusoydos;

import java.util.ArrayList;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;

/**
 External Citation
 Date: March 3, 2018
 Problem: We weren't sure how to start on the game interface
 Resource:
 Nuxoll and Vegdahl
 Solution: We started out with the code for SlapJack and modified it.
 */

/**
 * Contains the state of a PusoyDos game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 *
 * @author Jason Twigg
 * @author Cole Holbrook
 * @author Tawny Motoyama
 * @author Josh Azicate
 *
 * @version April 2018
 */
public class PDState extends GameState {
    private static final long serialVersionUID = -8269749892027578792L;

    ///////////////////////////////////////////////////
    // ************** instance variables ************
    ///////////////////////////////////////////////////

	// the three piles of cards:
	//  - 0: pile for player 0
	//  - 1: pile for player 1
	//  - 2: pile for player 2
	//  - 3: pile for player 3
	//  - 4: pile that knows all of the player's cards
	// Note that when players receive the state, all but the top card in all piles
	// are passed as null.
	private Deck[] piles;
	//The size of each of the piles
	private int[] pileSizes;
	//whose turn is it to play a card?
	private int turnNum;
	//Integers assigned to each player to represent their perspective of the game
	private int perspective;
	//A boolean value given to each Card object to tell if it is selected or not
	private boolean[] cardsSelected;
	//The integer value of the player who just played so that the player turn can be moved.
	private int playerLastPlayed;
	//A boolean value to determine if it the first play of the game
	private boolean isFirst;
	// 0 - open hand/control
	// 1 - singles
	// 2 - doubles
	// 3 - hands - straight
	// 4 - hands - flush
	// 5 - hands - full house
	// 6 - hands - four of a kind
	private int modeType;

	///////////////////////////////////////////////////
	// ********** End of instance variables ********* /
	///////////////////////////////////////////////////


	/**
	 * Constructor for objects of class PDState. Initializes for the beginning of the
	 * game, with a random player as the first to turn card
	 */
	public PDState() {
		//Perspective of four is the master copy perspective
		perspective = 4;
		//This means that it is the first turn
		isFirst = true;
		//Mode zero means that the person playing is in control
		modeType = 0;


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
		//piles[4].add52();
		piles[0].add52(); // give all cards to player whose turn it is, in order
		piles[0].shuffle(); // shuffle the cards

		// deal the cards to opponents, until to piles have ~same size
		while (piles[0].size() >= 14) {

			piles[0].moveTopCardTo(piles[1]);
			piles[0].moveTopCardTo(piles[2]);
			piles[0].moveTopCardTo(piles[3]);
		}

		piles[0].add(new Card(Rank.TWO,Suit.Diamond));
		piles[0].add(new Card(Rank.THREE,Suit.Heart));
		piles[0].add(new Card(Rank.THREE,Suit.Spade));
		piles[1].add(new Card(Rank.FIVE,Suit.Spade));
		piles[1].add(new Card(Rank.FIVE,Suit.Heart));




		//Sorts each player's hand from high card to low
		piles[0].sort();
		piles[1].sort();
		piles[2].sort();
		piles[3].sort();





		//This for loop checks to see who has the 3 of Clubs (power of 0)
		//and makes the first turn theirs
		for (int i = 0; i < 4; i++) {
			if (piles[i].getCards().get(piles[i].getCards().size()-1).getPower() == 0) {
				turnNum = i;
			}
		}

		playerLastPlayed = turnNum;
	}

	/**
	 * Copy constructor for objects of class PDState. Makes a copy of the given state
	 *
	 * @param orig the state to be copied
	 */
	public PDState(PDState orig, int playerNum) {
		// set index of player whose turn it is
		turnNum = orig.turnNum;
		// create new deck array, making a deep copy of the deck
		piles = new Deck[5];
		//Deep Copy of each of the player's decks
		piles[playerNum] = new Deck(orig.piles[playerNum]);
		piles[4] = new Deck( orig.piles[4]);
		//Creates deep copy of each of the data values stored in PDState
		perspective = playerNum;
		playerLastPlayed = orig.playerLastPlayed;
		modeType = orig.modeType;
		isFirst = orig.isFirst;
		pileSizes = new int[5];
		for (int i = 0; i < 5; i++) {

			pileSizes[i] = orig.piles[i].size();

			/**
 			External Citation
 			Date: March 3, 2018
 			Problem: Not sure of the syntax for a deep copy
 			Resource:
			 //https://stackoverflow.com/questions/64036/
			 how-do-you-make-a-deep-copy-of-an-object-in-java
 			Solution: We used the example code from this post.
 			*/
		}
	}

	/**
	 * Gives the given deck.
	 *
	 * @return the deck for the given player
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
	 * @param idx the index of the player whose move it now is
	 */
	public void setToPlay(int idx) {
		turnNum = idx;
	}

	/**
	 * Replaces all cards with null, except for the top card of deck 2
	 */




	/**
	 * Allows the player to select which cards they would like to play
	 * by changing the boolean value of isSelected()
	 *
	 * @param playerNum the index of the player whose move it now is
	 * @param pos the position of the card being selected
	 */
	public String selectCard(int playerNum, int pos) {
		//Makes sure it is actually the players turn
		if (playerNum == turnNum) {
			//Makes sure there is a card being selected
			if (piles[playerNum].getCards().get(pos) != null) {

				//Set a card equal to that being chosen
				Card c = piles[playerNum].getCards().get(pos);
				c.getRank();

				if (c.isSelected()) { //If the card is selected the card is then deselected
					c.setSelected(false);
					return "Card " + c.toString() + " was deselected! \n";
				} else { //If the card is not selected it is now selected
					c.setSelected(true);
					return "Card " + c.toString() + " was selected by Player " + (playerNum + 1) + ".\n";
				}

			}

		}
		//Error message for naughty players who try and select a card when it is not their turn.
		return "It is not your turn " + playerNum + ". Please stop trying to select a card.\n";
	}

	/**
	 * Allows the player to play the cards that they currently have selected IF they are
	 * allowed within the rules
	 *
	 * @param playerNum the index of the player whose move it now is
	 *
	 */
	public String playCard(int playerNum) {
		//Make two array lists, one of cards and the other of
		//integers. The one of cards is the selected cards of the player
		//and the one of integers is the index/location of those cards.
		ArrayList<Card> selectedCards = new ArrayList<Card>();
		ArrayList<Integer> selectedPos = new ArrayList<Integer>();

		//To loop through the selected cards.
		for (int i = 0; i < piles[playerNum].getCards().size(); i++) {

			Card c = piles[playerNum].getCards().get(i);
			//Adds to the arrays if the card is selected
			if (c.isSelected() == true) {

				selectedCards.add(c);
				selectedPos.add(i);
			}
		}
		//Makes sure that the array actually has a card in it (there are some selected)
		if (selectedCards.size() > 0) {
			//Makes sure that the selected cards are allowed within the rules
			if (canPlay(selectedCards)) {
				String returnValue = "";
				int count = 0;
				//Moves each of the cards from the player's hand to the center pile
				for (int i = 0; i < selectedCards.size(); i++) {
					Card c = piles[turnNum].getCards().get(selectedPos.get(i) - count);
					piles[playerNum].moveSelectedCard(piles[4], selectedPos.get(i) - count);
					returnValue += "Player " + (playerNum + 1) + " just played their " + c.toString() + " to the center pile.\n";
					count++;

				}
				//The turn moves to the next player
				playerLastPlayed = turnNum;
				changeTurn();
				return returnValue;

			} else { //Cards selected are not allowed according to the rules so none are played
				return "Cannot play selected Cards!\n";
			}
		} else { //There are not any cards selected therefore none to move
			return "No cards selected! \n";
		}
	}

	/**
	 * Allows the player to pass instead of selecting any cards
	 * and playing any cards. They are not allowed to do this
	 * if they are in control (Game Mode 0)
	 *
	 * @param playerNum the index of the player whose move it now is
	 *
	 */
	public String passAction(int playerNum) {

		String playerPassed = "";
		//Making sure it is the player's turn who is tring to pass
		if (playerNum == turnNum) {
			//Checks if the player is in control
			if( isFirst || modeType == 0 ){
				//Message to tell naughty players they can't pass while in control
				return "You have Control Player " + (playerNum+1) + ". You cannot Pass!\n";
			}
			//Changes the turn to the next player
			changeTurn();
			//Message for good players who are passing when they should
			playerPassed = "Player " + (playerNum + 1) + " passed \n";
		} else if (playerNum != turnNum) {
			//An error message for naughty players who are trying to pass on somebody else's turn
			return "It is not your turn Player " + (playerNum + 1) + ", It is player " + (turnNum + 1) + "'s Turn!\n";
		}
		//Returns one of the messages
		return playerPassed;

		/**
		 External Citation
		 Date: March 3, 2018
		 Problem: We forgot the syntax for logical or
		 Resource:
		 http://www.cafeaulait.org/course/week2/45.html
		 Solution: We used the example code from this post.
		 */
	}

	/**
	 * The method makes it possible to print out who's
	 * turn it is as well the cards in each player's
	 * hand. If the perspective is that of the Master
	 * than everything is showed.
	 *
	 */
	public String toString() {

		String gameInfo = "";
		gameInfo = "Player " + (turnNum + 1) + "'s turn.\n";
		if (perspective != 4) {
			gameInfo += "Your cards: " + piles[perspective].toString() + "\n"
					+ "Player 1 has " + pileSizes[0] + " cards remaining. \n"
					+ "Player 2 has " + pileSizes[1] + " cards remaining. \n"
					+ "Player 3 has " + pileSizes[2] + " cards remaining. \n"
					+ "Player 4 has " + pileSizes[3] + " cards remaining. \n";
		} else {
			gameInfo += "Player 1 has " + piles[0].toString() + "\n"
					+ "Player 2 has " + piles[1].toString() + "\n"
					+ "Player 3 has " + piles[2].toString() + "\n"
					+ "Player 4 has " + piles[3].toString() + "\n"
					+ "Middle Pile has " + piles[4].toString() + "\n";
		}
		return gameInfo;

	}

	/**
	 * The method changes the player turn. This rotates through
	 * the four players in clockwise order. (1->4)
	 *
	 */
	public void changeTurn() {
		//Checks to see if it the last player's turn
		if (turnNum == 3) {
			//Goes back to the first player
			turnNum = 0;
			//Checks to see if the turn went all the way
			//back to the player who played the cads in the center
			//and if it did gives that player control
			if (turnNum == playerLastPlayed) {

				modeType = 0;
			}
		//Moves the player turn from 0 to 1, 1 to 2 or 2 to 3.
		} else {
			turnNum++;
			//Checks to see if the turn went all the way
			//back to the player who played the cads in the center
			//and if it did gives that player control
			if (turnNum == playerLastPlayed) {

				modeType = 0;
			}
		}
	}



    /**
     * Determines whether or not the move is legal.
     *
     * @param Cards relevant cards to current play
     * @return true if move is legal or false if it is illegal.
     */
	public boolean canPlay(ArrayList<Card> Cards) {

		int size = Cards.size();
		if( size == 0 ){
			return false;
		}
		int firstCardPower = Cards.get(0).getPower();

        /*
		"power" refers to the value of a card. The power is dependent on the rank and suit of the
		card. The 3 Clubs (lowest ranking card in game) has a power of 0. Each card increases by
		power of 1 until the 2 of Diamonds (highest card) which has a power of 51.
		 */
		int[] powers = new int[size];

		int count = 0;
        //Creates an array of the power values of each card
		for (Card c : Cards) {
			powers[count] = c.getPower();
			count++;
		}
        //Ensures that the first play of the game contains the 3 of Clubs
		if (isFirst) {
			boolean has3Club = false;
			for (Card c : Cards) {
				if (c.getPower() == 0) {
					has3Club = true;
					isFirst = false;
				}
			}
			if (!has3Club) {
				return false;
			}
		}
        //The following considers plays of single cards
		if (size == 1 && (modeType == 0 || modeType == 1)) {


            /*
			If the center pile is empty, and the current player plays a single card,
			set the modeType to 1
			*/
			if (piles[4].getCards().size() == 0 || modeType == 0) {
				modeType = 1;
				return true;
                /*
                If the center pile has live cards, and the power of the card being played by the
	 		    current user is greater than the power of the current (top) card of the center pile,
		        set modeType to 1
			    */
			} else if (firstCardPower > piles[4].getCards().get(piles[4].size()-1).getPower()) {
				//Log.i(firstCardPower + "", piles[4].getCards().get(0).getPower() + "");
				modeType = 1;
				return true;
			} else {
                //If neither of the two above, return false. The move is illegal.
				return false;
			}

		} else if (size == 2 && (modeType == 0 || modeType == 2)) {


			if (Cards.get(0).getRank() != Cards.get(1).getRank()) {
				return false;
			}

			if (modeType == 0) {
				modeType = 2;
				return true;
			}

			if (piles[4].getCards().size() == 0) {
				modeType = 2;
				return true;
			} else if (firstCardPower > piles[4].getCards().get(piles[4].size()-2).getPower()) {
				modeType = 2;
				return true;
			} else {
				return false;
			}

		} else if (size == 5 && (modeType == 0 || modeType == 3 || modeType == 4 || modeType == 5)) {


			boolean isFlush = true;
			boolean isStraight = true;
			boolean is4ofKind = (
					(Cards.get(0).getRank() == Cards.get(1).getRank() &&
					Cards.get(1).getRank() == Cards.get(2).getRank() &&
					Cards.get(2).getRank() == Cards.get(3).getRank() &&
					Cards.get(3).getRank() != Cards.get(4).getRank()) ||
					(Cards.get(0).getRank() != Cards.get(1).getRank() &&
					Cards.get(1).getRank() == Cards.get(2).getRank() &&
					Cards.get(2).getRank() == Cards.get(3).getRank() &&
					Cards.get(3).getRank() == Cards.get(4).getRank())
			);

			boolean isFullHouse = (
					(Cards.get(0).getRank() == Cards.get(1).getRank() &&
							Cards.get(1).getRank() == Cards.get(2).getRank() &&
							Cards.get(2).getRank() != Cards.get(3).getRank() &&
							Cards.get(3).getRank() == Cards.get(4).getRank()) ||
							(Cards.get(0).getRank() == Cards.get(1).getRank() &&
							Cards.get(1).getRank() != Cards.get(2).getRank() &&
							Cards.get(2).getRank() == Cards.get(3).getRank() &&
							Cards.get(3).getRank() == Cards.get(4).getRank())
			);


			Card tempCard;
			Card nextCard = Cards.get(0);

			for (int i = 1; i < 5; i++) {

				tempCard = Cards.get(i-1);
				nextCard = Cards.get(i);
				//Check for a straight
				if (tempCard.getPower() / 4 != nextCard.getPower() / 4 + 1) {

					isStraight = false;

				}
				//check for a flush
				if (tempCard.getSuit() != nextCard.getSuit()) {
					isFlush = false;
				}

			}


			if (isStraight) {
                /*
                if the modeType matches (indicates straights are currently played)
                and the power of the first card (hand being played) is greater
                than the lowest card currently (center pile). The play is legal
                (return true)
                 */
				if (modeType == 3) {
					if (Cards.get(0).getPower() > piles[4].getCards().get(4).getPower()) {
						modeType = 3;
						return true;
					}
				} else if (modeType == 0) {
                    /*
                    If the modeType does not match, but the player is in control,
                    the play is legal (return true)
                     */
					modeType = 3;
					return true;
				}
				//Otherwise return false (not legal)
				return false;

				//To consider plays with a flush
			} else if (isFlush) {
				if (modeType == 3) {
					return true;
				} else if (modeType == 4) {
					if (Cards.get(0).getPower() > piles[4].getCards().get(4).getPower()) {
						modeType = 4;
						return true;
					}
				 /*
			     If neither of the previous two cases, but the player is in
				 control, the move is legal, return true.
				 */
				} else if (modeType == 0) {
					modeType = 4;
					return true;
				}
				//Otherwise, return false
				return false;
			} else if (isFullHouse) {
			    /*
				If a straight or flush is currently in play, the play is legal (return true)
                because a fullhouse is higher than all straights or flushes
                */

			if (modeType == 3 || modeType == 4 || modeType == 5) {
				return true;
			} else if (modeType == 6) {
					/*
				    If a fullhouse is currently in play, and the power of the 4 cards that are
				    matching (without regard to the random fifth of the players choice) in your hand
				    is greater than the power of the 4 cards that are currently played in the center
				    pile, the move is legal (return true).
				    */
				int firstHandCardPower = Cards.get(0).getPower();
				int lastHandCardPower = Cards.get(4).getPower();

				int firstDeckCardPower = piles[4].getCards().get(4).getPower();
				int lastDeckCardPower = piles[4].getCards().get(4).getPower();


				if ((firstHandCardPower > firstDeckCardPower && firstHandCardPower > lastDeckCardPower) ||
						lastHandCardPower > firstDeckCardPower && lastHandCardPower > lastDeckCardPower) {
					modeType = 6;
					return true;
				}
			} else if ( modeType == 0 ){

				modeType = 6;
				return true;

			}

			return false;


		} else if (is4ofKind) {
			    /*
				If a straight or flush or a full house is currently in play, the play is legal (return true)
                because a 4-of-a-kind is higher than all straights or flushes
                */

				if (modeType == 3 || modeType == 4 || modeType == 5) {
					return true;
				} else if (modeType == 6) {
					/*
				    If a 4-of-a-kind is currently in play, and the power of the 4 cards that are
				    matching (without regard to the random fifth of the players choice) in your hand
				    is greater than the power of the 4 cards that are currently played in the center
				    pile, the move is legal (return true).
				    */
					if (Cards.get(2).getPower() > piles[4].getCards().get(2).getPower()) {
						modeType = 6;
						return true;
					}
				} else if ( modeType == 0 ){

					modeType = 6;
					return true;

				}

				return false;


			} else {
				//Otherwise, return false
				return false;
			}
		} else {
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

	public int getModeType(){
		return modeType;
	}

	public int getPlayerLastPlayed() { return playerLastPlayed; }



}
