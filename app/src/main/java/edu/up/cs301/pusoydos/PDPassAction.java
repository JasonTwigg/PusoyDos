package edu.up.cs301.pusoydos;

import edu.up.cs301.game.GamePlayer;

/**
 * Created by Jason on 4/7/2018.
 */

public class PDPassAction extends SJMoveAction {


    /**
     * Constructor for SJMoveAction
     *
     * @param player the player making the move
     */
    public PDPassAction(GamePlayer player) {
        super(player);
    }

    public boolean isPass(){
        return true;
    }
}
