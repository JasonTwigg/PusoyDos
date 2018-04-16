package edu.up.cs301.pusoydos;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A game-move object that a PusoyDos player sends to the game to make
 * a move.
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
public abstract class PDMoveAction extends GameAction {
	
	private static final long serialVersionUID = -3107100271012188849L;


    /**
     * Constructor for PDMoveAction
     *
     * @param player the player making the move
     */
    public PDMoveAction(GamePlayer player)
    {
        // invoke superclass constructor to set source
        super(player);

    }
    
    /**
     * @return
     * 		whether the move was a slap
     */
    public boolean isSelect() {
    	return false;
    }
    
    /**
     * @return
     * 		whether the move was a "play"
     */
    public boolean isPlay() {
        return false;
    }

    /**
     * @return
     * 		whether the move was a "pass"
     */
    public boolean isPass() {
        return false;
    }




}
