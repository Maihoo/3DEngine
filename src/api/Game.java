package api;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import gameClasses.MyPoint;
import gameClasses.PointConverter;
import gameClasses.TF;
import gameClasses.Vertex;
import parts.Applier;
import parts.Clipper;
import parts.Culler;
import parts.PrimitiveAssembler;
import parts.VertexAssemlber;
import shapes.MyTriangle;
import shapes.Tetrahedron;

public class Game implements Runnable {
	
	public  boolean liefSchonMal = false;
	public  boolean mouseControl = false;
	private boolean running = false;
	
	public int width, height, ticks, resultFPS;
	int MAX_FRAMESKIP = 5;
	int fov = 60;
	
	public double ts, ts2, count, interpolation, angleX, angleY, standRotation;
	public double fps = 144;
	public double near = 1.1;
	public double far = 10;
	public double fudgeFactor = 0.1;
	double cuttingEdge = 1;
	int SKIP_TICKS = (int) (1000 / fps);
	
	public double[]   cameraPos, lookAtPos, upVector;
	public double[][] viewMat, projMat, modlMat, rsltMat;
	
	public  String title;
	
	private Thread thread;
	private BufferStrategy bs;
	private Graphics g;
	public  Display display;
	public  PointConverter pc = new PointConverter(this);
	public  Robot robot;
	
	double[] slider = new double[] {0, 0, 0};
	
	public Vertex[] pts;
	public double[][] nor;
	public Color[] clr;
	
	//Parts of Pipeline
	public Clipper clip;
	public Applier appl;
	public PrimitiveAssembler prim;
	public VertexAssemlber vert;
	
	//Input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	//shapes
	Tetrahedron tet;
	Tetrahedron surface;
	
	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}
	
	private void init() {
		if(display != null) display.close();
		display = new Display(title, width, height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		display.setTitle(title);
		Assets.init();
	
		clip = new Clipper(this);
		appl = new Applier(this);
		prim = new PrimitiveAssembler(this);
		vert = new VertexAssemlber(this);
		
		defineShapes();
		initMatrices();
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	int x = 250, y = 250;
	
	public void checkKeys() {
		keyManager.tick();
		mouseManager.tick();
		
		if(keyManager.keys[KeyEvent.VK_UP]) 	angleY++;
		if(keyManager.keys[KeyEvent.VK_DOWN]) 	angleY--;
		if(keyManager.keys[KeyEvent.VK_LEFT])	angleX--;
		if(keyManager.keys[KeyEvent.VK_RIGHT]) 	angleX++;
		if(keyManager.keys[KeyEvent.VK_W])		{	cameraPos[0] += 0.04 * Math.sin(Math.toRadians(angleX));
													cameraPos[2] += 0.04 * Math.cos(Math.toRadians(angleX));	}
		if(keyManager.keys[KeyEvent.VK_S]) 		{ 	cameraPos[0] -= 0.04 * Math.sin(Math.toRadians(angleX));
													cameraPos[2] -= 0.04 * Math.cos(Math.toRadians(angleX));	}
		if(keyManager.keys[KeyEvent.VK_A]) 		{ 	cameraPos[0] -= 0.04 * Math.cos(Math.toRadians(angleX));
													cameraPos[2] += 0.04 * Math.sin(Math.toRadians(angleX));	}
		if(keyManager.keys[KeyEvent.VK_D])		{ 	cameraPos[0] += 0.04 * Math.cos(Math.toRadians(angleX));
													cameraPos[2] -= 0.04 * Math.sin(Math.toRadians(angleX));	}
		
		if(keyManager.keys[KeyEvent.VK_SPACE]) 		cameraPos[1] += 0.04;
		if(keyManager.keys[KeyEvent.VK_CONTROL])	cameraPos[1] -= 0.04;
		
		if(angleY >  88) angleY =  88;
		if(angleY < -88) angleY = -88;
		
		lookAtPos[0] = cameraPos[0] + Math.sin(Math.toRadians(angleX)) * Math.cos(Math.toRadians(angleY));
		lookAtPos[1] = cameraPos[1] + Math.sin(Math.toRadians(angleY));
		lookAtPos[2] = cameraPos[2] + Math.cos(Math.toRadians(angleX)) * Math.cos(Math.toRadians(angleY));
		
		updateViewMat();
	}
	
	@SuppressWarnings("static-access")
	public void mouseMove() {
		if(keyManager.keys[KeyEvent.VK_ESCAPE]) {
			robot.mouseMove(display.getSP().x + width/2, display.getSP().y + height/2);
			if(mouseControl) {
				mouseControl = false;
				display.getFrame().getContentPane().setCursor(Cursor.getDefaultCursor());
			}
			else {
				mouseControl = true;
				BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
				Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
					    cursorImg, new Point(0, 0), "blank cursor");

				display.getFrame().getContentPane().setCursor(blankCursor);
			}
			keyManager.reset();
			
			try {
				this.thread.sleep(200);
			} catch (InterruptedException e) {}
			
		}
		
		if(mouseControl) {
			angleX -= (double) (width/2  - display.getMP().x) /3;
			angleY += (double) (height/2 - display.getMP().y) /3;
			robot.mouseMove(display.getSP().x + width/2, display.getSP().y + height/2);
		}
	}
	
	private void tick() {
		mouseMove();
		checkKeys();
	}
	
	public void render() {		
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);
		//Draw Here!
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		modlMat = TF.mat4();
		updateViewMat();
		
		/*
		double[] W = new double[] {-5,  -5,   5};
		double[] X = new double[] { 5,  -5,   5};
		double[] Y = new double[] { 5,  -5,  -5};
		double[] Z = new double[] {-5,  -5,  -5};
		Vertex[] temporary = vert.assebleWithColor(new double[][] {X, Y, Z, Z, W, X}, Color.CYAN);
		
		Vertex[] afterModlMat2 = appl.applyTransformationToAllVertices(temporary, modlMat);
		Vertex[] afterCulling2 = Culler.cull(afterModlMat2, cameraPos);
		Vertex[] afterviewMat2 = appl.applyTransformationToAllVertices(afterCulling2, viewMat);
		Vertex[] afterTrnsfrm2 = appl.applyTransformationToAllVertices(afterviewMat2, projMat);
		MyTriangle[] afterClipping2 = clip.clip(prim.assembleVertices(afterTrnsfrm2), cuttingEdge);
		
		for(int i = 0; i < afterClipping2.length; i++) {
			afterClipping2[i].renderLine(g);
			afterClipping2[i].render(g);
		}*/
		
		//////////////////////////////////////////////////////////////////////////////////////////////////
		
		if(mouseControl) standRotation += 0.25;
		
		slider[0] += display.xRot;
		slider[1] += display.yRot;
		slider[2] += display.zRot;
		
		modlMat = TF.matMult(modlMat, TF.rotate(slider[0], slider[1], slider[2]));
		updateViewMat();
		//TF.displayVec(cameraPos);
		//TF.displayVec(appl.applyTransformation(cameraPos, TF.matMult(modlMat, viewMat)));
		
		/*double[][] afterModlMat 	= applyTransformationToAll(pts, modlMat);
		double[][] afterviewMat 	= applyTransformationToAll(afterModlMat, viewMat);
		double[][] afterTrnsfrm 	= applyTransformationToAll(afterviewMat, projMat);
		
		MyTriangle[] afterPA	  	= primitiveAssemble(afterTrnsfrm, clr, nor);
		
		MyTriangle[] afterCulling 	= cull(afterPA);
		
		double[][] debug = applyTransformationToAll(afterModlMat, viewMat);
					 debug = applyTransformationToAll(debug, projMat);
		MyTriangle[] debugResult = primitiveAssemble(debug, clr, nor);
		
		//Quicksort.quickSort(tempArray, 0, tempArray.length-1, cameraPos);
		
		MyTriangle[] afterClipping = clip(afterCulling);
		
		MyTriangle[] afterModlMat 	= appl.applyTransformationToAllTriangles(TF.tetToTri(tet), modlMat);
		
		MyTriangle[] afterCulling 	= cull.cull(afterModlMat, nor, modlMat, cameraPos);
		
		MyTriangle[] afterviewMat 	= appl.applyTransformationToAllTriangles(afterCulling, viewMat);
		MyTriangle[] afterTrnsfrm 	= appl.applyTransformationToAllTriangles(afterviewMat, projMat);
		
		
		MyTriangle[] debug = appl.applyTransformationToAllTriangles(afterModlMat, viewMat);
					 debug = appl.applyTransformationToAllTriangles(debug, projMat);
		
		//Quicksort.quickSort(tempArray, 0, tempArray.length-1, cameraPos);
		
		MyTriangle[] afterClipping = clip.clip(afterTrnsfrm, cuttingEdge);
		*/
		
		
		Vertex[] afterModlMat 	= appl.applyTransformationToAllVertices(pts, modlMat);
		
		Vertex[] afterCulling 	= Culler.cull(afterModlMat, cameraPos);
		
		Vertex[] afterviewMat 	= appl.applyTransformationToAllVertices(afterCulling, viewMat);
		Vertex[] afterTrnsfrm 	= appl.applyTransformationToAllVertices(afterviewMat, projMat);
		
		//Vertex[] afterCulling 	= Culler.cull(afterTrnsfrm, cameraPos);
		
		MyTriangle[] afterPrim	= prim.assembleVertices(afterTrnsfrm);
		
		Vertex[] debug1 = appl.applyTransformationToAllVertices(afterModlMat, viewMat);
				 debug1 = appl.applyTransformationToAllVertices(debug1, projMat);
		MyTriangle[] debug	= prim.assembleVertices(debug1);
		
		MyTriangle[] afterClipping = clip.clip(afterPrim, cuttingEdge);	
		
		double[] cameraPosTest = new double[] {0, 1.5, -5};
		double[] lookAtPosTest = new double[] {0, 0,  0};
		double[][] viewMatTest = TF.createViewMat(cameraPosTest, lookAtPosTest, upVector);
		double[][] testMatrix = TF.matMult(TF.matMult(projMat, viewMatTest), TF.rotate(0, standRotation, 0));
		
		for(int i = 0; i < debug.length; i++) {
			debug[i].renderLine(g);
		}
		
		for(int i = 0; i < afterClipping.length; i++) {		
			afterClipping[i].renderLine(g);
			afterClipping[i].render(g);
		}	
		
		//DecimalFormat df = new DecimalFormat("###.###");
//		for(int i = 0; i < afterTrnsfrm.length; i++) {
//			System.out.println("(" + df.format(afterTrnsfrm[i].points[0].x) + " | " + df.format(afterTrnsfrm[i].points[0].y) + " | " + df.format(afterTrnsfrm[i].points[0].z) +")");
//			afterTrnsfrm[i].renderLine(g);
//		}	

		MyTriangle[] afterSecondTransformation = appl.applyTransformationToAllTriangles(afterClipping, testMatrix);
		
		for(int i = 0; i < afterSecondTransformation.length; i++) {		
			afterSecondTransformation[i].renderSpecial(g);
		}	
		
		g.setColor(Color.darkGray);
	
		double[] LeftDownNear  = new double[] { -cuttingEdge, -cuttingEdge, -cuttingEdge};
		double[] LeftDownFar   = new double[] { -cuttingEdge, -cuttingEdge, +cuttingEdge};
		double[] LeftUpNear    = new double[] { -cuttingEdge, +cuttingEdge, -cuttingEdge};
		double[] LeftUpFar     = new double[] { -cuttingEdge, +cuttingEdge, +cuttingEdge};
		double[] RightDownNear = new double[] { +cuttingEdge, -cuttingEdge, -cuttingEdge};
		double[] RightDownFar  = new double[] { +cuttingEdge, -cuttingEdge, +cuttingEdge};
		double[] RightUpNear   = new double[] { +cuttingEdge, +cuttingEdge, -cuttingEdge};
		double[] RightUpFar    = new double[] { +cuttingEdge, +cuttingEdge, +cuttingEdge};
		
		Vertex[] box = vert.assebleWithColor(new double[][] {
			LeftDownNear,
			LeftDownFar,
			LeftUpNear,
			LeftUpFar,
			RightDownNear,
			RightDownFar,
			RightUpNear,
			RightUpFar,
			new double[] {0, 0, -cuttingEdge}
		}, Color.GRAY);
		
		Vertex[] boxResult = new Vertex[9];
		
		for(int i = 0; i < box.length; i++) {
			 boxResult[i] = appl.applyTransformationVertices(box[i], testMatrix);
		}
		
		if(keyManager.keys[KeyEvent.VK_K]) {
			while(true) {
				System.out.print("");
				if(keyManager.keys[KeyEvent.VK_L]) {
					System.out.print("1 Frame");
					keyManager.keys[KeyEvent.VK_L] = false;
					break;
				}
			}
		}
		
		for(int i = 0; i < boxResult.length; i++) {
			boxResult[i].x = 1600 - (200 * boxResult[i].x);
			boxResult[i].y = 200  - (200 * boxResult[i].y);
		}
		
		g.drawLine((int) boxResult[0].x, (int) boxResult[0].y, (int) boxResult[4].x, (int) boxResult[4].y); //near  down  left to right
		g.drawLine((int) boxResult[2].x, (int) boxResult[2].y, (int) boxResult[6].x, (int) boxResult[6].y); //near  up    left to right
		g.drawLine((int) boxResult[2].x, (int) boxResult[2].y, (int) boxResult[0].x, (int) boxResult[0].y); //near  left  up   to down
		g.drawLine((int) boxResult[6].x, (int) boxResult[6].y, (int) boxResult[4].x, (int) boxResult[4].y); //near  rigth up   to down
		g.drawLine((int) boxResult[1].x, (int) boxResult[1].y, (int) boxResult[5].x, (int) boxResult[5].y); //far   down  left to right
		g.drawLine((int) boxResult[3].x, (int) boxResult[3].y, (int) boxResult[7].x, (int) boxResult[7].y); //far   up    left to right
		g.drawLine((int) boxResult[3].x, (int) boxResult[3].y, (int) boxResult[1].x, (int) boxResult[1].y); //far   left  up   to down
		g.drawLine((int) boxResult[7].x, (int) boxResult[7].y, (int) boxResult[5].x, (int) boxResult[5].y); //far   rigth up   to down
		g.drawLine((int) boxResult[6].x, (int) boxResult[6].y, (int) boxResult[7].x, (int) boxResult[7].y); //right up    near to far
		g.drawLine((int) boxResult[4].x, (int) boxResult[4].y, (int) boxResult[5].x, (int) boxResult[5].y); //right down  near to far
		g.drawLine((int) boxResult[2].x, (int) boxResult[2].y, (int) boxResult[3].x, (int) boxResult[3].y); //left  up    near to far
		g.drawLine((int) boxResult[0].x, (int) boxResult[0].y, (int) boxResult[1].x, (int) boxResult[1].y); //left  down  near to far
		
		g.setColor(Color.red);
		g.drawRect((int) boxResult[8].x, (int) boxResult[8].y, (int) 1, (int) 1);
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect((int) (width/2 - cuttingEdge*500 ), (int) (height/2 - cuttingEdge*500 ), (int) (2*cuttingEdge*500), (int) (2*cuttingEdge*500));
		
		showFPS(g);
		
		//End Drawing
		bs.show();
		g.dispose();
	}
	
	public void showNormal(MyTriangle[] input, int place) {
		//taking middlepoint of triangle
		double[] point = new double[4];
		point[0] = (input[place].points[0].x + input[place].points[1].x + input[place].points[2].x)/3;
		point[1] = (input[place].points[0].y + input[place].points[1].y + input[place].points[2].y)/3;
		point[2] = (input[place].points[0].z + input[place].points[1].z + input[place].points[2].z)/3;
		point[3] = 1;
		
		//transformation to 2D
		double[] pointAT = TF.matVecMult(rsltMat, point);

		//depth devision
		pointAT[0] = pointAT[0]/pointAT[3];
		pointAT[1] = pointAT[1]/pointAT[3];
				
		//adjust to screen
		pointAT[0] = width /2 + 500*-pointAT[0];
		pointAT[1] = height/2 + 500*-pointAT[1];
		
		//calculate normal position/vector
		double[] p0 = new double[] {input[place].points[0].x, input[place].points[0].y, input[place].points[0].z};
		double[] p1 = new double[] {input[place].points[1].x, input[place].points[1].y, input[place].points[1].z};
		double[] p2 = new double[] {input[place].points[2].x, input[place].points[2].y, input[place].points[2].z};
		
		double[] side1 = TF.subtract(p1, p0);
		double[] side2 = TF.subtract(p2, p0);
		
		double[] cross = TF.cross(TF.normalize(side1), TF.normalize(side2));
		double[] normalVec = new double[] {cross[0], cross[1], cross[2], 0};
		
		double[] v = new double[] {	cameraPos[0] - input[place].points[0].x, 
									cameraPos[1] - input[place].points[0].y, 
									cameraPos[2] - input[place].points[0].z, 0};
		
		cross = TF.vecMult(cross, 0.3);
		
		double[] point2 = new double[4];
		point2[0] = (input[place].points[0].x + input[place].points[1].x + input[place].points[2].x)/3 + cross[0];
		point2[1] = (input[place].points[0].y + input[place].points[1].y + input[place].points[2].y)/3 + cross[1];
		point2[2] = (input[place].points[0].z + input[place].points[1].z + input[place].points[2].z)/3 + cross[2];
		point2[3] = 1;
		
		//transformation to 2D
		double[] point2AT = TF.matVecMult(rsltMat, point2);

		//depth devision
		point2AT[0] = point2AT[0]/point2AT[3];
		point2AT[1] = point2AT[1]/point2AT[3];
						
		//adjust to screen
		point2AT[0] = width /2 + 500 * -point2AT[0];
		point2AT[1] = height/2 + 500 * -point2AT[1];
		
		g.setColor(Color.RED);
		if(TF.vecVecMult(TF.normalize(normalVec), (TF.normalize(v))) >= 0) g.drawLine((int) pointAT[0], (int) pointAT[1], (int) (point2AT[0]), (int) (point2AT[1]));
	}
	
	public void showFPS(Graphics g) {
		ts2 = System.currentTimeMillis();
		
		if(ticks % 50 == 0) {
			resultFPS = (int) (50000/(ts2-ts));
			ts = System.currentTimeMillis();
		}
		ticks++;
		
		g.setFont(new Font("Arial", Font.PLAIN, 15)); 
		g.setColor(Color.WHITE);
		byte[] data = (resultFPS + " fps").getBytes();
	    g.drawBytes(data, 0, data.length, width-60, 20);
	}
	
	public void updateRsltMat() {
		rsltMat = TF.matMult(TF.matMult(projMat, viewMat), modlMat);
	}
	
	public void updateViewMat() {
		viewMat = TF.createViewMat(cameraPos, lookAtPos, upVector);
		updateRsltMat();
	}
	
	public void initMatrices() {
		cameraPos = new double[] {0, 0, -cuttingEdge};
		lookAtPos = new double[] {0, 0,  0};
		upVector  = new double[] {0, 1,  0};
		projMat = TF.perspective(fov, (double) (width/height), near, far);
		modlMat = TF.mat4();
	
		updateViewMat();
	}
	
	public void defineShapesTeaPod() { 
		
		Scanner s;
		try {
			s = new Scanner(new File("res/obj.txt"));
			
			int maxVertices = 3500;
			
			String row1;
			String row2;
			String row3;			
			
			ArrayList<Vertex> output = new ArrayList<Vertex>();
			
			for (int i = 3; i < maxVertices; i += 3) {
				
				try {
					row1 = s.nextLine();
					row2 = s.nextLine();
					row3 = s.nextLine();
				} catch (Exception e) {
					System.out.println(e);
					break;
				}
				
				if(row1 == "" || row2 == "" || row3 == "") break;
	
				Vertex[] temp = convert(row1, row2, row3);
				
				output.add(temp[0]);
				output.add(temp[1]);
				output.add(temp[2]);
				
				if(i == maxVertices-1) System.out.println("too many Vertices");
			}
			pts = new Vertex[output.size()];
			for(int i = 0; i < output.size(); i++) {
				pts[i] = output.get(i);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}
	
	public Vertex[] convert (String input1, String input2, String input3) {
		String[] temp1 = input1.split(" ", 3);
		String[] temp2 = input2.split(" ", 3);
		String[] temp3 = input3.split(" ", 3);

		//System.out.println("HIER:" + temp1[0] + " " + temp1[1] + " " + temp1[2]);
		
		double[] t1 = new double[] {Double.parseDouble(temp1[0]), Double.parseDouble(temp1[1]), Double.parseDouble(temp1[2])};
		double[] t2 = new double[] {Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]), Double.parseDouble(temp2[2])};
		double[] t3 = new double[] {Double.parseDouble(temp3[0]), Double.parseDouble(temp3[1]), Double.parseDouble(temp3[2])};
		
		//return new MyTriangle(this, new MyPoint[] {new MyPoint(a1, a2, a3), new MyPoint(b1, b2, b3), new MyPoint(c1, c2, c3)});
		return vert.asseble(new double[][] {t1, t2, t3});
	}
	
	public void defineShapes() {
		
		double[] A = new double[] { -0.5,  0.0,   0.5, 1};
		double[] B = new double[] {  0.5,  0.0,   0.5, 1};
		double[] C = new double[] {  0.5,  0.0,  -0.5, 1};
		double[] D = new double[] { -0.5,  0.0,  -0.5, 1};
		
		double[] E = new double[] { -0.25,  0.25,   0.25, 1};
		double[] F = new double[] {  0.25,  0.25,   0.25, 1};
		double[] G = new double[] {  0.25,  0.25,  -0.25, 1};
		double[] H = new double[] { -0.25,  0.25,  -0.25, 1};
		
		pts = vert.asseble(new double[][] {
			D, C, B,
			B, A, D,
			
			B, F, A,
			A, F, E,
			
			A, E, D,
			D, E, H,
			
			D, H, C,
			C, H, G,
			
			C, G, B,
			B, G, F,
			
			F, G, H,
			H, E, F
		});
	}
	
	public void run() {
		init();
		
		double next_game_tick = System.currentTimeMillis();
	    int loops;
	    
		while (running) {
	        loops = 0;
	        while (System.currentTimeMillis() > next_game_tick && loops < fps) {

	        	tick();
				render();

	            next_game_tick += SKIP_TICKS;
	            loops++;
	        }
	    }
		stop();
	}
	
	public synchronized void start() {
		if(running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(!running)
			return;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}