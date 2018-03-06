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
public class SJState extends GameState {
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
	private int[] pileSizes;

	// whose turn is it to turn a card?
	private int turnNum;


	private int perspective;

	private boolean[] cardsSelected;

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
		while (piles[0].size() >= 14) {

			piles[0].moveTopCardTo(piles[1]);
			piles[0].moveTopCardTo(piles[2]);
			piles[0].moveTopCardTo(piles[3]);


		}

		piles[0].sort();
		piles[1].sort();
		piles[2].sort();
		piles[3].sort();

		for (int i = 0; i < 4; i++) {
			if (piles[i].getCards().get(12).getPower() == 0) {
				turnNum = i;
			}
		}
	}

	/**
	 * Copy constructor for objects of class SJState. Makes a copy of the given state
	 *
	 * @param orig the state to be copied
	 */
	public SJState(SJState orig, int playerNum) {
		// set index of player whose turn it is
		turnNum = orig.turnNum;
		// create new deck array, making copy of each deck
		piles = new Deck[5];


		piles[playerNum] = new Deck(orig.piles[playerNum]);


		pileSizes = new int[5];
		for (int i = 0; i < 5; i++) {

			pileSizes[i] = orig.piles[i].size();

		}

		perspective = playerNum;

		playerLastPlayed = orig.playerLastPlayed;
		modeType = orig.modeType;
		isFirst = orig.isFirst;


	}

	/**
	 * Gives the given deck.
	 *
	 * @return the deck for the given player, or the middle deck if the
	 * index is 2
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

	public String selectCard(int playerNum, int pos) {

		if (playerNum == turnNum) {

			if (piles[playerNum].getCards().get(pos) != null) {

				Card c = piles[playerNum].getCards().get(pos);
				c.getRank();
				if (c.isSelected()) {
					c.setSelected(false);
					return "Card " + c.toString() + " was deselected! \n";
				} else {
					c.setSelected(true);
					return "Card " + c.toString() + " was selected by Player " + (playerNum + 1) + ".\n" +
							"The rank of this card is " + c.getRank().shortName() + ". \n";
				}

			}

		}

		return "It is not your turn " + playerNum + ". Please stop trying to select a card.\n";
	}

	public String playCard(int playerNum) {

		//setCenterVal();

		ArrayList<Card> selectedCards = new ArrayList<Card>();
		ArrayList<Integer> selectedPos = new ArrayList<Integer>();


		//for( Card c : piles[playerNum].getCards() ) {
		for (int i = 0; i < piles[playerNum].getCards().size(); i++) {

			Card c = piles[playerNum].getCards().get(i);

			if (c.isSelected() == true) {

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

		if (selectedCards.size() > 0) {
			if (canPlay(selectedCards)) {
				String returnValue = "";
				int count = 0;
				for (int i = 0; i < selectedCards.size(); i++) {
					Card c = piles[turnNum].getCards().get(selectedPos.get(i) - count);
					piles[playerNum].moveSelectedCard(piles[4], selectedPos.get(i) - count);
					returnValue += "Player " + (playerNum + 1) + " just played their " + c.toString() + " to the center pile.\n";
					count++;

				}
				changeTurn();
				return returnValue;

			} else {
				return "Cannot play selected Cards!\n";
			}
		} else {
			return "No cards selected! \n";
		}

	}

	public String passString(int playerNum) {

		String playerPassed = "";
		if (playerNum == turnNum) {
			changeTurn();
			playerPassed = "Player " + (playerNum + 1) + " passed \n";
		} else if (playerNum != turnNum) {
			return "It is not your turn Player " + (playerNum + 1) + ", It is player " + (turnNum + 1) + "'s Turn!\n";
		}
		return playerPassed;

	}


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
			//gameInfo +=  "Your cards: " + piles[0].toString() +"\n";

			gameInfo += "Player 1 has " + piles[0].toString() + "\n"
					+ "Player 2 has " + piles[1].toString() + "\n"
					+ "Player 3 has " + piles[2].toString() + "\n"
					+ "Player 4 has " + piles[3].toString() + "\n"
					+ "Middle Pile has " + piles[4].toString() + "\n";
		}

		return gameInfo;
	}

	public void changeTurn() {

		if (turnNum == 3) {

			turnNum = 0;

			if (turnNum == playerLastPlayed) {

				modeType = 0;

			}

		} else {
			turnNum++;

			if (turnNum == playerLastPlayed) {

				modeType = 0;

			}


		}

	}

	public boolean canPlay(ArrayList<Card> Cards) {

		int size = Cards.size();
		int firstCardPower = Cards.get(0).getPower();

		int[] powers = new int[size];

		int count = 0;
		for (Card c : Cards) {
			powers[count] = c.getPower();
			count++;
		}

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

		if (size == 1 && (modeType == 0 || modeType == 1)) {


			if (piles[4].getCards().size() == 0) {
				modeType = 1;
				return true;
			} else if (firstCardPower > piles[4].getCards().get(0).getPower()) {
				Log.i(firstCardPower + "", piles[4].getCards().get(0).getPower() + "");
				modeType = 1;
				return true;
			} else {
				return false;
			}

		} else if (size == 2 && (modeType == 0 || modeType == 2)) {


			if (Cards.get(0).getRank() != Cards.get(1).getRank()) {
				return false;
			}

			if (modeType == 0) {
				return true;
			}

			if (piles[4].getCards().size() == 0) {
				modeType = 2;
				return true;
			} else if (firstCardPower > piles[4].getCards().get(0).getPower()) {
				modeType = 2;
				return true;
			} else {
				return false;
			}

		} else if (size == 5 && (modeType == 0 || modeType == 3 || modeType == 4 || modeType == 5)) {


			boolean isFlush = true;
			boolean isStraight = true;
			boolean is4ofKind = true;
			boolean secondChance4 = false;

			Card tempCard = Cards.get(0);
			Card nextCard;

			for (int i = 1; i < 5; i++) {

				nextCard = Cards.get(i);
				//Check for a straight
				if (tempCard.getPower() % 4 != nextCard.getPower() % 4 + 1) {

					isStraight = false;

				}
				//check for a flush
				if (tempCard.getSuit() != nextCard.getSuit()) {
					isFlush = false;
				}

				if (tempCard.getRank() != nextCard.getRank()) {

					if (secondChance4) {

						secondChance4 = false;

					} else {

						is4ofKind = false;

					}
				}

				tempCard = Cards.get(i);

			}


			if (isStraight) {
				if (modeType == 3) {
					if (Cards.get(0).getPower() > piles[4].getCards().get(4).getPower()) {
						modeType = 3;
						return true;
					}
				} else if (modeType == 0) {
					return true;
				}

				return false;
			} else if (isFlush) {
				if (modeType == 3) {
					return true;
				} else if (modeType == 4) {
					if (Cards.get(0).getPower() > piles[4].getCards().get(4).getPower()) {
						modeType = 4;
						return true;
					}
				} else if (modeType == 0) {
					return true;
				}

				return false;
			} else if (is4ofKind) {
				if (modeType == 3 || modeType == 4) {
					return true;
				} else if (modeType == 5) {
					if (Cards.get(2).getPower() > piles[4].getCards().get(4).getPower()) ;
					modeType = 4;
					return true;
				} else if (modeType == 0) {
					return true;
				}

				return false;
			}

			return false;


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



}
