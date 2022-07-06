package api;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Assets {
	
	Game game;
	
	Assets(Game game){
		this.game = game;
	}
		
	public static void init() {
		
	}
		
	public static void scaleAssets(int amount) {
		
	}
		
	public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
		BufferedImage dbi = null;
		if(sbi != null) {
		    if(dWidth <= 0) dWidth = 1;
		    if(dHeight <= 0) dHeight = 1;
		    dbi = new BufferedImage(dWidth, dHeight, imageType);
		    Graphics2D g = dbi.createGraphics();
		    AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
		    g.drawRenderedImage(sbi, at);
		}
		return dbi;
	}
}