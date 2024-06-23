import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

class Game extends JPanel implements ActionListener {

    private static final String LOGO_PATH = "Images/start/logo.png";
    private static final String START_BUTTON_PATH = "Images/start/startButton.png";
    private static final String INFO_BUTTON_PATH = "Images/start/infoButton.png";
    private static final String ELEPHANT_UP_PATH = "Images/elephants/up.png";
    private static final String ELEPHANT_LEFT_PATH = "Images/elephants/left.png";
    private static final String ELEPHANT_RIGHT_PATH = "Images/elephants/right.png";
    private static final String ELEPHANT_DOWN_PATH = "Images/elephants/down.png";
    private static final String GRASS_PATH = "Images/environment/grass.jpg";
    private static final String TREE_PATH = "Images/environment/tree.png";
    private static final String SHRUB_PATH = "Images/environment/shrub.png";
    private static final String WATER_PATH = "Images/environment/water.png";

    private static final int NUM_ROWS = 9;
    private static final int NUM_SPRITES = 8;
    private static final int ELEPHANT_START_X = 300;
    private static final int ELEPHANT_START_Y = 400;
    private static final int TIMER_DELAY = 25;

    private Sprite gameLogo = new Sprite(LOGO_PATH);
    private boolean showGameLogo = true;
    private ScoreKeeping scoreKeeper = new ScoreKeeping();
    private LandBuilder builder = new LandBuilder();
    private MovingObstacle carGen = new MovingObstacle("car");
    private ArrayList<Sprite> cars = new ArrayList<>();
    private MovingObstacle bisonGen = new MovingObstacle("bison");
    private ArrayList<Sprite> bison = new ArrayList<>();
    private Sprite[][] allRows = new Sprite[NUM_ROWS][NUM_SPRITES];
    private ArrayList<Integer> special = new ArrayList<>();
    private boolean onWaterBlock = false;
    private Sprite elephant = new Sprite(ELEPHANT_UP_PATH);
    private int score = 0, movement = 0;
	private int lives = 5;
    private int up = 0, down = 0;
    private double left = 0, right = 0;
    private boolean press = false;
    private Timer gameLoop;
    private Random rand = new Random();
    private JButton startButton;
    private JButton infoButton;
    private boolean newGame = false;
    private boolean firstGame = true;

    public Game(boolean pause) {
        startButton = new JButton(new ImageIcon(START_BUTTON_PATH));
        startButton.setBorder(BorderFactory.createEmptyBorder());
        startButton.setContentAreaFilled(false);
        startButton.addActionListener(this);
        add(startButton);
        startButton.setBounds(110, 450, 230, 109);

        infoButton = new JButton(new ImageIcon(INFO_BUTTON_PATH));
        infoButton.setBorder(BorderFactory.createEmptyBorder());
        infoButton.setContentAreaFilled(false);
        infoButton.addActionListener(this);
        add(infoButton);
        infoButton.setBounds(470, 450, 230, 109);
        setLayout(null);

        addKeyListener(new KeyPressing());
        setFocusable(true);
        setDoubleBuffered(true);
        setInitialBoard();

        gameLoop = new Timer(TIMER_DELAY, this);

        showGameLogo = true;
        startButton.setVisible(true);
        infoButton.setVisible(true);
    }

    private void startGame() {
        showGameLogo = false;
        startButton.setVisible(false);
        infoButton.setVisible(false);
        resetGame();
        gameLoop.start();
    }

    private void resetGame() {
        score = 0;
        movement = 0;
        lives = 5;
        up = 0;
        down = 0;
        left = 0;
        right = 0;
        press = false;
        onWaterBlock = false;
        cars.clear();
        bison.clear();
        setInitialBoard();
        elephant.loadImage(ELEPHANT_UP_PATH);
        repaint();
    }

    private void newGame() {
        if (newGame) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            new Main(false);
        }
    }

    private void setInitialBoard() {
        setInitialElephant();
        setInitialLand();
    }

    private void setInitialElephant() {
        elephant.setX(ELEPHANT_START_X);
        elephant.setY(ELEPHANT_START_Y);
    }

    private void getInitialLand() {
        for (int i = 0; i < NUM_ROWS; i++) {
            allRows[i] = builder.getRow();
        }
    }

    private void setInitialLand() {
        getInitialLand();

        allRows[5][3].loadImage(GRASS_PATH);
        allRows[4][3].loadImage(GRASS_PATH);

        int x = 0;
        int y = -100;

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int z = 0; z < NUM_SPRITES; z++) {
                allRows[i][z].setX(x);
                allRows[i][z].setY(y);
                x += 100;
            }
            x = 0;
            y += 100;
        }

        for (int i = 0; i < NUM_SPRITES; i++) {
            if (allRows[0][i].getFileName().equals(GRASS_PATH)) {
                special.add(i);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            if (firstGame) {startGame();}
            else {newGame();}
            // newGame = true;
            // // startGame();
        } else if (e.getSource() == infoButton) {
            JOptionPane.showMessageDialog(null, "Hello! Welcome to Elephant Crossing.\nHelp Ellie the Elephant cross the road!\nMove the arrow keys to control Ellie's movement.\nEllie is able to swim in the water up to 5 times\nper game. Make sure she doesn't drown!");
        } else {
            elephantBounds();
            updateElephant();
            updateCars();
            updateBison();

            for (Sprite[] strip : allRows) {
                for (Sprite s : strip) {
                    s.move();
                }
            }

            manageRows();
            scrollScreen();

            if (movement > score) {
                score = movement;
            }

            repaint();
        }
    }

    private class KeyPressing extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if (!press) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        if (elephant.getX() < 695) {
                            right = 7;
                            press = true;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if (elephant.getX() > 0) {
                            left = 7;
                            press = true;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        up = 10;
                        press = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        down = 10;
                        press = true;
                        break;
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_LEFT:
                    elephant.setXDir(0);
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    elephant.setYDir(2);
                    break;
            }
        }
    }

    private void updateBison() {
        for (int i = 0; i < bison.size(); i++) {
            Sprite bison1 = bison.get(i);
            if (bison1.getY() > 800) {
                bison.remove(i);
                i--;
            } else {
                bison1.move();
                adjustBisonPosition(bison1);

                if (collision(bison1, elephant)) {
                    gameEnd("bison");
                }
            }
        }
    }

    private void updateElephant() {
        if (left > 0 && press) {
            elephant.setXDir(-13.2);
            left--;

            elephant.loadImage(ELEPHANT_LEFT_PATH);
        } else if (right > 0 && press) {
            elephant.setXDir(14.5);
            right--;
            elephant.loadImage(ELEPHANT_RIGHT_PATH);
        } else if (left == 0 && right == 0 && up == 0 && down == 0) {
            elephant.setXDir(0);
            press = false;
        }

        if (up > 0 && press) {
            elephant.setYDir(-10);
            elephant.move();
            elephant.loadImage(ELEPHANT_UP_PATH);
            adjustElephantPosition(true);
        } else if (down > 0 && press) {
            elephant.setYDir(10);
            elephant.move();
            elephant.loadImage(ELEPHANT_DOWN_PATH);
            adjustElephantPosition(false);
        }

        elephant.move();
    }

    private void adjustElephantPosition(boolean isMovingUp) {
        int location = elephant.getY();
        for (Sprite[] strip : allRows) {
            Sprite current = strip[0];
            if (isMovingUp && location - current.getY() > 95 && location - current.getY() < 105) {
                elephant.setYDir(0);
                up = 0;
                press = false;
                elephant.setY(current.getY() + 101);
                movement++;
                break;
            } else if (!isMovingUp && location - current.getY() < -95 && location - current.getY() > -105) {
                elephant.setYDir(0);
                down = 0;
                press = false;
                elephant.setY(current.getY() - 99);
                movement--;
                break;
            }
        }
    }

    private void updateCars() {
        for (int i = 0; i < cars.size(); i++) {
            Sprite car = cars.get(i);
            if (car.getY() > 800) {
                cars.remove(i);
                i--;
            } else {
                car.move();
                adjustCarPosition(car);

                if (collision(car, elephant)) {
                    gameEnd("car");
                }
            }
        }
    }

    private void adjustBisonPosition(Sprite bison) {
        if (bison.getX() < -(rand.nextInt(600) + 400)) {
            bison.setXDir(-(rand.nextInt(10) + 10));
            bison.setX(900);
            bison.loadImage("Images/bison/bisonLeft.png");
        } else if (bison.getX() > (rand.nextInt(600) + 1100)) {
            bison.setXDir((rand.nextInt(10) + 10));
            bison.setX(-200);
            bison.loadImage("Images/bison/bisonRight.png");
        }
    }

    private void adjustCarPosition(Sprite car) {
        if (car.getX() < -(rand.nextInt(600) + 400)) {
            car.setXDir(-(rand.nextInt(10) + 10));
            car.setX(900);
            car.loadImage(carGen.randomObstacle(true));
        } else if (car.getX() > (rand.nextInt(600) + 1100)) {
            car.setXDir((rand.nextInt(10) + 10));
            car.setX(-200);
            car.loadImage(carGen.randomObstacle(false));
        }
    }

    // --------------------------------------------------------------------------------------------------------------------------
    // --------------------------------------------------- GAME END/COLLISION ---------------------------------------------------
    // --------------------------------------------------------------------------------------------------------------------------
    private void gameEnd(String reason) {
        repaint();
        gameLoop.stop();

        String message = "Oh no! Ellie ";
        switch (reason) {
            case "water":
				message += "drowned in the water :(";
                break;
            case "outOfScreen":
                message += "left the game :(";
                break;
            case "car":
                message += "got hit by a car :(";
                break;
            case "bison":
                message += "got hit by a bison :(";
                break;
        }

        scoreKeeper.updateScores(score);

        message += "\nScore: " + score;
        JOptionPane.showMessageDialog(null, message);
        showGameLogo = true;
        startButton.setVisible(true);
        infoButton.setVisible(true);

    }

    private void elephantBounds() {
        boolean onWater = false;
        for (Sprite[] strip : allRows) {
            for (Sprite s : strip) {
                if (collision(elephant, s)) {
                    if ((s.getFileName().equals(TREE_PATH)) || (s.getFileName().equals(SHRUB_PATH))) {
						handleImpassableObstacles(s);
                    } else if (s.getFileName().equals(WATER_PATH)) {
						handleWaterCollision();
                        onWater = true;
                    }
                }

                if (outOfScreen()) {
                    gameEnd("outOfScreen");
                }
            }
        }
        if (!onWater) {
            onWaterBlock = false;
        }
    }

	private void handleWaterCollision() {
        if (!onWaterBlock) {
            lives--;
            if (lives == 0) {
                gameEnd("water");
            } else {
                elephant.setYDir(0);
                elephant.setXDir(0);
            }
        }
        onWaterBlock = true;
        
    }

	private void handleImpassableObstacles(Sprite tree) {
		int elephantTop = elephant.getY();
		int elephantBottom = elephantTop + elephant.getHeight();
		int elephantLeft = elephant.getX();
		int elephantRight = elephantLeft + elephant.getWidth();
		int treeTop = tree.getY();
		int treeBottom = treeTop + tree.getHeight();
		int treeLeft = tree.getX();
		int treeRight = treeLeft + tree.getWidth();
		
		// testing for horizontal and vertical "overlap"
		if (elephantBottom > treeTop && elephantTop < treeBottom && elephantRight > treeLeft && elephantLeft < treeRight) {
			if (up > 0) {
				up = 0;
				elephant.setY(treeBottom);
				elephant.setYDir(0);
			} else if (down > 0) {
				down = 0;
				elephant.setY(treeTop - elephant.getHeight());
				elephant.setYDir(0);
			} else if (left > 0) {
				left = 0;
				elephant.setX(treeRight);
				elephant.setXDir(0);
			} else if (right > 0) {
				right = 0;
				elephant.setX(treeLeft - elephant.getWidth());
				elephant.setXDir(0);
			}
		}	
	}

    private boolean outOfScreen() {
        if (elephant.getY() > 800 || elephant.getY() < -110) {
            elephant.setY(500);
            elephant.setX(900);
            return true;
        }
        return false;
    }

    private boolean collision(Sprite one, Sprite two) {
        Rectangle first = new Rectangle(one.getX() + 20, one.getY() + 20, one.getWidth() - 20, one.getHeight() - 20);
        Rectangle second = new Rectangle(two.getX() + 20, two.getY() + 20, two.getWidth() - 20, two.getHeight() - 20);
        return first.intersects(second);
    }

    // --------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------- ENVIRONMENT UPDATING --------------------------------------------------
    // --------------------------------------------------------------------------------------------------------------------------
    private void scrollScreen() {
        for (Sprite[] strip : allRows) {
            for (Sprite s : strip) {
                s.setYDir(2);
            }
        }
        if (!press) {
            elephant.setYDir(2);
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Sprite[] strip : allRows) {
            for (Sprite s : strip) {
                s.paintSprite(g, this);
            }
        }

        elephant.paintSprite(g, this);

        for (Sprite s : cars) {s.paintSprite(g, this);}
        for (Sprite s : bison) {s.paintSprite(g, this);}

        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + score, 30, 80);
		
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g.setColor(Color.RED);
		g.drawString("Water Lives: " + lives, 550, 750);

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("High Score: " + scoreKeeper.readScore(), 30, 40);

        if (showGameLogo) {
            gameLogo.setX(160);
            gameLogo.setY(10);
            gameLogo.paintSprite(g, this);
            startButton.setVisible(true);
            infoButton.setVisible(true);
        }
    }

    private void manageRows() {
        for (int v = 0; v < NUM_ROWS; v++) {
            if (allRows[v][0].getY() > 800) {
                allRows[v] = builder.getRow();

                resetRowPosition(v);

                if (LandBuilder.getLandType(allRows[v][0]).equals("road")) {
                    cars.add(carGen.setObstacle(allRows[v][0].getY() + 10));
                }

                if (LandBuilder.getLandType(allRows[v][0]).equals("sand")) {
                    bison.add(bisonGen.setObstacle(allRows[v][0].getY() + 10));
                }
            }
        }
    }

    private void resetRowPosition(int v) {
        int x = 0;
        for (int i = 0; i < NUM_SPRITES; i++) {
            allRows[v][i].setY(-99);
            allRows[v][i].setX(x);
            x += 100;
        }
    }
}
