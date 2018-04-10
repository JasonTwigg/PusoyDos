package edu.up.cs301.pusoydos;

import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.TimerInfo;
import edu.up.cs301.game.util.PossibleHands;

/**
 * This is a computer player that slaps at an average rate given
 * by the constructor parameter.
 * 
 * @author Steven R. Vegdahl
 *
 * @author Jason Twigg
 * @author Cole Holbrook
 * @author Tawny Motoyama
 * @author Josh Azicate
 *
 * @version July 2013 
 */
public class SJComputerPlayer extends GameComputerPlayer
{
	// the minimum reaction time for this player, in milliseconds
	private double minReactionTimeInMillis;
	
	// the most recent state of the game
	private SJState savedState;
	private int size;
	
    /**
     * Constructor for the SJComputerPlayer class; creates an "average"
     * player.
     * 
     * @param name
     * 		the player's name
     */
    public SJComputerPlayer(String name) {
        // invoke general constructor to create player whose average reaction
    	// time is half a second.
        this(name, 0.5);
    }	
    
    /*
     * Constructor for the SJComputerPlayer class
     */
    public SJComputerPlayer(String name, double avgReactionTime) {
        // invoke superclass constructor
        super(name);
        
        // set the minimim reaction time, which is half the average reaction
        // time, converted to milliseconds (0.5 * 1000 = 500)
        minReactionTimeInMillis = 500*avgReactionTime;
    }



    /**
     * callback method, called when we receive a message, typicallly from
     * the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {


		// update our state variable
		if( info instanceof  SJState ){
			savedState = (SJState)info;

		} else {
			IllegalMoveInfo moveInfo = (IllegalMoveInfo)info;
			Log.i("ERROR","");
			return;
		}


		Deck myDeck = savedState.getDeck(playerNum);
		Deck middleDeck = savedState.getDeck(4);



		synchronized (myDeck) {
			size = myDeck.getCards().size();
		}

		sleep(750);
		if( playerNum == savedState.toPlay() ){


			if( savedState.getModeType() == 0 ){
				game.sendAction(new PDSelectAction(this,size-1));
				game.sendAction(new SJPlayAction(this));
				return;
			} else {
				if( myDeck.getCards().get(0).getPower() > middleDeck.getCards().get(middleDeck.getCards().size()-1).getPower()) {
					game.sendAction(new PDSelectAction(this, 0));
					game.sendAction(new SJPlayAction(this));
				} else {
					game.sendAction(new PDPassAction(this));
				}

				if( 1==1){
					return;
				}
				/*

				if(middleDeck.getCards().size() == 0 ){
					game.sendAction(new PDSelectAction(this,myDeck.size()-1));
					game.sendAction(new SJPlayAction(this));
					return;
				}
				for( int i = myDeck.getCards().size()-1; i>=0; i++){
					if(myDeck.getCards().get(i).getPower() > middleDeck.getCards().get(middleDeck.getCards().size()-1).getPower()){

						game.sendAction(new PDSelectAction(this,i-1));
						game.sendAction(new SJPlayAction(this));
						return;
					}
				}
				*/
			}


		} else {
			return;
		}

		if( 1==1 ){
			return;
		}


    	// if we don't have a game-state, ignore
    	if (!(info instanceof SJState)) {
    		return;
    	}
    	

    	
    	// access the state's middle deck


		if( middleDeck != null) {
			Card topCard = middleDeck.getCards().get(0);
		}


		//game.sendAction(new PDPassAction(this));

		if( savedState.getModeType() == 0 ){
			game.sendAction(new PDSelectAction(this,myDeck.getCards().size()-2));
			game.sendAction(new SJPlayAction(this));
		} else {
			game.sendAction(new PDPassAction(this));
		}

		//WHERE COMPUTER THINKS
		/*
    	// look at the top card
    	Card topCard = middleDeck.peekAtTopCard();
    	
    	// if it's a Jack, slap it; otherwise, if it's our turn to
    	// play, play a card
    	if (topCard != null && topCard.getRank() == Rank.JACK) {
    		// we have a Jack to slap: set up a timer, depending on reaction time.
    		// The slap will occur when the timer "ticks". Our reaction time will be
    		// between the minimum reaction time and 3 times the minimum reaction time
        	int time = (int)(minReactionTimeInMillis*(1+2*Math.random()));
    		this.getTimer().setInterval(time);
    		this.getTimer().start();
    	}
    	else if (savedState.toPlay() == this.playerNum) {
    		// not a Jack but it's my turn to play a card
    		
    		// delay for up to two seconds; then play
        	sleep((int)(2000*Math.random()));
        	
        	// submit our move to the game object
        	game.sendAction(new SJPlayAction(this));
    	}

    	*/
    }

	public ArrayList<PossibleHands> getPossibleHands (){
		return null;
	}


}
