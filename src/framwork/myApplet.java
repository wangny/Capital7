package framwork;

import de.looksgood.ani.Ani;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class myApplet extends PApplet{
	
	Plate currentp;
	Ani ani;
	public final static int width = 800, height = 700 , cubewidth = 50, cubeheight = 60;
	
	public void setup(){

		size(width, height);
		background(240);
		smooth();
		
		Ani.init(this);
		
		currentp = new Plate(this);
		Thread t2 = new Thread(currentp);
		t2.start();
		
	}
	
	public void draw(){
		//plate background and frame
		fill(255);
		rect(50, 20, width-100, height-80, 15);
		
		currentp.display();
	}
	
	public void mousePressed(){
		System.out.println("press press");
		currentp.mousePressed();
	}
	
	public void mouseDragged(){
		currentp.mouseDragged();
	}
	
	public void mouseReleased(){
		currentp.mouseReleased();
	}

	
}
