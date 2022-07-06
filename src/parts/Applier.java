package parts;

import api.Game;
import gameClasses.MyPoint;
import gameClasses.TF;
import gameClasses.Vertex;
import shapes.MyTriangle;

public class Applier {
	
	Game game;
	
	public Applier(Game game) {
		this.game = game;
	}
	
	/*
	public double[][] applyTransformationToAll(double[][] input, double[][] mat) {
		double[][] output = new double[input.length][4];
		
		for(int i = 0; i < input.length; i++) {
			output[i] = applyTransformation(input[i], mat);
		}
		return output;
	}
	*/
	public double[] applyTransformation(double[] input, double[][] mat) {
		double[] result = TF.matVecMult(mat, new double[] {input[0], input[1], input[2], 1});
		
		double zToDevideBy = Math.abs(result[3]);
		if(zToDevideBy == 0) {
			System.out.println("Devision by 0 in applyTransformation");
			return new double[] {0,0,0,0};
		}
		
		//depth devision
		return new double[] {result[0] / zToDevideBy, result[1] / zToDevideBy, result[2], zToDevideBy};
	}
	
	public MyTriangle[] applyTransformationToAllTriangles(MyTriangle[] input, double[][] mat) {
		MyTriangle[] output = new MyTriangle[input.length];
		
		for(int i = 0; i < input.length; i++) {
			MyPoint[] temp = new MyPoint[input[i].points.length];
			for(int j = 0; j < input[i].points.length; j++) {
				temp[j] = applyTransformationTriangles(input[i].points[j], mat);
			}
			output[i] = new MyTriangle(game, input[i].color, temp);
		}
		return output;
	}
	
	public MyPoint applyTransformationTriangles(MyPoint input, double[][] mat) {
		double[] temp = new double[] {input.x, input.y, input.z, 1};
		
		double[] result = TF.matVecMult(mat, temp);
		
		double zToDevideBy = Math.abs(result[3]);
		
		//depth devision
		return new MyPoint(result[0] / zToDevideBy, result[1] / zToDevideBy, result[2]);
	}
	
	public Vertex[] applyTransformationToAllVertices(Vertex[] input, double[][] mat) {
		Vertex[] output = new Vertex[input.length];
		
		for(int i = 0; i < input.length; i++) {
			output[i] = applyTransformationVertices(input[i], mat);
		}
		return output;
	}
	
	public Vertex applyTransformationVertices(Vertex input, double[][] mat) {
	
		double[] result = TF.matVecMult(mat, new double[] {input.x, input.y, input.z, input.w});
		
		double zToDevideBy = Math.abs(result[3]);
	
		//depth devision
		return new Vertex (result[0] / zToDevideBy, result[1] / zToDevideBy, result[2], zToDevideBy, TF.matVecMult(mat, input.normal), input.color);
	}
	
	
	
	
	
	
	
	
	
}
