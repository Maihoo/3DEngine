package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Tetrahedron {
	
	public MyTriangle[] polygons;
	public Color color;
	
	public Tetrahedron(Color color, MyTriangle... polygons) {
		this.color = color;
		this.polygons = new MyTriangle[polygons.length];
		for(int i = 0; i < polygons.length; i++) {
			this.polygons[i] = polygons[i];
		}
		setPolygonColor();
	}
	
	public Tetrahedron(MyTriangle... polygons) {
		color = Color.WHITE;
		this.polygons = new MyTriangle[polygons.length];
		for(int i = 0; i < polygons.length; i++) {
			this.polygons[i] = polygons[i];
		}
		setPolygonColor();
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < polygons.length; i++) {
			polygons[i].render(g);
		}
	}
	
	public void setPolygonColor() {
		for(MyTriangle poly : this.polygons) {
			poly.setColor(this.color);
		}
	}
}
