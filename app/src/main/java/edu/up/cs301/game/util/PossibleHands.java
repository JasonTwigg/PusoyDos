package edu.up.cs301.game.util;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

/**
 * Created by Jason on 4/7/2018.
 */

public class PossibleHands {

    private ArrayList<Card> cards;

    public PossibleHands(ArrayList<Card> cards){
        cards = new ArrayList<Card>();
        for( int i = 0; i < cards.size(); i++ ){
            cards.add(cards.get(i));
        }
    }

}
