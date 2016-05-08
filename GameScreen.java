package rbadia.voidspace.main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.EnemyShip;
import rbadia.voidspace.model.Ship;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Main game screen. Handles all game graphics updates and some of the game logic.
 */
// AUTORES DEL PROYECTO
//Iñaki Renedo Muñoz de la Peña
// Mario A Rodriguez Cruz
public class GameScreen extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage backBuffer;
	private Graphics2D g2d;
	private int shipsleft = 3;

	private static final int NEW_SHIP_DELAY = 500;
	private static final int NEW_ASTEROID_DELAY = 500;
	
	private long lastShipTime;
	private long lastAsteroidTime;
	
	private Asteroid[] asteroids;
	
	private Rectangle asteroidExplosion;
	private Rectangle shipExplosion;
	
	private JLabel shipsValueLabel;
	private JLabel destroyedValueLabel;
	private JLabel pointsValueLabel;
	private JLabel levelValueLabel;

	private Random rand;
	
	private Font originalFont;
	private Font bigFont;
	private Font biggestFont;
	
	private GameStatus status;
	private SoundManager soundMan;
	private GraphicsManager graphicsMan;
	private GameLogic gameLogic;
	private int trajectory;
	

	

	/**
	 * This method initializes 
	 * 
	 */
	public GameScreen() {
		super();
		// initialize random number generator
		rand = new Random();
		
		initialize();
		
		// init graphics manager
		graphicsMan = new GraphicsManager();
		
		// init back buffer image
		backBuffer = new BufferedImage(820, 600, BufferedImage.TYPE_INT_RGB);
		g2d = backBuffer.createGraphics();
	}

	/**
	 * Initialization method (for VE compatibility).
	 */
	private void initialize() {
		// set panel properties
        this.setSize(new Dimension(820, 600));
        this.setPreferredSize(new Dimension(820, 600));
        this.setBackground(Color.BLACK);

	}

	/**
	 * Update the game screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw current backbuffer to the actual game screen
		g.drawImage(backBuffer, 0, 0, this);
	}
	
	/**
	 * Update the game screen's backbuffer image.
	 */
	

	public void updateScreen(){
		Ship ship = gameLogic.getShip();
		Asteroid asteroid = gameLogic.getAsteroid();
		asteroids = gameLogic.getAsteroids();

		List<Bullet> bullets = gameLogic.getBullets();
		EnemyShip enemy = gameLogic.getEnemyShip();
		List<Bullet> enemyShipBullets = gameLogic.getEnemyBullets();
		// set orignal font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}
		
		// erase screen
		g2d.setPaint(Color.darkGray);
		g2d.fillRect(0, 0, getSize().width, getSize().height);

		// draw 50 random stars
		drawStars(50);
		
		// if the game is starting, draw "Get Ready" message
		if(status.isGameStarting()){
			drawGetReady();
			return;
		}
		
		// if the game is over, draw the "Game Over" message
		if(status.isGameOver()){
			// draw the message
			drawGameOver();
			
			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			if((currentTime - lastShipTime) < NEW_SHIP_DELAY){
				graphicsMan.drawShipExplosion(shipExplosion, g2d, this);
			}
			return;
		}
		
		// the game has not started yet
		if(!status.isGameStarted()){
			// draw game title screen
			initialMessage();
			return;
		}
		
		
		if(status.getShipsLeft()<3){
			
			if(status.getAsteroidsDestroyed() % 20==0 && status.getAsteroidsDestroyed()!=0){
				
				drawLiveRecover(); 
				
				shipsValueLabel.setText(Integer.toString(status.getShipsLeft()+1));
				status.setShipsLeft(status.getShipsLeft() + 1);
			}
		}
	

		//levels
switch ((int) status.getAsteroidsDestroyed()) {
		
		case 5:  
			level=2;
			drawNextLevel();
			levelValueLabel.setText("2");
			
			for(int i=0;i<gameLogic.NumOfAsteroids; i++){
				
				asteroids[i].setSpeed(4);
			}
			
			break;
			
		case 10:
			
			level=3;
		
			drawNextLevel();
			levelValueLabel.setText("3");
			for(int i=0;i<gameLogic.NumOfAsteroids; i++){
				
				asteroids[i].setSpeed(6);
			}
			break;
			
		case 15: 
			level=4;
			
			drawNextLevel();
			levelValueLabel.setText("4");
			
			for(int i=0;i<gameLogic.NumOfAsteroids; i++){
				
				asteroids[i].setSpeed(8);
			}
			break;
			
		case 20:
			level=5;
			drawNextLevel();
			levelValueLabel.setText("5");
			
			for(int i=0;i<gameLogic.NumOfAsteroids; i++){
				
				asteroids[i].setSpeed(10);
			}
			
			break;
		case 25: 
			level=6;
			drawNextLevel();
			levelValueLabel.setText("6");
			for(int i=0;i<gameLogic.NumOfAsteroids; i++){
				
				asteroids[i].setSpeed(12);
			}
			break;
		case 30: 
			level=7;
			
			drawNextLevel();
			levelValueLabel.setText("7");
			for(int i=0;i<gameLogic.NumOfAsteroids; i++){
				
				asteroids[i].setSpeed(14);
			}
			break;	
		case 35:
			level=8;
			drawNextLevel();
			levelValueLabel.setText("8");
			for(int i=0;i<gameLogic.NumOfAsteroids; i++){
				
				asteroids[i].setSpeed(16);
			}
			break;
		case 40: 
			level=9;
			drawNextLevel();
			levelValueLabel.setText("9");
			for(int i=0;i<gameLogic.NumOfAsteroids; i++){
				
				asteroids[i].setSpeed(20);
			}
			break;
			
		case 45:
				level=10;
				drawNextLevel();
				levelValueLabel.setText("10");
				for(int i=0;i<gameLogic.NumOfAsteroids; i++){
					
					asteroids[i].setSpeed(25);
				}
				
				break;
		
		
		}
		// draw ship
if(!status.isNewShip()){
	// Draw it in its current location
	graphicsMan.drawShip(ship, g2d, this);
	if(status.getAsteroidsDestroyed() >= 15)
		graphicsMan.drawEnemyShip(enemy, g2d, this);
	long time = System.currentTimeMillis();
	if (time % 100 == 0 && status.getAsteroidsDestroyed() >= 15) {
		graphicsMan.drawEnemyShip(enemy, g2d, this);
		if (!status.isNewEnemyShip())
			gameLogic.fireEnemyBullet();
		if (status.isNewEnemyShip() && time % 500 == 0) {
			enemy.setLocation(rand.nextInt(this.getWidth() - enemy.width), 0);
			graphicsMan.drawEnemyShip(enemy, g2d, this);
			status.setNewEnemyShip(false);
		}
		
	}
		
}
else{
	// draw a new one
	long currentTime = System.currentTimeMillis();
	if((currentTime - lastShipTime) > NEW_SHIP_DELAY){
		lastShipTime = currentTime;
		status.setNewShip(false);
		ship = gameLogic.newShip(this);

		if (status.getAsteroidsDestroyed() >= 15)
		graphicsMan.drawEnemyShip(enemy, g2d, this);
	}
	else{
		// draw explosion
		graphicsMan.drawShipExplosion(shipExplosion, g2d, this);

		if (status.getAsteroidsDestroyed() >= 15)
		graphicsMan.drawEnemyShip(enemy, g2d, this);
	}
}
		// draw bullets
				for(int i=0; i<bullets.size(); i++){
					Bullet bullet = bullets.get(i);
					graphicsMan.drawBullet(bullet, g2d, this);
					
					boolean remove = gameLogic.moveBullet(bullet);
					if(remove){
						bullets.remove(i);
						i--;
					}
				}

				//Draw enemy bullets
				for(int i=0; i<enemyShipBullets.size(); i++){
					Bullet bullet = enemyShipBullets.get(i);
					graphicsMan.drawBullet(bullet, g2d, this);

					boolean remove = gameLogic.moveEnemyBullet(bullet);
					if(remove){
						enemyShipBullets.remove(i);
						i--;
					}
				}
			
		
		// draw asteroid
		
if(status.getAsteroidsDestroyed()<5){		
	// en el primer nivel alterna velocidades lentas( 4 y 5)	
	int speed=5;
		
		if(!status.isNewAsteroid()[0]){
			// draw the asteroid until it reaches the bottom of the screen
			//Switch para elegir las trayectorias random
			
				
			if(trajectory==0){
				if(this.contains((int)asteroids[0].getX(),(int)asteroids[0].getY()) || this.contains((int)asteroids[0].getX()+asteroids[0].width, (int)asteroids[0].getY()+asteroids[0].height)){
					asteroids[0].setSpeed(speed);
					asteroids[0].translate(0, asteroid.getSpeed());
					graphicsMan.drawAsteroid(asteroids[0], g2d, this);
					
				}else{
				trajectory = rand.nextInt(4);

				asteroid.setLocation(rand.nextInt(getWidth()-asteroid.width),0);
				}
			}else if(trajectory==1){
				if(this.contains((int)asteroids[0].getX(),(int)asteroids[0].getY()) || this.contains((int)asteroids[0].getX()+asteroids[0].width, (int)asteroids[0].getY()+asteroids[0].height)){
					asteroids[0].setSpeed(speed);
					asteroids[0].translate(asteroids[0].getSpeed()-5, asteroids[0].getSpeed()+1);
					graphicsMan.drawAsteroid(asteroids[0], g2d, this);
					
				}else{
				trajectory = rand.nextInt(4);

				asteroids[0].setLocation(getWidth()/4 + rand.nextInt(3*getWidth()/4),0);
				}
			}else if(trajectory==2){
				if(this.contains((int)asteroids[0].getX(),(int)asteroids[0].getY()) || this.contains((int)asteroids[0].getX()+asteroids[0].width, (int)asteroids[0].getY()+asteroids[0].height)){
					asteroids[0].setSpeed(speed);
					asteroids[0].translate(asteroids[0].getSpeed()-3,asteroids[0].getSpeed()+1);
					graphicsMan.drawAsteroid(asteroids[0], g2d, this);
				
				}else{
				trajectory = rand.nextInt(4);

				asteroids[0].setLocation(rand.nextInt((3*getWidth()/4)),0);
				}
			}else if(trajectory==3){
				if(this.contains((int)asteroids[0].getX(),(int)asteroids[0].getY()) || this.contains((int)asteroids[0].getX()+asteroids[0].width, (int)asteroids[0].getY()+asteroids[0].height)){
					asteroids[0].setSpeed(speed);
					asteroids[0].translate(asteroids[0].getSpeed()-6, asteroids[0].getSpeed()+1);
					graphicsMan.drawAsteroid(asteroids[0], g2d, this);
					
				}else{
				trajectory = rand.nextInt(4);

				asteroids[0].setLocation((getWidth()/2)-asteroids[0].width+rand.nextInt(getWidth()/2),0);
				}
			}else {
				if(this.contains((int)asteroids[0].getX(),(int)asteroids[0].getY()) || this.contains((int)asteroids[0].getX()+asteroids[0].width, (int)asteroids[0].getY()+asteroids[0].height)){
					asteroids[0].setSpeed(speed);
					asteroid.translate(2,4);//asteroids[0].getSpeed()-2, asteroids[0].getSpeed());
					graphicsMan.drawAsteroid(asteroids[0], g2d, this);
					
				}else{
				trajectory = rand.nextInt(5);

				asteroids[0].setLocation(rand.nextInt((getWidth()/2)+asteroids[0].width),0);
				}
			}

		}
		
		else{
			
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
				// draw a new asteroid
				
				trajectory = rand.nextInt(4);
				
				lastAsteroidTime = currentTime;
				status.setNewAsteroid(false,0);
				
					if(trajectory==0){						
						asteroids[0].setLocation(rand.nextInt(getWidth()-asteroids[0].width),0);
						
					}else if(trajectory==1){
						asteroids[0].setLocation(getWidth()/4 + rand.nextInt(3*getWidth()/4),0);

					}else if(trajectory==2){
						asteroids[0].setLocation(rand.nextInt((3*getWidth()/4)),0);

					}else if(trajectory==3){
						asteroids[0].setLocation((getWidth()/2)-asteroids[0].width+rand.nextInt(getWidth()/2),0);
					}else{
					asteroids[0].setLocation(rand.nextInt((getWidth()/2)+asteroids[0].width),0);
					}
				
				//asteroid.setLocation(rand.nextInt(getWidth() - asteroid.width), 0);
			}
			else{
				// draw explosion
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);

			}
		}
		
		
		
		// check bullet-asteroid collisions
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(asteroids[0].intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 1);
				status.setScore(status.getScore() + 100);

				// "remove" asteroid
		        asteroidExplosion = new Rectangle(
		        		asteroids[0].x,
		        		asteroids[0].y,
		        		asteroids[0].width,
		        		asteroids[0].height);
		        asteroids[0].setLocation(-asteroids[0].width, -asteroids[0].height);
				status.setNewAsteroid(true,0);
				lastAsteroidTime = System.currentTimeMillis();
				
				// play asteroid explosion sound
				soundMan.playAsteroidExplosionSound();
				
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
		
		// check ship-asteroid collisions
		if(asteroids[0].intersects(ship)){
			// decrease number of ships left
			status.setShipsLeft(status.getShipsLeft() - 1);
			status.setScore(status.getScore() - 50);
			
			status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 1);

			// "remove" asteroid
	        asteroidExplosion = new Rectangle(
	        		asteroids[0].x,
	        		asteroids[0].y,
	        		asteroids[0].width,
	        		asteroids[0].height);
	        asteroids[0].setLocation(-asteroids[0].width, -asteroids[0].height);
			status.setNewAsteroid(true,0);
			lastAsteroidTime = System.currentTimeMillis();
			
			// "remove" ship
	        shipExplosion = new Rectangle(
	        		ship.x,
	        		ship.y,
	        		ship.width,
	        		ship.height);
			ship.setLocation(this.getWidth() + ship.width, -ship.height);
			status.setNewShip(true);
			lastShipTime = System.currentTimeMillis();
			
			// play ship explosion sound
			soundMan.playShipExplosionSound();
			// play asteroid explosion sound
			soundMan.playAsteroidExplosionSound();
		}
		
		// update asteroids destroyed label
		destroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));
		
		// update ships left label
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));
		
		// update score label
		pointsValueLabel.setText(Long.toString(status.getScore()));
	}
	
else  {
	

		for (int i = 0; i < gameLogic.NumOfAsteroids; i++) {
			if(!status.isNewAsteroid()[i]) {
				// draw the asteroid until it reaches the bottom of the screen
				if(asteroids[i].getY() + asteroids[i].getSpeed() < this.getHeight()){

					asteroids[i].translate(0, asteroids[i].getSpeed());
					graphicsMan.drawAsteroid(asteroids[i], g2d, this);
				}
				else {
					asteroids[i].setLocation(rand.nextInt(getWidth() - asteroids[i].width),0);
				}
			}

			else{
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
					// draw a new asteroid
					lastAsteroidTime = currentTime;
					status.setNewAsteroid(false, i);
					asteroids[i].setLocation(rand.nextInt(getWidth() - asteroids[i].width), 0);
				}
				else{
					// draw explosion
					graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
				}
			}
		}
		
		
		
		
		//-------------------------ASTEROID- COLLISION---------------------------------------
		// check bullet-asteroid collisions
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			for(int j = 0; j < gameLogic.NumOfAsteroids; j++) {
				if(asteroids[j].intersects(bullet)){
					// increase asteroids destroyed count
				
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 1);
					status.setScore(status.getScore() + 100);
					// "remove" asteroid
					asteroidExplosion = new Rectangle(
							asteroids[j].x,
							asteroids[j].y,
							asteroids[j].width,
							asteroids[j].height);
					asteroids[j].setLocation(-asteroids[j].width, -asteroids[j].height);
					status.setNewAsteroid(true, j);
					lastAsteroidTime = System.currentTimeMillis();

					// play asteroid explosion sound
					soundMan.playAsteroidExplosionSound();

					// remove bullet
					bullets.remove(i);
					break;
				}
			}
		}
// ---------------------------------BLOQUE DE LA NAVE ENEMIGA------------------------------
	
		// Only run the enemy ship-related code if 15 asteroids have been destroyed.
		if(status.getAsteroidsDestroyed() >= 15) {

			//Check player ship bullet-enemy ship collisions
			for(int i = 0; i < bullets.size(); i++){
				Bullet bullet = bullets.get(i);
				if(enemy.intersects(bullet)){
					shipExplosion = new Rectangle(
							enemy.x,
							enemy.y,
							enemy.width,
							enemy.height);
					enemy.setLocation(this.getWidth() + enemy.width, -enemy.height);
					graphicsMan.drawShipExplosion(shipExplosion, g2d, this);
					status.setNewEnemyShip(true);
					status.setScore(status.getScore() + 250);

					// play ship explosion sound
					soundMan.playShipExplosionSound();

					// remove bullet
					bullets.remove(i);
					break;
				}
			}

			//Check for enemy ship bullet collisions with player ship
			for(int i = 0; i < enemyShipBullets.size(); i++){
				Bullet bullet = enemyShipBullets.get(i);
				if(ship.intersects(bullet)){
					// Decrease number of ships left
					if(shipsleft > 1)
						shipsleft -= 1;
					else {
						shipsleft = 3;
						status.setShipsLeft(status.getShipsLeft() - 1);
						status.setScore(status.getScore() -50);

						shipExplosion = new Rectangle(
								ship.x,
								ship.y,
								ship.width,
								ship.height);
						ship.setLocation(this.getWidth() + ship.width, -ship.height);
						status.setNewShip(true);
						lastShipTime = System.currentTimeMillis();

						// play ship explosion sound
						soundMan.playShipExplosionSound();
					}

					// remove bullet
					enemyShipBullets.remove(i);
					break;
				}
			}
			//Checks for ship on ship collisions
			if(enemy.intersects(ship)){
				// decrease number of ships left
				status.setShipsLeft(status.getShipsLeft() - 1);
				// +250 PUNTOS POR MATAR A LA NAVE ENEMIGA, -50 PUNTOS POR PERDER UNA VIDA
				status.setScore(status.getScore() + 200);

				// "remove" ship
				shipExplosion = new Rectangle(
						ship.x,
						ship.y,
						ship.width,
						ship.height);
				ship.setLocation(this.getWidth() + ship.width, -ship.height);
				status.setNewShip(true);
				shipsleft = 3;
				lastShipTime = System.currentTimeMillis();
				graphicsMan.drawShipExplosion(shipExplosion, g2d, this);

				//Destroy enemy ship
				Rectangle enemyShipExplosion = new Rectangle(
						enemy.x,
						enemy.y,
						enemy.width,
						enemy.height);
				enemy.setLocation(this.getWidth() + enemy.width, this.getHeight() + enemy.height);
				graphicsMan.drawShipExplosion(enemyShipExplosion, g2d, this);
				status.setNewEnemyShip(true);

				// play ship explosion sound
				soundMan.playShipExplosionSound();
				// play asteroid explosion sound
				soundMan.playAsteroidExplosionSound();
			}
		}


		// check ship-asteroid collisions
		for (int i = 0; i < gameLogic.NumOfAsteroids; i++) {
			if(asteroids[i].intersects(ship)) {
				// decrease number of ships left
				status.setShipsLeft(status.getShipsLeft() - 1);
				status.setScore(status.getScore() - 50);
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 1);

				// "remove" asteroid
				asteroidExplosion = new Rectangle(
						asteroids[i].x,
						asteroids[i].y,
						asteroids[i].width,
						asteroids[i].height);
				asteroids[i].setLocation(-asteroids[i].width, -asteroids[i].height);
				status.setNewAsteroid(true, i);
				lastAsteroidTime = System.currentTimeMillis();

				// "remove" ship
				shipExplosion = new Rectangle(
						ship.x,
						ship.y,
						ship.width,
						ship.height);
				ship.setLocation(this.getWidth() + ship.width, -ship.height);
				status.setNewShip(true);

				//shipHealth = 3;
				lastShipTime = System.currentTimeMillis();

				// play ship explosion sound
				soundMan.playShipExplosionSound();
				// play asteroid explosion sound
				soundMan.playAsteroidExplosionSound();

				for (int j = 0; j < gameLogic.NumOfAsteroids; j++)
					graphicsMan.drawAsteroid(asteroids[j], g2d, this);
			}
		}
		//----------------- FIN DEL BLOQUE DE LAS COLISIONES DE ASTEROIDES ---------------------------
		
		
		
	
	
	}


// update asteroids destroyed label
destroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));

// update ships left label
shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));

pointsValueLabel.setText(Long.toString(status.getScore()));

	}



	
	/**
	 * Draws the "Game Over" message.
	 */
	private void drawGameOver() {
		String gameOverStr = "GAME OVER";
		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameOverStr);
		if(strWidth > this.getWidth() - 10){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(gameOverStr);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.RED);
		g2d.drawString(gameOverStr, strX, strY);
	}

	/**
	 * Draws the initial "Get Ready!" message.
	 */
	 public void drawGetReady() {
		String readyStr = "Get Ready!";
		g2d.setFont(originalFont.deriveFont(originalFont.getSize2D() + 1));
		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();
		int strWidth = fm.stringWidth(readyStr);
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(readyStr, strX, strY);
	}
	 
	 public void drawLiveRecover() {
			String live3Str = "New lives!";
			g2d.setFont(originalFont.deriveFont(originalFont.getSize2D() + 1));
			FontMetrics fm = g2d.getFontMetrics();
			int ascent = fm.getAscent();
			int strWidth = fm.stringWidth(live3Str);
			int strX = (this.getWidth() - strWidth)/2;
			int strY = (this.getHeight() + ascent)/2;
			g2d.setPaint(Color.WHITE);
			g2d.drawString(live3Str, strX, strY);
		}
	
	int level;
	private void drawNextLevel() {

				String levelStr = "LEVEL "+level  ;
				g2d.setFont(originalFont.deriveFont(originalFont.getSize2D() + 1));
				FontMetrics fm = g2d.getFontMetrics();
				int ascent = fm.getAscent();
				int strWidth = fm.stringWidth(levelStr);
				int strX = (this.getWidth() - strWidth)/2;
				int strY = (this.getHeight() + ascent)/2;
				g2d.setPaint(Color.WHITE);
				g2d.drawString(levelStr, strX, strY);
         

	}

	
	/**
	 * Draws each new level's message.
	 */


	/**
	 * Draws the specified number of stars randomly on the game screen.
	 * @param numberOfStars the number of stars to draw
	 */
	private void drawStars(int numberOfStars) {
		g2d.setColor(Color.WHITE);
		for(int i=0; i<numberOfStars; i++){
			int x = (int)(Math.random() * this.getWidth());
			int y = (int)(Math.random() * this.getHeight());
			g2d.drawLine(x, y, x, y);
		}
	}

	/**
	 * Display initial game title screen.
	 */
	private void initialMessage() {
		String gameTitleStr = "Void Space";
		
		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD).deriveFont(Font.ITALIC);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameTitleStr);
		if(strWidth > this.getWidth() - 10){
			bigFont = currentFont;
			biggestFont = currentFont;
			fm = g2d.getFontMetrics(currentFont);
			strWidth = fm.stringWidth(gameTitleStr);
		}
		g2d.setFont(bigFont);
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2 - ascent;
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(gameTitleStr, strX, strY);
		
		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Press <Space> to Start a New Game.";
		strWidth = fm.stringWidth(newGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = (this.getHeight() + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(newGameStr, strX, strY);
		
		fm = g2d.getFontMetrics();
		String exitGameStr = "Press <Esc> to Exit the Game.";
		strWidth = fm.stringWidth(exitGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(exitGameStr, strX, strY);
	}
	
	/**
	 * Prepare screen for game over.
	 */
	public void doGameOver(){
		shipsValueLabel.setForeground(new Color(128, 0, 0));
	}
	
	/**
	 * Prepare screen for a new game.
	 */
	public void doNewGame(){		
		lastAsteroidTime = -NEW_ASTEROID_DELAY;
		lastShipTime = -NEW_SHIP_DELAY;
				
		bigFont = originalFont;
		biggestFont = null;
				
        // set labels' text
		shipsValueLabel.setForeground(Color.BLACK);
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));
		destroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));
	}

	/**
	 * Sets the game graphics manager.
	 * @param graphicsMan the graphics manager
	 */
	public void setGraphicsMan(GraphicsManager graphicsMan) {
		this.graphicsMan = graphicsMan;
	}

	/**
	 * Sets the game logic handler
	 * @param gameLogic the game logic handler
	 */
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		this.status = gameLogic.getStatus();
		this.soundMan = gameLogic.getSoundMan();
	}

	/**
	 * Sets the label that displays the value for asteroids destroyed.
	 * @param destroyedValueLabel the label to set
	 */
	public void setDestroyedValueLabel(JLabel destroyedValueLabel) {
		this.destroyedValueLabel = destroyedValueLabel;
	}
	
	/**
	 * Sets the label that displays the value for ship (lives) left
	 * @param shipsValueLabel the label to set
	 */
	public void setShipsValueLabel(JLabel shipsValueLabel) {
		this.shipsValueLabel = shipsValueLabel;
	}

	public void setPointsValueLabel(JLabel pointsValueLabel) {
		this.pointsValueLabel = pointsValueLabel;
	}
	public void setLevelValueLabel(JLabel levelNumber) {
		this.levelValueLabel = levelNumber;
	}
}
