package framwork;

import java.util.ArrayList;

import de.looksgood.ani.Ani;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class myApplet extends PApplet{
	
	Plate currentp;
	ProgressBar pbar;
	Ani ani;
	public final static int width = 800, height = 800 , cubewidth = 50, cubeheight = 60;
	
	public void setup(){
		//cubes = new ArrayList<Cube>();
		size(width, height);
		background(240);
		smooth();
		
		Ani.init(this);
		
		pbar = new ProgressBar(this);
		Thread t = new Thread(pbar);
		t.start();
				
		currentp = new Plate(this);
		Thread t2 = new Thread(currentp);
		t2.start();
		
	}
	
	public void draw(){
		
		fill(255);
		rect(50, 50, width-100, height-100, 15);
		
		pbar.display();
		currentp.display();
		
	}
	

	
}
