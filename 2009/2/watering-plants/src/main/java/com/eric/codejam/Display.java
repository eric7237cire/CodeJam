package com.eric.codejam;

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

public class Display extends Frame {

	String keymsg = "";
	String mousemsg = "";
	int mouseX = 30, mouseY = 30;
	
	List<Circle> circles = new ArrayList<>();

	public Display() {
		addKeyListener(new MyKeyAdapter(this));
		addMouseListener(new MyMouseAdapter(this));
		addWindowListener(new MyWindowAdapter());
	}

	public void addCircle(Circle c) {
		this.circles.add(c);
	}
	private void drawCircle(Circle c, Graphics g) {
		double blowUpFactor = 3.0;
		int transX = 200;
		int transY = 200;
		int r = (int) (2 * c.getR()*blowUpFactor);
		this.getGraphics().drawOval((int) (c.getX()*blowUpFactor) + transX - r/2,
				(int) (c.getY()*blowUpFactor) + transY - r/2, r, r);
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
			appWindow.mousemsg = "Mouse Down at " + appWindow.mouseX + ", "
					+ appWindow.mouseY;
			appWindow.repaint();
		}
	}

	class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
}
