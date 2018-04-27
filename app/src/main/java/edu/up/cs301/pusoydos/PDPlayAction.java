package edu.up.cs301.pusoydos;

import edu.up.cs301.game.GamePlayer;

/**
 * A PDPlayAction is an action that represents playing a card on the "up"
 * pile.
 * 
 * @author Jason Twigg
 * @author Cole Holbrook
 * @author Tawny Motoyama
 * @author Josh Azicate
 *
 * @version 31 July 2002
 */
public class PDPlayAction extends PDMoveAction
{
	private static final long serialVersionUID = 3250439793499599047L;

    boolean[] selections;

	/**
     * Constructor for the SJPlayMoveAction class.
     * 
     * @param player  the player making the move
     */
    public PDPlayAction(GamePlayer player, boolean[] selections)
    {
        // initialize the source with the superclass constructor
        super(player);

        this.selections = selections;

    }

    /**
     * @return
     * 		whether this action is a "play" move
     */
    public boolean isPlay() {
        return true;
    }

    /**
     * @return
     * 		the boolean array of selections
     */
    public boolean[] getSelections() {
        return selections;
    }

    
}
