package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import api.Game;
import gameClasses.MyPoint;

public class MyTriangle {
	
	public Game game;
	
	public MyPoint[] points;
	public double[] P1, P2, P3;
	
	public Color color = Color.BLUE;
	public double[] normal;
	
	public MyTriangle(Game game, MyPoint... points) {
		this.game = game;
		this.points = new MyPoint[points.length];
		for(int i = 0; i < points.length; i++) {
			MyPoint p = points[i];
			this.points[i] = new MyPoint(p.x, p.y, p.z);
		}
		
		this.P1 = new double[] {points[0].x, points[0].y, points[0].z}; 
		this.P2 = new double[] {points[1].x, points[1].y, points[1].z}; 
		this.P3 = new double[] {points[2].x, points[2].y, points[2].z}; 
	}
	
	public MyTriangle(Game game, Color color, MyPoint... points) {
		this.game = game;
		this.color = color;
		this.points = new MyPoint[points.length];
		for(int i = 0; i < points.length; i++) {
			MyPoint p = points[i];
			this.points[i] = new MyPoint(p.x, p.y, p.z);
		}
		
		this.P1 = new double[] {points[0].x, points[0].y, points[0].z}; 
		this.P2 = new double[] {points[1].x, points[1].y, points[1].z}; 
		this.P3 = new double[] {points[2].x, points[2].y, points[2].z}; 
	}
	
	public MyTriangle(Game game, Color color, double[] normal, double[] P1, double[] P2, double[] P3) {
		this.game = game;
		this.color = color;
		this.normal = normal;
		this.P1 = P1;
		this.P2 = P2;
		this.P3 = P3;
		
		this.points = new MyPoint[3];
		this.points[0] = new MyPoint(P1[0], P1[1], P1[2]);
		this.points[1] = new MyPoint(P2[0], P2[1], P2[2]);
		this.points[2] = new MyPoint(P3[0], P3[1], P3[2]);
	}
	
	public void render(Graphics g) {
		Polygon poly = new Polygon();
		
		for(int i = 0; i < points.length; i++) {
			poly.addPoint(	(int) (game.width /2 - points[i].x*500 ), 
							(int) (game.height/2 - points[i].y*500 ));
		}
		
		g.setColor(this.color);
		g.fillPolygon(poly);
	}
	
	public void renderSpecial(Graphics g) {
		Polygon poly = new Polygon();
		
		for(int i = 0; i < points.length; i++) {
			poly.addPoint(	(int) (1600 - points[i].x*200), 
							(int) (200  - points[i].y*200));
		}
		
		g.setColor(this.color);
		g.fillPolygon(poly);
	}
	
	public void renderLine(Graphics g) {
		Polygon poly = new Polygon();
		
		for(int i = 0; i < points.length; i++) {
			poly.addPoint(	(int) (game.width /2 - points[i].x*500 ), 
							(int) (game.height/2 - points[i].y*500 ));
		}
		
		g.setColor(this.color);
		g.drawPolygon(poly);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
