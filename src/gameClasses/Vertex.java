package gameClasses;

import java.awt.Color;

public class Vertex {
	public double x;
	public double y;
	public double z;
	public double w;
	
	public double[] normal;
	public Color color;
	
	public Vertex(double x, double y, double z, double w, double[] normal, Color color) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.normal = normal;
		this.color = color;
	}
}
