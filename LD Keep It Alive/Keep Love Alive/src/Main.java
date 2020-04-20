import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;

	private final static int SCALE = (int)(Toolkit.getDefaultToolkit().getScreenSize().height / 200.);
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

	private GameState currState = GameState.PLAY;

	private final Color BACKGROUND = new Color(0xdd88cc), PLAYER = new Color(0xcc5555);

	static ArrayList<Heart> hearts = new ArrayList<Heart>();
	static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

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
		hearts.add(new Heart(SCALE, winSize));
		lastHeartTime = getTime();
		;
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
			Thread.sleep(2);
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

	private void render(Graphics2D g) {
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());

		switch(currState) {
		case MENU:
			try {
				Font f = Font.createFont(Font.TRUETYPE_FONT, new File("LD-KLA/Symtext.ttf"));
				g.setFont(f);
			} catch (FontFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			g.drawString("KEEP LOVE ALIVE:", SCALE * 10, SCALE * 5);
		case PLAY:
			for (Heart h : hearts) {
				h.draw(g);
			}
			g.setColor(Color.BLACK);
			g.drawLine((int)(getWidth() / 2.), 0, (int)(getWidth() / 2.), getHeight());

			g.setColor(PLAYER);
			g.fillOval(playerX-SCALE*5, playerY, SCALE*10, SCALE*10);
			g.rotate(direction, getWidth() / 2., getHeight());
			
			for (Projectile p : projectiles)
				p.draw(g);
			
			g.setColor(PLAYER);
			g.fillRect(playerX, playerY, playerWidth, playerHeight);
			break;
		}
	}

	private void tick() {
		if (currState == GameState.PLAY) {
			playerX = (int) ((((double)getWidth() / SCALE)/ 2.) * SCALE);
			playerY = (int)(getHeight() - playerHeight/2.);
			direction = Math.atan2(mouseY - getHeight(), mouseX - getWidth() / 2.);

			if (getTime() - lastHeartTime >= 3) {
				hearts.add(new Heart(SCALE, winSize));
				lastHeartTime = getTime();
			}

			for (Heart h : hearts)
				h.tick();
			
			for (Projectile p : projectiles)
				p.tick();
		}

		while (!mouseQueue.isEmpty()) {
			switch (mouseQueue.remove()) {
			case 1:
				projectiles.add(new Bullet(SCALE, playerX, playerY, direction));
				break;
			case 2:
				// MENU
				break;
			case 3:
				// CATCH
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
