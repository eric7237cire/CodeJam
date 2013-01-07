package codejam.y2012.round_2.aerobics;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Circle;

public class Display extends Frame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4340527108758519768L;


	final static Logger log = LoggerFactory.getLogger(Display.class);


	String keymsg = "";
	String mousemsg = "";
	int mouseX = 30, mouseY = 30;
	
	double minX = Double.MAX_VALUE;
	double maxX = Double.MIN_VALUE;
	double minY = Double.MAX_VALUE;
	double maxY = Double.MIN_VALUE;
	
	final int minDisplayX;
	final int maxDisplayX;
	final int minDisplayY;
	final int maxDisplayY;
	
	List<Circle> circles = new ArrayList<>();

	public Display(int width, int height) {
		setBounds(0, 0, width, height);
		
		final int marge = 25;
		minDisplayX = marge;
		maxDisplayX = width - marge;
		minDisplayY = 2*marge;
		maxDisplayY = height - marge;
		
		addKeyListener(new MyKeyAdapter(this));
		addMouseListener(new MyMouseAdapter(this));
		addWindowListener(new MyWindowAdapter());
	}

	public void addCircle(Circle c) {
		minX = Math.min(c.getX() - c.getR(),  minX);
		maxX = Math.max(c.getX() + c.getR(),  maxX);
		minY = Math.min(c.getY() - c.getR(),  minY);
		maxY = Math.max(c.getY() + c.getR(),  maxY);
		this.circles.add(c);
	}
	
	private double translateCoord(double coord, double coordMin, double coordMax, double targetCoordMin, double targetCoordMax) {
		double perc = (coord - coordMin) / (coordMax - coordMin);
		double r = targetCoordMin + perc * (targetCoordMax - targetCoordMin);
		return  r;
	}
	
	
	private void drawCircle(Circle c, Graphics g) {
		
		int x = (int) translateCoord(c.getX(),minX,maxX,minDisplayX, maxDisplayX);
		int y = (int) translateCoord(c.getY(),minY,maxY,minDisplayY, maxDisplayY);
		int width = (int)  (2*c.getR() / (maxX - minX) * (maxDisplayX - minDisplayX));
		int height = (int)  (2*c.getR() / (maxY - minY) * (maxDisplayY - minDisplayY));
		log.debug("DrawCircle {} ({}, {}) width {} height {} ", c, x, y, width, height);
		
		this.getGraphics().drawOval((int) (x  - width/2),
				(int) (y -height / 2), width, height);
	}
	
	public void paint(Graphics g) {
		g.drawString(keymsg, 10, 40);
		g.drawString(mousemsg, mouseX, mouseY);
		//g.drawOval(mouseX, mouseY, 50, 50);
		
		int i = 0;
		for(Circle c : circles) {
			++i;
			if (i==1) {
				g.setColor(Color.BLUE);
			} else if (i==2) {
				g.setColor(Color.GREEN);
			} else if (i==3) {
				g.setColor(Color.RED);
			} else if (i==4) {
				g.setColor(Color.BLACK);
			}
			g.drawString("c" + i, (int)c.getX()*50+100, (int)c.getY()*50+100);
			drawCircle(c, g);
		}
		
	}

	class MyKeyAdapter extends KeyAdapter {
		Display appWindow;

		public MyKeyAdapter(Display appWindow) {
			this.appWindow = appWindow;
		}

		public void keyTyped(KeyEvent ke) {
			appWindow.keymsg += ke.getKeyChar();
			appWindow.repaint();
		};
	}

	class MyMouseAdapter extends MouseAdapter {
		Display appWindow;

		public MyMouseAdapter(Display appWindow) {
			this.appWindow = appWindow;
		}

		public void mousePressed(MouseEvent me) {
			appWindow.mouseX = me.getX();
			appWindow.mouseY = me.getY();
			appWindow.mousemsg = "Mouse Down at " +
			translateCoord(appWindow.mouseX, minDisplayX, 
					maxDisplayX, minX, maxX) + ", "
					+ translateCoord(appWindow.mouseY, minDisplayY, 
							maxDisplayY,  minY,  maxY);
			appWindow.repaint();
		}
	}

	class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
}
