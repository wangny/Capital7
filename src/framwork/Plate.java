package framwork;

import java.util.ArrayList;
import java.util.Random;
import de.looksgood.ani.Ani;

public class Plate implements Runnable{
	private ArrayList<Cube> cubes;
	public ArrayList<Cube> cubeDB;
	private myApplet parent;
	private ProgressBar pbar;
	private Cube dragCube;
	private Random random;
	private boolean GameOver;
	private final static int inix=100, iniy=myApplet.height-150, finalx=myApplet.width-160 ,finaly=50;
	
	public Plate(myApplet applet){
		this.parent = applet;
		cubes = new ArrayList<Cube>();
		cubeDB = new ArrayList<Cube>();
		dragCube = new Cube();
		random = new Random();
		
		pbar = new ProgressBar(parent);
	
		GameOver = false;
	}
	
	public void display(){	
		pbar.display();
		for(int i=0; i<cubes.size(); i++){
			cubes.get(i).display();
		}
	}

	
	public void addCube(){		
		System.out.println("add cube");
		for(Cube c : cubes){
			if(c!=dragCube){
				int tmp = c.getY();
				if(tmp-myApplet.cubeheight-10 < finaly ) GameOver=true;
				else Ani.to(c, (float)0.5, "y", tmp-myApplet.cubeheight-10, Ani.LINEAR);
			}
		}
		
		int x = 0;
		for(int i=0; i<10; i++){
			Cube c = new Cube(parent, 0, cubeDB.get(random.nextInt(cubeDB.size())), inix+x, myApplet.height-150);
			cubes.add(c);
			x = x + myApplet.cubewidth + 10;
		}
	}

	public void mousePressed(){
		//System.out.println("mouse pressed");
		for (Cube c: cubes){
			if ( (parent.mouseX>c.getX()&&parent.mouseX<c.getX()+myApplet.cubewidth) && (parent.mouseY>c.getY()&&parent.mouseY<c.getY()+myApplet.cubeheight)){
				dragCube = c;
			}
		}
	}
	
	public void mouseDragged(){
		dragCube.setDrag(true);
		
		boolean hitx = false, hity=false;
		int diffx = parent.mouseX-parent.pmouseX;
		int diffy = parent.mouseY-parent.pmouseY;
		
		for (int i = 0; i < cubes.size(); i++){
			Cube c = cubes.get(i);
			if (c!=dragCube){
				//out of boundary
				if(dragCube.getX()+diffx>=finalx || dragCube.getX()+diffx<=inix ) hitx = true;
				if(dragCube.getY()+diffy>=iniy+10 || dragCube.getY()+diffy<=finaly) hity = true;
				//judge if hit the match cube
				if(Math.abs(dragCube.getX()+diffx-c.getX())<myApplet.cubewidth && Math.abs(dragCube.getY()+diffy-c.getY())<myApplet.cubeheight){
					//judge if match
					if(c.getTarget().equals(dragCube.getName())){/// is match, do merging animation
						System.out.println("match");
						merge(c, dragCube);
						cubes.remove(dragCube);
						dragCube = new Cube();	//important! not to re-judge
					}else{	///else don't move
						if(Math.abs(dragCube.getX()+diffx-c.getX())<myApplet.cubewidth) hitx = true;
						if(Math.abs(dragCube.getY()+diffy-c.getY())<myApplet.cubeheight) hity = true;
					}
				}
			}
		}
		
		
		if(!hitx)dragCube.addX(diffx);
		if(!hity)dragCube.addY(diffy);
		
	}
	
	public void merge(Cube a, Cube b){ ///will always remove b
		//judge which to remain/remove
		if (a.getState()>b.getState()){
			//a is lighter
			System.out.println(a.getState());
			a.setState(a.getState()+1);
			System.out.println(a.getState());
			cubes.remove(b);
		} else {
			//b is lighter
			System.out.println(a.getState());
			a.setState(b.getState()+1);
			System.out.println(a.getState());
			cubes.remove(b);
		}
		
		if(a.getState()>=7) cubes.remove(a);
		
	}
	
	public void mouseReleased(){
		//set dragCube in position
		
		///adjust to right x position
		int tmp = (dragCube.getX()-inix)/(myApplet.cubewidth+10);
		if( (dragCube.getX()-inix)%(myApplet.cubewidth+10) > ((myApplet.cubewidth+10))/2 ) dragCube.setX(inix+(tmp+1)*(myApplet.cubewidth+10) );
		else if(  (dragCube.getX()-inix)%(myApplet.cubewidth+10) <= ((myApplet.cubewidth+10))/2 ) dragCube.setX(inix+tmp*(myApplet.cubewidth+10) );
		
		///adjust to right y position (for all cubes)
		int higest = myApplet.height-80;
		for (Cube c: cubes) if(c.getX()==dragCube.getX() && c.getY()<higest && c!=dragCube && c.getY()>dragCube.getY()) higest = c.getY();
		higest = higest - 10 - myApplet.cubeheight;
		Ani.to(dragCube, (float)0.3, "y", higest, Ani.LINEAR);
		
		dragCube.setDrag(false);
		dragCube = new Cube();
	}
	
	
	public void reset(){	///for restarting the game
		cubes.clear();
		dragCube = new Cube();
		random = new Random();
		pbar = new ProgressBar(parent);
	
		GameOver = false;
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		addCube();
		Thread t = new Thread(pbar);	///progressBar start counting
		t.start();
		
		while(true){
			
			for (int i = 0; i < cubes.size(); i++){		///cubes will always been dragged to the lowest position they can
				Cube ch = cubes.get(i);
				int higest = myApplet.height-80;
				for (Cube c: cubes) if( ((c.getX()<=ch.getX() && c.getX()+myApplet.cubewidth>=ch.getX())|| (c.getX()<=ch.getX()+myApplet.cubewidth && c.getX()+myApplet.cubewidth>=ch.getX()+myApplet.cubewidth)) 
										&& c.getY()<higest && c!=ch && c.getY()>ch.getY()) higest = c.getY();
				higest = higest - 10 - myApplet.cubeheight;
				if(!ch.isDragged() && ch.getY()<higest)ch.setY(ch.getY()+1);
			}
			
			if(pbar.isdone()==true) {	///add a new line of cubes if progressBar achieve it's goal
				addCube();
				pbar.undone();
			}
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(GameOver){
				t.interrupt();	///stop progressBar
				break;	
			}
			
		}
		parent.returnMenu(); ///call reply and home button
	}
}
