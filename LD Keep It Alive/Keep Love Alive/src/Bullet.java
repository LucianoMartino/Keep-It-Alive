import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bullet extends Projectile{
	private final int SPEED = 5;
	private final Color BULLET = Color.black;
	private static int SIZE;
	public Bullet(int SCALE, int x, int y, double direction) {

		this.x = x;
		this.y = y;
		
		xSpeed = Math.cos(direction) * SPEED;
		ySpeed = Math.sin(direction) * SPEED;
		
		SIZE = SCALE * 3;
		
		bounds = new Rectangle(x, y, SIZE, SIZE);
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(BULLET);
		g.fill(bounds);
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public static int getSize() {
		return SIZE;
	}

	
	
}
