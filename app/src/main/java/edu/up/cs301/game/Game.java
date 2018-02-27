package edu.up.cs301.game;

import edu.up.cs301.game.actionMsg.GameAction;

/**
 * To support remote play, this game framework has two types of Games: local
 * games and remote games that are represented by a proxy. Both types adhere to
 * this common interface.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version July 2013
 * @see LocalGame
 * @see ProxyGame
 */


/*
Game Stuff Lecture


Step 0: Create a Game Project
	- includes a github repository

Step 1: Define your game state
	- OFFICIAL Game State
		a. local game
		b. LIMITED game state (limits amount of info presented to user; i.e. other players cards)
		c. player
		d. game actions

Step 2: Define game actions
	- make these as ATOMIC as possible: separate the actions as much as possible
	- don't do anything that's multi-step (have separate actions)
	- every action must be a class (a new instance/created object every time an action happens)
	- 1. Player presses button
	  2. Listener creates a game action object to describe what the player just told it he wants to do
	  3. Human player object sends the action to the LOCAL game
	  4. LOCAL game:
		a. Verifies the move is legal; if not, sends an error to the human player
		b. Modify the OFFICIAL game state based on this action object
	  5. Send an appropriate copy of OFFICIAL game state to each player
	  6. Human player object updates the GUI to reflect the change
	  7. Human player observes
	*REPEATS*

Step 3: Define the Human Player (GUI)

Step 4: Create a simple AI player

Step 5: Create your LOCAL game

Step 6: Implement MainActivity

Step 7: Android Manifest (XML file that is a part of the project; describes everything you need to know in order to run it)
	- package (package where mainActivity is)
	- change PERMISSIONS (add two permissions that are in slapjack sample code)
	- change application label (not Counter Game)
	- specify where your MainActivity is (in activity section)
	- can figure out how to lock orientation (in activity in manifest


 */




public interface Game {
	
	/**
	 * starts the game
	 * 
	 * @param players
	 * 			the players who are in the game
	 */
	public abstract void start(GamePlayer[] players);

	/**
	 * sends the given action to the Game object.
	 * 
	 * @param action
	 *            the action to send
	 */
	public abstract void sendAction(GameAction action);	
	
}
