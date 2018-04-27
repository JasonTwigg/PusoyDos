package edu.up.cs301.pusoydos;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 External Citation
 Date: April 20th, 2018
 Problem: There was a large issue with the select action, where it would crashes and slow response
 times
 Resource:
 Matthew (Computer Science Tutor)
 Solution: He advised us to take away the select action completely and have selections handled only
 by the player and have nothing to do with the state. This fixed a lot of our bugs.
 */


/**
 * A GUI that allows a human to play PusoyDos. Moves are made by clicking
 * regions on a surface. Presently, it is laid out for landscape orientation.
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
public class PDHumanPlayer extends GameHumanPlayer implements Animator {

	// sizes and locations of card decks and cards, expressed as percentages
	// of the screen height and width
	private final static float CARD_HEIGHT_PERCENT = 40; // height of a card
	private final static float CARD_WIDTH_PERCENT = 17; // width of a card
	private final static float LEFT_BORDER_PERCENT = 4; // width of left border
	private final static float RIGHT_BORDER_PERCENT = 20; // width of right border
	private final static float VERTICAL_BORDER_PERCENT = 4; // width of top/bottom borders
	
	// our game state
	protected PDState state;

	// our activity
	private Activity myActivity;

	// the animation surface
	private AnimationSurface surface;
	
	// the background color
	private int backgroundColor;

	//Makes clickable buttons and cards
	private RectF[] cardPositions;
	private RectF passButton;
	private RectF playButton;

	//Card dimensions
	private int cardWidth, cardGap, cardHeight, width, height;
	private float deltaX, deltaY;

    //Counter to see which player needs to be drawn
	private int otherPlayerCounter;

	private boolean[] selections;

	/**
	 * constructor
	 * 
	 * @param name
	 * 		the player's name
	 * @param bkColor
	 * 		the background color
	 */
	public PDHumanPlayer(String name, int bkColor) {
		super(name);
		backgroundColor = bkColor;
		selections = null;
	}

	/**
	 * callback method: we have received a message from the game
	 * 
	 * @param info
	 * 		the message we have received from the game
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		Log.i("PDComputerPlayer", "receiving updated state ("+info.getClass()+")");
		if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
			// if we had an out-of-turn or illegal move, flash the screen
			//surface.flash(Color.RED, 50);
		}
		else if (!(info instanceof PDState)) {
			// otherwise, if it's not a game-state message, ignore
			return;
		}
		else {
			// it's a game-state object: update the state. Since we have an animation
			// going, there is no need to explicitly display anything. That will happen
			// at the next animation-tick, which should occur within 1/20 of a second
			this.state = (PDState)info;
			Log.i("human player", "receiving");
		}

		int size = state.getDeck(playerNum).getCards().size();
		selections = new boolean[size];
		for(int i = 0; i < size; i++){
			selections[i] = false;
		}


	}

	/**
	 * call-back method: called whenever the GUI has changed (e.g., at the beginning
	 * of the game, or when the screen orientation changes).
	 * 
	 * @param activity
	 * 		the current activity
	 */
	public void setAsGui(GameMainActivity activity) {

		// remember the activity
		myActivity = activity;

		// Load the layout resource for the new configuration
		activity.setContentView(R.layout.sj_human_player);

		// link the animator (this object) to the animation surface
		surface = (AnimationSurface) myActivity
				.findViewById(R.id.animation_surface);
		surface.setAnimator(this);
		
		// read in the card images
		Card.initImages(activity);

		// if the state is not null, simulate having just received the state so that
		// any state-related processing is done
		if (state != null) {
			receiveInfo(state);
		}
	}

	/**
	 * @return the top GUI view
	 */
	@Override
	public View getTopView() {
		return myActivity.findViewById(R.id.top_gui_layout);
	}

	/**
	 * @return
	 * 		the animation interval, in milliseconds
	 */
	public int interval() {
		// 1/20 of a second
		return 50;
	}

	/**
	 * @return
	 * 		the background color
	 */
	public int backgroundColor() {
		return backgroundColor;
	}

	/**
	 * @return
	 * 		whether the animation should be paused
	 */
	public boolean doPause() {
		return false;
	}

	/**
	 * @return
	 * 		whether the animation should be terminated
	 */
	public boolean doQuit() {
		return false;
	}

	/**
	 * callback-method: we have gotten an animation "tick"; redraw the screen image:
	 * - the middle deck, with the top card face-up, others face-down
	 * - the two players' decks, with all cards face-down
	 * - a red bar to indicate whose turn it is
	 * 
	 * @param g
	 * 		the canvas on which we are to draw
	 */
	public void tick(Canvas g) {

		//tick for player;
		// ignore if we have not yet received the game state
		if (state == null) return;

		// get the height and width of the animation surface
		height = surface.getHeight();
		width = surface.getWidth();

		Paint paint = new Paint();
		paint.setColor(Color.RED);

		Deck deck = null;
		if( state.getDeck(playerNum) != null ) {
			deck = state.getDeck(playerNum);
		} else {
			return;
		}

		cardPositions = new RectF[deck.size()];

		cardWidth = width/15;
		int cardGap = (int)(width*(.4/deck.size()));
		cardHeight = height/6;

		int rectLeft = (int)(width*.3);
		int rectRight = rectLeft + cardWidth;
		int rectTop = (int)(height*.7);
		int rectTopNonSelected = (int)(height*.8);
		int rectTopSelected = (int)(height*.75);
		int rectBottom = rectTop + cardHeight;

		//Draws each card in the decks
		for( int i = 0; i < state.getDeck(playerNum).getCards().size(); i++) {

			Card c = state.getDeck(playerNum).getCards().get(i);

			if (c != null) {
				// if middle card is not empty, draw a set of N card-backs
				// behind the middle card, so that the user can see the size of
				// the pile
				RectF midTopLocation = middlePileTopCardLocation();

				// draw the top card, face-up
				//if(deck.getCards().get(i).isSelected()){
				if( selections[i]){
					rectTop = rectTopSelected;
					rectBottom = rectTop + cardHeight;
				} else {
					rectTop = rectTopNonSelected;
					rectBottom = rectTop + cardHeight;
				}

				cardPositions[i] = new RectF(rectLeft, rectTop, rectRight, rectBottom);

				drawCard(g, cardPositions[i], c);
				rectLeft+=cardGap;
				rectRight+=cardGap;
			}
		}

		//to draw the title and current player text to screen
		drawMainText(g);

		//Paint for player name labels
		Paint whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);

		//Sets the size for the Player labels
        whitePaint.setTextSize(35);

		deltaX = (float) (cardWidth*.1);

		//to draw card backs for other players
		otherPlayerCounter = playerNum + 1;

		drawPlayer(g,(int)(width*.1),(int)((height*.5)-cardHeight),whitePaint);

		drawPlayer(g,(int)((width*.5)-cardWidth),(int)(height*.1),whitePaint);

		drawPlayer(g,(int) (width*.75),(int)((height*.5)-cardHeight),whitePaint);

		drawMiddlePile(g, deck);

		drawPassButton(g);
		drawPlayButton(g);

	}

	/**
	 * helper draw method: draws the title "Pusoy Dos," current player label to screen, and the
	 * 		current mode label
	 *
	 * @param g
	 * 		the canvas on which we are to draw
	 */
	public void drawMainText( Canvas g ) {

		//Paint for text on the screen (title and player's turn)
		Paint psPaint = new Paint();
		psPaint.setColor(Color.argb(80, 255, 255, 255));
		psPaint.setTextSize(150);
		//to set text to bolded and italic style
		psPaint.setFakeBoldText(true);
		psPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
		//Draws the title and whose turn it is
		int titleX = (int)(width*.35);
		int titleY = (int)(height*.75);
		g.drawText("Pusoy Dos", titleX, titleY, psPaint);

		//to get the current players name (as entered in start up screen)
		String playerName = this.allPlayerNames[state.toPlay()];

		//sets x and y location of player's turn text
		int turnLableX = (int) (width*.03);
		int turnLableY = (int) (height*.09);

		//sets x location for the mode label
		int modeLabelX = turnLableX+(int)(width*.65);

		//draws the turn label text onto the upper left corner of screen
		psPaint.setTextSize(80); //sets turn label text size
		g.drawText("It is "+playerName+"'s turn!", turnLableX, turnLableY, psPaint);

		//draws the mode label text onto the upper right corner of screen
		psPaint.setTextSize(60); //sets the mode label size (smaller)
		String mode = createModeString();
		g.drawText("Current Mode: ", modeLabelX, turnLableY, psPaint);
		g.drawText(mode, modeLabelX+150, turnLableY+100, psPaint);

		/**
		 External Citation
		 Date: April 1, 2018
		 Problem: Wanted text (from drawText) to be bold and italic style
		 Resource:
		 https://stackoverflow.com/questions/17878770/how-do-draw-italic-text-on-android-canvas
		 Solution: Used two lines of code to create the desired effect for the text.
		 */



	}


	/**
	 * helper GUI method: to create the mode string to be drawn onto the screen
	 *
	 * @return String
	 * 		the current mode of the game
	 */
	public String createModeString(){
		int currMode = state.getModeType();
		String modeLabel;

		if( currMode==0 ){
			modeLabel = "CONTROL";
		}
		else if( currMode==1 ){
			modeLabel = "SINGLES";
		}
		else if( currMode==2 ){
			modeLabel = "DOUBLES";
		}
		else if( currMode==3 ){
			modeLabel = "STRAIGHT";
		}
		else if( currMode==4 ){
			modeLabel = "FLUSH";
		}
		else if( currMode==5 ){
			modeLabel = "FULL HOUSE";
		}
		else if( currMode==6 ){
			modeLabel = "FOUR OF A KIND";
		}
		else{
			return "";
		}
		return modeLabel;

	}

    /**
     * drawMiddlePile method: This method draws the middle pile with the
     * most recent cards on top. It shows a maximum of five cards for
     * the sake of space. (Also draws the pass and play buttons.)
     *
     * @param g
     * 		the canvas on which we are to draw
     * @param deck
     *      the deck is the pile of cards in the center
     */
	public void drawMiddlePile( Canvas g, Deck deck ){

		//if there are cards in the middle pile, carry out the following actions
		if( (state.getDeck(4).getCards().size() != 0)){

			deltaX = (float) (cardWidth*.2); //to "layer" cards in middle
			int size = state.getDeck(4).getCards().size(); //current size of the middle pile

			int bottom = 0; //to keep track of the bottom card shown in middle
			int maxSize = 5; //to limit the amount of cards in the middle pile

			//set the bottom when the cards in the middle exceeds 5
			if( size >= maxSize ){
				bottom = size - maxSize;
			}

			// -loops through the cards in the middle pile
			// -shifts them appropriately so the rank and suit of each card can be seen
			// -draws each card (overlapping) with the last played card on top
			for( int i=size-bottom; i>0; i--) {

				//set location for the top card
				RectF topRect = middlePileTopCardLocation();

				//shift each card over (on the x-axis) to create "overlapping" effect
				float left = topRect.left + i*deltaX;
				//set the top corn value of each card in middle pile
				float top = topRect.top;

				// draw a card-back (hence null) into the appropriate rectangle
				drawCard(g,
						new RectF(left, top, left + topRect.width(), top + topRect.height()),
						state.getDeck(4).getCards().get(state.getDeck(4).getCards().size()-i));
			}

		}
		//if there are no cards in the middle pile draw the back of a card to the center
		else{
			RectF emptyCenter = (middlePileTopCardLocation());
			drawCardBacks(g, emptyCenter, 0, 0, 1);
		}
		//Draws the Pass and Play Buttons
		drawPassButton(g);
		drawPlayButton(g);
	}

    /**
     * drawPlayer method: This method draws the player's hand, back of
     * the card facing up. It also displays how many cards are left in
     * the user's hand.
     *
     * @param g
     * 		the canvas on which we are to draw
     * @param rectLeft
     *      the left x coordinate to be used
     * @param rectTop
     *      the top y coordinate to be used
     * @param textPaint
     *      the textPaint to be used to draw the Player name
     */
	public void drawPlayer( Canvas g, int rectLeft, int rectTop, Paint textPaint){

        //Moves on to the next player's deck to draw

		if( otherPlayerCounter == 4 ) {
			otherPlayerCounter = 0;
		}

		//to set the PLAYER's card back
		String playerName = this.allPlayerNames[otherPlayerCounter];
		int rectRight = rectLeft+cardWidth;
		int rectBottom = rectTop+cardHeight;

        //Draws the outline around the decks
		textPaint.setColor(Color.argb(80, 255, 255, 255));
		textPaint.setTextSize(50);

		if( state.getPlayerLastPlayed() == otherPlayerCounter ) {
			RectF outLine = new RectF((int) (rectLeft - cardWidth * .1), (int) (rectTop - cardHeight * .1),
					(int) (rectRight + cardWidth * (.2 + state.getPileSizes()[otherPlayerCounter]) * .1), (int) (rectBottom + cardHeight * (.1)));
			g.drawRoundRect(outLine, 10f, 10f, textPaint);
		}

        //Draws the card backs
		RectF cardBack = new RectF(rectLeft, rectTop, rectRight, rectBottom);
		textPaint.setFakeBoldText(false);
		textPaint.setUnderlineText(false);
		textPaint.setTextSize(30);
		textPaint.setColor(Color.WHITE);
		drawCardBacks(g, cardBack, deltaX, 0.0f, state.getPileSizes()[otherPlayerCounter]);
		//Shows the amount of cards left in each player's hand
        g.drawText("Cards left: "+state.getPileSizes()[otherPlayerCounter], (float) (rectLeft+(cardWidth*.3)), (float) (rectBottom+(cardHeight*.3)), textPaint);

        //Changes depending on if it is their turn
		if( state.toPlay() == otherPlayerCounter ){
			textPaint.setColor(Color.YELLOW);
			textPaint.setFakeBoldText(true);
			textPaint.setUnderlineText(true);
		}

		//Draws the player name near their deck
		g.drawText(playerName, (float) (rectLeft+(cardWidth*.1)), (float) (rectTop-(cardHeight*.2)), textPaint);

		otherPlayerCounter++;
	}

	/**
	 External Citation
	 Date: March 3, 2018
	 Problem: Wanted a rectangle with rounded edges
	 Resource:
	 https://developer.android.com/reference/android/graphics/
	 	drawable/shapes/RoundRectShape.html
	 Solution: This code told us how to draw a roundRect, which we use for both
	 of the buttons (Pass and Play)
	 */

    /**
     * drawPassButton method: This method draws the pass button on the
     * bottom left corner of the play area.
     *
     * @param g
     * 		the canvas on which we are to draw
     */
	public void drawPassButton(Canvas g) {
		//Paint for the button
		Paint RedPaint = new Paint();
		RedPaint.setColor(Color.RED);
		//Outline of the button
		Paint outline = new Paint();
		outline.setStyle(Paint.Style.STROKE);
		outline.setColor(Color.BLACK);
		outline.setStrokeWidth(6);
		//Paint for the Text "PASS"
		Paint WhitePaint = new Paint();
		WhitePaint.setColor(Color.WHITE);
		WhitePaint.setTextSize(75);
		WhitePaint.setFakeBoldText(true);
		WhitePaint.setTypeface(Typeface.create("Arial",Typeface.ITALIC));

		//to set position of Pass Button
		int rectLeftP = (int) (width*.075);
		int rectRightP = rectLeftP +250;
		int rectTopP = (int)((height*.8));
		int rectBottomP = rectTopP+130;

        //Draws the button
		passButton = new RectF(rectLeftP, rectTopP, rectRightP, rectBottomP);
		g.drawRoundRect(new RectF(rectLeftP, rectTopP, rectRightP, rectBottomP), 10,10, RedPaint);
		g.drawRoundRect(new RectF(rectLeftP, rectTopP, rectRightP, rectBottomP), 10,10, outline);
		g.drawText("PASS",rectLeftP+30, rectTopP+95, WhitePaint);
	}

    /**
     * drawPlayButton method: This method draws the play button on the
     * bottom right corner of the play area.
     *
     * @param g
     * 		the canvas on which we are to draw
     */
	public void drawPlayButton(Canvas g) {

		//Paint for the button
		Paint RedPaint = new Paint();
		RedPaint.setColor(Color.RED);
		//Outline of the button
		Paint outline = new Paint();
		outline.setStyle(Paint.Style.STROKE);
		outline.setColor(Color.BLACK);
		outline.setStrokeWidth(6);
		//Paint for the text "PLAY"
		Paint WhitePaint = new Paint();
		WhitePaint.setColor(Color.WHITE);
		WhitePaint.setTextSize(75);
		WhitePaint.setFakeBoldText(true);
		WhitePaint.setTypeface(Typeface.create("Arial",Typeface.ITALIC));

		//to set position of Play Button
		int rectLeftP = (int) (width*.80);
		int rectRightP = rectLeftP +250;
		int rectTopP = (int)((height*.8));
		int rectBottomP = rectTopP+130;

        //Draws the button
		playButton = new RectF(rectLeftP, rectTopP, rectRightP, rectBottomP);
		g.drawRoundRect(new RectF(rectLeftP, rectTopP, rectRightP, rectBottomP), 10,10, RedPaint);
		g.drawRoundRect(new RectF(rectLeftP, rectTopP, rectRightP, rectBottomP), 10,10, outline);
		g.drawText("PLAY",rectLeftP+25, rectTopP+95, WhitePaint);

	}

	/**
	 * @return
	 * 		the rectangle that represents the location on the drawing
	 * 		surface where the top card in the opponent's deck is to
	 * 		be drawn
	 */
	private RectF opponentTopCardLocation() {
		// near the left-bottom of the drawing surface, based on the height
		// and width, and the percentages defined above
		int width = surface.getWidth();
		int height = surface.getHeight();
		return new RectF(LEFT_BORDER_PERCENT*width/100f,
				(100-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f,
				(LEFT_BORDER_PERCENT+CARD_WIDTH_PERCENT)*width/100f,
				(100-VERTICAL_BORDER_PERCENT)*height/100f);
	}

	/**
	 * @return
	 * 		the rectangle that represents the location on the drawing
	 * 		surface where the top card in the current player's deck is to
	 * 		be drawn
	 */	
	private RectF thisPlayerTopCardLocation() {
		// near the right-bottom of the drawing surface, based on the height
		// and width, and the percentages defined above
		int width = surface.getWidth();
		int height = surface.getHeight();
		return new RectF((100-RIGHT_BORDER_PERCENT-CARD_WIDTH_PERCENT)*width/100f,
				(100-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f,
				(100-RIGHT_BORDER_PERCENT)*width/100f,
				(100-VERTICAL_BORDER_PERCENT)*height/100f);
	}
	
	/**
	 * @return
	 * 		the rectangle that represents the location on the drawing
	 * 		surface where the top card in the middle pile is to
	 * 		be drawn
	 */	
	private RectF middlePileTopCardLocation() {
		// near the middle-bottom of the drawing surface, based on the height
		// and width, and the percentages defined above
		float rectLeft = (float) ((width*.5)-(cardWidth*.5));
		float rectRight = rectLeft+cardWidth;
		float rectTop = (float) ((height*.5)-(cardHeight*.5));
		float rectBottom = rectTop+cardHeight;
		return new RectF(rectLeft, rectTop, rectRight, rectBottom);
	}
		
	/**
	 * draws a sequence of card-backs, each offset a bit from the previous one, so that all can be
	 * seen to some extent
	 * 
	 * @param g
	 * 		the canvas to draw on
	 * @param topRect
	 * 		the rectangle that defines the location of the top card (and the size of all
	 * 		the cards
	 * @param deltaX
	 * 		the horizontal change between the drawing position of two consecutive cards
	 * @param deltaY
	 * 		the vertical change between the drawing position of two consecutive cards
	 * @param numCards
	 * 		the number of card-backs to draw
	 */
	private void drawCardBacks(Canvas g, RectF topRect, float deltaX, float deltaY,
			int numCards) {
		// loop through from back to front, drawing a card-back in each location
		for (int i = numCards-1; i >= 0; i--) {
			// determine theh position of this card's top/left corner
			float left = topRect.left + i*deltaX;
			float top = topRect.top + i*deltaY;
			// draw a card-back (hence null) into the appropriate rectangle
			drawCard(g,
					new RectF(left, top, left + topRect.width(), top + topRect.height()),
					null);
		}
	}

	/**
	 * callback method: we have received a touch on the animation surface
	 * 
	 * @param event
	 * 		the motion-event
	 */
	public void onTouch(MotionEvent event) {
		
		// ignore everything except down-touch events
		if (event.getAction() != MotionEvent.ACTION_DOWN) return;

		// get the location of the touch on the surface
		int x = (int) event.getX();
		int y = (int) event.getY();

		int find = -1;
		//Gets the deck of the current player
		Deck myDeck = state.getDeck(playerNum);
		synchronized (cardPositions ) {
			for (int i = 0; i < myDeck.size(); i++) {
				if (cardPositions[i] != null) {
					if (cardPositions[i].contains(x, y)) {
						find = i;
					}
				}
			}
		}

		if( find != -1 ){
			//toggles the boolean in the select array with the same index as the cards.
			selections[find] = !selections[find];

		} else if( passButton.contains(x,y)){

			//if the player presses the pass button
			game.sendAction(new PDPassAction(this));
		} else if( playButton.contains(x,y)){

			//if the player presses the play button
			game.sendAction(new PDPlayAction(this,selections));
		}
		else {
			// illegal touch-location: flash for 1/20 second
			surface.flash(Color.RED, 50);
		}
	}
	
	/**
	 * draws a card on the canvas; if the card is null, draw a card-back
	 * 
	 * @param g
	 * 		the canvas object
	 * @param rect
	 * 		a rectangle defining the location to draw the card
	 * @param c
	 * 		the card to draw; if null, a card-back is drawn
	 */
	private static void drawCard(Canvas g, RectF rect, Card c) {
		if (c == null) {
			// null: draw a card-back, consisting of a blue card
			// with a white line near the border. We implement this
			// by drawing 3 concentric rectangles:
			// - blue, full-size
			// - white, slightly smaller
			// - blue, even slightly smaller
			Paint white = new Paint();
			white.setColor(Color.WHITE);
			Paint blue = new Paint();
			blue.setColor(Color.BLUE);
			RectF inner1 = scaledBy(rect, 0.96f); // scaled by 96%
			RectF inner2 = scaledBy(rect, 0.98f); // scaled by 98%
			g.drawRect(rect, blue); // outer rectangle: blue
			g.drawRect(inner2, white); // middle rectangle: white
			g.drawRect(inner1, blue); // inner rectangle: blue
		}
		else {
			// just draw the card
			c.drawOn(g, rect);
		}
	}
	
	/**
	 * scales a rectangle, moving all edges with respect to its center
	 * 
	 * @param rect
	 * 		the original rectangle
	 * @param factor
	 * 		the scaling factor
	 * @return
	 * 		the scaled rectangle
	 */
	private static RectF scaledBy(RectF rect, float factor) {
		// compute the edge locations of the original rectangle, but with
		// the middle of the rectangle moved to the origin
		float midX = (rect.left+rect.right)/2;
		float midY = (rect.top+rect.bottom)/2;
		float left = rect.left-midX;
		float right = rect.right-midX;
		float top = rect.top-midY;
		float bottom = rect.bottom-midY;
		
		// scale each side; move back so that center is in original location
		left = left*factor + midX;
		right = right*factor + midX;
		top = top*factor + midY;
		bottom = bottom*factor + midY;
		
		// create/return the new rectangle
		return new RectF(left, top, right, bottom);
	}

	/*
    * getSelections
    *
    *  Returns the boolean array of selected cards
    */
	public boolean[] getSelections(){
		return selections;
	}
}
