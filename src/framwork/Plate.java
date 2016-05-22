package framwork;

import java.util.ArrayList;
import java.awt.event.KeyEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import de.looksgood.ani.Ani;

public class Plate implements Runnable{
	private ArrayList<Cube> cubes;
	private myApplet parent;
	private ProgressBar pbar;
	private Cube dragCube;
	
	public Plate(myApplet applet){
		this.parent = applet;
		cubes = new ArrayList<Cube>();
		
		pbar = new ProgressBar(parent);
		Thread t = new Thread(pbar);
		t.start();
	}
	
	public void display(){	
		pbar.display();
		for(Cube c : cubes){
			c.display();
		}
	}

	
	public void addCube(){		
		//for(Cube c : cubes) c.grow();
		for(Cube c : cubes){
			int tmp = c.getY();
			Ani.to(c, (float)0.5, "y", tmp-70, Ani.LINEAR);
		}
		
		int x = 100;
		for(int i=0; i<10; i++){
			Cube c = new Cube(parent, 3, "try", x, myApplet.height-150);
			cubes.add(c);
			x = x + myApplet.cubewidth + 10;
		}
	}
	
	public void mousePressed(){
		System.out.println("mouse pressed");
		for (Cube c: cubes){
			if ( (parent.mouseX>c.getX()&&parent.mouseX<c.getX()+myApplet.cubewidth) && (parent.mouseY>c.getY()&&parent.mouseY<c.getY()+myApplet.cubeheight)){
				dragCube = c;
			}
		}
	}
	
	public void mouseDragged(){
		dragCube.setDarg(true);
		dragCube.setX(parent.mouseX-parent.pmouseX);
		dragCube.setY(parent.mouseY-parent.pmouseY);
		//
		for (Cube c: cubes){
			if (c!=dragCube){
				//judge if hit
				if (dragCube.getX()+myApplet.cubewidth>c.getX()){	//left hit right
					//judge if match
				}
				else if (dragCube.getX()<c.getX()+myApplet.cubewidth){	//right hit left
					//judge if match
				}
				else if (dragCube.getY()+myApplet.cubeheight>c.getY()){	//up hit down
					//judge if match
				}
				else if (dragCube.getY()<c.getY()+myApplet.cubeheight){	//down hit up (impossible?!)
					//judge if match
				}
			}
		}
	}
	
	public void mouseReleased(){
		//set dragCube in position
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(pbar.isdone()==true) {
				addCube();
				pbar.undone();
			}
		}
	}
}
