package framwork;

import java.util.ArrayList;
import java.util.Comparator;
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
	private final static int inix=130, iniy=myApplet.height-150, finalx=inix+540 ,finaly=180;
	
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
		
		for(int i=0; i<cubes.size(); i++){
			cubes.get(i).display();
		}
		parent.image(parent.img_play,0,0);
		pbar.display();
		parent.stroke(200);
		parent.strokeWeight(5);
		parent.noFill();
		parent.rect(80, 150, myApplet.width-260, myApplet.height-220, 15);//(50, 20, myApplet.width-100, myApplet.height-80, 15);
		parent.noStroke();
	}

	
	public void addCube(){		
		System.out.println("add cube");
		for(Cube c : cubes){
			if(c!=dragCube){
				int tmp = c.getY();
				if(tmp-myApplet.cubeheight-10 < finaly) GameOver=true;
				
				//if(GameOver) Ani.to(c, (float)0.3, "y", tmp-40, Ani.LINEAR);
				else Ani.to(c, (float)0.5, "y", tmp-myApplet.cubeheight-10, Ani.LINEAR);
			}
		}
		
		int x = 0;
		for(int i=0; i<10; i++){
			random.setSeed(random.nextLong());
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
				dragCube.setDrag(true);
			}
		}
	}
	
	public void mouseDragged(){
		
		
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
			a.setState(a.getState()+b.getState()+1);
			System.out.println(a.getState());
			cubes.remove(b);
		} else {
			//b is lighter
			System.out.println(a.getState());
			a.setState(b.getState()+b.getState()+1);
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
	
	public void sortCubes(){
		//sort cubes:
		cubes.sort(new Comparator<Cube>(){
			@Override
			public int compare(Cube c1, Cube c2) {
				if (c1.getY()<c2.getY())
					return 1;
				else if (c1.getY()==c2.getY()){
					if (c1.getX()>c2.getX())
						return 0;
					else return -1;
				}
				else
					return -1;
			}
		});
		/*for (Cube c : cubes){
			System.out.println(c.getX()+", "+c.getY()+", "+c.getName());
		}
		System.out.println("-----------------");*/
		//judge if can merge with the lower cube
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		addCube();
		Thread t = new Thread(pbar);	///progressBar start counting
		t.start();
		
		
		while(true){
			
			System.out.print("");
			if(parent.gamePhase==5) continue;
			
			for (int i = 0; i < cubes.size(); i++){		///cubes will always been dragged to the lowest position they can
				Cube ch = cubes.get(i);
				
				Cube highest=null;
				for (Cube c: cubes) if( ((c.getX()<=ch.getX() && c.getX()+myApplet.cubewidth>ch.getX())|| (c.getX()<ch.getX()+myApplet.cubewidth && c.getX()+myApplet.cubewidth>ch.getX()+myApplet.cubewidth)) 
						&& (highest==null||c.getY()<highest.getY()) && c!=ch && c.getY()>=ch.getY() ) highest = c;
				
				int high ;
				if(highest==null) high = myApplet.height-80;
				else high= highest.getY();
				high = high - 10 - myApplet.cubeheight;
				if(!ch.isDragged() && ch.getY()<high)ch.setY(ch.getY()+1);
				else if(ch.getY()>high && ch==dragCube )ch.setY(ch.getY()-1);
				
				
				if(ch.getY()>=high && highest!=null && ch.getX()==highest.getX()) if(ch.getTarget().equals(highest.getName())) merge(highest,ch);
				
				
				if(ch.getY()<finaly) GameOver=true;
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
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pbar.stop();	///stop progressBar
				break;	
			}
			
		}
		parent.returnMenu(); ///call replay and home button
		parent.cp5.getController("Replay").setVisible(true);
		parent.cp5.getController("Home").setVisible(true);
	}
}
