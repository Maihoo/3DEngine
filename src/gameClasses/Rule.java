package gameClasses;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import api.Game;

public class Rule {
	
	Game game;

	Point p = MouseInfo.getPointerInfo().getLocation();
	Point b = MouseInfo.getPointerInfo().getLocation();
	
	public Rule (Game game) {
		this.game = game;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {			
		
	}
	
	public void init() {	
		
	}
}
