package edu.up.cs301.pusoydos;

import org.junit.Test;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;

import static org.junit.Assert.*;

/**
 * Created by Jason on 3/22/2018.
 */
public class SJStateTest {
    @Test
    public void getDeck() throws Exception {

    }

    @Test
    public void toPlay() throws Exception {

        SJState sjState = new SJState();

        int num = sjState.toPlay();
        assertTrue(sjState.getDeck(num).getCards().get(12).getPower() == 0);

    }

    @Test
    public void setToPlay() throws Exception {

    }

    @Test
    public void nullAllButTopOf2() throws Exception {

    }

    @Test
    public void selectCard() throws Exception {

        SJState sjState = new SJState();
        int num = sjState.toPlay();

        sjState.selectCard(num,0);
        assertTrue(sjState.getDeck(num).getCards().get(0).isSelected());


    }

    @Test
    public void playCard() throws Exception {

        SJState sjState = new SJState();

        int num = sjState.getTurnNum();

        sjState.getDeck(num).getCards().get(12).setSelected(true);

        assertTrue(sjState.playCard(num).equalsIgnoreCase("Player " + (num + 1) + " just played their " +
                new Card(Rank.THREE, Suit.Club) + " to the center pile.\n"));

        int num2 = sjState.getTurnNum();

        assertNotEquals(num,num2);




    }

    @Test
    public void passAction() throws Exception {

        SJState sjState = new SJState();
        assertTrue(sjState.passAction(sjState.toPlay()).equalsIgnoreCase("You have Control Player " + (sjState.toPlay()+1) + ". You cannot Pass!\n"));



    }

    @Test
    public void changeTurn() throws Exception {

        SJState sjState = new SJState();
        sjState.setTurnNum(3);
        sjState.changeTurn();
        assertTrue(sjState.getTurnNum() == 0);


    }

    @Test
    public void canPlay() throws Exception {

        SJState sjState = new SJState();

        int num = sjState.toPlay();

        sjState.selectCard(num,12);
        sjState.playCard(num);

        sjState.changeTurn();
        num = sjState.toPlay();
        sjState.selectCard(num,0);
        sjState.playCard(num);

        sjState.changeTurn();
        num = sjState.toPlay();
        sjState.selectCard(num,11);
        assertFalse(sjState.canPlay(sjState.getDeck(num).getCards()));

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