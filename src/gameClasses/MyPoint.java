package gameClasses;

public class MyPoint {

	public double x, y, z;
	
	public MyPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void scale(double X, double Y, double Z) {
		this.x = x * X;
		this.y = y * Y;
		this.z = z * Z;
	}
	
}
