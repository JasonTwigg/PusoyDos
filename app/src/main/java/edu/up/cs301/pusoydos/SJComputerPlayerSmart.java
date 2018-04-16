package edu.up.cs301.pusoydos;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.TimerInfo;
import edu.up.cs301.game.util.PossibleHands;

/**
 * This is a computer player that plays very poorly. It plays
 * the highest card it has, unless in control, in which case
 * it plays the worst card.
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
public class SJComputerPlayerSmart extends GameComputerPlayer
{
    // the minimum reaction time for this player, in milliseconds
    private double minReactionTimeInMillis;

    // the most recent state of the game
    private SJState savedState;
    private int size;
    //Constant for the time each computer takes to go
    private int waitTime = 750;
    private ArrayList<Integer> playability;

    //playability values
    private int singles = 1;
    private int doubles = 2;
    private int straight = 3;
    private int flush = 4;
    private int fullHouse = 5;
    private int fourOfAKind = 6;

    private Deck myDeck;

    /**
     * Constructor for the SJComputerPlayer class; creates an "average"
     * player.
     *
     * @param name
     * 		the player's name
     */
    public SJComputerPlayerSmart(String name) {
        // invoke general constructor to create player whose average reaction
        // time is half a second.
        this(name, 0.5);


    }

    /*
     * Constructor for the SJComputerPlayer class
     */
    public SJComputerPlayerSmart(String name, double avgReactionTime) {
        // invoke superclass constructor
        super(name);


    }
    /**
     * callback method, called when we receive a message, typically from
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

        myDeck = savedState.getDeck(playerNum);
        Deck middleDeck = savedState.getDeck(4);

        synchronized (myDeck) {
            size = myDeck.getCards().size();
        }


        playability = new ArrayList<Integer>();
        //to set all playability values to singles
        for( int i=0; i<myDeck.size(); i++) {
            playability.add( i, singles);
        }


        //checking for doubles in hand
        for( int i=myDeck.size()-1; i>2; i--) {
            if( myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-1).getRank() ) {
                playability.set( i, doubles);
                playability.set( i-1, doubles );
            }
            if( myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-2).getRank()
                    && myDeck.getCards().get(i-2)!=null ) {
                playability.set(i, doubles);
                playability.set(i-2, singles);
            }
        }

        //checking for triples for a full house
        for( int i=myDeck.size()-1; i>3; i--) {

            if( playability.get(i) == 0 ){
                if( myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-1).getRank()
                        && myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-2).getRank()) {
                    playability.set( i, fullHouse);
                    playability.set( i-1, fullHouse );
                    playability.set( i-2, fullHouse );
                }
            }
        }




        //Has the player wait to make their move
        //and it takes twice as long if they are
        //in control (to slow things down)

        /**
         External Citation
         Date: March 3, 2018
         Problem: We couldn't remember exactly how sleeping worked with
         a thread
         Resource:
         https://developer.android.com/reference/java/lang/Thread.html
         Solution: This information helped us get a better grasp
         on threads and how to use sleep
         */


        //Computer plays if it is their turn
        if( playerNum == savedState.toPlay() ){

            //If they are in control, they play their worst card
            if( savedState.getModeType() == 0 ){
                game.sendAction(new PDSelectAction(this,size-1));
                game.sendAction(new SJPlayAction(this));
                return;
            }
            else if (savedState.getModeType() == 1) {
                //If they are not in control they play their best card
                if( myDeck.getCards().get(0).getPower() > middleDeck.getCards().get(middleDeck.getCards().size()-1).getPower()) {
                    game.sendAction(new PDSelectAction(this, 0));
                    game.sendAction(new SJPlayAction(this));
                } else {
                    //If they cannot play, they pass
                    game.sendAction(new PDPassAction(this));
                }

                if( 1==1){
                    return;
                }
            }
            else if (savedState.getModeType() == 2 ){
                for( int i = 0; i < playability.size(); i++ ){
                    if(playability.get(i) == doubles ){
                        game.sendAction(new PDSelectAction(this, i));
                        game.sendAction(new PDSelectAction(this, i+1));
                        game.sendAction(new SJPlayAction(this));
                        return;
                    }
                }
                game.sendAction(new PDPassAction(this));
                return;
            }
            else if (savedState.getModeType() == 5 ){
                for( int i = 0; i < playability.size(); i++ ){
                    if(playability.get(i) == fullHouse ){
                        game.sendAction(new PDSelectAction(this, i));
                        game.sendAction(new PDSelectAction(this, i+1));
                        game.sendAction(new PDSelectAction(this, i+2));
                        game.sendAction(new SJPlayAction(this));
                        return;
                    }
                }
                game.sendAction(new PDPassAction(this));
                return;
            }
            else {
                //Passes if the game mode is not singles
                game.sendAction((new PDPassAction(this)));
                return;
            }
        } else {
            //If it is not their turn
            return;
        }


    }

    /**
     * getPossibleHands method, we will be implementing this method in the future,
     * it will be used for the computer player to help decide which hand it
     * should choose to play.
     */
    public ArrayList<PossibleHands> getPossibleHands (){
        return null;
    }

}
