package edu.up.cs301.pusoydos;

import org.junit.Test;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;

import static org.junit.Assert.*;

/**
 * Created by Jason on 3/22/2018.
 */
public class PDStateTest {

    @Test
    public void getDeck() throws Exception {

        //creates a instance of PDState, this will start a new game
        PDState pdState = new PDState();

        //gets the player index of whose turn it is
        int num = pdState.toPlay();


        //checks that the player has three cards prior to playing their card
        assertTrue(pdState.getDeck(num).size() == 13 );

        //setsup the boolean array for what cards are selected for the player and selects
        //index 12
        boolean[] selected = new boolean[13];
        for( int i = 0; i < 13; i++ ){
            if( i == 12 ){
                selected[i] = true;
            } else {
                selected[i] = false;
            }
        }

        //the player plays their three of clubs
        pdState.playCard(num,selected);

        //checks to make sure the the deck now has 12 cards
        assertTrue(pdState.getDeck(num).size() == 12 );

    }

    @Test
    public void toPlay() throws Exception {

        //creates a instance of PDState, this will start a new game
        PDState pdState = new PDState();

        //gets the player index of whose turn it is
        int num = pdState.toPlay();
        int size = pdState.getDeck(num).getCards().size();

        //checks that the player who starts has the three of clubs, this will be his 12th
        //card because they are sorted best to worst, and the three of clubs has power 0
        assertTrue(pdState.getDeck(num).getCards().get(size-1).getPower() == 0);



    }

    @Test
    public void setToPlay() throws Exception {

    }

    @Test
    public void nullAllButTopOf2() throws Exception {

    }

    @Test
    public void selectCard() throws Exception {

        //PDState PDState = new PDState();
        //int num = PDState.toPlay();

        //PDState.selectCard(num,0);
        //assertTrue(PDState.getDeck(num).getCards().get(0).isSelected());


    }

    @Test
    public void playCard() throws Exception {

        //New Game State
        PDState pdState = new PDState();
        //The current players turn number
        int num = pdState.getTurnNum();
        //The number of cards in their hand
        int size = pdState.getDeck(num).size();

        //This player selects their second best card
        boolean selected[] = new boolean[size];
        //Selects the second best card in the hand
        selected[1] = true;
        //Player tries to play a card besides the 3 of Clubs for the first play
        pdState.playCard(num, selected);

        //They should not have been able to play a card
        assertFalse(pdState.getDeck(num).size() == size-1);

        //It should still be this persons turn because they could not play
        assertTrue(pdState.getTurnNum() == num);
        //undo the selection from earlier
        selected[1]=false;

        //Player selects their 3 of clubs
        selected[size-1] = true;

        pdState.passAction(num);
        //They should not be able to pass even though they selected the 3 of Clubs
        assertFalse(pdState.getTurnNum() != num);

        //Player plays their 3 of clubs
        pdState.playCard(num, selected);
        //The turn num should now be different
        assertFalse(pdState.getTurnNum() == num);
        //Set the array back
        selected[size-1] = false;


        //The current players turn number
        int numTwo = pdState.getTurnNum();
        //Make sure that the turn number moved
        assertFalse(num == numTwo);


    }

    @Test
    public void passAction() throws Exception {

        //New Game State
        PDState PDState = new PDState();
        //The current players turn number
        int num = PDState.getTurnNum();
        //The number of cards in their hand

        //Player tries to pass for their first move (of the game)
        PDState.passAction(num);
        //They should not have been able to pass on the start
        assertTrue(PDState.getTurnNum() == num);

    }

    @Test
    public void changeTurn() throws Exception {


        //goes through every turn change possibility and makes sure the turns are changing
        //correctly
        PDState pdState = new PDState();
        pdState.setTurnNum(0);
        assertTrue(pdState.getTurnNum() == 0);
        pdState.changeTurn();
        assertTrue(pdState.getTurnNum() == 1);
        pdState.changeTurn();
        assertTrue(pdState.getTurnNum() == 2);
        pdState.changeTurn();
        assertTrue(pdState.getTurnNum() == 3);
        pdState.changeTurn();
        assertTrue(pdState.getTurnNum() == 0);

    }

    @Test
    public void canPlay() throws Exception {

        //---------------------------------CASE 1---------------------------------------
        //checks to make sure the lowest card (3 of clubs) is the first card played

        //new instances of game state to start new game
        PDState PdState = new PDState();

        //gets turn number of first player (player with 3 of clubs)
        int num = PdState.toPlay();

        //setsup the boolean array for what cards are selected for the player and selects
        //index 12 (3 of clubs)
        boolean[] selected = new boolean[13];
        for( int i = 0; i < 13; i++ ){
            if( i == 12 ){
                selected[i] = true;
            } else {
                selected[i] = false;
            }
        }

        //creates a new array list of cards to pass into the canPlay method
        ArrayList<Card> selectedCards = new ArrayList<Card>();
        //loops through the selected boolean array and puts selected cards into the new array list
        for( int i=0; i<13; i++ ){
            if( selected[i] == true ){
                selectedCards.add(PdState.getDeck(num).getCards().get(i));
            }
        }
        //checks to make sure the 3 of clubs is played in the first play of the game
        assertTrue(PdState.canPlay(selectedCards));




        //--------------------------------CASE 2 & 3----------------------------------------

        //new instance of game state to start new game
        PDState PdState2 = new PDState();
        //gets turn number of first player (player with 3 of clubs)
        int num2 = PdState2.toPlay();

        PdState2.getDeck(num2).getCards().add(new Card( Rank.THREE, Suit.Diamond));

        //sets up the boolean array for what cards are selected for the player and selects
        //index 12 (3 of clubs)
        boolean[] selected2 = new boolean[14];
        for( int i = 0; i < 14; i++ ){
            if( i == 12 || i == 13 ){
                selected2[i] = true;
            } else {
                selected2[i] = false;
            }
        }

        //have first player play a double (3 of clubs and 3 of diamonds)
        PdState2.playCard(num2, selected2);

        //creates new array list of cards to pass into the canPlay method
        ArrayList<Card> selectedCards2 = new ArrayList<Card>();
        selectedCards2.add(new Card(Rank.FIVE, Suit.Heart));
        selectedCards2.add(new Card(Rank.FIVE, Suit.Diamond));

        //checks to make sure the mode type is 2 when doubles are in play
        assertTrue(PdState2.getModeType() == 2);
        //checks to make sure a higher double can be played after a lower double
        assertTrue(PdState2.canPlay(selectedCards2));



        //--------------------------------CASE 4-------------------------------------------------
        //new instance of game state to start new game
        PDState PdState3 = new PDState();
        //gets turn number of first player (player with 3 of clubs)
        int num3 = PdState3.toPlay();

        PdState3.getDeck(num3).getCards().add(new Card( Rank.THREE, Suit.Diamond));

        //sets up the boolean array for what cards are selected for the player and selects
        //index 12 (3 of clubs)
        boolean[] selected3 = new boolean[14];
        for( int i = 0; i < 14; i++ ){
            if( i == 12 || i == 13 ){
                selected3[i] = true;
            } else {
                selected3[i] = false;
            }
        }

        //have first player play a double (3 of clubs and 3 of diamonds)
        PdState3.playCard(num3, selected3);

        //creates new array list of cards to pass into the canPlay method
        ArrayList<Card> selectedCards3 = new ArrayList<Card>();
        selectedCards3.add(new Card(Rank.FIVE, Suit.Heart));
        selectedCards3.add(new Card(Rank.FIVE, Suit.Diamond));

        //force change mode type to STRAIGHT
        PdState3.setModeType(3);

        //checks to make sure the double cannot be played in straight mode
        assertFalse(PdState3.canPlay(selectedCards3));



    }

    @Test
    public void getPiles() throws Exception {

    }

    @Test
    public void setPiles() throws Exception {

    }

    @Test
    public void getPileSizes() throws Exception {

    }

    @Test
    public void setPileSizes() throws Exception {

    }

    @Test
    public void getTurnNum() throws Exception {
        //New Game State
        PDState pdState = new PDState();
        //The current players turn number
        int num = pdState.getTurnNum();
        //The number of cards in their hand
        int size = pdState.getDeck(num).size();

        //This player selects their worst card
        boolean selected[] = new boolean[size];
        selected[size-1] = true;
        //The player plays their worst card
        pdState.playCard(num, selected);

        //This test should pass as they should have selected their 3 of clubs
        //Since the person with the 3 of clubs goes first
        assertTrue(pdState.getTurnNum() != num);

    }

    @Test
    public void setTurnNum() throws Exception {

    }

    @Test
    public void getPerspective() throws Exception {

    }

    @Test
    public void setPerspective() throws Exception {

    }

    @Test
    public void getCardsSelected() throws Exception {

    }

    @Test
    public void setCardsSelected() throws Exception {

    }

}