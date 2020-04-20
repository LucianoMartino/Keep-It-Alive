import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Parachute extends hasImage implements Projectile {
	private final int SPEED = 2;
	private int x, y, width, height;
	private double xSpeed, ySpeed;
	private Rectangle bounds;

	public Parachute(int winScale, int x, int y, double direction) {
		SCALE = winScale;
		
		this.x = x;
		this.y = y;

		setImage("LD-KLA/para_shot.png");
		
		xSpeed = Math.cos(direction) * SPEED;
		ySpeed = Math.sin(direction) * SPEED;

		width = image.getWidth() * SCALE;
		height = image.getHeight() * SCALE;
		
		bounds = new Rectangle(x, y, width, height);
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, x, y, width, height, null);
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
		return bounds;
	}
}
