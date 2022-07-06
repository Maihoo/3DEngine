package gameClasses;

import java.text.DecimalFormat;

import shapes.MyTriangle;
import shapes.Tetrahedron;

public class TF {
	public static double[][] translate(double x, double y, double z) {
		return new double[][] {	
			{ 1,  0,  0,  x},
			{ 0,  1,  0,  y},
			{ 0,  0,  1,  z},
			{ 0,  0,  0,  1}};
											
		//updateRsltMat();									
	}
	
	public static double[][] scale(double x, double y, double z) {
		return new double[][] {	
			{ x,  0,  0,  0},
			{ 0,  y,  0,  0},
			{ 0,  0,  z,  0},
			{ 0,  0,  0,  1}};
									
		//updateRsltMat();									
	}
	
	public static double[][] rotate(double x, double y, double z) {
		double c = Math.cos(Math.toRadians(x));
		double s = Math.sin(Math.toRadians(x));
		double[][] xRot = new double[][] {	{ 1,  0,  0,  0},
											{ 0,  c, -s,  0},
											{ 0,  s,  c,  0},
											{ 0,  0,  0,  1}};
		
		c = Math.cos(Math.toRadians(y));
		s = Math.sin(Math.toRadians(y));
		double[][] yRot = new double[][] {	{ c,  0,  s,  0},
											{ 0,  1,  0,  0},
											{-s,  0,  c,  0},
											{ 0,  0,  0,  1}};

		c = Math.cos(Math.toRadians(z));
		s = Math.sin(Math.toRadians(z));
		double[][] zRot = new double[][] {	{ c, -s,  0,  0 },
											{ s,  c,  0,  0 },
											{ 0,  0,  1,  0 },
											{ 0,  0,  0,  1 }};
							
											
		double[][] output = matMult(mat4(), xRot);		
				   output = matMult(output, yRot);
				   output = matMult(output, zRot);
		
		return output;
		//updateRsltMat();
	}
	
	public static double[][] mat4() {
		double[][] output = {
				{1, 0, 0, 0},
				{0, 1, 0, 0},
				{0, 0, 1, 0},
				{0, 0, 0, 1}};
		
		return output;
	}
	
	public static void displayMat(double[][] input) {
		System.out.println();
		DecimalFormat df = new DecimalFormat("###.###");
		
		for(int i = 0; i < input.length; i++) {
			System.out.println();
			for(int j = 0; j < input[0].length; j++) {
				System.out.print(df.format(input[i][j]) + "\t");
			}
		}
		System.out.println();
	}
	
	public static void displayVec(double[] input) {
		System.out.println();
		DecimalFormat df = new DecimalFormat("###.###");
		for(int i = 0; i < input.length; i++) {
			System.out.println(df.format(input[i]));
		}
	}
	
	public static double[] vecMult(double[] v, double x) {
		double[] output = new double[v.length];
		for(int i = 0; i < v.length; i++) {
			output[i] = v[i] * x;
		}
		return output;
	}
	
	public static double vecVecMult(double[] u, double[] v) {
		if(u.length != v.length) System.out.println("input 1 and 2 didn't have the same length in vecMult.");
		
		double output = 0;
		
		for(int i = 0; i < u.length; i++) {
			output += u[i]*v[i];
		}
		
		return output;
	}
	
	public static double[][] matMult(double[][] M1, double[][] M2) {
		if(M1.length != M2.length || M1[0].length != M2[0].length || M1.length != M1[0].length) System.out.println("input 1 and 2 didn't have the same length in matMult.");
		
		double[][] output = new double[M1.length][M1[0].length];
		
		for(int i = 0; i < M1.length; i++) {
			for(int j = 0; j < M1[0].length; j++) {
				double sum = 0;
				for(int h = 0; h < M1.length; h++) {
					sum += M1[i][h] * M2[h][j];
				}	
				output[i][j] = sum;
			}
		}
		
		return output;
	}
	
	public static double[] matVecMult(double[][] M, double[] v) {
		
		if(M[0].length != v.length) System.out.println("input 1 and 2 didn't have the same length in matVecMult.");
		
		double[] output = new double[v.length];
		
		for(int i = 0; i < v.length; i++) {
			double sum = 0;
			for(int j = 0; j < v.length; j++) {
				sum += M[i][j] * v[j];
			}	
			output[i] = sum;
		}
		
		return output;
	}
	
	public static double[][] perspective(int fov, double aspect, double near, double far) {
		
		double f = 1.0 / Math.tan( Math.toRadians(fov) / 2 );
    	double d = far - near;

    	double[][] output = mat4();
    	output[0][0] = f / aspect;
    	output[1][1] = f;
    	output[2][2] = -1/d;					//-(near+far) / d
    	output[2][3] = -(1 * near * far) / d;	//-(2 * near * far) / d
    	output[3][2] = -1;
    	output[3][3] = 0;
    	
    	return output;
	}
	
	public static double[][] transpose(double[][] M) {
		return new double[][] {
			{M[0][0], M[1][0], M[2][0], M[3][0]},
			{M[0][1], M[1][1], M[2][1], M[3][1]},
			{M[0][2], M[1][2], M[2][2], M[3][2]},
			{M[0][3], M[1][3], M[2][3], M[3][3]},
		};
	}
	
	public static double[][] createViewMat(double[] eye, double[] look, double[] up) {
		if(eye[0] == look[0] && eye[1] == look[1] && eye[2] == look[2]) return TF.mat4();
		
		double[][] output = new double[4][4];
		
		double[] v = normalize(subtract(look, eye));
		double[] n = normalize(cross   (v	, up ));
		double[] u = normalize(cross   (n	, v  ));
		
		v = negate(v);
		
		output[0][0] = n[0]; 	output[0][1] = n[1]; 	output[0][2] = n[2]; 	output[0][3] = - dot(n, eye);
		output[1][0] = u[0]; 	output[1][1] = u[1]; 	output[1][2] = u[2]; 	output[1][3] = - dot(u, eye);
		output[2][0] = v[0]; 	output[2][1] = v[1];	output[2][2] = v[2]; 	output[2][3] = - dot(v, eye);
		output[3][0] = 0; 		output[3][1] = 0; 		output[3][2] = 0; 		output[3][3] = 1;
		
		return output;
	}
	
	public static double dot(double[] u, double[] v) {
		if(u.length != v.length) System.out.println("input 1 and 2 didn't have the same length in dot.");
		
		double sum = 0;
		for(int i = 0; i < u.length; i++) {
			sum += u[i] * v[i];
		}
		
		return sum;
	}
	
	public static double[] vec4() {
		return new double[] {0, 0, 0, 1};
	}
	
	public static double[] negate(double[] u) {
		double[] output = new double[u.length];
		
		for(int i = 0; i < u.length; i++) {
			output[i] = - u[i];
		}
		
		return output;
	}
	
	public static double[] normalize(double[] input) {
		double total = vecLength(input);
		
		if(total == 0) {
			if(input.length == 3) return new double[] {0, 0, 1};
			else return new double[] {0, 0, 0, 1};
		}

		double[] output = new double[input.length];
				
		for(int i = 0; i < input.length; i++) {
			output[i] = input[i]/total;
		}
			
		return output;
	}
	
	public static double vecLength(double[] input) {
		double output = 0;
		for(int i = 0; i < input.length; i++) {
			output += input[i] * input[i];
		}
		output = Math.sqrt(output);
		
		return output;
	}
	
	public static double[] subtract(double[] u, double[] v) {
		if(u.length != v.length) System.out.println("input 1 and 2 didn't have the same length in subtract.");
		
		double[] output = new double[u.length];
		for(int i = 0; i < output.length; i++) {
			output[i] = u[i] - v[i];
		}
		
		return output;
	}
	
	public static double[] cross(double[] u, double[] v) {
		if(u.length != 3 || v.length != 3) System.out.println("one vector didn't have length of 3 in cross.");
		
		double[] output = new double[3];
		
		output[0] = u[1]*v[2] - u[2]*v[1];
		output[1] = u[2]*v[0] - u[0]*v[2];
		output[2] = u[0]*v[1] - u[1]*v[0];
		
		return output;
	}
	
	public static MyTriangle[] tetToTri(Tetrahedron input) {
		MyTriangle[] output = new MyTriangle[input.polygons.length];
		for(int i = 0; i < input.polygons.length; i++) {
			output[i] = input.polygons[i];
		}
		return output;
	}
	
	public static double[] VertToDoubleArray(Vertex input) {
		return new double[] {input.x, input.y, input.z, input.w};
	}
}
