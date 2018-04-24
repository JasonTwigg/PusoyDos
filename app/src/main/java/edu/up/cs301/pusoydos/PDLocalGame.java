package edu.up.cs301.pusoydos;

import android.util.Log;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;


/**
 External Citation
 Date: March 11, 2018
 Problem: We were not sure how to
 Resource: Nuxoll and Vegdahl
 Solution: We debugged the slap jack game and figured out how actions were called in the game framework
 This showed us how to add our own action classes and listen for them
 */

/**
 * The LocalGame class for a PusoyDos game.  Defines and enforces
 * the game rules; handles interactions between players.
 * 
 * @author Steven R. Vegdahl
 *
 * @author Jason Twigg
 * @author Cole Holbrook
 * @author Tawny Motoyama
 * @author Josh Azicate
 *
 * @version April 2018
 */

public class PDLocalGame extends LocalGame {

    // the game's state
    PDState state;

    /**
     * Constructor for the PDLocalGame.
     */
    public PDLocalGame() {
        Log.i("PDLocalGame", "creating game");
        // create the state for the beginning of the game
        state = new PDState();
    }


    /**
     * checks whether the game is over; if so, returns a string giving the result
     * 
     * @result
     * 		the end-of-game message, or null if the game is not over
     */
    @Override
    protected String checkIfGameOver() {



		// loops through all four players and if one of them has no more cards left
		// end the game and print that that player has won!
		for( int i = 0; i<4; i++){
			if( state.getDeck(i).size() == 0 ){
				return this.playerNames[i] + " Has Won!";
			}
		}

		//else return null, stating that the game is not over
		return null;


    }

    /**
     * sends the updated state to the given player. In our case, we need to
     * make a copy of the Deck, and null out all the cards except the top card
     * in the middle deck, since that's the only one they can "see"
     * 
     * @param p
     * 		the player to which the state is to be sent
     */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// if there is no state to send, ignore
		if (state == null) {
			return;
		}

		GamePlayer player = p;
		int pNum = 0;

		if( p instanceof GameComputerPlayer ){

			player = (GameComputerPlayer)p;
			pNum = ((GameComputerPlayer) p).getPlayerNum();

		}
		else if( p instanceof GameHumanPlayer ){

			player = (GameHumanPlayer)p;
			pNum = ((GameHumanPlayer) p).getPlayerNum();

		}

		// make a copy of the state; null out all cards except for the
		// top card in the middle deck
		PDState stateForPlayer = new PDState(state,pNum); // copy of state
		//stateForPlayer.nullAllButTopOf2(); // put nulls except for visible card
		
		// send the modified copy of the state to the player
		p.sendInfo(stateForPlayer);
	}
	
	/**
	 * whether a player is allowed to move
	 *
	 * @param playerIdx
	 * 		the player-number of the player in question
	 */
	protected boolean canMove(int playerIdx) {
		if (playerIdx < 0 || playerIdx > 3) {
			// if our player-number is out of range, return false
			return false;
		}
		else {
			// player can move if it's their turn, or if the middle deck is non-empty
			// so they can slap
			return true;
		}
	}

	/**
	 * makes a move on behalf of a player
	 * 
	 * @param action
	 * 		the action denoting the move to be made
	 * @return
	 * 		true if the move was legal; false otherwise
	 */
	@Override
	protected boolean makeMove(GameAction action) {
		
		// check that we have action; if so cast it
		if (!(action instanceof PDMoveAction) && !(action instanceof PDSelectAction)) {
			return false;
		}

		if( action instanceof  PDMoveAction ) {
			PDMoveAction sjma = (PDMoveAction) action;
			int thisPlayerIdx = getPlayerIdx(sjma.getPlayer());

			if (thisPlayerIdx < 0 || thisPlayerIdx > 3) { // illegal player
				return false;
			}
			if( sjma.isPass() ){

				if (thisPlayerIdx != state.toPlay()) {
					// attempt to pass when it's the other players turn

					return false;

				} else {

					//calls the pass actoin
					Log.i(state.passAction(thisPlayerIdx),"");
				}

				//check if the move action is a play action
			} else if( sjma.isPlay() ){
				if (thisPlayerIdx != state.toPlay()) {
					// attempt to play when it's the other player's turn
					return false;

				} else {
					//play their cards, if they are unable to play the log will print out why
					Log.i(state.playCard(thisPlayerIdx),"");

				}


			} else { // some unexpected action
				return false;
			}
		} else if( action instanceof PDSelectAction ){
			PDSelectAction pdsa = (PDSelectAction) action;

			int thisPlayerIdx = getPlayerIdx(pdsa.getPlayer());

			if (thisPlayerIdx < 0 || thisPlayerIdx > 3) { // illegal player
				return false;
			}
			PDSelectAction selectAction = (PDSelectAction)action;
			Log.i(state.selectCard(thisPlayerIdx,selectAction.getIndex()),"");
			//state.getDeck(thisPlayerIdx).getCards().get(selectAction.getIndex()).setSelected(true);

			//we return false instead of true because, this action is always called in conjunction
			//with another action and if we return true, it will send the updated state for the computer,
			//making it move again, causing a out of bounds error, because the card was already played
			return true;
		}
		
		// get the index of the player making the move; return false


		//check if the move action is a select action
		//if so, cast the action to a select action and call the select card method

		// return true, because the move was successful if we get her
		return true;
	}
	

}
