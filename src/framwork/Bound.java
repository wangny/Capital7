package framwork;

import java.util.Random;
import processing.core.PImage;

public class Bound {
	myApplet parent;
	int height;
	int width;
	int breakpoint;
	int x,y;
	PImage image;
	
	public Bound(myApplet applet, int x, int y ){
		parent = applet;
		this.x = x;
		this.y = y;
		height = 60;
		width = 600;
		breakpoint = new Random().nextInt(10);
		image = parent.loadImage("wall"+ ((Integer)(new Random().nextInt(4))).toString() + ".jpg");
	}
	
	public void display(){
		parent.image(image, x, y, width, height);
	}
	
	
	
}
