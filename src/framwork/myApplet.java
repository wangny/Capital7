package framwork;

import java.util.ArrayList;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class myApplet extends PApplet{
	//ArrayList<Cube> cubes;
	
	Plate currentp;
	ProgressBar pbar;
	public final static int width = 800, height = 800 , cubewidth = 50, cubeheight = 60;
	
	public void setup(){
		//cubes = new ArrayList<Cube>();
		size(width, height);
		background(240);
		smooth();
		
		pbar = new ProgressBar(this);
		Thread t = new Thread(pbar);
		t.start();
		
		
		
		currentp = new Plate(this);
		Thread t2 = new Thread(currentp);
		t2.start();
		//addCube();
		
	}
	
	public void draw(){
		
		fill(255);
		rect(50, 50, width-100, height-100, 15);
		
		pbar.display();
		currentp.display();
		
		/*for(Cube c : currentp.cubes){
			c.display();
		}*/
	}
	
	
	
	/*public void addCube(){		
		for(Cube c : cubes) c.grow();

		int x = 100;
		for(int i=0; i<10; i++){
			Cube c = new Cube(this, 3, "try", x, height-150);
			cubes.add(c);
			x = x + cubewidth + 10;
		}
	}*/
	
	
}
