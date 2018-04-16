package edu.up.cs301.pusoydos;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
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

//a
public class PDComputerPlayerSmart extends GameComputerPlayer
{
    // the minimum reaction time for this player, in milliseconds
    private double minReactionTimeInMillis;

    // the most recent state of the game
    private PDState savedState;
    private int size;
    //Constant for the time each computer takes to go
    private int waitTime = 750;
    private ArrayList<Integer> playability;

    //playability values
    private int singles = 1;
    private int doubles = 2;
    private int straight = 3;
    private int flushC = 40;
    private int flushS = 41;
    private int flushH = 42;
    private int flushD = 43;
    private int fullHouse = 5;
    private int fourOfAKind = 6;

    private Deck myDeck;
    private int diamond, heart, spade, club;

    /**
     * Constructor for the SJComputerPlayer class; creates an "average"
     * player.
     *
     * @param name
     * 		the player's name
     */
    public PDComputerPlayerSmart(String name) {
        // invoke general constructor to create player whose average reaction
        // time is half a second.
        this(name, 0.5);


    }

    /*
     * Constructor for the SJComputerPlayer class
     */
    public PDComputerPlayerSmart(String name, double avgReactionTime) {
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
        if( info instanceof  PDState ){
            savedState = (PDState)info;

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

        /*
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


        ArrayList<Integer> count = new ArrayList<Integer>();
        int countIdx = 0;
        //checking for straight
        for( int i=myDeck.size()-1; i>0; i--) {

            if( playability.get(i) == 0 ){
                count = 0;
                countIdx = i;
                while( countIdx > 0 ){

                    if( myDeck.getCards().get(i).getPower()/4 == myDeck.getCards().get(i-1).getPower()/4 + 1){


                        count++;

                        if( count == 4){

                            playability.set(i, );
                            playability.set(i - 1, fullHouse);
                            playability.set(i - 2, fullHouse);
                            playability.set(i - 3, fullHouse);
                            playability.set(i - 4, fullHouse);


                        }


                    } else if ( myDeck.getCards().get(i).getPower()/4 == myDeck.getCards().get(i-1).getPower()/4 + 1) {


                    } else {



                        break;
                    }




                }




                if( myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-1).getRank()
                        && myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-2).getRank()) {
                    playability.set(i, fullHouse);
                    playability.set(i - 1, fullHouse);
                    playability.set(i - 2, fullHouse);
                }
            }
        }
        */


        //checking for flushes
        findFlushes();

        //checking for triples for a full house
        findTriples();

        //checking for doubles in hand
        findDoubles();

        if( playability.contains(fullHouse) && playability.contains(doubles)){

            for( int i=0; i<myDeck.size()-1; i++){
                if( playability.get(i) == doubles && playability.get(i+1)!=null){
                    playability.set(i, fullHouse);
                    playability.set(i+1, fullHouse);
                    break;
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
                game.sendAction(new PDPlayAction(this));
                return;
            }
            else if (savedState.getModeType() == 1) {
                //If they are not in control they play their best card
                if( myDeck.getCards().get(0).getPower() > middleDeck.getCards().get(middleDeck.getCards().size()-1).getPower()) {
                    game.sendAction(new PDSelectAction(this, 0));
                    game.sendAction(new PDPlayAction(this));
                } else {
                    //If they cannot play, they pass
                    game.sendAction(new PDPassAction(this));
                }
            }
            else if (savedState.getModeType() == 2 ){
                for( int i = 0; i < playability.size(); i++ ){
                    if(playability.get(i) == doubles ){
                        game.sendAction(new PDSelectAction(this, i));
                        game.sendAction(new PDSelectAction(this, i+1));
                        game.sendAction(new PDPlayAction(this));
                        return;
                    }
                }
                game.sendAction(new PDPassAction(this));
                return;
            }
            else if( savedState.getModeType() == 4 || savedState.getModeType() == 3 ) {

                int searchingFor = -1;
                boolean found = true;
                if( playability.contains(flushC)){
                    searchingFor = flushC;
                } else if( playability.contains(flushS)){
                    searchingFor = flushS;
                } else if( playability.contains(flushH)){
                    searchingFor = flushH;
                } else if( playability.contains(flushD)){
                    searchingFor = flushD;
                } else {
                    found = false;
                }

                if( found ) {
                    for (int i = 0; i < playability.size(); i++) {

                        if (playability.get(i) == searchingFor ) {
                            game.sendAction(new PDSelectAction(this, i));
                        }
                    }
                    game.sendAction(new PDPlayAction(this));
                    return;
                }
                game.sendAction(new PDPassAction(this));
                return;
            }
            else if (savedState.getModeType() == 5  || savedState.getModeType() == 4 ||
                    savedState.getModeType() == 3){
                for( int i = 0; i < playability.size(); i++ ){
                    if(playability.get(i) == fullHouse ){
                        game.sendAction(new PDSelectAction(this, i));
                        game.sendAction(new PDSelectAction(this, i+1));
                        game.sendAction(new PDSelectAction(this, i+2));
                        game.sendAction(new PDPlayAction(this));
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

    /**
     * Checks for any doubles in hand (of remaining cards that have not been assigned to a
     * better hand).
     */
    public void findDoubles() {

        for( int i=myDeck.size()-1; i>1; i--) {

            if( playability.get(i)==singles && playability.get(i-1)==singles ){
                if( myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-1).getRank() ) {
                    playability.set( i, doubles);
                    playability.set( i-1, doubles );
                }
            }
        }
    }

    /**
     * Checks for any triples in hand (of remaining cards that have not been assigned to a
     * better hand).
     */
    public void findTriples() {

        for( int i=myDeck.size()-1; i>3; i--) {

            if(playability.get(i)==1 && playability.get(i-1) == 1 && playability.get(i-2) == 1){
                if( playability.get(i) == singles ){
                    if( myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-1).getRank()
                            && myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-2).getRank()) {
                        playability.set( i, fullHouse);
                        playability.set( i-1, fullHouse );
                        playability.set( i-2, fullHouse );
                    }
                }
            }
        }
    }

    /**
     * Checks for any flushes in hand (of remaining cards that have not been assigned to a
     * better hand).
     */
    public void findFlushes() {
        ArrayList<Integer> heartCount = new ArrayList<Integer>();
        ArrayList<Integer> diamondCount = new ArrayList<Integer>();
        ArrayList<Integer> spadeCount = new ArrayList<Integer>();
        ArrayList<Integer> clubCount = new ArrayList<Integer>();

        for( int i=myDeck.size()-1; i>0; i-- ){
            if( playability.get(i) == singles ){
                if( myDeck.getCards().get(i).getSuit() == Suit.Diamond) {
                    diamondCount.add(i);
                }
                else if( myDeck.getCards().get(i).getSuit() == Suit.Heart) {
                    heartCount.add(i);
                }
                else if( myDeck.getCards().get(i).getSuit() == Suit.Spade) {
                    spadeCount.add(i);
                }
                else if( myDeck.getCards().get(i).getSuit() == Suit.Club) {
                    clubCount.add(i);
                }
            }
        }

        if( diamondCount.size() >= 5 ) {
            playability.set(diamondCount.get(0), flushD);
            playability.set(diamondCount.get(1), flushD);
            playability.set(diamondCount.get(2), flushD);
            playability.set(diamondCount.get(3), flushD);
            playability.set(diamondCount.get(4), flushD);
        }
        else if( heartCount.size() >= 5 ){
            playability.set(heartCount.get(0), flushH);
            playability.set(heartCount.get(1), flushH);
            playability.set(heartCount.get(2), flushH);
            playability.set(heartCount.get(3), flushH);
            playability.set(heartCount.get(4), flushH);
        }
        else if( spadeCount.size() >= 5 ){
            playability.set(spadeCount.get(0), flushS);
            playability.set(spadeCount.get(1), flushS);
            playability.set(spadeCount.get(2), flushS);
            playability.set(spadeCount.get(3), flushS);
            playability.set(spadeCount.get(4), flushS);
        }
        else if( clubCount.size() >= 5 ){
            playability.set(clubCount.get(0), flushC);
            playability.set(clubCount.get(1), flushC);
            playability.set(clubCount.get(2), flushC);
            playability.set(clubCount.get(3), flushC);
            playability.set(clubCount.get(4), flushC);
        }
    }

}
