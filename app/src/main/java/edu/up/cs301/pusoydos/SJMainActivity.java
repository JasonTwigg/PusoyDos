package edu.up.cs301.pusoydos;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;
import android.graphics.Color;

/**
 * this is the primary activity for pusoyDos game
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
public class SJMainActivity extends GameMainActivity {
	
	public static final int PORT_NUMBER = 47520;

	/** a pusoydos game for four players. The default is human vs. computer */
	@Override
	public GameConfig createDefaultConfig() {

		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

		final int purpleBackground = Color.rgb(127, 55, 155);
		playerTypes.add(new GamePlayerType("human player (purple )") {
			public GamePlayer createPlayer(String name) {
				return new SJHumanPlayer(name, purpleBackground);
			}});
		playerTypes.add(new GamePlayerType("human player (black)") {
			public GamePlayer createPlayer(String name) {
				return new SJHumanPlayer(name, Color.BLACK);
			}
		});
		playerTypes.add(new GamePlayerType("computer player (dumb)") {
			public GamePlayer createPlayer(String name) {
				return new SJComputerPlayer(name);
			}
		});
		playerTypes.add(new GamePlayerType("computer player (smart)") {
			public GamePlayer createPlayer(String name) {
				return new SJComputerPlayerSmart(name);
			}
		});


		// Create a game configuration class for PusoyJack
		GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4, "PusoyDos", PORT_NUMBER);

		// Add the default players
		defaultConfig.addPlayer("Human", 0);
		defaultConfig.addPlayer("Computer", 2);
		defaultConfig.addPlayer("Computer", 2);
		defaultConfig.addPlayer("Computer", 2);
		
		// Set the initial information for the remote player
		defaultConfig.setRemoteData("Guest", "", 1);
		
		//done!
		return defaultConfig;
	}//createDefaultConfig

	@Override
	public LocalGame createLocalGame() {
		return new SJLocalGame();
	}
}
