package parts;

import java.awt.Color;

import api.Game;
import gameClasses.TF;
import gameClasses.Vertex;
import shapes.MyTriangle;

public class PrimitiveAssembler {
	
	Game game;
	
	public PrimitiveAssembler(Game game) {
		this.game = game;
	}
	

	public MyTriangle[] assemble(double[][] input, Color[] colors, double[][] normals) {
		if(input.length % 3 != 0) System.out.println("The amount of points is not devisable by 3: " + input.length);
		
		MyTriangle[] output = new MyTriangle[input.length / 3];
		
		for(int i = 2; i < input.length; i += 3) {
			output[i/3] = new MyTriangle(game, colors[i], normals[i], input[i-2], input[i-1], input[i]);
		}
		
		return output;
	}
	
	public MyTriangle[] assembleVertices(Vertex[] input) {
		if(input.length % 3 != 0) System.out.println("The amount of points is not devisable by 3: " + input.length);
		
		MyTriangle[] output = new MyTriangle[input.length / 3];
		
		for(int i = 2; i < input.length; i += 3) {
			output[i/3] = new MyTriangle(game, input[i].color, input[i].normal, TF.VertToDoubleArray(input[i-2]), TF.VertToDoubleArray(input[i-1]), TF.VertToDoubleArray(input[i]));
		}
		
		return output;
	}
}
