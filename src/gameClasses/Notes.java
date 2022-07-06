package gameClasses;

public class Notes {
	/*public MyTriangle[] onePointOld(MyTriangle input, int offset) {
		int zero = (0 + offset)%3;
		int one  = (1 + offset)%3;
		int two  = (2 + offset)%3;
		
		double deltaX1 = input.points[one].x - input.points[zero].x;
		double deltaY1 = input.points[one].y - input.points[zero].y;
		double m1 = deltaY1/deltaX1;
		double n1 = input.points[zero].y - m1 * input.points[zero].x;
		
		double deltaX2 = input.points[two].x - input.points[zero].x;
		double deltaY2 = input.points[two].y - input.points[zero].y;
		double m2 = deltaY2/deltaX2;
		double n2 = input.points[zero].y - m2 * input.points[zero].x;
		
		MyPoint sp1 = new MyPoint((cuttingEdge-n1)/m1, cuttingEdge, 1);
		MyPoint sp2 = new MyPoint((cuttingEdge-n2)/m2, cuttingEdge, 1);
		
		MyTriangle[] result =  new MyTriangle[] {
								new MyTriangle(this, input.color, 
								 new MyPoint(input.points[zero].x, input.points[zero].y, input.points[zero].z),
								 sp1,
								 sp2)};
		return result;
	}
	
	public MyTriangle[] twoPointOld(MyTriangle input, int offset, double cuttingEdge) {
		
		int zero = (0 + offset)%3;
		int one  = (1 + offset)%3;
		int two  = (2 + offset)%3;
		
		double x1 = input.points[one].x - input.points[zero].x;
		double y1 = input.points[one].y - input.points[zero].y;
		double m1 = y1/x1;
		double n1 = input.points[zero].y - m1 * input.points[zero].x;
		
		double x2 = input.points[two ].x - input.points[zero].x;
		double y2 = input.points[two ].y - input.points[zero].y;
		double m2 = y2/x2;
		double n2 = input.points[zero].y - m2 * input.points[zero].x;
		
		MyPoint sp1 = new MyPoint((cuttingEdge-n1)/m1	, cuttingEdge	, 1				);
		MyPoint sp2 = new MyPoint((cuttingEdge-n2)/m2	, cuttingEdge	, 1				);
		MyPoint sp3 = new MyPoint((input.points[one].x+input.points[two].x)/2			, (input.points[one].y+input.points[two].y)/2	, (input.points[one].z+input.points[two].z)/2	);
		if(x1 == 0) {
			sp1.x = input.points[one].x;
		}
		if(x2 == 0) {
			sp2.x = input.points[two ].x;
		}
		
		return new MyTriangle[] {new MyTriangle(this, input.color, 		input.points[one], sp1, sp3				 ),
								 new MyTriangle(this, input.color, 		sp1, 			   sp2,	sp3				 ),
								 new MyTriangle(this, input.color, 		sp3,			   sp2,	input.points[two])};
	}
	
	public MyTriangle[] clipOld(MyTriangle[] input) {
		ArrayList<MyTriangle> temp = new ArrayList<MyTriangle>();
		
		double dist = 2;

		for(int i = 0; i < input.length; i++) {
			if(	input[i].points[0].x > -dist && input[i].points[0].x < dist && 
				input[i].points[0].y > -dist && input[i].points[0].y < dist &&
				input[i].points[0].z >  0 && input[i].points[0].z <  10 &&
					
				input[i].points[1].x > -dist && input[i].points[1].x < dist && 
				input[i].points[1].y > -dist && input[i].points[1].y < dist &&
				input[i].points[1].z >  0 && input[i].points[1].z <  10 &&
					
				input[i].points[2].x > -dist && input[i].points[2].x < dist && 
				input[i].points[2].y > -dist && input[i].points[2].y < dist &&
				input[i].points[2].z >  0 && input[i].points[2].z    <  10 	) temp.add(input[i]);
		}
		
		MyTriangle[] output = new MyTriangle[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			output[i] = temp.get(i);
		}
		
		return output;
	}
	
	
	*/
}
