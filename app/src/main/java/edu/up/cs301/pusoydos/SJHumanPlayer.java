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
import edu.up.cs301.game.infoMsg.GameState;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * A GUI that allows a human to play Slapjack. Moves are made by clicking
 * regions on a surface. Presently, it is laid out for landscape orientation.
 * If the device is held in portrait mode, the cards will be very long and
 * skinny.
 * 
 * @author Steven R. Vegdahl
 *
 * @author Jason Twigg
 * @author Cole Holbrook
 * @author Tawny Motoyama
 * @author Josh Azicate
 *
 * @version July 2013
 */
public class SJHumanPlayer extends GameHumanPlayer implements Animator {

	// sizes and locations of card decks and cards, expressed as percentages
	// of the screen height and width
	private final static float CARD_HEIGHT_PERCENT = 40; // height of a card
	private final static float CARD_WIDTH_PERCENT = 17; // width of a card
	private final static float LEFT_BORDER_PERCENT = 4; // width of left border
	private final static float RIGHT_BORDER_PERCENT = 20; // width of right border
	private final static float VERTICAL_BORDER_PERCENT = 4; // width of top/bottom borders
	
	// our game state
	protected SJState state;

	// our activity
	private Activity myActivity;

	// the amination surface
	private AnimationSurface surface;
	
	// the background color
	private int backgroundColor;

	private RectF[] cardPositions;

	private int cardWidth,cardGap, cardHeight, width, height;
	private float deltaX;


	/**
	 * constructor
	 * 
	 * @param name
	 * 		the player's name
	 * @param bkColor
	 * 		the background color
	 */
	public SJHumanPlayer(String name, int bkColor) {
		super(name);
		backgroundColor = bkColor;
	}

	/**
	 * callback method: we have received a message from the game
	 * 
	 * @param info
	 * 		the message we have received from the game
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		Log.i("SJComputerPlayer", "receiving updated state ("+info.getClass()+")");
		if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
			// if we had an out-of-turn or illegal move, flash the screen
			surface.flash(Color.RED, 50);
		}
		else if (!(info instanceof SJState)) {
			// otherwise, if it's not a game-state message, ignore
			return;
		}
		else {
			// it's a game-state object: update the state. Since we have an animation
			// going, there is no need to explicitly display anything. That will happen
			// at the next animation-tick, which should occur within 1/20 of a second
			this.state = (SJState)info;
			Log.i("human player", "receiving");
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
	 * 		the amimation interval, in milliseconds
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






		/*// draw the middle card-pile
		Card c = state.getDeck(2).peekAtTopCard(); // top card in pile
		if (c != null) {
			// if middle card is not empty, draw a set of N card-backs
			// behind the middle card, so that the user can see the size of
			// the pile
			RectF midTopLocation = middlePileTopCardLocation();
			drawCardBacks(g, midTopLocation,
					0.0025f*width, -0.01f*height, state.getDeck(2).size());
			// draw the top card, face-up
			drawCard(g, midTopLocation, c);
		}
		*/

		/*
		// draw the opponent's cards, face down
		RectF oppTopLocation = opponentTopCardLocation(); // drawing size/location
		drawCardBacks(g, oppTopLocation,
				0.0025f*width, -0.01f*height, state.getDeck(1-this.playerNum).size());

		// draw my cards, face down
		RectF thisTopLocation = thisPlayerTopCardLocation(); // drawing size/location
		drawCardBacks(g, thisTopLocation,
				0.0025f*width, -0.01f*height, state.getDeck(this.playerNum).size());
		
		// draw a red bar to denote which player is to play (flip) a card
		RectF currentPlayerRect =
				state.toPlay() == this.playerNum ? thisTopLocation : oppTopLocation;
		RectF turnIndicator =
				new RectF(currentPlayerRect.left,
						currentPlayerRect.bottom,
						currentPlayerRect.right,
					height);
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		g.drawRect(turnIndicator, paint);
		*/


		Paint paint = new Paint();
		paint.setColor(Color.RED);







		/*
		double percentLeft = .25;
		double percentTop = .8;
		double percentWidth = 0.05;
		double percentHeight = .1;
		int rectLeft = (int)(width*(percentLeft));
		int rectRight = (int)(width*(percentLeft-percentWidth));
		int rectTop = (int)(height*percentTop);
		int rectBottom = (int)(height*(percentTop+percentHeight));
		*/

		Deck deck = state.getDeck(playerNum);

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

		for( int i = 0; i < state.getDeck(playerNum).getCards().size(); i++) {
			Card c = state.getDeck(playerNum).getCards().get(i);
			if (c != null) {
				// if middle card is not empty, draw a set of N card-backs
				// behind the middle card, so that the user can see the size of
				// the pile
				RectF midTopLocation = middlePileTopCardLocation();

				// draw the top card, face-up

				if(deck.getCards().get(i).isSelected()){
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

				//rectLeft = (int)(width*(percentLeft*(i+1)));
				//rectRight = (int)(width*(percentLeft-percentWidth*(i+1)));

			}

		}

		Paint whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);
		whitePaint.setTextSize(150);
		whitePaint.setFakeBoldText(true);
		whitePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
		g.drawText("Pusoy Dos", 50, 150, whitePaint);

		g.drawText(""+state.toPlay(), 100, 250, whitePaint);



        whitePaint.setTextSize(35);

		deltaX = (float) (cardWidth*.1);

		//to draw card backs for other players
		int otherPlayer = 0;
		if( otherPlayer == playerNum ) {
			otherPlayer++;
		}
		drawLeftPlayer(g, otherPlayer, whitePaint);
		otherPlayer++;
		if( otherPlayer == playerNum ) {
			otherPlayer++;
		}
		drawTopPlayer(g, otherPlayer, whitePaint);
		otherPlayer++;
		if( otherPlayer == playerNum ) {
			otherPlayer++;
		}
		drawRightPlayer(g, otherPlayer, whitePaint);

		if( (state.getDeck(4) != null) && (state.getDeck(4).size()>0)){
			drawCard(g, middlePileTopCardLocation(), state.getDeck(4).getCards().get(0));
		}
		else{
			RectF emptyCenter = (middlePileTopCardLocation());
			drawCardBacks(g, emptyCenter, 0, 0, 1);
		}


		this.drawButton(g, "PASS", 600, 750, 800, 850);
		this.drawButton(g, "PLAY", 1200, 750, 1400, 850);




	}

	public void drawLeftPlayer( Canvas g, int playerNum, Paint textPaint ) {

		//to set LEFT PLAYER card back
		int rectLeftL = (int)(width*.1);
		int rectRightL = rectLeftL+cardWidth;
		int rectTopL = (int)((height*.5)-cardHeight);
		int rectBottomL = rectTopL+cardHeight;

		RectF cardBackL = new RectF(rectLeftL, rectTopL, rectRightL, rectBottomL);
		drawCardBacks(g, cardBackL, deltaX, 0.0f, state.getPileSizes()[playerNum]);
		g.drawText("Cards left: "+state.getPileSizes()[playerNum], (float) (rectLeftL+(cardWidth*.2)), (float) (rectBottomL+(cardHeight*.2)), textPaint);

	}

	public void drawTopPlayer( Canvas g, int playerNum, Paint textPaint ) {

		//to set TOP PLAYER card back
		int rectLeftT = (int)((width*.5)-cardWidth);
		int rectRightT = rectLeftT+cardWidth;
		int rectTopT = (int)(height*.1);
		int rectBottomT = rectTopT+cardHeight;

		RectF cardBackT = new RectF(rectLeftT, rectTopT, rectRightT, rectBottomT);
		drawCardBacks(g, cardBackT, deltaX, 0.0f, state.getPileSizes()[playerNum]);
		g.drawText("Cards left: "+state.getPileSizes()[playerNum], (float) (rectLeftT+(cardWidth*.2)), (float) (rectBottomT+(cardHeight*.2)), textPaint);
	}

	public void drawRightPlayer( Canvas g, int playerNum, Paint textPaint ) {

		//to set RIGHT PLAYER card back
		int rectLeftR = (int) (width*.75);
		int rectRightR = rectLeftR+cardWidth;
		int rectTopR = (int)((height*.5)-cardHeight);
		int rectBottomR = rectTopR+cardHeight;

		RectF cardBackR = new RectF(rectLeftR, rectTopR, rectRightR, rectBottomR);
		drawCardBacks(g, cardBackR, deltaX, 0.0f, state.getPileSizes()[playerNum]);
		g.drawText("Cards left: "+state.getPileSizes()[playerNum], (float) (rectLeftR+(cardWidth*.2)), (float) (rectBottomR+(cardHeight*.2)), textPaint);
	}

	public void drawButton( Canvas g, String title, float left, float top, float right, float bottom) {
		Paint redPaint = new Paint();
		redPaint.setColor(Color.RED);
		Paint blackPaint = new Paint();
		blackPaint.setColor(Color.BLACK);
		blackPaint.setTextSize(80);
		g.drawRect(left, top, right, bottom, redPaint);
		g.drawText(title, 0, title.length(), left+5, top+70, blackPaint);
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
		
		// determine whether the touch occurred on the top-card of either
		// the player's pile or the middle pile
		/*
		RectF myTopCardLoc = thisPlayerTopCardLocation();
		RectF middleTopCardLoc = middlePileTopCardLocation();
		if (myTopCardLoc.contains(x, y)) {
			// it's on my pile: we're playing a card: send action to
			// the game
			game.sendAction(new SJPlayAction(this));
		}
		else if (middleTopCardLoc.contains(x, y)) {
			// it's on the middlel pile: we're slapping a card: send
			// action to the game
			game.sendAction(new SJSlapAction(this));
		}
		*/
		int find = -1;
		Deck myDeck = state.getDeck(playerNum);
		for( int i = 0; i < myDeck.size(); i++){
			if(cardPositions[i].contains(x,y)){
				find = i;
			}
		}

		if( find != -1 ){
			//game.sendAction(new SJSlapAction(this));
			game.sendAction(new PDSelectAction(this, find));
			/*
			if( myDeck.getCards().get(find).isSelected()) {
				myDeck.getCards().get(find).setSelected(false);
			} else {
				myDeck.getCards().get(find).setSelected(true);
			}*/
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
}
