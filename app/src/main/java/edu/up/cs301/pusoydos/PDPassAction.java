package edu.up.cs301.pusoydos;

import edu.up.cs301.game.GamePlayer;

/**
 * A PDSelectAction is an action that represents a player selecting a card
 * to play.
 *
 * @author Jason Twigg
 * @author Tawny Motoyama
 * @author Josh Azicate
 * @author Cole Holbrook
 *
 * @version April 2018
 */
public class PDPassAction extends PDMoveAction {

    private static final long serialVersionUID = 3250638793499599047L;

    /**
     * Constructor for PDMoveAction
     *
     * @param player the player making the move
     */
    public PDPassAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return whether this action is a "pass"
     */
    public boolean isPass(){
        return true;
    }
}
