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

        PDState PDState = new PDState();

        int num = PDState.getTurnNum();

        //PDState.getDeck(num).getCards().get(12).setSelected(true);
/*
        assertTrue(PDState.playCard(num).equalsIgnoreCase("Player " + (num + 1) + " just played their " +
                new Card(Rank.THREE, Suit.Club) + " to the center pile.\n"));
*/
        int num2 = PDState.getTurnNum();

        assertNotEquals(num,num2);




    }

    @Test
    public void passAction() throws Exception {

        PDState PDState = new PDState();
        assertTrue(PDState.passAction(PDState.toPlay()).equalsIgnoreCase("You have Control Player " + (PDState.toPlay()+1) + ". You cannot Pass!\n"));



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