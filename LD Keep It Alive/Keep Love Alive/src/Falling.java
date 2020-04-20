import java.awt.Graphics2D;
import java.awt.Rectangle;

public interface Falling {
	void draw(Graphics2D g);

	void tick();

	void destroy();

	Rectangle getBounds();
}
