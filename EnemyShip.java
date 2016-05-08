package rbadia.voidspace.model;

import java.util.Random;

import rbadia.voidspace.main.GameScreen;

/**
 * Represents enemy ship.
 * @author Derick Rodriguez
 *
 */
public class EnemyShip extends Ship {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED = 8;
	private static final int Y_OFFSET = 570; // initial y distance of the ship from the bottom of the screen 
	private Random rand = new Random();
	
	/**
	 * Creates new enemy ship.
	 * @param screen the game screen
	 */
	public EnemyShip(GameScreen screen) {
		super(screen);
		this.setLocation((rand.nextInt(screen.getWidth() - super.getShipWidth())/2),
				screen.getHeight() - super.getShipHeight() - Y_OFFSET);
		
	}
	


}