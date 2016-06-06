package framwork;

import processing.core.PImage;

public class HowToPlay {
	
	private myApplet parent;
	public int x;
	public int y;
	private PImage explan;
	
	public HowToPlay(myApplet applet){
		this.parent = applet;
		this.x = 30;
		this.y = myApplet.height+10;
		explan = parent.loadImage("explan.png");
	}
	
	public void display(){
		this.parent.image(explan, x, y);
	}
}
