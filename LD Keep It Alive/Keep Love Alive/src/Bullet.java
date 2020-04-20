import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bullet implements Projectile {
	
	private final int SPEED = 2;
	private final Color BULLET = Color.black;
	private int x, y;
	private double xSpeed, ySpeed;
	private Rectangle bounds;
	private static int SIZE;

	public Bullet(int winScale, int x, int y, double direction) {

		this.x = x;
		this.y = y;

		xSpeed = Math.cos(direction) * SPEED;
		ySpeed = Math.sin(direction) * SPEED;

		SIZE = winScale * 3;

		bounds = new Rectangle(x, y, SIZE, SIZE);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(BULLET);
		g.fill(bounds);
	}

	@Override
	public void tick() {
		x += xSpeed;
		y += ySpeed;

		bounds.setLocation(x, y);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
}
