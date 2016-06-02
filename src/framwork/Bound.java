package framwork;

import java.util.Random;
import processing.core.PImage;

public class Bound {
	myApplet parent;
	int height;
	int width;
	int breakpoint;
	int x,y;
	PImage image, b_point;
	
	public Bound(myApplet applet, int x, int y ){
		parent = applet;
		this.x = x;
		this.y = y;
		height = 70;
		width = 700;
		breakpoint = new Random().nextInt(10);
		image = parent.loadImage("wall"+ ((Integer)(new Random().nextInt(4)+1)).toString() + ".jpg");
		b_point = parent.loadImage("bling.png");
	}
	
	public void display(){
		parent.image(image, x, y, width, height);
		parent.image(b_point, x+breakpoint*70, y, 50, 50);
	}
	
	
	
}
