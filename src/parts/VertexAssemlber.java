package parts;

import java.awt.Color;

import api.Game;
import gameClasses.TF;
import gameClasses.Vertex;

public class VertexAssemlber {
	
	Game game;
	
	public VertexAssemlber(Game game) {
		this.game = game;
	}
	
	public Vertex[] asseble(double[][] input) {
		Vertex[] output = new Vertex[input.length];
		
		for (int i = 2; i < output.length; i += 3) {
			
			Color tempColor = new Color((100+ i*155)/output.length, (100 + i*155)/output.length, (100 + i*155)/output.length);
			
			double[] side1 = TF.subtract(input[i-1], input[i-2]);
			double[] side2 = TF.subtract(input[i  ], input[i-2]);
			double[] crossp = TF.cross(new double[] {side1[0], side1[1], side1[2]}, new double[] {side2[0], side2[1], side2[2]});
			crossp = new double[] {crossp[0], crossp[1], crossp[2], 0};
			
			output[i-2] = new Vertex(input[i-2][0], input[i-2][1], input[i-2][2], 1, TF.normalize(crossp), tempColor);
			output[i-1] = new Vertex(input[i-1][0], input[i-1][1], input[i-1][2], 1, TF.normalize(crossp), tempColor);
			output[i  ] = new Vertex(input[i  ][0], input[i  ][1], input[i  ][2], 1, TF.normalize(crossp), tempColor);
		}

		return output;
	}
	
	public Vertex[] assebleWithColor(double[][] input, Color color) {
		Vertex[] output = new Vertex[input.length];
		
		for (int i = 2; i < output.length; i += 3) {
			
			double[] side1 = TF.subtract(input[i-1], input[i-2]);
			double[] side2 = TF.subtract(input[i  ], input[i-2]);
			double[] crossp = TF.cross(new double[] {side1[0], side1[1], side1[2]}, new double[] {side2[0], side2[1], side2[2]});
			crossp = new double[] {crossp[0], crossp[1], crossp[2], 0};
			
			output[i-2] = new Vertex(input[i-2][0], input[i-2][1], input[i-2][2], 1, TF.normalize(crossp), color);
			output[i-1] = new Vertex(input[i-1][0], input[i-1][1], input[i-1][2], 1, TF.normalize(crossp), color);
			output[i  ] = new Vertex(input[i  ][0], input[i  ][1], input[i  ][2], 1, TF.normalize(crossp), color);
		}

		return output;
	}
}
