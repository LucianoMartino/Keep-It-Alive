import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {
	//IF YOU'RE READING THIS YOU'LL WANT TO CHANGE THE GAMESTATE IN ADDNOTIFY TO MENU
	private static final long serialVersionUID = 1L;

	private final static int SCALE = (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 200.);
	private final static Dimension winSize = new Dimension(150 * SCALE, 200 * SCALE);

	private static Queue<Integer> mouseQueue = new LinkedBlockingQueue<Integer>();
	private static final MouseAdapter MA = new MouseAdapter() {
		public void mousePressed(MouseEvent me) {
			mouseQueue.add(me.getButton());
		}

		public void mouseMoved(MouseEvent me) {
			mouseX = me.getX();
			mouseY = me.getY();
		}
//		public void mouseWheelMoved(MouseWheelEvent mwe){
//			if(mwe.getPreciseWheelRotation() > 0) {
//				SCALE++;
//			}else {
//				SCALE--;
//			}
//		}
	};

	private static int mouseX = 0, mouseY = 0;
	private double direction;

	private GameState currState;
	public static int lives;

	private final Color BACKGROUND = new Color(0xdd88cc), PLAYER = new Color(0xcc5555);

	static ArrayList<Falling> stuff = new ArrayList<Falling>();
	static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();;

	public static void main(String[] args) {
		JPanel panel = new Main();
		panel.setPreferredSize(winSize);

		JFrame frame = new JFrame("Keep Love Alive");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(panel);
		frame.pack();

//		frame.addKeyListener(new KeyAdapter() {
//			 public void keyPressed(KeyEvent e) {
//				 switch(e.getKeyCode()) {
//				 case KeyEvent.VK_ESCAPE:
//					 frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
//					 break;
//				 }
//		     }
//		});

		frame.addMouseListener(MA);
		frame.addMouseMotionListener(MA);
		frame.addMouseWheelListener(MA);

		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}

	private double lastHeartTime;

	public void addNotify() {
		super.addNotify();
		
		mouseQueue.clear();
		stuff.clear();;
		projectiles.clear();
		
		currState = GameState.DEAD;
		lives = 3;
		
		stuff.add(new Heart(SCALE, winSize));
		lastHeartTime = getTime();
	}

	private double delta = 0, secTime = 0, now, last;
	private int ticks = 0, frames = 0;

	public void paint(Graphics _g) {
		if (secTime == 0) {
			secTime = last = getTime();
		}

		now = getTime();
		delta += 60. * (now - last);
		last = now;

		while (delta >= 1) {

			tick();
			ticks++;
			delta--;
		}

		render((Graphics2D) _g);
		frames++;

		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (getTime() - secTime >= 1) {
			System.out.printf("ticks: %d, frames: %d\n", ticks, frames);
			ticks = 0;
			frames = 0;
			secTime = getTime();
		}

		repaint();
	}

	private double getTime() {
		return System.nanoTime() / 1000000000.;
	}

	private int playerWidth = SCALE * 10, playerHeight = SCALE * 7, playerX, playerY;
	BufferedImage image = null;
	int width = 0, height = 0;
	private void render(Graphics2D g) {
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());

		switch (currState) {
		case MENU:
			
			try {
				image = ImageIO.read(new File("LD-KLA/icon.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			width = image.getWidth() * SCALE;
			height = image.getHeight() * SCALE;

			g.drawImage(image, (int) (getWidth() / 2. - width / 2.), 0, width, height, null);

			try {
				image = ImageIO.read(new File("LD-KLA/menu.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			width = image.getWidth() * SCALE;

			g.drawImage(image, (int) (getWidth() / 2. - width / 2.), height, width, image.getHeight() * SCALE, null);
			break;
		case PLAY:
			for (Falling f : stuff) {
				f.draw(g);
			}
			g.setColor(Color.BLACK);
			
			g.setColor(PLAYER);
			g.fillOval(playerX - SCALE * 5, playerY, SCALE * 10, SCALE * 10);
			g.rotate(direction, getWidth() / 2., getHeight());

			for (Projectile p : projectiles)
				p.draw(g);

			g.setColor(PLAYER);
			g.fillRect(playerX, playerY, playerWidth, playerHeight);
			break;
		case DEAD:
			try {
				image = ImageIO.read(new File("LD-KLA/fin.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			width = image.getWidth() * SCALE;
			height = image.getHeight() * SCALE;

			g.drawImage(image, (int) (getWidth() / 2. - width / 2.), (int) (getHeight() / 2. - height / 2.), width, height, null);
			break;
		}
	}

	private void tick() {
		if (currState == GameState.PLAY) {
			playerX = (int) ((((double) getWidth() / SCALE) / 2.) * SCALE);
			playerY = (int) (getHeight() - playerHeight / 2.);
			direction = Math.atan2(mouseY - getHeight(), mouseX - getWidth() / 2.);

			if (getTime() - lastHeartTime >= 1.5) {
				stuff.add(Math.random() < .5 ? new Heart(SCALE, winSize) : new Fist(SCALE, winSize));
				lastHeartTime = getTime();
			}

			for (Falling f : stuff)
				f.tick();

			for (Projectile p : projectiles)
				p.tick();
		}

		while (!mouseQueue.isEmpty()) {
			switch (mouseQueue.remove()) {
			case 1:
				projectiles.add(new Bullet(SCALE, playerX, playerY, direction));
				break;
			case 2:
				addNotify();
				currState = GameState.PLAY;
				break;
			case 3:
				projectiles.add(new Parachute(SCALE, playerX, playerY, direction));
				break;
			}
		}

//		if(ticks % 10 == 0) {
//			if(getWidth() != WIDTH || getHeight() != HEIGHT) {
//				WIDTH = getWidth(); HEIGHT = getHeight();
//				if(WIDTH)
//		}
	}
}
