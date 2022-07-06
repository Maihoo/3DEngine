package api;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Display{
	private JFrame frame;
	private Canvas canvas;
	
	private String title;
	private int width, height;
	public double xRot, yRot, zRot;
	
	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		
		createDisplay();
	}
	
	public Point getMP() {
		return frame.getMousePosition();	
	}
	
	public Point getSP() {
		return frame.getLocationOnScreen();
	}
	
	public void close() {
		frame.dispose();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createDisplay() {
		// Create and set up a frame window
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setPreferredSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
		frame.setLayout(new BorderLayout());
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
 
		JLayeredPane mainPanel = new JLayeredPane();
		mainPanel.setBounds(0, 0, width, height);
		
		Hashtable lables = new Hashtable();
		lables.put(-20, new JLabel("-2"));
		lables.put(-10, new JLabel("-1"));
		lables.put( 0,  new JLabel("0"));
		lables.put( 10, new JLabel("1"));
		lables.put( 20, new JLabel("2"));
		
		JSlider xRotation = createSlider(lables);	
		xRotation.setName("x");
		JSlider yRotation = createSlider(lables);	
		yRotation.setName("y");
		JSlider zRotation = createSlider(lables);	
		zRotation.setName("z");
		
		xRotation.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e1) {
                xRot = ((double) ((JSlider)e1.getSource()).getValue()) / 10;
            }
        });
		
		yRotation.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e2) {
                yRot = ((double) ((JSlider)e2.getSource()).getValue()) / 10;
            }
        });
		
		zRotation.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e3) {
                zRot = ((double) ((JSlider)e3.getSource()).getValue()) / 10;
            }
        });
		         
		// Add the slider to the panel
		JPanel sliderPanel = new JPanel();
		//sliderPanel.setLayout(new BorderLayout());
		sliderPanel.add(new JLabel("X-Rotation"));
        sliderPanel.add(xRotation);
        sliderPanel.add(new JLabel("Y-Rotation"));
        sliderPanel.add(yRotation);
        sliderPanel.add(new JLabel("Z-Rotation"));
        sliderPanel.add(zRotation);
        sliderPanel.setBounds(0, 0, 300, 150);
        sliderPanel.setOpaque(true);
		
        JPanel canvasPanel = new JPanel();
        canvasPanel.add(canvas);
        canvasPanel.setBounds(0, 0, width, height);
        canvasPanel.setOpaque(true);
        
        mainPanel.add(canvasPanel, new Integer(0), 0);
        mainPanel.add(sliderPanel, new Integer(1), 0);
		         
		frame.getContentPane().add(mainPanel);
        //frame.add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}	
	
	@SuppressWarnings("rawtypes")
	public JSlider createSlider(Hashtable lables) {
		JSlider output = new JSlider(-20, 20, 0);	

		output.setMajorTickSpacing(10);
		output.setMinorTickSpacing(1);
		output.setPaintTicks(true);
		output.setPaintLabels(true);
		output.setFocusable(false);
		
		output.setLabelTable(lables);
		
		return output;
	}
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public JFrame getFrame() {
		return frame;
	}	
}
