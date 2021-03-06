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
 *	FEATURES OF SMART COMPUTER PLAYER
 *	- Searches through its cards and finds any existing hands (doubles, straights, flushes,
 *      full houses, and four-of-a-kind)
 *  - Evaluates which hand should be played on any given turn
 *  - Evaluates last card(s) played by previous player, and recognizes the current game mode
 *	- When singles are being played, the smart AI will play its lowest card that beats the last
 *      card played
 *	- When the game is in a hand mode, the smart AI will play its lowest hand that could beat the
 *      current hand
 *	- Passes if it cannot beat the current card(s)
 *
 */

/**
 * This is a smart computer player that plays much more strategically than the regular computer
 * player. When in singles, this AI will "save" its highest cards by playing the lowest playable
 * card. This AI is also capable of playing hands. The AI searches through its cards for hands,
 * which are saved and played when the mode type matches.
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
    /*
    *   Playability values are values that the computer uses to distinguish what the cards can
    *   make certain types of hands, this makes it so the player can distinguish doubles and poker
    *   hands from singles and can save them for later play. Each type of hand has its own value
    *
    */
    private int singles = 1;
    private int doubles = 2;
    private int straight = 3;
    private int flushC = 40;
    private int flushS = 41;
    private int flushH = 42;
    private int flushD = 43;
    private int fullHouse = 5;
    private int fourOfAKind = 6;

    //the computers deck
    private Deck myDeck;

    //An array of selections, this keeps track of what the player selects, all of the values
    //will start at false and any cards selected will be true
    private boolean[] selections;

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
     * RecieveInfo
     *
     * callback method, called when we receive a message, typically from
     * the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {

        // update our state variable
        if (info instanceof PDState) {
            savedState = (PDState) info;

        // if the state is an illegal move, then return
        } else {
            IllegalMoveInfo moveInfo = (IllegalMoveInfo) info;

            return;
        }

        //sets their deck to the deck from the new state
        //also gets the middle deck, for seeing what has been played
        myDeck = savedState.getDeck(playerNum);
        Deck middleDeck = savedState.getDeck(4);

        //gets the size of our deck
        size = myDeck.getCards().size();

        //set the selections array to a size that matches the player's cards
        selections = new boolean[size];
        //loops through every value of selections and sets it to false
        synchronized ( selections ) {
            for (int i = 0; i < size; i++) {
                selections[i] = false;
            }
        }

        //creates a new array list of integers to keep track of existing hands (i.e. doubles,
        // straight, full houses) in the smart AI's hand
        playability = new ArrayList<Integer>();

        //to set all playability values to singles
        for (int i = 0; i < myDeck.size(); i++) {
            playability.add(i, singles);
        }

        /**
         * Checks for hands in smart AI's hand (in this order), changing the playability array to
         * accurately reflect existing hands with each check
         * 1. check for 4 of a kinds
         * 2. check for triples
         * 3. check for flushes
         * 4. check for straights
         * 5. check for doubles
         */
        findFourofAKinds();
        findTriples();
        findFlushes();
        findStraight();
        findDoubles();


        //Has the player wait to make their move
        //and it takes twice as long if they are
        //in control (to slow things down)
        if(savedState.getModeType() == 0) {
            sleep(waitTime*2);
        }
        else{
            sleep(waitTime);
        }

        //----------------------------Assign which cards are playable---------------------------------------



        //If a triple AND a double is found in the hand, change the doubles playability value to
        //full house instead of doubles
        if (playability.contains(fullHouse) && playability.contains(doubles)) {
            //loops through the playability array, finds the lowest double, and changes its
            //playability value to full house
            for (int i = myDeck.size() - 1; i > 1; i--) {
                if (playability.get(i) == doubles && playability.get(i - 1) != null) {
                    playability.set(i, fullHouse);
                    playability.set(i - 1, fullHouse);
                    break;
                }
            }
        }
        //If there is only a triple in the hand (and no double to pair to make a full house),
        //change the triple back into singles
        else {
            //loops through playability array and sets the triple values back to singles
            for (int i = myDeck.size() - 1; i > 0; i--) {
                if (playability.get(i) == fullHouse) {
                    playability.set(i, singles);
                }
            }
        }

        //If a 4-of-a-kind AND a single is found in the hand, change the lowest single's
        //playability value to 4-of-a-kind
        if (playability.contains(fourOfAKind) && playability.contains(singles)) {
            //loops through playability array and change the lowest single to 4-of-a-kind
            for (int i = myDeck.size() - 1; i > 1; i--) {
                if (playability.get(i) == singles) {
                    playability.set(i, fourOfAKind);
                    break;
                }
            }
        }


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
        if (playerNum == savedState.toPlay()) {


            //If they are in control, they play their worst card
            if( savedState.getModeType() == 0 ){

                selections[size-1] = !selections[size-1];
                game.sendAction(new PDPlayAction(this,selections));
                return;
            }
            else if (savedState.getModeType() == singles) {

                //if the gamemode is in singles, call the smart play singles method
                SmartPlaySingles(myDeck, middleDeck);

            }
            else if (savedState.getModeType() == doubles ){

                //if the gamemode is in double, call the method that handles the doubles
                SmartPlayDoubles(myDeck, middleDeck);

            }
            else if( savedState.getModeType() == 4 || savedState.getModeType() == straight ) {

                //If the playability array contains any type of flush, set the searchingFor
                //value to the value corresponding to the type of flush
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

                //If a flush exists in the hand, do the following
                if( found ) {
                    //loops through the playability array, and selects the cards that are a part
                    //of the flush
                    for (int i = 0; i < playability.size(); i++) {

                        //if the playability value of the current card matches that of a set flush,
                        //set the selection value at that index to true
                        if (playability.get(i) == searchingFor ) {
                            selections[i] = !selections[i];
                        }
                    }
                    game.sendAction(new PDPlayAction(this,selections)); //play the flush
                    return;
                }
                //if no flush is found, pass
                game.sendAction(new PDPassAction(this));
                return;
            }
            else if (savedState.getModeType() == fullHouse  || savedState.getModeType() == 4 ||
                    savedState.getModeType() == straight){

                //Boolean to determine if the full house is legal
                boolean canPlayFullHouse = true;
                int count = 0;
                for (int i = playability.size()-1; i>=0; i--) {

                    if( count == 2 ) {
                        //Checks the power of the triple
                        if( myDeck.getCards().get(i).getPower() > middleDeck.getCards().get(2).getPower()){
                            canPlayFullHouse = true;
                        } else {
                            canPlayFullHouse = false;
                        }
                    }

                    if (playability.get(i) == fullHouse ) {
                        //game.sendAction(new PDSelectAction(this, i));
                        selections[i] = !selections[i];
                    }
                    count++;

                }
                if( canPlayFullHouse ) {
                    game.sendAction(new PDPlayAction(this,selections));
                }
                else{
                    game.sendAction(new PDPassAction(this));
                }
                game.sendAction(new PDPassAction(this));
                return;
            }
            else if ((savedState.getModeType()== fourOfAKind ||savedState.getModeType() == fullHouse  ||
                    savedState.getModeType() == 4 || savedState.getModeType() == straight)
                    && playability.contains(fourOfAKind) ){

                for (int i = playability.size()-1; i>=0; i--) {

                    if (playability.get(i) == fourOfAKind ) {
                        //game.sendAction(new PDSelectAction(this, i));
                    }
                }
                game.sendAction(new PDPlayAction(this,selections));
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
     * findDoubles
     *
     * Checks for any doubles in hand (of remaining cards that have not been assigned to a
     * better hand).
     */
    public void findDoubles() {

        for( int i=myDeck.size()-1; i>1; i--) {

            if( playability.get(i)==singles && playability.get(i-1)==singles ){
                if( myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-1).getRank() ) {
                    //Assign the doubles
                    playability.set( i, doubles);
                    playability.set( i-1, doubles );
                }
            }
        }
    }

    /**
     * findTriples
     *
     * Checks for any triples in hand (of remaining cards that have not been assigned to a
     * better hand).
     */
    public void findTriples() {

        for( int i=myDeck.size()-1; i>2; i--) {
            //Checks if the card is the same as the two that follow
            if(playability.get(i)==singles && playability.get(i-1) == singles && playability.get(i-2) == singles){
                //Makes sure the card hasn't been labeled yet
                if( playability.get(i) == singles ){
                    if( myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-1).getRank()
                            && myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-2).getRank()) {
                        //assigns the triples it finds to the Full House hand type
                        playability.set(i, fullHouse);
                        playability.set(i - 1, fullHouse);
                        playability.set(i - 2, fullHouse);
                    }
                }
            }
        }
    }

    /**
     * findFlushes
     *
     * Checks for any flushes in hand (of remaining cards that have not been assigned to a
     * better hand).
     */
    public void findFlushes() {


        //creates separate array lists of cards to "sort" through the players hand by suit
        //this will keep track of every type of suit and will count how many suits at the end
        ArrayList<Integer> heartCount = new ArrayList<Integer>();
        ArrayList<Integer> diamondCount = new ArrayList<Integer>();
        ArrayList<Integer> spadeCount = new ArrayList<Integer>();
        ArrayList<Integer> clubCount = new ArrayList<Integer>();

        //loops through player's hand
        for( int i=myDeck.size()-1; i>0; i-- ){
            //if the current card is not a part of a higher hand, do the following
            if( playability.get(i) == singles ){
                //add the current card to its corresponding suit array list based on its suit
                //(i.e. a diamond card gets added into the diamonCount array list)
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

        /**
         * If any of the separate suit array lists have at least 5 cards in them, that means there
         * is a flush in the players hand.
         * If this is true, set the playability value of the lowest 5 cards of the same suit to
         * its designated flush value:
         * - DIAMOND FLUSH : flushD
         * - HEART FLUSH : flushH
         * - SPADE FLUSH : flushS
         * - CLUB FLUSH : flushC
         */
        if( diamondCount.size() >= 5 ) {
            //Sets the playability values to Flush
            playability.set(diamondCount.get(0), flushD);
            playability.set(diamondCount.get(1), flushD);
            playability.set(diamondCount.get(2), flushD);
            playability.set(diamondCount.get(3), flushD);
            playability.set(diamondCount.get(4), flushD);
        }
        else if( heartCount.size() >= 5 ){
            //sets the playability values to flush
            playability.set(heartCount.get(0), flushH);
            playability.set(heartCount.get(1), flushH);
            playability.set(heartCount.get(2), flushH);
            playability.set(heartCount.get(3), flushH);
            playability.set(heartCount.get(4), flushH);
        }
        else if( spadeCount.size() >= 5 ){
            //sets the playability values to flush
            playability.set(spadeCount.get(0), flushS);
            playability.set(spadeCount.get(1), flushS);
            playability.set(spadeCount.get(2), flushS);
            playability.set(spadeCount.get(3), flushS);
            playability.set(spadeCount.get(4), flushS);
        }
        else if( clubCount.size() >= 5 ){
            //sets the playability values to flush
            playability.set(clubCount.get(0), flushC);
            playability.set(clubCount.get(1), flushC);
            playability.set(clubCount.get(2), flushC);
            playability.set(clubCount.get(3), flushC);
            playability.set(clubCount.get(4), flushC);
        }
    }

    /**
     * findFourofAKinds()
     *
     * checks to see if their is a four of a kind in the player's hand
     */
    public void findFourofAKinds(){
        //loops through all of the cards, from highest to lowest
        for(int i = myDeck.size()-1; i > 4; i--){
            //Makes sure that each of the cards has the same rank
            if(myDeck.getCards().get(i).getRank() == myDeck.getCards().get(i-1).getRank() &&
                    myDeck.getCards().get(i-1).getRank() == myDeck.getCards().get(i-2).getRank() &&
                    myDeck.getCards().get(i-2).getRank() == myDeck.getCards().get(i-3).getRank()){

                //Sets all of their values in the playability array
                playability.set(i, fourOfAKind);
                playability.set(i-1, fourOfAKind);
                playability.set(i-2, fourOfAKind);
                playability.set(i-3, fourOfAKind);

            }
        }
    }

    /**
     * findStraight()
     *
     * checks to see if their is a Straight in the player's hand
     */
    public void findStraight(){

        //creates an arraylist of integers called count and creates an integer for holding
        //the index
        ArrayList<Integer> count = new ArrayList<Integer>();
        int countIdx = 0;

        //loops through all the cards from top to bottom
        for( int i=myDeck.size()-1; i>0; i--) {

            //only changes cards that are singles, the ones that have been changes already
            //have priority over the straight
            if( playability.get(i) == singles ){

                //resets count for every time it checks a new card in the first loop,
                //sets the index to the starting card that it is checkig
                count = new ArrayList<Integer>();
                countIdx = i;
                count.add(countIdx);

                //loops through the cards after the one it is starting with
                for( int j = i; j>1; j--) {

                    //checks to see if the card is the one rank below the card before, if that is the
                    //case and the count is not 5 already, add that to the count array, keeping track if
                    //the straight can be continued
                    if (myDeck.getCards().get(j).getPower() / 4 == myDeck.getCards().get(j - 1).getPower() / 4 + 1) {

                        //if the count if 5 the straight has already been found and will be set and the loop will
                        //break
                        if (count.size() == 5) {

                            playability.set(count.get(0), straight);
                            playability.set(count.get(1), straight);
                            playability.set(count.get(2), straight);
                            playability.set(count.get(3), straight);
                            playability.set(count.get(4), straight);

                            break;

                        }

                        //add the card to the arraylist if it can be used in the straight
                        count.add(j);

                    //if the card is the same rank, that means there is still the possibility of a straight
                    //therefore, instead of breaking the search, we just continue and ignore that card
                    } else if (myDeck.getCards().get(j).getPower() / 4 == myDeck.getCards().get(j).getPower() / 4) {

                    //if the next card is not the same rank as the one next to it nor one rank less
                    //break the search, because the straight cannot be made anymore
                    } else {

                        break;
                    }
                }
            }
        }
    }

    /**
     *
     * @param initDeck
     * @param initMiddleDeck
     */
    public void SmartPlaySingles(Deck initDeck, Deck initMiddleDeck) {
        //Int to hold the position of their worst card
        int worstCard;
        myDeck = initDeck;
        Deck middleDeck = initMiddleDeck;
        //If they are not in control they play their worst possible card
        for(worstCard = myDeck.getCards().size()-1; worstCard >= 0; worstCard--) {
            //Checks to see if their worst card can be player, if
            //not then it moves to their next highest card
            if (myDeck.getCards().get(worstCard).getPower() > middleDeck.getCards().get(middleDeck.getCards().size() - 1).getPower()) {

                selections[worstCard] = !selections[worstCard];
                game.sendAction(new PDPlayAction(this,selections));

            }

        }
        //If they cannot play they pass
        game.sendAction(new PDPassAction(this));

    }

    /**
     *
     * @param initDeck
     * @param initMiddleDeck
     */
    public void SmartPlayDoubles(Deck initDeck, Deck initMiddleDeck) {
        //saves the data for the deck and middle deck
        myDeck = initDeck;
        Deck middleDeck = initMiddleDeck;

        //loops through the array that tells all of the playabilities of every card
        for( int i = 0; i < playability.size(); i++ ){
            //if one of the cards can be played as a double
            if(playability.get(i) == doubles ){

                //select that card, aswell as its partner code
                selections[i] = !selections[i];
                selections[i+1] = !selections[i+1];

                //check if the selected cards have a higher power than the ones in the middle,
                //if they are higher, play the pair, else pass
                if(myDeck.getCards().get(i).getPower() > middleDeck.getCards().get(middleDeck.getCards().size()-1).getPower()){
                    game.sendAction(new PDPlayAction(this,selections));
                }
                else{
                    game.sendAction(new PDPassAction(this));
                }
                return;
            }
        }
        game.sendAction(new PDPassAction(this));
        return;

    }

    /**
     * getSelections()
     *
     * @return
     *     returns the array of selected cards
     */
    public boolean[] getSelections(){
        return selections;
    }


}
