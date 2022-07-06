package parts;

import java.util.ArrayList;

import gameClasses.TF;
import gameClasses.Vertex;

public class Culler {
	
	/*public MyTriangle[] cull(MyTriangle[] input, double[][] nor, double[][] modlMat, double[] cameraPos) {
		ArrayList<MyTriangle> temp = new ArrayList<MyTriangle>();
		
		for(int i = 0; i < input.length; i++) {

			double[] normalVec = game.appl.applyTransformation(new double[] {nor[i][0], nor[i][1], nor[i][2], 1}, modlMat);
//			normalVec = applyTransformation(normalVec, viewMat);
//			normalVec = applyTransformation(normalVec, projMat);
			
			double[] v = new double[] {	cameraPos[0] - input[i].points[0].x, 
										cameraPos[1] - input[i].points[0].y, 
										cameraPos[2] - input[i].points[0].z, 0};
			
			if((TF.vecVecMult(normalVec, v)/(TF.vecLength(normalVec)*TF.vecLength(v))) >= 0) {
				temp.add(input[i]);
			}
		}
		
		MyTriangle[] output = new MyTriangle[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			output[i] = temp.get(i);
		}
		return output;
	}*/
	
	public static Vertex[] cull(Vertex[] input, double[] cameraPos) {
		ArrayList<Vertex> temp = new ArrayList<Vertex>();
		
		for(int i = 2; i < input.length; i += 3) {
			
			double[] v = new double[] {	cameraPos[0] - input[i].x, 
										cameraPos[1] - input[i].y, 
										cameraPos[2] - input[i].z, 0};
			
			if((TF.vecVecMult(input[i].normal, v) / (TF.vecLength(input[i].normal)*TF.vecLength(v))) >= 0) {
				temp.add(input[i-2]);
				temp.add(input[i-1]);
				temp.add(input[i  ]);
			}
		}
		
		Vertex[] output = new Vertex[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			output[i] = temp.get(i);
		}
		return output;
	}	
}
