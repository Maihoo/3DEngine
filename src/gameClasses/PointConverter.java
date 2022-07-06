package gameClasses;

import java.awt.Point;

import api.Game;

public class PointConverter {
	
	Game game;

	//static int distToOrigin = 15;
	static int flyPoint = 14000;
	
	public PointConverter(Game game) {
		this.game = game;
	}
	
	public Point convertPoint(MyPoint point3D) {
		Point point2D = new Point((int) ((game.width /2 + point3D.x*500 )),
								  (int) ((game.height/2 + point3D.y*500)));
		return point2D;
	}
}
