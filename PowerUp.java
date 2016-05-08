package rbadia.voidspace.model;

import java.awt.Rectangle;
import java.util.Random;

import rbadia.voidspace.main.GameScreen;

public class PowerUp extends Rectangle {

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED = 5;
	
	private int powerupWidth = 32;
	private int powerupHeight = 32;
	private int speed = DEFAULT_SPEED;
	
	private Random rand = new Random();

	
	public PowerUp(GameScreen screen) {
		this.setLocation(
        		rand.nextInt(screen.getWidth() - powerupWidth),
        		0);
		this.setSize(powerupWidth, powerupHeight);
	}
	
	public int getPowerUpWidth() {
		return powerupWidth;
	}
	
	public int getPowerUpHeight() {
		return powerupHeight;
	}

	/**
	 * Returns the current speed of the powerup.
	 * @return current speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Sets the current speed of the powerup.
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Returns the default speed of the powerup.
	 * @return
	 */
	public int getDefaultSpeed() {
		return DEFAULT_SPEED;
	}
	
	
}