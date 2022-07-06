package parts;

import java.util.ArrayList;

import api.Game;
import gameClasses.MyPoint;
import gameClasses.TF;
import shapes.MyTriangle;

public class Clipper {
	
	Game game;
	
	public Clipper(Game game) {
		this.game = game;
	}
	
	public MyTriangle[] clip(MyTriangle[] input, double cuttingEdge) {
		
		//clipping up
		ArrayList<MyTriangle> temp = new ArrayList<MyTriangle>();
		for(int i = 0; i < input.length; i++) {
			MyTriangle[] result = clipperBox(input[i], -1, cuttingEdge);
			for(int j = 0; j < result.length; j++) {
				temp.add(result[j]);
			}
		}
		MyTriangle[] afterUp = new MyTriangle[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			afterUp[i] = temp.get(i);
		}
		
		//clipping down
		temp = new ArrayList<MyTriangle>();
		for(int i = 0; i < afterUp.length; i++) {
			MyTriangle[] result = clipperBox(afterUp[i], 1, cuttingEdge);
			for(int j = 0; j < result.length; j++) {
				temp.add(result[j]);
			}
		}
		MyTriangle[] afterDown = new MyTriangle[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			afterDown[i] = temp.get(i);
		}
		
		afterDown = game.appl.applyTransformationToAllTriangles(afterDown, TF.rotate(0, 0, -90));
		
		//clipping left
		temp = new ArrayList<MyTriangle>();
		for(int i = 0; i < afterDown.length; i++) {
			MyTriangle[] result = clipperBox(afterDown[i], -1, cuttingEdge);
			for(int j = 0; j < result.length; j++) {
				temp.add(result[j]);
			}
		}
		MyTriangle[] afterLeft = new MyTriangle[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			afterLeft[i] = temp.get(i);
		}
	
		//clipping right
		temp = new ArrayList<MyTriangle>();
		for(int i = 0; i < afterLeft.length; i++) {
			MyTriangle[] result = clipperBox(afterLeft[i], 1, cuttingEdge);
			for(int j = 0; j < result.length; j++) {
				temp.add(result[j]);
			}
		}
		MyTriangle[] afterRight = new MyTriangle[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			afterRight[i] = temp.get(i);
		}
		
		afterRight = game.appl.applyTransformationToAllTriangles(afterRight, TF.rotate(0, 0, 90));
		
		afterRight = game.appl.applyTransformationToAllTriangles(afterRight, TF.rotate(-90, 0, 0));
		
		//clipping near
		temp = new ArrayList<MyTriangle>();
		for(int i = 0; i < afterRight.length; i++) {
			MyTriangle[] result = clipperBox(afterRight[i], -1, cuttingEdge);
			for(int j = 0; j < result.length; j++) {
				temp.add(result[j]);
			}
		}
		MyTriangle[] afterNear = new MyTriangle[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			afterNear[i] = temp.get(i);
		}
	
		//clipping far
		temp = new ArrayList<MyTriangle>();
		for(int i = 0; i < afterNear.length; i++) {
			MyTriangle[] result = clipperBox(afterNear[i], 1, cuttingEdge);
			for(int j = 0; j < result.length; j++) {
				temp.add(result[j]);
			}
		}
		MyTriangle[] afterFar = new MyTriangle[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			afterFar[i] = temp.get(i);
		}
		
		afterFar = game.appl.applyTransformationToAllTriangles(afterFar, TF.rotate(90, 0, 0));
		
		return afterFar;
	}
	
	public MyTriangle[] clipperBox(MyTriangle input, int dir, double cuttingEdge) {

		double pt0 = dir * input.points[0].y;
		double pt1 = dir * input.points[1].y;
		double pt2 = dir * input.points[2].y;
		
		//all points outside
		if(pt0 >= cuttingEdge && pt1 >= cuttingEdge && pt2 >= cuttingEdge ) return new MyTriangle[0];
		//all points inside
		if(pt0 <= cuttingEdge && pt1 <= cuttingEdge && pt2 <= cuttingEdge ) return new MyTriangle[] {input};
		//one point inside
		if(pt0 <= cuttingEdge && pt1 >= cuttingEdge && pt2 >= cuttingEdge ) return onePoint(input, 0, dir * cuttingEdge);
		if(pt0 >= cuttingEdge && pt1 <= cuttingEdge && pt2 >= cuttingEdge ) return onePoint(input, 1, dir * cuttingEdge);
		if(pt0 >= cuttingEdge && pt1 >= cuttingEdge && pt2 <= cuttingEdge ) return onePoint(input, 2, dir * cuttingEdge);
		//two points inside
		if(pt0 >= cuttingEdge && pt1 <= cuttingEdge && pt2 <= cuttingEdge ) return twoPoint(input, 0, dir * cuttingEdge);
		if(pt0 <= cuttingEdge && pt1 >= cuttingEdge && pt2 <= cuttingEdge ) return twoPoint(input, 1, dir * cuttingEdge);
		if(pt0 <= cuttingEdge && pt1 <= cuttingEdge && pt2 >= cuttingEdge ) return twoPoint(input, 2, dir * cuttingEdge);
		
		return new MyTriangle[0];
	}
	
	public MyTriangle[] onePoint(MyTriangle input, int offset, double cuttingEdge) {
		int zero = (0 + offset)%3;
		int one  = (1 + offset)%3;
		int two  = (2 + offset)%3;
		
		double r1 = ((cuttingEdge - input.points[zero].y) / (input.points[zero].y - input.points[one].y));
		double r2 = ((cuttingEdge - input.points[zero].y) / (input.points[zero].y - input.points[two].y));
		
		MyPoint sp1 = new MyPoint(input.points[zero].x + r1 * (input.points[zero].x - input.points[one].x), cuttingEdge, (input.points[zero].z + r1 * (input.points[zero].z - input.points[one].z)));
		MyPoint sp2 = new MyPoint(input.points[zero].x + r2 * (input.points[zero].x - input.points[two].x), cuttingEdge, (input.points[zero].z + r2 * (input.points[zero].z - input.points[two].z)));
		
		MyTriangle[] result =  new MyTriangle[] {
								new MyTriangle(game, input.color, 
								 new MyPoint(input.points[zero].x, input.points[zero].y, input.points[zero].z),
								 sp1,
								 sp2)};
		return result;
	}
	
	public MyTriangle[] twoPoint(MyTriangle input, int offset, double cuttingEdge) {
		
		int zero = (0 + offset)%3;
		int one  = (1 + offset)%3;
		int two  = (2 + offset)%3;
		
		double r1 = ((cuttingEdge - input.points[zero].y) / (input.points[zero].y - input.points[one].y));
		double r2 = ((cuttingEdge - input.points[zero].y) / (input.points[zero].y - input.points[two].y));
		
		MyPoint sp1 = new MyPoint(input.points[zero].x + r1 * (input.points[zero].x - input.points[one].x), cuttingEdge, (input.points[zero].z + r1 * (input.points[zero].z - input.points[one].z)));
		MyPoint sp2 = new MyPoint(input.points[zero].x + r2 * (input.points[zero].x - input.points[two].x), cuttingEdge, (input.points[zero].z + r2 * (input.points[zero].z - input.points[two].z)));
		MyPoint sp3 = new MyPoint((input.points[one].x + input.points[two].x)/2, (input.points[one].y+input.points[two].y)/2	, (input.points[one].z+input.points[two].z)/2	);
				
		return new MyTriangle[] {new MyTriangle(game, input.color, 		input.points[one], sp1, sp3				 ),
								 new MyTriangle(game, input.color, 		sp1, 			   sp2,	sp3				 ),
								 new MyTriangle(game, input.color, 		sp3,			   sp2,	input.points[two])};
	}
}
