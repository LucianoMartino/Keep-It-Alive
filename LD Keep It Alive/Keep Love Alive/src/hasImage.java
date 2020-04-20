import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class hasImage {
	protected int SCALE;
	protected BufferedImage image = null;
	protected int width, height;
	protected void setImage(String path) {
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		width = image.getWidth() * SCALE;
		height = image.getHeight() * SCALE;
	}
}
