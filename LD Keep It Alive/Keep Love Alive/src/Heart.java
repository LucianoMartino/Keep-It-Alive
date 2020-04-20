import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Heart extends hasImage implements Falling{
	private final Dimension WINDOW;
	private Rectangle bounds;
	private int x, y;
	private double ySpeed = 1;
	private boolean hasParachute = false, beenHit = false;

	public Heart(int winScale, Dimension winSize) {
		WINDOW = winSize;
		SCALE = winScale;

		setImage("LD-KLA/heart.png");

		x = (int) (Math.random() * (WINDOW.width - width));
		y = -height;

		bounds = new Rectangle(width, height, x, y);
	}

	public void draw(Graphics2D g) {
		g.drawImage(image, x, y, width, height, null);
	}

	public void tick() {
		// move to landing location
		if (y + height < WINDOW.height) {
			y += ySpeed;
			ySpeed += 0.2;
		} else if (hasParachute) {
			if (y < WINDOW.height) {
				y += ySpeed;
			} else {
				destroy();
			}
		} else if(beenHit){
			setImage("LD-KLA/broken_heart_2.png");
		}else {
			setImage("LD-KLA/broken_heart_rubble.png");
		}

		bounds.setLocation(x, y);
	}

	public void setYSpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void destroy() {
		Main.stuff.remove(this);
	}
}
