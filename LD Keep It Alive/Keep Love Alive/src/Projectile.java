import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Projectile {
	protected Rectangle bounds;
	protected int x, y;
	protected double xSpeed, ySpeed;
	public abstract void draw(Graphics2D g);
	public abstract void tick();
	public abstract void destroy();
	
	public Rectangle getBounds() {
		return bounds;
	}
}
