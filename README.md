package rbadia.voidspace.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.EnemyShip;
import rbadia.voidspace.model.Ship;
import rbadia.voidspace.sounds.SoundManager;


/**
 * Handles general game logic and status.
 */
public class GameLogic {
	protected GameScreen gameScreen;
	protected GameStatus status;
	private SoundManager soundMan;
	public final int NumOfAsteroids = 5;

	private Ship ship;
	private Asteroid asteroid;
	protected List<Bullet> bullets;
	private Asteroid[] asteroids = new Asteroid[NumOfAsteroids];

	
	private EnemyShip enemyShip;
	private List<Bullet> enemyShipBullets;
	/**
	 * Create a new game logic handler
	 * @param gameScreen the game screen
	 */
	public GameLogic(GameScreen gameScreen){
		this.gameScreen = gameScreen;
		
		// initialize game status information
		status = new GameStatus();
		// initialize the sound manager
		soundMan = new SoundManager();
		
		// init some variables
		bullets = new ArrayList<Bullet>();
		enemyShipBullets = new ArrayList<Bullet>();
	}

	/**
	 * Returns the game status
	 * @return the game status 
	 */
	public GameStatus getStatus() {
		return status;
	}

	public SoundManager getSoundMan() {
		return soundMan;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	/**
	 * Prepare for a new game.
	 */
	public void newGame(){  						
		status.setGameStarting(true);
		
		// init game variables
		bullets = new ArrayList<Bullet>();
		enemyShipBullets= new ArrayList<Bullet>();
		
		status.setShipsLeft(3);
		status.setGameOver(false);
		status.setAsteroidsDestroyed(0);
		status.setNewAsteroid(false,0);
		for (int i = 0; i < NumOfAsteroids; i++) {
			status.setNewAsteroid(false, i);
		}
				
		// init the ship and the asteroid
        newShip(gameScreen);
        newAsteroid(gameScreen);
        newEnemyShip(gameScreen);
        
        for (int i = 0; i < NumOfAsteroids; i++)
			newAsteroid(gameScreen, i);

	
		
        // prepare game screen
        gameScreen.doNewGame();
        
        // delay to display "Get Ready" message for 1.5 seconds
		Timer timer = new Timer(1500, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameStarting(false);
				status.setGameStarted(true);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	
	
	/**
	 * Check game or level ending conditions.
	 */
	public void checkConditions(){
		// check game over conditions
		if(!status.isGameOver() && status.isGameStarted()){
			if(status.getShipsLeft() == 0){
				gameOver();
			}
		}
	}
	
	/**
	 * Actions to take when the game is over.
	 */
	public void gameOver(){
		status.setGameStarted(false);
		status.setGameOver(true);
		gameScreen.doGameOver();
		
        // delay to display "Game Over" message for 3 seconds
		Timer timer = new Timer(3000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameOver(false);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	 

	/**
	 * Fire a bullet from ship.
	 */
	public void fireBullet(){
		Bullet bullet = new Bullet(ship);
		bullets.add(bullet);
		soundMan.playBulletSound();
	}
	
	public void fireEnemyBullet(){
		Bullet bullet = new Bullet(enemyShip);
		enemyShipBullets.add(bullet);
		soundMan.playBulletSound();
	}
	
	/**
	 * Move a bullet once fired.
	 * @param bullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBullet(Bullet bullet){
		if(bullet.getY() - bullet.getSpeed() >= 0){
			bullet.translate(0, -bullet.getSpeed());
			return false;
		}
		else{
			return true;
		}
	}
	public boolean moveEnemyBullet(Bullet bullet){
		if(bullet.getY() - bullet.getSpeed() <= gameScreen.getHeight()){
			bullet.translate(0, bullet.getSpeed());
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * Create a new ship (and replace current one).
	 */
	public Ship newShip(GameScreen screen){
		this.ship = new Ship(screen);
		return ship;
	}
	
	/**
	 * Creates a new enemy ship.
	 */
	public EnemyShip newEnemyShip(GameScreen screen) {
		this.enemyShip = new EnemyShip(screen);
		return enemyShip;
	}
	
	/**
	 * Create a new asteroid.
	 */
	public Asteroid newAsteroid(GameScreen screen){
		this.asteroid = new Asteroid(screen);
		return asteroid;
	}
	
	public Asteroid newAsteroid(GameScreen screen, int i){
		asteroids[i] = new Asteroid(screen);
		return asteroids[i];
	}
	
	/**
	 * Returns the ship.
	 * @return the ship
	 */
	public Ship getShip() {
		return ship;
	}
	
	/**
	 * Returns the enemy ship.
	 * @return enemy ship
	 */
	public EnemyShip getEnemyShip() {
		return enemyShip;
	}

	/**
	 * Returns the asteroid.
	 * @return the asteroid
	 */
	public Asteroid[] getAsteroids() {
		return asteroids;
	}
	public Asteroid getAsteroid() {
		return asteroid;
	}

	/**
	 * Returns the list of bullets.
	 * @return the list of bullets
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}
	public List<Bullet> getEnemyBullets() {
		return enemyShipBullets;
	}
}
