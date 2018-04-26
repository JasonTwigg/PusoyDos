package edu.up.cs301.pusoydos;

import org.junit.Test;

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

    }

    @Test
    public void toPlay() throws Exception {

        PDState PDState = new PDState();

        int num = PDState.toPlay();
        assertTrue(PDState.getDeck(num).getCards().get(12).getPower() == 0);

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

        PDState PDState = new PDState();
        PDState.setTurnNum(3);
        PDState.changeTurn();
        assertTrue(PDState.getTurnNum() == 0);

    }

    @Test
    public void canPlay() throws Exception {

       /* PDState PDState = new PDState();

        int num = PDState.toPlay();

        PDState.selectCard(num,12);
        PDState.playCard(num);

        PDState.changeTurn();
        num = PDState.toPlay();
        PDState.selectCard(num,0);
        PDState.playCard(num);

        PDState.changeTurn();
        num = PDState.toPlay();
        PDState.selectCard(num,11);
        assertFalse(PDState.canPlay(PDState.getDeck(num).getCards()));
*/
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
        PDState PDState = new PDState();



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