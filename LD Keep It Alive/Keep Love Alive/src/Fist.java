import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Fist extends hasImage implements Falling{
	private final Dimension WINDOW;
	private Rectangle bounds;
	private int x, y;
	private double ySpeed = 1;
	private boolean beenHit = false;

	public Fist(int winScale, Dimension winSize) {
		WINDOW = winSize;
		SCALE = winScale;

		setImage("LD-KLA/fist.png");

		x = (int) (Math.random() * (WINDOW.width - width));
		y = -height;

		bounds = new Rectangle(width, height, x, y);
	}

	public void draw(Graphics2D g) {
		g.drawImage(image, x, y, width, height, null);
	}

	public void tick() {
		if (y + height < WINDOW.height) {
			y += ySpeed;
			ySpeed += 0.2;
		} else if (beenHit) {
			if (y < WINDOW.height) {
				y += ySpeed;
			} else {
				destroy();
			}
		}else {
			setImage("LD-KLA/fist_landed.png");
		}
		
		//if hit set image to loved fist
		
		bounds.setLocation(x, y);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void destroy() {
		Main.stuff.remove(this);
	}
}
