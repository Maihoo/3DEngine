package api;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener {

	public boolean[] keys;
	public boolean leftPressed, rightPressed;
	private int mouseX, mouseY;
	
	public MouseManager(){
		keys = new boolean[256];
	}

	public void tick() {
		leftPressed  = keys[MouseEvent.BUTTON1];
		rightPressed = keys[MouseEvent.BUTTON3];
	}
	
	public boolean isLeftPressed(){
		return leftPressed;
	}
	
	public boolean isRightPressed(){
		return rightPressed;
	}
	
	public int getMouseX(){
		return mouseX;
	}
	
	public int getMouseY(){
		return mouseY;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		keys[e.getButton()] = true;
		
		
		/*
		if(e.getButton() == MouseEvent.BUTTON1)
			leftPressed = true;
		else if(e.getButton() == MouseEvent.BUTTON3)
			rightPressed = true;
		*/
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		keys[e.getButton()] = false;
		
		/*
		if(e.getButton() == MouseEvent.BUTTON1)
			leftPressed = false;
		else if(e.getButton() == MouseEvent.BUTTON3)
			rightPressed = false;
		*/
	}
	
	public void reset() {
		keys[MouseEvent.BUTTON1] = false;
		keys[MouseEvent.BUTTON3] = false;
		leftPressed  = false;
		rightPressed = false;

	}

	public void mouseMoved(MouseEvent e) {
	}
	
	public void mouseDragged(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {		
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}