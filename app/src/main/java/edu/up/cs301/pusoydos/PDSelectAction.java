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
public class PDSelectAction extends SJMoveAction {

    private static final long serialVersionUID = 3250638793499599047L;

    private int index;

    public PDSelectAction(GamePlayer player, int index) {
        // initialize the source with the superclass constructor
        super(player);
        this.index = index;
    }

    /**
     * @return whether this action is a "select" move
     */
    public boolean isSelect() {
        return true;
    }

    /**
     * @return the index
     */
    public int getIndex(){
        return index;
    }
}
