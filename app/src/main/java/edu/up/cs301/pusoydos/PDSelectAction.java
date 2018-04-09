package edu.up.cs301.pusoydos;

import edu.up.cs301.game.GamePlayer;

/**
 * Created by Jason on 4/7/2018.
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

    public int getIndex(){
        return index;
    }
}
