package menus;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.EmptyImageData;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import general.AppGame;
import general.Player;

import menus.PlayersMenu;
import menus.ui.Page;

public class WelcomeMenu extends Page {

	private int ID;

	private Image logo;

	private boolean logoVisibility;

	protected int logoBoxWidth;
	protected int logoBoxHeight;
	protected int logoBoxX;
	protected int logoBoxY;

	private int logoWidth;
	private int logoHeight;
	private int logoX;
	private int logoY;

	private int logoNaturalWidth;
	private int logoNaturalHeight;

	public WelcomeMenu (int ID) {
		this.ID = ID;
	}

	@Override
	public int getID () {
		return this.ID;
	}

	@Override
	public void init (GameContainer container, StateBasedGame game) {
		super.initSize (container, game, 600, 400);
		super.init (container, game);

		this.hintBoxX = this.contentX;
		this.hintBoxY = this.contentY;

		this.logoBoxX = this.contentX;
		this.logoBoxY = this.hintBoxY + this.hintBoxHeight + Page.gap;
		this.logoBoxWidth = this.contentWidth;
		this.logoBoxHeight = this.contentY + this.contentHeight - this.logoBoxY;

		this.logoVisibility = true;

		this.titleVisibility = false;
		this.subtitleVisibility = false;
		this.hintBlink = true;

		this.setHint ("PRESS ENTER");
		Image logo;
		try {
			logo = new Image ("images/logo.png");
		} catch (SlickException exception) {
			logo = new Image (new EmptyImageData (0, 0));
		};
		this.setLogo (logo);
	}

	@Override
	public void update (GameContainer container, StateBasedGame game, int  delta) {
		super.update (container, game, delta);
		Input input = container.getInput ();
		if (input.isKeyPressed (Input.KEY_ESCAPE)) {
			System.exit (0);
		} else {
			int gameMasterID = Input.ANY_CONTROLLER;
			if (input.isKeyPressed (Input.KEY_ENTER)) {
				gameMasterID = 0;
			} else {
				for (int i = 0, l = input.getControllerCount (); i < l; i++) {
					if (input.isButtonPressed (AppGame.BUTTON_A, i)) {
						gameMasterID = i;
						break;
					};
				};
			};
			if (gameMasterID != Input.ANY_CONTROLLER) {
				PlayersMenu playersMenu = (PlayersMenu) game.getState (AppGame.MENUS_PLAYERS_MENU);
				int colorID = playersMenu.availableColorIDs.remove (0);
				String name = "Joueur " + Player.COLOR_NAMES [colorID]; // TODO: set user name
				playersMenu.players.add (0, new Player (colorID, gameMasterID, name));
				playersMenu.playersControls.add (0, 1 << AppGame.BUTTON_A);
				game.enterState (AppGame.MENUS_MAIN_MENU, new FadeOutTransition (), new FadeInTransition ());
			};
		};
	}

	@Override
	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		super.render (container, game, context);
		this.renderLogo (container, game, context);
	}

	private void renderLogo (GameContainer container, StateBasedGame game, Graphics context) {
		if (this.logoVisibility) {
			context.drawImage (
				this.logo,
				this.logoX,
				this.logoY,
				this.logoX + this.logoWidth,
				this.logoY + this.logoHeight,
				0,
				0,
				this.logoNaturalWidth,
				this.logoNaturalHeight
			);
		};
	}

	public void setLogo (Image logo) {
		this.logo = logo.copy ();
		this.logoNaturalWidth = logo.getWidth ();
		this.logoNaturalHeight = logo.getHeight ();
		this.logoWidth = Math.min (this.logoBoxWidth, this.logoNaturalWidth);
		this.logoHeight = Math.min (this.logoBoxHeight, this.logoNaturalHeight);
		if (this.logoWidth * this.logoNaturalHeight < this.logoNaturalWidth * this.logoHeight) {
			this.logoHeight = this.logoNaturalHeight * this.logoWidth / this.logoNaturalWidth;
		} else {
			this.logoWidth = this.logoNaturalWidth * this.logoHeight / this.logoNaturalHeight;
		};
		this.logoX = this.logoBoxX + (this.logoBoxWidth - this.logoWidth) / 2;
		this.logoY = this.logoBoxY + (this.logoBoxHeight - this.logoHeight) / 2;
	}

	public Image getLogo () {
		return logo.copy ();
	}

}
